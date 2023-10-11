/*
 * Decompiled with CFR 0_115.
 */
package cow.milkgod.cheese.utils;

import cow.milkgod.cheese.Cheese;
import cow.milkgod.cheese.managers.NetHandlerPlayClientHook;
import cow.milkgod.cheese.module.Module;
import cow.milkgod.cheese.module.ModuleManager;
import cow.milkgod.cheese.ttf.FontUtils;
import cow.milkgod.cheese.utils.SessionUtils;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C01PacketChatMessage;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Session;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

public class Wrapper {
    public static Minecraft mc = Minecraft.getMinecraft();
    public static FontRenderer fr = Wrapper.mc.fontRendererObj;
    public static FontUtils fu_default = new FontUtils("Audiowide", 0, 19);
    public static FontUtils fu_default_big = new FontUtils("Audiowide", 0, 27);
    public static FontUtils fu_veranda = new FontUtils("Veranda Bold", 0, 18);
    public static FontUtils fu_robotochat = new FontUtils("Roboto", 0, 16);

    public static void sendPacket(Packet p2) {
        mc.getNetHandler().getNetworkManager().sendPacket(p2);
    }

    public static Block getBlockAtPos(BlockPos inBlockPos) {
        IBlockState s = Wrapper.mc.theWorld.getBlockState(inBlockPos);
        return s.getBlock();
    }

    public static BlockPos getBlockPos(BlockPos inBlockPos) {
        return inBlockPos;
    }

    public static void useCommand(String commandAndArguments) {
    }

    public static Block getBlockUnderPlayer(EntityPlayer inPlayer) {
        return Wrapper.getBlockAtPos(new BlockPos(inPlayer.posX, inPlayer.posY + (Wrapper.mc.thePlayer.motionY + 0.1) - 1.2, inPlayer.posZ));
    }

    public static Block getBlockAbovePlayer(EntityPlayer inPlayer, double blocks) {
        return Wrapper.getBlockAtPos(new BlockPos(inPlayer.posX, inPlayer.posY + (blocks += (double)inPlayer.height), inPlayer.posZ));
    }

    public static Block getBlockAtPosC(EntityPlayer inPlayer, double x2, double y2, double z2) {
        return Wrapper.getBlockAtPos(new BlockPos(inPlayer.posX - x2, inPlayer.posY - y2, inPlayer.posZ - z2));
    }

    public static BlockPos getBlockPos(double x2, double y2, double z2) {
        return Wrapper.getBlockPos(new BlockPos(x2, y2, z2));
    }

    public static Block getBlockUnderPlayer2(EntityPlayer inPlayer, double height) {
        return Wrapper.getBlockAtPos(new BlockPos(inPlayer.posX, inPlayer.posY - height, inPlayer.posZ));
    }

    public static Minecraft getMinecraft() {
        return Minecraft.getMinecraft();
    }

    public static EntityPlayerSP getPlayer() {
        return Wrapper.getMinecraft().thePlayer;
    }

    public static WorldClient getWorld() {
        return Wrapper.getMinecraft().theWorld;
    }

    public static FontRenderer getFontRenderer() {
        return Wrapper.getMinecraft().fontRendererObj;
    }

    public static void addChatMessage(String msg, boolean hide) {
        if (hide) {
            Wrapper.getPlayer().addChatComponentMessage(new ChatComponentTranslation(msg, new Object[0]));
        } else {
            Cheese.getInstance();
            Wrapper.getPlayer().addChatComponentMessage(new ChatComponentTranslation("\u00a78[\u00a79" + Cheese.getClient_Name() + "\u00a78] \u00a77" + msg, new Object[0]));
        }
    }

    public static void addChatMessage(String msg) {
        Wrapper.addChatMessage(msg, false);
    }

    public static void addErrorChatMessage(String msg) {
        Wrapper.addChatMessage(msg);
    }

    public static void sendChatMessage(String msg) {
        Wrapper.sendPacket(new C01PacketChatMessage(msg));
    }

    public static PlayerControllerMP getPlayerController() {
        return Wrapper.getMinecraft().playerController;
    }

    public static NetHandlerPlayClient getSendQueue() {
        return Wrapper.getPlayer().sendQueue;
    }

    public static GameSettings getGameSettings() {
        return Wrapper.getMinecraft().gameSettings;
    }

    public static double getDistance(Entity e2, BlockPos pos) {
        return MathHelper.sqrt_float((float)(e2.posX - (double)pos.getX()) * (float)(e2.posX - (double)pos.getX()) + (float)(e2.posY - (double)pos.getY()) * (float)(e2.posY - (double)pos.getY()) + (float)(e2.posZ - (double)pos.getZ()) * (float)(e2.posZ - (double)pos.getZ()));
    }

    public static void sendPacketDirect(Packet packet) {
        NetHandlerPlayClientHook.netManager.sendPacket(packet);
    }

    public static Block getBlock(BlockPos pos) {
        return Minecraft.getMinecraft().theWorld.getBlockState(pos).getBlock();
    }

    public static InventoryPlayer getInventory() {
        return Wrapper.getPlayer().inventory;
    }

    public static List<Module> getRenderArrayList() {
        Cheese.getInstance();
        return ModuleManager.activeModules;
    }

    public static Session getSession() {
        return SessionUtils.getSession();
    }

    public static MovingObjectPosition getMouseOver() {
        return Wrapper.getMinecraft().objectMouseOver;
    }

    public static boolean isMoving(Entity e2) {
        if (e2.motionX != 0.0 && e2.motionZ != 0.0 && (e2.motionY != 0.0 || e2.motionY > 0.0)) {
            return true;
        }
        return false;
    }

    public static boolean isOnLiquid(AxisAlignedBB boundingBox) {
        boundingBox = boundingBox.contract(0.01, 0.0, 0.01).offset(0.0, -0.01, 0.0);
        boolean onLiquid = false;
        int y2 = (int)boundingBox.minY;
        int x2 = MathHelper.floor_double(boundingBox.minX);
        while (x2 < MathHelper.floor_double(boundingBox.maxX + 1.0)) {
            int z2 = MathHelper.floor_double(boundingBox.minZ);
            while (z2 < MathHelper.floor_double(boundingBox.maxZ + 1.0)) {
                Block block = Wrapper.getBlock(new BlockPos(x2, y2, z2));
                if (block != Blocks.air) {
                    if (!(block instanceof BlockLiquid)) {
                        return false;
                    }
                    onLiquid = true;
                }
                ++z2;
            }
            ++x2;
        }
        return onLiquid;
    }

    public static boolean isInLiquid(AxisAlignedBB par1AxisAlignedBB) {
        par1AxisAlignedBB = par1AxisAlignedBB.contract(0.001, 0.001, 0.001);
        int var4 = MathHelper.floor_double(par1AxisAlignedBB.minX);
        int var5 = MathHelper.floor_double(par1AxisAlignedBB.maxX + 1.0);
        int var6 = MathHelper.floor_double(par1AxisAlignedBB.minY);
        int var7 = MathHelper.floor_double(par1AxisAlignedBB.maxY + 1.0);
        int var8 = MathHelper.floor_double(par1AxisAlignedBB.minZ);
        int var9 = MathHelper.floor_double(par1AxisAlignedBB.maxZ + 1.0);
        if (Wrapper.getWorld().getChunkFromBlockCoords(new BlockPos(Wrapper.getPlayer().posX, Wrapper.getPlayer().posY, Wrapper.getPlayer().posZ)) == null) {
            return false;
        }
        Vec3 var11 = new Vec3(0.0, 0.0, 0.0);
        int var12 = var4;
        while (var12 < var5) {
            int var13 = var6;
            while (var13 < var7) {
                int var14 = var8;
                while (var14 < var9) {
                    Block var15 = Wrapper.getBlock(new BlockPos(var12, var13, var14));
                    if (var15 instanceof BlockLiquid) {
                        return true;
                    }
                    ++var14;
                }
                ++var13;
            }
            ++var12;
        }
        return false;
    }

    public static String getDirection() {
        return Wrapper.getMinecraft().func_175606_aa().func_174811_aO().name();
    }

    public Float[] getRotationToPosition(Entity e2, BlockPos pos) {
        double x2 = (double)pos.getX() - e2.posX;
        double y2 = (double)pos.getY() - (e2.posY + (double)e2.getEyeHeight());
        double z2 = (double)pos.getZ() - e2.posZ;
        double helper = MathHelper.sqrt_double(x2 * x2 + z2 * z2);
        float newYaw = (float)Math.toDegrees(- Math.atan(x2 / z2));
        float newPitch = (float)(- Math.toDegrees(Math.atan(y2 / helper)));
        if (z2 < 0.0 && x2 < 0.0) {
            newYaw = (float)(90.0 + Math.toDegrees(Math.atan(z2 / x2)));
        } else if (z2 < 0.0 && x2 > 0.0) {
            newYaw = (float)(-90.0 + Math.toDegrees(Math.atan(z2 / x2)));
        }
        return new Float[]{Float.valueOf(newYaw), Float.valueOf(newPitch)};
    }

    public static boolean isInBlock(Entity e2, float offset) {
        int x2 = MathHelper.floor_double(e2.getEntityBoundingBox().minX);
        while (x2 < MathHelper.floor_double(e2.getEntityBoundingBox().maxX) + 1) {
            int y2 = MathHelper.floor_double(e2.getEntityBoundingBox().minY);
            while (y2 < MathHelper.floor_double(e2.getEntityBoundingBox().maxY) + 1) {
                int z2 = MathHelper.floor_double(e2.getEntityBoundingBox().minZ);
                while (z2 < MathHelper.floor_double(e2.getEntityBoundingBox().maxZ) + 1) {
                    AxisAlignedBB boundingBox;
                    Block block = Wrapper.getWorld().getBlockState(new BlockPos((double)x2, (float)y2 + offset, (double)z2)).getBlock();
                    if (block != null && !(block instanceof BlockAir) && !(block instanceof BlockLiquid) && (boundingBox = block.getCollisionBoundingBox(Wrapper.getWorld(), new BlockPos((double)x2, (float)y2 + offset, (double)z2), Wrapper.getWorld().getBlockState(new BlockPos((double)x2, (float)y2 + offset, (double)z2)))) != null && e2.getEntityBoundingBox().intersectsWith(boundingBox)) {
                        return true;
                    }
                    ++z2;
                }
                ++y2;
            }
            ++x2;
        }
        return false;
    }

    public static float[] getBlockRotations(double x2, double y2, double z2) {
        double var4 = x2 - Wrapper.mc.thePlayer.posX + 0.5;
        double var6 = z2 - Wrapper.mc.thePlayer.posZ + 0.5;
        double var8 = y2 - (Wrapper.mc.thePlayer.posY + (double)Wrapper.mc.thePlayer.getEyeHeight() - 1.0);
        double var14 = MathHelper.sqrt_double(var4 * var4 + var6 * var6);
        float var12 = (float)(Math.atan2(var6, var4) * 180.0 / 3.141592653589793) - 90.0f;
        return new float[]{var12, (float)(- Math.atan2(var8, var14) * 180.0 / 3.141592653589793)};
    }

    public static float getDistanceToGround(Entity e2) {
        if (Wrapper.getPlayer().isCollidedVertically) {
            return 0.0f;
        }
        float a2 = (float)e2.posY;
        while (a2 > 0.0f) {
            int[] stairs = new int[]{53, 67, 108, 109, 114, 128, 134, 135, 136, 156, 163, 164, 180};
            int[] exemptIds = new int[]{6, 27, 28, 30, 31, 32, 37, 38, 39, 40, 50, 51, 55, 59, 63, 65, 66, 68, 69, 70, 72, 75, 76, 77, 83, 92, 93, 94, 104, 105, 106, 115, 119, 131, 132, 143, 147, 148, 149, 150, 157, 171, 175, 176, 177};
            Block block = Wrapper.getBlock(new BlockPos(e2.posX, a2 - 1.0f, e2.posZ));
            if (!(block instanceof BlockAir)) {
                int id2;
                if (Block.getIdFromBlock(block) == 44 || Block.getIdFromBlock(block) == 126) {
                    return (float)(e2.posY - (double)a2 - 0.5) < 0.0f ? 0.0f : (float)(e2.posY - (double)a2 - 0.5);
                }
                int[] arrn = stairs;
                int n = arrn.length;
                int n2 = 0;
                while (n2 < n) {
                    id2 = arrn[n2];
                    if (Block.getIdFromBlock(block) == id2) {
                        return (float)(e2.posY - (double)a2 - 1.0) < 0.0f ? 0.0f : (float)(e2.posY - (double)a2 - 1.0);
                    }
                    ++n2;
                }
                arrn = exemptIds;
                n = arrn.length;
                n2 = 0;
                while (n2 < n) {
                    id2 = arrn[n2];
                    if (Block.getIdFromBlock(block) == id2) {
                        return (float)(e2.posY - (double)a2) < 0.0f ? 0.0f : (float)(e2.posY - (double)a2);
                    }
                    ++n2;
                }
                return (float)(e2.posY - (double)a2 + block.getBlockBoundsMaxY() - 1.0);
            }
            a2 -= 1.0f;
        }
        return 0.0f;
    }

    public static Block getBlockatPosSpeed(EntityPlayer inPlayer, float x2, float z2) {
        double posX = inPlayer.posX + inPlayer.motionX * (double)x2;
        double posZ = inPlayer.posZ + inPlayer.motionZ * (double)z2;
        return Wrapper.getBlockAtPos(new BlockPos(posX, inPlayer.posY, posZ));
    }

    public static boolean isMoving() {
        if (Wrapper.mc.thePlayer.lastTickPosX != Wrapper.mc.thePlayer.posX && Wrapper.mc.thePlayer.lastTickPosZ != Wrapper.mc.thePlayer.posZ) {
            return true;
        }
        return false;
    }

    public static boolean isOnLiquid() {
        boolean onLiquid = false;
        if (Wrapper.getBlockAtPosC(Wrapper.mc.thePlayer, 0.30000001192092896, 0.10000000149011612, 0.30000001192092896).getMaterial().isLiquid() && Wrapper.getBlockAtPosC(Wrapper.mc.thePlayer, -0.30000001192092896, 0.10000000149011612, -0.30000001192092896).getMaterial().isLiquid()) {
            onLiquid = true;
        }
        return onLiquid;
    }

    public static boolean isInLiquid() {
        boolean inLiquid = false;
        if (Wrapper.getBlockAtPosC(Wrapper.mc.thePlayer, 0.30000001192092896, 0.0, 0.30000001192092896).getMaterial().isLiquid() || Wrapper.getBlockAtPosC(Wrapper.mc.thePlayer, -0.30000001192092896, 0.0, -0.30000001192092896).getMaterial().isLiquid()) {
            inLiquid = true;
        }
        return inLiquid;
    }

    public static boolean isInFire() {
        boolean isInFire = false;
        if (Wrapper.getBlockAtPosC(Wrapper.mc.thePlayer, 0.30000001192092896, 0.0, 0.30000001192092896).getMaterial() == Material.fire || Wrapper.getBlockAtPosC(Wrapper.mc.thePlayer, -0.30000001192092896, 0.0, -0.30000001192092896).getMaterial() == Material.fire) {
            isInFire = true;
        }
        return isInFire;
    }

    public static String liquidCollision() {
        String colission = "";
        if (Wrapper.getBlockAtPosC(Wrapper.mc.thePlayer, 0.3100000023841858, 0.0, 0.3100000023841858).getMaterial().isLiquid()) {
            colission = "Positive";
        }
        if (Wrapper.getBlockAtPosC(Wrapper.mc.thePlayer, -0.3100000023841858, 0.0, -0.3100000023841858).getMaterial().isLiquid()) {
            colission = "Negative";
        }
        return colission;
    }

    public static boolean stairCollision() {
        boolean collision = false;
        if (Wrapper.getBlockAtPosC(Wrapper.mc.thePlayer, 0.3100000023841858, 0.0, 0.3100000023841858) instanceof BlockStairs || Wrapper.getBlockAtPosC(Wrapper.mc.thePlayer, -0.3100000023841858, 0.0, -0.3100000023841858) instanceof BlockStairs || Wrapper.getBlockAtPosC(Wrapper.mc.thePlayer, 0.3100000023841858, 0.0, -0.3100000023841858) instanceof BlockStairs || Wrapper.getBlockAtPosC(Wrapper.mc.thePlayer, -0.3100000023841858, 0.0, 0.3100000023841858) instanceof BlockStairs || Wrapper.getBlockatPosSpeed(Wrapper.mc.thePlayer, 1.05f, 1.05f) instanceof BlockStairs) {
            collision = true;
        }
        return collision;
    }

    public static boolean isInsideBlock() {
        int x2 = MathHelper.floor_double(Wrapper.mc.thePlayer.boundingBox.minX);
        while (x2 < MathHelper.floor_double(Wrapper.mc.thePlayer.boundingBox.maxX) + 1) {
            int y2 = MathHelper.floor_double(Wrapper.mc.thePlayer.boundingBox.minY);
            while (y2 < MathHelper.floor_double(Wrapper.mc.thePlayer.boundingBox.maxY) + 1) {
                int z2 = MathHelper.floor_double(Wrapper.mc.thePlayer.boundingBox.minZ);
                while (z2 < MathHelper.floor_double(Wrapper.mc.thePlayer.boundingBox.maxZ) + 1) {
                    AxisAlignedBB boundingBox;
                    Block block = Wrapper.mc.theWorld.getBlockState(new BlockPos(x2, y2, z2)).getBlock();
                    if (block != null && !(block instanceof BlockAir) && (boundingBox = block.getCollisionBoundingBox(Wrapper.mc.theWorld, new BlockPos(x2, y2, z2), Wrapper.mc.theWorld.getBlockState(new BlockPos(x2, y2, z2)))) != null && Wrapper.mc.thePlayer.boundingBox.intersectsWith(boundingBox)) {
                        return true;
                    }
                    ++z2;
                }
                ++y2;
            }
            ++x2;
        }
        return false;
    }

    public static boolean isBlockUnderPlayer(Material material, float height) {
        if (Wrapper.getBlockAtPosC(Wrapper.mc.thePlayer, 0.3100000023841858, height, 0.3100000023841858).getMaterial() == material && Wrapper.getBlockAtPosC(Wrapper.mc.thePlayer, -0.3100000023841858, height, -0.3100000023841858).getMaterial() == material && Wrapper.getBlockAtPosC(Wrapper.mc.thePlayer, -0.3100000023841858, height, 0.3100000023841858).getMaterial() == material && Wrapper.getBlockAtPosC(Wrapper.mc.thePlayer, 0.3100000023841858, height, -0.3100000023841858).getMaterial() == material) {
            return true;
        }
        return false;
    }

    public static boolean isBlockUnderPlayer2(float height) {
        if (Wrapper.getBlockAtPosC(Wrapper.mc.thePlayer, 0.3100000023841858, height, 0.3100000023841858) instanceof BlockStairs && Wrapper.getBlockAtPosC(Wrapper.mc.thePlayer, -0.3100000023841858, height, -0.3100000023841858) instanceof BlockStairs && Wrapper.getBlockAtPosC(Wrapper.mc.thePlayer, -0.3100000023841858, height, 0.3100000023841858) instanceof BlockStairs && Wrapper.getBlockAtPosC(Wrapper.mc.thePlayer, 0.3100000023841858, height, -0.3100000023841858) instanceof BlockStairs) {
            return false;
        }
        return true;
    }

    public static void blinkToPos(double[] startPos, BlockPos endPos, double slack) {
        double curX = startPos[0];
        double curY = startPos[1];
        double curZ = startPos[2];
        double endX = (double)endPos.getX() + 0.5;
        double endY = (double)endPos.getY() + 1.0;
        double endZ = (double)endPos.getZ() + 0.5;
        double distance = Math.abs(curX - endX) + Math.abs(curY - endY) + Math.abs(curZ - endZ);
        int count = 0;
        while (distance > slack) {
            double offset;
            distance = Math.abs(curX - endX) + Math.abs(curY - endY) + Math.abs(curZ - endZ);
            if (count > 120) break;
            boolean next = false;
            double diffX = curX - endX;
            double diffY = curY - endY;
            double diffZ = curZ - endZ;
            double d2 = offset = (count & 1) == 0 ? 0.4 : 0.1;
            if (diffX < 0.0) {
                curX = Math.abs(diffX) > offset ? (curX += offset) : (curX += Math.abs(diffX));
            }
            if (diffX > 0.0) {
                curX = Math.abs(diffX) > offset ? (curX -= offset) : (curX -= Math.abs(diffX));
            }
            if (diffY < 0.0) {
                curY = Math.abs(diffY) > 0.25 ? (curY += 0.25) : (curY += Math.abs(diffY));
            }
            if (diffY > 0.0) {
                curY = Math.abs(diffY) > 0.25 ? (curY -= 0.25) : (curY -= Math.abs(diffY));
            }
            if (diffZ < 0.0) {
                curZ = Math.abs(diffZ) > offset ? (curZ += offset) : (curZ += Math.abs(diffZ));
            }
            if (diffZ > 0.0) {
                curZ = Math.abs(diffZ) > offset ? (curZ -= offset) : (curZ -= Math.abs(diffZ));
            }
            Minecraft.getMinecraft().getNetHandler().addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(curX, curY, curZ, true));
            ++count;
        }
    }
}

