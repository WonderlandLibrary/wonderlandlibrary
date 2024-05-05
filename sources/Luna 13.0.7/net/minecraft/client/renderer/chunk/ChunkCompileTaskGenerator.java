package net.minecraft.client.renderer.chunk;

import com.google.common.collect.Lists;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;
import net.minecraft.client.renderer.RegionRenderCacheBuilder;

public class ChunkCompileTaskGenerator
{
  private final RenderChunk field_178553_a;
  private final ReentrantLock field_178551_b = new ReentrantLock();
  private final List field_178552_c = Lists.newArrayList();
  private final Type field_178549_d;
  private RegionRenderCacheBuilder field_178550_e;
  private CompiledChunk field_178547_f;
  private Status field_178548_g;
  private boolean field_178554_h;
  private static final String __OBFID = "CL_00002466";
  
  public ChunkCompileTaskGenerator(RenderChunk p_i46208_1_, Type p_i46208_2_)
  {
    this.field_178548_g = Status.PENDING;
    this.field_178553_a = p_i46208_1_;
    this.field_178549_d = p_i46208_2_;
  }
  
  public Status func_178546_a()
  {
    return this.field_178548_g;
  }
  
  public RenderChunk func_178536_b()
  {
    return this.field_178553_a;
  }
  
  public CompiledChunk func_178544_c()
  {
    return this.field_178547_f;
  }
  
  public void func_178543_a(CompiledChunk p_178543_1_)
  {
    this.field_178547_f = p_178543_1_;
  }
  
  public RegionRenderCacheBuilder func_178545_d()
  {
    return this.field_178550_e;
  }
  
  public void func_178541_a(RegionRenderCacheBuilder p_178541_1_)
  {
    this.field_178550_e = p_178541_1_;
  }
  
