/*     */ package org.yaml.snakeyaml.parser;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import org.yaml.snakeyaml.DumperOptions;
/*     */ import org.yaml.snakeyaml.error.Mark;
/*     */ import org.yaml.snakeyaml.error.YAMLException;
/*     */ import org.yaml.snakeyaml.events.AliasEvent;
/*     */ import org.yaml.snakeyaml.events.DocumentEndEvent;
/*     */ import org.yaml.snakeyaml.events.DocumentStartEvent;
/*     */ import org.yaml.snakeyaml.events.Event;
/*     */ import org.yaml.snakeyaml.events.ImplicitTuple;
/*     */ import org.yaml.snakeyaml.events.MappingEndEvent;
/*     */ import org.yaml.snakeyaml.events.MappingStartEvent;
/*     */ import org.yaml.snakeyaml.events.ScalarEvent;
/*     */ import org.yaml.snakeyaml.events.SequenceEndEvent;
/*     */ import org.yaml.snakeyaml.events.SequenceStartEvent;
/*     */ import org.yaml.snakeyaml.events.StreamEndEvent;
/*     */ import org.yaml.snakeyaml.events.StreamStartEvent;
/*     */ import org.yaml.snakeyaml.reader.StreamReader;
/*     */ import org.yaml.snakeyaml.scanner.Scanner;
/*     */ import org.yaml.snakeyaml.scanner.ScannerImpl;
/*     */ import org.yaml.snakeyaml.tokens.AliasToken;
/*     */ import org.yaml.snakeyaml.tokens.AnchorToken;
/*     */ import org.yaml.snakeyaml.tokens.BlockEntryToken;
/*     */ import org.yaml.snakeyaml.tokens.DirectiveToken;
/*     */ import org.yaml.snakeyaml.tokens.ScalarToken;
/*     */ import org.yaml.snakeyaml.tokens.StreamEndToken;
/*     */ import org.yaml.snakeyaml.tokens.StreamStartToken;
/*     */ import org.yaml.snakeyaml.tokens.TagToken;
/*     */ import org.yaml.snakeyaml.tokens.TagTuple;
/*     */ import org.yaml.snakeyaml.tokens.Token;
/*     */ import org.yaml.snakeyaml.util.ArrayStack;
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
/*     */ public class ParserImpl
/*     */   implements Parser
/*     */ {
/* 118 */   private static final Map<String, String> DEFAULT_TAGS = new HashMap<>();
/*     */   static {
/* 120 */     DEFAULT_TAGS.put("!", "!");
/* 121 */     DEFAULT_TAGS.put("!!", "tag:yaml.org,2002:");
/*     */   }
/*     */   
/*     */   protected final Scanner scanner;
/*     */   private Event currentEvent;
/*     */   private final ArrayStack<Production> states;
/*     */   private final ArrayStack<Mark> marks;
/*     */   private Production state;
/*     */   private VersionTagsTuple directives;
/*     */   
/*     */   public ParserImpl(StreamReader reader) {
/* 132 */     this((Scanner)new ScannerImpl(reader));
/*     */   }
/*     */   
/*     */   public ParserImpl(Scanner scanner) {
/* 136 */     this.scanner = scanner;
/* 137 */     this.currentEvent = null;
/* 138 */     this.directives = new VersionTagsTuple(null, new HashMap<>(DEFAULT_TAGS));
/* 139 */     this.states = new ArrayStack(100);
/* 140 */     this.marks = new ArrayStack(10);
/* 141 */     this.state = new ParseStreamStart();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean checkEvent(Event.ID choice) {
/* 148 */     peekEvent();
/* 149 */     return (this.currentEvent != null && this.currentEvent.is(choice));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Event peekEvent() {
/* 156 */     if (this.currentEvent == null && 
/* 157 */       this.state != null) {
/* 158 */       this.currentEvent = this.state.produce();
/*     */     }
/*     */     
/* 161 */     return this.currentEvent;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Event getEvent() {
/* 168 */     peekEvent();
/* 169 */     Event value = this.currentEvent;
/* 170 */     this.currentEvent = null;
/* 171 */     return value;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private class ParseStreamStart
/*     */     implements Production
/*     */   {
/*     */     private ParseStreamStart() {}
/*     */ 
/*     */ 
/*     */     
/*     */     public Event produce() {
/* 184 */       StreamStartToken token = (StreamStartToken)ParserImpl.this.scanner.getToken();
/* 185 */       StreamStartEvent streamStartEvent = new StreamStartEvent(token.getStartMark(), token.getEndMark());
/*     */       
/* 187 */       ParserImpl.this.state = new ParserImpl.ParseImplicitDocumentStart();
/* 188 */       return (Event)streamStartEvent;
/*     */     }
/*     */   }
/*     */   
/*     */   private class ParseImplicitDocumentStart implements Production { private ParseImplicitDocumentStart() {}
/*     */     
/*     */     public Event produce() {
/* 195 */       if (!ParserImpl.this.scanner.checkToken(new Token.ID[] { Token.ID.Directive, Token.ID.DocumentStart, Token.ID.StreamEnd })) {
/* 196 */         ParserImpl.this.directives = new VersionTagsTuple(null, ParserImpl.DEFAULT_TAGS);
/* 197 */         Token token = ParserImpl.this.scanner.peekToken();
/* 198 */         Mark startMark = token.getStartMark();
/* 199 */         Mark endMark = startMark;
/* 200 */         DocumentStartEvent documentStartEvent = new DocumentStartEvent(startMark, endMark, false, null, null);
/*     */         
/* 202 */         ParserImpl.this.states.push(new ParserImpl.ParseDocumentEnd());
/* 203 */         ParserImpl.this.state = new ParserImpl.ParseBlockNode();
/* 204 */         return (Event)documentStartEvent;
/*     */       } 
/* 206 */       Production p = new ParserImpl.ParseDocumentStart();
/* 207 */       return p.produce();
/*     */     } }
/*     */   
/*     */   private class ParseDocumentStart implements Production {
/*     */     private ParseDocumentStart() {}
/*     */     
/*     */     public Event produce() {
/*     */       StreamEndEvent streamEndEvent;
/* 215 */       while (ParserImpl.this.scanner.checkToken(new Token.ID[] { Token.ID.DocumentEnd })) {
/* 216 */         ParserImpl.this.scanner.getToken();
/*     */       }
/*     */ 
/*     */       
/* 220 */       if (!ParserImpl.this.scanner.checkToken(new Token.ID[] { Token.ID.StreamEnd })) {
/* 221 */         Token token = ParserImpl.this.scanner.peekToken();
/* 222 */         Mark startMark = token.getStartMark();
/* 223 */         VersionTagsTuple tuple = ParserImpl.this.processDirectives();
/* 224 */         if (!ParserImpl.this.scanner.checkToken(new Token.ID[] { Token.ID.DocumentStart })) {
/* 225 */           throw new ParserException(null, null, "expected '<document start>', but found '" + ParserImpl.this.scanner.peekToken().getTokenId() + "'", ParserImpl.this.scanner.peekToken().getStartMark());
/*     */         }
/*     */         
/* 228 */         token = ParserImpl.this.scanner.getToken();
/* 229 */         Mark endMark = token.getEndMark();
/* 230 */         DocumentStartEvent documentStartEvent = new DocumentStartEvent(startMark, endMark, true, tuple.getVersion(), tuple.getTags());
/*     */         
/* 232 */         ParserImpl.this.states.push(new ParserImpl.ParseDocumentEnd());
/* 233 */         ParserImpl.this.state = new ParserImpl.ParseDocumentContent();
/*     */       } else {
/*     */         
/* 236 */         StreamEndToken token = (StreamEndToken)ParserImpl.this.scanner.getToken();
/* 237 */         streamEndEvent = new StreamEndEvent(token.getStartMark(), token.getEndMark());
/* 238 */         if (!ParserImpl.this.states.isEmpty()) {
/* 239 */           throw new YAMLException("Unexpected end of stream. States left: " + ParserImpl.this.states);
/*     */         }
/* 241 */         if (!ParserImpl.this.marks.isEmpty()) {
/* 242 */           throw new YAMLException("Unexpected end of stream. Marks left: " + ParserImpl.this.marks);
/*     */         }
/* 244 */         ParserImpl.this.state = null;
/*     */       } 
/* 246 */       return (Event)streamEndEvent;
/*     */     }
/*     */   }
/*     */   
/*     */   private class ParseDocumentEnd implements Production { private ParseDocumentEnd() {}
/*     */     
/*     */     public Event produce() {
/* 253 */       Token token = ParserImpl.this.scanner.peekToken();
/* 254 */       Mark startMark = token.getStartMark();
/* 255 */       Mark endMark = startMark;
/* 256 */       boolean explicit = false;
/* 257 */       if (ParserImpl.this.scanner.checkToken(new Token.ID[] { Token.ID.DocumentEnd })) {
/* 258 */         token = ParserImpl.this.scanner.getToken();
/* 259 */         endMark = token.getEndMark();
/* 260 */         explicit = true;
/*     */       } 
/* 262 */       DocumentEndEvent documentEndEvent = new DocumentEndEvent(startMark, endMark, explicit);
/*     */       
/* 264 */       ParserImpl.this.state = new ParserImpl.ParseDocumentStart();
/* 265 */       return (Event)documentEndEvent;
/*     */     } }
/*     */   
/*     */   private class ParseDocumentContent implements Production {
/*     */     private ParseDocumentContent() {}
/*     */     
/*     */     public Event produce() {
/* 272 */       if (ParserImpl.this.scanner.checkToken(new Token.ID[] { Token.ID.Directive, Token.ID.DocumentStart, Token.ID.DocumentEnd, Token.ID.StreamEnd })) {
/*     */         
/* 274 */         Event event = ParserImpl.this.processEmptyScalar(ParserImpl.this.scanner.peekToken().getStartMark());
/* 275 */         ParserImpl.this.state = (Production)ParserImpl.this.states.pop();
/* 276 */         return event;
/*     */       } 
/* 278 */       Production p = new ParserImpl.ParseBlockNode();
/* 279 */       return p.produce();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private VersionTagsTuple processDirectives() {
/* 286 */     DumperOptions.Version yamlVersion = null;
/* 287 */     HashMap<String, String> tagHandles = new HashMap<>();
/* 288 */     while (this.scanner.checkToken(new Token.ID[] { Token.ID.Directive })) {
/*     */       
/* 290 */       DirectiveToken token = (DirectiveToken)this.scanner.getToken();
/* 291 */       if (token.getName().equals("YAML")) {
/* 292 */         if (yamlVersion != null) {
/* 293 */           throw new ParserException(null, null, "found duplicate YAML directive", token.getStartMark());
/*     */         }
/*     */         
/* 296 */         List<Integer> value = token.getValue();
/* 297 */         Integer major = value.get(0);
/* 298 */         if (major.intValue() != 1) {
/* 299 */           throw new ParserException(null, null, "found incompatible YAML document (version 1.* is required)", token.getStartMark());
/*     */         }
/*     */ 
/*     */         
/* 303 */         Integer minor = value.get(1);
/* 304 */         switch (minor.intValue()) {
/*     */           case 0:
/* 306 */             yamlVersion = DumperOptions.Version.V1_0;
/*     */             continue;
/*     */         } 
/*     */         
/* 310 */         yamlVersion = DumperOptions.Version.V1_1;
/*     */         continue;
/*     */       } 
/* 313 */       if (token.getName().equals("TAG")) {
/* 314 */         List<String> value = token.getValue();
/* 315 */         String handle = value.get(0);
/* 316 */         String prefix = value.get(1);
/* 317 */         if (tagHandles.containsKey(handle)) {
/* 318 */           throw new ParserException(null, null, "duplicate tag handle " + handle, token.getStartMark());
/*     */         }
/*     */         
/* 321 */         tagHandles.put(handle, prefix);
/*     */       } 
/*     */     } 
/* 324 */     if (yamlVersion != null || !tagHandles.isEmpty()) {
/*     */       
/* 326 */       for (String key : DEFAULT_TAGS.keySet()) {
/*     */         
/* 328 */         if (!tagHandles.containsKey(key)) {
/* 329 */           tagHandles.put(key, DEFAULT_TAGS.get(key));
/*     */         }
/*     */       } 
/* 332 */       this.directives = new VersionTagsTuple(yamlVersion, tagHandles);
/*     */     } 
/* 334 */     return this.directives;
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
/*     */   private class ParseBlockNode
/*     */     implements Production
/*     */   {
/*     */     private ParseBlockNode() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Event produce() {
/* 359 */       return ParserImpl.this.parseNode(true, false);
/*     */     }
/*     */   }
/*     */   
/*     */   private Event parseFlowNode() {
/* 364 */     return parseNode(false, false);
/*     */   }
/*     */   
/*     */   private Event parseBlockNodeOrIndentlessSequence() {
/* 368 */     return parseNode(true, true);
/*     */   }
/*     */   
/*     */   private Event parseNode(boolean block, boolean indentlessSequence) {
/*     */     ScalarEvent scalarEvent;
/* 373 */     Mark startMark = null;
/* 374 */     Mark endMark = null;
/* 375 */     Mark tagMark = null;
/* 376 */     if (this.scanner.checkToken(new Token.ID[] { Token.ID.Alias })) {
/* 377 */       AliasToken token = (AliasToken)this.scanner.getToken();
/* 378 */       AliasEvent aliasEvent = new AliasEvent(token.getValue(), token.getStartMark(), token.getEndMark());
/* 379 */       this.state = (Production)this.states.pop();
/*     */     } else {
/* 381 */       String anchor = null;
/* 382 */       TagTuple tagTokenTag = null;
/* 383 */       if (this.scanner.checkToken(new Token.ID[] { Token.ID.Anchor })) {
/* 384 */         AnchorToken token = (AnchorToken)this.scanner.getToken();
/* 385 */         startMark = token.getStartMark();
/* 386 */         endMark = token.getEndMark();
/* 387 */         anchor = token.getValue();
/* 388 */         if (this.scanner.checkToken(new Token.ID[] { Token.ID.Tag })) {
/* 389 */           TagToken tagToken = (TagToken)this.scanner.getToken();
/* 390 */           tagMark = tagToken.getStartMark();
/* 391 */           endMark = tagToken.getEndMark();
/* 392 */           tagTokenTag = tagToken.getValue();
/*     */         } 
/*     */       } else {
/* 395 */         TagToken tagToken = (TagToken)this.scanner.getToken();
/* 396 */         startMark = tagToken.getStartMark();
/* 397 */         tagMark = startMark;
/* 398 */         endMark = tagToken.getEndMark();
/* 399 */         tagTokenTag = tagToken.getValue();
/* 400 */         if (this.scanner.checkToken(new Token.ID[] { Token.ID.Tag }) && this.scanner.checkToken(new Token.ID[] { Token.ID.Anchor })) {
/* 401 */           AnchorToken token = (AnchorToken)this.scanner.getToken();
/* 402 */           endMark = token.getEndMark();
/* 403 */           anchor = token.getValue();
/*     */         } 
/*     */       } 
/* 406 */       String tag = null;
/* 407 */       if (tagTokenTag != null) {
/* 408 */         String handle = tagTokenTag.getHandle();
/* 409 */         String suffix = tagTokenTag.getSuffix();
/* 410 */         if (handle != null) {
/* 411 */           if (!this.directives.getTags().containsKey(handle)) {
/* 412 */             throw new ParserException("while parsing a node", startMark, "found undefined tag handle " + handle, tagMark);
/*     */           }
/*     */           
/* 415 */           tag = (String)this.directives.getTags().get(handle) + suffix;
/*     */         } else {
/* 417 */           tag = suffix;
/*     */         } 
/*     */       } 
/* 420 */       if (startMark == null) {
/* 421 */         startMark = this.scanner.peekToken().getStartMark();
/* 422 */         endMark = startMark;
/*     */       } 
/* 424 */       Event event = null;
/* 425 */       boolean implicit = (tag == null || tag.equals("!"));
/* 426 */       if (indentlessSequence && this.scanner.checkToken(new Token.ID[] { Token.ID.BlockEntry })) {
/* 427 */         endMark = this.scanner.peekToken().getEndMark();
/* 428 */         SequenceStartEvent sequenceStartEvent = new SequenceStartEvent(anchor, tag, implicit, startMark, endMark, DumperOptions.FlowStyle.BLOCK);
/*     */         
/* 430 */         this.state = new ParseIndentlessSequenceEntry();
/*     */       }
/* 432 */       else if (this.scanner.checkToken(new Token.ID[] { Token.ID.Scalar })) {
/* 433 */         ImplicitTuple implicitValues; ScalarToken token = (ScalarToken)this.scanner.getToken();
/* 434 */         endMark = token.getEndMark();
/*     */         
/* 436 */         if ((token.getPlain() && tag == null) || "!".equals(tag)) {
/* 437 */           implicitValues = new ImplicitTuple(true, false);
/* 438 */         } else if (tag == null) {
/* 439 */           implicitValues = new ImplicitTuple(false, true);
/*     */         } else {
/* 441 */           implicitValues = new ImplicitTuple(false, false);
/*     */         } 
/* 443 */         scalarEvent = new ScalarEvent(anchor, tag, implicitValues, token.getValue(), startMark, endMark, token.getStyle());
/*     */         
/* 445 */         this.state = (Production)this.states.pop();
/* 446 */       } else if (this.scanner.checkToken(new Token.ID[] { Token.ID.FlowSequenceStart })) {
/* 447 */         endMark = this.scanner.peekToken().getEndMark();
/* 448 */         SequenceStartEvent sequenceStartEvent = new SequenceStartEvent(anchor, tag, implicit, startMark, endMark, DumperOptions.FlowStyle.FLOW);
/*     */         
/* 450 */         this.state = new ParseFlowSequenceFirstEntry();
/* 451 */       } else if (this.scanner.checkToken(new Token.ID[] { Token.ID.FlowMappingStart })) {
/* 452 */         endMark = this.scanner.peekToken().getEndMark();
/* 453 */         MappingStartEvent mappingStartEvent = new MappingStartEvent(anchor, tag, implicit, startMark, endMark, DumperOptions.FlowStyle.FLOW);
/*     */         
/* 455 */         this.state = new ParseFlowMappingFirstKey();
/* 456 */       } else if (block && this.scanner.checkToken(new Token.ID[] { Token.ID.BlockSequenceStart })) {
/* 457 */         endMark = this.scanner.peekToken().getStartMark();
/* 458 */         SequenceStartEvent sequenceStartEvent = new SequenceStartEvent(anchor, tag, implicit, startMark, endMark, DumperOptions.FlowStyle.BLOCK);
/*     */         
/* 460 */         this.state = new ParseBlockSequenceFirstEntry();
/* 461 */       } else if (block && this.scanner.checkToken(new Token.ID[] { Token.ID.BlockMappingStart })) {
/* 462 */         endMark = this.scanner.peekToken().getStartMark();
/* 463 */         MappingStartEvent mappingStartEvent = new MappingStartEvent(anchor, tag, implicit, startMark, endMark, DumperOptions.FlowStyle.BLOCK);
/*     */         
/* 465 */         this.state = new ParseBlockMappingFirstKey();
/* 466 */       } else if (anchor != null || tag != null) {
/*     */ 
/*     */         
/* 469 */         scalarEvent = new ScalarEvent(anchor, tag, new ImplicitTuple(implicit, false), "", startMark, endMark, DumperOptions.ScalarStyle.PLAIN);
/*     */         
/* 471 */         this.state = (Production)this.states.pop();
/*     */       } else {
/*     */         String node;
/* 474 */         if (block) {
/* 475 */           node = "block";
/*     */         } else {
/* 477 */           node = "flow";
/*     */         } 
/* 479 */         Token token = this.scanner.peekToken();
/* 480 */         throw new ParserException("while parsing a " + node + " node", startMark, "expected the node content, but found '" + token.getTokenId() + "'", token.getStartMark());
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 486 */     return (Event)scalarEvent;
/*     */   }
/*     */   
/*     */   private class ParseBlockSequenceFirstEntry
/*     */     implements Production {
/*     */     private ParseBlockSequenceFirstEntry() {}
/*     */     
/*     */     public Event produce() {
/* 494 */       Token token = ParserImpl.this.scanner.getToken();
/* 495 */       ParserImpl.this.marks.push(token.getStartMark());
/* 496 */       return (new ParserImpl.ParseBlockSequenceEntry()).produce();
/*     */     }
/*     */   }
/*     */   
/*     */   private class ParseBlockSequenceEntry implements Production {
/*     */     public Event produce() {
/* 502 */       if (ParserImpl.this.scanner.checkToken(new Token.ID[] { Token.ID.BlockEntry })) {
/* 503 */         BlockEntryToken blockEntryToken = (BlockEntryToken)ParserImpl.this.scanner.getToken();
/* 504 */         if (!ParserImpl.this.scanner.checkToken(new Token.ID[] { Token.ID.BlockEntry, Token.ID.BlockEnd })) {
/* 505 */           ParserImpl.this.states.push(new ParseBlockSequenceEntry());
/* 506 */           return (new ParserImpl.ParseBlockNode()).produce();
/*     */         } 
/* 508 */         ParserImpl.this.state = new ParseBlockSequenceEntry();
/* 509 */         return ParserImpl.this.processEmptyScalar(blockEntryToken.getEndMark());
/*     */       } 
/*     */       
/* 512 */       if (!ParserImpl.this.scanner.checkToken(new Token.ID[] { Token.ID.BlockEnd })) {
/* 513 */         Token token1 = ParserImpl.this.scanner.peekToken();
/* 514 */         throw new ParserException("while parsing a block collection", (Mark)ParserImpl.this.marks.pop(), "expected <block end>, but found '" + token1.getTokenId() + "'", token1.getStartMark());
/*     */       } 
/*     */ 
/*     */       
/* 518 */       Token token = ParserImpl.this.scanner.getToken();
/* 519 */       SequenceEndEvent sequenceEndEvent = new SequenceEndEvent(token.getStartMark(), token.getEndMark());
/* 520 */       ParserImpl.this.state = (Production)ParserImpl.this.states.pop();
/* 521 */       ParserImpl.this.marks.pop();
/* 522 */       return (Event)sequenceEndEvent;
/*     */     }
/*     */     
/*     */     private ParseBlockSequenceEntry() {} }
/*     */   
/*     */   private class ParseIndentlessSequenceEntry implements Production { private ParseIndentlessSequenceEntry() {}
/*     */     
/*     */     public Event produce() {
/* 530 */       if (ParserImpl.this.scanner.checkToken(new Token.ID[] { Token.ID.BlockEntry })) {
/* 531 */         Token token1 = ParserImpl.this.scanner.getToken();
/* 532 */         if (!ParserImpl.this.scanner.checkToken(new Token.ID[] { Token.ID.BlockEntry, Token.ID.Key, Token.ID.Value, Token.ID.BlockEnd })) {
/*     */           
/* 534 */           ParserImpl.this.states.push(new ParseIndentlessSequenceEntry());
/* 535 */           return (new ParserImpl.ParseBlockNode()).produce();
/*     */         } 
/* 537 */         ParserImpl.this.state = new ParseIndentlessSequenceEntry();
/* 538 */         return ParserImpl.this.processEmptyScalar(token1.getEndMark());
/*     */       } 
/*     */       
/* 541 */       Token token = ParserImpl.this.scanner.peekToken();
/* 542 */       SequenceEndEvent sequenceEndEvent = new SequenceEndEvent(token.getStartMark(), token.getEndMark());
/* 543 */       ParserImpl.this.state = (Production)ParserImpl.this.states.pop();
/* 544 */       return (Event)sequenceEndEvent;
/*     */     } }
/*     */   
/*     */   private class ParseBlockMappingFirstKey implements Production { private ParseBlockMappingFirstKey() {}
/*     */     
/*     */     public Event produce() {
/* 550 */       Token token = ParserImpl.this.scanner.getToken();
/* 551 */       ParserImpl.this.marks.push(token.getStartMark());
/* 552 */       return (new ParserImpl.ParseBlockMappingKey()).produce();
/*     */     } }
/*     */   
/*     */   private class ParseBlockMappingKey implements Production { private ParseBlockMappingKey() {}
/*     */     
/*     */     public Event produce() {
/* 558 */       if (ParserImpl.this.scanner.checkToken(new Token.ID[] { Token.ID.Key })) {
/* 559 */         Token token1 = ParserImpl.this.scanner.getToken();
/* 560 */         if (!ParserImpl.this.scanner.checkToken(new Token.ID[] { Token.ID.Key, Token.ID.Value, Token.ID.BlockEnd })) {
/* 561 */           ParserImpl.this.states.push(new ParserImpl.ParseBlockMappingValue());
/* 562 */           return ParserImpl.this.parseBlockNodeOrIndentlessSequence();
/*     */         } 
/* 564 */         ParserImpl.this.state = new ParserImpl.ParseBlockMappingValue();
/* 565 */         return ParserImpl.this.processEmptyScalar(token1.getEndMark());
/*     */       } 
/*     */       
/* 568 */       if (!ParserImpl.this.scanner.checkToken(new Token.ID[] { Token.ID.BlockEnd })) {
/* 569 */         Token token1 = ParserImpl.this.scanner.peekToken();
/* 570 */         throw new ParserException("while parsing a block mapping", (Mark)ParserImpl.this.marks.pop(), "expected <block end>, but found '" + token1.getTokenId() + "'", token1.getStartMark());
/*     */       } 
/*     */ 
/*     */       
/* 574 */       Token token = ParserImpl.this.scanner.getToken();
/* 575 */       MappingEndEvent mappingEndEvent = new MappingEndEvent(token.getStartMark(), token.getEndMark());
/* 576 */       ParserImpl.this.state = (Production)ParserImpl.this.states.pop();
/* 577 */       ParserImpl.this.marks.pop();
/* 578 */       return (Event)mappingEndEvent;
/*     */     } }
/*     */   
/*     */   private class ParseBlockMappingValue implements Production { private ParseBlockMappingValue() {}
/*     */     
/*     */     public Event produce() {
/* 584 */       if (ParserImpl.this.scanner.checkToken(new Token.ID[] { Token.ID.Value })) {
/* 585 */         Token token1 = ParserImpl.this.scanner.getToken();
/* 586 */         if (!ParserImpl.this.scanner.checkToken(new Token.ID[] { Token.ID.Key, Token.ID.Value, Token.ID.BlockEnd })) {
/* 587 */           ParserImpl.this.states.push(new ParserImpl.ParseBlockMappingKey());
/* 588 */           return ParserImpl.this.parseBlockNodeOrIndentlessSequence();
/*     */         } 
/* 590 */         ParserImpl.this.state = new ParserImpl.ParseBlockMappingKey();
/* 591 */         return ParserImpl.this.processEmptyScalar(token1.getEndMark());
/*     */       } 
/*     */       
/* 594 */       ParserImpl.this.state = new ParserImpl.ParseBlockMappingKey();
/* 595 */       Token token = ParserImpl.this.scanner.peekToken();
/* 596 */       return ParserImpl.this.processEmptyScalar(token.getStartMark());
/*     */     } }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private class ParseFlowSequenceFirstEntry
/*     */     implements Production
/*     */   {
/*     */     private ParseFlowSequenceFirstEntry() {}
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Event produce() {
/* 615 */       Token token = ParserImpl.this.scanner.getToken();
/* 616 */       ParserImpl.this.marks.push(token.getStartMark());
/* 617 */       return (new ParserImpl.ParseFlowSequenceEntry(true)).produce();
/*     */     }
/*     */   }
/*     */   
/*     */   private class ParseFlowSequenceEntry implements Production {
/*     */     private boolean first = false;
/*     */     
/*     */     public ParseFlowSequenceEntry(boolean first) {
/* 625 */       this.first = first;
/*     */     }
/*     */     
/*     */     public Event produce() {
/* 629 */       if (!ParserImpl.this.scanner.checkToken(new Token.ID[] { Token.ID.FlowSequenceEnd })) {
/* 630 */         if (!this.first) {
/* 631 */           if (ParserImpl.this.scanner.checkToken(new Token.ID[] { Token.ID.FlowEntry })) {
/* 632 */             ParserImpl.this.scanner.getToken();
/*     */           } else {
/* 634 */             Token token1 = ParserImpl.this.scanner.peekToken();
/* 635 */             throw new ParserException("while parsing a flow sequence", (Mark)ParserImpl.this.marks.pop(), "expected ',' or ']', but got " + token1.getTokenId(), token1.getStartMark());
/*     */           } 
/*     */         }
/*     */ 
/*     */         
/* 640 */         if (ParserImpl.this.scanner.checkToken(new Token.ID[] { Token.ID.Key })) {
/* 641 */           Token token1 = ParserImpl.this.scanner.peekToken();
/* 642 */           MappingStartEvent mappingStartEvent = new MappingStartEvent(null, null, true, token1.getStartMark(), token1.getEndMark(), DumperOptions.FlowStyle.FLOW);
/*     */           
/* 644 */           ParserImpl.this.state = new ParserImpl.ParseFlowSequenceEntryMappingKey();
/* 645 */           return (Event)mappingStartEvent;
/* 646 */         }  if (!ParserImpl.this.scanner.checkToken(new Token.ID[] { Token.ID.FlowSequenceEnd })) {
/* 647 */           ParserImpl.this.states.push(new ParseFlowSequenceEntry(false));
/* 648 */           return ParserImpl.this.parseFlowNode();
/*     */         } 
/*     */       } 
/* 651 */       Token token = ParserImpl.this.scanner.getToken();
/* 652 */       SequenceEndEvent sequenceEndEvent = new SequenceEndEvent(token.getStartMark(), token.getEndMark());
/* 653 */       ParserImpl.this.state = (Production)ParserImpl.this.states.pop();
/* 654 */       ParserImpl.this.marks.pop();
/* 655 */       return (Event)sequenceEndEvent;
/*     */     } }
/*     */   
/*     */   private class ParseFlowSequenceEntryMappingKey implements Production { private ParseFlowSequenceEntryMappingKey() {}
/*     */     
/*     */     public Event produce() {
/* 661 */       Token token = ParserImpl.this.scanner.getToken();
/* 662 */       if (!ParserImpl.this.scanner.checkToken(new Token.ID[] { Token.ID.Value, Token.ID.FlowEntry, Token.ID.FlowSequenceEnd })) {
/* 663 */         ParserImpl.this.states.push(new ParserImpl.ParseFlowSequenceEntryMappingValue());
/* 664 */         return ParserImpl.this.parseFlowNode();
/*     */       } 
/* 666 */       ParserImpl.this.state = new ParserImpl.ParseFlowSequenceEntryMappingValue();
/* 667 */       return ParserImpl.this.processEmptyScalar(token.getEndMark());
/*     */     } }
/*     */ 
/*     */   
/*     */   private class ParseFlowSequenceEntryMappingValue implements Production { private ParseFlowSequenceEntryMappingValue() {}
/*     */     
/*     */     public Event produce() {
/* 674 */       if (ParserImpl.this.scanner.checkToken(new Token.ID[] { Token.ID.Value })) {
/* 675 */         Token token1 = ParserImpl.this.scanner.getToken();
/* 676 */         if (!ParserImpl.this.scanner.checkToken(new Token.ID[] { Token.ID.FlowEntry, Token.ID.FlowSequenceEnd })) {
/* 677 */           ParserImpl.this.states.push(new ParserImpl.ParseFlowSequenceEntryMappingEnd());
/* 678 */           return ParserImpl.this.parseFlowNode();
/*     */         } 
/* 680 */         ParserImpl.this.state = new ParserImpl.ParseFlowSequenceEntryMappingEnd();
/* 681 */         return ParserImpl.this.processEmptyScalar(token1.getEndMark());
/*     */       } 
/*     */       
/* 684 */       ParserImpl.this.state = new ParserImpl.ParseFlowSequenceEntryMappingEnd();
/* 685 */       Token token = ParserImpl.this.scanner.peekToken();
/* 686 */       return ParserImpl.this.processEmptyScalar(token.getStartMark());
/*     */     } }
/*     */   
/*     */   private class ParseFlowSequenceEntryMappingEnd implements Production {
/*     */     private ParseFlowSequenceEntryMappingEnd() {}
/*     */     
/*     */     public Event produce() {
/* 693 */       ParserImpl.this.state = new ParserImpl.ParseFlowSequenceEntry(false);
/* 694 */       Token token = ParserImpl.this.scanner.peekToken();
/* 695 */       return (Event)new MappingEndEvent(token.getStartMark(), token.getEndMark());
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private class ParseFlowMappingFirstKey
/*     */     implements Production
/*     */   {
/*     */     private ParseFlowMappingFirstKey() {}
/*     */ 
/*     */ 
/*     */     
/*     */     public Event produce() {
/* 710 */       Token token = ParserImpl.this.scanner.getToken();
/* 711 */       ParserImpl.this.marks.push(token.getStartMark());
/* 712 */       return (new ParserImpl.ParseFlowMappingKey(true)).produce();
/*     */     }
/*     */   }
/*     */   
/*     */   private class ParseFlowMappingKey implements Production {
/*     */     private boolean first = false;
/*     */     
/*     */     public ParseFlowMappingKey(boolean first) {
/* 720 */       this.first = first;
/*     */     }
/*     */     
/*     */     public Event produce() {
/* 724 */       if (!ParserImpl.this.scanner.checkToken(new Token.ID[] { Token.ID.FlowMappingEnd })) {
/* 725 */         if (!this.first) {
/* 726 */           if (ParserImpl.this.scanner.checkToken(new Token.ID[] { Token.ID.FlowEntry })) {
/* 727 */             ParserImpl.this.scanner.getToken();
/*     */           } else {
/* 729 */             Token token1 = ParserImpl.this.scanner.peekToken();
/* 730 */             throw new ParserException("while parsing a flow mapping", (Mark)ParserImpl.this.marks.pop(), "expected ',' or '}', but got " + token1.getTokenId(), token1.getStartMark());
/*     */           } 
/*     */         }
/*     */ 
/*     */         
/* 735 */         if (ParserImpl.this.scanner.checkToken(new Token.ID[] { Token.ID.Key })) {
/* 736 */           Token token1 = ParserImpl.this.scanner.getToken();
/* 737 */           if (!ParserImpl.this.scanner.checkToken(new Token.ID[] { Token.ID.Value, Token.ID.FlowEntry, Token.ID.FlowMappingEnd })) {
/*     */             
/* 739 */             ParserImpl.this.states.push(new ParserImpl.ParseFlowMappingValue());
/* 740 */             return ParserImpl.this.parseFlowNode();
/*     */           } 
/* 742 */           ParserImpl.this.state = new ParserImpl.ParseFlowMappingValue();
/* 743 */           return ParserImpl.this.processEmptyScalar(token1.getEndMark());
/*     */         } 
/* 745 */         if (!ParserImpl.this.scanner.checkToken(new Token.ID[] { Token.ID.FlowMappingEnd })) {
/* 746 */           ParserImpl.this.states.push(new ParserImpl.ParseFlowMappingEmptyValue());
/* 747 */           return ParserImpl.this.parseFlowNode();
/*     */         } 
/*     */       } 
/* 750 */       Token token = ParserImpl.this.scanner.getToken();
/* 751 */       MappingEndEvent mappingEndEvent = new MappingEndEvent(token.getStartMark(), token.getEndMark());
/* 752 */       ParserImpl.this.state = (Production)ParserImpl.this.states.pop();
/* 753 */       ParserImpl.this.marks.pop();
/* 754 */       return (Event)mappingEndEvent;
/*     */     } }
/*     */   
/*     */   private class ParseFlowMappingValue implements Production { private ParseFlowMappingValue() {}
/*     */     
/*     */     public Event produce() {
/* 760 */       if (ParserImpl.this.scanner.checkToken(new Token.ID[] { Token.ID.Value })) {
/* 761 */         Token token1 = ParserImpl.this.scanner.getToken();
/* 762 */         if (!ParserImpl.this.scanner.checkToken(new Token.ID[] { Token.ID.FlowEntry, Token.ID.FlowMappingEnd })) {
/* 763 */           ParserImpl.this.states.push(new ParserImpl.ParseFlowMappingKey(false));
/* 764 */           return ParserImpl.this.parseFlowNode();
/*     */         } 
/* 766 */         ParserImpl.this.state = new ParserImpl.ParseFlowMappingKey(false);
/* 767 */         return ParserImpl.this.processEmptyScalar(token1.getEndMark());
/*     */       } 
/*     */       
/* 770 */       ParserImpl.this.state = new ParserImpl.ParseFlowMappingKey(false);
/* 771 */       Token token = ParserImpl.this.scanner.peekToken();
/* 772 */       return ParserImpl.this.processEmptyScalar(token.getStartMark());
/*     */     } }
/*     */   
/*     */   private class ParseFlowMappingEmptyValue implements Production {
/*     */     private ParseFlowMappingEmptyValue() {}
/*     */     
/*     */     public Event produce() {
/* 779 */       ParserImpl.this.state = new ParserImpl.ParseFlowMappingKey(false);
/* 780 */       return ParserImpl.this.processEmptyScalar(ParserImpl.this.scanner.peekToken().getStartMark());
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
/*     */   private Event processEmptyScalar(Mark mark) {
/* 793 */     return (Event)new ScalarEvent(null, null, new ImplicitTuple(true, false), "", mark, mark, DumperOptions.ScalarStyle.PLAIN);
/*     */   }
/*     */ }


/* Location:              C:\Users\Admin\OneDrive\Рабочий стол\NeverHook Crack.jar!\org\yaml\snakeyaml\parser\ParserImpl.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */