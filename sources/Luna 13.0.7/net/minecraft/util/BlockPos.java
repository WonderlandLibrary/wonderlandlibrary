package net.minecraft.util;

import com.google.common.collect.AbstractIterator;
import java.util.Iterator;
import net.minecraft.entity.Entity;

public class BlockPos
  extends Vec3i
{
  public static final BlockPos ORIGIN = new BlockPos(0, 0, 0);
  private static final int field_177990_b = 1 + MathHelper.calculateLogBaseTwo(MathHelper.roundUpToPowerOfTwo(30000000));
  private static final int field_177991_c = field_177990_b;
  private static final int field_177989_d = 64 - field_177990_b - field_177991_c;
  private static final int field_177987_f = 0 + field_177991_c;
  private static final int field_177988_g = field_177987_f + field_177989_d;
  private static final long field_177994_h = (1L << field_177990_b) - 1L;
  private static final long field_177995_i = (1L << field_177989_d) - 1L;
  private static final long field_177993_j = (1L << field_177991_c) - 1L;
  private static final String __OBFID = "CL_00002334";
  
  public BlockPos(int x, int y, int z)
  {
    super(x, y, z);
  }
  
  public BlockPos(double x, double y, double z)
  {
    super(x, y, z);
  }
  
  public BlockPos(Entity p_i46032_1_)
  {
    this(p_i46032_1_.posX, p_i46032_1_.posY, p_i46032_1_.posZ);
  }
  
  public BlockPos(Vec3 p_i46033_1_)
  {
    this(p_i46033_1_.xCoord, p_i46033_1_.yCoord, p_i46033_1_.zCoord);
  }
  
  public BlockPos(Vec3i p_i46034_1_)
  {
    this(p_i46034_1_.getX(), p_i46034_1_.getY(), p_i46034_1_.getZ());
  }
  
  public BlockPos add(double x, double y, double z)
  {
    return new BlockPos(getX() + x, getY() + y, getZ() + z);
  }
  
  public BlockPos add(int x, int y, int z)
  {
    return new BlockPos(getX() + x, getY() + y, getZ() + z);
  }
  
  public BlockPos add(Vec3i vec)
  {
    return new BlockPos(getX() + vec.getX(), getY() + vec.getY(), getZ() + vec.getZ());
  }
  
  public BlockPos subtract(Vec3i vec)
  {
    return new BlockPos(getX() - vec.getX(), getY() - vec.getY(), getZ() - vec.getZ());
  }
  
  public BlockPos multiply(int factor)
  {
    return new BlockPos(getX() * factor, getY() * factor, getZ() * factor);
  }
  
  public BlockPos offsetUp()
  {
    return offsetUp(1);
  }
  
  public BlockPos offsetUp(int n)
  {
    return offset(EnumFacing.UP, n);
  }
  
  public BlockPos offsetDown()
  {
    return offsetDown(1);
  }
  
  public BlockPos offsetDown(int n)
  {
    return offset(EnumFacing.DOWN, n);
  }
  
  public BlockPos offsetNorth()
  {
    return offsetNorth(1);
  }
  
  public BlockPos offsetNorth(int n)
  {
    return offset(EnumFacing.NORTH, n);
  }
  
  public BlockPos offsetSouth()
  {
    return offsetSouth(1);
  }
  
  public BlockPos offsetSouth(int n)
  {
    return offset(EnumFacing.SOUTH, n);
  }
  
  public BlockPos offsetWest()
  {
    return offsetWest(1);
  }
  
  public BlockPos offsetWest(int n)
  {
    return offset(EnumFacing.WEST, n);
  }
  
  public BlockPos offsetEast()
  {
    return offsetEast(1);
  }
  
  public BlockPos offsetEast(int n)
  {
    return offset(EnumFacing.EAST, n);
  }
  
  public BlockPos offset(EnumFacing facing)
  {
    return offset(facing, 1);
  }
  
  public BlockPos offset(EnumFacing facing, int n)
  {
    return new BlockPos(getX() + facing.getFrontOffsetX() * n, getY() + facing.getFrontOffsetY() * n, getZ() + facing.getFrontOffsetZ() * n);
  }
  
  public BlockPos crossProductBP(Vec3i vec)
  {
    return new BlockPos(getY() * vec.getZ() - getZ() * vec.getY(), getZ() * vec.getX() - getX() * vec.getZ(), getX() * vec.getY() - getY() * vec.getX());
  }
  
  public long toLong()
  {
    return (getX() & field_177994_h) << field_177988_g | (getY() & field_177995_i) << field_177987_f | (getZ() & field_177993_j) << 0;
  }
  
  public static BlockPos fromLong(long serialized)
  {
    int var2 = (int)(serialized << 64 - field_177988_g - field_177990_b >> 64 - field_177990_b);
    int var3 = (int)(serialized << 64 - field_177987_f - field_177989_d >> 64 - field_177989_d);
    int var4 = (int)(serialized << 64 - field_177991_c >> 64 - field_177991_c);
    return new BlockPos(var2, var3, var4);
  }
  
  public static Iterable getAllInBox(BlockPos from, BlockPos to)
  {
    BlockPos var2 = new BlockPos(Math.min(from.getX(), to.getX()), Math.min(from.getY(), to.getY()), Math.min(from.getZ(), to.getZ()));
    final BlockPos var3 = new BlockPos(Math.max(from.getX(), to.getX()), Math.max(from.getY(), to.getY()), Math.max(from.getZ(), to.getZ()));
    new Iterable()
    {
      private static final String __OBFID = "CL_00002333";
      
      public Iterator iterator()
      {
        new AbstractIterator()
        {
          private BlockPos lastReturned = null;
          private static final String __OBFID = "CL_00002332";
          
          protected BlockPos computeNext0()
          {
            if (this.lastReturned == null)
            {
              this.lastReturned = BlockPos.1.this.val$var2;
              return this.lastReturned;
            }
            if (this.lastReturned.equals(BlockPos.1.this.val$var3)) {
              return (BlockPos)endOfData();
            }
            int var1 = this.lastReturned.getX();
            int var2x = this.lastReturned.getY();
            int var3x = this.lastReturned.getZ();
            if (var1 < BlockPos.1.this.val$var3.getX())
            {
              var1++;
            }
            else if (var2x < BlockPos.1.this.val$var3.getY())
            {
              var1 = BlockPos.1.this.val$var2.getX();
              var2x++;
            }
            else if (var3x < BlockPos.1.this.val$var3.getZ())
            {
              var1 = BlockPos.1.this.val$var2.getX();
              var2x = BlockPos.1.this.val$var2.getY();
              var3x++;
            }
            this.lastReturned = new BlockPos(var1, var2x, var3x);
            return this.lastReturned;
          }
          
          protected Object computeNext()
          {
            return computeNext0();
          }
        };
      }
    };
  }
  
  public static Iterable getAllInBoxMutable(BlockPos from, BlockPos to)
  {
    BlockPos var2 = new BlockPos(Math.min(from.getX(), to.getX()), Math.min(from.getY(), to.getY()), Math.min(from.getZ(), to.getZ()));
    final BlockPos var3 = new BlockPos(Math.max(from.getX(), to.getX()), Math.max(from.getY(), to.getY()), Math.max(from.getZ(), to.getZ()));
    new Iterable()
    {
      private static final String __OBFID = "CL_00002331";
      
      public Iterator iterator()
      {
        new AbstractIterator()
        {
          private BlockPos.MutableBlockPos theBlockPos = null;
          private static final String __OBFID = "CL_00002330";
          
          protected BlockPos.MutableBlockPos computeNext0()
          {
            if (this.theBlockPos == null)
            {
              this.theBlockPos = new BlockPos.MutableBlockPos(BlockPos.2.this.val$var2.getX(), BlockPos.2.this.val$var2.getY(), BlockPos.2.this.val$var2.getZ(), null);
              return this.theBlockPos;
            }
            if (this.theBlockPos.equals(BlockPos.2.this.val$var3)) {
              return (BlockPos.MutableBlockPos)endOfData();
            }
            int var1 = this.theBlockPos.getX();
            int var2xx = this.theBlockPos.getY();
            int var3x = this.theBlockPos.getZ();
            if (var1 < BlockPos.2.this.val$var3.getX())
            {
              var1++;
            }
            else if (var2xx < BlockPos.2.this.val$var3.getY())
            {
              var1 = BlockPos.2.this.val$var2.getX();
              var2xx++;
            }
            else if (var3x < BlockPos.2.this.val$var3.getZ())
            {
              var1 = BlockPos.2.this.val$var2.getX();
              var2xx = BlockPos.2.this.val$var2.getY();
              var3x++;
            }
            this.theBlockPos.x = var1;
            this.theBlockPos.y = var2xx;
            this.theBlockPos.z = var3x;
            return this.theBlockPos;
          }
          
          protected Object computeNext()
          {
            return computeNext0();
          }
        };
      }
    };
  }
  
  public Vec3i crossProduct(Vec3i vec)
  {
    return crossProductBP(vec);
  }
  
  public static final class MutableBlockPos
    extends BlockPos
  {
    public int x;
    public int y;
    public int z;
    private static final String __OBFID = "CL_00002329";
    
    private MutableBlockPos(int x_, int y_, int z_)
    {
      super(0, 0);
      this.x = x_;
      this.y = y_;
      this.z = z_;
    }
    
    public int getX()
    {
      return this.x;
    }
    
    public int getY()
    {
      return this.y;
    }
    
    public int getZ()
    {
      return this.z;
    }
    
    public Vec3i crossProduct(Vec3i vec)
    {
      return super.crossProductBP(vec);
    }
    
    MutableBlockPos(int p_i46025_1_, int p_i46025_2_, int p_i46025_3_, Object p_i46025_4_)
    {
      this(p_i46025_1_, p_i46025_2_, p_i46025_3_);
    }
  }
}
