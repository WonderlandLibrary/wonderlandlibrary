/*     */ package org.yaml.snakeyaml.representer;
/*     */ 
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Calendar;
/*     */ import java.util.Date;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.TimeZone;
/*     */ import java.util.UUID;
/*     */ import java.util.regex.Pattern;
/*     */ import org.yaml.snakeyaml.DumperOptions;
/*     */ import org.yaml.snakeyaml.error.YAMLException;
/*     */ import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;
/*     */ import org.yaml.snakeyaml.nodes.Node;
/*     */ import org.yaml.snakeyaml.nodes.Tag;
/*     */ import org.yaml.snakeyaml.reader.StreamReader;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class SafeRepresenter
/*     */   extends BaseRepresenter
/*     */ {
/*     */   protected Map<Class<? extends Object>, Tag> classTags;
/*  47 */   protected TimeZone timeZone = null;
/*     */   protected DumperOptions.NonPrintableStyle nonPrintableStyle;
/*     */   
/*     */   public SafeRepresenter() {
/*  51 */     this(new DumperOptions());
/*     */   }
/*     */   
/*     */   public SafeRepresenter(DumperOptions options) {
/*  55 */     this.nullRepresenter = new RepresentNull();
/*  56 */     this.representers.put(String.class, new RepresentString());
/*  57 */     this.representers.put(Boolean.class, new RepresentBoolean());
/*  58 */     this.representers.put(Character.class, new RepresentString());
/*  59 */     this.representers.put(UUID.class, new RepresentUuid());
/*  60 */     this.representers.put(byte[].class, new RepresentByteArray());
/*     */     
/*  62 */     Represent primitiveArray = new RepresentPrimitiveArray();
/*  63 */     this.representers.put(short[].class, primitiveArray);
/*  64 */     this.representers.put(int[].class, primitiveArray);
/*  65 */     this.representers.put(long[].class, primitiveArray);
/*  66 */     this.representers.put(float[].class, primitiveArray);
/*  67 */     this.representers.put(double[].class, primitiveArray);
/*  68 */     this.representers.put(char[].class, primitiveArray);
/*  69 */     this.representers.put(boolean[].class, primitiveArray);
/*     */     
/*  71 */     this.multiRepresenters.put(Number.class, new RepresentNumber());
/*  72 */     this.multiRepresenters.put(List.class, new RepresentList());
/*  73 */     this.multiRepresenters.put(Map.class, new RepresentMap());
/*  74 */     this.multiRepresenters.put(Set.class, new RepresentSet());
/*  75 */     this.multiRepresenters.put(Iterator.class, new RepresentIterator());
/*  76 */     this.multiRepresenters.put((new Object[0]).getClass(), new RepresentArray());
/*  77 */     this.multiRepresenters.put(Date.class, new RepresentDate());
/*  78 */     this.multiRepresenters.put(Enum.class, new RepresentEnum());
/*  79 */     this.multiRepresenters.put(Calendar.class, new RepresentDate());
/*  80 */     this.classTags = new HashMap<>();
/*  81 */     this.nonPrintableStyle = options.getNonPrintableStyle();
/*     */   }
/*     */   
/*     */   protected Tag getTag(Class<?> clazz, Tag defaultTag) {
/*  85 */     if (this.classTags.containsKey(clazz)) {
/*  86 */       return this.classTags.get(clazz);
/*     */     }
/*  88 */     return defaultTag;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Tag addClassTag(Class<? extends Object> clazz, Tag tag) {
/* 103 */     if (tag == null) {
/* 104 */       throw new NullPointerException("Tag must be provided.");
/*     */     }
/* 106 */     return this.classTags.put(clazz, tag);
/*     */   }
/*     */   
/*     */   protected class RepresentNull implements Represent {
/*     */     public Node representData(Object data) {
/* 111 */       return SafeRepresenter.this.representScalar(Tag.NULL, "null");
/*     */     }
/*     */   }
/*     */   
/* 115 */   private static final Pattern MULTILINE_PATTERN = Pattern.compile("\n|| | ");
/*     */   
/*     */   protected class RepresentString implements Represent {
/*     */     public Node representData(Object data) {
/* 119 */       Tag tag = Tag.STR;
/* 120 */       DumperOptions.ScalarStyle style = null;
/* 121 */       String value = data.toString();
/* 122 */       if (SafeRepresenter.this.nonPrintableStyle == DumperOptions.NonPrintableStyle.BINARY && !StreamReader.isPrintable(value)) {
/* 123 */         char[] binary; tag = Tag.BINARY;
/*     */         
/*     */         try {
/* 126 */           byte[] bytes = value.getBytes("UTF-8");
/*     */ 
/*     */ 
/*     */           
/* 130 */           String checkValue = new String(bytes, "UTF-8");
/* 131 */           if (!checkValue.equals(value)) {
/* 132 */             throw new YAMLException("invalid string value has occurred");
/*     */           }
/* 134 */           binary = Base64Coder.encode(bytes);
/* 135 */         } catch (UnsupportedEncodingException e) {
/* 136 */           throw new YAMLException(e);
/*     */         } 
/* 138 */         value = String.valueOf(binary);
/* 139 */         style = DumperOptions.ScalarStyle.LITERAL;
/*     */       } 
/*     */ 
/*     */       
/* 143 */       if (SafeRepresenter.this.defaultScalarStyle == DumperOptions.ScalarStyle.PLAIN && SafeRepresenter.MULTILINE_PATTERN.matcher(value).find()) {
/* 144 */         style = DumperOptions.ScalarStyle.LITERAL;
/*     */       }
/* 146 */       return SafeRepresenter.this.representScalar(tag, value, style);
/*     */     }
/*     */   }
/*     */   
/*     */   protected class RepresentBoolean implements Represent {
/*     */     public Node representData(Object data) {
/*     */       String value;
/* 153 */       if (Boolean.TRUE.equals(data)) {
/* 154 */         value = "true";
/*     */       } else {
/* 156 */         value = "false";
/*     */       } 
/* 158 */       return SafeRepresenter.this.representScalar(Tag.BOOL, value);
/*     */     }
/*     */   }
/*     */   
/*     */   protected class RepresentNumber implements Represent {
/*     */     public Node representData(Object data) {
/*     */       Tag tag;
/*     */       String value;
/* 166 */       if (data instanceof Byte || data instanceof Short || data instanceof Integer || data instanceof Long || data instanceof java.math.BigInteger) {
/*     */         
/* 168 */         tag = Tag.INT;
/* 169 */         value = data.toString();
/*     */       } else {
/* 171 */         Number number = (Number)data;
/* 172 */         tag = Tag.FLOAT;
/* 173 */         if (number.equals(Double.valueOf(Double.NaN))) {
/* 174 */           value = ".NaN";
/* 175 */         } else if (number.equals(Double.valueOf(Double.POSITIVE_INFINITY))) {
/* 176 */           value = ".inf";
/* 177 */         } else if (number.equals(Double.valueOf(Double.NEGATIVE_INFINITY))) {
/* 178 */           value = "-.inf";
/*     */         } else {
/* 180 */           value = number.toString();
/*     */         } 
/*     */       } 
/* 183 */       return SafeRepresenter.this.representScalar(SafeRepresenter.this.getTag(data.getClass(), tag), value);
/*     */     }
/*     */   }
/*     */   
/*     */   protected class RepresentList
/*     */     implements Represent {
/*     */     public Node representData(Object data) {
/* 190 */       return SafeRepresenter.this.representSequence(SafeRepresenter.this.getTag(data.getClass(), Tag.SEQ), (List)data, DumperOptions.FlowStyle.AUTO);
/*     */     }
/*     */   }
/*     */   
/*     */   protected class RepresentIterator
/*     */     implements Represent {
/*     */     public Node representData(Object data) {
/* 197 */       Iterator<Object> iter = (Iterator<Object>)data;
/* 198 */       return SafeRepresenter.this.representSequence(SafeRepresenter.this.getTag(data.getClass(), Tag.SEQ), new SafeRepresenter.IteratorWrapper(iter), DumperOptions.FlowStyle.AUTO);
/*     */     }
/*     */   }
/*     */   
/*     */   private static class IteratorWrapper
/*     */     implements Iterable<Object> {
/*     */     private Iterator<Object> iter;
/*     */     
/*     */     public IteratorWrapper(Iterator<Object> iter) {
/* 207 */       this.iter = iter;
/*     */     }
/*     */     
/*     */     public Iterator<Object> iterator() {
/* 211 */       return this.iter;
/*     */     }
/*     */   }
/*     */   
/*     */   protected class RepresentArray implements Represent {
/*     */     public Node representData(Object data) {
/* 217 */       Object[] array = (Object[])data;
/* 218 */       List<Object> list = Arrays.asList(array);
/* 219 */       return SafeRepresenter.this.representSequence(Tag.SEQ, list, DumperOptions.FlowStyle.AUTO);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected class RepresentPrimitiveArray
/*     */     implements Represent
/*     */   {
/*     */     public Node representData(Object data) {
/* 230 */       Class<?> type = data.getClass().getComponentType();
/*     */       
/* 232 */       if (byte.class == type)
/* 233 */         return SafeRepresenter.this.representSequence(Tag.SEQ, asByteList(data), DumperOptions.FlowStyle.AUTO); 
/* 234 */       if (short.class == type)
/* 235 */         return SafeRepresenter.this.representSequence(Tag.SEQ, asShortList(data), DumperOptions.FlowStyle.AUTO); 
/* 236 */       if (int.class == type)
/* 237 */         return SafeRepresenter.this.representSequence(Tag.SEQ, asIntList(data), DumperOptions.FlowStyle.AUTO); 
/* 238 */       if (long.class == type)
/* 239 */         return SafeRepresenter.this.representSequence(Tag.SEQ, asLongList(data), DumperOptions.FlowStyle.AUTO); 
/* 240 */       if (float.class == type)
/* 241 */         return SafeRepresenter.this.representSequence(Tag.SEQ, asFloatList(data), DumperOptions.FlowStyle.AUTO); 
/* 242 */       if (double.class == type)
/* 243 */         return SafeRepresenter.this.representSequence(Tag.SEQ, asDoubleList(data), DumperOptions.FlowStyle.AUTO); 
/* 244 */       if (char.class == type)
/* 245 */         return SafeRepresenter.this.representSequence(Tag.SEQ, asCharList(data), DumperOptions.FlowStyle.AUTO); 
/* 246 */       if (boolean.class == type) {
/* 247 */         return SafeRepresenter.this.representSequence(Tag.SEQ, asBooleanList(data), DumperOptions.FlowStyle.AUTO);
/*     */       }
/*     */       
/* 250 */       throw new YAMLException("Unexpected primitive '" + type.getCanonicalName() + "'");
/*     */     }
/*     */     
/*     */     private List<Byte> asByteList(Object in) {
/* 254 */       byte[] array = (byte[])in;
/* 255 */       List<Byte> list = new ArrayList<>(array.length);
/* 256 */       for (int i = 0; i < array.length; i++)
/* 257 */         list.add(Byte.valueOf(array[i])); 
/* 258 */       return list;
/*     */     }
/*     */     
/*     */     private List<Short> asShortList(Object in) {
/* 262 */       short[] array = (short[])in;
/* 263 */       List<Short> list = new ArrayList<>(array.length);
/* 264 */       for (int i = 0; i < array.length; i++)
/* 265 */         list.add(Short.valueOf(array[i])); 
/* 266 */       return list;
/*     */     }
/*     */     
/*     */     private List<Integer> asIntList(Object in) {
/* 270 */       int[] array = (int[])in;
/* 271 */       List<Integer> list = new ArrayList<>(array.length);
/* 272 */       for (int i = 0; i < array.length; i++)
/* 273 */         list.add(Integer.valueOf(array[i])); 
/* 274 */       return list;
/*     */     }
/*     */     
/*     */     private List<Long> asLongList(Object in) {
/* 278 */       long[] array = (long[])in;
/* 279 */       List<Long> list = new ArrayList<>(array.length);
/* 280 */       for (int i = 0; i < array.length; i++)
/* 281 */         list.add(Long.valueOf(array[i])); 
/* 282 */       return list;
/*     */     }
/*     */     
/*     */     private List<Float> asFloatList(Object in) {
/* 286 */       float[] array = (float[])in;
/* 287 */       List<Float> list = new ArrayList<>(array.length);
/* 288 */       for (int i = 0; i < array.length; i++)
/* 289 */         list.add(Float.valueOf(array[i])); 
/* 290 */       return list;
/*     */     }
/*     */     
/*     */     private List<Double> asDoubleList(Object in) {
/* 294 */       double[] array = (double[])in;
/* 295 */       List<Double> list = new ArrayList<>(array.length);
/* 296 */       for (int i = 0; i < array.length; i++)
/* 297 */         list.add(Double.valueOf(array[i])); 
/* 298 */       return list;
/*     */     }
/*     */     
/*     */     private List<Character> asCharList(Object in) {
/* 302 */       char[] array = (char[])in;
/* 303 */       List<Character> list = new ArrayList<>(array.length);
/* 304 */       for (int i = 0; i < array.length; i++)
/* 305 */         list.add(Character.valueOf(array[i])); 
/* 306 */       return list;
/*     */     }
/*     */     
/*     */     private List<Boolean> asBooleanList(Object in) {
/* 310 */       boolean[] array = (boolean[])in;
/* 311 */       List<Boolean> list = new ArrayList<>(array.length);
/* 312 */       for (int i = 0; i < array.length; i++)
/* 313 */         list.add(Boolean.valueOf(array[i])); 
/* 314 */       return list;
/*     */     }
/*     */   }
/*     */   
/*     */   protected class RepresentMap
/*     */     implements Represent {
/*     */     public Node representData(Object data) {
/* 321 */       return SafeRepresenter.this.representMapping(SafeRepresenter.this.getTag(data.getClass(), Tag.MAP), (Map<?, ?>)data, DumperOptions.FlowStyle.AUTO);
/*     */     }
/*     */   }
/*     */   
/*     */   protected class RepresentSet
/*     */     implements Represent
/*     */   {
/*     */     public Node representData(Object data) {
/* 329 */       Map<Object, Object> value = new LinkedHashMap<>();
/* 330 */       Set<Object> set = (Set<Object>)data;
/* 331 */       for (Object key : set) {
/* 332 */         value.put(key, null);
/*     */       }
/* 334 */       return SafeRepresenter.this.representMapping(SafeRepresenter.this.getTag(data.getClass(), Tag.SET), value, DumperOptions.FlowStyle.AUTO);
/*     */     }
/*     */   }
/*     */   
/*     */   protected class RepresentDate
/*     */     implements Represent {
/*     */     public Node representData(Object data) {
/*     */       Calendar calendar;
/* 342 */       if (data instanceof Calendar) {
/* 343 */         calendar = (Calendar)data;
/*     */       } else {
/* 345 */         calendar = Calendar.getInstance((SafeRepresenter.this.getTimeZone() == null) ? TimeZone.getTimeZone("UTC") : SafeRepresenter.this.timeZone);
/*     */         
/* 347 */         calendar.setTime((Date)data);
/*     */       } 
/* 349 */       int years = calendar.get(1);
/* 350 */       int months = calendar.get(2) + 1;
/* 351 */       int days = calendar.get(5);
/* 352 */       int hour24 = calendar.get(11);
/* 353 */       int minutes = calendar.get(12);
/* 354 */       int seconds = calendar.get(13);
/* 355 */       int millis = calendar.get(14);
/* 356 */       StringBuilder buffer = new StringBuilder(String.valueOf(years));
/* 357 */       while (buffer.length() < 4)
/*     */       {
/* 359 */         buffer.insert(0, "0");
/*     */       }
/* 361 */       buffer.append("-");
/* 362 */       if (months < 10) {
/* 363 */         buffer.append("0");
/*     */       }
/* 365 */       buffer.append(String.valueOf(months));
/* 366 */       buffer.append("-");
/* 367 */       if (days < 10) {
/* 368 */         buffer.append("0");
/*     */       }
/* 370 */       buffer.append(String.valueOf(days));
/* 371 */       buffer.append("T");
/* 372 */       if (hour24 < 10) {
/* 373 */         buffer.append("0");
/*     */       }
/* 375 */       buffer.append(String.valueOf(hour24));
/* 376 */       buffer.append(":");
/* 377 */       if (minutes < 10) {
/* 378 */         buffer.append("0");
/*     */       }
/* 380 */       buffer.append(String.valueOf(minutes));
/* 381 */       buffer.append(":");
/* 382 */       if (seconds < 10) {
/* 383 */         buffer.append("0");
/*     */       }
/* 385 */       buffer.append(String.valueOf(seconds));
/* 386 */       if (millis > 0) {
/* 387 */         if (millis < 10) {
/* 388 */           buffer.append(".00");
/* 389 */         } else if (millis < 100) {
/* 390 */           buffer.append(".0");
/*     */         } else {
/* 392 */           buffer.append(".");
/*     */         } 
/* 394 */         buffer.append(String.valueOf(millis));
/*     */       } 
/*     */ 
/*     */       
/* 398 */       int gmtOffset = calendar.getTimeZone().getOffset(calendar.getTime().getTime());
/* 399 */       if (gmtOffset == 0) {
/* 400 */         buffer.append('Z');
/*     */       } else {
/* 402 */         if (gmtOffset < 0) {
/* 403 */           buffer.append('-');
/* 404 */           gmtOffset *= -1;
/*     */         } else {
/* 406 */           buffer.append('+');
/*     */         } 
/* 408 */         int minutesOffset = gmtOffset / 60000;
/* 409 */         int hoursOffset = minutesOffset / 60;
/* 410 */         int partOfHour = minutesOffset % 60;
/*     */         
/* 412 */         if (hoursOffset < 10) {
/* 413 */           buffer.append('0');
/*     */         }
/* 415 */         buffer.append(hoursOffset);
/* 416 */         buffer.append(':');
/* 417 */         if (partOfHour < 10) {
/* 418 */           buffer.append('0');
/*     */         }
/* 420 */         buffer.append(partOfHour);
/*     */       } 
/*     */       
/* 423 */       return SafeRepresenter.this.representScalar(SafeRepresenter.this.getTag(data.getClass(), Tag.TIMESTAMP), buffer.toString(), DumperOptions.ScalarStyle.PLAIN);
/*     */     }
/*     */   }
/*     */   
/*     */   protected class RepresentEnum implements Represent {
/*     */     public Node representData(Object data) {
/* 429 */       Tag tag = new Tag(data.getClass());
/* 430 */       return SafeRepresenter.this.representScalar(SafeRepresenter.this.getTag(data.getClass(), tag), ((Enum)data).name());
/*     */     }
/*     */   }
/*     */   
/*     */   protected class RepresentByteArray implements Represent {
/*     */     public Node representData(Object data) {
/* 436 */       char[] binary = Base64Coder.encode((byte[])data);
/* 437 */       return SafeRepresenter.this.representScalar(Tag.BINARY, String.valueOf(binary), DumperOptions.ScalarStyle.LITERAL);
/*     */     }
/*     */   }
/*     */   
/*     */   public TimeZone getTimeZone() {
/* 442 */     return this.timeZone;
/*     */   }
/*     */   
/*     */   public void setTimeZone(TimeZone timeZone) {
/* 446 */     this.timeZone = timeZone;
/*     */   }
/*     */   
/*     */   protected class RepresentUuid implements Represent {
/*     */     public Node representData(Object data) {
/* 451 */       return SafeRepresenter.this.representScalar(SafeRepresenter.this.getTag(data.getClass(), new Tag(UUID.class)), data.toString());
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\yaml\snakeyaml\representer\SafeRepresenter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */