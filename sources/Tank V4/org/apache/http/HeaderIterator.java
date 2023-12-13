package org.apache.http;

import java.util.Iterator;

public interface HeaderIterator extends Iterator {
   boolean hasNext();

   Header nextHeader();
}
