package main;

import java.io.*;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    public static void main(String[] args) {
        int count=0;
        String path;
        for ( ; ; ) {
            System.out.print("Путь к файлу: ");
            path = new Scanner(System.in).nextLine();
            File file = new File(path);
            boolean fileExists = file.exists();
            boolean isDirectory = file.isDirectory();
            if (!fileExists) {
                System.out.println("Указанный файл не существует");continue;
            }
            if (isDirectory){
                System.out.println("Указанный путь не является путем к файлу");
            }
            else {
                System.out.println("Путь указан верно");
                count++;
                System.out.println("Это файл номер " + count);
            }
            try {
                FileReader fileReader = new FileReader(path);
                BufferedReader reader = new BufferedReader(fileReader);
                String line;
                int googleBotCount = 0;
                int yandexBotCount = 0;
                int countlines = 0;
                Statistics statistics = new Statistics();
                Pattern pattern = Pattern.compile("^(\\S+) \\S+ \\S+ \\[([\\w:/]+\\s[+\\-]\\d{4})\\] \"(\\S+ \\S+ \\S+)\" (\\d{3}) (\\d+) \"(.*?)\" \"(.*?)\"$");
                while ((line = reader.readLine()) != null) {
                    int length = line.length();
                    if (length>1024) throw new LineTooLongException("Строка превышает максимальную длину: 1024 символа");
                    countlines++;
                    Matcher matcher = pattern.matcher(line);
                    if (matcher.matches()) {
                        LogEntry logEntry = new LogEntry(line);
                        UserAgent userAgent = new UserAgent(line);
                        statistics.addEntry(logEntry);
                        System.out.println(userAgent.getOs() + " " + userAgent.getBrowser());
                        if (logEntry.getUserAgent().contains("Googlebot")) {
                            googleBotCount++;
                        } else if (logEntry.getUserAgent().contains("YandexBot")) {
                            yandexBotCount++;
                        }
                    }
                }
                //System.out.println(statistics.getPages());
                //System.out.println(statistics.getOsDistribution());
                //double trafficRate = statistics.getTrafficRate();
                //System.out.println(trafficRate);
                // Выводим статистику страниц
                System.out.println("\nСписок страниц:");
                for (String page : statistics.getPages()) {
                    System.out.println(page);
                }
                // Выводим статистику операционных систем
                System.out.println("\nСтатистика операционных систем:");
                for (Map.Entry<String, Double> entry : statistics.getOsDistribution().entrySet()) {
                    System.out.println(entry.getKey() + ": " + entry.getValue());
                }
                // Выводим статистику браузеров
                System.out.println("\nСтатистика браузеров:");
                for (Map.Entry<String, Double> entry : statistics.getBrowserDistribution().entrySet()) {
                    System.out.println(entry.getKey() + ": " + entry.getValue());
                }
                System.out.println("Количество строк в файле: " + countlines);
                System.out.println("Запросы Googlebot: " + googleBotCount + " (" + (googleBotCount*100.0/countlines + "%)"));
                System.out.println("Запросы YandexBot: " + yandexBotCount + " (" + (yandexBotCount*100.0/countlines + "%)"));
            } catch (Exception ex){
                ex.printStackTrace();
            }
        }
    }
}
class LineTooLongException extends Exception{
    public LineTooLongException(String message) {
        super(message);
    }
}