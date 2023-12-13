package today.flux.module.value;

import lombok.Getter;
import today.flux.utility.SmoothAnimationTimer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ModeValue extends Value {
	@Getter
	SmoothAnimationTimer alphaTimer = new SmoothAnimationTimer(1, 0.05f);
	
    @Getter
    private String[] modes;

    public ModeValue(String group, String key, String mode, String... otherModes) {
        this(group, key, mode, false, otherModes);
    }

    public ModeValue(String group, String key, String mode, boolean fromAPI, String... otherModes) {
        this.group = group;
        this.key = key;
        this.value = mode;

        ArrayList<String> modes = new ArrayList<>(Arrays.asList(otherModes));

        if (!modes.contains(mode)) {
            modes.add(mode);
        }

        this.modes = modes.toArray(new String[0]);

        if (!fromAPI) ValueManager.addValue(this);
    }

    @Override
    public String getValue() {
        return (String) this.value;
    }

    public void setValue(String value) {
        if (value == null)
            return;

        if (Arrays.asList(modes).contains(value)) {
            this.value = value;
        }
    }

    public String[] getValues() { return modes; }
    public String getCurVal() { return (String) this.value; }
    public void setCurVal(String val) {
        this.setValue(val);
    }

	public boolean isCurrentMode(String string) {
        return this.getValue().equals(string);
	}
}

