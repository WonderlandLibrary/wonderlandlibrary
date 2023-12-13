/*     */ package org.yaml.snakeyaml.constructor;
/*     */ 
/*     */ import java.math.BigInteger;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Calendar;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.TimeZone;
/*     */ import java.util.TreeSet;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ import org.yaml.snakeyaml.LoaderOptions;
/*     */ import org.yaml.snakeyaml.error.YAMLException;
/*     */ import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;
/*     */ import org.yaml.snakeyaml.nodes.MappingNode;
/*     */ import org.yaml.snakeyaml.nodes.Node;
/*     */ import org.yaml.snakeyaml.nodes.NodeId;
/*     */ import org.yaml.snakeyaml.nodes.NodeTuple;
/*     */ import org.yaml.snakeyaml.nodes.ScalarNode;
/*     */ import org.yaml.snakeyaml.nodes.SequenceNode;
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
/*     */ public class SafeConstructor
/*     */   extends BaseConstructor
/*     */ {
/*  48 */   public static final ConstructUndefined undefinedConstructor = new ConstructUndefined();
/*     */   
/*     */   public SafeConstructor() {
/*  51 */     this(new LoaderOptions());
/*     */   }
/*     */   
/*     */   public SafeConstructor(LoaderOptions loadingConfig) {
/*  55 */     super(loadingConfig);
/*  56 */     this.yamlConstructors.put(Tag.NULL, new ConstructYamlNull());
/*  57 */     this.yamlConstructors.put(Tag.BOOL, new ConstructYamlBool());
/*  58 */     this.yamlConstructors.put(Tag.INT, new ConstructYamlInt());
/*  59 */     this.yamlConstructors.put(Tag.FLOAT, new ConstructYamlFloat());
/*  60 */     this.yamlConstructors.put(Tag.BINARY, new ConstructYamlBinary());
/*  61 */     this.yamlConstructors.put(Tag.TIMESTAMP, new ConstructYamlTimestamp());
/*  62 */     this.yamlConstructors.put(Tag.OMAP, new ConstructYamlOmap());
/*  63 */     this.yamlConstructors.put(Tag.PAIRS, new ConstructYamlPairs());
/*  64 */     this.yamlConstructors.put(Tag.SET, new ConstructYamlSet());
/*  65 */     this.yamlConstructors.put(Tag.STR, new ConstructYamlStr());
/*  66 */     this.yamlConstructors.put(Tag.SEQ, new ConstructYamlSeq());
/*  67 */     this.yamlConstructors.put(Tag.MAP, new ConstructYamlMap());
/*  68 */     this.yamlConstructors.put(null, undefinedConstructor);
/*  69 */     this.yamlClassConstructors.put(NodeId.scalar, undefinedConstructor);
/*  70 */     this.yamlClassConstructors.put(NodeId.sequence, undefinedConstructor);
/*  71 */     this.yamlClassConstructors.put(NodeId.mapping, undefinedConstructor);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void flattenMapping(MappingNode node) {
/*  76 */     processDuplicateKeys(node);
/*  77 */     if (node.isMerged()) {
/*  78 */       node.setValue(mergeNode(node, true, new HashMap<>(), new ArrayList<>()));
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected void processDuplicateKeys(MappingNode node) {
/*  84 */     List<NodeTuple> nodeValue = node.getValue();
/*  85 */     Map<Object, Integer> keys = new HashMap<>(nodeValue.size());
/*  86 */     TreeSet<Integer> toRemove = new TreeSet<>();
/*  87 */     int i = 0;
/*  88 */     for (NodeTuple tuple : nodeValue) {
/*  89 */       Node keyNode = tuple.getKeyNode();
/*  90 */       if (!keyNode.getTag().equals(Tag.MERGE)) {
/*  91 */         Object key = constructObject(keyNode);
/*  92 */         if (key != null) {
/*     */           try {
/*  94 */             key.hashCode();
/*  95 */           } catch (Exception e) {
/*  96 */             throw new ConstructorException("while constructing a mapping", node.getStartMark(), "found unacceptable key " + key, tuple.getKeyNode().getStartMark(), e);
/*     */           } 
/*     */         }
/*     */ 
/*     */ 
/*     */         
/* 102 */         Integer prevIndex = keys.put(key, Integer.valueOf(i));
/* 103 */         if (prevIndex != null) {
/* 104 */           if (!isAllowDuplicateKeys()) {
/* 105 */             throw new DuplicateKeyException(node.getStartMark(), key, tuple.getKeyNode().getStartMark());
/*     */           }
/*     */           
/* 108 */           toRemove.add(prevIndex);
/*     */         } 
/*     */       } 
/* 111 */       i++;
/*     */     } 
/*     */     
/* 114 */     Iterator<Integer> indices2remove = toRemove.descendingIterator();
/* 115 */     while (indices2remove.hasNext()) {
/* 116 */       nodeValue.remove(((Integer)indices2remove.next()).intValue());
/*     */     }
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private List<NodeTuple> mergeNode(MappingNode node, boolean isPreffered, Map<Object, Integer> key2index, List<NodeTuple> values) {
/* 136 */     Iterator<NodeTuple> iter = node.getValue().iterator();
/* 137 */     while (iter.hasNext()) {
/* 138 */       NodeTuple nodeTuple = iter.next();
/* 139 */       Node keyNode = nodeTuple.getKeyNode();
/* 140 */       Node valueNode = nodeTuple.getValueNode();
/* 141 */       if (keyNode.getTag().equals(Tag.MERGE)) {
/* 142 */         MappingNode mn; SequenceNode sn; List<Node> vals; iter.remove();
/* 143 */         switch (valueNode.getNodeId()) {
/*     */           case mapping:
/* 145 */             mn = (MappingNode)valueNode;
/* 146 */             mergeNode(mn, false, key2index, values);
/*     */             continue;
/*     */           case sequence:
/* 149 */             sn = (SequenceNode)valueNode;
/* 150 */             vals = sn.getValue();
/* 151 */             for (Node subnode : vals) {
/* 152 */               if (!(subnode instanceof MappingNode)) {
/* 153 */                 throw new ConstructorException("while constructing a mapping", node.getStartMark(), "expected a mapping for merging, but found " + subnode.getNodeId(), subnode.getStartMark());
/*     */               }
/*     */ 
/*     */ 
/*     */ 
/*     */               
/* 159 */               MappingNode mnode = (MappingNode)subnode;
/* 160 */               mergeNode(mnode, false, key2index, values);
/*     */             } 
/*     */             continue;
/*     */         } 
/* 164 */         throw new ConstructorException("while constructing a mapping", node.getStartMark(), "expected a mapping or list of mappings for merging, but found " + valueNode.getNodeId(), valueNode.getStartMark());
/*     */       } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 172 */       Object key = constructObject(keyNode);
/* 173 */       if (!key2index.containsKey(key)) {
/* 174 */         values.add(nodeTuple);
/*     */         
/* 176 */         key2index.put(key, Integer.valueOf(values.size() - 1)); continue;
/* 177 */       }  if (isPreffered)
/*     */       {
/*     */         
/* 180 */         values.set(((Integer)key2index.get(key)).intValue(), nodeTuple);
/*     */       }
/*     */     } 
/*     */     
/* 184 */     return values;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void constructMapping2ndStep(MappingNode node, Map<Object, Object> mapping) {
/* 189 */     flattenMapping(node);
/* 190 */     super.constructMapping2ndStep(node, mapping);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void constructSet2ndStep(MappingNode node, Set<Object> set) {
/* 195 */     flattenMapping(node);
/* 196 */     super.constructSet2ndStep(node, set);
/*     */   }
/*     */   
/*     */   public class ConstructYamlNull
/*     */     extends AbstractConstruct {
/*     */     public Object construct(Node node) {
/* 202 */       if (node != null) SafeConstructor.this.constructScalar((ScalarNode)node); 
/* 203 */       return null;
/*     */     }
/*     */   }
/*     */   
/* 207 */   private static final Map<String, Boolean> BOOL_VALUES = new HashMap<>();
/*     */   static {
/* 209 */     BOOL_VALUES.put("yes", Boolean.TRUE);
/* 210 */     BOOL_VALUES.put("no", Boolean.FALSE);
/* 211 */     BOOL_VALUES.put("true", Boolean.TRUE);
/* 212 */     BOOL_VALUES.put("false", Boolean.FALSE);
/* 213 */     BOOL_VALUES.put("on", Boolean.TRUE);
/* 214 */     BOOL_VALUES.put("off", Boolean.FALSE);
/*     */   }
/*     */   
/*     */   public class ConstructYamlBool
/*     */     extends AbstractConstruct {
/*     */     public Object construct(Node node) {
/* 220 */       String val = SafeConstructor.this.constructScalar((ScalarNode)node);
/* 221 */       return SafeConstructor.BOOL_VALUES.get(val.toLowerCase());
/*     */     }
/*     */   }
/*     */   
/*     */   public class ConstructYamlInt
/*     */     extends AbstractConstruct {
/*     */     public Object construct(Node node) {
/* 228 */       String value = SafeConstructor.this.constructScalar((ScalarNode)node).toString().replaceAll("_", "");
/* 229 */       int sign = 1;
/* 230 */       char first = value.charAt(0);
/* 231 */       if (first == '-') {
/* 232 */         sign = -1;
/* 233 */         value = value.substring(1);
/* 234 */       } else if (first == '+') {
/* 235 */         value = value.substring(1);
/*     */       } 
/* 237 */       int base = 10;
/* 238 */       if ("0".equals(value))
/* 239 */         return Integer.valueOf(0); 
/* 240 */       if (value.startsWith("0b"))
/* 241 */       { value = value.substring(2);
/* 242 */         base = 2; }
/* 243 */       else if (value.startsWith("0x"))
/* 244 */       { value = value.substring(2);
/* 245 */         base = 16; }
/* 246 */       else if (value.startsWith("0"))
/* 247 */       { value = value.substring(1);
/* 248 */         base = 8; }
/* 249 */       else { if (value.indexOf(':') != -1) {
/* 250 */           String[] digits = value.split(":");
/* 251 */           int bes = 1;
/* 252 */           int val = 0;
/* 253 */           for (int i = 0, j = digits.length; i < j; i++) {
/* 254 */             val = (int)(val + Long.parseLong(digits[j - i - 1]) * bes);
/* 255 */             bes *= 60;
/*     */           } 
/* 257 */           return SafeConstructor.this.createNumber(sign, String.valueOf(val), 10);
/*     */         } 
/* 259 */         return SafeConstructor.this.createNumber(sign, value, 10); }
/*     */       
/* 261 */       return SafeConstructor.this.createNumber(sign, value, base);
/*     */     }
/*     */   }
/*     */   
/* 265 */   private static final int[][] RADIX_MAX = new int[17][2];
/*     */   static {
/* 267 */     int[] radixList = { 2, 8, 10, 16 };
/* 268 */     for (int radix : radixList) {
/* 269 */       (new int[2])[0] = maxLen(2147483647, radix); (new int[2])[1] = maxLen(Long.MAX_VALUE, radix); RADIX_MAX[radix] = new int[2];
/*     */     } 
/*     */   }
/*     */   
/*     */   private static int maxLen(int max, int radix) {
/* 274 */     return Integer.toString(max, radix).length();
/*     */   }
/*     */   private static int maxLen(long max, int radix) {
/* 277 */     return Long.toString(max, radix).length();
/*     */   } private Number createNumber(int sign, String number, int radix) {
/*     */     Number number1;
/* 280 */     int len = (number != null) ? number.length() : 0;
/* 281 */     if (sign < 0) {
/* 282 */       number = "-" + number;
/*     */     }
/* 284 */     int[] maxArr = (radix < RADIX_MAX.length) ? RADIX_MAX[radix] : null;
/* 285 */     if (maxArr != null) {
/* 286 */       boolean gtInt = (len > maxArr[0]);
/* 287 */       if (gtInt) {
/* 288 */         if (len > maxArr[1]) {
/* 289 */           return new BigInteger(number, radix);
/*     */         }
/* 291 */         return createLongOrBigInteger(number, radix);
/*     */       } 
/*     */     } 
/*     */     
/*     */     try {
/* 296 */       number1 = Integer.valueOf(number, radix);
/* 297 */     } catch (NumberFormatException e) {
/* 298 */       number1 = createLongOrBigInteger(number, radix);
/*     */     } 
/* 300 */     return number1;
/*     */   }
/*     */   
/*     */   protected static Number createLongOrBigInteger(String number, int radix) {
/*     */     try {
/* 305 */       return Long.valueOf(number, radix);
/* 306 */     } catch (NumberFormatException e1) {
/* 307 */       return new BigInteger(number, radix);
/*     */     } 
/*     */   }
/*     */   
/*     */   public class ConstructYamlFloat
/*     */     extends AbstractConstruct {
/*     */     public Object construct(Node node) {
/* 314 */       String value = SafeConstructor.this.constructScalar((ScalarNode)node).toString().replaceAll("_", "");
/* 315 */       int sign = 1;
/* 316 */       char first = value.charAt(0);
/* 317 */       if (first == '-') {
/* 318 */         sign = -1;
/* 319 */         value = value.substring(1);
/* 320 */       } else if (first == '+') {
/* 321 */         value = value.substring(1);
/*     */       } 
/* 323 */       String valLower = value.toLowerCase();
/* 324 */       if (".inf".equals(valLower)) {
/* 325 */         return Double.valueOf((sign == -1) ? Double.NEGATIVE_INFINITY : Double.POSITIVE_INFINITY);
/*     */       }
/* 327 */       if (".nan".equals(valLower))
/* 328 */         return Double.valueOf(Double.NaN); 
/* 329 */       if (value.indexOf(':') != -1) {
/* 330 */         String[] digits = value.split(":");
/* 331 */         int bes = 1;
/* 332 */         double val = 0.0D;
/* 333 */         for (int i = 0, j = digits.length; i < j; i++) {
/* 334 */           val += Double.parseDouble(digits[j - i - 1]) * bes;
/* 335 */           bes *= 60;
/*     */         } 
/* 337 */         return Double.valueOf(sign * val);
/*     */       } 
/* 339 */       Double d = Double.valueOf(value);
/* 340 */       return Double.valueOf(d.doubleValue() * sign);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public class ConstructYamlBinary
/*     */     extends AbstractConstruct
/*     */   {
/*     */     public Object construct(Node node) {
/* 349 */       String noWhiteSpaces = SafeConstructor.this.constructScalar((ScalarNode)node).toString().replaceAll("\\s", "");
/*     */       
/* 351 */       byte[] decoded = Base64Coder.decode(noWhiteSpaces.toCharArray());
/* 352 */       return decoded;
/*     */     }
/*     */   }
/*     */   
/* 356 */   private static final Pattern TIMESTAMP_REGEXP = Pattern.compile("^([0-9][0-9][0-9][0-9])-([0-9][0-9]?)-([0-9][0-9]?)(?:(?:[Tt]|[ \t]+)([0-9][0-9]?):([0-9][0-9]):([0-9][0-9])(?:\\.([0-9]*))?(?:[ \t]*(?:Z|([-+][0-9][0-9]?)(?::([0-9][0-9])?)?))?)?$");
/*     */   
/* 358 */   private static final Pattern YMD_REGEXP = Pattern.compile("^([0-9][0-9][0-9][0-9])-([0-9][0-9]?)-([0-9][0-9]?)$");
/*     */   
/*     */   public static class ConstructYamlTimestamp
/*     */     extends AbstractConstruct {
/*     */     private Calendar calendar;
/*     */     
/*     */     public Calendar getCalendar() {
/* 365 */       return this.calendar;
/*     */     }
/*     */     
/*     */     public Object construct(Node node) {
/*     */       TimeZone timeZone;
/* 370 */       ScalarNode scalar = (ScalarNode)node;
/* 371 */       String nodeValue = scalar.getValue();
/* 372 */       Matcher match = SafeConstructor.YMD_REGEXP.matcher(nodeValue);
/* 373 */       if (match.matches()) {
/* 374 */         String str1 = match.group(1);
/* 375 */         String str2 = match.group(2);
/* 376 */         String str3 = match.group(3);
/* 377 */         this.calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
/* 378 */         this.calendar.clear();
/* 379 */         this.calendar.set(1, Integer.parseInt(str1));
/*     */         
/* 381 */         this.calendar.set(2, Integer.parseInt(str2) - 1);
/* 382 */         this.calendar.set(5, Integer.parseInt(str3));
/* 383 */         return this.calendar.getTime();
/*     */       } 
/* 385 */       match = SafeConstructor.TIMESTAMP_REGEXP.matcher(nodeValue);
/* 386 */       if (!match.matches()) {
/* 387 */         throw new YAMLException("Unexpected timestamp: " + nodeValue);
/*     */       }
/* 389 */       String year_s = match.group(1);
/* 390 */       String month_s = match.group(2);
/* 391 */       String day_s = match.group(3);
/* 392 */       String hour_s = match.group(4);
/* 393 */       String min_s = match.group(5);
/*     */       
/* 395 */       String seconds = match.group(6);
/* 396 */       String millis = match.group(7);
/* 397 */       if (millis != null) {
/* 398 */         seconds = seconds + "." + millis;
/*     */       }
/* 400 */       double fractions = Double.parseDouble(seconds);
/* 401 */       int sec_s = (int)Math.round(Math.floor(fractions));
/* 402 */       int usec = (int)Math.round((fractions - sec_s) * 1000.0D);
/*     */       
/* 404 */       String timezoneh_s = match.group(8);
/* 405 */       String timezonem_s = match.group(9);
/*     */       
/* 407 */       if (timezoneh_s != null) {
/* 408 */         String time = (timezonem_s != null) ? (":" + timezonem_s) : "00";
/* 409 */         timeZone = TimeZone.getTimeZone("GMT" + timezoneh_s + time);
/*     */       } else {
/*     */         
/* 412 */         timeZone = TimeZone.getTimeZone("UTC");
/*     */       } 
/* 414 */       this.calendar = Calendar.getInstance(timeZone);
/* 415 */       this.calendar.set(1, Integer.parseInt(year_s));
/*     */       
/* 417 */       this.calendar.set(2, Integer.parseInt(month_s) - 1);
/* 418 */       this.calendar.set(5, Integer.parseInt(day_s));
/* 419 */       this.calendar.set(11, Integer.parseInt(hour_s));
/* 420 */       this.calendar.set(12, Integer.parseInt(min_s));
/* 421 */       this.calendar.set(13, sec_s);
/* 422 */       this.calendar.set(14, usec);
/* 423 */       return this.calendar.getTime();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public class ConstructYamlOmap
/*     */     extends AbstractConstruct
/*     */   {
/*     */     public Object construct(Node node) {
/* 433 */       Map<Object, Object> omap = new LinkedHashMap<>();
/* 434 */       if (!(node instanceof SequenceNode)) {
/* 435 */         throw new ConstructorException("while constructing an ordered map", node.getStartMark(), "expected a sequence, but found " + node.getNodeId(), node.getStartMark());
/*     */       }
/*     */ 
/*     */       
/* 439 */       SequenceNode snode = (SequenceNode)node;
/* 440 */       for (Node subnode : snode.getValue()) {
/* 441 */         if (!(subnode instanceof MappingNode)) {
/* 442 */           throw new ConstructorException("while constructing an ordered map", node.getStartMark(), "expected a mapping of length 1, but found " + subnode.getNodeId(), subnode.getStartMark());
/*     */         }
/*     */ 
/*     */ 
/*     */         
/* 447 */         MappingNode mnode = (MappingNode)subnode;
/* 448 */         if (mnode.getValue().size() != 1) {
/* 449 */           throw new ConstructorException("while constructing an ordered map", node.getStartMark(), "expected a single mapping item, but found " + mnode.getValue().size() + " items", mnode.getStartMark());
/*     */         }
/*     */ 
/*     */ 
/*     */         
/* 454 */         Node keyNode = ((NodeTuple)mnode.getValue().get(0)).getKeyNode();
/* 455 */         Node valueNode = ((NodeTuple)mnode.getValue().get(0)).getValueNode();
/* 456 */         Object key = SafeConstructor.this.constructObject(keyNode);
/* 457 */         Object value = SafeConstructor.this.constructObject(valueNode);
/* 458 */         omap.put(key, value);
/*     */       } 
/* 460 */       return omap;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public class ConstructYamlPairs
/*     */     extends AbstractConstruct
/*     */   {
/*     */     public Object construct(Node node) {
/* 469 */       if (!(node instanceof SequenceNode)) {
/* 470 */         throw new ConstructorException("while constructing pairs", node.getStartMark(), "expected a sequence, but found " + node.getNodeId(), node.getStartMark());
/*     */       }
/*     */       
/* 473 */       SequenceNode snode = (SequenceNode)node;
/* 474 */       List<Object[]> pairs = new ArrayList(snode.getValue().size());
/* 475 */       for (Node subnode : snode.getValue()) {
/* 476 */         if (!(subnode instanceof MappingNode)) {
/* 477 */           throw new ConstructorException("while constructingpairs", node.getStartMark(), "expected a mapping of length 1, but found " + subnode.getNodeId(), subnode.getStartMark());
/*     */         }
/*     */ 
/*     */         
/* 481 */         MappingNode mnode = (MappingNode)subnode;
/* 482 */         if (mnode.getValue().size() != 1) {
/* 483 */           throw new ConstructorException("while constructing pairs", node.getStartMark(), "expected a single mapping item, but found " + mnode.getValue().size() + " items", mnode.getStartMark());
/*     */         }
/*     */ 
/*     */ 
/*     */         
/* 488 */         Node keyNode = ((NodeTuple)mnode.getValue().get(0)).getKeyNode();
/* 489 */         Node valueNode = ((NodeTuple)mnode.getValue().get(0)).getValueNode();
/* 490 */         Object key = SafeConstructor.this.constructObject(keyNode);
/* 491 */         Object value = SafeConstructor.this.constructObject(valueNode);
/* 492 */         pairs.add(new Object[] { key, value });
/*     */       } 
/* 494 */       return pairs;
/*     */     }
/*     */   }
/*     */   
/*     */   public class ConstructYamlSet
/*     */     implements Construct {
/*     */     public Object construct(Node node) {
/* 501 */       if (node.isTwoStepsConstruction()) {
/* 502 */         return SafeConstructor.this.constructedObjects.containsKey(node) ? SafeConstructor.this.constructedObjects.get(node) : SafeConstructor.this.createDefaultSet(((MappingNode)node).getValue().size());
/*     */       }
/*     */       
/* 505 */       return SafeConstructor.this.constructSet((MappingNode)node);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void construct2ndStep(Node node, Object object) {
/* 512 */       if (node.isTwoStepsConstruction()) {
/* 513 */         SafeConstructor.this.constructSet2ndStep((MappingNode)node, (Set<Object>)object);
/*     */       } else {
/* 515 */         throw new YAMLException("Unexpected recursive set structure. Node: " + node);
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   public class ConstructYamlStr
/*     */     extends AbstractConstruct {
/*     */     public Object construct(Node node) {
/* 523 */       return SafeConstructor.this.constructScalar((ScalarNode)node);
/*     */     }
/*     */   }
/*     */   
/*     */   public class ConstructYamlSeq
/*     */     implements Construct {
/*     */     public Object construct(Node node) {
/* 530 */       SequenceNode seqNode = (SequenceNode)node;
/* 531 */       if (node.isTwoStepsConstruction()) {
/* 532 */         return SafeConstructor.this.newList(seqNode);
/*     */       }
/* 534 */       return SafeConstructor.this.constructSequence(seqNode);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void construct2ndStep(Node node, Object data) {
/* 541 */       if (node.isTwoStepsConstruction()) {
/* 542 */         SafeConstructor.this.constructSequenceStep2((SequenceNode)node, (List)data);
/*     */       } else {
/* 544 */         throw new YAMLException("Unexpected recursive sequence structure. Node: " + node);
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   public class ConstructYamlMap
/*     */     implements Construct {
/*     */     public Object construct(Node node) {
/* 552 */       MappingNode mnode = (MappingNode)node;
/* 553 */       if (node.isTwoStepsConstruction()) {
/* 554 */         return SafeConstructor.this.createDefaultMap(mnode.getValue().size());
/*     */       }
/* 556 */       return SafeConstructor.this.constructMapping(mnode);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void construct2ndStep(Node node, Object object) {
/* 563 */       if (node.isTwoStepsConstruction()) {
/* 564 */         SafeConstructor.this.constructMapping2ndStep((MappingNode)node, (Map<Object, Object>)object);
/*     */       } else {
/* 566 */         throw new YAMLException("Unexpected recursive mapping structure. Node: " + node);
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   public static final class ConstructUndefined
/*     */     extends AbstractConstruct {
/*     */     public Object construct(Node node) {
/* 574 */       throw new ConstructorException(null, null, "could not determine a constructor for the tag " + node.getTag(), node.getStartMark());
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\yaml\snakeyaml\constructor\SafeConstructor.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */