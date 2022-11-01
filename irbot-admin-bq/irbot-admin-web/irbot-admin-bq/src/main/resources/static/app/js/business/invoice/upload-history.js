const PREFIX = ctx + "business/invoice/history";

const OPTIONS = {
    url: PREFIX + "/list",
    removeUrl: PREFIX + "/remove",
    downloadUrl: PREFIX + "/download_file",
    modalName: "Lịch sử nhập hóa đơn",
    columns: [
        {
            checkbox: true,
        },
        {
            field: 'id',
            title: 'invoice_upload_history_id',
            visible: false
        },
        {
            field: 'createTime',
            title: 'Ngày import',
            sortable: true
        },
        {
            field: 'createBy',
            title: 'Người import'
        },
        {
            field: 'invoiceType',
            title: 'Loại hóa đơn',
            align: 'center',
            formatter: function (value, row, index) {
                return typeFormatter(value, row, index);
            }
        },
        {
            field: 'fileName',
            title: 'Tên file',
            sortable: true
        },
        {
            title: 'Hành động',
            align: 'center',
            formatter: function (value, row, index) {
                var actions = [];
                actions.push('<a class="btn btn-success btn-xs " href="javascript:void(0)" title="Tải xuống" onclick="handleDownload(' + row.id + ')"><i class="fa fa-download"></i></a> ');
                actions.push('<a class="btn btn-danger btn-xs " href="javascript:void(0)" title="Xóa" onclick="$.operate.remove(\'' + row.id + '\')"><i class="fa fa-remove"></i></a> ');
                return actions.join('');
            }
        }]
};

$(document).ready(function () {
    $.table.init(OPTIONS);
});

function typeFormatter(value, row, index) {
    if (row.invoiceType == 0) {
      return '<span class="label label-warning">Khách lẻ online</span>';
    } else if (row.invoiceType == 1) {
      return '<span class="label label-success">Khách lẻ cửa hàng</span>';
    } else if (row.invoiceType == 2) {
      return '<span class="label label-primary">Khách sỹ</span>';
    }
  }

function handleDownload(id) {
    table.set();
    $.modal.loading("Đang xuất dữ liệu, vui lòng đợi ...");
    $.post(table.options.downloadUrl + "/" + id, null, function (result) {
        if (result.code == web_status.SUCCESS) {
            window.location.href = ctx + "common/download/file?filePath=" + encodeURI(result.msg);
        } else if (result.code == web_status.WARNING) {
            $.modal.alertWarning(result.msg)
        } else {
            $.modal.alertError(result.msg);
        }
        $.modal.closeLoading();
    });
}

