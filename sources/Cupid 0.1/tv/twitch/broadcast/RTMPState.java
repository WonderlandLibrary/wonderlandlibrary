package tv.twitch.broadcast;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum RTMPState {
  Invalid(-1),
  Idle(0),
  Initialize(1),
  Handshake(2),
  Connect(3),
  CreateStream(4),
  Publish(5),
  SendVideo(6),
  Shutdown(7),
  Error(8);
  
  private static Map<Integer, RTMPState> s_Map;
  
  private int m_Value;
  
  static {
    s_Map = new HashMap<Integer, RTMPState>();
    EnumSet<RTMPState> enumSet = EnumSet.allOf(RTMPState.class);
    for (RTMPState rTMPState : enumSet)
      s_Map.put(Integer.valueOf(rTMPState.getValue()), rTMPState); 
  }
  
  public static RTMPState lookupValue(int paramInt) {
    return s_Map.get(Integer.valueOf(paramInt));
  }
  
  RTMPState(int paramInt1) {
    this.m_Value = paramInt1;
  }
  
  public int getValue() {
    return this.m_Value;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\tv\twitch\broadcast\RTMPState.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */