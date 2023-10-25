/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.world.gen.structure;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.MapGenStructureIO;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;

public abstract class StructureStart {
    protected StructureBoundingBox boundingBox;
    private int chunkPosX;
    private int chunkPosZ;
    protected LinkedList<StructureComponent> components = new LinkedList();

    public int getChunkPosX() {
        return this.chunkPosX;
    }

    protected void setRandomHeight(World world, Random random, int n, int n2) {
        int n3 = n2 - n + 1 - this.boundingBox.getYSize();
        int n4 = 1;
        n4 = n3 > 1 ? n + random.nextInt(n3) : n;
        int n5 = n4 - this.boundingBox.minY;
        this.boundingBox.offset(0, n5, 0);
        for (StructureComponent structureComponent : this.components) {
            structureComponent.func_181138_a(0, n5, 0);
        }
    }

    public void generateStructure(World world, Random random, StructureBoundingBox structureBoundingBox) {
        Iterator iterator = this.components.iterator();
        while (iterator.hasNext()) {
            StructureComponent structureComponent = (StructureComponent)iterator.next();
            if (!structureComponent.getBoundingBox().intersectsWith(structureBoundingBox) || structureComponent.addComponentParts(world, random, structureBoundingBox)) continue;
            iterator.remove();
        }
    }

    public void readFromNBT(NBTTagCompound nBTTagCompound) {
    }

    public void func_175787_b(ChunkCoordIntPair chunkCoordIntPair) {
    }

    protected void updateBoundingBox() {
        this.boundingBox = StructureBoundingBox.getNewBoundingBox();
        for (StructureComponent structureComponent : this.components) {
            this.boundingBox.expandTo(structureComponent.getBoundingBox());
        }
    }

    public boolean func_175788_a(ChunkCoordIntPair chunkCoordIntPair) {
        return true;
    }

    public LinkedList<StructureComponent> getComponents() {
        return this.components;
    }

    public void writeToNBT(NBTTagCompound nBTTagCompound) {
    }

    public StructureBoundingBox getBoundingBox() {
        return this.boundingBox;
    }

    public StructureStart() {
    }

    public boolean isSizeableStructure() {
        return true;
    }

    protected void markAvailableHeight(World world, Random random, int n) {
        int n2 = world.func_181545_F() - n;
        int n3 = this.boundingBox.getYSize() + 1;
        if (n3 < n2) {
            n3 += random.nextInt(n2 - n3);
        }
        int n4 = n3 - this.boundingBox.maxY;
        this.boundingBox.offset(0, n4, 0);
        for (StructureComponent structureComponent : this.components) {
            structureComponent.func_181138_a(0, n4, 0);
        }
    }

    public int getChunkPosZ() {
        return this.chunkPosZ;
    }

    public void readStructureComponentsFromNBT(World world, NBTTagCompound nBTTagCompound) {
        this.chunkPosX = nBTTagCompound.getInteger("ChunkX");
        this.chunkPosZ = nBTTagCompound.getInteger("ChunkZ");
        if (nBTTagCompound.hasKey("BB")) {
            this.boundingBox = new StructureBoundingBox(nBTTagCompound.getIntArray("BB"));
        }
        NBTTagList nBTTagList = nBTTagCompound.getTagList("Children", 10);
        int n = 0;
        while (n < nBTTagList.tagCount()) {
            this.components.add(MapGenStructureIO.getStructureComponent(nBTTagList.getCompoundTagAt(n), world));
            ++n;
        }
        this.readFromNBT(nBTTagCompound);
    }

    public NBTTagCompound writeStructureComponentsToNBT(int n, int n2) {
        NBTTagCompound nBTTagCompound = new NBTTagCompound();
        nBTTagCompound.setString("id", MapGenStructureIO.getStructureStartName(this));
        nBTTagCompound.setInteger("ChunkX", n);
        nBTTagCompound.setInteger("ChunkZ", n2);
        nBTTagCompound.setTag("BB", this.boundingBox.toNBTTagIntArray());
        NBTTagList nBTTagList = new NBTTagList();
        for (StructureComponent structureComponent : this.components) {
            nBTTagList.appendTag(structureComponent.createStructureBaseNBT());
        }
        nBTTagCompound.setTag("Children", nBTTagList);
        this.writeToNBT(nBTTagCompound);
        return nBTTagCompound;
    }

    public StructureStart(int n, int n2) {
        this.chunkPosX = n;
        this.chunkPosZ = n2;
    }
}

