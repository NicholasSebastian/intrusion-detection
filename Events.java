import java.util.regex.Matcher;

class Event {
    String name;
    EventType type;
    int minimum;
    int maximum;
    int weight;
}

class Events extends Models<Event> {
    public Events(String filename) {
        super(filename);
    }

    @Override
    protected String getPattern() {
        return "(.*):([CD]):(\\d*):(\\d*):(\\d*):";
    }

    @Override
    protected Event createItem(Matcher match) {
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
