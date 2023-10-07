// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.world.gen.structure;

import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import java.util.Iterator;
import java.util.Random;
import net.minecraft.world.World;
import java.util.LinkedList;

public abstract class StructureStart
{
    protected LinkedList components;
    protected StructureBoundingBox boundingBox;
    private int field_143024_c;
    private int field_143023_d;
    private static final String __OBFID = "CL_00000513";
    
    public StructureStart() {
        this.components = new LinkedList();
    }
    
    public StructureStart(final int p_i43002_1_, final int p_i43002_2_) {
        this.components = new LinkedList();
        this.field_143024_c = p_i43002_1_;
        this.field_143023_d = p_i43002_2_;
    }
    
    public StructureBoundingBox getBoundingBox() {
        return this.boundingBox;
    }
    
    public LinkedList getComponents() {
        return this.components;
    }
    
    public void generateStructure(final World worldIn, final Random p_75068_2_, final StructureBoundingBox p_75068_3_) {
        final Iterator var4 = this.components.iterator();
        while (var4.hasNext()) {
            final StructureComponent var5 = var4.next();
            if (var5.getBoundingBox().intersectsWith(p_75068_3_) && !var5.addComponentParts(worldIn, p_75068_2_, p_75068_3_)) {
                var4.remove();
            }
        }
    }
    
    protected void updateBoundingBox() {
        this.boundingBox = StructureBoundingBox.getNewBoundingBox();
        for (final StructureComponent var2 : this.components) {
            this.boundingBox.expandTo(var2.getBoundingBox());
        }
    }
    
    public NBTTagCompound func_143021_a(final int p_143021_1_, final int p_143021_2_) {
        final NBTTagCompound var3 = new NBTTagCompound();
        var3.setString("id", MapGenStructureIO.func_143033_a(this));
        var3.setInteger("ChunkX", p_143021_1_);
        var3.setInteger("ChunkZ", p_143021_2_);
        var3.setTag("BB", this.boundingBox.func_151535_h());
        final NBTTagList var4 = new NBTTagList();
        for (final StructureComponent var6 : this.components) {
            var4.appendTag(var6.func_143010_b());
        }
        var3.setTag("Children", var4);
        this.func_143022_a(var3);
        return var3;
    }
    
    public void func_143022_a(final NBTTagCompound p_143022_1_) {
    }
    
    public void func_143020_a(final World worldIn, final NBTTagCompound p_143020_2_) {
        this.field_143024_c = p_143020_2_.getInteger("ChunkX");
        this.field_143023_d = p_143020_2_.getInteger("ChunkZ");
        if (p_143020_2_.hasKey("BB")) {
            this.boundingBox = new StructureBoundingBox(p_143020_2_.getIntArray("BB"));
        }
        final NBTTagList var3 = p_143020_2_.getTagList("Children", 10);
        for (int var4 = 0; var4 < var3.tagCount(); ++var4) {
            this.components.add(MapGenStructureIO.func_143032_b(var3.getCompoundTagAt(var4), worldIn));
        }
        this.func_143017_b(p_143020_2_);
    }
    
    public void func_143017_b(final NBTTagCompound p_143017_1_) {
    }
    
    protected void markAvailableHeight(final World worldIn, final Random p_75067_2_, final int p_75067_3_) {
        final int var4 = 63 - p_75067_3_;
        int var5 = this.boundingBox.getYSize() + 1;
        if (var5 < var4) {
            var5 += p_75067_2_.nextInt(var4 - var5);
        }
        final int var6 = var5 - this.boundingBox.maxY;
        this.boundingBox.offset(0, var6, 0);
        for (final StructureComponent var8 : this.components) {
            var8.getBoundingBox().offset(0, var6, 0);
        }
    }
    
    protected void setRandomHeight(final World worldIn, final Random p_75070_2_, final int p_75070_3_, final int p_75070_4_) {
        final int var5 = p_75070_4_ - p_75070_3_ + 1 - this.boundingBox.getYSize();
        final boolean var6 = true;
        int var7;
        if (var5 > 1) {
            var7 = p_75070_3_ + p_75070_2_.nextInt(var5);
        }
        else {
            var7 = p_75070_3_;
        }
        final int var8 = var7 - this.boundingBox.minY;
        this.boundingBox.offset(0, var8, 0);
        for (final StructureComponent var10 : this.components) {
            var10.getBoundingBox().offset(0, var8, 0);
        }
    }
    
    public boolean isSizeableStructure() {
        return true;
    }
    
    public boolean func_175788_a(final ChunkCoordIntPair p_175788_1_) {
        return true;
    }
    
    public void func_175787_b(final ChunkCoordIntPair p_175787_1_) {
    }
    
    public int func_143019_e() {
        return this.field_143024_c;
    }
    
    public int func_143018_f() {
        return this.field_143023_d;
    }
}
