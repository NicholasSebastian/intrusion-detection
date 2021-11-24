import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Consumer;

class AnalysisEngine {
    List<MyEvent> events;

    public AnalysisEngine(String baseDataFilename) {
        System.out.println("\nANALYSIS ENGINE");
        events = new ArrayList<MyEvent>();
        MyEvents.readFile(baseDataFilename, event -> events.add(event));
    }

    public void generateStatistics(Consumer<MyStat> callback) {
        System.out.println("Calculating each events' statistics...");
        for (MyEvent event : events) {
            double sum = Arrays.stream(event.values).sum();
            double average = sum / event.values.length;
            double variance = Arrays.stream(event.values)
                .map(val -> Math.pow(val - average, 2))
                .sum() / event.values.length;
            double sd = Math.sqrt(variance);

            MyStat stat = new MyStat() {{
                name = event.name;
                total = sum;
                mean = average;
                standardDeviation = sd;
            }};
            callback.accept(stat);
        }
    }
}
