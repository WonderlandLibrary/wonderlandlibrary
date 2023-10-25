package joptsimple.internal;

import java.util.LinkedHashSet;
import java.util.Set;

public class Rows {
  private final int overallWidth;
  
  private final int columnSeparatorWidth;
  
  private final Set<Row> rows = new LinkedHashSet<Row>();
  
  private int widthOfWidestOption;
  
  private int widthOfWidestDescription;
  
  public Rows(int overallWidth, int columnSeparatorWidth) {
    this.overallWidth = overallWidth;
    this.columnSeparatorWidth = columnSeparatorWidth;
  }
  
  public void add(String option, String description) {
    add(new Row(option, description));
  }
  
  private void add(Row row) {
    this.rows.add(row);
    this.widthOfWidestOption = Math.max(this.widthOfWidestOption, row.option.length());
    this.widthOfWidestDescription = Math.max(this.widthOfWidestDescription, row.description.length());
  }
  
  private void reset() {
    this.rows.clear();
    this.widthOfWidestOption = 0;
    this.widthOfWidestDescription = 0;
  }
  
  public void fitToWidth() {
    Columns columns = new Columns(optionWidth(), descriptionWidth());
    Set<Row> fitted = new LinkedHashSet<Row>();
    for (Row each : this.rows)
      fitted.addAll(columns.fit(each)); 
    reset();
    for (Row each : fitted)
      add(each); 
  }
  
  public String render() {
    StringBuilder buffer = new StringBuilder();
    for (Row each : this.rows) {
      pad(buffer, each.option, optionWidth()).append(Strings.repeat(' ', this.columnSeparatorWidth));
      pad(buffer, each.description, descriptionWidth()).append(Strings.LINE_SEPARATOR);
    } 
    return buffer.toString();
  }
  
  private int optionWidth() {
    return Math.min((this.overallWidth - this.columnSeparatorWidth) / 2, this.widthOfWidestOption);
  }
  
  private int descriptionWidth() {
    return Math.min((this.overallWidth - this.columnSeparatorWidth) / 2, this.widthOfWidestDescription);
  }
  
  private StringBuilder pad(StringBuilder buffer, String s, int length) {
    buffer.append(s).append(Strings.repeat(' ', length - s.length()));
    return buffer;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\joptsimple\internal\Rows.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */