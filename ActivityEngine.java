import java.util.ArrayList;
import java.util.Random;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

class ActivityEngine {
    Events events;
    Stats stats;
    double[][] activities;
    int days;

    public ActivityEngine(String[] args) {
        System.out.println("Reading input files...");
        loadEventStats(args);

        System.out.println("Generating events by normal distribution...");
        loadActivities();
        System.out.println("Events generated successfully!");

        String filename = "events-log.txt";
        System.out.printf("Writing events to '%s'...%n", filename);
        logEventsToFile(filename);
        System.out.println("Events written successfully!");
    }

    private void loadEventStats(String[] args) {
        try {
            if (args.length != 3) {
                throw new IllegalArgumentException();
            }
            events = new Events(args[0]);
            stats = new Stats(args[1]);
            days = Integer.parseInt(args[2]);
        }
        catch (Exception error) {
            System.out.println("Please run with the arguments like 'IDS <Event.txt> <Stats.txt> <Days>'");
            System.exit(1);
        }
    }

    private void loadActivities() {
        Random random = new Random();
        activities = new double[days][];
        for (int i = 0; i < days; i++) {
            double[] day = new double[stats.items.size()];
            for (int j = 0; j < stats.items.size(); j++) {
                Stat stat = stats.items.get(j);
                double value = random.nextGaussian() * stat.standardDeviation + stat.mean;
                day[j] = value;
            }
            activities[i] = day;
        }
    }

    private void logEventsToFile(String filename) {
        ArrayList<String> lines = new ArrayList<String>();

        String header = (" ").repeat(20);
        for (int i = 1; i <= days; i++) {
            header += String.format("%10s", "Day " + i);
        }

        String horizontalLine = ("-").repeat(header.length());
        lines.add(header);
        lines.add(horizontalLine);
        
        for (int i = 0; i < events.items.size(); i++) {
            Event event = events.items.get(i);
            String format = event.type == EventType.Continuous ? "%10.2f" : "%10.0f";

            String line = String.format("%20s", event.name);
            for (int j = 0; j < days; j++) {
                double[] day = activities[j];
                double value = day[i];
                line += String.format(format, value);
            }
            lines.add(line);
        }
        try {
            Path path = Paths.get(filename);
            Files.write(path, lines);
        }
        catch (IOException exception) {
            System.out.println("Something went wrong when attempting to write events to file.");
            System.exit(1);
        }
    }
}
