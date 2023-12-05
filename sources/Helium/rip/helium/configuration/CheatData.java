package rip.helium.configuration;

import rip.helium.cheat.Cheat;
import rip.helium.utils.property.abs.Property;

import java.util.HashMap;

/**
 * @author antja03
 */
public class CheatData {

    private final String name;
    private final boolean state;
    private final int bind;
    private final HashMap<String, String> valueData;

    public CheatData(String name, boolean state, int bind, HashMap<String, String> valueData) {
        this.name = name;
        this.state = state;
        this.bind = bind;
        this.valueData = valueData;
    }

    public CheatData(Cheat cheat) {
        this.name = cheat.getId();
        this.state = cheat.getState();
        this.bind = cheat.getBind();

        this.valueData = new HashMap<>();
        for (Property property : cheat.getPropertyRegistry().values()) {
            valueData.put(property.getId(), property.getValueAsString());
        }
    }

    public String getName() {
        return name;
    }

    public boolean isState() {
        return state;
    }

    public int getBind() {
        return bind;
    }

    public HashMap<String, String> getValueData() {
        return valueData;
    }
}
