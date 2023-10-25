package org.yaml.snakeyaml.scanner;

import java.nio.ByteBuffer;
import java.nio.charset.CharacterCodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.error.Mark;
import org.yaml.snakeyaml.error.YAMLException;
import org.yaml.snakeyaml.reader.StreamReader;
import org.yaml.snakeyaml.tokens.AliasToken;
import org.yaml.snakeyaml.tokens.AnchorToken;
import org.yaml.snakeyaml.tokens.BlockEndToken;
import org.yaml.snakeyaml.tokens.BlockEntryToken;
import org.yaml.snakeyaml.tokens.BlockMappingStartToken;
import org.yaml.snakeyaml.tokens.BlockSequenceStartToken;
import org.yaml.snakeyaml.tokens.DirectiveToken;
import org.yaml.snakeyaml.tokens.DocumentEndToken;
import org.yaml.snakeyaml.tokens.DocumentStartToken;
import org.yaml.snakeyaml.tokens.FlowEntryToken;
import org.yaml.snakeyaml.tokens.FlowMappingEndToken;
import org.yaml.snakeyaml.tokens.FlowMappingStartToken;
import org.yaml.snakeyaml.tokens.FlowSequenceEndToken;
import org.yaml.snakeyaml.tokens.FlowSequenceStartToken;
import org.yaml.snakeyaml.tokens.KeyToken;
import org.yaml.snakeyaml.tokens.ScalarToken;
import org.yaml.snakeyaml.tokens.StreamEndToken;
import org.yaml.snakeyaml.tokens.StreamStartToken;
import org.yaml.snakeyaml.tokens.TagToken;
import org.yaml.snakeyaml.tokens.TagTuple;
import org.yaml.snakeyaml.tokens.Token;
import org.yaml.snakeyaml.tokens.ValueToken;
import org.yaml.snakeyaml.util.ArrayStack;
import org.yaml.snakeyaml.util.UriEncoder;

public final class ScannerImpl implements Scanner {
  private static final Pattern NOT_HEXA = Pattern.compile("[^0-9A-Fa-f]");
  
  public static final Map<Character, String> ESCAPE_REPLACEMENTS = new HashMap<>();
  
  public static final Map<Character, Integer> ESCAPE_CODES = new HashMap<>();
  
  private final StreamReader reader;
  
  static {
    ESCAPE_REPLACEMENTS.put(Character.valueOf('0'), "\000");
    ESCAPE_REPLACEMENTS.put(Character.valueOf('a'), "\007");
    ESCAPE_REPLACEMENTS.put(Character.valueOf('b'), "\b");
    ESCAPE_REPLACEMENTS.put(Character.valueOf('t'), "\t");
    ESCAPE_REPLACEMENTS.put(Character.valueOf('n'), "\n");
    ESCAPE_REPLACEMENTS.put(Character.valueOf('v'), "\013");
    ESCAPE_REPLACEMENTS.put(Character.valueOf('f'), "\f");
    ESCAPE_REPLACEMENTS.put(Character.valueOf('r'), "\r");
    ESCAPE_REPLACEMENTS.put(Character.valueOf('e'), "\033");
    ESCAPE_REPLACEMENTS.put(Character.valueOf(' '), " ");
    ESCAPE_REPLACEMENTS.put(Character.valueOf('"'), "\"");
    ESCAPE_REPLACEMENTS.put(Character.valueOf('\\'), "\\");
    ESCAPE_REPLACEMENTS.put(Character.valueOf('N'), "");
    ESCAPE_REPLACEMENTS.put(Character.valueOf('_'), " ");
    ESCAPE_REPLACEMENTS.put(Character.valueOf('L'), " ");
    ESCAPE_REPLACEMENTS.put(Character.valueOf('P'), " ");
    ESCAPE_CODES.put(Character.valueOf('x'), Integer.valueOf(2));
    ESCAPE_CODES.put(Character.valueOf('u'), Integer.valueOf(4));
    ESCAPE_CODES.put(Character.valueOf('U'), Integer.valueOf(8));
  }
  
  private boolean done = false;
  
  private int flowLevel = 0;
  
  private List<Token> tokens;
  
  private int tokensTaken = 0;
  
  private int indent = -1;
  
  private ArrayStack<Integer> indents;
  
  private boolean allowSimpleKey = true;
  
  private Map<Integer, SimpleKey> possibleSimpleKeys;
  
  public ScannerImpl(StreamReader reader) {
    this.reader = reader;
    this.tokens = new ArrayList<>(100);
    this.indents = new ArrayStack(10);
    this.possibleSimpleKeys = new LinkedHashMap<>();
    fetchStreamStart();
  }
  
  public boolean checkToken(Token.ID... choices) {
    while (needMoreTokens())
      fetchMoreTokens(); 
    if (!this.tokens.isEmpty()) {
      if (choices.length == 0)
        return true; 
      Token.ID first = ((Token)this.tokens.get(0)).getTokenId();
      for (int i = 0; i < choices.length; i++) {
        if (first == choices[i])
          return true; 
      } 
    } 
    return false;
  }
  
  public Token peekToken() {
    while (needMoreTokens())
      fetchMoreTokens(); 
    return this.tokens.get(0);
  }
  
  public Token getToken() {
    this.tokensTaken++;
    return this.tokens.remove(0);
  }
  
  private boolean needMoreTokens() {
    if (this.done)
      return false; 
    if (this.tokens.isEmpty())
      return true; 
    stalePossibleSimpleKeys();
    return (nextPossibleSimpleKey() == this.tokensTaken);
  }
  
  private void fetchMoreTokens() {
    scanToNextToken();
    stalePossibleSimpleKeys();
    unwindIndent(this.reader.getColumn());
    int c = this.reader.peek();
    switch (c) {
      case 0:
        fetchStreamEnd();
        return;
      case 37:
        if (checkDirective()) {
          fetchDirective();
          return;
        } 
        break;
      case 45:
        if (checkDocumentStart()) {
          fetchDocumentStart();
          return;
        } 
        if (checkBlockEntry()) {
          fetchBlockEntry();
          return;
        } 
        break;
      case 46:
        if (checkDocumentEnd()) {
          fetchDocumentEnd();
          return;
        } 
        break;
      case 91:
        fetchFlowSequenceStart();
        return;
      case 123:
        fetchFlowMappingStart();
        return;
      case 93:
        fetchFlowSequenceEnd();
        return;
      case 125:
        fetchFlowMappingEnd();
        return;
      case 44:
        fetchFlowEntry();
        return;
      case 63:
        if (checkKey()) {
          fetchKey();
          return;
        } 
        break;
      case 58:
        if (checkValue()) {
          fetchValue();
          return;
        } 
        break;
      case 42:
        fetchAlias();
        return;
      case 38:
        fetchAnchor();
        return;
      case 33:
        fetchTag();
        return;
      case 124:
        if (this.flowLevel == 0) {
          fetchLiteral();
          return;
        } 
        break;
      case 62:
        if (this.flowLevel == 0) {
          fetchFolded();
          return;
        } 
        break;
      case 39:
        fetchSingle();
        return;
      case 34:
        fetchDouble();
        return;
    } 
    if (checkPlain()) {
      fetchPlain();
      return;
    } 
    String chRepresentation = String.valueOf(Character.toChars(c));
    for (Character s : ESCAPE_REPLACEMENTS.keySet()) {
      String v = ESCAPE_REPLACEMENTS.get(s);
      if (v.equals(chRepresentation)) {
        chRepresentation = "\\" + s;
        break;
      } 
    } 
    if (c == 9)
      chRepresentation = chRepresentation + "(TAB)"; 
    String text = String.format("found character '%s' that cannot start any token. (Do not use %s for indentation)", new Object[] { chRepresentation, chRepresentation });
    throw new ScannerException("while scanning for the next token", null, text, this.reader.getMark());
  }
  
  private int nextPossibleSimpleKey() {
    if (!this.possibleSimpleKeys.isEmpty())
      return ((SimpleKey)this.possibleSimpleKeys.values().iterator().next()).getTokenNumber(); 
    return -1;
  }
  
  private void stalePossibleSimpleKeys() {
    if (!this.possibleSimpleKeys.isEmpty()) {
      Iterator<SimpleKey> iterator = this.possibleSimpleKeys.values().iterator();
      while (iterator.hasNext()) {
        SimpleKey key = iterator.next();
        if (key.getLine() != this.reader.getLine() || this.reader.getIndex() - key.getIndex() > 1024) {
          if (key.isRequired())
            throw new ScannerException("while scanning a simple key", key.getMark(), "could not find expected ':'", this.reader.getMark()); 
          iterator.remove();
        } 
      } 
    } 
  }
  
  private void savePossibleSimpleKey() {
    boolean required = (this.flowLevel == 0 && this.indent == this.reader.getColumn());
    if (this.allowSimpleKey || !required) {
      if (this.allowSimpleKey) {
        removePossibleSimpleKey();
        int tokenNumber = this.tokensTaken + this.tokens.size();
        SimpleKey key = new SimpleKey(tokenNumber, required, this.reader.getIndex(), this.reader.getLine(), this.reader.getColumn(), this.reader.getMark());
        this.possibleSimpleKeys.put(Integer.valueOf(this.flowLevel), key);
      } 
      return;
    } 
    throw new YAMLException("A simple key is required only if it is the first token in the current line");
  }
  
  private void removePossibleSimpleKey() {
    SimpleKey key = this.possibleSimpleKeys.remove(Integer.valueOf(this.flowLevel));
    if (key != null && key.isRequired())
      throw new ScannerException("while scanning a simple key", key.getMark(), "could not find expected ':'", this.reader.getMark()); 
  }
  
  private void unwindIndent(int col) {
    if (this.flowLevel != 0)
      return; 
    while (this.indent > col) {
      Mark mark = this.reader.getMark();
      this.indent = ((Integer)this.indents.pop()).intValue();
      this.tokens.add(new BlockEndToken(mark, mark));
    } 
  }
  
  private boolean addIndent(int column) {
    if (this.indent < column) {
      this.indents.push(Integer.valueOf(this.indent));
      this.indent = column;
      return true;
    } 
    return false;
  }
  
  private void fetchStreamStart() {
    Mark mark = this.reader.getMark();
    StreamStartToken streamStartToken = new StreamStartToken(mark, mark);
    this.tokens.add(streamStartToken);
  }
  
  private void fetchStreamEnd() {
    unwindIndent(-1);
    removePossibleSimpleKey();
    this.allowSimpleKey = false;
    this.possibleSimpleKeys.clear();
    Mark mark = this.reader.getMark();
    StreamEndToken streamEndToken = new StreamEndToken(mark, mark);
    this.tokens.add(streamEndToken);
    this.done = true;
  }
  
  private void fetchDirective() {
    unwindIndent(-1);
    removePossibleSimpleKey();
    this.allowSimpleKey = false;
    Token tok = scanDirective();
    this.tokens.add(tok);
  }
  
  private void fetchDocumentStart() {
    fetchDocumentIndicator(true);
  }
  
  private void fetchDocumentEnd() {
    fetchDocumentIndicator(false);
  }
  
