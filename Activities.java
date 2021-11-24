import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.function.Consumer;

class Activity {
    String name;
    double[] values;
}

class Activities {
    public static void readFile(String filename, Consumer<Activity> callback) {
        List<String> lines = Utility.readFile(filename);
        for (int i = 2; i < lines.size(); i++) {
            String line = lines.get(i);
            Activity activity = parseLine(line);
            callback.accept(activity);
        }
    }
    
    public static void writeToFile(String filename, List<Activity> activities) {
        List<String> lines = new ArrayList<String>();

        String header = getHeader(activities.get(0).values.length);
        lines.add(header);

        String horizontalLine = ("-").repeat(header.length());
        lines.add(horizontalLine);

        for (Activity activity : activities) {
            String line = String.format("%20s", activity.name);
            for (double value : activity.values) {
                String format = (value % 1 == 0) ? "%10.0f" : "%10.2f";
                line += String.format(format, value);
            }
            lines.add(line);
        }

        Utility.writeFile(filename, lines);
    }

    private static Activity parseLine(String line) {
        String eventName = line.substring(0, 20).trim();
        double[] dayValues = Arrays.stream(line.substring(20).split("\\s+"))
            .skip(1)
            .mapToDouble(Double::parseDouble)
            .toArray();

        return new Activity() {{
            name = eventName;
            values = dayValues;
        }};
    }

    private static String getHeader(int length) {
        String header = (" ").repeat(20);
        for (int i = 1; i <= length; i++) {
            header += String.format("%10s", "Day " + i);
        }
        return header;
    }
}
