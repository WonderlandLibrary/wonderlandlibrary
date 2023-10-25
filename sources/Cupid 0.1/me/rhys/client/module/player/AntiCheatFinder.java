package me.rhys.client.module.player;

import me.rhys.base.module.Module;
import me.rhys.base.module.data.Category;
import me.rhys.base.module.setting.manifest.Name;

public class AntiCheatFinder extends Module {
  @Name("Mode")
  public Mode moduleMode;
  
  public AntiCheatFinder(String name, String description, Category category, int keyCode) {
    super(name, description, category, keyCode);
    this.moduleMode = Mode.VERUS;
  }
  
  private enum Mode {
    VULCAN, SPARTAN, NOCHEATPLUS, AAC, VERUS, MOONAC, BLACKPROTECT, OVERFLOW, MORGAN, ANTICHEAT;
  }
  
  public void onEnable() {
    switch (this.moduleMode) {
      case VULCAN:
        this.mc.thePlayer.sendMessage("Testing for VULCAN!");
        this.mc.thePlayer.sendChatMessage("/jday");
        break;
      case VERUS:
        this.mc.thePlayer.sendMessage("Testing for VERUS!");
        this.mc.thePlayer.sendMessage("This might be fake because some anticheats spoof this!");
        this.mc.thePlayer.sendChatMessage("/recentlogs");
        break;
      case AAC:
        this.mc.thePlayer.sendMessage("Testing for AAC!");
        this.mc.thePlayer.sendChatMessage("/aac");
        break;
      case MOONAC:
        this.mc.thePlayer.sendMessage("Testing for MOONAC!");
        this.mc.thePlayer.sendChatMessage("/moon");
        break;
      case BLACKPROTECT:
        this.mc.thePlayer.sendMessage("Testing for BLACKPROTECT!");
        this.mc.thePlayer.sendChatMessage("/blackprotect");
        break;
      case SPARTAN:
        this.mc.thePlayer.sendMessage("Testing for SPARTAN!");
        this.mc.thePlayer.sendChatMessage("/spartan");
        break;
      case NOCHEATPLUS:
        this.mc.thePlayer.sendMessage("Testing for NCP!");
        this.mc.thePlayer.sendChatMessage("/nocheatplus");
        this.mc.thePlayer.sendChatMessage("/ncp");
        break;
      case OVERFLOW:
        this.mc.thePlayer.sendMessage("Testing for OVERFLOW!");
        this.mc.thePlayer.sendChatMessage("/overflow");
      case MORGAN:
        this.mc.thePlayer.sendMessage("Testing for MORGAN!");
        this.mc.thePlayer.sendChatMessage("/morgan");
      case ANTICHEAT:
        this.mc.thePlayer.sendMessage("Testing for any AntiCheat!");
        this.mc.thePlayer.sendChatMessage("/anticheat");
        break;
    } 
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\me\rhys\client\module\player\AntiCheatFinder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */