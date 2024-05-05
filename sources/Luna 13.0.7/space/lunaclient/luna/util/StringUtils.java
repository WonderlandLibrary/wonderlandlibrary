package space.lunaclient.luna.util;

public class StringUtils
{
  public StringUtils() {}
  
  public static String toString(String[] args, int start)
  {
    StringBuilder builder = new StringBuilder();
    for (int i = start; i < args.length; i++) {
      builder.append(args[i]).append(" ");
    }
    return builder.toString();
  }
}
