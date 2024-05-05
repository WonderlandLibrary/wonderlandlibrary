package optifine;

import com.google.common.collect.AbstractIterator;
import java.util.Iterator;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;

public class BlockPosM
  extends BlockPos
{
  private int mx;
  private int my;
  private int mz;
  private int level;
  private BlockPosM[] facings;
  private boolean needsUpdate;
  
  public BlockPosM(int x, int y, int z)
  {
    this(x, y, z, 0);
  }
  
  public BlockPosM(double xIn, double yIn, double zIn)
  {
    this(MathHelper.floor_double(xIn), MathHelper.floor_double(yIn), MathHelper.floor_double(zIn));
  }
  
  public BlockPosM(int x, int y, int z, int level)
  {
    super(0, 0, 0);
    this.mx = x;
    this.my = y;
    this.mz = z;
    this.level = level;
  }
  
  public int getX()
  {
    return this.mx;
  }
  
  public int getY()
  {
    return this.my;
  }
  
  public int getZ()
  {
    return this.mz;
  }
  
  public void setXyz(int x, int y, int z)
  {
    this.mx = x;
    this.my = y;
    this.mz = z;
    this.needsUpdate = true;
  }
  
  public void setXyz(double xIn, double yIn, double zIn)
  {
    setXyz(MathHelper.floor_double(xIn), MathHelper.floor_double(yIn), MathHelper.floor_double(zIn));
  }
  
  public BlockPos offset(EnumFacing facing)
  {
    if (this.level <= 0) {
      return super.offset(facing, 1);
    }
    if (this.facings == null) {
      this.facings = new BlockPosM[EnumFacing.VALUES.length];
    }
    if (this.needsUpdate) {
      update();
    }
    int index = facing.getIndex();
    BlockPosM bpm = this.facings[index];
    if (bpm == null)
    {
      int nx = this.mx + facing.getFrontOffsetX();
      int ny = this.my + facing.getFrontOffsetY();
      int nz = this.mz + facing.getFrontOffsetZ();
      bpm = new BlockPosM(nx, ny, nz, this.level - 1);
      this.facings[index] = bpm;
    }
    return bpm;
  }
  
  public BlockPos offset(EnumFacing facing, int n)
  {
    return n == 1 ? offset(facing) : super.offset(facing, n);
  }
  
  private void update()
  {
    for (int i = 0; i < 6; i++)
    {
      BlockPosM bpm = this.facings[i];
      if (bpm != null)
      {
        EnumFacing facing = EnumFacing.VALUES[i];
        int nx = this.mx + facing.getFrontOffsetX();
        int ny = this.my + facing.getFrontOffsetY();
        int nz = this.mz + facing.getFrontOffsetZ();
        bpm.setXyz(nx, ny, nz);
      }
    }
    this.needsUpdate = false;
  }
  
  public static Iterable getAllInBoxMutable(BlockPos from, BlockPos to)
  {
    BlockPos posFrom = new BlockPos(Math.min(from.getX(), to.getX()), Math.min(from.getY(), to.getY()), Math.min(from.getZ(), to.getZ()));
    final BlockPos posTo = new BlockPos(Math.max(from.getX(), to.getX()), Math.max(from.getY(), to.getY()), Math.max(from.getZ(), to.getZ()));
    new Iterable()
    {
      public Iterator iterator()
      {
        new AbstractIterator()
        {
          private BlockPosM theBlockPosM = null;
          
          protected BlockPosM computeNext0()
          {
            if (this.theBlockPosM == null)
            {
              this.theBlockPosM = new BlockPosM(BlockPosM.1.this.val$posFrom.getX(), BlockPosM.1.this.val$posFrom.getY(), BlockPosM.1.this.val$posFrom.getZ(), 3);
              return this.theBlockPosM;
            }
            if (this.theBlockPosM.equals(BlockPosM.1.this.val$posTo)) {
              return (BlockPosM)endOfData();
            }
            int bx = this.theBlockPosM.getX();
            int by = this.theBlockPosM.getY();
            int bz = this.theBlockPosM.getZ();
            if (bx < BlockPosM.1.this.val$posTo.getX())
            {
              bx++;
            }
            else if (by < BlockPosM.1.this.val$posTo.getY())
            {
              bx = BlockPosM.1.this.val$posFrom.getX();
              by++;
            }
            else if (bz < BlockPosM.1.this.val$posTo.getZ())
            {
              bx = BlockPosM.1.this.val$posFrom.getX();
              by = BlockPosM.1.this.val$posFrom.getY();
              bz++;
            }
            this.theBlockPosM.setXyz(bx, by, bz);
            return this.theBlockPosM;
          }
          
          protected Object computeNext()
          {
            return computeNext0();
          }
        };
      }
    };
  }
  
  public BlockPos getImmutable()
  {
    return new BlockPos(getX(), getY(), getZ());
  }
}
