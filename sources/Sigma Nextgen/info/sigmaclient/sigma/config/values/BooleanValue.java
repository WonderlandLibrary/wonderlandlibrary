package info.sigmaclient.sigma.config.values;

import info.sigmaclient.sigma.sigma5.utils.Sigma5AnimationUtil;

public class BooleanValue extends Value<Boolean> {
    public Sigma5AnimationUtil animation = new Sigma5AnimationUtil(100, 100);
    public BooleanValue(String name, Boolean value){
        super(name);
        this.setValue(value);
    }

    public boolean isEnable(){
        return this.getValue();
    }
}
