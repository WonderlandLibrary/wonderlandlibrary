/*
 * Decompiled with CFR 0.152.
 */
package jdk.nashorn.internal.runtime;

import java.util.LinkedList;
import java.util.Stack;
import java.util.StringTokenizer;

public final class QuotedStringTokenizer {
    private final LinkedList<String> tokens;
    private final char[] quotes;

    public QuotedStringTokenizer(String str) {
        this(str, " ");
    }

    public QuotedStringTokenizer(String str, String delim) {
        this(str, delim, new char[]{'\"', '\''});
    }

    private QuotedStringTokenizer(String str, String delim, char[] quotes) {
        this.quotes = quotes;
        boolean delimIsWhitespace = true;
        for (int i = 0; i < delim.length(); ++i) {
            if (Character.isWhitespace(delim.charAt(i))) continue;
            delimIsWhitespace = false;
            break;
        }
        StringTokenizer st = new StringTokenizer(str, delim);
        this.tokens = new LinkedList();
        while (st.hasMoreTokens()) {
            String token = st.nextToken();
            while (this.unmatchedQuotesIn(token)) {
                if (!st.hasMoreTokens()) {
                    throw new IndexOutOfBoundsException(token);
                }
                token = token + (delimIsWhitespace ? " " : delim) + st.nextToken();
            }
            this.tokens.add(this.stripQuotes(token));
        }
    }

    public int countTokens() {
        return this.tokens.size();
    }

    public boolean hasMoreTokens() {
        return this.countTokens() > 0;
    }

    public String nextToken() {
        return this.tokens.removeFirst();
    }

    private String stripQuotes(String value0) {
        String value = value0.trim();
        for (char q : this.quotes) {
            if (value.length() < 2 || !value.startsWith("" + q) || !value.endsWith("" + q)) continue;
            value = value.substring(1, value.length() - 1);
            value = value.replace("\\" + q, "" + q);
        }
        return value;
    }

    private boolean unmatchedQuotesIn(String str) {
        Stack<Character> quoteStack = new Stack<Character>();
        for (int i = 0; i < str.length(); ++i) {
            char c = str.charAt(i);
            for (char q : this.quotes) {
                if (c != q) continue;
                if (quoteStack.isEmpty()) {
                    quoteStack.push(Character.valueOf(c));
                    continue;
                }
                char top = ((Character)quoteStack.pop()).charValue();
                if (top == c) continue;
                quoteStack.push(Character.valueOf(top));
                quoteStack.push(Character.valueOf(c));
            }
        }
        return !quoteStack.isEmpty();
    }
}

