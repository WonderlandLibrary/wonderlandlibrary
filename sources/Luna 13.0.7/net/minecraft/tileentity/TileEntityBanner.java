package net.minecraft.tileentity;

import com.google.common.collect.Lists;
import java.util.List;
import net.minecraft.block.BlockFlower.EnumFlowerType;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;

public class TileEntityBanner
  extends TileEntity
{
  private int baseColor;
  private NBTTagList field_175118_f;
  private boolean field_175119_g;
  private List field_175122_h;
  private List field_175123_i;
  private String field_175121_j;
  private static final String __OBFID = "CL_00002044";
  
  public TileEntityBanner() {}
  
  public void setItemValues(ItemStack p_175112_1_)
  {
    this.field_175118_f = null;
    if ((p_175112_1_.hasTagCompound()) && (p_175112_1_.getTagCompound().hasKey("BlockEntityTag", 10)))
    {
      NBTTagCompound var2 = p_175112_1_.getTagCompound().getCompoundTag("BlockEntityTag");
      if (var2.hasKey("Patterns")) {
        this.field_175118_f = ((NBTTagList)var2.getTagList("Patterns", 10).copy());
      }
      if (var2.hasKey("Base", 99)) {
        this.baseColor = var2.getInteger("Base");
      } else {
        this.baseColor = (p_175112_1_.getMetadata() & 0xF);
      }
    }
    else
    {
      this.baseColor = (p_175112_1_.getMetadata() & 0xF);
    }
    this.field_175122_h = null;
    this.field_175123_i = null;
    this.field_175121_j = "";
    this.field_175119_g = true;
  }
  
  public void writeToNBT(NBTTagCompound compound)
  {
    super.writeToNBT(compound);
    compound.setInteger("Base", this.baseColor);
    if (this.field_175118_f != null) {
      compound.setTag("Patterns", this.field_175118_f);
    }
  }
  
  public void readFromNBT(NBTTagCompound compound)
  {
    super.readFromNBT(compound);
    this.baseColor = compound.getInteger("Base");
    this.field_175118_f = compound.getTagList("Patterns", 10);
    this.field_175122_h = null;
    this.field_175123_i = null;
    this.field_175121_j = null;
    this.field_175119_g = true;
  }
  
  public Packet<INetHandler> getDescriptionPacket()
  {
    NBTTagCompound var1 = new NBTTagCompound();
    writeToNBT(var1);
    return new S35PacketUpdateTileEntity(this.pos, 6, var1);
  }
  
  public int getBaseColor()
  {
    return this.baseColor;
  }
  
  public static int getBaseColor(ItemStack stack)
  {
    NBTTagCompound var1 = stack.getSubCompound("BlockEntityTag", false);
    return (var1 != null) && (var1.hasKey("Base")) ? var1.getInteger("Base") : stack.getMetadata();
  }
  
  public static int func_175113_c(ItemStack p_175113_0_)
  {
    NBTTagCompound var1 = p_175113_0_.getSubCompound("BlockEntityTag", false);
    return (var1 != null) && (var1.hasKey("Patterns")) ? var1.getTagList("Patterns", 10).tagCount() : 0;
  }
  
  public List func_175114_c()
  {
    func_175109_g();
    return this.field_175122_h;
  }
  
  public List func_175110_d()
  {
    func_175109_g();
    return this.field_175123_i;
  }
  
  public String func_175116_e()
  {
    func_175109_g();
    return this.field_175121_j;
  }
  
  private void func_175109_g()
  {
    if ((this.field_175122_h == null) || (this.field_175123_i == null) || (this.field_175121_j == null)) {
      if (!this.field_175119_g)
      {
        this.field_175121_j = "";
      }
      else
      {
        this.field_175122_h = Lists.newArrayList();
        this.field_175123_i = Lists.newArrayList();
        this.field_175122_h.add(EnumBannerPattern.BASE);
        this.field_175123_i.add(EnumDyeColor.func_176766_a(this.baseColor));
        this.field_175121_j = ("b" + this.baseColor);
        if (this.field_175118_f != null) {
          for (int var1 = 0; var1 < this.field_175118_f.tagCount(); var1++)
          {
            NBTTagCompound var2 = this.field_175118_f.getCompoundTagAt(var1);
            EnumBannerPattern var3 = EnumBannerPattern.func_177268_a(var2.getString("Pattern"));
            if (var3 != null)
            {
              this.field_175122_h.add(var3);
              int var4 = var2.getInteger("Color");
              this.field_175123_i.add(EnumDyeColor.func_176766_a(var4));
              this.field_175121_j = (this.field_175121_j + var3.func_177273_b() + var4);
            }
          }
        }
      }
    }
  }
  
  public static void func_175117_e(ItemStack p_175117_0_)
  {
    NBTTagCompound var1 = p_175117_0_.getSubCompound("BlockEntityTag", false);
    if ((var1 != null) && (var1.hasKey("Patterns", 9)))
    {
      NBTTagList var2 = var1.getTagList("Patterns", 10);
      if (var2.tagCount() > 0)
      {
        var2.removeTag(var2.tagCount() - 1);
        if (var2.hasNoTags())
        {
          p_175117_0_.getTagCompound().removeTag("BlockEntityTag");
          if (p_175117_0_.getTagCompound().hasNoTags()) {
            p_175117_0_.setTagCompound((NBTTagCompound)null);
          }
        }
      }
    }
  }
  
  public static enum EnumBannerPattern
  {
    private String field_177284_N;
    private String field_177285_O;
    private String[] field_177291_P;
    private ItemStack field_177290_Q;
    private static final EnumBannerPattern[] $VALUES = { BASE, SQUARE_BOTTOM_LEFT, SQUARE_BOTTOM_RIGHT, SQUARE_TOP_LEFT, SQUARE_TOP_RIGHT, STRIPE_BOTTOM, STRIPE_TOP, STRIPE_LEFT, STRIPE_RIGHT, STRIPE_CENTER, STRIPE_MIDDLE, STRIPE_DOWNRIGHT, STRIPE_DOWNLEFT, STRIPE_SMALL, CROSS, STRAIGHT_CROSS, TRIANGLE_BOTTOM, TRIANGLE_TOP, TRIANGLES_BOTTOM, TRIANGLES_TOP, DIAGONAL_LEFT, DIAGONAL_RIGHT, DIAGONAL_LEFT_MIRROR, DIAGONAL_RIGHT_MIRROR, CIRCLE_MIDDLE, RHOMBUS_MIDDLE, HALF_VERTICAL, HALF_HORIZONTAL, HALF_VERTICAL_MIRROR, HALF_HORIZONTAL_MIRROR, BORDER, CURLY_BORDER, CREEPER, GRADIENT, GRADIENT_UP, BRICKS, SKULL, FLOWER, MOJANG };
    private static final String __OBFID = "CL_00002043";
    
    private EnumBannerPattern(String p_i45670_1_, int p_i45670_2_, String p_i45670_3_, String p_i45670_4_)
    {
      this.field_177291_P = new String[3];
      this.field_177284_N = p_i45670_3_;
      this.field_177285_O = p_i45670_4_;
    }
    
    private EnumBannerPattern(String p_i45671_1_, int p_i45671_2_, String p_i45671_3_, String p_i45671_4_, ItemStack p_i45671_5_)
    {
      this(p_i45671_1_, p_i45671_2_, p_i45671_3_, p_i45671_4_);
      this.field_177290_Q = p_i45671_5_;
    }
    
    private EnumBannerPattern(String p_i45672_1_, int p_i45672_2_, String p_i45672_3_, String p_i45672_4_, String p_i45672_5_, String p_i45672_6_, String p_i45672_7_)
    {
      this(p_i45672_1_, p_i45672_2_, p_i45672_3_, p_i45672_4_);
      this.field_177291_P[0] = p_i45672_5_;
      this.field_177291_P[1] = p_i45672_6_;
      this.field_177291_P[2] = p_i45672_7_;
    }
    
    public String func_177271_a()
    {
      return this.field_177284_N;
    }
    
    public String func_177273_b()
    {
      return this.field_177285_O;
    }
    
    public String[] func_177267_c()
    {
      return this.field_177291_P;
    }
    
    public boolean func_177270_d()
    {
      return (this.field_177290_Q != null) || (this.field_177291_P[0] != null);
    }
    
    public boolean func_177269_e()
    {
      return this.field_177290_Q != null;
    }
    
    public ItemStack func_177272_f()
    {
      return this.field_177290_Q;
    }
    
    public static EnumBannerPattern func_177268_a(String p_177268_0_)
    {
      EnumBannerPattern[] var1 = values();
      int var2 = var1.length;
      for (int var3 = 0; var3 < var2; var3++)
      {
        EnumBannerPattern var4 = var1[var3];
        if (var4.field_177285_O.equals(p_177268_0_)) {
          return var4;
        }
      }
      return null;
    }
  }
}