  private void fetchDocumentIndicator(boolean isDocumentStart) {
    DocumentEndToken documentEndToken;
    unwindIndent(-1);
    removePossibleSimpleKey();
    this.allowSimpleKey = false;
    Mark startMark = this.reader.getMark();
    this.reader.forward(3);
    Mark endMark = this.reader.getMark();
    if (isDocumentStart) {
      DocumentStartToken documentStartToken = new DocumentStartToken(startMark, endMark);
    } else {
      documentEndToken = new DocumentEndToken(startMark, endMark);
    } 
    this.tokens.add(documentEndToken);
  }
  
  private void fetchFlowSequenceStart() {
    fetchFlowCollectionStart(false);
  }
  
  private void fetchFlowMappingStart() {
    fetchFlowCollectionStart(true);
  }
  
  private void fetchFlowCollectionStart(boolean isMappingStart) {
    FlowSequenceStartToken flowSequenceStartToken;
    savePossibleSimpleKey();
    this.flowLevel++;
    this.allowSimpleKey = true;
    Mark startMark = this.reader.getMark();
    this.reader.forward(1);
    Mark endMark = this.reader.getMark();
    if (isMappingStart) {
      FlowMappingStartToken flowMappingStartToken = new FlowMappingStartToken(startMark, endMark);
    } else {
      flowSequenceStartToken = new FlowSequenceStartToken(startMark, endMark);
    } 
    this.tokens.add(flowSequenceStartToken);
  }
  
  private void fetchFlowSequenceEnd() {
    fetchFlowCollectionEnd(false);
  }
  
  private void fetchFlowMappingEnd() {
    fetchFlowCollectionEnd(true);
  }
  
  private void fetchFlowCollectionEnd(boolean isMappingEnd) {
    FlowSequenceEndToken flowSequenceEndToken;
    removePossibleSimpleKey();
    this.flowLevel--;
    this.allowSimpleKey = false;
    Mark startMark = this.reader.getMark();
    this.reader.forward();
    Mark endMark = this.reader.getMark();
    if (isMappingEnd) {
      FlowMappingEndToken flowMappingEndToken = new FlowMappingEndToken(startMark, endMark);
    } else {
      flowSequenceEndToken = new FlowSequenceEndToken(startMark, endMark);
    } 
    this.tokens.add(flowSequenceEndToken);
  }
  
  private void fetchFlowEntry() {
    this.allowSimpleKey = true;
    removePossibleSimpleKey();
    Mark startMark = this.reader.getMark();
    this.reader.forward();
    Mark endMark = this.reader.getMark();
    FlowEntryToken flowEntryToken = new FlowEntryToken(startMark, endMark);
    this.tokens.add(flowEntryToken);
  }
  
  private void fetchBlockEntry() {
    if (this.flowLevel == 0) {
      if (!this.allowSimpleKey)
        throw new ScannerException(null, null, "sequence entries are not allowed here", this.reader.getMark()); 
      if (addIndent(this.reader.getColumn())) {
        Mark mark = this.reader.getMark();
        this.tokens.add(new BlockSequenceStartToken(mark, mark));
      } 
    } 
    this.allowSimpleKey = true;
    removePossibleSimpleKey();
    Mark startMark = this.reader.getMark();
    this.reader.forward();
    Mark endMark = this.reader.getMark();
    BlockEntryToken blockEntryToken = new BlockEntryToken(startMark, endMark);
    this.tokens.add(blockEntryToken);
  }
  
  private void fetchKey() {
    if (this.flowLevel == 0) {
      if (!this.allowSimpleKey)
        throw new ScannerException(null, null, "mapping keys are not allowed here", this.reader.getMark()); 
      if (addIndent(this.reader.getColumn())) {
        Mark mark = this.reader.getMark();
        this.tokens.add(new BlockMappingStartToken(mark, mark));
      } 
    } 
    this.allowSimpleKey = (this.flowLevel == 0);
    removePossibleSimpleKey();
    Mark startMark = this.reader.getMark();
    this.reader.forward();
    Mark endMark = this.reader.getMark();
    KeyToken keyToken = new KeyToken(startMark, endMark);
    this.tokens.add(keyToken);
  }
  
  private void fetchValue() {
    SimpleKey key = this.possibleSimpleKeys.remove(Integer.valueOf(this.flowLevel));
    if (key != null) {
      this.tokens.add(key.getTokenNumber() - this.tokensTaken, new KeyToken(key.getMark(), key.getMark()));
      if (this.flowLevel == 0 && 
        addIndent(key.getColumn()))
        this.tokens.add(key.getTokenNumber() - this.tokensTaken, new BlockMappingStartToken(key.getMark(), key.getMark())); 
      this.allowSimpleKey = false;
    } else {
      if (this.flowLevel == 0)
        if (!this.allowSimpleKey)
          throw new ScannerException(null, null, "mapping values are not allowed here", this.reader.getMark());  
      if (this.flowLevel == 0 && 
        addIndent(this.reader.getColumn())) {
        Mark mark = this.reader.getMark();
        this.tokens.add(new BlockMappingStartToken(mark, mark));
      } 
      this.allowSimpleKey = (this.flowLevel == 0);
      removePossibleSimpleKey();
    } 
    Mark startMark = this.reader.getMark();
    this.reader.forward();
    Mark endMark = this.reader.getMark();
    ValueToken valueToken = new ValueToken(startMark, endMark);
    this.tokens.add(valueToken);
  }
  
  private void fetchAlias() {
    savePossibleSimpleKey();
    this.allowSimpleKey = false;
    Token tok = scanAnchor(false);
    this.tokens.add(tok);
  }
  
  private void fetchAnchor() {
    savePossibleSimpleKey();
    this.allowSimpleKey = false;
    Token tok = scanAnchor(true);
    this.tokens.add(tok);
  }
  
  private void fetchTag() {
    savePossibleSimpleKey();
    this.allowSimpleKey = false;
    Token tok = scanTag();
    this.tokens.add(tok);
  }
  
  private void fetchLiteral() {
    fetchBlockScalar('|');
  }
  
  private void fetchFolded() {
    fetchBlockScalar('>');
  }
  
  private void fetchBlockScalar(char style) {
    this.allowSimpleKey = true;
    removePossibleSimpleKey();
    Token tok = scanBlockScalar(style);
    this.tokens.add(tok);
  }
  
  private void fetchSingle() {
    fetchFlowScalar('\'');
  }
  
  private void fetchDouble() {
    fetchFlowScalar('"');
  }
  
  private void fetchFlowScalar(char style) {
    savePossibleSimpleKey();
    this.allowSimpleKey = false;
    Token tok = scanFlowScalar(style);
    this.tokens.add(tok);
  }
  
  private void fetchPlain() {
    savePossibleSimpleKey();
    this.allowSimpleKey = false;
    Token tok = scanPlain();
    this.tokens.add(tok);
  }
  
  private boolean checkDirective() {
    return (this.reader.getColumn() == 0);
  }
  
  private boolean checkDocumentStart() {
    if (this.reader.getColumn() == 0 && 
      "---".equals(this.reader.prefix(3)) && Constant.NULL_BL_T_LINEBR.has(this.reader.peek(3)))
      return true; 
    return false;
  }
  
  private boolean checkDocumentEnd() {
    if (this.reader.getColumn() == 0 && 
      "...".equals(this.reader.prefix(3)) && Constant.NULL_BL_T_LINEBR.has(this.reader.peek(3)))
      return true; 
    return false;
  }
  
  private boolean checkBlockEntry() {
    return Constant.NULL_BL_T_LINEBR.has(this.reader.peek(1));
  }
  
  private boolean checkKey() {
    if (this.flowLevel != 0)
      return true; 
    return Constant.NULL_BL_T_LINEBR.has(this.reader.peek(1));
  }
  
  private boolean checkValue() {
    if (this.flowLevel != 0)
      return true; 
    return Constant.NULL_BL_T_LINEBR.has(this.reader.peek(1));
  }
  
  private boolean checkPlain() {
    int c = this.reader.peek();
    return (Constant.NULL_BL_T_LINEBR.hasNo(c, "-?:,[]{}#&*!|>'\"%@`") || (Constant.NULL_BL_T_LINEBR.hasNo(this.reader.peek(1)) && (c == 45 || (this.flowLevel == 0 && "?:".indexOf(c) != -1))));
  }
  
  private void scanToNextToken() {
    if (this.reader.getIndex() == 0 && this.reader.peek() == 65279)
      this.reader.forward(); 
    boolean found = false;
    while (!found) {
      int ff = 0;
      while (this.reader.peek(ff) == 32)
        ff++; 
      if (ff > 0)
        this.reader.forward(ff); 
      if (this.reader.peek() == 35) {
        ff = 0;
        while (Constant.NULL_OR_LINEBR.hasNo(this.reader.peek(ff)))
          ff++; 
        if (ff > 0)
          this.reader.forward(ff); 
      } 
      if (scanLineBreak().length() != 0) {
        if (this.flowLevel == 0)
          this.allowSimpleKey = true; 
        continue;
      } 
      found = true;
    } 
  }
  
  private Token scanDirective() {
    Mark endMark, startMark = this.reader.getMark();
    this.reader.forward();
    String name = scanDirectiveName(startMark);
    List<?> value = null;
    if ("YAML".equals(name)) {
      value = scanYamlDirectiveValue(startMark);
      endMark = this.reader.getMark();
    } else if ("TAG".equals(name)) {
      value = scanTagDirectiveValue(startMark);
      endMark = this.reader.getMark();
    } else {
      endMark = this.reader.getMark();
      int ff = 0;
      while (Constant.NULL_OR_LINEBR.hasNo(this.reader.peek(ff)))
        ff++; 
      if (ff > 0)
        this.reader.forward(ff); 
    } 
    scanDirectiveIgnoredLine(startMark);
    return (Token)new DirectiveToken(name, value, startMark, endMark);
  }
  
  private String scanDirectiveName(Mark startMark) {
    int length = 0;
    int c = this.reader.peek(length);
    while (Constant.ALPHA.has(c)) {
      length++;
      c = this.reader.peek(length);
    } 
    if (length == 0) {
      String s = String.valueOf(Character.toChars(c));
      throw new ScannerException("while scanning a directive", startMark, "expected alphabetic or numeric character, but found " + s + "(" + c + ")", this.reader.getMark());
    } 
    String value = this.reader.prefixForward(length);
    c = this.reader.peek();
    if (Constant.NULL_BL_LINEBR.hasNo(c)) {
      String s = String.valueOf(Character.toChars(c));
      throw new ScannerException("while scanning a directive", startMark, "expected alphabetic or numeric character, but found " + s + "(" + c + ")", this.reader.getMark());
    } 
    return value;
  }
  
  private List<Integer> scanYamlDirectiveValue(Mark startMark) {
    while (this.reader.peek() == 32)
      this.reader.forward(); 
    Integer major = scanYamlDirectiveNumber(startMark);
    int c = this.reader.peek();
    if (c != 46) {
      String s = String.valueOf(Character.toChars(c));
      throw new ScannerException("while scanning a directive", startMark, "expected a digit or '.', but found " + s + "(" + c + ")", this.reader.getMark());
    } 
    this.reader.forward();
    Integer minor = scanYamlDirectiveNumber(startMark);
    c = this.reader.peek();
    if (Constant.NULL_BL_LINEBR.hasNo(c)) {
      String s = String.valueOf(Character.toChars(c));
      throw new ScannerException("while scanning a directive", startMark, "expected a digit or ' ', but found " + s + "(" + c + ")", this.reader.getMark());
    } 
    List<Integer> result = new ArrayList<>(2);
    result.add(major);
    result.add(minor);
    return result;
  }
  
  private Integer scanYamlDirectiveNumber(Mark startMark) {
    int c = this.reader.peek();
    if (!Character.isDigit(c)) {
      String s = String.valueOf(Character.toChars(c));
      throw new ScannerException("while scanning a directive", startMark, "expected a digit, but found " + s + "(" + c + ")", this.reader.getMark());
    } 
    int length = 0;
    while (Character.isDigit(this.reader.peek(length)))
      length++; 
    Integer value = Integer.valueOf(Integer.parseInt(this.reader.prefixForward(length)));
    return value;
  }
  
  private List<String> scanTagDirectiveValue(Mark startMark) {
    while (this.reader.peek() == 32)
      this.reader.forward(); 
    String handle = scanTagDirectiveHandle(startMark);
    while (this.reader.peek() == 32)
      this.reader.forward(); 
    String prefix = scanTagDirectivePrefix(startMark);
    List<String> result = new ArrayList<>(2);
    result.add(handle);
    result.add(prefix);
    return result;
  }
  
  private String scanTagDirectiveHandle(Mark startMark) {
    String value = scanTagHandle("directive", startMark);
    int c = this.reader.peek();
    if (c != 32) {
      String s = String.valueOf(Character.toChars(c));
      throw new ScannerException("while scanning a directive", startMark, "expected ' ', but found " + s + "(" + c + ")", this.reader.getMark());
    } 
    return value;
  }
  
  private String scanTagDirectivePrefix(Mark startMark) {
    String value = scanTagUri("directive", startMark);
    int c = this.reader.peek();
    if (Constant.NULL_BL_LINEBR.hasNo(c)) {
      String s = String.valueOf(Character.toChars(c));
      throw new ScannerException("while scanning a directive", startMark, "expected ' ', but found " + s + "(" + c + ")", this.reader.getMark());
    } 
    return value;
  }
  
  private void scanDirectiveIgnoredLine(Mark startMark) {
    while (this.reader.peek() == 32)
      this.reader.forward(); 
    if (this.reader.peek() == 35)
      while (Constant.NULL_OR_LINEBR.hasNo(this.reader.peek()))
        this.reader.forward();  
    int c = this.reader.peek();
    String lineBreak = scanLineBreak();
    if (lineBreak.length() == 0 && c != 0) {
      String s = String.valueOf(Character.toChars(c));
      throw new ScannerException("while scanning a directive", startMark, "expected a comment or a line break, but found " + s + "(" + c + ")", this.reader.getMark());
    } 
  }
  
  private Token scanAnchor(boolean isAnchor) {
    AliasToken aliasToken;
    Mark startMark = this.reader.getMark();
    int indicator = this.reader.peek();
    String name = (indicator == 42) ? "alias" : "anchor";
    this.reader.forward();
    int length = 0;
    int c = this.reader.peek(length);
    while (Constant.NULL_BL_T_LINEBR.hasNo(c, ":,[]{}")) {
      length++;
      c = this.reader.peek(length);
    } 
    if (length == 0) {
      String s = String.valueOf(Character.toChars(c));
      throw new ScannerException("while scanning an " + name, startMark, "unexpected character found " + s + "(" + c + ")", this.reader.getMark());
    } 
    String value = this.reader.prefixForward(length);
    c = this.reader.peek();
    if (Constant.NULL_BL_T_LINEBR.hasNo(c, "?:,]}%@`")) {
      String s = String.valueOf(Character.toChars(c));
      throw new ScannerException("while scanning an " + name, startMark, "unexpected character found " + s + "(" + c + ")", this.reader.getMark());
    } 
    Mark endMark = this.reader.getMark();
    if (isAnchor) {
      AnchorToken anchorToken = new AnchorToken(value, startMark, endMark);
    } else {
      aliasToken = new AliasToken(value, startMark, endMark);
    } 
    return (Token)aliasToken;
  }
  
  private Token scanTag() {
    Mark startMark = this.reader.getMark();
    int c = this.reader.peek(1);
    String handle = null;
    String suffix = null;
    if (c == 60) {
      this.reader.forward(2);
      suffix = scanTagUri("tag", startMark);
      c = this.reader.peek();
      if (c != 62) {
        String s = String.valueOf(Character.toChars(c));
        throw new ScannerException("while scanning a tag", startMark, "expected '>', but found '" + s + "' (" + c + ")", this.reader.getMark());
      } 
      this.reader.forward();
    } else if (Constant.NULL_BL_T_LINEBR.has(c)) {
      suffix = "!";
      this.reader.forward();
    } else {
      int length = 1;
      boolean useHandle = false;
      while (Constant.NULL_BL_LINEBR.hasNo(c)) {
        if (c == 33) {
          useHandle = true;
          break;
        } 
        length++;
        c = this.reader.peek(length);
      } 
      if (useHandle) {
        handle = scanTagHandle("tag", startMark);
      } else {
        handle = "!";
        this.reader.forward();
      } 
      suffix = scanTagUri("tag", startMark);
    } 
    c = this.reader.peek();
    if (Constant.NULL_BL_LINEBR.hasNo(c)) {
      String s = String.valueOf(Character.toChars(c));
      throw new ScannerException("while scanning a tag", startMark, "expected ' ', but found '" + s + "' (" + c + ")", this.reader.getMark());
    } 
    TagTuple value = new TagTuple(handle, suffix);
    Mark endMark = this.reader.getMark();
    return (Token)new TagToken(value, startMark, endMark);
  }
  
