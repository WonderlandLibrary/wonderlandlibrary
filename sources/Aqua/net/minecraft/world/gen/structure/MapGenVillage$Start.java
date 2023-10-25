package net.minecraft.world.gen.structure;

import java.util.List;
import java.util.Random;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.StructureStart;
import net.minecraft.world.gen.structure.StructureVillagePieces;

public static class MapGenVillage.Start
extends StructureStart {
    private boolean hasMoreThanTwoComponents;

    public MapGenVillage.Start() {
    }

    public MapGenVillage.Start(World worldIn, Random rand, int x, int z, int size) {
        super(x, z);
        List list = StructureVillagePieces.getStructureVillageWeightedPieceList((Random)rand, (int)size);
        StructureVillagePieces.Start structurevillagepieces$start = new StructureVillagePieces.Start(worldIn.getWorldChunkManager(), 0, rand, (x << 4) + 2, (z << 4) + 2, list, size);
        this.components.add((Object)structurevillagepieces$start);
        structurevillagepieces$start.buildComponent((StructureComponent)structurevillagepieces$start, (List)this.components, rand);
        List list1 = structurevillagepieces$start.field_74930_j;
        List list2 = structurevillagepieces$start.field_74932_i;
        while (!list1.isEmpty() || !list2.isEmpty()) {
            if (list1.isEmpty()) {
                int i = rand.nextInt(list2.size());
                StructureComponent structurecomponent = (StructureComponent)list2.remove(i);
                structurecomponent.buildComponent((StructureComponent)structurevillagepieces$start, (List)this.components, rand);
                continue;
            }
            int j = rand.nextInt(list1.size());
            StructureComponent structurecomponent2 = (StructureComponent)list1.remove(j);
            structurecomponent2.buildComponent((StructureComponent)structurevillagepieces$start, (List)this.components, rand);
        }
        this.updateBoundingBox();
        int k = 0;
        for (StructureComponent structurecomponent1 : this.components) {
            if (structurecomponent1 instanceof StructureVillagePieces.Road) continue;
            ++k;
        }
        this.hasMoreThanTwoComponents = k > 2;
    }

    public boolean isSizeableStructure() {
        return this.hasMoreThanTwoComponents;
    }

    public void writeToNBT(NBTTagCompound tagCompound) {
        super.writeToNBT(tagCompound);
        tagCompound.setBoolean("Valid", this.hasMoreThanTwoComponents);
    }

    public void readFromNBT(NBTTagCompound tagCompound) {
        super.readFromNBT(tagCompound);
        this.hasMoreThanTwoComponents = tagCompound.getBoolean("Valid");
    }
}
