package net.java.games.input;

final class ElementType {
  private static final ElementType[] map = new ElementType[514];
  
  public static final ElementType INPUT_MISC = new ElementType(1);
  
  public static final ElementType INPUT_BUTTON = new ElementType(2);
  
  public static final ElementType INPUT_AXIS = new ElementType(3);
  
  public static final ElementType INPUT_SCANCODES = new ElementType(4);
  
  public static final ElementType OUTPUT = new ElementType(129);
  
  public static final ElementType FEATURE = new ElementType(257);
  
  public static final ElementType COLLECTION = new ElementType(513);
  
  private final int type_id;
  
  public static final ElementType map(int type_id) {
    if (type_id < 0 || type_id >= map.length)
      return null; 
    return map[type_id];
  }
  
  private ElementType(int type_id) {
    map[type_id] = this;
    this.type_id = type_id;
  }
  
  public final String toString() {
    return "ElementType (0x" + Integer.toHexString(this.type_id) + ")";
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\net\java\games\input\ElementType.class
 * Java compiler version: 4 (48.0)
 * JD-Core Version:       1.1.3
 */