import java.util.List;
import java.util.function.Consumer;
import java.util.regex.Matcher;

class Event {
    String name;
    EventType type;
    int minimum;
    int maximum;
    int weight;
}

class Events {
    private static String pattern = "(.*):([CD]):(\\d*):(\\d*):(\\d*):";

    public static void readFile(String filename, Consumer<Event> callback) {
        List<String> lines = Utility.readFile(filename);
        int number = Integer.parseInt(lines.get(0));
        for (int i = 1; i <= number; i++) {
            String line = lines.get(i);
            Matcher match = Utility.parseRegex(line, pattern);
            Event event = createEvent(match);
            callback.accept(event);
        }
    }

    private static Event createEvent(Matcher match) {
        return new Event() {{
            name = match.group(1);
            type = parseType(match.group(2));
            minimum = parseInt(match.group(3));
            maximum = parseInt(match.group(4));
            weight = parseInt(match.group(5));
        }};
    }

    private static int parseInt(String string) {
        boolean notBlank = string.length() > 0;
        return notBlank ? Integer.parseInt(string) : Integer.MAX_VALUE;
    }

    private static EventType parseType(String type) throws IllegalArgumentException {
        switch (type) {
            case "C":
                return EventType.Continuous;
            case "D":
                return EventType.Discrete;
            default:
                throw new IllegalArgumentException();
        }
    }
}

enum EventType { Continuous, Discrete }
