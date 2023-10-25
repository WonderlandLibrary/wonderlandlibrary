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
import java.util.Locale;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.UUID;

public final class TypeAdapters {
  public static final TypeAdapter<Class> CLASS = new TypeAdapter<Class>() {
      public void write(JsonWriter out, Class value) throws IOException {
        if (value == null) {
          out.nullValue();
        } else {
          throw new UnsupportedOperationException("Attempted to serialize java.lang.Class: " + value.getName() + ". Forgot to register a type adapter?");
        } 
      }
      
      public Class read(JsonReader in) throws IOException {
        if (in.peek() == JsonToken.NULL) {
          in.nextNull();
          return null;
        } 
        throw new UnsupportedOperationException("Attempted to deserialize a java.lang.Class. Forgot to register a type adapter?");
      }
    };
  
  public static final TypeAdapterFactory CLASS_FACTORY = newFactory(Class.class, CLASS);
  
  public static final TypeAdapter<BitSet> BIT_SET = new TypeAdapter<BitSet>() {
      public BitSet read(JsonReader in) throws IOException {
        if (in.peek() == JsonToken.NULL) {
          in.nextNull();
          return null;
        } 
        BitSet bitset = new BitSet();
        in.beginArray();
        int i = 0;
        JsonToken tokenType = in.peek();
        while (tokenType != JsonToken.END_ARRAY) {
          boolean set;
          String stringValue;
          switch (tokenType) {
            case NUMBER:
              set = (in.nextInt() != 0);
              break;
            case BOOLEAN:
              set = in.nextBoolean();
              break;
            case STRING:
              stringValue = in.nextString();
              try {
                set = (Integer.parseInt(stringValue) != 0);
              } catch (NumberFormatException e) {
                throw new JsonSyntaxException("Error: Expecting: bitset number value (1, 0), Found: " + stringValue);
              } 
              break;
            default:
              throw new JsonSyntaxException("Invalid bitset value type: " + tokenType);
          } 
          if (set)
            bitset.set(i); 
          i++;
          tokenType = in.peek();
        } 
        in.endArray();
        return bitset;
      }
      
      public void write(JsonWriter out, BitSet src) throws IOException {
        if (src == null) {
          out.nullValue();
          return;
        } 
        out.beginArray();
        for (int i = 0; i < src.length(); i++) {
          int value = src.get(i) ? 1 : 0;
          out.value(value);
        } 
        out.endArray();
      }
    };
  
  public static final TypeAdapterFactory BIT_SET_FACTORY = newFactory(BitSet.class, BIT_SET);
  
  public static final TypeAdapter<Boolean> BOOLEAN = new TypeAdapter<Boolean>() {
      public Boolean read(JsonReader in) throws IOException {
        if (in.peek() == JsonToken.NULL) {
          in.nextNull();
          return null;
        } 
        if (in.peek() == JsonToken.STRING)
          return Boolean.valueOf(Boolean.parseBoolean(in.nextString())); 
        return Boolean.valueOf(in.nextBoolean());
      }
      
      public void write(JsonWriter out, Boolean value) throws IOException {
        if (value == null) {
          out.nullValue();
          return;
        } 
        out.value(value.booleanValue());
      }
    };
  
  public static final TypeAdapter<Boolean> BOOLEAN_AS_STRING = new TypeAdapter<Boolean>() {
      public Boolean read(JsonReader in) throws IOException {
        if (in.peek() == JsonToken.NULL) {
          in.nextNull();
          return null;
        } 
        return Boolean.valueOf(in.nextString());
      }
      
      public void write(JsonWriter out, Boolean value) throws IOException {
        out.value((value == null) ? "null" : value.toString());
      }
    };
  
  public static final TypeAdapterFactory BOOLEAN_FACTORY = newFactory(boolean.class, (Class)Boolean.class, (TypeAdapter)BOOLEAN);
  
