/*     */ package org.yaml.snakeyaml.nodes;
/*     */ 
/*     */ import java.math.BigDecimal;
/*     */ import java.math.BigInteger;
/*     */ import java.net.URI;
/*     */ import java.util.Date;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import org.yaml.snakeyaml.error.YAMLException;
/*     */ import org.yaml.snakeyaml.util.UriEncoder;
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
/*     */ public final class Tag
/*     */ {
/*     */   public static final String PREFIX = "tag:yaml.org,2002:";
/*  32 */   public static final Tag YAML = new Tag("tag:yaml.org,2002:yaml");
/*  33 */   public static final Tag MERGE = new Tag("tag:yaml.org,2002:merge");
/*  34 */   public static final Tag SET = new Tag("tag:yaml.org,2002:set");
/*  35 */   public static final Tag PAIRS = new Tag("tag:yaml.org,2002:pairs");
/*  36 */   public static final Tag OMAP = new Tag("tag:yaml.org,2002:omap");
/*  37 */   public static final Tag BINARY = new Tag("tag:yaml.org,2002:binary");
/*  38 */   public static final Tag INT = new Tag("tag:yaml.org,2002:int");
/*  39 */   public static final Tag FLOAT = new Tag("tag:yaml.org,2002:float");
/*  40 */   public static final Tag TIMESTAMP = new Tag("tag:yaml.org,2002:timestamp");
/*  41 */   public static final Tag BOOL = new Tag("tag:yaml.org,2002:bool");
/*  42 */   public static final Tag NULL = new Tag("tag:yaml.org,2002:null");
/*  43 */   public static final Tag STR = new Tag("tag:yaml.org,2002:str");
/*  44 */   public static final Tag SEQ = new Tag("tag:yaml.org,2002:seq");
/*  45 */   public static final Tag MAP = new Tag("tag:yaml.org,2002:map");
/*     */ 
/*     */   
/*  48 */   protected static final Map<Tag, Set<Class<?>>> COMPATIBILITY_MAP = new HashMap<>(); static {
/*  49 */     Set<Class<?>> floatSet = new HashSet<>();
/*  50 */     floatSet.add(Double.class);
/*  51 */     floatSet.add(Float.class);
/*  52 */     floatSet.add(BigDecimal.class);
/*  53 */     COMPATIBILITY_MAP.put(FLOAT, floatSet);
/*     */     
/*  55 */     Set<Class<?>> intSet = new HashSet<>();
/*  56 */     intSet.add(Integer.class);
/*  57 */     intSet.add(Long.class);
/*  58 */     intSet.add(BigInteger.class);
/*  59 */     COMPATIBILITY_MAP.put(INT, intSet);
/*     */     
/*  61 */     Set<Class<?>> timestampSet = new HashSet<>();
/*  62 */     timestampSet.add(Date.class);
/*     */ 
/*     */     
/*     */     try {
/*  66 */       timestampSet.add(Class.forName("java.sql.Date"));
/*  67 */       timestampSet.add(Class.forName("java.sql.Timestamp"));
/*  68 */     } catch (ClassNotFoundException ignored) {}
/*     */ 
/*     */ 
/*     */     
/*  72 */     COMPATIBILITY_MAP.put(TIMESTAMP, timestampSet);
/*     */   }
/*     */   
/*     */   private final String value;
/*     */   private boolean secondary = false;
/*     */   
/*     */   public Tag(String tag) {
/*  79 */     if (tag == null)
/*  80 */       throw new NullPointerException("Tag must be provided."); 
/*  81 */     if (tag.length() == 0)
/*  82 */       throw new IllegalArgumentException("Tag must not be empty."); 
/*  83 */     if (tag.trim().length() != tag.length()) {
/*  84 */       throw new IllegalArgumentException("Tag must not contain leading or trailing spaces.");
/*     */     }
/*  86 */     this.value = UriEncoder.encode(tag);
/*  87 */     this.secondary = !tag.startsWith("tag:yaml.org,2002:");
/*     */   }
/*     */   
/*     */   public Tag(Class<? extends Object> clazz) {
/*  91 */     if (clazz == null) {
/*  92 */       throw new NullPointerException("Class for tag must be provided.");
/*     */     }
/*  94 */     this.value = "tag:yaml.org,2002:" + UriEncoder.encode(clazz.getName());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Tag(URI uri) {
/* 102 */     if (uri == null) {
/* 103 */       throw new NullPointerException("URI for tag must be provided.");
/*     */     }
/* 105 */     this.value = uri.toASCIIString();
/*     */   }
/*     */   
/*     */   public boolean isSecondary() {
/* 109 */     return this.secondary;
/*     */   }
/*     */   
/*     */   public String getValue() {
/* 113 */     return this.value;
/*     */   }
/*     */   
/*     */   public boolean startsWith(String prefix) {
/* 117 */     return this.value.startsWith(prefix);
/*     */   }
/*     */   
/*     */   public String getClassName() {
/* 121 */     if (!this.value.startsWith("tag:yaml.org,2002:")) {
/* 122 */       throw new YAMLException("Invalid tag: " + this.value);
/*     */     }
/* 124 */     return UriEncoder.decode(this.value.substring("tag:yaml.org,2002:".length()));
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 129 */     return this.value;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/* 134 */     if (obj instanceof Tag) {
/* 135 */       return this.value.equals(((Tag)obj).getValue());
/*     */     }
/* 137 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 142 */     return this.value.hashCode();
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
/*     */   public boolean isCompatible(Class<?> clazz) {
/* 155 */     Set<Class<?>> set = COMPATIBILITY_MAP.get(this);
/* 156 */     if (set != null) {
/* 157 */       return set.contains(clazz);
/*     */     }
/* 159 */     return false;
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
/*     */   public boolean matches(Class<? extends Object> clazz) {
/* 171 */     return this.value.equals("tag:yaml.org,2002:" + clazz.getName());
/*     */   }
/*     */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\yaml\snakeyaml\nodes\Tag.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */