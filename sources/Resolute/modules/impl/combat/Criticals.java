// 
// Decompiled by Procyon v0.5.36
// 

package vip.Resolute.modules.impl.combat;

import vip.Resolute.util.world.RandomUtil;
import net.minecraft.network.Packet;
import vip.Resolute.util.movement.MovementUtils;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.entity.Entity;
import net.minecraft.network.play.client.C03PacketPlayer;
import vip.Resolute.events.impl.EventPacket;
import vip.Resolute.events.impl.EventMotion;
import vip.Resolute.events.Event;
import vip.Resolute.settings.Setting;
import vip.Resolute.settings.impl.NumberSetting;
import vip.Resolute.settings.impl.ModeSetting;
import vip.Resolute.util.misc.TimerUtils;
import vip.Resolute.modules.Module;

public class Criticals extends Module
{
    private int groundTicks;
    TimerUtils timer;
    private final double[] ncpOffsets;
    private final double[] watchdogOffsets;
    public ModeSetting mode;
    public ModeSetting watchdogMode;
    public NumberSetting delay;
    
    public boolean isModeSelected() {
        return this.mode.is("Watchdog");
    }
    
    public Criticals() {
        super("Criticals", 0, "Allows for a critical every hit", Category.COMBAT);
        this.timer = new TimerUtils();
        this.ncpOffsets = new double[] { 0.06251999735832214, 0.0 };
        this.watchdogOffsets = new double[] { 0.0560000017285347, 0.01600000075995922, 0.003000000026077032 };
        this.mode = new ModeSetting("Mode", "Watchdog", new String[] { "Watchdog", "Watchdog", "Packet", "Ground", "NoGround" });
        this.watchdogMode = new ModeSetting("Watchdog Mode", "Packet 1", this::isModeSelected, new String[] { "Packet 1", "Packet 2", "Packet 3", "Packet 4" });
        this.delay = new NumberSetting("Delay", 500.0, 0.0, 2000.0, 10.0);
        this.addSettings(this.mode, this.watchdogMode, this.delay);
    }
    
    @Override
    public void onEvent(final Event e) {
        if (this.mode.is("Watchdog")) {
            this.setSuffix(this.watchdogMode.getMode());
        }
        else {
            this.setSuffix(this.mode.getMode());
        }
        if (e instanceof EventMotion) {
            final EventMotion eventMotion = (EventMotion)e;
            if (this.mode.is("NoGround") && Criticals.mc.thePlayer.fallDistance <= 3.0f) {
                eventMotion.setOnGround(false);
            }
        }
        if (e instanceof EventPacket) {
            if (((EventPacket)e).getPacket() instanceof C03PacketPlayer) {
                final C03PacketPlayer c03PacketPlayer = ((EventPacket)e).getPacket();
                if (this.mode.is("Ground")) {
                    if (Criticals.mc.thePlayer.fallDistance > 0.0f) {
                        c03PacketPlayer.onGround = true;
                    }
                    if (Criticals.mc.thePlayer.onGround && KillAura.target != null && (((EventPacket)e).getPacket() instanceof C03PacketPlayer.C04PacketPlayerPosition || ((EventPacket)e).getPacket() instanceof C03PacketPlayer.C06PacketPlayerPosLook)) {
                        c03PacketPlayer.onGround = false;
                        Criticals.mc.thePlayer.onCriticalHit(KillAura.target);
                    }
                }
            }
            if (((EventPacket)e).getPacket() instanceof C0APacketAnimation) {
                if (this.mode.is("Packet") && this.timer.hasTimeElapsed((long)this.delay.getValue(), true) && MovementUtils.isOnGround() && KillAura.target != null) {
                    Criticals.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(Criticals.mc.thePlayer.posX, Criticals.mc.thePlayer.posY + 0.1625, Criticals.mc.thePlayer.posZ, false));
                    Criticals.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(Criticals.mc.thePlayer.posX, Criticals.mc.thePlayer.posY, Criticals.mc.thePlayer.posZ, false));
                    Criticals.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(Criticals.mc.thePlayer.posX, Criticals.mc.thePlayer.posY + 4.0E-6, Criticals.mc.thePlayer.posZ, false));
                    Criticals.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(Criticals.mc.thePlayer.posX, Criticals.mc.thePlayer.posY, Criticals.mc.thePlayer.posZ, false));
                    Criticals.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(Criticals.mc.thePlayer.posX, Criticals.mc.thePlayer.posY + 1.0E-6, Criticals.mc.thePlayer.posZ, false));
                    Criticals.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(Criticals.mc.thePlayer.posX, Criticals.mc.thePlayer.posY, Criticals.mc.thePlayer.posZ, false));
                    Criticals.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer());
                    Criticals.mc.thePlayer.onCriticalHit(KillAura.target);
                }
                if (this.mode.is("Watchdog")) {
                    if (!Criticals.mc.thePlayer.onGround || Criticals.mc.thePlayer.isOnLadder() || Criticals.mc.thePlayer.isInWeb || Criticals.mc.thePlayer.isInWater() || Criticals.mc.thePlayer.isInLava() || Criticals.mc.thePlayer.ridingEntity != null) {
                        return;
                    }
                    if (this.watchdogMode.is("Packet 1") && this.timer.hasTimeElapsed((long)this.delay.getValue(), true)) {
                        for (final double offset : this.watchdogOffsets) {
                            if (MovementUtils.isOnGround() && KillAura.target != null) {
                                Criticals.mc.thePlayer.sendQueue.sendPacketNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(Criticals.mc.thePlayer.posX, Criticals.mc.thePlayer.posY + offset + Math.random() * 3.000000142492354E-4, Criticals.mc.thePlayer.posZ, false));
                                Criticals.mc.thePlayer.onCriticalHit(KillAura.target);
                            }
                        }
                    }
                    if (this.watchdogMode.is("Packet 2") && this.timer.hasTimeElapsed((long)this.delay.getValue(), true)) {
                        for (final double offset : this.watchdogOffsets) {
                            if (MovementUtils.isOnGround() && KillAura.target != null) {
                                Criticals.mc.thePlayer.sendQueue.sendPacketNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(Criticals.mc.thePlayer.posX, Criticals.mc.thePlayer.posY + offset + 0.045, Criticals.mc.thePlayer.posZ, false));
                                Criticals.mc.thePlayer.onCriticalHit(KillAura.target);
                            }
                        }
                    }
                    if (this.watchdogMode.is("Packet 3")) {
                        final double random = RandomUtil.getRandom(4.0E-7, 4.0E-5);
                        if (this.timer.hasTimeElapsed((long)this.delay.getValue(), true)) {
                            final double[] array;
                            final double[] arrayOfDouble = array = new double[] { 0.007017625 + random, 0.007349825 + random, 0.006102874 + random };
                            for (final double value : array) {
                                if (MovementUtils.isOnGround() && KillAura.target != null) {
                                    Criticals.mc.thePlayer.sendQueue.sendPacketNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(Criticals.mc.thePlayer.posX, Criticals.mc.thePlayer.posY + value, Criticals.mc.thePlayer.posZ, false));
                                    Criticals.mc.thePlayer.onCriticalHit(KillAura.target);
                                }
                            }
                        }
                    }
                    if (this.watchdogMode.is("Packet 4") && this.timer.hasTimeElapsed((long)this.delay.getValue(), true) && MovementUtils.isOnGround() && KillAura.target != null) {
                        for (int i = 0; i <= 2; ++i) {
                            Criticals.mc.thePlayer.sendQueue.sendPacketNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(Criticals.mc.thePlayer.posX, Criticals.mc.thePlayer.posY + 0.101 - i * 0.02, Criticals.mc.thePlayer.posZ, false));
                            Criticals.mc.thePlayer.onCriticalHit(KillAura.target);
                        }
                    }
                }
            }
        }
    }
}
