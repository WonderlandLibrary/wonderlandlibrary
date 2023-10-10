/*
 * Decompiled with CFR 0.152.
 */
package tk.rektsky.module.impl.rektsky;

import tk.rektsky.gui.ClickGuiOLD;
import tk.rektsky.gui.clickgui.AstolfoClickGui;
import tk.rektsky.module.Category;
import tk.rektsky.module.Module;
import tk.rektsky.module.settings.BooleanSetting;
import tk.rektsky.module.settings.ListSetting;
import tk.rektsky.ui.clickgui.AlloyClickGUI;

public class ClickGUI
extends Module {
    public ListSetting mode = new ListSetting("Mode", new String[]{"Alloy", "Legacy"}, "Alloy");
    public BooleanSetting gradient = new BooleanSetting("Gradient", true);
    public BooleanSetting blur = new BooleanSetting("Blur", true);

    public ClickGUI() {
        super("ClickGUI", "Just a ClickGUI", 54, Category.REKTSKY);
    }

    @Override
    public void onEnable() {
        if (this.mc.thePlayer == null || this.mc.thePlayer.ticksExisted < 1) {
            this.setToggled(false);
            return;
        }
        switch (this.mode.getValue()) {
            case "Alloy": {
                if (this.mc.currentScreen != null && this.mc.currentScreen instanceof AstolfoClickGui) break;
                this.mc.displayGuiScreen(new AstolfoClickGui());
                break;
            }
            case "Legacy": {
                if (this.mc.currentScreen != null && this.mc.currentScreen instanceof AstolfoClickGui) break;
                this.mc.displayGuiScreen(new ClickGuiOLD());
            }
        }
    }

    @Override
    public void onDisable() {
        if (this.mc.currentScreen != null && (this.mc.currentScreen instanceof AstolfoClickGui || this.mc.currentScreen instanceof ClickGuiOLD || this.mc.currentScreen instanceof AlloyClickGUI)) {
            this.mc.currentScreen.closeGui();
            this.mc.currentScreen = null;
        }
    }
}

