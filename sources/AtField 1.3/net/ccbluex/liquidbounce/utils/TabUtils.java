/*
 * Decompiled with CFR 0.152.
 */
package net.ccbluex.liquidbounce.utils;

import net.ccbluex.liquidbounce.api.minecraft.client.gui.IGuiTextField;

public final class TabUtils {
    public static void tab(IGuiTextField ... iGuiTextFieldArray) {
        for (int i = 0; i < iGuiTextFieldArray.length; ++i) {
            IGuiTextField iGuiTextField = iGuiTextFieldArray[i];
            if (!iGuiTextField.isFocused()) continue;
            iGuiTextField.setFocused(false);
            if (++i >= iGuiTextFieldArray.length) {
                i = 0;
            }
            iGuiTextFieldArray[i].setFocused(true);
            break;
        }
    }
}

