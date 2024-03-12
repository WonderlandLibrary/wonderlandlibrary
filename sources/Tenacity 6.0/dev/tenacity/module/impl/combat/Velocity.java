// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.module.impl.combat;

import dev.tenacity.ui.notifications.NotificationManager;
import dev.tenacity.ui.notifications.NotificationType;
import dev.tenacity.event.impl.game.WorldEvent;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S19PacketEntityStatus;
import net.minecraft.network.play.server.S27PacketExplosion;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import dev.tenacity.utils.misc.MathUtils;
import dev.tenacity.utils.player.MovementUtils;
import dev.tenacity.event.impl.network.PacketReceiveEvent;
import net.minecraft.network.play.client.C0FPacketConfirmTransaction;
import dev.tenacity.event.impl.network.PacketSendEvent;
import dev.tenacity.module.settings.Setting;
import dev.tenacity.module.Category;
import dev.tenacity.module.settings.impl.BooleanSetting;
import dev.tenacity.module.settings.impl.NumberSetting;
import dev.tenacity.module.settings.impl.ModeSetting;
import dev.tenacity.module.Module;

public class Velocity extends Module
{
    private final ModeSetting mode;
    private final NumberSetting horizontal;
    private final NumberSetting vertical;
    private final NumberSetting chance;
    private final BooleanSetting onlyWhileMoving;
    private final BooleanSetting staffCheck;
    private long lastDamageTimestamp;
    private long lastAlertTimestamp;
    private boolean cancel;
    private int stack;
    
    public Velocity() {
        super("Velocity", "Velocity", Category.COMBAT, "Reduces your knockback");
        this.mode = new ModeSetting("Mode", "Packet", new String[] { "Packet", "Matrix", "Tick", "Stack", "C0F Cancel" });
        this.horizontal = new NumberSetting("Horizontal", 0.0, 100.0, 0.0, 1.0);
        this.vertical = new NumberSetting("Vertical", 0.0, 100.0, 0.0, 1.0);
        this.chance = new NumberSetting("Chance", 100.0, 100.0, 0.0, 1.0);
        this.onlyWhileMoving = new BooleanSetting("Only while moving", false);
        this.staffCheck = new BooleanSetting("Staff check", false);
        Setting.addParent(this.mode, m -> m.is("Packet"), this.horizontal, this.vertical, this.staffCheck);
        this.addSettings(this.mode, this.horizontal, this.vertical, this.chance, this.onlyWhileMoving, this.staffCheck);
    }
    
    @Override
    public void onPacketSendEvent(final PacketSendEvent event) {
        if (this.mode.is("C0F Cancel") && event.getPacket() instanceof C0FPacketConfirmTransaction && Velocity.mc.thePlayer.hurtTime > 0) {
            event.cancel();
        }
    }
    
