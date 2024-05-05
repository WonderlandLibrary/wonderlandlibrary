package Velo.impl.Modules.visuals.hud;


import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.client.gui.inventory.GuiInventory;

import java.awt.*;

import Velo.api.Module.Module;
import Velo.api.Util.Other.ColorUtil;
import Velo.api.Util.fontRenderer.Fonts;
import Velo.impl.Event.EventRender;
import Velo.impl.Settings.BooleanSetting;
import Velo.impl.Settings.NumberSetting;

public class Keystrokes extends Module {

    public int fade;
    public int fade2;
    public int fade3;
    public int fade4;
    public int fade5;
    public int fade6;
    public int fade7;
    
    public NumberSetting yAdd = new NumberSetting("Y (Up)", 0, 0, 300, 1);
    public NumberSetting xAdd = new NumberSetting("X (Right)", 0, 0, 850, 1);

    
      public BooleanSetting wasd = new BooleanSetting("WASD", true);
        public BooleanSetting space = new BooleanSetting("Spacebar", true);
        public BooleanSetting mb = new BooleanSetting("Mouse Buttons", true);
 
    ScaledResolution sr = new ScaledResolution(this.mc);

    public Keystrokes() {
        super("Keystrokes", "Keystrokes", 0, Category.VISUALS);
    loadSettings(xAdd, yAdd, wasd, space, mb);
    }

    
    
