package org.apache.commons.lang3.math;

public class IEEE754rUtils {
  public static double min(double[] array) {
    if (array == null)
      throw new IllegalArgumentException("The Array must not be null"); 
    if (array.length == 0)
      throw new IllegalArgumentException("Array cannot be empty."); 
    double min = array[0];
    for (int i = 1; i < array.length; i++)
      min = min(array[i], min); 
    return min;
  }
  
  public static float min(float[] array) {
    if (array == null)
      throw new IllegalArgumentException("The Array must not be null"); 
    if (array.length == 0)
      throw new IllegalArgumentException("Array cannot be empty."); 
    float min = array[0];
    for (int i = 1; i < array.length; i++)
      min = min(array[i], min); 
    return min;
  }
  
  public static double min(double a, double b, double c) {
    return min(min(a, b), c);
  }
  
  public static double min(double a, double b) {
    if (Double.isNaN(a))
      return b; 
    if (Double.isNaN(b))
      return a; 
    return Math.min(a, b);
  }
  
  public static float min(float a, float b, float c) {
    return min(min(a, b), c);
  }
  
  public static float min(float a, float b) {
    if (Float.isNaN(a))
      return b; 
    if (Float.isNaN(b))
      return a; 
    return Math.min(a, b);
  }
  
  public static double max(double[] array) {
    if (array == null)
      throw new IllegalArgumentException("The Array must not be null"); 
    if (array.length == 0)
      throw new IllegalArgumentException("Array cannot be empty."); 
    double max = array[0];
    for (int j = 1; j < array.length; j++)
      max = max(array[j], max); 
    return max;
  }
  
  public static float max(float[] array) {
    if (array == null)
      throw new IllegalArgumentException("The Array must not be null"); 
    if (array.length == 0)
      throw new IllegalArgumentException("Array cannot be empty."); 
    float max = array[0];
    for (int j = 1; j < array.length; j++)
      max = max(array[j], max); 
    return max;
  }
  
  public static double max(double a, double b, double c) {
    return max(max(a, b), c);
  }
  
  public static double max(double a, double b) {
    if (Double.isNaN(a))
      return b; 
    if (Double.isNaN(b))
      return a; 
    return Math.max(a, b);
  }
  
  public static float max(float a, float b, float c) {
    return max(max(a, b), c);
  }
  
  public static float max(float a, float b) {
    if (Float.isNaN(a))
      return b; 
    if (Float.isNaN(b))
      return a; 
    return Math.max(a, b);
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\commons\lang3\math\IEEE754rUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */