package net.minecraft.world.gen.structure;

import java.util.List;
import java.util.Random;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.StructureStart;
import net.minecraft.world.gen.structure.StructureStrongholdPieces;

public static class MapGenStronghold.Start
extends StructureStart {
    public MapGenStronghold.Start() {
    }

    public MapGenStronghold.Start(World worldIn, Random p_i2067_2_, int p_i2067_3_, int p_i2067_4_) {
        super(p_i2067_3_, p_i2067_4_);
        StructureStrongholdPieces.prepareStructurePieces();
        StructureStrongholdPieces.Stairs2 structurestrongholdpieces$stairs2 = new StructureStrongholdPieces.Stairs2(0, p_i2067_2_, (p_i2067_3_ << 4) + 2, (p_i2067_4_ << 4) + 2);
        this.components.add((Object)structurestrongholdpieces$stairs2);
        structurestrongholdpieces$stairs2.buildComponent((StructureComponent)structurestrongholdpieces$stairs2, (List)this.components, p_i2067_2_);
        List list = structurestrongholdpieces$stairs2.field_75026_c;
        while (!list.isEmpty()) {
            int i = p_i2067_2_.nextInt(list.size());
            StructureComponent structurecomponent = (StructureComponent)list.remove(i);
            structurecomponent.buildComponent((StructureComponent)structurestrongholdpieces$stairs2, (List)this.components, p_i2067_2_);
        }
        this.updateBoundingBox();
        this.markAvailableHeight(worldIn, p_i2067_2_, 10);
    }
}
