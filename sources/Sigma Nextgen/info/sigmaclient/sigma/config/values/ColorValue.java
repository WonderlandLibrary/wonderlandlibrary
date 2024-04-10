package info.sigmaclient.sigma.config.values;

import java.awt.*;

public class ColorValue extends Value<Integer> {
    public ColorValue(String name, Integer value){
        super(name);
        this.setValue(value);
    }
    public Color getColor(){
        return new Color(this.getValue());
    }
    public int getColorInt(){
        return this.getValue();
    }
}
