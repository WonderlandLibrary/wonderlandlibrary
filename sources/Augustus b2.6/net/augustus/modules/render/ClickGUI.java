// 
// Decompiled by Procyon v0.5.36
// 

package net.augustus.modules.render;

import net.lenni0451.eventapi.reflection.EventTarget;
import net.augustus.clickgui.ClickGui;
import net.augustus.events.EventTick;
import net.minecraft.client.gui.GuiScreen;
import net.augustus.Augustus;
import net.augustus.modules.Categorys;
import java.awt.Color;
import net.augustus.settings.StringValue;
import net.augustus.modules.Module;

public class ClickGUI extends Module
{
    public StringValue sorting;
    
    public ClickGUI() {
        super("ClickGui", Color.BLACK, Categorys.RENDER);
        this.sorting = new StringValue(1, "Sorting", this, "Random", new String[] { "Random", "Length", "Alphabet" });
    }
    
    @Override
    public void onEnable() {
        Augustus.getInstance().getConverter().moduleSaver(ClickGUI.mm.getModules());
        Augustus.getInstance().getConverter().settingSaver(ClickGUI.sm.getStgs());
        ClickGUI.mc.displayGuiScreen(Augustus.getInstance().getClickGui());
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
