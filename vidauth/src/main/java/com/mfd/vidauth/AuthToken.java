package com.mfd.vidauth;

public class AuthToken {
	String authorization;

	public AuthToken(String Authorization) {
		this.authorization = Authorization;
	}

	public String getAuthorization() {
		return authorization;
	}

	public void setAuthorization(String authorization) {
		this.authorization = authorization;
	}
}
