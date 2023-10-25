package me.rhys.client.ui.alt.element;

import me.rhys.base.ui.element.panel.Panel;
import me.rhys.base.util.LoginThread;
import me.rhys.base.util.render.FontUtil;
import me.rhys.base.util.vec.Vec2f;

public class AccountItem extends Panel {
  private final String email;
  
  private final String password;
  
  public String getEmail() {
    return this.email;
  }
  
  public String getPassword() {
    return this.password;
  }
  
  private long lastClick = System.currentTimeMillis();
  
  public long getLastClick() {
    return this.lastClick;
  }
  
  public AccountItem(String email, String password, int width, int height) {
    super(new Vec2f(), width, height);
    this.email = email;
    this.password = password;
  }
  
  public void draw(Vec2f mouse, float partialTicks) {
    StringBuilder info = new StringBuilder();
    if (this.email.contains("@")) {
      String[] parts = this.email.split("@");
      int clip = parts[0].length() / 2;
      info.append(parts[0], 0, clip);
      for (int i = 0; i < parts[0].length() - clip; i++)
        info.append("*"); 
      info.append("@");
      info.append(parts[1]);
    } else {
      int clip = this.email.length() / 2;
      info.append(this.email, 0, clip);
      for (int i = 0; i < this.email.length() - clip; i++)
        info.append("*"); 
    } 
    FontUtil.drawStringWithShadow(info.toString(), this.pos.clone().add(5, 5), -1);
    super.draw(mouse, partialTicks);
  }
  
  public void clickMouse(Vec2f pos, int button) {
    if (System.currentTimeMillis() - this.lastClick > 1000L) {
      LoginThread loginThread = new LoginThread(this.email, this.password);
      loginThread.playSound = true;
      loginThread.start();
      getScreen().displayPopup(null);
      this.lastClick = System.currentTimeMillis();
    } 
    super.clickMouse(pos, button);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\me\rhys\clien\\ui\alt\element\AccountItem.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */