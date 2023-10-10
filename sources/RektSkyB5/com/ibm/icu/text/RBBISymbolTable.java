/*
 * Decompiled with CFR 0.152.
 */
package com.ibm.icu.text;

import com.ibm.icu.lang.UCharacter;
import com.ibm.icu.text.RBBINode;
import com.ibm.icu.text.RBBIRuleScanner;
import com.ibm.icu.text.SymbolTable;
import com.ibm.icu.text.UTF16;
import com.ibm.icu.text.UnicodeMatcher;
import com.ibm.icu.text.UnicodeSet;
import java.text.ParsePosition;
import java.util.HashMap;

class RBBISymbolTable
implements SymbolTable {
    HashMap<String, RBBISymbolTableEntry> fHashTable;
    RBBIRuleScanner fRuleScanner;
    String ffffString;
    UnicodeSet fCachedSetLookup;

    RBBISymbolTable(RBBIRuleScanner rs) {
        this.fRuleScanner = rs;
        this.fHashTable = new HashMap();
        this.ffffString = "\uffff";
    }

    @Override
    public char[] lookup(String s2) {
        String retString;
        RBBISymbolTableEntry el = this.fHashTable.get(s2);
        if (el == null) {
            return null;
        }
        RBBINode varRefNode = el.val;
        while (varRefNode.fLeftChild.fType == 2) {
            varRefNode = varRefNode.fLeftChild;
        }
        RBBINode exprNode = varRefNode.fLeftChild;
        if (exprNode.fType == 0) {
            RBBINode usetNode = exprNode.fLeftChild;
            this.fCachedSetLookup = usetNode.fInputSet;
            retString = this.ffffString;
        } else {
            this.fRuleScanner.error(66063);
            retString = exprNode.fText;
            this.fCachedSetLookup = null;
        }
        return retString.toCharArray();
    }

    @Override
    public UnicodeMatcher lookupMatcher(int ch) {
        UnicodeSet retVal = null;
        if (ch == 65535) {
            retVal = this.fCachedSetLookup;
            this.fCachedSetLookup = null;
        }
        return retVal;
    }

    @Override
    public String parseReference(String text, ParsePosition pos, int limit) {
        int start;
        int i2;
        int c2;
        String result = "";
        for (i2 = start = pos.getIndex(); i2 < limit; i2 += UTF16.getCharCount(c2)) {
            c2 = UTF16.charAt(text, i2);
            if (i2 == start && !UCharacter.isUnicodeIdentifierStart(c2) || !UCharacter.isUnicodeIdentifierPart(c2)) break;
        }
        if (i2 == start) {
            return result;
        }
        pos.setIndex(i2);
        result = text.substring(start, i2);
        return result;
    }

    RBBINode lookupNode(String key) {
        RBBINode retNode = null;
        RBBISymbolTableEntry el = this.fHashTable.get(key);
        if (el != null) {
            retNode = el.val;
        }
        return retNode;
    }

    void addEntry(String key, RBBINode val2) {
        RBBISymbolTableEntry e2 = this.fHashTable.get(key);
        if (e2 != null) {
            this.fRuleScanner.error(66055);
            return;
        }
        e2 = new RBBISymbolTableEntry();
        e2.key = key;
        e2.val = val2;
        this.fHashTable.put(e2.key, e2);
    }

    void rbbiSymtablePrint() {
        RBBISymbolTableEntry s2;
        int i2;
        System.out.print("Variable Definitions\nName               Node Val     String Val\n----------------------------------------------------------------------\n");
        RBBISymbolTableEntry[] syms = this.fHashTable.values().toArray(new RBBISymbolTableEntry[0]);
        for (i2 = 0; i2 < syms.length; ++i2) {
            s2 = syms[i2];
            System.out.print("  " + s2.key + "  ");
            System.out.print("  " + s2.val + "  ");
            System.out.print(s2.val.fLeftChild.fText);
            System.out.print("\n");
        }
        System.out.println("\nParsed Variable Definitions\n");
        for (i2 = 0; i2 < syms.length; ++i2) {
            s2 = syms[i2];
            System.out.print(s2.key);
            s2.val.fLeftChild.printTree(true);
            System.out.print("\n");
        }
    }

    static class RBBISymbolTableEntry {
        String key;
        RBBINode val;

        RBBISymbolTableEntry() {
        }
    }
}

