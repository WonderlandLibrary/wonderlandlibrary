package dev.zprestige.prestige.client.module.impl.combat;

import dev.zprestige.prestige.client.event.EventListener;
import dev.zprestige.prestige.client.event.impl.MoveEvent;
import dev.zprestige.prestige.client.module.Category;
import dev.zprestige.prestige.client.module.Module;
import dev.zprestige.prestige.client.setting.impl.BooleanSetting;
import dev.zprestige.prestige.client.setting.impl.DragSetting;
import dev.zprestige.prestige.client.util.impl.*;
import net.minecraft.block.Blocks;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.entity.mob.MagmaCubeEntity;
import net.minecraft.entity.mob.SlimeEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;

public class AutoCrystal extends Module {

    public DragSetting delay;
    public BooleanSetting silentSwap;
    public BooleanSetting headBob;
    public BooleanSetting inAir;
    public BooleanSetting switchSetting;
    public BooleanSetting damageTick;
    public BooleanSetting pauseOnKill;
    public TimerUtil timer;
    public BlockPos pos;

    public AutoCrystal() {
        super("Auto Crystal", Category.Combat, "Automatically places and explodes crystals.");
        delay = setting("Delay", 30.0f, 50.0f, 0.0f, 200.0f).description("Delay between each action");
        silentSwap = setting("Silent Swap", false).description("Silently swaps to crystals to place allowing you to hold e.g a sword and crystal at the same time");
        headBob = setting("Head Bob", false).description("Silently update your pitch to break crystals above/below you");
        inAir = setting("In Air", false).description("Place crystals while in air, otherwise will time out in air.");
        switchSetting = setting("Switch", false).description("Switches to crystals when the module is enabled.");
        damageTick = setting("Damage Tick", false).description("Only break crystals when they do damage");
        pauseOnKill = setting("Pause On Kill", false).description("Pauses when there is a dead body");
        timer = new TimerUtil();
    }

    @Override
    public void onEnable() {
        if (switchSetting.getObject()) {
            Integer n = InventoryUtil.INSTANCE.findItemInHotbar(Items.END_CRYSTAL);
            if (n == null) {
                return;
            }
            InventoryUtil.INSTANCE.setCurrentSlot(n);
        }
        timer.reset();
    }

    @Override
    public void onDisable() {
        pos = null;
    }

    @EventListener
    public void event(MoveEvent event) {
        if (getMc().currentScreen != null || !getMc().isWindowFocused() || pauseOnKill.getObject() && OneLineUtil.isInvalidPlayer()) {
            return;
        }

        event.setCancelled();
        if (!InventoryUtil.INSTANCE.isHoldingItem(Items.END_CRYSTAL) && silentSwap.getObject() && InventoryUtil.INSTANCE.findItemInHotbar(Items.END_CRYSTAL) == null) {
            return;
        }
        if (getMc().player.isUsingItem() || !getMc().player.isOnGround() && !inAir.getObject() || !timer.delay(delay)) {
            return;
        }
        timer.reset();
        delay.setValue();
        PlayerEntity entity = EntityUtil.INSTANCE.getPlayer();
        boolean bool;
        if (damageTick.getObject()) {
            bool = true;
        } else {
            bool = entity == null || entity.hurtTime == 0;
        }
        if (bool) {
            HitResult hitResult = getMc().crosshairTarget;
            if (hitResult.getType() == HitResult.Type.ENTITY) {
                EntityHitResult entityHitResult = (EntityHitResult) hitResult;
                if (entityHitResult.getEntity() instanceof EndCrystalEntity || entityHitResult.getEntity() instanceof SlimeEntity || entityHitResult.getEntity() instanceof MagmaCubeEntity) {
                    ClientPlayerInteractionManager clientPlayerInteractionManager = getMc().interactionManager;
                    clientPlayerInteractionManager.attackEntity(getMc().player, entityHitResult.getEntity());
                    getMc().player.swingHand(Hand.MAIN_HAND);
                    pos = null;
                }
            } else if (headBob.getObject()) {
                BlockPos blockPos = pos;
                if (blockPos == null) {
                    HitResult hitResult2 = getMc().crosshairTarget;
                    if (hitResult2.getType() == HitResult.Type.BLOCK) {
                        blockPos = ((BlockHitResult) hitResult2).getBlockPos();
                    }
                }
                if (blockPos != null) {
                    for (Object object : getMc().world.getEntities()) {
                        if (object instanceof EndCrystalEntity) {
                            EndCrystalEntity endCrystalEntity = (EndCrystalEntity) object;
                            if (endCrystalEntity.getPos().distanceTo(blockPos.up().toCenterPos()) < 1 && getMc().player.distanceTo(endCrystalEntity) <= 4.5f) {
                                Rotation rotation = RotationUtil.INSTANCE.getNeededRotations((float) (endCrystalEntity.getPos().x + RandomUtil.INSTANCE.randomInRange(-0.25f, 0.25f)), (float) (endCrystalEntity.getPos().y + RandomUtil.INSTANCE.randomInRange(0.3f, 0.6f)), (float) (endCrystalEntity.getPos().z + RandomUtil.INSTANCE.randomInRange(-0.25f, 0.25f)));
                                event.setPitch(rotation.getPitch());
                                ClientPlayerInteractionManager clientPlayerInteractionManager = getMc().interactionManager;
                                clientPlayerInteractionManager.attackEntity(getMc().player, endCrystalEntity);
                                getMc().player.swingHand(Hand.MAIN_HAND);
                                pos = null;
                                return;
                            }
                        }
                    }
                }

            }
        }
        HitResult hitResult = getMc().crosshairTarget;
        if (hitResult.getType() == HitResult.Type.BLOCK) {
            BlockHitResult blockHitResult = (BlockHitResult) hitResult;
            if (getMc().world.getBlockState(blockHitResult.getBlockPos()).getBlock() == Blocks.BEDROCK || getMc().world.getBlockState(blockHitResult.getBlockPos()).getBlock() == Blocks.OBSIDIAN) {
                BlockPos blockPos = blockHitResult.getBlockPos().up();
                if (BlockUtil.INSTANCE.isCollidesEntity(blockPos)) {
                    return;
                }
                if (InventoryUtil.INSTANCE.isHoldingItem(Items.END_CRYSTAL) || silentSwap.getObject()) {
                    int n = getMc().player.getInventory().selectedSlot;
                    int n2 = InventoryUtil.INSTANCE.findItemInHotbar(Items.END_CRYSTAL);
                    if (n2 != -1 && silentSwap.getObject()) {
                        getMc().player.getInventory().selectedSlot = n2;
                    }
                    ClientPlayerInteractionManager clientPlayerInteractionManager = getMc().interactionManager;
                    clientPlayerInteractionManager.interactBlock(getMc().player, Hand.MAIN_HAND, blockHitResult);
                    getMc().player.getInventory().selectedSlot = n;
                    pos = blockHitResult.getBlockPos();
                }
            }
        }
        timer.reset();
    }
}