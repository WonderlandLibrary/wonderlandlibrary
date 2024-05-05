/*
 * Decompiled with CFR 0.152.
 */
package jdk.nashorn.internal.runtime;

import jdk.nashorn.api.scripting.NashornException;
import jdk.nashorn.internal.objects.Global;
import jdk.nashorn.internal.parser.Token;
import jdk.nashorn.internal.runtime.ECMAErrors;
import jdk.nashorn.internal.runtime.JSErrorType;
import jdk.nashorn.internal.runtime.Source;

public final class ParserException
extends NashornException {
    private final Source source;
    private final long token;
    private final JSErrorType errorType;

    public ParserException(String msg) {
        this(JSErrorType.SYNTAX_ERROR, msg, null, -1, -1, -1L);
    }

    public ParserException(JSErrorType errorType, String msg, Source source, int line, int column, long token) {
        super(msg, source != null ? source.getName() : null, line, column);
        this.source = source;
        this.token = token;
        this.errorType = errorType;
    }

    public Source getSource() {
        return this.source;
    }

    public long getToken() {
        return this.token;
    }

    public int getPosition() {
        return Token.descPosition(this.token);
    }

    public JSErrorType getErrorType() {
        return this.errorType;
    }

    public void throwAsEcmaException() {
        throw ECMAErrors.asEcmaException(this);
    }

    public void throwAsEcmaException(Global global) {
        throw ECMAErrors.asEcmaException(global, this);
    }
}

