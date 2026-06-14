import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Encapsulates properties, labels, and consumption logic for an individual electrical sub-meter.
 */
class SubMeter {
    private final String meterId;
    private final String roomLabel;
    private int previousReading;
    private int currentReading;

    public SubMeter(String meterId, String roomLabel, int defaultReading) {
        this.meterId = meterId;
        this.roomLabel = roomLabel;
        this.previousReading = defaultReading;
        this.currentReading = defaultReading;
    }

    public String getMeterId() { return meterId; }
    public String getRoomLabel() { return roomLabel; }
    public int getPreviousReading() { return previousReading; }
    public int getCurrentReading() { return currentReading; }

    public void setPreviousReading(int previousReading) { this.previousReading = previousReading; }
    public void setCurrentReading(int currentReading) { this.currentReading = currentReading; }

    public int calculateUnits() {
        return currentReading - previousReading;
    }

    public String getFullName() {
        return "meter_" + meterId + "_" + roomLabel;
    }
}

/**
 * Orchestrates the sub-meter registries, file-based storage tracking, and structural multi-wing rules.
 */
class HostelBillingManager {
    private static final double RATE_PER_UNIT = 6.0;
    private final String wingName;
    private final String storageFile;
    private final List<SubMeter> meters;

    public HostelBillingManager(String wingName, String storageFile) {
        this.wingName = wingName;
        this.storageFile = storageFile;
        this.meters = new ArrayList<>();
    }

    public void registerMeter(SubMeter meter) {
        meters.add(meter);
    }

    /**
     * Loads historical meter readings from persistent disk storage files.
     */
    public void loadHistoricalData() {
        File file = new File(storageFile);
        if (!file.exists()) return; // Maintain default baseline sequences if the file hasn't been generated yet

        try (Scanner fileScanner = new Scanner(file)) {
            for (SubMeter meter : meters) {
                if (fileScanner.hasNextInt()) {
                    meter.setPreviousReading(fileScanner.nextInt());
                }
            }
        } catch (FileNotFoundException e) {
            System.err.println("[Storage Alert] Could not locate historical file for " + wingName + ". Tracking from defaults.");
        }
    }

    /**
     * Commits active computational states to disk storage for the subsequent billing cycle.
     */
    public void saveCurrentData() {
        try (PrintWriter writer = new PrintWriter(new File(storageFile))) {
            for (SubMeter meter : meters) {
                writer.println(meter.getCurrentReading());
            }
            System.out.println(">>> Persistent updates synced successfully to disk [" + storageFile + "].");
        } catch (IOException e) {
            System.err.println("[System Error] Critical file write failure encountered while saving records for " + wingName);
        }
    }

    /**
     * Executes Calculations for Hostel Wing A (7 Independent Rooms System)
     */
    public void processWingA(Scanner scanner) {
        System.out.println("\n==========================================================");
        System.out.println(" RUNTIME KERNEL ACTIVE: " + wingName.toUpperCase());
        System.out.println("==========================================================");
        loadHistoricalData();

        for (SubMeter m : meters) {
            System.out.print("Enter current reading for " + m.getFullName() + " (Prev: " + m.getPreviousReading() + "): ");
            m.setCurrentReading(scanner.nextInt());
        }

        System.out.println("\n----------------------------------------------------------");
        System.out.println("            OFFICIAL SUMMARY: " + wingName.toUpperCase());
        System.out.println("----------------------------------------------------------");
        for (SubMeter m : meters) {
            int units = m.calculateUnits();
            double netCost = units * RATE_PER_UNIT;
            System.out.printf(" Room Unit: %-25s | Total Units: %4d | Bill: Rs. %.2f\n", m.getFullName(), units, netCost);
        }
        System.out.println("----------------------------------------------------------");
        saveCurrentData();
    }

    /**
     * Executes Calculations for Hostel Wing B (8 Rooms + 3 Shared Heavy-Appliance Utility Transformers)
     */
    public void processWingB(Scanner scanner) {
        System.out.println("\n==========================================================");
        System.out.println(" RUNTIME KERNEL ACTIVE: " + wingName.toUpperCase());
        System.out.println("==========================================================");
        loadHistoricalData();

        int[] occupancyMap = new int[meters.size()];

        // Capture room population boundaries while avoiding industrial appliance nodes (Indices: 2, 3, and 8)
        for (int i = 0; i < meters.size(); i++) {
            if (i != 2 && i != 3 && i != 8) {
                System.out.print("Enter current resident occupancy for " + meters.get(i).getFullName() + ": ");
                occupancyMap[i] = scanner.nextInt();
            }
        }

        System.out.println();
        for (SubMeter m : meters) {
            System.out.print("Enter current reading for " + m.getFullName() + " (Prev: " + m.getPreviousReading() + "): ");
            m.setCurrentReading(scanner.nextInt());
        }

        // Shared Node Load Partitioning Computations
        int zone1Occupancy = occupancyMap[0] + occupancyMap[1] + occupancyMap[6];
        int geyser1SharedUnits = (zone1Occupancy > 0) ? meters.get(2).calculateUnits() / zone1Occupancy : 0;

        int zone2Occupancy = occupancyMap[4] + occupancyMap[5] + occupancyMap[9];
        int geyser2SharedUnits = (zone2Occupancy > 0) ? meters.get(3).calculateUnits() / zone2Occupancy : 0;

        int isolatedGeyserUnits = meters.get(8).calculateUnits();

        System.out.println("\n----------------------------------------------------------");
        System.out.println("            OFFICIAL SUMMARY: " + wingName.toUpperCase());
        System.out.println("----------------------------------------------------------");

        // Print Metrics for Zone 1 Compartments linked to Geyser 1
        int[] groupA = {0, 1, 6};
        for (int idx : groupA) {
            printSharedItemRow(idx, geyser1SharedUnits * occupancyMap[idx]);
        }

        // Print Metrics for Zone 2 Compartments linked to Geyser 2
        int[] groupB = {4, 5, 9};
        for (int idx : groupB) {
            printSharedItemRow(idx, geyser2SharedUnits * occupancyMap[idx]);
        }

        // Print Metrics for Room 6 Back linked to its isolated sub-geyser node
        printSharedItemRow(7, isolatedGeyserUnits);

        // Print Metrics for Standalone Room 8 Layout (Zero shared utility overhead)
        int independentUnits = meters.get(10).calculateUnits();
        System.out.printf(" Room Unit: %-25s | Base: %4d | Utility: %4d | Bill: Rs. %.2f\n",
                meters.get(10).getFullName(), independentUnits, 0, independentUnits * RATE_PER_UNIT);

        System.out.println("----------------------------------------------------------");
        saveCurrentData();
    }

