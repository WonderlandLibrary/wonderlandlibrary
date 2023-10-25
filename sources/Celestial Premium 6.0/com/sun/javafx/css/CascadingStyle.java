/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.css.Declaration
 *  javafx.css.Match
 *  javafx.css.ParsedValue
 *  javafx.css.PseudoClass
 *  javafx.css.Rule
 *  javafx.css.Selector
 *  javafx.css.Style
 *  javafx.css.StyleOrigin
 */
package com.sun.javafx.css;

import java.util.Set;
import javafx.css.Declaration;
import javafx.css.Match;
import javafx.css.ParsedValue;
import javafx.css.PseudoClass;
import javafx.css.Rule;
import javafx.css.Selector;
import javafx.css.Style;
import javafx.css.StyleOrigin;

public class CascadingStyle
implements Comparable<CascadingStyle> {
    private final Style style;
    private final Set<PseudoClass> pseudoClasses;
    private final int specificity;
    private final int ordinal;
    private final boolean skinProp;

    public Style getStyle() {
        return this.style;
    }

    public CascadingStyle(Style style, Set<PseudoClass> set, int n, int n2) {
        this.style = style;
        this.pseudoClasses = set;
        this.specificity = n;
        this.ordinal = n2;
        this.skinProp = "-fx-skin".equals(style.getDeclaration().getProperty());
    }

    public CascadingStyle(Declaration declaration, Match match, int n) {
        this(new Style(match.getSelector(), declaration), (Set<PseudoClass>)((Object)match.getPseudoClasses()), match.getSpecificity(), n);
    }

    public String getProperty() {
        return this.style.getDeclaration().getProperty();
    }

    public Selector getSelector() {
        return this.style.getSelector();
    }

    public Rule getRule() {
        return this.style.getDeclaration().getRule();
    }

    public StyleOrigin getOrigin() {
        return this.getRule().getOrigin();
    }

    public ParsedValue getParsedValue() {
        return this.style.getDeclaration().getParsedValue();
    }

    public String toString() {
        return this.getProperty();
    }

    public boolean equals(Object object) {
        if (object == null) {
            return false;
        }
        if (this.getClass() != object.getClass()) {
            return false;
        }
        CascadingStyle cascadingStyle = (CascadingStyle)object;
        String string = this.getProperty();
        String string2 = cascadingStyle.getProperty();
        if (string == null ? string2 != null : !string.equals(string2)) {
            return false;
        }
        return !(this.pseudoClasses == null ? cascadingStyle.pseudoClasses != null : !this.pseudoClasses.containsAll(cascadingStyle.pseudoClasses));
    }

    public int hashCode() {
        int n = 7;
        String string = this.getProperty();
        n = 47 * n + (string != null ? string.hashCode() : 0);
        n = 47 * n + (this.pseudoClasses != null ? this.pseudoClasses.hashCode() : 0);
        return n;
    }

    @Override
    public int compareTo(CascadingStyle cascadingStyle) {
        Declaration declaration = this.style.getDeclaration();
        boolean bl = declaration != null ? declaration.isImportant() : false;
        Rule rule = declaration != null ? declaration.getRule() : null;
        StyleOrigin styleOrigin = rule != null ? rule.getOrigin() : null;
        Declaration declaration2 = cascadingStyle.style.getDeclaration();
        boolean bl2 = declaration2 != null ? declaration2.isImportant() : false;
        Rule rule2 = declaration2 != null ? declaration2.getRule() : null;
        StyleOrigin styleOrigin2 = rule2 != null ? rule2.getOrigin() : null;
        int n = 0;
        n = this.skinProp && !cascadingStyle.skinProp ? 1 : (bl != bl2 ? (bl ? -1 : 1) : (styleOrigin != styleOrigin2 ? (styleOrigin == null ? -1 : (styleOrigin2 == null ? 1 : styleOrigin2.compareTo((Enum)styleOrigin))) : cascadingStyle.specificity - this.specificity));
        if (n == 0) {
            n = cascadingStyle.ordinal - this.ordinal;
        }
        return n;
    }
}

