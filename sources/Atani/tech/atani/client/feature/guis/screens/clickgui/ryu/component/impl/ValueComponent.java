package tech.atani.client.feature.guis.screens.clickgui.ryu.component.impl;

import tech.atani.client.feature.value.Value;
import tech.atani.client.feature.guis.screens.clickgui.ryu.component.Component;

public abstract class ValueComponent extends Component {

    protected final Value value;

    public ValueComponent(Value value, float posX, float posY, float baseWidth, float baseHeight) {
        super(posX, posY, baseWidth, baseHeight);
        this.value = value;
    }

    public Value getValue() {
        return value;
    }
}
