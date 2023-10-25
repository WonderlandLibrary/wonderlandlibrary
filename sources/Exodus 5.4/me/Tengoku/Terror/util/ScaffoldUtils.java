/*
 * Decompiled with CFR 0.152.
 */
package me.Tengoku.Terror.util;

import me.Tengoku.Terror.util.RayCastUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntitySnowball;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovementInput;
import net.minecraft.util.Vec3;

public class ScaffoldUtils {
    protected static Minecraft mc;
    public float serverPitch;
    public float serverYaw;
    RayCastUtil raycast = new RayCastUtil();
    public static EntitySnowball snowball;
    private static final Minecraft MC;

    public static float[] getRotations(Entity entity) {
        double d;
        snowball = new EntitySnowball(Minecraft.theWorld);
        ScaffoldUtils.snowball.posX = Minecraft.thePlayer.posX;
        ScaffoldUtils.snowball.posY = Minecraft.thePlayer.posY - 10.0;
        ScaffoldUtils.snowball.posZ = Minecraft.thePlayer.posZ;
        double d2 = entity.posX - Minecraft.thePlayer.posX;
        double d3 = entity.posZ - Minecraft.thePlayer.posZ;
        if (entity instanceof EntityLivingBase) {
            EntityLivingBase entityLivingBase = (EntityLivingBase)entity;
            d = entityLivingBase.posY + (double)entityLivingBase.getEyeHeight() - Minecraft.thePlayer.posY + (double)Minecraft.thePlayer.getEyeHeight();
        } else {
            d = (entity.getEntityBoundingBox().minY + entity.getEntityBoundingBox().maxY) / 2.0 - Minecraft.thePlayer.posY + (double)Minecraft.thePlayer.getEyeHeight();
        }
        double d4 = MathHelper.sqrt_double(d2 * d2 + d3 * d3);
        float f = (float)(Math.atan2(d3, d2) * 180.0 / Math.PI) - 90.0f;
        float f2 = (float)(-(Math.atan2(d - (entity instanceof EntityPlayer ? 0.25 : 0.0), d4) * 180.0 / Math.PI));
        float f3 = ScaffoldUtils.changeRotation(Minecraft.thePlayer.rotationPitch, f2);
        float f4 = ScaffoldUtils.changeRotation(Minecraft.thePlayer.rotationYaw, f);
        return new float[]{f4, f3};
    }

    public static float[] getRotationsWatchdog(BlockPos blockPos, EnumFacing enumFacing) {
        double d = blockPos.getX();
        Minecraft.getMinecraft();
        double d2 = d - Minecraft.thePlayer.posX + (double)enumFacing.getFrontOffsetX();
        double d3 = blockPos.getZ();
        Minecraft.getMinecraft();
        double d4 = d3 - Minecraft.thePlayer.posZ + (double)enumFacing.getFrontOffsetZ();
        double d5 = blockPos.getY();
        Minecraft.getMinecraft();
        double d6 = Minecraft.thePlayer.posY;
        Minecraft.getMinecraft();
        double d7 = d6 + (double)Minecraft.thePlayer.getEyeHeight() + d5 + 90.0;
        double d8 = MathHelper.sqrt_double(d2 * d2 + d4 * d4);
        float f = (float)(Math.atan2(d4, d2) * 180.0 / Math.PI) - 140.0f;
        float f2 = (float)(Math.atan2(d7, d8) * 180.0 / Math.PI);
        return new float[]{f, f2};
    }

    public final double getRotationDifference(float[] fArray, float[] fArray2) {
        return Math.hypot(this.getDifference(fArray[0], fArray2[0]), fArray[1] - fArray2[1]);
    }

