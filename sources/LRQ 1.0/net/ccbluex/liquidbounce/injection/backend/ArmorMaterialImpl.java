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

    public final ItemArmor.ArmorMaterial getWrapped() {
        return this.wrapped;
    }

    public ArmorMaterialImpl(ItemArmor.ArmorMaterial wrapped) {
        this.wrapped = wrapped;
    }
}

