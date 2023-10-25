package net.minecraft.world.gen.structure;

import java.util.List;
import java.util.Random;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityMinecartChest;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3i;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.StructureMineshaftPieces;

/*
 * Exception performing whole class analysis ignored.
 */
public static class StructureMineshaftPieces.Corridor
extends StructureComponent {
    private boolean hasRails;
    private boolean hasSpiders;
    private boolean spawnerPlaced;
    private int sectionCount;

    public StructureMineshaftPieces.Corridor() {
    }

    protected void writeStructureToNBT(NBTTagCompound tagCompound) {
        tagCompound.setBoolean("hr", this.hasRails);
        tagCompound.setBoolean("sc", this.hasSpiders);
        tagCompound.setBoolean("hps", this.spawnerPlaced);
        tagCompound.setInteger("Num", this.sectionCount);
    }

    protected void readStructureFromNBT(NBTTagCompound tagCompound) {
        this.hasRails = tagCompound.getBoolean("hr");
        this.hasSpiders = tagCompound.getBoolean("sc");
        this.spawnerPlaced = tagCompound.getBoolean("hps");
        this.sectionCount = tagCompound.getInteger("Num");
    }

    public StructureMineshaftPieces.Corridor(int type, Random rand, StructureBoundingBox structurebb, EnumFacing facing) {
        super(type);
        this.coordBaseMode = facing;
        this.boundingBox = structurebb;
        this.hasRails = rand.nextInt(3) == 0;
        this.hasSpiders = !this.hasRails && rand.nextInt(23) == 0;
        this.sectionCount = this.coordBaseMode != EnumFacing.NORTH && this.coordBaseMode != EnumFacing.SOUTH ? structurebb.getXSize() / 5 : structurebb.getZSize() / 5;
    }

    public static StructureBoundingBox func_175814_a(List<StructureComponent> p_175814_0_, Random rand, int x, int y, int z, EnumFacing facing) {
        int i;
        StructureBoundingBox structureboundingbox = new StructureBoundingBox(x, y, z, x, y + 2, z);
        for (i = rand.nextInt(3) + 2; i > 0; --i) {
            int j = i * 5;
            switch (StructureMineshaftPieces.1.$SwitchMap$net$minecraft$util$EnumFacing[facing.ordinal()]) {
                case 1: {
                    structureboundingbox.maxX = x + 2;
                    structureboundingbox.minZ = z - (j - 1);
                    break;
                }
                case 2: {
                    structureboundingbox.maxX = x + 2;
                    structureboundingbox.maxZ = z + (j - 1);
                    break;
                }
                case 3: {
                    structureboundingbox.minX = x - (j - 1);
                    structureboundingbox.maxZ = z + 2;
                    break;
                }
                case 4: {
                    structureboundingbox.maxX = x + (j - 1);
                    structureboundingbox.maxZ = z + 2;
                }
            }
            if (StructureComponent.findIntersecting(p_175814_0_, (StructureBoundingBox)structureboundingbox) == null) break;
        }
        return i > 0 ? structureboundingbox : null;
    }

    public void buildComponent(StructureComponent componentIn, List<StructureComponent> listIn, Random rand) {
        block25: {
            int i = this.getComponentType();
            int j = rand.nextInt(4);
            if (this.coordBaseMode != null) {
                switch (StructureMineshaftPieces.1.$SwitchMap$net$minecraft$util$EnumFacing[this.coordBaseMode.ordinal()]) {
                    case 1: {
                        if (j <= 1) {
                            StructureMineshaftPieces.access$000((StructureComponent)componentIn, listIn, (Random)rand, (int)this.boundingBox.minX, (int)(this.boundingBox.minY - 1 + rand.nextInt(3)), (int)(this.boundingBox.minZ - 1), (EnumFacing)this.coordBaseMode, (int)i);
                            break;
                        }
                        if (j == 2) {
                            StructureMineshaftPieces.access$000((StructureComponent)componentIn, listIn, (Random)rand, (int)(this.boundingBox.minX - 1), (int)(this.boundingBox.minY - 1 + rand.nextInt(3)), (int)this.boundingBox.minZ, (EnumFacing)EnumFacing.WEST, (int)i);
                            break;
                        }
                        StructureMineshaftPieces.access$000((StructureComponent)componentIn, listIn, (Random)rand, (int)(this.boundingBox.maxX + 1), (int)(this.boundingBox.minY - 1 + rand.nextInt(3)), (int)this.boundingBox.minZ, (EnumFacing)EnumFacing.EAST, (int)i);
                        break;
                    }
                    case 2: {
                        if (j <= 1) {
                            StructureMineshaftPieces.access$000((StructureComponent)componentIn, listIn, (Random)rand, (int)this.boundingBox.minX, (int)(this.boundingBox.minY - 1 + rand.nextInt(3)), (int)(this.boundingBox.maxZ + 1), (EnumFacing)this.coordBaseMode, (int)i);
                            break;
                        }
                        if (j == 2) {
                            StructureMineshaftPieces.access$000((StructureComponent)componentIn, listIn, (Random)rand, (int)(this.boundingBox.minX - 1), (int)(this.boundingBox.minY - 1 + rand.nextInt(3)), (int)(this.boundingBox.maxZ - 3), (EnumFacing)EnumFacing.WEST, (int)i);
                            break;
                        }
                        StructureMineshaftPieces.access$000((StructureComponent)componentIn, listIn, (Random)rand, (int)(this.boundingBox.maxX + 1), (int)(this.boundingBox.minY - 1 + rand.nextInt(3)), (int)(this.boundingBox.maxZ - 3), (EnumFacing)EnumFacing.EAST, (int)i);
                        break;
                    }
                    case 3: {
                        if (j <= 1) {
                            StructureMineshaftPieces.access$000((StructureComponent)componentIn, listIn, (Random)rand, (int)(this.boundingBox.minX - 1), (int)(this.boundingBox.minY - 1 + rand.nextInt(3)), (int)this.boundingBox.minZ, (EnumFacing)this.coordBaseMode, (int)i);
                            break;
                        }
                        if (j == 2) {
                            StructureMineshaftPieces.access$000((StructureComponent)componentIn, listIn, (Random)rand, (int)this.boundingBox.minX, (int)(this.boundingBox.minY - 1 + rand.nextInt(3)), (int)(this.boundingBox.minZ - 1), (EnumFacing)EnumFacing.NORTH, (int)i);
                            break;
                        }
                        StructureMineshaftPieces.access$000((StructureComponent)componentIn, listIn, (Random)rand, (int)this.boundingBox.minX, (int)(this.boundingBox.minY - 1 + rand.nextInt(3)), (int)(this.boundingBox.maxZ + 1), (EnumFacing)EnumFacing.SOUTH, (int)i);
                        break;
                    }
                    case 4: {
                        if (j <= 1) {
                            StructureMineshaftPieces.access$000((StructureComponent)componentIn, listIn, (Random)rand, (int)(this.boundingBox.maxX + 1), (int)(this.boundingBox.minY - 1 + rand.nextInt(3)), (int)this.boundingBox.minZ, (EnumFacing)this.coordBaseMode, (int)i);
                            break;
                        }
                        if (j == 2) {
                            StructureMineshaftPieces.access$000((StructureComponent)componentIn, listIn, (Random)rand, (int)(this.boundingBox.maxX - 3), (int)(this.boundingBox.minY - 1 + rand.nextInt(3)), (int)(this.boundingBox.minZ - 1), (EnumFacing)EnumFacing.NORTH, (int)i);
                            break;
                        }
                        StructureMineshaftPieces.access$000((StructureComponent)componentIn, listIn, (Random)rand, (int)(this.boundingBox.maxX - 3), (int)(this.boundingBox.minY - 1 + rand.nextInt(3)), (int)(this.boundingBox.maxZ + 1), (EnumFacing)EnumFacing.SOUTH, (int)i);
                    }
                }
            }
            if (i >= 8) break block25;
            if (this.coordBaseMode != EnumFacing.NORTH && this.coordBaseMode != EnumFacing.SOUTH) {
                int i1 = this.boundingBox.minX + 3;
                while (i1 + 3 <= this.boundingBox.maxX) {
                    int j1 = rand.nextInt(5);
                    if (j1 == 0) {
                        StructureMineshaftPieces.access$000((StructureComponent)componentIn, listIn, (Random)rand, (int)i1, (int)this.boundingBox.minY, (int)(this.boundingBox.minZ - 1), (EnumFacing)EnumFacing.NORTH, (int)(i + 1));
                    } else if (j1 == 1) {
                        StructureMineshaftPieces.access$000((StructureComponent)componentIn, listIn, (Random)rand, (int)i1, (int)this.boundingBox.minY, (int)(this.boundingBox.maxZ + 1), (EnumFacing)EnumFacing.SOUTH, (int)(i + 1));
                    }
                    i1 += 5;
                }
            } else {
                int k = this.boundingBox.minZ + 3;
                while (k + 3 <= this.boundingBox.maxZ) {
                    int l = rand.nextInt(5);
                    if (l == 0) {
                        StructureMineshaftPieces.access$000((StructureComponent)componentIn, listIn, (Random)rand, (int)(this.boundingBox.minX - 1), (int)this.boundingBox.minY, (int)k, (EnumFacing)EnumFacing.WEST, (int)(i + 1));
                    } else if (l == 1) {
                        StructureMineshaftPieces.access$000((StructureComponent)componentIn, listIn, (Random)rand, (int)(this.boundingBox.maxX + 1), (int)this.boundingBox.minY, (int)k, (EnumFacing)EnumFacing.EAST, (int)(i + 1));
                    }
                    k += 5;
                }
            }
        }
    }

    protected boolean generateChestContents(World worldIn, StructureBoundingBox boundingBoxIn, Random rand, int x, int y, int z, List<WeightedRandomChestContent> listIn, int max) {
        BlockPos blockpos = new BlockPos(this.getXWithOffset(x, z), this.getYWithOffset(y), this.getZWithOffset(x, z));
        if (boundingBoxIn.isVecInside((Vec3i)blockpos) && worldIn.getBlockState(blockpos).getBlock().getMaterial() == Material.air) {
            int i = rand.nextBoolean() ? 1 : 0;
            worldIn.setBlockState(blockpos, Blocks.rail.getStateFromMeta(this.getMetadataWithOffset(Blocks.rail, i)), 2);
            EntityMinecartChest entityminecartchest = new EntityMinecartChest(worldIn, (double)((float)blockpos.getX() + 0.5f), (double)((float)blockpos.getY() + 0.5f), (double)((float)blockpos.getZ() + 0.5f));
            WeightedRandomChestContent.generateChestContents((Random)rand, listIn, (IInventory)entityminecartchest, (int)max);
            worldIn.spawnEntityInWorld((Entity)entityminecartchest);
            return true;
        }
        return false;
    }

    public boolean addComponentParts(World worldIn, Random randomIn, StructureBoundingBox structureBoundingBoxIn) {
        if (this.isLiquidInStructureBoundingBox(worldIn, structureBoundingBoxIn)) {
            return false;
        }
        boolean i = false;
        int j = 2;
        boolean k = false;
        int l = 2;
        int i1 = this.sectionCount * 5 - 1;
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 0, 0, 2, 1, i1, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
        this.func_175805_a(worldIn, structureBoundingBoxIn, randomIn, 0.8f, 0, 2, 0, 2, 2, i1, Blocks.air.getDefaultState(), Blocks.air.getDefaultState(), false);
        if (this.hasSpiders) {
            this.func_175805_a(worldIn, structureBoundingBoxIn, randomIn, 0.6f, 0, 0, 0, 2, 1, i1, Blocks.web.getDefaultState(), Blocks.air.getDefaultState(), false);
        }
        for (int j1 = 0; j1 < this.sectionCount; ++j1) {
            int k1 = 2 + j1 * 5;
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 0, k1, 0, 1, k1, Blocks.oak_fence.getDefaultState(), Blocks.air.getDefaultState(), false);
            this.fillWithBlocks(worldIn, structureBoundingBoxIn, 2, 0, k1, 2, 1, k1, Blocks.oak_fence.getDefaultState(), Blocks.air.getDefaultState(), false);
            if (randomIn.nextInt(4) == 0) {
                this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 2, k1, 0, 2, k1, Blocks.planks.getDefaultState(), Blocks.air.getDefaultState(), false);
                this.fillWithBlocks(worldIn, structureBoundingBoxIn, 2, 2, k1, 2, 2, k1, Blocks.planks.getDefaultState(), Blocks.air.getDefaultState(), false);
            } else {
                this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 2, k1, 2, 2, k1, Blocks.planks.getDefaultState(), Blocks.air.getDefaultState(), false);
            }
            this.randomlyPlaceBlock(worldIn, structureBoundingBoxIn, randomIn, 0.1f, 0, 2, k1 - 1, Blocks.web.getDefaultState());
            this.randomlyPlaceBlock(worldIn, structureBoundingBoxIn, randomIn, 0.1f, 2, 2, k1 - 1, Blocks.web.getDefaultState());
            this.randomlyPlaceBlock(worldIn, structureBoundingBoxIn, randomIn, 0.1f, 0, 2, k1 + 1, Blocks.web.getDefaultState());
            this.randomlyPlaceBlock(worldIn, structureBoundingBoxIn, randomIn, 0.1f, 2, 2, k1 + 1, Blocks.web.getDefaultState());
            this.randomlyPlaceBlock(worldIn, structureBoundingBoxIn, randomIn, 0.05f, 0, 2, k1 - 2, Blocks.web.getDefaultState());
            this.randomlyPlaceBlock(worldIn, structureBoundingBoxIn, randomIn, 0.05f, 2, 2, k1 - 2, Blocks.web.getDefaultState());
            this.randomlyPlaceBlock(worldIn, structureBoundingBoxIn, randomIn, 0.05f, 0, 2, k1 + 2, Blocks.web.getDefaultState());
            this.randomlyPlaceBlock(worldIn, structureBoundingBoxIn, randomIn, 0.05f, 2, 2, k1 + 2, Blocks.web.getDefaultState());
            this.randomlyPlaceBlock(worldIn, structureBoundingBoxIn, randomIn, 0.05f, 1, 2, k1 - 1, Blocks.torch.getStateFromMeta(EnumFacing.UP.getIndex()));
            this.randomlyPlaceBlock(worldIn, structureBoundingBoxIn, randomIn, 0.05f, 1, 2, k1 + 1, Blocks.torch.getStateFromMeta(EnumFacing.UP.getIndex()));
            if (randomIn.nextInt(100) == 0) {
                this.generateChestContents(worldIn, structureBoundingBoxIn, randomIn, 2, 0, k1 - 1, (List<WeightedRandomChestContent>)WeightedRandomChestContent.func_177629_a((List)StructureMineshaftPieces.access$100(), (WeightedRandomChestContent[])new WeightedRandomChestContent[]{Items.enchanted_book.getRandom(randomIn)}), 3 + randomIn.nextInt(4));
            }
            if (randomIn.nextInt(100) == 0) {
                this.generateChestContents(worldIn, structureBoundingBoxIn, randomIn, 0, 0, k1 + 1, (List<WeightedRandomChestContent>)WeightedRandomChestContent.func_177629_a((List)StructureMineshaftPieces.access$100(), (WeightedRandomChestContent[])new WeightedRandomChestContent[]{Items.enchanted_book.getRandom(randomIn)}), 3 + randomIn.nextInt(4));
            }
            if (!this.hasSpiders || this.spawnerPlaced) continue;
            int l1 = this.getYWithOffset(0);
            int i2 = k1 - 1 + randomIn.nextInt(3);
            int j2 = this.getXWithOffset(1, i2);
            BlockPos blockpos = new BlockPos(j2, l1, i2 = this.getZWithOffset(1, i2));
            if (!structureBoundingBoxIn.isVecInside((Vec3i)blockpos)) continue;
            this.spawnerPlaced = true;
            worldIn.setBlockState(blockpos, Blocks.mob_spawner.getDefaultState(), 2);
            TileEntity tileentity = worldIn.getTileEntity(blockpos);
            if (!(tileentity instanceof TileEntityMobSpawner)) continue;
            ((TileEntityMobSpawner)tileentity).getSpawnerBaseLogic().setEntityName("CaveSpider");
        }
        for (int k2 = 0; k2 <= 2; ++k2) {
            for (int i3 = 0; i3 <= i1; ++i3) {
                int j3 = -1;
                IBlockState iblockstate1 = this.getBlockStateFromPos(worldIn, k2, j3, i3, structureBoundingBoxIn);
                if (iblockstate1.getBlock().getMaterial() != Material.air) continue;
                int k3 = -1;
                this.setBlockState(worldIn, Blocks.planks.getDefaultState(), k2, k3, i3, structureBoundingBoxIn);
            }
        }
        if (this.hasRails) {
            for (int l2 = 0; l2 <= i1; ++l2) {
                IBlockState iblockstate = this.getBlockStateFromPos(worldIn, 1, -1, l2, structureBoundingBoxIn);
                if (iblockstate.getBlock().getMaterial() == Material.air || !iblockstate.getBlock().isFullBlock()) continue;
                this.randomlyPlaceBlock(worldIn, structureBoundingBoxIn, randomIn, 0.7f, 1, 0, l2, Blocks.rail.getStateFromMeta(this.getMetadataWithOffset(Blocks.rail, 0)));
            }
        }
        return true;
    }
}
