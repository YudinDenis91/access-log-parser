import java.io.*;
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
                Pattern pattern = Pattern.compile("^(\\S+) \\S+ \\S+ \\[([\\w:/]+\\s[+\\-]\\d{4})\\] \"(\\S+ \\S+ \\S+)\" (\\d{3}) (\\d+) \"(.*?)\" \"(.*?)\"$");
                while ((line = reader.readLine()) != null) {
                    int length = line.length();
                    if (length>1024) throw new LineTooLongException("Строка превышает максимальную длину: 1024 символа");
                    countlines++;
                    Matcher matcher = pattern.matcher(line);
                    if (matcher.matches()) {
                        String ip = matcher.group(1);
                        String dateTime = matcher.group(2);
                        String request = matcher.group(3);
                        String statusCode = matcher.group(4);
                        String bytesSent = matcher.group(5);
                        String referer = matcher.group(6);
                        String userAgent = matcher.group(7);
                        if (userAgent.contains("Googlebot")) {
                            googleBotCount++;
                        } else if (userAgent.contains("YandexBot")) {
                            yandexBotCount++;
                        }
                    }
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