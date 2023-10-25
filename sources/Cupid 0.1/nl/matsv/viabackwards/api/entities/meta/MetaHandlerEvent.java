package nl.matsv.viabackwards.api.entities.meta;

import java.util.ArrayList;
import java.util.List;
import nl.matsv.viabackwards.api.entities.storage.EntityTracker;
import nl.matsv.viabackwards.api.entities.storage.MetaStorage;
import org.jetbrains.annotations.Nullable;
import us.myles.ViaVersion.api.data.UserConnection;
import us.myles.ViaVersion.api.minecraft.metadata.Metadata;

public class MetaHandlerEvent {
  private final UserConnection user;
  
  private final EntityTracker.StoredEntity entity;
  
  private final int index;
  
  private final Metadata data;
  
  private final MetaStorage storage;
  
  private List<Metadata> extraData;
  
  public MetaHandlerEvent(UserConnection user, EntityTracker.StoredEntity entity, int index, Metadata data, MetaStorage storage) {
    this.user = user;
    this.entity = entity;
    this.index = index;
    this.data = data;
    this.storage = storage;
  }
  
  public boolean hasData() {
    return (this.data != null);
  }
  
  public Metadata getMetaByIndex(int index) {
    for (Metadata meta : this.storage.getMetaDataList()) {
      if (index == meta.getId())
        return meta; 
    } 
    return null;
  }
  
  public void clearExtraData() {
    this.extraData = null;
  }
  
  public void createMeta(Metadata metadata) {
    ((this.extraData != null) ? this.extraData : (this.extraData = new ArrayList<>())).add(metadata);
  }
  
  public UserConnection getUser() {
    return this.user;
  }
  
  public EntityTracker.StoredEntity getEntity() {
    return this.entity;
  }
  
  public int getIndex() {
    return this.index;
  }
  
  public Metadata getData() {
    return this.data;
  }
  
  public MetaStorage getStorage() {
    return this.storage;
  }
  
  @Nullable
  public List<Metadata> getExtraData() {
    return this.extraData;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\nl\matsv\viabackwards\api\entities\meta\MetaHandlerEvent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */