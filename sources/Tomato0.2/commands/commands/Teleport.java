/*
 * Decompiled with CFR 0_115.
 */
package cow.milkgod.cheese.commands.commands;

import com.darkmagician6.eventapi.EventTarget;
import cow.milkgod.cheese.commands.Command;
import cow.milkgod.cheese.events.EventChatSend;
import cow.milkgod.cheese.events.EventTick;
import cow.milkgod.cheese.utils.Logger;
import java.util.Objects;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.entity.Entity;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;

public class Teleport
extends Command {
    public Teleport() {
        super("teleport", "U fucked up! Teleport ", new String[]{"tp"}, "<x, y, z>", "Teleports you to the specified coords. [You need exploit doe]");
    }

    @EventTarget
    public void onTick(EventTick event) {
        try {
            String Sx = EventChatSend.getMessage().split(" ")[1];
            String Sy = EventChatSend.getMessage().split(" ")[2];
            String Sz = EventChatSend.getMessage().split(" ")[3];
            double x2 = Double.parseDouble(Sx);
            double y2 = Double.parseDouble(Sy);
            double z2 = Double.parseDouble(Sz);
            if (Objects.nonNull(this.mc.thePlayer.ridingEntity)) {
                this.mc.thePlayer.ridingEntity.setPosition(x2, y2, z2);
            }
            this.mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(this.mc.thePlayer.posX, 0.0, this.mc.thePlayer.posZ, false));
            this.mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(x2, y2 + 2.0, z2, false));
            this.mc.thePlayer.setPositionAndUpdate(x2, y2, z2);
            int lol = 0;
            while (lol < 10) {
                this.mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(x2 * (double)lol, y2 - 0.1 * (double)lol, z2 * (double)lol, true));
                ++lol;
            }
            this.mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(x2, y2, z2, false));
            this.mc.thePlayer.setPositionAndUpdate(x2, y2, z2);
            if (Objects.nonNull(this.mc.thePlayer.ridingEntity)) {
                this.mc.thePlayer.ridingEntity.setPosition(this.mc.thePlayer.posX, this.mc.thePlayer.posY, this.mc.thePlayer.posZ);
            }
            Logger.logChat("Teleported to: \u00a7e" + x2 + "\u00a77, \u00a7e" + y2 + "\u00a77, \u00a7e" + z2 + "\u00a77.");
        }
        catch (Exception e) {
            Logger.logChat(String.valueOf(String.valueOf(this.getErrorMessage())) + this.getArguments());
        }
        this.Toggle();
    }
}

