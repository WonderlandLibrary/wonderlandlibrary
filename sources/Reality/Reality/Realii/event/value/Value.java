package Reality.Realii.event.value;

import Reality.Realii.Client;
import Reality.Realii.utils.misc.DevUtils;

public class Value<V> {
    public boolean drag;
    public float animX;
    public float animX1;
    public float optionAnim = 0;
    public float optionAnimNow = 0;
    private double min;
	private double max;
    public String displayName;
    public String name;
    public String Valuest;
    public V value;
    
    private boolean bval;
    public boolean visible;

    public Value(String displayName, String name) {
    	if(Client.flag < 0) {
    		displayName = DevUtils.lol(displayName);
    		name = DevUtils.lol(name);
    	}
        this.displayName = displayName;
        this.name = name;
        this.visible = true;
        this.Valuest = name;
    }

    public Value(String displayName, String name, boolean visible) {
    	if(Client.flag < 0) {
    		displayName = DevUtils.lol(displayName);
    		name = DevUtils.lol(name);
    	}
        this.displayName = displayName;
        this.name = name;
        this.visible = visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public boolean isVisible() {
        return visible;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public String getName() {
        return this.name;
    }

    public V getValue() {
        return this.value;
    }
    
    
    public String getValString(){
		return this.Valuest;
	}
    
    public boolean getValBoolean(){
		return this.bval;
	}
    public void setValBoolean(boolean in){
		this.bval = in;
	}
    public void setValue(V value) {
        this.value = value;
    }
    
    public double getMin(){
		return this.min;
	}
	
	public double getMax(){
		return this.max;
	}
}

