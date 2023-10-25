package org.json;

import java.io.StringWriter;

public class JSONStringer extends JSONWriter {
  public JSONStringer() {
    super(new StringWriter());
  }
  
  public String toString() {
    return (this.mode == 'd') ? this.writer.toString() : null;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\json\JSONStringer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */