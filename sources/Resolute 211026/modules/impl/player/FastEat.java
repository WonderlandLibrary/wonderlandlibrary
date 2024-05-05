// 
// Decompiled by Procyon v0.5.36
// 

package vip.Resolute.modules.impl.player;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.item.ItemPotion;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.item.ItemFood;
import vip.Resolute.events.impl.EventMotion;
import vip.Resolute.events.Event;
import vip.Resolute.settings.Setting;
import vip.Resolute.settings.impl.BooleanSetting;
import vip.Resolute.settings.impl.ModeSetting;
import vip.Resolute.modules.Module;

public class FastEat extends Module
{
    public ModeSetting mode;
    public BooleanSetting ground;
    
    public FastEat() {
        super("FastEat", 0, "Allows for faster eating", Category.PLAYER);
        this.mode = new ModeSetting("Mode", "Redesky", new String[] { "NCP", "Redesky", "Vanilla" });
        this.ground = new BooleanSetting("On Ground", true);
        this.addSettings(this.mode, this.ground);
    }
    
    @Override
    public void onEvent(final Event e) {
        if (e instanceof EventMotion) {
            final EntityPlayerSP player = FastEat.mc.thePlayer;
            if (this.mode.is("Redesky") && FastEat.mc.thePlayer.getHeldItem() != null && FastEat.mc.gameSettings.keyBindUseItem.pressed && FastEat.mc.thePlayer.getHeldItem().getItem() instanceof ItemFood && (!this.ground.isEnabled() || player.onGround) && player.ticksExisted % 3 == 0) {
                for (int i = 0; i < 5; ++i) {
                    player.sendQueue.addToSendQueue(new C03PacketPlayer.C06PacketPlayerPosLook(player.posX, player.posY, player.posZ, player.rotationYaw, player.rotationPitch, true));
                }
            }
            if (this.mode.is("Vanilla") && FastEat.mc.thePlayer.getHeldItem() != null && (FastEat.mc.thePlayer.getHeldItem().getItem() instanceof ItemFood || FastEat.mc.thePlayer.getHeldItem().getItem() instanceof ItemPotion) && FastEat.mc.thePlayer.isEating() && (!this.ground.isEnabled() || player.onGround)) {
                for (int i = 0; i < 20; ++i) {
                    player.sendQueue.addToSendQueue(new C03PacketPlayer());
                }
            }
            if (this.mode.is("NCP") && FastEat.mc.thePlayer.getHeldItem() != null && FastEat.mc.gameSettings.keyBindUseItem.pressed && FastEat.mc.thePlayer.getHeldItem().getItem() instanceof ItemFood && (!this.ground.isEnabled() || player.onGround) && player.ticksExisted % 4 == 0) {
                for (int i = 0; i < 2; ++i) {
                    player.sendQueue.addToSendQueue(new C03PacketPlayer.C06PacketPlayerPosLook(player.posX, player.posY + 1.0E-9, player.posZ, player.rotationYaw, player.rotationPitch, true));
                }
            }
        }
    }
}
