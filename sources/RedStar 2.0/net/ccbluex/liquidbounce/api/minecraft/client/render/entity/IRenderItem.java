package net.ccbluex.liquidbounce.api.minecraft.client.render.entity;

import kotlin.Metadata;
import net.ccbluex.liquidbounce.api.minecraft.client.gui.IFontRenderer;
import net.ccbluex.liquidbounce.api.minecraft.item.IItemStack;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u00006\n\n\u0000\n\u0000\n\n\b\n\n\u0000\n\n\u0000\n\b\n\b\n\n\b\n\n\b\bf\u000020J \b0\t2\n02\f0\r20\rH&J 0\t2\n02\f0\r20\rH&J 0\t2\n02\f0\r20\rH&J(0\t202\n02\f0\r20\rH&J(0\t202\n02\f0\r20\rH&R0X¦¢\f\b\"\b¨"}, d2={"Lnet/ccbluex/liquidbounce/api/minecraft/client/render/entity/IRenderItem;", "", "zLevel", "", "getZLevel", "()F", "setZLevel", "(F)V", "renderItemAndEffectIntoGUI", "", "stack", "Lnet/ccbluex/liquidbounce/api/minecraft/item/IItemStack;", "x", "", "y", "renderItemAndEffectIntoGUI2", "Lnet/minecraft/item/ItemStack;", "renderItemIntoGUI", "renderItemOverlays", "fontRenderer", "Lnet/ccbluex/liquidbounce/api/minecraft/client/gui/IFontRenderer;", "renderItemOverlays2", "Pride"})
public interface IRenderItem {
    public float getZLevel();

    public void setZLevel(float var1);

    public void renderItemAndEffectIntoGUI(@NotNull IItemStack var1, int var2, int var3);

    public void renderItemIntoGUI(@NotNull IItemStack var1, int var2, int var3);

    public void renderItemOverlays(@NotNull IFontRenderer var1, @NotNull IItemStack var2, int var3, int var4);

    public void renderItemAndEffectIntoGUI2(@NotNull ItemStack var1, int var2, int var3);

    public void renderItemOverlays2(@NotNull IFontRenderer var1, @NotNull ItemStack var2, int var3, int var4);
}
