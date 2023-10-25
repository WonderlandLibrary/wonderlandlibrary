package joptsimple.internal;

import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

class Columns {
  private static final int INDENT_WIDTH = 2;
  
  private final int optionWidth;
  
  private final int descriptionWidth;
  
  Columns(int optionWidth, int descriptionWidth) {
    this.optionWidth = optionWidth;
    this.descriptionWidth = descriptionWidth;
  }
  
  List<Row> fit(Row row) {
    List<String> options = piecesOf(row.option, this.optionWidth);
    List<String> descriptions = piecesOf(row.description, this.descriptionWidth);
    List<Row> rows = new ArrayList<Row>();
    for (int i = 0; i < Math.max(options.size(), descriptions.size()); i++)
      rows.add(new Row(itemOrEmpty(options, i), itemOrEmpty(descriptions, i))); 
    return rows;
  }
  
  private static String itemOrEmpty(List<String> items, int index) {
    return (index >= items.size()) ? "" : items.get(index);
  }
  
  private List<String> piecesOf(String raw, int width) {
    List<String> pieces = new ArrayList<String>();
    for (String each : raw.trim().split(Strings.LINE_SEPARATOR))
      pieces.addAll(piecesOfEmbeddedLine(each, width)); 
    return pieces;
  }
  
  private List<String> piecesOfEmbeddedLine(String line, int width) {
    List<String> pieces = new ArrayList<String>();
    BreakIterator words = BreakIterator.getLineInstance(Locale.US);
    words.setText(line);
    StringBuilder nextPiece = new StringBuilder();
    int start = words.first();
    int end;
    for (end = words.next(); end != -1; start = end, end = words.next())
      nextPiece = processNextWord(line, nextPiece, start, end, width, pieces); 
    if (nextPiece.length() > 0)
      pieces.add(nextPiece.toString()); 
    return pieces;
  }
  
  private StringBuilder processNextWord(String source, StringBuilder nextPiece, int start, int end, int width, List<String> pieces) {
    StringBuilder augmented = nextPiece;
    String word = source.substring(start, end);
    if (augmented.length() + word.length() > width) {
      pieces.add(augmented.toString().replaceAll("\\s+$", ""));
      augmented = (new StringBuilder(Strings.repeat(' ', 2))).append(word);
    } else {
      augmented.append(word);
    } 
    return augmented;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\joptsimple\internal\Columns.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */