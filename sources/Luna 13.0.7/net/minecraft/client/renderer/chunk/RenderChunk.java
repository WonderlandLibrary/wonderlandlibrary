package net.minecraft.client.renderer.chunk;

import java.nio.FloatBuffer;
import java.util.Iterator;
import java.util.concurrent.locks.ReentrantLock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockCactus;
import net.minecraft.block.BlockRedstoneWire;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RegionRenderCache;
import net.minecraft.client.renderer.RegionRenderCacheBuilder;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.world.World;
import optifine.BlockPosM;
import optifine.Config;
import optifine.Reflector;
import optifine.ReflectorClass;
import optifine.ReflectorForge;
import optifine.ReflectorMethod;
import shadersmod.client.SVertexBuilder;

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
  private static EnumWorldBlockLayer[] ENUM_WORLD_BLOCK_LAYERS = ;
  private EnumWorldBlockLayer[] blockLayersSingle;
  private boolean isMipmaps;
  private boolean fixBlockLayer;
  private boolean playerUpdate;
  
  public RenderChunk(World worldIn, RenderGlobal renderGlobalIn, BlockPos blockPosIn, int indexIn)
  {
    this.positionOffsets16 = new BlockPos[EnumFacing.VALUES.length];
    this.blockLayersSingle = new EnumWorldBlockLayer[1];
    this.isMipmaps = Config.isMipmaps();
    this.fixBlockLayer = (!Reflector.BetterFoliageClient.exists());
    this.playerUpdate = false;
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
    if (!blockPosIn.equals(func_178568_j())) {
      func_178576_a(blockPosIn);
    }
    if (OpenGlHelper.func_176075_f()) {
      for (int var5 = 0; var5 < EnumWorldBlockLayer.values().length; var5++) {
        this.field_178594_l[var5] = new VertexBuffer(DefaultVertexFormats.field_176600_a);
      }
    }
  }
  
  public boolean func_178577_a(int frameIndexIn)
  {
    if (this.field_178595_m == frameIndexIn) {
      return false;
    }
    this.field_178595_m = frameIndexIn;
    return true;
  }
  
  public VertexBuffer func_178565_b(int p_178565_1_)
  {
    return this.field_178594_l[p_178565_1_];
  }
  
  public void func_178576_a(BlockPos p_178576_1_)
  {
    func_178585_h();
    this.field_178586_f = p_178576_1_;
    this.field_178591_c = new AxisAlignedBB(p_178576_1_, p_178576_1_.add(16, 16, 16));
    func_178567_n();
    for (int i = 0; i < this.positionOffsets16.length; i++) {
      this.positionOffsets16[i] = null;
    }
  }
  
  public void func_178570_a(float p_178570_1_, float p_178570_2_, float p_178570_3_, ChunkCompileTaskGenerator p_178570_4_)
  {
    CompiledChunk var5 = p_178570_4_.func_178544_c();
    if ((var5.func_178487_c() != null) && (!var5.func_178491_b(EnumWorldBlockLayer.TRANSLUCENT)))
    {
      WorldRenderer worldRenderer = p_178570_4_.func_178545_d().func_179038_a(EnumWorldBlockLayer.TRANSLUCENT);
      func_178573_a(worldRenderer, this.field_178586_f);
      worldRenderer.setVertexState(var5.func_178487_c());
      func_178584_a(EnumWorldBlockLayer.TRANSLUCENT, p_178570_1_, p_178570_2_, p_178570_3_, worldRenderer, var5);
    }
  }
  
  public void func_178581_b(float p_178581_1_, float p_178581_2_, float p_178581_3_, ChunkCompileTaskGenerator p_178581_4_)
  {
    CompiledChunk var5 = new CompiledChunk();
    boolean var6 = true;
    BlockPos var7 = this.field_178586_f;
    BlockPos var8 = var7.add(15, 15, 15);
    p_178581_4_.func_178540_f().lock();
    try
    {
      if (p_178581_4_.func_178546_a() != ChunkCompileTaskGenerator.Status.COMPILING) {
        return;
      }
      if (this.field_178588_d != null)
      {
        RegionRenderCache var9 = createRegionRenderCache(this.field_178588_d, var7.add(-1, -1, -1), var8.add(1, 1, 1), 1);
        if (Reflector.MinecraftForgeClient_onRebuildChunk.exists()) {
          Reflector.call(Reflector.MinecraftForgeClient_onRebuildChunk, new Object[] { this.field_178588_d, this.field_178586_f, var9 });
        }
        p_178581_4_.func_178543_a(var5);
        
        p_178581_4_.func_178540_f().unlock();
        break label175;
      }
    }
    finally
    {
      p_178581_4_.func_178540_f().unlock();
    }
    return;
    label175:
    RegionRenderCache var9;
    VisGraph var10 = new VisGraph();
    if (!var9.extendedLevelsInChunkCache())
    {
      field_178592_a += 1;
      Iterator var11 = BlockPosM.getAllInBoxMutable(var7, var8).iterator();
      boolean forgeHasTileEntityExists = Reflector.ForgeBlock_hasTileEntity.exists();
      boolean forgeBlockCanRenderInLayerExists = Reflector.ForgeBlock_canRenderInLayer.exists();
      boolean forgeHooksSetRenderLayerExists = Reflector.ForgeHooksClient_setRenderLayer.exists();
      while (var11.hasNext())
      {
        BlockPosM var20 = (BlockPosM)var11.next();
        IBlockState var21 = var9.getBlockState(var20);
        Block var22 = var21.getBlock();
        if (var22.isOpaqueCube()) {
          var10.func_178606_a(var20);
        }
        if (ReflectorForge.blockHasTileEntity(var21))
        {
          TileEntity var23 = var9.getTileEntity(new BlockPos(var20));
          if ((var23 != null) && (TileEntityRendererDispatcher.instance.hasSpecialRenderer(var23))) {
            var5.func_178490_a(var23);
          }
        }
        EnumWorldBlockLayer[] var28;
        EnumWorldBlockLayer[] var28;
        if (forgeBlockCanRenderInLayerExists)
        {
          var28 = ENUM_WORLD_BLOCK_LAYERS;
        }
        else
        {
          var28 = this.blockLayersSingle;
          var28[0] = var22.getBlockLayer();
        }
        for (int i = 0; i < var28.length; i++)
        {
          EnumWorldBlockLayer var24 = var28[i];
          if (forgeBlockCanRenderInLayerExists)
          {
            boolean var16 = Reflector.callBoolean(var22, Reflector.ForgeBlock_canRenderInLayer, new Object[] { var24 });
            if (!var16) {}
          }
          else
          {
            if (forgeHooksSetRenderLayerExists) {
              Reflector.callVoid(Reflector.ForgeHooksClient_setRenderLayer, new Object[] { var24 });
            }
            if (this.fixBlockLayer) {
              var24 = fixBlockLayer(var22, var24);
            }
            int var30 = var24.ordinal();
            if (var22.getRenderType() != -1)
            {
              WorldRenderer var17 = p_178581_4_.func_178545_d().func_179039_a(var30);
              var17.setBlockLayer(var24);
              if (!var5.func_178492_d(var24))
              {
                var5.func_178493_c(var24);
                func_178573_a(var17, var7);
              }
              if (Minecraft.getMinecraft().getBlockRendererDispatcher().func_175018_a(var21, var20, var9, var17)) {
                var5.func_178486_a(var24);
              }
            }
          }
        }
      }
      EnumWorldBlockLayer[] var25 = ENUM_WORLD_BLOCK_LAYERS;
      int var26 = var25.length;
      for (int var27 = 0; var27 < var26; var27++)
      {
        EnumWorldBlockLayer var29 = var25[var27];
        if (var5.func_178492_d(var29))
        {
          if (Config.isShaders()) {
            SVertexBuilder.calcNormalChunkLayer(p_178581_4_.func_178545_d().func_179038_a(var29));
          }
          func_178584_a(var29, p_178581_1_, p_178581_2_, p_178581_3_, p_178581_4_.func_178545_d().func_179038_a(var29), var5);
        }
      }
    }
    var5.func_178488_a(var10.func_178607_a());
  }
  
  /* Error */
  protected void func_178578_b()
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 17	net/minecraft/client/renderer/chunk/RenderChunk:field_178587_g	Ljava/util/concurrent/locks/ReentrantLock;
    //   4: invokevirtual 55	java/util/concurrent/locks/ReentrantLock:lock	()V
    //   7: aload_0
    //   8: getfield 19	net/minecraft/client/renderer/chunk/RenderChunk:field_178599_i	Lnet/minecraft/client/renderer/chunk/ChunkCompileTaskGenerator;
    //   11: ifnull +28 -> 39
    //   14: aload_0
    //   15: getfield 19	net/minecraft/client/renderer/chunk/RenderChunk:field_178599_i	Lnet/minecraft/client/renderer/chunk/ChunkCompileTaskGenerator;
    //   18: invokevirtual 56	net/minecraft/client/renderer/chunk/ChunkCompileTaskGenerator:func_178546_a	()Lnet/minecraft/client/renderer/chunk/ChunkCompileTaskGenerator$Status;
    //   21: getstatic 106	net/minecraft/client/renderer/chunk/ChunkCompileTaskGenerator$Status:DONE	Lnet/minecraft/client/renderer/chunk/ChunkCompileTaskGenerator$Status;
    //   24: if_acmpeq +15 -> 39
    //   27: aload_0
    //   28: getfield 19	net/minecraft/client/renderer/chunk/RenderChunk:field_178599_i	Lnet/minecraft/client/renderer/chunk/ChunkCompileTaskGenerator;
    //   31: invokevirtual 107	net/minecraft/client/renderer/chunk/ChunkCompileTaskGenerator:func_178542_e	()V
    //   34: aload_0
    //   35: aconst_null
    //   36: putfield 19	net/minecraft/client/renderer/chunk/RenderChunk:field_178599_i	Lnet/minecraft/client/renderer/chunk/ChunkCompileTaskGenerator;
    //   39: aload_0
    //   40: getfield 17	net/minecraft/client/renderer/chunk/RenderChunk:field_178587_g	Ljava/util/concurrent/locks/ReentrantLock;
    //   43: invokevirtual 58	java/util/concurrent/locks/ReentrantLock:unlock	()V
    //   46: goto +13 -> 59
    //   49: astore_1
    //   50: aload_0
    //   51: getfield 17	net/minecraft/client/renderer/chunk/RenderChunk:field_178587_g	Ljava/util/concurrent/locks/ReentrantLock;
    //   54: invokevirtual 58	java/util/concurrent/locks/ReentrantLock:unlock	()V
    //   57: aload_1
    //   58: athrow
    //   59: return
    // Line number table:
    //   Java source line #283	-> byte code offset #0
    //   Java source line #287	-> byte code offset #7
    //   Java source line #289	-> byte code offset #27
    //   Java source line #290	-> byte code offset #34
    //   Java source line #295	-> byte code offset #39
    //   Java source line #296	-> byte code offset #46
    //   Java source line #295	-> byte code offset #49
    //   Java source line #296	-> byte code offset #57
    //   Java source line #297	-> byte code offset #59
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	60	0	this	RenderChunk
    //   49	9	1	localObject	Object
    // Exception table:
    //   from	to	target	type
    //   7	39	49	finally
  }
  
  public ReentrantLock func_178579_c()
  {
    return this.field_178587_g;
  }
  
  /* Error */
  public ChunkCompileTaskGenerator func_178574_d()
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 17	net/minecraft/client/renderer/chunk/RenderChunk:field_178587_g	Ljava/util/concurrent/locks/ReentrantLock;
    //   4: invokevirtual 55	java/util/concurrent/locks/ReentrantLock:lock	()V
    //   7: aload_0
    //   8: invokevirtual 108	net/minecraft/client/renderer/chunk/RenderChunk:func_178578_b	()V
    //   11: aload_0
    //   12: new 109	net/minecraft/client/renderer/chunk/ChunkCompileTaskGenerator
    //   15: dup
    //   16: aload_0
    //   17: getstatic 110	net/minecraft/client/renderer/chunk/ChunkCompileTaskGenerator$Type:REBUILD_CHUNK	Lnet/minecraft/client/renderer/chunk/ChunkCompileTaskGenerator$Type;
    //   20: invokespecial 111	net/minecraft/client/renderer/chunk/ChunkCompileTaskGenerator:<init>	(Lnet/minecraft/client/renderer/chunk/RenderChunk;Lnet/minecraft/client/renderer/chunk/ChunkCompileTaskGenerator$Type;)V
    //   23: putfield 19	net/minecraft/client/renderer/chunk/RenderChunk:field_178599_i	Lnet/minecraft/client/renderer/chunk/ChunkCompileTaskGenerator;
    //   26: aload_0
    //   27: getfield 19	net/minecraft/client/renderer/chunk/RenderChunk:field_178599_i	Lnet/minecraft/client/renderer/chunk/ChunkCompileTaskGenerator;
    //   30: astore_1
    //   31: aload_0
    //   32: getfield 17	net/minecraft/client/renderer/chunk/RenderChunk:field_178587_g	Ljava/util/concurrent/locks/ReentrantLock;
    //   35: invokevirtual 58	java/util/concurrent/locks/ReentrantLock:unlock	()V
    //   38: goto +13 -> 51
    //   41: astore_2
    //   42: aload_0
    //   43: getfield 17	net/minecraft/client/renderer/chunk/RenderChunk:field_178587_g	Ljava/util/concurrent/locks/ReentrantLock;
    //   46: invokevirtual 58	java/util/concurrent/locks/ReentrantLock:unlock	()V
    //   49: aload_2
    //   50: athrow
    //   51: aload_1
    //   52: areturn
    // Line number table:
    //   Java source line #306	-> byte code offset #0
    //   Java source line #311	-> byte code offset #7
    //   Java source line #312	-> byte code offset #11
    //   Java source line #313	-> byte code offset #26
    //   Java source line #317	-> byte code offset #31
    //   Java source line #318	-> byte code offset #38
    //   Java source line #317	-> byte code offset #41
    //   Java source line #318	-> byte code offset #49
    //   Java source line #320	-> byte code offset #51
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	53	0	this	RenderChunk
    //   30	2	1	var1	ChunkCompileTaskGenerator
    //   51	1	1	var1	ChunkCompileTaskGenerator
    //   41	9	2	localObject	Object
    // Exception table:
    //   from	to	target	type
    //   7	31	41	finally
  }
  
  public ChunkCompileTaskGenerator func_178582_e()
  {
    this.field_178587_g.lock();
    try
    {
      if ((this.field_178599_i != null) && (this.field_178599_i.func_178546_a() == ChunkCompileTaskGenerator.Status.PENDING))
      {
        ChunkCompileTaskGenerator var1 = null;
        return var1;
      }
      if ((this.field_178599_i != null) && (this.field_178599_i.func_178546_a() != ChunkCompileTaskGenerator.Status.DONE))
      {
        this.field_178599_i.func_178542_e();
        this.field_178599_i = null;
      }
      this.field_178599_i = new ChunkCompileTaskGenerator(this, ChunkCompileTaskGenerator.Type.RESORT_TRANSPARENCY);
      this.field_178599_i.func_178543_a(this.field_178590_b);
      ChunkCompileTaskGenerator var1 = this.field_178599_i;
      var2 = var1;
    }
    finally
    {
      ChunkCompileTaskGenerator var2;
      this.field_178587_g.unlock();
    }
    ChunkCompileTaskGenerator var2;
    return var2;
  }
  
  private void func_178573_a(WorldRenderer p_178573_1_, BlockPos p_178573_2_)
  {
    p_178573_1_.startDrawing(7);
    p_178573_1_.setVertexFormat(DefaultVertexFormats.field_176600_a);
    p_178573_1_.setTranslation(-p_178573_2_.getX(), -p_178573_2_.getY(), -p_178573_2_.getZ());
  }
  
  private void func_178584_a(EnumWorldBlockLayer p_178584_1_, float p_178584_2_, float p_178584_3_, float p_178584_4_, WorldRenderer p_178584_5_, CompiledChunk p_178584_6_)
  {
    if ((p_178584_1_ == EnumWorldBlockLayer.TRANSLUCENT) && (!p_178584_6_.func_178491_b(p_178584_1_))) {
      p_178584_6_.func_178494_a(p_178584_5_.getVertexState(p_178584_2_, p_178584_3_, p_178584_4_));
    }
    p_178584_5_.draw();
  }
  
  private void func_178567_n()
  {
    GlStateManager.pushMatrix();
    GlStateManager.loadIdentity();
    float var1 = 1.000001F;
    GlStateManager.translate(-8.0F, -8.0F, -8.0F);
    GlStateManager.scale(var1, var1, var1);
    GlStateManager.translate(8.0F, 8.0F, 8.0F);
    GlStateManager.getFloat(2982, this.field_178597_k);
    GlStateManager.popMatrix();
  }
  
  public void func_178572_f()
  {
    GlStateManager.multMatrix(this.field_178597_k);
  }
  
  public CompiledChunk func_178571_g()
  {
    return this.field_178590_b;
  }
  
  /* Error */
  public void func_178580_a(CompiledChunk p_178580_1_)
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 18	net/minecraft/client/renderer/chunk/RenderChunk:field_178598_h	Ljava/util/concurrent/locks/ReentrantLock;
    //   4: invokevirtual 55	java/util/concurrent/locks/ReentrantLock:lock	()V
    //   7: aload_0
    //   8: aload_1
    //   9: putfield 14	net/minecraft/client/renderer/chunk/RenderChunk:field_178590_b	Lnet/minecraft/client/renderer/chunk/CompiledChunk;
    //   12: aload_0
    //   13: getfield 18	net/minecraft/client/renderer/chunk/RenderChunk:field_178598_h	Ljava/util/concurrent/locks/ReentrantLock;
    //   16: invokevirtual 58	java/util/concurrent/locks/ReentrantLock:unlock	()V
    //   19: goto +13 -> 32
    //   22: astore_2
    //   23: aload_0
    //   24: getfield 18	net/minecraft/client/renderer/chunk/RenderChunk:field_178598_h	Ljava/util/concurrent/locks/ReentrantLock;
    //   27: invokevirtual 58	java/util/concurrent/locks/ReentrantLock:unlock	()V
    //   30: aload_2
    //   31: athrow
    //   32: return
    // Line number table:
    //   Java source line #398	-> byte code offset #0
    //   Java source line #402	-> byte code offset #7
    //   Java source line #406	-> byte code offset #12
    //   Java source line #407	-> byte code offset #19
    //   Java source line #406	-> byte code offset #22
    //   Java source line #407	-> byte code offset #30
    //   Java source line #408	-> byte code offset #32
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	33	0	this	RenderChunk
    //   0	33	1	p_178580_1_	CompiledChunk
    //   22	9	2	localObject	Object
    // Exception table:
    //   from	to	target	type
    //   7	12	22	finally
  }
  
  public void func_178585_h()
  {
    func_178578_b();
    this.field_178590_b = CompiledChunk.field_178502_a;
  }
  
  public void func_178566_a()
  {
    func_178585_h();
    this.field_178588_d = null;
    for (int var1 = 0; var1 < EnumWorldBlockLayer.values().length; var1++) {
      if (this.field_178594_l[var1] != null) {
        this.field_178594_l[var1].func_177362_c();
      }
    }
  }
  
  public BlockPos func_178568_j()
  {
    return this.field_178586_f;
  }
  
  /* Error */
  public boolean func_178583_l()
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 17	net/minecraft/client/renderer/chunk/RenderChunk:field_178587_g	Ljava/util/concurrent/locks/ReentrantLock;
    //   4: invokevirtual 55	java/util/concurrent/locks/ReentrantLock:lock	()V
    //   7: aload_0
    //   8: getfield 19	net/minecraft/client/renderer/chunk/RenderChunk:field_178599_i	Lnet/minecraft/client/renderer/chunk/ChunkCompileTaskGenerator;
    //   11: ifnull +16 -> 27
    //   14: aload_0
    //   15: getfield 19	net/minecraft/client/renderer/chunk/RenderChunk:field_178599_i	Lnet/minecraft/client/renderer/chunk/ChunkCompileTaskGenerator;
    //   18: invokevirtual 56	net/minecraft/client/renderer/chunk/ChunkCompileTaskGenerator:func_178546_a	()Lnet/minecraft/client/renderer/chunk/ChunkCompileTaskGenerator$Status;
    //   21: getstatic 112	net/minecraft/client/renderer/chunk/ChunkCompileTaskGenerator$Status:PENDING	Lnet/minecraft/client/renderer/chunk/ChunkCompileTaskGenerator$Status;
    //   24: if_acmpne +7 -> 31
    //   27: iconst_1
    //   28: goto +4 -> 32
    //   31: iconst_0
    //   32: istore_1
    //   33: aload_0
    //   34: getfield 17	net/minecraft/client/renderer/chunk/RenderChunk:field_178587_g	Ljava/util/concurrent/locks/ReentrantLock;
    //   37: invokevirtual 58	java/util/concurrent/locks/ReentrantLock:unlock	()V
    //   40: goto +13 -> 53
    //   43: astore_2
    //   44: aload_0
    //   45: getfield 17	net/minecraft/client/renderer/chunk/RenderChunk:field_178587_g	Ljava/util/concurrent/locks/ReentrantLock;
    //   48: invokevirtual 58	java/util/concurrent/locks/ReentrantLock:unlock	()V
    //   51: aload_2
    //   52: athrow
    //   53: iload_1
    //   54: ireturn
    // Line number table:
    //   Java source line #437	-> byte code offset #0
    //   Java source line #442	-> byte code offset #7
    //   Java source line #446	-> byte code offset #33
    //   Java source line #447	-> byte code offset #40
    //   Java source line #446	-> byte code offset #43
    //   Java source line #447	-> byte code offset #51
    //   Java source line #449	-> byte code offset #53
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	55	0	this	RenderChunk
    //   32	2	1	var1	boolean
    //   53	1	1	var1	boolean
    //   43	9	2	localObject	Object
    // Exception table:
    //   from	to	target	type
    //   7	33	43	finally
  }
  
  public void func_178575_a(boolean p_178575_1_)
  {
    this.field_178593_n = p_178575_1_;
    if (this.field_178593_n)
    {
      if (isWorldPlayerUpdate()) {
        this.playerUpdate = true;
      }
    }
    else {
      this.playerUpdate = false;
    }
  }
  
  public boolean func_178569_m()
  {
    return this.field_178593_n;
  }
  
  public BlockPos getPositionOffset16(EnumFacing facing)
  {
    int index = facing.getIndex();
    BlockPos posOffset = this.positionOffsets16[index];
    if (posOffset == null)
    {
      posOffset = func_178568_j().offset(facing, 16);
      this.positionOffsets16[index] = posOffset;
    }
    return posOffset;
  }
  
  private boolean isWorldPlayerUpdate()
  {
    if ((this.field_178588_d instanceof WorldClient))
    {
      WorldClient worldClient = (WorldClient)this.field_178588_d;
      return worldClient.isPlayerUpdate();
    }
    return false;
  }
  
  public boolean isPlayerUpdate()
  {
    return this.playerUpdate;
  }
  
  protected RegionRenderCache createRegionRenderCache(World world, BlockPos from, BlockPos to, int subtract)
  {
    return new RegionRenderCache(world, from, to, subtract);
  }
  
  private EnumWorldBlockLayer fixBlockLayer(Block block, EnumWorldBlockLayer layer)
  {
    if (this.isMipmaps)
    {
      if (layer == EnumWorldBlockLayer.CUTOUT)
      {
        if ((block instanceof BlockRedstoneWire)) {
          return layer;
        }
        if ((block instanceof BlockCactus)) {
          return layer;
        }
        return EnumWorldBlockLayer.CUTOUT_MIPPED;
      }
    }
    else if (layer == EnumWorldBlockLayer.CUTOUT_MIPPED) {
      return EnumWorldBlockLayer.CUTOUT;
    }
    return layer;
  }
}
