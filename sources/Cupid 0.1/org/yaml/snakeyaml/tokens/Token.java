package org.yaml.snakeyaml.tokens;

import org.yaml.snakeyaml.error.Mark;
import org.yaml.snakeyaml.error.YAMLException;

public abstract class Token {
  private final Mark startMark;
  
  private final Mark endMark;
  
  public enum ID {
    Alias("<alias>"),
    Anchor("<anchor>"),
    BlockEnd("<block end>"),
    BlockEntry("-"),
    BlockMappingStart("<block mapping start>"),
    BlockSequenceStart("<block sequence start>"),
    Directive("<directive>"),
    DocumentEnd("<document end>"),
    DocumentStart("<document start>"),
    FlowEntry(","),
    FlowMappingEnd("}"),
    FlowMappingStart("{"),
    FlowSequenceEnd("]"),
    FlowSequenceStart("["),
    Key("?"),
    Scalar("<scalar>"),
    StreamEnd("<stream end>"),
    StreamStart("<stream start>"),
    Tag("<tag>"),
    Value(":"),
    Whitespace("<whitespace>"),
    Comment("#"),
    Error("<error>");
    
    private final String description;
    
    ID(String s) {
      this.description = s;
    }
    
    public String toString() {
      return this.description;
    }
  }
  
  public Token(Mark startMark, Mark endMark) {
    if (startMark == null || endMark == null)
      throw new YAMLException("Token requires marks."); 
    this.startMark = startMark;
    this.endMark = endMark;
  }
  
  public Mark getStartMark() {
    return this.startMark;
  }
  
  public Mark getEndMark() {
    return this.endMark;
  }
  
  public abstract ID getTokenId();
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\yaml\snakeyaml\tokens\Token.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */