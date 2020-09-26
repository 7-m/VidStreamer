package com.mfd.vidauth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepository {
	JdbcTemplate tmplt;

	@Autowired
	public UserRepository(JdbcTemplate tmplt) {
		this.tmplt = tmplt;
	}

	public static final String fetchUser  = "select * from USER where username = ?";
	public static final String insertUser = "insert into USER values(?, ?);";

	public boolean checkIfUserExists(String user) {
		return tmplt.queryForList(fetchUser, user).size() > 0;
	}

	public void createUser(String user, String passwd) {
		tmplt.update(insertUser, user, passwd);
	}

	public boolean validate(String user, String providedPassword) {
		UserDto dto = null;
		try {
			dto = tmplt.queryForObject(fetchUser, new Object[]{user},
									   (rs, rowNum) -> new UserDto(rs.getString("username"), rs.getString("passwd")));
		} catch (IncorrectResultSizeDataAccessException e) {
			return false;
		}
		return dto.password.equals(providedPassword);
	}
}

