const PREFIX = ctx + "business/invoice-detail";
const PREFIX_INV = ctx + "business/invoice";

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
            title: 'Số lượng',
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
        }
    ],
  };
  $.table.init(OPTIONS);
}

function promotionFormatter(value, row, index) {
    if (row.promotion == 0) {
        return 'Không';
    } else if (row.promotion == 1) {
        return 'Có';
    }
}
