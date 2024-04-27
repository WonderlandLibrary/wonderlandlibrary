/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.gui.Gui
 */
package dev.sakura_starring.ui.button;

import dev.sakura_starring.ui.button.GuiButton;
import net.ccbluex.liquidbounce.api.minecraft.util.IResourceLocation;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.minecraft.client.gui.Gui;

public class GuiPictureButton
implements GuiButton {
    private boolean enabled = true;
    private final int y;
    private final int x;
    private final int height;
    private final int width;
    private final IResourceLocation image;

    @Override
    public boolean getEnabled() {
        return this.enabled;
    }

    @Override
    public void setEnabled(boolean bl) {
        this.enabled = bl;
    }

    @Override
    public void draw(int n, int n2, float f) {
        RenderUtils.drawImage(this.image, this.x, this.y, this.width, this.height);
        if (this.isHover(n, n2, this.x, this.y, this.width, this.height)) {
            Gui.func_73734_a((int)this.x, (int)this.y, (int)(this.x + this.width), (int)(this.y + this.height), (int)-1879048192);
        }
    }

    public GuiPictureButton(int n, int n2, int n3, int n4, IResourceLocation iResourceLocation) {
        this.x = n;
        this.y = n2;
        this.width = n3;
        this.height = n4;
        this.image = iResourceLocation;
    }
}

