/*
 * Decompiled with CFR 0_115.
 */
package cow.milkgod.cheese.module.modules;

import com.darkmagician6.eventapi.EventTarget;
import cow.milkgod.cheese.events.EventTick;
import cow.milkgod.cheese.module.Category;
import cow.milkgod.cheese.module.Module;
import cow.milkgod.cheese.utils.Wrapper;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;

public class NoRender
extends Module {
    public NoRender() {
        super("NoRender", 0, Category.RENDER, 6684876, true, "Removes dropped items to avoid lag.", new String[]{"nor"});
    }

    @EventTarget
    public void onTick(EventTick tick) {
        for (Object o : Wrapper.mc.theWorld.loadedEntityList) {
            Entity entity = (Entity)o;
            if (!(entity instanceof EntityItem)) continue;
            EntityItem item = (EntityItem)entity;
            item.renderDistanceWeight = 0.0;
        }
    }

    @Override
    public void onDisable() {
        super.onDisable();
        for (Object o : Wrapper.mc.theWorld.loadedEntityList) {
            Entity entity = (Entity)o;
            if (!(entity instanceof EntityItem)) continue;
            EntityItem item = (EntityItem)entity;
            item.renderDistanceWeight = 1.0;
        }
    }
}

