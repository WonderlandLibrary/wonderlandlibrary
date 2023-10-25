/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 */
package net.minecraft.client.renderer.chunk;

import com.google.common.collect.Lists;
import java.util.List;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.chunk.SetVisibility;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumWorldBlockLayer;

public class CompiledChunk {
    public static final CompiledChunk DUMMY = new CompiledChunk(){

        @Override
        public boolean isVisible(EnumFacing enumFacing, EnumFacing enumFacing2) {
            return false;
        }

        @Override
        protected void setLayerUsed(EnumWorldBlockLayer enumWorldBlockLayer) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void setLayerStarted(EnumWorldBlockLayer enumWorldBlockLayer) {
            throw new UnsupportedOperationException();
        }
    };
    private WorldRenderer.State state;
    private boolean empty = true;
    private final List<TileEntity> tileEntities;
    private final boolean[] layersStarted;
    private final boolean[] layersUsed = new boolean[EnumWorldBlockLayer.values().length];
    private SetVisibility setVisibility;

    public boolean isLayerStarted(EnumWorldBlockLayer enumWorldBlockLayer) {
        return this.layersStarted[enumWorldBlockLayer.ordinal()];
    }

    protected void setLayerUsed(EnumWorldBlockLayer enumWorldBlockLayer) {
        this.empty = false;
        this.layersUsed[enumWorldBlockLayer.ordinal()] = true;
    }

    public boolean isVisible(EnumFacing enumFacing, EnumFacing enumFacing2) {
        return this.setVisibility.isVisible(enumFacing, enumFacing2);
    }

    public WorldRenderer.State getState() {
        return this.state;
    }

    public void setState(WorldRenderer.State state) {
        this.state = state;
    }

    public boolean isLayerEmpty(EnumWorldBlockLayer enumWorldBlockLayer) {
        return !this.layersUsed[enumWorldBlockLayer.ordinal()];
    }

    public void setVisibility(SetVisibility setVisibility) {
        this.setVisibility = setVisibility;
    }

    public void addTileEntity(TileEntity tileEntity) {
        this.tileEntities.add(tileEntity);
    }

    public List<TileEntity> getTileEntities() {
        return this.tileEntities;
    }

    public boolean isEmpty() {
        return this.empty;
    }

    public CompiledChunk() {
        this.layersStarted = new boolean[EnumWorldBlockLayer.values().length];
        this.tileEntities = Lists.newArrayList();
        this.setVisibility = new SetVisibility();
    }

    public void setLayerStarted(EnumWorldBlockLayer enumWorldBlockLayer) {
        this.layersStarted[enumWorldBlockLayer.ordinal()] = true;
    }
}

