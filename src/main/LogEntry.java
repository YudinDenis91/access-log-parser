package main;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class LogEntry {
    private final String ip;
    private final LocalDateTime dateTime;
    private final HttpMethod method;
    private final String path;
    private final int statusCode;
    private final int bytesSent;
    private final String referer;
    private final String userAgent;

    public LogEntry(String logLine) {
        Pattern pattern = Pattern.compile("^(\\S+) \\S+ \\S+ \\[([\\w:/]+\\s[+\\-]\\d{4})\\] \"(\\S+ \\S+ \\S+)\" (\\d{3}) (\\d+) \"(.*?)\" \"(.*?)\"$");
        Matcher matcher = pattern.matcher(logLine);
        if (matcher.matches()) {
            this.ip = matcher.group(1);
            this.dateTime = parseDateTime(matcher.group(2));
            this.method = HttpMethod.valueOf(matcher.group(3).split(" ")[0]);
            this.path = matcher.group(3).split(" ")[1] + " " + matcher.group(3).split(" ")[2];
            this.statusCode = Integer.parseInt(matcher.group(4));
            this.bytesSent = Integer.parseInt(matcher.group(5));
            this.referer = matcher.group(6);
            this.userAgent = matcher.group(7);
        } else {
            throw new IllegalArgumentException("Неверный формат строки лога: " + logLine);
        }
    }
    private LocalDateTime parseDateTime(String dateTimeString) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MMM/yyyy:HH:mm:ss Z", Locale.ENGLISH);
            return LocalDateTime.parse(dateTimeString, formatter);
        } catch (Exception e) {
            throw new IllegalArgumentException("Неверный формат даты и времени: " + dateTimeString);
        }
    }
    public String getIp() {
        return ip;
    }
    public LocalDateTime getDateTime() {
        return dateTime;
    }
    public HttpMethod getMethod() {
        return method;
    }
    public String getPath() {
        return path;
    }
    public int getStatusCode() {
        return statusCode;
    }
    public int getBytesSent() {
        return bytesSent;
    }
    public String getReferer() {
        return referer;
    }
    public String getUserAgent() {
        return userAgent;
    }
    public enum HttpMethod {
        GET,
        POST,
        PUT,
        DELETE,
        HEAD,
        OPTIONS,
        CONNECT,
        TRACE
    }
}
