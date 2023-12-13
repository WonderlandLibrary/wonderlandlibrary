// 
// Decompiled by Procyon v0.6.0
// 

package net.augustus.utils.skid.rise6;

import java.util.List;
import java.util.Comparator;
import java.util.ArrayList;
import net.minecraft.util.Vec3;
import net.minecraft.block.BlockLiquid;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.world.World;
import net.minecraft.util.MathHelper;
import net.minecraft.block.BlockAir;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.entity.EntityLivingBase;
import net.augustus.utils.MoveUtil;
import net.minecraft.entity.Entity;
import net.minecraft.util.BlockPos;
import net.minecraft.block.Block;
import java.util.HashMap;
import net.augustus.utils.interfaces.MC;

public class PlayerUtil implements MC
{
    private final HashMap<Integer, Integer> GOOD_POTIONS;
    
    public PlayerUtil() {
        this.GOOD_POTIONS = new HashMap<Integer, Integer>() {
            {
                this.put(6, 1);
                this.put(10, 2);
                this.put(11, 3);
                this.put(21, 4);
                this.put(22, 5);
                this.put(23, 6);
                this.put(5, 7);
                this.put(1, 8);
                this.put(12, 9);
                this.put(14, 10);
                this.put(3, 11);
                this.put(13, 12);
            }
        };
    }
    
    public static Block block(final double x, final double y, final double z) {
        return PlayerUtil.mc.theWorld.getBlockState(new BlockPos(x, y, z)).getBlock();
    }
    
    public static Block block(final BlockPos blockPos) {
        return PlayerUtil.mc.theWorld.getBlockState(blockPos).getBlock();
    }
    
    public double distance(final BlockPos pos1, final BlockPos pos2) {
        final double x = pos1.getX() - pos2.getX();
        final double y = pos1.getY() - pos2.getY();
        final double z = pos1.getZ() - pos2.getZ();
        return x * x + y * y + z * z;
    }
    
    public static Block blockRelativeToPlayer(final double offsetX, final double offsetY, final double offsetZ) {
        return PlayerUtil.mc.theWorld.getBlockState(new BlockPos(PlayerUtil.mc.thePlayer).add(offsetX, offsetY, offsetZ)).getBlock();
    }
    
    public Block blockAheadOfPlayer(final double offsetXZ, final double offsetY) {
        return blockRelativeToPlayer(-Math.sin(MoveUtil.direction()) * offsetXZ, offsetY, Math.cos(MoveUtil.direction()) * offsetXZ);
    }
    
    public boolean sameTeam(final EntityLivingBase player) {
        if (player.getTeam() != null && PlayerUtil.mc.thePlayer.getTeam() != null) {
            final char c1 = player.getDisplayName().getFormattedText().charAt(1);
            final char c2 = PlayerUtil.mc.thePlayer.getDisplayName().getFormattedText().charAt(1);
            return c1 == c2;
        }
        return false;
    }
    
    public boolean isBlockUnder(final double height) {
        return this.isBlockUnder(height, true);
    }
    
    public boolean isBlockUnder(final double height, final boolean boundingBox) {
        if (boundingBox) {
            for (int offset = 0; offset < height; offset += 2) {
                final AxisAlignedBB bb = PlayerUtil.mc.thePlayer.getEntityBoundingBox().offset(0.0, -offset, 0.0);
                if (!PlayerUtil.mc.theWorld.getCollidingBoundingBoxes(PlayerUtil.mc.thePlayer, bb).isEmpty()) {
                    return true;
                }
            }
        }
        else {
            for (int offset = 0; offset < height; ++offset) {
                if (blockRelativeToPlayer(0.0, -offset, 0.0).isFullBlock()) {
                    return true;
                }
            }
        }
        return false;
    }
    
    public boolean isBlockUnder() {
        return this.isBlockUnder(PlayerUtil.mc.thePlayer.posY + PlayerUtil.mc.thePlayer.getEyeHeight());
    }
    
    public boolean goodPotion(final int id) {
        return this.GOOD_POTIONS.containsKey(id);
    }
    
    public int potionRanking(final int id) {
        return this.GOOD_POTIONS.getOrDefault(id, -1);
    }
    
    public boolean inLiquid() {
        return PlayerUtil.mc.thePlayer.isInWater() || PlayerUtil.mc.thePlayer.isInLava();
    }
    
    public boolean blockNear(final int range) {
        for (int x = -range; x <= range; ++x) {
            for (int y = -range; y <= range; ++y) {
                for (int z = -range; z <= range; ++z) {
                    final Block block = blockRelativeToPlayer(x, y, z);
                    if (!(block instanceof BlockAir)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
    public boolean insideBlock() {
        if (PlayerUtil.mc.thePlayer.ticksExisted < 5) {
            return false;
        }
        final EntityPlayerSP player = PlayerUtil.mc.thePlayer;
        final WorldClient world = PlayerUtil.mc.theWorld;
        final AxisAlignedBB bb = player.getEntityBoundingBox();
        for (int x = MathHelper.floor_double(bb.minX); x < MathHelper.floor_double(bb.maxX) + 1; ++x) {
            for (int y = MathHelper.floor_double(bb.minY); y < MathHelper.floor_double(bb.maxY) + 1; ++y) {
                for (int z = MathHelper.floor_double(bb.minZ); z < MathHelper.floor_double(bb.maxZ) + 1; ++z) {
                    final Block block = world.getBlockState(new BlockPos(x, y, z)).getBlock();
                    final AxisAlignedBB boundingBox;
                    if (block != null && !(block instanceof BlockAir) && (boundingBox = block.getCollisionBoundingBox(world, new BlockPos(x, y, z), world.getBlockState(new BlockPos(x, y, z)))) != null && player.getEntityBoundingBox().intersectsWith(boundingBox)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
    public void sendClick(final int button, final boolean state) {
        final int keyBind = (button == 0) ? PlayerUtil.mc.gameSettings.keyBindAttack.getKeyCode() : PlayerUtil.mc.gameSettings.keyBindUseItem.getKeyCode();
        KeyBinding.setKeyBindState(keyBind, state);
        if (state) {
            KeyBinding.onTick(keyBind);
        }
    }
    
    public static boolean onLiquid() {
        boolean onLiquid = false;
        final AxisAlignedBB playerBB = PlayerUtil.mc.thePlayer.getEntityBoundingBox();
        final WorldClient world = PlayerUtil.mc.theWorld;
        final int y = (int)playerBB.offset(0.0, -0.01, 0.0).minY;
        for (int x = MathHelper.floor_double(playerBB.minX); x < MathHelper.floor_double(playerBB.maxX) + 1; ++x) {
            for (int z = MathHelper.floor_double(playerBB.minZ); z < MathHelper.floor_double(playerBB.maxZ) + 1; ++z) {
                final Block block = world.getBlockState(new BlockPos(x, y, z)).getBlock();
                if (block != null && !(block instanceof BlockAir)) {
                    if (!(block instanceof BlockLiquid)) {
                        return false;
                    }
                    onLiquid = true;
                }
            }
        }
        return onLiquid;
    }
    
    public Vec3 getPlacePossibility(final double offsetX, final double offsetZ) {
        final List<Vec3> possibilities = new ArrayList<Vec3>();
        for (int range = (int)(5.0 + (Math.abs(offsetX) + Math.abs(offsetZ))), x = -range; x <= range; ++x) {
            for (int y = -range; y <= range; ++y) {
                for (int z = -range; z <= range; ++z) {
                    final Block block = blockRelativeToPlayer(x, y, z);
                    if (!(block instanceof BlockAir)) {
                        for (int x2 = -1; x2 <= 1; x2 += 2) {
                            possibilities.add(new Vec3(PlayerUtil.mc.thePlayer.posX + x + x2, PlayerUtil.mc.thePlayer.posY + y, PlayerUtil.mc.thePlayer.posZ + z));
                        }
                        for (int y2 = -1; y2 <= 1; y2 += 2) {
                            possibilities.add(new Vec3(PlayerUtil.mc.thePlayer.posX + x, PlayerUtil.mc.thePlayer.posY + y + y2, PlayerUtil.mc.thePlayer.posZ + z));
                        }
                        for (int z2 = -1; z2 <= 1; z2 += 2) {
                            possibilities.add(new Vec3(PlayerUtil.mc.thePlayer.posX + x, PlayerUtil.mc.thePlayer.posY + y, PlayerUtil.mc.thePlayer.posZ + z + z2));
                        }
                    }
                }
            }
        }
        possibilities.removeIf(vec3 -> PlayerUtil.mc.thePlayer.getDistance(vec3.xCoord, vec3.yCoord, vec3.zCoord) > 5.0 || !(block(vec3.xCoord, vec3.yCoord, vec3.zCoord) instanceof BlockAir) || vec3.yCoord > PlayerUtil.mc.thePlayer.posY);
        if (possibilities.isEmpty()) {
            return null;
        }
        possibilities.sort(Comparator.comparingDouble(vec3 -> {
            final double d0 = PlayerUtil.mc.thePlayer.posX + offsetX - vec3.xCoord;
            final double d2 = PlayerUtil.mc.thePlayer.posY - 1.0 - vec3.yCoord;
            final double d3 = PlayerUtil.mc.thePlayer.posZ + offsetZ - vec3.zCoord;
            return (double)MathHelper.sqrt_double(d0 * d0 + d2 * d2 + d3 * d3);
        }));
        return possibilities.get(0);
    }
    
    public Vec3 getPlacePossibility() {
        return this.getPlacePossibility(0.0, 0.0);
    }
}
