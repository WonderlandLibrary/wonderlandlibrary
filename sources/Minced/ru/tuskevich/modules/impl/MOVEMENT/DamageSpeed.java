// 
// Decompiled by Procyon v0.5.36
// 

package ru.tuskevich.modules.impl.MOVEMENT;

import net.minecraft.client.Minecraft;
import ru.tuskevich.util.movement.MoveUtility;
import ru.tuskevich.event.events.impl.EventMotion;
import ru.tuskevich.event.EventTarget;
import net.minecraft.network.play.server.SPacketEntityVelocity;
import ru.tuskevich.event.events.impl.EventPacket;
import ru.tuskevich.ui.dropui.setting.Setting;
import ru.tuskevich.ui.dropui.setting.imp.BooleanSetting;
import ru.tuskevich.modules.Type;
import ru.tuskevich.modules.ModuleAnnotation;
import ru.tuskevich.modules.Module;

@ModuleAnnotation(name = "DamageSpeed", desc = "\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd \ufffd\ufffd\ufffd \ufffd\ufffd\ufffd\ufffd\ufffd \ufffd\ufffd\ufffd\ufffd\ufffd", type = Type.MOVEMENT)
public class DamageSpeed extends Module
{
    public BooleanSetting damageCheck;
    boolean isVelocity;
    boolean damage;
    boolean velocity;
    int ticks;
    double motion;
    
    public DamageSpeed() {
        this.damageCheck = new BooleanSetting("Damage Check", true);
        this.add(this.damageCheck);
    }
    
    @EventTarget
    public void onReceivePacket(final EventPacket eventPacket) {
        if (eventPacket.getPacket() instanceof SPacketEntityVelocity) {
            if (((SPacketEntityVelocity)eventPacket.getPacket()).getMotionY() > 0) {
                this.isVelocity = true;
            }
            if (((SPacketEntityVelocity)eventPacket.getPacket()).getMotionY() / 8000.0 > 0.2) {
                this.motion = ((SPacketEntityVelocity)eventPacket.getPacket()).getMotionY() / 8000.0;
                this.velocity = true;
            }
        }
    }
    
    @EventTarget
    public void onMotion(final EventMotion eventMotion) {
        final double radians = MoveUtility.getDirection();
        if (this.damageCheck.get()) {
            final Minecraft mc = DamageSpeed.mc;
            if (Minecraft.player.hurtTime == 9) {
                this.damage = true;
            }
            if (this.damage && this.isVelocity) {
                if (this.velocity) {
                    final Minecraft mc2 = DamageSpeed.mc;
                    if (Minecraft.player.onGround && MoveUtility.isMoving()) {
                        final Minecraft mc3 = DamageSpeed.mc;
                        Minecraft.player.addVelocity(-Math.sin(radians) * 8.0 / 24.5, 0.0, Math.cos(radians) * 8.0 / 24.5);
                        MoveUtility.setStrafe(MoveUtility.getSpeed());
                    }
                    ++this.ticks;
                }
                if (this.ticks >= Math.max(24, 30)) {
                    this.isVelocity = false;
                    this.velocity = false;
                    this.damage = false;
                    this.toggle();
                    this.ticks = 0;
                }
            }
        }
        else {
            final Minecraft mc4 = DamageSpeed.mc;
            if (Minecraft.player.onGround && MoveUtility.isMoving()) {
                final Minecraft mc5 = DamageSpeed.mc;
                Minecraft.player.addVelocity(-Math.sin(radians) * 8.0 / 24.5, 0.0, Math.cos(radians) * 8.0 / 24.5);
                MoveUtility.setStrafe(MoveUtility.getSpeed());
            }
        }
    }
}
