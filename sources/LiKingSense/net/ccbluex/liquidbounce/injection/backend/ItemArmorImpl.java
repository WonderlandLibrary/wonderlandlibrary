/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.Metadata
 *  kotlin.jvm.internal.Intrinsics
 *  net.minecraft.inventory.EntityEquipmentSlot
 *  net.minecraft.item.Item
 *  net.minecraft.item.ItemArmor
 *  net.minecraft.item.ItemArmor$ArmorMaterial
 *  net.minecraft.item.ItemStack
 *  org.jetbrains.annotations.NotNull
 */
package net.ccbluex.liquidbounce.injection.backend;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.api.minecraft.item.IItemArmor;
import net.ccbluex.liquidbounce.api.minecraft.item.IItemStack;
import net.ccbluex.liquidbounce.api.minecraft.minecraft.IArmorMaterial;
import net.ccbluex.liquidbounce.injection.backend.ArmorMaterialImpl;
import net.ccbluex.liquidbounce.injection.backend.ItemImpl;
import net.ccbluex.liquidbounce.injection.backend.ItemStackImpl;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u00002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u00012\u00020\u0003B\r\u0012\u0006\u0010\u0004\u001a\u00020\u0002\u00a2\u0006\u0002\u0010\u0005J\u0010\u0010\u0012\u001a\u00020\u000b2\u0006\u0010\u0013\u001a\u00020\u0014H\u0016R\u0014\u0010\u0006\u001a\u00020\u00078VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b\b\u0010\tR\u0014\u0010\n\u001a\u00020\u000b8VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b\f\u0010\rR\u0014\u0010\u000e\u001a\u00020\u000f8VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b\u0010\u0010\u0011\u00a8\u0006\u0015"}, d2={"Lnet/ccbluex/liquidbounce/injection/backend/ItemArmorImpl;", "Lnet/ccbluex/liquidbounce/injection/backend/ItemImpl;", "Lnet/minecraft/item/ItemArmor;", "Lnet/ccbluex/liquidbounce/api/minecraft/item/IItemArmor;", "wrapped", "(Lnet/minecraft/item/ItemArmor;)V", "armorMaterial", "Lnet/ccbluex/liquidbounce/api/minecraft/minecraft/IArmorMaterial;", "getArmorMaterial", "()Lnet/ccbluex/liquidbounce/api/minecraft/minecraft/IArmorMaterial;", "armorType", "", "getArmorType", "()I", "unlocalizedName", "", "getUnlocalizedName", "()Ljava/lang/String;", "getColor", "stack", "Lnet/ccbluex/liquidbounce/api/minecraft/item/IItemStack;", "LiKingSense"})
public final class ItemArmorImpl
extends ItemImpl<ItemArmor>
implements IItemArmor {
    @Override
    @NotNull
    public IArmorMaterial getArmorMaterial() {
        ItemArmor.ArmorMaterial armorMaterial = ((ItemArmor)this.getWrapped()).func_82812_d();
        Intrinsics.checkExpressionValueIsNotNull((Object)armorMaterial, (String)"wrapped.armorMaterial");
        ItemArmor.ArmorMaterial $this$wrap$iv = armorMaterial;
        boolean $i$f$wrap = false;
        return new ArmorMaterialImpl($this$wrap$iv);
    }

    @Override
    public int getArmorType() {
        EntityEquipmentSlot entityEquipmentSlot = ((ItemArmor)this.getWrapped()).field_77881_a;
        Intrinsics.checkExpressionValueIsNotNull((Object)entityEquipmentSlot, (String)"wrapped.armorType");
        return entityEquipmentSlot.func_188454_b();
    }

    @Override
    @NotNull
    public String getUnlocalizedName() {
        String string = ((ItemArmor)this.getWrapped()).func_77658_a();
        Intrinsics.checkExpressionValueIsNotNull((Object)string, (String)"wrapped.unlocalizedName");
        return string;
    }

    /*
     * WARNING - void declaration
     */
    @Override
    public int getColor(@NotNull IItemStack stack) {
        void $this$unwrap$iv;
        Intrinsics.checkParameterIsNotNull((Object)stack, (String)"stack");
        IItemStack iItemStack = stack;
        ItemArmor itemArmor = (ItemArmor)this.getWrapped();
        boolean $i$f$unwrap = false;
        ItemStack itemStack = ((ItemStackImpl)$this$unwrap$iv).getWrapped();
        return itemArmor.func_82814_b(itemStack);
    }

    public ItemArmorImpl(@NotNull ItemArmor wrapped) {
        Intrinsics.checkParameterIsNotNull((Object)wrapped, (String)"wrapped");
        super((Item)wrapped);
    }
}

