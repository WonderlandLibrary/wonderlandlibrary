/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  javafx.geometry.Bounds
 *  javafx.scene.AccessibleAction
 *  javafx.scene.AccessibleAttribute
 *  javafx.scene.text.Font
 *  javafx.scene.text.FontWeight
 */
package com.sun.glass.ui.win;

import com.sun.glass.ui.win.WinAccessible;
import com.sun.glass.ui.win.WinVariant;
import java.text.BreakIterator;
import javafx.geometry.Bounds;
import javafx.scene.AccessibleAction;
import javafx.scene.AccessibleAttribute;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

class WinTextRangeProvider {
    private static final int TextPatternRangeEndpoint_Start = 0;
    private static final int TextPatternRangeEndpoint_End = 1;
    private static final int TextUnit_Character = 0;
    private static final int TextUnit_Format = 1;
    private static final int TextUnit_Word = 2;
    private static final int TextUnit_Line = 3;
    private static final int TextUnit_Paragraph = 4;
    private static final int TextUnit_Page = 5;
    private static final int TextUnit_Document = 6;
    private static final int UIA_FontNameAttributeId = 40005;
    private static final int UIA_FontSizeAttributeId = 40006;
    private static final int UIA_FontWeightAttributeId = 40007;
    private static final int UIA_IsHiddenAttributeId = 40013;
    private static final int UIA_IsItalicAttributeId = 40014;
    private static final int UIA_IsReadOnlyAttributeId = 40015;
    private static int idCount;
    private int id;
    private int start;
    private int end;
    private WinAccessible accessible;
    private long peer;

    private static native void _initIDs();

    private native long _createTextRangeProvider(long var1);

    private native void _destroyTextRangeProvider(long var1);

    WinTextRangeProvider(WinAccessible winAccessible) {
        this.accessible = winAccessible;
        this.peer = this._createTextRangeProvider(winAccessible.getNativeAccessible());
        this.id = idCount++;
    }

    long getNativeProvider() {
        return this.peer;
    }

    void dispose() {
        this._destroyTextRangeProvider(this.peer);
        this.peer = 0L;
    }

    void setRange(int n, int n2) {
        this.start = n;
        this.end = n2;
    }

    int getStart() {
        return this.start;
    }

    int getEnd() {
        return this.end;
    }

    public String toString() {
        return "Range(start: " + this.start + ", end: " + this.end + ", id: " + this.id + ")";
    }

    private Object getAttribute(AccessibleAttribute accessibleAttribute, Object ... arrobject) {
        return this.accessible.getAttribute(accessibleAttribute, arrobject);
    }

    private boolean isWordStart(BreakIterator breakIterator, String string, int n) {
        if (n == 0) {
            return true;
        }
        if (n == string.length()) {
            return true;
        }
        if (n == -1) {
            return true;
        }
        return breakIterator.isBoundary(n) && Character.isLetterOrDigit(string.charAt(n));
    }

    private long Clone() {
        WinTextRangeProvider winTextRangeProvider = new WinTextRangeProvider(this.accessible);
        winTextRangeProvider.setRange(this.start, this.end);
        return winTextRangeProvider.getNativeProvider();
    }

    private boolean Compare(WinTextRangeProvider winTextRangeProvider) {
        if (winTextRangeProvider == null) {
            return false;
        }
        return this.accessible == winTextRangeProvider.accessible && this.start == winTextRangeProvider.start && this.end == winTextRangeProvider.end;
    }

    private int CompareEndpoints(int n, WinTextRangeProvider winTextRangeProvider, int n2) {
        int n3 = n == 0 ? this.start : this.end;
        int n4 = n2 == 0 ? winTextRangeProvider.start : winTextRangeProvider.end;
        return n3 - n4;
    }

    private void ExpandToEnclosingUnit(int n) {
        String string = (String)this.getAttribute(AccessibleAttribute.TEXT, new Object[0]);
        if (string == null) {
            return;
        }
        int n2 = string.length();
        if (n2 == 0) {
            return;
        }
        switch (n) {
            case 0: {
                if (this.start == n2) {
                    --this.start;
                }
                this.end = this.start + 1;
                break;
            }
            case 1: 
            case 2: {
                int n3;
                BreakIterator breakIterator = BreakIterator.getWordInstance();
                breakIterator.setText(string);
                if (!this.isWordStart(breakIterator, string, this.start)) {
                    n3 = breakIterator.preceding(this.start);
                    while (!this.isWordStart(breakIterator, string, n3)) {
                        n3 = breakIterator.previous();
                    }
                    int n4 = this.start = n3 != -1 ? n3 : 0;
                }
                if (this.isWordStart(breakIterator, string, this.end)) break;
                n3 = breakIterator.following(this.end);
                while (!this.isWordStart(breakIterator, string, n3)) {
                    n3 = breakIterator.next();
                }
                this.end = n3 != -1 ? n3 : n2;
                break;
            }
            case 3: {
                Integer n5 = (Integer)this.getAttribute(AccessibleAttribute.LINE_FOR_OFFSET, this.start);
                Integer n6 = (Integer)this.getAttribute(AccessibleAttribute.LINE_START, n5);
                Integer n7 = (Integer)this.getAttribute(AccessibleAttribute.LINE_END, n5);
                if (n5 == null || n7 == null || n6 == null) {
                    this.start = 0;
                    this.end = n2;
                    break;
                }
                this.start = n6;
                this.end = n7;
                break;
            }
            case 4: {
                int n8;
                Integer n9 = (Integer)this.getAttribute(AccessibleAttribute.LINE_FOR_OFFSET, this.start);
                if (n9 == null) {
                    this.start = 0;
                    this.end = n2;
                    break;
                }
                BreakIterator breakIterator = BreakIterator.getSentenceInstance();
                breakIterator.setText(string);
                if (!breakIterator.isBoundary(this.start)) {
                    n8 = breakIterator.preceding(this.start);
                    this.start = n8 != -1 ? n8 : 0;
                }
                this.end = (n8 = breakIterator.following(this.start)) != -1 ? n8 : n2;
                break;
            }
            case 5: 
            case 6: {
                this.start = 0;
                this.end = n2;
            }
        }
        this.start = Math.max(0, Math.min(this.start, n2));
        this.end = Math.max(this.start, Math.min(this.end, n2));
    }

    private long FindAttribute(int n, WinVariant winVariant, boolean bl) {
        System.err.println("FindAttribute NOT IMPLEMENTED");
        return 0L;
    }

    private long FindText(String string, boolean bl, boolean bl2) {
        if (string == null) {
            return 0L;
        }
        String string2 = (String)this.getAttribute(AccessibleAttribute.TEXT, new Object[0]);
        if (string2 == null) {
            return 0L;
        }
        String string3 = string2.substring(this.start, this.end);
        if (bl2) {
            string3 = string3.toLowerCase();
            string = string.toLowerCase();
        }
        int n = -1;
        n = bl ? string3.lastIndexOf(string) : string3.indexOf(string);
        if (n == -1) {
            return 0L;
        }
        WinTextRangeProvider winTextRangeProvider = new WinTextRangeProvider(this.accessible);
        winTextRangeProvider.setRange(this.start + n, this.start + n + string.length());
        return winTextRangeProvider.getNativeProvider();
    }

    private WinVariant GetAttributeValue(int n) {
        WinVariant winVariant = null;
        switch (n) {
            case 40005: {
                Font font = (Font)this.getAttribute(AccessibleAttribute.FONT, new Object[0]);
                if (font == null) break;
                winVariant = new WinVariant();
                winVariant.vt = (short)8;
                winVariant.bstrVal = font.getName();
                break;
            }
            case 40006: {
                Font font = (Font)this.getAttribute(AccessibleAttribute.FONT, new Object[0]);
                if (font == null) break;
                winVariant = new WinVariant();
                winVariant.vt = (short)5;
                winVariant.dblVal = font.getSize();
                break;
            }
            case 40007: {
                Font font = (Font)this.getAttribute(AccessibleAttribute.FONT, new Object[0]);
                if (font == null) break;
                boolean bl = font.getStyle().toLowerCase().contains("bold");
                winVariant = new WinVariant();
                winVariant.vt = (short)3;
                winVariant.lVal = bl ? FontWeight.BOLD.getWeight() : FontWeight.NORMAL.getWeight();
                break;
            }
            case 40013: 
            case 40015: {
                winVariant = new WinVariant();
                winVariant.vt = (short)11;
                winVariant.boolVal = false;
                break;
            }
            case 40014: {
                Font font = (Font)this.getAttribute(AccessibleAttribute.FONT, new Object[0]);
                if (font == null) break;
                boolean bl = font.getStyle().toLowerCase().contains("italic");
                winVariant = new WinVariant();
                winVariant.vt = (short)11;
                winVariant.boolVal = bl;
                break;
            }
        }
        return winVariant;
    }

    private double[] GetBoundingRectangles() {
        Bounds[] arrbounds;
        String string = (String)this.getAttribute(AccessibleAttribute.TEXT, new Object[0]);
        if (string == null) {
            return null;
        }
        int n = string.length();
        if (n == 0) {
            return new double[0];
        }
        int n2 = this.end;
        if (n2 > 0 && n2 > this.start && string.charAt(n2 - 1) == '\n') {
            --n2;
        }
        if (n2 > 0 && n2 > this.start && string.charAt(n2 - 1) == '\r') {
            --n2;
        }
        if (n2 > 0 && n2 > this.start && n2 == n) {
            --n2;
        }
        if ((arrbounds = (Bounds[])this.getAttribute(AccessibleAttribute.BOUNDS_FOR_RANGE, this.start, n2)) != null) {
            double[] arrd = new double[arrbounds.length * 4];
            int n3 = 0;
            for (int i = 0; i < arrbounds.length; ++i) {
                Bounds bounds = arrbounds[i];
                arrd[n3++] = bounds.getMinX();
                arrd[n3++] = bounds.getMinY();
                arrd[n3++] = bounds.getWidth();
                arrd[n3++] = bounds.getHeight();
            }
            return arrd;
        }
        return null;
    }

    private long GetEnclosingElement() {
        return this.accessible.getNativeAccessible();
    }

    private String GetText(int n) {
        String string = (String)this.getAttribute(AccessibleAttribute.TEXT, new Object[0]);
        if (string == null) {
            return null;
        }
        int n2 = n != -1 ? Math.min(this.end, this.start + n) : this.end;
        return string.substring(this.start, n2);
    }

    private int Move(int n, int n2) {
        int n3;
        if (n2 == 0) {
            return 0;
        }
        String string = (String)this.getAttribute(AccessibleAttribute.TEXT, new Object[0]);
        if (string == null) {
            return 0;
        }
        int n4 = string.length();
        if (n4 == 0) {
            return 0;
        }
        switch (n) {
            case 0: {
                int n5 = this.start;
                this.start = Math.max(0, Math.min(this.start + n2, n4 - 1));
                this.end = this.start + 1;
                n3 = this.start - n5;
                break;
            }
            case 1: 
            case 2: {
                BreakIterator breakIterator = BreakIterator.getWordInstance();
                breakIterator.setText(string);
                int n6 = this.start;
                while (!this.isWordStart(breakIterator, string, n6)) {
                    n6 = breakIterator.preceding(this.start);
                }
                while (n6 != -1 && n3 != n2) {
                    if (n2 > 0) {
                        n6 = breakIterator.following(n6);
                        while (!this.isWordStart(breakIterator, string, n6)) {
                            n6 = breakIterator.next();
                        }
                        ++n3;
                        continue;
                    }
                    n6 = breakIterator.preceding(n6);
                    while (!this.isWordStart(breakIterator, string, n6)) {
                        n6 = breakIterator.previous();
                    }
                    --n3;
                }
                if (n3 == 0) break;
                this.start = n6 != -1 ? n6 : (n2 > 0 ? n4 : 0);
                n6 = breakIterator.following(this.start);
                while (!this.isWordStart(breakIterator, string, n6)) {
                    n6 = breakIterator.next();
                }
                this.end = n6 != -1 ? n6 : n4;
                break;
            }
            case 3: {
                int n7;
                Integer n8 = (Integer)this.getAttribute(AccessibleAttribute.LINE_FOR_OFFSET, this.start);
                if (n8 == null) {
                    return 0;
                }
                int n9 = n7 = n2 > 0 ? 1 : -1;
                for (n3 = 0; n2 != n3; n3 += n7) {
                    if (this.getAttribute(AccessibleAttribute.LINE_START, n8 + n7) == null) break;
                    n8 = n8 + n7;
                }
                if (n3 == 0) break;
                Integer n10 = (Integer)this.getAttribute(AccessibleAttribute.LINE_START, n8);
                Integer n11 = (Integer)this.getAttribute(AccessibleAttribute.LINE_END, n8);
                if (n10 == null || n11 == null) {
                    return 0;
                }
                this.start = n10;
                this.end = n11;
                break;
            }
            case 4: {
                int n12;
                BreakIterator breakIterator = BreakIterator.getSentenceInstance();
                breakIterator.setText(string);
                int n13 = n12 = breakIterator.isBoundary(this.start) ? this.start : breakIterator.preceding(this.start);
                while (n12 != -1 && n3 != n2) {
                    if (n2 > 0) {
                        n12 = breakIterator.following(n12);
                        ++n3;
                        continue;
                    }
                    n12 = breakIterator.preceding(n12);
                    --n3;
                }
                if (n3 == 0) break;
                this.start = n12 != -1 ? n12 : 0;
                n12 = breakIterator.following(this.start);
                this.end = n12 != -1 ? n12 : n4;
                break;
            }
            case 5: 
            case 6: {
                return 0;
            }
        }
        this.start = Math.max(0, Math.min(this.start, n4));
        this.end = Math.max(this.start, Math.min(this.end, n4));
        return n3;
    }

