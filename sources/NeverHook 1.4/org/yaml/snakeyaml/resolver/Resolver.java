/*     */ package org.yaml.snakeyaml.resolver;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.regex.Pattern;
/*     */ import org.yaml.snakeyaml.nodes.NodeId;
/*     */ import org.yaml.snakeyaml.nodes.Tag;
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
/*     */ public class Resolver
/*     */ {
/*  31 */   public static final Pattern BOOL = Pattern.compile("^(?:yes|Yes|YES|no|No|NO|true|True|TRUE|false|False|FALSE|on|On|ON|off|Off|OFF)$");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  38 */   public static final Pattern FLOAT = Pattern.compile("^([-+]?(\\.[0-9]+|[0-9_]+(\\.[0-9_]*)?)([eE][-+]?[0-9]+)?|[-+]?[0-9][0-9_]*(?::[0-5]?[0-9])+\\.[0-9_]*|[-+]?\\.(?:inf|Inf|INF)|\\.(?:nan|NaN|NAN))$");
/*     */   
/*  40 */   public static final Pattern INT = Pattern.compile("^(?:[-+]?0b[0-1_]+|[-+]?0[0-7_]+|[-+]?(?:0|[1-9][0-9_]*)|[-+]?0x[0-9a-fA-F_]+|[-+]?[1-9][0-9_]*(?::[0-5]?[0-9])+)$");
/*     */   
/*  42 */   public static final Pattern MERGE = Pattern.compile("^(?:<<)$");
/*  43 */   public static final Pattern NULL = Pattern.compile("^(?:~|null|Null|NULL| )$");
/*  44 */   public static final Pattern EMPTY = Pattern.compile("^$");
/*  45 */   public static final Pattern TIMESTAMP = Pattern.compile("^(?:[0-9][0-9][0-9][0-9]-[0-9][0-9]-[0-9][0-9]|[0-9][0-9][0-9][0-9]-[0-9][0-9]?-[0-9][0-9]?(?:[Tt]|[ \t]+)[0-9][0-9]?:[0-9][0-9]:[0-9][0-9](?:\\.[0-9]*)?(?:[ \t]*(?:Z|[-+][0-9][0-9]?(?::[0-9][0-9])?))?)$");
/*     */   
/*  47 */   public static final Pattern VALUE = Pattern.compile("^(?:=)$");
/*  48 */   public static final Pattern YAML = Pattern.compile("^(?:!|&|\\*)$");
/*     */   
/*  50 */   protected Map<Character, List<ResolverTuple>> yamlImplicitResolvers = new HashMap<>();
/*     */   
/*     */   protected void addImplicitResolvers() {
/*  53 */     addImplicitResolver(Tag.BOOL, BOOL, "yYnNtTfFoO");
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  59 */     addImplicitResolver(Tag.INT, INT, "-+0123456789");
/*  60 */     addImplicitResolver(Tag.FLOAT, FLOAT, "-+0123456789.");
/*  61 */     addImplicitResolver(Tag.MERGE, MERGE, "<");
/*  62 */     addImplicitResolver(Tag.NULL, NULL, "~nN\000");
/*  63 */     addImplicitResolver(Tag.NULL, EMPTY, null);
/*  64 */     addImplicitResolver(Tag.TIMESTAMP, TIMESTAMP, "0123456789");
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  69 */     addImplicitResolver(Tag.YAML, YAML, "!&*");
/*     */   }
/*     */   
/*     */   public Resolver() {
/*  73 */     addImplicitResolvers();
/*     */   }
/*     */   
/*     */   public void addImplicitResolver(Tag tag, Pattern regexp, String first) {
/*  77 */     if (first == null) {
/*  78 */       List<ResolverTuple> curr = this.yamlImplicitResolvers.get(null);
/*  79 */       if (curr == null) {
/*  80 */         curr = new ArrayList<>();
/*  81 */         this.yamlImplicitResolvers.put(null, curr);
/*     */       } 
/*  83 */       curr.add(new ResolverTuple(tag, regexp));
/*     */     } else {
/*  85 */       char[] chrs = first.toCharArray();
/*  86 */       for (int i = 0, j = chrs.length; i < j; i++) {
/*  87 */         Character theC = Character.valueOf(chrs[i]);
/*  88 */         if (theC.charValue() == '\000')
/*     */         {
/*  90 */           theC = null;
/*     */         }
/*  92 */         List<ResolverTuple> curr = this.yamlImplicitResolvers.get(theC);
/*  93 */         if (curr == null) {
/*  94 */           curr = new ArrayList<>();
/*  95 */           this.yamlImplicitResolvers.put(theC, curr);
/*     */         } 
/*  97 */         curr.add(new ResolverTuple(tag, regexp));
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public Tag resolve(NodeId kind, String value, boolean implicit) {
/* 103 */     if (kind == NodeId.scalar && implicit) {
/*     */       List<ResolverTuple> resolvers;
/* 105 */       if (value.length() == 0) {
/* 106 */         resolvers = this.yamlImplicitResolvers.get(Character.valueOf(false));
/*     */       } else {
/* 108 */         resolvers = this.yamlImplicitResolvers.get(Character.valueOf(value.charAt(0)));
/*     */       } 
/* 110 */       if (resolvers != null) {
/* 111 */         for (ResolverTuple v : resolvers) {
/* 112 */           Tag tag = v.getTag();
/* 113 */           Pattern regexp = v.getRegexp();
/* 114 */           if (regexp.matcher(value).matches()) {
/* 115 */             return tag;
/*     */           }
/*     */         } 
/*     */       }
/* 119 */       if (this.yamlImplicitResolvers.containsKey(null)) {
/* 120 */         for (ResolverTuple v : this.yamlImplicitResolvers.get(null)) {
/* 121 */           Tag tag = v.getTag();
/* 122 */           Pattern regexp = v.getRegexp();
/* 123 */           if (regexp.matcher(value).matches()) {
/* 124 */             return tag;
/*     */           }
/*     */         } 
/*     */       }
/*     */     } 
/* 129 */     switch (kind) {
/*     */       case scalar:
/* 131 */         return Tag.STR;
/*     */       case sequence:
/* 133 */         return Tag.SEQ;
/*     */     } 
/* 135 */     return Tag.MAP;
/*     */   }
/*     */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\yaml\snakeyaml\resolver\Resolver.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */