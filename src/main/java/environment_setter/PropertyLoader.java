package environment_setter;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertyLoader {

    public PropertyLoader(){

    }

    public Properties getDbConnexionDetails() throws IOException {
        InputStream inputStream = new FileInputStream(".\\src\\main\\resources\\db_settings.properties");

        Properties properties = new Properties();
        properties.load(inputStream);
        return properties;
    }
}
