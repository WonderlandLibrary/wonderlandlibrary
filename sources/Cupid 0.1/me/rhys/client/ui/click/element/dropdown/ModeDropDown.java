package me.rhys.client.ui.click.element.dropdown;

import java.util.Comparator;
import me.rhys.base.module.Module;
import me.rhys.base.module.ModuleMode;
import me.rhys.base.ui.element.button.DropDownButton;
import me.rhys.base.util.vec.Vec2f;
import me.rhys.client.Manager;
import me.rhys.client.ui.click.element.button.ModuleButton;

public class ModeDropDown extends DropDownButton {
  private final Module module;
  
  public ModeDropDown(Module module, Vec2f offset, int width, int height) {
    super(offset, width, height, new String[] { module.getCurrentMode().getName() });
    this.module = module;
    module.getItems().stream().filter(moduleMode -> !moduleMode.getName().equalsIgnoreCase(module.getCurrentMode().getName())).sorted(Comparator.comparingInt(value -> value.getName().charAt(0))).forEachOrdered(moduleMode -> this.items.add(moduleMode.getName()));
  }
  
  public void setCurrent(String current) {
    if (this.items.contains(current)) {
      this.label = current;
      this.current = this.items.indexOf(current);
      this.module.setCurrentMode(this.label);
      Manager.UI.CLICK.addSettings(((ModuleButton)Manager.UI.CLICK.modulesPanel.getContainer().get(Manager.UI.CLICK.moduleCurrent)).getModule());
    } 
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\me\rhys\clien\\ui\click\element\dropdown\ModeDropDown.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */