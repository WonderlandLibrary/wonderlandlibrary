/*
 * Decompiled with CFR 0.152.
 */
package com.ibm.icu.impl.coll;

import com.ibm.icu.impl.coll.CollationIterator;
import com.ibm.icu.impl.coll.CollationSettings;

public final class CollationCompare {
    public static int compareUpToQuaternary(CollationIterator left, CollationIterator right, CollationSettings settings) {
        int leftIndex;
        int options = settings.options;
        long variableTop = (options & 0xC) == 0 ? 0L : settings.variableTop + 1L;
        boolean anyVariable = false;
        while (true) {
            long rightPrimary;
            long ce;
            long leftPrimary;
            if ((leftPrimary = (ce = left.nextCE()) >>> 32) < variableTop && leftPrimary > 0x2000000L) {
                anyVariable = true;
                do {
                    left.setCurrentCE(ce & 0xFFFFFFFF00000000L);
                    while ((leftPrimary = (ce = left.nextCE()) >>> 32) == 0L) {
                        left.setCurrentCE(0L);
                    }
                } while (leftPrimary < variableTop && leftPrimary > 0x2000000L);
            }
            if (leftPrimary == 0L) continue;
            do {
                long ce2;
                if ((rightPrimary = (ce2 = right.nextCE()) >>> 32) >= variableTop || rightPrimary <= 0x2000000L) continue;
                anyVariable = true;
                do {
                    right.setCurrentCE(ce2 & 0xFFFFFFFF00000000L);
                    while ((rightPrimary = (ce2 = right.nextCE()) >>> 32) == 0L) {
                        right.setCurrentCE(0L);
                    }
                } while (rightPrimary < variableTop && rightPrimary > 0x2000000L);
            } while (rightPrimary == 0L);
            if (leftPrimary != rightPrimary) {
                if (settings.hasReordering()) {
                    leftPrimary = settings.reorder(leftPrimary);
                    rightPrimary = settings.reorder(rightPrimary);
                }
                return leftPrimary < rightPrimary ? -1 : 1;
            }
            if (leftPrimary == 1L) break;
        }
        if (CollationSettings.getStrength(options) >= 1) {
            if ((options & 0x800) == 0) {
                int leftIndex2 = 0;
                int rightIndex = 0;
                while (true) {
                    int rightSecondary;
                    int leftSecondary;
                    if ((leftSecondary = (int)left.getCE(leftIndex2++) >>> 16) == 0) {
                        continue;
                    }
                    while ((rightSecondary = (int)right.getCE(rightIndex++) >>> 16) == 0) {
                    }
                    if (leftSecondary != rightSecondary) {
                        return leftSecondary < rightSecondary ? -1 : 1;
                    }
                    if (leftSecondary == 256) break;
                }
            } else {
                int leftStart = 0;
                int rightStart = 0;
                while (true) {
                    int leftSecondary;
                    long p2;
                    int leftLimit = leftStart;
                    while ((p2 = left.getCE(leftLimit) >>> 32) > 0x2000000L || p2 == 0L) {
                        ++leftLimit;
                    }
                    int rightLimit = rightStart;
                    while ((p2 = right.getCE(rightLimit) >>> 32) > 0x2000000L || p2 == 0L) {
                        ++rightLimit;
                    }
                    int leftIndex3 = leftLimit;
                    int rightIndex = rightLimit;
                    do {
                        leftSecondary = 0;
                        while (leftSecondary == 0 && leftIndex3 > leftStart) {
                            leftSecondary = (int)left.getCE(--leftIndex3) >>> 16;
                        }
                        int rightSecondary = 0;
                        while (rightSecondary == 0 && rightIndex > rightStart) {
                            rightSecondary = (int)right.getCE(--rightIndex) >>> 16;
                        }
                        if (leftSecondary == rightSecondary) continue;
                        return leftSecondary < rightSecondary ? -1 : 1;
                    } while (leftSecondary != 0);
                    assert (left.getCE(leftLimit) == right.getCE(rightLimit));
                    if (p2 == 1L) break;
                    leftStart = leftLimit + 1;
                    rightStart = rightLimit + 1;
                }
            }
        }
        if ((options & 0x400) != 0) {
            int leftLower32;
            int strength = CollationSettings.getStrength(options);
            leftIndex = 0;
            int rightIndex = 0;
            do {
                int rightCase;
                int leftCase;
                if (strength == 0) {
                    long ce;
                    do {
                        ce = left.getCE(leftIndex++);
                        leftCase = (int)ce;
                    } while (ce >>> 32 == 0L || leftCase == 0);
                    leftLower32 = leftCase;
                    leftCase &= 0xC000;
                    do {
                        ce = right.getCE(rightIndex++);
                        rightCase = (int)ce;
                    } while (ce >>> 32 == 0L || rightCase == 0);
                    rightCase &= 0xC000;
                } else {
                    while (((leftCase = (int)left.getCE(leftIndex++)) & 0xFFFF0000) == 0) {
                    }
                    leftLower32 = leftCase;
                    leftCase &= 0xC000;
                    while (((rightCase = (int)right.getCE(rightIndex++)) & 0xFFFF0000) == 0) {
                    }
                    rightCase &= 0xC000;
                }
                if (leftCase == rightCase) continue;
                if ((options & 0x100) == 0) {
                    return leftCase < rightCase ? -1 : 1;
                }
                return leftCase < rightCase ? 1 : -1;
            } while (leftLower32 >>> 16 != 256);
        }
        if (CollationSettings.getStrength(options) <= 1) {
            return 0;
        }
        int tertiaryMask = CollationSettings.getTertiaryMask(options);
        leftIndex = 0;
        int rightIndex = 0;
        int anyQuaternaries = 0;
        while (true) {
            int rightLower32;
            int rightTertiary;
            int leftLower32 = (int)left.getCE(leftIndex++);
            anyQuaternaries |= leftLower32;
            assert ((leftLower32 & 0x3F3F) != 0 || (leftLower32 & 0xC0C0) == 0);
            int leftTertiary = leftLower32 & tertiaryMask;
            if (leftTertiary == 0) continue;
            do {
                rightLower32 = (int)right.getCE(rightIndex++);
                anyQuaternaries |= rightLower32;
                assert ((rightLower32 & 0x3F3F) != 0 || (rightLower32 & 0xC0C0) == 0);
            } while ((rightTertiary = rightLower32 & tertiaryMask) == 0);
            if (leftTertiary != rightTertiary) {
                if (CollationSettings.sortsTertiaryUpperCaseFirst(options)) {
                    if (leftTertiary > 256) {
                        leftTertiary = (leftLower32 & 0xFFFF0000) != 0 ? (leftTertiary ^= 0xC000) : (leftTertiary += 16384);
                    }
                    if (rightTertiary > 256) {
                        rightTertiary = (rightLower32 & 0xFFFF0000) != 0 ? (rightTertiary ^= 0xC000) : (rightTertiary += 16384);
                    }
                }
                return leftTertiary < rightTertiary ? -1 : 1;
            }
            if (leftTertiary == 256) break;
        }
        if (CollationSettings.getStrength(options) <= 2) {
            return 0;
        }
        if (!anyVariable && (anyQuaternaries & 0xC0) == 0) {
            return 0;
        }
        leftIndex = 0;
        rightIndex = 0;
        while (true) {
            long rightQuaternary;
            long ce;
            long leftQuaternary;
            leftQuaternary = (leftQuaternary = (ce = left.getCE(leftIndex++)) & 0xFFFFL) <= 256L ? ce >>> 32 : (leftQuaternary |= 0xFFFFFF3FL);
            if (leftQuaternary == 0L) continue;
            do {
                long ce3;
                if ((rightQuaternary = (ce3 = right.getCE(rightIndex++)) & 0xFFFFL) <= 256L) {
                    rightQuaternary = ce3 >>> 32;
                    continue;
                }
                rightQuaternary |= 0xFFFFFF3FL;
            } while (rightQuaternary == 0L);
            if (leftQuaternary != rightQuaternary) {
                if (settings.hasReordering()) {
                    leftQuaternary = settings.reorder(leftQuaternary);
                    rightQuaternary = settings.reorder(rightQuaternary);
                }
                return leftQuaternary < rightQuaternary ? -1 : 1;
            }
            if (leftQuaternary == 1L) break;
        }
        return 0;
    }
}

