/*
 Copyright Alan Wood 2021
 None of this code to be reused without my written permission
 Intellectual Rights owned by Alan Wood
 */
package dev.rise.module.impl.movement;

import dev.rise.event.impl.motion.PreMotionEvent;
import dev.rise.module.Module;
import dev.rise.module.api.ModuleInfo;
import dev.rise.module.enums.Category;
import dev.rise.util.player.MoveUtil;
import dev.rise.util.player.PacketUtil;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

/**
 * Makes you automatically sprint without pressing the sprint key.
 */
@ModuleInfo(name = "BowFly", description = "Allows you to fly by using a bow", category = Category.MOVEMENT)
public final class BowFly extends Module {

    boolean jumped, flag;
    int i, ticks;
    public static float pitch;
    int slot;

    int currentSlot;

    @Override
    protected void onEnable() {
        shootBow();
        slot = -1;
        currentSlot = -1;
    }

    @Override
    protected void onDisable() {
        mc.timer.timerSpeed = 1;
        if (currentSlot != mc.thePlayer.inventory.currentItem) {
            mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem));
        }
    }

    @Override
    public void onPreMotion(final PreMotionEvent event) {
        mc.timer.timerSpeed = 0.5f;

        //if ticks == 1 it will start the bow shooting again, bow shooting lasts for 9 ticks
        if (ticks >= 9) {
            shootBow();
            mc.timer.timerSpeed = 1;
        }
        if (ticks < 1) ticks = 1;

        if (mc.thePlayer.hurtTime == 9) {
            MoveUtil.strafe();
        }

        //if (mc.thePlayer.hurtTime > 0) mc.thePlayer.motionY += 0.015;

        MoveUtil.strafe();

        handleBow(event);
    }

    private void handleBow(final PreMotionEvent event) {
        ItemStack itemStack = null;

        //Pitch
        pitch = (float) mc.thePlayer.motionY * 70;
        pitch -= 45;
        if (pitch > 70) pitch = 70;
        if (pitch < -70) pitch = -70;

        pitch *= -1;

        pitch -= MoveUtil.getSpeed() * 60;

        if (MoveUtil.getSpeed() < 0.3) {
            pitch = -85;
        }

        if (mc.thePlayer.motionZ == 0 && mc.thePlayer.motionX == 0) pitch = -90;

        if (pitch > 90) pitch = 90;
        if (pitch < -90) pitch = -90;
        event.setPitch(pitch);

        //Yaw
        final float direction = (float) (Math.atan2(mc.thePlayer.motionZ, mc.thePlayer.motionX) * (180 / Math.PI)) + 90 - 180;
        mc.thePlayer.renderYawOffset = direction;
        mc.thePlayer.rotationYawHead = direction;
        event.setYaw(direction);

        int i2;
        for (i2 = 0; i2 < 9; ++i2) {
            itemStack = mc.thePlayer.inventoryContainer.getSlot(i2 + 36).getStack();
            if (itemStack != null) {
                final Item item = itemStack.getItem();
                if (item instanceof ItemBow) {
                    for (int i = 0; i < mc.thePlayer.inventory.mainInventory.length; i++) {
                        final ItemStack stack = mc.thePlayer.inventory.mainInventory[i];
                        if (stack != null) {
                            if (stack.getItem().getUnlocalizedName().contains("arrow")) {

                                if (currentSlot != i2) {
                                    PacketUtil.sendPacketWithoutEvent(new C09PacketHeldItemChange(i2));
                                }
                                currentSlot = i2;
                            }
                        }
                    }
                }
            }
        }

        if (currentSlot != i2) {
            if (ticks == 1) {
                mc.thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(itemStack));
            }

            if (ticks == 4) {
                PacketUtil.sendPacketWithoutEvent(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
            }
            ticks++;
        }

        //Jumped is just saying the the bow or rod has been shot
        /*if (!jumped) {
            for (i = 0; i < 9; ++i) {
                itemStack = mc.thePlayer.inventoryContainer.getSlot(i + 36).getStack();

                if (itemStack != null) {
                    final Item item = itemStack.getItem();

                    if (item instanceof ItemBow) {
                        for (int i = 0; i < mc.thePlayer.inventory.mainInventory.length; i++) {
                            final ItemStack stack = mc.thePlayer.inventory.mainInventory[i];

                            if (stack != null) {
                                if (stack.getItem().getUnlocalizedName().contains("arrow")) {

                                    if (mc.thePlayer.inventory.currentItem != slot && slot != i) PacketUtil.sendPacketWithoutEvent(new C09PacketHeldItemChange(i));
                                    slot = i;
                                }
                            }
                        }
                    }
                }
            }

            if (slot != -1) {
                itemStack = mc.thePlayer.inventoryContainer.getSlot(slot + 36).getStack();

                if (ticks == 5) {
                    mc.thePlayer.sendQueue.addToSendQueue(new C08PacketPlayerBlockPlacement(itemStack));
                }

                ticks++;

                if (ticks == 9) {
                    PacketUtil.sendPacketWithoutEvent(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
                    jumped = true;
                }
            }
        }*/
    }

    private void shootBow() {
        ticks = 1;
        jumped = false;
        flag = false;
    }
}
