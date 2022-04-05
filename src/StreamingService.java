import java.util.List;

public class StreamingService {
    private final String shortName;
    private String longName;

    private Integer subscription;

    private Boolean accessed = false;

    private Integer total = 0;
    private Integer previous = 0;
    private Integer current = 0;
    private Integer licensing = 0;

    public StreamingService(String shortName, String longName, Integer subscription) {
        this.shortName = shortName;
        this.longName = longName;
        this.subscription = subscription;
    }

    public static boolean process(String command, List<StreamingService> services) {
        String[] tokens = command.split(",");

        switch (tokens[0]) {
            case "create_stream":
                if (tokens.length != 4) {
                    Logger.println("Expected 3 parameters. Received: " + (tokens.length - 1));

                    return false;
                }

                return create(services, tokens[1], tokens[2], tokens[3]);

            case "display_stream":
                if (tokens.length != 2) {
                    Logger.println("Expected 1 parameter. Received: " + (tokens.length - 1));

                    return false;
                }

                return display(services, tokens[1]);

            case "update_stream":
                if (tokens.length != 4) {
                    Logger.println("Expected 3 parameters. Received: " + (tokens.length - 1));

                    return false;
                }

                return update(services, tokens[1], tokens[2], tokens[3]);
        }

        Logger.println("Unrecognized command received: " + tokens[0]);

        return false;
    }

    private static boolean create(List<StreamingService> services, String shortName, String longName,
                                  String subscription) {
        boolean success = false;

        try {
            boolean invalid = false;

            int cost = Integer.parseInt(subscription);

            if (services.stream().anyMatch(x -> x.getShortName().equals(shortName))) {
                Logger.println("Stream Service with the short name " + shortName + " already exists.");

                invalid = true;
            }

            if (shortName.trim().isEmpty()) {
                Logger.println("The short name of the streaming service is missing.");

                invalid = true;
            }

            if (longName.trim().isEmpty()) {
                Logger.println("The long name of the streaming service is missing.");

                invalid = true;
            }

            if (cost < 0) {
                Logger.println("The subscription fee must be a positive integer value.");

                invalid = true;
            }

            if (!invalid) {
                services.add(new StreamingService(shortName, longName, cost));

                success = true;
            }
        } catch (Exception e) {
            Logger.println("Number expected. Received: " + subscription);
        }

        return success;
    }

    private static boolean update(List<StreamingService> services, String shortName, String longName,
                                  String subscription) {
        StreamingService service = services.stream().filter(x -> x.getShortName().equals(shortName))
                .findFirst()
                .orElse(null);

        boolean success = false;

        try {
            boolean invalid = false;

            int cost = Integer.parseInt(subscription);

            if (services.isEmpty()) {
                Logger.println("Streaming Services is not initialized/does not exist.");

                invalid = true;
            }

            if (service == null) {
                Logger.println("The Streaming Service with the provided name " + shortName + " does not exist.");

                invalid = true;
            }

            if (cost < 0) {
                Logger.println("The subscription fee of the Streaming Service must be a positive integer value.");

                invalid = true;
            }

            if (longName.trim().isEmpty()) {
                Logger.println("The long name for the Streaming Service is missing.");

                invalid = true;
            }

            if (!invalid) {
                service.setLongName(longName);

                service.updateSubscription(cost);

                success = true;
            }
        } catch (Exception e) {
            Logger.println("Number expected. Received: " + subscription);
        }

        return success;
    }

    private static boolean display(List<StreamingService> services, String service) {
        StreamingService stream = services.stream().filter(x -> x.getShortName().equals(service))
                .findFirst()
                .orElse(null);

        if (stream != null) {
            Logger.println(String.join(",", "stream", stream.getShortName(), stream.getLongName()),
                    false);
            Logger.println("subscription," + stream.getSubscription(), false);
            Logger.println("current_period," + stream.getCurrentRevenue(), false);
            Logger.println("previous_period," + stream.getPreviousRevenue(), false);
            Logger.println("total," + stream.getTotalRevenue(), false);
            Logger.println("licensing," + stream.getTotalLicensing(), false);

            return true;
        } else {
            Logger.println("Streaming service " + service + " does not exist.");
        }

        return false;
    }

    public void collect(Integer amount) {
        current += amount;
    }

    public void pay(Studio studio, Integer amount) {
        licensing += amount;

        studio.collect(amount);
    }

    private void updateSubscription(Integer subscription) {
        if (!accessed) {
            this.subscription = subscription;
        }
    }

    public void nextMonth() {
        total += current;

        previous = current;

        current = 0;

        accessed = false;
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

    public Integer getTotalLicensing() {
        return licensing;
    }

    public String getLongName() {
        return longName;
    }

    public void setLongName(String longName) {
        this.longName = longName;
    }

    public String getShortName() {
        return shortName;
    }

    public Integer getSubscription() {
        return subscription;
    }

    public void setAccessed(Boolean accessed) {
        this.accessed = accessed;
    }
}
