import java.util.regex.Matcher;

class Stat {
    String name;
    double mean;
    double standardDeviation;
}

class Stats extends Models<Stat> {
    public Stats(String filename) {
        super(filename);
    }

    @Override
    protected String getPattern() {
        return "(.*):(\\d*\\.?\\d*):(\\d*\\.?\\d*):";
    }

    @Override
    protected Stat createItem(Matcher match) {
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
