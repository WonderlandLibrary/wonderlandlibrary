package com.ibm.icu.util;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class StringTrieBuilder {
   private StringTrieBuilder.State state;
   /** @deprecated */
   protected StringBuilder strings;
   private StringTrieBuilder.Node root;
   private HashMap nodes;
   private StringTrieBuilder.ValueNode lookupFinalValueNode;
   static final boolean $assertionsDisabled = !StringTrieBuilder.class.desiredAssertionStatus();

   /** @deprecated */
   protected StringTrieBuilder() {
      this.state = StringTrieBuilder.State.ADDING;
      this.strings = new StringBuilder();
      this.nodes = new HashMap();
      this.lookupFinalValueNode = new StringTrieBuilder.ValueNode();
   }

   /** @deprecated */
   protected void addImpl(CharSequence var1, int var2) {
      if (this.state != StringTrieBuilder.State.ADDING) {
         throw new IllegalStateException("Cannot add (string, value) pairs after build().");
      } else if (var1.length() > 65535) {
         throw new IndexOutOfBoundsException("The maximum string length is 0xffff.");
      } else {
         if (this.root == null) {
            this.root = this.createSuffixNode(var1, 0, var2);
         } else {
            this.root = this.root.add(this, var1, 0, var2);
         }

      }
   }

   /** @deprecated */
   protected final void buildImpl(StringTrieBuilder.Option var1) {
      switch(this.state) {
      case ADDING:
         if (this.root == null) {
            throw new IndexOutOfBoundsException("No (string, value) pairs were added.");
         } else if (var1 == StringTrieBuilder.Option.FAST) {
            this.state = StringTrieBuilder.State.BUILDING_FAST;
         } else {
            this.state = StringTrieBuilder.State.BUILDING_SMALL;
         }
      default:
         this.root = this.root.register(this);
         this.root.markRightEdgesFirst(-1);
         this.root.write(this);
         this.state = StringTrieBuilder.State.BUILT;
         return;
      case BUILDING_FAST:
      case BUILDING_SMALL:
         throw new IllegalStateException("Builder failed and must be clear()ed.");
      case BUILT:
      }
   }

   /** @deprecated */
   protected void clearImpl() {
      this.strings.setLength(0);
      this.nodes.clear();
      this.root = null;
      this.state = StringTrieBuilder.State.ADDING;
   }

   private final StringTrieBuilder.Node registerNode(StringTrieBuilder.Node var1) {
      if (this.state == StringTrieBuilder.State.BUILDING_FAST) {
         return var1;
      } else {
         StringTrieBuilder.Node var2 = (StringTrieBuilder.Node)this.nodes.get(var1);
         if (var2 != null) {
            return var2;
         } else {
            var2 = (StringTrieBuilder.Node)this.nodes.put(var1, var1);
            if (!$assertionsDisabled && var2 != null) {
               throw new AssertionError();
            } else {
               return var1;
            }
         }
      }
   }

   private final StringTrieBuilder.ValueNode registerFinalValue(int var1) {
      StringTrieBuilder.ValueNode.access$000(this.lookupFinalValueNode, var1);
      StringTrieBuilder.Node var2 = (StringTrieBuilder.Node)this.nodes.get(this.lookupFinalValueNode);
      if (var2 != null) {
         return (StringTrieBuilder.ValueNode)var2;
      } else {
         StringTrieBuilder.ValueNode var3 = new StringTrieBuilder.ValueNode(var1);
         var2 = (StringTrieBuilder.Node)this.nodes.put(var3, var3);
         if (!$assertionsDisabled && var2 != null) {
            throw new AssertionError();
         } else {
            return var3;
         }
      }
   }

   private StringTrieBuilder.ValueNode createSuffixNode(CharSequence var1, int var2, int var3) {
      Object var4 = this.registerFinalValue(var3);
      if (var2 < var1.length()) {
         int var5 = this.strings.length();
         this.strings.append(var1, var2, var1.length());
         var4 = new StringTrieBuilder.LinearMatchNode(this.strings, var5, var1.length() - var2, (StringTrieBuilder.Node)var4);
      }

      return (StringTrieBuilder.ValueNode)var4;
   }

   /** @deprecated */
   protected abstract boolean matchNodesCanHaveValues();

   /** @deprecated */
   protected abstract int getMaxBranchLinearSubNodeLength();

   /** @deprecated */
   protected abstract int getMinLinearMatch();

   /** @deprecated */
   protected abstract int getMaxLinearMatchLength();

   /** @deprecated */
   protected abstract int write(int var1);

   /** @deprecated */
   protected abstract int write(int var1, int var2);

   /** @deprecated */
   protected abstract int writeValueAndFinal(int var1, boolean var2);

   /** @deprecated */
   protected abstract int writeValueAndType(boolean var1, int var2, int var3);

   /** @deprecated */
   protected abstract int writeDeltaTo(int var1);

   static StringTrieBuilder.ValueNode access$100(StringTrieBuilder var0, CharSequence var1, int var2, int var3) {
      return var0.createSuffixNode(var1, var2, var3);
   }

   static StringTrieBuilder.Node access$200(StringTrieBuilder var0, StringTrieBuilder.Node var1) {
      return var0.registerNode(var1);
   }

   private static enum State {
      ADDING,
      BUILDING_FAST,
      BUILDING_SMALL,
      BUILT;

      private static final StringTrieBuilder.State[] $VALUES = new StringTrieBuilder.State[]{ADDING, BUILDING_FAST, BUILDING_SMALL, BUILT};
   }

   private static final class BranchHeadNode extends StringTrieBuilder.ValueNode {
      private int length;
      private StringTrieBuilder.Node next;

      public BranchHeadNode(int var1, StringTrieBuilder.Node var2) {
         this.length = var1;
         this.next = var2;
      }

      public int hashCode() {
         return (248302782 + this.length) * 37 + this.next.hashCode();
      }

      public boolean equals(Object var1) {
         if (this == var1) {
            return true;
         } else if (!super.equals(var1)) {
            return false;
         } else {
            StringTrieBuilder.BranchHeadNode var2 = (StringTrieBuilder.BranchHeadNode)var1;
            return this.length == var2.length && this.next == var2.next;
         }
      }

      public int markRightEdgesFirst(int var1) {
         if (this.offset == 0) {
            this.offset = var1 = this.next.markRightEdgesFirst(var1);
         }

         return var1;
      }

      public void write(StringTrieBuilder var1) {
         this.next.write(var1);
         if (this.length <= var1.getMinLinearMatch()) {
            this.offset = var1.writeValueAndType(this.hasValue, this.value, this.length - 1);
         } else {
            var1.write(this.length - 1);
            this.offset = var1.writeValueAndType(this.hasValue, this.value, 0);
         }

      }
   }

   private static final class SplitBranchNode extends StringTrieBuilder.BranchNode {
      private char unit;
      private StringTrieBuilder.Node lessThan;
      private StringTrieBuilder.Node greaterOrEqual;
      static final boolean $assertionsDisabled = !StringTrieBuilder.class.desiredAssertionStatus();

      public SplitBranchNode(char var1, StringTrieBuilder.Node var2, StringTrieBuilder.Node var3) {
         this.hash = ((206918985 + var1) * 37 + var2.hashCode()) * 37 + var3.hashCode();
         this.unit = var1;
         this.lessThan = var2;
         this.greaterOrEqual = var3;
      }

      public boolean equals(Object var1) {
         if (this == var1) {
            return true;
         } else if (!super.equals(var1)) {
            return false;
         } else {
            StringTrieBuilder.SplitBranchNode var2 = (StringTrieBuilder.SplitBranchNode)var1;
            return this.unit == var2.unit && this.lessThan == var2.lessThan && this.greaterOrEqual == var2.greaterOrEqual;
         }
      }

      public int hashCode() {
         return super.hashCode();
      }

      public int markRightEdgesFirst(int var1) {
         if (this.offset == 0) {
            this.firstEdgeNumber = var1;
            var1 = this.greaterOrEqual.markRightEdgesFirst(var1);
            this.offset = var1 = this.lessThan.markRightEdgesFirst(var1 - 1);
         }

         return var1;
      }

      public void write(StringTrieBuilder var1) {
         this.lessThan.writeUnlessInsideRightEdge(this.firstEdgeNumber, this.greaterOrEqual.getOffset(), var1);
         this.greaterOrEqual.write(var1);
         if (!$assertionsDisabled && this.lessThan.getOffset() <= 0) {
            throw new AssertionError();
         } else {
            var1.writeDeltaTo(this.lessThan.getOffset());
            this.offset = var1.write(this.unit);
         }
      }
   }

   private static final class ListBranchNode extends StringTrieBuilder.BranchNode {
      private StringTrieBuilder.Node[] equal;
      private int length;
      private int[] values;
      private char[] units;
      static final boolean $assertionsDisabled = !StringTrieBuilder.class.desiredAssertionStatus();

      public ListBranchNode(int var1) {
         this.hash = 165535188 + var1;
         this.equal = new StringTrieBuilder.Node[var1];
         this.values = new int[var1];
         this.units = new char[var1];
      }

      public boolean equals(Object var1) {
         if (this == var1) {
            return true;
         } else if (!super.equals(var1)) {
            return false;
         } else {
            StringTrieBuilder.ListBranchNode var2 = (StringTrieBuilder.ListBranchNode)var1;

            for(int var3 = 0; var3 < this.length; ++var3) {
               if (this.units[var3] != var2.units[var3] || this.values[var3] != var2.values[var3] || this.equal[var3] != var2.equal[var3]) {
                  return false;
               }
            }

            return true;
         }
      }

      public int hashCode() {
         return super.hashCode();
      }

      public int markRightEdgesFirst(int var1) {
         if (this.offset == 0) {
            this.firstEdgeNumber = var1;
            byte var2 = 0;
            int var3 = this.length;

            do {
               --var3;
               StringTrieBuilder.Node var4 = this.equal[var3];
               if (var4 != null) {
                  var1 = var4.markRightEdgesFirst(var1 - var2);
               }

               var2 = 1;
            } while(var3 > 0);

            this.offset = var1;
         }

         return var1;
      }

      public void write(StringTrieBuilder var1) {
         int var2 = this.length - 1;
         StringTrieBuilder.Node var3 = this.equal[var2];
         int var4 = var3 == null ? this.firstEdgeNumber : var3.getOffset();

         do {
            --var2;
            if (this.equal[var2] != null) {
               this.equal[var2].writeUnlessInsideRightEdge(this.firstEdgeNumber, var4, var1);
            }
         } while(var2 > 0);

         var2 = this.length - 1;
         if (var3 == null) {
            var1.writeValueAndFinal(this.values[var2], true);
         } else {
            var3.write(var1);
         }

         this.offset = var1.write(this.units[var2]);

         while(true) {
            --var2;
            if (var2 < 0) {
               return;
            }

            int var5;
            boolean var6;
            if (this.equal[var2] == null) {
               var5 = this.values[var2];
               var6 = true;
            } else {
               if (!$assertionsDisabled && this.equal[var2].getOffset() <= 0) {
                  throw new AssertionError();
               }

               var5 = this.offset - this.equal[var2].getOffset();
               var6 = false;
            }

            var1.writeValueAndFinal(var5, var6);
            this.offset = var1.write(this.units[var2]);
         }
      }

      public void add(int var1, int var2) {
         this.units[this.length] = (char)var1;
         this.equal[this.length] = null;
         this.values[this.length] = var2;
         ++this.length;
         this.hash = (this.hash * 37 + var1) * 37 + var2;
      }

      public void add(int var1, StringTrieBuilder.Node var2) {
         this.units[this.length] = (char)var1;
         this.equal[this.length] = var2;
         this.values[this.length] = 0;
         ++this.length;
         this.hash = (this.hash * 37 + var1) * 37 + var2.hashCode();
      }
   }

   private abstract static class BranchNode extends StringTrieBuilder.Node {
      protected int hash;
      protected int firstEdgeNumber;

      public BranchNode() {
      }

      public int hashCode() {
         return this.hash;
      }
   }

   private static final class DynamicBranchNode extends StringTrieBuilder.ValueNode {
      private StringBuilder chars = new StringBuilder();
      private ArrayList equal = new ArrayList();

      public DynamicBranchNode() {
      }

      public void add(char var1, StringTrieBuilder.Node var2) {
         int var3 = this.find(var1);
         this.chars.insert(var3, var1);
         this.equal.add(var3, var2);
      }

      public StringTrieBuilder.Node add(StringTrieBuilder var1, CharSequence var2, int var3, int var4) {
         if (var3 == var2.length()) {
            if (this.hasValue) {
               throw new IllegalArgumentException("Duplicate string.");
            } else {
               this.setValue(var4);
               return this;
            }
         } else {
            char var5 = var2.charAt(var3++);
            int var6 = this.find(var5);
            if (var6 < this.chars.length() && var5 == this.chars.charAt(var6)) {
               this.equal.set(var6, ((StringTrieBuilder.Node)this.equal.get(var6)).add(var1, var2, var3, var4));
            } else {
               this.chars.insert(var6, var5);
               this.equal.add(var6, StringTrieBuilder.access$100(var1, var2, var3, var4));
            }

            return this;
         }
      }

      public StringTrieBuilder.Node register(StringTrieBuilder var1) {
         StringTrieBuilder.Node var2 = this.register(var1, 0, this.chars.length());
         StringTrieBuilder.BranchHeadNode var3 = new StringTrieBuilder.BranchHeadNode(this.chars.length(), var2);
         Object var4 = var3;
         if (this.hasValue) {
            if (var1.matchNodesCanHaveValues()) {
               var3.setValue(this.value);
            } else {
               var4 = new StringTrieBuilder.IntermediateValueNode(this.value, StringTrieBuilder.access$200(var1, var3));
            }
         }

         return StringTrieBuilder.access$200(var1, (StringTrieBuilder.Node)var4);
      }

      private StringTrieBuilder.Node register(StringTrieBuilder var1, int var2, int var3) {
         int var4 = var3 - var2;
         if (var4 > var1.getMaxBranchLinearSubNodeLength()) {
            int var8 = var2 + var4 / 2;
            return StringTrieBuilder.access$200(var1, new StringTrieBuilder.SplitBranchNode(this.chars.charAt(var8), this.register(var1, var2, var8), this.register(var1, var8, var3)));
         } else {
            StringTrieBuilder.ListBranchNode var5 = new StringTrieBuilder.ListBranchNode(var4);

            do {
               char var6 = this.chars.charAt(var2);
               StringTrieBuilder.Node var7 = (StringTrieBuilder.Node)this.equal.get(var2);
               if (var7.getClass() == StringTrieBuilder.ValueNode.class) {
                  var5.add(var6, ((StringTrieBuilder.ValueNode)var7).value);
               } else {
                  var5.add(var6, var7.register(var1));
               }

               ++var2;
            } while(var2 < var3);

            return StringTrieBuilder.access$200(var1, var5);
         }
      }

      private int find(char var1) {
         int var2 = 0;
         int var3 = this.chars.length();

         while(var2 < var3) {
            int var4 = (var2 + var3) / 2;
            char var5 = this.chars.charAt(var4);
            if (var1 < var5) {
               var3 = var4;
            } else {
               if (var1 == var5) {
                  return var4;
               }

               var2 = var4 + 1;
            }
         }

         return var2;
      }
   }

   private static final class LinearMatchNode extends StringTrieBuilder.ValueNode {
      private CharSequence strings;
      private int stringOffset;
      private int length;
      private StringTrieBuilder.Node next;
      private int hash;

      public LinearMatchNode(CharSequence var1, int var2, int var3, StringTrieBuilder.Node var4) {
         this.strings = var1;
         this.stringOffset = var2;
         this.length = var3;
         this.next = var4;
      }

      public int hashCode() {
         return this.hash;
      }

      public boolean equals(Object var1) {
         if (this == var1) {
            return true;
         } else if (!super.equals(var1)) {
            return false;
         } else {
            StringTrieBuilder.LinearMatchNode var2 = (StringTrieBuilder.LinearMatchNode)var1;
            if (this.length == var2.length && this.next == var2.next) {
               int var3 = this.stringOffset;
               int var4 = var2.stringOffset;

               for(int var5 = this.stringOffset + this.length; var3 < var5; ++var4) {
                  if (this.strings.charAt(var3) != this.strings.charAt(var4)) {
                     return false;
                  }

                  ++var3;
               }

               return true;
            } else {
               return false;
            }
         }
      }

      public StringTrieBuilder.Node add(StringTrieBuilder var1, CharSequence var2, int var3, int var4) {
         if (var3 == var2.length()) {
            if (this.hasValue) {
               throw new IllegalArgumentException("Duplicate string.");
            } else {
               this.setValue(var4);
               return this;
            }
         } else {
            int var5 = this.stringOffset + this.length;

            for(int var6 = this.stringOffset; var6 < var5; ++var3) {
               if (var3 == var2.length()) {
                  int var13 = var6 - this.stringOffset;
                  StringTrieBuilder.LinearMatchNode var14 = new StringTrieBuilder.LinearMatchNode(this.strings, var6, this.length - var13, this.next);
                  var14.setValue(var4);
                  this.length = var13;
                  this.next = var14;
                  return this;
               }

               char var7 = this.strings.charAt(var6);
               char var8 = var2.charAt(var3);
               if (var7 != var8) {
                  StringTrieBuilder.DynamicBranchNode var9 = new StringTrieBuilder.DynamicBranchNode();
                  Object var10;
                  Object var11;
                  if (var6 == this.stringOffset) {
                     if (this.hasValue) {
                        var9.setValue(this.value);
                        this.value = 0;
                        this.hasValue = false;
                     }

                     ++this.stringOffset;
                     --this.length;
                     var11 = this.length > 0 ? this : this.next;
                     var10 = var9;
                  } else if (var6 == var5 - 1) {
                     --this.length;
                     var11 = this.next;
                     this.next = var9;
                     var10 = this;
                  } else {
                     int var12 = var6 - this.stringOffset;
                     ++var6;
                     var11 = new StringTrieBuilder.LinearMatchNode(this.strings, var6, this.length - (var12 + 1), this.next);
                     this.length = var12;
                     this.next = var9;
                     var10 = this;
                  }

                  StringTrieBuilder.ValueNode var15 = StringTrieBuilder.access$100(var1, var2, var3 + 1, var4);
                  var9.add(var7, (StringTrieBuilder.Node)var11);
                  var9.add(var8, var15);
                  return (StringTrieBuilder.Node)var10;
               }

               ++var6;
            }

            this.next = this.next.add(var1, var2, var3, var4);
            return this;
         }
      }

      public StringTrieBuilder.Node register(StringTrieBuilder var1) {
         this.next = this.next.register(var1);

         StringTrieBuilder.LinearMatchNode var4;
         for(int var2 = var1.getMaxLinearMatchLength(); this.length > var2; this.next = StringTrieBuilder.access$200(var1, var4)) {
            int var3 = this.stringOffset + this.length - var2;
            this.length -= var2;
            var4 = new StringTrieBuilder.LinearMatchNode(this.strings, var3, var2, this.next);
            var4.setHashCode();
         }

         Object var5;
         if (this.hasValue && !var1.matchNodesCanHaveValues()) {
            int var6 = this.value;
            this.value = 0;
            this.hasValue = false;
            this.setHashCode();
            var5 = new StringTrieBuilder.IntermediateValueNode(var6, StringTrieBuilder.access$200(var1, this));
         } else {
            this.setHashCode();
            var5 = this;
         }

         return StringTrieBuilder.access$200(var1, (StringTrieBuilder.Node)var5);
      }

      public int markRightEdgesFirst(int var1) {
         if (this.offset == 0) {
            this.offset = var1 = this.next.markRightEdgesFirst(var1);
         }

         return var1;
      }

      public void write(StringTrieBuilder var1) {
         this.next.write(var1);
         var1.write(this.stringOffset, this.length);
         this.offset = var1.writeValueAndType(this.hasValue, this.value, var1.getMinLinearMatch() + this.length - 1);
      }

      private void setHashCode() {
         this.hash = (124151391 + this.length) * 37 + this.next.hashCode();
         if (this.hasValue) {
            this.hash = this.hash * 37 + this.value;
         }

         int var1 = this.stringOffset;

         for(int var2 = this.stringOffset + this.length; var1 < var2; ++var1) {
            this.hash = this.hash * 37 + this.strings.charAt(var1);
         }

      }
   }

   private static final class IntermediateValueNode extends StringTrieBuilder.ValueNode {
      private StringTrieBuilder.Node next;

      public IntermediateValueNode(int var1, StringTrieBuilder.Node var2) {
         this.next = var2;
         this.setValue(var1);
      }

      public int hashCode() {
         return (82767594 + this.value) * 37 + this.next.hashCode();
      }

      public boolean equals(Object var1) {
         if (this == var1) {
            return true;
         } else if (!super.equals(var1)) {
            return false;
         } else {
            StringTrieBuilder.IntermediateValueNode var2 = (StringTrieBuilder.IntermediateValueNode)var1;
            return this.next == var2.next;
         }
      }

      public int markRightEdgesFirst(int var1) {
         if (this.offset == 0) {
            this.offset = var1 = this.next.markRightEdgesFirst(var1);
         }

         return var1;
      }

      public void write(StringTrieBuilder var1) {
         this.next.write(var1);
         this.offset = var1.writeValueAndFinal(this.value, false);
      }
   }

   private static class ValueNode extends StringTrieBuilder.Node {
      protected boolean hasValue;
      protected int value;
      static final boolean $assertionsDisabled = !StringTrieBuilder.class.desiredAssertionStatus();

      public ValueNode() {
      }

      public ValueNode(int var1) {
         this.hasValue = true;
         this.value = var1;
      }

      public final void setValue(int var1) {
         if (!$assertionsDisabled && this.hasValue) {
            throw new AssertionError();
         } else {
            this.hasValue = true;
            this.value = var1;
         }
      }

      private void setFinalValue(int var1) {
         this.hasValue = true;
         this.value = var1;
      }

      public int hashCode() {
         int var1 = 1118481;
         if (this.hasValue) {
            var1 = var1 * 37 + this.value;
         }

         return var1;
      }

      public boolean equals(Object var1) {
         if (this == var1) {
            return true;
         } else if (!super.equals(var1)) {
            return false;
         } else {
            StringTrieBuilder.ValueNode var2 = (StringTrieBuilder.ValueNode)var1;
            return this.hasValue == var2.hasValue && (!this.hasValue || this.value == var2.value);
         }
      }

      public StringTrieBuilder.Node add(StringTrieBuilder var1, CharSequence var2, int var3, int var4) {
         if (var3 == var2.length()) {
            throw new IllegalArgumentException("Duplicate string.");
         } else {
            StringTrieBuilder.ValueNode var5 = StringTrieBuilder.access$100(var1, var2, var3, var4);
            var5.setValue(this.value);
            return var5;
         }
      }

      public void write(StringTrieBuilder var1) {
         this.offset = var1.writeValueAndFinal(this.value, true);
      }

      static void access$000(StringTrieBuilder.ValueNode var0, int var1) {
         var0.setFinalValue(var1);
      }
   }

   private abstract static class Node {
      protected int offset = 0;

      public Node() {
      }

      public abstract int hashCode();

      public boolean equals(Object var1) {
         return this == var1 || this.getClass() == var1.getClass();
      }

      public StringTrieBuilder.Node add(StringTrieBuilder var1, CharSequence var2, int var3, int var4) {
         return this;
      }

      public StringTrieBuilder.Node register(StringTrieBuilder var1) {
         return this;
      }

      public int markRightEdgesFirst(int var1) {
         if (this.offset == 0) {
            this.offset = var1;
         }

         return var1;
      }

      public abstract void write(StringTrieBuilder var1);

      public final void writeUnlessInsideRightEdge(int var1, int var2, StringTrieBuilder var3) {
         if (this.offset < 0 && (this.offset < var2 || var1 < this.offset)) {
            this.write(var3);
         }

      }

      public final int getOffset() {
         return this.offset;
      }
   }

   public static enum Option {
      FAST,
      SMALL;

      private static final StringTrieBuilder.Option[] $VALUES = new StringTrieBuilder.Option[]{FAST, SMALL};
   }
}
