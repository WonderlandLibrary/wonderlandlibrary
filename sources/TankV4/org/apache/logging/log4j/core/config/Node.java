package org.apache.logging.log4j.core.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.logging.log4j.core.config.plugins.PluginType;

public class Node {
   private final Node parent;
   private final String name;
   private String value;
   private final PluginType type;
   private final Map attributes = new HashMap();
   private final List children = new ArrayList();
   private Object object;

   public Node(Node var1, String var2, PluginType var3) {
      this.parent = var1;
      this.name = var2;
      this.type = var3;
   }

   public Node() {
      this.parent = null;
      this.name = null;
      this.type = null;
   }

   public Node(Node var1) {
      this.parent = var1.parent;
      this.name = var1.name;
      this.type = var1.type;
      this.attributes.putAll(var1.getAttributes());
      this.value = var1.getValue();
      Iterator var2 = var1.getChildren().iterator();

      while(var2.hasNext()) {
         Node var3 = (Node)var2.next();
         this.children.add(new Node(var3));
      }

      this.object = var1.object;
   }

   public Map getAttributes() {
      return this.attributes;
   }

   public List getChildren() {
      return this.children;
   }

   public boolean hasChildren() {
      return this.children.size() > 0;
   }

   public String getValue() {
      return this.value;
   }

   public void setValue(String var1) {
      this.value = var1;
   }

   public Node getParent() {
      return this.parent;
   }

   public String getName() {
      return this.name;
   }

   public boolean isRoot() {
      return this.parent == null;
   }

   public void setObject(Object var1) {
      this.object = var1;
   }

   public Object getObject() {
      return this.object;
   }

   public PluginType getType() {
      return this.type;
   }

   public String toString() {
      if (this.object == null) {
         return "null";
      } else {
         return this.type.isObjectPrintable() ? this.object.toString() : this.type.getPluginClass().getName() + " with name " + this.name;
      }
   }
}
