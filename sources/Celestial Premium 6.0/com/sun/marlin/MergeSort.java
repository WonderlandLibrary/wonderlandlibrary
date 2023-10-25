/*
 * Decompiled with CFR 0.150.
 */
package com.sun.marlin;

final class MergeSort {
    public static final int INSERTION_SORT_THRESHOLD = 14;

    static void mergeSortNoCopy(int[] arrn, int[] arrn2, int[] arrn3, int[] arrn4, int n, int n2) {
        if (n > arrn.length || n > arrn2.length || n > arrn3.length || n > arrn4.length) {
            throw new ArrayIndexOutOfBoundsException("bad arguments: toIndex=" + n);
        }
        MergeSort.mergeSort(arrn, arrn2, arrn, arrn3, arrn2, arrn4, n2, n);
        if (n2 == 0 || arrn3[n2 - 1] <= arrn3[n2]) {
            System.arraycopy(arrn3, 0, arrn, 0, n);
            System.arraycopy(arrn4, 0, arrn2, 0, n);
            return;
        }
        int n3 = 0;
        int n4 = n2;
        for (int i = 0; i < n; ++i) {
            if (n4 >= n || n3 < n2 && arrn3[n3] <= arrn3[n4]) {
                arrn[i] = arrn3[n3];
                arrn2[i] = arrn4[n3];
                ++n3;
                continue;
            }
            arrn[i] = arrn3[n4];
            arrn2[i] = arrn4[n4];
            ++n4;
        }
    }

    private static void mergeSort(int[] arrn, int[] arrn2, int[] arrn3, int[] arrn4, int[] arrn5, int[] arrn6, int n, int n2) {
        int n3 = n2 - n;
        if (n3 <= 14) {
            arrn4[n] = arrn[n];
            arrn6[n] = arrn2[n];
            int n4 = n + 1;
            int n5 = n;
            while (n4 < n2) {
                int n6 = arrn[n4];
                int n7 = arrn2[n4];
                while (arrn4[n5] > n6) {
                    arrn4[n5 + 1] = arrn4[n5];
                    arrn6[n5 + 1] = arrn6[n5];
                    if (n5-- != n) continue;
                }
                arrn4[n5 + 1] = n6;
                arrn6[n5 + 1] = n7;
                n5 = n4++;
            }
            return;
        }
        int n8 = n + n2 >> 1;
        MergeSort.mergeSort(arrn, arrn2, arrn4, arrn3, arrn6, arrn5, n, n8);
        MergeSort.mergeSort(arrn, arrn2, arrn4, arrn3, arrn6, arrn5, n8, n2);
        if (arrn3[n2 - 1] <= arrn3[n]) {
            int n9 = n8 - n;
            int n10 = n2 - n8;
            int n11 = n9 != n10 ? 1 : 0;
            System.arraycopy(arrn3, n, arrn4, n8 + n11, n9);
            System.arraycopy(arrn3, n8, arrn4, n, n10);
            System.arraycopy(arrn5, n, arrn6, n8 + n11, n9);
            System.arraycopy(arrn5, n8, arrn6, n, n10);
            return;
        }
        if (arrn3[n8 - 1] <= arrn3[n8]) {
            System.arraycopy(arrn3, n, arrn4, n, n3);
            System.arraycopy(arrn5, n, arrn6, n, n3);
            return;
        }
        int n12 = n;
        int n13 = n8;
        for (int i = n; i < n2; ++i) {
            if (n13 >= n2 || n12 < n8 && arrn3[n12] <= arrn3[n13]) {
                arrn4[i] = arrn3[n12];
                arrn6[i] = arrn5[n12];
                ++n12;
                continue;
            }
            arrn4[i] = arrn3[n13];
            arrn6[i] = arrn5[n13];
            ++n13;
        }
    }

    private MergeSort() {
    }
}

