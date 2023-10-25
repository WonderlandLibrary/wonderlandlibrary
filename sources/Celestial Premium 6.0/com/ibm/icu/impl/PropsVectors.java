/*
 * Decompiled with CFR 0.150.
 */
package com.ibm.icu.impl;

import com.ibm.icu.impl.IntTrie;
import com.ibm.icu.impl.IntTrieBuilder;
import com.ibm.icu.impl.PVecToTrieCompactHandler;
import com.ibm.icu.impl.Trie;
import com.ibm.icu.impl.TrieBuilder;
import java.util.Arrays;
import java.util.Comparator;

public class PropsVectors {
    private int[] v;
    private int columns;
    private int maxRows;
    private int rows;
    private int prevRow;
    private boolean isCompacted;
    public static final int FIRST_SPECIAL_CP = 0x110000;
    public static final int INITIAL_VALUE_CP = 0x110000;
    public static final int ERROR_VALUE_CP = 0x110001;
    public static final int MAX_CP = 0x110001;
    public static final int INITIAL_ROWS = 4096;
    public static final int MEDIUM_ROWS = 65536;
    public static final int MAX_ROWS = 0x110002;

    private boolean areElementsSame(int index1, int[] target, int index2, int length) {
        for (int i = 0; i < length; ++i) {
            if (this.v[index1 + i] == target[index2 + i]) continue;
            return false;
        }
        return true;
    }

    private int findRow(int rangeStart) {
        int index = 0;
        index = this.prevRow * this.columns;
        if (rangeStart >= this.v[index]) {
            if (rangeStart < this.v[index + 1]) {
                return index;
            }
            if (rangeStart < this.v[(index += this.columns) + 1]) {
                ++this.prevRow;
                return index;
            }
            if (rangeStart < this.v[(index += this.columns) + 1]) {
                this.prevRow += 2;
                return index;
            }
            if (rangeStart - this.v[index + 1] < 10) {
                this.prevRow += 2;
                do {
                    ++this.prevRow;
                } while (rangeStart >= this.v[(index += this.columns) + 1]);
                return index;
            }
        } else if (rangeStart < this.v[1]) {
            this.prevRow = 0;
            return 0;
        }
        int start = 0;
        int mid = 0;
        int limit = this.rows;
        while (start < limit - 1) {
            mid = (start + limit) / 2;
            index = this.columns * mid;
            if (rangeStart < this.v[index]) {
                limit = mid;
                continue;
            }
            if (rangeStart < this.v[index + 1]) {
                this.prevRow = mid;
                return index;
            }
            start = mid;
        }
        this.prevRow = start;
        index = start * this.columns;
        return index;
    }

    public PropsVectors(int numOfColumns) {
        if (numOfColumns < 1) {
            throw new IllegalArgumentException("numOfColumns need to be no less than 1; but it is " + numOfColumns);
        }
        this.columns = numOfColumns + 2;
        this.v = new int[4096 * this.columns];
        this.maxRows = 4096;
        this.rows = 3;
        this.prevRow = 0;
        this.isCompacted = false;
        this.v[0] = 0;
        this.v[1] = 0x110000;
        int index = this.columns;
        for (int cp = 0x110000; cp <= 0x110001; ++cp) {
            this.v[index] = cp;
            this.v[index + 1] = cp + 1;
            index += this.columns;
        }
    }

