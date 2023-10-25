package me.rhys.client.module.render;

import me.rhys.base.module.Module;
import me.rhys.base.module.data.Category;
import me.rhys.base.module.setting.manifest.Name;
import me.rhys.client.Manager;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.EntityRenderer;

public class ClickGui extends Module {
  @Name("Blur")
  public boolean blur = true;
  
  private boolean loadedShader;
  
  public ClickGui(String name, String description, Category category, int keyCode) {
    super(name, description, category, keyCode);
    setHidden(true);
  }
  
  public void onEnable() {
    this.mc.displayGuiScreen((GuiScreen)Manager.UI.CLICK);
    if (this.blur && 
      !this.loadedShader) {
      this.loadedShader = true;
      this.mc.entityRenderer.loadShader(EntityRenderer.shaderResourceLocations[18]);
    } 
  }
  
  public void onDisable() {
    if (this.loadedShader) {
      this.loadedShader = false;
      this.mc.entityRenderer.theShaderGroup = null;
    } 
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\me\rhys\client\module\render\ClickGui.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */