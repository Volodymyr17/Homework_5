package homework_5.utils;

/**
 * Help class to get passed parameters from environment for further usage in the automation project
 */
public class Properties {
    private static final String DEFAULT_BASE_URL = "http://prestashop-automation.qatestlab.com.ua/";

    private Properties() {
    }
    /**
     *
     * @return Website frontend.
     */
    public static String getBaseUrl() {
        return System.getProperty(EnvironmentVariable.BASE_URL.toString(), DEFAULT_BASE_URL);
    }
}

/**
 * All parameters that are passed to automation project
 */
enum EnvironmentVariable {
    BASE_URL("env.url");

    private String value;
    EnvironmentVariable(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}