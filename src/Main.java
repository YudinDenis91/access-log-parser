import java.io.File;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        int count=0;
        for ( ; ; ) {
            System.out.print("Путь к файлу: ");
            String path = new Scanner(System.in).nextLine();
            File file = new File(path);
            boolean fileExists = file.exists();
            boolean isDirectory = file.isDirectory();
            if (!fileExists) {
                System.out.println("Указанный файл не существует"); continue;
            }
            if (isDirectory){
                System.out.println("Указанный путь не является путем к файлу"); continue;
            }
            else System.out.println("Путь указан верно");
            count++;
            System.out.println("Это файл номер "+count);
        }
    }
}
