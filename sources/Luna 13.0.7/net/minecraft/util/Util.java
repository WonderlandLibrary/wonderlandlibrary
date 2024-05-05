package net.minecraft.util;

public class Util
{
  private static final String __OBFID = "CL_00001633";
  
  public Util() {}
  
  public static EnumOS getOSType()
  {
    String var0 = System.getProperty("os.name").toLowerCase();
    return var0.contains("unix") ? EnumOS.LINUX : var0.contains("linux") ? EnumOS.LINUX : var0.contains("sunos") ? EnumOS.SOLARIS : var0.contains("solaris") ? EnumOS.SOLARIS : var0.contains("mac") ? EnumOS.OSX : var0.contains("win") ? EnumOS.WINDOWS : EnumOS.UNKNOWN;
  }
  
  public static enum EnumOS
  {
    private static final EnumOS[] $VALUES = { LINUX, SOLARIS, WINDOWS, OSX, UNKNOWN };
    private static final String __OBFID = "CL_00001660";
  }
}
