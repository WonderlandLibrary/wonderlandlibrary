package tech.atani.client.feature.guis.screens.clickgui.icarus.window.component.impl;

import tech.atani.client.feature.value.Value;
import tech.atani.client.feature.guis.screens.clickgui.icarus.window.component.Component;

public abstract class ValueComponent extends Component {

    private final Value value;

    public ValueComponent(Value value, float posX, float posY, float height) {
        super(posX, posY, height);
        this.value = value;
    }

    public Value getValue() {
        return value;
    }
}
