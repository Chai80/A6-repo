import java.util.List;

public class Offering {
    private final Integer fee;

    private final String service;

    private final Event event;

    public Offering(String service, Event event, Integer fee) {
        this.service = service;
        this.event = event;
        this.fee = fee;
    }

    public static boolean process(String command, List<Event> events, List<Offering> offers,
                                  List<StreamingService> services, List<Studio> studios) {
        String[] tokens = command.split(",");

        switch (tokens[0]) {
            case "offer_movie":
                if (tokens.length != 4) {
                    Logger.println("Expected 3 parameters. Received: " + (tokens.length - 1));

                    return false;
                }

                return create(events, offers, services, studios, tokens[1], tokens[2], tokens[3], "0");

            case "offer_ppv":
                if (tokens.length != 5) {
                    Logger.println("Expected 4 parameters. Received: " + (tokens.length - 1));

                    return false;
                }

                return create(events, offers, services, studios, tokens[1], tokens[2], tokens[3], tokens[4]);

            case "display_offers":
                return display(offers);

            case "retract_movie":
                if (tokens.length != 4) {
                    Logger.println("Expected 3 parameters. Received: " + (tokens.length - 1));

                    return false;
                }

                return retract(offers, tokens[1], tokens[2], tokens[3]);
        }

        Logger.println("Unrecognized command received: " + tokens[0]);

        return false;
    }

    private static boolean create(List<Event> events, List<Offering> offers, List<StreamingService> services,
                                  List<Studio> studios, String service, String name, String year, String fee) {
        StreamingService stream = services.stream().filter(x -> x.getShortName().equals(service))
                .findFirst()
                .orElse(null);

        boolean success = false;

        try {
            Integer yearValue = Integer.parseInt(year);

            try {
                boolean invalid = false;

                int cost = Integer.parseInt(fee);

                Event event = events.stream().filter(x -> x.getName().equals(name))
                        .filter(x -> x.getYear().equals(yearValue))
                        .findFirst()
                        .orElse(null);

                Offering offer = offers.stream().filter(x -> x.getService().equals(service))
                        .filter(x -> x.getEvent().equals(event))
                        .filter(x -> x.getFee().equals(cost))
                        .findFirst()
                        .orElse(null);

                if (stream == null) {
                    Logger.println("Streaming Service with name " + service + " does not exist.");

                    invalid = true;
                }

                if (event == null) {
                    Logger.println("Provided Event does not exist.");

                    invalid = true;
                }

                if (offer != null) {
                    Logger.println("The offering already exists.");

                    invalid = true;
                }

                if (cost < 0) {
                    Logger.println("The PPV fee must be a positive integer value.");

                    invalid = true;
                }

                if ((stream != null) && (event != null)) {
                    Studio studio = studios.stream().filter(x -> x.getShortName().equals(event.getStudio()))
                            .findFirst()
                            .orElse(null);

                    if (studio == null) {
                        Logger.println("No studio named " + event.getStudio() + " exists.");

                        invalid = true;
                    }

                    if (!invalid) {
                        offers.add(new Offering(service, event, cost));

                        stream.pay(studio, event.getFee());

                        success = true;
                    }
                }
            } catch (Exception e) {
                Logger.println("Number expected. Received: " + fee);
            }
        } catch (Exception e) {
            Logger.println("Number expected. Received: " + year);
        }

        return success;
    }

    private static boolean retract(List<Offering> offers, String serviceName, String name, String year) {
        boolean success = false;

        try {
            boolean invalid = false;

            Integer yearValue = Integer.parseInt(year);

            Offering offer = offers.stream().filter(x -> x.getService().equals(serviceName))
                    .filter(x -> x.getEvent().getType().equals(Event.Type.MOVIE))
                    .filter(x -> x.getEvent().getName().equals(name))
                    .filter(x -> x.getEvent().getYear().equals(yearValue))
                    .findFirst()
                    .orElse(null);

            if (offers.isEmpty()) {
                Logger.println("Offering is not initialized.");

                invalid = true;
            }

            if (offer == null) {
                Logger.println("The offering does not exist.");

                invalid = true;
            }

            if (!invalid) {
                offers.remove(offer);

                success = true;
            }
        } catch (Exception e) {
            Logger.println("Number expected. Received: " + year);
        }

        return success;
    }

    private static boolean display(List<Offering> offers) {
        if (!offers.isEmpty()) {
            for (Offering offer : offers) {
                Logger.print(String.join(",", offer.getService(),
                        offer.getEvent().getType().toString().toLowerCase(), offer.getEvent().getName(),
                        String.valueOf(offer.getEvent().getYear())), false);

                if (offer.getEvent().getType() == Event.Type.PPV) {
                    Logger.println("," + offer.getFee(), false);
                } else {
                    Logger.println("", false);
                }
            }
        } else {
            Logger.println("No Offering present.");
        }

        return true;
    }

    public String getService() {
        return service;
    }

    public Event getEvent() {
        return event;
    }

    public Integer getFee() {
        return fee;
    }
}
