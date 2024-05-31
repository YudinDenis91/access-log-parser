package main;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class Statistics {
    private int totalTraffic;
    private LocalDateTime minTime;
    private LocalDateTime maxTime;
    private final HashSet<String> pages = new HashSet<>();
    private final HashMap<String, Integer> osCounts = new HashMap<>();

    public Statistics() {
        this.totalTraffic = 0;
        this.minTime = LocalDateTime.MAX; // Инициализируем максимальным значением
        this.maxTime = LocalDateTime.MIN; // Инициализируем минимальным значением
    }
    public void addEntry(LogEntry entry) {
        if (entry.getStatusCode() == 200) {
            pages.add(entry.getPath());
        }
        if (osCounts.containsKey(entry.getUserAgent())) {
            osCounts.put(entry.getUserAgent(), osCounts.get(entry.getUserAgent()) + 1);
        } else {
            osCounts.put(entry.getUserAgent(), 1);
        }
        /*totalTraffic += entry.getBytesSent();
        if (entry.getDateTime().isBefore(minTime)) {
            minTime = entry.getDateTime();
        }
        if (entry.getDateTime().isAfter(maxTime)) {
            maxTime = entry.getDateTime();
        }*/
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
    public double getTrafficRate() {
        double hours = ChronoUnit.HOURS.between(maxTime, minTime); // Вычисляем разницу в часах
        if (hours == 0) {
            return 0; // Возвращаем 0, если разница в часах равна 0
        }
        return (double) totalTraffic / hours;
    }
}
