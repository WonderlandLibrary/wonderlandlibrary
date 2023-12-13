/*
 * Decompiled with CFR 0.152.
 */
package org.yaml.snakeyaml.error;

import java.io.Serializable;
import org.yaml.snakeyaml.scanner.Constant;

public final class Mark
implements Serializable {
    private String name;
    private int index;
    private int line;
    private int column;
    private int[] buffer;
    private int pointer;

    private static int[] toCodePoints(char[] str) {
        int[] codePoints = new int[Character.codePointCount(str, 0, str.length)];
        int i2 = 0;
        int c2 = 0;
        while (i2 < str.length) {
            int cp;
            codePoints[c2] = cp = Character.codePointAt(str, i2);
            i2 += Character.charCount(cp);
            ++c2;
        }
        return codePoints;
    }

    public Mark(String name, int index, int line, int column, char[] str, int pointer) {
        this(name, index, line, column, Mark.toCodePoints(str), pointer);
    }

    @Deprecated
    public Mark(String name, int index, int line, int column, String buffer, int pointer) {
        this(name, index, line, column, buffer.toCharArray(), pointer);
    }

    public Mark(String name, int index, int line, int column, int[] buffer, int pointer) {
        this.name = name;
        this.index = index;
        this.line = line;
        this.column = column;
        this.buffer = buffer;
        this.pointer = pointer;
    }

    private boolean isLineBreak(int c2) {
        return Constant.NULL_OR_LINEBR.has(c2);
    }

    public String get_snippet(int indent, int max_length) {
        int i2;
        float half = (float)max_length / 2.0f - 1.0f;
        int start = this.pointer;
        String head = "";
        while (start > 0 && !this.isLineBreak(this.buffer[start - 1])) {
            if (!((float)(this.pointer - --start) > half)) continue;
            head = " ... ";
            start += 5;
            break;
        }
        String tail = "";
        int end = this.pointer;
        while (end < this.buffer.length && !this.isLineBreak(this.buffer[end])) {
            if (!((float)(++end - this.pointer) > half)) continue;
            tail = " ... ";
            end -= 5;
            break;
        }
        StringBuilder result = new StringBuilder();
        for (i2 = 0; i2 < indent; ++i2) {
            result.append(" ");
        }
        result.append(head);
        for (i2 = start; i2 < end; ++i2) {
            result.appendCodePoint(this.buffer[i2]);
        }
        result.append(tail);
        result.append("\n");
        for (i2 = 0; i2 < indent + this.pointer - start + head.length(); ++i2) {
            result.append(" ");
        }
        result.append("^");
        return result.toString();
    }

    public String get_snippet() {
        return this.get_snippet(4, 75);
    }

    public String toString() {
        String snippet = this.get_snippet();
        StringBuilder builder = new StringBuilder(" in ");
        builder.append(this.name);
        builder.append(", line ");
        builder.append(this.line + 1);
        builder.append(", column ");
        builder.append(this.column + 1);
        builder.append(":\n");
        builder.append(snippet);
        return builder.toString();
    }

    public String getName() {
        return this.name;
    }

    public int getLine() {
        return this.line;
    }

    public int getColumn() {
        return this.column;
    }

    public int getIndex() {
        return this.index;
    }

    public int[] getBuffer() {
        return this.buffer;
    }

    public int getPointer() {
        return this.pointer;
    }
}

