package vn.com.irtech.irbot.business.type;

public enum NotificationType {
	INFO(0), WARNING(1);

	private final Integer value;

	NotificationType(Integer value) {
		this.value = value;
	}

	public Integer value() {
		return this.value;
	}

	public static NotificationType fromValue(Integer value) {
		for (NotificationType e : NotificationType.values()) {
			if (value == e.value) {
				return e;
			}
		}
		return null;
	}
}
