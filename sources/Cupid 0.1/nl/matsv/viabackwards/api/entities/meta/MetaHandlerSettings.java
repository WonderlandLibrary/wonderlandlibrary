package nl.matsv.viabackwards.api.entities.meta;

import nl.matsv.viabackwards.api.exceptions.RemovedValueException;
import org.jetbrains.annotations.Nullable;
import us.myles.ViaVersion.api.entities.EntityType;
import us.myles.ViaVersion.api.minecraft.metadata.Metadata;

public class MetaHandlerSettings {
  private EntityType filterType;
  
  private boolean filterFamily;
  
  private int filterIndex = -1;
  
  private MetaHandler handler;
  
  public MetaHandlerSettings filter(EntityType type) {
    return filter(type, this.filterFamily, this.filterIndex);
  }
  
  public MetaHandlerSettings filter(EntityType type, boolean filterFamily) {
    return filter(type, filterFamily, this.filterIndex);
  }
  
  public MetaHandlerSettings filter(int index) {
    return filter(this.filterType, this.filterFamily, index);
  }
  
  public MetaHandlerSettings filter(EntityType type, int index) {
    return filter(type, this.filterFamily, index);
  }
  
  public MetaHandlerSettings filter(EntityType type, boolean filterFamily, int index) {
    this.filterType = type;
    this.filterFamily = filterFamily;
    this.filterIndex = index;
    return this;
  }
  
  public void handle(@Nullable MetaHandler handler) {
    this.handler = handler;
  }
  
  public void handleIndexChange(int newIndex) {
    handle(e -> {
          Metadata data = e.getData();
          data.setId(newIndex);
          return data;
        });
  }
  
  public void removed() {
    handle(e -> {
          throw RemovedValueException.EX;
        });
  }
  
  public boolean hasHandler() {
    return (this.handler != null);
  }
  
  public boolean hasType() {
    return (this.filterType != null);
  }
  
  public boolean hasIndex() {
    return (this.filterIndex > -1);
  }
  
  public boolean isFilterFamily() {
    return this.filterFamily;
  }
  
  public boolean isGucci(EntityType type, Metadata metadata) {
    if (!hasHandler())
      return false; 
    if (hasType() && (this.filterFamily ? !type.isOrHasParent(this.filterType) : !this.filterType.is(type)))
      return false; 
    return (!hasIndex() || metadata.getId() == this.filterIndex);
  }
  
  public EntityType getFilterType() {
    return this.filterType;
  }
  
  public int getFilterIndex() {
    return this.filterIndex;
  }
  
  @Nullable
  public MetaHandler getHandler() {
    return this.handler;
  }
  
  public String toString() {
    return "MetaHandlerSettings{filterType=" + this.filterType + ", filterFamily=" + this.filterFamily + ", filterIndex=" + this.filterIndex + ", handler=" + this.handler + '}';
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\nl\matsv\viabackwards\api\entities\meta\MetaHandlerSettings.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */