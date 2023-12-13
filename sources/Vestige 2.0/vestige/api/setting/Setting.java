package vestige.api.setting;

import lombok.AllArgsConstructor;
import lombok.Getter;
import vestige.api.module.Module;

@Getter
@AllArgsConstructor
public class Setting {

    protected String name;
    protected Module parent;

    public String getDisplayName() {
        return name; // Made to be overriden
    }
    
    public boolean isShown() {
		return true; // Made to be overriden
    }

}
