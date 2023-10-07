package cc.novoline.events.events;

public class DisplayChestGuiEvent implements Event{

    String string;

    public DisplayChestGuiEvent(String string) {
        this.string = string;
    }

    public String getString() {
        return string;
    }
}
