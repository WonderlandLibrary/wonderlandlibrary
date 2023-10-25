/*
 * Decompiled with CFR 0.152.
 */
package me.Tengoku.Terror.module.combat;

import de.Hero.settings.Setting;
import java.util.ArrayList;
import me.Tengoku.Terror.Exodus;
import me.Tengoku.Terror.event.EventTarget;
import me.Tengoku.Terror.event.events.EventMotion;
import me.Tengoku.Terror.event.events.EventPacket;
import me.Tengoku.Terror.event.events.EventUpdate;
import me.Tengoku.Terror.event.events.EventUseEntity;
import me.Tengoku.Terror.module.Category;
import me.Tengoku.Terror.module.Module;
import me.Tengoku.Terror.util.PlayerUtils;
import me.Tengoku.Terror.util.TimerUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C0APacketAnimation;

public class Criticals
extends Module {
    private final double[] watchdogOffsets = new double[]{0.056f, 0.016f, 0.003f};
    private final TimerUtils timer = new TimerUtils();
    private int groundTicks;

    @EventTarget
    public void onHit(EventUseEntity eventUseEntity) {
        String string = Exodus.INSTANCE.settingsManager.getSettingByName("Criticals Mode").getValString();
        if (string.equalsIgnoreCase("MiniJump")) {
            if (Minecraft.thePlayer.onGround) {
                Minecraft.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(Minecraft.thePlayer.posX, Minecraft.thePlayer.posY + 3.0001, Minecraft.thePlayer.posZ, Minecraft.thePlayer.onGround));
                Minecraft.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(Minecraft.thePlayer.posX, Minecraft.thePlayer.posY, Minecraft.thePlayer.posZ, Minecraft.thePlayer.onGround));
                Minecraft.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(Minecraft.thePlayer.posX, Minecraft.thePlayer.posY, Minecraft.thePlayer.posZ, Minecraft.thePlayer.onGround));
                Minecraft.thePlayer.jump();
                Minecraft.thePlayer.motionY -= (double)0.32152f;
            }
        }
    }

    @EventTarget
    public void onSendPacket(EventPacket eventPacket) {
        String string = Exodus.INSTANCE.settingsManager.getSettingByName("Criticals Mode").getValString();
        if (string.equalsIgnoreCase("AAC") && eventPacket.getPacket() instanceof C0APacketAnimation) {
            C02PacketUseEntity c02PacketUseEntity;
            if (this.groundTicks > 1 && this.timer.hasReached(800.0)) {
                double[] dArray = this.watchdogOffsets;
                int n = this.watchdogOffsets.length;
                int n2 = 0;
                while (n2 < n) {
                    double d = dArray[n2];
                    this.timer.reset();
                    mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(Minecraft.thePlayer.posX, Minecraft.thePlayer.posY + d, Minecraft.thePlayer.posZ, false));
                    ++n2;
                }
            }
            if (string.equalsIgnoreCase("Packet") && this.canCrit() && eventPacket.getPacket() instanceof C02PacketUseEntity && (c02PacketUseEntity = (C02PacketUseEntity)eventPacket.getPacket()).getAction() == C02PacketUseEntity.Action.ATTACK && string.equalsIgnoreCase("Packet")) {
                Minecraft.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(Minecraft.thePlayer.posX, Minecraft.thePlayer.posY + 0.1625, Minecraft.thePlayer.posZ, false));
                Minecraft.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(Minecraft.thePlayer.posX, Minecraft.thePlayer.posY, Minecraft.thePlayer.posZ, false));
                Minecraft.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(Minecraft.thePlayer.posX, Minecraft.thePlayer.posY + 4.0E-6, Minecraft.thePlayer.posZ, false));
                Minecraft.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(Minecraft.thePlayer.posX, Minecraft.thePlayer.posY, Minecraft.thePlayer.posZ, false));
                Minecraft.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(Minecraft.thePlayer.posX, Minecraft.thePlayer.posY + 1.0E-6, Minecraft.thePlayer.posZ, false));
                Minecraft.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(Minecraft.thePlayer.posX, Minecraft.thePlayer.posY, Minecraft.thePlayer.posZ, false));
                Minecraft.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer());
            }
        }
    }

    @EventTarget
    public void onUpdate(EventUpdate eventUpdate) {
        String string = Exodus.INSTANCE.settingsManager.getSettingByName("Criticals Mode").getValString();
        this.setDisplayName("Criticals \ufffdf" + string);
    }

    public Criticals() {
        super("Criticals", 0, Category.COMBAT, "");
    }

    private boolean canCrit() {
        if (!PlayerUtils.isInLiquid()) {
            if (Minecraft.thePlayer.onGround) {
                return true;
            }
        }
        return false;
    }

    @EventTarget
    public void onMove(EventMotion eventMotion) {
        this.groundTicks = eventMotion.isOnGround() ? this.groundTicks + 1 : 0;
    }

    @Override
    public void setup() {
        ArrayList<String> arrayList = new ArrayList<String>();
        arrayList.add("Packet");
        arrayList.add("MiniJump");
        arrayList.add("AAC");
        arrayList.add("Vanilla");
        Exodus.INSTANCE.settingsManager.addSetting(new Setting("Criticals Mode", (Module)this, "Packet", arrayList));
    }
}

