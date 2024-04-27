/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.GuiScreenResourcePacks
 *  net.minecraft.client.resources.IResourcePack
 *  net.minecraft.client.resources.ResourcePackRepository
 *  net.minecraft.client.resources.ResourcePackRepository$Entry
 */
package net.dev.important.injection.forge.mixins.bugfixes;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreenResourcePacks;
import net.minecraft.client.resources.IResourcePack;
import net.minecraft.client.resources.ResourcePackRepository;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={GuiScreenResourcePacks.class})
public class GuiScreenResourcePacksMixin_ClearHandles {
    @Inject(method={"actionPerformed"}, at={@At(value="INVOKE", target="Ljava/util/Collections;reverse(Ljava/util/List;)V", remap=false)})
    private void patcher$clearHandles(CallbackInfo ci) {
        ResourcePackRepository repository = Minecraft.func_71410_x().func_110438_M();
        for (ResourcePackRepository.Entry entry : repository.func_110613_c()) {
            IResourcePack current = repository.func_148530_e();
            if (current != null && entry.func_110515_d().equals(current.func_130077_b())) continue;
            entry.func_110517_b();
        }
    }
}

