import java.util.List;
import java.util.Scanner;
import java.util.ArrayList;

class IDS {
    String originalEventsFile;
    final String baseEventsFile = "base-events.txt";
    final String baseStatsFile = "base-stats.txt";

    private IDS(String eventsFile, String statsFile, int days) {
        originalEventsFile = eventsFile;

        // Generate baseline events from stats file.
        ActivityEngine actEngine = new ActivityEngine(eventsFile, statsFile, days);
        List<MyEvent> events = new ArrayList<MyEvent>();
        actEngine.generateActivities(event -> events.add(event));
        MyEvents.writeToFile(baseEventsFile, events);

        // Generate baseline stats from baseline events.
        AnalysisEngine analEngine = new AnalysisEngine(baseEventsFile);
        List<MyStat> stats = new ArrayList<MyStat>();
        analEngine.generateStatistics(stat -> stats.add(stat));
        MyStats.writeToFile(baseStatsFile, stats);

        prompt();
    }

    private void process(String statsFile, int days) {
        // Generate events from stats file.
        ActivityEngine actEngine = new ActivityEngine(originalEventsFile, statsFile, days);
        List<MyEvent> events = new ArrayList<MyEvent>();
        actEngine.generateActivities(event -> events.add(event));

        // Original events file.
        List<Event> originalEvents = actEngine.events;

        // Baseline stats.
        List<MyStat> baseStats = new ArrayList<MyStat>();
        MyStats.readFile(baseStatsFile, stat -> baseStats.add(stat));

        // Alert Engine
        AlertEngine altEngine = new AlertEngine(originalEvents, baseStats, events);
        List<Alert> alerts = new ArrayList<Alert>();
        altEngine.calculateAnomaly(alarm -> alerts.add(alarm));

        // Display the results.
        Utility.reportAlerts(alerts);
    }

    private void prompt() {
        try (Scanner input = new Scanner(System.in);) {
            while (true) {
                System.out.println("\n[1] Enter a file.\n[2] Close Program.");
                System.out.print("\nSelect an option: ");
                int option = input.nextInt(); input.nextLine();
                switch (option) {
                    case 1:
                        System.out.print("Enter live data source file: ");
                        String statsFile = input.nextLine();
                        System.out.print("Enter number of days: ");
                        int days = input.nextInt(); input.nextLine();
                        process(statsFile, days);
                        break;
                    case 2:
                        System.out.println("Bye bye!");
                        System.exit(0);
                        break;
                    default:
                        System.out.println("Invalid input.");
                        break;
                }
            }
        }
        catch (Exception exception) {
            System.out.println("Invalid input.");
            System.exit(1);
        }
    }

    public static void main(String[] args) {
        try {
            if (args.length != 3) {
                throw new IllegalArgumentException();
            }
            int days = Integer.parseInt(args[2]);
            new IDS(args[0], args[1], days);
        }
        catch (Exception exception) {
            System.out.println("Please run with the arguments like 'IDS <Event.txt> <Stats.txt> <Days>'");
            System.exit(1);
        }
    }
}
