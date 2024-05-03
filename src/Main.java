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
                int countlines=0;
                int length=0;
                int maxline = length;
                int minline = 1024;
                while ((line = reader.readLine()) != null) {
                    length = line.length();
                    if (length>maxline) maxline=length;
                    if (length<minline) minline=length;
                    if (length>1024) throw new IllegalArgumentException("Найдена очень длинная строка, длина больше 1024 символов");
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