const PREFIX = ctx + "business/invoice";

let date = new Date();
$("input[name='invoiceDate']").val(parseTime(date, "{y}-{m}-{d}"));

$('input[name=rdoModeRun]').change(function() {
    $("#errorInvoiceDate").html("");
    let modeRun = $('input[name="rdoModeRun"]:checked').val();
    $('input[name="invoiceDate"]').prop('disabled', (modeRun == 1));
    $('select[name="startHour"]').prop('disabled', (modeRun == 1));
    $('select[name="endHour"]').prop('disabled', (modeRun == 1));
});

function handleFileChange() {
    $("#file-chosen").html($("#file").val().split("\\").pop());
}

function downloadTemplate() {
    $.modal.loading("Đang tải file, vui lòng đợi ...");
    window.location.href =
        ctx +
        "common/download/file?filePath=" +
        encodeURI("/template/Invoice_Template.xls");
    $.modal.closeLoading();
}

function submitHandler(index, layero) {
    $("#errorInvoiceDate").html("");
    $("#errorFile").html("");
    $("#errorsText").html("");

    // Get invoice date
    let modeRun = $('input[name="rdoModeRun"]:checked').val();
    let invDate = new Date();
    if (modeRun == 0){
        let invoiceDate = $('input[name="invoiceDate"]').val();
        if (!invoiceDate){
            $("#errorInvoiceDate").html("Vui lòng chọn ngày xuất hóa đơn");
            return false;
        }

        // Check ngay hoa don
        invDate =  new Date(invoiceDate);
        invDate.setHours(0, 0, 0, 0);
        let now = new Date();
        now.setHours(0, 0, 0, 0);
        if (invDate < now) {
            $("#errorInvoiceDate").html("Ngày xuất hóa đơn phải sau hoặc bằng ngày hiện tại");
            return false;
        }

        if (invDate.getTime() == now.getTime()){
           let currentHour = (new Date()).getHours();
           if (currentHour >= 21){
            $("#errorInvoiceDate").html("Đã quá 21h, hãy cọn ngày xuất hóa đơn khác");
            return false;
           } 
        }

        // Check khung gio nhap lieu
        let startHour = $('select[name="startHour"]').val();
        let endHour = $('select[name="endHour"]').val();
        if (Number(endHour) <= Number(startHour)){
            $("#errorInvoiceDate").html("Khung giờ nhập liệu không hợp lệ");
            return false;
        }
    }

    

    // Check file have to selected by user
    let fileValue = $("#file").val();
    if (
        !fileValue ||
        (!$.common.endWith(fileValue, ".xls") && !$.common.endWith(fileValue, ".xlsx"))
    ) {
        $("#errorFile").html("Vui lòng chọn tệp có đuôi 'xls' hoặc ''xlsx'");
        return false;
    }

    // Check file size
    let fileSize = $("#file")[0].files[0].size;
    if (fileSize < 2 && fileSize > (1024*1024*10)){
        $("#errorFile").html("Vui lòng chọn tệp có dung lượng từ 0MB - 10MB");
        return false;
    }

    // Get invoice type
    let invoiceType = $('input[name="rdoInvoiceType"]:checked').val();
    let file = $("#file")[0];
    let formData = new FormData();
    formData.append("file", file.files[0]);
    formData.append("invoiceDate", $.common.dateFormat(invDate, "yyyy-MM-dd"));
    formData.append("startHour", $('select[name="startHour"]').val());
    formData.append("endHour", $('select[name="endHour"]').val());
    formData.append("invoiceType", invoiceType);
    formData.append("modeRun", modeRun);

    //Call API upload invoice
    $.ajax({
        type: "POST",
        url: PREFIX + "/upload",
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
                let errors = data.errors;
                if (Array.isArray(errors) && errors.length > 0){
                    $.modal.closeLoading();
                    errors.forEach(element => {
                        $("#errorsText").append(`
                            <p> `+ element +`</p>
                        `);
                    });
                    

                } else {
                    $.modal.closeLoading();
                    $.modal.close();
                    parent.$('input[name="invoiceUploadId"]').val(data.uploadId);
                    parent.search();
                }
            } else {
                $.modal.closeLoading();
                $.modal.alertError("System error! \r\n " + data.msg);
            }
        },
    });
      

}
