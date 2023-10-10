package com.google.gson;

import com.google.gson.stream.JsonReader;
import java.io.Reader;
import java.io.StringReader;
import java.util.Iterator;

public final class JsonStreamParser implements Iterator {
   private final JsonReader parser;
   private final Object lock;

   public JsonStreamParser(String var1) {
      this((Reader)(new StringReader(var1)));
   }

   public JsonStreamParser(Reader var1) {
      this.parser = new JsonReader(var1);
      this.parser.setLenient(true);
      this.lock = new Object();
   }

   public JsonElement next() throws JsonParseException {
      // $FF: Couldn't be decompiled
   }

   public void remove() {
      throw new UnsupportedOperationException();
   }

   public Object next() {
      return this.next();
   }
}