  public static final TypeAdapter<Number> BYTE = new TypeAdapter<Number>() {
      public Number read(JsonReader in) throws IOException {
        if (in.peek() == JsonToken.NULL) {
          in.nextNull();
          return null;
        } 
        try {
          int intValue = in.nextInt();
          return Byte.valueOf((byte)intValue);
        } catch (NumberFormatException e) {
          throw new JsonSyntaxException(e);
        } 
      }
      
      public void write(JsonWriter out, Number value) throws IOException {
        out.value(value);
      }
    };
  
  public static final TypeAdapterFactory BYTE_FACTORY = newFactory(byte.class, (Class)Byte.class, (TypeAdapter)BYTE);
  
  public static final TypeAdapter<Number> SHORT = new TypeAdapter<Number>() {
      public Number read(JsonReader in) throws IOException {
        if (in.peek() == JsonToken.NULL) {
          in.nextNull();
          return null;
        } 
        try {
          return Short.valueOf((short)in.nextInt());
        } catch (NumberFormatException e) {
          throw new JsonSyntaxException(e);
        } 
      }
      
      public void write(JsonWriter out, Number value) throws IOException {
        out.value(value);
      }
    };
  
  public static final TypeAdapterFactory SHORT_FACTORY = newFactory(short.class, (Class)Short.class, (TypeAdapter)SHORT);
  
  public static final TypeAdapter<Number> INTEGER = new TypeAdapter<Number>() {
      public Number read(JsonReader in) throws IOException {
        if (in.peek() == JsonToken.NULL) {
          in.nextNull();
          return null;
        } 
        try {
          return Integer.valueOf(in.nextInt());
        } catch (NumberFormatException e) {
          throw new JsonSyntaxException(e);
        } 
      }
      
      public void write(JsonWriter out, Number value) throws IOException {
        out.value(value);
      }
    };
  
  public static final TypeAdapterFactory INTEGER_FACTORY = newFactory(int.class, (Class)Integer.class, (TypeAdapter)INTEGER);
  
  public static final TypeAdapter<Number> LONG = new TypeAdapter<Number>() {
      public Number read(JsonReader in) throws IOException {
        if (in.peek() == JsonToken.NULL) {
          in.nextNull();
          return null;
        } 
        try {
          return Long.valueOf(in.nextLong());
        } catch (NumberFormatException e) {
          throw new JsonSyntaxException(e);
        } 
      }
      
      public void write(JsonWriter out, Number value) throws IOException {
        out.value(value);
      }
    };
  
  public static final TypeAdapter<Number> FLOAT = new TypeAdapter<Number>() {
      public Number read(JsonReader in) throws IOException {
        if (in.peek() == JsonToken.NULL) {
          in.nextNull();
          return null;
        } 
        return Float.valueOf((float)in.nextDouble());
      }
      
      public void write(JsonWriter out, Number value) throws IOException {
        out.value(value);
      }
    };
  
  public static final TypeAdapter<Number> DOUBLE = new TypeAdapter<Number>() {
      public Number read(JsonReader in) throws IOException {
        if (in.peek() == JsonToken.NULL) {
          in.nextNull();
          return null;
        } 
        return Double.valueOf(in.nextDouble());
      }
      
      public void write(JsonWriter out, Number value) throws IOException {
        out.value(value);
      }
    };
  
  public static final TypeAdapter<Number> NUMBER = new TypeAdapter<Number>() {
      public Number read(JsonReader in) throws IOException {
        JsonToken jsonToken = in.peek();
        switch (jsonToken) {
          case NULL:
            in.nextNull();
            return null;
          case NUMBER:
            return (Number)new LazilyParsedNumber(in.nextString());
        } 
        throw new JsonSyntaxException("Expecting number, got: " + jsonToken);
      }
      
      public void write(JsonWriter out, Number value) throws IOException {
        out.value(value);
      }
    };
  
  public static final TypeAdapterFactory NUMBER_FACTORY = newFactory(Number.class, NUMBER);
  
  public static final TypeAdapter<Character> CHARACTER = new TypeAdapter<Character>() {
      public Character read(JsonReader in) throws IOException {
        if (in.peek() == JsonToken.NULL) {
          in.nextNull();
          return null;
        } 
        String str = in.nextString();
        if (str.length() != 1)
          throw new JsonSyntaxException("Expecting character, got: " + str); 
        return Character.valueOf(str.charAt(0));
      }
      
      public void write(JsonWriter out, Character value) throws IOException {
        out.value((value == null) ? null : String.valueOf(value));
      }
    };
  
  public static final TypeAdapterFactory CHARACTER_FACTORY = newFactory(char.class, (Class)Character.class, (TypeAdapter)CHARACTER);
  
  public static final TypeAdapter<String> STRING = new TypeAdapter<String>() {
      public String read(JsonReader in) throws IOException {
        JsonToken peek = in.peek();
        if (peek == JsonToken.NULL) {
          in.nextNull();
          return null;
        } 
        if (peek == JsonToken.BOOLEAN)
          return Boolean.toString(in.nextBoolean()); 
        return in.nextString();
      }
      
      public void write(JsonWriter out, String value) throws IOException {
        out.value(value);
      }
    };
  
  public static final TypeAdapter<BigDecimal> BIG_DECIMAL = new TypeAdapter<BigDecimal>() {
      public BigDecimal read(JsonReader in) throws IOException {
        if (in.peek() == JsonToken.NULL) {
          in.nextNull();
          return null;
        } 
        try {
          return new BigDecimal(in.nextString());
        } catch (NumberFormatException e) {
          throw new JsonSyntaxException(e);
        } 
      }
      
      public void write(JsonWriter out, BigDecimal value) throws IOException {
        out.value(value);
      }
    };
  
  public static final TypeAdapter<BigInteger> BIG_INTEGER = new TypeAdapter<BigInteger>() {
      public BigInteger read(JsonReader in) throws IOException {
        if (in.peek() == JsonToken.NULL) {
          in.nextNull();
          return null;
        } 
        try {
          return new BigInteger(in.nextString());
        } catch (NumberFormatException e) {
          throw new JsonSyntaxException(e);
        } 
      }
      
      public void write(JsonWriter out, BigInteger value) throws IOException {
        out.value(value);
      }
    };
  
  public static final TypeAdapterFactory STRING_FACTORY = newFactory(String.class, STRING);
  
  public static final TypeAdapter<StringBuilder> STRING_BUILDER = new TypeAdapter<StringBuilder>() {
      public StringBuilder read(JsonReader in) throws IOException {
        if (in.peek() == JsonToken.NULL) {
          in.nextNull();
          return null;
        } 
        return new StringBuilder(in.nextString());
      }
      
      public void write(JsonWriter out, StringBuilder value) throws IOException {
        out.value((value == null) ? null : value.toString());
      }
    };
  
  public static final TypeAdapterFactory STRING_BUILDER_FACTORY = newFactory(StringBuilder.class, STRING_BUILDER);
  
  public static final TypeAdapter<StringBuffer> STRING_BUFFER = new TypeAdapter<StringBuffer>() {
      public StringBuffer read(JsonReader in) throws IOException {
        if (in.peek() == JsonToken.NULL) {
          in.nextNull();
          return null;
        } 
        return new StringBuffer(in.nextString());
      }
      
      public void write(JsonWriter out, StringBuffer value) throws IOException {
        out.value((value == null) ? null : value.toString());
      }
    };
  
  public static final TypeAdapterFactory STRING_BUFFER_FACTORY = newFactory(StringBuffer.class, STRING_BUFFER);
  
  public static final TypeAdapter<URL> URL = new TypeAdapter<URL>() {
      public URL read(JsonReader in) throws IOException {
        if (in.peek() == JsonToken.NULL) {
          in.nextNull();
          return null;
        } 
        String nextString = in.nextString();
        return "null".equals(nextString) ? null : new URL(nextString);
      }
      
      public void write(JsonWriter out, URL value) throws IOException {
        out.value((value == null) ? null : value.toExternalForm());
      }
    };
  
  public static final TypeAdapterFactory URL_FACTORY = newFactory(URL.class, URL);
  
  public static final TypeAdapter<URI> URI = new TypeAdapter<URI>() {
      public URI read(JsonReader in) throws IOException {
        if (in.peek() == JsonToken.NULL) {
          in.nextNull();
          return null;
        } 
        try {
          String nextString = in.nextString();
          return "null".equals(nextString) ? null : new URI(nextString);
        } catch (URISyntaxException e) {
          throw new JsonIOException(e);
        } 
      }
      
      public void write(JsonWriter out, URI value) throws IOException {
        out.value((value == null) ? null : value.toASCIIString());
      }
    };
  
  public static final TypeAdapterFactory URI_FACTORY = newFactory(URI.class, URI);
  
  public static final TypeAdapter<InetAddress> INET_ADDRESS = new TypeAdapter<InetAddress>() {
      public InetAddress read(JsonReader in) throws IOException {
        if (in.peek() == JsonToken.NULL) {
          in.nextNull();
          return null;
        } 
        return InetAddress.getByName(in.nextString());
      }
      
      public void write(JsonWriter out, InetAddress value) throws IOException {
        out.value((value == null) ? null : value.getHostAddress());
      }
    };
  
  public static final TypeAdapterFactory INET_ADDRESS_FACTORY = newTypeHierarchyFactory(InetAddress.class, INET_ADDRESS);
  
  public static final TypeAdapter<UUID> UUID = new TypeAdapter<UUID>() {
      public UUID read(JsonReader in) throws IOException {
        if (in.peek() == JsonToken.NULL) {
          in.nextNull();
          return null;
        } 
        return UUID.fromString(in.nextString());
      }
      
      public void write(JsonWriter out, UUID value) throws IOException {
        out.value((value == null) ? null : value.toString());
      }
    };
  
  public static final TypeAdapterFactory UUID_FACTORY = newFactory(UUID.class, UUID);
  
