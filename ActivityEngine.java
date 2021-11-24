import java.util.List;
import java.util.ArrayList;
import java.util.Random;

class ActivityEngine {
    List<Event> events;
    List<Stat> stats;
    List<Activity> activities;
    int days;

    public ActivityEngine(String[] args, String baseDataFilename) {
        events = new ArrayList<Event>();
        stats = new ArrayList<Stat>();

        System.out.println("Reading input files...");
        loadEventStats(args);

        System.out.println("Generating events by normal distribution...");
        loadActivities();
        System.out.println("Events generated successfully!");

        System.out.printf("Writing events to '%s'...%n", baseDataFilename);
        Activities.writeToFile(baseDataFilename, activities);
        System.out.println("Events written successfully!");
    }

    private void loadEventStats(String[] args) {
        try {
            if (args.length != 3) {
                throw new IllegalArgumentException();
            }
            Events.readFile(args[0], event -> events.add(event));
            Stats.readFile(args[1], stat -> stats.add(stat));
            days = Integer.parseInt(args[2]);
        }
        catch (Exception error) {
            System.out.println("Please run with the arguments like 'IDS <Event.txt> <Stats.txt> <Days>'");
            System.exit(1);
        }
    }

    private void loadActivities() {
        Random random = new Random();
        activities = new ArrayList<Activity>();
        for (Stat stat : stats) {
            double[] vals = new double[days];
            for (int i = 0; i < days; i++) {
                double value = random.nextGaussian() * stat.standardDeviation + stat.mean;
                vals[i] = value;
            }
            Activity activity = new Activity() {{ name = stat.name; values = vals; }};
            activities.add(activity);
        }
    }
}
