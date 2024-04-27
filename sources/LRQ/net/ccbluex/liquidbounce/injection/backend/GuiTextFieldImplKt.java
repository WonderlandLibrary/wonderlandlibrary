/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.GuiTextField
 */
package net.ccbluex.liquidbounce.injection.backend;

import net.ccbluex.liquidbounce.api.minecraft.client.gui.IGuiTextField;
import net.ccbluex.liquidbounce.injection.backend.GuiTextFieldImpl;
import net.minecraft.client.gui.GuiTextField;

public final class GuiTextFieldImplKt {
    public static final GuiTextField unwrap(IGuiTextField $this$unwrap) {
        int $i$f$unwrap = 0;
        return ((GuiTextFieldImpl)$this$unwrap).getWrapped();
    }

    public static final IGuiTextField wrap(GuiTextField $this$wrap) {
        int $i$f$wrap = 0;
        return new GuiTextFieldImpl($this$wrap);
    }
}

