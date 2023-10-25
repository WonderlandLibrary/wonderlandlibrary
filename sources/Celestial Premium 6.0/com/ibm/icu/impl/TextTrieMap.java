/*
 * Decompiled with CFR 0.150.
 */
package com.ibm.icu.impl;

import com.ibm.icu.lang.UCharacter;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public class TextTrieMap<V> {
    private Node _root = new Node();
    boolean _ignoreCase;

    public TextTrieMap(boolean ignoreCase) {
        this._ignoreCase = ignoreCase;
    }

    public TextTrieMap<V> put(CharSequence text, V val) {
        CharIterator chitr = new CharIterator(text, 0, this._ignoreCase);
        this._root.add(chitr, val);
        return this;
    }

    public Iterator<V> get(String text) {
        return this.get(text, 0);
    }

    public Iterator<V> get(CharSequence text, int start) {
        return this.get(text, start, null);
    }

    public Iterator<V> get(CharSequence text, int start, int[] matchLen) {
        LongestMatchHandler handler = new LongestMatchHandler();
        this.find(text, start, handler);
        if (matchLen != null && matchLen.length > 0) {
            matchLen[0] = handler.getMatchLength();
        }
        return handler.getMatches();
    }

    public void find(CharSequence text, ResultHandler<V> handler) {
        this.find(text, 0, handler);
    }

    public void find(CharSequence text, int offset, ResultHandler<V> handler) {
        CharIterator chitr = new CharIterator(text, offset, this._ignoreCase);
        this.find(this._root, chitr, handler);
    }

    private synchronized void find(Node node, CharIterator chitr, ResultHandler<V> handler) {
        Iterator values = node.values();
        if (values != null && !handler.handlePrefixMatch(chitr.processedLength(), values)) {
            return;
        }
        Node nextMatch = node.findMatch(chitr);
        if (nextMatch != null) {
            this.find(nextMatch, chitr, handler);
        }
    }

    private static char[] toCharArray(CharSequence text) {
        char[] array = new char[text.length()];
        for (int i = 0; i < array.length; ++i) {
            array[i] = text.charAt(i);
        }
        return array;
    }

    private static char[] subArray(char[] array, int start) {
        if (start == 0) {
            return array;
        }
        char[] sub = new char[array.length - start];
        System.arraycopy(array, start, sub, 0, sub.length);
        return sub;
    }

    private static char[] subArray(char[] array, int start, int limit) {
        if (start == 0 && limit == array.length) {
            return array;
        }
        char[] sub = new char[limit - start];
        System.arraycopy(array, start, sub, 0, limit - start);
        return sub;
    }

    /*
     * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
     */
    private class Node {
        private char[] _text;
        private List<V> _values;
        private List<Node> _children;

        private Node() {
        }

        private Node(char[] text, List<V> values, List<Node> children) {
            this._text = text;
            this._values = values;
            this._children = children;
        }

        public Iterator<V> values() {
            if (this._values == null) {
                return null;
            }
            return this._values.iterator();
        }

        public void add(CharIterator chitr, V value) {
            StringBuilder buf = new StringBuilder();
            while (chitr.hasNext()) {
                buf.append(chitr.next());
            }
            this.add(TextTrieMap.toCharArray(buf), 0, value);
        }

        public Node findMatch(CharIterator chitr) {
            if (this._children == null) {
                return null;
            }
            if (!chitr.hasNext()) {
                return null;
            }
            Node match = null;
            Character ch = chitr.next();
            for (Node child : this._children) {
                if (ch.charValue() < child._text[0]) break;
                if (ch.charValue() != child._text[0]) continue;
                if (!child.matchFollowing(chitr)) break;
                match = child;
                break;
            }
            return match;
        }

        private void add(char[] text, int offset, V value) {
            if (text.length == offset) {
                this._values = this.addValue(this._values, value);
                return;
            }
            if (this._children == null) {
                this._children = new LinkedList<Node>();
                Node child = new Node(TextTrieMap.subArray(text, offset), this.addValue(null, value), null);
                this._children.add(child);
                return;
            }
            ListIterator<Node> litr = this._children.listIterator();
            while (litr.hasNext()) {
                Node next = litr.next();
                if (text[offset] < next._text[0]) {
                    litr.previous();
                    break;
                }
                if (text[offset] != next._text[0]) continue;
                int matchLen = next.lenMatches(text, offset);
                if (matchLen == next._text.length) {
                    next.add(text, offset + matchLen, value);
                } else {
                    next.split(matchLen);
                    next.add(text, offset + matchLen, value);
                }
                return;
            }
            litr.add(new Node(TextTrieMap.subArray(text, offset), this.addValue(null, value), null));
        }

        private boolean matchFollowing(CharIterator chitr) {
            boolean matched = true;
            for (int idx = 1; idx < this._text.length; ++idx) {
                if (!chitr.hasNext()) {
                    matched = false;
                    break;
                }
                Character ch = chitr.next();
                if (ch.charValue() == this._text[idx]) continue;
                matched = false;
                break;
            }
            return matched;
        }

        private int lenMatches(char[] text, int offset) {
            int len;
            int textLen = text.length - offset;
            int limit = this._text.length < textLen ? this._text.length : textLen;
            for (len = 0; len < limit && this._text[len] == text[offset + len]; ++len) {
            }
            return len;
        }

        private void split(int offset) {
            char[] childText = TextTrieMap.subArray(this._text, offset);
            this._text = TextTrieMap.subArray(this._text, 0, offset);
            Node child = new Node(childText, this._values, this._children);
            this._values = null;
            this._children = new LinkedList<Node>();
            this._children.add(child);
        }

        private List<V> addValue(List<V> list, V value) {
            if (list == null) {
                list = new LinkedList();
            }
            list.add(value);
            return list;
        }
    }

    /*
     * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
     */
    private static class LongestMatchHandler<V>
    implements ResultHandler<V> {
        private Iterator<V> matches = null;
        private int length = 0;

        private LongestMatchHandler() {
        }

        @Override
        public boolean handlePrefixMatch(int matchLength, Iterator<V> values) {
            if (matchLength > this.length) {
                this.length = matchLength;
                this.matches = values;
            }
            return true;
        }

        public Iterator<V> getMatches() {
            return this.matches;
        }

        public int getMatchLength() {
            return this.length;
        }
    }

    /*
     * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
     */
    public static interface ResultHandler<V> {
        public boolean handlePrefixMatch(int var1, Iterator<V> var2);
    }

    /*
     * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
     */
    public static class CharIterator
    implements Iterator<Character> {
        private boolean _ignoreCase;
        private CharSequence _text;
        private int _nextIdx;
        private int _startIdx;
        private Character _remainingChar;

        CharIterator(CharSequence text, int offset, boolean ignoreCase) {
            this._text = text;
            this._nextIdx = this._startIdx = offset;
            this._ignoreCase = ignoreCase;
        }

        @Override
        public boolean hasNext() {
            return this._nextIdx != this._text.length() || this._remainingChar != null;
        }

        @Override
        public Character next() {
            Character next;
            if (this._nextIdx == this._text.length() && this._remainingChar == null) {
                return null;
            }
            if (this._remainingChar != null) {
                next = this._remainingChar;
                this._remainingChar = null;
            } else if (this._ignoreCase) {
                int cp = UCharacter.foldCase(Character.codePointAt(this._text, this._nextIdx), true);
                this._nextIdx += Character.charCount(cp);
                char[] chars = Character.toChars(cp);
                next = Character.valueOf(chars[0]);
                if (chars.length == 2) {
                    this._remainingChar = Character.valueOf(chars[1]);
                }
            } else {
                next = Character.valueOf(this._text.charAt(this._nextIdx));
                ++this._nextIdx;
            }
            return next;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("remove() not supproted");
        }

        public int nextIndex() {
            return this._nextIdx;
        }

        public int processedLength() {
            if (this._remainingChar != null) {
                throw new IllegalStateException("In the middle of surrogate pair");
            }
            return this._nextIdx - this._startIdx;
        }
    }
}

