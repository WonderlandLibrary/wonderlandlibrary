package org.newdawn.slick.muffin;

import java.io.IOException;
import java.util.HashMap;

public interface Muffin {
   void saveFile(HashMap var1, String var2) throws IOException;

   HashMap loadFile(String var1) throws IOException;
}
