package me.theresa.fontRenderer.font.util;

import java.io.InputStream;
import java.net.URL;

public interface SlickResourceLocation {
   InputStream getResourceAsStream(String var1);

   URL getResource(String var1);
}
