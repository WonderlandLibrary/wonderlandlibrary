package dev.tenacity.scripting.api.bindings;

import dev.tenacity.utils.tuples.Pair;
import dev.tenacity.Tenacity;
import dev.tenacity.module.Module;
import dev.tenacity.module.impl.combat.KillAura;
import dev.tenacity.module.impl.render.HUDMod;
import dev.tenacity.utils.player.ChatUtil;
import dev.tenacity.utils.time.TimerUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import store.intent.intentguard.annotation.Exclude;
import store.intent.intentguard.annotation.Strategy;

import java.awt.*;

@Exclude(Strategy.NAME_REMAPPING)
public class ClientBinding {

    public String getClientVersion() {
        return Tenacity.INSTANCE.getVersion();
    }


    public TimerUtil createTimer() {
        return new TimerUtil();
    }

    public void printClientMsg(String text) {
        ChatUtil.print(text);
    }

    public float fps() {
        return Minecraft.getDebugFPS();
    }

    public EntityLivingBase getAuraTarget() {
        KillAura killAura = Tenacity.INSTANCE.getModuleCollection().getModule(KillAura.class);
        return KillAura.target;
    }

    public Pair<Color, Color> getClientColors() {
        return HUDMod.getClientColors();
    }

    public boolean leftMouseButtonDown() {
        return Mouse.isButtonDown(0);
    }

    public boolean rightMouseButtonDown() {
        return Mouse.isButtonDown(1);
    }

    public boolean isKeyDown(int key) {
        return Keyboard.isCreated() && Keyboard.isKeyDown(key);
    }


    public Module getModule(String moduleName) {
        Module module = Tenacity.INSTANCE.getModuleCollection().getModuleByName(moduleName);
        if (module != null) {
            return module;
        } else {
            throw new NullPointerException("Module " + moduleName + " does not exist.");
        }
    }

}
