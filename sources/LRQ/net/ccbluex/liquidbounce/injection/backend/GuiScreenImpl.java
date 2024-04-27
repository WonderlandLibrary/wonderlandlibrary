/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.TypeCastException
 *  net.minecraft.client.gui.FontRenderer
 *  net.minecraft.client.gui.Gui
 *  net.minecraft.client.gui.GuiGameOver
 *  net.minecraft.client.gui.GuiScreen
 *  net.minecraft.client.gui.inventory.GuiChest
 *  net.minecraft.client.gui.inventory.GuiContainer
 *  org.jetbrains.annotations.Nullable
 */
package net.ccbluex.liquidbounce.injection.backend;

import java.util.List;
import kotlin.TypeCastException;
import net.ccbluex.liquidbounce.api.minecraft.client.gui.IFontRenderer;
import net.ccbluex.liquidbounce.api.minecraft.client.gui.IGuiButton;
import net.ccbluex.liquidbounce.api.minecraft.client.gui.IGuiGameOver;
import net.ccbluex.liquidbounce.api.minecraft.client.gui.IGuiScreen;
import net.ccbluex.liquidbounce.api.minecraft.client.gui.inventory.IGuiChest;
import net.ccbluex.liquidbounce.api.minecraft.client.gui.inventory.IGuiContainer;
import net.ccbluex.liquidbounce.api.util.WrappedMutableList;
import net.ccbluex.liquidbounce.injection.backend.FontRendererImpl;
import net.ccbluex.liquidbounce.injection.backend.GuiChestImpl;
import net.ccbluex.liquidbounce.injection.backend.GuiContainerImpl;
import net.ccbluex.liquidbounce.injection.backend.GuiGameOverImpl;
import net.ccbluex.liquidbounce.injection.backend.GuiImpl;
import net.ccbluex.liquidbounce.injection.backend.GuiScreenImpl;
import net.ccbluex.liquidbounce.injection.backend.utils.GuiScreenWrapper;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGameOver;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.client.gui.inventory.GuiContainer;
import org.jetbrains.annotations.Nullable;

public class GuiScreenImpl<T extends GuiScreen>
extends GuiImpl<T>
implements IGuiScreen {
    @Override
    public IFontRenderer getFontRendererObj() {
        FontRenderer $this$wrap$iv = ((GuiScreen)this.getWrapped()).field_146289_q;
        boolean $i$f$wrap = false;
        return new FontRendererImpl($this$wrap$iv);
    }

    @Override
    public List<IGuiButton> getButtonList() {
        return new WrappedMutableList(((GuiScreen)this.getWrapped()).field_146292_n, buttonList.1.INSTANCE, buttonList.2.INSTANCE);
    }

    @Override
    public IGuiContainer asGuiContainer() {
        Object t = this.getWrapped();
        if (t == null) {
            throw new TypeCastException("null cannot be cast to non-null type net.minecraft.client.gui.inventory.GuiContainer");
        }
        return new GuiContainerImpl<GuiContainer>((GuiContainer)t);
    }

    @Override
    public IGuiGameOver asGuiGameOver() {
        Object t = this.getWrapped();
        if (t == null) {
            throw new TypeCastException("null cannot be cast to non-null type net.minecraft.client.gui.GuiGameOver");
        }
        return new GuiGameOverImpl<GuiGameOver>((GuiGameOver)t);
    }

    @Override
    public IGuiChest asGuiChest() {
        Object t = this.getWrapped();
        if (t == null) {
            throw new TypeCastException("null cannot be cast to non-null type net.minecraft.client.gui.inventory.GuiChest");
        }
        return new GuiChestImpl<GuiChest>((GuiChest)t);
    }

    @Override
    public void superMouseReleased(int mouseX, int mouseY, int state) {
        Object t = this.getWrapped();
        if (t == null) {
            throw new TypeCastException("null cannot be cast to non-null type net.ccbluex.liquidbounce.injection.backend.utils.GuiScreenWrapper");
        }
        ((GuiScreenWrapper)((Object)t)).superMouseReleased(mouseX, mouseY, state);
    }

    @Override
    public void drawBackground(int i) {
        ((GuiScreen)this.getWrapped()).func_146278_c(i);
    }

    @Override
    public void drawDefaultBackground() {
        ((GuiScreen)this.getWrapped()).func_146276_q_();
    }

    @Override
    public void superKeyTyped(char typedChar, int keyCode) {
        Object t = this.getWrapped();
        if (t == null) {
            throw new TypeCastException("null cannot be cast to non-null type net.ccbluex.liquidbounce.injection.backend.utils.GuiScreenWrapper");
        }
        ((GuiScreenWrapper)((Object)t)).superKeyTyped(typedChar, keyCode);
    }

    @Override
    public void superHandleMouseInput() {
        Object t = this.getWrapped();
        if (t == null) {
            throw new TypeCastException("null cannot be cast to non-null type net.ccbluex.liquidbounce.injection.backend.utils.GuiScreenWrapper");
        }
        ((GuiScreenWrapper)((Object)t)).superHandleMouseInput();
    }

    @Override
    public void superMouseClicked(int mouseX, int mouseY, int mouseButton) {
        Object t = this.getWrapped();
        if (t == null) {
            throw new TypeCastException("null cannot be cast to non-null type net.ccbluex.liquidbounce.injection.backend.utils.GuiScreenWrapper");
        }
        ((GuiScreenWrapper)((Object)t)).superMouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    public void superDrawScreen(int mouseX, int mouseY, float partialTicks) {
        Object t = this.getWrapped();
        if (t == null) {
            throw new TypeCastException("null cannot be cast to non-null type net.ccbluex.liquidbounce.injection.backend.utils.GuiScreenWrapper");
        }
        ((GuiScreenWrapper)((Object)t)).superDrawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    public int getHeight() {
        return ((GuiScreen)this.getWrapped()).field_146295_m;
    }

    public void setHeight(int value) {
        ((GuiScreen)this.getWrapped()).field_146295_m = value;
    }

    @Override
    public int getWidth() {
        return ((GuiScreen)this.getWrapped()).field_146294_l;
    }

    public void setWidth(int value) {
        ((GuiScreen)this.getWrapped()).field_146294_l = value;
    }

    public boolean equals(@Nullable Object other) {
        return other instanceof GuiScreenImpl && ((GuiScreen)((GuiScreenImpl)other).getWrapped()).equals((GuiScreen)this.getWrapped());
    }

    public GuiScreenImpl(T wrapped) {
        super((Gui)wrapped);
    }
}

