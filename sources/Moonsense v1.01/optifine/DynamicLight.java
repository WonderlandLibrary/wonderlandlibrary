// 
// Decompiled by Procyon v0.5.36
// 

package optifine;

import java.util.Iterator;
import net.minecraft.client.renderer.chunk.CompiledChunk;
import net.minecraft.client.renderer.chunk.RenderChunk;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.util.EnumFacing;
import net.minecraft.init.Blocks;
import net.minecraft.util.MathHelper;
import net.minecraft.client.renderer.RenderGlobal;
import java.util.HashSet;
import net.minecraft.util.BlockPos;
import java.util.Set;
import net.minecraft.entity.Entity;

public class DynamicLight
{
    private Entity entity;
    private double offsetY;
    private double lastPosX;
    private double lastPosY;
    private double lastPosZ;
    private int lastLightLevel;
    private boolean underwater;
    private long timeCheckMs;
    private Set<BlockPos> setLitChunkPos;
    private BlockPosM blockPosMutable;
    
    public DynamicLight(final Entity entity) {
        this.entity = null;
        this.offsetY = 0.0;
        this.lastPosX = -2.147483648E9;
        this.lastPosY = -2.147483648E9;
        this.lastPosZ = -2.147483648E9;
        this.lastLightLevel = 0;
        this.underwater = false;
        this.timeCheckMs = 0L;
        this.setLitChunkPos = new HashSet<BlockPos>();
        this.blockPosMutable = new BlockPosM(0, 0, 0);
        this.entity = entity;
        this.offsetY = entity.getEyeHeight();
    }
    
    public void update(final RenderGlobal renderGlobal) {
        if (Config.isDynamicLightsFast()) {
            final long posX = System.currentTimeMillis();
            if (posX < this.timeCheckMs + 500L) {
                return;
            }
            this.timeCheckMs = posX;
        }
        final double posX2 = this.entity.posX - 0.5;
        final double posY = this.entity.posY - 0.5 + this.offsetY;
        final double posZ = this.entity.posZ - 0.5;
        final int lightLevel = DynamicLights.getLightLevel(this.entity);
        final double dx = posX2 - this.lastPosX;
        final double dy = posY - this.lastPosY;
        final double dz = posZ - this.lastPosZ;
        final double delta = 0.1;
        if (Math.abs(dx) > delta || Math.abs(dy) > delta || Math.abs(dz) > delta || this.lastLightLevel != lightLevel) {
            this.lastPosX = posX2;
            this.lastPosY = posY;
            this.lastPosZ = posZ;
            this.lastLightLevel = lightLevel;
            this.underwater = false;
            final WorldClient world = renderGlobal.getWorld();
            if (world != null) {
                this.blockPosMutable.setXyz(MathHelper.floor_double(posX2), MathHelper.floor_double(posY), MathHelper.floor_double(posZ));
                final IBlockState setNewPos = world.getBlockState(this.blockPosMutable);
                final Block dirX = setNewPos.getBlock();
                this.underwater = (dirX == Blocks.water);
            }
            final HashSet setNewPos2 = new HashSet();
            if (lightLevel > 0) {
                final EnumFacing dirX2 = ((MathHelper.floor_double(posX2) & 0xF) >= 8) ? EnumFacing.EAST : EnumFacing.WEST;
                final EnumFacing dirY = ((MathHelper.floor_double(posY) & 0xF) >= 8) ? EnumFacing.UP : EnumFacing.DOWN;
                final EnumFacing dirZ = ((MathHelper.floor_double(posZ) & 0xF) >= 8) ? EnumFacing.SOUTH : EnumFacing.NORTH;
                final BlockPos pos = new BlockPos(posX2, posY, posZ);
                final RenderChunk chunkView = renderGlobal.getRenderChunk(pos);
                final RenderChunk chunkX = renderGlobal.getRenderChunk(chunkView, dirX2);
                final RenderChunk chunkZ = renderGlobal.getRenderChunk(chunkView, dirZ);
                final RenderChunk chunkXZ = renderGlobal.getRenderChunk(chunkX, dirZ);
                final RenderChunk chunkY = renderGlobal.getRenderChunk(chunkView, dirY);
                final RenderChunk chunkYX = renderGlobal.getRenderChunk(chunkY, dirX2);
                final RenderChunk chunkYZ = renderGlobal.getRenderChunk(chunkY, dirZ);
                final RenderChunk chunkYXZ = renderGlobal.getRenderChunk(chunkYX, dirZ);
                this.updateChunkLight(chunkView, this.setLitChunkPos, setNewPos2);
                this.updateChunkLight(chunkX, this.setLitChunkPos, setNewPos2);
                this.updateChunkLight(chunkZ, this.setLitChunkPos, setNewPos2);
                this.updateChunkLight(chunkXZ, this.setLitChunkPos, setNewPos2);
                this.updateChunkLight(chunkY, this.setLitChunkPos, setNewPos2);
                this.updateChunkLight(chunkYX, this.setLitChunkPos, setNewPos2);
                this.updateChunkLight(chunkYZ, this.setLitChunkPos, setNewPos2);
                this.updateChunkLight(chunkYXZ, this.setLitChunkPos, setNewPos2);
            }
            this.updateLitChunks(renderGlobal);
            this.setLitChunkPos = (Set<BlockPos>)setNewPos2;
        }
    }
    
    private void updateChunkLight(final RenderChunk renderChunk, final Set<BlockPos> setPrevPos, final Set<BlockPos> setNewPos) {
        if (renderChunk != null) {
            final CompiledChunk compiledChunk = renderChunk.func_178571_g();
            if (compiledChunk != null && !compiledChunk.func_178489_a()) {
                renderChunk.func_178575_a(true);
            }
            final BlockPos pos = renderChunk.func_178568_j();
            if (setPrevPos != null) {
                setPrevPos.remove(pos);
            }
            if (setNewPos != null) {
                setNewPos.add(pos);
            }
        }
    }
    
    public void updateLitChunks(final RenderGlobal renderGlobal) {
        for (final BlockPos posOld : this.setLitChunkPos) {
            final RenderChunk chunkOld = renderGlobal.getRenderChunk(posOld);
            this.updateChunkLight(chunkOld, null, null);
        }
    }
    
    public Entity getEntity() {
        return this.entity;
    }
    
    public double getLastPosX() {
        return this.lastPosX;
    }
    
    public double getLastPosY() {
        return this.lastPosY;
    }
    
    public double getLastPosZ() {
        return this.lastPosZ;
    }
    
    public int getLastLightLevel() {
        return this.lastLightLevel;
    }
    
    public boolean isUnderwater() {
        return this.underwater;
    }
    
    public double getOffsetY() {
        return this.offsetY;
    }
    
    @Override
    public String toString() {
        return "Entity: " + this.entity + ", offsetY: " + this.offsetY;
    }
}
