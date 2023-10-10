package org.newdawn.slick.loading;

import java.io.IOException;

public interface DeferredResource {
   void load() throws IOException;

   String getDescription();
}
