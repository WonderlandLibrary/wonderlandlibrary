/*
 * Decompiled with CFR 0.150.
 */
package com.ibm.icu.text;

import com.ibm.icu.impl.Norm2AllModes;
import com.ibm.icu.impl.Normalizer2Impl;
import com.ibm.icu.impl.Utility;
import com.ibm.icu.lang.UCharacter;
import com.ibm.icu.text.Normalizer;
import com.ibm.icu.text.Normalizer2;
import com.ibm.icu.text.UTF16;
import com.ibm.icu.text.UnicodeSet;
import com.ibm.icu.text.UnicodeSetIterator;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public final class CanonicalIterator {
    private static boolean PROGRESS = false;
    private static boolean SKIP_ZEROS = true;
    private final Normalizer2 nfd;
    private final Normalizer2Impl nfcImpl;
    private String source;
    private boolean done;
    private String[][] pieces;
    private int[] current;
    private transient StringBuilder buffer = new StringBuilder();
    private static final Set<String> SET_WITH_NULL_STRING = new HashSet<String>();

    public CanonicalIterator(String source) {
        Norm2AllModes allModes = Norm2AllModes.getNFCInstance();
        this.nfd = allModes.decomp;
        this.nfcImpl = allModes.impl.ensureCanonIterData();
        this.setSource(source);
    }

    public String getSource() {
        return this.source;
    }

    public void reset() {
        this.done = false;
        for (int i = 0; i < this.current.length; ++i) {
            this.current[i] = 0;
        }
    }

    public String next() {
        if (this.done) {
            return null;
        }
        this.buffer.setLength(0);
        for (int i = 0; i < this.pieces.length; ++i) {
            this.buffer.append(this.pieces[i][this.current[i]]);
        }
        String result = this.buffer.toString();
        int i = this.current.length - 1;
        while (true) {
            if (i < 0) {
                this.done = true;
                break;
            }
            int n = i;
            this.current[n] = this.current[n] + 1;
            if (this.current[i] < this.pieces[i].length) break;
            this.current[i] = 0;
            --i;
        }
        return result;
    }

    public void setSource(String newSource) {
        int i;
        int cp;
        this.source = this.nfd.normalize(newSource);
        this.done = false;
        if (newSource.length() == 0) {
            this.pieces = new String[1][];
            this.current = new int[1];
            this.pieces[0] = new String[]{""};
            return;
        }
        ArrayList<String> segmentList = new ArrayList<String>();
        int start = 0;
        for (i = UTF16.findOffsetFromCodePoint(this.source, 1); i < this.source.length(); i += Character.charCount(cp)) {
            cp = this.source.codePointAt(i);
            if (!this.nfcImpl.isCanonSegmentStarter(cp)) continue;
            segmentList.add(this.source.substring(start, i));
            start = i;
        }
        segmentList.add(this.source.substring(start, i));
        this.pieces = new String[segmentList.size()][];
        this.current = new int[segmentList.size()];
        for (i = 0; i < this.pieces.length; ++i) {
            if (PROGRESS) {
                System.out.println("SEGMENT");
            }
            this.pieces[i] = this.getEquivalents((String)segmentList.get(i));
        }
    }

    public static void permute(String source, boolean skipZeros, Set<String> output) {
        int cp;
        if (source.length() <= 2 && UTF16.countCodePoint(source) <= 1) {
            output.add(source);
            return;
        }
        HashSet<String> subpermute = new HashSet<String>();
        for (int i = 0; i < source.length(); i += UTF16.getCharCount(cp)) {
            cp = UTF16.charAt(source, i);
            if (skipZeros && i != 0 && UCharacter.getCombiningClass(cp) == 0) continue;
            subpermute.clear();
            CanonicalIterator.permute(source.substring(0, i) + source.substring(i + UTF16.getCharCount(cp)), skipZeros, subpermute);
            String chStr = UTF16.valueOf(source, i);
            for (String s : subpermute) {
                String piece = chStr + s;
                output.add(piece);
            }
        }
    }

    private String[] getEquivalents(String segment) {
        HashSet<String> result = new HashSet<String>();
        Set<String> basic = this.getEquivalents2(segment);
        HashSet<String> permutations = new HashSet<String>();
        for (String item : basic) {
            permutations.clear();
            CanonicalIterator.permute(item, SKIP_ZEROS, permutations);
            for (String possible : permutations) {
                if (Normalizer.compare(possible, segment, 0) == 0) {
                    if (PROGRESS) {
                        System.out.println("Adding Permutation: " + Utility.hex(possible));
                    }
                    result.add(possible);
                    continue;
                }
                if (!PROGRESS) continue;
                System.out.println("-Skipping Permutation: " + Utility.hex(possible));
            }
        }
        String[] finalResult = new String[result.size()];
        result.toArray(finalResult);
        return finalResult;
    }

    private Set<String> getEquivalents2(String segment) {
        int cp;
        HashSet<String> result = new HashSet<String>();
        if (PROGRESS) {
            System.out.println("Adding: " + Utility.hex(segment));
        }
        result.add(segment);
        StringBuffer workingBuffer = new StringBuffer();
        UnicodeSet starts = new UnicodeSet();
        for (int i = 0; i < segment.length(); i += Character.charCount(cp)) {
            cp = segment.codePointAt(i);
            if (!this.nfcImpl.getCanonStartSet(cp, starts)) continue;
            UnicodeSetIterator iter = new UnicodeSetIterator(starts);
            while (iter.next()) {
                int cp2 = iter.codepoint;
                Set<String> remainder = this.extract(cp2, segment, i, workingBuffer);
                if (remainder == null) continue;
                String prefix = segment.substring(0, i);
                prefix = prefix + UTF16.valueOf(cp2);
                for (String item : remainder) {
                    result.add(prefix + item);
                }
            }
        }
        return result;
    }

    private Set<String> extract(int comp, String segment, int segmentPos, StringBuffer buf) {
        int cp;
        String decomp;
        if (PROGRESS) {
            System.out.println(" extract: " + Utility.hex(UTF16.valueOf(comp)) + ", " + Utility.hex(segment.substring(segmentPos)));
        }
        if ((decomp = this.nfcImpl.getDecomposition(comp)) == null) {
            decomp = UTF16.valueOf(comp);
        }
        boolean ok = false;
        int decompPos = 0;
        int decompCp = UTF16.charAt(decomp, 0);
        decompPos += UTF16.getCharCount(decompCp);
        buf.setLength(0);
        for (int i = segmentPos; i < segment.length(); i += UTF16.getCharCount(cp)) {
            cp = UTF16.charAt(segment, i);
            if (cp == decompCp) {
                if (PROGRESS) {
                    System.out.println("  matches: " + Utility.hex(UTF16.valueOf(cp)));
                }
                if (decompPos == decomp.length()) {
                    buf.append(segment.substring(i + UTF16.getCharCount(cp)));
                    ok = true;
                    break;
                }
                decompCp = UTF16.charAt(decomp, decompPos);
                decompPos += UTF16.getCharCount(decompCp);
                continue;
            }
            if (PROGRESS) {
                System.out.println("  buffer: " + Utility.hex(UTF16.valueOf(cp)));
            }
            UTF16.append(buf, cp);
        }
        if (!ok) {
            return null;
        }
        if (PROGRESS) {
            System.out.println("Matches");
        }
        if (buf.length() == 0) {
            return SET_WITH_NULL_STRING;
        }
        String remainder = buf.toString();
        if (0 != Normalizer.compare(UTF16.valueOf(comp) + remainder, segment.substring(segmentPos), 0)) {
            return null;
        }
        return this.getEquivalents2(remainder);
    }

    static {
        SET_WITH_NULL_STRING.add("");
    }
}

