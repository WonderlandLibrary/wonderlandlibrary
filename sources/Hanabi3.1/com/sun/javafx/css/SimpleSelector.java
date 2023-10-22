package com.sun.javafx.css;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javafx.collections.ObservableList;
import javafx.css.PseudoClass;
import javafx.css.Styleable;
import javafx.geometry.NodeOrientation;
import javafx.scene.Node;

public final class SimpleSelector extends Selector {
   private final String name;
   private final StyleClassSet styleClassSet;
   private final String id;
   private final PseudoClassState pseudoClassState;
   private final boolean matchOnName;
   private final boolean matchOnId;
   private final boolean matchOnStyleClass;
   private final NodeOrientation nodeOrientation;

   public String getName() {
      return this.name;
   }

   public List getStyleClasses() {
      ArrayList var1 = new ArrayList();
      Iterator var2 = this.styleClassSet.iterator();

      while(var2.hasNext()) {
         var1.add(((StyleClass)var2.next()).getStyleClassName());
      }

      return Collections.unmodifiableList(var1);
   }

   Set getStyleClassSet() {
      return this.styleClassSet;
   }

   public String getId() {
      return this.id;
   }

   Set getPseudoClassStates() {
      return this.pseudoClassState;
   }

   public List getPseudoclasses() {
      ArrayList var1 = new ArrayList();
      Iterator var2 = this.pseudoClassState.iterator();

      while(var2.hasNext()) {
         var1.add(((PseudoClass)var2.next()).getPseudoClassName());
      }

      if (this.nodeOrientation == NodeOrientation.RIGHT_TO_LEFT) {
         var1.add("dir(rtl)");
      } else if (this.nodeOrientation == NodeOrientation.LEFT_TO_RIGHT) {
         var1.add("dir(ltr)");
      }

      return Collections.unmodifiableList(var1);
   }

   NodeOrientation getNodeOrientation() {
      return this.nodeOrientation;
   }

   public SimpleSelector(String var1, List var2, List var3, String var4) {
      this.name = var1 == null ? "*" : var1;
      this.matchOnName = var1 != null && !"".equals(var1) && !"*".equals(var1);
      this.styleClassSet = new StyleClassSet();
      int var5 = var2 != null ? var2.size() : 0;

      for(int var6 = 0; var6 < var5; ++var6) {
         String var7 = (String)var2.get(var6);
         if (var7 != null && !var7.isEmpty()) {
            StyleClass var8 = StyleClassSet.getStyleClass(var7);
            this.styleClassSet.add(var8);
         }
      }

      this.matchOnStyleClass = this.styleClassSet.size() > 0;
      this.pseudoClassState = new PseudoClassState();
      var5 = var3 != null ? var3.size() : 0;
      NodeOrientation var10 = NodeOrientation.INHERIT;

      for(int var11 = 0; var11 < var5; ++var11) {
         String var12 = (String)var3.get(var11);
         if (var12 != null && !var12.isEmpty()) {
            if ("dir(".regionMatches(true, 0, var12, 0, 4)) {
               boolean var9 = "dir(rtl)".equalsIgnoreCase(var12);
               var10 = var9 ? NodeOrientation.RIGHT_TO_LEFT : NodeOrientation.LEFT_TO_RIGHT;
            } else {
               PseudoClass var13 = PseudoClassState.getPseudoClass(var12);
               this.pseudoClassState.add(var13);
            }
         }
      }

      this.nodeOrientation = var10;
      this.id = var4 == null ? "" : var4;
      this.matchOnId = var4 != null && !"".equals(var4);
   }

   Match createMatch() {
      int var1 = this.matchOnId ? 1 : 0;
      int var2 = this.styleClassSet.size();
      return new Match(this, this.pseudoClassState, var1, var2);
   }

   public boolean applies(Styleable var1) {
      if (this.nodeOrientation != NodeOrientation.INHERIT && var1 instanceof Node) {
         Node var2 = (Node)var1;
         NodeOrientation var3 = var2.getNodeOrientation();
         if (var3 == NodeOrientation.INHERIT) {
            if (var2.getEffectiveNodeOrientation() != this.nodeOrientation) {
               return false;
            }
         } else if (var3 != this.nodeOrientation) {
            return false;
         }
      }

      String var8;
      boolean var10;
      if (this.matchOnId) {
         var8 = var1.getId();
         var10 = this.id.equals(var8);
         if (!var10) {
            return false;
         }
      }

      if (this.matchOnName) {
         var8 = var1.getTypeSelector();
         var10 = this.name.equals(var8);
         if (!var10) {
            return false;
         }
      }

      if (this.matchOnStyleClass) {
         StyleClassSet var9 = new StyleClassSet();
         ObservableList var11 = var1.getStyleClass();
         int var4 = 0;

         for(int var5 = var11.size(); var4 < var5; ++var4) {
            String var6 = (String)var11.get(var4);
            if (var6 != null && !var6.isEmpty()) {
               StyleClass var7 = StyleClassSet.getStyleClass(var6);
               var9.add(var7);
            }
         }

         boolean var12 = this.matchStyleClasses(var9);
         if (!var12) {
            return false;
         }
      }

      return true;
   }

