/*
 * Decompiled with CFR 0.150.
 */
package me.wintware.client.module.combat;

import java.util.ArrayList;
import me.wintware.client.Main;
import me.wintware.client.clickgui.settings.Setting;
import me.wintware.client.event.EventTarget;
import me.wintware.client.event.impl.EventAttackPacket;
import me.wintware.client.event.impl.EventReceivePacket;
import me.wintware.client.event.impl.EventStep;
import me.wintware.client.event.impl.EventUpdate;
import me.wintware.client.module.Category;
import me.wintware.client.module.Module;
import me.wintware.client.module.combat.KillAura;
import me.wintware.client.utils.other.TimerHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketConfirmTransaction;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.server.SPacketPlayerPosLook;

public class Criticals
extends Module {
    EntityLivingBase target = KillAura.target;
    int groundTicks;
    int stage;
    int count;
    TimerHelper lastStep = new TimerHelper();
    TimerHelper timer = new TimerHelper();

    public Criticals() {
        super("Criticals", Category.Combat);
        ArrayList<String> mode = new ArrayList<String>();
        mode.add("Packet");
        mode.add("PacketNew");
        Main.instance.setmgr.rSetting(new Setting("Criticals Mode", this, "Packet", mode));
    }

    @EventTarget
    public void onAttackPacket(EventAttackPacket event) {
        String mode = Main.instance.setmgr.getSettingByName("Criticals Mode").getValString();
        if (KillAura.target != null && Main.instance.moduleManager.getModuleByClass(KillAura.class).getState()) {
            double x = Minecraft.player.posX;
            double y = Minecraft.player.posY;
            double z = Minecraft.player.posZ;
            if (mode.equalsIgnoreCase("Packet")) {
                mc.getConnection().sendPacket(new CPacketPlayer.Position(x, y + 0.5, z, true));
                mc.getConnection().sendPacket(new CPacketPlayer.Position(x, y, z, false));
            }
            if (mode.equalsIgnoreCase("PacketNew")) {
                Minecraft.player.setPosition(x, y + 0.04, z);
                Minecraft.player.connection.sendPacket(new CPacketPlayer.Position(Minecraft.player.posX, Minecraft.player.posY + 0.01, Minecraft.player.posZ, true));
                Minecraft.player.connection.sendPacket(new CPacketPlayer.Position(Minecraft.player.posX, Minecraft.player.posY, Minecraft.player.posZ, false));
            }
        }
    }

    @EventTarget
    public void onPacket(EventReceivePacket event) {
        Packet packet = event.getPacket();
        if (packet instanceof SPacketPlayerPosLook) {
            this.stage = 0;
        }
        if (packet instanceof CPacketConfirmTransaction) {
            CPacketConfirmTransaction confirmTransaction = (CPacketConfirmTransaction)packet;
            boolean accepted = confirmTransaction.isAccepted();
            short uid = confirmTransaction.getUid();
            if (accepted && uid == 0) {
                ++this.count;
            }
        }
    }

    @EventTarget
    public void onStep(EventStep event) {
        if (!event.isPre()) {
            this.lastStep.reset();
            if (!Minecraft.player.isCollidedHorizontally) {
                this.stage = 0;
            }
        }
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        String mode = Main.instance.setmgr.getSettingByName("Criticals Mode").getValString();
        this.setSuffix(mode);
    }
}

