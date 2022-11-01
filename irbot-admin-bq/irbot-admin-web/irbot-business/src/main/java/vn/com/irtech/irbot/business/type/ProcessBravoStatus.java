package vn.com.irtech.irbot.business.type;

public enum ProcessBravoStatus {
	NOTSEND(0), SENT(1),  PROCESSING(2), FAIL(3), SUCCESS(4);

	private final Integer value;

	ProcessBravoStatus(Integer value) {
		this.value = value;
	}

	public Integer value() {
		return this.value;
	}

	public static ProcessBravoStatus fromValue(Integer value) {
		for (ProcessBravoStatus e : ProcessBravoStatus.values()) {
			if (value == e.value) {
				return e;
			}
		}
		return null;
	}
}
