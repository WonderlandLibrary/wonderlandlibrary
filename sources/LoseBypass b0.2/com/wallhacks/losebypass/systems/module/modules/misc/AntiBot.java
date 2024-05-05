/*
 * Decompiled with CFR 0.152.
 */
package com.wallhacks.losebypass.systems.module.modules.misc;

import com.wallhacks.losebypass.LoseBypass;
import com.wallhacks.losebypass.event.eventbus.SubscribeEvent;
import com.wallhacks.losebypass.event.events.TickEvent;
import com.wallhacks.losebypass.systems.module.Module;
import com.wallhacks.losebypass.utils.SessionUtils;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.entity.player.EntityPlayer;

@Module.Registration(name="AntiBot", description="Removes bots from world and prevents client from attacking ncps")
public class AntiBot
extends Module {
    public static AntiBot instance;
    private static List<EntityPlayer> tabList;

    public AntiBot() {
        instance = this;
    }

    @SubscribeEvent
    public void onTick(TickEvent event) {
        tabList = SessionUtils.getTabPlayerList();
        ArrayList l = new ArrayList(AntiBot.mc.theWorld.playerEntities);
        Iterator iterator = l.iterator();
        while (iterator.hasNext()) {
            EntityPlayer entity = (EntityPlayer)iterator.next();
            if (!entity.isInvisible() || entity.ticksExisted <= 105 || tabList.contains(entity) || entity.isSpectator()) continue;
            AntiBot.mc.theWorld.removeEntity(entity);
        }
    }

    public static boolean allowRender(EntityPlayer entityPlayer) {
        if (AntiBot.mc.thePlayer == entityPlayer) {
            return false;
        }
        if (entityPlayer.getHealth() == 0.0f) {
            return false;
        }
        if (!instance.isEnabled()) return true;
        if (tabList.contains(entityPlayer)) return true;
        return false;
    }

    public static boolean allowAttack(EntityPlayer entityPlayer) {
        if (AntiBot.mc.thePlayer == entityPlayer) {
            return false;
        }
        if (entityPlayer.getHealth() == 0.0f) {
            return false;
        }
        if (LoseBypass.socialManager.isFriend(entityPlayer.getName())) {
            return false;
        }
        if (!instance.isEnabled()) return true;
        if (tabList.contains(entityPlayer)) return true;
        return false;
    }

    static {
        tabList = new ArrayList<EntityPlayer>();
    }
}

