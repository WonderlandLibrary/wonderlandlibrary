package org.yaml.snakeyaml.tokens;

import org.yaml.snakeyaml.error.Mark;

public class WhitespaceToken extends Token {
  public WhitespaceToken(Mark startMark, Mark endMark) {
    super(startMark, endMark);
  }
  
  public Token.ID getTokenId() {
    return Token.ID.Whitespace;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\yaml\snakeyaml\tokens\WhitespaceToken.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */