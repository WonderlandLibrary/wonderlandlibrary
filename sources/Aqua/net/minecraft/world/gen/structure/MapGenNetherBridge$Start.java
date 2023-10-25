package net.minecraft.world.gen.structure;

import java.util.List;
import java.util.Random;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.StructureNetherBridgePieces;
import net.minecraft.world.gen.structure.StructureStart;

public static class MapGenNetherBridge.Start
extends StructureStart {
    public MapGenNetherBridge.Start() {
    }

    public MapGenNetherBridge.Start(World worldIn, Random p_i2040_2_, int p_i2040_3_, int p_i2040_4_) {
        super(p_i2040_3_, p_i2040_4_);
        StructureNetherBridgePieces.Start structurenetherbridgepieces$start = new StructureNetherBridgePieces.Start(p_i2040_2_, (p_i2040_3_ << 4) + 2, (p_i2040_4_ << 4) + 2);
        this.components.add((Object)structurenetherbridgepieces$start);
        structurenetherbridgepieces$start.buildComponent((StructureComponent)structurenetherbridgepieces$start, (List)this.components, p_i2040_2_);
        List list = structurenetherbridgepieces$start.field_74967_d;
        while (!list.isEmpty()) {
            int i = p_i2040_2_.nextInt(list.size());
            StructureComponent structurecomponent = (StructureComponent)list.remove(i);
            structurecomponent.buildComponent((StructureComponent)structurenetherbridgepieces$start, (List)this.components, p_i2040_2_);
        }
        this.updateBoundingBox();
        this.setRandomHeight(worldIn, p_i2040_2_, 48, 70);
    }
}
