
$(function () {

    //隐藏错误提示框
    $('.alert-danger').css("display", "none");

    $("#jqGrid").jqGrid({
        // 设置API
        url: '/api/eventreport/list',
        datatype: "json",
        colModel: [
            // 设置列表表头
            {label: 'Event ID', name: 'id', index: 'id', width: 15},
            {label: 'Driver ID', name: 'driverId', index: 'driverId', width: 20, key: true, hidden: false},
            {label: 'Card Plate Num', name: 'carPlateNumber', index: 'carPlateNumber', width: 20},
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
        onSelectRow: function () {
            //返回选中的id
            let selectedRowIndex = $("#" + this.id).getGridParam('selrow');
            //返回点击这行xlmc的值
            window.location.href="/driver.html?driverId=" + selectedRowIndex;
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