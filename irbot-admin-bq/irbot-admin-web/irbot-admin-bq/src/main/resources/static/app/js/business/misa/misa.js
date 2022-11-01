var prefix = ctx + "business/misa";
$(function () {
    var options = {
        url: prefix + "/list",
        detailUrl: prefix + "/detail/{id}",
        removeUrl: prefix + "/remove",
        exportUrl: prefix + "/export",
        modalName: "lịch sử làm lệnh",
        columns: [{
            checkbox: true
        },
            {
                field: 'id',
                title: 'process_misa Id',
                visible: false
            },
            {
                field: 'invoiceDate',
                title: 'Ngày hóa đơn',
                sortable: true
            },
            {
                field: 'invoiceType',
                title: 'Loại đơn hàng',
                sortable: true,
                formatter: function (value, row, index) {
                    return invoiceTypeFormat(value, row, index);
                },
            },
            {
                field: 'taxCode',
                title: 'Mã số thuế',
                sortable: true
            },
            {
                field: 'customerName',
                title: 'Tên Khách Hàng',
                sortable: true
            },
            {
                field: 'startDate',
                title: 'Ngày đồng bộ',
                sortable: true
            },

            {
                field: 'status',
                title: 'Trạng thái',
                formatter: function (value, row, index) {
                    return statusMisaFormat(value, row, index);
                },
            },

            {
                field: 'invoiceNo',
                title: 'Số hóa đơn',
                sortable: true
            },
            {
                field: 'error',
                title: 'Lỗi'
            },
            {
                title: 'Hành động',
                align: 'center',
                formatter: function (value, row, index) {
                    var actions = [];
                    actions.push('<a class="btn btn-primary btn-xs" href="javascript:void(0)" onclick="detail(' + row.id + ' )"><i class="fa fa-eye"></i></a> ');
                    actions.push('<a class="btn btn-danger btn-xs ' + removeFlag + '" href="javascript:void(0)" onclick="$.operate.remove(\'' + row.id + '\')"><i class="fa fa-remove"></i></a>');
                    return actions.join('');
                }
            }]
    };
    $.table.init(options);
});

function statusMisaFormat(value, row, index) {
    if (row.status == 0) {
        return '<span class="label label-default"> Chưa làm</span>';
    } else if (row.status == 1) {
        return '<span class="label label-warning"> Đã gửi</span>'
    } else if (row.status == 2) {
        return '<span  class="label label-success"> Đang làm</span>'
    } else if (row.status == 3) {
        return '<span class="label label-danger"> Thất bại</span>'
    } else {
        return '<span class="label label-primary">Thành công</span>';
    }
}

function invoiceTypeFormat(value, row, index) {
    if (row.invoiceType == 0) {
        return '<span class="label label-warning" > Khách lẻ online</span>';
    } else if (row.invoiceType == 1) {
        return '<span class="label label-success"> Khách lẻ cửa hàng</span>'
    } else {
        return '<span class="label label-primary">Khách sỉ</span>';
    }
}

function detail(id) {
    table.set();
    var _url = $.operate.detailUrl(id);
    var options = {
        title: "Chi tiết " + table.options.modalName,
        width: "900px",
        url: _url,
        skin: 'layui-layer-gray',
        btn: ['Đóng'],
        yes: function (index, layero) {
            layer.close(index);
        }
    };
    $.modal.openOptions(options);
}
