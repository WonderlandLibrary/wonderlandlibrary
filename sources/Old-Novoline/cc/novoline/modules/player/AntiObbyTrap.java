package cc.novoline.modules.player;

import cc.novoline.events.EventTarget;
import cc.novoline.events.events.MotionUpdateEvent;
import cc.novoline.modules.AbstractModule;
import cc.novoline.modules.EnumModuleType;
import cc.novoline.modules.ModuleManager;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFurnace;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.lwjgl.input.Keyboard;

import java.util.concurrent.ThreadLocalRandom;

public final class AntiObbyTrap extends AbstractModule {

    private float currentDamage;
    private boolean digging;

    public AntiObbyTrap(@NonNull ModuleManager moduleManager) {
        super(moduleManager, "AntiObbyTrap", "Anti Obby Trap", Keyboard.KEY_NONE, EnumModuleType.PLAYER,
                "Stop being a bot and falling for obby traps");
    }

    @EventTarget
    public void onPre(MotionUpdateEvent event) {
        if (event.getState().equals(MotionUpdateEvent.State.PRE)) {
            if (this.mc.world.getBlockState(new BlockPos(event.getX(), event.getY() + 1, event.getZ()))
                    .getBlock() == Blocks.obsidian || this.mc.world.getBlockState(new BlockPos(event.getX(), event.getY() + 1, event.getZ()))
                    .getBlock() == Blocks.cobblestone || this.mc.world.getBlockState(new BlockPos(event.getX(), event.getY() + 2, event.getZ()))
                    .getBlock() instanceof BlockFurnace) {
                event.setPitch(89 + ThreadLocalRandom.current().nextFloat());
                Block currentBlock = this.mc.world.getBlockState(new BlockPos(event.getX(), event.getY() - 1, event.getZ())).getBlock();
                BlockPos pos = new BlockPos(event.getX(), event.getY() - 1, event.getZ());

                if (this.currentDamage == 0.0F) {
                    this.digging = true;
                    sendPacket(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.START_DESTROY_BLOCK, pos, EnumFacing.UP));
                }

                mc.player.updateTool(pos);
                sendPacket(new C0APacketAnimation());
                this.currentDamage += currentBlock.getPlayerRelativeBlockHardness(this.mc.player, this.mc.world, pos);
                this.mc.world.sendBlockBreakProgress(this.mc.player.getEntityID(), pos, (int) (this.currentDamage * 10.0F) - 1);

                if (this.currentDamage >= 1.0F) {
                    sendPacket(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK, pos, EnumFacing.UP));
                    this.mc.playerController.onPlayerDestroyBlock(pos, EnumFacing.UP);
                    this.currentDamage = 0.0F;
                    this.digging = false;
                }
            } else {
                this.currentDamage = 0.0F;
                this.digging = false;
            }
        }
    }

    public boolean isDigging() {
        return digging;
    }

}
