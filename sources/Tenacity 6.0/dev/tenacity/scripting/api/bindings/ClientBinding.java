// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.scripting.api.bindings;

import dev.tenacity.module.Module;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import dev.tenacity.module.impl.render.HUDMod;
import java.awt.Color;
import dev.tenacity.utils.tuples.Pair;
import dev.tenacity.module.api.TargetManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.client.Minecraft;
import dev.tenacity.utils.player.ChatUtil;
import dev.tenacity.utils.time.TimerUtil;
import dev.tenacity.Tenacity;
import store.intent.intentguard.annotation.Strategy;
import store.intent.intentguard.annotation.Exclude;

@Exclude({ Strategy.NAME_REMAPPING })
public class ClientBinding
{
    public String getClientVersion() {
        return Tenacity.INSTANCE.getVersion();
    }
    
    public TimerUtil createTimer() {
        return new TimerUtil();
    }
    
    public void printClientMsg(final String text) {
        ChatUtil.print(text);
    }
    
    public float fps() {
        return (float)Minecraft.getDebugFPS();
    }
    
    public EntityLivingBase getAuraTarget() {
        return TargetManager.target;
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
    
    public boolean isKeyDown(final int key) {
        return Keyboard.isCreated() && Keyboard.isKeyDown(key);
    }
    
    public Module getModule(final String moduleName) {
        final Module module = Tenacity.INSTANCE.getModuleCollection().getModuleByName(moduleName);
        if (module != null) {
            return module;
        }
        throw new NullPointerException("Module " + moduleName + " does not exist.");
    }
}
