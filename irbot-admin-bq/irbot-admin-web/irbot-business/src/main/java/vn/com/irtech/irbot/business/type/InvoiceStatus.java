package vn.com.irtech.irbot.business.type;

public enum InvoiceStatus {
	NOTSEND(0), SENT(1);

	private final Integer value;

	InvoiceStatus(Integer value) {
		this.value = value;
	}

	public Integer value() {
		return this.value;
	}

	public static InvoiceStatus fromValue(Integer value) {
		for (InvoiceStatus e : InvoiceStatus.values()) {
			if (value == e.value) {
				return e;
			}
		}
		return null;
	}
}
