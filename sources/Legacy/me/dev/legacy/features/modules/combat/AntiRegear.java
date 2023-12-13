package me.dev.legacy.features.modules.combat;

import me.dev.legacy.event.events.PacketEvent;
import me.dev.legacy.event.events.UpdateEvent;
import me.dev.legacy.features.modules.Module;
import me.dev.legacy.features.setting.Setting;
import me.dev.legacy.util.BlockUtil;
import net.minecraft.block.BlockShulkerBox;
import net.minecraft.item.ItemShulkerBox;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.List;

public class AntiRegear extends Module {
    public AntiRegear() {
        super("AntiRegear", "AntiRegear.", Module.Category.COMBAT, true, false, false);
    }

    private final Setting<Float> reach = this.register(new Setting<Float>("Reach", 5f, 1f, 6f));
    private final Setting<Integer> retry = this.register(new Setting<Integer>("Retry Delay", 10, 0, 20));

    private final List<BlockPos> retries = new ArrayList<>();
    private final List<BlockPos> selfPlaced = new ArrayList<>();

    private int ticks;

    @SubscribeEvent
    public void onUpdate(UpdateEvent event) {
        if (ticks++ < retry.getValue()) {
            ticks = 0;
            retries.clear();
        }

        final List<BlockPos> sphere = BlockUtil.getSphere(reach.getValue());
        final int size = sphere.size();
        for (int i = 0; i < size; ++i) {
            final BlockPos pos = sphere.get(i);
            if (retries.contains(pos) || selfPlaced.contains(pos)) {
                continue;
            }
            if (mc.world.getBlockState(pos).getBlock() instanceof BlockShulkerBox) {
                mc.player.swingArm(EnumHand.MAIN_HAND);
                mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.START_DESTROY_BLOCK, pos, EnumFacing.UP));
                mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, pos, EnumFacing.UP));
                retries.add(pos);
            }
        }
    }

    @SubscribeEvent
    public void onPacketSend(PacketEvent.Send event) {
        if (event.getPacket() instanceof CPacketPlayerTryUseItemOnBlock) {
            final CPacketPlayerTryUseItemOnBlock packet = (CPacketPlayerTryUseItemOnBlock) event.getPacket();
            if (mc.player.getHeldItem(packet.getHand()).getItem() instanceof ItemShulkerBox) {
                selfPlaced.add(packet.getPos().offset(packet.getDirection()));
            }
        }
    }
}
