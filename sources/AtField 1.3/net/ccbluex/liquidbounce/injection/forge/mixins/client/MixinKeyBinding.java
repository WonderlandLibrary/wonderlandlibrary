/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.settings.KeyBinding
 *  net.minecraftforge.client.settings.IKeyConflictContext
 *  net.minecraftforge.client.settings.KeyModifier
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Overwrite
 *  org.spongepowered.asm.mixin.Shadow
 */
package net.ccbluex.liquidbounce.injection.forge.mixins.client;

import net.ccbluex.liquidbounce.LiquidBounce;
import net.ccbluex.liquidbounce.features.module.modules.movement.InventoryMove;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.client.settings.IKeyConflictContext;
import net.minecraftforge.client.settings.KeyModifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value={KeyBinding.class})
public abstract class MixinKeyBinding {
    @Shadow
    public boolean field_74513_e;

    @Overwrite
    public boolean func_151470_d() {
        InventoryMove inventoryMove = (InventoryMove)LiquidBounce.moduleManager.get(InventoryMove.class);
        boolean bl = inventoryMove.getState() ? this.field_74513_e : this.field_74513_e && this.getKeyConflictContext().isActive() && this.getKeyModifier().isActive(this.getKeyConflictContext());
        return bl;
    }

    @Shadow
    public abstract KeyModifier getKeyModifier();

    @Shadow
    public abstract IKeyConflictContext getKeyConflictContext();
}