    private void printSharedItemRow(int index, int assignedUtilityUnits) {
        SubMeter m = meters.get(index);
        int baseRoomUnits = m.calculateUnits();
        int cumulativeUnits = baseRoomUnits + assignedUtilityUnits;
        double overallCost = cumulativeUnits * RATE_PER_UNIT;

        System.out.printf(" Room Unit: %-25s | Base: %4d | Utility: %4d | Bill: Rs. %.2f\n",
                m.getFullName(), baseRoomUnits, assignedUtilityUnits, overallCost);
    }
}

/**
 * System Management Command Controller
 */
public class MainHostelBillingEngine {
    public static void main(String[] args) {
        Scanner inputScanner = new Scanner(System.in);

        // Setup Registry: Initialize Wing A Infrastructure
        HostelBillingManager wingA = new HostelBillingManager("Hostel Wing A (Standard)", "readings_wing_a.txt");
        wingA.registerMeter(new SubMeter("1243375", "room1r", 2003));
        wingA.registerMeter(new SubMeter("1243277", "room2m", 1732));
        wingA.registerMeter(new SubMeter("1269268", "room3l", 1566));
        wingA.registerMeter(new SubMeter("1243266", "room4r", 1454));
        wingA.registerMeter(new SubMeter("1243162", "room5m", 1060));
        wingA.registerMeter(new SubMeter("1243278", "room6l", 2086));
        wingA.registerMeter(new SubMeter("1243143", "room7", 916));

        // Setup Registry: Initialize Wing B Infrastructure
        HostelBillingManager wingB = new HostelBillingManager("Hostel Wing B (Advanced Shared)", "readings_wing_b.txt");
        wingB.registerMeter(new SubMeter("037353", "room1_baithak", 204));
        wingB.registerMeter(new SubMeter("290", "room2_back", 290));
        wingB.registerMeter(new SubMeter("1276081", "geyser1", 1776));
        wingB.registerMeter(new SubMeter("1276113", "geyser2", 518));
        wingB.registerMeter(new SubMeter("043850", "room3_hamam", 91));
        wingB.registerMeter(new SubMeter("1169394", "room4_kitchen", 2135));
        wingB.registerMeter(new SubMeter("1269323", "room5_baithak", 1784));
        wingB.registerMeter(new SubMeter("1276233", "room6_back", 1230));
        wingB.registerMeter(new SubMeter("1276322", "room6_backgeyser", 1728));
        wingB.registerMeter(new SubMeter("043818", "room7", 119));
        wingB.registerMeter(new SubMeter("1269458", "abv_kitchen_room8", 2195));

        while (true) {
            System.out.println("\n==========================================================");
            System.out.println("          CAMPUS HOSTEL ENERGY MANAGEMENT TERMINAL       ");
            System.out.println("==========================================================");
            System.out.println(" 1. Run Billing System: HOSTEL WING A (Rooms 1 - 7)");
            System.out.println(" 2. Run Billing System: HOSTEL WING B (Rooms 1 - 8 + Shared Utilities)");
            System.out.println(" 3. Safe Shutdown Application");
            System.out.print(" Enter Command Selection [1-3]: ");

            int selection;
            if (inputScanner.hasNextInt()) {
                selection = inputScanner.nextInt();
            } else {
                System.out.println("[Alert] Input type mismatch. Please provide numerical options only.");
                inputScanner.next(); // Flush invalid tokens from scanner buffer
                continue;
            }

            if (selection == 1) {
                wingA.processWingA(inputScanner);
            } else if (selection == 2) {
                wingB.processWingB(inputScanner);
            } else if (selection == 3) {
                System.out.println("Shutting down core resource components. Systems locked cleanly.");
                break;
            } else {
                System.out.println("[Alert] Choice out of bounds. Please choose within structural limits [1-3].");
            }
        }
        inputScanner.close();
    }
}