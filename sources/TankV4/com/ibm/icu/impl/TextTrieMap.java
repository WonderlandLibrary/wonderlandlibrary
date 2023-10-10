package com.ibm.icu.impl;

import com.ibm.icu.lang.UCharacter;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

public class TextTrieMap {
   private TextTrieMap.Node _root = new TextTrieMap.Node(this);
   boolean _ignoreCase;

   public TextTrieMap(boolean var1) {
      this._ignoreCase = var1;
   }

   public TextTrieMap put(CharSequence var1, Object var2) {
      TextTrieMap.CharIterator var3 = new TextTrieMap.CharIterator(var1, 0, this._ignoreCase);
      this._root.add(var3, var2);
      return this;
   }

   public Iterator get(String var1) {
      return this.get(var1, 0);
   }

   public Iterator get(CharSequence var1, int var2) {
      return this.get(var1, var2, (int[])null);
   }

   public Iterator get(CharSequence var1, int var2, int[] var3) {
      TextTrieMap.LongestMatchHandler var4 = new TextTrieMap.LongestMatchHandler();
      this.find(var1, var2, var4);
      if (var3 != null && var3.length > 0) {
         var3[0] = var4.getMatchLength();
      }

      return var4.getMatches();
   }

   public void find(CharSequence var1, TextTrieMap.ResultHandler var2) {
      this.find(var1, 0, var2);
   }

   public void find(CharSequence var1, int var2, TextTrieMap.ResultHandler var3) {
      TextTrieMap.CharIterator var4 = new TextTrieMap.CharIterator(var1, var2, this._ignoreCase);
      this.find(this._root, var4, var3);
   }

   private synchronized void find(TextTrieMap.Node var1, TextTrieMap.CharIterator var2, TextTrieMap.ResultHandler var3) {
      Iterator var4 = var1.values();
      if (var4 == null || var3.handlePrefixMatch(var2.processedLength(), var4)) {
         TextTrieMap.Node var5 = var1.findMatch(var2);
         if (var5 != null) {
            this.find(var5, var2, var3);
         }

      }
   }

   private static char[] toCharArray(CharSequence var0) {
      char[] var1 = new char[var0.length()];

      for(int var2 = 0; var2 < var1.length; ++var2) {
         var1[var2] = var0.charAt(var2);
      }

      return var1;
   }

   private static char[] subArray(char[] var0, int var1) {
      if (var1 == 0) {
         return var0;
      } else {
         char[] var2 = new char[var0.length - var1];
         System.arraycopy(var0, var1, var2, 0, var2.length);
         return var2;
      }
   }

   private static char[] subArray(char[] var0, int var1, int var2) {
      if (var1 == 0 && var2 == var0.length) {
         return var0;
      } else {
         char[] var3 = new char[var2 - var1];
         System.arraycopy(var0, var1, var3, 0, var2 - var1);
         return var3;
      }
   }

   static char[] access$200(CharSequence var0) {
      return toCharArray(var0);
   }

   static char[] access$300(char[] var0, int var1) {
      return subArray(var0, var1);
   }

   static char[] access$400(char[] var0, int var1, int var2) {
      return subArray(var0, var1, var2);
   }

   private class Node {
      private char[] _text;
      private List _values;
      private List _children;
      final TextTrieMap this$0;

      private Node(TextTrieMap var1) {
         this.this$0 = var1;
      }

      private Node(TextTrieMap var1, char[] var2, List var3, List var4) {
         this.this$0 = var1;
         this._text = var2;
         this._values = var3;
         this._children = var4;
      }

      public Iterator values() {
         return this._values == null ? null : this._values.iterator();
      }

      public void add(TextTrieMap.CharIterator var1, Object var2) {
         StringBuilder var3 = new StringBuilder();

         while(var1.hasNext()) {
            var3.append(var1.next());
         }

         this.add(TextTrieMap.access$200(var3), 0, var2);
      }

      public TextTrieMap.Node findMatch(TextTrieMap.CharIterator var1) {
         if (this._children == null) {
            return null;
         } else if (!var1.hasNext()) {
            return null;
         } else {
            TextTrieMap.Node var2 = null;
            Character var3 = var1.next();
            Iterator var4 = this._children.iterator();

            while(var4.hasNext()) {
               TextTrieMap.Node var5 = (TextTrieMap.Node)var4.next();
               if (var3 < var5._text[0]) {
                  break;
               }

               if (var3 == var5._text[0]) {
                  if (var5 < var1) {
                     var2 = var5;
                  }
                  break;
               }
            }

            return var2;
         }
      }

