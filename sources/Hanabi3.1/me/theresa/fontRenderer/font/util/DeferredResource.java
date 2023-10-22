package me.theresa.fontRenderer.font.util;

import java.io.IOException;

public interface DeferredResource {
   void load() throws IOException;

   String getDescription();
}
