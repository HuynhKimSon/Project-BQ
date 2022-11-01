const PREFIX = ctx + "business/invoice-detail";
const PREFIX_INV = ctx + "business/invoice";

$("#planStartDate").datetimepicker({
    format: "yyyy-mm-dd hh:ii:ss",
    autoclose: true
});
let modeRun = $('input[name="rdoModeRun"]:checked').val();
$('input[name="planStartDate"]').prop('disabled', (modeRun == 1));

$('input[name=rdoModeRun]').change(function() {
    $("#errorInvoiceDate").html("");
    let modeRun = $('input[name="rdoModeRun"]:checked').val();
    $('input[name="planStartDate"]').prop('disabled', (modeRun == 1));
});

// Init screen
$(document).ready(function () {
  loadList();
});

function loadList() {
  const OPTIONS = {
    pagination: false,
    showSearch: false,
    showRefresh: false,
    removeUrl: PREFIX + "/remove",
    updateUrl: PREFIX + "/edit",
    modalName: 'Sản phẩm',
    data: details,
    columns: [
        {
            field: 'id',
            title: 'STT',
            align: 'center', 
            formatter: function(value, row, index) {
                return index + 1;
            }
        },
        {
            field: 'productName',
            title: 'Tên hàng hóa'
        },
        {
            field: 'unit',
            title: 'ĐVT',
            align: 'center',
        },
        {
            field: 'qty',
            title: 'SL',
            align: 'center', 
        },
        {
            field: 'price',
            title: 'Đơn giá',
            align: 'center', 
            formatter: function (value, row, index) {
                return $.common.currencyFormat(row.price);
            }
        },
        {
            field: 'amount',
            title: 'Thành tiền',
            align: 'center', 
            formatter: function (value, row, index) {
                return $.common.currencyFormat(row.amount);
            }
        },
        {
            field: 'taxPercent',
            title: '%VAT',
            align: 'center', 
        },
        {
            field: 'taxAmount',
            title: 'VAT',
            align: 'center', 
            formatter: function (value, row, index) {
                return $.common.currencyFormat(row.taxAmount);
            }
        },
        {
            field: 'taxAmount',
            title: 'Thanh toán',
            align: 'center', 
            formatter: function (value, row, index) {
                var totalAmount = row.amount + row.taxAmount;
                return $.common.currencyFormat(totalAmount);
            }
        },
        {
            field: 'code',
            title: 'Mã code',
            align: 'center', 
        },
        {
            field: 'storeCode',
            title: 'Mã kho',
            align: 'center', 
        },
        {
            field: 'promotion',
            title: 'KM',
            align: 'center', 
            formatter: function (value, row, index) {
                return promotionFormatter(value, row, index);
            }
        },
        {
            title: "Hành động",
            align: "center",
            formatter: function (value, row, index) {
                var actions = [];
                actions.push(
                '<a class="btn btn-success btn-xs" href="javascript:void(0)" title="Chi tiết" onclick="edit(\'' +
                row.id +
                '\')"><i class="fa fa-edit"  ></i></a> '
                );
                actions.push(
                '<a class="btn btn-danger btn-xs" href="javascript:void(0)" title="Xóa" onclick="remove(\'' +
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

/* Save invoice */
function submitHandler() {
    var data = [];
    var invDate = new Date();
    var dataRequest = {
        id: invoice.id,
        invoiceDate: $("#invoiceDate").val(),
        planStartDate: "",
        taxCode: $("#taxCode").val(),
        customerName: $("#customerName").val(),
        address: $("#address").val(),
        buyer: $("#buyer").val(),
        invoiceNo: $("#invoiceNo").val(),
        modeRun: $('input[name="rdoModeRun"]:checked').val()
    };
    data.push({ "name": "id", "value": dataRequest.id });
    data.push({ "name": "invoiceDate", "value": dataRequest.invoiceDate });
   
    data.push({ "name": "taxCode", "value": dataRequest.taxCode });
    data.push({ "name": "customerName", "value": dataRequest.customerName });
    data.push({ "name": "address", "value": dataRequest.address });
    data.push({ "name": "buyer", "value":  dataRequest.buyer });
    data.push({ "name": "invoiceNo", "value":  dataRequest.invoiceNo });
    data.push({ "name": "modeRun", "value":  dataRequest.modeRun });
    if (dataRequest.modeRun == 1) {
        dataRequest.planStartDate = null;
        data.push({ "name": "planStartDate", "value": dataRequest.planStartDate });
        if ($.validate.form()) {
            $.operate.saveTab(PREFIX_INV + "/edit", data);    
        }
    } else {
        // get plan start date
        var startDate = $("#planStartDate").val();
        if (!startDate) {
            $("#errorInvoiceDate").html("Vui lòng chọn ngày xuất hóa đơn");
        } else {
            invDate =  new Date(startDate);
            //invDate.setHours(0, 0, 0, 0);
            let now = new Date();
            //now.setHours(0, 0, 0, 0);
            if (invDate < now) {
                $("#errorInvoiceDate").html("Ngày xuất hóa đơn phải sau hoặc bằng ngày hiện tại");
            } else {
                dataRequest.planStartDate = $.common.dateFormat(invDate, "yyyy-MM-dd HH:mm:ss");
                data.push({ "name": "planStartDate", "value": dataRequest.planStartDate });
                if ($.validate.form()) {
                    $.operate.saveTab(PREFIX_INV + "/edit", data);  
                }
            }  
        }
    }  
}

/* Remove product */
function remove(id) {
    table.set();
    $.modal.confirm("Bạn có chắc chắn để xóa sản phẩm khỏi hóa đơn này không？", function () {
        var url = $.common.isEmpty(id) ? table.options.removeUrl : table.options.removeUrl.replace("{id}", id);
        var data = { "ids": id };
        $.ajax({
            type: "POST",
            url: url,
            dataType : "json",
            data: data,
            beforeSend: function () {
                $.modal.loading("Đang xử lý, vui lòng đợi...");
            },
            error: function (request) {
                $.modal.closeLoading();
                $.modal.alertError("System error!");
            },
            success: function (data) {
                if (data == null){
                    return;
                }
                if (data.code == 0){
                    $.modal.closeLoading();
                    refreshTab();
                } else{
                    $.modal.closeLoading();
                    $.modal.alertError("System error! \r\n " + data.msg);
                }
            },
        });
        
    });
}

function promotionFormatter(value, row, index) {
    if (row.promotion == 0) {
        return 'Không';
    } else if (row.promotion == 1) {
        return 'Có';
    }
}

function edit(id){
    table.set();
    var options = {
        title: "Chi tiết sản phẩm",
        url: table.options.updateUrl + "/" + id,
        btn: ['<i class="fa fa-check"></i> Xác nhận', '<i class="fa fa-close"></i> Đóng'],
        yes: function (index, layero) {
            var iframeWin = layero.find('iframe')[0];
            iframeWin.contentWindow.submitHandler(index, layero);
        }
    };
    $.modal.openOptions(options);
}

function closeInv() {
   closeItem();
   $.table.refresh();
}