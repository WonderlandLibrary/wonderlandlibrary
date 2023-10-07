/*
 Copyright Alan Wood 2021
 None of this code to be reused without my written permission
 Intellectual Rights owned by Alan Wood
 */
package dev.rise.module.impl.ghost;

import dev.rise.event.impl.motion.PreMotionEvent;
import dev.rise.event.impl.render.Render3DEvent;
import dev.rise.module.Module;
import dev.rise.module.api.ModuleInfo;
import dev.rise.module.enums.Category;
import dev.rise.setting.impl.BooleanSetting;
import dev.rise.setting.impl.NoteSetting;
import dev.rise.setting.impl.NumberSetting;
import dev.rise.util.math.TimeUtil;
import net.minecraft.block.BlockAir;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import org.lwjgl.input.Mouse;

/**
 * @author Hazsi
 * @since 03/29/2022
 */
@ModuleInfo(name = "AutoClicker", description = "Automatically clicks for you", category = Category.LEGIT)
public final class AutoClicker extends Module {

    private final NoteSetting left = new NoteSetting("Left", this);
    private final BooleanSetting enableLeft = new BooleanSetting("Enable Left Click", this, true);
    private final NumberSetting LminCps = new NumberSetting("Left CPS Min", this, 10, 1, 20, 0.5);
    private final NumberSetting LmaxCps = new NumberSetting("Left CPS Max", this, 14, 1, 20, 0.5);
    private final NumberSetting Ljitter = new NumberSetting("Left Jitter", this, 0.4, 0, 1, 0.05);
    private final BooleanSetting LswordCheck = new BooleanSetting("Limit to Swords", this, false);
    private final BooleanSetting LbreakBlocks = new BooleanSetting("Allow Breaking Blocks", this, false);
    private final BooleanSetting Lblockhitting = new BooleanSetting("Allow Blockhitting", this, true);
    private final BooleanSetting Lautoblockhit = new BooleanSetting("Auto Blockhit", this, false);

    private final NoteSetting right = new NoteSetting("Right", this);
    private final BooleanSetting enableRight = new BooleanSetting("Enable Right Click", this, false);
    private final NumberSetting RminCps = new NumberSetting("Right CPS Min", this, 11, 1, 20, 0.5);
    private final NumberSetting RmaxCps = new NumberSetting("Right CPS Max", this, 15, 1, 20, 0.5);
    private final NumberSetting Rjitter = new NumberSetting("Right Jitter", this, 0.3, 0, 1, 0.05);
    private final BooleanSetting RblockCheck = new BooleanSetting("Limit to Blocks", this, true);
//    private final BooleanSetting Rinvfill = new BooleanSetting("Allow Inventory Fill", this, false);

    private final TimeUtil leftTimer = new TimeUtil();
    private final TimeUtil blockHitTimer = new TimeUtil();
    private final TimeUtil rightTimer = new TimeUtil();
    private double leftDelay, rightDelay;
    private long blockHitDelay = 250;

    @Override
    public void onEnable() {
        leftTimer.reset();
        blockHitTimer.reset();
        rightTimer.reset();
    }

    @Override
    public void onRender3DEvent(final Render3DEvent event) {

        if (Mouse.isButtonDown(0) && enableLeft.isEnabled()) {
            ItemStack heldItem = mc.thePlayer.getHeldItem();

            KeyBinding.setKeyBindState(mc.gameSettings.keyBindAttack.getKeyCode(), false);

            if (LswordCheck.isEnabled() && (heldItem == null || !(heldItem.getItem() instanceof ItemSword))) return;

            if (mc.thePlayer.isUsingItem()) return;

            if (LbreakBlocks.isEnabled() && mc.objectMouseOver.getBlockPos() != null && !(mc.theWorld.getBlockState(mc.objectMouseOver.getBlockPos()).getBlock() instanceof BlockAir)) {
                KeyBinding.setKeyBindState(mc.gameSettings.keyBindAttack.getKeyCode(), true);
                return;
            }

            if (leftTimer.hasReached((long) leftDelay)) {
                if (mc.currentScreen == null) {
                    mc.clickMouse();
                    mc.thePlayer.rotationYaw += (Math.random() - 0.5) * Ljitter.getValue() * 2;
                    mc.thePlayer.rotationPitch += (Math.random() - 0.5) * Ljitter.getValue() * 2;
//                } else if (Rinvfill.isEnabled()) {
//                    // TODO
                }

                double cpsRange = Math.abs(LmaxCps.getValue() - LminCps.getValue());
                leftDelay = (1 / (LminCps.getValue() + Math.random() * cpsRange)) * 1000;
                leftTimer.reset();
            }
        }
        else if (Mouse.isButtonDown(1) && enableRight.isEnabled() && !Mouse.isButtonDown(0)) {
            ItemStack heldItem = mc.thePlayer.getHeldItem();

            if (RblockCheck.isEnabled() && (heldItem == null || !(heldItem.getItem() instanceof ItemBlock))) return;

            if (rightTimer.hasReached((long) rightDelay)) {
                if (mc.currentScreen == null) {
                    mc.rightClickMouse();
                    mc.thePlayer.rotationYaw += (Math.random() - 0.5) * Rjitter.getValue() * 2;
                    mc.thePlayer.rotationPitch += (Math.random() - 0.5) * Rjitter.getValue() * 2;

                    KeyBinding.setKeyBindState(mc.gameSettings.keyBindUseItem.getKeyCode(), false);
//                } else if (Rinvfill.isEnabled()) {
//                    // TODO
                }

                double cpsRange = Math.abs(RmaxCps.getValue() - RminCps.getValue());
                rightDelay = (1 / (RminCps.getValue() + Math.random() * cpsRange)) * 1000;
                rightTimer.reset();
            }
        }
        else {
            KeyBinding.setKeyBindState(mc.gameSettings.keyBindAttack.getKeyCode(), Mouse.isButtonDown(0));
            KeyBinding.setKeyBindState(mc.gameSettings.keyBindUseItem.getKeyCode(), Mouse.isButtonDown(1));
        }
    }

    @Override
    public void onPreMotion(final PreMotionEvent event) {

        ItemStack heldItem = mc.thePlayer.getHeldItem();

        if (blockHitTimer.hasReached(blockHitDelay)) {
            if (mc.gameSettings.keyBindUseItem.isKeyDown()) {
                mc.gameSettings.keyBindUseItem.setKeyPressed(false);
                blockHitDelay = (long) (75 + Math.random() * 50);
            }

            else if (Lblockhitting.isEnabled() && Mouse.isButtonDown(1) && heldItem != null && heldItem.getItem() instanceof ItemSword) {
                mc.gameSettings.keyBindUseItem.setKeyPressed(true);
                blockHitDelay = (long) (1250 + Math.random() * 100);
            }
        }

//        if (mc.objectMouseOver.typeOfHit.equals(MovingObjectPosition.MovingObjectType.ENTITY)) {
//            Entity target = mc.objectMouseOver.entityHit;
//
//            if (target instanceof EntityPlayer && ((EntityPlayer) target).hurtTime == 5) {
//                mc.gameSettings.keyBindUseItem.setKeyPressed(true);
//                blockHitDelay = (long) (250 + Math.random() * 100);
//            }
//        }
    }
}
