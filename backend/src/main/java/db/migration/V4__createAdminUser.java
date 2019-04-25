package db.migration;

import java.sql.ResultSet;
import java.sql.Statement;

import org.flywaydb.core.api.migration.BaseJavaMigration;
import org.flywaydb.core.api.migration.Context;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class V4__createAdminUser extends BaseJavaMigration {
    private static final BCryptPasswordEncoder ENCODER = new BCryptPasswordEncoder();

    @Override
    public void migrate(Context context) throws Exception {
	Statement statment = context.getConnection().createStatement();

	// Insert Admin user
	statment.execute(
		"insert into users (user_name,user_password) values ('Admin','" + ENCODER.encode("admin123") + "')");

	statment.execute(
		"insert into users (user_name,user_password) values ('Guest','" + ENCODER.encode("guest123") + "')");

	addUsersRoles(statment, "Admin", "ADMIN");
	addUsersRoles(statment, "Guest", "USER");
    }

    private void addUsersRoles(Statement statment, String username, String roleName) throws Exception {
	int userId = 0, roleId = 0;
	ResultSet result = statment.executeQuery("SELECT id FROM users where user_name ='" + username + "'");
	if (result.next()) {
	    userId = result.getInt(1);
	}

	result = statment.executeQuery("SELECT id FROM roles where role_name ='" + roleName + "'");
	if (result.next()) {
	    roleId = result.getInt(1);
	}

	if (userId != 0 && roleId != 0) {
	    statment.execute("insert into users_roles (user_id,roles_id) values ('" + userId + "','" + roleId + "')");
	}
    }
}
