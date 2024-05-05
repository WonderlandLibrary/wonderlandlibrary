package com.initial.modules;

import net.minecraft.client.*;
import net.minecraft.client.entity.*;
import com.initial.settings.*;
import java.util.*;
import com.initial.events.impl.*;
import com.initial.*;
import net.minecraft.util.*;
import com.initial.events.*;
import com.initial.scriptmanager.*;

public class Module
{
    private String arrayName;
    protected static Random rand;
    protected Minecraft mc;
    public static EntityPlayerSP localPlayer;
    public boolean visible;
    private double transition;
    private boolean beingEnabled;
    private double verticalTransition;
    public double scaleFactor;
    private boolean expanded;
    public List<Setting> settings;
    public String name;
    public String hidden;
    public String displayName;
    private int key;
    public Category category;
    public boolean toggled;
    
    public Module(final String name, final int key, final Category category) {
        this.mc = Minecraft.getMinecraft();
        this.visible = true;
        this.beingEnabled = false;
        this.settings = new ArrayList<Setting>();
        this.name = name;
        this.key = key;
        this.category = category;
        this.toggled = false;
        this.setup();
    }
    
    public Module(final String name, final Category category) {
        this.mc = Minecraft.getMinecraft();
        this.visible = true;
        this.beingEnabled = false;
        this.settings = new ArrayList<Setting>();
        this.name = name;
        this.category = category;
    }
    
    public void addSettings(final Setting... settings) {
        this.settings.addAll(Arrays.asList(settings));
    }
    
    public List<Setting> getSettings() {
        return this.settings;
    }
    
    public boolean isVisible() {
        return this.visible;
    }
    
    public void setVisible(final boolean visible) {
        this.visible = visible;
    }
    
    public void onEvent(final EventNigger e) {
    }
    
    public boolean isEnabled() {
        return this.toggled;
    }
    
    public void onEnable() {
        if (this.getCategory() == Category.SCRIPT) {
            final Script script = Astomero.instance.scriptManager.getScriptByName(this.getName());
            script.callEvent("onEnable");
        }
        this.beingEnabled = true;
        this.verticalTransition = 0.0;
        this.transition = this.mc.fontRendererObj.getStringWidth(StringUtils.stripControlCodes(this.getDisplayName()));
        EventManager.register(this);
    }
    
    public void onUpdate() {
    }
    
    public void onDisable() {
        if (this.getCategory() == Category.SCRIPT) {
            final Script script = Astomero.instance.scriptManager.getScriptByName(this.getName());
            script.callEvent("onDisable");
        }
        this.beingEnabled = false;
        EventManager.unregister(this);
    }
    
    public void onToggle() {
    }
    
    public void toggle() {
        this.toggled = !this.toggled;
        if (this.toggled) {
            this.onEnable();
        }
        else {
            this.onDisable();
        }
    }
    
    public void setToggled(final boolean toggled) {
        this.toggled = toggled;
        if (this.toggled) {
            this.onEnable();
        }
        else {
            this.onDisable();
        }
    }
    
    public String getName() {
        return this.name;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    public int getKey() {
        return this.key;
    }
    
    public void setKey(final int key) {
        this.key = key;
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
    
    public String getDisplayName() {
        return (this.displayName == null) ? this.name : this.displayName;
    }
    
    public void setDisplayName(final String displayName) {
        this.displayName = displayName;
    }
    
    public boolean isExpanded() {
        return this.expanded;
    }
    
    public void setExpanded(final boolean expanded) {
        this.expanded = expanded;
    }
    
    public String getArrayName() {
        return this.arrayName;
    }
    
    public void setArrayName(final String arrayName) {
        this.arrayName = arrayName;
    }
    
    public double getVerticalTransition() {
        return this.verticalTransition;
    }
    
    public void setVerticalTransition(final double verticalTransition) {
        this.verticalTransition = verticalTransition;
    }
    
    public boolean isBeingEnabled() {
        return this.beingEnabled;
    }
    
    public void setBeingEnabled(final boolean beingEnabled) {
        this.beingEnabled = beingEnabled;
    }
    
    public double getTransition() {
        return this.transition;
    }
    
    public void setTransition(final double transition) {
        this.transition = transition;
    }
    
    public void setup() {
    }
    
    static {
        Module.rand = new Random();
    }
}
