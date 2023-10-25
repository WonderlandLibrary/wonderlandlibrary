package nl.matsv.viabackwards.protocol.protocol1_10to1_11;

import us.myles.viaversion.libs.fastutil.ints.Int2IntMap;
import us.myles.viaversion.libs.fastutil.ints.Int2IntOpenHashMap;

public class PotionSplashHandler {
  private static final Int2IntMap DATA = (Int2IntMap)new Int2IntOpenHashMap(14, 1.0F);
  
  static {
    DATA.defaultReturnValue(-1);
    DATA.put(2039713, 5);
    DATA.put(8356754, 7);
    DATA.put(2293580, 9);
    DATA.put(14981690, 12);
    DATA.put(8171462, 14);
    DATA.put(5926017, 17);
    DATA.put(3035801, 19);
    DATA.put(16262179, 21);
    DATA.put(4393481, 23);
    DATA.put(5149489, 25);
    DATA.put(13458603, 28);
    DATA.put(9643043, 31);
    DATA.put(4738376, 34);
    DATA.put(3381504, 36);
  }
  
  public static int getOldData(int data) {
    return DATA.get(data);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\nl\matsv\viabackwards\protocol\protocol1_10to1_11\PotionSplashHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */