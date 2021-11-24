import java.util.List;
import java.util.ArrayList;

class AnalysisEngine {
    List<Activity> activities;
    List<Stat> stats;

    public AnalysisEngine(String baseDataFilename) {
        System.out.printf("Reading events from '%s'...%n", baseDataFilename);
        activities = new ArrayList<Activity>();
        Activities.readFile(baseDataFilename, activity -> activities.add(activity));
        
        System.out.println("Calculating each events' statistics...");
        // todo
    }
}

// TODO: Analysis Engine: Measure the events data and determine the statistics associated with the baseline.
// - Produce totals for each event for each day; store it in a file.
// - Determine the mean and standard deviation associated with that event across that data.
