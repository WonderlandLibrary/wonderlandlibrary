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

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, d1={"\u00008\n\n\u0000\n\b\n\b\n\u0000\n\n\u0000\n!\n\n\u0000\n\n\n\u0000\n \n\n\b\bÆ\u000020B\b¢J\"02\f\b0\t0\b2\n\n0j`\fHJ\r\n\b00020J\b0\t020HR0X¢\n\u0000¨"}, d2={"Lnet/ccbluex/liquidbounce/features/command/shortcuts/ShortcutParser;", "", "()V", "SEPARATOR", "", "finishLiteral", "", "tokens", "", "Lnet/ccbluex/liquidbounce/features/command/shortcuts/Token;", "tokenBuf", "Ljava/lang/StringBuilder;", "Lkotlin/text/StringBuilder;", "parse", "", "", "script", "tokenize", "Pride"})
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
