package net.ccbluex.liquidbounce.api.minecraft.item;

import kotlin.Metadata;
import net.ccbluex.liquidbounce.api.minecraft.item.IItemArmor;
import net.ccbluex.liquidbounce.api.minecraft.item.IItemBlock;
import net.ccbluex.liquidbounce.api.minecraft.item.IItemBucket;
import net.ccbluex.liquidbounce.api.minecraft.item.IItemPotion;
import net.ccbluex.liquidbounce.api.minecraft.item.IItemSword;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u00000\n\n\u0000\n\u0000\n\n\b\n\n\u0000\n\n\u0000\n\n\u0000\n\n\u0000\n\n\u0000\bf\u000020J\b0H&J\b\b0\tH&J\b\n0H&J\b\f0\rH&J\b0H&R0X¦¢\b¨"}, d2={"Lnet/ccbluex/liquidbounce/api/minecraft/item/IItem;", "", "unlocalizedName", "", "getUnlocalizedName", "()Ljava/lang/String;", "asItemArmor", "Lnet/ccbluex/liquidbounce/api/minecraft/item/IItemArmor;", "asItemBlock", "Lnet/ccbluex/liquidbounce/api/minecraft/item/IItemBlock;", "asItemBucket", "Lnet/ccbluex/liquidbounce/api/minecraft/item/IItemBucket;", "asItemPotion", "Lnet/ccbluex/liquidbounce/api/minecraft/item/IItemPotion;", "asItemSword", "Lnet/ccbluex/liquidbounce/api/minecraft/item/IItemSword;", "Pride"})
public interface IItem {
    @NotNull
    public String getUnlocalizedName();

    @NotNull
    public IItemArmor asItemArmor();

    @NotNull
    public IItemPotion asItemPotion();

    @NotNull
    public IItemBlock asItemBlock();

    @NotNull
    public IItemSword asItemSword();

    @NotNull
    public IItemBucket asItemBucket();
}