    @Override
    public void onPacketReceiveEvent(final PacketReceiveEvent e) {
        this.setSuffix(this.mode.getMode());
        if ((this.onlyWhileMoving.isEnabled() && !MovementUtils.isMoving()) || (this.chance.getValue() != 100.0 && MathUtils.getRandomInRange(0, 100) > this.chance.getValue())) {
            return;
        }
        final Packet<?> packet = e.getPacket();
        final String mode = this.mode.getMode();
        switch (mode) {
            case "Packet": {
                if (packet instanceof S12PacketEntityVelocity) {
                    final S12PacketEntityVelocity s12 = (S12PacketEntityVelocity)e.getPacket();
                    if (Velocity.mc.thePlayer != null && s12.getEntityID() == Velocity.mc.thePlayer.getEntityId()) {
                        if (this.cancel(e)) {
                            return;
                        }
                        final S12PacketEntityVelocity s12PacketEntityVelocity = s12;
                        s12PacketEntityVelocity.motionX *= (int)(this.horizontal.getValue() / 100.0);
                        final S12PacketEntityVelocity s12PacketEntityVelocity2 = s12;
                        s12PacketEntityVelocity2.motionZ *= (int)(this.horizontal.getValue() / 100.0);
                        final S12PacketEntityVelocity s12PacketEntityVelocity3 = s12;
                        s12PacketEntityVelocity3.motionY *= (int)(this.vertical.getValue() / 100.0);
                    }
                    break;
                }
                if (packet instanceof S27PacketExplosion) {
                    if (this.cancel(e)) {
                        return;
                    }
                    final S27PacketExplosion s27PacketExplosion;
                    final S27PacketExplosion s13 = s27PacketExplosion = (S27PacketExplosion)e.getPacket();
                    s27PacketExplosion.motionX *= (float)(this.horizontal.getValue() / 100.0);
                    final S27PacketExplosion s27PacketExplosion2 = s13;
                    s27PacketExplosion2.motionZ *= (float)(this.horizontal.getValue() / 100.0);
                    final S27PacketExplosion s27PacketExplosion3 = s13;
                    s27PacketExplosion3.motionY *= (float)(this.vertical.getValue() / 100.0);
                    break;
                }
                else {
                    if (e.getPacket() instanceof S19PacketEntityStatus) {
                        final S19PacketEntityStatus s14 = (S19PacketEntityStatus)e.getPacket();
                        if (Velocity.mc.thePlayer != null && s14.getEntityId() == Velocity.mc.thePlayer.getEntityId() && s14.getOpCode() == 2) {
                            this.lastDamageTimestamp = System.currentTimeMillis();
                        }
                        break;
                    }
                    break;
                }
                break;
            }
            case "C0F Cancel": {
                if (packet instanceof S12PacketEntityVelocity) {
                    final S12PacketEntityVelocity s12 = (S12PacketEntityVelocity)e.getPacket();
                    if (Velocity.mc.thePlayer != null && s12.getEntityID() == Velocity.mc.thePlayer.getEntityId()) {
                        e.cancel();
                    }
                }
                if (packet instanceof S27PacketExplosion) {
                    e.cancel();
                    break;
                }
                break;
            }
            case "Stack": {
                if (packet instanceof S12PacketEntityVelocity) {
                    final S12PacketEntityVelocity s12 = (S12PacketEntityVelocity)packet;
                    this.cancel = !this.cancel;
                    if (this.cancel) {
                        e.cancel();
                    }
                }
                if (packet instanceof S27PacketExplosion) {
                    e.cancel();
                    break;
                }
                break;
            }
            case "Matrix": {
                if (packet instanceof S12PacketEntityVelocity) {
                    final S12PacketEntityVelocity s12 = (S12PacketEntityVelocity)e.getPacket();
                    if (Velocity.mc.thePlayer != null && s12.getEntityID() == Velocity.mc.thePlayer.getEntityId()) {
                        final S12PacketEntityVelocity s12PacketEntityVelocity4 = s12;
                        s12PacketEntityVelocity4.motionX *= (int)0.05;
                        final S12PacketEntityVelocity s12PacketEntityVelocity5 = s12;
                        s12PacketEntityVelocity5.motionZ *= (int)0.05;
                        final S12PacketEntityVelocity s12PacketEntityVelocity6 = s12;
                        s12PacketEntityVelocity6.motionY *= (int)1.0;
                    }
                    break;
                }
                break;
            }
            case "Tick": {
                if (!(packet instanceof S12PacketEntityVelocity)) {
                    break;
                }
                final S12PacketEntityVelocity s12 = (S12PacketEntityVelocity)e.getPacket();
                if (Velocity.mc.thePlayer != null && s12.getEntityID() == Velocity.mc.thePlayer.getEntityId() && Velocity.mc.thePlayer.ticksExisted % 3 == 0) {
                    final S12PacketEntityVelocity s12PacketEntityVelocity7 = s12;
                    s12PacketEntityVelocity7.motionX *= (int)0.05;
                    final S12PacketEntityVelocity s12PacketEntityVelocity8 = s12;
                    s12PacketEntityVelocity8.motionZ *= (int)0.05;
                    final S12PacketEntityVelocity s12PacketEntityVelocity9 = s12;
                    s12PacketEntityVelocity9.motionY *= (int)1.0;
                    break;
                }
                break;
            }
        }
    }
    
    @Override
    public void onWorldEvent(final WorldEvent event) {
        this.stack = 0;
    }
    
    private boolean cancel(final PacketReceiveEvent e) {
        if (this.staffCheck.isEnabled() && System.currentTimeMillis() - this.lastDamageTimestamp > 500L) {
            if (System.currentTimeMillis() - this.lastAlertTimestamp > 250L) {
                NotificationManager.post(NotificationType.WARNING, "Velocity", "Suspicious knockback detected!", 2.0f);
                this.lastAlertTimestamp = System.currentTimeMillis();
            }
            return true;
        }
        if (this.horizontal.getValue() == 0.0 && this.vertical.getValue() == 0.0) {
            e.cancel();
            return true;
        }
        return false;
    }
}
