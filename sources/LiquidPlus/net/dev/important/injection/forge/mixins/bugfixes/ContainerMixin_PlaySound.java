/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.audio.ISound
 *  net.minecraft.client.audio.PositionedSoundRecord
 *  net.minecraft.inventory.Container
 *  net.minecraft.inventory.ContainerPlayer
 *  net.minecraft.inventory.Slot
 *  net.minecraft.item.ItemArmor
 *  net.minecraft.item.ItemStack
 *  net.minecraft.util.ResourceLocation
 */
package net.dev.important.injection.forge.mixins.bugfixes;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={Container.class})
public class ContainerMixin_PlaySound {
    @Inject(method={"putStackInSlot"}, at={@At(value="HEAD")})
    private void patcher$playArmorBreakingSound(int slotID, ItemStack stack, CallbackInfo ci) {
        ItemStack slotStack;
        Slot slot;
        if (!Minecraft.func_71410_x().field_71441_e.field_72995_K || stack != null) {
            return;
        }
        Container container = (Container)this;
        if (slotID >= 5 && slotID <= 8 && container instanceof ContainerPlayer && (slot = container.func_75139_a(slotID)) != null && (slotStack = slot.func_75211_c()) != null && slotStack.func_77973_b() instanceof ItemArmor && slotStack.func_77952_i() > slotStack.func_77958_k() - 2) {
            Minecraft.func_71410_x().func_147118_V().func_147682_a((ISound)PositionedSoundRecord.func_147673_a((ResourceLocation)new ResourceLocation("random.break")));
        }
    }
}

