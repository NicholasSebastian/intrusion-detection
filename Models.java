import java.util.List;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

abstract class Models<T> {
    public List<T> items;
    protected abstract String getPattern();
    protected abstract T createItem(Matcher match);

    public Models(String filename) {
        items = new ArrayList<T>();
        readFile(filename);
    }

    private void readFile(String filename) {
        try {
            Path path = Paths.get(filename);
            List<String> lines = Files.readAllLines(path);
            int number = Integer.parseInt(lines.get(0));

            for (int i = 0; i < number; i++) {
                String line = lines.get(i + 1);
                T item = parseLine(line);
                items.add(item);
            }
        }
        catch (Exception exception) {
            System.out.printf("Unable to process '%s'%n", filename);
            System.exit(1);
        }
    }

    private T parseLine(String line) {
        String pattern = getPattern();
        Pattern regex = Pattern.compile(pattern);
        Matcher match = regex.matcher(line);
        try {
            if (match.find()) 
                return createItem(match);
            else 
                throw new IllegalArgumentException();
        }
        catch (Exception exception) {
            System.out.printf("Unable to parse line '%s'%n", line);
            System.exit(1);
        }
        return null;
    }
}
