import java.util.List;

public class Studio {
    private final String shortName;
    private final String longName;

    private Integer total = 0;
    private Integer previous = 0;
    private Integer current = 0;

    public Studio(String shortName, String longName) {
        this.shortName = shortName;
        this.longName = longName;
    }

    public static boolean process(String command, List<Studio> studios) {
        String[] tokens = command.split(",");

        switch (tokens[0]) {
            case "create_studio":
                if (tokens.length != 3) {
                    Logger.println("Expected 2 parameters. Received: " + (tokens.length - 1));

                    return false;
                }

                return create(studios, tokens[1], tokens[2]);

            case "display_studio":
                if (tokens.length != 2) {
                    Logger.println("Expected 1 parameter. Received: " + (tokens.length - 1));

                    return false;
                }

                return display(studios, tokens[1]);
        }

        Logger.println("Unrecognized command received: " + tokens[0]);

        return false;
    }

    private static boolean create(List<Studio> studios, String shortName, String longName) {
        boolean invalid = false;
        boolean success = false;

        if (studios.stream().anyMatch(x -> x.getShortName().equals(shortName))) {
            Logger.println("A Studio with the same short name already exists.");

            invalid = true;
        }

        if (shortName.trim().isEmpty()) {
            Logger.println("The short name for the Studio is missing.");

            invalid = true;
        }

        if (longName.trim().isEmpty()) {
            Logger.println("The long name for the Studio is missing.");

            invalid = true;
        }

        if (!invalid) {
            studios.add(new Studio(shortName, longName));

            success = true;
        }

        return success;
    }

    private static boolean display(List<Studio> studios, String studio) {
        Studio creator = studios.stream().filter(x -> x.getShortName().equals(studio))
                .findFirst()
                .orElse(null);

        if (creator != null) {
            Logger.println(String.join(",", "studio", creator.getShortName(), creator.getLongName()),
                    false);
            Logger.println("current_period," + creator.getCurrentRevenue(), false);
            Logger.println("previous_period," + creator.getPreviousRevenue(), false);
            Logger.println("total," + creator.getTotalRevenue(), false);

            return true;
        } else {
            Logger.println("No such Studio exists.");
        }

        return false;
    }

    public void collect(Integer amount) {
        current += amount;
    }

    public void nextMonth() {
        total += current;

        previous = current;

        current = 0;
    }

    public Integer getCurrentRevenue() {
        return current;
    }

    public Integer getPreviousRevenue() {
        return previous;
    }

    public Integer getTotalRevenue() {
        return total;
    }

    public String getLongName() {
        return longName;
    }

    public String getShortName() {
        return shortName;
    }
}
