import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        /*System.out.println("Введите текст и нажмите <Enter>: ");
        String text = new Scanner(System.in).nextLine();
        System.out.println("Длина текста: " + text.length());*/

         //запрашиваем путь к файлу
        int fileCount=0;
        String path; // вынесла инициализацию переменной (13.11)
        while (true) {
            System.out.println("Введите путь к файлу и нажмите <Enter>: ");
            path = new Scanner(System.in).nextLine();
            File file = new File(path);
            boolean fileExists = file.exists(); // проверка на существование файла и/или каталога
            boolean isDirectory = file.isDirectory(); // проверка на то, что это директория
            if (fileExists==true && isDirectory==false) {
                fileCount++;
                System.out.println("Путь указан верно." + "Это файл номер " + fileCount + ".");
                // #9 (новое от 13.11.2023)
                //C:\Users\MGerasimova\Desktop\Работа\Обучение\Файл\access.log
                try (FileReader fileReader = new FileReader(path)){
                    //FileReader fileReader = new FileReader(path); // класс для чтения файла
                    BufferedReader reader = new BufferedReader(fileReader); // сохранение содержимого файла (всего) в буфер для чтения
                    String line;
                    List<Integer> linelst=new ArrayList<>(); // массив для хранения размера строк
                    Statistics statistics=new Statistics(); //Statistics
                    while ((line = reader.readLine()) != null) { //readLine() - считает построчно(берет целую строку из буфера и возвращает ее как string)
                        int length = line.length();
                        linelst.add(length);
                        if (length>1024) {
                            throw new LenghtLineExceed1024Exception("Длина строки превышает 1024 символа");
                        };
                        //System.out.println(linelst.size());
                        LogEntry entry=new LogEntry(line);
                        statistics.addEntry(entry);
                    }
                    System.out.println("Общее количество строк в файле: " + linelst.size());
                    System.out.println("Общий траффик за час" + statistics.getTrafficRate());
                }
                catch (FileNotFoundException e){
                    e.printStackTrace();
                }
                catch (IOException ex){
                    ex.printStackTrace();
                } catch (LenghtLineExceed1024Exception e) {
                    throw new RuntimeException(e);
                }
            }
            else if (fileExists==true && isDirectory==true) {
                System.out.println("Путь указан к папке, а не к файлу"); continue;
            }
            else {
                System.out.println("Файл не существует или указанный путь не является путём к папке"); continue;
            }
        }
     }
    //метод, возврщающий наибольший размер из листа Integer
    public static int maxLenght (List <Integer> lst){
        int maxLenght=lst.get(0);
        for (int i = 1; i < lst.size(); i++) {
            if (lst.get(i) > maxLenght) maxLenght = lst.get(i);
        }
        return maxLenght;
    }
    //метод, возвращающий наибольший размер из листа Integer
    public static int minLenght (List <Integer> lst){
        int minLenght=lst.get(0);
        for (int i = 1; i < lst.size(); i++) {
            if (lst.get(i) < minLenght) minLenght = lst.get(i);
        }
        return minLenght;
    }
}
