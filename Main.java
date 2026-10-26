
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("==================================");
        System.out.println(" SMART ELECTRICITY BILL MANAGEMENT");
        System.out.println("==================================");

        System.out.print("Enter previous meter reading: ");
        double previousReading = scanner.nextDouble();

        System.out.print("Enter current meter reading: ");
        double currentReading = scanner.nextDouble();

        if (previousReading < 0 ||
            currentReading < previousReading) {
            System.out.println("Invalid meter readings.");
        } else {
            double unitsConsumed =
                    currentReading - previousReading;

            System.out.println("------------------------------");
            System.out.println("Previous Reading: " + previousReading);
            System.out.println("Current Reading: " + currentReading);
            System.out.println("Units Consumed: " + unitsConsumed);
            System.out.println("------------------------------");
        }

        scanner.close();
    }
}
