/*
 * Decompiled with CFR 0.152.
 */
package jdk.nashorn.internal.runtime;

import java.io.PrintWriter;
import jdk.nashorn.internal.parser.Token;
import jdk.nashorn.internal.runtime.ECMAErrors;
import jdk.nashorn.internal.runtime.ParserException;
import jdk.nashorn.internal.runtime.Source;

public class ErrorManager {
    private final PrintWriter writer;
    private int errors;
    private int warnings;
    private int limit;
    private boolean warningsAsErrors;

    public ErrorManager() {
        this(new PrintWriter(System.err, true));
    }

    public ErrorManager(PrintWriter writer) {
        this.writer = writer;
        this.limit = 100;
        this.warningsAsErrors = false;
    }

    private void checkLimit() {
        int count = this.errors;
        if (this.warningsAsErrors) {
            count += this.warnings;
        }
        if (this.limit != 0 && count > this.limit) {
            throw ECMAErrors.rangeError("too.many.errors", Integer.toString(this.limit));
        }
    }

    public static String format(String message, Source source, int line, int column, long token) {
        String eoln = System.lineSeparator();
        int position = Token.descPosition(token);
        StringBuilder sb = new StringBuilder();
        sb.append(source.getName()).append(':').append(line).append(':').append(column).append(' ').append(message).append(eoln);
        String sourceLine = source.getSourceLine(position);
        sb.append(sourceLine).append(eoln);
        for (int i = 0; i < column; ++i) {
            if (i < sourceLine.length() && sourceLine.charAt(i) == '\t') {
                sb.append('\t');
                continue;
            }
            sb.append(' ');
        }
        sb.append('^');
        return sb.toString();
    }

    public void error(ParserException e) {
        this.error(e.getMessage());
    }

    public void error(String message) {
        this.writer.println(message);
        this.writer.flush();
        ++this.errors;
        this.checkLimit();
    }

    public void warning(ParserException e) {
        this.warning(e.getMessage());
    }

    public void warning(String message) {
        this.writer.println(message);
        this.writer.flush();
        ++this.warnings;
        this.checkLimit();
    }

    public boolean hasErrors() {
        return this.errors != 0;
    }

    public int getLimit() {
        return this.limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public boolean isWarningsAsErrors() {
        return this.warningsAsErrors;
    }

    public void setWarningsAsErrors(boolean warningsAsErrors) {
        this.warningsAsErrors = warningsAsErrors;
    }

    public int getNumberOfErrors() {
        return this.errors;
    }

    public int getNumberOfWarnings() {
        return this.warnings;
    }

    void reset() {
        this.warnings = 0;
        this.errors = 0;
    }
}

