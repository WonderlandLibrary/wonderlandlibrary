// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.client.renderer.chunk;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import java.util.Iterator;
import net.minecraft.world.IBlockAccess;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.Vec3i;
import net.minecraft.optifine.BlockPosM;
import net.minecraft.client.renderer.RegionRenderCache;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.client.renderer.vertex.VertexBuffer;
import java.nio.FloatBuffer;
import java.util.concurrent.locks.ReentrantLock;
import net.minecraft.util.BlockPos;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.world.World;

public class RenderChunk
{
    private World field_178588_d;
    private final RenderGlobal field_178589_e;
    public static int field_178592_a;
    private BlockPos field_178586_f;
    public CompiledChunk field_178590_b;
    private final ReentrantLock field_178587_g;
    private final ReentrantLock field_178598_h;
    private ChunkCompileTaskGenerator field_178599_i;
    private final int field_178596_j;
    private final FloatBuffer field_178597_k;
    private final VertexBuffer[] field_178594_l;
    public AxisAlignedBB field_178591_c;
    private int field_178595_m;
    private boolean field_178593_n;
    private static final String __OBFID = "CL_00002452";
    private BlockPos[] positionOffsets16;
    
    public RenderChunk(final World worldIn, final RenderGlobal renderGlobalIn, final BlockPos blockPosIn, final int indexIn) {
        this.positionOffsets16 = new BlockPos[EnumFacing.VALUES.length];
        this.field_178590_b = CompiledChunk.field_178502_a;
        this.field_178587_g = new ReentrantLock();
        this.field_178598_h = new ReentrantLock();
        this.field_178599_i = null;
        this.field_178597_k = GLAllocation.createDirectFloatBuffer(16);
        this.field_178594_l = new VertexBuffer[EnumWorldBlockLayer.values().length];
        this.field_178595_m = -1;
        this.field_178593_n = true;
        this.field_178588_d = worldIn;
        this.field_178589_e = renderGlobalIn;
        this.field_178596_j = indexIn;
        if (!blockPosIn.equals(this.func_178568_j())) {
            this.func_178576_a(blockPosIn);
        }
        if (OpenGlHelper.func_176075_f()) {
            for (int var5 = 0; var5 < EnumWorldBlockLayer.values().length; ++var5) {
                this.field_178594_l[var5] = new VertexBuffer(DefaultVertexFormats.field_176600_a);
            }
        }
    }
    
    public boolean func_178577_a(final int frameIndexIn) {
        if (this.field_178595_m == frameIndexIn) {
            return false;
        }
        this.field_178595_m = frameIndexIn;
        return true;
    }
    
    public VertexBuffer func_178565_b(final int p_178565_1_) {
        return this.field_178594_l[p_178565_1_];
    }
    
    public void func_178576_a(final BlockPos p_178576_1_) {
        this.func_178585_h();
        this.field_178586_f = p_178576_1_;
        this.field_178591_c = new AxisAlignedBB(p_178576_1_, p_178576_1_.add(16, 16, 16));
        this.func_178567_n();
        for (int i = 0; i < this.positionOffsets16.length; ++i) {
            this.positionOffsets16[i] = null;
        }
    }
    
    public void func_178570_a(final float p_178570_1_, final float p_178570_2_, final float p_178570_3_, final ChunkCompileTaskGenerator p_178570_4_) {
        final CompiledChunk var5 = p_178570_4_.func_178544_c();
        if (var5.func_178487_c() != null && !var5.func_178491_b(EnumWorldBlockLayer.TRANSLUCENT)) {
            final WorldRenderer worldRenderer = p_178570_4_.func_178545_d().func_179038_a(EnumWorldBlockLayer.TRANSLUCENT);
            this.func_178573_a(worldRenderer, this.field_178586_f);
            worldRenderer.setVertexState(var5.func_178487_c());
            this.func_178584_a(EnumWorldBlockLayer.TRANSLUCENT, p_178570_1_, p_178570_2_, p_178570_3_, worldRenderer, var5);
        }
    }
    
