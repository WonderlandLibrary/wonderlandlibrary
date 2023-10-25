package nl.matsv.viabackwards.api.entities.storage;

public class EntityObjectData extends EntityData {
  private final boolean isObject;
  
  private final int objectData;
  
  public EntityObjectData(int id, boolean isObject, int replacementId, int objectData) {
    super(id, replacementId);
    this.isObject = isObject;
    this.objectData = objectData;
  }
  
  public boolean isObject() {
    return this.isObject;
  }
  
  public int getObjectData() {
    return this.objectData;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\nl\matsv\viabackwards\api\entities\storage\EntityObjectData.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */