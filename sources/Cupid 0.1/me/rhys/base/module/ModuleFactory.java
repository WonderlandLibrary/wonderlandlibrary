package me.rhys.base.module;

import java.util.stream.Stream;
import me.rhys.base.Lite;
import me.rhys.base.event.data.EventTarget;
import me.rhys.base.event.impl.input.KeyboardInputEvent;
import me.rhys.base.util.container.Container;

public class ModuleFactory extends Container<Module> {
  public long lastToggleTime = System.currentTimeMillis();
  
  @EventTarget
  void keyInput(KeyboardInputEvent event) {
    Lite.MODULE_FACTORY
      .filter(module -> (module.getData().getKeyCode() == event.getKeyCode()))
      .forEach(module -> {
          module.toggle(!module.getData().isEnabled());
          this.lastToggleTime = System.currentTimeMillis();
        });
  }
  
  public Module findByName(String name) {
    return (Module)find(module -> module.getData().getName().equalsIgnoreCase(name));
  }
  
  public Module findByClass(Class clazz) {
    return (Module)find(module -> (module.getClass() == clazz));
  }
  
  public Stream<Module> getActiveModules() {
    return filter(module -> module.getData().isEnabled());
  }
  
  public Stream<Module> getDisabledModules() {
    return filter(module -> !module.getData().isEnabled());
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\me\rhys\base\module\ModuleFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */