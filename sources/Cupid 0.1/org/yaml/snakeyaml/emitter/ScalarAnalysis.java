package org.yaml.snakeyaml.emitter;

public final class ScalarAnalysis {
  private String scalar;
  
  private boolean empty;
  
  private boolean multiline;
  
  private boolean allowFlowPlain;
  
  private boolean allowBlockPlain;
  
  private boolean allowSingleQuoted;
  
  private boolean allowBlock;
  
  public ScalarAnalysis(String scalar, boolean empty, boolean multiline, boolean allowFlowPlain, boolean allowBlockPlain, boolean allowSingleQuoted, boolean allowBlock) {
    this.scalar = scalar;
    this.empty = empty;
    this.multiline = multiline;
    this.allowFlowPlain = allowFlowPlain;
    this.allowBlockPlain = allowBlockPlain;
    this.allowSingleQuoted = allowSingleQuoted;
    this.allowBlock = allowBlock;
  }
  
  public String getScalar() {
    return this.scalar;
  }
  
  public boolean isEmpty() {
    return this.empty;
  }
  
  public boolean isMultiline() {
    return this.multiline;
  }
  
  public boolean isAllowFlowPlain() {
    return this.allowFlowPlain;
  }
  
  public boolean isAllowBlockPlain() {
    return this.allowBlockPlain;
  }
  
  public boolean isAllowSingleQuoted() {
    return this.allowSingleQuoted;
  }
  
  public boolean isAllowBlock() {
    return this.allowBlock;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\yaml\snakeyaml\emitter\ScalarAnalysis.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */