package net.minecraft.world.gen.structure;

import com.google.common.collect.Lists;
import java.util.List;
import java.util.Random;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.StructureNetherBridgePieces;

/*
 * Exception performing whole class analysis ignored.
 */
public static class StructureNetherBridgePieces.Start
extends StructureNetherBridgePieces.Crossing3 {
    public StructureNetherBridgePieces.PieceWeight theNetherBridgePieceWeight;
    public List<StructureNetherBridgePieces.PieceWeight> primaryWeights;
    public List<StructureNetherBridgePieces.PieceWeight> secondaryWeights;
    public List<StructureComponent> field_74967_d = Lists.newArrayList();

    public StructureNetherBridgePieces.Start() {
    }

    public StructureNetherBridgePieces.Start(Random p_i2059_1_, int p_i2059_2_, int p_i2059_3_) {
        super(p_i2059_1_, p_i2059_2_, p_i2059_3_);
        this.primaryWeights = Lists.newArrayList();
        for (StructureNetherBridgePieces.PieceWeight structurenetherbridgepieces$pieceweight : StructureNetherBridgePieces.access$100()) {
            structurenetherbridgepieces$pieceweight.field_78827_c = 0;
            this.primaryWeights.add((Object)structurenetherbridgepieces$pieceweight);
        }
        this.secondaryWeights = Lists.newArrayList();
        for (StructureNetherBridgePieces.PieceWeight structurenetherbridgepieces$pieceweight1 : StructureNetherBridgePieces.access$200()) {
            structurenetherbridgepieces$pieceweight1.field_78827_c = 0;
            this.secondaryWeights.add((Object)structurenetherbridgepieces$pieceweight1);
        }
    }

    protected void readStructureFromNBT(NBTTagCompound tagCompound) {
        super.readStructureFromNBT(tagCompound);
    }

    protected void writeStructureToNBT(NBTTagCompound tagCompound) {
        super.writeStructureToNBT(tagCompound);
    }
}
