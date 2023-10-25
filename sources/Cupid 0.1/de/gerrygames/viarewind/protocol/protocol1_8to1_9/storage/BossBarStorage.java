package de.gerrygames.viarewind.protocol.protocol1_8to1_9.storage;

import de.gerrygames.viarewind.protocol.protocol1_8to1_9.bossbar.WitherBossBar;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import us.myles.ViaVersion.api.data.StoredObject;
import us.myles.ViaVersion.api.data.UserConnection;

public class BossBarStorage extends StoredObject {
  private Map<UUID, WitherBossBar> bossBars = new HashMap<>();
  
  public BossBarStorage(UserConnection user) {
    super(user);
  }
  
  public void add(UUID uuid, String title, float health) {
    WitherBossBar bossBar = new WitherBossBar(getUser(), uuid, title, health);
    PlayerPosition playerPosition = (PlayerPosition)getUser().get(PlayerPosition.class);
    bossBar.setPlayerLocation(playerPosition.getPosX(), playerPosition.getPosY(), playerPosition.getPosZ(), playerPosition.getYaw(), playerPosition.getPitch());
    bossBar.show();
    this.bossBars.put(uuid, bossBar);
  }
  
  public void remove(UUID uuid) {
    WitherBossBar bossBar = this.bossBars.remove(uuid);
    if (bossBar == null)
      return; 
    bossBar.hide();
  }
  
  public void updateLocation() {
    PlayerPosition playerPosition = (PlayerPosition)getUser().get(PlayerPosition.class);
    this.bossBars.values().forEach(bossBar -> bossBar.setPlayerLocation(playerPosition.getPosX(), playerPosition.getPosY(), playerPosition.getPosZ(), playerPosition.getYaw(), playerPosition.getPitch()));
  }
  
  public void changeWorld() {
    this.bossBars.values().forEach(bossBar -> {
          bossBar.hide();
          bossBar.show();
        });
  }
  
  public void updateHealth(UUID uuid, float health) {
    WitherBossBar bossBar = this.bossBars.get(uuid);
    if (bossBar == null)
      return; 
    bossBar.setHealth(health);
  }
  
  public void updateTitle(UUID uuid, String title) {
    WitherBossBar bossBar = this.bossBars.get(uuid);
    if (bossBar == null)
      return; 
    bossBar.setTitle(title);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\de\gerrygames\viarewind\protocol\protocol1_8to1_9\storage\BossBarStorage.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */