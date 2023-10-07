package sudo.module.settings;

public class Setting {

	public String name;
	public boolean focused;
	protected boolean visible = true;
	
	public Setting(String name) {
		this.name = name;
	}
	
	public boolean isVisible() {
		return visible;
	}
	
	public void setVisible(boolean visible) {
		this.visible = visible;
	}
	
	public String getName() {
		return name;
	}
}