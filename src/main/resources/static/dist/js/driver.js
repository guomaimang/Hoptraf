let driverId = getQueryParam("driverId");

$(function () {

    //隐藏错误提示框
    $('.alert-danger').css("display", "none");

    $("#jqGrid").jqGrid({
        // 设置API
        url: '/api/eventreport/list?driverId=' + driverId,
        datatype: "json",
        colModel: [
            // 设置列表表头
            {label: 'Event ID', name: 'id', index: 'id', width: 15, key: true, hidden: false},
            {label: 'Driver ID', name: 'driverId', index: 'driverId', width: 20},
            {label: 'Card Plate Num', name: 'carPlateNumber', index: 'carPlateNumber', width: 30},
            {label: 'Behavior', name: 'behavior', index: 'behavior', width: 60},
            {label: 'Report Time', name: 'reportTime', index: 'reportTime', width: 30, editable: true, formatter: utcToLocalFormatter},
        ],
        height: 560,
        rowNum: 10,
        rowList: [10, 20, 50],
        styleUI: 'Bootstrap',
        loadtext: 'Information reading in progress...',
        rownumbers: false,
        rownumWidth: 20,
        autowidth: true,
        multiselect: false,
        pager: "#jqGridPager",
        jsonReader: {
            root: "data.rows",
            records: "data.count",
            page: "data.currentPage",
            total: "data.totalPage",
        },
        prmNames: {
            page: "pageNum",
            rows: "pageSize",
            order: "order",
        },
        gridComplete: function () {
            //隐藏grid底部滚动条
            $("#jqGrid").closest(".ui-jqgrid-bdiv").css({"overflow-x": "hidden"});
        },
    });

    $("#searchButton").click(function(){
        let keyword = $("#searchInput").val(); //获取输入框的值
        $("#jqGrid").jqGrid('setGridParam',{
            postData: {'keyword': keyword}, //设置postData参数
            page: 1
        }).trigger("reloadGrid"); //重新加载JqGrid
    });

    $(window).resize(function () {
        $("#jqGrid").setGridWidth($(".card-body").width() * 2);

    });


});

// reload the content
function reload() {
    let page = $("#jqGrid").jqGrid('getGridParam', 'page');
    $("#jqGrid").jqGrid('setGridParam', {
        page: page
    }).trigger("reloadGrid");

    updateTimeText();
    contentsPreparation();
}

