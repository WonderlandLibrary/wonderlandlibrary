package org.yaml.snakeyaml.tokens;

import org.yaml.snakeyaml.error.Mark;

public final class TagToken extends Token {
  private final TagTuple value;
  
  public TagToken(TagTuple value, Mark startMark, Mark endMark) {
    super(startMark, endMark);
    this.value = value;
  }
  
  public TagTuple getValue() {
    return this.value;
  }
  
  public Token.ID getTokenId() {
    return Token.ID.Tag;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\yaml\snakeyaml\tokens\TagToken.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */