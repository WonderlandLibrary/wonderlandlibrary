package me.rhys.base.module;

import me.rhys.base.module.data.Toggleable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;

public class ModuleMode<T extends Module> implements Toggleable {
  protected Minecraft mc = Minecraft.getMinecraft();
  
  protected final String name;
  
  protected final T parent;
  
  public Minecraft getMc() {
    return this.mc;
  }
  
  public String getName() {
    return this.name;
  }
  
  public T getParent() {
    return this.parent;
  }
  
  public ModuleMode(String name, T parent) {
    this.name = name;
    this.parent = parent;
  }
  
  public EntityPlayerSP player() {
    return (Minecraft.getMinecraft()).thePlayer;
  }
  
  public WorldClient world() {
    return this.mc.theWorld;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\me\rhys\base\module\ModuleMode.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */