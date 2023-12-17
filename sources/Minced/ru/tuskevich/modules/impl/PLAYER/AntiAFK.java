// 
// Decompiled by Procyon v0.5.36
// 

package ru.tuskevich.modules.impl.PLAYER;

import ru.tuskevich.event.EventTarget;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.Minecraft;
import ru.tuskevich.event.events.impl.EventMotion;
import ru.tuskevich.ui.dropui.setting.Setting;
import ru.tuskevich.ui.dropui.setting.imp.BooleanSetting;
import ru.tuskevich.modules.Type;
import ru.tuskevich.modules.ModuleAnnotation;
import ru.tuskevich.modules.Module;

@ModuleAnnotation(name = "AntiAFK", desc = "\ufffd\ufffd \ufffd\ufffd\ufffd\ufffd \ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd \ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd \ufffd\ufffd\ufffd \ufffd\ufffd \ufffd\ufffd\ufffd", type = Type.PLAYER)
public class AntiAFK extends Module
{
    private final BooleanSetting spin;
    private final BooleanSetting jump;
    private final BooleanSetting message;
    private final BooleanSetting click;
    
    public AntiAFK() {
        this.spin = new BooleanSetting("Spin", true);
        this.jump = new BooleanSetting("Jump", true);
        this.message = new BooleanSetting("Send Message", true);
        this.click = new BooleanSetting("Click", true);
        this.add(this.spin, this.jump, this.message, this.click);
    }
    
    @EventTarget
    public void onMotion(final EventMotion e) {
        final Minecraft mc = AntiAFK.mc;
        if (!Minecraft.player.isDead) {
            final Minecraft mc2 = AntiAFK.mc;
            if (Minecraft.player.getHealth() > 0.0f) {
                if (this.spin.get()) {
                    final Minecraft mc3 = AntiAFK.mc;
                    if (Minecraft.player.ticksExisted % 60 == 0) {
                        final Minecraft mc4 = AntiAFK.mc;
                        final EntityPlayerSP player = Minecraft.player;
                        player.rotationYaw += 300.0f;
                    }
                }
                if (this.jump.get()) {
                    final Minecraft mc5 = AntiAFK.mc;
                    if (Minecraft.player.ticksExisted % 40 == 0 && !AntiAFK.mc.gameSettings.keyBindJump.pressed) {
                        final Minecraft mc6 = AntiAFK.mc;
                        if (Minecraft.player.onGround) {
                            final Minecraft mc7 = AntiAFK.mc;
                            Minecraft.player.jump();
                        }
                    }
                }
                if (this.message.get()) {
                    final Minecraft mc8 = AntiAFK.mc;
                    if (Minecraft.player.ticksExisted % 400 == 0) {
                        final Minecraft mc9 = AntiAFK.mc;
                        Minecraft.player.sendChatMessage("/\u044f\u0433\u0435\u0439\u044f\u0433\u0435\u0439");
                    }
                }
                if (this.click.get()) {
                    final Minecraft mc10 = AntiAFK.mc;
                    if (Minecraft.player.ticksExisted % 60 == 0) {
                        AntiAFK.mc.clickMouse();
                    }
                }
            }
        }
    }
}
