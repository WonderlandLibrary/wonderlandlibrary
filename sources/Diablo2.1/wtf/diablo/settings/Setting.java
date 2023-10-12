package wtf.diablo.settings;

import wtf.diablo.module.Module;
import wtf.diablo.settings.impl.ModeSetting;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class Setting {
    protected String name;
    private boolean hidden;
    private ModeSetting parent = null;
    private String[] parentValue = null;

    public void setHidden(boolean hidden){
        this.hidden = hidden;
    }
    public boolean isHidden() {
        return this.hidden;
    }
    public String getName() {
        return name;
    }

    public Object getObjectValue() {
        return null;
    }

    public void setParent(ModeSetting modeSetting, String... mode) {
        parent = modeSetting;
        parentValue = mode;
    }

    public boolean hasParent() {
        if(parent != null)
            return true;
        return false;
    }

    public boolean parentCheck(){
        if(this.hidden)
            return false;
        for(String parent : parentValue) {
            if (Objects.equals(this.parent.getMode(), parent)) {
                return true;
            }
        }
        return false;
    }


}
