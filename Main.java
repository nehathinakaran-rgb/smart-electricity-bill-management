
import java.util.Scanner;

public class Main {

    // Sample tariff rates for project demonstration.
    // These are not verified official billing rates.
    public static double calculateEnergyCharge(int units) {
        double charge = 0;

        if (units <= 0) {
            return 0;
        }

        // First 200 billable units
        int slab = Math.min(units, 200);
        charge += slab * 4.70;
        units -= slab;

        // Next 100 billable units
        slab = Math.min(units, 100);
        charge += slab * 6.30;
        units -= slab;

        // Next 100 billable units
        slab = Math.min(units, 100);
        charge += slab * 8.40;
        units -= slab;

        // Next 200 billable units
        slab = Math.min(units, 200);
        charge += slab * 9.45;
        units -= slab;

        // Next 200 billable units
        slab = Math.min(units, 200);
        charge += slab * 10.50;
        units -= slab;

        // Remaining units
        if (units > 0) {
            charge += units * 11.55;
        }

        return charge;
    }

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        System.out.println("====================================");
        System.out.println(" SMART ELECTRICITY BILL MANAGEMENT");
        System.out.println("====================================");

        System.out.print("Enter previous meter reading: ");

        if (!input.hasNextInt()) {
            System.out.println("Invalid input. Enter whole numbers.");
            input.close();
            return;
        }
        int previous = input.nextInt();

        System.out.print("Enter current meter reading: ");

        if (!input.hasNextInt()) {
            System.out.println("Invalid input. Enter whole numbers.");
            input.close();
            return;
        }
        int current = input.nextInt();

        if (previous < 0 || current < previous) {
            System.out.println("Invalid meter readings.");
            input.close();
            return;
        }

        System.out.print("Enter free-unit allowance: ");

        if (!input.hasNextInt()) {
            System.out.println("Invalid input. Enter whole numbers.");
            input.close();
            return;
        }
        int freeUnits = input.nextInt();

        if (freeUnits < 0) {
            System.out.println("Free units cannot be negative.");
            input.close();
            return;
        }

        int consumed = current - previous;
        int billable = Math.max(0, consumed - freeUnits);
        double charge = calculateEnergyCharge(billable);

        System.out.println();
        System.out.println("========== ELECTRICITY BILL ==========");
        System.out.println("Previous reading : " + previous);
        System.out.println("Current reading  : " + current);
        System.out.println("Units consumed   : " + consumed);
        System.out.println("Free units       : " + freeUnits);
        System.out.println("Billable units   : " + billable);
        System.out.printf("Estimated charge : Rs. %.2f%n", charge);
        System.out.println("--------------------------------------");
        System.out.println("Note: Sample tariff rates are used.");
        System.out.println("Fixed charges are not included.");
        System.out.println("======================================");

        input.close();
    }
}