    public static float[] getRotations(BlockPos blockPos, EnumFacing enumFacing) {
        double d = (double)blockPos.getX() + 0.5;
        Minecraft.getMinecraft();
        double d2 = d - Minecraft.thePlayer.posX + (double)enumFacing.getFrontOffsetX() / 2.0;
        double d3 = (double)blockPos.getZ() + 0.5;
        Minecraft.getMinecraft();
        double d4 = d3 - Minecraft.thePlayer.posZ + (double)enumFacing.getFrontOffsetZ() / 2.0;
        double d5 = (double)blockPos.getY() + 0.5;
        Minecraft.getMinecraft();
        double d6 = Minecraft.thePlayer.posY;
        Minecraft.getMinecraft();
        double d7 = d6 + (double)Minecraft.thePlayer.getEyeHeight() - d5;
        double d8 = MathHelper.sqrt_double(d2 * d2 + d4 * d4);
        float f = (float)(Math.atan2(d4, d2) * 180.0 / Math.PI) - 90.0f;
        float f2 = (float)(Math.atan2(d7, d8) * 180.0 / Math.PI);
        return new float[]{f, f2};
    }

    public static final float[] getRotations(Vec3 vec3, boolean bl, double d) {
        Vec3 vec32 = new Vec3(Minecraft.thePlayer.posX + (bl ? Minecraft.thePlayer.motionX * d : 0.0), Minecraft.thePlayer.posY + (bl ? Minecraft.thePlayer.motionY * d : 0.0), Minecraft.thePlayer.posZ + (bl ? Minecraft.thePlayer.motionZ * d : 0.0));
        double d2 = vec3.xCoord + 0.5 - vec32.xCoord;
        double d3 = vec3.yCoord + 0.5 - (vec32.yCoord + (double)Minecraft.thePlayer.getEyeHeight());
        double d4 = vec3.zCoord + 0.5 - vec32.zCoord;
        double d5 = MathHelper.sqrt_double(d2 * d2 + d4 * d4);
        double d6 = Math.toDegrees(Math.atan2(d4, d2)) - 90.0;
        double d7 = -Math.toDegrees(Math.atan2(d3, d5));
        d6 = (double)Minecraft.thePlayer.rotationYaw + MathHelper.wrapAngleTo180_double(d6 - (double)Minecraft.thePlayer.rotationYaw);
        d7 = (double)Minecraft.thePlayer.rotationPitch + MathHelper.wrapAngleTo180_double(d7 - (double)Minecraft.thePlayer.rotationPitch);
        return new float[]{(float)d6, (float)d7};
    }

    public final double getRotationDifference(Entity entity) {
        float[] fArray = this.getRotationsAdvanced(entity, false, 1.0);
        return this.getRotationDifference(fArray, new float[]{Minecraft.thePlayer.rotationYaw, Minecraft.thePlayer.rotationPitch});
    }

    public final boolean isFaced(double d) {
        return this.raycast.raycastEntity(d, new float[]{this.serverYaw, this.serverPitch}) != null;
    }

    public final float getDifference(float f, float f2) {
        float f3 = (float)((double)(f - f2) % 360.0);
        if ((double)f3 < -180.0) {
            f3 = (float)((double)f3 + 360.0);
        }
        if ((double)f3 >= 180.0) {
            f3 = (float)((double)f3 - 360.0);
        }
        return f3;
    }

    public static int findBlockSlot() {
        int n = -1;
        int n2 = -1;
        int n3 = 36;
        while (n3 < 45) {
            ItemStack itemStack = Minecraft.thePlayer.inventoryContainer.getSlot(n3).getStack();
            if (itemStack != null && itemStack.getItem() instanceof ItemBlock && ScaffoldUtils.canIItemBePlaced(itemStack.getItem()) && itemStack.stackSize > 0 && itemStack.stackSize > n2) {
                n = n3;
                n2 = itemStack.stackSize;
            }
            ++n3;
        }
        return n;
    }

    public static int getLastHotbarSlot() {
        int n = -1;
        int n2 = 0;
        while (n2 < 9) {
            Minecraft.getMinecraft();
            ItemStack itemStack = Minecraft.thePlayer.inventory.mainInventory[n2];
            if (itemStack != null && itemStack.getItem() instanceof ItemBlock && ScaffoldUtils.canItemBePlaced(itemStack) && itemStack.stackSize > 1) {
                n = n2;
            }
            ++n2;
        }
        return n;
    }

