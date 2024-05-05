/*
 * Decompiled with CFR 0.152.
 */
package jdk.nashorn.internal.runtime.regexp.joni;

import jdk.nashorn.internal.runtime.regexp.joni.MinMaxLen;
import jdk.nashorn.internal.runtime.regexp.joni.ScanEnvironment;

final class OptEnvironment {
    final MinMaxLen mmd = new MinMaxLen();
    int options;
    int caseFoldFlag;
    ScanEnvironment scanEnv;

    OptEnvironment() {
    }

    void copy(OptEnvironment other) {
        this.mmd.copy(other.mmd);
        this.options = other.options;
        this.caseFoldFlag = other.caseFoldFlag;
        this.scanEnv = other.scanEnv;
    }
}

