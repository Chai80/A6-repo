import java.util.ArrayList;
import java.util.List;

public class Account {
    private final List<Event> events = new ArrayList<>();
    private final List<String> services = new ArrayList<>();

    public Integer watch(StreamingService service, Offering offer) {
        service.setAccessed(true);

        offer.getEvent().setWatched(true);

        if (offer.getEvent().getType() == Event.Type.MOVIE) {
            if (!services.contains(service.getShortName())) {
                services.add(service.getShortName());

                return service.getSubscription();
            }
        } else {
            if (!events.contains(offer.getEvent())) {
                events.add(offer.getEvent());

                return offer.getFee();
            }
        }

        return 0;
    }

    public void nextMonth() {
        events.clear();

        services.clear();
    }
}