    public void func_178581_b(final float p_178581_1_, final float p_178581_2_, final float p_178581_3_, final ChunkCompileTaskGenerator p_178581_4_) {
        final CompiledChunk var5 = new CompiledChunk();
        final boolean var6 = true;
        final BlockPos var7 = this.field_178586_f;
        final BlockPos var8 = var7.add(15, 15, 15);
        p_178581_4_.func_178540_f().lock();
        RegionRenderCache var9;
        try {
            if (p_178581_4_.func_178546_a() != ChunkCompileTaskGenerator.Status.COMPILING) {
                return;
            }
            if (this.field_178588_d == null) {
                return;
            }
            var9 = new RegionRenderCache(this.field_178588_d, var7.add(-1, -1, -1), var8.add(1, 1, 1), 1);
            p_178581_4_.func_178543_a(var5);
        }
        finally {
            p_178581_4_.func_178540_f().unlock();
        }
        final VisGraph var10 = new VisGraph();
        if (!var9.extendedLevelsInChunkCache()) {
            ++RenderChunk.field_178592_a;
            for (final BlockPosM var12 : BlockPosM.getAllInBoxMutable(var7, var8)) {
                final IBlockState var13 = var9.getBlockState(var12);
                final Block var14 = var13.getBlock();
                if (var14.isOpaqueCube()) {
                    var10.func_178606_a(var12);
                }
                if (var14.hasTileEntity()) {
                    final TileEntity var15 = var9.getTileEntity(new BlockPos(var12));
                    if (var15 != null && TileEntityRendererDispatcher.instance.hasSpecialRenderer(var15)) {
                        var5.func_178490_a(var15);
                    }
                }
                final EnumWorldBlockLayer var16 = var14.getBlockLayer();
                final int var17 = var16.ordinal();
                if (var14.getRenderType() != -1) {
                    final WorldRenderer var18 = p_178581_4_.func_178545_d().func_179039_a(var17);
                    if (!var5.func_178492_d(var16)) {
                        var5.func_178493_c(var16);
                        this.func_178573_a(var18, var7);
                    }
                    if (!Minecraft.getMinecraft().getBlockRendererDispatcher().func_175018_a(var13, var12, var9, var18)) {
                        continue;
                    }
                    var5.func_178486_a(var16);
                }
            }
            final EnumWorldBlockLayer[] var19 = EnumWorldBlockLayer.values();
            for (int var20 = var19.length, var21 = 0; var21 < var20; ++var21) {
                final EnumWorldBlockLayer var16 = var19[var21];
                if (var5.func_178492_d(var16)) {
                    this.func_178584_a(var16, p_178581_1_, p_178581_2_, p_178581_3_, p_178581_4_.func_178545_d().func_179038_a(var16), var5);
                }
            }
        }
        var5.func_178488_a(var10.func_178607_a());
    }
    
    protected void func_178578_b() {
        this.field_178587_g.lock();
        try {
            if (this.field_178599_i != null && this.field_178599_i.func_178546_a() != ChunkCompileTaskGenerator.Status.DONE) {
                this.field_178599_i.func_178542_e();
                this.field_178599_i = null;
            }
        }
        finally {
            this.field_178587_g.unlock();
        }
    }
    
    public ReentrantLock func_178579_c() {
        return this.field_178587_g;
    }
    
    public ChunkCompileTaskGenerator func_178574_d() {
        this.field_178587_g.lock();
        ChunkCompileTaskGenerator var1;
        try {
            this.func_178578_b();
            this.field_178599_i = new ChunkCompileTaskGenerator(this, ChunkCompileTaskGenerator.Type.REBUILD_CHUNK);
            var1 = this.field_178599_i;
        }
        finally {
            this.field_178587_g.unlock();
        }
        return var1;
    }
    
