package org.yaml.snakeyaml.tokens;

import org.yaml.snakeyaml.error.Mark;

public final class AnchorToken extends Token {
  private final String value;
  
  public AnchorToken(String value, Mark startMark, Mark endMark) {
    super(startMark, endMark);
    this.value = value;
  }
  
  public String getValue() {
    return this.value;
  }
  
  public Token.ID getTokenId() {
    return Token.ID.Anchor;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\yaml\snakeyaml\tokens\AnchorToken.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */