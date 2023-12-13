// 
// Decompiled by Procyon v0.5.36
// 

package com.elementars.eclient.module.render;

import net.minecraft.block.state.IBlockState;
import net.minecraft.block.BlockFenceGate;
import net.minecraft.block.BlockWall;
import net.minecraft.block.BlockFence;
import net.minecraft.block.BlockRailBase;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.material.Material;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import java.util.Iterator;
import java.util.Comparator;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;
import com.elementars.eclient.event.events.RenderEvent;
import net.minecraft.pathfinding.Path;
import net.minecraft.pathfinding.WalkNodeProcessor;
import java.util.Collection;
import java.util.Arrays;
import com.elementars.eclient.command.Command;
import net.minecraft.entity.EntityLiving;
import net.minecraft.world.IBlockAccess;
import net.minecraft.util.math.BlockPos;
import net.minecraft.pathfinding.NodeProcessor;
import net.minecraft.pathfinding.PathFinder;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.world.World;
import net.minecraft.entity.monster.EntityZombie;
import com.elementars.eclient.module.Category;
import net.minecraft.pathfinding.PathPoint;
import java.util.ArrayList;
import com.elementars.eclient.module.Module;

public class Pathfind extends Module
{
    public static ArrayList<PathPoint> points;
    static PathPoint to;
    
    public Pathfind() {
        super("Pathfind", "Finds a path lol", 0, Category.RENDER, true);
    }
    
    public static boolean createPath(final PathPoint end) {
        Pathfind.to = end;
        final WalkNodeProcessor walkNodeProcessor = new AnchoredWalkNodeProcessor(new PathPoint((int)Pathfind.mc.player.posX, (int)Pathfind.mc.player.posY, (int)Pathfind.mc.player.posZ));
        final EntityZombie zombie = new EntityZombie((World)Pathfind.mc.world);
        zombie.setPathPriority(PathNodeType.WATER, 16.0f);
        zombie.posX = Pathfind.mc.player.posX;
        zombie.posY = Pathfind.mc.player.posY;
        zombie.posZ = Pathfind.mc.player.posZ;
        final PathFinder finder = new PathFinder((NodeProcessor)walkNodeProcessor);
        final Path path = finder.findPath((IBlockAccess)Pathfind.mc.world, (EntityLiving)zombie, new BlockPos(end.x, end.y, end.z), Float.MAX_VALUE);
        zombie.setPathPriority(PathNodeType.WATER, 0.0f);
        if (path == null) {
            Command.sendChatMessage("Failed to create path!");
            return false;
        }
        Pathfind.points = new ArrayList<PathPoint>(Arrays.asList(path.points));
        return Pathfind.points.get(Pathfind.points.size() - 1).distanceTo(end) <= 1.0f;
    }
    
    @Override
    public void onWorldRender(final RenderEvent event) {
        if (Pathfind.points.isEmpty()) {
            return;
        }
        GL11.glDisable(3042);
        GL11.glDisable(3553);
        GL11.glDisable(2896);
        GL11.glLineWidth(1.5f);
        GL11.glColor3f(1.0f, 1.0f, 1.0f);
        GlStateManager.disableDepth();
        GL11.glBegin(1);
        final PathPoint first = Pathfind.points.get(0);
        GL11.glVertex3d(first.x - Pathfind.mc.getRenderManager().renderPosX + 0.5, first.y - Pathfind.mc.getRenderManager().renderPosY, first.z - Pathfind.mc.getRenderManager().renderPosZ + 0.5);
        for (int i = 0; i < Pathfind.points.size() - 1; ++i) {
            final PathPoint pathPoint = Pathfind.points.get(i);
            GL11.glVertex3d(pathPoint.x - Pathfind.mc.getRenderManager().renderPosX + 0.5, pathPoint.y - Pathfind.mc.getRenderManager().renderPosY, pathPoint.z - Pathfind.mc.getRenderManager().renderPosZ + 0.5);
            if (i != Pathfind.points.size() - 1) {
                GL11.glVertex3d(pathPoint.x - Pathfind.mc.getRenderManager().renderPosX + 0.5, pathPoint.y - Pathfind.mc.getRenderManager().renderPosY, pathPoint.z - Pathfind.mc.getRenderManager().renderPosZ + 0.5);
            }
        }
        GL11.glEnd();
        GlStateManager.enableDepth();
    }
    
    @Override
    public void onUpdate() {
        final PathPoint closest = Pathfind.points.stream().min(Comparator.comparing(pathPoint -> Pathfind.mc.player.getDistance((double)pathPoint.x, (double)pathPoint.y, (double)pathPoint.z))).orElse(null);
        if (closest == null) {
            return;
        }
        if (Pathfind.mc.player.getDistance((double)closest.x, (double)closest.y, (double)closest.z) > 0.8) {
            return;
        }
        final Iterator<PathPoint> iterator = Pathfind.points.iterator();
        while (iterator.hasNext()) {
            if (iterator.next() == closest) {
                iterator.remove();
                break;
            }
            iterator.remove();
        }
        if (Pathfind.points.size() <= 1 && Pathfind.to != null) {
            final boolean b = createPath(Pathfind.to);
            final boolean flag = Pathfind.points.size() <= 4;
            if ((b && flag) || flag) {
                Pathfind.points.clear();
                Pathfind.to = null;
                if (b) {
                    Command.sendChatMessage("Arrived!");
                }
                else {
                    Command.sendChatMessage("Can't go on: pathfinder has hit dead end");
                }
            }
        }
    }
    
    static {
        Pathfind.points = new ArrayList<PathPoint>();
        Pathfind.to = null;
    }
    
    private static class AnchoredWalkNodeProcessor extends WalkNodeProcessor
    {
        PathPoint from;
        
        public AnchoredWalkNodeProcessor(final PathPoint from) {
            this.from = from;
        }
        
        public PathPoint getStart() {
            return this.from;
        }
        
        public boolean getCanEnterDoors() {
            return true;
        }
        
        public boolean getCanSwim() {
            return true;
        }
        
        public PathNodeType getPathNodeType(final IBlockAccess blockaccessIn, final int x, final int y, final int z) {
            PathNodeType pathnodetype = this.getPathNodeTypeRaw(blockaccessIn, x, y, z);
            if (pathnodetype == PathNodeType.OPEN && y >= 1) {
                final Block block = blockaccessIn.getBlockState(new BlockPos(x, y - 1, z)).getBlock();
                final PathNodeType pathnodetype2 = this.getPathNodeTypeRaw(blockaccessIn, x, y - 1, z);
                pathnodetype = ((pathnodetype2 != PathNodeType.WALKABLE && pathnodetype2 != PathNodeType.OPEN && pathnodetype2 != PathNodeType.LAVA) ? PathNodeType.WALKABLE : PathNodeType.OPEN);
                if (pathnodetype2 == PathNodeType.DAMAGE_FIRE || block == Blocks.MAGMA) {
                    pathnodetype = PathNodeType.DAMAGE_FIRE;
                }
                if (pathnodetype2 == PathNodeType.DAMAGE_CACTUS) {
                    pathnodetype = PathNodeType.DAMAGE_CACTUS;
                }
            }
            pathnodetype = this.checkNeighborBlocks(blockaccessIn, x, y, z, pathnodetype);
            return pathnodetype;
        }
        
        protected PathNodeType getPathNodeTypeRaw(final IBlockAccess p_189553_1_, final int p_189553_2_, final int p_189553_3_, final int p_189553_4_) {
            final BlockPos blockpos = new BlockPos(p_189553_2_, p_189553_3_, p_189553_4_);
            final IBlockState iblockstate = p_189553_1_.getBlockState(blockpos);
            final Block block = iblockstate.getBlock();
            final Material material = iblockstate.getMaterial();
            final PathNodeType type = block.getAiPathNodeType(iblockstate, p_189553_1_, blockpos);
            if (type != null) {
                return type;
            }
            if (material == Material.AIR) {
                return PathNodeType.OPEN;
            }
            if (block == Blocks.TRAPDOOR || block == Blocks.IRON_TRAPDOOR || block == Blocks.WATERLILY) {
                return PathNodeType.TRAPDOOR;
            }
            if (block == Blocks.FIRE) {
                return PathNodeType.DAMAGE_FIRE;
            }
            if (block == Blocks.CACTUS) {
                return PathNodeType.DAMAGE_CACTUS;
            }
            if (block instanceof BlockDoor && material == Material.WOOD && !(boolean)iblockstate.getValue((IProperty)BlockDoor.OPEN)) {
                return PathNodeType.DOOR_WOOD_CLOSED;
            }
            if (block instanceof BlockDoor && material == Material.IRON && !(boolean)iblockstate.getValue((IProperty)BlockDoor.OPEN)) {
                return PathNodeType.DOOR_IRON_CLOSED;
            }
            if (block instanceof BlockDoor && (boolean)iblockstate.getValue((IProperty)BlockDoor.OPEN)) {
                return PathNodeType.DOOR_OPEN;
            }
            if (block instanceof BlockRailBase) {
                return PathNodeType.RAIL;
            }
            if (block instanceof BlockFence || block instanceof BlockWall || (block instanceof BlockFenceGate && !(boolean)iblockstate.getValue((IProperty)BlockFenceGate.OPEN))) {
                return PathNodeType.FENCE;
            }
            if (material == Material.WATER) {
                return PathNodeType.WALKABLE;
            }
            if (material == Material.LAVA) {
                return PathNodeType.LAVA;
            }
            return block.isPassable(p_189553_1_, blockpos) ? PathNodeType.OPEN : PathNodeType.BLOCKED;
        }
    }
}
