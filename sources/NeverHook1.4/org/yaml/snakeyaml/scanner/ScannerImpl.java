/*      */ package org.yaml.snakeyaml.scanner;
/*      */ 
/*      */ import java.nio.ByteBuffer;
/*      */ import java.nio.charset.CharacterCodingException;
/*      */ import java.util.ArrayList;
/*      */ import java.util.HashMap;
/*      */ import java.util.Iterator;
/*      */ import java.util.LinkedHashMap;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.regex.Pattern;
/*      */ import org.yaml.snakeyaml.DumperOptions;
/*      */ import org.yaml.snakeyaml.error.Mark;
/*      */ import org.yaml.snakeyaml.error.YAMLException;
/*      */ import org.yaml.snakeyaml.reader.StreamReader;
/*      */ import org.yaml.snakeyaml.tokens.AliasToken;
/*      */ import org.yaml.snakeyaml.tokens.AnchorToken;
/*      */ import org.yaml.snakeyaml.tokens.BlockEndToken;
/*      */ import org.yaml.snakeyaml.tokens.BlockEntryToken;
/*      */ import org.yaml.snakeyaml.tokens.BlockMappingStartToken;
/*      */ import org.yaml.snakeyaml.tokens.BlockSequenceStartToken;
/*      */ import org.yaml.snakeyaml.tokens.DirectiveToken;
/*      */ import org.yaml.snakeyaml.tokens.DocumentEndToken;
/*      */ import org.yaml.snakeyaml.tokens.DocumentStartToken;
/*      */ import org.yaml.snakeyaml.tokens.FlowEntryToken;
/*      */ import org.yaml.snakeyaml.tokens.FlowMappingEndToken;
/*      */ import org.yaml.snakeyaml.tokens.FlowMappingStartToken;
/*      */ import org.yaml.snakeyaml.tokens.FlowSequenceEndToken;
/*      */ import org.yaml.snakeyaml.tokens.FlowSequenceStartToken;
/*      */ import org.yaml.snakeyaml.tokens.KeyToken;
/*      */ import org.yaml.snakeyaml.tokens.ScalarToken;
/*      */ import org.yaml.snakeyaml.tokens.StreamEndToken;
/*      */ import org.yaml.snakeyaml.tokens.StreamStartToken;
/*      */ import org.yaml.snakeyaml.tokens.TagToken;
/*      */ import org.yaml.snakeyaml.tokens.TagTuple;
/*      */ import org.yaml.snakeyaml.tokens.Token;
/*      */ import org.yaml.snakeyaml.tokens.ValueToken;
/*      */ import org.yaml.snakeyaml.util.ArrayStack;
/*      */ import org.yaml.snakeyaml.util.UriEncoder;
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
/*      */ public final class ScannerImpl
/*      */   implements Scanner
/*      */ {
/*   88 */   private static final Pattern NOT_HEXA = Pattern.compile("[^0-9A-Fa-f]");
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
/*   99 */   public static final Map<Character, String> ESCAPE_REPLACEMENTS = new HashMap<>();
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
/*  115 */   public static final Map<Character, Integer> ESCAPE_CODES = new HashMap<>();
/*      */   private final StreamReader reader;
/*      */   
/*      */   static {
/*  119 */     ESCAPE_REPLACEMENTS.put(Character.valueOf('0'), "\000");
/*      */     
/*  121 */     ESCAPE_REPLACEMENTS.put(Character.valueOf('a'), "\007");
/*      */     
/*  123 */     ESCAPE_REPLACEMENTS.put(Character.valueOf('b'), "\b");
/*      */     
/*  125 */     ESCAPE_REPLACEMENTS.put(Character.valueOf('t'), "\t");
/*      */     
/*  127 */     ESCAPE_REPLACEMENTS.put(Character.valueOf('n'), "\n");
/*      */     
/*  129 */     ESCAPE_REPLACEMENTS.put(Character.valueOf('v'), "\013");
/*      */     
/*  131 */     ESCAPE_REPLACEMENTS.put(Character.valueOf('f'), "\f");
/*      */     
/*  133 */     ESCAPE_REPLACEMENTS.put(Character.valueOf('r'), "\r");
/*      */     
/*  135 */     ESCAPE_REPLACEMENTS.put(Character.valueOf('e'), "\033");
/*      */     
/*  137 */     ESCAPE_REPLACEMENTS.put(Character.valueOf(' '), " ");
/*      */     
/*  139 */     ESCAPE_REPLACEMENTS.put(Character.valueOf('"'), "\"");
/*      */     
/*  141 */     ESCAPE_REPLACEMENTS.put(Character.valueOf('\\'), "\\");
/*      */     
/*  143 */     ESCAPE_REPLACEMENTS.put(Character.valueOf('N'), "");
/*      */     
/*  145 */     ESCAPE_REPLACEMENTS.put(Character.valueOf('_'), " ");
/*      */     
/*  147 */     ESCAPE_REPLACEMENTS.put(Character.valueOf('L'), " ");
/*      */     
/*  149 */     ESCAPE_REPLACEMENTS.put(Character.valueOf('P'), " ");
/*      */ 
/*      */     
/*  152 */     ESCAPE_CODES.put(Character.valueOf('x'), Integer.valueOf(2));
/*      */     
/*  154 */     ESCAPE_CODES.put(Character.valueOf('u'), Integer.valueOf(4));
/*      */     
/*  156 */     ESCAPE_CODES.put(Character.valueOf('U'), Integer.valueOf(8));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean done = false;
/*      */ 
/*      */   
/*  164 */   private int flowLevel = 0;
/*      */ 
/*      */   
/*      */   private List<Token> tokens;
/*      */ 
/*      */   
/*  170 */   private int tokensTaken = 0;
/*      */ 
/*      */   
/*  173 */   private int indent = -1;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private ArrayStack<Integer> indents;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean allowSimpleKey = true;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private Map<Integer, SimpleKey> possibleSimpleKeys;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ScannerImpl(StreamReader reader) {
/*  214 */     this.reader = reader;
/*  215 */     this.tokens = new ArrayList<>(100);
/*  216 */     this.indents = new ArrayStack(10);
/*      */     
/*  218 */     this.possibleSimpleKeys = new LinkedHashMap<>();
/*  219 */     fetchStreamStart();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean checkToken(Token.ID... choices) {
/*  226 */     while (needMoreTokens()) {
/*  227 */       fetchMoreTokens();
/*      */     }
/*  229 */     if (!this.tokens.isEmpty()) {
/*  230 */       if (choices.length == 0) {
/*  231 */         return true;
/*      */       }
/*      */ 
/*      */       
/*  235 */       Token.ID first = ((Token)this.tokens.get(0)).getTokenId();
/*  236 */       for (int i = 0; i < choices.length; i++) {
/*  237 */         if (first == choices[i]) {
/*  238 */           return true;
/*      */         }
/*      */       } 
/*      */     } 
/*  242 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Token peekToken() {
/*  249 */     while (needMoreTokens()) {
/*  250 */       fetchMoreTokens();
/*      */     }
/*  252 */     return this.tokens.get(0);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Token getToken() {
/*  259 */     this.tokensTaken++;
/*  260 */     return this.tokens.remove(0);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean needMoreTokens() {
/*  269 */     if (this.done) {
/*  270 */       return false;
/*      */     }
/*      */     
/*  273 */     if (this.tokens.isEmpty()) {
/*  274 */       return true;
/*      */     }
/*      */ 
/*      */     
/*  278 */     stalePossibleSimpleKeys();
/*  279 */     return (nextPossibleSimpleKey() == this.tokensTaken);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void fetchMoreTokens() {
/*  287 */     scanToNextToken();
/*      */     
/*  289 */     stalePossibleSimpleKeys();
/*      */ 
/*      */     
/*  292 */     unwindIndent(this.reader.getColumn());
/*      */ 
/*      */     
/*  295 */     int c = this.reader.peek();
/*  296 */     switch (c) {
/*      */       
/*      */       case 0:
/*  299 */         fetchStreamEnd();
/*      */         return;
/*      */       
/*      */       case 37:
/*  303 */         if (checkDirective()) {
/*  304 */           fetchDirective();
/*      */           return;
/*      */         } 
/*      */         break;
/*      */       
/*      */       case 45:
/*  310 */         if (checkDocumentStart()) {
/*  311 */           fetchDocumentStart();
/*      */           return;
/*      */         } 
/*  314 */         if (checkBlockEntry()) {
/*  315 */           fetchBlockEntry();
/*      */           return;
/*      */         } 
/*      */         break;
/*      */       
/*      */       case 46:
/*  321 */         if (checkDocumentEnd()) {
/*  322 */           fetchDocumentEnd();
/*      */           return;
/*      */         } 
/*      */         break;
/*      */ 
/*      */       
/*      */       case 91:
/*  329 */         fetchFlowSequenceStart();
/*      */         return;
/*      */       
/*      */       case 123:
/*  333 */         fetchFlowMappingStart();
/*      */         return;
/*      */       
/*      */       case 93:
/*  337 */         fetchFlowSequenceEnd();
/*      */         return;
/*      */       
/*      */       case 125:
/*  341 */         fetchFlowMappingEnd();
/*      */         return;
/*      */       
/*      */       case 44:
/*  345 */         fetchFlowEntry();
/*      */         return;
/*      */ 
/*      */       
/*      */       case 63:
/*  350 */         if (checkKey()) {
/*  351 */           fetchKey();
/*      */           return;
/*      */         } 
/*      */         break;
/*      */       
/*      */       case 58:
/*  357 */         if (checkValue()) {
/*  358 */           fetchValue();
/*      */           return;
/*      */         } 
/*      */         break;
/*      */       
/*      */       case 42:
/*  364 */         fetchAlias();
/*      */         return;
/*      */       
/*      */       case 38:
/*  368 */         fetchAnchor();
/*      */         return;
/*      */       
/*      */       case 33:
/*  372 */         fetchTag();
/*      */         return;
/*      */       
/*      */       case 124:
/*  376 */         if (this.flowLevel == 0) {
/*  377 */           fetchLiteral();
/*      */           return;
/*      */         } 
/*      */         break;
/*      */       
/*      */       case 62:
/*  383 */         if (this.flowLevel == 0) {
/*  384 */           fetchFolded();
/*      */           return;
/*      */         } 
/*      */         break;
/*      */       
/*      */       case 39:
/*  390 */         fetchSingle();
/*      */         return;
/*      */       
/*      */       case 34:
/*  394 */         fetchDouble();
/*      */         return;
/*      */     } 
/*      */     
/*  398 */     if (checkPlain()) {
/*  399 */       fetchPlain();
/*      */ 
/*      */       
/*      */       return;
/*      */     } 
/*      */     
/*  405 */     String chRepresentation = String.valueOf(Character.toChars(c));
/*  406 */     for (Character s : ESCAPE_REPLACEMENTS.keySet()) {
/*  407 */       String v = ESCAPE_REPLACEMENTS.get(s);
/*  408 */       if (v.equals(chRepresentation)) {
/*  409 */         chRepresentation = "\\" + s;
/*      */         break;
/*      */       } 
/*      */     } 
/*  413 */     if (c == 9)
/*  414 */       chRepresentation = chRepresentation + "(TAB)"; 
/*  415 */     String text = String.format("found character '%s' that cannot start any token. (Do not use %s for indentation)", new Object[] { chRepresentation, chRepresentation });
/*      */ 
/*      */     
/*  418 */     throw new ScannerException("while scanning for the next token", null, text, this.reader.getMark());
/*      */   }
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
/*      */   private int nextPossibleSimpleKey() {
/*  433 */     if (!this.possibleSimpleKeys.isEmpty()) {
/*  434 */       return ((SimpleKey)this.possibleSimpleKeys.values().iterator().next()).getTokenNumber();
/*      */     }
/*  436 */     return -1;
/*      */   }
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
/*      */   private void stalePossibleSimpleKeys() {
/*  450 */     if (!this.possibleSimpleKeys.isEmpty()) {
/*  451 */       Iterator<SimpleKey> iterator = this.possibleSimpleKeys.values().iterator();
/*  452 */       while (iterator.hasNext()) {
/*  453 */         SimpleKey key = iterator.next();
/*  454 */         if (key.getLine() != this.reader.getLine() || this.reader.getIndex() - key.getIndex() > 1024) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */           
/*  460 */           if (key.isRequired())
/*      */           {
/*      */             
/*  463 */             throw new ScannerException("while scanning a simple key", key.getMark(), "could not find expected ':'", this.reader.getMark());
/*      */           }
/*      */           
/*  466 */           iterator.remove();
/*      */         } 
/*      */       } 
/*      */     } 
/*      */   }
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
/*      */   private void savePossibleSimpleKey() {
/*  485 */     boolean required = (this.flowLevel == 0 && this.indent == this.reader.getColumn());
/*      */     
/*  487 */     if (this.allowSimpleKey || !required) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  497 */       if (this.allowSimpleKey) {
/*  498 */         removePossibleSimpleKey();
/*  499 */         int tokenNumber = this.tokensTaken + this.tokens.size();
/*  500 */         SimpleKey key = new SimpleKey(tokenNumber, required, this.reader.getIndex(), this.reader.getLine(), this.reader.getColumn(), this.reader.getMark());
/*      */         
/*  502 */         this.possibleSimpleKeys.put(Integer.valueOf(this.flowLevel), key);
/*      */       } 
/*      */       return;
/*      */     } 
/*      */     throw new YAMLException("A simple key is required only if it is the first token in the current line");
/*      */   }
/*      */   
/*      */   private void removePossibleSimpleKey() {
/*  510 */     SimpleKey key = this.possibleSimpleKeys.remove(Integer.valueOf(this.flowLevel));
/*  511 */     if (key != null && key.isRequired()) {
/*  512 */       throw new ScannerException("while scanning a simple key", key.getMark(), "could not find expected ':'", this.reader.getMark());
/*      */     }
/*      */   }
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
/*      */   private void unwindIndent(int col) {
/*  542 */     if (this.flowLevel != 0) {
/*      */       return;
/*      */     }
/*      */ 
/*      */     
/*  547 */     while (this.indent > col) {
/*  548 */       Mark mark = this.reader.getMark();
/*  549 */       this.indent = ((Integer)this.indents.pop()).intValue();
/*  550 */       this.tokens.add(new BlockEndToken(mark, mark));
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean addIndent(int column) {
/*  558 */     if (this.indent < column) {
/*  559 */       this.indents.push(Integer.valueOf(this.indent));
/*  560 */       this.indent = column;
/*  561 */       return true;
/*      */     } 
/*  563 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void fetchStreamStart() {
/*  574 */     Mark mark = this.reader.getMark();
/*      */ 
/*      */     
/*  577 */     StreamStartToken streamStartToken = new StreamStartToken(mark, mark);
/*  578 */     this.tokens.add(streamStartToken);
/*      */   }
/*      */ 
/*      */   
/*      */   private void fetchStreamEnd() {
/*  583 */     unwindIndent(-1);
/*      */ 
/*      */     
/*  586 */     removePossibleSimpleKey();
/*  587 */     this.allowSimpleKey = false;
/*  588 */     this.possibleSimpleKeys.clear();
/*      */ 
/*      */     
/*  591 */     Mark mark = this.reader.getMark();
/*      */ 
/*      */     
/*  594 */     StreamEndToken streamEndToken = new StreamEndToken(mark, mark);
/*  595 */     this.tokens.add(streamEndToken);
/*      */ 
/*      */     
/*  598 */     this.done = true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void fetchDirective() {
/*  610 */     unwindIndent(-1);
/*      */ 
/*      */     
/*  613 */     removePossibleSimpleKey();
/*  614 */     this.allowSimpleKey = false;
/*      */ 
/*      */     
/*  617 */     Token tok = scanDirective();
/*  618 */     this.tokens.add(tok);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void fetchDocumentStart() {
/*  625 */     fetchDocumentIndicator(true);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void fetchDocumentEnd() {
/*  632 */     fetchDocumentIndicator(false);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void fetchDocumentIndicator(boolean isDocumentStart) {
/*      */     DocumentEndToken documentEndToken;
/*  641 */     unwindIndent(-1);
/*      */ 
/*      */ 
/*      */     
/*  645 */     removePossibleSimpleKey();
/*  646 */     this.allowSimpleKey = false;
/*      */ 
/*      */     
/*  649 */     Mark startMark = this.reader.getMark();
/*  650 */     this.reader.forward(3);
/*  651 */     Mark endMark = this.reader.getMark();
/*      */     
/*  653 */     if (isDocumentStart) {
/*  654 */       DocumentStartToken documentStartToken = new DocumentStartToken(startMark, endMark);
/*      */     } else {
/*  656 */       documentEndToken = new DocumentEndToken(startMark, endMark);
/*      */     } 
/*  658 */     this.tokens.add(documentEndToken);
/*      */   }
/*      */   
/*      */   private void fetchFlowSequenceStart() {
/*  662 */     fetchFlowCollectionStart(false);
/*      */   }
/*      */   
/*      */   private void fetchFlowMappingStart() {
/*  666 */     fetchFlowCollectionStart(true);
/*      */   }
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
/*      */   private void fetchFlowCollectionStart(boolean isMappingStart) {
/*      */     FlowSequenceStartToken flowSequenceStartToken;
/*  683 */     savePossibleSimpleKey();
/*      */ 
/*      */     
/*  686 */     this.flowLevel++;
/*      */ 
/*      */     
/*  689 */     this.allowSimpleKey = true;
/*      */ 
/*      */     
/*  692 */     Mark startMark = this.reader.getMark();
/*  693 */     this.reader.forward(1);
/*  694 */     Mark endMark = this.reader.getMark();
/*      */     
/*  696 */     if (isMappingStart) {
/*  697 */       FlowMappingStartToken flowMappingStartToken = new FlowMappingStartToken(startMark, endMark);
/*      */     } else {
/*  699 */       flowSequenceStartToken = new FlowSequenceStartToken(startMark, endMark);
/*      */     } 
/*  701 */     this.tokens.add(flowSequenceStartToken);
/*      */   }
/*      */   
/*      */   private void fetchFlowSequenceEnd() {
/*  705 */     fetchFlowCollectionEnd(false);
/*      */   }
/*      */   
/*      */   private void fetchFlowMappingEnd() {
/*  709 */     fetchFlowCollectionEnd(true);
/*      */   }
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
/*      */   private void fetchFlowCollectionEnd(boolean isMappingEnd) {
/*      */     FlowSequenceEndToken flowSequenceEndToken;
/*  724 */     removePossibleSimpleKey();
/*      */ 
/*      */     
/*  727 */     this.flowLevel--;
/*      */ 
/*      */     
/*  730 */     this.allowSimpleKey = false;
/*      */ 
/*      */     
/*  733 */     Mark startMark = this.reader.getMark();
/*  734 */     this.reader.forward();
/*  735 */     Mark endMark = this.reader.getMark();
/*      */     
/*  737 */     if (isMappingEnd) {
/*  738 */       FlowMappingEndToken flowMappingEndToken = new FlowMappingEndToken(startMark, endMark);
/*      */     } else {
/*  740 */       flowSequenceEndToken = new FlowSequenceEndToken(startMark, endMark);
/*      */     } 
/*  742 */     this.tokens.add(flowSequenceEndToken);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void fetchFlowEntry() {
/*  753 */     this.allowSimpleKey = true;
/*      */ 
/*      */     
/*  756 */     removePossibleSimpleKey();
/*      */ 
/*      */     
/*  759 */     Mark startMark = this.reader.getMark();
/*  760 */     this.reader.forward();
/*  761 */     Mark endMark = this.reader.getMark();
/*  762 */     FlowEntryToken flowEntryToken = new FlowEntryToken(startMark, endMark);
/*  763 */     this.tokens.add(flowEntryToken);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void fetchBlockEntry() {
/*  773 */     if (this.flowLevel == 0) {
/*      */       
/*  775 */       if (!this.allowSimpleKey) {
/*  776 */         throw new ScannerException(null, null, "sequence entries are not allowed here", this.reader.getMark());
/*      */       }
/*      */ 
/*      */ 
/*      */       
/*  781 */       if (addIndent(this.reader.getColumn())) {
/*  782 */         Mark mark = this.reader.getMark();
/*  783 */         this.tokens.add(new BlockSequenceStartToken(mark, mark));
/*      */       } 
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  790 */     this.allowSimpleKey = true;
/*      */ 
/*      */     
/*  793 */     removePossibleSimpleKey();
/*      */ 
/*      */     
/*  796 */     Mark startMark = this.reader.getMark();
/*  797 */     this.reader.forward();
/*  798 */     Mark endMark = this.reader.getMark();
/*  799 */     BlockEntryToken blockEntryToken = new BlockEntryToken(startMark, endMark);
/*  800 */     this.tokens.add(blockEntryToken);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void fetchKey() {
/*  810 */     if (this.flowLevel == 0) {
/*      */       
/*  812 */       if (!this.allowSimpleKey) {
/*  813 */         throw new ScannerException(null, null, "mapping keys are not allowed here", this.reader.getMark());
/*      */       }
/*      */ 
/*      */       
/*  817 */       if (addIndent(this.reader.getColumn())) {
/*  818 */         Mark mark = this.reader.getMark();
/*  819 */         this.tokens.add(new BlockMappingStartToken(mark, mark));
/*      */       } 
/*      */     } 
/*      */     
/*  823 */     this.allowSimpleKey = (this.flowLevel == 0);
/*      */ 
/*      */     
/*  826 */     removePossibleSimpleKey();
/*      */ 
/*      */     
/*  829 */     Mark startMark = this.reader.getMark();
/*  830 */     this.reader.forward();
/*  831 */     Mark endMark = this.reader.getMark();
/*  832 */     KeyToken keyToken = new KeyToken(startMark, endMark);
/*  833 */     this.tokens.add(keyToken);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void fetchValue() {
/*  843 */     SimpleKey key = this.possibleSimpleKeys.remove(Integer.valueOf(this.flowLevel));
/*  844 */     if (key != null) {
/*      */       
/*  846 */       this.tokens.add(key.getTokenNumber() - this.tokensTaken, new KeyToken(key.getMark(), key.getMark()));
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  851 */       if (this.flowLevel == 0 && 
/*  852 */         addIndent(key.getColumn())) {
/*  853 */         this.tokens.add(key.getTokenNumber() - this.tokensTaken, new BlockMappingStartToken(key.getMark(), key.getMark()));
/*      */       }
/*      */ 
/*      */ 
/*      */       
/*  858 */       this.allowSimpleKey = false;
/*      */     
/*      */     }
/*      */     else {
/*      */ 
/*      */       
/*  864 */       if (this.flowLevel == 0)
/*      */       {
/*      */ 
/*      */         
/*  868 */         if (!this.allowSimpleKey) {
/*  869 */           throw new ScannerException(null, null, "mapping values are not allowed here", this.reader.getMark());
/*      */         }
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  877 */       if (this.flowLevel == 0 && 
/*  878 */         addIndent(this.reader.getColumn())) {
/*  879 */         Mark mark = this.reader.getMark();
/*  880 */         this.tokens.add(new BlockMappingStartToken(mark, mark));
/*      */       } 
/*      */ 
/*      */ 
/*      */       
/*  885 */       this.allowSimpleKey = (this.flowLevel == 0);
/*      */ 
/*      */       
/*  888 */       removePossibleSimpleKey();
/*      */     } 
/*      */     
/*  891 */     Mark startMark = this.reader.getMark();
/*  892 */     this.reader.forward();
/*  893 */     Mark endMark = this.reader.getMark();
/*  894 */     ValueToken valueToken = new ValueToken(startMark, endMark);
/*  895 */     this.tokens.add(valueToken);
/*      */   }
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
/*      */   private void fetchAlias() {
/*  910 */     savePossibleSimpleKey();
/*      */ 
/*      */     
/*  913 */     this.allowSimpleKey = false;
/*      */ 
/*      */     
/*  916 */     Token tok = scanAnchor(false);
/*  917 */     this.tokens.add(tok);
/*      */   }
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
/*      */   private void fetchAnchor() {
/*  931 */     savePossibleSimpleKey();
/*      */ 
/*      */     
/*  934 */     this.allowSimpleKey = false;
/*      */ 
/*      */     
/*  937 */     Token tok = scanAnchor(true);
/*  938 */     this.tokens.add(tok);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void fetchTag() {
/*  948 */     savePossibleSimpleKey();
/*      */ 
/*      */     
/*  951 */     this.allowSimpleKey = false;
/*      */ 
/*      */     
/*  954 */     Token tok = scanTag();
/*  955 */     this.tokens.add(tok);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void fetchLiteral() {
/*  966 */     fetchBlockScalar('|');
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void fetchFolded() {
/*  976 */     fetchBlockScalar('>');
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void fetchBlockScalar(char style) {
/*  988 */     this.allowSimpleKey = true;
/*      */ 
/*      */     
/*  991 */     removePossibleSimpleKey();
/*      */ 
/*      */     
/*  994 */     Token tok = scanBlockScalar(style);
/*  995 */     this.tokens.add(tok);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void fetchSingle() {
/* 1002 */     fetchFlowScalar('\'');
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void fetchDouble() {
/* 1009 */     fetchFlowScalar('"');
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void fetchFlowScalar(char style) {
/* 1021 */     savePossibleSimpleKey();
/*      */ 
/*      */     
/* 1024 */     this.allowSimpleKey = false;
/*      */ 
/*      */     
/* 1027 */     Token tok = scanFlowScalar(style);
/* 1028 */     this.tokens.add(tok);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void fetchPlain() {
/* 1036 */     savePossibleSimpleKey();
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1041 */     this.allowSimpleKey = false;
/*      */ 
/*      */     
/* 1044 */     Token tok = scanPlain();
/* 1045 */     this.tokens.add(tok);
/*      */   }
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
/*      */   private boolean checkDirective() {
/* 1058 */     return (this.reader.getColumn() == 0);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean checkDocumentStart() {
/* 1067 */     if (this.reader.getColumn() == 0 && 
/* 1068 */       "---".equals(this.reader.prefix(3)) && Constant.NULL_BL_T_LINEBR.has(this.reader.peek(3))) {
/* 1069 */       return true;
/*      */     }
/*      */     
/* 1072 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean checkDocumentEnd() {
/* 1081 */     if (this.reader.getColumn() == 0 && 
/* 1082 */       "...".equals(this.reader.prefix(3)) && Constant.NULL_BL_T_LINEBR.has(this.reader.peek(3))) {
/* 1083 */       return true;
/*      */     }
/*      */     
/* 1086 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean checkBlockEntry() {
/* 1094 */     return Constant.NULL_BL_T_LINEBR.has(this.reader.peek(1));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean checkKey() {
/* 1102 */     if (this.flowLevel != 0) {
/* 1103 */       return true;
/*      */     }
/*      */     
/* 1106 */     return Constant.NULL_BL_T_LINEBR.has(this.reader.peek(1));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean checkValue() {
/* 1115 */     if (this.flowLevel != 0) {
/* 1116 */       return true;
/*      */     }
/*      */     
/* 1119 */     return Constant.NULL_BL_T_LINEBR.has(this.reader.peek(1));
/*      */   }
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
/*      */   private boolean checkPlain() {
/* 1143 */     int c = this.reader.peek();
/*      */ 
/*      */     
/* 1146 */     return (Constant.NULL_BL_T_LINEBR.hasNo(c, "-?:,[]{}#&*!|>'\"%@`") || (Constant.NULL_BL_T_LINEBR.hasNo(this.reader.peek(1)) && (c == 45 || (this.flowLevel == 0 && "?:".indexOf(c) != -1))));
/*      */   }
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
/*      */   private void scanToNextToken() {
/* 1177 */     if (this.reader.getIndex() == 0 && this.reader.peek() == 65279) {
/* 1178 */       this.reader.forward();
/*      */     }
/* 1180 */     boolean found = false;
/* 1181 */     while (!found) {
/* 1182 */       int ff = 0;
/*      */ 
/*      */       
/* 1185 */       while (this.reader.peek(ff) == 32) {
/* 1186 */         ff++;
/*      */       }
/* 1188 */       if (ff > 0) {
/* 1189 */         this.reader.forward(ff);
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1195 */       if (this.reader.peek() == 35) {
/* 1196 */         ff = 0;
/* 1197 */         while (Constant.NULL_OR_LINEBR.hasNo(this.reader.peek(ff))) {
/* 1198 */           ff++;
/*      */         }
/* 1200 */         if (ff > 0) {
/* 1201 */           this.reader.forward(ff);
/*      */         }
/*      */       } 
/*      */ 
/*      */       
/* 1206 */       if (scanLineBreak().length() != 0) {
/* 1207 */         if (this.flowLevel == 0)
/*      */         {
/*      */           
/* 1210 */           this.allowSimpleKey = true; } 
/*      */         continue;
/*      */       } 
/* 1213 */       found = true;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private Token scanDirective() {
/* 1221 */     Mark endMark, startMark = this.reader.getMark();
/*      */     
/* 1223 */     this.reader.forward();
/* 1224 */     String name = scanDirectiveName(startMark);
/* 1225 */     List<?> value = null;
/* 1226 */     if ("YAML".equals(name)) {
/* 1227 */       value = scanYamlDirectiveValue(startMark);
/* 1228 */       endMark = this.reader.getMark();
/* 1229 */     } else if ("TAG".equals(name)) {
/* 1230 */       value = scanTagDirectiveValue(startMark);
/* 1231 */       endMark = this.reader.getMark();
/*      */     } else {
/* 1233 */       endMark = this.reader.getMark();
/* 1234 */       int ff = 0;
/* 1235 */       while (Constant.NULL_OR_LINEBR.hasNo(this.reader.peek(ff))) {
/* 1236 */         ff++;
/*      */       }
/* 1238 */       if (ff > 0) {
/* 1239 */         this.reader.forward(ff);
/*      */       }
/*      */     } 
/* 1242 */     scanDirectiveIgnoredLine(startMark);
/* 1243 */     return (Token)new DirectiveToken(name, value, startMark, endMark);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private String scanDirectiveName(Mark startMark) {
/* 1254 */     int length = 0;
/*      */ 
/*      */ 
/*      */     
/* 1258 */     int c = this.reader.peek(length);
/* 1259 */     while (Constant.ALPHA.has(c)) {
/* 1260 */       length++;
/* 1261 */       c = this.reader.peek(length);
/*      */     } 
/*      */     
/* 1264 */     if (length == 0) {
/* 1265 */       String s = String.valueOf(Character.toChars(c));
/* 1266 */       throw new ScannerException("while scanning a directive", startMark, "expected alphabetic or numeric character, but found " + s + "(" + c + ")", this.reader.getMark());
/*      */     } 
/*      */ 
/*      */     
/* 1270 */     String value = this.reader.prefixForward(length);
/* 1271 */     c = this.reader.peek();
/* 1272 */     if (Constant.NULL_BL_LINEBR.hasNo(c)) {
/* 1273 */       String s = String.valueOf(Character.toChars(c));
/* 1274 */       throw new ScannerException("while scanning a directive", startMark, "expected alphabetic or numeric character, but found " + s + "(" + c + ")", this.reader.getMark());
/*      */     } 
/*      */ 
/*      */     
/* 1278 */     return value;
/*      */   }
/*      */ 
/*      */   
/*      */   private List<Integer> scanYamlDirectiveValue(Mark startMark) {
/* 1283 */     while (this.reader.peek() == 32) {
/* 1284 */       this.reader.forward();
/*      */     }
/* 1286 */     Integer major = scanYamlDirectiveNumber(startMark);
/* 1287 */     int c = this.reader.peek();
/* 1288 */     if (c != 46) {
/* 1289 */       String s = String.valueOf(Character.toChars(c));
/* 1290 */       throw new ScannerException("while scanning a directive", startMark, "expected a digit or '.', but found " + s + "(" + c + ")", this.reader.getMark());
/*      */     } 
/*      */ 
/*      */     
/* 1294 */     this.reader.forward();
/* 1295 */     Integer minor = scanYamlDirectiveNumber(startMark);
/* 1296 */     c = this.reader.peek();
/* 1297 */     if (Constant.NULL_BL_LINEBR.hasNo(c)) {
/* 1298 */       String s = String.valueOf(Character.toChars(c));
/* 1299 */       throw new ScannerException("while scanning a directive", startMark, "expected a digit or ' ', but found " + s + "(" + c + ")", this.reader.getMark());
/*      */     } 
/*      */ 
/*      */     
/* 1303 */     List<Integer> result = new ArrayList<>(2);
/* 1304 */     result.add(major);
/* 1305 */     result.add(minor);
/* 1306 */     return result;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private Integer scanYamlDirectiveNumber(Mark startMark) {
/* 1318 */     int c = this.reader.peek();
/* 1319 */     if (!Character.isDigit(c)) {
/* 1320 */       String s = String.valueOf(Character.toChars(c));
/* 1321 */       throw new ScannerException("while scanning a directive", startMark, "expected a digit, but found " + s + "(" + c + ")", this.reader.getMark());
/*      */     } 
/*      */     
/* 1324 */     int length = 0;
/* 1325 */     while (Character.isDigit(this.reader.peek(length))) {
/* 1326 */       length++;
/*      */     }
/* 1328 */     Integer value = Integer.valueOf(Integer.parseInt(this.reader.prefixForward(length)));
/* 1329 */     return value;
/*      */   }
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
/*      */   private List<String> scanTagDirectiveValue(Mark startMark) {
/* 1346 */     while (this.reader.peek() == 32) {
/* 1347 */       this.reader.forward();
/*      */     }
/* 1349 */     String handle = scanTagDirectiveHandle(startMark);
/* 1350 */     while (this.reader.peek() == 32) {
/* 1351 */       this.reader.forward();
/*      */     }
/* 1353 */     String prefix = scanTagDirectivePrefix(startMark);
/* 1354 */     List<String> result = new ArrayList<>(2);
/* 1355 */     result.add(handle);
/* 1356 */     result.add(prefix);
/* 1357 */     return result;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private String scanTagDirectiveHandle(Mark startMark) {
/* 1369 */     String value = scanTagHandle("directive", startMark);
/* 1370 */     int c = this.reader.peek();
/* 1371 */     if (c != 32) {
/* 1372 */       String s = String.valueOf(Character.toChars(c));
/* 1373 */       throw new ScannerException("while scanning a directive", startMark, "expected ' ', but found " + s + "(" + c + ")", this.reader.getMark());
/*      */     } 
/*      */     
/* 1376 */     return value;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private String scanTagDirectivePrefix(Mark startMark) {
/* 1386 */     String value = scanTagUri("directive", startMark);
/* 1387 */     int c = this.reader.peek();
/* 1388 */     if (Constant.NULL_BL_LINEBR.hasNo(c)) {
/* 1389 */       String s = String.valueOf(Character.toChars(c));
/* 1390 */       throw new ScannerException("while scanning a directive", startMark, "expected ' ', but found " + s + "(" + c + ")", this.reader.getMark());
/*      */     } 
/*      */ 
/*      */     
/* 1394 */     return value;
/*      */   }
/*      */ 
/*      */   
/*      */   private void scanDirectiveIgnoredLine(Mark startMark) {
/* 1399 */     while (this.reader.peek() == 32) {
/* 1400 */       this.reader.forward();
/*      */     }
/* 1402 */     if (this.reader.peek() == 35) {
/* 1403 */       while (Constant.NULL_OR_LINEBR.hasNo(this.reader.peek())) {
/* 1404 */         this.reader.forward();
/*      */       }
/*      */     }
/* 1407 */     int c = this.reader.peek();
/* 1408 */     String lineBreak = scanLineBreak();
/* 1409 */     if (lineBreak.length() == 0 && c != 0) {
/* 1410 */       String s = String.valueOf(Character.toChars(c));
/* 1411 */       throw new ScannerException("while scanning a directive", startMark, "expected a comment or a line break, but found " + s + "(" + c + ")", this.reader.getMark());
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private Token scanAnchor(boolean isAnchor) {
/*      */     AliasToken aliasToken;
/* 1418 */     Mark startMark = this.reader.getMark();
/* 1419 */     int indicator = this.reader.peek();
/* 1420 */     String name = (indicator == 42) ? "alias" : "anchor";
/* 1421 */     this.reader.forward();
/* 1422 */     int length = 0;
/* 1423 */     int c = this.reader.peek(length);
/*      */ 
/*      */     
/* 1426 */     while (Constant.NULL_BL_T_LINEBR.hasNo(c, ":,[]{}")) {
/* 1427 */       length++;
/* 1428 */       c = this.reader.peek(length);
/*      */     } 
/* 1430 */     if (length == 0) {
/* 1431 */       String s = String.valueOf(Character.toChars(c));
/* 1432 */       throw new ScannerException("while scanning an " + name, startMark, "unexpected character found " + s + "(" + c + ")", this.reader.getMark());
/*      */     } 
/*      */     
/* 1435 */     String value = this.reader.prefixForward(length);
/* 1436 */     c = this.reader.peek();
/* 1437 */     if (Constant.NULL_BL_T_LINEBR.hasNo(c, "?:,]}%@`")) {
/* 1438 */       String s = String.valueOf(Character.toChars(c));
/* 1439 */       throw new ScannerException("while scanning an " + name, startMark, "unexpected character found " + s + "(" + c + ")", this.reader.getMark());
/*      */     } 
/*      */     
/* 1442 */     Mark endMark = this.reader.getMark();
/*      */     
/* 1444 */     if (isAnchor) {
/* 1445 */       AnchorToken anchorToken = new AnchorToken(value, startMark, endMark);
/*      */     } else {
/* 1447 */       aliasToken = new AliasToken(value, startMark, endMark);
/*      */     } 
/* 1449 */     return (Token)aliasToken;
/*      */   }
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
/*      */ 
/*      */   
/*      */   private Token scanTag() {
/* 1487 */     Mark startMark = this.reader.getMark();
/*      */ 
/*      */     
/* 1490 */     int c = this.reader.peek(1);
/* 1491 */     String handle = null;
/* 1492 */     String suffix = null;
/*      */     
/* 1494 */     if (c == 60) {
/*      */ 
/*      */       
/* 1497 */       this.reader.forward(2);
/* 1498 */       suffix = scanTagUri("tag", startMark);
/* 1499 */       c = this.reader.peek();
/* 1500 */       if (c != 62) {
/*      */ 
/*      */         
/* 1503 */         String s = String.valueOf(Character.toChars(c));
/* 1504 */         throw new ScannerException("while scanning a tag", startMark, "expected '>', but found '" + s + "' (" + c + ")", this.reader.getMark());
/*      */       } 
/*      */ 
/*      */       
/* 1508 */       this.reader.forward();
/* 1509 */     } else if (Constant.NULL_BL_T_LINEBR.has(c)) {
/*      */ 
/*      */       
/* 1512 */       suffix = "!";
/* 1513 */       this.reader.forward();
/*      */     
/*      */     }
/*      */     else {
/*      */ 
/*      */       
/* 1519 */       int length = 1;
/* 1520 */       boolean useHandle = false;
/* 1521 */       while (Constant.NULL_BL_LINEBR.hasNo(c)) {
/* 1522 */         if (c == 33) {
/* 1523 */           useHandle = true;
/*      */           break;
/*      */         } 
/* 1526 */         length++;
/* 1527 */         c = this.reader.peek(length);
/*      */       } 
/*      */ 
/*      */       
/* 1531 */       if (useHandle) {
/* 1532 */         handle = scanTagHandle("tag", startMark);
/*      */       } else {
/* 1534 */         handle = "!";
/* 1535 */         this.reader.forward();
/*      */       } 
/* 1537 */       suffix = scanTagUri("tag", startMark);
/*      */     } 
/* 1539 */     c = this.reader.peek();
/*      */ 
/*      */     
/* 1542 */     if (Constant.NULL_BL_LINEBR.hasNo(c)) {
/* 1543 */       String s = String.valueOf(Character.toChars(c));
/* 1544 */       throw new ScannerException("while scanning a tag", startMark, "expected ' ', but found '" + s + "' (" + c + ")", this.reader.getMark());
/*      */     } 
/*      */     
/* 1547 */     TagTuple value = new TagTuple(handle, suffix);
/* 1548 */     Mark endMark = this.reader.getMark();
/* 1549 */     return (Token)new TagToken(value, startMark, endMark);
/*      */   }
/*      */   
/*      */   private Token scanBlockScalar(char style) {
/*      */     boolean folded;
/*      */     String breaks;
/*      */     int indent;
/*      */     Mark mark1;
/* 1557 */     if (style == '>') {
/* 1558 */       folded = true;
/*      */     } else {
/* 1560 */       folded = false;
/*      */     } 
/* 1562 */     StringBuilder chunks = new StringBuilder();
/* 1563 */     Mark startMark = this.reader.getMark();
/*      */     
/* 1565 */     this.reader.forward();
/* 1566 */     Chomping chompi = scanBlockScalarIndicators(startMark);
/* 1567 */     int increment = chompi.getIncrement();
/* 1568 */     scanBlockScalarIgnoredLine(startMark);
/*      */ 
/*      */     
/* 1571 */     int minIndent = this.indent + 1;
/* 1572 */     if (minIndent < 1) {
/* 1573 */       minIndent = 1;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1579 */     if (increment == -1) {
/* 1580 */       Object[] brme = scanBlockScalarIndentation();
/* 1581 */       breaks = (String)brme[0];
/* 1582 */       int maxIndent = ((Integer)brme[1]).intValue();
/* 1583 */       mark1 = (Mark)brme[2];
/* 1584 */       indent = Math.max(minIndent, maxIndent);
/*      */     } else {
/* 1586 */       indent = minIndent + increment - 1;
/* 1587 */       Object[] brme = scanBlockScalarBreaks(indent);
/* 1588 */       breaks = (String)brme[0];
/* 1589 */       mark1 = (Mark)brme[1];
/*      */     } 
/*      */     
/* 1592 */     String lineBreak = "";
/*      */ 
/*      */     
/* 1595 */     while (this.reader.getColumn() == indent && this.reader.peek() != 0) {
/* 1596 */       chunks.append(breaks);
/* 1597 */       boolean leadingNonSpace = (" \t".indexOf(this.reader.peek()) == -1);
/* 1598 */       int length = 0;
/* 1599 */       while (Constant.NULL_OR_LINEBR.hasNo(this.reader.peek(length))) {
/* 1600 */         length++;
/*      */       }
/* 1602 */       chunks.append(this.reader.prefixForward(length));
/* 1603 */       lineBreak = scanLineBreak();
/* 1604 */       Object[] brme = scanBlockScalarBreaks(indent);
/* 1605 */       breaks = (String)brme[0];
/* 1606 */       mark1 = (Mark)brme[1];
/* 1607 */       if (this.reader.getColumn() == indent && this.reader.peek() != 0) {
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 1612 */         if (folded && "\n".equals(lineBreak) && leadingNonSpace && " \t".indexOf(this.reader.peek()) == -1) {
/*      */           
/* 1614 */           if (breaks.length() == 0)
/* 1615 */             chunks.append(" "); 
/*      */           continue;
/*      */         } 
/* 1618 */         chunks.append(lineBreak);
/*      */       } 
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1627 */     if (chompi.chompTailIsNotFalse()) {
/* 1628 */       chunks.append(lineBreak);
/*      */     }
/* 1630 */     if (chompi.chompTailIsTrue()) {
/* 1631 */       chunks.append(breaks);
/*      */     }
/*      */     
/* 1634 */     return (Token)new ScalarToken(chunks.toString(), false, startMark, mark1, DumperOptions.ScalarStyle.createStyle(Character.valueOf(style)));
/*      */   }
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
/*      */   private Chomping scanBlockScalarIndicators(Mark startMark) {
/* 1654 */     Boolean chomping = null;
/* 1655 */     int increment = -1;
/* 1656 */     int c = this.reader.peek();
/* 1657 */     if (c == 45 || c == 43) {
/* 1658 */       if (c == 43) {
/* 1659 */         chomping = Boolean.TRUE;
/*      */       } else {
/* 1661 */         chomping = Boolean.FALSE;
/*      */       } 
/* 1663 */       this.reader.forward();
/* 1664 */       c = this.reader.peek();
/* 1665 */       if (Character.isDigit(c)) {
/* 1666 */         String s = String.valueOf(Character.toChars(c));
/* 1667 */         increment = Integer.parseInt(s);
/* 1668 */         if (increment == 0) {
/* 1669 */           throw new ScannerException("while scanning a block scalar", startMark, "expected indentation indicator in the range 1-9, but found 0", this.reader.getMark());
/*      */         }
/*      */ 
/*      */         
/* 1673 */         this.reader.forward();
/*      */       } 
/* 1675 */     } else if (Character.isDigit(c)) {
/* 1676 */       String s = String.valueOf(Character.toChars(c));
/* 1677 */       increment = Integer.parseInt(s);
/* 1678 */       if (increment == 0) {
/* 1679 */         throw new ScannerException("while scanning a block scalar", startMark, "expected indentation indicator in the range 1-9, but found 0", this.reader.getMark());
/*      */       }
/*      */ 
/*      */       
/* 1683 */       this.reader.forward();
/* 1684 */       c = this.reader.peek();
/* 1685 */       if (c == 45 || c == 43) {
/* 1686 */         if (c == 43) {
/* 1687 */           chomping = Boolean.TRUE;
/*      */         } else {
/* 1689 */           chomping = Boolean.FALSE;
/*      */         } 
/* 1691 */         this.reader.forward();
/*      */       } 
/*      */     } 
/* 1694 */     c = this.reader.peek();
/* 1695 */     if (Constant.NULL_BL_LINEBR.hasNo(c)) {
/* 1696 */       String s = String.valueOf(Character.toChars(c));
/* 1697 */       throw new ScannerException("while scanning a block scalar", startMark, "expected chomping or indentation indicators, but found " + s + "(" + c + ")", this.reader.getMark());
/*      */     } 
/*      */ 
/*      */     
/* 1701 */     return new Chomping(chomping, increment);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private String scanBlockScalarIgnoredLine(Mark startMark) {
/* 1712 */     while (this.reader.peek() == 32) {
/* 1713 */       this.reader.forward();
/*      */     }
/*      */ 
/*      */     
/* 1717 */     if (this.reader.peek() == 35) {
/* 1718 */       while (Constant.NULL_OR_LINEBR.hasNo(this.reader.peek())) {
/* 1719 */         this.reader.forward();
/*      */       }
/*      */     }
/*      */ 
/*      */     
/* 1724 */     int c = this.reader.peek();
/* 1725 */     String lineBreak = scanLineBreak();
/* 1726 */     if (lineBreak.length() == 0 && c != 0) {
/* 1727 */       String s = String.valueOf(Character.toChars(c));
/* 1728 */       throw new ScannerException("while scanning a block scalar", startMark, "expected a comment or a line break, but found " + s + "(" + c + ")", this.reader.getMark());
/*      */     } 
/*      */ 
/*      */     
/* 1732 */     return lineBreak;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private Object[] scanBlockScalarIndentation() {
/* 1744 */     StringBuilder chunks = new StringBuilder();
/* 1745 */     int maxIndent = 0;
/* 1746 */     Mark endMark = this.reader.getMark();
/*      */ 
/*      */ 
/*      */     
/* 1750 */     while (Constant.LINEBR.has(this.reader.peek(), " \r")) {
/* 1751 */       if (this.reader.peek() != 32) {
/*      */ 
/*      */         
/* 1754 */         chunks.append(scanLineBreak());
/* 1755 */         endMark = this.reader.getMark();
/*      */         
/*      */         continue;
/*      */       } 
/*      */       
/* 1760 */       this.reader.forward();
/* 1761 */       if (this.reader.getColumn() > maxIndent) {
/* 1762 */         maxIndent = this.reader.getColumn();
/*      */       }
/*      */     } 
/*      */ 
/*      */     
/* 1767 */     return new Object[] { chunks.toString(), Integer.valueOf(maxIndent), endMark };
/*      */   }
/*      */ 
/*      */   
/*      */   private Object[] scanBlockScalarBreaks(int indent) {
/* 1772 */     StringBuilder chunks = new StringBuilder();
/* 1773 */     Mark endMark = this.reader.getMark();
/* 1774 */     int col = this.reader.getColumn();
/*      */ 
/*      */     
/* 1777 */     while (col < indent && this.reader.peek() == 32) {
/* 1778 */       this.reader.forward();
/* 1779 */       col++;
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/* 1784 */     String lineBreak = null;
/* 1785 */     while ((lineBreak = scanLineBreak()).length() != 0) {
/* 1786 */       chunks.append(lineBreak);
/* 1787 */       endMark = this.reader.getMark();
/*      */ 
/*      */       
/* 1790 */       col = this.reader.getColumn();
/* 1791 */       while (col < indent && this.reader.peek() == 32) {
/* 1792 */         this.reader.forward();
/* 1793 */         col++;
/*      */       } 
/*      */     } 
/*      */     
/* 1797 */     return new Object[] { chunks.toString(), endMark };
/*      */   }
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
/*      */   private Token scanFlowScalar(char style) {
/*      */     boolean _double;
/* 1820 */     if (style == '"') {
/* 1821 */       _double = true;
/*      */     } else {
/* 1823 */       _double = false;
/*      */     } 
/* 1825 */     StringBuilder chunks = new StringBuilder();
/* 1826 */     Mark startMark = this.reader.getMark();
/* 1827 */     int quote = this.reader.peek();
/* 1828 */     this.reader.forward();
/* 1829 */     chunks.append(scanFlowScalarNonSpaces(_double, startMark));
/* 1830 */     while (this.reader.peek() != quote) {
/* 1831 */       chunks.append(scanFlowScalarSpaces(startMark));
/* 1832 */       chunks.append(scanFlowScalarNonSpaces(_double, startMark));
/*      */     } 
/* 1834 */     this.reader.forward();
/* 1835 */     Mark endMark = this.reader.getMark();
/* 1836 */     return (Token)new ScalarToken(chunks.toString(), false, startMark, endMark, DumperOptions.ScalarStyle.createStyle(Character.valueOf(style)));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private String scanFlowScalarNonSpaces(boolean doubleQuoted, Mark startMark) {
/* 1844 */     StringBuilder chunks = new StringBuilder();
/*      */ 
/*      */     
/*      */     while (true) {
/* 1848 */       int length = 0;
/* 1849 */       while (Constant.NULL_BL_T_LINEBR.hasNo(this.reader.peek(length), "'\"\\")) {
/* 1850 */         length++;
/*      */       }
/* 1852 */       if (length != 0) {
/* 1853 */         chunks.append(this.reader.prefixForward(length));
/*      */       }
/*      */ 
/*      */       
/* 1857 */       int c = this.reader.peek();
/* 1858 */       if (!doubleQuoted && c == 39 && this.reader.peek(1) == 39) {
/* 1859 */         chunks.append("'");
/* 1860 */         this.reader.forward(2); continue;
/* 1861 */       }  if ((doubleQuoted && c == 39) || (!doubleQuoted && "\"\\".indexOf(c) != -1)) {
/* 1862 */         chunks.appendCodePoint(c);
/* 1863 */         this.reader.forward(); continue;
/* 1864 */       }  if (doubleQuoted && c == 92) {
/* 1865 */         this.reader.forward();
/* 1866 */         c = this.reader.peek();
/* 1867 */         if (!Character.isSupplementaryCodePoint(c) && ESCAPE_REPLACEMENTS.containsKey(Character.valueOf((char)c))) {
/*      */ 
/*      */ 
/*      */           
/* 1871 */           chunks.append(ESCAPE_REPLACEMENTS.get(Character.valueOf((char)c)));
/* 1872 */           this.reader.forward(); continue;
/* 1873 */         }  if (!Character.isSupplementaryCodePoint(c) && ESCAPE_CODES.containsKey(Character.valueOf((char)c))) {
/*      */ 
/*      */           
/* 1876 */           length = ((Integer)ESCAPE_CODES.get(Character.valueOf((char)c))).intValue();
/* 1877 */           this.reader.forward();
/* 1878 */           String hex = this.reader.prefix(length);
/* 1879 */           if (NOT_HEXA.matcher(hex).find()) {
/* 1880 */             throw new ScannerException("while scanning a double-quoted scalar", startMark, "expected escape sequence of " + length + " hexadecimal numbers, but found: " + hex, this.reader.getMark());
/*      */           }
/*      */ 
/*      */ 
/*      */           
/* 1885 */           int decimal = Integer.parseInt(hex, 16);
/* 1886 */           String unicode = new String(Character.toChars(decimal));
/* 1887 */           chunks.append(unicode);
/* 1888 */           this.reader.forward(length); continue;
/* 1889 */         }  if (scanLineBreak().length() != 0) {
/* 1890 */           chunks.append(scanFlowScalarBreaks(startMark)); continue;
/*      */         } 
/* 1892 */         String s = String.valueOf(Character.toChars(c));
/* 1893 */         throw new ScannerException("while scanning a double-quoted scalar", startMark, "found unknown escape character " + s + "(" + c + ")", this.reader.getMark());
/*      */       } 
/*      */       
/*      */       break;
/*      */     } 
/* 1898 */     return chunks.toString();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private String scanFlowScalarSpaces(Mark startMark) {
/* 1905 */     StringBuilder chunks = new StringBuilder();
/* 1906 */     int length = 0;
/*      */ 
/*      */     
/* 1909 */     while (" \t".indexOf(this.reader.peek(length)) != -1) {
/* 1910 */       length++;
/*      */     }
/* 1912 */     String whitespaces = this.reader.prefixForward(length);
/* 1913 */     int c = this.reader.peek();
/* 1914 */     if (c == 0)
/*      */     {
/* 1916 */       throw new ScannerException("while scanning a quoted scalar", startMark, "found unexpected end of stream", this.reader.getMark());
/*      */     }
/*      */ 
/*      */     
/* 1920 */     String lineBreak = scanLineBreak();
/* 1921 */     if (lineBreak.length() != 0) {
/* 1922 */       String breaks = scanFlowScalarBreaks(startMark);
/* 1923 */       if (!"\n".equals(lineBreak)) {
/* 1924 */         chunks.append(lineBreak);
/* 1925 */       } else if (breaks.length() == 0) {
/* 1926 */         chunks.append(" ");
/*      */       } 
/* 1928 */       chunks.append(breaks);
/*      */     } else {
/* 1930 */       chunks.append(whitespaces);
/*      */     } 
/* 1932 */     return chunks.toString();
/*      */   }
/*      */ 
/*      */   
/*      */   private String scanFlowScalarBreaks(Mark startMark) {
/* 1937 */     StringBuilder chunks = new StringBuilder();
/*      */ 
/*      */     
/*      */     while (true) {
/* 1941 */       String prefix = this.reader.prefix(3);
/* 1942 */       if (("---".equals(prefix) || "...".equals(prefix)) && Constant.NULL_BL_T_LINEBR.has(this.reader.peek(3)))
/*      */       {
/* 1944 */         throw new ScannerException("while scanning a quoted scalar", startMark, "found unexpected document separator", this.reader.getMark());
/*      */       }
/*      */ 
/*      */       
/* 1948 */       while (" \t".indexOf(this.reader.peek()) != -1) {
/* 1949 */         this.reader.forward();
/*      */       }
/*      */ 
/*      */       
/* 1953 */       String lineBreak = scanLineBreak();
/* 1954 */       if (lineBreak.length() != 0) {
/* 1955 */         chunks.append(lineBreak); continue;
/*      */       }  break;
/* 1957 */     }  return chunks.toString();
/*      */   }
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
/*      */   private Token scanPlain() {
/* 1974 */     StringBuilder chunks = new StringBuilder();
/* 1975 */     Mark startMark = this.reader.getMark();
/* 1976 */     Mark endMark = startMark;
/* 1977 */     int indent = this.indent + 1;
/* 1978 */     String spaces = "";
/*      */     
/*      */     do {
/* 1981 */       int length = 0;
/*      */       
/* 1983 */       if (this.reader.peek() == 35) {
/*      */         break;
/*      */       }
/*      */       while (true) {
/* 1987 */         int c = this.reader.peek(length);
/* 1988 */         if (Constant.NULL_BL_T_LINEBR.has(c) || (c == 58 && Constant.NULL_BL_T_LINEBR.has(this.reader.peek(length + 1), (this.flowLevel != 0) ? ",[]{}" : "")) || (this.flowLevel != 0 && ",?[]{}".indexOf(c) != -1)) {
/*      */           break;
/*      */         }
/*      */ 
/*      */         
/* 1993 */         length++;
/*      */       } 
/* 1995 */       if (length == 0) {
/*      */         break;
/*      */       }
/* 1998 */       this.allowSimpleKey = false;
/* 1999 */       chunks.append(spaces);
/* 2000 */       chunks.append(this.reader.prefixForward(length));
/* 2001 */       endMark = this.reader.getMark();
/* 2002 */       spaces = scanPlainSpaces();
/*      */     }
/* 2004 */     while (spaces.length() != 0 && this.reader.peek() != 35 && (this.flowLevel != 0 || this.reader.getColumn() >= indent));
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2009 */     return (Token)new ScalarToken(chunks.toString(), startMark, endMark, true);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private String scanPlainSpaces() {
/* 2017 */     int length = 0;
/* 2018 */     while (this.reader.peek(length) == 32 || this.reader.peek(length) == 9) {
/* 2019 */       length++;
/*      */     }
/* 2021 */     String whitespaces = this.reader.prefixForward(length);
/* 2022 */     String lineBreak = scanLineBreak();
/* 2023 */     if (lineBreak.length() != 0) {
/* 2024 */       this.allowSimpleKey = true;
/* 2025 */       String prefix = this.reader.prefix(3);
/* 2026 */       if ("---".equals(prefix) || ("...".equals(prefix) && Constant.NULL_BL_T_LINEBR.has(this.reader.peek(3))))
/*      */       {
/* 2028 */         return "";
/*      */       }
/* 2030 */       StringBuilder breaks = new StringBuilder();
/*      */       while (true) {
/* 2032 */         while (this.reader.peek() == 32) {
/* 2033 */           this.reader.forward();
/*      */         }
/* 2035 */         String lb = scanLineBreak();
/* 2036 */         if (lb.length() != 0) {
/* 2037 */           breaks.append(lb);
/* 2038 */           prefix = this.reader.prefix(3);
/* 2039 */           if ("---".equals(prefix) || ("...".equals(prefix) && Constant.NULL_BL_T_LINEBR.has(this.reader.peek(3))))
/*      */           {
/* 2041 */             return "";
/*      */           }
/*      */           
/*      */           continue;
/*      */         } 
/*      */         break;
/*      */       } 
/* 2048 */       if (!"\n".equals(lineBreak))
/* 2049 */         return lineBreak + breaks; 
/* 2050 */       if (breaks.length() == 0) {
/* 2051 */         return " ";
/*      */       }
/* 2053 */       return breaks.toString();
/*      */     } 
/* 2055 */     return whitespaces;
/*      */   }
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
/*      */   private String scanTagHandle(String name, Mark startMark) {
/* 2081 */     int c = this.reader.peek();
/* 2082 */     if (c != 33) {
/* 2083 */       String s = String.valueOf(Character.toChars(c));
/* 2084 */       throw new ScannerException("while scanning a " + name, startMark, "expected '!', but found " + s + "(" + c + ")", this.reader.getMark());
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2090 */     int length = 1;
/* 2091 */     c = this.reader.peek(length);
/* 2092 */     if (c != 32) {
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 2097 */       while (Constant.ALPHA.has(c)) {
/* 2098 */         length++;
/* 2099 */         c = this.reader.peek(length);
/*      */       } 
/*      */ 
/*      */ 
/*      */       
/* 2104 */       if (c != 33) {
/* 2105 */         this.reader.forward(length);
/* 2106 */         String s = String.valueOf(Character.toChars(c));
/* 2107 */         throw new ScannerException("while scanning a " + name, startMark, "expected '!', but found " + s + "(" + c + ")", this.reader.getMark());
/*      */       } 
/*      */       
/* 2110 */       length++;
/*      */     } 
/* 2112 */     String value = this.reader.prefixForward(length);
/* 2113 */     return value;
/*      */   }
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
/*      */   private String scanTagUri(String name, Mark startMark) {
/* 2134 */     StringBuilder chunks = new StringBuilder();
/*      */ 
/*      */ 
/*      */     
/* 2138 */     int length = 0;
/* 2139 */     int c = this.reader.peek(length);
/* 2140 */     while (Constant.URI_CHARS.has(c)) {
/* 2141 */       if (c == 37) {
/* 2142 */         chunks.append(this.reader.prefixForward(length));
/* 2143 */         length = 0;
/* 2144 */         chunks.append(scanUriEscapes(name, startMark));
/*      */       } else {
/* 2146 */         length++;
/*      */       } 
/* 2148 */       c = this.reader.peek(length);
/*      */     } 
/*      */ 
/*      */     
/* 2152 */     if (length != 0) {
/* 2153 */       chunks.append(this.reader.prefixForward(length));
/*      */     }
/* 2155 */     if (chunks.length() == 0) {
/*      */       
/* 2157 */       String s = String.valueOf(Character.toChars(c));
/* 2158 */       throw new ScannerException("while scanning a " + name, startMark, "expected URI, but found " + s + "(" + c + ")", this.reader.getMark());
/*      */     } 
/*      */     
/* 2161 */     return chunks.toString();
/*      */   }
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
/*      */   private String scanUriEscapes(String name, Mark startMark) {
/* 2178 */     int length = 1;
/* 2179 */     while (this.reader.peek(length * 3) == 37) {
/* 2180 */       length++;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2186 */     Mark beginningMark = this.reader.getMark();
/* 2187 */     ByteBuffer buff = ByteBuffer.allocate(length);
/* 2188 */     while (this.reader.peek() == 37) {
/* 2189 */       this.reader.forward();
/*      */       try {
/* 2191 */         byte code = (byte)Integer.parseInt(this.reader.prefix(2), 16);
/* 2192 */         buff.put(code);
/* 2193 */       } catch (NumberFormatException nfe) {
/* 2194 */         int c1 = this.reader.peek();
/* 2195 */         String s1 = String.valueOf(Character.toChars(c1));
/* 2196 */         int c2 = this.reader.peek(1);
/* 2197 */         String s2 = String.valueOf(Character.toChars(c2));
/* 2198 */         throw new ScannerException("while scanning a " + name, startMark, "expected URI escape sequence of 2 hexadecimal numbers, but found " + s1 + "(" + c1 + ") and " + s2 + "(" + c2 + ")", this.reader.getMark());
/*      */       } 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 2204 */       this.reader.forward(2);
/*      */     } 
/* 2206 */     buff.flip();
/*      */     try {
/* 2208 */       return UriEncoder.decode(buff);
/* 2209 */     } catch (CharacterCodingException e) {
/* 2210 */       throw new ScannerException("while scanning a " + name, startMark, "expected URI in UTF-8: " + e.getMessage(), beginningMark);
/*      */     } 
/*      */   }
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
/*      */   private String scanLineBreak() {
/* 2233 */     int c = this.reader.peek();
/* 2234 */     if (c == 13 || c == 10 || c == 133) {
/* 2235 */       if (c == 13 && 10 == this.reader.peek(1)) {
/* 2236 */         this.reader.forward(2);
/*      */       } else {
/* 2238 */         this.reader.forward();
/*      */       } 
/* 2240 */       return "\n";
/* 2241 */     }  if (c == 8232 || c == 8233) {
/* 2242 */       this.reader.forward();
/* 2243 */       return String.valueOf(Character.toChars(c));
/*      */     } 
/* 2245 */     return "";
/*      */   }
/*      */ 
/*      */   
/*      */   private static class Chomping
/*      */   {
/*      */     private final Boolean value;
/*      */     
/*      */     private final int increment;
/*      */     
/*      */     public Chomping(Boolean value, int increment) {
/* 2256 */       this.value = value;
/* 2257 */       this.increment = increment;
/*      */     }
/*      */     
/*      */     public boolean chompTailIsNotFalse() {
/* 2261 */       return (this.value == null || this.value.booleanValue());
/*      */     }
/*      */     
/*      */     public boolean chompTailIsTrue() {
/* 2265 */       return (this.value != null && this.value.booleanValue());
/*      */     }
/*      */     
/*      */     public int getIncrement() {
/* 2269 */       return this.increment;
/*      */     }
/*      */   }
/*      */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\yaml\snakeyaml\scanner\ScannerImpl.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */