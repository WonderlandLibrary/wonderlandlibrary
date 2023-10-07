// 
// Decompiled by Procyon v0.6.0
// 

package net.augustus.modules.render;

import net.lenni0451.eventapi.reflection.EventTarget;
import net.augustus.clickgui.ClickGui;
import net.augustus.events.EventTick;
import net.augustus.material.themes.Dark;
import net.lenni0451.eventapi.events.IEvent;
import net.lenni0451.eventapi.manager.EventManager;
import net.augustus.events.EventClickGui;
import net.minecraft.client.gui.GuiScreen;
import net.augustus.Augustus;
import net.augustus.modules.Categorys;
import java.awt.Color;
import net.augustus.settings.StringValue;
import net.augustus.modules.Module;

public class ClickGUI extends Module
{
    public StringValue sorting;
    public StringValue mode;
    
    public ClickGUI() {
        super("ClickGui", Color.BLACK, Categorys.RENDER);
        this.sorting = new StringValue(1, "Sorting", this, "Random", new String[] { "Random", "Length", "Alphabet" });
        this.mode = new StringValue(25, "Mode", this, "Default", new String[] { "Default", "Plane", "CSGO", "New" });
    }
    
    @Override
    public void onEnable() {
        Augustus.getInstance().getConverter().moduleSaver(ClickGUI.mm.getModules());
        Augustus.getInstance().getConverter().settingSaver(ClickGUI.sm.getStgs());
        if (this.mode.getSelected().equalsIgnoreCase("Default")) {
            ClickGUI.mc.displayGuiScreen(Augustus.getInstance().getClickGui());
        }
        else if (this.mode.getSelected().equalsIgnoreCase("Plane")) {
            ClickGUI.mc.displayGuiScreen(Augustus.getInstance().getPlaneGui());
        }
        else if (this.mode.getSelected().equalsIgnoreCase("CSGO")) {
            ClickGUI.mc.displayGuiScreen(Augustus.getInstance().csgogui);
        }
        else if (this.mode.getSelected().equalsIgnoreCase("New")) {
            EventManager.call(new EventClickGui());
            ClickGUI.mc.displayGuiScreen(new Dark());
        }
        else {
            ClickGUI.mc.displayGuiScreen(Augustus.getInstance().getClickGui());
        }
    }
    
    @Override
    public void onDisable() {
        Augustus.getInstance().getConverter().moduleSaver(ClickGUI.mm.getModules());
        Augustus.getInstance().getConverter().settingSaver(ClickGUI.sm.getStgs());
    }
    
    @EventTarget
    public void onEventTick(final EventTick eventTick) {
        if (!(ClickGUI.mc.currentScreen instanceof ClickGui) && this.isToggled()) {
            this.toggle();
        }
    }
}
