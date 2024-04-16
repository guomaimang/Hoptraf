
$(function () {

    //隐藏错误提示框
    $('.alert-danger').css("display", "none");

    $("#jqGrid").jqGrid({
        // 设置API
        url: '/api/driver/list',
        datatype: "json",
        colModel: [
            // 设置列表表头
            {label: 'DriverID', name: 'driverID', index: 'driverID', width: 30, key: true, hidden: false},
            {label: 'CarPlateNumber', name: 'carPlateNumber', index: 'carPlateNumber', width: 30},
            {label: 'Speed', name: 'speed', index: 'speed', width: 30, editable: true, formatter: kmhFormatter},
            {label: 'RapidlySpeedup', name: 'isRapidlySpeedup', index: 'isRapidlySpeedup', width: 30, editable: true, formatter: statusFormatter},
            {label: 'RapidlySlowdown', name: 'isRapidlySlowdown', index: 'isRapidlySlowdown', width: 30, editable: true, formatter: statusFormatter},
            {label: 'NeutralSlide', name: 'isNeutralSlide', index: 'isNeutralSlide', width: 30, editable: true, formatter: statusFormatter},
            {label: 'OverSpeed', name: 'isOverspeed', index: 'isOverspeed', width: 30, editable: true, formatter: statusFormatter},
            {label: 'FatigueDriving', name: 'isFatigueDriving', index: 'isFatigueDriving', width: 30, editable: true, formatter: statusFormatter},
            {label: 'HthrottleStop', name: 'isHthrottleStop', index: 'isHthrottleStop', width: 30, editable: true, formatter: statusFormatter},
            {label: 'OilLeak', name: 'isOilLeak', index: 'isOilLeak', width: 30, editable: true, formatter: statusFormatter},
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
            root: "data",
            records: "data.count",
            page: "data.currentPage",
            total: "data.totalPage",
        },
        prmNames: {
            page: "pagenum",
            rows: "pagesize",
            order: "order",
        },
        gridComplete: function () {
            //隐藏grid底部滚动条
            $("#jqGrid").closest(".ui-jqgrid-bdiv").css({"overflow-x": "hidden"});
        },
        onSelectRow: function () {
            //返回选中的id
            let selectedRowIndex = $("#" + this.id).getGridParam('selrow');
            //返回点击这行xlmc的值
            window.open("/driver.html?driverid=" + selectedRowIndex);
        },
    });

    $(window).resize(function () {
        $("#jqGrid").setGridWidth($(".card-body").width());
    });

});

/**
 * jqGrid 重新加载
 */
function reload() {
    let page = $("#jqGrid").jqGrid('getGridParam', 'page');
    $("#jqGrid").jqGrid('setGridParam', {
        page: page
    }).trigger("reloadGrid");

    updateTimeText();
}

function statusFormatter(cellValue) {
    return cellValue == 1 ? "Yes" : "No";
}

function kmhFormatter(cellValue) {
    return cellValue + " km/h";
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
    document.getElementById('updateCountDown').innerText = countdown + 's';
}


function utcToLocalFormatter(cellValue) {
    let date = new Date(cellValue);
    return date.toLocaleString();
}

updateTimeText();