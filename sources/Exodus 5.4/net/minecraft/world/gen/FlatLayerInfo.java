/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.world.gen;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.ResourceLocation;

public class FlatLayerInfo {
    private IBlockState field_175901_b;
    private int layerCount = 1;
    private int layerMinimumY;
    private final int field_175902_a;

    private int getFillBlockMeta() {
        return this.field_175901_b.getBlock().getMetaFromState(this.field_175901_b);
    }

    public int getLayerCount() {
        return this.layerCount;
    }

    private Block func_151536_b() {
        return this.field_175901_b.getBlock();
    }

    public FlatLayerInfo(int n, Block block) {
        this(3, n, block);
    }

    public FlatLayerInfo(int n, int n2, Block block) {
        this.field_175902_a = n;
        this.layerCount = n2;
        this.field_175901_b = block.getDefaultState();
    }

    public void setMinY(int n) {
        this.layerMinimumY = n;
    }

    public int getMinY() {
        return this.layerMinimumY;
    }

    public String toString() {
        int n;
        String string;
        if (this.field_175902_a >= 3) {
            ResourceLocation resourceLocation = (ResourceLocation)Block.blockRegistry.getNameForObject(this.func_151536_b());
            String string2 = string = resourceLocation == null ? "null" : resourceLocation.toString();
            if (this.layerCount > 1) {
                string = String.valueOf(this.layerCount) + "*" + string;
            }
        } else {
            string = Integer.toString(Block.getIdFromBlock(this.func_151536_b()));
            if (this.layerCount > 1) {
                string = String.valueOf(this.layerCount) + "x" + string;
            }
        }
        if ((n = this.getFillBlockMeta()) > 0) {
            string = String.valueOf(string) + ":" + n;
        }
        return string;
    }

    public IBlockState func_175900_c() {
        return this.field_175901_b;
    }

    public FlatLayerInfo(int n, int n2, Block block, int n3) {
        this(n, n2, block);
        this.field_175901_b = block.getStateFromMeta(n3);
    }
}

