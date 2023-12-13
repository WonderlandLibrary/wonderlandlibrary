/*
 * Decompiled with CFR 0.152.
 */
package joptsimple;

import java.util.NoSuchElementException;
import joptsimple.AbstractOptionSpec;
import joptsimple.AlternativeLongOptionSpec;
import joptsimple.NoArgumentOptionSpec;
import joptsimple.OptionParser;
import joptsimple.OptionalArgumentOptionSpec;
import joptsimple.ParserRules;
import joptsimple.RequiredArgumentOptionSpec;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
class OptionSpecTokenizer {
    private static final char POSIXLY_CORRECT_MARKER = '+';
    private static final char HELP_MARKER = '*';
    private String specification;
    private int index;

    OptionSpecTokenizer(String specification) {
        if (specification == null) {
            throw new NullPointerException("null option specification");
        }
        this.specification = specification;
    }

    boolean hasMore() {
        return this.index < this.specification.length();
    }

    AbstractOptionSpec<?> next() {
        NoArgumentOptionSpec spec;
        if (!this.hasMore()) {
            throw new NoSuchElementException();
        }
        String optionCandidate = String.valueOf(this.specification.charAt(this.index));
        ++this.index;
        if ("W".equals(optionCandidate) && (spec = this.handleReservedForExtensionsToken()) != null) {
            return spec;
        }
        ParserRules.ensureLegalOption(optionCandidate);
        if (this.hasMore()) {
            boolean forHelp = false;
            if (this.specification.charAt(this.index) == '*') {
                forHelp = true;
                ++this.index;
            }
            AbstractOptionSpec abstractOptionSpec = spec = this.hasMore() && this.specification.charAt(this.index) == ':' ? this.handleArgumentAcceptingOption(optionCandidate) : new NoArgumentOptionSpec(optionCandidate);
            if (forHelp) {
                spec.forHelp();
            }
        } else {
            spec = new NoArgumentOptionSpec(optionCandidate);
        }
        return spec;
    }

    void configure(OptionParser parser) {
        this.adjustForPosixlyCorrect(parser);
        while (this.hasMore()) {
            parser.recognize(this.next());
        }
    }

    private void adjustForPosixlyCorrect(OptionParser parser) {
        if ('+' == this.specification.charAt(0)) {
            parser.posixlyCorrect(true);
            this.specification = this.specification.substring(1);
        }
    }

    private AbstractOptionSpec<?> handleReservedForExtensionsToken() {
        if (!this.hasMore()) {
            return new NoArgumentOptionSpec("W");
        }
        if (this.specification.charAt(this.index) == ';') {
            ++this.index;
            return new AlternativeLongOptionSpec();
        }
        return null;
    }

    private AbstractOptionSpec<?> handleArgumentAcceptingOption(String candidate) {
        ++this.index;
        if (this.hasMore() && this.specification.charAt(this.index) == ':') {
            ++this.index;
            return new OptionalArgumentOptionSpec(candidate);
        }
        return new RequiredArgumentOptionSpec(candidate);
    }
}

