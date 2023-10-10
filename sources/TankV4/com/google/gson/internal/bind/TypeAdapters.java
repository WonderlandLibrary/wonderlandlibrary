package com.google.gson.internal.bind;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.annotations.SerializedName;
import com.google.gson.internal.LazilyParsedNumber;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.Timestamp;
import java.util.BitSet;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.UUID;
import java.util.Map.Entry;

public final class TypeAdapters {
   public static final TypeAdapter CLASS = new TypeAdapter() {
      public void write(JsonWriter var1, Class var2) throws IOException {
         if (var2 == null) {
            var1.nullValue();
         } else {
            throw new UnsupportedOperationException("Attempted to serialize java.lang.Class: " + var2.getName() + ". Forgot to register a type adapter?");
         }
      }

      public Class read(JsonReader var1) throws IOException {
         if (var1.peek() == JsonToken.NULL) {
            var1.nextNull();
            return null;
         } else {
            throw new UnsupportedOperationException("Attempted to deserialize a java.lang.Class. Forgot to register a type adapter?");
         }
      }

      public Object read(JsonReader var1) throws IOException {
         return this.read(var1);
      }

      public void write(JsonWriter var1, Object var2) throws IOException {
         this.write(var1, (Class)var2);
      }
   };
   public static final TypeAdapterFactory CLASS_FACTORY;
   public static final TypeAdapter BIT_SET;
   public static final TypeAdapterFactory BIT_SET_FACTORY;
   public static final TypeAdapter BOOLEAN;
   public static final TypeAdapter BOOLEAN_AS_STRING;
   public static final TypeAdapterFactory BOOLEAN_FACTORY;
   public static final TypeAdapter BYTE;
   public static final TypeAdapterFactory BYTE_FACTORY;
   public static final TypeAdapter SHORT;
   public static final TypeAdapterFactory SHORT_FACTORY;
   public static final TypeAdapter INTEGER;
   public static final TypeAdapterFactory INTEGER_FACTORY;
   public static final TypeAdapter LONG;
   public static final TypeAdapter FLOAT;
   public static final TypeAdapter DOUBLE;
   public static final TypeAdapter NUMBER;
   public static final TypeAdapterFactory NUMBER_FACTORY;
   public static final TypeAdapter CHARACTER;
   public static final TypeAdapterFactory CHARACTER_FACTORY;
   public static final TypeAdapter STRING;
   public static final TypeAdapter BIG_DECIMAL;
   public static final TypeAdapter BIG_INTEGER;
   public static final TypeAdapterFactory STRING_FACTORY;
   public static final TypeAdapter STRING_BUILDER;
   public static final TypeAdapterFactory STRING_BUILDER_FACTORY;
   public static final TypeAdapter STRING_BUFFER;
   public static final TypeAdapterFactory STRING_BUFFER_FACTORY;
   public static final TypeAdapter URL;
   public static final TypeAdapterFactory URL_FACTORY;
   public static final TypeAdapter URI;
   public static final TypeAdapterFactory URI_FACTORY;
   public static final TypeAdapter INET_ADDRESS;
   public static final TypeAdapterFactory INET_ADDRESS_FACTORY;
   public static final TypeAdapter UUID;
   public static final TypeAdapterFactory UUID_FACTORY;
   public static final TypeAdapterFactory TIMESTAMP_FACTORY;
   public static final TypeAdapter CALENDAR;
   public static final TypeAdapterFactory CALENDAR_FACTORY;
   public static final TypeAdapter LOCALE;
   public static final TypeAdapterFactory LOCALE_FACTORY;
   public static final TypeAdapter JSON_ELEMENT;
   public static final TypeAdapterFactory JSON_ELEMENT_FACTORY;
   public static final TypeAdapterFactory ENUM_FACTORY;

   private TypeAdapters() {
   }

   public static TypeAdapterFactory newEnumTypeHierarchyFactory() {
      return new TypeAdapterFactory() {
         public TypeAdapter create(Gson var1, TypeToken var2) {
            Class var3 = var2.getRawType();
            if (Enum.class.isAssignableFrom(var3) && var3 != Enum.class) {
               if (!var3.isEnum()) {
                  var3 = var3.getSuperclass();
               }

               return new TypeAdapters.EnumTypeAdapter(var3);
            } else {
               return null;
            }
         }
      };
   }

   public static TypeAdapterFactory newFactory(TypeToken var0, TypeAdapter var1) {
      return new TypeAdapterFactory(var0, var1) {
         final TypeToken val$type;
         final TypeAdapter val$typeAdapter;

         {
            this.val$type = var1;
            this.val$typeAdapter = var2;
         }

         public TypeAdapter create(Gson var1, TypeToken var2) {
            return var2.equals(this.val$type) ? this.val$typeAdapter : null;
         }
      };
   }

   public static TypeAdapterFactory newFactory(Class var0, TypeAdapter var1) {
      return new TypeAdapterFactory(var0, var1) {
         final Class val$type;
         final TypeAdapter val$typeAdapter;

         {
            this.val$type = var1;
            this.val$typeAdapter = var2;
         }

         public TypeAdapter create(Gson var1, TypeToken var2) {
            return var2.getRawType() == this.val$type ? this.val$typeAdapter : null;
         }

         public String toString() {
            return "Factory[type=" + this.val$type.getName() + ",adapter=" + this.val$typeAdapter + "]";
         }
      };
   }

   public static TypeAdapterFactory newFactory(Class var0, Class var1, TypeAdapter var2) {
      return new TypeAdapterFactory(var0, var1, var2) {
         final Class val$unboxed;
         final Class val$boxed;
         final TypeAdapter val$typeAdapter;

         {
            this.val$unboxed = var1;
            this.val$boxed = var2;
            this.val$typeAdapter = var3;
         }

         public TypeAdapter create(Gson var1, TypeToken var2) {
            Class var3 = var2.getRawType();
            return var3 != this.val$unboxed && var3 != this.val$boxed ? null : this.val$typeAdapter;
         }

         public String toString() {
            return "Factory[type=" + this.val$boxed.getName() + "+" + this.val$unboxed.getName() + ",adapter=" + this.val$typeAdapter + "]";
         }
      };
   }

   public static TypeAdapterFactory newFactoryForMultipleTypes(Class var0, Class var1, TypeAdapter var2) {
      return new TypeAdapterFactory(var0, var1, var2) {
         final Class val$base;
         final Class val$sub;
         final TypeAdapter val$typeAdapter;

         {
            this.val$base = var1;
            this.val$sub = var2;
            this.val$typeAdapter = var3;
         }

         public TypeAdapter create(Gson var1, TypeToken var2) {
            Class var3 = var2.getRawType();
            return var3 != this.val$base && var3 != this.val$sub ? null : this.val$typeAdapter;
         }

         public String toString() {
            return "Factory[type=" + this.val$base.getName() + "+" + this.val$sub.getName() + ",adapter=" + this.val$typeAdapter + "]";
         }
      };
   }

   public static TypeAdapterFactory newTypeHierarchyFactory(Class var0, TypeAdapter var1) {
      return new TypeAdapterFactory(var0, var1) {
         final Class val$clazz;
         final TypeAdapter val$typeAdapter;

         {
            this.val$clazz = var1;
            this.val$typeAdapter = var2;
         }

         public TypeAdapter create(Gson var1, TypeToken var2) {
            return this.val$clazz.isAssignableFrom(var2.getRawType()) ? this.val$typeAdapter : null;
         }

         public String toString() {
            return "Factory[typeHierarchy=" + this.val$clazz.getName() + ",adapter=" + this.val$typeAdapter + "]";
         }
      };
   }

   static {
      CLASS_FACTORY = newFactory(Class.class, CLASS);
      BIT_SET = new TypeAdapter() {
         public BitSet read(JsonReader var1) throws IOException {
            if (var1.peek() == JsonToken.NULL) {
               var1.nextNull();
               return null;
            } else {
               BitSet var2 = new BitSet();
               var1.beginArray();
               int var3 = 0;

               for(JsonToken var4 = var1.peek(); var4 != JsonToken.END_ARRAY; var4 = var1.peek()) {
                  boolean var5;
                  switch(var4) {
                  case NUMBER:
                     var5 = var1.nextInt() != 0;
                     break;
                  case BOOLEAN:
                     var5 = var1.nextBoolean();
                     break;
                  case STRING:
                     String var6 = var1.nextString();

                     try {
                        var5 = Integer.parseInt(var6) != 0;
                        break;
                     } catch (NumberFormatException var8) {
                        throw new JsonSyntaxException("Error: Expecting: bitset number value (1, 0), Found: " + var6);
                     }
                  default:
                     throw new JsonSyntaxException("Invalid bitset value type: " + var4);
                  }

                  if (var5) {
                     var2.set(var3);
                  }

                  ++var3;
               }

               var1.endArray();
               return var2;
            }
         }

         public void write(JsonWriter var1, BitSet var2) throws IOException {
            if (var2 == null) {
               var1.nullValue();
            } else {
               var1.beginArray();

               for(int var3 = 0; var3 < var2.length(); ++var3) {
                  int var4 = var2.get(var3) ? 1 : 0;
                  var1.value((long)var4);
               }

               var1.endArray();
            }
         }

         public Object read(JsonReader var1) throws IOException {
            return this.read(var1);
         }

         public void write(JsonWriter var1, Object var2) throws IOException {
            this.write(var1, (BitSet)var2);
         }
      };
      BIT_SET_FACTORY = newFactory(BitSet.class, BIT_SET);
      BOOLEAN = new TypeAdapter() {
         public Boolean read(JsonReader var1) throws IOException {
            if (var1.peek() == JsonToken.NULL) {
               var1.nextNull();
               return null;
            } else {
               return var1.peek() == JsonToken.STRING ? Boolean.parseBoolean(var1.nextString()) : var1.nextBoolean();
            }
         }

         public void write(JsonWriter var1, Boolean var2) throws IOException {
            if (var2 == null) {
               var1.nullValue();
            } else {
               var1.value(var2);
            }
         }

         public Object read(JsonReader var1) throws IOException {
            return this.read(var1);
         }

         public void write(JsonWriter var1, Object var2) throws IOException {
            this.write(var1, (Boolean)var2);
         }
      };
      BOOLEAN_AS_STRING = new TypeAdapter() {
         public Boolean read(JsonReader var1) throws IOException {
            if (var1.peek() == JsonToken.NULL) {
               var1.nextNull();
               return null;
            } else {
               return Boolean.valueOf(var1.nextString());
            }
         }

         public void write(JsonWriter var1, Boolean var2) throws IOException {
            var1.value(var2 == null ? "null" : var2.toString());
         }

         public Object read(JsonReader var1) throws IOException {
            return this.read(var1);
         }

         public void write(JsonWriter var1, Object var2) throws IOException {
            this.write(var1, (Boolean)var2);
         }
      };
      BOOLEAN_FACTORY = newFactory(Boolean.TYPE, Boolean.class, BOOLEAN);
      BYTE = new TypeAdapter() {
         public Number read(JsonReader var1) throws IOException {
            if (var1.peek() == JsonToken.NULL) {
               var1.nextNull();
               return null;
            } else {
               try {
                  int var2 = var1.nextInt();
                  return (byte)var2;
               } catch (NumberFormatException var3) {
                  throw new JsonSyntaxException(var3);
               }
            }
         }

         public void write(JsonWriter var1, Number var2) throws IOException {
            var1.value(var2);
         }

         public Object read(JsonReader var1) throws IOException {
            return this.read(var1);
         }

         public void write(JsonWriter var1, Object var2) throws IOException {
            this.write(var1, (Number)var2);
         }
      };
      BYTE_FACTORY = newFactory(Byte.TYPE, Byte.class, BYTE);
      SHORT = new TypeAdapter() {
         public Number read(JsonReader var1) throws IOException {
            if (var1.peek() == JsonToken.NULL) {
               var1.nextNull();
               return null;
            } else {
               try {
                  return (short)var1.nextInt();
               } catch (NumberFormatException var3) {
                  throw new JsonSyntaxException(var3);
               }
            }
         }

         public void write(JsonWriter var1, Number var2) throws IOException {
            var1.value(var2);
         }

         public Object read(JsonReader var1) throws IOException {
            return this.read(var1);
         }

         public void write(JsonWriter var1, Object var2) throws IOException {
            this.write(var1, (Number)var2);
         }
      };
      SHORT_FACTORY = newFactory(Short.TYPE, Short.class, SHORT);
      INTEGER = new TypeAdapter() {
         public Number read(JsonReader var1) throws IOException {
            if (var1.peek() == JsonToken.NULL) {
               var1.nextNull();
               return null;
            } else {
               try {
                  return var1.nextInt();
               } catch (NumberFormatException var3) {
                  throw new JsonSyntaxException(var3);
               }
            }
         }

         public void write(JsonWriter var1, Number var2) throws IOException {
            var1.value(var2);
         }

         public Object read(JsonReader var1) throws IOException {
            return this.read(var1);
         }

         public void write(JsonWriter var1, Object var2) throws IOException {
            this.write(var1, (Number)var2);
         }
      };
      INTEGER_FACTORY = newFactory(Integer.TYPE, Integer.class, INTEGER);
      LONG = new TypeAdapter() {
         public Number read(JsonReader var1) throws IOException {
            if (var1.peek() == JsonToken.NULL) {
               var1.nextNull();
               return null;
            } else {
               try {
                  return var1.nextLong();
               } catch (NumberFormatException var3) {
                  throw new JsonSyntaxException(var3);
               }
            }
         }

         public void write(JsonWriter var1, Number var2) throws IOException {
            var1.value(var2);
         }

         public Object read(JsonReader var1) throws IOException {
            return this.read(var1);
         }

         public void write(JsonWriter var1, Object var2) throws IOException {
            this.write(var1, (Number)var2);
         }
      };
      FLOAT = new TypeAdapter() {
         public Number read(JsonReader var1) throws IOException {
            if (var1.peek() == JsonToken.NULL) {
               var1.nextNull();
               return null;
            } else {
               return (float)var1.nextDouble();
            }
         }

         public void write(JsonWriter var1, Number var2) throws IOException {
            var1.value(var2);
         }

         public Object read(JsonReader var1) throws IOException {
            return this.read(var1);
         }

         public void write(JsonWriter var1, Object var2) throws IOException {
            this.write(var1, (Number)var2);
         }
      };
      DOUBLE = new TypeAdapter() {
         public Number read(JsonReader var1) throws IOException {
            if (var1.peek() == JsonToken.NULL) {
               var1.nextNull();
               return null;
            } else {
               return var1.nextDouble();
            }
         }

         public void write(JsonWriter var1, Number var2) throws IOException {
            var1.value(var2);
         }

         public Object read(JsonReader var1) throws IOException {
            return this.read(var1);
         }

         public void write(JsonWriter var1, Object var2) throws IOException {
            this.write(var1, (Number)var2);
         }
      };
      NUMBER = new TypeAdapter() {
         public Number read(JsonReader var1) throws IOException {
            JsonToken var2 = var1.peek();
            switch(var2) {
            case NUMBER:
               return new LazilyParsedNumber(var1.nextString());
            case NULL:
               var1.nextNull();
               return null;
            default:
               throw new JsonSyntaxException("Expecting number, got: " + var2);
            }
         }

         public void write(JsonWriter var1, Number var2) throws IOException {
            var1.value(var2);
         }

         public Object read(JsonReader var1) throws IOException {
            return this.read(var1);
         }

         public void write(JsonWriter var1, Object var2) throws IOException {
            this.write(var1, (Number)var2);
         }
      };
      NUMBER_FACTORY = newFactory(Number.class, NUMBER);
      CHARACTER = new TypeAdapter() {
         public Character read(JsonReader var1) throws IOException {
            if (var1.peek() == JsonToken.NULL) {
               var1.nextNull();
               return null;
            } else {
               String var2 = var1.nextString();
               if (var2.length() != 1) {
                  throw new JsonSyntaxException("Expecting character, got: " + var2);
               } else {
                  return var2.charAt(0);
               }
            }
         }

         public void write(JsonWriter var1, Character var2) throws IOException {
            var1.value(var2 == null ? null : String.valueOf(var2));
         }

         public Object read(JsonReader var1) throws IOException {
            return this.read(var1);
         }

         public void write(JsonWriter var1, Object var2) throws IOException {
            this.write(var1, (Character)var2);
         }
      };
      CHARACTER_FACTORY = newFactory(Character.TYPE, Character.class, CHARACTER);
      STRING = new TypeAdapter() {
         public String read(JsonReader var1) throws IOException {
            JsonToken var2 = var1.peek();
            if (var2 == JsonToken.NULL) {
               var1.nextNull();
               return null;
            } else {
               return var2 == JsonToken.BOOLEAN ? Boolean.toString(var1.nextBoolean()) : var1.nextString();
            }
         }

         public void write(JsonWriter var1, String var2) throws IOException {
            var1.value(var2);
         }

         public Object read(JsonReader var1) throws IOException {
            return this.read(var1);
         }

         public void write(JsonWriter var1, Object var2) throws IOException {
            this.write(var1, (String)var2);
         }
      };
      BIG_DECIMAL = new TypeAdapter() {
         public BigDecimal read(JsonReader var1) throws IOException {
            if (var1.peek() == JsonToken.NULL) {
               var1.nextNull();
               return null;
            } else {
               try {
                  return new BigDecimal(var1.nextString());
               } catch (NumberFormatException var3) {
                  throw new JsonSyntaxException(var3);
               }
            }
         }

         public void write(JsonWriter var1, BigDecimal var2) throws IOException {
            var1.value((Number)var2);
         }

         public Object read(JsonReader var1) throws IOException {
            return this.read(var1);
         }

         public void write(JsonWriter var1, Object var2) throws IOException {
            this.write(var1, (BigDecimal)var2);
         }
      };
      BIG_INTEGER = new TypeAdapter() {
         public BigInteger read(JsonReader var1) throws IOException {
            if (var1.peek() == JsonToken.NULL) {
               var1.nextNull();
               return null;
            } else {
               try {
                  return new BigInteger(var1.nextString());
               } catch (NumberFormatException var3) {
                  throw new JsonSyntaxException(var3);
               }
            }
         }

         public void write(JsonWriter var1, BigInteger var2) throws IOException {
            var1.value((Number)var2);
         }

         public Object read(JsonReader var1) throws IOException {
            return this.read(var1);
         }

         public void write(JsonWriter var1, Object var2) throws IOException {
            this.write(var1, (BigInteger)var2);
         }
      };
      STRING_FACTORY = newFactory(String.class, STRING);
      STRING_BUILDER = new TypeAdapter() {
         public StringBuilder read(JsonReader var1) throws IOException {
            if (var1.peek() == JsonToken.NULL) {
               var1.nextNull();
               return null;
            } else {
               return new StringBuilder(var1.nextString());
            }
         }

         public void write(JsonWriter var1, StringBuilder var2) throws IOException {
            var1.value(var2 == null ? null : var2.toString());
         }

         public Object read(JsonReader var1) throws IOException {
            return this.read(var1);
         }

         public void write(JsonWriter var1, Object var2) throws IOException {
            this.write(var1, (StringBuilder)var2);
         }
      };
      STRING_BUILDER_FACTORY = newFactory(StringBuilder.class, STRING_BUILDER);
      STRING_BUFFER = new TypeAdapter() {
         public StringBuffer read(JsonReader var1) throws IOException {
            if (var1.peek() == JsonToken.NULL) {
               var1.nextNull();
               return null;
            } else {
               return new StringBuffer(var1.nextString());
            }
         }

         public void write(JsonWriter var1, StringBuffer var2) throws IOException {
            var1.value(var2 == null ? null : var2.toString());
         }

         public Object read(JsonReader var1) throws IOException {
            return this.read(var1);
         }

         public void write(JsonWriter var1, Object var2) throws IOException {
            this.write(var1, (StringBuffer)var2);
         }
      };
      STRING_BUFFER_FACTORY = newFactory(StringBuffer.class, STRING_BUFFER);
      URL = new TypeAdapter() {
         public URL read(JsonReader var1) throws IOException {
            if (var1.peek() == JsonToken.NULL) {
               var1.nextNull();
               return null;
            } else {
               String var2 = var1.nextString();
               return "null".equals(var2) ? null : new URL(var2);
            }
         }

         public void write(JsonWriter var1, URL var2) throws IOException {
            var1.value(var2 == null ? null : var2.toExternalForm());
         }

         public Object read(JsonReader var1) throws IOException {
            return this.read(var1);
         }

         public void write(JsonWriter var1, Object var2) throws IOException {
            this.write(var1, (URL)var2);
         }
      };
      URL_FACTORY = newFactory(URL.class, URL);
      URI = new TypeAdapter() {
         public URI read(JsonReader var1) throws IOException {
            if (var1.peek() == JsonToken.NULL) {
               var1.nextNull();
               return null;
            } else {
               try {
                  String var2 = var1.nextString();
                  return "null".equals(var2) ? null : new URI(var2);
               } catch (URISyntaxException var3) {
                  throw new JsonIOException(var3);
               }
            }
         }

         public void write(JsonWriter var1, URI var2) throws IOException {
            var1.value(var2 == null ? null : var2.toASCIIString());
         }

         public Object read(JsonReader var1) throws IOException {
            return this.read(var1);
         }

         public void write(JsonWriter var1, Object var2) throws IOException {
            this.write(var1, (URI)var2);
         }
      };
      URI_FACTORY = newFactory(URI.class, URI);
      INET_ADDRESS = new TypeAdapter() {
         public InetAddress read(JsonReader var1) throws IOException {
            if (var1.peek() == JsonToken.NULL) {
               var1.nextNull();
               return null;
            } else {
               return InetAddress.getByName(var1.nextString());
            }
         }

         public void write(JsonWriter var1, InetAddress var2) throws IOException {
            var1.value(var2 == null ? null : var2.getHostAddress());
         }

         public Object read(JsonReader var1) throws IOException {
            return this.read(var1);
         }

         public void write(JsonWriter var1, Object var2) throws IOException {
            this.write(var1, (InetAddress)var2);
         }
      };
      INET_ADDRESS_FACTORY = newTypeHierarchyFactory(InetAddress.class, INET_ADDRESS);
      UUID = new TypeAdapter() {
         public UUID read(JsonReader var1) throws IOException {
            if (var1.peek() == JsonToken.NULL) {
               var1.nextNull();
               return null;
            } else {
               return java.util.UUID.fromString(var1.nextString());
            }
         }

         public void write(JsonWriter var1, UUID var2) throws IOException {
            var1.value(var2 == null ? null : var2.toString());
         }

         public Object read(JsonReader var1) throws IOException {
            return this.read(var1);
         }

         public void write(JsonWriter var1, Object var2) throws IOException {
            this.write(var1, (UUID)var2);
         }
      };
      UUID_FACTORY = newFactory(UUID.class, UUID);
      TIMESTAMP_FACTORY = new TypeAdapterFactory() {
         public TypeAdapter create(Gson var1, TypeToken var2) {
            if (var2.getRawType() != Timestamp.class) {
               return null;
            } else {
               TypeAdapter var3 = var1.getAdapter(Date.class);
               return new TypeAdapter(this, var3) {
                  final TypeAdapter val$dateTypeAdapter;
                  final <undefinedtype> this$0;

                  {
                     this.this$0 = var1;
                     this.val$dateTypeAdapter = var2;
                  }

                  public Timestamp read(JsonReader var1) throws IOException {
                     Date var2 = (Date)this.val$dateTypeAdapter.read(var1);
                     return var2 != null ? new Timestamp(var2.getTime()) : null;
                  }

                  public void write(JsonWriter var1, Timestamp var2) throws IOException {
                     this.val$dateTypeAdapter.write(var1, var2);
                  }

                  public Object read(JsonReader var1) throws IOException {
                     return this.read(var1);
                  }

                  public void write(JsonWriter var1, Object var2) throws IOException {
                     this.write(var1, (Timestamp)var2);
                  }
               };
            }
         }
      };
      CALENDAR = new TypeAdapter() {
         private static final String YEAR = "year";
         private static final String MONTH = "month";
         private static final String DAY_OF_MONTH = "dayOfMonth";
         private static final String HOUR_OF_DAY = "hourOfDay";
         private static final String MINUTE = "minute";
         private static final String SECOND = "second";

         public Calendar read(JsonReader var1) throws IOException {
            if (var1.peek() == JsonToken.NULL) {
               var1.nextNull();
               return null;
            } else {
               var1.beginObject();
               int var2 = 0;
               int var3 = 0;
               int var4 = 0;
               int var5 = 0;
               int var6 = 0;
               int var7 = 0;

               while(var1.peek() != JsonToken.END_OBJECT) {
                  String var8 = var1.nextName();
                  int var9 = var1.nextInt();
                  if ("year".equals(var8)) {
                     var2 = var9;
                  } else if ("month".equals(var8)) {
                     var3 = var9;
                  } else if ("dayOfMonth".equals(var8)) {
                     var4 = var9;
                  } else if ("hourOfDay".equals(var8)) {
                     var5 = var9;
                  } else if ("minute".equals(var8)) {
                     var6 = var9;
                  } else if ("second".equals(var8)) {
                     var7 = var9;
                  }
               }

               var1.endObject();
               return new GregorianCalendar(var2, var3, var4, var5, var6, var7);
            }
         }

         public void write(JsonWriter var1, Calendar var2) throws IOException {
            if (var2 == null) {
               var1.nullValue();
            } else {
               var1.beginObject();
               var1.name("year");
               var1.value((long)var2.get(1));
               var1.name("month");
               var1.value((long)var2.get(2));
               var1.name("dayOfMonth");
               var1.value((long)var2.get(5));
               var1.name("hourOfDay");
               var1.value((long)var2.get(11));
               var1.name("minute");
               var1.value((long)var2.get(12));
               var1.name("second");
               var1.value((long)var2.get(13));
               var1.endObject();
            }
         }

         public Object read(JsonReader var1) throws IOException {
            return this.read(var1);
         }

         public void write(JsonWriter var1, Object var2) throws IOException {
            this.write(var1, (Calendar)var2);
         }
      };
      CALENDAR_FACTORY = newFactoryForMultipleTypes(Calendar.class, GregorianCalendar.class, CALENDAR);
      LOCALE = new TypeAdapter() {
         public Locale read(JsonReader var1) throws IOException {
            if (var1.peek() == JsonToken.NULL) {
               var1.nextNull();
               return null;
            } else {
               String var2 = var1.nextString();
               StringTokenizer var3 = new StringTokenizer(var2, "_");
               String var4 = null;
               String var5 = null;
               String var6 = null;
               if (var3.hasMoreElements()) {
                  var4 = var3.nextToken();
               }

               if (var3.hasMoreElements()) {
                  var5 = var3.nextToken();
               }

               if (var3.hasMoreElements()) {
                  var6 = var3.nextToken();
               }

               if (var5 == null && var6 == null) {
                  return new Locale(var4);
               } else {
                  return var6 == null ? new Locale(var4, var5) : new Locale(var4, var5, var6);
               }
            }
         }

         public void write(JsonWriter var1, Locale var2) throws IOException {
            var1.value(var2 == null ? null : var2.toString());
         }

         public Object read(JsonReader var1) throws IOException {
            return this.read(var1);
         }

         public void write(JsonWriter var1, Object var2) throws IOException {
            this.write(var1, (Locale)var2);
         }
      };
      LOCALE_FACTORY = newFactory(Locale.class, LOCALE);
      JSON_ELEMENT = new TypeAdapter() {
         public JsonElement read(JsonReader var1) throws IOException {
            switch(var1.peek()) {
            case NUMBER:
               String var2 = var1.nextString();
               return new JsonPrimitive(new LazilyParsedNumber(var2));
            case BOOLEAN:
               return new JsonPrimitive(var1.nextBoolean());
            case STRING:
               return new JsonPrimitive(var1.nextString());
            case NULL:
               var1.nextNull();
               return JsonNull.INSTANCE;
            case BEGIN_ARRAY:
               JsonArray var3 = new JsonArray();
               var1.beginArray();

               while(var1.hasNext()) {
                  var3.add(this.read(var1));
               }

               var1.endArray();
               return var3;
            case BEGIN_OBJECT:
               JsonObject var4 = new JsonObject();
               var1.beginObject();

               while(var1.hasNext()) {
                  var4.add(var1.nextName(), this.read(var1));
               }

               var1.endObject();
               return var4;
            case END_DOCUMENT:
            case NAME:
            case END_OBJECT:
            case END_ARRAY:
            default:
               throw new IllegalArgumentException();
            }
         }

         public void write(JsonWriter var1, JsonElement var2) throws IOException {
            if (var2 != null && !var2.isJsonNull()) {
               if (var2.isJsonPrimitive()) {
                  JsonPrimitive var3 = var2.getAsJsonPrimitive();
                  if (var3.isNumber()) {
                     var1.value(var3.getAsNumber());
                  } else if (var3.isBoolean()) {
                     var1.value(var3.getAsBoolean());
                  } else {
                     var1.value(var3.getAsString());
                  }
               } else {
                  Iterator var5;
                  if (var2.isJsonArray()) {
                     var1.beginArray();
                     var5 = var2.getAsJsonArray().iterator();

                     while(var5.hasNext()) {
                        JsonElement var4 = (JsonElement)var5.next();
                        this.write(var1, var4);
                     }

                     var1.endArray();
                  } else {
                     if (!var2.isJsonObject()) {
                        throw new IllegalArgumentException("Couldn't write " + var2.getClass());
                     }

                     var1.beginObject();
                     var5 = var2.getAsJsonObject().entrySet().iterator();

                     while(var5.hasNext()) {
                        Entry var6 = (Entry)var5.next();
                        var1.name((String)var6.getKey());
                        this.write(var1, (JsonElement)var6.getValue());
                     }

                     var1.endObject();
                  }
               }
            } else {
               var1.nullValue();
            }

         }

         public Object read(JsonReader var1) throws IOException {
            return this.read(var1);
         }

         public void write(JsonWriter var1, Object var2) throws IOException {
            this.write(var1, (JsonElement)var2);
         }
      };
      JSON_ELEMENT_FACTORY = newTypeHierarchyFactory(JsonElement.class, JSON_ELEMENT);
      ENUM_FACTORY = newEnumTypeHierarchyFactory();
   }

   private static final class EnumTypeAdapter extends TypeAdapter {
      private final Map nameToConstant = new HashMap();
      private final Map constantToName = new HashMap();

      public EnumTypeAdapter(Class var1) {
         try {
            Enum[] var2 = (Enum[])var1.getEnumConstants();
            int var3 = var2.length;

            for(int var4 = 0; var4 < var3; ++var4) {
               Enum var5 = var2[var4];
               String var6 = var5.name();
               SerializedName var7 = (SerializedName)var1.getField(var6).getAnnotation(SerializedName.class);
               if (var7 != null) {
                  var6 = var7.value();
               }

               this.nameToConstant.put(var6, var5);
               this.constantToName.put(var5, var6);
            }

         } catch (NoSuchFieldException var8) {
            throw new AssertionError();
         }
      }

      public Enum read(JsonReader var1) throws IOException {
         if (var1.peek() == JsonToken.NULL) {
            var1.nextNull();
            return null;
         } else {
            return (Enum)this.nameToConstant.get(var1.nextString());
         }
      }

      public void write(JsonWriter var1, Enum var2) throws IOException {
         var1.value(var2 == null ? null : (String)this.constantToName.get(var2));
      }

      public Object read(JsonReader var1) throws IOException {
         return this.read(var1);
      }

      public void write(JsonWriter var1, Object var2) throws IOException {
         this.write(var1, (Enum)var2);
      }
   }
}
