/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.world.gen.structure;

import java.util.Iterator;
import java.util.Map;
import net.minecraft.util.MathHelper;
import net.minecraft.world.gen.structure.MapGenStructure;
import net.minecraft.world.gen.structure.StructureMineshaftStart;
import net.minecraft.world.gen.structure.StructureStart;

public class MapGenMineshaft
extends MapGenStructure {
    private double field_82673_e = 0.004;

    public MapGenMineshaft() {
    }

    @Override
    public String getStructureName() {
        return "Mineshaft";
    }

    public MapGenMineshaft(Map<String, String> p_i2034_1_) {
        Iterator<Map.Entry<String, String>> iterator = p_i2034_1_.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, String> entry = iterator.next();
            if (!entry.getKey().equals("chance")) continue;
            this.field_82673_e = MathHelper.parseDoubleWithDefault(entry.getValue(), this.field_82673_e);
        }
    }

    @Override
    protected boolean canSpawnStructureAtCoords(int chunkX, int chunkZ) {
        if (!(this.rand.nextDouble() < this.field_82673_e)) return false;
        if (this.rand.nextInt(80) >= Math.max(Math.abs(chunkX), Math.abs(chunkZ))) return false;
        return true;
    }

    @Override
    protected StructureStart getStructureStart(int chunkX, int chunkZ) {
        return new StructureMineshaftStart(this.worldObj, this.rand, chunkX, chunkZ);
    }
}

