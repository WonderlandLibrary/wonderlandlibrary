package org.apache.commons.lang3.builder;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class DiffResult implements Iterable<Diff<?>> {
  public static final String OBJECTS_SAME_STRING = "";
  
  private static final String DIFFERS_STRING = "differs from";
  
  private final List<Diff<?>> diffs;
  
  private final Object lhs;
  
  private final Object rhs;
  
  private final ToStringStyle style;
  
  DiffResult(Object lhs, Object rhs, List<Diff<?>> diffs, ToStringStyle style) {
    if (lhs == null)
      throw new IllegalArgumentException("Left hand object cannot be null"); 
    if (rhs == null)
      throw new IllegalArgumentException("Right hand object cannot be null"); 
    if (diffs == null)
      throw new IllegalArgumentException("List of differences cannot be null"); 
    this.diffs = diffs;
    this.lhs = lhs;
    this.rhs = rhs;
    if (style == null) {
      this.style = ToStringStyle.DEFAULT_STYLE;
    } else {
      this.style = style;
    } 
  }
  
  public List<Diff<?>> getDiffs() {
    return Collections.unmodifiableList(this.diffs);
  }
  
  public int getNumberOfDiffs() {
    return this.diffs.size();
  }
  
  public ToStringStyle getToStringStyle() {
    return this.style;
  }
  
  public String toString() {
    return toString(this.style);
  }
  
  public String toString(ToStringStyle style) {
    if (this.diffs.size() == 0)
      return ""; 
    ToStringBuilder lhsBuilder = new ToStringBuilder(this.lhs, style);
    ToStringBuilder rhsBuilder = new ToStringBuilder(this.rhs, style);
    for (Diff<?> diff : this.diffs) {
      lhsBuilder.append(diff.getFieldName(), diff.getLeft());
      rhsBuilder.append(diff.getFieldName(), diff.getRight());
    } 
    return String.format("%s %s %s", new Object[] { lhsBuilder.build(), "differs from", rhsBuilder.build() });
  }
  
  public Iterator<Diff<?>> iterator() {
    return this.diffs.iterator();
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\apache\commons\lang3\builder\DiffResult.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */