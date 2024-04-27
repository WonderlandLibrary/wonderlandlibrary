/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.FontRenderer
 *  net.minecraftforge.fml.relauncher.Side
 *  net.minecraftforge.fml.relauncher.SideOnly
 */
package net.dev.important.injection.forge.mixins.render;

import net.dev.important.Client;
import net.dev.important.event.TextEvent;
import net.dev.important.patcher.ducks.FontRendererExt;
import net.dev.important.patcher.hooks.font.FontRendererHook;
import net.minecraft.client.gui.FontRenderer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@SideOnly(value=Side.CLIENT)
@Mixin(value={FontRenderer.class})
public class MixinFontRenderer
implements FontRendererExt {
    @Unique
    private final FontRendererHook patcher$fontRendererHook = new FontRendererHook((FontRenderer)this);

    @Overwrite
    public int func_78256_a(String text) {
        if (text == null || Client.eventManager == null) {
            return this.patcher$fontRendererHook.getStringWidth(text);
        }
        TextEvent textEvent = new TextEvent(text);
        Client.eventManager.callEvent(textEvent);
        return this.patcher$fontRendererHook.getStringWidth(textEvent.getText());
    }

    @Inject(method={"renderStringAtPos"}, at={@At(value="HEAD")}, cancellable=true)
    private void patcher$useOptimizedRendering(String text, boolean shadow, CallbackInfo ci) {
        if (this.patcher$fontRendererHook.renderStringAtPos(text, shadow)) {
            ci.cancel();
        }
    }

    @ModifyVariable(method={"renderString"}, at=@At(value="HEAD"), ordinal=0)
    private String renderString(String string) {
        if (string == null || Client.eventManager == null) {
            return string;
        }
        TextEvent textEvent = new TextEvent(string);
        Client.eventManager.callEvent(textEvent);
        return textEvent.getText();
    }

    @Override
    public FontRendererHook patcher$getFontRendererHook() {
        return this.patcher$fontRendererHook;
    }
}

