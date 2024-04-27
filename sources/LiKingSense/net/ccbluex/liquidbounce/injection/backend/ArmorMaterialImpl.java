/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.Metadata
 *  kotlin.jvm.internal.Intrinsics
 *  net.minecraft.inventory.EntityEquipmentSlot
 *  net.minecraft.item.ItemArmor$ArmorMaterial
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.injection.backend;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.api.minecraft.minecraft.IArmorMaterial;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u0000(\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0005\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0002\b\u0004\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0013\u0010\u000b\u001a\u00020\f2\b\u0010\r\u001a\u0004\u0018\u00010\u000eH\u0096\u0002J\u0010\u0010\u000f\u001a\u00020\u00062\u0006\u0010\u0010\u001a\u00020\u0006H\u0016J\u0010\u0010\u0011\u001a\u00020\u00062\u0006\u0010\u0010\u001a\u00020\u0006H\u0016R\u0014\u0010\u0005\u001a\u00020\u00068VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b\u0007\u0010\bR\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\t\u0010\n\u00a8\u0006\u0012"}, d2={"Lnet/ccbluex/liquidbounce/injection/backend/ArmorMaterialImpl;", "Lnet/ccbluex/liquidbounce/api/minecraft/minecraft/IArmorMaterial;", "wrapped", "Lnet/minecraft/item/ItemArmor$ArmorMaterial;", "(Lnet/minecraft/item/ItemArmor$ArmorMaterial;)V", "enchantability", "", "getEnchantability", "()I", "getWrapped", "()Lnet/minecraft/item/ItemArmor$ArmorMaterial;", "equals", "", "other", "", "getDamageReductionAmount", "type", "getDurability", "LiKingSense"})
public final class ArmorMaterialImpl
implements IArmorMaterial {
    @NotNull
    private final ItemArmor.ArmorMaterial wrapped;

    @Override
    public int getEnchantability() {
        return this.wrapped.func_78045_a();
    }

    /*
     * WARNING - void declaration
     */
    @Override
    public int getDamageReductionAmount(int type) {
        EntityEquipmentSlot entityEquipmentSlot;
        void $this$toEntityEquipmentSlot$iv;
        int n = type;
        ItemArmor.ArmorMaterial armorMaterial = this.wrapped;
        boolean $i$f$toEntityEquipmentSlot = false;
        switch ($this$toEntityEquipmentSlot$iv) {
            case 0: {
                entityEquipmentSlot = EntityEquipmentSlot.FEET;
                break;
            }
            case 1: {
                entityEquipmentSlot = EntityEquipmentSlot.LEGS;
                break;
            }
            case 2: {
                entityEquipmentSlot = EntityEquipmentSlot.CHEST;
                break;
            }
            case 3: {
                entityEquipmentSlot = EntityEquipmentSlot.HEAD;
                break;
            }
            case 4: {
                entityEquipmentSlot = EntityEquipmentSlot.MAINHAND;
                break;
            }
            case 5: {
                entityEquipmentSlot = EntityEquipmentSlot.OFFHAND;
                break;
            }
            default: {
                throw (Throwable)new IllegalArgumentException("Invalid armorType " + (int)$this$toEntityEquipmentSlot$iv);
            }
        }
        EntityEquipmentSlot entityEquipmentSlot2 = entityEquipmentSlot;
        return armorMaterial.func_78044_b(entityEquipmentSlot2);
    }

    /*
     * WARNING - void declaration
     */
    @Override
    public int getDurability(int type) {
        EntityEquipmentSlot entityEquipmentSlot;
        void $this$toEntityEquipmentSlot$iv;
        int n = type;
        ItemArmor.ArmorMaterial armorMaterial = this.wrapped;
        boolean $i$f$toEntityEquipmentSlot = false;
        switch ($this$toEntityEquipmentSlot$iv) {
            case 0: {
                entityEquipmentSlot = EntityEquipmentSlot.FEET;
                break;
            }
            case 1: {
                entityEquipmentSlot = EntityEquipmentSlot.LEGS;
                break;
            }
            case 2: {
                entityEquipmentSlot = EntityEquipmentSlot.CHEST;
                break;
            }
            case 3: {
                entityEquipmentSlot = EntityEquipmentSlot.HEAD;
                break;
            }
            case 4: {
                entityEquipmentSlot = EntityEquipmentSlot.MAINHAND;
                break;
            }
            case 5: {
                entityEquipmentSlot = EntityEquipmentSlot.OFFHAND;
                break;
            }
            default: {
                throw (Throwable)new IllegalArgumentException("Invalid armorType " + (int)$this$toEntityEquipmentSlot$iv);
            }
        }
        EntityEquipmentSlot entityEquipmentSlot2 = entityEquipmentSlot;
        return armorMaterial.func_78046_a(entityEquipmentSlot2);
    }

    public boolean equals(@Nullable Object other) {
        return other instanceof ArmorMaterialImpl && ((ArmorMaterialImpl)other).wrapped == this.wrapped;
    }

    @NotNull
    public final ItemArmor.ArmorMaterial getWrapped() {
        return this.wrapped;
    }

    public ArmorMaterialImpl(@NotNull ItemArmor.ArmorMaterial wrapped) {
        Intrinsics.checkParameterIsNotNull((Object)wrapped, (String)"wrapped");
        this.wrapped = wrapped;
    }
}

