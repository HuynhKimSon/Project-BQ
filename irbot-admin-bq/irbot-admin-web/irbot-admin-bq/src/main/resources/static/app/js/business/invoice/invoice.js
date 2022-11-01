const PREFIX = ctx + "business/invoice";

let date = new Date();
let firstDay = lastDay = date;
$("input[name='params[beginTime]']").val(parseTime(firstDay, "{y}-{m}-{d}"));
$("input[name='params[endTime]']").val(parseTime(lastDay, "{y}-{m}-{d}"));

// Init screen
$(document).ready(function () {
  loadList();

  $("input").keyup(function (event) {
    if (event.keyCode == 13) {
      $.table.search();
      event.preventDefault();
    }
  });
});

function loadList() {
  const OPTIONS = {
    url: PREFIX + "/list",
    removeUrl: PREFIX + "/remove",
    detailUrl: PREFIX + "/detail/{id}",
    retryMisaUrl: PREFIX + "/retry-misa",
    retryBravoUrl: PREFIX + "/retry-bravo",
    uploadUrl: PREFIX + "/upload",
    modalName: "Hóa đơn",
    // sortName: "id",
    // sortOrder: "desc",
    pageSize: 50,
    pageList: [50, 100, 200],
    columns: [
      {
        checkbox: true,
      },
      {
        field: 'id',
        title: 'Id',
        visible: false
      },
      {
        field: 'invoiceDate',
        sortable: true,
        title: 'Ngày hóa đơn'
      },
      {
        field: 'customerName',
        sortable: true,
        title: 'Tên khách hàng'
      },
      {
        field: 'taxCode',
        title: 'Mã số thuế',
        visible: false
      },
      {
        field: 'address',
        title: 'Địa chỉ',
        visible: false
      },
      {
        field: 'buyer',
        sortable: true,
        title: 'Người mua hàng'
      },
      {
        field: 'createTime',
        title: 'Ngày import',
        visible: false
      },
      {
        field: 'createBy',
        title: 'Người import',
        visible: false
      },
      {
        field: 'type',
        title: 'Loại hóa đơn',
        align: 'center',
        formatter: function (value, row, index) {
          return typeFormatter(value, row, index);
        }
      },
      {
        field: 'totalProduct',
        title: 'Số SP'
      },
      {
        field: 'totalAmount',
        title: 'Tổng TT',
        formatter: function (value, row, index) {
          return currencyFormatter(value, row, index);
        }
      },
      {
        field: 'planStartDate',
        title: 'Thời gian nhập liệu'
      },
      {
        field: 'statusMisa',
        title: 'Trạng thái Misa',
        align: 'center',
        formatter: function (value, row, index) {
          return processStatusFormat(row.statusMisa);
        }
      },
      {
        field: 'statusBravo',
        title: 'Trạng thái Bravo',
        align: 'center',
        formatter: function (value, row, index) {
          return processStatusFormat(row.statusBravo);
        }
      },
      {
        field: 'invoiceNo',
        title: 'Số hóa đơn',
        sortable: true
      },
      {
        field: 'randomIndex',
        title: 'Số ngẫu nhiên',
        visible: false
      },
      {
        title: "Hành động",
        align: "center",
        formatter: function (value, row, index) {
          var actions = [];
          actions.push(
            '<a class="btn btn-primary btn-xs ma2" href="javascript:void(0)" title="Chi tiết" onclick="$.operate.detailTab(\'' +
            row.id +
            '\', \'90vw\')"><i class="fa fa-eye"  ></i>Chi tiết</a> '
          );
          actions.push(
            '<a class="btn btn-danger btn-xs ma2" href="javascript:void(0)" title="Xóa" onclick="$.operate.remove(\'' +
            row.id +
            '\')"><i class="fa fa-remove"></i>Xóa</a> '
          );
          var more = [];
					more.push("<a class='btn btn-default btn-xs' href='javascript:void(0)' onclick='detailProcessMisa(" + row.processMisaId + ")'><i class='fa fa-eye'></i>Misa</a> ");
          more.push("<a class='btn btn-default btn-xs' href='javascript:void(0)' onclick='detailProcessBravo(" + row.processBravoId + ")'><i class='fa fa-eye'></i>Bravo</a>");
          actions.push('<a tabindex="0" class="btn btn-info btn-xs ma2" role="button" data-container="body" data-placement="left" data-toggle="popover" data-html="true" data-trigger="hover" data-content="' + more.join('') + '"><i class="fa fa-chevron-circle-right"></i>Lịch sử làm lệnh</a> ');
          return actions.join("");
        },
      },
    ],
  };
  $.table.init(OPTIONS);
}

function typeFormatter(value, row, index) {
  if (row.type == 0) {
    return '<span class="label label-warning">Khách lẻ online</span>';
  } else if (row.type == 1) {
    return '<span class="label label-success">Khách lẻ cửa hàng</span>';
  } else if (row.type == 2) {
    return '<span class="label label-primary">Khách sỹ</span>';
  }
}

function processStatusFormat(status) {
  if (status == 0) {
      return '<span class="label label-default"> Chưa làm</span>';
  } else if (status == 1) {
      return '<span class="label label-warning"> Đã gửi</span>'
  } else if (status == 2) {
      return '<span  class="label label-success"> Đang làm</span>'
  } else if (status == 3) {
      return '<span class="label label-danger"> Thất bại</span>'
  } else {
      return '<span class="label label-primary">Thành công</span>';
  }
}

function currencyFormatter(value, row, index) {
  return $.common.currencyFormat(row.totalAmount);
}

function detailProcessMisa(id){
  if (!id || id < 1){
    $.modal.alertWarning("Không có dữ liệu làm lệnh");
    return;
  }
  var _url = ctx + "business/misa/detail/" + id;
  var options = {
      title: "Chi tiết làm lệnh Misa",
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

function detailProcessBravo(id){
  if (!id || id < 1){
    $.modal.alertWarning("Không có dữ liệu làm lệnh");
    return;
  }
  var _url = ctx + "business/bravo/detail/" + id;
  var options = {
      title: "Chi tiết làm lệnh Bravo",
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

function retryMisa(){
  table.set();
  var rows = $.common.isEmpty(table.options.uniqueId) ? $.table.selectFirstColumns() : $.table.selectColumns(table.options.uniqueId);
  if (rows.length == 0) {
    $.modal.alertWarning("Vui lòng chọn ít nhất một bản ghi");
    return;
  }
  $.modal.confirm("Xác nhận gửi robot làm lệnh Misa " + rows.length + " bản ghi đã chọn ?", function () {
      var url = table.options.retryMisaUrl;
      var data = { "ids": rows.join() };
      $.operate.submit(url, "post", "json", data);
  });
}

function retryBravo(){
  table.set();
  var rows = $.common.isEmpty(table.options.uniqueId) ? $.table.selectFirstColumns() : $.table.selectColumns(table.options.uniqueId);
  if (rows.length == 0) {
    $.modal.alertWarning("Vui lòng chọn ít nhất một bản ghi");
    return;
  }
  $.modal.confirm("Xác nhận gửi robot làm lệnh Bravo " + rows.length + " bản ghi đã chọn ?", function () {
      var url = table.options.retryBravoUrl;
      var data = { "ids": rows.join() };
      $.operate.submit(url, "post", "json", data);
  });
}