import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

public class Simulation {
    private final List<DemographicGroup> demos = new ArrayList<>();
    private final List<Event> events = new ArrayList<>();
    private final List<Offering> offers = new ArrayList<>();
    private final List<StreamingService> services = new ArrayList<>();
    private final List<Studio> studios = new ArrayList<>();
    private LocalDate simulationDate = LocalDate.of(2020, Month.OCTOBER, 1);

    private Boolean interactive;

    private Boolean success = true;

    public Simulation(Boolean interactive) {
        this.interactive = interactive;
    }

    public boolean execute(String command) {
        boolean running = true;

        String firstWord = command.split(",", 2)[0];

        if (!success && !firstWord.equals("write") && !firstWord.equals("stop")) {
            return true;
        }

        if (!firstWord.equals("write") && !firstWord.equals("stop")) {
            Logger.println("> " + command, false);
        }

        switch (firstWord) {
            case "create_demo":
            case "watch_event":
            case "display_demo":
            case "update_demo":
                success = DemographicGroup.process(command, demos, offers, services);
                break;

            case "create_event":
            case "display_events":
            case "update_event":
                success = Event.process(command, events, studios);
                break;

            case "offer_movie":
            case "offer_ppv":
            case "display_offers":
            case "retract_movie":
                success = Offering.process(command, events, offers, services, studios);
                break;

            case "create_stream":
            case "display_stream":
            case "update_stream":
                success = StreamingService.process(command, services);
                break;

            case "create_studio":
            case "display_studio":
                success = Studio.process(command, studios);
                break;

            case "display_time":
                display();
                break;

            case "next_month":
                nextMonth();
                break;

            case "write":
                new Zipper(command, Logger.getContents()).create();
                break;

            case "stop":
                running = false;
                break;
        }

        success |= interactive;

        return running;
    }

    private void display() {
        Logger.println(String.join(",", "time", String.valueOf(simulationDate.getMonthValue()),
                String.valueOf(simulationDate.getYear())), false);
    }

    private void nextMonth() {
        simulationDate = simulationDate.plusMonths(1);

        for (DemographicGroup group : demos) {
            group.nextMonth();
        }

        for (Event event : events) {
            event.setWatched(false);
        }

        offers.clear();

        for (StreamingService service : services) {
            service.nextMonth();
        }

        for (Studio studio : studios) {
            studio.nextMonth();
        }
    }
}
