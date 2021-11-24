class IDS {
    static String baseDataFilename = "events-log.txt";

    public static void main(String[] args) {
        System.out.println("ACTIVITY ENGINE");
        new ActivityEngine(args, baseDataFilename);
        
        System.out.println("\nANALYSIS ENGINE");
        new AnalysisEngine(baseDataFilename);
        
        System.out.println("\nALERT ENGINE");
        new AlertEngine();
    }
}
