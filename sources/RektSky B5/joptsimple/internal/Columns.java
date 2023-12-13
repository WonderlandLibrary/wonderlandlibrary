/*
 * Decompiled with CFR 0.152.
 */
package joptsimple.internal;

import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import joptsimple.internal.Row;
import joptsimple.internal.Strings;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
class Columns {
    private static final int INDENT_WIDTH = 2;
    private final int optionWidth;
    private final int descriptionWidth;

    Columns(int optionWidth, int descriptionWidth) {
        this.optionWidth = optionWidth;
        this.descriptionWidth = descriptionWidth;
    }

    List<Row> fit(Row row) {
        List<String> options = this.piecesOf(row.option, this.optionWidth);
        List<String> descriptions = this.piecesOf(row.description, this.descriptionWidth);
        ArrayList<Row> rows = new ArrayList<Row>();
        for (int i2 = 0; i2 < Math.max(options.size(), descriptions.size()); ++i2) {
            rows.add(new Row(Columns.itemOrEmpty(options, i2), Columns.itemOrEmpty(descriptions, i2)));
        }
        return rows;
    }

    private static String itemOrEmpty(List<String> items, int index) {
        return index >= items.size() ? "" : items.get(index);
    }

    private List<String> piecesOf(String raw, int width) {
        ArrayList<String> pieces = new ArrayList<String>();
        for (String each : raw.trim().split(Strings.LINE_SEPARATOR)) {
            pieces.addAll(this.piecesOfEmbeddedLine(each, width));
        }
        return pieces;
    }

    private List<String> piecesOfEmbeddedLine(String line, int width) {
        ArrayList<String> pieces = new ArrayList<String>();
        BreakIterator words = BreakIterator.getLineInstance(Locale.US);
        words.setText(line);
        StringBuilder nextPiece = new StringBuilder();
        int start = words.first();
        int end = words.next();
        while (end != -1) {
            nextPiece = this.processNextWord(line, nextPiece, start, end, width, pieces);
            start = end;
            end = words.next();
        }
        if (nextPiece.length() > 0) {
            pieces.add(nextPiece.toString());
        }
        return pieces;
    }

    private StringBuilder processNextWord(String source, StringBuilder nextPiece, int start, int end, int width, List<String> pieces) {
        StringBuilder augmented = nextPiece;
        String word = source.substring(start, end);
        if (augmented.length() + word.length() > width) {
            pieces.add(augmented.toString().replaceAll("\\s+$", ""));
            augmented = new StringBuilder(Strings.repeat(' ', 2)).append(word);
        } else {
            augmented.append(word);
        }
        return augmented;
    }
}

