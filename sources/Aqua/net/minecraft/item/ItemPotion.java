package net.minecraft.item;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityPotion;
import net.minecraft.init.Items;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionHelper;
import net.minecraft.stats.StatList;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

public class ItemPotion
extends Item {
    private Map<Integer, List<PotionEffect>> effectCache = Maps.newHashMap();
    private static final Map<List<PotionEffect>, Integer> SUB_ITEMS_CACHE = Maps.newLinkedHashMap();

    public ItemPotion() {
        this.setMaxStackSize(1);
        this.setHasSubtypes(true);
        this.setMaxDamage(0);
        this.setCreativeTab(CreativeTabs.tabBrewing);
    }

    public List<PotionEffect> getEffects(ItemStack stack) {
        if (stack.hasTagCompound() && stack.getTagCompound().hasKey("CustomPotionEffects", 9)) {
            ArrayList list1 = Lists.newArrayList();
            NBTTagList nbttaglist = stack.getTagCompound().getTagList("CustomPotionEffects", 10);
            for (int i = 0; i < nbttaglist.tagCount(); ++i) {
                NBTTagCompound nbttagcompound = nbttaglist.getCompoundTagAt(i);
                PotionEffect potioneffect = PotionEffect.readCustomPotionEffectFromNBT((NBTTagCompound)nbttagcompound);
                if (potioneffect == null) continue;
                list1.add((Object)potioneffect);
            }
            return list1;
        }
        List list = (List)this.effectCache.get((Object)stack.getMetadata());
        if (list == null) {
            list = PotionHelper.getPotionEffects((int)stack.getMetadata(), (boolean)false);
            this.effectCache.put((Object)stack.getMetadata(), (Object)list);
        }
        return list;
    }

    public List<PotionEffect> getEffects(int meta) {
        List list = (List)this.effectCache.get((Object)meta);
        if (list == null) {
            list = PotionHelper.getPotionEffects((int)meta, (boolean)false);
            this.effectCache.put((Object)meta, (Object)list);
        }
        return list;
    }

    public ItemStack onItemUseFinish(ItemStack stack, World worldIn, EntityPlayer playerIn) {
        List<PotionEffect> list;
        if (!playerIn.capabilities.isCreativeMode) {
            --stack.stackSize;
        }
        if (!worldIn.isRemote && (list = this.getEffects(stack)) != null) {
            for (PotionEffect potioneffect : list) {
                playerIn.addPotionEffect(new PotionEffect(potioneffect));
            }
        }
        playerIn.triggerAchievement(StatList.objectUseStats[Item.getIdFromItem((Item)this)]);
        if (!playerIn.capabilities.isCreativeMode) {
            if (stack.stackSize <= 0) {
                return new ItemStack(Items.glass_bottle);
            }
            playerIn.inventory.addItemStackToInventory(new ItemStack(Items.glass_bottle));
        }
        return stack;
    }

    public int getMaxItemUseDuration(ItemStack stack) {
        return 32;
    }

    public EnumAction getItemUseAction(ItemStack stack) {
        return EnumAction.DRINK;
    }

    public ItemStack onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn) {
        if (ItemPotion.isSplash(itemStackIn.getMetadata())) {
            if (!playerIn.capabilities.isCreativeMode) {
                --itemStackIn.stackSize;
            }
            worldIn.playSoundAtEntity((Entity)playerIn, "random.bow", 0.5f, 0.4f / (itemRand.nextFloat() * 0.4f + 0.8f));
            if (!worldIn.isRemote) {
                worldIn.spawnEntityInWorld((Entity)new EntityPotion(worldIn, (EntityLivingBase)playerIn, itemStackIn));
            }
            playerIn.triggerAchievement(StatList.objectUseStats[Item.getIdFromItem((Item)this)]);
            return itemStackIn;
        }
        playerIn.setItemInUse(itemStackIn, this.getMaxItemUseDuration(itemStackIn));
        return itemStackIn;
    }

    public static boolean isSplash(int meta) {
        return (meta & 0x4000) != 0;
    }

    public int getColorFromDamage(int meta) {
        return PotionHelper.getLiquidColor((int)meta, (boolean)false);
    }

    public int getColorFromItemStack(ItemStack stack, int renderPass) {
        return renderPass > 0 ? 0xFFFFFF : this.getColorFromDamage(stack.getMetadata());
    }

    public boolean isEffectInstant(int meta) {
        List<PotionEffect> list = this.getEffects(meta);
        if (list != null && !list.isEmpty()) {
            for (PotionEffect potioneffect : list) {
                if (!Potion.potionTypes[potioneffect.getPotionID()].isInstant()) continue;
                return true;
            }
            return false;
        }
        return false;
    }

    public String getItemStackDisplayName(ItemStack stack) {
        List<PotionEffect> list;
        if (stack.getMetadata() == 0) {
            return StatCollector.translateToLocal((String)"item.emptyPotion.name").trim();
        }
        String s = "";
        if (ItemPotion.isSplash(stack.getMetadata())) {
            s = StatCollector.translateToLocal((String)"potion.prefix.grenade").trim() + " ";
        }
        if ((list = Items.potionitem.getEffects(stack)) != null && !list.isEmpty()) {
            String s2 = ((PotionEffect)list.get(0)).getEffectName();
            s2 = s2 + ".postfix";
            return s + StatCollector.translateToLocal((String)s2).trim();
        }
        String s1 = PotionHelper.getPotionPrefix((int)stack.getMetadata());
        return StatCollector.translateToLocal((String)s1).trim() + " " + super.getItemStackDisplayName(stack);
    }

    public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
        if (stack.getMetadata() != 0) {
            List<PotionEffect> list = Items.potionitem.getEffects(stack);
            HashMultimap multimap = HashMultimap.create();
            if (list != null && !list.isEmpty()) {
                for (PotionEffect potioneffect : list) {
                    String s1 = StatCollector.translateToLocal((String)potioneffect.getEffectName()).trim();
                    Potion potion = Potion.potionTypes[potioneffect.getPotionID()];
                    Map map = potion.getAttributeModifierMap();
                    if (map != null && map.size() > 0) {
                        for (Map.Entry entry : map.entrySet()) {
                            AttributeModifier attributemodifier = (AttributeModifier)entry.getValue();
                            AttributeModifier attributemodifier1 = new AttributeModifier(attributemodifier.getName(), potion.getAttributeModifierAmount(potioneffect.getAmplifier(), attributemodifier), attributemodifier.getOperation());
                            multimap.put((Object)((IAttribute)entry.getKey()).getAttributeUnlocalizedName(), (Object)attributemodifier1);
                        }
                    }
                    if (potioneffect.getAmplifier() > 0) {
                        s1 = s1 + " " + StatCollector.translateToLocal((String)("potion.potency." + potioneffect.getAmplifier())).trim();
                    }
                    if (potioneffect.getDuration() > 20) {
                        s1 = s1 + " (" + Potion.getDurationString((PotionEffect)potioneffect) + ")";
                    }
                    if (potion.isBadEffect()) {
                        tooltip.add((Object)(EnumChatFormatting.RED + s1));
                        continue;
                    }
                    tooltip.add((Object)(EnumChatFormatting.GRAY + s1));
                }
            } else {
                String s = StatCollector.translateToLocal((String)"potion.empty").trim();
                tooltip.add((Object)(EnumChatFormatting.GRAY + s));
            }
            if (!multimap.isEmpty()) {
                tooltip.add((Object)"");
                tooltip.add((Object)(EnumChatFormatting.DARK_PURPLE + StatCollector.translateToLocal((String)"potion.effects.whenDrank")));
                for (Map.Entry entry1 : multimap.entries()) {
                    AttributeModifier attributemodifier2 = (AttributeModifier)entry1.getValue();
                    double d0 = attributemodifier2.getAmount();
                    double d1 = attributemodifier2.getOperation() != 1 && attributemodifier2.getOperation() != 2 ? attributemodifier2.getAmount() : attributemodifier2.getAmount() * 100.0;
                    if (d0 > 0.0) {
                        tooltip.add((Object)(EnumChatFormatting.BLUE + StatCollector.translateToLocalFormatted((String)("attribute.modifier.plus." + attributemodifier2.getOperation()), (Object[])new Object[]{ItemStack.DECIMALFORMAT.format(d1), StatCollector.translateToLocal((String)("attribute.name." + (String)entry1.getKey()))})));
                        continue;
                    }
                    if (!(d0 < 0.0)) continue;
                    tooltip.add((Object)(EnumChatFormatting.RED + StatCollector.translateToLocalFormatted((String)("attribute.modifier.take." + attributemodifier2.getOperation()), (Object[])new Object[]{ItemStack.DECIMALFORMAT.format(d1 *= -1.0), StatCollector.translateToLocal((String)("attribute.name." + (String)entry1.getKey()))})));
                }
            }
        }
    }

    public boolean hasEffect(ItemStack stack) {
        List<PotionEffect> list = this.getEffects(stack);
        return list != null && !list.isEmpty();
    }

    public void getSubItems(Item itemIn, CreativeTabs tab, List<ItemStack> subItems) {
        super.getSubItems(itemIn, tab, subItems);
        if (SUB_ITEMS_CACHE.isEmpty()) {
            for (int i = 0; i <= 15; ++i) {
                for (int j = 0; j <= 1; ++j) {
                    int lvt_6_1_ = j == 0 ? i | 0x2000 : i | 0x4000;
                    for (int l = 0; l <= 2; ++l) {
                        List list;
                        int i1 = lvt_6_1_;
                        if (l != 0) {
                            if (l == 1) {
                                i1 = lvt_6_1_ | 0x20;
                            } else if (l == 2) {
                                i1 = lvt_6_1_ | 0x40;
                            }
                        }
                        if ((list = PotionHelper.getPotionEffects((int)i1, (boolean)false)) == null || list.isEmpty()) continue;
                        SUB_ITEMS_CACHE.put((Object)list, (Object)i1);
                    }
                }
            }
        }
        Iterator iterator = SUB_ITEMS_CACHE.values().iterator();
        while (iterator.hasNext()) {
            int j1 = (Integer)iterator.next();
            subItems.add((Object)new ItemStack(itemIn, 1, j1));
        }
    }
}
