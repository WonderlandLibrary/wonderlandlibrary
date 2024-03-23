package dev.zprestige.prestige.client.module.impl.combat;

import dev.zprestige.prestige.client.event.EventListener;
import dev.zprestige.prestige.client.event.impl.MoveEvent;
import dev.zprestige.prestige.client.module.Category;
import dev.zprestige.prestige.client.module.Module;
import dev.zprestige.prestige.client.setting.impl.BooleanSetting;
import dev.zprestige.prestige.client.setting.impl.DragSetting;
import dev.zprestige.prestige.client.setting.impl.IntSetting;
import dev.zprestige.prestige.client.setting.impl.ModeSetting;
import dev.zprestige.prestige.client.util.impl.BlockUtil;
import dev.zprestige.prestige.client.util.impl.InventoryUtil;
import dev.zprestige.prestige.client.util.impl.TimerUtil;
import net.minecraft.block.Blocks;
import net.minecraft.item.Items;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.Direction;

public class AnchorPlacer extends Module {

    public ModeSetting mode;
    public DragSetting delay;
    public BooleanSetting glowstone;
    public BooleanSetting switchBack;
    public IntSetting switchSlot;
    public TimerUtil timer;
    public int progress;

    public AnchorPlacer() {
        super("Anchor Placer", Category.Combat, "Places an anchor on the block your looking at and glowstones it");
        mode = setting("Mode", "Normal", new String[]{"Normal", "Glowstone"}).description("Anchor Placer mode");
        delay = setting("Delay", 30.0f, 50.0f, 0.0f, 200.0f).description("Delay between each action");
        glowstone = setting("Glowstone", true).invokeVisibility(arg_0 -> mode.getObject().equals("Normal")).description("Whether to add a glowstone to the anchor");
        switchBack = setting("Switch Back", true).invokeVisibility(arg_0 -> mode.getObject().equals("Glowstone")).description("Switches to another slot after anchoring");
        switchSlot = setting("Switch Slot", 1, 1, 9).invokeVisibility(arg_0 -> mode.getObject().equals("Glowstone") && switchBack.getObject()).description("Switches to another slot after anchoring");
        timer = new TimerUtil();
    }

    @Override
    public void onEnable() {
        progress = 0;
        delay.setValue();
        timer.reset();
    }

    @EventListener
    public void event(MoveEvent event) {
        if (getMc().currentScreen == null && getMc().isWindowFocused() && timer.delay(delay)) {
            if (mode.getObject().equals("Normal")) {
                switch (progress) {
                    case 0 -> {
                        if (InventoryUtil.INSTANCE.findBlockSlot(Blocks.RESPAWN_ANCHOR) != null) {
                            InventoryUtil.INSTANCE.setCurrentSlot(InventoryUtil.INSTANCE.findBlockSlot(Blocks.RESPAWN_ANCHOR));
                            increaseProgress();
                        } else {
                            toggle();
                        }
                    }
                    case 1 -> {
                        if (getMc().crosshairTarget instanceof BlockHitResult blockHitResult && !getMc().world.getBlockState(blockHitResult.getBlockPos()).isAir()) {
                            Direction direction = blockHitResult.getSide();
                            if (!BlockUtil.INSTANCE.isCollidesEntity(blockHitResult.getBlockPos().add(direction.getOffsetX(), direction.getOffsetY(), direction.getOffsetZ()))) {
                                getMc().interactionManager.interactBlock(getMc().player, Hand.MAIN_HAND, blockHitResult);
                                getMc().player.swingHand(Hand.MAIN_HAND);
                                increaseProgress();
                            }
                        }
                    }
                    case 2 -> {
                        if (glowstone.getObject()) {
                            if (InventoryUtil.INSTANCE.findItemInHotbar(Items.GLOWSTONE) != null) {
                                InventoryUtil.INSTANCE.setCurrentSlot(InventoryUtil.INSTANCE.findItemInHotbar(Items.GLOWSTONE));
                                increaseProgress();
                            } else {
                                toggle();
                            }
                        } else {
                            toggle();
                        }
                    }
                    case 3 -> {
                        if (getMc().crosshairTarget instanceof BlockHitResult blockHitResult && getMc().world.getBlockState(blockHitResult.getBlockPos()).getBlock() == Blocks.RESPAWN_ANCHOR) {
                            getMc().interactionManager.interactBlock(getMc().player, Hand.MAIN_HAND, blockHitResult);
                            getMc().player.swingHand(Hand.MAIN_HAND);
                            increaseProgress();
                        }
                    }
                    case 4 -> toggle();
                }
            } else {
                switch (progress) {
                    case 0 -> {
                        if (getMc().crosshairTarget instanceof BlockHitResult blockHitResult && getMc().world.getBlockState(blockHitResult.getBlockPos()).getBlock() == Blocks.RESPAWN_ANCHOR && getMc().world.getBlockState(blockHitResult.getBlockPos()).get(Properties.CHARGES) == 0) {
                            if (InventoryUtil.INSTANCE.findBlockSlot(Blocks.GLOWSTONE) != null) {
                                InventoryUtil.INSTANCE.setCurrentSlot(InventoryUtil.INSTANCE.findBlockSlot(Blocks.GLOWSTONE));
                                increaseProgress();
                            } else {
                                toggle();
                            }
                        }
                    }
                    case 1 -> {
                        if (getMc().crosshairTarget instanceof BlockHitResult blockHitResult && getMc().world.getBlockState(blockHitResult.getBlockPos()).getBlock() == Blocks.RESPAWN_ANCHOR && getMc().world.getBlockState(blockHitResult.getBlockPos()).get(Properties.CHARGES) == 0) {
                            getMc().interactionManager.interactBlock(getMc().player, Hand.MAIN_HAND, blockHitResult);
                            getMc().player.swingHand(Hand.MAIN_HAND);
                            increaseProgress();
                        }
                        if (switchBack.getObject()) {
                            InventoryUtil.INSTANCE.setCurrentSlot(switchSlot.getObject() - 1);
                            progress = 0;
                            timer.reset();
                        }
                    }
                    case 2 -> {
                        if (switchBack.getObject()) {
                            InventoryUtil.INSTANCE.setCurrentSlot(switchSlot.getObject() - 1);
                            progress = 0;
                            timer.reset();
                        }
                    }
                }
            }
        }
    }

    private void increaseProgress() {
        delay.setValue();
        timer.reset();
        progress += 1;
    }
}