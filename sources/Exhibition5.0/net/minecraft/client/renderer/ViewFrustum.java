// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.client.renderer;

import net.minecraft.util.MathHelper;
import net.minecraft.util.BlockPos;
import net.minecraft.client.renderer.chunk.IRenderChunkFactory;
import net.minecraft.client.renderer.chunk.RenderChunk;
import net.minecraft.world.World;

public class ViewFrustum
{
    protected final RenderGlobal field_178169_a;
    protected final World field_178167_b;
    protected int field_178168_c;
    protected int field_178165_d;
    protected int field_178166_e;
    public RenderChunk[] field_178164_f;
    private static final String __OBFID = "CL_00002531";
    
    public ViewFrustum(final World worldIn, final int p_i46246_2_, final RenderGlobal p_i46246_3_, final IRenderChunkFactory p_i46246_4_) {
        this.field_178169_a = p_i46246_3_;
        this.field_178167_b = worldIn;
        this.func_178159_a(p_i46246_2_);
        this.func_178158_a(p_i46246_4_);
    }
    
    protected void func_178158_a(final IRenderChunkFactory p_178158_1_) {
        final int var2 = this.field_178165_d * this.field_178168_c * this.field_178166_e;
        this.field_178164_f = new RenderChunk[var2];
        int var3 = 0;
        for (int var4 = 0; var4 < this.field_178165_d; ++var4) {
            for (int var5 = 0; var5 < this.field_178168_c; ++var5) {
                for (int var6 = 0; var6 < this.field_178166_e; ++var6) {
                    final int var7 = (var6 * this.field_178168_c + var5) * this.field_178165_d + var4;
                    final BlockPos var8 = new BlockPos(var4 * 16, var5 * 16, var6 * 16);
                    this.field_178164_f[var7] = p_178158_1_.func_178602_a(this.field_178167_b, this.field_178169_a, var8, var3++);
                }
            }
        }
    }
    
    public void func_178160_a() {
        for (final RenderChunk var4 : this.field_178164_f) {
            var4.func_178566_a();
        }
    }
    
    protected void func_178159_a(final int p_178159_1_) {
        final int var2 = p_178159_1_ * 2 + 1;
        this.field_178165_d = var2;
        this.field_178168_c = 16;
        this.field_178166_e = var2;
    }
    
    public void func_178163_a(final double p_178163_1_, final double p_178163_3_) {
        final int var5 = MathHelper.floor_double(p_178163_1_) - 8;
        final int var6 = MathHelper.floor_double(p_178163_3_) - 8;
        final int var7 = this.field_178165_d * 16;
        for (int var8 = 0; var8 < this.field_178165_d; ++var8) {
            final int var9 = this.func_178157_a(var5, var7, var8);
            for (int var10 = 0; var10 < this.field_178166_e; ++var10) {
                final int var11 = this.func_178157_a(var6, var7, var10);
                for (int var12 = 0; var12 < this.field_178168_c; ++var12) {
                    final int var13 = var12 * 16;
                    final RenderChunk var14 = this.field_178164_f[(var10 * this.field_178168_c + var12) * this.field_178165_d + var8];
                    final BlockPos var15 = new BlockPos(var9, var13, var11);
                    if (!var15.equals(var14.func_178568_j())) {
                        var14.func_178576_a(var15);
                    }
                }
            }
        }
    }
    
    private int func_178157_a(final int p_178157_1_, final int p_178157_2_, final int p_178157_3_) {
        final int var4 = p_178157_3_ * 16;
        int var5 = var4 - p_178157_1_ + p_178157_2_ / 2;
        if (var5 < 0) {
            var5 -= p_178157_2_ - 1;
        }
        return var4 - var5 / p_178157_2_ * p_178157_2_;
    }
    
    public void func_178162_a(final int p_178162_1_, final int p_178162_2_, final int p_178162_3_, final int p_178162_4_, final int p_178162_5_, final int p_178162_6_) {
        final int var7 = MathHelper.bucketInt(p_178162_1_, 16);
        final int var8 = MathHelper.bucketInt(p_178162_2_, 16);
        final int var9 = MathHelper.bucketInt(p_178162_3_, 16);
        final int var10 = MathHelper.bucketInt(p_178162_4_, 16);
        final int var11 = MathHelper.bucketInt(p_178162_5_, 16);
        final int var12 = MathHelper.bucketInt(p_178162_6_, 16);
        for (int var13 = var7; var13 <= var10; ++var13) {
            int var14 = var13 % this.field_178165_d;
            if (var14 < 0) {
                var14 += this.field_178165_d;
            }
            for (int var15 = var8; var15 <= var11; ++var15) {
                int var16 = var15 % this.field_178168_c;
                if (var16 < 0) {
                    var16 += this.field_178168_c;
                }
                for (int var17 = var9; var17 <= var12; ++var17) {
                    int var18 = var17 % this.field_178166_e;
                    if (var18 < 0) {
                        var18 += this.field_178166_e;
                    }
                    final int var19 = (var18 * this.field_178168_c + var16) * this.field_178165_d + var14;
                    final RenderChunk var20 = this.field_178164_f[var19];
                    var20.func_178575_a(true);
                }
            }
        }
    }
    
    protected RenderChunk func_178161_a(final BlockPos p_178161_1_) {
        int var2 = MathHelper.bucketInt(p_178161_1_.getX(), 16);
        final int var3 = MathHelper.bucketInt(p_178161_1_.getY(), 16);
        int var4 = MathHelper.bucketInt(p_178161_1_.getZ(), 16);
        if (var3 >= 0 && var3 < this.field_178168_c) {
            var2 %= this.field_178165_d;
            if (var2 < 0) {
                var2 += this.field_178165_d;
            }
            var4 %= this.field_178166_e;
            if (var4 < 0) {
                var4 += this.field_178166_e;
            }
            final int var5 = (var4 * this.field_178168_c + var3) * this.field_178165_d + var2;
            return this.field_178164_f[var5];
        }
        return null;
    }
}
