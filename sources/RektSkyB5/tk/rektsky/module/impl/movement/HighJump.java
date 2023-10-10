/*
 * Decompiled with CFR 0.152.
 */
package tk.rektsky.module.impl.movement;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.util.BlockPos;
import org.greenrobot.eventbus.Subscribe;
import tk.rektsky.event.impl.WorldTickEvent;
import tk.rektsky.module.Category;
import tk.rektsky.module.Module;
import tk.rektsky.module.settings.DoubleSetting;

public class HighJump
extends Module {
    public DoubleSetting height = new DoubleSetting("Height", 0.1, 8.0, 2.0);

    public HighJump() {
        super("HighJump", "Makes you jump high", Category.MOVEMENT);
    }

    @Override
    public void onEnable() {
    }

    @Override
    public void onDisable() {
        this.mc.thePlayer.motionY = 0.0;
    }

    @Subscribe
    public void onTick(WorldTickEvent event) {
        this.mc.getNetHandler().addToSendQueueSilent(new C08PacketPlayerBlockPlacement(new BlockPos(-1, -1, -1), 255, new ItemStack(Items.water_bucket), 0.0f, 0.5f, 0.0f));
        this.mc.getNetHandler().addToSendQueueSilent(new C08PacketPlayerBlockPlacement(new BlockPos(this.mc.thePlayer.posX, this.mc.thePlayer.posY - 1.0, this.mc.thePlayer.posZ), 1, new ItemStack(Blocks.stone.getItem(this.mc.theWorld, new BlockPos(-1, -1, -1))), 0.0f, 0.9f, 0.0f));
    }
}

