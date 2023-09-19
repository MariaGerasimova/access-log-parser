import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        System.out.println("Введите первое число:");
        int number1= new Scanner(System.in).nextInt();
        System.out.println("Введите второе число отличное от 0:");
        int number2= new Scanner(System.in).nextInt();
        //сумма
        int sum=number1+number2;
        System.out.println("Сумма двух чисел = " + sum);
        //разность
        int diff=number1-number2;
        System.out.println("Разность двух чисел = " + diff);
        //Произведение
        int miltiplic=number1*number2;
        System.out.println("Произведение двух чисел = " + miltiplic);
        //Частное
        double quetient=(double) number1/number2;
        System.out.println("Частное двух чисел= " + quetient);
    }
}
