package sudo.module.settings;

public class KeybindSetting extends Setting {
    
    private int key;
	private boolean enabled;
    
    public KeybindSetting(String name, int defaultKey) {
        super(name);
        this.key = defaultKey;
    }

    public void setKey(int key) {
    	this.enabled = true;
    	this.key = key;
    	this.enabled = false;
    }

    public int getKey() {
    	return key;
    }

    public void toggle() {
    	this.enabled = !this.enabled;
    }
}