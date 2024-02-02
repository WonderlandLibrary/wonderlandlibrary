package CakeClient.modules;

import net.minecraft.client.Minecraft;

public class Module
{
    public Minecraft mc;
    public String name;
    public Integer activationKey;
    public Boolean enabled;
    public Module(final String name) {
        this.mc = Minecraft.getMinecraft();
        this.enabled = false;
        this.name = name;
    }
    
    public void toggle() {
    	
        this.enabled = !this.enabled;
        if (this.enabled) this.onEnable();
        else this.onDisable();
    }
    
    public void enable()
    {
    	this.enabled = true;
    	this.onEnable();
    }
    
    public void disable()
    {
    	this.enabled = false;
    	this.onDisable();
    }
    
    public void onEnable() {}
    public void onDisable() {} 
    public void onUpdate() {}
    public void onDraw() {}
    public void onLeftConfig() {}
    public void onRightConfig() {}
    public String getConfigStatus() {return "";}
    
    public void keyUpdate(final int key) {
    }
}