  private Token scanBlockScalar(char style) {
    boolean folded;
    String breaks;
    int indent;
    Mark mark1;
    if (style == '>') {
      folded = true;
    } else {
      folded = false;
    } 
    StringBuilder chunks = new StringBuilder();
    Mark startMark = this.reader.getMark();
    this.reader.forward();
    Chomping chompi = scanBlockScalarIndicators(startMark);
    int increment = chompi.getIncrement();
    scanBlockScalarIgnoredLine(startMark);
    int minIndent = this.indent + 1;
    if (minIndent < 1)
      minIndent = 1; 
    if (increment == -1) {
      Object[] brme = scanBlockScalarIndentation();
      breaks = (String)brme[0];
      int maxIndent = ((Integer)brme[1]).intValue();
      mark1 = (Mark)brme[2];
      indent = Math.max(minIndent, maxIndent);
    } else {
      indent = minIndent + increment - 1;
      Object[] brme = scanBlockScalarBreaks(indent);
      breaks = (String)brme[0];
      mark1 = (Mark)brme[1];
    } 
    String lineBreak = "";
    while (this.reader.getColumn() == indent && this.reader.peek() != 0) {
      chunks.append(breaks);
      boolean leadingNonSpace = (" \t".indexOf(this.reader.peek()) == -1);
      int length = 0;
      while (Constant.NULL_OR_LINEBR.hasNo(this.reader.peek(length)))
        length++; 
      chunks.append(this.reader.prefixForward(length));
      lineBreak = scanLineBreak();
      Object[] brme = scanBlockScalarBreaks(indent);
      breaks = (String)brme[0];
      mark1 = (Mark)brme[1];
      if (this.reader.getColumn() == indent && this.reader.peek() != 0) {
        if (folded && "\n".equals(lineBreak) && leadingNonSpace && " \t".indexOf(this.reader.peek()) == -1) {
          if (breaks.length() == 0)
            chunks.append(" "); 
          continue;
        } 
        chunks.append(lineBreak);
      } 
    } 
    if (chompi.chompTailIsNotFalse())
      chunks.append(lineBreak); 
    if (chompi.chompTailIsTrue())
      chunks.append(breaks); 
    return (Token)new ScalarToken(chunks.toString(), false, startMark, mark1, DumperOptions.ScalarStyle.createStyle(Character.valueOf(style)));
  }
  
  private Chomping scanBlockScalarIndicators(Mark startMark) {
    Boolean chomping = null;
    int increment = -1;
    int c = this.reader.peek();
    if (c == 45 || c == 43) {
      if (c == 43) {
        chomping = Boolean.TRUE;
      } else {
        chomping = Boolean.FALSE;
      } 
      this.reader.forward();
      c = this.reader.peek();
      if (Character.isDigit(c)) {
        String s = String.valueOf(Character.toChars(c));
        increment = Integer.parseInt(s);
        if (increment == 0)
          throw new ScannerException("while scanning a block scalar", startMark, "expected indentation indicator in the range 1-9, but found 0", this.reader.getMark()); 
        this.reader.forward();
      } 
    } else if (Character.isDigit(c)) {
      String s = String.valueOf(Character.toChars(c));
      increment = Integer.parseInt(s);
      if (increment == 0)
        throw new ScannerException("while scanning a block scalar", startMark, "expected indentation indicator in the range 1-9, but found 0", this.reader.getMark()); 
      this.reader.forward();
      c = this.reader.peek();
      if (c == 45 || c == 43) {
        if (c == 43) {
          chomping = Boolean.TRUE;
        } else {
          chomping = Boolean.FALSE;
        } 
        this.reader.forward();
      } 
    } 
    c = this.reader.peek();
    if (Constant.NULL_BL_LINEBR.hasNo(c)) {
      String s = String.valueOf(Character.toChars(c));
      throw new ScannerException("while scanning a block scalar", startMark, "expected chomping or indentation indicators, but found " + s + "(" + c + ")", this.reader.getMark());
    } 
    return new Chomping(chomping, increment);
  }
  
  private String scanBlockScalarIgnoredLine(Mark startMark) {
    while (this.reader.peek() == 32)
      this.reader.forward(); 
    if (this.reader.peek() == 35)
      while (Constant.NULL_OR_LINEBR.hasNo(this.reader.peek()))
        this.reader.forward();  
    int c = this.reader.peek();
    String lineBreak = scanLineBreak();
    if (lineBreak.length() == 0 && c != 0) {
      String s = String.valueOf(Character.toChars(c));
      throw new ScannerException("while scanning a block scalar", startMark, "expected a comment or a line break, but found " + s + "(" + c + ")", this.reader.getMark());
    } 
    return lineBreak;
  }
  
  private Object[] scanBlockScalarIndentation() {
    StringBuilder chunks = new StringBuilder();
    int maxIndent = 0;
    Mark endMark = this.reader.getMark();
    while (Constant.LINEBR.has(this.reader.peek(), " \r")) {
      if (this.reader.peek() != 32) {
        chunks.append(scanLineBreak());
        endMark = this.reader.getMark();
        continue;
      } 
      this.reader.forward();
      if (this.reader.getColumn() > maxIndent)
        maxIndent = this.reader.getColumn(); 
    } 
    return new Object[] { chunks.toString(), Integer.valueOf(maxIndent), endMark };
  }
  
  private Object[] scanBlockScalarBreaks(int indent) {
    StringBuilder chunks = new StringBuilder();
    Mark endMark = this.reader.getMark();
    int col = this.reader.getColumn();
    while (col < indent && this.reader.peek() == 32) {
      this.reader.forward();
      col++;
    } 
    String lineBreak = null;
    while ((lineBreak = scanLineBreak()).length() != 0) {
      chunks.append(lineBreak);
      endMark = this.reader.getMark();
      col = this.reader.getColumn();
      while (col < indent && this.reader.peek() == 32) {
        this.reader.forward();
        col++;
      } 
    } 
    return new Object[] { chunks.toString(), endMark };
  }
  
  private Token scanFlowScalar(char style) {
    boolean _double;
    if (style == '"') {
      _double = true;
    } else {
      _double = false;
    } 
    StringBuilder chunks = new StringBuilder();
    Mark startMark = this.reader.getMark();
    int quote = this.reader.peek();
    this.reader.forward();
    chunks.append(scanFlowScalarNonSpaces(_double, startMark));
    while (this.reader.peek() != quote) {
      chunks.append(scanFlowScalarSpaces(startMark));
      chunks.append(scanFlowScalarNonSpaces(_double, startMark));
    } 
    this.reader.forward();
    Mark endMark = this.reader.getMark();
    return (Token)new ScalarToken(chunks.toString(), false, startMark, endMark, DumperOptions.ScalarStyle.createStyle(Character.valueOf(style)));
  }
  
  private String scanFlowScalarNonSpaces(boolean doubleQuoted, Mark startMark) {
    StringBuilder chunks = new StringBuilder();
    while (true) {
      int length = 0;
      while (Constant.NULL_BL_T_LINEBR.hasNo(this.reader.peek(length), "'\"\\"))
        length++; 
      if (length != 0)
        chunks.append(this.reader.prefixForward(length)); 
      int c = this.reader.peek();
      if (!doubleQuoted && c == 39 && this.reader.peek(1) == 39) {
        chunks.append("'");
        this.reader.forward(2);
        continue;
      } 
      if ((doubleQuoted && c == 39) || (!doubleQuoted && "\"\\".indexOf(c) != -1)) {
        chunks.appendCodePoint(c);
        this.reader.forward();
        continue;
      } 
      if (doubleQuoted && c == 92) {
        this.reader.forward();
        c = this.reader.peek();
        if (!Character.isSupplementaryCodePoint(c) && ESCAPE_REPLACEMENTS.containsKey(Character.valueOf((char)c))) {
          chunks.append(ESCAPE_REPLACEMENTS.get(Character.valueOf((char)c)));
          this.reader.forward();
          continue;
        } 
        if (!Character.isSupplementaryCodePoint(c) && ESCAPE_CODES.containsKey(Character.valueOf((char)c))) {
          length = ((Integer)ESCAPE_CODES.get(Character.valueOf((char)c))).intValue();
          this.reader.forward();
          String hex = this.reader.prefix(length);
          if (NOT_HEXA.matcher(hex).find())
            throw new ScannerException("while scanning a double-quoted scalar", startMark, "expected escape sequence of " + length + " hexadecimal numbers, but found: " + hex, this.reader.getMark()); 
          int decimal = Integer.parseInt(hex, 16);
          String unicode = new String(Character.toChars(decimal));
          chunks.append(unicode);
          this.reader.forward(length);
          continue;
        } 
        if (scanLineBreak().length() != 0) {
          chunks.append(scanFlowScalarBreaks(startMark));
          continue;
        } 
        String s = String.valueOf(Character.toChars(c));
        throw new ScannerException("while scanning a double-quoted scalar", startMark, "found unknown escape character " + s + "(" + c + ")", this.reader.getMark());
      } 
      break;
    } 
    return chunks.toString();
  }
  
  private String scanFlowScalarSpaces(Mark startMark) {
    StringBuilder chunks = new StringBuilder();
    int length = 0;
    while (" \t".indexOf(this.reader.peek(length)) != -1)
      length++; 
    String whitespaces = this.reader.prefixForward(length);
    int c = this.reader.peek();
    if (c == 0)
      throw new ScannerException("while scanning a quoted scalar", startMark, "found unexpected end of stream", this.reader.getMark()); 
    String lineBreak = scanLineBreak();
    if (lineBreak.length() != 0) {
      String breaks = scanFlowScalarBreaks(startMark);
      if (!"\n".equals(lineBreak)) {
        chunks.append(lineBreak);
      } else if (breaks.length() == 0) {
        chunks.append(" ");
      } 
      chunks.append(breaks);
    } else {
      chunks.append(whitespaces);
    } 
    return chunks.toString();
  }
  
  private String scanFlowScalarBreaks(Mark startMark) {
    StringBuilder chunks = new StringBuilder();
    while (true) {
      String prefix = this.reader.prefix(3);
      if (("---".equals(prefix) || "...".equals(prefix)) && Constant.NULL_BL_T_LINEBR.has(this.reader.peek(3)))
        throw new ScannerException("while scanning a quoted scalar", startMark, "found unexpected document separator", this.reader.getMark()); 
      while (" \t".indexOf(this.reader.peek()) != -1)
        this.reader.forward(); 
      String lineBreak = scanLineBreak();
      if (lineBreak.length() != 0) {
        chunks.append(lineBreak);
        continue;
      } 
      break;
    } 
    return chunks.toString();
  }
  
