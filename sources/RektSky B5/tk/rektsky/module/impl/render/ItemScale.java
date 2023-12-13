/*
 * Decompiled with CFR 0.152.
 */
package tk.rektsky.module.impl.render;

import net.minecraft.client.renderer.GlStateManager;
import org.greenrobot.eventbus.Subscribe;
import tk.rektsky.event.impl.ItemRenderInFirstPersonEvent;
import tk.rektsky.event.impl.PostItemRenderInFirstPersonEvent;
import tk.rektsky.module.Category;
import tk.rektsky.module.Module;
import tk.rektsky.module.settings.DoubleSetting;

public class ItemScale
extends Module {
    public DoubleSetting scale = new DoubleSetting("Scale", 0.2, 5.0, 0.5);

    public ItemScale() {
        super("ItemScale", "Let's you change the size of items on your hand", Category.RENDER);
    }

    @Subscribe
    public void onRenderItem(ItemRenderInFirstPersonEvent event) {
        GlStateManager.scale(this.scale.getValue(), this.scale.getValue(), this.scale.getValue());
        if (this.scale.getValue() < 0.5999) {
            GlStateManager.translate(1.0 * this.scale.getValue(), 1.0 * this.scale.getValue(), 1.0 * this.scale.getValue());
        }
    }

    @Subscribe
    public void onPostRenderItem(PostItemRenderInFirstPersonEvent event) {
        GlStateManager.scale(this.scale.getValue() * 2.0, this.scale.getValue() * 2.0, this.scale.getValue() * 2.0);
    }
}

