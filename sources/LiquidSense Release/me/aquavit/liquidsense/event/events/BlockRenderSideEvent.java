package me.aquavit.liquidsense.event.events;

import me.aquavit.liquidsense.event.CancellableEvent;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;

public class BlockRenderSideEvent extends CancellableEvent {
    private boolean isToRender;
    private IBlockState state;
    private IBlockAccess world;
    private BlockPos pos;
    private EnumFacing side;
    private double maxX;
    private double minX;
    private double maxY;
    private double minY;
    private double maxZ;
    private double minZ;

    public boolean isToRender() {
        return this.isToRender;
    }

    public void setToRender(boolean isToRender) {
        this.isToRender = isToRender;
    }

    public IBlockState getState() {
        return this.state;
    }

    public void setState(IBlockState state) {
        this.state = state;
    }

    public IBlockAccess getWorld() {
        return this.world;
    }

    public void setWorld(IBlockAccess world) {
        this.world = world;
    }

    public BlockPos getPos() {
        return this.pos;
    }

    public void setPos(BlockPos pos) {
        this.pos = pos;
    }

    public EnumFacing getSide() {
        return this.side;
    }

    public void setSide(EnumFacing side) {
        this.side = side;
    }

    public double getMaxX() {
        return this.maxX;
    }

    public void setMaxX(double maxX) {
        this.maxX = maxX;
    }

    public double getMinX() {
        return this.minX;
    }

    public void setMinX(double minX) {
        this.minX = minX;
    }

    public double getMaxY() {
        return this.maxY;
    }

    public void setMaxY(double maxY) {
        this.maxY = maxY;
    }

    public double getMinY() {
        return this.minY;
    }

    public void setMinY(double minY) {
        this.minY = minY;
    }

    public double getMaxZ() {
        return this.maxZ;
    }

    public void setMaxZ(double maxZ) {
        this.maxZ = maxZ;
    }

    public double getMinZ() {
        return this.minZ;
    }

    public void setMinZ(double minZ) {
        this.minZ = minZ;
    }

    public BlockRenderSideEvent(IBlockAccess world, BlockPos pos, EnumFacing side, double maxX, double minX, double maxY, double minY, double maxZ, double minZ) {
        this.world = world;
        this.pos = pos;
        this.side = side;
        this.maxX = maxX;
        this.minX = minX;
        this.maxY = maxY;
        this.minY = minY;
        this.maxZ = maxZ;
        this.minZ = minZ;
        this.isToRender = true;
        this.state = Minecraft.getMinecraft().theWorld != null ? Minecraft.getMinecraft().theWorld.getBlockState(this.pos) : null;
    }
}

