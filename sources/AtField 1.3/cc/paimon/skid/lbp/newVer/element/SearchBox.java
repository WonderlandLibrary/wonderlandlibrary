/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.TypeCastException
 *  net.minecraft.client.gui.GuiTextField
 */
package cc.paimon.skid.lbp.newVer.element;

import kotlin.TypeCastException;
import net.ccbluex.liquidbounce.api.minecraft.client.gui.IFontRenderer;
import net.ccbluex.liquidbounce.injection.backend.FontRendererImpl;
import net.ccbluex.liquidbounce.ui.font.Fonts;
import net.minecraft.client.gui.GuiTextField;

public final class SearchBox
extends GuiTextField {
    public SearchBox(int n, int n2, int n3, int n4, int n5) {
        IFontRenderer iFontRenderer = Fonts.posterama40;
        if (iFontRenderer == null) {
            throw new TypeCastException("null cannot be cast to non-null type net.ccbluex.liquidbounce.injection.backend.FontRendererImpl");
        }
        super(n, ((FontRendererImpl)iFontRenderer).getWrapped(), n2, n3, n4, n5);
    }

    public boolean func_146181_i() {
        return false;
    }
}

