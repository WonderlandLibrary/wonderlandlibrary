/*
 * Decompiled with CFR 0_115.
 */
package cow.milkgod.cheese.commands.commands;

import com.darkmagician6.eventapi.EventTarget;
import cow.milkgod.cheese.commands.Command;
import cow.milkgod.cheese.events.EventChatSend;
import cow.milkgod.cheese.events.EventTick;
import cow.milkgod.cheese.ui.GuiIngameHook;
import cow.milkgod.cheese.utils.Logger;

public class Theme
extends Command {
    public Theme() {
        super("theme", null, new String[]{"gui"}, "<TabGUI | Enabled List | Watermark | Potion Status | Armor | Coordinates | Legit | All On>", "Manages what the gui displays.");
    }

    @EventTarget
    public void onChatSend(EventTick event) {
        if (this.getState()) {
            try {
                String arg2 = EventChatSend.getMessage().split(" ")[1];
                if (arg2.equalsIgnoreCase("coords") || arg2.equalsIgnoreCase("coordinates")) {
                    boolean bl2 = GuiIngameHook.drawCoords = !GuiIngameHook.drawCoords;
                    if (GuiIngameHook.drawCoords) {
                        Logger.logChat("\u00a7eCoordinates\u00a77 has been \u00a7aenabled\u00a77.");
                    } else {
                        Logger.logChat("\u00a7eCoordinates\u00a77 has been \u00a7cdisabled\u00a77.");
                    }
                } else if (arg2.equalsIgnoreCase("potionstatus") || arg2.equalsIgnoreCase("ps")) {
                    boolean bl3 = GuiIngameHook.drawPotionStatus = !GuiIngameHook.drawPotionStatus;
                    if (GuiIngameHook.drawPotionStatus) {
                        Logger.logChat("\u00a7ePotion Status\u00a77 has been \u00a7aenabled\u00a77.");
                    } else {
                        Logger.logChat("\u00a7ePotion Status\u00a77 has been \u00a7cdisabled\u00a77.");
                    }
                } else if (arg2.equalsIgnoreCase("tabgui") || arg2.equalsIgnoreCase("tabui")) {
                    boolean bl4 = GuiIngameHook.drawTabui = !GuiIngameHook.drawTabui;
                    if (GuiIngameHook.drawTabui) {
                        Logger.logChat("\u00a7eTabGUI\u00a77 has been \u00a7aenabled\u00a77.");
                    } else {
                        Logger.logChat("\u00a7eTabGUI\u00a77 has been \u00a7cdisabled\u00a77.");
                    }
                } else if (arg2.equalsIgnoreCase("enabledlist") || arg2.equalsIgnoreCase("arraylist")) {
                    boolean bl5 = GuiIngameHook.drawArraylist = !GuiIngameHook.drawArraylist;
                    if (GuiIngameHook.drawArraylist) {
                        Logger.logChat("\u00a7eEnabled List\u00a77 has been \u00a7aenabled\u00a77.");
                    } else {
                        Logger.logChat("\u00a7eEnabled List\u00a77 has been \u00a7cdisabled\u00a77.");
                    }
                } else if (arg2.equalsIgnoreCase("armor") || arg2.equalsIgnoreCase("status")) {
                    boolean bl6 = GuiIngameHook.drawStuffStatus = !GuiIngameHook.drawStuffStatus;
                    if (GuiIngameHook.drawStuffStatus) {
                        Logger.logChat("\u00a7eArmor Status\u00a77 has been \u00a7aenabled\u00a77.");
                    } else {
                        Logger.logChat("\u00a7eArmor Status\u00a77 has been \u00a7cdisabled\u00a77.");
                    }
                } else if (arg2.equalsIgnoreCase("watermark") || arg2.equalsIgnoreCase("name")) {
                    boolean bl7 = GuiIngameHook.drawWatermark = !GuiIngameHook.drawWatermark;
                    if (GuiIngameHook.drawWatermark) {
                        Logger.logChat("\u00a7eWatermark\u00a77 has been \u00a7aenabled\u00a77.");
                    } else {
                        Logger.logChat("\u00a7eWatermark\u00a77 has been \u00a7cdisabled\u00a77.");
                    }
                } else if (arg2.equalsIgnoreCase("alloff") || arg2.equalsIgnoreCase("legit")) {
                    GuiIngameHook.drawTabui = false;
                    GuiIngameHook.drawArraylist = false;
                    GuiIngameHook.drawCoords = false;
                    GuiIngameHook.drawWatermark = false;
                    GuiIngameHook.drawPotionStatus = false;
                    GuiIngameHook.drawStuffStatus = false;
                } else if (arg2.equalsIgnoreCase("allon")) {
                    GuiIngameHook.drawTabui = true;
                    GuiIngameHook.drawArraylist = true;
                    GuiIngameHook.drawCoords = true;
                    GuiIngameHook.drawWatermark = true;
                    GuiIngameHook.drawPotionStatus = true;
                    GuiIngameHook.drawStuffStatus = true;
                    Logger.logChat("Your GUI will now look Hackery hack.");
                } else {
                    Logger.logChat("Invalid Option! Options: " + this.getArguments());
                }
            }
            catch (Exception e) {
                Logger.logChat("Invalid Option! Options: " + this.getArguments());
            }
            this.Toggle();
        }
    }
}