  public static final TypeAdapterFactory TIMESTAMP_FACTORY = new TypeAdapterFactory() {
      public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> typeToken) {
        if (typeToken.getRawType() != Timestamp.class)
          return null; 
        final TypeAdapter<Date> dateTypeAdapter = gson.getAdapter(Date.class);
        return (TypeAdapter)new TypeAdapter<Timestamp>() {
            public Timestamp read(JsonReader in) throws IOException {
              Date date = (Date)dateTypeAdapter.read(in);
              return (date != null) ? new Timestamp(date.getTime()) : null;
            }
            
            public void write(JsonWriter out, Timestamp value) throws IOException {
              dateTypeAdapter.write(out, value);
            }
          };
      }
    };
  
  public static final TypeAdapter<Calendar> CALENDAR = new TypeAdapter<Calendar>() {
      private static final String YEAR = "year";
      
      private static final String MONTH = "month";
      
      private static final String DAY_OF_MONTH = "dayOfMonth";
      
      private static final String HOUR_OF_DAY = "hourOfDay";
      
      private static final String MINUTE = "minute";
      
      private static final String SECOND = "second";
      
      public Calendar read(JsonReader in) throws IOException {
        if (in.peek() == JsonToken.NULL) {
          in.nextNull();
          return null;
        } 
        in.beginObject();
        int year = 0;
        int month = 0;
        int dayOfMonth = 0;
        int hourOfDay = 0;
        int minute = 0;
        int second = 0;
        while (in.peek() != JsonToken.END_OBJECT) {
          String name = in.nextName();
          int value = in.nextInt();
          if ("year".equals(name)) {
            year = value;
            continue;
          } 
          if ("month".equals(name)) {
            month = value;
            continue;
          } 
          if ("dayOfMonth".equals(name)) {
            dayOfMonth = value;
            continue;
          } 
          if ("hourOfDay".equals(name)) {
            hourOfDay = value;
            continue;
          } 
          if ("minute".equals(name)) {
            minute = value;
            continue;
          } 
          if ("second".equals(name))
            second = value; 
        } 
        in.endObject();
        return new GregorianCalendar(year, month, dayOfMonth, hourOfDay, minute, second);
      }
      
      public void write(JsonWriter out, Calendar value) throws IOException {
        if (value == null) {
          out.nullValue();
          return;
        } 
        out.beginObject();
        out.name("year");
        out.value(value.get(1));
        out.name("month");
        out.value(value.get(2));
        out.name("dayOfMonth");
        out.value(value.get(5));
        out.name("hourOfDay");
        out.value(value.get(11));
        out.name("minute");
        out.value(value.get(12));
        out.name("second");
        out.value(value.get(13));
        out.endObject();
      }
    };
  
  public static final TypeAdapterFactory CALENDAR_FACTORY = newFactoryForMultipleTypes(Calendar.class, (Class)GregorianCalendar.class, CALENDAR);
  
  public static final TypeAdapter<Locale> LOCALE = new TypeAdapter<Locale>() {
      public Locale read(JsonReader in) throws IOException {
        if (in.peek() == JsonToken.NULL) {
          in.nextNull();
          return null;
        } 
        String locale = in.nextString();
        StringTokenizer tokenizer = new StringTokenizer(locale, "_");
        String language = null;
        String country = null;
        String variant = null;
        if (tokenizer.hasMoreElements())
          language = tokenizer.nextToken(); 
        if (tokenizer.hasMoreElements())
          country = tokenizer.nextToken(); 
        if (tokenizer.hasMoreElements())
          variant = tokenizer.nextToken(); 
        if (country == null && variant == null)
          return new Locale(language); 
        if (variant == null)
          return new Locale(language, country); 
        return new Locale(language, country, variant);
      }
      
      public void write(JsonWriter out, Locale value) throws IOException {
        out.value((value == null) ? null : value.toString());
      }
    };
  
  public static final TypeAdapterFactory LOCALE_FACTORY = newFactory(Locale.class, LOCALE);
  
  public static final TypeAdapter<JsonElement> JSON_ELEMENT = new TypeAdapter<JsonElement>() {
      public JsonElement read(JsonReader in) throws IOException {
        String number;
        JsonArray array;
        JsonObject object;
        switch (in.peek()) {
          case STRING:
            return (JsonElement)new JsonPrimitive(in.nextString());
          case NUMBER:
            number = in.nextString();
            return (JsonElement)new JsonPrimitive((Number)new LazilyParsedNumber(number));
          case BOOLEAN:
            return (JsonElement)new JsonPrimitive(Boolean.valueOf(in.nextBoolean()));
          case NULL:
            in.nextNull();
            return (JsonElement)JsonNull.INSTANCE;
          case BEGIN_ARRAY:
            array = new JsonArray();
            in.beginArray();
            while (in.hasNext())
              array.add(read(in)); 
            in.endArray();
            return (JsonElement)array;
          case BEGIN_OBJECT:
            object = new JsonObject();
            in.beginObject();
            while (in.hasNext())
              object.add(in.nextName(), read(in)); 
            in.endObject();
            return (JsonElement)object;
        } 
        throw new IllegalArgumentException();
      }
      
      public void write(JsonWriter out, JsonElement value) throws IOException {
        if (value == null || value.isJsonNull()) {
          out.nullValue();
        } else if (value.isJsonPrimitive()) {
          JsonPrimitive primitive = value.getAsJsonPrimitive();
          if (primitive.isNumber()) {
            out.value(primitive.getAsNumber());
          } else if (primitive.isBoolean()) {
            out.value(primitive.getAsBoolean());
          } else {
            out.value(primitive.getAsString());
          } 
        } else if (value.isJsonArray()) {
          out.beginArray();
          for (JsonElement e : value.getAsJsonArray())
            write(out, e); 
          out.endArray();
        } else if (value.isJsonObject()) {
          out.beginObject();
          for (Map.Entry<String, JsonElement> e : (Iterable<Map.Entry<String, JsonElement>>)value.getAsJsonObject().entrySet()) {
            out.name(e.getKey());
            write(out, e.getValue());
          } 
          out.endObject();
        } else {
          throw new IllegalArgumentException("Couldn't write " + value.getClass());
        } 
      }
    };
  
  public static final TypeAdapterFactory JSON_ELEMENT_FACTORY = newTypeHierarchyFactory(JsonElement.class, JSON_ELEMENT);
  
  private static final class EnumTypeAdapter<T extends Enum<T>> extends TypeAdapter<T> {
    private final Map<String, T> nameToConstant = new HashMap<String, T>();
    
    private final Map<T, String> constantToName = new HashMap<T, String>();
    
    public EnumTypeAdapter(Class<T> classOfT) {
      try {
        for (Enum enum_ : (Enum[])classOfT.getEnumConstants()) {
          String name = enum_.name();
          SerializedName annotation = classOfT.getField(name).<SerializedName>getAnnotation(SerializedName.class);
          if (annotation != null)
            name = annotation.value(); 
          this.nameToConstant.put(name, (T)enum_);
          this.constantToName.put((T)enum_, name);
        } 
      } catch (NoSuchFieldException e) {
        throw new AssertionError();
      } 
    }
    
    public T read(JsonReader in) throws IOException {
      if (in.peek() == JsonToken.NULL) {
        in.nextNull();
        return null;
      } 
      return this.nameToConstant.get(in.nextString());
    }
    
    public void write(JsonWriter out, T value) throws IOException {
      out.value((value == null) ? null : this.constantToName.get(value));
    }
  }
  
  public static final TypeAdapterFactory ENUM_FACTORY = newEnumTypeHierarchyFactory();
  
  public static TypeAdapterFactory newEnumTypeHierarchyFactory() {
    return new TypeAdapterFactory() {
        public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> typeToken) {
          Class<? super T> rawType = typeToken.getRawType();
          if (!Enum.class.isAssignableFrom(rawType) || rawType == Enum.class)
            return null; 
          if (!rawType.isEnum())
            rawType = rawType.getSuperclass(); 
          return new TypeAdapters.EnumTypeAdapter<T>(rawType);
        }
      };
  }
  
  public static <TT> TypeAdapterFactory newFactory(final TypeToken<TT> type, final TypeAdapter<TT> typeAdapter) {
    return new TypeAdapterFactory() {
        public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> typeToken) {
          return typeToken.equals(type) ? typeAdapter : null;
        }
      };
  }
  
  public static <TT> TypeAdapterFactory newFactory(final Class<TT> type, final TypeAdapter<TT> typeAdapter) {
    return new TypeAdapterFactory() {
        public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> typeToken) {
          return (typeToken.getRawType() == type) ? typeAdapter : null;
        }
        
        public String toString() {
          return "Factory[type=" + type.getName() + ",adapter=" + typeAdapter + "]";
        }
      };
  }
  
  public static <TT> TypeAdapterFactory newFactory(final Class<TT> unboxed, final Class<TT> boxed, final TypeAdapter<? super TT> typeAdapter) {
    return new TypeAdapterFactory() {
        public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> typeToken) {
          Class<? super T> rawType = typeToken.getRawType();
          return (rawType == unboxed || rawType == boxed) ? typeAdapter : null;
        }
        
        public String toString() {
          return "Factory[type=" + boxed.getName() + "+" + unboxed.getName() + ",adapter=" + typeAdapter + "]";
        }
      };
  }
  
  public static <TT> TypeAdapterFactory newFactoryForMultipleTypes(final Class<TT> base, final Class<? extends TT> sub, final TypeAdapter<? super TT> typeAdapter) {
    return new TypeAdapterFactory() {
        public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> typeToken) {
          Class<? super T> rawType = typeToken.getRawType();
          return (rawType == base || rawType == sub) ? typeAdapter : null;
        }
        
        public String toString() {
          return "Factory[type=" + base.getName() + "+" + sub.getName() + ",adapter=" + typeAdapter + "]";
        }
      };
  }
  
  public static <TT> TypeAdapterFactory newTypeHierarchyFactory(final Class<TT> clazz, final TypeAdapter<TT> typeAdapter) {
    return new TypeAdapterFactory() {
        public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> typeToken) {
          return clazz.isAssignableFrom(typeToken.getRawType()) ? typeAdapter : null;
        }
        
        public String toString() {
          return "Factory[typeHierarchy=" + clazz.getName() + ",adapter=" + typeAdapter + "]";
        }
      };
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\google\gson\internal\bind\TypeAdapters.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */