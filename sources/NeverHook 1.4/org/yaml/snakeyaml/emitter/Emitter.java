/*      */ package org.yaml.snakeyaml.emitter;
/*      */ 
/*      */ import java.io.IOException;
/*      */ import java.io.Writer;
/*      */ import java.util.HashMap;
/*      */ import java.util.HashSet;
/*      */ import java.util.Iterator;
/*      */ import java.util.LinkedHashMap;
/*      */ import java.util.Map;
/*      */ import java.util.Queue;
/*      */ import java.util.Set;
/*      */ import java.util.TreeSet;
/*      */ import java.util.concurrent.ArrayBlockingQueue;
/*      */ import java.util.regex.Matcher;
/*      */ import java.util.regex.Pattern;
/*      */ import org.yaml.snakeyaml.DumperOptions;
/*      */ import org.yaml.snakeyaml.error.YAMLException;
/*      */ import org.yaml.snakeyaml.events.CollectionStartEvent;
/*      */ import org.yaml.snakeyaml.events.DocumentEndEvent;
/*      */ import org.yaml.snakeyaml.events.DocumentStartEvent;
/*      */ import org.yaml.snakeyaml.events.Event;
/*      */ import org.yaml.snakeyaml.events.MappingStartEvent;
/*      */ import org.yaml.snakeyaml.events.NodeEvent;
/*      */ import org.yaml.snakeyaml.events.ScalarEvent;
/*      */ import org.yaml.snakeyaml.events.SequenceStartEvent;
/*      */ import org.yaml.snakeyaml.reader.StreamReader;
/*      */ import org.yaml.snakeyaml.scanner.Constant;
/*      */ import org.yaml.snakeyaml.util.ArrayStack;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public final class Emitter
/*      */   implements Emitable
/*      */ {
/*      */   public static final int MIN_INDENT = 1;
/*      */   public static final int MAX_INDENT = 10;
/*   67 */   private static final char[] SPACE = new char[] { ' ' };
/*      */   
/*   69 */   private static final Pattern SPACES_PATTERN = Pattern.compile("\\s");
/*   70 */   private static final Set<Character> INVALID_ANCHOR = new HashSet<>();
/*      */   static {
/*   72 */     INVALID_ANCHOR.add(Character.valueOf('['));
/*   73 */     INVALID_ANCHOR.add(Character.valueOf(']'));
/*   74 */     INVALID_ANCHOR.add(Character.valueOf('{'));
/*   75 */     INVALID_ANCHOR.add(Character.valueOf('}'));
/*   76 */     INVALID_ANCHOR.add(Character.valueOf(','));
/*   77 */     INVALID_ANCHOR.add(Character.valueOf('*'));
/*   78 */     INVALID_ANCHOR.add(Character.valueOf('&'));
/*      */   }
/*      */   
/*   81 */   private static final Map<Character, String> ESCAPE_REPLACEMENTS = new HashMap<>();
/*      */   static {
/*   83 */     ESCAPE_REPLACEMENTS.put(Character.valueOf(false), "0");
/*   84 */     ESCAPE_REPLACEMENTS.put(Character.valueOf('\007'), "a");
/*   85 */     ESCAPE_REPLACEMENTS.put(Character.valueOf('\b'), "b");
/*   86 */     ESCAPE_REPLACEMENTS.put(Character.valueOf('\t'), "t");
/*   87 */     ESCAPE_REPLACEMENTS.put(Character.valueOf('\n'), "n");
/*   88 */     ESCAPE_REPLACEMENTS.put(Character.valueOf('\013'), "v");
/*   89 */     ESCAPE_REPLACEMENTS.put(Character.valueOf('\f'), "f");
/*   90 */     ESCAPE_REPLACEMENTS.put(Character.valueOf('\r'), "r");
/*   91 */     ESCAPE_REPLACEMENTS.put(Character.valueOf('\033'), "e");
/*   92 */     ESCAPE_REPLACEMENTS.put(Character.valueOf('"'), "\"");
/*   93 */     ESCAPE_REPLACEMENTS.put(Character.valueOf('\\'), "\\");
/*   94 */     ESCAPE_REPLACEMENTS.put(Character.valueOf(''), "N");
/*   95 */     ESCAPE_REPLACEMENTS.put(Character.valueOf(' '), "_");
/*   96 */     ESCAPE_REPLACEMENTS.put(Character.valueOf(' '), "L");
/*   97 */     ESCAPE_REPLACEMENTS.put(Character.valueOf(' '), "P");
/*      */   }
/*      */   
/*  100 */   private static final Map<String, String> DEFAULT_TAG_PREFIXES = new LinkedHashMap<>();
/*      */   static {
/*  102 */     DEFAULT_TAG_PREFIXES.put("!", "!");
/*  103 */     DEFAULT_TAG_PREFIXES.put("tag:yaml.org,2002:", "!!");
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private final Writer stream;
/*      */ 
/*      */   
/*      */   private final ArrayStack<EmitterState> states;
/*      */   
/*      */   private EmitterState state;
/*      */   
/*      */   private final Queue<Event> events;
/*      */   
/*      */   private Event event;
/*      */   
/*      */   private final ArrayStack<Integer> indents;
/*      */   
/*      */   private Integer indent;
/*      */   
/*      */   private int flowLevel;
/*      */   
/*      */   private boolean rootContext;
/*      */   
/*      */   private boolean mappingContext;
/*      */   
/*      */   private boolean simpleKeyContext;
/*      */   
/*      */   private int column;
/*      */   
/*      */   private boolean whitespace;
/*      */   
/*      */   private boolean indention;
/*      */   
/*      */   private boolean openEnded;
/*      */   
/*      */   private final Boolean canonical;
/*      */   
/*      */   private final Boolean prettyFlow;
/*      */   
/*      */   private final boolean allowUnicode;
/*      */   
/*      */   private int bestIndent;
/*      */   
/*      */   private final int indicatorIndent;
/*      */   
/*      */   private final boolean indentWithIndicator;
/*      */   
/*      */   private int bestWidth;
/*      */   
/*      */   private final char[] bestLineBreak;
/*      */   
/*      */   private final boolean splitLines;
/*      */   
/*      */   private final int maxSimpleKeyLength;
/*      */   
/*      */   private Map<String, String> tagPrefixes;
/*      */   
/*      */   private String preparedAnchor;
/*      */   
/*      */   private String preparedTag;
/*      */   
/*      */   private ScalarAnalysis analysis;
/*      */   
/*      */   private DumperOptions.ScalarStyle style;
/*      */ 
/*      */   
/*      */   public Emitter(Writer stream, DumperOptions opts) {
/*  171 */     this.stream = stream;
/*      */ 
/*      */     
/*  174 */     this.states = new ArrayStack(100);
/*  175 */     this.state = new ExpectStreamStart();
/*      */     
/*  177 */     this.events = new ArrayBlockingQueue<>(100);
/*  178 */     this.event = null;
/*      */     
/*  180 */     this.indents = new ArrayStack(10);
/*  181 */     this.indent = null;
/*      */     
/*  183 */     this.flowLevel = 0;
/*      */     
/*  185 */     this.mappingContext = false;
/*  186 */     this.simpleKeyContext = false;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  194 */     this.column = 0;
/*  195 */     this.whitespace = true;
/*  196 */     this.indention = true;
/*      */ 
/*      */     
/*  199 */     this.openEnded = false;
/*      */ 
/*      */     
/*  202 */     this.canonical = Boolean.valueOf(opts.isCanonical());
/*  203 */     this.prettyFlow = Boolean.valueOf(opts.isPrettyFlow());
/*  204 */     this.allowUnicode = opts.isAllowUnicode();
/*  205 */     this.bestIndent = 2;
/*  206 */     if (opts.getIndent() > 1 && opts.getIndent() < 10) {
/*  207 */       this.bestIndent = opts.getIndent();
/*      */     }
/*  209 */     this.indicatorIndent = opts.getIndicatorIndent();
/*  210 */     this.indentWithIndicator = opts.getIndentWithIndicator();
/*  211 */     this.bestWidth = 80;
/*  212 */     if (opts.getWidth() > this.bestIndent * 2) {
/*  213 */       this.bestWidth = opts.getWidth();
/*      */     }
/*  215 */     this.bestLineBreak = opts.getLineBreak().getString().toCharArray();
/*  216 */     this.splitLines = opts.getSplitLines();
/*  217 */     this.maxSimpleKeyLength = opts.getMaxSimpleKeyLength();
/*      */ 
/*      */     
/*  220 */     this.tagPrefixes = new LinkedHashMap<>();
/*      */ 
/*      */     
/*  223 */     this.preparedAnchor = null;
/*  224 */     this.preparedTag = null;
/*      */ 
/*      */     
/*  227 */     this.analysis = null;
/*  228 */     this.style = null;
/*      */   }
/*      */   
/*      */   public void emit(Event event) throws IOException {
/*  232 */     this.events.add(event);
/*  233 */     while (!needMoreEvents()) {
/*  234 */       this.event = this.events.poll();
/*  235 */       this.state.expect();
/*  236 */       this.event = null;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean needMoreEvents() {
/*  243 */     if (this.events.isEmpty()) {
/*  244 */       return true;
/*      */     }
/*  246 */     Event event = this.events.peek();
/*  247 */     if (event instanceof DocumentStartEvent)
/*  248 */       return needEvents(1); 
/*  249 */     if (event instanceof SequenceStartEvent)
/*  250 */       return needEvents(2); 
/*  251 */     if (event instanceof MappingStartEvent) {
/*  252 */       return needEvents(3);
/*      */     }
/*  254 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   private boolean needEvents(int count) {
/*  259 */     int level = 0;
/*  260 */     Iterator<Event> iter = this.events.iterator();
/*  261 */     iter.next();
/*  262 */     while (iter.hasNext()) {
/*  263 */       Event event = iter.next();
/*  264 */       if (event instanceof DocumentStartEvent || event instanceof CollectionStartEvent) {
/*  265 */         level++;
/*  266 */       } else if (event instanceof DocumentEndEvent || event instanceof org.yaml.snakeyaml.events.CollectionEndEvent) {
/*  267 */         level--;
/*  268 */       } else if (event instanceof org.yaml.snakeyaml.events.StreamEndEvent) {
/*  269 */         level = -1;
/*      */       } 
/*  271 */       if (level < 0) {
/*  272 */         return false;
/*      */       }
/*      */     } 
/*  275 */     return (this.events.size() < count + 1);
/*      */   }
/*      */   
/*      */   private void increaseIndent(boolean flow, boolean indentless) {
/*  279 */     this.indents.push(this.indent);
/*  280 */     if (this.indent == null) {
/*  281 */       if (flow) {
/*  282 */         this.indent = Integer.valueOf(this.bestIndent);
/*      */       } else {
/*  284 */         this.indent = Integer.valueOf(0);
/*      */       } 
/*  286 */     } else if (!indentless) {
/*  287 */       Emitter emitter = this; emitter.indent = Integer.valueOf(emitter.indent.intValue() + this.bestIndent);
/*      */     } 
/*      */   }
/*      */   
/*      */   private class ExpectStreamStart
/*      */     implements EmitterState
/*      */   {
/*      */     private ExpectStreamStart() {}
/*      */     
/*      */     public void expect() throws IOException {
/*  297 */       if (Emitter.this.event instanceof org.yaml.snakeyaml.events.StreamStartEvent) {
/*  298 */         Emitter.this.writeStreamStart();
/*  299 */         Emitter.this.state = new Emitter.ExpectFirstDocumentStart();
/*      */       } else {
/*  301 */         throw new EmitterException("expected StreamStartEvent, but got " + Emitter.this.event);
/*      */       } 
/*      */     }
/*      */   }
/*      */   
/*      */   private class ExpectNothing implements EmitterState {
/*      */     public void expect() throws IOException {
/*  308 */       throw new EmitterException("expecting nothing, but got " + Emitter.this.event);
/*      */     }
/*      */     
/*      */     private ExpectNothing() {} }
/*      */   
/*      */   private class ExpectFirstDocumentStart implements EmitterState { private ExpectFirstDocumentStart() {}
/*      */     
/*      */     public void expect() throws IOException {
/*  316 */       (new Emitter.ExpectDocumentStart(true)).expect();
/*      */     } }
/*      */ 
/*      */   
/*      */   private class ExpectDocumentStart implements EmitterState {
/*      */     private final boolean first;
/*      */     
/*      */     public ExpectDocumentStart(boolean first) {
/*  324 */       this.first = first;
/*      */     }
/*      */     
/*      */     public void expect() throws IOException {
/*  328 */       if (Emitter.this.event instanceof DocumentStartEvent) {
/*  329 */         DocumentStartEvent ev = (DocumentStartEvent)Emitter.this.event;
/*  330 */         if ((ev.getVersion() != null || ev.getTags() != null) && Emitter.this.openEnded) {
/*  331 */           Emitter.this.writeIndicator("...", true, false, false);
/*  332 */           Emitter.this.writeIndent();
/*      */         } 
/*  334 */         if (ev.getVersion() != null) {
/*  335 */           String versionText = Emitter.this.prepareVersion(ev.getVersion());
/*  336 */           Emitter.this.writeVersionDirective(versionText);
/*      */         } 
/*  338 */         Emitter.this.tagPrefixes = (Map)new LinkedHashMap<>(Emitter.DEFAULT_TAG_PREFIXES);
/*  339 */         if (ev.getTags() != null) {
/*  340 */           Set<String> handles = new TreeSet<>(ev.getTags().keySet());
/*  341 */           for (String handle : handles) {
/*  342 */             String prefix = (String)ev.getTags().get(handle);
/*  343 */             Emitter.this.tagPrefixes.put(prefix, handle);
/*  344 */             String handleText = Emitter.this.prepareTagHandle(handle);
/*  345 */             String prefixText = Emitter.this.prepareTagPrefix(prefix);
/*  346 */             Emitter.this.writeTagDirective(handleText, prefixText);
/*      */           } 
/*      */         } 
/*  349 */         boolean implicit = (this.first && !ev.getExplicit() && !Emitter.this.canonical.booleanValue() && ev.getVersion() == null && (ev.getTags() == null || ev.getTags().isEmpty()) && !Emitter.this.checkEmptyDocument());
/*      */ 
/*      */ 
/*      */         
/*  353 */         if (!implicit) {
/*  354 */           Emitter.this.writeIndent();
/*  355 */           Emitter.this.writeIndicator("---", true, false, false);
/*  356 */           if (Emitter.this.canonical.booleanValue()) {
/*  357 */             Emitter.this.writeIndent();
/*      */           }
/*      */         } 
/*  360 */         Emitter.this.state = new Emitter.ExpectDocumentRoot();
/*  361 */       } else if (Emitter.this.event instanceof org.yaml.snakeyaml.events.StreamEndEvent) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  367 */         Emitter.this.writeStreamEnd();
/*  368 */         Emitter.this.state = new Emitter.ExpectNothing();
/*      */       } else {
/*  370 */         throw new EmitterException("expected DocumentStartEvent, but got " + Emitter.this.event);
/*      */       } 
/*      */     } }
/*      */   
/*      */   private class ExpectDocumentEnd implements EmitterState { private ExpectDocumentEnd() {}
/*      */     
/*      */     public void expect() throws IOException {
/*  377 */       if (Emitter.this.event instanceof DocumentEndEvent) {
/*  378 */         Emitter.this.writeIndent();
/*  379 */         if (((DocumentEndEvent)Emitter.this.event).getExplicit()) {
/*  380 */           Emitter.this.writeIndicator("...", true, false, false);
/*  381 */           Emitter.this.writeIndent();
/*      */         } 
/*  383 */         Emitter.this.flushStream();
/*  384 */         Emitter.this.state = new Emitter.ExpectDocumentStart(false);
/*      */       } else {
/*  386 */         throw new EmitterException("expected DocumentEndEvent, but got " + Emitter.this.event);
/*      */       } 
/*      */     } }
/*      */   
/*      */   private class ExpectDocumentRoot implements EmitterState { private ExpectDocumentRoot() {}
/*      */     
/*      */     public void expect() throws IOException {
/*  393 */       Emitter.this.states.push(new Emitter.ExpectDocumentEnd());
/*  394 */       Emitter.this.expectNode(true, false, false);
/*      */     } }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void expectNode(boolean root, boolean mapping, boolean simpleKey) throws IOException {
/*  401 */     this.rootContext = root;
/*  402 */     this.mappingContext = mapping;
/*  403 */     this.simpleKeyContext = simpleKey;
/*  404 */     if (this.event instanceof org.yaml.snakeyaml.events.AliasEvent) {
/*  405 */       expectAlias();
/*  406 */     } else if (this.event instanceof ScalarEvent || this.event instanceof CollectionStartEvent) {
/*  407 */       processAnchor("&");
/*  408 */       processTag();
/*  409 */       if (this.event instanceof ScalarEvent) {
/*  410 */         expectScalar();
/*  411 */       } else if (this.event instanceof SequenceStartEvent) {
/*  412 */         if (this.flowLevel != 0 || this.canonical.booleanValue() || ((SequenceStartEvent)this.event).isFlow() || checkEmptySequence()) {
/*      */           
/*  414 */           expectFlowSequence();
/*      */         } else {
/*  416 */           expectBlockSequence();
/*      */         }
/*      */       
/*  419 */       } else if (this.flowLevel != 0 || this.canonical.booleanValue() || ((MappingStartEvent)this.event).isFlow() || checkEmptyMapping()) {
/*      */         
/*  421 */         expectFlowMapping();
/*      */       } else {
/*  423 */         expectBlockMapping();
/*      */       } 
/*      */     } else {
/*      */       
/*  427 */       throw new EmitterException("expected NodeEvent, but got " + this.event);
/*      */     } 
/*      */   }
/*      */   
/*      */   private void expectAlias() throws IOException {
/*  432 */     if (!(this.event instanceof org.yaml.snakeyaml.events.AliasEvent)) {
/*  433 */       throw new EmitterException("Alias must be provided");
/*      */     }
/*  435 */     processAnchor("*");
/*  436 */     this.state = (EmitterState)this.states.pop();
/*      */   }
/*      */   
/*      */   private void expectScalar() throws IOException {
/*  440 */     increaseIndent(true, false);
/*  441 */     processScalar();
/*  442 */     this.indent = (Integer)this.indents.pop();
/*  443 */     this.state = (EmitterState)this.states.pop();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private void expectFlowSequence() throws IOException {
/*  449 */     writeIndicator("[", true, true, false);
/*  450 */     this.flowLevel++;
/*  451 */     increaseIndent(true, false);
/*  452 */     if (this.prettyFlow.booleanValue()) {
/*  453 */       writeIndent();
/*      */     }
/*  455 */     this.state = new ExpectFirstFlowSequenceItem();
/*      */   }
/*      */   private class ExpectFirstFlowSequenceItem implements EmitterState { private ExpectFirstFlowSequenceItem() {}
/*      */     
/*      */     public void expect() throws IOException {
/*  460 */       if (Emitter.this.event instanceof org.yaml.snakeyaml.events.SequenceEndEvent) {
/*  461 */         Emitter.this.indent = (Integer)Emitter.this.indents.pop();
/*  462 */         Emitter.this.flowLevel--;
/*  463 */         Emitter.this.writeIndicator("]", false, false, false);
/*  464 */         Emitter.this.state = (EmitterState)Emitter.this.states.pop();
/*      */       } else {
/*  466 */         if (Emitter.this.canonical.booleanValue() || (Emitter.this.column > Emitter.this.bestWidth && Emitter.this.splitLines) || Emitter.this.prettyFlow.booleanValue()) {
/*  467 */           Emitter.this.writeIndent();
/*      */         }
/*  469 */         Emitter.this.states.push(new Emitter.ExpectFlowSequenceItem());
/*  470 */         Emitter.this.expectNode(false, false, false);
/*      */       } 
/*      */     } }
/*      */   
/*      */   private class ExpectFlowSequenceItem implements EmitterState { private ExpectFlowSequenceItem() {}
/*      */     
/*      */     public void expect() throws IOException {
/*  477 */       if (Emitter.this.event instanceof org.yaml.snakeyaml.events.SequenceEndEvent) {
/*  478 */         Emitter.this.indent = (Integer)Emitter.this.indents.pop();
/*  479 */         Emitter.this.flowLevel--;
/*  480 */         if (Emitter.this.canonical.booleanValue()) {
/*  481 */           Emitter.this.writeIndicator(",", false, false, false);
/*  482 */           Emitter.this.writeIndent();
/*      */         } 
/*  484 */         Emitter.this.writeIndicator("]", false, false, false);
/*  485 */         if (Emitter.this.prettyFlow.booleanValue()) {
/*  486 */           Emitter.this.writeIndent();
/*      */         }
/*  488 */         Emitter.this.state = (EmitterState)Emitter.this.states.pop();
/*      */       } else {
/*  490 */         Emitter.this.writeIndicator(",", false, false, false);
/*  491 */         if (Emitter.this.canonical.booleanValue() || (Emitter.this.column > Emitter.this.bestWidth && Emitter.this.splitLines) || Emitter.this.prettyFlow.booleanValue()) {
/*  492 */           Emitter.this.writeIndent();
/*      */         }
/*  494 */         Emitter.this.states.push(new ExpectFlowSequenceItem());
/*  495 */         Emitter.this.expectNode(false, false, false);
/*      */       } 
/*      */     } }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void expectFlowMapping() throws IOException {
/*  503 */     writeIndicator("{", true, true, false);
/*  504 */     this.flowLevel++;
/*  505 */     increaseIndent(true, false);
/*  506 */     if (this.prettyFlow.booleanValue()) {
/*  507 */       writeIndent();
/*      */     }
/*  509 */     this.state = new ExpectFirstFlowMappingKey();
/*      */   }
/*      */   private class ExpectFirstFlowMappingKey implements EmitterState { private ExpectFirstFlowMappingKey() {}
/*      */     
/*      */     public void expect() throws IOException {
/*  514 */       if (Emitter.this.event instanceof org.yaml.snakeyaml.events.MappingEndEvent) {
/*  515 */         Emitter.this.indent = (Integer)Emitter.this.indents.pop();
/*  516 */         Emitter.this.flowLevel--;
/*  517 */         Emitter.this.writeIndicator("}", false, false, false);
/*  518 */         Emitter.this.state = (EmitterState)Emitter.this.states.pop();
/*      */       } else {
/*  520 */         if (Emitter.this.canonical.booleanValue() || (Emitter.this.column > Emitter.this.bestWidth && Emitter.this.splitLines) || Emitter.this.prettyFlow.booleanValue()) {
/*  521 */           Emitter.this.writeIndent();
/*      */         }
/*  523 */         if (!Emitter.this.canonical.booleanValue() && Emitter.this.checkSimpleKey()) {
/*  524 */           Emitter.this.states.push(new Emitter.ExpectFlowMappingSimpleValue());
/*  525 */           Emitter.this.expectNode(false, true, true);
/*      */         } else {
/*  527 */           Emitter.this.writeIndicator("?", true, false, false);
/*  528 */           Emitter.this.states.push(new Emitter.ExpectFlowMappingValue());
/*  529 */           Emitter.this.expectNode(false, true, false);
/*      */         } 
/*      */       } 
/*      */     } }
/*      */   
/*      */   private class ExpectFlowMappingKey implements EmitterState { private ExpectFlowMappingKey() {}
/*      */     
/*      */     public void expect() throws IOException {
/*  537 */       if (Emitter.this.event instanceof org.yaml.snakeyaml.events.MappingEndEvent) {
/*  538 */         Emitter.this.indent = (Integer)Emitter.this.indents.pop();
/*  539 */         Emitter.this.flowLevel--;
/*  540 */         if (Emitter.this.canonical.booleanValue()) {
/*  541 */           Emitter.this.writeIndicator(",", false, false, false);
/*  542 */           Emitter.this.writeIndent();
/*      */         } 
/*  544 */         if (Emitter.this.prettyFlow.booleanValue()) {
/*  545 */           Emitter.this.writeIndent();
/*      */         }
/*  547 */         Emitter.this.writeIndicator("}", false, false, false);
/*  548 */         Emitter.this.state = (EmitterState)Emitter.this.states.pop();
/*      */       } else {
/*  550 */         Emitter.this.writeIndicator(",", false, false, false);
/*  551 */         if (Emitter.this.canonical.booleanValue() || (Emitter.this.column > Emitter.this.bestWidth && Emitter.this.splitLines) || Emitter.this.prettyFlow.booleanValue()) {
/*  552 */           Emitter.this.writeIndent();
/*      */         }
/*  554 */         if (!Emitter.this.canonical.booleanValue() && Emitter.this.checkSimpleKey()) {
/*  555 */           Emitter.this.states.push(new Emitter.ExpectFlowMappingSimpleValue());
/*  556 */           Emitter.this.expectNode(false, true, true);
/*      */         } else {
/*  558 */           Emitter.this.writeIndicator("?", true, false, false);
/*  559 */           Emitter.this.states.push(new Emitter.ExpectFlowMappingValue());
/*  560 */           Emitter.this.expectNode(false, true, false);
/*      */         } 
/*      */       } 
/*      */     } }
/*      */   
/*      */   private class ExpectFlowMappingSimpleValue implements EmitterState { private ExpectFlowMappingSimpleValue() {}
/*      */     
/*      */     public void expect() throws IOException {
/*  568 */       Emitter.this.writeIndicator(":", false, false, false);
/*  569 */       Emitter.this.states.push(new Emitter.ExpectFlowMappingKey());
/*  570 */       Emitter.this.expectNode(false, true, false);
/*      */     } }
/*      */   
/*      */   private class ExpectFlowMappingValue implements EmitterState { private ExpectFlowMappingValue() {}
/*      */     
/*      */     public void expect() throws IOException {
/*  576 */       if (Emitter.this.canonical.booleanValue() || Emitter.this.column > Emitter.this.bestWidth || Emitter.this.prettyFlow.booleanValue()) {
/*  577 */         Emitter.this.writeIndent();
/*      */       }
/*  579 */       Emitter.this.writeIndicator(":", true, false, false);
/*  580 */       Emitter.this.states.push(new Emitter.ExpectFlowMappingKey());
/*  581 */       Emitter.this.expectNode(false, true, false);
/*      */     } }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void expectBlockSequence() throws IOException {
/*  588 */     boolean indentless = (this.mappingContext && !this.indention);
/*  589 */     increaseIndent(false, indentless);
/*  590 */     this.state = new ExpectFirstBlockSequenceItem();
/*      */   }
/*      */   private class ExpectFirstBlockSequenceItem implements EmitterState { private ExpectFirstBlockSequenceItem() {}
/*      */     
/*      */     public void expect() throws IOException {
/*  595 */       (new Emitter.ExpectBlockSequenceItem(true)).expect();
/*      */     } }
/*      */ 
/*      */   
/*      */   private class ExpectBlockSequenceItem implements EmitterState {
/*      */     private final boolean first;
/*      */     
/*      */     public ExpectBlockSequenceItem(boolean first) {
/*  603 */       this.first = first;
/*      */     }
/*      */     
/*      */     public void expect() throws IOException {
/*  607 */       if (!this.first && Emitter.this.event instanceof org.yaml.snakeyaml.events.SequenceEndEvent) {
/*  608 */         Emitter.this.indent = (Integer)Emitter.this.indents.pop();
/*  609 */         Emitter.this.state = (EmitterState)Emitter.this.states.pop();
/*      */       } else {
/*  611 */         Emitter.this.writeIndent();
/*  612 */         if (!Emitter.this.indentWithIndicator || this.first) {
/*  613 */           Emitter.this.writeWhitespace(Emitter.this.indicatorIndent);
/*      */         }
/*  615 */         Emitter.this.writeIndicator("-", true, false, true);
/*  616 */         if (Emitter.this.indentWithIndicator && this.first) {
/*  617 */           Emitter.this.indent = Integer.valueOf(Emitter.this.indent.intValue() + Emitter.this.indicatorIndent);
/*      */         }
/*  619 */         Emitter.this.states.push(new ExpectBlockSequenceItem(false));
/*  620 */         Emitter.this.expectNode(false, false, false);
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   private void expectBlockMapping() throws IOException {
/*  627 */     increaseIndent(false, false);
/*  628 */     this.state = new ExpectFirstBlockMappingKey();
/*      */   }
/*      */   private class ExpectFirstBlockMappingKey implements EmitterState { private ExpectFirstBlockMappingKey() {}
/*      */     
/*      */     public void expect() throws IOException {
/*  633 */       (new Emitter.ExpectBlockMappingKey(true)).expect();
/*      */     } }
/*      */ 
/*      */   
/*      */   private class ExpectBlockMappingKey implements EmitterState {
/*      */     private final boolean first;
/*      */     
/*      */     public ExpectBlockMappingKey(boolean first) {
/*  641 */       this.first = first;
/*      */     }
/*      */     
/*      */     public void expect() throws IOException {
/*  645 */       if (!this.first && Emitter.this.event instanceof org.yaml.snakeyaml.events.MappingEndEvent) {
/*  646 */         Emitter.this.indent = (Integer)Emitter.this.indents.pop();
/*  647 */         Emitter.this.state = (EmitterState)Emitter.this.states.pop();
/*      */       } else {
/*  649 */         Emitter.this.writeIndent();
/*  650 */         if (Emitter.this.checkSimpleKey()) {
/*  651 */           Emitter.this.states.push(new Emitter.ExpectBlockMappingSimpleValue());
/*  652 */           Emitter.this.expectNode(false, true, true);
/*      */         } else {
/*  654 */           Emitter.this.writeIndicator("?", true, false, true);
/*  655 */           Emitter.this.states.push(new Emitter.ExpectBlockMappingValue());
/*  656 */           Emitter.this.expectNode(false, true, false);
/*      */         } 
/*      */       } 
/*      */     } }
/*      */   
/*      */   private class ExpectBlockMappingSimpleValue implements EmitterState { private ExpectBlockMappingSimpleValue() {}
/*      */     
/*      */     public void expect() throws IOException {
/*  664 */       Emitter.this.writeIndicator(":", false, false, false);
/*  665 */       Emitter.this.states.push(new Emitter.ExpectBlockMappingKey(false));
/*  666 */       Emitter.this.expectNode(false, true, false);
/*      */     } }
/*      */   
/*      */   private class ExpectBlockMappingValue implements EmitterState { private ExpectBlockMappingValue() {}
/*      */     
/*      */     public void expect() throws IOException {
/*  672 */       Emitter.this.writeIndent();
/*  673 */       Emitter.this.writeIndicator(":", true, false, true);
/*  674 */       Emitter.this.states.push(new Emitter.ExpectBlockMappingKey(false));
/*  675 */       Emitter.this.expectNode(false, true, false);
/*      */     } }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean checkEmptySequence() {
/*  682 */     return (this.event instanceof SequenceStartEvent && !this.events.isEmpty() && this.events.peek() instanceof org.yaml.snakeyaml.events.SequenceEndEvent);
/*      */   }
/*      */   
/*      */   private boolean checkEmptyMapping() {
/*  686 */     return (this.event instanceof MappingStartEvent && !this.events.isEmpty() && this.events.peek() instanceof org.yaml.snakeyaml.events.MappingEndEvent);
/*      */   }
/*      */   
/*      */   private boolean checkEmptyDocument() {
/*  690 */     if (!(this.event instanceof DocumentStartEvent) || this.events.isEmpty()) {
/*  691 */       return false;
/*      */     }
/*  693 */     Event event = this.events.peek();
/*  694 */     if (event instanceof ScalarEvent) {
/*  695 */       ScalarEvent e = (ScalarEvent)event;
/*  696 */       return (e.getAnchor() == null && e.getTag() == null && e.getImplicit() != null && e.getValue().length() == 0);
/*      */     } 
/*      */     
/*  699 */     return false;
/*      */   }
/*      */   
/*      */   private boolean checkSimpleKey() {
/*  703 */     int length = 0;
/*  704 */     if (this.event instanceof NodeEvent && ((NodeEvent)this.event).getAnchor() != null) {
/*  705 */       if (this.preparedAnchor == null) {
/*  706 */         this.preparedAnchor = prepareAnchor(((NodeEvent)this.event).getAnchor());
/*      */       }
/*  708 */       length += this.preparedAnchor.length();
/*      */     } 
/*  710 */     String tag = null;
/*  711 */     if (this.event instanceof ScalarEvent) {
/*  712 */       tag = ((ScalarEvent)this.event).getTag();
/*  713 */     } else if (this.event instanceof CollectionStartEvent) {
/*  714 */       tag = ((CollectionStartEvent)this.event).getTag();
/*      */     } 
/*  716 */     if (tag != null) {
/*  717 */       if (this.preparedTag == null) {
/*  718 */         this.preparedTag = prepareTag(tag);
/*      */       }
/*  720 */       length += this.preparedTag.length();
/*      */     } 
/*  722 */     if (this.event instanceof ScalarEvent) {
/*  723 */       if (this.analysis == null) {
/*  724 */         this.analysis = analyzeScalar(((ScalarEvent)this.event).getValue());
/*      */       }
/*  726 */       length += this.analysis.getScalar().length();
/*      */     } 
/*  728 */     return (length < this.maxSimpleKeyLength && (this.event instanceof org.yaml.snakeyaml.events.AliasEvent || (this.event instanceof ScalarEvent && !this.analysis.isEmpty() && !this.analysis.isMultiline()) || checkEmptySequence() || checkEmptyMapping()));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void processAnchor(String indicator) throws IOException {
/*  736 */     NodeEvent ev = (NodeEvent)this.event;
/*  737 */     if (ev.getAnchor() == null) {
/*  738 */       this.preparedAnchor = null;
/*      */       return;
/*      */     } 
/*  741 */     if (this.preparedAnchor == null) {
/*  742 */       this.preparedAnchor = prepareAnchor(ev.getAnchor());
/*      */     }
/*  744 */     writeIndicator(indicator + this.preparedAnchor, true, false, false);
/*  745 */     this.preparedAnchor = null;
/*      */   }
/*      */   
/*      */   private void processTag() throws IOException {
/*  749 */     String tag = null;
/*  750 */     if (this.event instanceof ScalarEvent) {
/*  751 */       ScalarEvent ev = (ScalarEvent)this.event;
/*  752 */       tag = ev.getTag();
/*  753 */       if (this.style == null) {
/*  754 */         this.style = chooseScalarStyle();
/*      */       }
/*  756 */       if ((!this.canonical.booleanValue() || tag == null) && ((this.style == null && ev.getImplicit().canOmitTagInPlainScalar()) || (this.style != null && ev.getImplicit().canOmitTagInNonPlainScalar()))) {
/*      */ 
/*      */         
/*  759 */         this.preparedTag = null;
/*      */         return;
/*      */       } 
/*  762 */       if (ev.getImplicit().canOmitTagInPlainScalar() && tag == null) {
/*  763 */         tag = "!";
/*  764 */         this.preparedTag = null;
/*      */       } 
/*      */     } else {
/*  767 */       CollectionStartEvent ev = (CollectionStartEvent)this.event;
/*  768 */       tag = ev.getTag();
/*  769 */       if ((!this.canonical.booleanValue() || tag == null) && ev.getImplicit()) {
/*  770 */         this.preparedTag = null;
/*      */         return;
/*      */       } 
/*      */     } 
/*  774 */     if (tag == null) {
/*  775 */       throw new EmitterException("tag is not specified");
/*      */     }
/*  777 */     if (this.preparedTag == null) {
/*  778 */       this.preparedTag = prepareTag(tag);
/*      */     }
/*  780 */     writeIndicator(this.preparedTag, true, false, false);
/*  781 */     this.preparedTag = null;
/*      */   }
/*      */   
/*      */   private DumperOptions.ScalarStyle chooseScalarStyle() {
/*  785 */     ScalarEvent ev = (ScalarEvent)this.event;
/*  786 */     if (this.analysis == null) {
/*  787 */       this.analysis = analyzeScalar(ev.getValue());
/*      */     }
/*  789 */     if ((!ev.isPlain() && ev.getScalarStyle() == DumperOptions.ScalarStyle.DOUBLE_QUOTED) || this.canonical.booleanValue()) {
/*  790 */       return DumperOptions.ScalarStyle.DOUBLE_QUOTED;
/*      */     }
/*  792 */     if (ev.isPlain() && ev.getImplicit().canOmitTagInPlainScalar() && (
/*  793 */       !this.simpleKeyContext || (!this.analysis.isEmpty() && !this.analysis.isMultiline())) && ((this.flowLevel != 0 && this.analysis.isAllowFlowPlain()) || (this.flowLevel == 0 && this.analysis.isAllowBlockPlain())))
/*      */     {
/*  795 */       return null;
/*      */     }
/*      */     
/*  798 */     if (!ev.isPlain() && (ev.getScalarStyle() == DumperOptions.ScalarStyle.LITERAL || ev.getScalarStyle() == DumperOptions.ScalarStyle.FOLDED) && 
/*  799 */       this.flowLevel == 0 && !this.simpleKeyContext && this.analysis.isAllowBlock()) {
/*  800 */       return ev.getScalarStyle();
/*      */     }
/*      */     
/*  803 */     if ((ev.isPlain() || ev.getScalarStyle() == DumperOptions.ScalarStyle.SINGLE_QUOTED) && 
/*  804 */       this.analysis.isAllowSingleQuoted() && (!this.simpleKeyContext || !this.analysis.isMultiline())) {
/*  805 */       return DumperOptions.ScalarStyle.SINGLE_QUOTED;
/*      */     }
/*      */     
/*  808 */     return DumperOptions.ScalarStyle.DOUBLE_QUOTED;
/*      */   }
/*      */   
/*      */   private void processScalar() throws IOException {
/*  812 */     ScalarEvent ev = (ScalarEvent)this.event;
/*  813 */     if (this.analysis == null) {
/*  814 */       this.analysis = analyzeScalar(ev.getValue());
/*      */     }
/*  816 */     if (this.style == null) {
/*  817 */       this.style = chooseScalarStyle();
/*      */     }
/*  819 */     boolean split = (!this.simpleKeyContext && this.splitLines);
/*  820 */     if (this.style == null) {
/*  821 */       writePlain(this.analysis.getScalar(), split);
/*      */     } else {
/*  823 */       switch (this.style) {
/*      */         case DOUBLE_QUOTED:
/*  825 */           writeDoubleQuoted(this.analysis.getScalar(), split);
/*      */           break;
/*      */         case SINGLE_QUOTED:
/*  828 */           writeSingleQuoted(this.analysis.getScalar(), split);
/*      */           break;
/*      */         case FOLDED:
/*  831 */           writeFolded(this.analysis.getScalar(), split);
/*      */           break;
/*      */         case LITERAL:
/*  834 */           writeLiteral(this.analysis.getScalar());
/*      */           break;
/*      */         default:
/*  837 */           throw new YAMLException("Unexpected style: " + this.style);
/*      */       } 
/*      */     } 
/*  840 */     this.analysis = null;
/*  841 */     this.style = null;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private String prepareVersion(DumperOptions.Version version) {
/*  847 */     if (version.major() != 1) {
/*  848 */       throw new EmitterException("unsupported YAML version: " + version);
/*      */     }
/*  850 */     return version.getRepresentation();
/*      */   }
/*      */   
/*  853 */   private static final Pattern HANDLE_FORMAT = Pattern.compile("^![-_\\w]*!$");
/*      */   
/*      */   private String prepareTagHandle(String handle) {
/*  856 */     if (handle.length() == 0)
/*  857 */       throw new EmitterException("tag handle must not be empty"); 
/*  858 */     if (handle.charAt(0) != '!' || handle.charAt(handle.length() - 1) != '!')
/*  859 */       throw new EmitterException("tag handle must start and end with '!': " + handle); 
/*  860 */     if (!"!".equals(handle) && !HANDLE_FORMAT.matcher(handle).matches()) {
/*  861 */       throw new EmitterException("invalid character in the tag handle: " + handle);
/*      */     }
/*  863 */     return handle;
/*      */   }
/*      */   
/*      */   private String prepareTagPrefix(String prefix) {
/*  867 */     if (prefix.length() == 0) {
/*  868 */       throw new EmitterException("tag prefix must not be empty");
/*      */     }
/*  870 */     StringBuilder chunks = new StringBuilder();
/*  871 */     int start = 0;
/*  872 */     int end = 0;
/*  873 */     if (prefix.charAt(0) == '!') {
/*  874 */       end = 1;
/*      */     }
/*  876 */     while (end < prefix.length()) {
/*  877 */       end++;
/*      */     }
/*  879 */     if (start < end) {
/*  880 */       chunks.append(prefix, start, end);
/*      */     }
/*  882 */     return chunks.toString();
/*      */   }
/*      */   
/*      */   private String prepareTag(String tag) {
/*  886 */     if (tag.length() == 0) {
/*  887 */       throw new EmitterException("tag must not be empty");
/*      */     }
/*  889 */     if ("!".equals(tag)) {
/*  890 */       return tag;
/*      */     }
/*  892 */     String handle = null;
/*  893 */     String suffix = tag;
/*      */     
/*  895 */     for (String prefix : this.tagPrefixes.keySet()) {
/*  896 */       if (tag.startsWith(prefix) && ("!".equals(prefix) || prefix.length() < tag.length())) {
/*  897 */         handle = prefix;
/*      */       }
/*      */     } 
/*  900 */     if (handle != null) {
/*  901 */       suffix = tag.substring(handle.length());
/*  902 */       handle = this.tagPrefixes.get(handle);
/*      */     } 
/*      */     
/*  905 */     int end = suffix.length();
/*  906 */     String suffixText = (end > 0) ? suffix.substring(0, end) : "";
/*      */     
/*  908 */     if (handle != null) {
/*  909 */       return handle + suffixText;
/*      */     }
/*  911 */     return "!<" + suffixText + ">";
/*      */   }
/*      */   
/*      */   static String prepareAnchor(String anchor) {
/*  915 */     if (anchor.length() == 0) {
/*  916 */       throw new EmitterException("anchor must not be empty");
/*      */     }
/*  918 */     for (Character invalid : INVALID_ANCHOR) {
/*  919 */       if (anchor.indexOf(invalid.charValue()) > -1) {
/*  920 */         throw new EmitterException("Invalid character '" + invalid + "' in the anchor: " + anchor);
/*      */       }
/*      */     } 
/*  923 */     Matcher matcher = SPACES_PATTERN.matcher(anchor);
/*  924 */     if (matcher.find()) {
/*  925 */       throw new EmitterException("Anchor may not contain spaces: " + anchor);
/*      */     }
/*  927 */     return anchor;
/*      */   }
/*      */ 
/*      */   
/*      */   private ScalarAnalysis analyzeScalar(String scalar) {
/*  932 */     if (scalar.length() == 0) {
/*  933 */       return new ScalarAnalysis(scalar, true, false, false, true, true, false);
/*      */     }
/*      */     
/*  936 */     boolean blockIndicators = false;
/*  937 */     boolean flowIndicators = false;
/*  938 */     boolean lineBreaks = false;
/*  939 */     boolean specialCharacters = false;
/*      */ 
/*      */     
/*  942 */     boolean leadingSpace = false;
/*  943 */     boolean leadingBreak = false;
/*  944 */     boolean trailingSpace = false;
/*  945 */     boolean trailingBreak = false;
/*  946 */     boolean breakSpace = false;
/*  947 */     boolean spaceBreak = false;
/*      */ 
/*      */     
/*  950 */     if (scalar.startsWith("---") || scalar.startsWith("...")) {
/*  951 */       blockIndicators = true;
/*  952 */       flowIndicators = true;
/*      */     } 
/*      */     
/*  955 */     boolean preceededByWhitespace = true;
/*  956 */     boolean followedByWhitespace = (scalar.length() == 1 || Constant.NULL_BL_T_LINEBR.has(scalar.codePointAt(1)));
/*      */     
/*  958 */     boolean previousSpace = false;
/*      */ 
/*      */     
/*  961 */     boolean previousBreak = false;
/*      */     
/*  963 */     int index = 0;
/*      */     
/*  965 */     while (index < scalar.length()) {
/*  966 */       int c = scalar.codePointAt(index);
/*      */       
/*  968 */       if (index == 0) {
/*      */         
/*  970 */         if ("#,[]{}&*!|>'\"%@`".indexOf(c) != -1) {
/*  971 */           flowIndicators = true;
/*  972 */           blockIndicators = true;
/*      */         } 
/*  974 */         if (c == 63 || c == 58) {
/*  975 */           flowIndicators = true;
/*  976 */           if (followedByWhitespace) {
/*  977 */             blockIndicators = true;
/*      */           }
/*      */         } 
/*  980 */         if (c == 45 && followedByWhitespace) {
/*  981 */           flowIndicators = true;
/*  982 */           blockIndicators = true;
/*      */         } 
/*      */       } else {
/*      */         
/*  986 */         if (",?[]{}".indexOf(c) != -1) {
/*  987 */           flowIndicators = true;
/*      */         }
/*  989 */         if (c == 58) {
/*  990 */           flowIndicators = true;
/*  991 */           if (followedByWhitespace) {
/*  992 */             blockIndicators = true;
/*      */           }
/*      */         } 
/*  995 */         if (c == 35 && preceededByWhitespace) {
/*  996 */           flowIndicators = true;
/*  997 */           blockIndicators = true;
/*      */         } 
/*      */       } 
/*      */       
/* 1001 */       boolean isLineBreak = Constant.LINEBR.has(c);
/* 1002 */       if (isLineBreak) {
/* 1003 */         lineBreaks = true;
/*      */       }
/* 1005 */       if (c != 10 && (32 > c || c > 126)) {
/* 1006 */         if (c == 133 || (c >= 160 && c <= 55295) || (c >= 57344 && c <= 65533) || (c >= 65536 && c <= 1114111)) {
/*      */ 
/*      */ 
/*      */           
/* 1010 */           if (!this.allowUnicode) {
/* 1011 */             specialCharacters = true;
/*      */           }
/*      */         } else {
/* 1014 */           specialCharacters = true;
/*      */         } 
/*      */       }
/*      */       
/* 1018 */       if (c == 32) {
/* 1019 */         if (index == 0) {
/* 1020 */           leadingSpace = true;
/*      */         }
/* 1022 */         if (index == scalar.length() - 1) {
/* 1023 */           trailingSpace = true;
/*      */         }
/* 1025 */         if (previousBreak) {
/* 1026 */           breakSpace = true;
/*      */         }
/* 1028 */         previousSpace = true;
/* 1029 */         previousBreak = false;
/* 1030 */       } else if (isLineBreak) {
/* 1031 */         if (index == 0) {
/* 1032 */           leadingBreak = true;
/*      */         }
/* 1034 */         if (index == scalar.length() - 1) {
/* 1035 */           trailingBreak = true;
/*      */         }
/* 1037 */         if (previousSpace) {
/* 1038 */           spaceBreak = true;
/*      */         }
/* 1040 */         previousSpace = false;
/* 1041 */         previousBreak = true;
/*      */       } else {
/* 1043 */         previousSpace = false;
/* 1044 */         previousBreak = false;
/*      */       } 
/*      */ 
/*      */       
/* 1048 */       index += Character.charCount(c);
/* 1049 */       preceededByWhitespace = (Constant.NULL_BL_T.has(c) || isLineBreak);
/* 1050 */       followedByWhitespace = true;
/* 1051 */       if (index + 1 < scalar.length()) {
/* 1052 */         int nextIndex = index + Character.charCount(scalar.codePointAt(index));
/* 1053 */         if (nextIndex < scalar.length()) {
/* 1054 */           followedByWhitespace = (Constant.NULL_BL_T.has(scalar.codePointAt(nextIndex)) || isLineBreak);
/*      */         }
/*      */       } 
/*      */     } 
/*      */     
/* 1059 */     boolean allowFlowPlain = true;
/* 1060 */     boolean allowBlockPlain = true;
/* 1061 */     boolean allowSingleQuoted = true;
/* 1062 */     boolean allowBlock = true;
/*      */     
/* 1064 */     if (leadingSpace || leadingBreak || trailingSpace || trailingBreak) {
/* 1065 */       allowFlowPlain = allowBlockPlain = false;
/*      */     }
/*      */     
/* 1068 */     if (trailingSpace) {
/* 1069 */       allowBlock = false;
/*      */     }
/*      */ 
/*      */     
/* 1073 */     if (breakSpace) {
/* 1074 */       allowFlowPlain = allowBlockPlain = allowSingleQuoted = false;
/*      */     }
/*      */ 
/*      */     
/* 1078 */     if (spaceBreak || specialCharacters) {
/* 1079 */       allowFlowPlain = allowBlockPlain = allowSingleQuoted = allowBlock = false;
/*      */     }
/*      */ 
/*      */     
/* 1083 */     if (lineBreaks) {
/* 1084 */       allowFlowPlain = false;
/*      */     }
/*      */     
/* 1087 */     if (flowIndicators) {
/* 1088 */       allowFlowPlain = false;
/*      */     }
/*      */     
/* 1091 */     if (blockIndicators) {
/* 1092 */       allowBlockPlain = false;
/*      */     }
/*      */     
/* 1095 */     return new ScalarAnalysis(scalar, false, lineBreaks, allowFlowPlain, allowBlockPlain, allowSingleQuoted, allowBlock);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   void flushStream() throws IOException {
/* 1102 */     this.stream.flush();
/*      */   }
/*      */ 
/*      */   
/*      */   void writeStreamStart() {}
/*      */ 
/*      */   
/*      */   void writeStreamEnd() throws IOException {
/* 1110 */     flushStream();
/*      */   }
/*      */ 
/*      */   
/*      */   void writeIndicator(String indicator, boolean needWhitespace, boolean whitespace, boolean indentation) throws IOException {
/* 1115 */     if (!this.whitespace && needWhitespace) {
/* 1116 */       this.column++;
/* 1117 */       this.stream.write(SPACE);
/*      */     } 
/* 1119 */     this.whitespace = whitespace;
/* 1120 */     this.indention = (this.indention && indentation);
/* 1121 */     this.column += indicator.length();
/* 1122 */     this.openEnded = false;
/* 1123 */     this.stream.write(indicator);
/*      */   }
/*      */   
/*      */   void writeIndent() throws IOException {
/*      */     int indent;
/* 1128 */     if (this.indent != null) {
/* 1129 */       indent = this.indent.intValue();
/*      */     } else {
/* 1131 */       indent = 0;
/*      */     } 
/*      */     
/* 1134 */     if (!this.indention || this.column > indent || (this.column == indent && !this.whitespace)) {
/* 1135 */       writeLineBreak(null);
/*      */     }
/*      */     
/* 1138 */     writeWhitespace(indent - this.column);
/*      */   }
/*      */   
/*      */   private void writeWhitespace(int length) throws IOException {
/* 1142 */     if (length <= 0) {
/*      */       return;
/*      */     }
/* 1145 */     this.whitespace = true;
/* 1146 */     char[] data = new char[length];
/* 1147 */     for (int i = 0; i < data.length; i++) {
/* 1148 */       data[i] = ' ';
/*      */     }
/* 1150 */     this.column += length;
/* 1151 */     this.stream.write(data);
/*      */   }
/*      */   
/*      */   private void writeLineBreak(String data) throws IOException {
/* 1155 */     this.whitespace = true;
/* 1156 */     this.indention = true;
/* 1157 */     this.column = 0;
/* 1158 */     if (data == null) {
/* 1159 */       this.stream.write(this.bestLineBreak);
/*      */     } else {
/* 1161 */       this.stream.write(data);
/*      */     } 
/*      */   }
/*      */   
/*      */   void writeVersionDirective(String versionText) throws IOException {
/* 1166 */     this.stream.write("%YAML ");
/* 1167 */     this.stream.write(versionText);
/* 1168 */     writeLineBreak(null);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   void writeTagDirective(String handleText, String prefixText) throws IOException {
/* 1174 */     this.stream.write("%TAG ");
/* 1175 */     this.stream.write(handleText);
/* 1176 */     this.stream.write(SPACE);
/* 1177 */     this.stream.write(prefixText);
/* 1178 */     writeLineBreak(null);
/*      */   }
/*      */ 
/*      */   
/*      */   private void writeSingleQuoted(String text, boolean split) throws IOException {
/* 1183 */     writeIndicator("'", true, false, false);
/* 1184 */     boolean spaces = false;
/* 1185 */     boolean breaks = false;
/* 1186 */     int start = 0, end = 0;
/*      */     
/* 1188 */     while (end <= text.length()) {
/* 1189 */       char ch = Character.MIN_VALUE;
/* 1190 */       if (end < text.length()) {
/* 1191 */         ch = text.charAt(end);
/*      */       }
/* 1193 */       if (spaces) {
/* 1194 */         if (ch == '\000' || ch != ' ') {
/* 1195 */           if (start + 1 == end && this.column > this.bestWidth && split && start != 0 && end != text.length()) {
/*      */             
/* 1197 */             writeIndent();
/*      */           } else {
/* 1199 */             int len = end - start;
/* 1200 */             this.column += len;
/* 1201 */             this.stream.write(text, start, len);
/*      */           } 
/* 1203 */           start = end;
/*      */         } 
/* 1205 */       } else if (breaks) {
/* 1206 */         if (ch == '\000' || Constant.LINEBR.hasNo(ch)) {
/* 1207 */           if (text.charAt(start) == '\n') {
/* 1208 */             writeLineBreak(null);
/*      */           }
/* 1210 */           String data = text.substring(start, end);
/* 1211 */           for (char br : data.toCharArray()) {
/* 1212 */             if (br == '\n') {
/* 1213 */               writeLineBreak(null);
/*      */             } else {
/* 1215 */               writeLineBreak(String.valueOf(br));
/*      */             } 
/*      */           } 
/* 1218 */           writeIndent();
/* 1219 */           start = end;
/*      */         }
/*      */       
/* 1222 */       } else if (Constant.LINEBR.has(ch, "\000 '") && 
/* 1223 */         start < end) {
/* 1224 */         int len = end - start;
/* 1225 */         this.column += len;
/* 1226 */         this.stream.write(text, start, len);
/* 1227 */         start = end;
/*      */       } 
/*      */ 
/*      */       
/* 1231 */       if (ch == '\'') {
/* 1232 */         this.column += 2;
/* 1233 */         this.stream.write("''");
/* 1234 */         start = end + 1;
/*      */       } 
/* 1236 */       if (ch != '\000') {
/* 1237 */         spaces = (ch == ' ');
/* 1238 */         breaks = Constant.LINEBR.has(ch);
/*      */       } 
/* 1240 */       end++;
/*      */     } 
/* 1242 */     writeIndicator("'", false, false, false);
/*      */   }
/*      */   
/*      */   private void writeDoubleQuoted(String text, boolean split) throws IOException {
/* 1246 */     writeIndicator("\"", true, false, false);
/* 1247 */     int start = 0;
/* 1248 */     int end = 0;
/* 1249 */     while (end <= text.length()) {
/* 1250 */       Character ch = null;
/* 1251 */       if (end < text.length()) {
/* 1252 */         ch = Character.valueOf(text.charAt(end));
/*      */       }
/* 1254 */       if (ch == null || "\"\\  ﻿".indexOf(ch.charValue()) != -1 || ' ' > ch.charValue() || ch.charValue() > '~') {
/*      */         
/* 1256 */         if (start < end) {
/* 1257 */           int len = end - start;
/* 1258 */           this.column += len;
/* 1259 */           this.stream.write(text, start, len);
/* 1260 */           start = end;
/*      */         } 
/* 1262 */         if (ch != null) {
/*      */           String data;
/* 1264 */           if (ESCAPE_REPLACEMENTS.containsKey(ch)) {
/* 1265 */             data = "\\" + (String)ESCAPE_REPLACEMENTS.get(ch);
/* 1266 */           } else if (!this.allowUnicode || !StreamReader.isPrintable(ch.charValue())) {
/*      */ 
/*      */             
/* 1269 */             if (ch.charValue() <= 'ÿ') {
/* 1270 */               String s = "0" + Integer.toString(ch.charValue(), 16);
/* 1271 */               data = "\\x" + s.substring(s.length() - 2);
/* 1272 */             } else if (ch.charValue() >= '?' && ch.charValue() <= '?') {
/* 1273 */               if (end + 1 < text.length()) {
/* 1274 */                 Character ch2 = Character.valueOf(text.charAt(++end));
/* 1275 */                 String s = "000" + Long.toHexString(Character.toCodePoint(ch.charValue(), ch2.charValue()));
/* 1276 */                 data = "\\U" + s.substring(s.length() - 8);
/*      */               } else {
/* 1278 */                 String s = "000" + Integer.toString(ch.charValue(), 16);
/* 1279 */                 data = "\\u" + s.substring(s.length() - 4);
/*      */               } 
/*      */             } else {
/* 1282 */               String s = "000" + Integer.toString(ch.charValue(), 16);
/* 1283 */               data = "\\u" + s.substring(s.length() - 4);
/*      */             } 
/*      */           } else {
/* 1286 */             data = String.valueOf(ch);
/*      */           } 
/* 1288 */           this.column += data.length();
/* 1289 */           this.stream.write(data);
/* 1290 */           start = end + 1;
/*      */         } 
/*      */       } 
/* 1293 */       if (0 < end && end < text.length() - 1 && (ch.charValue() == ' ' || start >= end) && this.column + end - start > this.bestWidth && split) {
/*      */         String data;
/*      */         
/* 1296 */         if (start >= end) {
/* 1297 */           data = "\\";
/*      */         } else {
/* 1299 */           data = text.substring(start, end) + "\\";
/*      */         } 
/* 1301 */         if (start < end) {
/* 1302 */           start = end;
/*      */         }
/* 1304 */         this.column += data.length();
/* 1305 */         this.stream.write(data);
/* 1306 */         writeIndent();
/* 1307 */         this.whitespace = false;
/* 1308 */         this.indention = false;
/* 1309 */         if (text.charAt(start) == ' ') {
/* 1310 */           data = "\\";
/* 1311 */           this.column += data.length();
/* 1312 */           this.stream.write(data);
/*      */         } 
/*      */       } 
/* 1315 */       end++;
/*      */     } 
/* 1317 */     writeIndicator("\"", false, false, false);
/*      */   }
/*      */   
/*      */   private String determineBlockHints(String text) {
/* 1321 */     StringBuilder hints = new StringBuilder();
/* 1322 */     if (Constant.LINEBR.has(text.charAt(0), " ")) {
/* 1323 */       hints.append(this.bestIndent);
/*      */     }
/* 1325 */     char ch1 = text.charAt(text.length() - 1);
/* 1326 */     if (Constant.LINEBR.hasNo(ch1)) {
/* 1327 */       hints.append("-");
/* 1328 */     } else if (text.length() == 1 || Constant.LINEBR.has(text.charAt(text.length() - 2))) {
/* 1329 */       hints.append("+");
/*      */     } 
/* 1331 */     return hints.toString();
/*      */   }
/*      */   
/*      */   void writeFolded(String text, boolean split) throws IOException {
/* 1335 */     String hints = determineBlockHints(text);
/* 1336 */     writeIndicator(">" + hints, true, false, false);
/* 1337 */     if (hints.length() > 0 && hints.charAt(hints.length() - 1) == '+') {
/* 1338 */       this.openEnded = true;
/*      */     }
/* 1340 */     writeLineBreak(null);
/* 1341 */     boolean leadingSpace = true;
/* 1342 */     boolean spaces = false;
/* 1343 */     boolean breaks = true;
/* 1344 */     int start = 0, end = 0;
/* 1345 */     while (end <= text.length()) {
/* 1346 */       char ch = Character.MIN_VALUE;
/* 1347 */       if (end < text.length()) {
/* 1348 */         ch = text.charAt(end);
/*      */       }
/* 1350 */       if (breaks) {
/* 1351 */         if (ch == '\000' || Constant.LINEBR.hasNo(ch)) {
/* 1352 */           if (!leadingSpace && ch != '\000' && ch != ' ' && text.charAt(start) == '\n') {
/* 1353 */             writeLineBreak(null);
/*      */           }
/* 1355 */           leadingSpace = (ch == ' ');
/* 1356 */           String data = text.substring(start, end);
/* 1357 */           for (char br : data.toCharArray()) {
/* 1358 */             if (br == '\n') {
/* 1359 */               writeLineBreak(null);
/*      */             } else {
/* 1361 */               writeLineBreak(String.valueOf(br));
/*      */             } 
/*      */           } 
/* 1364 */           if (ch != '\000') {
/* 1365 */             writeIndent();
/*      */           }
/* 1367 */           start = end;
/*      */         } 
/* 1369 */       } else if (spaces) {
/* 1370 */         if (ch != ' ') {
/* 1371 */           if (start + 1 == end && this.column > this.bestWidth && split) {
/* 1372 */             writeIndent();
/*      */           } else {
/* 1374 */             int len = end - start;
/* 1375 */             this.column += len;
/* 1376 */             this.stream.write(text, start, len);
/*      */           } 
/* 1378 */           start = end;
/*      */         }
/*      */       
/* 1381 */       } else if (Constant.LINEBR.has(ch, "\000 ")) {
/* 1382 */         int len = end - start;
/* 1383 */         this.column += len;
/* 1384 */         this.stream.write(text, start, len);
/* 1385 */         if (ch == '\000') {
/* 1386 */           writeLineBreak(null);
/*      */         }
/* 1388 */         start = end;
/*      */       } 
/*      */       
/* 1391 */       if (ch != '\000') {
/* 1392 */         breaks = Constant.LINEBR.has(ch);
/* 1393 */         spaces = (ch == ' ');
/*      */       } 
/* 1395 */       end++;
/*      */     } 
/*      */   }
/*      */   
/*      */   void writeLiteral(String text) throws IOException {
/* 1400 */     String hints = determineBlockHints(text);
/* 1401 */     writeIndicator("|" + hints, true, false, false);
/* 1402 */     if (hints.length() > 0 && hints.charAt(hints.length() - 1) == '+') {
/* 1403 */       this.openEnded = true;
/*      */     }
/* 1405 */     writeLineBreak(null);
/* 1406 */     boolean breaks = true;
/* 1407 */     int start = 0, end = 0;
/* 1408 */     while (end <= text.length()) {
/* 1409 */       char ch = Character.MIN_VALUE;
/* 1410 */       if (end < text.length()) {
/* 1411 */         ch = text.charAt(end);
/*      */       }
/* 1413 */       if (breaks) {
/* 1414 */         if (ch == '\000' || Constant.LINEBR.hasNo(ch)) {
/* 1415 */           String data = text.substring(start, end);
/* 1416 */           for (char br : data.toCharArray()) {
/* 1417 */             if (br == '\n') {
/* 1418 */               writeLineBreak(null);
/*      */             } else {
/* 1420 */               writeLineBreak(String.valueOf(br));
/*      */             } 
/*      */           } 
/* 1423 */           if (ch != '\000') {
/* 1424 */             writeIndent();
/*      */           }
/* 1426 */           start = end;
/*      */         }
/*      */       
/* 1429 */       } else if (ch == '\000' || Constant.LINEBR.has(ch)) {
/* 1430 */         this.stream.write(text, start, end - start);
/* 1431 */         if (ch == '\000') {
/* 1432 */           writeLineBreak(null);
/*      */         }
/* 1434 */         start = end;
/*      */       } 
/*      */       
/* 1437 */       if (ch != '\000') {
/* 1438 */         breaks = Constant.LINEBR.has(ch);
/*      */       }
/* 1440 */       end++;
/*      */     } 
/*      */   }
/*      */   
/*      */   void writePlain(String text, boolean split) throws IOException {
/* 1445 */     if (this.rootContext) {
/* 1446 */       this.openEnded = true;
/*      */     }
/* 1448 */     if (text.length() == 0) {
/*      */       return;
/*      */     }
/* 1451 */     if (!this.whitespace) {
/* 1452 */       this.column++;
/* 1453 */       this.stream.write(SPACE);
/*      */     } 
/* 1455 */     this.whitespace = false;
/* 1456 */     this.indention = false;
/* 1457 */     boolean spaces = false;
/* 1458 */     boolean breaks = false;
/* 1459 */     int start = 0, end = 0;
/* 1460 */     while (end <= text.length()) {
/* 1461 */       char ch = Character.MIN_VALUE;
/* 1462 */       if (end < text.length()) {
/* 1463 */         ch = text.charAt(end);
/*      */       }
/* 1465 */       if (spaces) {
/* 1466 */         if (ch != ' ') {
/* 1467 */           if (start + 1 == end && this.column > this.bestWidth && split) {
/* 1468 */             writeIndent();
/* 1469 */             this.whitespace = false;
/* 1470 */             this.indention = false;
/*      */           } else {
/* 1472 */             int len = end - start;
/* 1473 */             this.column += len;
/* 1474 */             this.stream.write(text, start, len);
/*      */           } 
/* 1476 */           start = end;
/*      */         } 
/* 1478 */       } else if (breaks) {
/* 1479 */         if (Constant.LINEBR.hasNo(ch)) {
/* 1480 */           if (text.charAt(start) == '\n') {
/* 1481 */             writeLineBreak(null);
/*      */           }
/* 1483 */           String data = text.substring(start, end);
/* 1484 */           for (char br : data.toCharArray()) {
/* 1485 */             if (br == '\n') {
/* 1486 */               writeLineBreak(null);
/*      */             } else {
/* 1488 */               writeLineBreak(String.valueOf(br));
/*      */             } 
/*      */           } 
/* 1491 */           writeIndent();
/* 1492 */           this.whitespace = false;
/* 1493 */           this.indention = false;
/* 1494 */           start = end;
/*      */         }
/*      */       
/* 1497 */       } else if (Constant.LINEBR.has(ch, "\000 ")) {
/* 1498 */         int len = end - start;
/* 1499 */         this.column += len;
/* 1500 */         this.stream.write(text, start, len);
/* 1501 */         start = end;
/*      */       } 
/*      */       
/* 1504 */       if (ch != '\000') {
/* 1505 */         spaces = (ch == ' ');
/* 1506 */         breaks = Constant.LINEBR.has(ch);
/*      */       } 
/* 1508 */       end++;
/*      */     } 
/*      */   }
/*      */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\yaml\snakeyaml\emitter\Emitter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */