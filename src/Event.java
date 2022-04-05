import java.util.List;

public class Event {
    private final Type type;
    private final String name;
    private final Integer year;
    private final String studio;
    private Integer duration;
    private Integer fee;
    private Boolean watched = false;

    public Event(Type type, String name, Integer year, Integer duration, String studio, Integer fee) {
        this.type = type;
        this.name = name;
        this.year = year;
        this.duration = duration;
        this.studio = studio;
        this.fee = fee;
    }

    public static boolean process(String command, List<Event> events, List<Studio> studios) {
        String[] tokens = command.split(",");

        switch (tokens[0]) {
            case "create_event":
                if (tokens.length != 7) {
                    Logger.println("Expected 6 parameters. Received: " + (tokens.length - 1));

                    return false;
                }

                return create(events, studios, tokens[1], tokens[2], tokens[3], tokens[4], tokens[5], tokens[6]);

            case "display_events":
                return display(events);

            case "update_event":
                if (tokens.length != 5) {
                    Logger.println("Expected 4 parameters. Received: " + (tokens.length - 1));

                    return false;
                }

                return update(events, tokens[1], tokens[2], tokens[3], tokens[4]);
        }

        Logger.println("Unrecognized command received: " + tokens[0]);

        return false;
    }

    private static boolean create(List<Event> events, List<Studio> studios, String type, String name, String year,
                                  String duration, String studio, String fee) {
        boolean success = false;

        try {
            Type eventType = Type.valueOf(type.toUpperCase());

            try {
                int yearValue = Integer.parseInt(year);

                try {
                    int length = Integer.parseInt(duration);

                    try {
                        boolean invalid = false;

                        int cost = Integer.parseInt(fee);

                        if (studios.isEmpty() || studios.stream().noneMatch(x -> x.getShortName().equals(studio))) {
                            Logger.println("Studio named " + studio + " does not exists.");

                            invalid = true;
                        }

                        if (length < 0) {
                            Logger.println("The duration of the event must be a positive integer value.");

                            invalid = true;
                        }

                        if (cost < 0) {
                            Logger.println("The event fee must be a positive integer value.");

                            invalid = true;
                        }

                        if (name.trim().isEmpty()) {
                            Logger.println("The name of the event is missing.");

                            invalid = true;
                        }

                        if (yearValue < 1000 || yearValue > 9999) {
                            Logger.println("The provided year is invalid.");

                            invalid = true;
                        }

                        if (events.stream().filter(x -> x.getName().equals(name))
                                .anyMatch(x -> x.getYear().equals(yearValue))) {
                            Logger.println("The Event with the name " + name
                                    + " and year " + year + " already exists.");

                            invalid = true;
                        }

                        if (!invalid) {
                            events.add(new Event(eventType, name, yearValue, length, studio, cost));

                            success = true;
                        }
                    } catch (Exception e) {
                        Logger.println("Number expected. Received: " + fee);
                    }
                } catch (Exception e) {
                    Logger.println("Number expected. Received: " + duration);
                }
            } catch (Exception e) {
                Logger.println("Number expected. Received: " + year);
            }
        } catch (Exception e) {
            Logger.println("Unexpected event type received: " + type);
        }

        return success;
    }

    private static boolean update(List<Event> events, String name, String year, String duration, String fee) {
        boolean success = false;

        try {
            int yearValue = Integer.parseInt(year);

            try {
                int length = Integer.parseInt(duration);

                try {
                    boolean invalid = false;

                    int cost = Integer.parseInt(fee);

                    Event event = events.stream().filter(x -> x.getName().equals(name))
                            .filter(x -> x.getYear().equals(yearValue))
                            .findFirst()
                            .orElse(null);

                    if (events.isEmpty()) {
                        Logger.println("Events is not initialized.");

                        invalid = true;
                    }

                    if (event == null) {
                        Logger.println("The Event with the provided name and year does not exist.");

                        invalid = true;
                    }

                    if (length < 0) {
                        Logger.println("The duration of the event must be a positive integer value.");

                        invalid = true;
                    }

                    if (cost < 0) {
                        Logger.println("The event fee must be a positive integer value.");

                        invalid = true;
                    }

                    if (!invalid) {
                        event.setDuration(length);

                        event.updateFee(cost);

                        success = true;
                    }
                } catch (Exception e) {
                    Logger.println("Number expected. Received: " + fee);
                }
            } catch (Exception e) {
                Logger.println("Number expected. Received: " + duration);
            }
        } catch (Exception e) {
            Logger.println("Number expected. Received: " + year);
        }

        return success;
    }

    private static boolean display(List<Event> events) {
        if (!events.isEmpty()) {
            for (Event event : events) {
                Logger.println(String.join(",", event.getType().toString().toLowerCase(), event.getName(),
                        String.valueOf(event.getYear()), String.valueOf(event.getDuration()), event.getStudio(),
                        String.valueOf(event.getFee())),
                        false);

            }
        } else {
            Logger.println("No events present.");
        }

        return true;
    }

    public void updateFee(Integer fee) {
        if (!watched) {
            this.fee = fee;
        }
    }

    public Type getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public String getStudio() {
        return studio;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public Integer getFee() {
        return fee;
    }

    public Integer getYear() {
        return year;
    }

    public void setWatched(Boolean watched) {
        this.watched = watched;
    }

    public enum Type {
        MOVIE,
        PPV
    }
}