    public void setValue(int start, int end, int column, int value, int mask) {
        boolean splitLastRow;
        if (start < 0 || start > end || end > 0x110001 || column < 0 || column >= this.columns - 2) {
            throw new IllegalArgumentException();
        }
        if (this.isCompacted) {
            throw new IllegalStateException("Shouldn't be called aftercompact()!");
        }
        int limit = end + 1;
        int firstRow = this.findRow(start);
        int lastRow = this.findRow(end);
        boolean splitFirstRow = start != this.v[firstRow] && (value &= mask) != (this.v[firstRow + (column += 2)] & mask);
        boolean bl = splitLastRow = limit != this.v[lastRow + 1] && value != (this.v[lastRow + column] & mask);
        if (splitFirstRow || splitLastRow) {
            int count;
            int rowsToExpand = 0;
            if (splitFirstRow) {
                ++rowsToExpand;
            }
            if (splitLastRow) {
                ++rowsToExpand;
            }
            int newMaxRows = 0;
            if (this.rows + rowsToExpand > this.maxRows) {
                if (this.maxRows < 65536) {
                    newMaxRows = 65536;
                } else if (this.maxRows < 0x110002) {
                    newMaxRows = 0x110002;
                } else {
                    throw new IndexOutOfBoundsException("MAX_ROWS exceeded! Increase it to a higher valuein the implementation");
                }
                int[] temp = new int[newMaxRows * this.columns];
                System.arraycopy(this.v, 0, temp, 0, this.rows * this.columns);
                this.v = temp;
                this.maxRows = newMaxRows;
            }
            if ((count = this.rows * this.columns - (lastRow + this.columns)) > 0) {
                System.arraycopy(this.v, lastRow + this.columns, this.v, lastRow + (1 + rowsToExpand) * this.columns, count);
            }
            this.rows += rowsToExpand;
            if (splitFirstRow) {
                count = lastRow - firstRow + this.columns;
                System.arraycopy(this.v, firstRow, this.v, firstRow + this.columns, count);
                lastRow += this.columns;
                int n = start;
                this.v[firstRow + this.columns] = n;
                this.v[firstRow + 1] = n;
                firstRow += this.columns;
            }
            if (splitLastRow) {
                System.arraycopy(this.v, lastRow, this.v, lastRow + this.columns, this.columns);
                int n = limit;
                this.v[lastRow + this.columns] = n;
                this.v[lastRow + 1] = n;
            }
        }
        this.prevRow = lastRow / this.columns;
        firstRow += column;
        while (true) {
            this.v[firstRow] = this.v[firstRow] & (mask ^= 0xFFFFFFFF) | value;
            if (firstRow == (lastRow += column)) break;
            firstRow += this.columns;
        }
    }

    public int getValue(int c, int column) {
        if (this.isCompacted || c < 0 || c > 0x110001 || column < 0 || column >= this.columns - 2) {
            return 0;
        }
        int index = this.findRow(c);
        return this.v[index + 2 + column];
    }

    public int[] getRow(int rowIndex) {
        if (this.isCompacted) {
            throw new IllegalStateException("Illegal Invocation of the method after compact()");
        }
        if (rowIndex < 0 || rowIndex > this.rows) {
            throw new IllegalArgumentException("rowIndex out of bound!");
        }
        int[] rowToReturn = new int[this.columns - 2];
        System.arraycopy(this.v, rowIndex * this.columns + 2, rowToReturn, 0, this.columns - 2);
        return rowToReturn;
    }

    public int getRowStart(int rowIndex) {
        if (this.isCompacted) {
            throw new IllegalStateException("Illegal Invocation of the method after compact()");
        }
        if (rowIndex < 0 || rowIndex > this.rows) {
            throw new IllegalArgumentException("rowIndex out of bound!");
        }
        return this.v[rowIndex * this.columns];
    }

    public int getRowEnd(int rowIndex) {
        if (this.isCompacted) {
            throw new IllegalStateException("Illegal Invocation of the method after compact()");
        }
        if (rowIndex < 0 || rowIndex > this.rows) {
            throw new IllegalArgumentException("rowIndex out of bound!");
        }
        return this.v[rowIndex * this.columns + 1] - 1;
    }

    public void compact(CompactHandler compactor) {
        if (this.isCompacted) {
            return;
        }
        this.isCompacted = true;
        int valueColumns = this.columns - 2;
        Integer[] indexArray = new Integer[this.rows];
        for (int i = 0; i < this.rows; ++i) {
            indexArray[i] = this.columns * i;
        }
        Arrays.sort(indexArray, new Comparator<Integer>(){

            @Override
            public int compare(Integer o1, Integer o2) {
                int indexOfRow1 = o1;
                int indexOfRow2 = o2;
                int count = PropsVectors.this.columns;
                int index = 2;
                do {
                    if (PropsVectors.this.v[indexOfRow1 + index] != PropsVectors.this.v[indexOfRow2 + index]) {
                        return PropsVectors.this.v[indexOfRow1 + index] < PropsVectors.this.v[indexOfRow2 + index] ? -1 : 1;
                    }
                    if (++index != PropsVectors.this.columns) continue;
                    index = 0;
                } while (--count > 0);
                return 0;
            }
        });
        int count = -valueColumns;
        for (int i = 0; i < this.rows; ++i) {
            int start = this.v[indexArray[i]];
            if (count < 0 || !this.areElementsSame(indexArray[i] + 2, this.v, indexArray[i - 1] + 2, valueColumns)) {
                count += valueColumns;
            }
            if (start == 0x110000) {
                compactor.setRowIndexForInitialValue(count);
                continue;
            }
            if (start != 0x110001) continue;
            compactor.setRowIndexForErrorValue(count);
        }
        compactor.startRealValues(count += valueColumns);
        int[] temp = new int[count];
        count = -valueColumns;
        for (int i = 0; i < this.rows; ++i) {
            int start = this.v[indexArray[i]];
            int limit = this.v[indexArray[i] + 1];
            if (count < 0 || !this.areElementsSame(indexArray[i] + 2, temp, count, valueColumns)) {
                System.arraycopy(this.v, indexArray[i] + 2, temp, count += valueColumns, valueColumns);
            }
            if (start >= 0x110000) continue;
            compactor.setRowIndexForRange(start, limit - 1, count);
        }
        this.v = temp;
        this.rows = count / valueColumns + 1;
    }

    public int[] getCompactedArray() {
        if (!this.isCompacted) {
            throw new IllegalStateException("Illegal Invocation of the method before compact()");
        }
        return this.v;
    }

    public int getCompactedRows() {
        if (!this.isCompacted) {
            throw new IllegalStateException("Illegal Invocation of the method before compact()");
        }
        return this.rows;
    }

    public int getCompactedColumns() {
        if (!this.isCompacted) {
            throw new IllegalStateException("Illegal Invocation of the method before compact()");
        }
        return this.columns - 2;
    }

    public IntTrie compactToTrieWithRowIndexes() {
        PVecToTrieCompactHandler compactor = new PVecToTrieCompactHandler();
        this.compact(compactor);
        return compactor.builder.serialize(new DefaultGetFoldedValue(compactor.builder), new DefaultGetFoldingOffset());
    }

    public static interface CompactHandler {
        public void setRowIndexForRange(int var1, int var2, int var3);

        public void setRowIndexForInitialValue(int var1);

        public void setRowIndexForErrorValue(int var1);

        public void startRealValues(int var1);
    }

    private static class DefaultGetFoldedValue
    implements TrieBuilder.DataManipulate {
        private IntTrieBuilder builder;

        public DefaultGetFoldedValue(IntTrieBuilder inBuilder) {
            this.builder = inBuilder;
        }

        public int getFoldedValue(int start, int offset) {
            int initialValue = this.builder.m_initialValue_;
            int limit = start + 1024;
            while (start < limit) {
                boolean[] inBlockZero = new boolean[1];
                int value = this.builder.getValue(start, inBlockZero);
                if (inBlockZero[0]) {
                    start += 32;
                    continue;
                }
                if (value != initialValue) {
                    return offset;
                }
                ++start;
            }
            return 0;
        }
    }

    private static class DefaultGetFoldingOffset
    implements Trie.DataManipulate {
        private DefaultGetFoldingOffset() {
        }

        public int getFoldingOffset(int value) {
            return value;
        }
    }
}

