package me.rhys.base.module;

import java.util.List;
import me.rhys.base.Lite;
import me.rhys.base.module.data.Category;
import me.rhys.base.module.data.ModuleData;
import me.rhys.base.module.data.Toggleable;
import me.rhys.base.util.container.Container;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.util.EnumChatFormatting;

public abstract class Module extends Container<ModuleMode<? extends Module>> implements Toggleable {
  private ModuleData data;
  
  public void setMc(Minecraft mc) {
    this.mc = mc;
  }
  
  public void setHidden(boolean hidden) {
    this.hidden = hidden;
  }
  
  public void setData(ModuleData data) {
    this.data = data;
  }
  
  public ModuleData getData() {
    return this.data;
  }
  
  protected Minecraft mc = Minecraft.getMinecraft();
  
  private boolean hidden;
  
  public Minecraft getMc() {
    return this.mc;
  }
  
  public boolean isHidden() {
    return this.hidden;
  }
  
  public Module(String name, String description, Category category, int keyCode) {
    this.data = new ModuleData(name, description, category, keyCode, -1, false);
  }
  
  public Module(String name, String description, Category category) {
    this(name, description, category, 0);
  }
  
  public void add(ModuleMode<? extends Module> item) {
    super.add(item);
    if (this.data.getCurrentMode() == -1)
      setCurrentMode(0); 
  }
  
  public void toggle(boolean enabled) {
    this.data.setEnabled(enabled);
    Minecraft minecraft = Minecraft.getMinecraft();
    EntityPlayerSP player = minecraft.thePlayer;
    WorldClient world = minecraft.theWorld;
    boolean isWorldLoaded = (player != null && world != null);
    ModuleMode<? extends Module> currentMode = getCurrentMode();
    boolean isModeValid = (currentMode != null);
    if (this.data.isEnabled()) {
      Lite.EVENT_BUS.register(this);
      if (isModeValid)
        Lite.EVENT_BUS.register(currentMode); 
      if (isWorldLoaded) {
        onEnable();
        if (isModeValid)
          currentMode.onEnable(); 
      } 
    } else {
      Lite.EVENT_BUS.unRegister(this);
      if (isModeValid)
        Lite.EVENT_BUS.unRegister(currentMode); 
      if (isWorldLoaded) {
        onDisable();
        if (isModeValid)
          currentMode.onDisable(); 
      } 
    } 
    if (isWorldLoaded)
      onToggle(); 
  }
  
  public void toggle() {
    toggle(!this.data.isEnabled());
  }
  
  public void setCurrentMode(String mode) {
    if (mode == null)
      return; 
    setCurrentMode(indexOf(find(moduleMode -> moduleMode.getName().equalsIgnoreCase(mode))));
  }
  
  public void setCurrentMode(int modeIndex) {
    if (this.data.getCurrentMode() == modeIndex || modeIndex == -1)
      return; 
    Minecraft minecraft = Minecraft.getMinecraft();
    EntityPlayerSP player = minecraft.thePlayer;
    WorldClient world = minecraft.theWorld;
    boolean isWorldLoaded = (player != null && world != null);
    if (this.data.getCurrentMode() != -1) {
      ModuleMode<? extends Module> moduleMode = getCurrentMode();
      if (isWorldLoaded)
        moduleMode.onDisable(); 
      Lite.EVENT_BUS.unRegister(moduleMode);
    } 
    this.data.setCurrentMode(modeIndex);
    ModuleMode<? extends Module> currentMode = getCurrentMode();
    if (this.data.isEnabled()) {
      if (isWorldLoaded)
        currentMode.onEnable(); 
      Lite.EVENT_BUS.register(currentMode);
    } 
  }
  
  public ModuleMode<? extends Module> getCurrentMode() {
    if (this.data.getCurrentMode() == -1 || isEmpty())
      return null; 
    ModuleMode<? extends Module> moduleMode = (ModuleMode<? extends Module>)get(this.data.getCurrentMode());
    if (moduleMode != null)
      return moduleMode; 
    return (ModuleMode<? extends Module>)get(0);
  }
  
  public String getDisplayName() {
    return this.data.getName() + ((getCurrentMode() != null) ? (EnumChatFormatting.GRAY + " " + getCurrentMode().getName()) : "");
  }
  
  public List<String> getModeNames() {
    return null;
  }
  
  public EntityPlayerSP player() {
    return this.mc.thePlayer;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\me\rhys\base\module\Module.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */