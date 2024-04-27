package me.aquavit.liquidsense.command.shortcuts;

import java.util.ArrayList;
import java.util.List;

public final class ShortcutParser {
    private static final int SEPARATOR = ';';

    public static List<List<String>> parse(String script) {
        List<Token> tokens = tokenize(script);

        List<List<String>> parsed = new ArrayList<>();
        List<String> tmpStatement = new ArrayList<>();

        for (Token token : tokens) {
            if (token instanceof Literal) {
                tmpStatement.add(((Literal) token).getLiteral());
            } else if (token instanceof StatementEnd) {
                parsed.add(new ArrayList<>(tmpStatement));
                tmpStatement.clear();
            }
        }

        if (!tmpStatement.isEmpty()) {
            throw new IllegalArgumentException("Unexpected end of statement!");
        }

        return parsed;
    }

    private static List<Token> tokenize(String script) {
        List<Token> tokens = new ArrayList<>();
        StringBuilder tokenBuf = new StringBuilder();

        for (int i = 0; i < script.length(); i++) {
            int code = script.codePointAt(i);
            if (Character.isWhitespace(code)) {
                finishLiteral(tokens, tokenBuf);
            } else if (code == SEPARATOR) {
                finishLiteral(tokens, tokenBuf);
                tokens.add(new StatementEnd());
            } else {
                tokenBuf.appendCodePoint(code);
            }
        }

        if (tokenBuf.length() > 0) {
            throw new IllegalArgumentException("Unexpected end of literal!");
        }

        return tokens;
    }

    private static void finishLiteral(List<Token> tokens, StringBuilder tokenBuf) {
        if (tokenBuf.length() > 0) {
            tokens.add(new Literal(tokenBuf.toString()));
            tokenBuf.setLength(0);
        }
    }

    private static abstract class Token {
    }

    private static class Literal extends Token {
        private final String literal;

        public Literal(String literal) {
            this.literal = literal;
        }

        public String getLiteral() {
            return literal;
        }
    }

    private static class StatementEnd extends Token {
    }
}