    private Vec3 grabPosition(BlockPos blockPos, EnumFacing enumFacing) {
        Vec3 vec3 = new Vec3((double)enumFacing.getDirectionVec().getX() / 2.0, (double)enumFacing.getDirectionVec().getY() / 2.0 - 5.0, (double)enumFacing.getDirectionVec().getZ() / 2.0);
        Vec3 vec32 = new Vec3((double)blockPos.getX() + 0.5, (double)blockPos.getY() + 0.5, (double)blockPos.getZ() + 0.5);
        return vec32.add(vec3);
    }

    public static final float[] getRotationsAdvanced(Vec3 vec3, boolean bl, double d) {
        Vec3 vec32 = new Vec3(Minecraft.thePlayer.posX + (bl ? Minecraft.thePlayer.motionX * d : 0.0), Minecraft.thePlayer.posY + (bl ? Minecraft.thePlayer.motionY * d : 0.0), Minecraft.thePlayer.posZ + (bl ? Minecraft.thePlayer.motionZ * d : 0.0));
        double d2 = vec3.xCoord + 0.5 - vec32.xCoord;
        double d3 = vec3.yCoord + 0.5 - (vec32.yCoord + (double)Minecraft.thePlayer.getEyeHeight()) - 40.0;
        double d4 = vec3.zCoord + 0.5 - vec32.zCoord;
        double d5 = MathHelper.sqrt_double(d2 * d2 + d4 * d4);
        double d6 = Math.toDegrees(Math.atan2(d4, d2)) - 90.0;
        double d7 = -Math.toDegrees(Math.atan2(d3, d5));
        d6 = (double)Minecraft.thePlayer.rotationYaw + MathHelper.wrapAngleTo180_double(d6 - (double)Minecraft.thePlayer.rotationYaw);
        d7 = (double)Minecraft.thePlayer.rotationPitch + MathHelper.wrapAngleTo180_double(d7 - (double)Minecraft.thePlayer.rotationPitch);
        return new float[]{(float)d6, (float)d7};
    }

    public static final Vec3 getVectorForRotation(float f, float f2) {
        double d = Math.cos(Math.toRadians(-f) - Math.PI);
        double d2 = Math.sin(Math.toRadians(-f) - Math.PI);
        double d3 = -Math.cos(Math.toRadians(-f2));
        double d4 = Math.sin(Math.toRadians(-f2));
        return new Vec3(d2 * d3, d4, d * d3);
    }

    public static int getBlockCount() {
        int n = 0;
        int n2 = 0;
        while (n2 < 45) {
            Minecraft.getMinecraft();
            if (Minecraft.thePlayer.inventoryContainer.getSlot(n2).getHasStack()) {
                Minecraft.getMinecraft();
                ItemStack itemStack = Minecraft.thePlayer.inventoryContainer.getSlot(n2).getStack();
                Item item = itemStack.getItem();
                if (itemStack.getItem() instanceof ItemBlock && ScaffoldUtils.canIItemBePlaced(item)) {
                    n += itemStack.stackSize;
                }
            }
            ++n2;
        }
        return n;
    }

    public final float[] getServerRotations() {
        return new float[]{this.serverYaw, this.serverPitch};
    }

    public final void setRotations(float f, float f2) {
        this.serverYaw = f;
        this.serverPitch = f2;
    }

