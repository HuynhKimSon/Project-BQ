package vn.com.irtech.irbot.business.type;

public enum ModeRunType {
	SCHEDULE(0), IMMEDIATELY(1);

	private final Integer value;

	ModeRunType(Integer value) {
		this.value = value;
	}

	public Integer value() {
		return this.value;
	}

	public static ModeRunType fromValue(Integer value) {
		for (ModeRunType e : ModeRunType.values()) {
			if (value == e.value) {
				return e;
			}
		}
		return null;
	}
}
