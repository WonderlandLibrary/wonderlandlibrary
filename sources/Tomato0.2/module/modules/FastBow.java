/*
 * Decompiled with CFR 0_115.
 */
package cow.milkgod.cheese.module.modules;

import com.darkmagician6.eventapi.EventTarget;
import cow.milkgod.cheese.events.EventPreMotionUpdates;
import cow.milkgod.cheese.module.Category;
import cow.milkgod.cheese.module.Module;
import cow.milkgod.cheese.utils.Wrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

public class FastBow
extends Module {
    public FastBow() {
        super("FastBow", 0, Category.COMBAT, 13421568, true, "Makes you shoot cheese faster", new String[]{"fbow", "fb"});
    }

    @EventTarget
    public void onPreMotion(EventPreMotionUpdates event) {
        if (Wrapper.mc.thePlayer.getItemInUse().getItem() instanceof ItemBow && Wrapper.mc.thePlayer.getItemInUseDuration() == 16) {
            int i2 = 0;
            while (i2 < 5) {
                Wrapper.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer(true));
                ++i2;
            }
            Wrapper.mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
            Wrapper.mc.thePlayer.stopUsingItem();
        }
    }
}

