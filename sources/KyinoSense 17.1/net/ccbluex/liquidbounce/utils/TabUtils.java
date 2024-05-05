/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.GuiTextField
 */
package net.ccbluex.liquidbounce.utils;

import net.minecraft.client.gui.GuiTextField;

public final class TabUtils {
    public static void tab(GuiTextField ... textFields) {
        for (int i = 0; i < textFields.length; ++i) {
            GuiTextField textField = textFields[i];
            if (!textField.func_146206_l()) continue;
            textField.func_146195_b(false);
            if (++i >= textFields.length) {
                i = 0;
            }
            textFields[i].func_146195_b(true);
            break;
        }
    }
}

