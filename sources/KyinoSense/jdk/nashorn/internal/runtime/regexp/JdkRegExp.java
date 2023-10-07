/*
 * Decompiled with CFR 0.152.
 */
package jdk.nashorn.internal.runtime.regexp;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import jdk.nashorn.internal.runtime.ParserException;
import jdk.nashorn.internal.runtime.regexp.RegExp;
import jdk.nashorn.internal.runtime.regexp.RegExpMatcher;
import jdk.nashorn.internal.runtime.regexp.RegExpScanner;

public class JdkRegExp
extends RegExp {
    private Pattern pattern;

    public JdkRegExp(String source, String flags) throws ParserException {
        super(source, flags);
        int intFlags = 0;
        if (this.isIgnoreCase()) {
            intFlags |= 0x42;
        }
        if (this.isMultiline()) {
            intFlags |= 8;
        }
        try {
            RegExpScanner parsed;
            try {
                parsed = RegExpScanner.scan(source);
            }
            catch (PatternSyntaxException e) {
                Pattern.compile(source, intFlags);
                throw e;
            }
            if (parsed != null) {
                this.pattern = Pattern.compile(parsed.getJavaPattern(), intFlags);
                this.groupsInNegativeLookahead = parsed.getGroupsInNegativeLookahead();
            }
        }
        catch (PatternSyntaxException e2) {
            JdkRegExp.throwParserException("syntax", e2.getMessage());
        }
    }

    @Override
    public RegExpMatcher match(String str) {
        if (this.pattern == null) {
            return null;
        }
        return new DefaultMatcher(str);
    }

    class DefaultMatcher
    implements RegExpMatcher {
        final String input;
        final Matcher defaultMatcher;

        DefaultMatcher(String input) {
            this.input = input;
            this.defaultMatcher = JdkRegExp.this.pattern.matcher(input);
        }

        @Override
        public boolean search(int start) {
            return this.defaultMatcher.find(start);
        }

        @Override
        public String getInput() {
            return this.input;
        }

        @Override
        public int start() {
            return this.defaultMatcher.start();
        }

        @Override
        public int start(int group) {
            return this.defaultMatcher.start(group);
        }

        @Override
        public int end() {
            return this.defaultMatcher.end();
        }

        @Override
        public int end(int group) {
            return this.defaultMatcher.end(group);
        }

        @Override
        public String group() {
            return this.defaultMatcher.group();
        }

        @Override
        public String group(int group) {
            return this.defaultMatcher.group(group);
        }

        @Override
        public int groupCount() {
            return this.defaultMatcher.groupCount();
        }
    }
}

