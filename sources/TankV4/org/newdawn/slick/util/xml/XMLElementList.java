package org.newdawn.slick.util.xml;

import java.util.ArrayList;
import java.util.Collection;

public class XMLElementList {
   private ArrayList list = new ArrayList();

   public void add(XMLElement var1) {
      this.list.add(var1);
   }

   public int size() {
      return this.list.size();
   }

   public XMLElement get(int var1) {
      return (XMLElement)this.list.get(var1);
   }

   public boolean contains(XMLElement var1) {
      return this.list.contains(var1);
   }

   public void addAllTo(Collection var1) {
      var1.addAll(this.list);
   }
}
