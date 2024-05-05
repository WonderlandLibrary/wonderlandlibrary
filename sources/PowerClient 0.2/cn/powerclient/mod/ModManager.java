/*
 * Decompiled with CFR 0_132 Helper by Lightcolour E-mail wyy-666@hotmail.com.
 */
package me.AveReborn.mod;

import java.util.ArrayList;
import me.AveReborn.mod.Mod;
import me.AveReborn.mod.mods.COMBAT.Antibot;
import me.AveReborn.mod.mods.COMBAT.Criticals;
import me.AveReborn.mod.mods.COMBAT.Hitbox;
import me.AveReborn.mod.mods.COMBAT.KeepSprint;
import me.AveReborn.mod.mods.COMBAT.Killaura;
import me.AveReborn.mod.mods.COMBAT.Reach;
import me.AveReborn.mod.mods.COMBAT.Velocity;
import me.AveReborn.mod.mods.ClickGui;
import me.AveReborn.mod.mods.MISC.AutoArmor;
import me.AveReborn.mod.mods.MISC.ChestStealer;
import me.AveReborn.mod.mods.MISC.Eagle;
import me.AveReborn.mod.mods.MISC.FastPlace;
import me.AveReborn.mod.mods.MISC.InvCleaner;
import me.AveReborn.mod.mods.MISC.NameProtect;
import me.AveReborn.mod.mods.MISC.SpeedMine;
import me.AveReborn.mod.mods.MISC.Teams;
import me.AveReborn.mod.mods.MOVEMENT.Fly;
import me.AveReborn.mod.mods.MOVEMENT.InvMove;
import me.AveReborn.mod.mods.MOVEMENT.LongJump;
import me.AveReborn.mod.mods.MOVEMENT.NoFall;
import me.AveReborn.mod.mods.MOVEMENT.NoSlow;
import me.AveReborn.mod.mods.MOVEMENT.Safewalk;
import me.AveReborn.mod.mods.MOVEMENT.Scaffold;
import me.AveReborn.mod.mods.MOVEMENT.Scaffold2;
import me.AveReborn.mod.mods.MOVEMENT.Speed;
import me.AveReborn.mod.mods.MOVEMENT.Sprint;
import me.AveReborn.mod.mods.RENDER.Animation;
import me.AveReborn.mod.mods.RENDER.BlockESP;
import me.AveReborn.mod.mods.RENDER.BlockOverlay;
import me.AveReborn.mod.mods.RENDER.ChestESP;
import me.AveReborn.mod.mods.RENDER.Dab;
import me.AveReborn.mod.mods.RENDER.ESP;
import me.AveReborn.mod.mods.RENDER.FullBright;
import me.AveReborn.mod.mods.RENDER.HUD;
import me.AveReborn.mod.mods.RENDER.ItemPhysic;
import me.AveReborn.mod.mods.RENDER.NameTag;
import me.AveReborn.mod.mods.RENDER.Projectiles;

public class ModManager {
    public static ArrayList<Mod> modList = new ArrayList();
    public static ArrayList<Mod> sortedModList = new ArrayList();

    public ModManager() {
        this.addMod(new ClickGui());
        this.addMod(new Reach());
        this.addMod(new Hitbox());
        this.addMod(new Antibot());
        this.addMod(new Velocity());
        this.addMod(new Killaura());
        this.addMod(new Criticals());
        this.addMod(new KeepSprint());
        this.addMod(new Fly());
        this.addMod(new Speed());
        this.addMod(new Sprint());
        this.addMod(new NoFall());
        this.addMod(new NoSlow());
        this.addMod(new InvMove());
        this.addMod(new Safewalk());
        this.addMod(new Scaffold());
        this.addMod(new LongJump());
        this.addMod(new Scaffold2());
        this.addMod(new HUD());
        this.addMod(new ESP());
        this.addMod(new Dab());
        this.addMod(new NameTag());
        this.addMod(new BlockESP());
        this.addMod(new ChestESP());
        this.addMod(new Animation());
        this.addMod(new FullBright());
        this.addMod(new ItemPhysic());
        this.addMod(new Projectiles());
        this.addMod(new BlockOverlay());
        this.addMod(new Eagle());
        this.addMod(new Teams());
        this.addMod(new FastPlace());
        this.addMod(new SpeedMine());
        this.addMod(new AutoArmor());
        this.addMod(new InvCleaner());
        this.addMod(new NameProtect());
        this.addMod(new ChestStealer());
    }

    public void addMod(Mod m2) {
        modList.add(m2);
    }

    public ArrayList<Mod> getToggled() {
        ArrayList<Mod> toggled = new ArrayList<Mod>();
        for (Mod m2 : modList) {
            if (!m2.isEnabled()) continue;
            toggled.add(m2);
        }
        return toggled;
    }

    public static Mod getModByName(String mod) {
        for (Mod m2 : modList) {
            if (!m2.getName().equalsIgnoreCase(mod)) continue;
            return m2;
        }
        return null;
    }

    public static ArrayList<Mod> getModList() {
        return modList;
    }
}

