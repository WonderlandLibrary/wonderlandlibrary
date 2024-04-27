/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.TypeCastException
 *  kotlin.jvm.functions.Function1
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
import kotlin.jvm.functions.Function1;
import net.ccbluex.liquidbounce.api.minecraft.client.gui.IFontRenderer;
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

public class GuiScreenImpl
extends GuiImpl
implements IGuiScreen {
    @Override
    public void superMouseClicked(int n, int n2, int n3) {
        Gui gui = this.getWrapped();
        if (gui == null) {
            throw new TypeCastException("null cannot be cast to non-null type net.ccbluex.liquidbounce.injection.backend.utils.GuiScreenWrapper");
        }
        ((GuiScreenWrapper)gui).superMouseClicked(n, n2, n3);
    }

    @Override
    public void superKeyTyped(char c, int n) {
        Gui gui = this.getWrapped();
        if (gui == null) {
            throw new TypeCastException("null cannot be cast to non-null type net.ccbluex.liquidbounce.injection.backend.utils.GuiScreenWrapper");
        }
        ((GuiScreenWrapper)gui).superKeyTyped(c, n);
    }

    @Override
    public void drawBackground(int n) {
        ((GuiScreen)this.getWrapped()).func_146278_c(n);
    }

    @Override
    public void superMouseReleased(int n, int n2, int n3) {
        Gui gui = this.getWrapped();
        if (gui == null) {
            throw new TypeCastException("null cannot be cast to non-null type net.ccbluex.liquidbounce.injection.backend.utils.GuiScreenWrapper");
        }
        ((GuiScreenWrapper)gui).superMouseReleased(n, n2, n3);
    }

    @Override
    public IGuiGameOver asGuiGameOver() {
        Gui gui = this.getWrapped();
        if (gui == null) {
            throw new TypeCastException("null cannot be cast to non-null type net.minecraft.client.gui.GuiGameOver");
        }
        return new GuiGameOverImpl((GuiGameOver)gui);
    }

    @Override
    public List getButtonList() {
        return new WrappedMutableList(((GuiScreen)this.getWrapped()).field_146292_n, (Function1)buttonList.1.INSTANCE, (Function1)buttonList.2.INSTANCE);
    }

    @Override
    public void drawDefaultBackground() {
        ((GuiScreen)this.getWrapped()).func_146276_q_();
    }

    @Override
    public void superHandleMouseInput() {
        Gui gui = this.getWrapped();
        if (gui == null) {
            throw new TypeCastException("null cannot be cast to non-null type net.ccbluex.liquidbounce.injection.backend.utils.GuiScreenWrapper");
        }
        ((GuiScreenWrapper)gui).superHandleMouseInput();
    }

    @Override
    public int getWidth() {
        return ((GuiScreen)this.getWrapped()).field_146294_l;
    }

    public void setWidth(int n) {
        ((GuiScreen)this.getWrapped()).field_146294_l = n;
    }

    @Override
    public void superDrawScreen(int n, int n2, float f) {
        Gui gui = this.getWrapped();
        if (gui == null) {
            throw new TypeCastException("null cannot be cast to non-null type net.ccbluex.liquidbounce.injection.backend.utils.GuiScreenWrapper");
        }
        ((GuiScreenWrapper)gui).superDrawScreen(n, n2, f);
    }

    @Override
    public int getHeight() {
        return ((GuiScreen)this.getWrapped()).field_146295_m;
    }

    public boolean equals(@Nullable Object object) {
        return object instanceof GuiScreenImpl && ((GuiScreen)((GuiScreenImpl)object).getWrapped()).equals((GuiScreen)this.getWrapped());
    }

    @Override
    public IFontRenderer getFontRendererObj() {
        FontRenderer fontRenderer = ((GuiScreen)this.getWrapped()).field_146289_q;
        boolean bl = false;
        return new FontRendererImpl(fontRenderer);
    }

    public void setHeight(int n) {
        ((GuiScreen)this.getWrapped()).field_146295_m = n;
    }

    public GuiScreenImpl(GuiScreen guiScreen) {
        super((Gui)guiScreen);
    }

    @Override
    public IGuiChest asGuiChest() {
        Gui gui = this.getWrapped();
        if (gui == null) {
            throw new TypeCastException("null cannot be cast to non-null type net.minecraft.client.gui.inventory.GuiChest");
        }
        return new GuiChestImpl((GuiChest)gui);
    }

    @Override
    public IGuiContainer asGuiContainer() {
        Gui gui = this.getWrapped();
        if (gui == null) {
            throw new TypeCastException("null cannot be cast to non-null type net.minecraft.client.gui.inventory.GuiContainer");
        }
        return new GuiContainerImpl((GuiContainer)gui);
    }
}

