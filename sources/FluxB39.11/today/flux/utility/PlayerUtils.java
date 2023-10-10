package today.flux.utility;

import com.google.common.collect.Multimap;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntitySnowball;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.util.*;
import org.lwjgl.util.vector.Vector3f;
import today.flux.Flux;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Created by John on 2016/10/20.
 */
public class PlayerUtils {
    public static Minecraft mc = Minecraft.getMinecraft();

    public static boolean isNotMoving() {
        return mc.thePlayer.moveForward == 0 && mc.thePlayer.moveStrafing == 0;
    }

    public static boolean isInventoryFull() {
        for (int index = 9; index <= 44; ++index) {
            final ItemStack stack = mc.thePlayer.inventoryContainer.getSlot(index).getStack();
            if (stack == null) {
                return false;
            }
        }
        return true;
    }

    public static double getDistanceToFall() {
        double distance = 0.0;
        for (double i = mc.thePlayer.posY; i > 0.0; --i) {
            final Block block = BlockUtils.getBlock(new BlockPos(mc.thePlayer.posX, i, mc.thePlayer.posZ));
            if (block.getMaterial() != Material.air && block.isBlockNormalCube() && block.isCollidable()) {
                distance = i;
                break;
            }
            if (i < 0.0) {
                break;
            }
        }
        final double distancetofall = mc.thePlayer.posY - distance - 1.0;
        return distancetofall;
    }

    public static float[] aimAtLocation(final double x, final double y, final double z, final EnumFacing facing) {
        final EntitySnowball temp = new EntitySnowball(mc.theWorld);
        temp.posX = x + 0.5;
        temp.posY = y + 0.5;
        temp.posZ = z + 0.5;
        final EntitySnowball entitySnowball = temp;
        entitySnowball.posX += facing.getDirectionVec().getX() * 0.25;
        final EntitySnowball entitySnowball2 = temp;
        entitySnowball2.posY += facing.getDirectionVec().getY() * 0.25;
        final EntitySnowball entitySnowball3 = temp;
        entitySnowball3.posZ += facing.getDirectionVec().getZ() * 0.25;
        return aimAtLocation(temp.posX, temp.posY, temp.posZ);
    }

    public static float[] aimAtLocation(final double positionX, final double positionY, final double positionZ) {
        final double x = positionX - mc.thePlayer.posX;
        final double y = positionY - mc.thePlayer.posY;
        final double z = positionZ - mc.thePlayer.posZ;
        final double distance = MathHelper.sqrt_double(x * x + z * z);
        return new float[]{(float) (Math.atan2(z, x) * 180.0 / 3.141592653589793) - 90.0f,
                (float) (-(Math.atan2(y, distance) * 180.0 / 3.141592653589793))};
    }

    public static void tellPlayer(String string) {
        if (string != null && mc.thePlayer != null)
            mc.thePlayer.addChatMessage(new ChatComponentText(EnumChatFormatting.GRAY + "[" + EnumChatFormatting.WHITE + Flux.NAME + EnumChatFormatting.GRAY + "]: " + EnumChatFormatting.GRAY + string));
    }

    public void portMove(float yaw, float multiplyer, float up) {
        double moveX = -Math.sin(Math.toRadians((double) yaw)) * (double) multiplyer;
        double moveZ = Math.cos(Math.toRadians((double) yaw)) * (double) multiplyer;
        double moveY = (double) up;
        this.mc.thePlayer.setPosition(moveX + this.mc.thePlayer.posX, moveY + this.mc.thePlayer.posY,
                moveZ + this.mc.thePlayer.posZ);
    }

    public static double getBaseMoveSpeed() {
        double baseSpeed = 0.2873;
        if (mc.thePlayer.isPotionActive(Potion.moveSpeed)) {
            int amplifier = mc.thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier();
            baseSpeed *= 1.0 + 0.2 * (amplifier + 1);
        }
        return baseSpeed;
    }

    public static float getDirection() {
        float yaw = mc.thePlayer.rotationYaw;
        if (mc.thePlayer.moveForward < 0.0f) {
            yaw += 180.0f;
        }
        float forward = 1.0f;
        if (mc.thePlayer.moveForward < 0.0f) {
            forward = -0.5f;
        } else if (mc.thePlayer.moveForward > 0.0f) {
            forward = 0.5f;
        }
        if (mc.thePlayer.moveStrafing > 0.0f) {
            yaw -= 90.0f * forward;
        }
        if (mc.thePlayer.moveStrafing < 0.0f) {
            yaw += 90.0f * forward;
        }
        yaw *= 0.017453292f;
        return yaw;
    }

