package com.mfd.vidauth;

import java.util.Objects;

public class UserDto {
	String username;
	String password;

	public UserDto(String username, String password) {
		this.username = username;
		this.password = password;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		UserDto that = (UserDto) o;
		return Objects.equals(username, that.username) &&
				Objects.equals(password, that.password);
	}

	@Override
	public int hashCode() {
		return Objects.hash(username, password);
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
