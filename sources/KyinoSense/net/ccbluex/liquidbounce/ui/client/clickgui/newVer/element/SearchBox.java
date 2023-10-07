/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.FontRenderer
 *  net.minecraft.client.gui.GuiTextField
 */
package net.ccbluex.liquidbounce.ui.client.clickgui.newVer.element;

import kotlin.Metadata;
import net.ccbluex.liquidbounce.ui.font.Fonts;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiTextField;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, xi=2, d1={"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0006\n\u0002\u0010\u000b\n\u0000\u0018\u00002\u00020\u0001B-\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003\u0012\u0006\u0010\u0005\u001a\u00020\u0003\u0012\u0006\u0010\u0006\u001a\u00020\u0003\u0012\u0006\u0010\u0007\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\bJ\b\u0010\t\u001a\u00020\nH\u0016\u00a8\u0006\u000b"}, d2={"Lnet/ccbluex/liquidbounce/ui/client/clickgui/newVer/element/SearchBox;", "Lnet/minecraft/client/gui/GuiTextField;", "componentId", "", "x", "y", "width", "height", "(IIIII)V", "getEnableBackgroundDrawing", "", "KyinoClient"})
public final class SearchBox
extends GuiTextField {
    public boolean func_146181_i() {
        return false;
    }

    public SearchBox(int componentId, int x, int y, int width, int height) {
        super(componentId, (FontRenderer)Fonts.font40, x, y, width, height);
    }
}

