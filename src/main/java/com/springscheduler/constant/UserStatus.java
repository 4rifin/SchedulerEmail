package com.springscheduler.constant;

public enum UserStatus {
	MEMBER_DELETED("delete", -2), 
	MEMBER_ACTIVE("active", -1), 
	MEMBER_INACTIVE("Inactive", 0); 
	
	String labelKey;
	int code;

	private UserStatus(String labelKey, int code) {
		this.labelKey = labelKey;
		this.code = code;
	}

	public String getLabelKey() {
		return this.labelKey;
	}

	public int getCode() {
		return this.code;
	}

	public static String getLabelFromCode(int code) {
		for (UserStatus status : values()) {
			if (status.code == code) {
				return status.labelKey;
			}
		}
		return null;
	}

}
