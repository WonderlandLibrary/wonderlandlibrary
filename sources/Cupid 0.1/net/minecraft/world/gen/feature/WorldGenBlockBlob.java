package net.minecraft.world.gen.feature;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

public class WorldGenBlockBlob extends WorldGenerator {
  private final Block field_150545_a;
  
  private final int field_150544_b;
  
  public WorldGenBlockBlob(Block p_i45450_1_, int p_i45450_2_) {
    super(false);
    this.field_150545_a = p_i45450_1_;
    this.field_150544_b = p_i45450_2_;
  }
  
  public boolean generate(World worldIn, Random rand, BlockPos position) {
    // Byte code:
    //   0: aload_3
    //   1: invokevirtual getY : ()I
    //   4: iconst_3
    //   5: if_icmple -> 64
    //   8: aload_1
    //   9: aload_3
    //   10: invokevirtual down : ()Lnet/minecraft/util/BlockPos;
    //   13: invokevirtual isAirBlock : (Lnet/minecraft/util/BlockPos;)Z
    //   16: ifeq -> 22
    //   19: goto -> 282
    //   22: aload_1
    //   23: aload_3
    //   24: invokevirtual down : ()Lnet/minecraft/util/BlockPos;
    //   27: invokevirtual getBlockState : (Lnet/minecraft/util/BlockPos;)Lnet/minecraft/block/state/IBlockState;
    //   30: invokeinterface getBlock : ()Lnet/minecraft/block/Block;
    //   35: astore #4
    //   37: aload #4
    //   39: getstatic net/minecraft/init/Blocks.grass : Lnet/minecraft/block/BlockGrass;
    //   42: if_acmpeq -> 64
    //   45: aload #4
    //   47: getstatic net/minecraft/init/Blocks.dirt : Lnet/minecraft/block/Block;
    //   50: if_acmpeq -> 64
    //   53: aload #4
    //   55: getstatic net/minecraft/init/Blocks.stone : Lnet/minecraft/block/Block;
    //   58: if_acmpeq -> 64
    //   61: goto -> 282
    //   64: aload_3
    //   65: invokevirtual getY : ()I
    //   68: iconst_3
    //   69: if_icmpgt -> 74
    //   72: iconst_0
    //   73: ireturn
    //   74: aload_0
    //   75: getfield field_150544_b : I
    //   78: istore #4
    //   80: iconst_0
    //   81: istore #5
    //   83: iload #4
    //   85: iflt -> 280
    //   88: iload #5
    //   90: iconst_3
    //   91: if_icmpge -> 280
    //   94: iload #4
    //   96: aload_2
    //   97: iconst_2
    //   98: invokevirtual nextInt : (I)I
    //   101: iadd
    //   102: istore #6
    //   104: iload #4
    //   106: aload_2
    //   107: iconst_2
    //   108: invokevirtual nextInt : (I)I
    //   111: iadd
    //   112: istore #7
    //   114: iload #4
    //   116: aload_2
    //   117: iconst_2
    //   118: invokevirtual nextInt : (I)I
    //   121: iadd
    //   122: istore #8
    //   124: iload #6
    //   126: iload #7
    //   128: iadd
    //   129: iload #8
    //   131: iadd
    //   132: i2f
    //   133: ldc 0.333
    //   135: fmul
    //   136: ldc 0.5
    //   138: fadd
    //   139: fstore #9
    //   141: aload_3
    //   142: iload #6
    //   144: ineg
    //   145: iload #7
    //   147: ineg
    //   148: iload #8
    //   150: ineg
    //   151: invokevirtual add : (III)Lnet/minecraft/util/BlockPos;
    //   154: aload_3
    //   155: iload #6
    //   157: iload #7
    //   159: iload #8
    //   161: invokevirtual add : (III)Lnet/minecraft/util/BlockPos;
    //   164: invokestatic getAllInBox : (Lnet/minecraft/util/BlockPos;Lnet/minecraft/util/BlockPos;)Ljava/lang/Iterable;
    //   167: invokeinterface iterator : ()Ljava/util/Iterator;
    //   172: astore #10
    //   174: aload #10
    //   176: invokeinterface hasNext : ()Z
    //   181: ifeq -> 230
    //   184: aload #10
    //   186: invokeinterface next : ()Ljava/lang/Object;
    //   191: checkcast net/minecraft/util/BlockPos
    //   194: astore #11
    //   196: aload #11
    //   198: aload_3
    //   199: invokevirtual distanceSq : (Lnet/minecraft/util/Vec3i;)D
    //   202: fload #9
    //   204: fload #9
    //   206: fmul
    //   207: f2d
    //   208: dcmpg
    //   209: ifgt -> 227
    //   212: aload_1
    //   213: aload #11
    //   215: aload_0
    //   216: getfield field_150545_a : Lnet/minecraft/block/Block;
    //   219: invokevirtual getDefaultState : ()Lnet/minecraft/block/state/IBlockState;
    //   222: iconst_4
    //   223: invokevirtual setBlockState : (Lnet/minecraft/util/BlockPos;Lnet/minecraft/block/state/IBlockState;I)Z
    //   226: pop
    //   227: goto -> 174
    //   230: aload_3
    //   231: iload #4
    //   233: iconst_1
    //   234: iadd
    //   235: ineg
    //   236: aload_2
    //   237: iconst_2
    //   238: iload #4
    //   240: iconst_2
    //   241: imul
    //   242: iadd
    //   243: invokevirtual nextInt : (I)I
    //   246: iadd
    //   247: iconst_0
    //   248: aload_2
    //   249: iconst_2
    //   250: invokevirtual nextInt : (I)I
    //   253: isub
    //   254: iload #4
    //   256: iconst_1
    //   257: iadd
    //   258: ineg
    //   259: aload_2
    //   260: iconst_2
    //   261: iload #4
    //   263: iconst_2
    //   264: imul
    //   265: iadd
    //   266: invokevirtual nextInt : (I)I
    //   269: iadd
    //   270: invokevirtual add : (III)Lnet/minecraft/util/BlockPos;
    //   273: astore_3
    //   274: iinc #5, 1
    //   277: goto -> 83
    //   280: iconst_1
    //   281: ireturn
    //   282: aload_3
    //   283: invokevirtual down : ()Lnet/minecraft/util/BlockPos;
    //   286: astore_3
    //   287: goto -> 0
    // Line number table:
    //   Java source line number -> byte code offset
    //   #27	-> 0
    //   #29	-> 8
    //   #31	-> 19
    //   #34	-> 22
    //   #36	-> 37
    //   #38	-> 61
    //   #42	-> 64
    //   #44	-> 72
    //   #47	-> 74
    //   #49	-> 80
    //   #51	-> 94
    //   #52	-> 104
    //   #53	-> 114
    //   #54	-> 124
    //   #56	-> 141
    //   #58	-> 196
    //   #60	-> 212
    //   #62	-> 227
    //   #64	-> 230
    //   #49	-> 274
    //   #67	-> 280
    //   #69	-> 282
    // Local variable table:
    //   start	length	slot	name	descriptor
    //   37	27	4	block	Lnet/minecraft/block/Block;
    //   196	31	11	blockpos	Lnet/minecraft/util/BlockPos;
    //   104	170	6	j	I
    //   114	160	7	k	I
    //   124	150	8	l	I
    //   141	133	9	f	F
    //   83	197	5	i	I
    //   80	202	4	i1	I
    //   0	290	0	this	Lnet/minecraft/world/gen/feature/WorldGenBlockBlob;
    //   0	290	1	worldIn	Lnet/minecraft/world/World;
    //   0	290	2	rand	Ljava/util/Random;
    //   0	290	3	position	Lnet/minecraft/util/BlockPos;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\minecraft\world\gen\feature\WorldGenBlockBlob.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */