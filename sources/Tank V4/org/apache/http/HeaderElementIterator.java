package org.apache.http;

import java.util.Iterator;

public interface HeaderElementIterator extends Iterator {
   boolean hasNext();

   HeaderElement nextElement();
}
