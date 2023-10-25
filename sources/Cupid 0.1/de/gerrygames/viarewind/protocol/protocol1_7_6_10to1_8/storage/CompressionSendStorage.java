package de.gerrygames.viarewind.protocol.protocol1_7_6_10to1_8.storage;

import us.myles.ViaVersion.api.data.StoredObject;
import us.myles.ViaVersion.api.data.UserConnection;

public class CompressionSendStorage extends StoredObject {
  public String toString() {
    return "CompressionSendStorage(compressionSend=" + isCompressionSend() + ")";
  }
  
  public int hashCode() {
    int PRIME = 59;
    result = 1;
    return result * 59 + (isCompressionSend() ? 79 : 97);
  }
  
  protected boolean canEqual(Object other) {
    return other instanceof CompressionSendStorage;
  }
  
  public boolean equals(Object o) {
    if (o == this)
      return true; 
    if (!(o instanceof CompressionSendStorage))
      return false; 
    CompressionSendStorage other = (CompressionSendStorage)o;
    return !other.canEqual(this) ? false : (!(isCompressionSend() != other.isCompressionSend()));
  }
  
  public void setCompressionSend(boolean compressionSend) {
    this.compressionSend = compressionSend;
  }
  
  private boolean compressionSend = false;
  
  public boolean isCompressionSend() {
    return this.compressionSend;
  }
  
  public CompressionSendStorage(UserConnection user) {
    super(user);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\de\gerrygames\viarewind\protocol\protocol1_7_6_10to1_8\storage\CompressionSendStorage.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */