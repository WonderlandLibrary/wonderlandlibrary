/*
 * Decompiled with CFR 0.152.
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

    @Override
    protected boolean canSpawnStructureAtCoords(int n, int n2) {
        return this.rand.nextDouble() < this.field_82673_e && this.rand.nextInt(80) < Math.max(Math.abs(n), Math.abs(n2));
    }

    public MapGenMineshaft() {
    }

    public MapGenMineshaft(Map<String, String> map) {
        for (Map.Entry<String, String> entry : map.entrySet()) {
            if (!entry.getKey().equals("chance")) continue;
            this.field_82673_e = MathHelper.parseDoubleWithDefault(entry.getValue(), this.field_82673_e);
        }
    }

    @Override
    public String getStructureName() {
        return "Mineshaft";
    }

    @Override
    protected StructureStart getStructureStart(int n, int n2) {
        return new StructureMineshaftStart(this.worldObj, this.rand, n, n2);
    }
}

