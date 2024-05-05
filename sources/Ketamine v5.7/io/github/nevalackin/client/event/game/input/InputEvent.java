package io.github.nevalackin.client.event.game.input;

import io.github.nevalackin.client.event.Event;

public final class InputEvent implements Event {

    private final InputType type;
    private final int button;
    private final State state;

    public InputEvent(InputType type, int button, State state) {
        this.type = type;
        this.button = button;
        this.state = state;
    }

    public InputType getType() {
        return type;
    }

    public int getButton() {
        return button;
    }

    public State getState() {
        return state;
    }
}
