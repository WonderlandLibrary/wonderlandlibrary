/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.inventory.EntityEquipmentSlot
 *  net.minecraft.item.ItemArmor$ArmorMaterial
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.injection.backend;

import net.ccbluex.liquidbounce.api.minecraft.minecraft.IArmorMaterial;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import org.jetbrains.annotations.Nullable;

public final class ArmorMaterialImpl
implements IArmorMaterial {
    private final ItemArmor.ArmorMaterial wrapped;

    @Override
    public int getEnchantability() {
        return this.wrapped.func_78045_a();
    }

    public ArmorMaterialImpl(ItemArmor.ArmorMaterial armorMaterial) {
        this.wrapped = armorMaterial;
    }

    @Override
    public int getDurability(int n) {
        EntityEquipmentSlot entityEquipmentSlot;
        int n2 = n;
        ItemArmor.ArmorMaterial armorMaterial = this.wrapped;
        boolean bl = false;
        switch (n2) {
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
                throw (Throwable)new IllegalArgumentException("Invalid armorType " + n2);
            }
        }
        EntityEquipmentSlot entityEquipmentSlot2 = entityEquipmentSlot;
        return armorMaterial.func_78046_a(entityEquipmentSlot2);
    }

    public boolean equals(@Nullable Object object) {
        return object instanceof ArmorMaterialImpl && ((ArmorMaterialImpl)object).wrapped == this.wrapped;
    }

    @Override
    public int getDamageReductionAmount(int n) {
        EntityEquipmentSlot entityEquipmentSlot;
        int n2 = n;
        ItemArmor.ArmorMaterial armorMaterial = this.wrapped;
        boolean bl = false;
        switch (n2) {
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
                throw (Throwable)new IllegalArgumentException("Invalid armorType " + n2);
            }
        }
        EntityEquipmentSlot entityEquipmentSlot2 = entityEquipmentSlot;
        return armorMaterial.func_78044_b(entityEquipmentSlot2);
    }

    public final ItemArmor.ArmorMaterial getWrapped() {
        return this.wrapped;
    }
}

