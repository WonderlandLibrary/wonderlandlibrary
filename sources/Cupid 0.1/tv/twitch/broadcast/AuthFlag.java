package tv.twitch.broadcast;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public enum AuthFlag {
  TTV_AuthOption_None(0),
  TTV_AuthOption_Broadcast(1),
  TTV_AuthOption_Chat(2);
  
  private static Map<Integer, AuthFlag> s_Map;
  
  private int m_Value;
  
  static {
    s_Map = new HashMap<Integer, AuthFlag>();
    EnumSet<AuthFlag> enumSet = EnumSet.allOf(AuthFlag.class);
    for (AuthFlag authFlag : enumSet)
      s_Map.put(Integer.valueOf(authFlag.getValue()), authFlag); 
  }
  
  public static AuthFlag lookupValue(int paramInt) {
    return s_Map.get(Integer.valueOf(paramInt));
  }
  
  public static int getNativeValue(HashSet<AuthFlag> paramHashSet) {
    if (paramHashSet == null)
      return TTV_AuthOption_None.getValue(); 
    int i = 0;
    for (AuthFlag authFlag : paramHashSet) {
      if (authFlag != null)
        i |= authFlag.getValue(); 
    } 
    return i;
  }
  
  AuthFlag(int paramInt1) {
    this.m_Value = paramInt1;
  }
  
  public int getValue() {
    return this.m_Value;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\tv\twitch\broadcast\AuthFlag.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */