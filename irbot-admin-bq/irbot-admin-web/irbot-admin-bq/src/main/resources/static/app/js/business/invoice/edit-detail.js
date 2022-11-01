const PREFIX = ctx + "business/invoice-detail";

function getTotal() {
  var amount = $("input[name='amount']").val();
  var taxAmount = $("input[name='taxAmount']").val();
  var totalAmount = parseInt(amount) + parseInt(taxAmount);
  $("input[name='totalAmount']").val(totalAmount);
}

function submitHandler() {
    var data = [];
    var pro = 0;
    if ($("input[name='promotion']").prop("checked") == true) {
        pro = 1;
    }
    data.push({ "name": "id", "value": $("input[name='id']").val() });
    data.push({ "name": "productName", "value": $("input[name='productName']").val() });
    data.push({ "name": "unit", "value": $("input[name='unit']").val() });
    data.push({ "name": "qty", "value": $("input[name='qty']").val() });
    data.push({ "name": "amount", "value": $("input[name='amount']").val() });
    data.push({ "name": "price", "value": $("input[name='price']").val() });
    data.push({ "name": "taxPercent", "value": $("input[name='taxPercent']").val() });
    data.push({ "name": "taxAmount", "value": $("input[name='taxAmount']").val() });
    //data.push({ "name": "totalAmount", "value": $("input[name='totalAmount']").val() });
    data.push({ "name": "channelCode", "value": $("input[name='channelCode']").val() });
    data.push({ "name": "code", "value": $("input[name='code']").val() });
    data.push({ "name": "storeCode", "value": $("input[name='storeCode']").val() });
    data.push({ "name": "promotion", "value": pro });

    if ($.validate.form()) {
        $.ajax({
          type: "POST",
          url: PREFIX + "/edit",
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
              $.modal.close();
              parent.refreshTab();
            } else{
              $.modal.closeLoading();
              $.modal.alertError("System error! \r\n " + data.msg);
            }
          },
        });
    }
}