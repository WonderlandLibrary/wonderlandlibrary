package de.gerrygames.viarewind.protocol.protocol1_8to1_9.sound;

import java.util.HashMap;

public class Effect {
  private static final HashMap<Integer, Integer> effects = new HashMap<>();
  
  public static int getOldId(int id) {
    return ((Integer)effects.getOrDefault(Integer.valueOf(id), Integer.valueOf(id))).intValue();
  }
  
  static {
    effects.put(Integer.valueOf(1003), Integer.valueOf(1002));
    effects.put(Integer.valueOf(1005), Integer.valueOf(1003));
    effects.put(Integer.valueOf(1006), Integer.valueOf(1003));
    effects.put(Integer.valueOf(1007), Integer.valueOf(1003));
    effects.put(Integer.valueOf(1008), Integer.valueOf(1003));
    effects.put(Integer.valueOf(1009), Integer.valueOf(1004));
    effects.put(Integer.valueOf(1010), Integer.valueOf(1005));
    effects.put(Integer.valueOf(1011), Integer.valueOf(1006));
    effects.put(Integer.valueOf(1012), Integer.valueOf(1006));
    effects.put(Integer.valueOf(1013), Integer.valueOf(1006));
    effects.put(Integer.valueOf(1014), Integer.valueOf(1006));
    effects.put(Integer.valueOf(1015), Integer.valueOf(1007));
    effects.put(Integer.valueOf(1016), Integer.valueOf(1008));
    effects.put(Integer.valueOf(1017), Integer.valueOf(1008));
    effects.put(Integer.valueOf(1018), Integer.valueOf(1009));
    effects.put(Integer.valueOf(1019), Integer.valueOf(1010));
    effects.put(Integer.valueOf(1020), Integer.valueOf(1011));
    effects.put(Integer.valueOf(1021), Integer.valueOf(1012));
    effects.put(Integer.valueOf(1022), Integer.valueOf(1012));
    effects.put(Integer.valueOf(1023), Integer.valueOf(1013));
    effects.put(Integer.valueOf(1024), Integer.valueOf(1014));
    effects.put(Integer.valueOf(1025), Integer.valueOf(1015));
    effects.put(Integer.valueOf(1026), Integer.valueOf(1016));
    effects.put(Integer.valueOf(1027), Integer.valueOf(1017));
    effects.put(Integer.valueOf(1028), Integer.valueOf(1018));
    effects.put(Integer.valueOf(1029), Integer.valueOf(1020));
    effects.put(Integer.valueOf(1030), Integer.valueOf(1021));
    effects.put(Integer.valueOf(1031), Integer.valueOf(1022));
    effects.put(Integer.valueOf(1032), Integer.valueOf(-1));
    effects.put(Integer.valueOf(1033), Integer.valueOf(-1));
    effects.put(Integer.valueOf(1034), Integer.valueOf(-1));
    effects.put(Integer.valueOf(1035), Integer.valueOf(-1));
    effects.put(Integer.valueOf(1036), Integer.valueOf(1003));
    effects.put(Integer.valueOf(1037), Integer.valueOf(1006));
    effects.put(Integer.valueOf(3000), Integer.valueOf(-1));
    effects.put(Integer.valueOf(3001), Integer.valueOf(-1));
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\de\gerrygames\viarewind\protocol\protocol1_8to1_9\sound\Effect.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */