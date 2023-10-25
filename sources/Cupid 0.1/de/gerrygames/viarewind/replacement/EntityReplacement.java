package de.gerrygames.viarewind.replacement;

import java.util.List;
import us.myles.ViaVersion.api.minecraft.metadata.Metadata;

public interface EntityReplacement {
  int getEntityId();
  
  void setLocation(double paramDouble1, double paramDouble2, double paramDouble3);
  
  void relMove(double paramDouble1, double paramDouble2, double paramDouble3);
  
  void setYawPitch(float paramFloat1, float paramFloat2);
  
  void setHeadYaw(float paramFloat);
  
  void spawn();
  
  void despawn();
  
  void updateMetadata(List<Metadata> paramList);
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\de\gerrygames\viarewind\replacement\EntityReplacement.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */