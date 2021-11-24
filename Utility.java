import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

class Utility {
    public static List<String> readFile(String filename) {
        try {
            Path path = Paths.get(filename);
            List<String> lines = Files.readAllLines(path);
            return lines;
        }
        catch (Exception exception) {
            System.out.printf("Unable to process '%s'%n", filename);
            System.exit(1);
        }
        return null;
    }

    public static void writeFile(String filename, List<String> lines) {
        try {
            Path path = Paths.get(filename);
            Files.write(path, lines);
        }
        catch (IOException exception) {
            System.out.println("Something went wrong when attempting to write events to file.");
            System.exit(1);
        }
    }

    public static Matcher parseRegex(String line, String pattern) {
        Pattern regex = Pattern.compile(pattern);
        Matcher match = regex.matcher(line);
        try {
            if (match.find()) 
                return match;
            else 
                throw new IllegalArgumentException();
        }
        catch (Exception exception) {
            System.out.printf("Unable to parse line '%s'%n", line);
            System.exit(1);
        }
        return null;
    }

    public static void reportAlerts(List<Alert> alerts) {
        String header = (" ").repeat(10) + String.format("%10s%10s", "Anomaly", "Alarm");
        System.out.println(header);

        String horizontalLine = ("-").repeat(header.length());
        System.out.println(horizontalLine);

        for (int i = 0; i < alerts.size(); i++) {
            Alert alert = alerts.get(i);
            String day = "Day " + (i + 1);
            String alarm = alert.alarm ? "Yes" : "No";
            System.out.printf("%10s%10.2f%10s%n", day, alert.anomaly, alarm);
        }
    }
}
