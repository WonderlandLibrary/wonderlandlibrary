/*
 * Decompiled with CFR 0_115.
 */
package cow.milkgod.cheese.module.modules;

import com.darkmagician6.eventapi.EventTarget;
import cow.milkgod.cheese.events.EventTick;
import cow.milkgod.cheese.module.Category;
import cow.milkgod.cheese.module.Module;
import cow.milkgod.cheese.utils.Wrapper;
import java.util.Random;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.player.EnumPlayerModelParts;

public class SkinDerp
extends Module {
    public SkinDerp() {
        super("SkinDerp", 0, Category.EXPLOITS, 10079232, true, "Flashes your skin", new String[]{"sderp"});
    }

    @EventTarget
    public void onTick(EventTick event) {
        Wrapper.mc.gameSettings.func_178878_a(EnumPlayerModelParts.HAT, new Random().nextBoolean());
        Wrapper.mc.gameSettings.func_178878_a(EnumPlayerModelParts.JACKET, new Random().nextBoolean());
        Wrapper.mc.gameSettings.func_178878_a(EnumPlayerModelParts.LEFT_PANTS_LEG, new Random().nextBoolean());
        Wrapper.mc.gameSettings.func_178878_a(EnumPlayerModelParts.RIGHT_PANTS_LEG, new Random().nextBoolean());
        Wrapper.mc.gameSettings.func_178878_a(EnumPlayerModelParts.LEFT_SLEEVE, new Random().nextBoolean());
        Wrapper.mc.gameSettings.func_178878_a(EnumPlayerModelParts.RIGHT_SLEEVE, new Random().nextBoolean());
    }
}

