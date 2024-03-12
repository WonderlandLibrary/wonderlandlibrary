// 
// Decompiled by Procyon v0.5.36
// 

package dev.tenacity.module.impl.combat;

import dev.tenacity.module.settings.Setting;
import dev.tenacity.module.Category;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.Packet;
import dev.tenacity.utils.server.PacketUtils;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.item.ItemBow;
import org.lwjgl.input.Mouse;
import dev.tenacity.event.impl.player.MotionEvent;
import dev.tenacity.utils.time.TimerUtil;
import dev.tenacity.module.settings.impl.NumberSetting;
import dev.tenacity.module.settings.impl.ModeSetting;
import dev.tenacity.module.Module;

public final class FastBow extends Module
{
    private final ModeSetting mode;
    private final NumberSetting shotDelay;
    private final TimerUtil delayTimer;
    
    @Override
    public void onMotionEvent(final MotionEvent event) {
        if (FastBow.mc.thePlayer.getCurrentEquippedItem() == null) {
            return;
        }
        if (this.delayTimer.hasTimeElapsed(this.shotDelay.getValue().longValue() * 250L)) {
            final String mode = this.mode.getMode();
            switch (mode) {
                case "Vanilla": {
                    if (Mouse.isButtonDown(1) && FastBow.mc.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemBow) {
                        for (int i = 0; i < 20; ++i) {
                            PacketUtils.sendPacketNoEvent(new C03PacketPlayer(true));
                        }
                        FastBow.mc.rightClickDelayTimer = 0;
                        FastBow.mc.playerController.onStoppedUsingItem(FastBow.mc.thePlayer);
                        break;
                    }
                    break;
                }
                case "Ghostly": {
                    if (Mouse.isButtonDown(1) && FastBow.mc.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemBow) {
                        for (int i = 0; i < 20; ++i) {
                            PacketUtils.sendPacketNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(FastBow.mc.thePlayer.posX, FastBow.mc.thePlayer.posY, FastBow.mc.thePlayer.posZ, FastBow.mc.thePlayer.onGround));
                        }
                        FastBow.mc.rightClickDelayTimer = 0;
                        FastBow.mc.playerController.onStoppedUsingItem(FastBow.mc.thePlayer);
                        break;
                    }
                    break;
                }
            }
            this.delayTimer.reset();
        }
    }
    
    @Override
    public void onDisable() {
        FastBow.mc.rightClickDelayTimer = 4;
        super.onDisable();
    }
    
    public FastBow() {
        super("FastBow", "Fast Bow", Category.COMBAT, "shoot bows faster");
        this.mode = new ModeSetting("Mode", "Vanilla", new String[] { "Vanilla", "Ghostly" });
        this.shotDelay = new NumberSetting("Shot Delay", 0.0, 2.0, 0.0, 0.1);
        this.delayTimer = new TimerUtil();
        this.addSettings(this.mode, this.shotDelay);
    }
}