  /* Error */
  public void func_178535_a(Status p_178535_1_)
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 4	net/minecraft/client/renderer/chunk/ChunkCompileTaskGenerator:field_178551_b	Ljava/util/concurrent/locks/ReentrantLock;
    //   4: invokevirtual 13	java/util/concurrent/locks/ReentrantLock:lock	()V
    //   7: aload_0
    //   8: aload_1
    //   9: putfield 8	net/minecraft/client/renderer/chunk/ChunkCompileTaskGenerator:field_178548_g	Lnet/minecraft/client/renderer/chunk/ChunkCompileTaskGenerator$Status;
    //   12: aload_0
    //   13: getfield 4	net/minecraft/client/renderer/chunk/ChunkCompileTaskGenerator:field_178551_b	Ljava/util/concurrent/locks/ReentrantLock;
    //   16: invokevirtual 14	java/util/concurrent/locks/ReentrantLock:unlock	()V
    //   19: goto +13 -> 32
    //   22: astore_2
    //   23: aload_0
    //   24: getfield 4	net/minecraft/client/renderer/chunk/ChunkCompileTaskGenerator:field_178551_b	Ljava/util/concurrent/locks/ReentrantLock;
    //   27: invokevirtual 14	java/util/concurrent/locks/ReentrantLock:unlock	()V
    //   30: aload_2
    //   31: athrow
    //   32: return
    // Line number table:
    //   Java source line #60	-> byte code offset #0
    //   Java source line #64	-> byte code offset #7
    //   Java source line #68	-> byte code offset #12
    //   Java source line #69	-> byte code offset #19
    //   Java source line #68	-> byte code offset #22
    //   Java source line #69	-> byte code offset #30
    //   Java source line #70	-> byte code offset #32
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	33	0	this	ChunkCompileTaskGenerator
    //   0	33	1	p_178535_1_	Status
    //   22	9	2	localObject	Object
    // Exception table:
    //   from	to	target	type
    //   7	12	22	finally
  }
  
  /* Error */
  public void func_178542_e()
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 4	net/minecraft/client/renderer/chunk/ChunkCompileTaskGenerator:field_178551_b	Ljava/util/concurrent/locks/ReentrantLock;
    //   4: invokevirtual 13	java/util/concurrent/locks/ReentrantLock:lock	()V
    //   7: aload_0
    //   8: getfield 10	net/minecraft/client/renderer/chunk/ChunkCompileTaskGenerator:field_178549_d	Lnet/minecraft/client/renderer/chunk/ChunkCompileTaskGenerator$Type;
    //   11: getstatic 15	net/minecraft/client/renderer/chunk/ChunkCompileTaskGenerator$Type:REBUILD_CHUNK	Lnet/minecraft/client/renderer/chunk/ChunkCompileTaskGenerator$Type;
    //   14: if_acmpne +21 -> 35
    //   17: aload_0
    //   18: getfield 8	net/minecraft/client/renderer/chunk/ChunkCompileTaskGenerator:field_178548_g	Lnet/minecraft/client/renderer/chunk/ChunkCompileTaskGenerator$Status;
    //   21: getstatic 16	net/minecraft/client/renderer/chunk/ChunkCompileTaskGenerator$Status:DONE	Lnet/minecraft/client/renderer/chunk/ChunkCompileTaskGenerator$Status;
    //   24: if_acmpeq +11 -> 35
    //   27: aload_0
    //   28: getfield 9	net/minecraft/client/renderer/chunk/ChunkCompileTaskGenerator:field_178553_a	Lnet/minecraft/client/renderer/chunk/RenderChunk;
    //   31: iconst_1
    //   32: invokevirtual 17	net/minecraft/client/renderer/chunk/RenderChunk:func_178575_a	(Z)V
    //   35: aload_0
    //   36: iconst_1
    //   37: putfield 18	net/minecraft/client/renderer/chunk/ChunkCompileTaskGenerator:field_178554_h	Z
    //   40: aload_0
    //   41: getstatic 16	net/minecraft/client/renderer/chunk/ChunkCompileTaskGenerator$Status:DONE	Lnet/minecraft/client/renderer/chunk/ChunkCompileTaskGenerator$Status;
    //   44: putfield 8	net/minecraft/client/renderer/chunk/ChunkCompileTaskGenerator:field_178548_g	Lnet/minecraft/client/renderer/chunk/ChunkCompileTaskGenerator$Status;
    //   47: aload_0
    //   48: getfield 6	net/minecraft/client/renderer/chunk/ChunkCompileTaskGenerator:field_178552_c	Ljava/util/List;
    //   51: invokeinterface 19 1 0
    //   56: astore_1
    //   57: aload_1
    //   58: invokeinterface 20 1 0
    //   63: ifeq +22 -> 85
    //   66: aload_1
    //   67: invokeinterface 21 1 0
    //   72: checkcast 22	java/lang/Runnable
    //   75: astore_2
    //   76: aload_2
    //   77: invokeinterface 23 1 0
    //   82: goto -25 -> 57
    //   85: aload_0
    //   86: getfield 4	net/minecraft/client/renderer/chunk/ChunkCompileTaskGenerator:field_178551_b	Ljava/util/concurrent/locks/ReentrantLock;
    //   89: invokevirtual 14	java/util/concurrent/locks/ReentrantLock:unlock	()V
    //   92: goto +13 -> 105
    //   95: astore_3
    //   96: aload_0
    //   97: getfield 4	net/minecraft/client/renderer/chunk/ChunkCompileTaskGenerator:field_178551_b	Ljava/util/concurrent/locks/ReentrantLock;
    //   100: invokevirtual 14	java/util/concurrent/locks/ReentrantLock:unlock	()V
    //   103: aload_3
    //   104: athrow
    //   105: return
    // Line number table:
    //   Java source line #74	-> byte code offset #0
    //   Java source line #78	-> byte code offset #7
    //   Java source line #80	-> byte code offset #27
    //   Java source line #83	-> byte code offset #35
    //   Java source line #84	-> byte code offset #40
    //   Java source line #85	-> byte code offset #47
    //   Java source line #87	-> byte code offset #57
    //   Java source line #89	-> byte code offset #66
    //   Java source line #90	-> byte code offset #76
    //   Java source line #91	-> byte code offset #82
    //   Java source line #95	-> byte code offset #85
    //   Java source line #96	-> byte code offset #92
    //   Java source line #95	-> byte code offset #95
    //   Java source line #96	-> byte code offset #103
    //   Java source line #97	-> byte code offset #105
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	106	0	this	ChunkCompileTaskGenerator
    //   56	11	1	var1	java.util.Iterator
    //   75	2	2	var2	Runnable
    //   95	9	3	localObject	Object
    // Exception table:
    //   from	to	target	type
    //   7	85	95	finally
  }
  
  /* Error */
  public void func_178539_a(Runnable p_178539_1_)
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 4	net/minecraft/client/renderer/chunk/ChunkCompileTaskGenerator:field_178551_b	Ljava/util/concurrent/locks/ReentrantLock;
    //   4: invokevirtual 13	java/util/concurrent/locks/ReentrantLock:lock	()V
    //   7: aload_0
    //   8: getfield 6	net/minecraft/client/renderer/chunk/ChunkCompileTaskGenerator:field_178552_c	Ljava/util/List;
    //   11: aload_1
    //   12: invokeinterface 24 2 0
    //   17: pop
    //   18: aload_0
    //   19: getfield 18	net/minecraft/client/renderer/chunk/ChunkCompileTaskGenerator:field_178554_h	Z
    //   22: ifeq +9 -> 31
    //   25: aload_1
    //   26: invokeinterface 23 1 0
    //   31: aload_0
    //   32: getfield 4	net/minecraft/client/renderer/chunk/ChunkCompileTaskGenerator:field_178551_b	Ljava/util/concurrent/locks/ReentrantLock;
    //   35: invokevirtual 14	java/util/concurrent/locks/ReentrantLock:unlock	()V
    //   38: goto +13 -> 51
    //   41: astore_2
    //   42: aload_0
    //   43: getfield 4	net/minecraft/client/renderer/chunk/ChunkCompileTaskGenerator:field_178551_b	Ljava/util/concurrent/locks/ReentrantLock;
    //   46: invokevirtual 14	java/util/concurrent/locks/ReentrantLock:unlock	()V
    //   49: aload_2
    //   50: athrow
    //   51: return
    // Line number table:
    //   Java source line #101	-> byte code offset #0
    //   Java source line #105	-> byte code offset #7
    //   Java source line #107	-> byte code offset #18
    //   Java source line #109	-> byte code offset #25
    //   Java source line #114	-> byte code offset #31
    //   Java source line #115	-> byte code offset #38
    //   Java source line #114	-> byte code offset #41
    //   Java source line #115	-> byte code offset #49
    //   Java source line #116	-> byte code offset #51
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	52	0	this	ChunkCompileTaskGenerator
    //   0	52	1	p_178539_1_	Runnable
    //   41	9	2	localObject	Object
    // Exception table:
    //   from	to	target	type
    //   7	31	41	finally
  }
  
  public ReentrantLock func_178540_f()
  {
    return this.field_178551_b;
  }
  
  public Type func_178538_g()
  {
    return this.field_178549_d;
  }
  
  public boolean func_178537_h()
  {
    return this.field_178554_h;
  }
  
  public static enum Status
  {
    private static final Status[] $VALUES = { PENDING, COMPILING, UPLOADING, DONE };
    private static final String __OBFID = "CL_00002465";
    
    private Status(String p_i46385_1_, int p_i46385_2_, String p_i46207_1_, int p_i46207_2_) {}
  }
  
  public static enum Type
  {
    private static final Type[] $VALUES = { REBUILD_CHUNK, RESORT_TRANSPARENCY };
    private static final String __OBFID = "CL_00002464";
    
    private Type(String p_i46386_1_, int p_i46386_2_, String p_i46206_1_, int p_i46206_2_) {}
  }
}
