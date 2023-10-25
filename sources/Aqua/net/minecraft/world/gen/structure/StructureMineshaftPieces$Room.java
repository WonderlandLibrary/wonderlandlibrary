package net.minecraft.world.gen.structure;

import com.google.common.collect.Lists;
import java.util.List;
import java.util.Random;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.StructureMineshaftPieces;

/*
 * Exception performing whole class analysis ignored.
 */
public static class StructureMineshaftPieces.Room
extends StructureComponent {
    private List<StructureBoundingBox> roomsLinkedToTheRoom = Lists.newLinkedList();

    public StructureMineshaftPieces.Room() {
    }

    public StructureMineshaftPieces.Room(int type, Random rand, int x, int z) {
        super(type);
        this.boundingBox = new StructureBoundingBox(x, 50, z, x + 7 + rand.nextInt(6), 54 + rand.nextInt(6), z + 7 + rand.nextInt(6));
    }

    public void buildComponent(StructureComponent componentIn, List<StructureComponent> listIn, Random rand) {
        int k;
        int i = this.getComponentType();
        int j = this.boundingBox.getYSize() - 3 - 1;
        if (j <= 0) {
            j = 1;
        }
        for (k = 0; k < this.boundingBox.getXSize() && (k += rand.nextInt(this.boundingBox.getXSize())) + 3 <= this.boundingBox.getXSize(); k += 4) {
            StructureComponent structurecomponent = StructureMineshaftPieces.access$000((StructureComponent)componentIn, listIn, (Random)rand, (int)(this.boundingBox.minX + k), (int)(this.boundingBox.minY + rand.nextInt(j) + 1), (int)(this.boundingBox.minZ - 1), (EnumFacing)EnumFacing.NORTH, (int)i);
            if (structurecomponent == null) continue;
            StructureBoundingBox structureboundingbox = structurecomponent.getBoundingBox();
            this.roomsLinkedToTheRoom.add((Object)new StructureBoundingBox(structureboundingbox.minX, structureboundingbox.minY, this.boundingBox.minZ, structureboundingbox.maxX, structureboundingbox.maxY, this.boundingBox.minZ + 1));
        }
        for (k = 0; k < this.boundingBox.getXSize() && (k += rand.nextInt(this.boundingBox.getXSize())) + 3 <= this.boundingBox.getXSize(); k += 4) {
            StructureComponent structurecomponent1 = StructureMineshaftPieces.access$000((StructureComponent)componentIn, listIn, (Random)rand, (int)(this.boundingBox.minX + k), (int)(this.boundingBox.minY + rand.nextInt(j) + 1), (int)(this.boundingBox.maxZ + 1), (EnumFacing)EnumFacing.SOUTH, (int)i);
            if (structurecomponent1 == null) continue;
            StructureBoundingBox structureboundingbox1 = structurecomponent1.getBoundingBox();
            this.roomsLinkedToTheRoom.add((Object)new StructureBoundingBox(structureboundingbox1.minX, structureboundingbox1.minY, this.boundingBox.maxZ - 1, structureboundingbox1.maxX, structureboundingbox1.maxY, this.boundingBox.maxZ));
        }
        for (k = 0; k < this.boundingBox.getZSize() && (k += rand.nextInt(this.boundingBox.getZSize())) + 3 <= this.boundingBox.getZSize(); k += 4) {
            StructureComponent structurecomponent2 = StructureMineshaftPieces.access$000((StructureComponent)componentIn, listIn, (Random)rand, (int)(this.boundingBox.minX - 1), (int)(this.boundingBox.minY + rand.nextInt(j) + 1), (int)(this.boundingBox.minZ + k), (EnumFacing)EnumFacing.WEST, (int)i);
            if (structurecomponent2 == null) continue;
            StructureBoundingBox structureboundingbox2 = structurecomponent2.getBoundingBox();
            this.roomsLinkedToTheRoom.add((Object)new StructureBoundingBox(this.boundingBox.minX, structureboundingbox2.minY, structureboundingbox2.minZ, this.boundingBox.minX + 1, structureboundingbox2.maxY, structureboundingbox2.maxZ));
        }
        for (k = 0; k < this.boundingBox.getZSize() && (k += rand.nextInt(this.boundingBox.getZSize())) + 3 <= this.boundingBox.getZSize(); k += 4) {
            StructureComponent structurecomponent3 = StructureMineshaftPieces.access$000((StructureComponent)componentIn, listIn, (Random)rand, (int)(this.boundingBox.maxX + 1), (int)(this.boundingBox.minY + rand.nextInt(j) + 1), (int)(this.boundingBox.minZ + k), (EnumFacing)EnumFacing.EAST, (int)i);
            if (structurecomponent3 == null) continue;
            StructureBoundingBox structureboundingbox3 = structurecomponent3.getBoundingBox();
            this.roomsLinkedToTheRoom.add((Object)new StructureBoundingBox(this.boundingBox.maxX - 1, structureboundingbox3.minY, structureboundingbox3.minZ, this.boundingBox.maxX, structureboundingbox3.maxY, structureboundingbox3.maxZ));
        }
    }

    public boolean addComponentParts(World worldIn, Random randomIn, StructureBoundingBox structureBoundingBoxIn) {
        if (this.isLiquidInStructureBoundingBox(worldIn, structureBoundingBoxIn)) {
            return false;
        }
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, this.boundingBox.minX, this.boundingBox.minY, this.boundingBox.minZ, this.boundingBox.maxX, this.boundingBox.minY, this.boundingBox.maxZ, Blocks.dirt.getDefaultState(), Blocks.air.getDefaultState(), true);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, this.boundingBox.minX, this.boundingBox.minY + 1, this.boundingBox.minZ, this.boundingBox.maxX, Math.min((int)(this.boundingBox.minY + 3), (int)this.boundingBox.maxY), this.boundingBox.maxZ, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
        for (StructureBoundingBox structureboundingbox : this.roomsLinkedToTheRoom) {
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, structureboundingbox.minX, structureboundingbox.maxY - 2, structureboundingbox.minZ, structureboundingbox.maxX, structureboundingbox.maxY, structureboundingbox.maxZ, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
        }
        this.randomlyRareFillWithBlocks(worldIn, structureBoundingBoxIn, this.boundingBox.minX, this.boundingBox.minY + 4, this.boundingBox.minZ, this.boundingBox.maxX, this.boundingBox.maxY, this.boundingBox.maxZ, Blocks.air.getDefaultState(), false);
        return true;
    }

    public void func_181138_a(int p_181138_1_, int p_181138_2_, int p_181138_3_) {
        super.func_181138_a(p_181138_1_, p_181138_2_, p_181138_3_);
        for (StructureBoundingBox structureboundingbox : this.roomsLinkedToTheRoom) {
            structureboundingbox.offset(p_181138_1_, p_181138_2_, p_181138_3_);
        }
    }

    protected void writeStructureToNBT(NBTTagCompound tagCompound) {
        NBTTagList nbttaglist = new NBTTagList();
        for (StructureBoundingBox structureboundingbox : this.roomsLinkedToTheRoom) {
            nbttaglist.appendTag((NBTBase)structureboundingbox.toNBTTagIntArray());
        }
        tagCompound.setTag("Entrances", (NBTBase)nbttaglist);
    }

    protected void readStructureFromNBT(NBTTagCompound tagCompound) {
        NBTTagList nbttaglist = tagCompound.getTagList("Entrances", 11);
        for (int i = 0; i < nbttaglist.tagCount(); ++i) {
            this.roomsLinkedToTheRoom.add((Object)new StructureBoundingBox(nbttaglist.getIntArrayAt(i)));
        }
    }
}
