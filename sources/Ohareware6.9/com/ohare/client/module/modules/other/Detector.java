package com.ohare.client.module.modules.other;

import com.ohare.client.Client;
import com.ohare.client.event.events.world.PacketEvent;
import com.ohare.client.module.Module;
import com.ohare.client.utils.Printer;
import com.ohare.client.utils.value.impl.BooleanValue;
import dorkbox.messageBus.annotations.Subscribe;
import net.minecraft.block.BlockAir;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.server.S02PacketChat;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3;

import java.awt.Color;
import java.util.Arrays;
import java.util.List;

/**
 * @author Xen for OhareWare
 * @since 8/16/2019
 **/
public class Detector extends Module {
    private BooleanValue autodisable = new BooleanValue("Autodisable", true);
    private BooleanValue unblock = new BooleanValue("Unblock", true);
    private BooleanValue chat = new BooleanValue("Chat", true);
    private BooleanValue strict = new BooleanValue("Strict", true);
    private BooleanValue suspect = new BooleanValue("CheatingWarning", true);
    private BooleanValue autoNoU = new BooleanValue("AutoNoU", true);

    private final List<String> hackMatches = Arrays.asList("hack", "report", "cheat",
            "aura", "speed", "record", "what client", "ban", "bhop", "bunny hop", "hax");

    private final List<String> noUMatches = Arrays.asList("gay", "fag", "stupid",
            "retard", "idiot", "skid", "loser", "cheater", "retard",
            "kys", "neck your self", "delete self", "cunt", "suck", "kill yourself");

    public Detector() {
        super("Detector", Category.OTHER, new Color(0xA75E62).getRGB());
    }

    @Subscribe
    public void onPacket(PacketEvent event) {
        if (mc.thePlayer == null || mc.theWorld == null) {
            return;
        }
        if (!event.isSending()) {
            if (event.getPacket() instanceof S08PacketPlayerPosLook) {
                final S08PacketPlayerPosLook packet = (S08PacketPlayerPosLook) event.getPacket();
                final Vec3 tppos = new Vec3(packet.getX(), packet.getY(), packet.getY());
                double dist = tppos.squareDistanceTo(mc.thePlayer.getPositionVector());
                if (dist < 25 && dist > 0.1) {
                    if (unblock.getValue()) {
                        mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
                    }
                    if (autodisable.getValue() && (!(mc.theWorld.getBlockState(new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 1.0, mc.thePlayer.posZ)).getBlock() instanceof BlockAir) || !(mc.theWorld.getBlockState(new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 2.0, mc.thePlayer.posZ)).getBlock() instanceof BlockAir))) {
                        Client.INSTANCE.getModuleManager().getModule("speed").setEnabled(false);
                        Client.INSTANCE.getModuleManager().getModule("flight").setEnabled(false);
                        Client.INSTANCE.getModuleManager().getModule("longjump").setEnabled(false);
                        Client.INSTANCE.getNotificationManager().addNotification("Lag back! turned off some mods", 1500);

                        Printer.print("Automatically turned off mods to prevent being banned.");
                        return;
                    }
                    Printer.print("Please turn off whatever mod lagged you back to prevent being banned!!!");
                }
            } else if (chat.getValue() && event.getPacket() instanceof S02PacketChat) {
                final S02PacketChat chatPacket = (S02PacketChat) event.getPacket();
                final String message = chatPacket.getChatComponent().getUnformattedText();
                if (message.toLowerCase().contains(mc.getSession().getUsername().toLowerCase()) || !strict.getValue()) {
                    if (autoNoU.isEnabled()) {
                        for (String noUMatch : noUMatches) {
                            if (!message.contains(noUMatch)) {
                                continue;
                            }
                            mc.thePlayer.sendChatMessage("no u");
                            Client.INSTANCE.getNotificationManager().addNotification("Detected a Salty Noob, auto no u", 1500);
                            break;
                        }
                    }
                    if (suspect.isEnabled()) {
                        for (String hackMatch : hackMatches) {
                            if (!message.contains(hackMatch)) {
                                continue;
                            }
                            Client.INSTANCE.getNotificationManager().addNotification("Suspected of cheating! turn it down buddy!", 1500);
                            break;
                        }
                    }
                }
                if (message.equalsIgnoreCase("ground items will be removed in ")) {
                    Client.INSTANCE.getNotificationManager().addNotification("Ground items will be gone in " + message.split("be removed in ")[1], 1500);
                }
            }
        }
    }

}
