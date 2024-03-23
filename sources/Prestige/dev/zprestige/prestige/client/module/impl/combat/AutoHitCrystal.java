package dev.zprestige.prestige.client.module.impl.combat;

import dev.zprestige.prestige.client.event.EventListener;
import dev.zprestige.prestige.client.event.impl.MoveEvent;
import dev.zprestige.prestige.client.module.Category;
import dev.zprestige.prestige.client.module.Module;
import dev.zprestige.prestige.client.setting.impl.BooleanSetting;
import dev.zprestige.prestige.client.setting.impl.DragSetting;
import dev.zprestige.prestige.client.setting.impl.ModeSetting;
import dev.zprestige.prestige.client.util.impl.*;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;

public class AutoHitCrystal extends Module {
    public DragSetting delay;
    public ModeSetting mode;
    public BooleanSetting silent;
    public BooleanSetting perfectTiming;
    public BooleanSetting pauseOnKill;
    public TimerUtil timer;
    public PlayerEntity target;
    public int progress;

    public AutoHitCrystal() {
        super("Auto Hit Crystal", Category.Combat, "Automatically hit crystals and attempts to d-tap them by syncing with enemy damage ticks");
        delay = setting("Delay", 30.0f, 50.0f, 0.0f, 200.0f).description("Delay between each action");
        mode = setting("Crystal", "Single Tap", new String[]{"None", "Single Tap", "Double Tap"}).description("How many crystals should be placed");
        silent = setting("Silent", false).description("Whether crystals should be attacked using silent rotations");
        perfectTiming = setting("Perfect Timing", false).description("Times crystals perfectly with enemy damage ticks");
        pauseOnKill = setting("Pause On Kill", false).description("Pauses when there is a dead body");
        timer = new TimerUtil();
    }

    @Override
    public void onDisable() {
        this.target = null;
        this.progress = 0;
    }

    @EventListener
    public void event(MoveEvent event) {
        if (getMc().currentScreen != null || !getMc().isWindowFocused() || !timer.delay(delay) || pauseOnKill.getObject() && OneLineUtil.isInvalidPlayer()) {
            return;
        }
        PlayerEntity playerEntity = EntityUtil.INSTANCE.getPlayer();
        if (playerEntity != null) {
            if (playerEntity.distanceTo(getMc().player) > 10.0f) {
                playerEntity = null;
            }
        }
        switch (progress) {
            case 0: {
                if (InventoryUtil.INSTANCE.findItemInHotbar(Items.OBSIDIAN) != null) {
                    InventoryUtil.INSTANCE.setCurrentSlot(InventoryUtil.INSTANCE.findItemInHotbar(Items.OBSIDIAN));
                    setProgress();
                }
                break;
            }
            case 1: {
                if (getMc().crosshairTarget != null) {
                    HitResult hitResult = getMc().crosshairTarget;
                    if (hitResult instanceof BlockHitResult blockHitResult && !getMc().world.getBlockState(blockHitResult.getBlockPos()).isAir()) {
                        BlockPos blockPos = blockHitResult.getBlockPos().add(blockHitResult.getSide().getOffsetX(), blockHitResult.getSide().getOffsetY(), ((BlockHitResult) hitResult).getSide().getOffsetZ());
                        if (!BlockUtil.INSTANCE.isCollidesEntity(blockPos)) {
                            if (getMc().world.getBlockState(blockHitResult.getBlockPos()).getBlock() == Blocks.OBSIDIAN) {
                                setProgress();
                                break;
                            }
                            getMc().interactionManager.interactBlock(getMc().player, Hand.MAIN_HAND, blockHitResult);
                            getMc().player.swingHand(Hand.MAIN_HAND);
                            setProgress();
                        }
                    }
                }
                break;
            }
            case 2: {
                if (InventoryUtil.INSTANCE.findItemInHotbar(Items.END_CRYSTAL) != null) {
                    InventoryUtil.INSTANCE.setCurrentSlot(InventoryUtil.INSTANCE.findItemInHotbar(Items.END_CRYSTAL));
                    setProgress();
                }
            }
            case 3: {
                if (getMc().crosshairTarget != null) {
                    HitResult hitResult = getMc().crosshairTarget;
                    if (hitResult instanceof BlockHitResult blockHitResult && getMc().world.getBlockState(blockHitResult.getBlockPos()).getBlock() == Blocks.OBSIDIAN && !BlockUtil.INSTANCE.isCollidesEntity(blockHitResult.getBlockPos())) {
                        getMc().interactionManager.interactBlock(getMc().player, Hand.MAIN_HAND, blockHitResult);
                        getMc().player.swingHand(Hand.MAIN_HAND);
                        setProgress();
                    }
                }
                break;
            }
            case 4: {
                if (mode.getObject().equals("None")) {
                    toggle();
                    break;
                }
                if (perfectTiming.getObject() && playerEntity != null && playerEntity.hurtTime != 0) {
                    break;
                }

                HitResult hitResult = getMc().crosshairTarget;
                if (hitResult.getType() == HitResult.Type.ENTITY) {
                    EntityHitResult entityHitResult = (EntityHitResult) hitResult;
                    if (entityHitResult.getEntity() instanceof EndCrystalEntity) {
                        getMc().interactionManager.attackEntity(getMc().player, entityHitResult.getEntity());
                        getMc().player.swingHand(Hand.MAIN_HAND);
                        if (mode.getObject().equals("Double Tap")) {
                            setProgress();
                        } else {
                            toggle();
                        }
                    }
                } else if (!silent.getObject().booleanValue()) {
                    BlockPos blockPos = null;
                    HitResult hitResult2 = getMc().crosshairTarget;
                    if (hitResult2.getType() == HitResult.Type.BLOCK) {
                        blockPos = ((BlockHitResult) hitResult2).getBlockPos();
                    }

                    if (blockPos != null && RaytraceUtil.isBlockAtPosition(blockPos, Blocks.OBSIDIAN)) {
                        for (Entity entity : getMc().world.getEntities()) {
                            if (entity instanceof EndCrystalEntity && entity.getPos().distanceTo(blockPos.up().toCenterPos()) < 1.0 && getMc().player.distanceTo(entity) <= 4.5f) {
                                Rotation rotation = RotationUtil.INSTANCE.getNeededRotations((float) (entity.getPos().x + (double) RandomUtil.INSTANCE.randomInRange(-0.25f, 0.25f)), (float) (entity.getPos().y + (double) RandomUtil.INSTANCE.randomInRange(0.3f, 0.6f)), (float) (entity.getPos().z + (double) RandomUtil.INSTANCE.randomInRange(-0.25f, 0.25f)));
                                event.setPitch(rotation.getPitch());
                                getMc().interactionManager.attackEntity(getMc().player, entity);
                                getMc().player.swingHand(Hand.MAIN_HAND);
                                if (mode.getObject().equals("Double Tap")) {
                                    setProgress();
                                } else {
                                    toggle();
                                }
                            }
                        }
                    }
                }
                break;
            }
            case 5: {
                if (getMc().crosshairTarget != null) {
                    HitResult hitResult = getMc().crosshairTarget;
                    if (hitResult instanceof BlockHitResult blockHitResult && getMc().world.getBlockState(blockHitResult.getBlockPos()).getBlock() == Blocks.OBSIDIAN && !BlockUtil.INSTANCE.isCollidesEntity(blockHitResult.getBlockPos())) {
                        getMc().interactionManager.interactBlock(getMc().player, Hand.MAIN_HAND, blockHitResult);
                        getMc().player.swingHand(Hand.MAIN_HAND);
                        setProgress();
                    }
                }
                break;
            }
            case 6: {
                if (perfectTiming.getObject() && playerEntity != null && playerEntity.hurtTime != 0) {
                    break;
                }
                HitResult hitResult = getMc().crosshairTarget;
                if (hitResult.getType() == HitResult.Type.ENTITY) {
                    EntityHitResult entityHitResult = (EntityHitResult) hitResult;
                    if (entityHitResult.getEntity() instanceof EndCrystalEntity) {
                        getMc().interactionManager.attackEntity(getMc().player, entityHitResult.getEntity());
                        getMc().player.swingHand(Hand.MAIN_HAND);
                        if (mode.getObject().equals("Double Tap")) {
                            setProgress();
                        } else {
                            toggle();
                        }
                    }
                } else if (!silent.getObject().booleanValue()) {
                    BlockPos blockPos = null;
                    HitResult hitResult2 = getMc().crosshairTarget;
                    if (hitResult2.getType() == HitResult.Type.BLOCK) {
                        blockPos = ((BlockHitResult) hitResult2).getBlockPos();
                    }

                    if (blockPos != null && RaytraceUtil.isBlockAtPosition(blockPos, Blocks.OBSIDIAN)) {
                        for (Entity entity : getMc().world.getEntities()) {
                            if (entity instanceof EndCrystalEntity && entity.getPos().distanceTo(blockPos.up().toCenterPos()) < 1.0 && getMc().player.distanceTo(entity) <= 4.5f) {
                                Rotation rotation = RotationUtil.INSTANCE.getNeededRotations((float) (entity.getPos().x + (double) RandomUtil.INSTANCE.randomInRange(-0.25f, 0.25f)), (float) (entity.getPos().y + (double) RandomUtil.INSTANCE.randomInRange(0.3f, 0.6f)), (float) (entity.getPos().z + (double) RandomUtil.INSTANCE.randomInRange(-0.25f, 0.25f)));
                                event.setPitch(rotation.getPitch());
                                getMc().interactionManager.attackEntity(getMc().player, entity);
                                getMc().player.swingHand(Hand.MAIN_HAND);
                                toggle();
                            }
                        }
                    }
                }
            }
        }
    }

    private void setProgress() {
        delay.setValue();
        progress += 1;
        timer.reset();
    }
}