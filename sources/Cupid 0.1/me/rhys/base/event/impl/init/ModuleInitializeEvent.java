package me.rhys.base.event.impl.init;

import java.util.Arrays;
import me.rhys.base.event.Event;
import me.rhys.base.module.Module;
import me.rhys.base.module.ModuleFactory;

public class ModuleInitializeEvent extends Event {
  private final ModuleFactory factory;
  
  public ModuleInitializeEvent(ModuleFactory factory) {
    this.factory = factory;
  }
  
  public void register(Module module) {
    this.factory.add(module);
  }
  
  public void register(Module... modules) {
    Arrays.<Module>stream(modules).forEach(this::register);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\me\rhys\base\event\impl\init\ModuleInitializeEvent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */