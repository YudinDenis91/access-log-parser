package main;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserAgent {
    private final String os;
    private final String browser;

    public UserAgent(String userAgentString) {
        Pattern osPattern = Pattern.compile("(?i)(Windows NT|Mac OS X|Linux)");
        Pattern browserPattern = Pattern.compile("(?i)(Edge|Firefox|Chrome|Opera)");

        Matcher osMatcher = osPattern.matcher(userAgentString);
        Matcher browserMatcher = browserPattern.matcher(userAgentString);

        if (osMatcher.find()) {
            this.os = osMatcher.group(1).trim();
        } else {
            this.os = "Other";
        }
        if (browserMatcher.find()) {
            this.browser = browserMatcher.group(1).trim();
        } else {
            this.browser = "Other";
        }
    }
    public String getOs() {
        return os;
    }
    public String getBrowser() {
        return browser;
    }
}
