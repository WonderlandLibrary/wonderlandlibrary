/*
 * Decompiled with CFR 0.152.
 */
package jdk.nashorn.internal.runtime.regexp;

import jdk.nashorn.internal.runtime.BitVector;
import jdk.nashorn.internal.runtime.ECMAErrors;
import jdk.nashorn.internal.runtime.ParserException;
import jdk.nashorn.internal.runtime.regexp.RegExpMatcher;

public abstract class RegExp {
    private final String source;
    private boolean global;
    private boolean ignoreCase;
    private boolean multiline;
    protected BitVector groupsInNegativeLookahead;

    protected RegExp(String source, String flags) {
        this.source = source.length() == 0 ? "(?:)" : source;
        block5: for (int i = 0; i < flags.length(); ++i) {
            char ch = flags.charAt(i);
            switch (ch) {
                case 'g': {
                    if (this.global) {
                        RegExp.throwParserException("repeated.flag", "g");
                    }
                    this.global = true;
                    continue block5;
                }
                case 'i': {
                    if (this.ignoreCase) {
                        RegExp.throwParserException("repeated.flag", "i");
                    }
                    this.ignoreCase = true;
                    continue block5;
                }
                case 'm': {
                    if (this.multiline) {
                        RegExp.throwParserException("repeated.flag", "m");
                    }
                    this.multiline = true;
                    continue block5;
                }
                default: {
                    RegExp.throwParserException("unsupported.flag", Character.toString(ch));
                }
            }
        }
    }

    public String getSource() {
        return this.source;
    }

    public void setGlobal(boolean global) {
        this.global = global;
    }

    public boolean isGlobal() {
        return this.global;
    }

    public boolean isIgnoreCase() {
        return this.ignoreCase;
    }

    public boolean isMultiline() {
        return this.multiline;
    }

    public BitVector getGroupsInNegativeLookahead() {
        return this.groupsInNegativeLookahead;
    }

    public abstract RegExpMatcher match(String var1);

    protected static void throwParserException(String key, String str) throws ParserException {
        throw new ParserException(ECMAErrors.getMessage("parser.error.regex." + key, str));
    }
}

