const PREFIX = ctx + "business/invoice";
const PREFIX_HISTORY = ctx + "business/invoice/history";

// Init screen
$(document).ready(function () {
  $('input[name="invoiceUploadId"]').val("-1");
  loadList();

  $("input").keyup(function (event) {
    if (event.keyCode == 13) {
      $.table.search();
      event.preventDefault();
    }
  });
});

function search() {
  $.table.search();
}

function loadList() {
  const OPTIONS = {
    url: PREFIX + "/list",
    removeUrl: PREFIX + "/remove",
    updateUrl: PREFIX + "/edit/{id}",
    retryUrl: PREFIX + "/retry",
    uploadUrl: PREFIX + "/upload",
    modalName: "Hóa đơn",
    // sortName: "plan_start_date",
    // sortOrder: "asc",
    height: $(document).height(),
    pageSize: 200,
    pageList: [100, 200, 500],
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
        sortable: true,
        title: 'Số sản phẩm'
      },
      {
        field: 'totalAmount',
        title: 'Tổng thanh toán',
        formatter: function (value, row, index) {
          return currencyFormatter(row.totalAmount);
        }
      },
      {
        field: 'diff',
        title: 'Chênh lệch',
        formatter: function (value, row, index) {
          return `<span class="text-danger">` + currencyFormatter(row.diff) + `</span> `;
        }
      },
      {
        field: 'planStartDate',
        title: 'Thời gian nhập liệu',
        sortable: true,
        formatter: function (value, row, index) {
          return startTimeFormatter(value, row, index);
        }
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
            '<a class="btn btn-success btn-xs" href="javascript:void(0)" title="Chi tiết" onclick="$.operate.editTab(\'' +
            row.id +
            '\', \'90vw\')"><i class="fa fa-edit"  ></i></a> '
          );
          actions.push(
            '<a class="btn btn-danger btn-xs" href="javascript:void(0)" title="Xóa" onclick="$.operate.remove(\'' +
            row.id +
            '\')"><i class="fa fa-remove"></i></a> '
          );
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

function currencyFormatter(value) {
  if (value < 0) {
    return value + " VND";
  }
  return $.common.currencyFormat(value);
}

function startTimeFormatter(value, row, index) {
  if (row.modeRun == 0) {
    return row.planStartDate;
  }

  return 'Ngay lập tức';
}

function importExcel() {
  table.set();
  var _url = table.options.uploadUrl;
  var options = {
    title: "Tải lên file hóa đơn",
    width: "900px",
    url: _url,
    yes: function (index, layero) {
      var iframeWin = layero.find('iframe')[0];
      iframeWin.contentWindow.submitHandler(index, layero);
    },
  };
  $.modal.openOptions(options);
}

function sendRobot() {
  var uploadId = $('input[name="invoiceUploadId"]').val();
  //kiem tra nguoi dung upload hoa don hay chưa
  if (!uploadId || uploadId == "-1") {
    $.modal.alertWarning("Chưa nhập file hóa đơn!");
    return;
  }

  //call API send robot
  $.modal.confirm("Xác nhận gửi robot làm lệnh cho tất cả hóa đơn đã nhập, bạn có chắc chắn không?", function () {
    //kiem tra chenh lenh
    var isDiff = true;
    let formData = new FormData();
    formData.append("uploadId", uploadId);
    $.ajax({
      type: "POST",
      url: PREFIX + "/checkDataSendRobot",
      contentType: false,
      processData: false,
      data: formData,
      beforeSend: function () {
        $.modal.loading("Đang xử lý, vui lòng đợi...");
      },
      error: function (request) {
        $.modal.closeLoading();
        $.modal.alertError("System error!");
      },
      success: function (data) {
        $.modal.closeLoading();
        if (data == null) {
          return;
        }
        if (data.code == 0) {
          isDiff = data.isDiff;
          //Call API send robot
          if (!isDiff) {
            submitSendRobot(uploadId);
          } else {
            $.modal.confirm("<span class='text-danger'>Có hóa đơn chênh lệch tiền thuế GTGT, bạn có muốn tiếp tục gửi robot không?</span>", function () {
              submitSendRobot(uploadId);
            });
          }
        } else {
          $.modal.alertError("System error! \r\n " + data.msg);
        }
      },
    });

  });

}

function submitSendRobot(uploadId) {
  let formData = new FormData();
  formData.append("uploadId", uploadId);
  //Call API send robot
  $.ajax({
    type: "POST",
    url: PREFIX + "/sendRobot",
    contentType: false,
    processData: false,
    data: formData,
    beforeSend: function () {
      $.modal.loading("Đang xử lý, vui lòng đợi...");
    },
    error: function (request) {
      $.modal.closeLoading();
      $.modal.alertError("System error!");
    },
    success: function (data) {
      $.modal.closeLoading();
      if (data == null) {
        return;
      }
      if (data.code == 0) {
        $.modal.close();
        parent.$.modal.alertSuccess("Đã gửi " + data.result + " hóa đơn thành công!");
        $('input[name="invoiceUploadId"]').val("-1");
        $.table.search();
      } else {
        $.modal.alertError("System error! \r\n " + data.msg);
      }
    },
  });
}

function history() {
  table.set();
  var options = {
    title: "Lịch sử nhập hóa đơn ",
    url: PREFIX_HISTORY + '/history',
    width: "900px",
    skin: 'layui-layer-gray',
    btn: ['Đóng'],
    yes: function (index, layero) {
      layer.close(index);
    }
  };
  $.modal.openOptions(options);
}

function randomIndex() {
  var uploadId = $('input[name="invoiceUploadId"]').val();
  let formData = new FormData();
  formData.append("uploadId", uploadId);
  //Call API random index
  $.ajax({
    type: "POST",
    url: PREFIX + "/randomIndex",
    contentType: false,
    processData: false,
    data: formData,
    beforeSend: function () {
      $.modal.loading("Đang xử lý, vui lòng đợi...");
    },
    error: function (request) {
      $.modal.closeLoading();
      $.modal.alertError("System error!");
    },
    success: function (data) {
    
      if (data == null) {
        return;
      }
      if (data.code == 0) {
        $.modal.closeLoading();
        $.modal.close();
        $('input[name="invoiceUploadId"]').val(data.uploadId);
        $.table.search();
      } else {
        $.modal.alertError("System error! \r\n " + data.msg);
      }
    },
  });
}

function mergeInvoices() {
  var uploadId = $('input[name="invoiceUploadId"]').val();
  table.set();
  var rows = $.common.isEmpty(table.options.uniqueId) ? $.table.selectFirstColumns() : $.table.selectColumns(table.options.uniqueId);
  if (rows.length == 0) {
      $.modal.alertWarning("Vui lòng chọn ít nhất một bản ghi");
      return;
  }
  $.modal.confirm("Bạn có chắc chắn muốn gộp " + rows.length + " hóa đơn đã chọn không?", function () {
    var data = { "ids": rows.join() };
    var result = true;
    //Call API random index
    $.ajax({
      type: "POST",
      url: PREFIX + "/mergeInvoice",
      dataType: "json",
      data: data,
      beforeSend: function () {
        $.modal.loading("Đang xử lý, vui lòng đợi...");
      },
      error: function (request) {
        $.modal.closeLoading();
        $.modal.alertError("System error!");
      },
      success: function (data) {
        $.modal.closeLoading();
        $.modal.close();
        if (data == null) {
          return;
        }
        if (data.code == 0) {
          $.table.search();
          // if (!result) {
          //   $.modal.alertWarning("Gộp hóa đơn thất bại! Tên khách hàng hoặc Người mua hàng không trùng nhau!");
          //   return;
          // } else {
          //   mergeInvoices(data, uploadId);
          // }
          
        } else {
          $.modal.alertError("System error! \r\n " + data.msg);
        }
      },
    });
  });
}

