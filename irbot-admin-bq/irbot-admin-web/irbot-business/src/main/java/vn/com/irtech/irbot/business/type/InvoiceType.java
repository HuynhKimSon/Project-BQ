package vn.com.irtech.irbot.business.type;

public enum InvoiceType {
	LE_ONLINE(0), LE_STORE(1), SY(2);

	private final Integer value;

	InvoiceType(Integer value) {
		this.value = value;
	}

	public Integer value() {
		return this.value;
	}

	public static InvoiceType fromValue(Integer value) {
		for (InvoiceType e : InvoiceType.values()) {
			if (value == e.value) {
				return e;
			}
		}
		return null;
	}
}