    public ChunkCompileTaskGenerator func_178582_e() {
        this.field_178587_g.lock();
        ChunkCompileTaskGenerator var1;
        try {
            if (this.field_178599_i == null || this.field_178599_i.func_178546_a() != ChunkCompileTaskGenerator.Status.PENDING) {
                if (this.field_178599_i != null && this.field_178599_i.func_178546_a() != ChunkCompileTaskGenerator.Status.DONE) {
                    this.field_178599_i.func_178542_e();
                    this.field_178599_i = null;
                }
                (this.field_178599_i = new ChunkCompileTaskGenerator(this, ChunkCompileTaskGenerator.Type.RESORT_TRANSPARENCY)).func_178543_a(this.field_178590_b);
                final ChunkCompileTaskGenerator var2;
                var1 = (var2 = this.field_178599_i);
                return var2;
            }
            var1 = null;
        }
        finally {
            this.field_178587_g.unlock();
        }
        return var1;
    }
    
    private void func_178573_a(final WorldRenderer p_178573_1_, final BlockPos p_178573_2_) {
        p_178573_1_.startDrawing(7);
        p_178573_1_.setVertexFormat(DefaultVertexFormats.field_176600_a);
        p_178573_1_.setTranslation(-p_178573_2_.getX(), -p_178573_2_.getY(), -p_178573_2_.getZ());
    }
    
    private void func_178584_a(final EnumWorldBlockLayer p_178584_1_, final float p_178584_2_, final float p_178584_3_, final float p_178584_4_, final WorldRenderer p_178584_5_, final CompiledChunk p_178584_6_) {
        if (p_178584_1_ == EnumWorldBlockLayer.TRANSLUCENT && !p_178584_6_.func_178491_b(p_178584_1_)) {
            p_178584_6_.func_178494_a(p_178584_5_.getVertexState(p_178584_2_, p_178584_3_, p_178584_4_));
        }
        p_178584_5_.draw();
    }
    
    private void func_178567_n() {
        GlStateManager.pushMatrix();
        GlStateManager.loadIdentity();
        final float var1 = 1.000001f;
        GlStateManager.translate(-8.0f, -8.0f, -8.0f);
        GlStateManager.scale(var1, var1, var1);
        GlStateManager.translate(8.0f, 8.0f, 8.0f);
        GlStateManager.getFloat(2982, this.field_178597_k);
        GlStateManager.popMatrix();
    }
    
    public void func_178572_f() {
        GlStateManager.multMatrix(this.field_178597_k);
    }
    
    public CompiledChunk func_178571_g() {
        return this.field_178590_b;
    }
    
    public void func_178580_a(final CompiledChunk p_178580_1_) {
        this.field_178598_h.lock();
        try {
            this.field_178590_b = p_178580_1_;
        }
        finally {
            this.field_178598_h.unlock();
        }
    }
    
    public void func_178585_h() {
        this.func_178578_b();
        this.field_178590_b = CompiledChunk.field_178502_a;
    }
    
    public void func_178566_a() {
        this.func_178585_h();
        this.field_178588_d = null;
        for (int var1 = 0; var1 < EnumWorldBlockLayer.values().length; ++var1) {
            if (this.field_178594_l[var1] != null) {
                this.field_178594_l[var1].func_177362_c();
            }
        }
    }
    
    public BlockPos func_178568_j() {
        return this.field_178586_f;
    }
    
    public boolean func_178583_l() {
        this.field_178587_g.lock();
        boolean var1;
        try {
            var1 = (this.field_178599_i == null || this.field_178599_i.func_178546_a() == ChunkCompileTaskGenerator.Status.PENDING);
        }
        finally {
            this.field_178587_g.unlock();
        }
        return var1;
    }
    
    public void func_178575_a(final boolean p_178575_1_) {
        this.field_178593_n = p_178575_1_;
    }
    
    public boolean func_178569_m() {
        return this.field_178593_n;
    }
    
    public BlockPos getPositionOffset16(final EnumFacing facing) {
        final int index = facing.getIndex();
        BlockPos posOffset = this.positionOffsets16[index];
        if (posOffset == null) {
            posOffset = this.func_178568_j().offset(facing, 16);
            this.positionOffsets16[index] = posOffset;
        }
        return posOffset;
    }
}
