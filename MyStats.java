import java.util.List;
import java.util.ArrayList;
import java.util.function.Consumer;
import java.util.regex.Matcher;

class MyStat {
    String name;
    double total;
    double mean;
    double standardDeviation;
}

class MyStats {
    public static void readFile(String filename, Consumer<MyStat> callback) {
        List<String> lines = Utility.readFile(filename);
        for (int i = 2; i < lines.size(); i++) {
            String line = lines.get(i);
            MyStat stat = parseLine(line);
            callback.accept(stat);
        }
    }

    public static void writeToFile(String filename, List<MyStat> stats) {
        System.out.printf("Writting statistics to '%s'...%n", filename);
        List<String> lines = new ArrayList<String>();
        
        String header = (" ").repeat(20) + String.format("%10s%10s%21s", "Total", "Mean", "Standard Deviation");
        lines.add(header);

        String horizontalLine = ("-").repeat(header.length());
        lines.add(horizontalLine);

        for (MyStat stat : stats) {
            String format = (stat.total % 1 == 0) ? "%10.0f" : "%10.2f";
            String line = String.format("%20s", stat.name);
            line += String.format(format, stat.total);
            line += String.format(format, stat.mean);
            line += String.format("%21.2f", stat.standardDeviation);
            lines.add(line);
        }

        Utility.writeFile(filename, lines);
    }

    private static MyStat parseLine(String line) {
        String eventName = line.substring(0, 20).trim();
        String pattern = "(.{10})(.{10})(.{21})";
        Matcher match = Utility.parseRegex(line.substring(20), pattern);

        return new MyStat() {{
            name = eventName;
            total = Double.parseDouble(match.group(1).trim());
            mean = Double.parseDouble(match.group(2).trim());
            standardDeviation = Double.parseDouble(match.group(3).trim());
        }};
    }
}
