import java.io.*;
import java.util.Scanner;

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
                int countlines = 0;
                int maxline = 0;
                int minline = 1024;
                while ((line = reader.readLine()) != null) {
                    int length = line.length();
                    if (length>maxline) maxline=length;
                    if (length<minline) minline=length;
                    if (length>1024) throw new LineTooLongException("Строка превышает максимальную длину: 1024 символа");
                    countlines++;
                }
                System.out.println("Количество строк в файле: " + countlines);
                System.out.println("Длина самой длинной строки: " + maxline);
                System.out.println("Длина самой короткой строки: " + minline);
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