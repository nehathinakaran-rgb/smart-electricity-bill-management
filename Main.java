
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        System.out.println("======================================");
        System.out.println(" SMART ELECTRICITY BILL MANAGEMENT");
        System.out.println("======================================");

        System.out.print("Enter previous meter reading: ");
        double previous = input.nextDouble();

        System.out.print("Enter current meter reading: ");
        double current = input.nextDouble();

        if (previous < 0 || current < previous) {
            System.out.println("Invalid meter readings.");
            input.close();
            return;
        }

        double units = current - previous;

        System.out.println("\nUnits consumed: " + units);

        System.out.print("Enter free units allowance: ");
        double freeUnits = input.nextDouble();

        if (freeUnits < 0) {
            System.out.println("Invalid free units allowance.");
            input.close();
            return;
        }

        double billableUnits = Math.max(0, units - freeUnits);
        double estimatedBill = calculateEnergyCharge(billableUnits);

        System.out.println("\n========== BILL SUMMARY ==========");
        System.out.println("Previous reading : " + previous);
        System.out.println("Current reading  : " + current);
        System.out.println("Units consumed   : " + units);
        System.out.println("Free units       : " + freeUnits);
        System.out.println("Billable units   : " + billableUnits);
        System.out.printf("Estimated energy charge: Rs. %.2f%n",
                estimatedBill);
        System.out.println("==================================");
        System.out.println("Note: Fixed charges and official");
        System.out.println("tariff rules are not included yet.");

        input.close();
    }

    public static double calculateEnergyCharge(double units) {
        double charge = 0;

        if (units > 0) {
            charge += Math.min(units, 100) * 0;
        }
        if (units > 100) {
            charge += Math.min(units - 100, 300) * 4.70;
        }
        if (units > 400) {
            charge += Math.min(units - 400, 100) * 6.30;
        }
        if (units > 500) {
            charge += Math.min(units - 500, 100) * 8.40;
        }
        if (units > 600) {
            charge += Math.min(units - 600, 200) * 9.45;
        }
        if (units > 800) {
            charge += (units - 800) * 11.55;
        }

        return charge;
    }
}
