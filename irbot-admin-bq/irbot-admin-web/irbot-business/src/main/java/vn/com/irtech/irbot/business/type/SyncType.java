package vn.com.irtech.irbot.business.type;

public enum SyncType {
	MISA("1", "SyncMisa"), BRAVO("2", "SyncBravo");
	
	private final String type;
	private final String value;
	
	SyncType(String value, String type) {
		this.value = value;
		this.type = type;
	}

	public String value() {
		return this.value;
	}
	
	public String type() {
		return this.type;
	}

	public static SyncType fromValue(String value) {
		for (SyncType e : SyncType.values()) {
			if (value == e.value) {
				return e;
			}
		}
		return null;
	}
}
