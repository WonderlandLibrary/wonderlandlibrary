package best.azura.client.impl.events;

import best.azura.scripting.event.NamedEvent;

public class EventRenderHand implements NamedEvent {
    private String state = "Pre";

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public boolean isPre() {
        return state.equals("Pre");
    }

    public boolean isPost() {
        return state.equals("Post");
    }

    @Override
    public String name() {
        return "renderHand";
    }
}
