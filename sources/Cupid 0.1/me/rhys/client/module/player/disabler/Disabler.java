package me.rhys.client.module.player.disabler;

import me.rhys.base.module.Module;
import me.rhys.base.module.ModuleMode;
import me.rhys.base.module.data.Category;
import me.rhys.client.module.player.disabler.modes.AngelX;
import me.rhys.client.module.player.disabler.modes.BlocksMC;
import me.rhys.client.module.player.disabler.modes.C0C;
import me.rhys.client.module.player.disabler.modes.Ghostly;
import me.rhys.client.module.player.disabler.modes.Ground;
import me.rhys.client.module.player.disabler.modes.Kauri;
import me.rhys.client.module.player.disabler.modes.KeepAliveCancel;
import me.rhys.client.module.player.disabler.modes.LatinPlay;
import me.rhys.client.module.player.disabler.modes.OldAHM;
import me.rhys.client.module.player.disabler.modes.OldVerus;
import me.rhys.client.module.player.disabler.modes.Position;
import me.rhys.client.module.player.disabler.modes.TransactionCancel;
import me.rhys.client.module.player.disabler.modes.VerusCombat;

public class Disabler extends Module {
  public Disabler(String name, String description, Category category, int keyCode) {
    super(name, description, category, keyCode);
    add((Object[])new ModuleMode[] { 
          (ModuleMode)new KeepAliveCancel("KeepAliveCancel", this), (ModuleMode)new TransactionCancel("TransactionCancel", this), (ModuleMode)new LatinPlay("LatinPlay", this), (ModuleMode)new OldVerus("VerusPosition", this), (ModuleMode)new Kauri("Kauri", this), (ModuleMode)new Ghostly("Ghostly", this), (ModuleMode)new OldAHM("OldAHM", this), (ModuleMode)new Ground("Ground", this), (ModuleMode)new BlocksMC("BlocksMC", this), (ModuleMode)new AngelX("AngelX", this), 
          (ModuleMode)new Position("Position", this), (ModuleMode)new VerusCombat("VerusCombat", this), (ModuleMode)new C0C("C0C", this) });
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\me\rhys\client\module\player\disabler\Disabler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */