import java.util.Scanner;

/**
 * Smart Electricity Consumption and Bill Management System
 * Console-based Java component for the project.
 *
 * IMPORTANT:
 * The rates below are DEMONSTRATION rates, not a claim of the current
 * official Tamil Nadu tariff. Verify and update them against the applicable
 * TANGEDCO/TNERC tariff order before using this for real bills.
 */
public class Main {
    // Demonstration slab rates in rupees per unit. Update from official tariff.
    private static final double[] SLAB_LIMITS = {100, 200, 300, 400, 500, Double.POSITIVE_INFINITY};
    private static final double[] SLAB_RATES  = {0.0, 2.25, 4.50, 6.00, 8.00, 10.00};

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("==============================================");
        System.out.println(" SMART ELECTRICITY BILL MANAGEMENT SYSTEM");
        System.out.println("==============================================");

        System.out.print("Enter consumer name: ");
        String consumerName = scanner.nextLine().trim();
        if (consumerName.isEmpty()) {
            consumerName = "Customer";
        }

        System.out.print("Enter consumer / service number: ");
        String serviceNumber = scanner.nextLine().trim();
        if (serviceNumber.isEmpty()) {
            serviceNumber = "Not provided";
        }

        double previousReading = readNonNegativeDouble(scanner, "Enter previous meter reading (units): ");
        double currentReading;
        do {
            currentReading = readNonNegativeDouble(scanner, "Enter current meter reading (units): ");
            if (currentReading < previousReading) {
                System.out.println("Current reading must be greater than or equal to previous reading.");
            }
        } while (currentReading < previousReading);

        double unitsConsumed = currentReading - previousReading;
        double energyCharge = calculateEnergyCharge(unitsConsumed);

        System.out.print("Enter fixed charge in rupees (enter 0 if not applicable): ");
        double fixedCharge = readNonNegativeDouble(scanner, "");

        System.out.print("Enter other charges in rupees (enter 0 if none): ");
        double otherCharges = readNonNegativeDouble(scanner, "");

        double total = energyCharge + fixedCharge + otherCharges;

        System.out.println();
        System.out.println("--------------- ELECTRICITY BILL -------------");
        System.out.printf("Consumer name       : %s%n", consumerName);
        System.out.printf("Service number      : %s%n", serviceNumber);
        System.out.printf("Previous reading    : %.2f units%n", previousReading);
        System.out.printf("Current reading     : %.2f units%n", currentReading);
        System.out.printf("Units consumed      : %.2f units%n", unitsConsumed);
        System.out.printf("Energy charge       : Rs. %.2f%n", energyCharge);
        System.out.printf("Fixed charge        : Rs. %.2f%n", fixedCharge);
        System.out.printf("Other charges       : Rs. %.2f%n", otherCharges);
        System.out.println("-----------------------------------------------");
        System.out.printf("TOTAL ESTIMATED BILL: Rs. %.2f%n", total);
        System.out.println("-----------------------------------------------");
        System.out.println("Note: Demonstration tariff rates are used.");
        System.out.println("Verify current Tamil Nadu tariff/subsidy rules");
        System.out.println("before treating this estimate as an official bill.");
        System.out.println("This Java console program runs separately from");
        System.out.println("the HTML dashboard unless a backend is added.");
        scanner.close();
    }

    private static double calculateEnergyCharge(double units) {
        double charge = 0.0;
        double lowerLimit = 0.0;

        for (int i = 0; i < SLAB_LIMITS.length; i++) {
            if (units <= lowerLimit) {
                break;
            }
            double unitsInSlab = Math.min(units, SLAB_LIMITS[i]) - lowerLimit;
            if (unitsInSlab > 0) {
                charge += unitsInSlab * SLAB_RATES[i];
            }
            lowerLimit = SLAB_LIMITS[i];
        }
        return charge;
    }

    private static double readNonNegativeDouble(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            try {
                double value = Double.parseDouble(input);
                if (Double.isFinite(value) && value >= 0) {
                    return value;
                }
            } catch (NumberFormatException ignored) {
                // Show the validation message below.
            }
            System.out.println("Please enter a valid number that is 0 or greater.");
        }
    }
}
