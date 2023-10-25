package me.rhys.client.module.player.disabler.modes;

import me.rhys.base.event.data.EventTarget;
import me.rhys.base.event.impl.network.PacketEvent;
import me.rhys.base.module.Module;
import me.rhys.base.module.ModuleMode;
import me.rhys.base.module.setting.manifest.Name;
import me.rhys.client.module.player.disabler.Disabler;
import net.minecraft.network.play.client.C0FPacketConfirmTransaction;

public class TransactionCancel extends ModuleMode<Disabler> {
  @Name("Block C0F Below 0")
  public boolean blockNegs;
  
  public TransactionCancel(String name, Disabler parent) {
    super(name, (Module)parent);
    this.blockNegs = true;
  }
  
  @EventTarget
  void onPacket(PacketEvent event) {
    if (event.getPacket() instanceof C0FPacketConfirmTransaction)
      if (this.blockNegs) {
        C0FPacketConfirmTransaction c0FPacketConfirmTransaction = (C0FPacketConfirmTransaction)event.getPacket();
        if (c0FPacketConfirmTransaction.getUid() < 0)
          event.setCancelled(true); 
      } else {
        event.setCancelled(true);
      }  
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\me\rhys\client\module\player\disabler\modes\TransactionCancel.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */