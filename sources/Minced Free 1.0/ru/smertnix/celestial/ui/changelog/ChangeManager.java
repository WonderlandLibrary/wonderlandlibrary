package ru.smertnix.celestial.ui.changelog;


import java.awt.Color;
import java.util.ArrayList;

import net.minecraft.client.Minecraft;
import ru.smertnix.celestial.utils.render.RenderUtils;
import ru.smertnix.celestial.utils.render.RoundedUtil;

public class ChangeManager {

    public static ArrayList<ChangeLog> changeLogs = new ArrayList<>();

    public ArrayList<ChangeLog> getChangeLogs() {
        return changeLogs;
    }

    public static void addLogs() {
        changeLogs.add(new ChangeLog("Added Flight \"ReallyWorld\""));
        changeLogs.add(new ChangeLog("Improved \"AttackAura\" under ReallyWorld"));
        changeLogs.add(new ChangeLog("Fix \"Rotation Advanced\""));
    }
}