package tech.atani.client.feature.guis.screens.clickgui.xave.window.component.impl;

import tech.atani.client.feature.guis.screens.clickgui.xave.window.component.Component;
import tech.atani.client.feature.value.Value;

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
