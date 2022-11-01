package vn.com.irtech.irbot.business.type;

public enum RobotServiceType {
	SYNC_MISA(100L), SYNC_BRAVO(200L);

	private final Long value;

	RobotServiceType(Long value) {
		this.value = value;
	}

	public Long value() {
		return this.value;
	}

	public static RobotServiceType fromValue(Long value) {
		for (RobotServiceType e : RobotServiceType.values()) {
			if (value == e.value) {
				return e;
			}
		}
		return null;
	}
}
