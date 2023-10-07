package cc.novoline.modules.player;

import cc.novoline.events.EventTarget;
import cc.novoline.events.events.PacketEvent;
import cc.novoline.events.events.PlayerUpdateEvent;
import cc.novoline.modules.AbstractModule;
import cc.novoline.modules.EnumModuleType;
import cc.novoline.modules.ModuleManager;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import org.checkerframework.checker.nullness.qual.NonNull;

public final class SpeedMine extends AbstractModule {

    private boolean destroy = false;
    private float progress = 0.0f;
    private BlockPos blockPos;
    private EnumFacing facing;

    /* constructors */
    public SpeedMine(@NonNull ModuleManager moduleManager) {
        super(moduleManager, "SpeedMine", "Speed Mine", EnumModuleType.PLAYER, "Speeds up block breaking");
    }

    /* events */

    @EventTarget
    public void onDamageBlock(PacketEvent event) {
        if (event.getState().equals(PacketEvent.State.OUTGOING)) {
            if (mc.playerController.getCurrentGameType().isCreative()) {
                return;
            }

            if (event.getPacket() instanceof C07PacketPlayerDigging && !mc.playerController.extendedReach() && mc.playerController != null) {
                C07PacketPlayerDigging packet = (C07PacketPlayerDigging) event.getPacket();

                if (packet.getStatus() == C07PacketPlayerDigging.Action.START_DESTROY_BLOCK) {
                    destroy = true;
                    blockPos = packet.getPosition();
                    facing = packet.getFacing();
                    progress = 0.0f;

                } else if (packet.getStatus() == C07PacketPlayerDigging.Action.ABORT_DESTROY_BLOCK
                        || packet.getStatus() == C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK) {
                    destroy = false;
                    progress = 0.0f;
                    blockPos = null;
                    facing = null;
                }
            }
        }
    }

    @EventTarget
    public void onUpdate(PlayerUpdateEvent event) {
        if (mc.playerController.getCurrentGameType().isCreative()) {
            return;
        }

        if (mc.playerController.extendedReach()) {
            mc.playerController.blockHitDelay = 0;

        } else if (destroy && mc.playerController.isHittingBlock() && mc.player.canHarvestBlock
                (mc.world.getBlockState(mc.objectMouseOver.getBlockPos()).getBlock())) {
            Block block = mc.world.getBlockState(blockPos).getBlock();
            progress += block.getPlayerRelativeBlockHardness(mc.player, mc.world, blockPos) * 1.4F;

            if (progress >= 1.0f) {
                mc.world.setBlockState(blockPos, Blocks.air.getDefaultState(), 11);
                sendPacketNoEvent(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK, blockPos, facing));
            }
        }
    }

    @Override
    public void onDisable() {
        progress = 0.0f;
        destroy = false;
        blockPos = null;
        facing = null;
    }
}
