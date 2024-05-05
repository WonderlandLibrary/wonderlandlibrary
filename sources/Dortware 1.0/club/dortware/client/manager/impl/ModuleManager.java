package club.dortware.client.manager.impl;

import club.dortware.client.manager.Manager;
import club.dortware.client.module.Module;
import club.dortware.client.module.enums.ModuleCategory;
import club.dortware.client.module.impl.combat.AutoPot;
import club.dortware.client.module.impl.combat.KillAura;
import club.dortware.client.module.impl.combat.Regen;
import club.dortware.client.module.impl.combat.Velocity;
import club.dortware.client.module.impl.exploit.Disabler;
import club.dortware.client.module.impl.exploit.InfiniteDurability;
import club.dortware.client.module.impl.exploit.PlayerLagger;
import club.dortware.client.module.impl.hidden.ClickGUI;
import club.dortware.client.module.impl.misc.AntiBot;
import club.dortware.client.module.impl.misc.NameProtect;
import club.dortware.client.module.impl.misc.NoFall;
import club.dortware.client.module.impl.movement.*;
import club.dortware.client.module.impl.player.Breaker;
import club.dortware.client.module.impl.player.Derp;
import club.dortware.client.module.impl.player.FastInteract;
import club.dortware.client.module.impl.player.Zoot;
import club.dortware.client.module.impl.render.Hud;
import club.dortware.client.module.impl.render.Tags;
import club.dortware.client.util.impl.render.CustomFontRenderer;
import net.minecraft.client.gui.FontRenderer;

import java.util.ArrayList;
import java.util.List;

public class ModuleManager extends Manager<Module> {

    public ModuleManager() {
        super(new ArrayList<>());
    }

    /**
     * Sorts this manager by it's list's objects name string length
     * @param fontRenderer - The {@code FontRenderer} used for obtaining the length
     */
    public void sort(FontRenderer fontRenderer) {
        getList().sort((a, b) -> {
            int first = fontRenderer.getStringWidth(a.getModuleData().name());
            int second = fontRenderer.getStringWidth(b.getModuleData().name());
            return second - first;
        });
    }
    /**
     * Sorts this manager by it's list's objects name string length
     * @param customFontRenderer - The {@code CustomFontRenderer} used for obtaining the length
     */
    public void sort(CustomFontRenderer customFontRenderer) {
        getList().sort((a, b) -> {
            int first = (int) customFontRenderer.getWidth(a.getModuleData().name());
            int second = (int) customFontRenderer.getWidth(b.getModuleData().name());
            return second - first;
        });
    }

    @Override
    public void onCreated() {
        // COMBAT
        add(new AutoPot());
        add(new Regen());
        add(new KillAura());
        add(new Velocity());


        // MOVEMENT
        add(new NoSlow());
        add(new Flight());
        add(new LongJump());
        add(new Speed());
        add(new Sprint());
        add(new InvMove());
        add(new Step());
        add(new Jesus());


        // PLAYER
        add(new Derp());
        add(new Breaker());
        add(new Zoot());


        // MISC
        add(new NoFall());
        add(new FastInteract());
        add(new AntiBot());
        add(new NameProtect());

        // RENDER
        add(new Hud());
        add(new Tags());

        // EXPLOIT
        add(new PlayerLagger());
        add(new InfiniteDurability());
        add(new Disabler());


        // HIDDEN
        add(new ClickGUI());
    }

    public List<Module> getAllInCategory(ModuleCategory category) {
        List<Module> list = new ArrayList<>();
        for (Module module : getList()) {
            if (module.getModuleData().category() == category) {
                list.add(module);
            }
        }
        return list;
    }

}
