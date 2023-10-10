/*
 * Decompiled with CFR 0.152.
 */
package tk.rektsky.module.impl.rektsky;

import tk.rektsky.module.Category;
import tk.rektsky.module.Module;

public class GuiBlur
extends Module {
    public GuiBlur() {
        super("GuiBlur", "Blurs the GUI background", Category.RENDER);
        this.toggle();
    }
}