   boolean applies(Styleable var1, Set[] var2, int var3) {
      boolean var4 = this.applies(var1);
      if (var4 && var2 != null && var3 < var2.length) {
         if (var2[var3] == null) {
            var2[var3] = new PseudoClassState();
         }

         var2[var3].addAll(this.pseudoClassState);
      }

      return var4;
   }

   public boolean stateMatches(Styleable var1, Set var2) {
      return var2 != null ? var2.containsAll(this.pseudoClassState) : false;
   }

   private boolean matchStyleClasses(StyleClassSet var1) {
      return var1.containsAll(this.styleClassSet);
   }

   public boolean equals(Object var1) {
      if (var1 == null) {
         return false;
      } else if (this.getClass() != var1.getClass()) {
         return false;
      } else {
         SimpleSelector var2;
         label40: {
            var2 = (SimpleSelector)var1;
            if (this.name == null) {
               if (var2.name == null) {
                  break label40;
               }
            } else if (this.name.equals(var2.name)) {
               break label40;
            }

            return false;
         }

         label33: {
            if (this.id == null) {
               if (var2.id == null) {
                  break label33;
               }
            } else if (this.id.equals(var2.id)) {
               break label33;
            }

            return false;
         }

         if (!this.styleClassSet.equals(var2.styleClassSet)) {
            return false;
         } else {
            return this.pseudoClassState.equals(var2.pseudoClassState);
         }
      }
   }

   public int hashCode() {
      int var1 = 7;
      var1 = 31 * (var1 + this.name.hashCode());
      var1 = 31 * (var1 + this.styleClassSet.hashCode());
      var1 = 31 * (var1 + this.styleClassSet.hashCode());
      var1 = this.id != null ? 31 * (var1 + this.id.hashCode()) : 0;
      var1 = 31 * (var1 + this.pseudoClassState.hashCode());
      return var1;
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      if (this.name != null && !this.name.isEmpty()) {
         var1.append(this.name);
      } else {
         var1.append("*");
      }

      Iterator var2 = this.styleClassSet.iterator();

      while(var2.hasNext()) {
         StyleClass var3 = (StyleClass)var2.next();
         var1.append('.').append(var3.getStyleClassName());
      }

      if (this.id != null && !this.id.isEmpty()) {
         var1.append('#');
         var1.append(this.id);
      }

      Iterator var5 = this.pseudoClassState.iterator();

      while(var5.hasNext()) {
         PseudoClass var4 = (PseudoClass)var5.next();
         var1.append(':').append(var4.getPseudoClassName());
      }

      return var1.toString();
   }

   public final void writeBinary(DataOutputStream var1, StringStore var2) throws IOException {
      super.writeBinary(var1, var2);
      var1.writeShort(var2.addString(this.name));
      var1.writeShort(this.styleClassSet.size());
      Iterator var3 = this.styleClassSet.iterator();

      while(var3.hasNext()) {
         StyleClass var4 = (StyleClass)var3.next();
         var1.writeShort(var2.addString(var4.getStyleClassName()));
      }

      var1.writeShort(var2.addString(this.id));
      int var7 = this.pseudoClassState.size() + (this.nodeOrientation != NodeOrientation.RIGHT_TO_LEFT && this.nodeOrientation != NodeOrientation.LEFT_TO_RIGHT ? 0 : 1);
      var1.writeShort(var7);
      Iterator var5 = this.pseudoClassState.iterator();

      while(var5.hasNext()) {
         PseudoClass var6 = (PseudoClass)var5.next();
         var1.writeShort(var2.addString(var6.getPseudoClassName()));
      }

      if (this.nodeOrientation == NodeOrientation.RIGHT_TO_LEFT) {
         var1.writeShort(var2.addString("dir(rtl)"));
      } else if (this.nodeOrientation == NodeOrientation.LEFT_TO_RIGHT) {
         var1.writeShort(var2.addString("dir(ltr)"));
      }

   }

   static SimpleSelector readBinary(int var0, DataInputStream var1, String[] var2) throws IOException {
      String var3 = var2[var1.readShort()];
      short var4 = var1.readShort();
      ArrayList var5 = new ArrayList();

      for(int var6 = 0; var6 < var4; ++var6) {
         var5.add(var2[var1.readShort()]);
      }

      String var10 = var2[var1.readShort()];
      short var7 = var1.readShort();
      ArrayList var8 = new ArrayList();

      for(int var9 = 0; var9 < var7; ++var9) {
         var8.add(var2[var1.readShort()]);
      }

      return new SimpleSelector(var3, var5, var8, var10);
   }
}
