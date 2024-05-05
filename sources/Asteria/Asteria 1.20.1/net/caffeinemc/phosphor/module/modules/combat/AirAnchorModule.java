package net.caffeinemc.phosphor.module.modules.combat;

import net.caffeinemc.phosphor.api.event.events.ItemUseEvent;
import net.caffeinemc.phosphor.api.event.events.PlayerTickEvent;
import net.caffeinemc.phosphor.api.event.orbit.EventHandler;
import net.caffeinemc.phosphor.api.event.orbit.EventPriority;
import net.caffeinemc.phosphor.api.util.BlockUtils;
import net.caffeinemc.phosphor.api.util.KeyUtils;
import net.caffeinemc.phosphor.module.Module;
import net.minecraft.block.Blocks;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.PlayerInteractBlockC2SPacket;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import org.lwjgl.glfw.GLFW;

public class AirAnchorModule extends Module {
    public AirAnchorModule() {
        super("AirAnchor", "Makes MINDBLOWING Minecraft Crystal PVP Method easier!", Category.COMBAT);
    }

    private BlockPos currentBlockPos;
    private int count;

    @Override
    public void onEnable() {
        currentBlockPos = null;
        count = 0;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    private void onItemUse(ItemUseEvent.Pre event) {
        if (mc.player.getMainHandStack().isOf(Items.RESPAWN_ANCHOR)) {
            if (mc.crosshairTarget instanceof BlockHitResult blockHitResult && BlockUtils.isAnchorCharged(blockHitResult.getBlockPos())) {
                if (blockHitResult.getBlockPos().equals(currentBlockPos)) {
                    if (count >= 1) return;
                } else {
                    currentBlockPos = blockHitResult.getBlockPos();
                    count = 0;
                }

                mc.getNetworkHandler().sendPacket(new PlayerInteractBlockC2SPacket(Hand.MAIN_HAND, blockHitResult, 0));
                count++;
            }
        }
    }
}