// prepare the page
function contentsPreparation(){

    //请求基本数据
    $.ajax({
        url: "/api/driver/info",
        type: "GET",
        data: {
            driverId: driverId
        },
        success: function(r) {
            if (r.code === 0 && r.data != null) {
                document.getElementById("driverID").innerText = r.data.driverID;
                document.getElementById("carPlateNumber").innerText = r.data.carPlateNumber;
                document.getElementById("updateTime").innerText = utcToLocalFormatter(r.data.updateTime);
                document.getElementById("latitude").innerText = r.data.latitude;
                document.getElementById("longitude").innerText = r.data.longitude;
                document.getElementById("speed").innerText = kmhFormatter(r.data.speed);
                document.getElementById("direction").innerText = r.data.direction;

                document.getElementById("isRapidlySpeedup").innerText = statusFormatter(r.data.isRapidlySpeedup);
                document.getElementById("isRapidlySlowdown").innerText = statusFormatter(r.data.isRapidlySlowdown);
                document.getElementById("isNeutralSlide").innerText = statusFormatter(r.data.isNeutralSlide);
                document.getElementById("isOverspeed").innerText = statusFormatter(r.data.isOverspeed);
                document.getElementById("isFatigueDriving").innerText = statusFormatter(r.data.isFatigueDriving);
                document.getElementById("isHthrottleStop").innerText = statusFormatter(r.data.isHthrottleStop);
                document.getElementById("isOilLeak").innerText = statusFormatter(r.data.isOilLeak);

                document.getElementById("rapidlySpeedupTimes").innerText = r.data.rapidlySpeedupTimes;
                document.getElementById("rapidlySlowdownTimes").innerText = r.data.rapidlySlowdownTimes;
                document.getElementById("neutralSlideTimes").innerText = r.data.neutralSlideTimes;
                document.getElementById("overspeedTimes").innerText = r.data.overspeedTimes;
                document.getElementById("fatigueDrivingTimes").innerText = r.data.fatigueDrivingTimes;
                document.getElementById("hthrottleStopTimes").innerText = r.data.hthrottleStopTimes;
                document.getElementById("oilLeakTimes").innerText = r.data.oilLeakTimes;

                // remind the driver in over speed
                if(r.data.isOverspeed == 1){
                    swal("Driver is Overspeeding!!!", {
                        icon: "warning",
                    });
                }

            }else {
                swal(r.msg, {
                    icon: "error",
                });
            }
        },
        error: function(jqXHR, textStatus, errorThrown) {
            // handle error
            console.error("AJAX Error: ", textStatus, errorThrown);
        }
    });

    // 请求chart
    let speedChart =  echarts.init(document.getElementById('speedChart'));
    let speedOption = {
        title: {
            text: 'Avergae Speed'
        },
        xAxis: {
            data: [],
            name: 'Time'
        },
        yAxis: {
            name: 'Speed (km/h)'
        },
        series: [
            {
                data: [],
                type: 'line',
                smooth: true
            }
        ]
    };
    speedChart.setOption(speedOption);
    speedChart.showLoading({
        text: 'Loading...',
        textStyle: {
            fontSize: 20
        },
        effectOption: {
            backgroundColor: '#fff'
        }
    });

    window.onresize = function() {
        speedChart.resize();
    }

    $.ajax({
        url: "/api/driver/diagram",
        type: "GET",
        data: {
            driverId: driverId
        },
        success: function(response) {
            // 处理数据
            let times = [];
            let speeds = [];
            for (let key in response.data) {
                times.push(key.split('T')[1].split(':').slice(0,2).join(':')); // 提取时间并去掉年份
                speeds.push(response.data[key]);
            }

            // 更新图表
            speedOption.xAxis.data = times;
            speedOption.series[0].data = speeds;
            speedChart.setOption(speedOption);
        },
        error: function(error) {
            console.log(error);
        },
        complete: function() {
            speedChart.hideLoading(); // 数据加载完成后隐藏加载提示
        }
    });
}


function updateTimeText() {
    $.ajax({
        url: "/api/app/getcutofftime",
        type: "GET",
        success: function (r) {
            if (r.code === 0) {
                document.getElementById("cutoffTime").innerText = utcToLocalFormatter(r.data);
                document.getElementById("lastUpdateTime").innerText = new Date().toLocaleString();
            }
        }
    });
}

// 倒计时
let countdown = 30;
let intervalId = setInterval(function() {
    // 每秒减少倒计时的时间
    countdown--;
    // 更新页面上的倒计时显示
    updateCountdownDisplay();

    // 如果倒计时结束
    if (countdown === 0) {
        // 停止定时器
        clearInterval(intervalId);

        // 执行倒计时结束后执行
        reload();

        // 重置倒计时的时间
        countdown = 30;
        intervalId = setInterval(arguments.callee, 1000);
    }
}, 1000);

// 定义一个函数用于更新页面上显示的倒计时
function updateCountdownDisplay() {
    // 假设你有一个 id 为 'countdown' 的元素用于显示倒计时
    document.getElementById('updateCountdown').innerText = countdown + 's';
}

function utcToLocalFormatter(cellValue) {
    let date = new Date(cellValue);
    return date.toLocaleString();

}

function statusFormatter(cellValue) {
    return cellValue == 1 ? "Yes" : "No";
}

function kmhFormatter(cellValue) {
    return cellValue + " km/h";
}


// run
updateTimeText();
contentsPreparation();

