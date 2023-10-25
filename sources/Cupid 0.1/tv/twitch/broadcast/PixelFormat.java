package tv.twitch.broadcast;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum PixelFormat {
  TTV_PF_BGRA(66051),
  TTV_PF_ABGR(16909056),
  TTV_PF_RGBA(33619971),
  TTV_PF_ARGB(50462976);
  
  private static Map<Integer, PixelFormat> s_Map;
  
  private int m_Value;
  
  static {
    s_Map = new HashMap<Integer, PixelFormat>();
    EnumSet<PixelFormat> enumSet = EnumSet.allOf(PixelFormat.class);
    for (PixelFormat pixelFormat : enumSet)
      s_Map.put(Integer.valueOf(pixelFormat.getValue()), pixelFormat); 
  }
  
  public static PixelFormat lookupValue(int paramInt) {
    return s_Map.get(Integer.valueOf(paramInt));
  }
  
  PixelFormat(int paramInt1) {
    this.m_Value = paramInt1;
  }
  
  public int getValue() {
    return this.m_Value;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\tv\twitch\broadcast\PixelFormat.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */