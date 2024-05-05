package net.minecraft.entity.item;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Random;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityHanging;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.PlayerCapabilities;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;

public class EntityPainting
  extends EntityHanging
{
  public EnumArt art;
  private static final String __OBFID = "CL_00001556";
  
  public EntityPainting(World worldIn)
  {
    super(worldIn);
  }
  
  public EntityPainting(World worldIn, BlockPos p_i45849_2_, EnumFacing p_i45849_3_)
  {
    super(worldIn, p_i45849_2_);
    ArrayList var4 = Lists.newArrayList();
    EnumArt[] var5 = EnumArt.values();
    int var6 = var5.length;
    for (int var7 = 0; var7 < var6; var7++)
    {
      EnumArt var8 = var5[var7];
      this.art = var8;
      func_174859_a(p_i45849_3_);
      if (onValidSurface()) {
        var4.add(var8);
      }
    }
    if (!var4.isEmpty()) {
      this.art = ((EnumArt)var4.get(this.rand.nextInt(var4.size())));
    }
    func_174859_a(p_i45849_3_);
  }
  
  public EntityPainting(World worldIn, BlockPos p_i45850_2_, EnumFacing p_i45850_3_, String p_i45850_4_)
  {
    this(worldIn, p_i45850_2_, p_i45850_3_);
    EnumArt[] var5 = EnumArt.values();
    int var6 = var5.length;
    for (int var7 = 0; var7 < var6; var7++)
    {
      EnumArt var8 = var5[var7];
      if (var8.title.equals(p_i45850_4_))
      {
        this.art = var8;
        break;
      }
    }
    func_174859_a(p_i45850_3_);
  }
  
  public void writeEntityToNBT(NBTTagCompound tagCompound)
  {
    tagCompound.setString("Motive", this.art.title);
    super.writeEntityToNBT(tagCompound);
  }
  
  public void readEntityFromNBT(NBTTagCompound tagCompund)
  {
    String var2 = tagCompund.getString("Motive");
    EnumArt[] var3 = EnumArt.values();
    int var4 = var3.length;
    for (int var5 = 0; var5 < var4; var5++)
    {
      EnumArt var6 = var3[var5];
      if (var6.title.equals(var2)) {
        this.art = var6;
      }
    }
    if (this.art == null) {
      this.art = EnumArt.KEBAB;
    }
    super.readEntityFromNBT(tagCompund);
  }
  
  public int getWidthPixels()
  {
    return this.art.sizeX;
  }
  
  public int getHeightPixels()
  {
    return this.art.sizeY;
  }
  
  public void onBroken(Entity p_110128_1_)
  {
    if (this.worldObj.getGameRules().getGameRuleBooleanValue("doTileDrops"))
    {
      if ((p_110128_1_ instanceof EntityPlayer))
      {
        EntityPlayer var2 = (EntityPlayer)p_110128_1_;
        if (var2.capabilities.isCreativeMode) {
          return;
        }
      }
      entityDropItem(new ItemStack(Items.painting), 0.0F);
    }
  }
  
  public void setLocationAndAngles(double x, double y, double z, float yaw, float pitch)
  {
    BlockPos var9 = new BlockPos(x - this.posX, y - this.posY, z - this.posZ);
    BlockPos var10 = this.field_174861_a.add(var9);
    setPosition(var10.getX(), var10.getY(), var10.getZ());
  }
  
  public void func_180426_a(double p_180426_1_, double p_180426_3_, double p_180426_5_, float p_180426_7_, float p_180426_8_, int p_180426_9_, boolean p_180426_10_)
  {
    BlockPos var11 = new BlockPos(p_180426_1_ - this.posX, p_180426_3_ - this.posY, p_180426_5_ - this.posZ);
    BlockPos var12 = this.field_174861_a.add(var11);
    setPosition(var12.getX(), var12.getY(), var12.getZ());
  }
  
  public static enum EnumArt
  {
    public static final int field_180001_A = "SkullAndRoses".length();
    public final String title;
    public final int sizeX;
    public final int sizeY;
    public final int offsetX;
    public final int offsetY;
    private static final EnumArt[] $VALUES = { KEBAB, AZTEC, ALBAN, AZTEC_2, BOMB, PLANT, WASTELAND, POOL, COURBET, SEA, SUNSET, CREEBET, WANDERER, GRAHAM, MATCH, BUST, STAGE, VOID, SKULL_AND_ROSES, WITHER, FIGHTERS, POINTER, PIGSCENE, BURNING_SKULL, SKELETON, DONKEY_KONG };
    private static final String __OBFID = "CL_00001557";
    
    private EnumArt(String p_i1598_1_, int p_i1598_2_, String p_i1598_3_, int p_i1598_4_, int p_i1598_5_, int p_i1598_6_, int p_i1598_7_)
    {
      this.title = p_i1598_3_;
      this.sizeX = p_i1598_4_;
      this.sizeY = p_i1598_5_;
      this.offsetX = p_i1598_6_;
      this.offsetY = p_i1598_7_;
    }
  }
}
