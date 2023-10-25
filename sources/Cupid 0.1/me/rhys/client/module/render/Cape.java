package me.rhys.client.module.render;

import me.rhys.base.module.Module;
import me.rhys.base.module.data.Category;
import me.rhys.base.module.setting.manifest.Name;

public class Cape extends Module {
  @Name("Mode")
  public LOGO mode;
  
  public Cape(String name, String description, Category category, int keyCode) {
    super(name, description, category, keyCode);
    this.mode = LOGO.LOGO;
  }
  
  public enum LOGO {
    CUPID, DARK, DORT, LOGO;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\me\rhys\client\module\render\Cape.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */