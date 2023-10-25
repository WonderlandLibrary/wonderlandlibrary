package com.google.gson;

public final class JsonNull extends JsonElement {
  public static final JsonNull INSTANCE = new JsonNull();
  
  JsonNull deepCopy() {
    return INSTANCE;
  }
  
  public int hashCode() {
    return JsonNull.class.hashCode();
  }
  
  public boolean equals(Object other) {
    return (this == other || other instanceof JsonNull);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\google\gson\JsonNull.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */