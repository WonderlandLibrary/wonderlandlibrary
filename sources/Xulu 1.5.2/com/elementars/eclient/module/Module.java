// 
// Decompiled by Procyon v0.5.36
// 

package com.elementars.eclient.module;

import com.elementars.eclient.command.Command;
import com.elementars.eclient.event.events.RenderEvent;
import com.elementars.eclient.guirewrite.elements.FeatureList;
import com.elementars.eclient.Xulu;
import org.apache.logging.log4j.LogManager;
import net.minecraft.client.settings.KeyBinding;
import dev.xulu.settings.Bind;
import dev.xulu.settings.Value;
import org.apache.logging.log4j.Logger;
import com.elementars.eclient.util.Helper;

public class Module implements Helper
{
    protected final Logger LOGGER;
    private String name;
    private String desc;
    private String displayName;
    private Category category;
    private boolean toggled;
    private boolean drawn;
    public final Value<Bind> bind;
    public final Value<Animation> inAnimation;
    public KeyBinding keybind;
    public static Module instance;
    
    public Module(final String name, final String desc, final int key, final Category category, final boolean drawn) {
        this.LOGGER = LogManager.getLogger("Xulu");
        this.bind = this.register(new Value<Bind>("Bind", this, new Bind(0)));
        this.inAnimation = new Value<Animation>("In Animation", this, Animation.NONE, Animation.values());
        this.name = name;
        this.desc = desc;
        this.bind.getValue().setNum(key);
        this.category = category;
        this.toggled = false;
        this.drawn = drawn;
        this.setup();
        Module.instance = this;
    }
    
    public void destroy() {
    }
    
    public void onEnable() {
    }
    
    public void onDisable() {
    }
    
    public void onEnableR() {
        Xulu.EVENT_MANAGER.register(this);
        this.onEnable();
    }
    
    public void onDisableR() {
        Xulu.EVENT_MANAGER.unregister(this);
        this.onDisable();
    }
    
    public void onToggle() {
    }
    
    public void toggle() {
        this.toggled = !this.toggled;
        this.onToggle();
        if (this.toggled) {
            if (FeatureList.animation.getValue() && (FeatureList.type.getValue().equalsIgnoreCase("Enable") || FeatureList.type.getValue().equalsIgnoreCase("Both"))) {
                this.inAnimation.setEnumValue("ENABLE");
            }
            this.onEnableR();
        }
        else {
            if (FeatureList.animation.getValue() && (FeatureList.type.getValue().equalsIgnoreCase("Disable") || FeatureList.type.getValue().equalsIgnoreCase("Both"))) {
                this.inAnimation.setEnumValue("DISABLE");
            }
            this.onDisableR();
        }
    }
    
    public void initToggle(final boolean enabled) {
        this.toggled = enabled;
        this.onToggle();
        if (this.toggled) {
            this.onEnableR();
        }
        else {
            this.onDisableR();
        }
    }
    
    public void disable() {
        if (this.toggled) {
            this.toggle();
        }
    }
    
    public void setDrawn(final boolean in) {
        this.drawn = in;
    }
    
    public boolean isDrawn() {
        return this.drawn;
    }
    
    public String getName() {
        return this.name;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    public String getDesc() {
        return this.desc;
    }
    
    public int getKey() {
        return this.bind.getValue().getNum();
    }
    
    public void setKey(final int key) {
        this.bind.getValue().setNum(key);
    }
    
    public Category getCategory() {
        return this.category;
    }
    
    public void setCategory(final Category category) {
        this.category = category;
    }
    
    public boolean isToggled() {
        return this.toggled;
    }
    
    public boolean isToggledAnim() {
        return this.toggled || this.inAnimation.getValue() == Animation.DISABLE;
    }
    
    public String getDisplayName() {
        return (this.displayName == null) ? this.name : this.displayName;
    }
    
    public void setDisplayName(final String displayName) {
        this.displayName = displayName;
    }
    
    public void setup() {
    }
    
    public void onUpdate() {
    }
    
    public void onRender() {
    }
    
    public void onWorldRender(final RenderEvent event) {
    }
    
    public String getHudInfo() {
        return null;
    }
    
    public <T> Value<T> register(final Value<T> s) {
        Xulu.VALUE_MANAGER.register(s);
        return s;
    }
    
    public static Module getModule(final Class<? extends Module> clazz) {
        return Xulu.MODULE_MANAGER.getModule(clazz);
    }
    
    public static <T extends Module> T getModuleT(final Class<T> clazz) {
        return Xulu.MODULE_MANAGER.getModuleT(clazz);
    }
    
    protected void sendDebugMessage(final String text) {
        Command.sendChatMessage("&b[" + this.name + "]:&r " + text);
    }
    
    protected void sendRawDebugMessage(final String text) {
        Command.sendRawChatMessage("&b[" + this.name + "]:&r " + text);
    }
    
    public enum Animation
    {
        NONE, 
        ENABLE, 
        DISABLE;
    }
}
