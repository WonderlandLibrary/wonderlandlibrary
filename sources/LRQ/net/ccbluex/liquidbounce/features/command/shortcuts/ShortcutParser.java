/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.collections.CollectionsKt
 *  kotlin.text.StringsKt
 */
package net.ccbluex.liquidbounce.features.command.shortcuts;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.PrimitiveIterator;
import kotlin.collections.CollectionsKt;
import kotlin.text.StringsKt;
import net.ccbluex.liquidbounce.features.command.shortcuts.Literal;
import net.ccbluex.liquidbounce.features.command.shortcuts.StatementEnd;
import net.ccbluex.liquidbounce.features.command.shortcuts.Token;

public final class ShortcutParser {
    private static final int SEPARATOR;
    public static final ShortcutParser INSTANCE;

    public final List<List<String>> parse(String script) {
        List<Token> tokens = this.tokenize(script);
        boolean bl = false;
        List parsed = new ArrayList();
        boolean bl2 = false;
        List tmpStatement = new ArrayList();
        for (Token token : tokens) {
            boolean bl3;
            Object object;
            Collection collection;
            Token token2 = token;
            if (token2 instanceof Literal) {
                collection = tmpStatement;
                object = ((Literal)token).getLiteral();
                bl3 = false;
                collection.add(object);
                continue;
            }
            if (!(token2 instanceof StatementEnd)) continue;
            collection = parsed;
            object = CollectionsKt.toList((Iterable)tmpStatement);
            bl3 = false;
            collection.add(object);
            tmpStatement.clear();
        }
        Collection collection = tmpStatement;
        boolean bl4 = false;
        if (!collection.isEmpty()) {
            throw (Throwable)new IllegalArgumentException("Unexpected end of statement!");
        }
        return parsed;
    }

    private final List<Token> tokenize(String script) {
        boolean bl = false;
        List tokens = new ArrayList();
        StringBuilder tokenBuf = new StringBuilder();
        PrimitiveIterator.OfInt ofInt = script.codePoints().iterator();
        while (ofInt.hasNext()) {
            Integer code = ofInt.next();
            if (Character.isWhitespace(code)) {
                this.finishLiteral(tokens, tokenBuf);
                continue;
            }
            int n = SEPARATOR;
            if (code == n) {
                this.finishLiteral(tokens, tokenBuf);
                Collection collection = tokens;
                StatementEnd statementEnd = new StatementEnd();
                boolean bl2 = false;
                collection.add(statementEnd);
                continue;
            }
            tokenBuf.appendCodePoint(code);
        }
        CharSequence charSequence = tokenBuf;
        boolean bl3 = false;
        if (charSequence.length() > 0) {
            throw (Throwable)new IllegalArgumentException("Unexpected end of literal!");
        }
        return tokens;
    }

    private final void finishLiteral(List<Token> tokens, StringBuilder tokenBuf) {
        Object object = tokenBuf;
        boolean bl = false;
        if (object.length() > 0) {
            object = tokens;
            Literal literal = new Literal(tokenBuf.toString());
            boolean bl2 = false;
            object.add(literal);
            StringsKt.clear((StringBuilder)tokenBuf);
        }
    }

    private ShortcutParser() {
    }

    static {
        ShortcutParser shortcutParser;
        INSTANCE = shortcutParser = new ShortcutParser();
        String string = ";";
        int n = 0;
        boolean bl = false;
        SEPARATOR = string.codePointAt(n);
    }
}

