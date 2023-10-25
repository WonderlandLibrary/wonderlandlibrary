package tv.twitch.broadcast;

import java.util.HashMap;
import java.util.Map;

public class FrameBuffer {
  private static Map<Long, FrameBuffer> s_OutstandingBuffers = new HashMap<Long, FrameBuffer>();
  
  public static FrameBuffer lookupBuffer(long paramLong) {
    return s_OutstandingBuffers.get(Long.valueOf(paramLong));
  }
  
  protected static void registerBuffer(FrameBuffer paramFrameBuffer) {
    if (paramFrameBuffer.getAddress() != 0L)
      s_OutstandingBuffers.put(Long.valueOf(paramFrameBuffer.getAddress()), paramFrameBuffer); 
  }
  
  protected static void unregisterBuffer(FrameBuffer paramFrameBuffer) {
    s_OutstandingBuffers.remove(Long.valueOf(paramFrameBuffer.getAddress()));
  }
  
  protected long m_NativeAddress = 0L;
  
  protected int m_Size = 0;
  
  protected StreamAPI m_API = null;
  
  FrameBuffer(StreamAPI paramStreamAPI, int paramInt) {
    this.m_NativeAddress = paramStreamAPI.allocateFrameBuffer(paramInt);
    if (this.m_NativeAddress == 0L)
      return; 
    this.m_API = paramStreamAPI;
    this.m_Size = paramInt;
    registerBuffer(this);
  }
  
  public boolean getIsValid() {
    return (this.m_NativeAddress != 0L);
  }
  
  public int getSize() {
    return this.m_Size;
  }
  
  public long getAddress() {
    return this.m_NativeAddress;
  }
  
  public void free() {
    if (this.m_NativeAddress != 0L) {
      unregisterBuffer(this);
      this.m_API.freeFrameBuffer(this.m_NativeAddress);
      this.m_NativeAddress = 0L;
    } 
  }
  
  protected void finalize() {
    free();
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\tv\twitch\broadcast\FrameBuffer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */