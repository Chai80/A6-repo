import java.util.ArrayList;
import java.util.List;

public class DemographicGroup {
    private final String shortName;
    private final List<Account> members = new ArrayList<>();
    private String longName;
    private Boolean viewedEvents = false;

    private Integer total = 0;
    private Integer previous = 0;
    private Integer current = 0;

    public DemographicGroup(String shortName, String longName, Integer accounts) {
        this.shortName = shortName;
        this.longName = longName;

        for (int i = 0; i < accounts; i++) {
            members.add(new Account());
        }
    }

    public static boolean process(String command, List<DemographicGroup> demos, List<Offering> offers,
                                  List<StreamingService> services) {
        String[] tokens = command.split(",");

        switch (tokens[0]) {
            case "create_demo":
                if (tokens.length != 4) {
                    Logger.println("Expected 3 parameters. Received: " + (tokens.length - 1));

                    return false;
                }

                return create(demos, tokens[1], tokens[2], tokens[3]);

            case "watch_event":
                if (tokens.length != 6) {
                    Logger.println("Expected 5 parameters. Received: " + (tokens.length - 1));

                    return false;
                }

                return findEvent(demos, offers, services, tokens[1], tokens[2], tokens[3], tokens[4], tokens[5]);

            case "display_demo":
                if (tokens.length != 2) {
                    Logger.println("Expected 1 parameter. Received: " + (tokens.length - 1));

                    return false;
                }

                return display(demos, tokens[1]);

            case "update_demo":
                if (tokens.length != 4) {
                    Logger.println("Expected 3 parameters. Received: " + (tokens.length - 1));

                    return false;
                }

                return update(demos, tokens[1], tokens[2], tokens[3]);
        }

        Logger.println("Unrecognized command received: " + tokens[0]);

        return false;
    }

    private static boolean findEvent(List<DemographicGroup> demos, List<Offering> offers, List<StreamingService> services,
                                     String groupName, String percent, String serviceName, String name, String year) {
        DemographicGroup group = demos.stream()
                .filter(x -> x.getShortName().equals(groupName))
                .findFirst()
                .orElse(null);

        StreamingService service = services.stream()
                .filter(x -> x.getShortName().equals(serviceName))
                .findFirst()
                .orElse(null);

        try {
            Integer yearValue = Integer.parseInt(year);

            try {
                Integer percentValue = Integer.parseInt(percent);

                Offering offer = offers.stream()
                        .filter(x -> x.getService().equals(serviceName))
                        .filter(x -> x.getEvent().getName().equals(name))
                        .filter(x -> x.getEvent().getYear().equals(yearValue))
                        .findFirst()
                        .orElse(null);

                if ((group != null) && (service != null) && (offer != null)) {
                    group.watch(service, offer, percentValue);
                    return true;
                }
            } catch (Exception e) {
                Logger.println("Number expected. Received: " + percent);
            }
        } catch (Exception e) {
            Logger.println("Number expected. Received: " + year);
        }

        return false;
    }

    private static boolean create(List<DemographicGroup> demos, String shortName, String longName, String accounts) {
        boolean success = false;

        try {
            boolean invalid = false;

            int count = Integer.parseInt(accounts);

            if (count < 0) {
                Logger.println("The number of accounts provided for the Demographic Group must be a positive integer value.");

                invalid = true;
            }

            if (shortName.trim().isEmpty()) {
                Logger.println("The short name for the Demographic Group is missing.");

                invalid = true;
            }

            if (longName.trim().isEmpty()) {
                Logger.println("The long name for the Demographic Group is missing.");

                invalid = true;
            }

            if (demos.stream().anyMatch(x -> x.getShortName().equals(shortName))) {
                Logger.println("A Demographic Group with the same short name already exists.");

                invalid = true;
            }

            if (!invalid) {
                demos.add(new DemographicGroup(shortName, longName, count));

                success = true;
            }
        } catch (Exception e) {
            Logger.println("Number expected. Received: " + accounts);
        }

        return success;
    }

    private static boolean update(List<DemographicGroup> demos, String shortName, String longName, String accounts) {
        boolean success = false;

        DemographicGroup group = demos.stream().filter(x -> x.getShortName().equals(shortName))
                .findFirst()
                .orElse(null);

        try {
            boolean invalid = false;

            int count = Integer.parseInt(accounts);

            if (demos.isEmpty()) {
                Logger.println("Demographic Group is not initialized.");

                invalid = true;
            }

            if (group == null) {
                Logger.println("The Demographic Group with the short name " + shortName + " does not exist.");

                invalid = true;
            }

            if (longName.trim().isEmpty()) {
                Logger.println("The long name for the Demographic Group is missing.");

                invalid = true;
            }

            if (count < 0) {
                Logger.println("The number of accounts provided for the Demographic Group must be a positive integer value.");

                invalid = true;
            }

            if (!invalid) {
                group.setLongName(longName);

                group.updateMembership(count);

                success = true;
            }
        } catch (Exception e) {
            Logger.println("Number expected. Received: " + accounts);
        }

        return success;
    }

    private static boolean display(List<DemographicGroup> demos, String demo) {
        DemographicGroup group = demos.stream().filter(x -> x.getShortName().equals(demo))
                .findFirst().orElse(null);

        if (group != null) {
            Logger.println(String.join(",", "demo", group.getShortName(), group.getLongName()),
                    false);
            Logger.println("size," + group.getSize(), false);
            Logger.println("current_period," + group.getCurrentCost(), false);
            Logger.println("previous_period," + group.getPreviousCost(), false);
            Logger.println("total," + group.getTotalCost(), false);

            return true;
        } else {
            Logger.println("Demographic Group named " + demo + " does not exist.");
        }

        return false;
    }

    public void pay(StreamingService service, Integer amount) {
        current += amount;

        service.collect(amount);
    }

    public void watch(StreamingService service, Offering offer, Integer percent) {
        int viewers = members.size() * percent / 100;

        viewedEvents = true;

        for (int i = 0; i < viewers; i++) {
            pay(service, members.get(i).watch(service, offer));
        }
    }

    private void updateMembership(Integer accounts) {
        if (!viewedEvents) {
            members.clear();

            for (int i = 0; i < accounts; i++) {
                members.add(new Account());
            }
        }
    }

    public void nextMonth() {
        total += current;

        previous = current;

        current = 0;

        viewedEvents = false;

        for (Account member : members) {
            member.nextMonth();
        }
    }

    public Integer getCurrentCost() {
        return current;
    }

    public Integer getPreviousCost() {
        return previous;
    }

    public Integer getTotalCost() {
        return total;
    }

    public String getLongName() {
        return longName;
    }

    public void setLongName(String name) {
        longName = name;
    }

    public String getShortName() {
        return shortName;
    }

    public Integer getSize() {
        return members.size();
    }
}
