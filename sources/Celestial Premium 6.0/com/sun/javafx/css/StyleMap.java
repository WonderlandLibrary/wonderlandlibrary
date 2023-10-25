/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.css.Declaration
 *  javafx.css.Match
 *  javafx.css.Selector
 */
package com.sun.javafx.css;

import com.sun.javafx.css.CascadingStyle;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.css.Declaration;
import javafx.css.Match;
import javafx.css.Selector;

public final class StyleMap {
    public static final StyleMap EMPTY_MAP = new StyleMap(-1, Collections.emptyList());
    private static final Comparator<CascadingStyle> cascadingStyleComparator = (cascadingStyle, cascadingStyle2) -> {
        String string;
        String string2 = cascadingStyle.getProperty();
        int n = string2.compareTo(string = cascadingStyle2.getProperty());
        if (n != 0) {
            return n;
        }
        return cascadingStyle.compareTo((CascadingStyle)cascadingStyle2);
    };
    private final int id;
    private List<Selector> selectors;
    private Map<String, List<CascadingStyle>> cascadingStyles;

    public StyleMap(int n, List<Selector> list) {
        this.id = n;
        this.selectors = list;
    }

    public int getId() {
        return this.id;
    }

    public boolean isEmpty() {
        if (this.selectors != null) {
            return this.selectors.isEmpty();
        }
        if (this.cascadingStyles != null) {
            return this.cascadingStyles.isEmpty();
        }
        return true;
    }

    public Map<String, List<CascadingStyle>> getCascadingStyles() {
        if (this.cascadingStyles == null) {
            int n;
            Object object;
            Object object2;
            int n2;
            if (this.selectors == null || this.selectors.isEmpty()) {
                this.cascadingStyles = Collections.emptyMap();
                return this.cascadingStyles;
            }
            ArrayList<CascadingStyle> arrayList = new ArrayList<CascadingStyle>();
            int n3 = 0;
            int n4 = this.selectors.size();
            for (n2 = 0; n2 < n4; ++n2) {
                object2 = this.selectors.get(n2);
                Match match = object2.createMatch();
                object = object2.getRule();
                int n5 = object.getDeclarations().size();
                for (n = 0; n < n5; ++n) {
                    Declaration declaration = (Declaration)object.getDeclarations().get(n);
                    CascadingStyle cascadingStyle = new CascadingStyle(declaration, match, n3++);
                    arrayList.add(cascadingStyle);
                }
            }
            if (arrayList.isEmpty()) {
                this.cascadingStyles = Collections.emptyMap();
                return this.cascadingStyles;
            }
            Collections.sort(arrayList, cascadingStyleComparator);
            n2 = arrayList.size();
            this.cascadingStyles = new HashMap<String, List<CascadingStyle>>(n2);
            CascadingStyle cascadingStyle = (CascadingStyle)arrayList.get(0);
            object2 = cascadingStyle.getProperty();
            int n6 = 0;
            while (n6 < n2) {
                object = this.cascadingStyles.get(object2);
                if (object == null) {
                    n = n6;
                    Object object3 = object2;
                    while (++n < n2 && ((String)(object2 = (cascadingStyle = (CascadingStyle)arrayList.get(n)).getProperty())).equals(object3)) {
                    }
                    this.cascadingStyles.put((String)object3, arrayList.subList(n6, n));
                    n6 = n;
                    continue;
                }
                assert (false);
            }
            this.selectors.clear();
            this.selectors = null;
        }
        return this.cascadingStyles;
    }
}

