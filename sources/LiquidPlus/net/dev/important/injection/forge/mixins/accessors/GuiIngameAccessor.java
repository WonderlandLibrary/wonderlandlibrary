/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.GuiIngame
 */
package net.dev.important.injection.forge.mixins.accessors;

import net.minecraft.client.gui.GuiIngame;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(value={GuiIngame.class})
public interface GuiIngameAccessor {
    @Accessor
    public String getDisplayedTitle();

    @Accessor
    public String getDisplayedSubTitle();

    @Accessor
    public void setDisplayedTitle(String var1);

    @Accessor
    public void setDisplayedSubTitle(String var1);

    @Invoker
    public boolean callShowCrosshair();
}

