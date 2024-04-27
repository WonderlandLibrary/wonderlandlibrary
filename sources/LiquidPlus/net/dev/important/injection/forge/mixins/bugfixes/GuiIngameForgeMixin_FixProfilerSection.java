/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.GuiIngame
 *  net.minecraftforge.client.GuiIngameForge
 */
package net.dev.important.injection.forge.mixins.bugfixes;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiIngame;
import net.minecraftforge.client.GuiIngameForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={GuiIngameForge.class})
public abstract class GuiIngameForgeMixin_FixProfilerSection
extends GuiIngame {
    public GuiIngameForgeMixin_FixProfilerSection(Minecraft mcIn) {
        super(mcIn);
    }

    @Inject(method={"renderChat"}, slice={@Slice(from=@At(value="INVOKE", target="Lnet/minecraftforge/fml/common/eventhandler/EventBus;post(Lnet/minecraftforge/fml/common/eventhandler/Event;)Z", ordinal=0, remap=false))}, at={@At(value="RETURN", ordinal=0)}, remap=false)
    private void patcher$fixProfilerSectionNotEnding(int width, int height, CallbackInfo ci) {
        if (this.field_73839_d.field_71424_I.func_76322_c().endsWith("chat")) {
            this.field_73839_d.field_71424_I.func_76319_b();
        }
    }
}

