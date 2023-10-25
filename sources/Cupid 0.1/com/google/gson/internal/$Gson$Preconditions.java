package com.google.gson.internal;

public final class $Gson$Preconditions {
  public static <T> T checkNotNull(T obj) {
    if (obj == null)
      throw new NullPointerException(); 
    return obj;
  }
  
  public static void checkArgument(boolean condition) {
    if (!condition)
      throw new IllegalArgumentException(); 
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\google\gson\internal\$Gson$Preconditions.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */