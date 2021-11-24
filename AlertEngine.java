import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

class Alert {
    double anomaly;
    boolean alarm;
}

class AlertEngine {
    List<Event> baseEvents;
    List<MyStat> baseStats;
    List<MyEvent> events;

    public AlertEngine(List<Event> baseEvents, List<MyStat> baseStats, List<MyEvent> events) {
        System.out.println("\nALERT ENGINE");
        this.baseEvents = baseEvents;
        this.baseStats = baseStats;
        this.events = events;
    }

    public void calculateAnomaly(Consumer<Alert> callback) {
        System.out.println("Calculating anomalies...");
        int days = events.get(0).values.length;
        double[][] anomalies = new double[days][events.size()];
        for (int i = 0; i < events.size(); i++) {
            MyEvent event = events.get(i);
            Event data = findEvent(event.name);
            MyStat stat = findStat(event.name);
            for (int j = 0; j < days; j++) {
                double value = event.values[j];
                double anomaly = Math.abs(value - stat.mean) / stat.standardDeviation;
                double weightedAnomaly = anomaly * data.weight;
                anomalies[j][i] = weightedAnomaly;
            }
        }

        double threshold = baseEvents.stream().mapToDouble(e -> e.weight).sum() * 2;
        System.out.printf("Threshold: %.2f%n", threshold);

        for (double[] events : anomalies) {
            double sum = Arrays.stream(events).sum();
            Alert alert = new Alert() {{
                anomaly = sum;
                alarm = sum >= threshold;
            }};
            callback.accept(alert);
        }
    }

    private Event findEvent(String name) {
        return baseEvents.stream()
            .filter(event -> event.name.equals(name))
            .findFirst()
            .get();
    }

    private MyStat findStat(String name) {
        return baseStats.stream()
            .filter(stat -> stat.name.equals(name))
            .findFirst()
            .get();
    }
}