    private int MoveEndpointByUnit(int n, int n2, int n3) {
        if (n3 == 0) {
            return 0;
        }
        String string = (String)this.getAttribute(AccessibleAttribute.TEXT, new Object[0]);
        if (string == null) {
            return 0;
        }
        int n4 = string.length();
        int n5 = 0;
        int n6 = n == 0 ? this.start : this.end;
        switch (n2) {
            case 0: {
                int n7 = n6;
                n6 = Math.max(0, Math.min(n6 + n3, n4));
                n5 = n6 - n7;
                break;
            }
            case 1: 
            case 2: {
                BreakIterator breakIterator = BreakIterator.getWordInstance();
                breakIterator.setText(string);
                while (n6 != -1 && n5 != n3) {
                    if (n3 > 0) {
                        n6 = breakIterator.following(n6);
                        while (!this.isWordStart(breakIterator, string, n6)) {
                            n6 = breakIterator.next();
                        }
                        ++n5;
                        continue;
                    }
                    n6 = breakIterator.preceding(n6);
                    while (!this.isWordStart(breakIterator, string, n6)) {
                        n6 = breakIterator.previous();
                    }
                    --n5;
                }
                if (n6 != -1) break;
                n6 = n3 > 0 ? n4 : 0;
                break;
            }
            case 3: {
                Integer n8 = (Integer)this.getAttribute(AccessibleAttribute.LINE_FOR_OFFSET, n6);
                Integer n9 = (Integer)this.getAttribute(AccessibleAttribute.LINE_START, n8);
                Integer n10 = (Integer)this.getAttribute(AccessibleAttribute.LINE_END, n8);
                if (n8 == null || n9 == null || n10 == null) {
                    n6 = n3 > 0 ? n4 : 0;
                    break;
                }
                int n11 = n3 > 0 ? 1 : -1;
                int n12 = n3 > 0 ? n10 : n9;
                if (n6 != n12) {
                    n5 += n11;
                }
                while (n3 != n5) {
                    if (this.getAttribute(AccessibleAttribute.LINE_START, n8 + n11) == null) break;
                    n8 = n8 + n11;
                    n5 += n11;
                }
                if (n5 == 0) break;
                n9 = (Integer)this.getAttribute(AccessibleAttribute.LINE_START, n8);
                n10 = (Integer)this.getAttribute(AccessibleAttribute.LINE_END, n8);
                if (n9 == null || n10 == null) {
                    return 0;
                }
                n6 = n3 > 0 ? n10 : n9;
                break;
            }
            case 4: {
                BreakIterator breakIterator = BreakIterator.getSentenceInstance();
                breakIterator.setText(string);
                while (n6 != -1 && n5 != n3) {
                    if (n3 > 0) {
                        n6 = breakIterator.following(n6);
                        ++n5;
                        continue;
                    }
                    n6 = breakIterator.preceding(n6);
                    --n5;
                }
                if (n6 != -1) break;
                n6 = n3 > 0 ? n4 : 0;
                break;
            }
            case 5: 
            case 6: {
                return 0;
            }
        }
        if (n == 0) {
            this.start = n6;
        } else {
            this.end = n6;
        }
        if (this.start > this.end) {
            this.start = this.end = n6;
        }
        this.start = Math.max(0, Math.min(this.start, n4));
        this.end = Math.max(this.start, Math.min(this.end, n4));
        return n5;
    }

    private void MoveEndpointByRange(int n, WinTextRangeProvider winTextRangeProvider, int n2) {
        int n3;
        String string = (String)this.getAttribute(AccessibleAttribute.TEXT, new Object[0]);
        if (string == null) {
            return;
        }
        int n4 = string.length();
        int n5 = n3 = n2 == 0 ? winTextRangeProvider.start : winTextRangeProvider.end;
        if (n == 0) {
            this.start = n3;
        } else {
            this.end = n3;
        }
        if (this.start > this.end) {
            this.start = this.end = n3;
        }
        this.start = Math.max(0, Math.min(this.start, n4));
        this.end = Math.max(this.start, Math.min(this.end, n4));
    }

    private void Select() {
        this.accessible.executeAction(AccessibleAction.SET_TEXT_SELECTION, this.start, this.end);
    }

    private void AddToSelection() {
    }

    private void RemoveFromSelection() {
    }

    private void ScrollIntoView(boolean bl) {
        this.accessible.executeAction(AccessibleAction.SHOW_TEXT_RANGE, this.start, this.end);
    }

    private long[] GetChildren() {
        return new long[0];
    }

    static {
        WinTextRangeProvider._initIDs();
        idCount = 1;
    }
}

