package club.cloverhook.configuration;

import java.util.HashMap;

import club.cloverhook.cheat.Cheat;
import club.cloverhook.utils.property.abs.Property;

/**
 * @author antja03
 */
public class CheatData {

    private String name;
    private boolean state;
    private int bind;
    private HashMap<String, String> valueData;

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
