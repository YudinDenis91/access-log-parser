package main;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Statistics {
    private int totalTraffic;
    private LocalDateTime minTime;
    private LocalDateTime maxTime;
    private final HashSet<String> pages = new HashSet<>();
    private final HashMap<String, Integer> osCounts = new HashMap<>();
    private final HashMap<String, Integer> browserCounts = new HashMap<>();
    private final Set<String> userIps = new HashSet<>();
    private final List<LogEntry> entries = new ArrayList<>();
    private int errorCount = 0;
    private final HashMap<Integer, Integer> secondsVisits = new HashMap<>();
    private final HashSet<String> refererDomains = new HashSet<>();
    private final HashMap<String, Integer> userVisits = new HashMap<>();


    public Statistics() {
        this.totalTraffic = 0;
        this.minTime = LocalDateTime.MAX; // Инициализируем максимальным значением
        this.maxTime = LocalDateTime.MIN; // Инициализируем минимальным значением
    }
    public void addEntry(LogEntry entry) {
        if (entry.getStatusCode() == 404) {
            pages.add(entry.getPath());
        }
        String os = new UserAgent(entry.getUserAgent()).getOs();
        if (osCounts.containsKey(os)) {
            osCounts.put(os, osCounts.get(os) + 1);
        } else {
            osCounts.put(os, 1);
        }
        String browser = new UserAgent(entry.getUserAgent()).getBrowser();
        if (browserCounts.containsKey(browser)) {
            browserCounts.put(browser, browserCounts.get(browser) + 1);
        } else {
            browserCounts.put(browser, 1);
        }
        if (!entry.getUserAgent().contains("bot") && !userIps.contains(entry.getIp())) {
            userIps.add(entry.getIp());
        }
        entries.add(entry);
        if (entry.getStatusCode() >= 400) {
            errorCount++;
        }
        if (!entry.getUserAgent().contains("bot")) {
            int second = entry.getDateTime().getSecond();
            if (secondsVisits.containsKey(second)) {
                secondsVisits.put(second, secondsVisits.get(second) + 1);
            } else {
                secondsVisits.put(second, 1);
            }
            if (entry.getReferer() != null && !entry.getReferer().isEmpty()) {
                String domain = extractDomain(entry.getReferer());
                refererDomains.add(domain);
            }
            String ip = entry.getIp();
            if (userVisits.containsKey(ip)) {
                userVisits.put(ip, userVisits.get(ip) + 1);
            } else {
                userVisits.put(ip, 1);
            }
        }
        /*totalTraffic += entry.getBytesSent();
        if (entry.getDateTime().isBefore(minTime)) {
            minTime = entry.getDateTime();
        }
        if (entry.getDateTime().isAfter(maxTime)) {
            maxTime = entry.getDateTime();
        }*/
    }
    private String extractDomain(String url) {
        Pattern pattern = Pattern.compile("^(?:https?:\\/\\/)?([^/]+)");
        Matcher matcher = pattern.matcher(url);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return "";
    }
    public HashSet<String> getPages() {
        return pages;
    }
    public Map<String, Double> getOsDistribution() {
        HashMap<String, Double> osDistribution = new HashMap<>();
        int totalOsCount = osCounts.values().stream().reduce(0, Integer::sum);
        for (Map.Entry<String, Integer> entry : osCounts.entrySet()) {
            double osRatio = (double) entry.getValue() / totalOsCount;
            osDistribution.put(entry.getKey(), osRatio);
        }
        return osDistribution;
    }
    public Map<String, Double> getBrowserDistribution() {
        HashMap<String, Double> browserDistribution = new HashMap<>();
        int totalBrowserCount = browserCounts.values().stream().reduce(0, Integer::sum);
        for (Map.Entry<String, Integer> entry : browserCounts.entrySet()) {
            double browserRatio = (double) entry.getValue() / totalBrowserCount;
            browserDistribution.put(entry.getKey(), browserRatio);
        }
        return browserDistribution;
    }
    public double getAverageVisitsPerHour() {
        if (entries.isEmpty()) {
            return 0;
        }
        LocalDateTime firstTime = entries.get(0).getDateTime();
        LocalDateTime lastTime = entries.get(entries.size() - 1).getDateTime();
        long hours = Math.abs(lastTime.until(firstTime, java.time.temporal.ChronoUnit.HOURS));
        return (double) entries.size() / hours;
    }

    public double getAverageErrorRequestsPerHour() {
        if (entries.isEmpty()) {
            return 0;
        }
        LocalDateTime firstTime = entries.get(0).getDateTime();
        LocalDateTime lastTime = entries.get(entries.size() - 1).getDateTime();
        long hours = Math.abs(lastTime.until(firstTime, java.time.temporal.ChronoUnit.HOURS));
        return hours / (double) errorCount;
    }

    public double getAverageVisitsPerUser() {
        if (userIps.isEmpty()) {
            return 0;
        }
        return (double) entries.size() / userIps.size();
    }
    public int getPeakVisitsPerSecond() {
        return secondsVisits.values().stream().max(Integer::compareTo).orElse(0);
    }

    public Set<String> getRefererDomains() {
        return refererDomains;
    }

    public int getMaxVisitsPerUser() {
        return userVisits.values().stream().max(Integer::compareTo).orElse(0);
    }
    public double getTrafficRate() {
        double hours = ChronoUnit.HOURS.between(maxTime, minTime); //
        if (hours == 0) {
            return 0; //
        }
        return (double) totalTraffic / hours;
    }
}