    public static boolean canItemBePlaced(ItemStack itemStack) {
        itemStack.getItem();
        if (Item.getIdFromItem(itemStack.getItem()) == 116) {
            return false;
        }
        itemStack.getItem();
        if (Item.getIdFromItem(itemStack.getItem()) == 30) {
            return false;
        }
        itemStack.getItem();
        if (Item.getIdFromItem(itemStack.getItem()) == 31) {
            return false;
        }
        itemStack.getItem();
        if (Item.getIdFromItem(itemStack.getItem()) == 175) {
            return false;
        }
        itemStack.getItem();
        if (Item.getIdFromItem(itemStack.getItem()) == 28) {
            return false;
        }
        itemStack.getItem();
        if (Item.getIdFromItem(itemStack.getItem()) == 27) {
            return false;
        }
        itemStack.getItem();
        if (Item.getIdFromItem(itemStack.getItem()) == 66) {
            return false;
        }
        itemStack.getItem();
        if (Item.getIdFromItem(itemStack.getItem()) == 157) {
            return false;
        }
        itemStack.getItem();
        if (Item.getIdFromItem(itemStack.getItem()) == 31) {
            return false;
        }
        itemStack.getItem();
        if (Item.getIdFromItem(itemStack.getItem()) == 6) {
            return false;
        }
        itemStack.getItem();
        if (Item.getIdFromItem(itemStack.getItem()) == 31) {
            return false;
        }
        itemStack.getItem();
        if (Item.getIdFromItem(itemStack.getItem()) == 32) {
            return false;
        }
        itemStack.getItem();
        if (Item.getIdFromItem(itemStack.getItem()) == 140) {
            return false;
        }
        itemStack.getItem();
        if (Item.getIdFromItem(itemStack.getItem()) == 390) {
            return false;
        }
        itemStack.getItem();
        if (Item.getIdFromItem(itemStack.getItem()) == 37) {
            return false;
        }
        itemStack.getItem();
        if (Item.getIdFromItem(itemStack.getItem()) == 38) {
            return false;
        }
        itemStack.getItem();
        if (Item.getIdFromItem(itemStack.getItem()) == 39) {
            return false;
        }
        itemStack.getItem();
        if (Item.getIdFromItem(itemStack.getItem()) == 40) {
            return false;
        }
        itemStack.getItem();
        if (Item.getIdFromItem(itemStack.getItem()) == 69) {
            return false;
        }
        itemStack.getItem();
        if (Item.getIdFromItem(itemStack.getItem()) == 50) {
            return false;
        }
        itemStack.getItem();
        if (Item.getIdFromItem(itemStack.getItem()) == 75) {
            return false;
        }
        itemStack.getItem();
        if (Item.getIdFromItem(itemStack.getItem()) == 76) {
            return false;
        }
        itemStack.getItem();
        if (Item.getIdFromItem(itemStack.getItem()) == 54) {
            return false;
        }
        itemStack.getItem();
        if (Item.getIdFromItem(itemStack.getItem()) == 130) {
            return false;
        }
        itemStack.getItem();
        if (Item.getIdFromItem(itemStack.getItem()) == 146) {
            return false;
        }
        itemStack.getItem();
        if (Item.getIdFromItem(itemStack.getItem()) == 342) {
            return false;
        }
        itemStack.getItem();
        if (Item.getIdFromItem(itemStack.getItem()) == 12) {
            return false;
        }
        itemStack.getItem();
        if (Item.getIdFromItem(itemStack.getItem()) == 77) {
            return false;
        }
        itemStack.getItem();
        if (Item.getIdFromItem(itemStack.getItem()) == 143) {
            return false;
        }
        itemStack.getItem();
        if (Item.getIdFromItem(itemStack.getItem()) == 46) {
            return false;
        }
        itemStack.getItem();
        return Item.getIdFromItem(itemStack.getItem()) != 145;
    }

    public static float changeRotation(float f, float f2) {
        float f3 = MathHelper.wrapAngleTo180_float(f2 - f);
        if (f3 > 1000.0f) {
            f3 = 1000.0f;
        }
        if (f3 < -1000.0f) {
            f3 = -1000.0f;
        }
        return f + f3;
    }

    public static float[] getRotationsBack(BlockPos blockPos, EnumFacing enumFacing) {
        double d = (double)blockPos.getX() + 0.5;
        Minecraft.getMinecraft();
        double d2 = d - Minecraft.thePlayer.posX + (double)enumFacing.getFrontOffsetX() / 2.0;
        double d3 = (double)blockPos.getZ() + 0.5;
        Minecraft.getMinecraft();
        double d4 = d3 - Minecraft.thePlayer.posZ + (double)enumFacing.getFrontOffsetZ() / 2.0;
        double d5 = (double)blockPos.getY() + 0.5 - 90.0;
        Minecraft.getMinecraft();
        double d6 = Minecraft.thePlayer.posY;
        Minecraft.getMinecraft();
        double d7 = d6 + (double)Minecraft.thePlayer.getEyeHeight() - d5;
        double d8 = MathHelper.sqrt_double(d2 * d2 + d4 * d4);
        float f = (float)(Math.atan2(d4, d2) * 180.0 / Math.PI) - 90.0f;
        float f2 = (float)(Math.atan2(d7, d8) * 180.0 / Math.PI);
        if (f < 0.0f) {
            f += 0.0f;
        }
        return new float[]{f, f2};
    }

