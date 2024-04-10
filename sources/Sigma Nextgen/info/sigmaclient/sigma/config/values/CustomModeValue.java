package info.sigmaclient.sigma.config.values;

import com.google.gson.annotations.Expose;
import info.sigmaclient.sigma.modules.Module;

public class CustomModeValue extends ModeValue {
    public Module[] values2;
    public Module getCurrent(){
        for(Module m : values2){
            if(m.name.equalsIgnoreCase(this.getValue())) return m;
        }
        return null;
    }

    public CustomModeValue(String name, String mode, Module[] modes){
        super(name, mode, modes);
        this.values2 = modes;
    }
}
