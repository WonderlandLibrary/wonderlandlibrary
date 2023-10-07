/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jetbrains.annotations.NotNull
 */
package net.ccbluex.liquidbounce.features.command.shortcuts;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.PrimitiveIterator;
import kotlin.Metadata;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import net.ccbluex.liquidbounce.features.command.shortcuts.Literal;
import net.ccbluex.liquidbounce.features.command.shortcuts.StatementEnd;
import net.ccbluex.liquidbounce.features.command.shortcuts.Token;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, xi=2, d1={"\u00008\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010!\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0010\u000e\n\u0002\b\u0003\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\"\u0010\u0005\u001a\u00020\u00062\f\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\t0\b2\n\u0010\n\u001a\u00060\u000bj\u0002`\fH\u0002J\u001a\u0010\r\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u000f0\u000e0\u000e2\u0006\u0010\u0010\u001a\u00020\u000fJ\u0016\u0010\u0011\u001a\b\u0012\u0004\u0012\u00020\t0\u000e2\u0006\u0010\u0010\u001a\u00020\u000fH\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0012"}, d2={"Lnet/ccbluex/liquidbounce/features/command/shortcuts/ShortcutParser;", "", "()V", "SEPARATOR", "", "finishLiteral", "", "tokens", "", "Lnet/ccbluex/liquidbounce/features/command/shortcuts/Token;", "tokenBuf", "Ljava/lang/StringBuilder;", "Lkotlin/text/StringBuilder;", "parse", "", "", "script", "tokenize", "KyinoClient"})
public final class ShortcutParser {
    private static final int SEPARATOR;
    public static final ShortcutParser INSTANCE;

    @NotNull
    public final List<List<String>> parse(@NotNull String script) {
        Intrinsics.checkParameterIsNotNull(script, "script");
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
            object = CollectionsKt.toList(tmpStatement);
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
            Integer n = code;
            Intrinsics.checkExpressionValueIsNotNull(n, "code");
            if (Character.isWhitespace(n)) {
                this.finishLiteral(tokens, tokenBuf);
                continue;
            }
            int n2 = SEPARATOR;
            if (code == n2) {
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
            String string = tokenBuf.toString();
            Intrinsics.checkExpressionValueIsNotNull(string, "tokenBuf.toString()");
            Literal literal = new Literal(string);
            boolean bl2 = false;
            object.add(literal);
            StringsKt.clear(tokenBuf);
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

