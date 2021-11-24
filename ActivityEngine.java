import java.util.List;
import java.util.ArrayList;
import java.util.Random;
import java.util.function.Consumer;

class ActivityEngine {
    List<Event> events;
    List<Stat> stats;
    int days;

    public ActivityEngine(String eventsFile, String statsFile, int days) {
        System.out.println("\nACTIVITY ENGINE");
        this.events = new ArrayList<Event>();
        this.stats = new ArrayList<Stat>();
        this.days = days;

        System.out.println("Reading input files...");
        Events.readFile(eventsFile, event -> events.add(event));
        Stats.readFile(statsFile, stat -> stats.add(stat));
    }

    public void generateActivities(Consumer<MyEvent> callback) {
        System.out.println("Generating events by normal distribution...");
        Random random = new Random();
        for (Stat stat : stats) {
            EventType type = events.stream()
                .filter(e -> e.name.equals(stat.name))
                .findFirst()
                .get().type;
            
            double[] vals = new double[days];
            for (int i = 0; i < days; i++) {
                double value = random.nextGaussian() * stat.standardDeviation + stat.mean;
                vals[i] = (type == EventType.Discrete) ? Math.round(value) : value;
            }
            
            MyEvent event = new MyEvent() {{ name = stat.name; values = vals; }};
            callback.accept(event);
        }
    }
}
