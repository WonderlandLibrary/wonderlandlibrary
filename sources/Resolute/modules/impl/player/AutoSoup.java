// 
// Decompiled by Procyon v0.5.36
// 

package vip.Resolute.modules.impl.player;

import net.minecraft.item.ItemSoup;
import net.minecraft.client.entity.EntityPlayerSP;
import vip.Resolute.Resolute;
import net.minecraft.util.EnumFacing;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import vip.Resolute.events.impl.EventMotion;
import vip.Resolute.events.Event;
import vip.Resolute.settings.Setting;
import vip.Resolute.settings.impl.NumberSetting;
import vip.Resolute.modules.Module;

public class AutoSoup extends Module
{
    public NumberSetting health;
    
    public AutoSoup() {
        super("AutoSoup", 0, "", Category.PLAYER);
        this.health = new NumberSetting("Health", 7.0, 2.0, 9.5, 0.5);
        this.addSettings(this.health);
    }
    
    @Override
    public void onEvent(final Event e) {
        if (e instanceof EventMotion) {
            final EntityPlayerSP thePlayer;
            final EntityPlayerSP player = thePlayer = AutoSoup.mc.thePlayer;
            thePlayer.rotationPitch += 1.0E-4f;
            if (player.getHealth() != player.getMaxHealth() && player.getHealth() < this.health.getValue() * 2.0 && this.doesNextSlotHaveSoup() && player.hurtTime >= 9) {
                player.sendQueue.addToSendQueue(new C03PacketPlayer.C06PacketPlayerPosLook(player.posX, player.posY, player.posZ, player.rotationYawHead, 90.0f, player.onGround));
                player.sendQueue.addToSendQueue(new C09PacketHeldItemChange(this.getSlotWithSoup()));
                AutoSoup.mc.playerController.sendUseItem(player, AutoSoup.mc.theWorld, player.inventory.getStackInSlot(this.getSlotWithSoup()));
                new BlockPos(0, 0, 0);
                player.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.DROP_ALL_ITEMS, BlockPos.ORIGIN, EnumFacing.DOWN));
                player.sendQueue.addToSendQueue(new C09PacketHeldItemChange(player.inventory.currentItem));
                AutoSoup.mc.playerController.onStoppedUsingItem(player);
                Resolute.addChatMessage("Consumed Soup");
            }
        }
    }
    
    public boolean doesNextSlotHaveSoup() {
        final EntityPlayerSP player = AutoSoup.mc.thePlayer;
        for (int i = 0; i < 9; ++i) {
            if (player.inventory.getStackInSlot(i) != null && player.inventory.getStackInSlot(i).getItem() instanceof ItemSoup) {
                return true;
            }
        }
        return false;
    }
    
    public int getSlotWithSoup() {
        final EntityPlayerSP player = AutoSoup.mc.thePlayer;
        for (int i = 0; i < 9; ++i) {
            if (player.inventory.getStackInSlot(i) != null && player.inventory.getStackInSlot(i).getItem() instanceof ItemSoup) {
                return i;
            }
        }
        return 0;
    }
}
