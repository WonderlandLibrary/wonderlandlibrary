package net.ccbluex.liquidbounce.api.minecraft.item;

import java.util.Collection;
import kotlin.Metadata;
import net.ccbluex.liquidbounce.api.minecraft.block.state.IIBlockState;
import net.ccbluex.liquidbounce.api.minecraft.enchantments.IEnchantment;
import net.ccbluex.liquidbounce.api.minecraft.entity.ai.attributes.IAttributeModifier;
import net.ccbluex.liquidbounce.api.minecraft.item.IItem;
import net.ccbluex.liquidbounce.api.minecraft.nbt.INBTBase;
import net.ccbluex.liquidbounce.api.minecraft.nbt.INBTTagCompound;
import net.ccbluex.liquidbounce.api.minecraft.nbt.INBTTagList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000n\n\n\u0000\n\u0000\n\n\b\n\n\b\n\n\b\n\b\n\b\n\t\n\b\n\n\b\n\n\u0000\n\n\b\n\n\n\b\n\n\u0000\n\n\u0000\n\n\b\n\n\u0000\bf\u000020J$0%2&0'2(0H&J)\b0+0*2,0H&J-0.2/00H&J\b102H&J30\u000020H&J40%2,02506H&R0X¦¢\bR0X¦¢\b\b\tR\n0X¦¢\b\f\rR0X¦¢\f\b\"\bR0X¦¢\bR0X¦¢\bR0X¦¢\bR0X¦¢\f\b\"\b !R\"0X¦¢\b#¨7"}, d2={"Lnet/ccbluex/liquidbounce/api/minecraft/item/IItemStack;", "", "displayName", "", "getDisplayName", "()Ljava/lang/String;", "enchantmentTagList", "Lnet/ccbluex/liquidbounce/api/minecraft/nbt/INBTTagList;", "getEnchantmentTagList", "()Lnet/ccbluex/liquidbounce/api/minecraft/nbt/INBTTagList;", "item", "Lnet/ccbluex/liquidbounce/api/minecraft/item/IItem;", "getItem", "()Lnet/ccbluex/liquidbounce/api/minecraft/item/IItem;", "itemDamage", "", "getItemDamage", "()I", "setItemDamage", "(I)V", "itemDelay", "", "getItemDelay", "()J", "maxItemUseDuration", "getMaxItemUseDuration", "stackSize", "getStackSize", "tagCompound", "Lnet/ccbluex/liquidbounce/api/minecraft/nbt/INBTTagCompound;", "getTagCompound", "()Lnet/ccbluex/liquidbounce/api/minecraft/nbt/INBTTagCompound;", "setTagCompound", "(Lnet/ccbluex/liquidbounce/api/minecraft/nbt/INBTTagCompound;)V", "unlocalizedName", "getUnlocalizedName", "addEnchantment", "", "enchantment", "Lnet/ccbluex/liquidbounce/api/minecraft/enchantments/IEnchantment;", "level", "getAttributeModifier", "", "Lnet/ccbluex/liquidbounce/api/minecraft/entity/ai/attributes/IAttributeModifier;", "key", "getStrVsBlock", "", "block", "Lnet/ccbluex/liquidbounce/api/minecraft/block/state/IIBlockState;", "isSplash", "", "setStackDisplayName", "setTagInfo", "nbt", "Lnet/ccbluex/liquidbounce/api/minecraft/nbt/INBTBase;", "Pride"})
public interface IItemStack {
    @NotNull
    public String getDisplayName();

    @NotNull
    public String getUnlocalizedName();

    public int getMaxItemUseDuration();

    @Nullable
    public INBTTagList getEnchantmentTagList();

    @Nullable
    public INBTTagCompound getTagCompound();

    public void setTagCompound(@Nullable INBTTagCompound var1);

    public int getStackSize();

    public int getItemDamage();

    public void setItemDamage(int var1);

    @Nullable
    public IItem getItem();

    public long getItemDelay();

    public float getStrVsBlock(@NotNull IIBlockState var1);

    public void setTagInfo(@NotNull String var1, @NotNull INBTBase var2);

    @NotNull
    public IItemStack setStackDisplayName(@NotNull String var1);

    public void addEnchantment(@NotNull IEnchantment var1, int var2);

    @NotNull
    public Collection<IAttributeModifier> getAttributeModifier(@NotNull String var1);

    public boolean isSplash();
}
