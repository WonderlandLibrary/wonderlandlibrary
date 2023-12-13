/*
 * Decompiled with CFR 0.150.
 */
package net.minecraft.world.gen.structure;

import java.util.Map;
import net.minecraft.util.MathHelper;
import net.minecraft.world.gen.structure.MapGenStructure;
import net.minecraft.world.gen.structure.StructureMineshaftStart;
import net.minecraft.world.gen.structure.StructureStart;

public class MapGenMineshaft
extends MapGenStructure {
    private double field_82673_e = 0.004;
    private static final String __OBFID = "CL_00000443";

    public MapGenMineshaft() {
    }

    @Override
    public String getStructureName() {
        return "Mineshaft";
    }

    public MapGenMineshaft(Map p_i2034_1_) {
        for (Map.Entry var3 : p_i2034_1_.entrySet()) {
            if (!((String)var3.getKey()).equals("chance")) continue;
            this.field_82673_e = MathHelper.parseDoubleWithDefault((String)var3.getValue(), this.field_82673_e);
        }
    }

    @Override
    protected boolean canSpawnStructureAtCoords(int p_75047_1_, int p_75047_2_) {
        return this.rand.nextDouble() < this.field_82673_e && this.rand.nextInt(80) < Math.max(Math.abs(p_75047_1_), Math.abs(p_75047_2_));
    }

    @Override
    protected StructureStart getStructureStart(int p_75049_1_, int p_75049_2_) {
        return new StructureMineshaftStart(this.worldObj, this.rand, p_75049_1_, p_75049_2_);
    }
}

