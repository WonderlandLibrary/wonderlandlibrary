package net.smoothboot.client.module.client;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.smoothboot.client.events.Event;
import net.smoothboot.client.module.Mod;
import net.smoothboot.client.module.settings.BooleanSetting;
import net.smoothboot.client.module.settings.ModeSetting;
import net.smoothboot.client.module.settings.NumberSetting;

import static net.smoothboot.client.hud.frame.*;

public class ClientSetting extends Mod {

    String Red = "Red";
    String Green = "Green";
    String Blue = "Blue";
    String dynamic = "Dynamic";
    String st = "Static";
    String off = "Off";
    String true1 = "On";
    String withBG = "Background";
    String false1 = "Off";

    public NumberSetting clientred = new NumberSetting(Red, 0, 255, 190, 1);
    public NumberSetting clientgreen = new NumberSetting(Green, 0, 255, 130, 1);
    public NumberSetting clientblue = new NumberSetting(Blue, 0, 255, 255, 1);
    public BooleanSetting showfps = new BooleanSetting("Display FPS", false);
    public ModeSetting arrayList = new ModeSetting("Module list", false1, false1, true1, withBG);
    public ModeSetting showtooltips = new ModeSetting("Tooltips", dynamic, dynamic, st, off);

    public ClientSetting() {
        super("Settings", "Overrides client settings", Category.Client);
        addsettings(clientred, clientgreen, clientblue, showfps, arrayList, showtooltips);
    }

    public static boolean displayFPS = false;
    public static boolean modList = false;
    public static boolean listBG = false;
    public static int EnabledTooltips = 1;

    @Override
    public void onTick() {
        menured = clientred.getValueInt();
        menugreen = clientgreen.getValueInt();
        menublue = clientblue.getValueInt();
        if (showtooltips.isMode(dynamic)) {
            EnabledTooltips = 1;
        }
        else if (showtooltips.isMode(st)) {
            EnabledTooltips = 2;
        }
        else if (showtooltips.isMode(off)) {
            EnabledTooltips = 0;
        }
        displayFPS = showfps.isEnabled();
        if (arrayList.isMode(true1)) {
            modList = true;
        }
        if (arrayList.isMode(true1)) {
            modList = true;
        }
        if (arrayList.isMode(withBG)) {
            modList = true;
            listBG = true;
        }
        if (arrayList.isMode(false1)) {
            modList = false;
            listBG = false;
        }
        super.onTick();
    }

    @Override
    public void onDisable() {
        toggle();
        super.onDisable();
    }
}