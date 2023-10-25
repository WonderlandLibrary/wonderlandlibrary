package net.minecraft.block;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import java.util.Collection;
import java.util.List;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;

public abstract class BlockFlower extends BlockBush {
  protected PropertyEnum<EnumFlowerType> type;
  
  protected BlockFlower() {
    setDefaultState(this.blockState.getBaseState().withProperty(getTypeProperty(), (getBlockType() == EnumFlowerColor.RED) ? EnumFlowerType.POPPY : EnumFlowerType.DANDELION));
  }
  
  public int damageDropped(IBlockState state) {
    return ((EnumFlowerType)state.getValue(getTypeProperty())).getMeta();
  }
  
  public void getSubBlocks(Item itemIn, CreativeTabs tab, List<ItemStack> list) {
    for (EnumFlowerType blockflower$enumflowertype : EnumFlowerType.getTypes(getBlockType()))
      list.add(new ItemStack(itemIn, 1, blockflower$enumflowertype.getMeta())); 
  }
  
  public IBlockState getStateFromMeta(int meta) {
    return getDefaultState().withProperty(getTypeProperty(), EnumFlowerType.getType(getBlockType(), meta));
  }
  
  public abstract EnumFlowerColor getBlockType();
  
  public IProperty<EnumFlowerType> getTypeProperty() {
    if (this.type == null)
      this.type = PropertyEnum.create("type", EnumFlowerType.class, new Predicate<EnumFlowerType>() {
            public boolean apply(BlockFlower.EnumFlowerType p_apply_1_) {
              return (p_apply_1_.getBlockType() == BlockFlower.this.getBlockType());
            }
          }); 
    return (IProperty<EnumFlowerType>)this.type;
  }
  
  public int getMetaFromState(IBlockState state) {
    return ((EnumFlowerType)state.getValue(getTypeProperty())).getMeta();
  }
  
  protected BlockState createBlockState() {
    return new BlockState(this, new IProperty[] { getTypeProperty() });
  }
  
  public Block.EnumOffsetType getOffsetType() {
    return Block.EnumOffsetType.XZ;
  }
  
  public enum EnumFlowerColor {
    YELLOW, RED;
    
    public BlockFlower getBlock() {
      return (this == YELLOW) ? Blocks.yellow_flower : Blocks.red_flower;
    }
  }
  
  public enum EnumFlowerType implements IStringSerializable {
    DANDELION((String)BlockFlower.EnumFlowerColor.YELLOW, 0, (BlockFlower.EnumFlowerColor)"dandelion"),
    POPPY((String)BlockFlower.EnumFlowerColor.RED, 0, (BlockFlower.EnumFlowerColor)"poppy"),
    BLUE_ORCHID((String)BlockFlower.EnumFlowerColor.RED, 1, (BlockFlower.EnumFlowerColor)"blue_orchid", "blueOrchid"),
    ALLIUM((String)BlockFlower.EnumFlowerColor.RED, 2, (BlockFlower.EnumFlowerColor)"allium"),
    HOUSTONIA((String)BlockFlower.EnumFlowerColor.RED, 3, (BlockFlower.EnumFlowerColor)"houstonia"),
    RED_TULIP((String)BlockFlower.EnumFlowerColor.RED, 4, (BlockFlower.EnumFlowerColor)"red_tulip", "tulipRed"),
    ORANGE_TULIP((String)BlockFlower.EnumFlowerColor.RED, 5, (BlockFlower.EnumFlowerColor)"orange_tulip", "tulipOrange"),
    WHITE_TULIP((String)BlockFlower.EnumFlowerColor.RED, 6, (BlockFlower.EnumFlowerColor)"white_tulip", "tulipWhite"),
    PINK_TULIP((String)BlockFlower.EnumFlowerColor.RED, 7, (BlockFlower.EnumFlowerColor)"pink_tulip", "tulipPink"),
    OXEYE_DAISY((String)BlockFlower.EnumFlowerColor.RED, 8, (BlockFlower.EnumFlowerColor)"oxeye_daisy", "oxeyeDaisy");
    
    private static final EnumFlowerType[][] TYPES_FOR_BLOCK = new EnumFlowerType[(BlockFlower.EnumFlowerColor.values()).length][];
    
    private final BlockFlower.EnumFlowerColor blockType;
    
    private final int meta;
    
    private final String name;
    
    private final String unlocalizedName;
    
    static {
      for (BlockFlower.EnumFlowerColor blockflower$enumflowercolor : BlockFlower.EnumFlowerColor.values()) {
        Collection<EnumFlowerType> collection = Collections2.filter(Lists.newArrayList((Object[])values()), new Predicate<EnumFlowerType>() {
              public boolean apply(BlockFlower.EnumFlowerType p_apply_1_) {
                return (p_apply_1_.getBlockType() == blockflower$enumflowercolor);
              }
            });
        TYPES_FOR_BLOCK[blockflower$enumflowercolor.ordinal()] = collection.<EnumFlowerType>toArray(new EnumFlowerType[collection.size()]);
      } 
    }
    
    EnumFlowerType(BlockFlower.EnumFlowerColor blockType, int meta, String name, String unlocalizedName) {
      this.blockType = blockType;
      this.meta = meta;
      this.name = name;
      this.unlocalizedName = unlocalizedName;
    }
    
    public BlockFlower.EnumFlowerColor getBlockType() {
      return this.blockType;
    }
    
    public int getMeta() {
      return this.meta;
    }
    
    public static EnumFlowerType getType(BlockFlower.EnumFlowerColor blockType, int meta) {
      EnumFlowerType[] ablockflower$enumflowertype = TYPES_FOR_BLOCK[blockType.ordinal()];
      if (meta < 0 || meta >= ablockflower$enumflowertype.length)
        meta = 0; 
      return ablockflower$enumflowertype[meta];
    }
    
    public static EnumFlowerType[] getTypes(BlockFlower.EnumFlowerColor flowerColor) {
      return TYPES_FOR_BLOCK[flowerColor.ordinal()];
    }
    
    public String toString() {
      return this.name;
    }
    
    public String getName() {
      return this.name;
    }
    
    public String getUnlocalizedName() {
      return this.unlocalizedName;
    }
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\minecraft\block\BlockFlower.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */