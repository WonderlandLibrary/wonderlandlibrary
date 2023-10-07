package cc.novoline.events.events;

public class ScoreBoardEvent implements Event{

    private int order;
    private String string;

    public ScoreBoardEvent(int order, String string) {
        this.order = order;
        this.string = string;
    }

    public int getOrder() {
        return order;
    }

    public String getString() {
        return string;
    }
}
