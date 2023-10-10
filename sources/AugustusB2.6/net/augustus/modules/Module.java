// 
// Decompiled by Procyon v0.5.36
// 

package net.augustus.modules;

import net.minecraft.client.gui.FontRenderer;
import net.augustus.modules.render.ClickGUI;
import net.lenni0451.eventapi.manager.EventManager;
import net.augustus.utils.sound.SoundUtil;
import net.augustus.Augustus;
import net.minecraft.client.gui.ScaledResolution;
import net.augustus.utils.AnimationUtil;
import java.awt.Color;
import com.google.gson.annotations.SerializedName;
import com.google.gson.annotations.Expose;
import net.augustus.utils.interfaces.SM;
import net.augustus.utils.interfaces.MM;
import net.augustus.utils.interfaces.MC;

public class Module implements MC, MM, SM, Comparable<Module>
{
    @Expose
    @SerializedName("Name")
    private String name;
    @Expose
    @SerializedName("IsInConfig")
    private boolean isInConfig;
    private String displayName;
    @Expose
    @SerializedName("Toggled")
    private boolean toggled;
    @Expose
    @SerializedName("Key")
    private int key;
    @Expose
    @SerializedName("Category")
    private Categorys category;
    private Color color;
    private float x;
    private float y;
    private float minX;
    private float maxX;
    private float moduleWidth;
    public AnimationUtil animationUtil;
    protected ScaledResolution sr;
    
    public Module(final String name, final Color color, final Categorys category) {
        this.isInConfig = true;
        this.displayName = "";
        this.moduleWidth = 0.0f;
        this.sr = new ScaledResolution(Module.mc);
        this.name = name;
        this.color = color;
        this.category = category;
        this.displayName = name;
        Augustus.getInstance().getManager().getModules().add(this);
    }
    
    public final void toggle() {
        if (this.toggled) {
            if (Module.mc.theWorld != null && Module.mm.arrayList.toggleSound.getBoolean() && !this.equals(Module.mm.clickGUI)) {
                SoundUtil.play(SoundUtil.toggleOffSound);
            }
            this.toggled = false;
            Module.mm.getActiveModules().remove(this);
            this.onPreDisable();
            EventManager.unregister(this);
            this.onDisable();
        }
        else {
            if (Module.mc.theWorld != null && Module.mm.arrayList.toggleSound.getBoolean() && !this.equals(Module.mm.clickGUI)) {
                SoundUtil.play(SoundUtil.toggleOnSound);
            }
            this.toggled = true;
            if (!(this instanceof ClickGUI)) {
                Module.mm.getActiveModules().add(this);
            }
            this.onEnable();
            EventManager.register((Object)this);
        }
        Module.mm.arrayList.updateSorting();
    }
    
    public void onEnable() {
    }
    
    public void onDisable() {
    }
    
    public void onPreDisable() {
    }
    
    public Color getColor() {
        return this.color;
    }
    
    public void setColor(final Color color) {
        this.color = color;
    }
    
    public String getName() {
        return this.name;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    public String getDisplayName() {
        return this.displayName;
    }
    
    public void setDisplayName(final String displayName) {
        final String lastName = this.displayName;
        if (Module.mc.thePlayer != null) {
            if (Module.mm.arrayList.isToggled() && Module.mm.arrayList.suffix.getBoolean()) {
                this.displayName = displayName;
            }
            else {
                this.displayName = this.name;
            }
        }
        else {
            this.displayName = displayName;
        }
        if (!this.displayName.equalsIgnoreCase(lastName)) {
            Module.mm.arrayList.updateSorting();
        }
    }
    
    public boolean isToggled() {
        return this.toggled;
    }
    
    public void setToggled(final boolean toggled) {
        if (this.toggled != toggled) {
            this.toggle();
        }
    }
    
    public int getKey() {
        return this.key;
    }
    
    public void setKey(final int key) {
        this.key = key;
    }
    
    public Categorys getCategory() {
        return this.category;
    }
    
    public void setCategory(final Categorys category) {
        this.category = category;
    }
    
    public float getX() {
        return this.x;
    }
    
    public void setX(final float x) {
        this.x = x;
    }
    
    public float getY() {
        return this.y;
    }
    
    public void setY(final float y) {
        this.y = y;
    }
    
    public float getMaxX() {
        return this.maxX;
    }
    
    public void setMaxX(final float maxX) {
        this.maxX = maxX;
    }
    
    public float getMinX() {
        return this.minX;
    }
    
    public void setMinX(final float minX) {
        this.minX = minX;
    }
    
    public AnimationUtil getAnimationUtil() {
        return this.animationUtil;
    }
    
    public float getModuleWidth() {
        return this.moduleWidth;
    }
    
    public void setModuleWidth(final float moduleWidth) {
        this.moduleWidth = moduleWidth;
    }
    
    public void setAnimationUtil(final AnimationUtil animationUtil) {
        this.animationUtil = animationUtil;
    }
    
    public boolean isInConfig() {
        return this.isInConfig;
    }
    
    public void setInConfig(final boolean inConfig) {
        this.isInConfig = inConfig;
    }
    
    public void readModule(final Module module) {
        this.setName(module.getName());
        if (!(this instanceof ClickGUI)) {
            this.setToggled(module.isToggled());
        }
        this.setCategory(module.getCategory());
        this.setKey(module.getKey());
        this.setInConfig(module.isInConfig);
    }
    
    public void readConfig(final Module module) {
        if (this.isInConfig && !(this instanceof ClickGUI)) {
            this.setToggled(module.isToggled());
        }
    }
    
    @Override
    public int compareTo(final Module module) {
        if (Module.mm.arrayList.sortOption.getSelected().equals("Alphabet")) {
            return -module.getName().compareTo(this.getName());
        }
        return Module.mm.arrayList.font.getSelected().equals("Minecraft") ? (Module.mc.fontRendererObj.getStringWidth(module.getDisplayName()) - Module.mc.fontRendererObj.getStringWidth(this.getDisplayName())) : Math.round(Module.mm.arrayList.getCustomFont().getStringWidth(module.getDisplayName()) - Module.mm.arrayList.getCustomFont().getStringWidth(FontRenderer.getFormatFromString(module.getDisplayName())) - (Module.mm.arrayList.getCustomFont().getStringWidth(this.getDisplayName()) - Module.mm.arrayList.getCustomFont().getStringWidth(FontRenderer.getFormatFromString(this.getDisplayName()))));
    }
}