  private Token scanPlain() {
    StringBuilder chunks = new StringBuilder();
    Mark startMark = this.reader.getMark();
    Mark endMark = startMark;
    int indent = this.indent + 1;
    String spaces = "";
    do {
      int length = 0;
      if (this.reader.peek() == 35)
        break; 
      while (true) {
        int c = this.reader.peek(length);
        if (Constant.NULL_BL_T_LINEBR.has(c) || (c == 58 && Constant.NULL_BL_T_LINEBR.has(this.reader.peek(length + 1), (this.flowLevel != 0) ? ",[]{}" : "")) || (this.flowLevel != 0 && ",?[]{}".indexOf(c) != -1))
          break; 
        length++;
      } 
      if (length == 0)
        break; 
      this.allowSimpleKey = false;
      chunks.append(spaces);
      chunks.append(this.reader.prefixForward(length));
      endMark = this.reader.getMark();
      spaces = scanPlainSpaces();
    } while (spaces.length() != 0 && this.reader.peek() != 35 && (this.flowLevel != 0 || this.reader.getColumn() >= indent));
    return (Token)new ScalarToken(chunks.toString(), startMark, endMark, true);
  }
  
  private String scanPlainSpaces() {
    int length = 0;
    while (this.reader.peek(length) == 32 || this.reader.peek(length) == 9)
      length++; 
    String whitespaces = this.reader.prefixForward(length);
    String lineBreak = scanLineBreak();
    if (lineBreak.length() != 0) {
      this.allowSimpleKey = true;
      String prefix = this.reader.prefix(3);
      if ("---".equals(prefix) || ("...".equals(prefix) && Constant.NULL_BL_T_LINEBR.has(this.reader.peek(3))))
        return ""; 
      StringBuilder breaks = new StringBuilder();
      while (true) {
        while (this.reader.peek() == 32)
          this.reader.forward(); 
        String lb = scanLineBreak();
        if (lb.length() != 0) {
          breaks.append(lb);
          prefix = this.reader.prefix(3);
          if ("---".equals(prefix) || ("...".equals(prefix) && Constant.NULL_BL_T_LINEBR.has(this.reader.peek(3))))
            return ""; 
          continue;
        } 
        break;
      } 
      if (!"\n".equals(lineBreak))
        return lineBreak + breaks; 
      if (breaks.length() == 0)
        return " "; 
      return breaks.toString();
    } 
    return whitespaces;
  }
  
  private String scanTagHandle(String name, Mark startMark) {
    int c = this.reader.peek();
    if (c != 33) {
      String s = String.valueOf(Character.toChars(c));
      throw new ScannerException("while scanning a " + name, startMark, "expected '!', but found " + s + "(" + c + ")", this.reader.getMark());
    } 
    int length = 1;
    c = this.reader.peek(length);
    if (c != 32) {
      while (Constant.ALPHA.has(c)) {
        length++;
        c = this.reader.peek(length);
      } 
      if (c != 33) {
        this.reader.forward(length);
        String s = String.valueOf(Character.toChars(c));
        throw new ScannerException("while scanning a " + name, startMark, "expected '!', but found " + s + "(" + c + ")", this.reader.getMark());
      } 
      length++;
    } 
    String value = this.reader.prefixForward(length);
    return value;
  }
  
  private String scanTagUri(String name, Mark startMark) {
    StringBuilder chunks = new StringBuilder();
    int length = 0;
    int c = this.reader.peek(length);
    while (Constant.URI_CHARS.has(c)) {
      if (c == 37) {
        chunks.append(this.reader.prefixForward(length));
        length = 0;
        chunks.append(scanUriEscapes(name, startMark));
      } else {
        length++;
      } 
      c = this.reader.peek(length);
    } 
    if (length != 0)
      chunks.append(this.reader.prefixForward(length)); 
    if (chunks.length() == 0) {
      String s = String.valueOf(Character.toChars(c));
      throw new ScannerException("while scanning a " + name, startMark, "expected URI, but found " + s + "(" + c + ")", this.reader.getMark());
    } 
    return chunks.toString();
  }
  
  private String scanUriEscapes(String name, Mark startMark) {
    int length = 1;
    while (this.reader.peek(length * 3) == 37)
      length++; 
    Mark beginningMark = this.reader.getMark();
    ByteBuffer buff = ByteBuffer.allocate(length);
    while (this.reader.peek() == 37) {
      this.reader.forward();
      try {
        byte code = (byte)Integer.parseInt(this.reader.prefix(2), 16);
        buff.put(code);
      } catch (NumberFormatException nfe) {
        int c1 = this.reader.peek();
        String s1 = String.valueOf(Character.toChars(c1));
        int c2 = this.reader.peek(1);
        String s2 = String.valueOf(Character.toChars(c2));
        throw new ScannerException("while scanning a " + name, startMark, "expected URI escape sequence of 2 hexadecimal numbers, but found " + s1 + "(" + c1 + ") and " + s2 + "(" + c2 + ")", this.reader.getMark());
      } 
      this.reader.forward(2);
    } 
    buff.flip();
    try {
      return UriEncoder.decode(buff);
    } catch (CharacterCodingException e) {
      throw new ScannerException("while scanning a " + name, startMark, "expected URI in UTF-8: " + e.getMessage(), beginningMark);
    } 
  }
  
  private String scanLineBreak() {
    int c = this.reader.peek();
    if (c == 13 || c == 10 || c == 133) {
      if (c == 13 && 10 == this.reader.peek(1)) {
        this.reader.forward(2);
      } else {
        this.reader.forward();
      } 
      return "\n";
    } 
    if (c == 8232 || c == 8233) {
      this.reader.forward();
      return String.valueOf(Character.toChars(c));
    } 
    return "";
  }
  
  private static class Chomping {
    private final Boolean value;
    
    private final int increment;
    
    public Chomping(Boolean value, int increment) {
      this.value = value;
      this.increment = increment;
    }
    
    public boolean chompTailIsNotFalse() {
      return (this.value == null || this.value.booleanValue());
    }
    
    public boolean chompTailIsTrue() {
      return (this.value != null && this.value.booleanValue());
    }
    
    public int getIncrement() {
      return this.increment;
    }
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\yaml\snakeyaml\scanner\ScannerImpl.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */