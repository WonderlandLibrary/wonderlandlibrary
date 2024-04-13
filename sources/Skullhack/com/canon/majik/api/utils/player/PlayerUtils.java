package com.canon.majik.api.utils.player;

import com.canon.majik.api.utils.Globals;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.potion.Potion;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.Objects;

public class PlayerUtils implements Globals {

    public static boolean isMoving(){
        return mc.gameSettings.keyBindForward.isKeyDown() ||
                mc.gameSettings.keyBindBack.isKeyDown() ||
                mc.gameSettings.keyBindLeft.isKeyDown() ||
                mc.gameSettings.keyBindRight.isKeyDown();
    }

    public static void placeBlock(BlockPos pos, EnumFacing face, EnumHand hand, boolean packet){
        if(packet){
            mc.player.connection.sendPacket(new CPacketPlayerTryUseItemOnBlock(pos, face, hand,0,0,0));
        }else{
            mc.playerController.processRightClickBlock(mc.player,mc.world,pos,face,new Vec3d(0,0,0),hand);
        }
    }


    public static float[] strafe(double motionSpeed) {
        float forward = mc.player.movementInput.moveForward;
        float strafe = mc.player.movementInput.moveStrafe;
        float yaw = mc.player.prevRotationYaw + (mc.player.rotationYaw - mc.player.prevRotationYaw) * mc.getRenderPartialTicks();

        if (forward != 0) {
            if (strafe > 0) {
                yaw += forward > 0 ? -45 : 45;
            } else if (strafe < 0) {
                yaw += forward > 0 ? 45 : -45;
            }

            strafe = 0;

            if (forward > 0) {
                forward = 1;
            } else if (forward < 0) {
                forward = -1;
            }
        }

        double cos = Math.cos(Math.toRadians(yaw));
        double sin = -Math.sin(Math.toRadians(yaw));

        float posX = (float) ((forward * motionSpeed * sin) + (strafe * motionSpeed * cos));
        float posZ = (float) ((forward * motionSpeed * cos) - (strafe * motionSpeed * sin));

        return new float[]{(float) posX, (float) posZ};
    }

    //good for later
    public static float getBaseMoveSpeed(float accel) {
        float baseSpeed = 0.2873f;
        if (mc.player != null && mc.player.isPotionActive(Objects.requireNonNull(Potion.getPotionById(1)))) {
            int amplifier = Objects.requireNonNull(mc.player.getActivePotionEffect(Objects.requireNonNull(Potion.getPotionById(1)))).getAmplifier();
            baseSpeed *= 1.0 + 0.2 * (amplifier + 1);
        }
        return baseSpeed *= accel;
    }

    public static float getBaseMoveSpeed() {
        float baseSpeed = 0.2873f;
        if (mc.player != null && mc.player.isPotionActive(Objects.requireNonNull(Potion.getPotionById(1)))) {
            int amplifier = Objects.requireNonNull(mc.player.getActivePotionEffect(Objects.requireNonNull(Potion.getPotionById(1)))).getAmplifier();
            baseSpeed *= 1.0 + 0.2 * (amplifier + 1);
        }
        return baseSpeed;
    }

    public static void switchToSlot(int slot) {
        if (slot == -1) {
            return;
        }
        if (mc.getConnection() != null) {
            mc.getConnection().getNetworkManager().channel().writeAndFlush(new CPacketHeldItemChange(slot));
        }
        mc.player.inventory.currentItem = slot;
        mc.playerController.updateController();
    }

    public static int getItemSlot(Item items) {
        for (int i = 0; i < 36; ++i) {
            Item item = mc.player.inventory.getStackInSlot(i).getItem();
            if (item == items) {
                if (i < 9) {
                    i += 36;
                }

                return i;
            }
        }
        return -1;
    }

    public static ArrayList<ItemStackUtil> getAllItems() {
        final ArrayList<ItemStackUtil> items = new ArrayList<ItemStackUtil>();
        for (int i = 0; i < 36; ++i) {
            items.add(new ItemStackUtil(getItemStack(i), i));
        }
        return items;
    }

    public static int getTotalAmountOfItem(Item item) {
        int amountOfItem = 0;
        for(int i = 0; i < 36; i++) {
            ItemStack stack = Minecraft.getMinecraft().player.inventory.getStackInSlot(i);
            if(stack.getItem() == item)
                amountOfItem += stack.getCount();
        }
        if(Minecraft.getMinecraft().player.getHeldItemOffhand().getItem() == item)
            amountOfItem += Minecraft.getMinecraft().player.getHeldItemOffhand().getCount();
        return amountOfItem;
    }

    public static ItemStack getItemStack(final int id) {
        try {
            return mc.player.inventory.getStackInSlot(id);
        }
        catch (NullPointerException e) {
            return null;
        }
    }

    public static void clickSlot(final int id) {
        if (id != -1) {
            try {
                mc.playerController.windowClick(mc.player.openContainer.windowId, getClickSlot(id), 0, ClickType.PICKUP, (EntityPlayer)mc.player);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static int getClickSlot(int id) {
        if (id == -1) {
            return id;
        }
        if (id < 9) {
            id += 36;
            return id;
        }
        if (id == 39) {
            id = 5;
        }
        else if (id == 38) {
            id = 6;
        }
        else if (id == 37) {
            id = 7;
        }
        else if (id == 36) {
            id = 8;
        }
        else if (id == 40) {
            id = 45;
        }
        return id;
    }


    public static class ItemStackUtil
    {
        public ItemStack itemStack;
        public int slotId;

        public ItemStackUtil(final ItemStack itemStack, final int slotId) {
            this.itemStack = itemStack;
            this.slotId = slotId;
        }
    }
}
