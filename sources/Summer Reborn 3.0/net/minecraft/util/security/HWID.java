package net.minecraft.util.security;

import net.minecraft.util.security.utils.StringUtil;

public class HWID {
  public static String getHWID() {
    String returnhwid = "";
    String hwid = System.getProperty("user.name") + System.getProperty("user.home") + System.getProperty("os.version") + System.getProperty("os.name");
    for (String s : StringUtil.getSubstrings(hwid))
      returnhwid = returnhwid + StringUtil.convertToString(s); 
    return returnhwid;
  }
}
