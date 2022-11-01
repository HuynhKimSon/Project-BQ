package vn.com.irtech.irbot.business.type;

public enum ProcessMisaStatus {
	NOTSEND(0), SENT(1),  PROCESSING(2), FAIL(3), SUCCESS(4);

	private final Integer value;

	ProcessMisaStatus(Integer value) {
		this.value = value;
	}

	public Integer value() {
		return this.value;
	}

	public static ProcessMisaStatus fromValue(Integer value) {
		for (ProcessMisaStatus e : ProcessMisaStatus.values()) {
			if (value == e.value) {
				return e;
			}
		}
		return null;
	}
}
