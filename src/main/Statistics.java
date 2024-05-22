package main;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class Statistics {
    private int totalTraffic;
    private LocalDateTime minTime;
    private LocalDateTime maxTime;

    public Statistics() {
        this.totalTraffic = 0;
        this.minTime = LocalDateTime.MAX; // Инициализируем максимальным значением
        this.maxTime = LocalDateTime.MIN; // Инициализируем минимальным значением
    }
    public void addEntry(LogEntry entry) {
        totalTraffic += entry.getBytesSent();
        if (entry.getDateTime().isBefore(minTime)) {
            minTime = entry.getDateTime();
        }
        if (entry.getDateTime().isAfter(maxTime)) {
            maxTime = entry.getDateTime();
        }
    }
    public double getTrafficRate() {
        double hours = ChronoUnit.HOURS.between(maxTime, minTime); // Вычисляем разницу в часах
        if (hours == 0) {
            return 0; // Возвращаем 0, если разница в часах равна 0
        }
        return (double) totalTraffic / hours;
    }
}
