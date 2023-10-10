/*
 * Decompiled with CFR 0.152.
 */
package tk.rektsky.module.impl.render;

import tk.rektsky.module.Category;
import tk.rektsky.module.Module;

public class NoRender
extends Module {
    public NoRender() {
        super("NoRender", "Doesn't render some things", Category.RENDER);
        this.rawSetToggled(true);
    }
}

