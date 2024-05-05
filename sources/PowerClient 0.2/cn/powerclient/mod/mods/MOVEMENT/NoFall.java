/*
 * Decompiled with CFR 0_132 Helper by Lightcolour E-mail wyy-666@hotmail.com.
 */
package me.AveReborn.mod.mods.MOVEMENT;

import com.darkmagician6.eventapi.EventTarget;
import java.util.ArrayList;
import me.AveReborn.Value;
import me.AveReborn.events.EventMove;
import me.AveReborn.mod.Category;
import me.AveReborn.mod.Mod;
import me.AveReborn.util.PlayerUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.MathHelper;

public class NoFall
extends Mod {
    public Value<String> mode = new Value("NoFall", "Mode", 0);

    public NoFall() {
        super("NoFall", Category.MOVEMENT);
        this.mode.mode.add("Hypixel");
    }

    @EventTarget
    public void onMove(EventMove event) {
        if (this.mode.isCurrentMode("Hypixel")) {
            this.setDisplayName("Hypixel");
            if (Minecraft.thePlayer.fallDistance > 2.0f) {
                Minecraft.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer(true));
                Minecraft.thePlayer.fallDistance = 0.0f;
            }
        }
    }

    public void setSpeed(double speed) {
        Minecraft.thePlayer.motionX = (double)(- MathHelper.sin(PlayerUtil.getDirection())) * speed;
        Minecraft.thePlayer.motionZ = (double)MathHelper.cos(PlayerUtil.getDirection()) * speed;
    }
}

