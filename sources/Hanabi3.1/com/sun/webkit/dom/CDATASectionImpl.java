package com.sun.webkit.dom;

import org.w3c.dom.CDATASection;

public class CDATASectionImpl extends TextImpl implements CDATASection {
   CDATASectionImpl(long var1) {
      super(var1);
   }

   static CDATASection getImpl(long var0) {
      return (CDATASection)create(var0);
   }
}