    @Override
    public void onRenderUpdate(EventRender event) {
    	   //-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-
        if (mc.gameSettings.keyBindForward.isKeyDown() && fade != 150) {
            if(!(mc.currentScreen instanceof GuiScreen)) {
                fade += 5;
            }
        } else {
            if (!mc.gameSettings.keyBindForward.isKeyDown() && fade != 0) {
                fade -= 5;
            }
        }
        //-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-
        if (mc.gameSettings.keyBindLeft.isKeyDown() && fade2 != 150) {
            if(!(mc.currentScreen instanceof GuiScreen)) {
                fade2 += 5;
            }
        } else {
            if (!mc.gameSettings.keyBindLeft.isKeyDown() && fade2 != 0) {
                fade2 -= 5;
            }
        }
        //-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-
        if (mc.gameSettings.keyBindBack.isKeyDown() && fade3 != 150) {
            if(!(mc.currentScreen instanceof GuiScreen)) {
                fade3 += 5;
            }
        } else {
            if (!mc.gameSettings.keyBindBack.isKeyDown() && fade3 != 0) {
                fade3 -= 5;
            }
        }
        //-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-
        if (mc.gameSettings.keyBindRight.isKeyDown() && fade4 != 150) {
            if(!(mc.currentScreen instanceof GuiScreen)) {
                fade4 += 5;
            }
        } else {
            if (!mc.gameSettings.keyBindRight.isKeyDown() && fade4 != 0) {
                fade4 -= 5;
            }
        }
        //-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-
        if (mc.gameSettings.keyBindJump.isKeyDown() && fade5 != 150) {
            if(!(mc.currentScreen instanceof GuiScreen)) {
                fade5 += 5;
            }
        } else {
            if (!mc.gameSettings.keyBindJump.isKeyDown() && fade5 != 0) {
                fade5 -= 5;
            }
        }
        //-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-
        if (mc.gameSettings.keyBindAttack.isKeyDown() && fade6 != 160) {
            fade6 += 20;
        } else {
            if (!mc.gameSettings.keyBindAttack.isKeyDown() && fade6 != 0) {
                fade6 -= 20;
            }
        }
        //-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-
        if (mc.gameSettings.keyBindUseItem.isKeyDown() && fade7 != 160) {
            fade7 += 20;
        } else {
            if (!mc.gameSettings.keyBindUseItem.isKeyDown() && fade7 != 0) {
                fade7 -= 20;
            }
        }
        //-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-
                if (wasd.enabled == true)
                {
                    Gui.drawRect(sr.getScaledWidth() / 7 - 98.1f-3.9 + xAdd.getValue() + 80, sr.getScaledHeight() / 4 + 5 + 250 - yAdd.getValue(), sr.getScaledWidth() / 7 - 70-3.9 + xAdd.getValue() + 80, sr.getScaledHeight() / 4 + 33 + 250 - yAdd.getValue(), 0x80000000);
                    Gui.drawRect(sr.getScaledWidth() / 7 - 98.1f-3.9 + xAdd.getValue() + 80, sr.getScaledHeight() / 4 + 5 + 250 - yAdd.getValue(), sr.getScaledWidth() / 7 - 70-3.9 + xAdd.getValue() + 80, sr.getScaledHeight() / 4 + 33 + 250 - yAdd.getValue(), white(fade).getRGB());
                    Fonts.fs.drawCenteredString("W", (float) (sr.getScaledWidth() / 7 - 90.5f - 3.9 + xAdd.getValue() + 80), (float) (sr.getScaledHeight() / 4 + 14 + 250 - yAdd.getValue()), ColorUtil.rainbow( 30));
                    Gui.drawRect(sr.getScaledWidth() / 7 - 132.1f-3.9 + xAdd.getValue() + 80, sr.getScaledHeight() / 4 + 39 + 250 - yAdd.getValue(), sr.getScaledWidth() / 7 - 104-3.9 + xAdd.getValue() + 80, sr.getScaledHeight() / 4 + 67 + 250 - yAdd.getValue(), 0x80000000);
                    Gui.drawRect(sr.getScaledWidth() / 7 - 132.1f-3.9 + xAdd.getValue() + 80, sr.getScaledHeight() / 4 + 39 + 250 - yAdd.getValue(), sr.getScaledWidth() / 7 - 104-3.9 + xAdd.getValue() + 80, sr.getScaledHeight() / 4 + 67 + 250 - yAdd.getValue(), white(fade2).getRGB());
                   Fonts.fs.drawCenteredString("A", (float) (sr.getScaledWidth() / 7 - 124-3.9 + xAdd.getValue() + 80), (float) (sr.getScaledHeight() / 4 + 48 + 250 - yAdd.getValue()), ColorUtil.rainbow(30));
                    Gui.drawRect(sr.getScaledWidth() / 7 - 98.1f-3.9 + xAdd.getValue() + 80, sr.getScaledHeight() / 4 + 39 + 250 - yAdd.getValue(), sr.getScaledWidth() / 7 - 70-3.9 + xAdd.getValue() + 80, sr.getScaledHeight() / 4 + 67 + 250 - yAdd.getValue(), 0x80000000);
                    Gui.drawRect(sr.getScaledWidth() / 7 - 98.1f-3.9 + xAdd.getValue() + 80, sr.getScaledHeight() / 4 + 39 + 250 - yAdd.getValue(), sr.getScaledWidth() / 7 - 70-3.9 + xAdd.getValue() + 80, sr.getScaledHeight() / 4 + 67 + 250 - yAdd.getValue(), white(fade3).getRGB());
                   Fonts.fs.drawCenteredString("S", (float) (sr.getScaledWidth() / 7 - 90.5f-3.9 + xAdd.getValue() + 80), (float) (sr.getScaledHeight() / 4 + 48 + 250 - yAdd.getValue()),ColorUtil.rainbow( 30));
                    Gui.drawRect(sr.getScaledWidth() / 7 - 64.1f-3.9 + xAdd.getValue() + 80, sr.getScaledHeight() / 4 + 39 + 250 - yAdd.getValue(), sr.getScaledWidth() / 7 - 36-3.9 + xAdd.getValue() + 80, sr.getScaledHeight() / 4 + 67 + 250 - yAdd.getValue(), 0x80000000);
                    Gui.drawRect(sr.getScaledWidth() / 7 - 64.1f-3.9 + xAdd.getValue() + 80, sr.getScaledHeight() / 4 + 39 + 250 - yAdd.getValue(), sr.getScaledWidth() / 7 - 36-3.9 + xAdd.getValue() + 80, sr.getScaledHeight() / 4 + 67 + 250 - yAdd.getValue(), white(fade4).getRGB());
                   Fonts.fs.drawCenteredString("D", (float) (sr.getScaledWidth() / 7 - 56-3.9 + xAdd.getValue() + 80), (float) (sr.getScaledHeight() / 4 + 48 + 250 - yAdd.getValue()), ColorUtil.rainbow(30));
                }
                if (space.enabled == true && mb.enabled == true)
                {
                    Gui.drawRect(sr.getScaledWidth() / 7 - 132.1f-3.9 + xAdd.getValue() + 80, sr.getScaledHeight() / 4 + 107 + 250 - yAdd.getValue(), sr.getScaledWidth() / 7 - 36-3.9 + xAdd.getValue() + 80, sr.getScaledHeight() / 4 + 135 + 250 - yAdd.getValue(), 0x80000000);
                    Gui.drawRect(sr.getScaledWidth() / 7 - 132.1f-3.9 + xAdd.getValue() + 80, sr.getScaledHeight() / 4 + 107 + 250 - yAdd.getValue(), sr.getScaledWidth() / 7 - 36-3.9 + xAdd.getValue() + 80, sr.getScaledHeight() / 4 + 135 + 250 - yAdd.getValue(), white(fade5).getRGB());
                    Gui.drawRect(sr.getScaledWidth() / 7 - 122.1f-3.9 + xAdd.getValue() + 80, sr.getScaledHeight() / 4 + 120 + 250 - yAdd.getValue(), sr.getScaledWidth() / 7 - 46-3.9 + xAdd.getValue() + 80, sr.getScaledHeight() / 4 + 122 + 250 - yAdd.getValue(), ColorUtil.rainbow(30));
                }
                if (space.enabled == true && mb.enabled == false)
                {
                    Gui.drawRect(sr.getScaledWidth() / 7 - 132.1f-3.9 + xAdd.getValue() + 80, sr.getScaledHeight() / 4 + 73 + 250 - yAdd.getValue(), sr.getScaledWidth() / 7 - 36-3.9 + xAdd.getValue() + 80, sr.getScaledHeight() / 4 + 101 + 250 - yAdd.getValue(), 0x80000000);
                    Gui.drawRect(sr.getScaledWidth() / 7 - 132.1f-3.9 + xAdd.getValue() + 80, sr.getScaledHeight() / 4 + 73 + 250 - yAdd.getValue(), sr.getScaledWidth() / 7 - 36-3.9 + xAdd.getValue() + 80, sr.getScaledHeight() / 4 + 101 + 250 - yAdd.getValue(), white(fade5).getRGB());
                    Gui.drawRect(sr.getScaledWidth() / 7 - 122.1f-3.9 + xAdd.getValue() + 80, sr.getScaledHeight() / 4 + 86 + 250 - yAdd.getValue(), sr.getScaledWidth() / 7 - 46-3.9 + xAdd.getValue() + 80, sr.getScaledHeight() / 4 + 88 + 250 - yAdd.getValue(), ColorUtil.rainbow(30));
                }                                                                                   //13 more                                                                                              //13 less
                if (mb.enabled == true)
                {
                    Gui.drawRect(sr.getScaledWidth() / 7 - 132.1f-3.9 + xAdd.getValue() + 80, sr.getScaledHeight() / 4 + 73 + 250 - yAdd.getValue(), sr.getScaledWidth() / 7 - 88-3.9 + xAdd.getValue() + 80, sr.getScaledHeight() / 4 + 101 + 250 - yAdd.getValue(), 0x80000000);
                    Gui.drawRect(sr.getScaledWidth() / 7 - 132.1f-3.9 + xAdd.getValue() + 80, sr.getScaledHeight() / 4 + 73 + 250 - yAdd.getValue(), sr.getScaledWidth() / 7 - 88-3.9 + xAdd.getValue() + 80, sr.getScaledHeight() / 4 + 101 + 250 - yAdd.getValue(), white(fade6).getRGB());
                   Fonts.fs.drawCenteredString("LMB", (float) (sr.getScaledWidth() / 7 - 125.72f-3.9 + xAdd.getValue() + 80), (float) (sr.getScaledHeight() / 4 + 82.5f + 250 - yAdd.getValue()), ColorUtil.rainbow(30));
                    Gui.drawRect(sr.getScaledWidth() / 7 - 82-3.9 + xAdd.getValue() + 80, sr.getScaledHeight() / 4 + 73 + 250 - yAdd.getValue(), sr.getScaledWidth() / 7 - 36-3.9 + xAdd.getValue() + 80, sr.getScaledHeight() / 4 + 101 + 250 - yAdd.getValue(), 0x80000000);
                    Gui.drawRect(sr.getScaledWidth() / 7 - 82-3.9 + xAdd.getValue() + 80, sr.getScaledHeight() / 4 + 73 + 250 - yAdd.getValue(), sr.getScaledWidth() / 7 - 36-3.9 + xAdd.getValue() + 80, sr.getScaledHeight() / 4 + 101 + 250 - yAdd.getValue(), white(fade7).getRGB());
                   Fonts.fs.drawCenteredString("RMB", (float) (sr.getScaledWidth() / 7 - 74-3.9 + xAdd.getValue() + 80), (float) (sr.getScaledHeight() / 4 + 82.5f + 250 - yAdd.getValue()), ColorUtil.rainbow(30));
                }
    	super.onRenderUpdate(event);
    }


 
    public static Color white(int a) {
        Color c = new Color(255, 255, 255, a);
        return c;
    }
}
