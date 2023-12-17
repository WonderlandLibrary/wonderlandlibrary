// 
// Decompiled by Procyon v0.5.36
// 

package ru.tuskevich.modules.impl.MOVEMENT;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.Minecraft;
import ru.tuskevich.util.Utility;
import ru.tuskevich.event.events.impl.EventUpdate;
import ru.tuskevich.event.EventTarget;
import net.minecraft.network.play.client.CPacketPlayer;
import ru.tuskevich.event.events.impl.EventPacket;
import ru.tuskevich.ui.dropui.setting.Setting;
import ru.tuskevich.ui.dropui.setting.imp.ModeSetting;
import ru.tuskevich.ui.dropui.setting.imp.SliderSetting;
import ru.tuskevich.modules.Type;
import ru.tuskevich.modules.ModuleAnnotation;
import ru.tuskevich.modules.Module;

@ModuleAnnotation(name = "NoSlowDown", desc = "\ufffd\ufffd \ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd \ufffd\ufffd\ufffd \ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd \ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd\ufffd", type = Type.MOVEMENT)
public class NoSlow extends Module
{
    public static SliderSetting value;
    private final ModeSetting noSlowMode;
    public int usingTicks;
    
    public NoSlow() {
        this.noSlowMode = new ModeSetting("NoSlow Mode", "Matrix", () -> true, new String[] { "Vanilla", "Matrix" });
        this.add(this.noSlowMode, NoSlow.value);
    }
    
    @EventTarget
    public void onSendPacket(final EventPacket eventSendPacket) {
        final CPacketPlayer packet = (CPacketPlayer)eventSendPacket.getPacket();
    }
    
    @EventTarget
    public void onUpdate(final EventUpdate event) {
        final Minecraft mc = Utility.mc;
        this.usingTicks = (Minecraft.player.isUsingItem() ? (++this.usingTicks) : 0);
        if (this.noSlowMode.isVisible()) {
            final Minecraft mc2 = Utility.mc;
            if (Minecraft.player.isUsingItem()) {
                if (this.noSlowMode.currentMode.equals("Matrix")) {
                    final Minecraft mc3 = Utility.mc;
                    if (Minecraft.player.isUsingItem()) {
                        final Minecraft mc4 = Utility.mc;
                        if (Minecraft.player.onGround && !Utility.mc.gameSettings.keyBindJump.isKeyDown()) {
                            final Minecraft mc5 = Utility.mc;
                            if (Minecraft.player.ticksExisted % 2 == 0) {
                                final Minecraft mc6 = Utility.mc;
                                final EntityPlayerSP player = Minecraft.player;
                                player.motionX *= 0.35;
                                final Minecraft mc7 = Utility.mc;
                                final EntityPlayerSP player2 = Minecraft.player;
                                player2.motionZ *= 0.35;
                            }
                        }
                        else {
                            final Minecraft mc8 = Utility.mc;
                            if (Minecraft.player.fallDistance > 0.2) {
                                final Minecraft mc9 = Utility.mc;
                                final EntityPlayerSP player3 = Minecraft.player;
                                player3.motionX *= 0.9100000262260437;
                                final Minecraft mc10 = Utility.mc;
                                final EntityPlayerSP player4 = Minecraft.player;
                                player4.motionZ *= 0.9100000262260437;
                            }
                        }
                    }
                }
            }
        }
    }
    
    static {
        NoSlow.value = new SliderSetting("Value", 1.0f, 0.0f, 1.0f, 0.01f);
    }
}
