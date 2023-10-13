/*
 * Decompiled with CFR 0.150.
 */
package markgg.modules.render;

import markgg.modules.Module;
import markgg.ui.GUIMethod;

public class ClickGUI
extends Module {
    public ClickGUI() {
        super("ClickGUI", 54, Module.Category.RENDER);
    }

    @Override
    public void onEnable() {
        this.mc.displayGuiScreen(new GUIMethod());
        this.toggle();
    }
}

