package sudo.module.settings;


import java.util.Arrays;
import java.util.List;

public class ModeSetting extends Setting {
	
    private String selected;
	private String mode;
	private List<String> modes;
	private int index;
	
	public ModeSetting(String name, String defaultMode, String... modes) {
		super(name);
		this.modes = Arrays.asList(modes);
		this.mode = defaultMode;
		this.index = this.modes.indexOf(defaultMode);
	}
	
	public String getMode() {
		return mode;
	}
	
	public List<String> getModes() {
		return modes;
	}
	
    public String getSelected() {
    	selected=this.mode;
        return selected;
    }
	public void setMode(String mode) {
		this.mode = mode;
		this.index = modes.indexOf(mode);
	}
	
	public int getIndex() {
		return (int) index;
	}
	
	public void setIndex(int index) {
		this.index = index;
		this.mode = modes.get(index);
	}
	
	public void cycle() {
		if (index < modes.size() - 1) {
			index++;
			mode = modes.get(index);
		} else if (index >= modes.size() - 1 ) {
			index = 0;
			mode = modes.get(0);
		}
	}
	
	public void cycleBack() {
		if (index>0 && index <= modes.size()-1) {
			index--;
			mode = modes.get(index);
		} else if (index<=0) {
			index=modes.size()-1;
			mode=modes.get(modes.size()-1);
		}
	}
	
	public boolean is(String mode) {
		return this.mode == mode;
	}
}