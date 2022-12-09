package shopdackh.dao;

import java.util.List;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

import shopdackh.constant.GlobalConstant;
import shopdackh.model.Role;

@Repository
public class RoleDAO extends AbstractDAO<Role> {

	@Override
	public List<Role> getAll() {
		String sql = "SELECT * FROM roles";
		return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Role.class));
	}

	public List<Role> getRoleNotAdmin() {
		String sql = "SELECT * FROM roles WHERE roleId != ?";
		return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Role.class), GlobalConstant.ROLE_ADMIN);
	}

}
