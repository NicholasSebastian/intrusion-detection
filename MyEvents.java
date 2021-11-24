import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.function.Consumer;

class MyEvent {
    String name;
    double[] values;
}

class MyEvents {
    public static void readFile(String filename, Consumer<MyEvent> callback) {
        System.out.printf("Reading events from '%s'...%n", filename);
        List<String> lines = Utility.readFile(filename);
        for (int i = 2; i < lines.size(); i++) {
            String line = lines.get(i);
            MyEvent event = parseLine(line);
            callback.accept(event);
        }
    }
    
    public static void writeToFile(String filename, List<MyEvent> events) {
        System.out.printf("Writing events to '%s'...%n", filename);
        List<String> lines = new ArrayList<String>();

        String header = getHeader(events.get(0).values.length);
        lines.add(header);

        String horizontalLine = ("-").repeat(header.length());
        lines.add(horizontalLine);

        for (MyEvent event : events) {
            String line = String.format("%20s", event.name);
            for (double value : event.values) {
                String format = (value % 1 == 0) ? "%10.0f" : "%10.2f";
                line += String.format(format, value);
            }
            lines.add(line);
        }

        Utility.writeFile(filename, lines);
    }

    private static MyEvent parseLine(String line) {
        String eventName = line.substring(0, 20).trim();
        double[] dayValues = Arrays.stream(line.substring(20).split("\\s+"))
            .skip(1)
            .mapToDouble(Double::parseDouble)
            .toArray();

        return new MyEvent() {{
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
