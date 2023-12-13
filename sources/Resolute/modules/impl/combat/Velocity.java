// 
// Decompiled by Procyon v0.5.36
// 

package vip.Resolute.modules.impl.combat;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.network.play.server.S27PacketExplosion;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import vip.Resolute.events.impl.EventPacket;
import vip.Resolute.events.impl.EventMotion;
import vip.Resolute.events.impl.EventUpdate;
import vip.Resolute.events.Event;
import vip.Resolute.settings.Setting;
import vip.Resolute.util.misc.TimerUtils;
import vip.Resolute.settings.impl.NumberSetting;
import vip.Resolute.settings.impl.ModeSetting;
import vip.Resolute.modules.Module;

public class Velocity extends Module
{
    public ModeSetting mode;
    public NumberSetting horizontal;
    public NumberSetting vertical;
    public NumberSetting reduce;
    TimerUtils timer;
    private int hitTimes;
    private boolean hurt;
    
    public boolean isModeSelected() {
        return this.mode.is("Percentage");
    }
    
    public boolean isReduceSelected() {
        return this.mode.is("Reduce");
    }
    
    public Velocity() {
        super("Velocity", 0, "Adjusts player velocity", Category.COMBAT);
        this.mode = new ModeSetting("Mode", "Packet", new String[] { "Packet", "Reduce", "Matrix", "Stack", "AACP" });
        this.horizontal = new NumberSetting("Horizontal", 0.0, this::isModeSelected, 0.0, 100.0, 1.0);
        this.vertical = new NumberSetting("Vertical", 0.0, this::isModeSelected, 0.0, 100.0, 1.0);
        this.reduce = new NumberSetting("Reduce", 0.7, this::isReduceSelected, 0.1, 1.0, 0.05);
        this.timer = new TimerUtils();
        this.hitTimes = 0;
        this.hurt = false;
        this.addSettings(this.mode, this.horizontal, this.vertical, this.reduce);
    }
    
    @Override
    public void onEnable() {
        this.hitTimes = 0;
    }
    
    @Override
    public void onDisable() {
        Velocity.mc.timer.timerSpeed = 1.0f;
        Velocity.mc.thePlayer.speedInAir = 0.02f;
    }
    
    @Override
    public void onEvent(final Event e) {
        this.setSuffix(this.mode.getMode());
        if (e instanceof EventUpdate) {
            if (this.mode.is("AACP")) {
                if (!Velocity.mc.thePlayer.onGround) {
                    if (Velocity.mc.thePlayer.hurtTime != 0 && this.hurt) {
                        Velocity.mc.thePlayer.speedInAir = 0.02f;
                        final EntityPlayerSP thePlayer = Velocity.mc.thePlayer;
                        thePlayer.motionX *= 0.699999988079071;
                        final EntityPlayerSP thePlayer2 = Velocity.mc.thePlayer;
                        thePlayer2.motionZ *= 0.699999988079071;
                    }
                }
                else if (this.timer.hasReached(80.0)) {
                    this.hurt = false;
                    Velocity.mc.thePlayer.speedInAir = 0.02f;
                }
            }
            if (this.mode.is("Reduce") && Velocity.mc.thePlayer.hurtTime > 4 && Velocity.mc.thePlayer.hurtTime < 9) {
                final EntityPlayerSP thePlayer3 = Velocity.mc.thePlayer;
                thePlayer3.motionX *= this.reduce.getValue();
                final EntityPlayerSP thePlayer4 = Velocity.mc.thePlayer;
                thePlayer4.motionZ *= this.reduce.getValue();
            }
        }
        if (e instanceof EventMotion && this.mode.is("Matrix") && Velocity.mc.thePlayer.hurtTime > 0) {
            if (Velocity.mc.thePlayer.onGround) {
                if (Velocity.mc.thePlayer.hurtTime <= 6) {
                    final EntityPlayerSP thePlayer5 = Velocity.mc.thePlayer;
                    thePlayer5.motionX *= 0.7;
                    final EntityPlayerSP thePlayer6 = Velocity.mc.thePlayer;
                    thePlayer6.motionZ *= 0.7;
                }
                if (Velocity.mc.thePlayer.hurtTime <= 5) {
                    final EntityPlayerSP thePlayer7 = Velocity.mc.thePlayer;
                    thePlayer7.motionX *= 0.8;
                    final EntityPlayerSP thePlayer8 = Velocity.mc.thePlayer;
                    thePlayer8.motionZ *= 0.8;
                }
            }
            else if (Velocity.mc.thePlayer.hurtTime <= 10) {
                final EntityPlayerSP thePlayer9 = Velocity.mc.thePlayer;
                thePlayer9.motionX *= 0.6;
                final EntityPlayerSP thePlayer10 = Velocity.mc.thePlayer;
                thePlayer10.motionZ *= 0.6;
            }
        }
        if (e instanceof EventPacket) {
            if (this.mode.is("Packet") && ((EventPacket)e).getPacket() instanceof S12PacketEntityVelocity) {
                final S12PacketEntityVelocity packet = ((EventPacket)e).getPacket();
                if (packet.getEntityID() == Velocity.mc.thePlayer.getEntityId()) {
                    final double horiz = this.horizontal.getValue();
                    final double vert = this.vertical.getValue();
                    if (horiz == 0.0 && vert == 0.0) {
                        e.setCancelled(true);
                    }
                    else {
                        final S12PacketEntityVelocity s12PacketEntityVelocity = packet;
                        s12PacketEntityVelocity.motionX *= (int)(horiz / 100.0);
                        final S12PacketEntityVelocity s12PacketEntityVelocity2 = packet;
                        s12PacketEntityVelocity2.motionY *= (int)(vert / 100.0);
                        final S12PacketEntityVelocity s12PacketEntityVelocity3 = packet;
                        s12PacketEntityVelocity3.motionZ *= (int)(horiz / 100.0);
                    }
                }
            }
            if (((EventPacket)e).getPacket() instanceof S12PacketEntityVelocity && this.mode.is("AAC5.2.0")) {
                e.setCancelled(true);
                Velocity.mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(Velocity.mc.thePlayer.posX, Double.MAX_VALUE, Velocity.mc.thePlayer.posZ, true));
            }
            if (((EventPacket)e).getPacket() instanceof S12PacketEntityVelocity && this.mode.is("AACP")) {
                if (Velocity.mc.thePlayer == null) {
                    return;
                }
                this.timer.reset();
                this.hurt = true;
            }
            if (((EventPacket)e).getPacket() instanceof S12PacketEntityVelocity && this.mode.is("Stack")) {
                final S12PacketEntityVelocity packet = ((EventPacket)e).getPacket();
                if (packet.getEntityID() == Velocity.mc.thePlayer.getEntityId()) {
                    if (this.hitTimes != 2) {
                        e.setCancelled(true);
                        ++this.hitTimes;
                    }
                    else {
                        this.hitTimes = 0;
                    }
                }
            }
            if (((EventPacket)e).getPacket() instanceof S27PacketExplosion) {
                e.setCancelled(true);
            }
        }
    }
}
