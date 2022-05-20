package environment_setter;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class EnvironmentReader {
    final String environment;
    final  String server;
    final String db_name;
    final String user_name;
    final String password;


    public String getServer() {
        return server;
    }

    public String getDb_name() {
        return db_name;
    }

    public String getUser_name() {
        return user_name;
    }

    public String getPassword() {
        return password;
    }

    public EnvironmentReader(String environment, String server, String db_name, String user_name, String password) {
            this.environment = environment;
            this.server = server;
            this.db_name = db_name;
            this.user_name = user_name;
            this.password = password;
    }
}
