package org.lwjgl.opengl;

interface ContextAttribsImplementation {
   int getMajorVersionAttrib();

   int getMinorVersionAttrib();

   int getLayerPlaneAttrib();

   int getFlagsAttrib();

   int getDebugBit();

   int getForwardCompatibleBit();

   int getProfileMaskAttrib();

   int getProfileCoreBit();

   int getProfileCompatibilityBit();
}
