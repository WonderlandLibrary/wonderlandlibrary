package com.ibm.icu.impl.locale;

public class StringTokenIterator {
   private String _text;
   private String _dlms;
   private String _token;
   private int _start;
   private int _end;
   private boolean _done;

   public StringTokenIterator(String var1, String var2) {
      this._text = var1;
      this._dlms = var2;
      this.setStart(0);
   }

   public String first() {
      this.setStart(0);
      return this._token;
   }

   public String current() {
      return this._token;
   }

   public int currentStart() {
      return this._start;
   }

   public int currentEnd() {
      return this._end;
   }

   public boolean isDone() {
      return this._done;
   }

   public String next() {
      // $FF: Couldn't be decompiled
   }

   public StringTokenIterator setStart(int var1) {
      if (var1 > this._text.length()) {
         throw new IndexOutOfBoundsException();
      } else {
         this._start = var1;
         this._end = this.nextDelimiter(this._start);
         this._token = this._text.substring(this._start, this._end);
         this._done = false;
         return this;
      }
   }

   public StringTokenIterator setText(String var1) {
      this._text = var1;
      this.setStart(0);
      return this;
   }

   private int nextDelimiter(int var1) {
      int var2;
      for(var2 = var1; var2 < this._text.length(); ++var2) {
         char var3 = this._text.charAt(var2);

         for(int var4 = 0; var4 < this._dlms.length(); ++var4) {
            if (var3 == this._dlms.charAt(var4)) {
               return var2;
            }
         }
      }

      return var2;
   }
}
