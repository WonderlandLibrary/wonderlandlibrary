package com.google.common.collect;

import java.util.SortedSet;

interface SortedMultisetBridge extends Multiset {
   SortedSet elementSet();
}
