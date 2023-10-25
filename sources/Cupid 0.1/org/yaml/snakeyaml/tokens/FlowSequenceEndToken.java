package org.yaml.snakeyaml.tokens;

import org.yaml.snakeyaml.error.Mark;

public final class FlowSequenceEndToken extends Token {
  public FlowSequenceEndToken(Mark startMark, Mark endMark) {
    super(startMark, endMark);
  }
  
  public Token.ID getTokenId() {
    return Token.ID.FlowSequenceEnd;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\yaml\snakeyaml\tokens\FlowSequenceEndToken.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */