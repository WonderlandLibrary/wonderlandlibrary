/*
 * Decompiled with CFR 0.152.
 */
package com.ibm.icu.text;

import com.ibm.icu.text.Bidi;
import com.ibm.icu.text.BidiRun;
import java.util.Arrays;

final class BidiLine {
    BidiLine() {
    }

    static void setTrailingWSStart(Bidi bidi) {
        int start;
        byte[] dirProps = bidi.dirProps;
        byte[] levels = bidi.levels;
        byte paraLevel = bidi.paraLevel;
        if (dirProps[start - 1] == 7) {
            bidi.trailingWSStart = start;
            return;
        }
        for (start = bidi.length; start > 0 && (Bidi.DirPropFlag(dirProps[start - 1]) & Bidi.MASK_WS) != 0; --start) {
        }
        while (start > 0 && levels[start - 1] == paraLevel) {
            --start;
        }
        bidi.trailingWSStart = start;
    }

    static Bidi setLine(Bidi paraBidi, int start, int limit) {
        Bidi lineBidi = new Bidi();
        lineBidi.originalLength = lineBidi.resultLength = limit - start;
        lineBidi.length = lineBidi.resultLength;
        int length = lineBidi.resultLength;
        lineBidi.text = new char[length];
        System.arraycopy(paraBidi.text, start, lineBidi.text, 0, length);
        lineBidi.paraLevel = paraBidi.GetParaLevelAt(start);
        lineBidi.paraCount = paraBidi.paraCount;
        lineBidi.runs = new BidiRun[0];
        lineBidi.reorderingMode = paraBidi.reorderingMode;
        lineBidi.reorderingOptions = paraBidi.reorderingOptions;
        if (paraBidi.controlCount > 0) {
            for (int j2 = start; j2 < limit; ++j2) {
                if (!Bidi.IsBidiControlChar(paraBidi.text[j2])) continue;
                ++lineBidi.controlCount;
            }
            lineBidi.resultLength -= lineBidi.controlCount;
        }
        lineBidi.getDirPropsMemory(length);
        lineBidi.dirProps = lineBidi.dirPropsMemory;
        System.arraycopy(paraBidi.dirProps, start, lineBidi.dirProps, 0, length);
        lineBidi.getLevelsMemory(length);
        lineBidi.levels = lineBidi.levelsMemory;
        System.arraycopy(paraBidi.levels, start, lineBidi.levels, 0, length);
        lineBidi.runCount = -1;
        if (paraBidi.direction != 2) {
            lineBidi.direction = paraBidi.direction;
            lineBidi.trailingWSStart = paraBidi.trailingWSStart <= start ? 0 : (paraBidi.trailingWSStart < limit ? paraBidi.trailingWSStart - start : length);
        } else {
            byte[] levels = lineBidi.levels;
            BidiLine.setTrailingWSStart(lineBidi);
            int trailingWSStart = lineBidi.trailingWSStart;
            if (trailingWSStart == 0) {
                lineBidi.direction = (byte)(lineBidi.paraLevel & 1);
            } else {
                byte level = (byte)(levels[0] & 1);
                if (trailingWSStart < length && (lineBidi.paraLevel & 1) != level) {
                    lineBidi.direction = (byte)2;
                } else {
                    int i2 = 1;
                    while (true) {
                        if (i2 == trailingWSStart) {
                            lineBidi.direction = level;
                            break;
                        }
                        if ((levels[i2] & 1) != level) {
                            lineBidi.direction = (byte)2;
                            break;
                        }
                        ++i2;
                    }
                }
            }
            switch (lineBidi.direction) {
                case 0: {
                    lineBidi.paraLevel = (byte)(lineBidi.paraLevel + 1 & 0xFFFFFFFE);
                    lineBidi.trailingWSStart = 0;
                    break;
                }
                case 1: {
                    lineBidi.paraLevel = (byte)(lineBidi.paraLevel | 1);
                    lineBidi.trailingWSStart = 0;
                    break;
                }
            }
        }
        lineBidi.paraBidi = paraBidi;
        return lineBidi;
    }

    static byte getLevelAt(Bidi bidi, int charIndex) {
        if (bidi.direction != 2 || charIndex >= bidi.trailingWSStart) {
            return bidi.GetParaLevelAt(charIndex);
        }
        return bidi.levels[charIndex];
    }

    static byte[] getLevels(Bidi bidi) {
        int start = bidi.trailingWSStart;
        int length = bidi.length;
        if (start != length) {
            Arrays.fill(bidi.levels, start, length, bidi.paraLevel);
            bidi.trailingWSStart = length;
        }
        if (length < bidi.levels.length) {
            byte[] levels = new byte[length];
            System.arraycopy(bidi.levels, 0, levels, 0, length);
            return levels;
        }
        return bidi.levels;
    }

    static BidiRun getLogicalRun(Bidi bidi, int logicalPosition) {
        BidiRun newRun = new BidiRun();
        BidiLine.getRuns(bidi);
        int runCount = bidi.runCount;
        int visualStart = 0;
        int logicalLimit = 0;
        BidiRun iRun = bidi.runs[0];
        for (int i2 = 0; i2 < runCount; ++i2) {
            iRun = bidi.runs[i2];
            logicalLimit = iRun.start + iRun.limit - visualStart;
            if (logicalPosition >= iRun.start && logicalPosition < logicalLimit) break;
            visualStart = iRun.limit;
        }
        newRun.start = iRun.start;
        newRun.limit = logicalLimit;
        newRun.level = iRun.level;
        return newRun;
    }

    static BidiRun getVisualRun(Bidi bidi, int runIndex) {
        int start = bidi.runs[runIndex].start;
        byte level = bidi.runs[runIndex].level;
        int limit = runIndex > 0 ? start + bidi.runs[runIndex].limit - bidi.runs[runIndex - 1].limit : start + bidi.runs[0].limit;
        return new BidiRun(start, limit, level);
    }

    static void getSingleRun(Bidi bidi, byte level) {
        bidi.runs = bidi.simpleRuns;
        bidi.runCount = 1;
        bidi.runs[0] = new BidiRun(0, bidi.length, level);
    }

    private static void reorderLine(Bidi bidi, byte minLevel, byte maxLevel) {
        BidiRun tempRun;
        int firstRun;
        if (maxLevel <= (minLevel | 1)) {
            return;
        }
        minLevel = (byte)(minLevel + 1);
        BidiRun[] runs = bidi.runs;
        byte[] levels = bidi.levels;
        int runCount = bidi.runCount;
        if (bidi.trailingWSStart < bidi.length) {
            --runCount;
        }
        block0: while ((maxLevel = (byte)(maxLevel - 1)) >= minLevel) {
            firstRun = 0;
            while (true) {
                if (firstRun < runCount && levels[runs[firstRun].start] < maxLevel) {
                    ++firstRun;
                    continue;
                }
                if (firstRun >= runCount) continue block0;
                int limitRun = firstRun;
                while (++limitRun < runCount && levels[runs[limitRun].start] >= maxLevel) {
                }
                for (int endRun = limitRun - 1; firstRun < endRun; ++firstRun, --endRun) {
                    tempRun = runs[firstRun];
                    runs[firstRun] = runs[endRun];
                    runs[endRun] = tempRun;
                }
                if (limitRun == runCount) continue block0;
                firstRun = limitRun + 1;
            }
        }
        if ((minLevel & 1) == 0) {
            firstRun = 0;
            if (bidi.trailingWSStart == bidi.length) {
                --runCount;
            }
            while (firstRun < runCount) {
                tempRun = runs[firstRun];
                runs[firstRun] = runs[runCount];
                runs[runCount] = tempRun;
                ++firstRun;
                --runCount;
            }
        }
    }

    static int getRunFromLogicalIndex(Bidi bidi, int logicalIndex) {
        BidiRun[] runs = bidi.runs;
        int runCount = bidi.runCount;
        int visualStart = 0;
        for (int i2 = 0; i2 < runCount; ++i2) {
            int length = runs[i2].limit - visualStart;
            int logicalStart = runs[i2].start;
            if (logicalIndex >= logicalStart && logicalIndex < logicalStart + length) {
                return i2;
            }
            visualStart += length;
        }
        throw new IllegalStateException("Internal ICU error in getRunFromLogicalIndex");
    }

    static void getRuns(Bidi bidi) {
        if (bidi.runCount >= 0) {
            return;
        }
        if (bidi.direction != 2) {
            BidiLine.getSingleRun(bidi, bidi.paraLevel);
        } else {
            int i2;
            int length = bidi.length;
            byte[] levels = bidi.levels;
            byte level = -1;
            int limit = bidi.trailingWSStart;
            int runCount = 0;
            for (i2 = 0; i2 < limit; ++i2) {
                if (levels[i2] == level) continue;
                ++runCount;
                level = levels[i2];
            }
            if (runCount == 1 && limit == length) {
                BidiLine.getSingleRun(bidi, levels[0]);
            } else {
                byte minLevel = 126;
                byte maxLevel = 0;
                if (limit < length) {
                    ++runCount;
                }
                bidi.getRunsMemory(runCount);
                BidiRun[] runs = bidi.runsMemory;
                int runIndex = 0;
                i2 = 0;
                do {
                    int start = i2;
                    level = levels[i2];
                    if (level < minLevel) {
                        minLevel = level;
                    }
                    if (level > maxLevel) {
                        maxLevel = level;
                    }
                    while (++i2 < limit && levels[i2] == level) {
                    }
                    runs[runIndex] = new BidiRun(start, i2 - start, level);
                    ++runIndex;
                } while (i2 < limit);
                if (limit < length) {
                    runs[runIndex] = new BidiRun(limit, length - limit, bidi.paraLevel);
                    if (bidi.paraLevel < minLevel) {
                        minLevel = bidi.paraLevel;
                    }
                }
                bidi.runs = runs;
                bidi.runCount = runCount;
                BidiLine.reorderLine(bidi, minLevel, maxLevel);
                limit = 0;
                for (i2 = 0; i2 < runCount; ++i2) {
                    runs[i2].level = levels[runs[i2].start];
                    limit = runs[i2].limit += limit;
                }
                if (runIndex < runCount) {
                    int trailingRun = (bidi.paraLevel & 1) != 0 ? 0 : runIndex;
                    runs[trailingRun].level = bidi.paraLevel;
                }
            }
        }
        if (bidi.insertPoints.size > 0) {
            for (int ip = 0; ip < bidi.insertPoints.size; ++ip) {
                Bidi.Point point = bidi.insertPoints.points[ip];
                int runIndex = BidiLine.getRunFromLogicalIndex(bidi, point.pos);
                bidi.runs[runIndex].insertRemove |= point.flag;
            }
        }
        if (bidi.controlCount > 0) {
            for (int ic = 0; ic < bidi.length; ++ic) {
                char c2 = bidi.text[ic];
                if (!Bidi.IsBidiControlChar(c2)) continue;
                int runIndex = BidiLine.getRunFromLogicalIndex(bidi, ic);
                --bidi.runs[runIndex].insertRemove;
            }
        }
    }

    static int[] prepareReorder(byte[] levels, byte[] pMinLevel, byte[] pMaxLevel) {
        if (levels == null || levels.length <= 0) {
            return null;
        }
        byte minLevel = 126;
        byte maxLevel = 0;
        int start = levels.length;
        while (start > 0) {
            byte level;
            if ((level = levels[--start]) < 0) {
                return null;
            }
            if (level > 126) {
                return null;
            }
            if (level < minLevel) {
                minLevel = level;
            }
            if (level <= maxLevel) continue;
            maxLevel = level;
        }
        pMinLevel[0] = minLevel;
        pMaxLevel[0] = maxLevel;
        int[] indexMap = new int[levels.length];
        start = levels.length;
        while (start > 0) {
            indexMap[--start] = start;
        }
        return indexMap;
    }

    static int[] reorderLogical(byte[] levels) {
        byte[] aMinLevel = new byte[1];
        byte[] aMaxLevel = new byte[1];
        int[] indexMap = BidiLine.prepareReorder(levels, aMinLevel, aMaxLevel);
        if (indexMap == null) {
            return null;
        }
        byte minLevel = aMinLevel[0];
        byte maxLevel = aMaxLevel[0];
        if (minLevel == maxLevel && (minLevel & 1) == 0) {
            return indexMap;
        }
        minLevel = (byte)(minLevel | 1);
        block0: do {
            int start = 0;
            while (true) {
                if (start < levels.length && levels[start] < maxLevel) {
                    ++start;
                    continue;
                }
                if (start >= levels.length) continue block0;
                int limit = start;
                while (++limit < levels.length && levels[limit] >= maxLevel) {
                }
                int sumOfSosEos = start + limit - 1;
                do {
                    indexMap[start] = sumOfSosEos - indexMap[start];
                } while (++start < limit);
                if (limit == levels.length) continue block0;
                start = limit + 1;
            }
        } while ((maxLevel = (byte)(maxLevel - 1)) >= minLevel);
        return indexMap;
    }

    static int[] reorderVisual(byte[] levels) {
        byte[] aMinLevel = new byte[1];
        byte[] aMaxLevel = new byte[1];
        int[] indexMap = BidiLine.prepareReorder(levels, aMinLevel, aMaxLevel);
        if (indexMap == null) {
            return null;
        }
        byte minLevel = aMinLevel[0];
        byte maxLevel = aMaxLevel[0];
        if (minLevel == maxLevel && (minLevel & 1) == 0) {
            return indexMap;
        }
        minLevel = (byte)(minLevel | 1);
        block0: do {
            int start = 0;
            while (true) {
                if (start < levels.length && levels[start] < maxLevel) {
                    ++start;
                    continue;
                }
                if (start >= levels.length) continue block0;
                int limit = start;
                while (++limit < levels.length && levels[limit] >= maxLevel) {
                }
                for (int end = limit - 1; start < end; ++start, --end) {
                    int temp = indexMap[start];
                    indexMap[start] = indexMap[end];
                    indexMap[end] = temp;
                }
                if (limit == levels.length) continue block0;
                start = limit + 1;
            }
        } while ((maxLevel = (byte)(maxLevel - 1)) >= minLevel);
        return indexMap;
    }

    static int getVisualIndex(Bidi bidi, int logicalIndex) {
        int i2;
        BidiRun[] runs;
        int visualIndex = -1;
        switch (bidi.direction) {
            case 0: {
                visualIndex = logicalIndex;
                break;
            }
            case 1: {
                visualIndex = bidi.length - logicalIndex - 1;
                break;
            }
            default: {
                BidiLine.getRuns(bidi);
                runs = bidi.runs;
                int visualStart = 0;
                for (i2 = 0; i2 < bidi.runCount; ++i2) {
                    int length = runs[i2].limit - visualStart;
                    int offset = logicalIndex - runs[i2].start;
                    if (offset >= 0 && offset < length) {
                        if (runs[i2].isEvenRun()) {
                            visualIndex = visualStart + offset;
                            break;
                        }
                        visualIndex = visualStart + length - offset - 1;
                        break;
                    }
                    visualStart += length;
                }
                if (i2 < bidi.runCount) break;
                return -1;
            }
        }
        if (bidi.insertPoints.size > 0) {
            runs = bidi.runs;
            int visualStart = 0;
            int markFound = 0;
            i2 = 0;
            while (true) {
                int length = runs[i2].limit - visualStart;
                int insertRemove = runs[i2].insertRemove;
                if ((insertRemove & 5) > 0) {
                    ++markFound;
                }
                if (visualIndex < runs[i2].limit) {
                    return visualIndex + markFound;
                }
                if ((insertRemove & 0xA) > 0) {
                    ++markFound;
                }
                ++i2;
                visualStart += length;
            }
        }
        if (bidi.controlCount > 0) {
            runs = bidi.runs;
            int visualStart = 0;
            int controlFound = 0;
            char uchar = bidi.text[logicalIndex];
            if (Bidi.IsBidiControlChar(uchar)) {
                return -1;
            }
            i2 = 0;
            while (true) {
                int length = runs[i2].limit - visualStart;
                int insertRemove = runs[i2].insertRemove;
                if (visualIndex >= runs[i2].limit) {
                    controlFound -= insertRemove;
                } else {
                    int limit;
                    int start;
                    if (insertRemove == 0) {
                        return visualIndex - controlFound;
                    }
                    if (runs[i2].isEvenRun()) {
                        start = runs[i2].start;
                        limit = logicalIndex;
                    } else {
                        start = logicalIndex + 1;
                        limit = runs[i2].start + length;
                    }
                    for (int j2 = start; j2 < limit; ++j2) {
                        uchar = bidi.text[j2];
                        if (!Bidi.IsBidiControlChar(uchar)) continue;
                        ++controlFound;
                    }
                    return visualIndex - controlFound;
                }
                ++i2;
                visualStart += length;
            }
        }
        return visualIndex;
    }

    static int getLogicalIndex(Bidi bidi, int visualIndex) {
        int i2;
        int runCount;
        BidiRun[] runs;
        block21: {
            int insertRemove;
            runs = bidi.runs;
            runCount = bidi.runCount;
            if (bidi.insertPoints.size > 0) {
                int markFound = 0;
                int visualStart = 0;
                i2 = 0;
                while (true) {
                    int length = runs[i2].limit - visualStart;
                    insertRemove = runs[i2].insertRemove;
                    if ((insertRemove & 5) > 0) {
                        if (visualIndex <= visualStart + markFound) {
                            return -1;
                        }
                        ++markFound;
                    }
                    if (visualIndex < runs[i2].limit + markFound) {
                        visualIndex -= markFound;
                        break block21;
                    }
                    if ((insertRemove & 0xA) > 0) {
                        if (visualIndex == visualStart + length + markFound) {
                            return -1;
                        }
                        ++markFound;
                    }
                    ++i2;
                    visualStart += length;
                }
            }
            if (bidi.controlCount > 0) {
                int controlFound = 0;
                int visualStart = 0;
                i2 = 0;
                while (true) {
                    int length = runs[i2].limit - visualStart;
                    insertRemove = runs[i2].insertRemove;
                    if (visualIndex >= runs[i2].limit - controlFound + insertRemove) {
                        controlFound -= insertRemove;
                    } else {
                        if (insertRemove == 0) {
                            visualIndex += controlFound;
                            break;
                        }
                        int logicalStart = runs[i2].start;
                        boolean evenRun = runs[i2].isEvenRun();
                        int logicalEnd = logicalStart + length - 1;
                        for (int j2 = 0; j2 < length; ++j2) {
                            int k2 = evenRun ? logicalStart + j2 : logicalEnd - j2;
                            char uchar = bidi.text[k2];
                            if (Bidi.IsBidiControlChar(uchar)) {
                                ++controlFound;
                            }
                            if (visualIndex + controlFound == visualStart + j2) break;
                        }
                        visualIndex += controlFound;
                        break;
                    }
                    ++i2;
                    visualStart += length;
                }
            }
        }
        if (runCount <= 10) {
            i2 = 0;
            while (visualIndex >= runs[i2].limit) {
                ++i2;
            }
        } else {
            int begin = 0;
            int limit = runCount;
            while (true) {
                i2 = begin + limit >>> 1;
                if (visualIndex >= runs[i2].limit) {
                    begin = i2 + 1;
                    continue;
                }
                if (i2 == 0 || visualIndex >= runs[i2 - 1].limit) break;
                limit = i2;
            }
        }
        int start = runs[i2].start;
        if (runs[i2].isEvenRun()) {
            if (i2 > 0) {
                visualIndex -= runs[i2 - 1].limit;
            }
            return start + visualIndex;
        }
        return start + runs[i2].limit - visualIndex - 1;
    }

    static int[] getLogicalMap(Bidi bidi) {
        int[] indexMap;
        block18: {
            int logicalStart;
            int visualStart;
            BidiRun[] runs;
            block17: {
                runs = bidi.runs;
                indexMap = new int[bidi.length];
                if (bidi.length > bidi.resultLength) {
                    Arrays.fill(indexMap, -1);
                }
                visualStart = 0;
                for (int j2 = 0; j2 < bidi.runCount; ++j2) {
                    logicalStart = runs[j2].start;
                    int visualLimit = runs[j2].limit;
                    if (runs[j2].isEvenRun()) {
                        do {
                            indexMap[logicalStart++] = visualStart++;
                        } while (visualStart < visualLimit);
                        continue;
                    }
                    logicalStart += visualLimit - visualStart;
                    do {
                        indexMap[--logicalStart] = visualStart++;
                    } while (visualStart < visualLimit);
                }
                if (bidi.insertPoints.size <= 0) break block17;
                int markFound = 0;
                int runCount = bidi.runCount;
                runs = bidi.runs;
                visualStart = 0;
                int i2 = 0;
                while (i2 < runCount) {
                    int length = runs[i2].limit - visualStart;
                    int insertRemove = runs[i2].insertRemove;
                    if ((insertRemove & 5) > 0) {
                        ++markFound;
                    }
                    if (markFound > 0) {
                        logicalStart = runs[i2].start;
                        int logicalLimit = logicalStart + length;
                        int j3 = logicalStart;
                        while (j3 < logicalLimit) {
                            int n2 = j3++;
                            indexMap[n2] = indexMap[n2] + markFound;
                        }
                    }
                    if ((insertRemove & 0xA) > 0) {
                        ++markFound;
                    }
                    ++i2;
                    visualStart += length;
                }
                break block18;
            }
            if (bidi.controlCount <= 0) break block18;
            int controlFound = 0;
            int runCount = bidi.runCount;
            runs = bidi.runs;
            visualStart = 0;
            int i3 = 0;
            while (i3 < runCount) {
                int length = runs[i3].limit - visualStart;
                int insertRemove = runs[i3].insertRemove;
                if (controlFound - insertRemove != 0) {
                    int j4;
                    logicalStart = runs[i3].start;
                    boolean evenRun = runs[i3].isEvenRun();
                    int logicalLimit = logicalStart + length;
                    if (insertRemove == 0) {
                        j4 = logicalStart;
                        while (j4 < logicalLimit) {
                            int n3 = j4++;
                            indexMap[n3] = indexMap[n3] - controlFound;
                        }
                    } else {
                        for (j4 = 0; j4 < length; ++j4) {
                            int k2 = evenRun ? logicalStart + j4 : logicalLimit - j4 - 1;
                            char uchar = bidi.text[k2];
                            if (Bidi.IsBidiControlChar(uchar)) {
                                ++controlFound;
                                indexMap[k2] = -1;
                                continue;
                            }
                            int n4 = k2;
                            indexMap[n4] = indexMap[n4] - controlFound;
                        }
                    }
                }
                ++i3;
                visualStart += length;
            }
        }
        return indexMap;
    }

    static int[] getVisualMap(Bidi bidi) {
        int visualLimit;
        int logicalStart;
        BidiRun[] runs = bidi.runs;
        int allocLength = bidi.length > bidi.resultLength ? bidi.length : bidi.resultLength;
        int[] indexMap = new int[allocLength];
        int visualStart = 0;
        int idx = 0;
        for (int j2 = 0; j2 < bidi.runCount; ++j2) {
            logicalStart = runs[j2].start;
            visualLimit = runs[j2].limit;
            if (runs[j2].isEvenRun()) {
                do {
                    indexMap[idx++] = logicalStart++;
                } while (++visualStart < visualLimit);
                continue;
            }
            logicalStart += visualLimit - visualStart;
            do {
                indexMap[idx++] = --logicalStart;
            } while (++visualStart < visualLimit);
        }
        if (bidi.insertPoints.size > 0) {
            int insertRemove;
            int i2;
            int markFound = 0;
            int runCount = bidi.runCount;
            runs = bidi.runs;
            for (i2 = 0; i2 < runCount; ++i2) {
                insertRemove = runs[i2].insertRemove;
                if ((insertRemove & 5) > 0) {
                    ++markFound;
                }
                if ((insertRemove & 0xA) <= 0) continue;
                ++markFound;
            }
            int k2 = bidi.resultLength;
            for (i2 = runCount - 1; i2 >= 0 && markFound > 0; --i2) {
                insertRemove = runs[i2].insertRemove;
                if ((insertRemove & 0xA) > 0) {
                    indexMap[--k2] = -1;
                    --markFound;
                }
                visualStart = i2 > 0 ? runs[i2 - 1].limit : 0;
                for (int j3 = runs[i2].limit - 1; j3 >= visualStart && markFound > 0; --j3) {
                    indexMap[--k2] = indexMap[j3];
                }
                if ((insertRemove & 5) <= 0) continue;
                indexMap[--k2] = -1;
                --markFound;
            }
        } else if (bidi.controlCount > 0) {
            int runCount = bidi.runCount;
            runs = bidi.runs;
            visualStart = 0;
            int k3 = 0;
            int i3 = 0;
            while (i3 < runCount) {
                int j4;
                int length = runs[i3].limit - visualStart;
                int insertRemove = runs[i3].insertRemove;
                if (insertRemove == 0 && k3 == visualStart) {
                    k3 += length;
                } else if (insertRemove == 0) {
                    visualLimit = runs[i3].limit;
                    for (j4 = visualStart; j4 < visualLimit; ++j4) {
                        indexMap[k3++] = indexMap[j4];
                    }
                } else {
                    logicalStart = runs[i3].start;
                    boolean evenRun = runs[i3].isEvenRun();
                    int logicalEnd = logicalStart + length - 1;
                    for (j4 = 0; j4 < length; ++j4) {
                        int m2 = evenRun ? logicalStart + j4 : logicalEnd - j4;
                        char uchar = bidi.text[m2];
                        if (Bidi.IsBidiControlChar(uchar)) continue;
                        indexMap[k3++] = m2;
                    }
                }
                ++i3;
                visualStart += length;
            }
        }
        if (allocLength == bidi.resultLength) {
            return indexMap;
        }
        int[] newMap = new int[bidi.resultLength];
        System.arraycopy(indexMap, 0, newMap, 0, bidi.resultLength);
        return newMap;
    }

    static int[] invertMap(int[] srcMap) {
        int srcEntry;
        int i2;
        int srcLength = srcMap.length;
        int destLength = -1;
        int count = 0;
        for (i2 = 0; i2 < srcLength; ++i2) {
            srcEntry = srcMap[i2];
            if (srcEntry > destLength) {
                destLength = srcEntry;
            }
            if (srcEntry < 0) continue;
            ++count;
        }
        int[] destMap = new int[++destLength];
        if (count < destLength) {
            Arrays.fill(destMap, -1);
        }
        for (i2 = 0; i2 < srcLength; ++i2) {
            srcEntry = srcMap[i2];
            if (srcEntry < 0) continue;
            destMap[srcEntry] = i2;
        }
        return destMap;
    }
}

