/*
 * Decompiled with CFR 0.152.
 */
package me.Tengoku.Terror.module.misc;

import me.Tengoku.Terror.event.EventTarget;
import me.Tengoku.Terror.event.events.EventUpdate;
import me.Tengoku.Terror.module.Category;
import me.Tengoku.Terror.module.Module;
import me.Tengoku.Terror.util.PacketUtil;
import me.Tengoku.Terror.util.TimerUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.entity.Entity;
import net.minecraft.network.play.client.C01PacketChatMessage;

public class AutoReport
extends Module {
    TimerUtils timer = new TimerUtils();

    @EventTarget
    public void onUpdate(EventUpdate eventUpdate) {
        for (Entity entity : Minecraft.theWorld.loadedEntityList) {
            if (!(entity instanceof EntityOtherPlayerMP) || mc.getNetHandler().getPlayerInfo(entity.getUniqueID()) == null || entity.isInvisible() || !this.timer.waitUntil(200.0)) continue;
            PacketUtil.sendPacket(new C01PacketChatMessage("/report " + entity.getName() + " cheating"));
            this.timer.reset();
        }
    }

    public AutoReport() {
        super("AutoReport", 0, Category.MISC, "Automatically report everyone.");
    }
}