    public static boolean isInWater() {
        return mc.theWorld.getBlockState(
                        new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ))
                .getBlock().getMaterial() == Material.water;
    }

    public static void toFwd(final double speed) {
        final float yaw = mc.thePlayer.rotationYaw * 0.017453292f;
        final EntityPlayerSP thePlayer = mc.thePlayer;
        thePlayer.motionX -= MathHelper.sin(yaw) * speed;
        final EntityPlayerSP thePlayer2 = mc.thePlayer;
        thePlayer2.motionZ += MathHelper.cos(yaw) * speed;
    }

    public static void setSpeed(final double speed) {
        mc.thePlayer.motionX = -(Math.sin(getDirection()) * speed);
        mc.thePlayer.motionZ = Math.cos(getDirection()) * speed;
    }

    public static double getSpeed() {
        return getSpeed(mc.thePlayer);
    }

    public static double getSpeed(Entity entity) {
        return Math.sqrt(entity.motionX * entity.motionX + entity.motionZ * entity.motionZ);
    }

    public static Block getBlockUnderPlayer(final EntityPlayer inPlayer) {
        return getBlock(new BlockPos(inPlayer.posX, inPlayer.posY - 1.0, inPlayer.posZ));
    }

    public static Block getBlock(final BlockPos pos) {
        return mc.theWorld.getBlockState(pos).getBlock();
    }

    public static Block getBlockAtPosC(final EntityPlayer inPlayer, final double x, final double y, final double z) {
        return getBlock(new BlockPos(inPlayer.posX - x, inPlayer.posY - y, inPlayer.posZ - z));
    }

    public static ArrayList<Vector3f> vanillaTeleportPositions(final double tpX, final double tpY, final double tpZ,
                                                               final double speed) {
        final ArrayList<Vector3f> positions = new ArrayList<Vector3f>();
        final double posX = tpX - mc.thePlayer.posX;
        final double posY = tpY - (mc.thePlayer.posY + mc.thePlayer.getEyeHeight() + 1.1);
        final double posZ = tpZ - mc.thePlayer.posZ;
        final float yaw = (float) (Math.atan2(posZ, posX) * 180.0 / 3.141592653589793 - 90.0);
        final float pitch = (float) (-Math.atan2(posY, Math.sqrt(posX * posX + posZ * posZ)) * 180.0
                / 3.141592653589793);
        double tmpX = mc.thePlayer.posX;
        double tmpY = mc.thePlayer.posY;
        double tmpZ = mc.thePlayer.posZ;
        double steps = 1.0;
        for (double d = speed; d < getDistance(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, tpX, tpY,
                tpZ); d += speed) {
            ++steps;
        }
        for (double d = speed; d < getDistance(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, tpX, tpY,
                tpZ); d += speed) {
            tmpX = mc.thePlayer.posX - Math.sin(getDirection(yaw)) * d;
            tmpZ = mc.thePlayer.posZ + Math.cos(getDirection(yaw)) * d;
            tmpY -= (mc.thePlayer.posY - tpY) / steps;
            positions.add(new Vector3f((float) tmpX, (float) tmpY, (float) tmpZ));
        }
        positions.add(new Vector3f((float) tpX, (float) tpY, (float) tpZ));
        return positions;
    }

    public static float getDirection(float yaw) {
        if (mc.thePlayer.moveForward < 0.0f) {
            yaw += 180.0f;
        }
        float forward = 1.0f;
        if (mc.thePlayer.moveForward < 0.0f) {
            forward = -0.5f;
        } else if (mc.thePlayer.moveForward > 0.0f) {
            forward = 0.5f;
        }
        if (mc.thePlayer.moveStrafing > 0.0f) {
            yaw -= 90.0f * forward;
        }
        if (mc.thePlayer.moveStrafing < 0.0f) {
            yaw += 90.0f * forward;
        }
        yaw *= 0.017453292f;
        return yaw;
    }

    public static double getDistance(final double x1, final double y1, final double z1, final double x2,
                                     final double y2, final double z2) {
        final double d0 = x1 - x2;
        final double d2 = y1 - y2;
        final double d3 = z1 - z2;
        return MathHelper.sqrt_double(d0 * d0 + d2 * d2 + d3 * d3);
    }

    public static boolean MovementInput() {
        return mc.gameSettings.keyBindForward.pressed || mc.gameSettings.keyBindLeft.pressed
                || mc.gameSettings.keyBindRight.pressed || mc.gameSettings.keyBindBack.pressed;
    }

    public static void blockHit(Entity en, boolean value) {
        ItemStack stack = mc.thePlayer.getCurrentEquippedItem();

        if (mc.thePlayer.getCurrentEquippedItem() != null && en != null && value) {
            if (stack.getItem() instanceof ItemSword && mc.thePlayer.swingProgress > 0.2) {
                mc.thePlayer.getCurrentEquippedItem().useItemRightClick(mc.theWorld, mc.thePlayer);
            }
        }
    }

    public static float getItemAtkDamage(ItemStack itemStack) {
        final Multimap multimap = itemStack.getAttributeModifiers();
        if (!multimap.isEmpty()) {
            final Iterator iterator = multimap.entries().iterator();
            if (iterator.hasNext()) {
                final Map.Entry entry = (Entry) iterator.next();
                final AttributeModifier attributeModifier = (AttributeModifier) entry.getValue();
                double damage = attributeModifier.getOperation() != 1 && attributeModifier.getOperation() != 2
                        ? attributeModifier.getAmount()
                        : attributeModifier.getAmount() * 100.0;

                if (attributeModifier.getAmount() > 1.0) {
                    return 1.0f + (float) damage;
                }
                return 1.0f;
            }
        }
        return 1.0f;
    }

    public static int bestWeapon(Entity target) {
        int firstSlot = mc.thePlayer.inventory.currentItem = 0;
        int bestWeapon = -1;
        int j = 1;

        for (byte i = 0; i < 9; i++) {
            mc.thePlayer.inventory.currentItem = i;
            ItemStack itemStack = mc.thePlayer.getHeldItem();

            if (itemStack != null) {
                int itemAtkDamage = (int) getItemAtkDamage(itemStack);
                itemAtkDamage += EnchantmentHelper.getModifierForCreature(itemStack, EnumCreatureAttribute.UNDEFINED);

                if (itemAtkDamage > j) {
                    j = itemAtkDamage;
                    bestWeapon = i;
                }
            }
        }

        if (bestWeapon != -1) {
            return bestWeapon;
        } else {
            return firstSlot;
        }
    }

    public static void shiftClick(Item i) {
        for (int i1 = 9; i1 < 37; ++i1) {
            ItemStack itemstack = mc.thePlayer.inventoryContainer.getSlot(i1).getStack();

            if (itemstack != null && itemstack.getItem() == i) {
                mc.playerController.windowClick(0, i1, 0, 1, mc.thePlayer);
                break;
            }
        }
    }

    public static boolean hotbarIsFull() {
        for (int i = 0; i <= 36; ++i) {
            ItemStack itemstack = mc.thePlayer.inventory.getStackInSlot(i);

            if (itemstack == null) {
                return false;
            }
        }

        return true;
    }

    public static boolean isMoving() {
        if ((!mc.thePlayer.isCollidedHorizontally) && (!mc.thePlayer.isSneaking())) {
            return ((mc.thePlayer.movementInput.moveForward != 0.0F || mc.thePlayer.movementInput.moveStrafe != 0.0F));
        }
        return false;
    }

    public static boolean isMoving2() {
        return ((mc.thePlayer.moveForward != 0.0F || mc.thePlayer.moveStrafing != 0.0F));
    }

    public static boolean isAirUnder(Entity ent) {
        return mc.theWorld.getBlockState(new BlockPos(ent.posX, ent.posY - 1, ent.posZ)).getBlock() == Blocks.air;
    }

    public static void blinkToPos(double[] startPos, BlockPos endPos, double slack, double[] pOffset) {
        double curX = startPos[0];
        double curY = startPos[1];
        double curZ = startPos[2];
        double endX = (double) endPos.getX() + 0.5D;
        double endY = (double) endPos.getY() + 1.0D;
        double endZ = (double) endPos.getZ() + 0.5D;
        double distance = Math.abs(curX - endX) + Math.abs(curY - endY) + Math.abs(curZ - endZ);

        for (int count = 0; distance > slack; ++count) {
            distance = Math.abs(curX - endX) + Math.abs(curY - endY) + Math.abs(curZ - endZ);
            if (count > 120) {
                break;
            }

            boolean next = false;
            double diffX = curX - endX;
            double diffY = curY - endY;
            double diffZ = curZ - endZ;
            double offset = (count & 1) == 0 ? pOffset[0] : pOffset[1];
            if (diffX < 0.0D) {
                if (Math.abs(diffX) > offset) {
                    curX += offset;
                } else {
                    curX += Math.abs(diffX);
                }
            }

            if (diffX > 0.0D) {
                if (Math.abs(diffX) > offset) {
                    curX -= offset;
                } else {
                    curX -= Math.abs(diffX);
                }
            }

            if (diffY < 0.0D) {
                if (Math.abs(diffY) > 0.25D) {
                    curY += 0.25D;
                } else {
                    curY += Math.abs(diffY);
                }
            }

            if (diffY > 0.0D) {
                if (Math.abs(diffY) > 0.25D) {
                    curY -= 0.25D;
                } else {
                    curY -= Math.abs(diffY);
                }
            }

            if (diffZ < 0.0D) {
                if (Math.abs(diffZ) > offset) {
                    curZ += offset;
                } else {
                    curZ += Math.abs(diffZ);
                }
            }

            if (diffZ > 0.0D) {
                if (Math.abs(diffZ) > offset) {
                    curZ -= offset;
                } else {
                    curZ -= Math.abs(diffZ);
                }
            }

            mc.getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(curX, curY, curZ, true));
        }

    }

    public static void disconnectServer(String customMessage) {
        mc.thePlayer.sendQueue.getNetworkManager().closeChannel(new ChatComponentText(EnumChatFormatting.DARK_PURPLE + "[Flux] " + EnumChatFormatting.GRAY + customMessage));
    }
}
