/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.GuiMultiplayer
 *  net.minecraft.client.gui.GuiScreen
 *  net.minecraft.client.multiplayer.ServerData
 */
package net.dev.important.injection.forge.mixins.accessors;

import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.multiplayer.ServerData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value={GuiMultiplayer.class})
public interface GuiMultiplayerAccessor {
    @Accessor
    public void setParentScreen(GuiScreen var1);

    @Accessor
    public void setDirectConnect(boolean var1);

    @Accessor
    public void setSelectedServer(ServerData var1);
}

