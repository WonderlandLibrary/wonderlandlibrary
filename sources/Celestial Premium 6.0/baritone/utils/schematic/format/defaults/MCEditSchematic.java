/*
 * Decompiled with CFR 0.150.
 */
package baritone.utils.schematic.format.defaults;

import baritone.utils.schematic.StaticSchematic;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;

public final class MCEditSchematic
extends StaticSchematic {
    public MCEditSchematic(NBTTagCompound schematic) {
        String type = schematic.getString("Materials");
        if (!type.equals("Alpha")) {
            throw new IllegalStateException("bad schematic " + type);
        }
        this.x = schematic.getInteger("Width");
        this.y = schematic.getInteger("Height");
        this.z = schematic.getInteger("Length");
        byte[] blocks = schematic.getByteArray("Blocks");
        byte[] metadata = schematic.getByteArray("Data");
        byte[] additional = null;
        if (schematic.hasKey("AddBlocks")) {
            byte[] addBlocks = schematic.getByteArray("AddBlocks");
            additional = new byte[addBlocks.length * 2];
            for (int i = 0; i < addBlocks.length; ++i) {
                additional[i * 2 + 0] = (byte)(addBlocks[i] >> 4 & 0xF);
                additional[i * 2 + 1] = (byte)(addBlocks[i] >> 0 & 0xF);
            }
        }
        this.states = new IBlockState[this.x][this.z][this.y];
        for (int y = 0; y < this.y; ++y) {
            for (int z = 0; z < this.z; ++z) {
                for (int x = 0; x < this.x; ++x) {
                    int blockInd = (y * this.z + z) * this.x + x;
                    int blockID = blocks[blockInd] & 0xFF;
                    if (additional != null) {
                        blockID |= additional[blockInd] << 8;
                    }
                    Block block = Block.REGISTRY.getObjectById(blockID);
                    int meta = metadata[blockInd] & 0xFF;
                    this.states[x][z][y] = block.getStateFromMeta(meta);
                }
            }
        }
    }
}

