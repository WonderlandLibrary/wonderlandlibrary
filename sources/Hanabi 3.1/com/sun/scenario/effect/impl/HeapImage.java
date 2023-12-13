package com.sun.scenario.effect.impl;

import com.sun.scenario.effect.Filterable;

public interface HeapImage extends Filterable {
   int getScanlineStride();

   int[] getPixelArray();
}
