import java.util.List;
import java.util.function.Consumer;
import java.util.regex.Matcher;

class Stat {
    String name;
    double mean;
    double standardDeviation;
}

class Stats {
    private static String pattern = "(.*):(\\d*\\.?\\d*):(\\d*\\.?\\d*):";

    public static void readFile(String filename, Consumer<Stat> callback) {
        List<String> lines = Utility.readFile(filename);
        int number = Integer.parseInt(lines.get(0));
        for (int i = 1; i <= number; i++) {
            String line = lines.get(i);
            Matcher match = Utility.parseRegex(line, pattern);
            Stat event = createStat(match);
            callback.accept(event);
        }
    }

    private static Stat createStat(Matcher match) {
        return new Stat() {{
            name = match.group(1);
            mean = parseDouble(match.group(2));
            standardDeviation = parseDouble(match.group(3));
        }};
    }

    private static double parseDouble(String string) {
        boolean notBlank = string.length() > 0;
        return notBlank ? Double.parseDouble(string) : Double.MAX_VALUE;
    }
}
