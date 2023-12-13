/*
 * Decompiled with CFR 0.152.
 */
package tk.rektsky.module.impl.player;

import net.minecraft.item.ItemSword;
import tk.rektsky.module.Category;
import tk.rektsky.module.Module;

public class NoSlow
extends Module {
    public NoSlow() {
        super("NoSlow", "Stop you from slowing done when you're blocking sword", Category.PLAYER);
    }

    public boolean doSlowDown() {
        return this.mc.thePlayer != null && this.mc.theWorld != null && this.mc.thePlayer.getHeldItem() != null && !(this.mc.thePlayer.getHeldItem().getItem() instanceof ItemSword) || !this.isToggled();
    }
}

