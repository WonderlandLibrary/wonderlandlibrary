package me.rhys.client.module.render;

import me.rhys.base.event.data.EventTarget;
import me.rhys.base.module.Module;
import me.rhys.base.module.data.Category;
import me.rhys.base.module.setting.manifest.Clamp;
import me.rhys.base.module.setting.manifest.Name;
import net.minecraft.client.renderer.GlStateManager;

public class ItemScale extends Module {
  @Name("Size")
  @Clamp(min = 0.0D, max = 10.0D)
  public double scale;
  
  public ItemScale(String name, String description, Category category, int keyCode) {
    super(name, description, category, keyCode);
    this.scale = 5.0D;
  }
  
  @EventTarget
  public void onRenderItem(ItemScale event) {
    GlStateManager.scale(this.scale, this.scale, this.scale);
    if (this.scale < 0.5999D)
      GlStateManager.translate(1.0D * this.scale, 1.0D * this.scale, 1.0D * this.scale); 
  }
  
  @EventTarget
  public void onPostRenderItem(ItemScale event) {
    GlStateManager.scale(this.scale * 2.0D, this.scale * 2.0D, this.scale * 2.0D);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\me\rhys\client\module\render\ItemScale.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */