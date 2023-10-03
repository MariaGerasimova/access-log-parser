import com.sun.source.tree.WhileLoopTree;

import java.io.File;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        /*System.out.println("Введите текст и нажмите <Enter>: ");
        String text = new Scanner(System.in).nextLine();
        System.out.println("Длина текста: " + text.length());*/

         //запрашиваем путь к файлу
        int fileCount=0;
        while (true) {
            System.out.println("Введите путь к файлу и нажмите <Enter>: ");
            String path = new Scanner(System.in).nextLine();
            File file = new File(path);
            boolean fileExists = file.exists(); // проверка на существование файла и/или каталога
            boolean isDirectory = file.isDirectory(); // проверка на то, что это дирекция
            if (fileExists==true && isDirectory==false) {
                fileCount++;
                System.out.println("Путь указан верно." + "Это файл номер " + fileCount + ".");
            }
            else if (fileExists==true && isDirectory==true) {
                System.out.println("Путь указан к папке, а не к файлу"); continue;
            }
            else {
                System.out.println("Файл не существует или указанный путь не является путём к папке"); continue;
            }
        }
    }
}
