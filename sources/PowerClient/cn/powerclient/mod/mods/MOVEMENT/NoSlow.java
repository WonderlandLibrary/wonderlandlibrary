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
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;

public class NoSlow
extends Mod {
    public Value<String> mode = new Value("NoSlow", "Mode", 0);

    public NoSlow() {
        super("NoSlow", Category.MOVEMENT);
        this.mode.mode.add("Hypixel");
    }

    @EventTarget
    public void onMove(EventMove event) {
        if (this.mode.isCurrentMode("Hypixel")) {
            this.setDisplayName("Hypixel");
            if (Minecraft.thePlayer.isBlocking()) {
                Minecraft.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
            }
        }
    }

    public void setSpeed(double speed) {
        Minecraft.thePlayer.motionX = (double)(- MathHelper.sin(PlayerUtil.getDirection())) * speed;
        Minecraft.thePlayer.motionZ = (double)MathHelper.cos(PlayerUtil.getDirection()) * speed;
    }

	public static double getSlowness() {
		// TODO 自动生成的方法存根
		return 0;
	}
}