    public final float[] smoothRotation(float[] fArray, float[] fArray2, float f) {
        float f2 = this.getDifference(fArray2[0], fArray[0]);
        float f3 = this.getDifference(fArray2[1], fArray[1]);
        float f4 = f;
        f4 = f2 > f ? f : Math.max(f2, -f);
        float f5 = f;
        f5 = f3 > f ? f : Math.max(f3, -f);
        float f6 = fArray[0] + f4;
        float f7 = fArray[1] + f5;
        return new float[]{f6, f7};
    }

    public final float[] getRotationsAdvanced(Entity entity, boolean bl, double d) {
        Vec3 vec3 = new Vec3(Minecraft.thePlayer.posX + (bl ? Minecraft.thePlayer.motionX * d : 0.0), Minecraft.thePlayer.posY + (double)(entity instanceof EntityLivingBase ? Minecraft.thePlayer.getEyeHeight() : 0.0f) + (bl ? Minecraft.thePlayer.motionY * d : 0.0), Minecraft.thePlayer.posZ + (bl ? Minecraft.thePlayer.motionZ * d : 0.0));
        Vec3 vec32 = new Vec3(entity.posX + (bl ? (entity.posX - entity.prevPosX) * d : 0.0), entity.posY + (bl ? (entity.posY - entity.prevPosY) * d : 0.0), entity.posZ + (bl ? (entity.posZ - entity.prevPosZ) * d : 0.0));
        double d2 = vec32.xCoord - vec3.xCoord;
        double d3 = entity instanceof EntityLivingBase ? vec32.yCoord + (double)((EntityLivingBase)entity).getEyeHeight() - vec3.yCoord : vec32.yCoord - vec3.yCoord;
        double d4 = vec32.zCoord - vec3.zCoord;
        double d5 = Math.sqrt(d2 * d2 + d4 * d4);
        double d6 = Math.toDegrees(Math.atan2(d4, d2)) - 90.0;
        double d7 = -Math.toDegrees(Math.atan2(d3, d5));
        return new float[]{(float)d6, (float)d7};
    }

    static {
        MC = Minecraft.getMinecraft();
        mc = Minecraft.getMinecraft();
    }

    public static boolean canIItemBePlaced(Item item) {
        if (Item.getIdFromItem(item) == 116) {
            return false;
        }
        if (Item.getIdFromItem(item) == 30) {
            return false;
        }
        if (Item.getIdFromItem(item) == 31) {
            return false;
        }
        if (Item.getIdFromItem(item) == 175) {
            return false;
        }
        if (Item.getIdFromItem(item) == 28) {
            return false;
        }
        if (Item.getIdFromItem(item) == 27) {
            return false;
        }
        if (Item.getIdFromItem(item) == 66) {
            return false;
        }
        if (Item.getIdFromItem(item) == 157) {
            return false;
        }
        if (Item.getIdFromItem(item) == 31) {
            return false;
        }
        if (Item.getIdFromItem(item) == 6) {
            return false;
        }
        if (Item.getIdFromItem(item) == 31) {
            return false;
        }
        if (Item.getIdFromItem(item) == 32) {
            return false;
        }
        if (Item.getIdFromItem(item) == 140) {
            return false;
        }
        if (Item.getIdFromItem(item) == 390) {
            return false;
        }
        if (Item.getIdFromItem(item) == 37) {
            return false;
        }
        if (Item.getIdFromItem(item) == 38) {
            return false;
        }
        if (Item.getIdFromItem(item) == 39) {
            return false;
        }
        if (Item.getIdFromItem(item) == 40) {
            return false;
        }
        if (Item.getIdFromItem(item) == 69) {
            return false;
        }
        if (Item.getIdFromItem(item) == 50) {
            return false;
        }
        if (Item.getIdFromItem(item) == 75) {
            return false;
        }
        if (Item.getIdFromItem(item) == 76) {
            return false;
        }
        if (Item.getIdFromItem(item) == 54) {
            return false;
        }
        if (Item.getIdFromItem(item) == 130) {
            return false;
        }
        if (Item.getIdFromItem(item) == 146) {
            return false;
        }
        if (Item.getIdFromItem(item) == 342) {
            return false;
        }
        if (Item.getIdFromItem(item) == 12) {
            return false;
        }
        if (Item.getIdFromItem(item) == 77) {
            return false;
        }
        if (Item.getIdFromItem(item) == 143) {
            return false;
        }
        return Item.getIdFromItem(item) != 46;
    }