      private void add(char[] var1, int var2, Object var3) {
         if (var1.length == var2) {
            this._values = this.addValue(this._values, var3);
         } else if (this._children == null) {
            this._children = new LinkedList();
            TextTrieMap.Node var7 = this.this$0.new Node(this.this$0, TextTrieMap.access$300(var1, var2), this.addValue((List)null, var3), (List)null);
            this._children.add(var7);
         } else {
            ListIterator var4 = this._children.listIterator();

            while(var4.hasNext()) {
               TextTrieMap.Node var5 = (TextTrieMap.Node)var4.next();
               if (var1[var2] < var5._text[0]) {
                  var4.previous();
                  break;
               }

               if (var1[var2] == var5._text[0]) {
                  int var6 = var5.lenMatches(var1, var2);
                  if (var6 == var5._text.length) {
                     var5.add(var1, var2 + var6, var3);
                  } else {
                     var5.split(var6);
                     var5.add(var1, var2 + var6, var3);
                  }

                  return;
               }
            }

            var4.add(this.this$0.new Node(this.this$0, TextTrieMap.access$300(var1, var2), this.addValue((List)null, var3), (List)null));
         }
      }

      private int lenMatches(char[] var1, int var2) {
         int var3 = var1.length - var2;
         int var4 = this._text.length < var3 ? this._text.length : var3;

         int var5;
         for(var5 = 0; var5 < var4 && this._text[var5] == var1[var2 + var5]; ++var5) {
         }

         return var5;
      }

      private void split(int var1) {
         char[] var2 = TextTrieMap.access$300(this._text, var1);
         this._text = TextTrieMap.access$400(this._text, 0, var1);
         TextTrieMap.Node var3 = this.this$0.new Node(this.this$0, var2, this._values, this._children);
         this._values = null;
         this._children = new LinkedList();
         this._children.add(var3);
      }

      private List addValue(List var1, Object var2) {
         if (var1 == null) {
            var1 = new LinkedList();
         }

         ((List)var1).add(var2);
         return (List)var1;
      }

      Node(TextTrieMap var1, Object var2) {
         this(var1);
      }
   }

   private static class LongestMatchHandler implements TextTrieMap.ResultHandler {
      private Iterator matches;
      private int length;

      private LongestMatchHandler() {
         this.matches = null;
         this.length = 0;
      }

      public boolean handlePrefixMatch(int var1, Iterator var2) {
         if (var1 > this.length) {
            this.length = var1;
            this.matches = var2;
         }

         return true;
      }

      public Iterator getMatches() {
         return this.matches;
      }

      public int getMatchLength() {
         return this.length;
      }

      LongestMatchHandler(Object var1) {
         this();
      }
   }

   public interface ResultHandler {
      boolean handlePrefixMatch(int var1, Iterator var2);
   }

   public static class CharIterator implements Iterator {
      private boolean _ignoreCase;
      private CharSequence _text;
      private int _nextIdx;
      private int _startIdx;
      private Character _remainingChar;

      CharIterator(CharSequence var1, int var2, boolean var3) {
         this._text = var1;
         this._nextIdx = this._startIdx = var2;
         this._ignoreCase = var3;
      }

      public boolean hasNext() {
         return this._nextIdx != this._text.length() || this._remainingChar != null;
      }

      public Character next() {
         if (this._nextIdx == this._text.length() && this._remainingChar == null) {
            return null;
         } else {
            Character var1;
            if (this._remainingChar != null) {
               var1 = this._remainingChar;
               this._remainingChar = null;
            } else if (this._ignoreCase) {
               int var2 = UCharacter.foldCase(Character.codePointAt(this._text, this._nextIdx), true);
               this._nextIdx += Character.charCount(var2);
               char[] var3 = Character.toChars(var2);
               var1 = var3[0];
               if (var3.length == 2) {
                  this._remainingChar = var3[1];
               }
            } else {
               var1 = this._text.charAt(this._nextIdx);
               ++this._nextIdx;
            }

            return var1;
         }
      }

      public void remove() {
         throw new UnsupportedOperationException("remove() not supproted");
      }

      public int nextIndex() {
         return this._nextIdx;
      }

      public int processedLength() {
         if (this._remainingChar != null) {
            throw new IllegalStateException("In the middle of surrogate pair");
         } else {
            return this._nextIdx - this._startIdx;
         }
      }

      public Object next() {
         return this.next();
      }
   }
}
