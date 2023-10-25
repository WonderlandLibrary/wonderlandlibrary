/*
 * Decompiled with CFR 0.150.
 */
package com.sun.javafx.css.parser;

import com.sun.javafx.css.parser.Recognizer;

public class LexerState {
    private final int type;
    private final String name;
    private final Recognizer[] recognizers;

    public boolean accepts(int n) {
        int n2 = this.recognizers != null ? this.recognizers.length : 0;
        for (int i = 0; i < n2; ++i) {
            if (!this.recognizers[i].recognize(n)) continue;
            return true;
        }
        return false;
    }

    public int getType() {
        return this.type;
    }

    public LexerState(int n, String string, Recognizer recognizer, Recognizer ... arrrecognizer) {
        assert (string != null);
        this.type = n;
        this.name = string;
        if (recognizer != null) {
            int n2 = 1 + (arrrecognizer != null ? arrrecognizer.length : 0);
            this.recognizers = new Recognizer[n2];
            this.recognizers[0] = recognizer;
            for (int i = 1; i < this.recognizers.length; ++i) {
                this.recognizers[i] = arrrecognizer[i - 1];
            }
        } else {
            this.recognizers = null;
        }
    }

    public LexerState(String string, Recognizer recognizer, Recognizer ... arrrecognizer) {
        this(0, string, recognizer, arrrecognizer);
    }

    private LexerState() {
        this(0, "invalid", null, new Recognizer[0]);
    }

    public String toString() {
        return this.name;
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        return object instanceof LexerState ? this.name.equals(((LexerState)object).name) : false;
    }

    public int hashCode() {
        return this.name.hashCode();
    }
}

