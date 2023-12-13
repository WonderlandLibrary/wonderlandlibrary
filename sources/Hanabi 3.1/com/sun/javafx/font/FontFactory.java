package com.sun.javafx.font;

import java.io.InputStream;

public interface FontFactory {
   String DEFAULT_FULLNAME = "System Regular";

   PGFont createFont(String var1, float var2);

   PGFont createFont(String var1, boolean var2, boolean var3, float var4);

   PGFont deriveFont(PGFont var1, boolean var2, boolean var3, float var4);

   String[] getFontFamilyNames();

   String[] getFontFullNames();

   String[] getFontFullNames(String var1);

   boolean hasPermission();

   PGFont loadEmbeddedFont(String var1, InputStream var2, float var3, boolean var4);

   PGFont loadEmbeddedFont(String var1, String var2, float var3, boolean var4);

   boolean isPlatformFont(String var1);
}
