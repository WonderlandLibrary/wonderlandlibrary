package dev.zprestige.prestige.client.module.impl.combat;

import dev.zprestige.prestige.client.event.EventListener;
import dev.zprestige.prestige.client.event.impl.MoveEvent;
import dev.zprestige.prestige.client.module.Category;
import dev.zprestige.prestige.client.module.Module;
import dev.zprestige.prestige.client.util.impl.BlockUtil;
import dev.zprestige.prestige.client.util.impl.InventoryUtil;
import dev.zprestige.prestige.client.util.impl.TimerUtil;
import net.minecraft.block.Blocks;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import org.lwjgl.glfw.GLFW;

public class GhostObby extends Module {
    public TimerUtil field559 = new TimerUtil();

    public GhostObby() {
        super("Ghost Obby", Category.Combat, "Automatically places obsidian when looking at a block while holding a totem");
    }

    @EventListener
    public void event(MoveEvent event) {
        if (getMc().currentScreen != null || !getMc().isWindowFocused()) {
            return;
        }
        if (!field559.delay(200)) {
            return;
        }
        if (getMc().player.getMainHandStack().isFood() || getMc().player.getMainHandStack().isStackable()) {
            return;
        }
        if (GLFW.glfwGetMouseButton(getMc().getWindow().getHandle(), 1) != 1) {
            return;
        }
        HitResult hitResult = getMc().crosshairTarget;
        if (hitResult == null || hitResult.getType() != HitResult.Type.BLOCK) {
            return;
        }
        BlockPos blockPos = ((BlockHitResult)hitResult).getBlockPos();
        if (getMc().world.getBlockState(blockPos).getBlock() != Blocks.OBSIDIAN && getMc().world.getBlockState(blockPos).getBlock() != Blocks.BEDROCK && !BlockUtil.INSTANCE.isCollidesEntity(blockPos)) {
            int slot = getMc().player.getInventory().selectedSlot;
            if (InventoryUtil.INSTANCE.findBlockSlot(Blocks.OBSIDIAN) != null) {
                InventoryUtil.INSTANCE.setCurrentSlot(InventoryUtil.INSTANCE.findBlockSlot(Blocks.OBSIDIAN));
                getMc().interactionManager.interactBlock(getMc().player, getMc().player.getActiveHand(), (BlockHitResult) hitResult);
                InventoryUtil.INSTANCE.setCurrentSlot(slot);
                field559.reset();
            }
        }
    }
}