    public final void setRotations(float[] fArray) {
        this.setRotations(fArray[0], fArray[1]);
    }

    public static int grabBlockSlot() {
        int n = -1;
        int n2 = -1;
        boolean bl = false;
        int n3 = 0;
        while (n3 < 36) {
            if (Minecraft.thePlayer != null) {
                Minecraft.getMinecraft();
                ItemStack itemStack = Minecraft.thePlayer.inventory.mainInventory[n3];
                if (itemStack != null && itemStack.getItem() instanceof ItemBlock && ScaffoldUtils.canItemBePlaced(itemStack) && itemStack.stackSize > 0) {
                    int n4;
                    Minecraft.getMinecraft();
                    if (Minecraft.thePlayer.inventory.mainInventory[n3].stackSize > n2 && n3 < 9) {
                        Minecraft.getMinecraft();
                        n2 = Minecraft.thePlayer.inventory.mainInventory[n3].stackSize;
                        n = n3;
                        if (n == ScaffoldUtils.getLastHotbarSlot()) {
                            bl = true;
                        }
                    }
                    if (n3 > 8 && !bl && (n4 = ScaffoldUtils.getFreeHotbarSlot()) != -1) {
                        Minecraft.getMinecraft();
                        if (Minecraft.thePlayer.inventory.mainInventory[n3].stackSize > n2) {
                            Minecraft.getMinecraft();
                            n2 = Minecraft.thePlayer.inventory.mainInventory[n3].stackSize;
                            n = n3;
                        }
                    }
                }
            }
            ++n3;
        }
        if (n > 8) {
            n3 = ScaffoldUtils.getFreeHotbarSlot();
            if (n3 != -1) {
                Minecraft.getMinecraft();
                Minecraft.getMinecraft();
                int n5 = Minecraft.thePlayer.inventoryContainer.windowId;
                Minecraft.getMinecraft();
                Minecraft.playerController.windowClick(n5, n, n3, 2, Minecraft.thePlayer);
            } else {
                return -1;
            }
        }
        return n;
    }

    public static int getFreeHotbarSlot() {
        int n = -1;
        int n2 = 0;
        while (n2 < 9) {
            Minecraft.getMinecraft();
            n = Minecraft.thePlayer.inventory.mainInventory[n2] == null ? n2 : 7;
            ++n2;
        }
        return n;
    }

    public static float clampRotation() {
        Minecraft.getMinecraft();
        float f = Minecraft.thePlayer.rotationYaw;
        float f2 = 1.0f;
        Minecraft.getMinecraft();
        MovementInput cfr_ignored_0 = Minecraft.thePlayer.movementInput;
        if (MovementInput.moveForward < 0.0f) {
            f += 180.0f;
            f2 = -0.5f;
        } else {
            Minecraft.getMinecraft();
            MovementInput cfr_ignored_1 = Minecraft.thePlayer.movementInput;
            if (MovementInput.moveForward > 0.0f) {
                f2 = 0.5f;
            }
        }
        Minecraft.getMinecraft();
        MovementInput cfr_ignored_2 = Minecraft.thePlayer.movementInput;
        if (MovementInput.moveStrafe > 0.0f) {
            f -= 90.0f * f2;
        }
        Minecraft.getMinecraft();
        MovementInput cfr_ignored_3 = Minecraft.thePlayer.movementInput;
        if (MovementInput.moveStrafe < 0.0f) {
            f += 90.0f * f2;
        }
        return f * ((float)Math.PI / 180);
    }
}

