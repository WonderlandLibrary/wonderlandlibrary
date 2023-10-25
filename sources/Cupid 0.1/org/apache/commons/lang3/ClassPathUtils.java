package org.apache.commons.lang3;

public class ClassPathUtils {
  public static String toFullyQualifiedName(Class<?> context, String resourceName) {
    Validate.notNull(context, "Parameter '%s' must not be null!", new Object[] { "context" });
    Validate.notNull(resourceName, "Parameter '%s' must not be null!", new Object[] { "resourceName" });
    return toFullyQualifiedName(context.getPackage(), resourceName);
  }
  
  public static String toFullyQualifiedName(Package context, String resourceName) {
    Validate.notNull(context, "Parameter '%s' must not be null!", new Object[] { "context" });
    Validate.notNull(resourceName, "Parameter '%s' must not be null!", new Object[] { "resourceName" });
    StringBuilder sb = new StringBuilder();
    sb.append(context.getName());
    sb.append(".");
    sb.append(resourceName);
    return sb.toString();
  }
  
  public static String toFullyQualifiedPath(Class<?> context, String resourceName) {
    Validate.notNull(context, "Parameter '%s' must not be null!", new Object[] { "context" });
    Validate.notNull(resourceName, "Parameter '%s' must not be null!", new Object[] { "resourceName" });
    return toFullyQualifiedPath(context.getPackage(), resourceName);
  }
  
  public static String toFullyQualifiedPath(Package context, String resourceName) {
    Validate.notNull(context, "Parameter '%s' must not be null!", new Object[] { "context" });
    Validate.notNull(resourceName, "Parameter '%s' must not be null!", new Object[] { "resourceName" });
    StringBuilder sb = new StringBuilder();
    sb.append(context.getName().replace('.', '/'));
    sb.append("/");
    sb.append(resourceName);
    return sb.toString();
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\commons\lang3\ClassPathUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */