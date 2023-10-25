package com.ibm.icu.text;

import java.util.Arrays;

final class BidiLine {
  static void setTrailingWSStart(Bidi bidi) {
    byte[] dirProps = bidi.dirProps;
    byte[] levels = bidi.levels;
    int start = bidi.length;
    byte paraLevel = bidi.paraLevel;
    if (Bidi.NoContextRTL(dirProps[start - 1]) == 7) {
      bidi.trailingWSStart = start;
      return;
    } 
    while (start > 0 && (Bidi.DirPropFlagNC(dirProps[start - 1]) & Bidi.MASK_WS) != 0)
      start--; 
    while (start > 0 && levels[start - 1] == paraLevel)
      start--; 
    bidi.trailingWSStart = start;
  }
  
  static Bidi setLine(Bidi paraBidi, int start, int limit) {
    Bidi lineBidi = new Bidi();
    int length = lineBidi.length = lineBidi.originalLength = lineBidi.resultLength = limit - start;
    lineBidi.text = new char[length];
    System.arraycopy(paraBidi.text, start, lineBidi.text, 0, length);
    lineBidi.paraLevel = paraBidi.GetParaLevelAt(start);
    lineBidi.paraCount = paraBidi.paraCount;
    lineBidi.runs = new BidiRun[0];
    lineBidi.reorderingMode = paraBidi.reorderingMode;
    lineBidi.reorderingOptions = paraBidi.reorderingOptions;
    if (paraBidi.controlCount > 0) {
      for (int j = start; j < limit; j++) {
        if (Bidi.IsBidiControlChar(paraBidi.text[j]))
          lineBidi.controlCount++; 
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
      if (paraBidi.trailingWSStart <= start) {
        lineBidi.trailingWSStart = 0;
      } else if (paraBidi.trailingWSStart < limit) {
        paraBidi.trailingWSStart -= start;
      } else {
        lineBidi.trailingWSStart = length;
      } 
    } else {
      byte[] levels = lineBidi.levels;
      setTrailingWSStart(lineBidi);
      int trailingWSStart = lineBidi.trailingWSStart;
      if (trailingWSStart == 0) {
        lineBidi.direction = (byte)(lineBidi.paraLevel & 0x1);
      } else {
        byte level = (byte)(levels[0] & 0x1);
        if (trailingWSStart < length && (lineBidi.paraLevel & 0x1) != level) {
          lineBidi.direction = 2;
        } else {
          for (int i = 1;; i++) {
            if (i == trailingWSStart) {
              lineBidi.direction = level;
              break;
            } 
            if ((levels[i] & 0x1) != level) {
              lineBidi.direction = 2;
              break;
            } 
          } 
        } 
      } 
      switch (lineBidi.direction) {
        case 0:
          lineBidi.paraLevel = (byte)(lineBidi.paraLevel + 1 & 0xFFFFFFFE);
          lineBidi.trailingWSStart = 0;
          break;
        case 1:
          lineBidi.paraLevel = (byte)(lineBidi.paraLevel | 0x1);
          lineBidi.trailingWSStart = 0;
          break;
      } 
    } 
    lineBidi.paraBidi = paraBidi;
    return lineBidi;
  }
  
  static byte getLevelAt(Bidi bidi, int charIndex) {
    if (bidi.direction != 2 || charIndex >= bidi.trailingWSStart)
      return bidi.GetParaLevelAt(charIndex); 
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
    getRuns(bidi);
    int runCount = bidi.runCount;
    int visualStart = 0, logicalLimit = 0;
    BidiRun iRun = bidi.runs[0];
    for (int i = 0; i < runCount; i++) {
      iRun = bidi.runs[i];
      logicalLimit = iRun.start + iRun.limit - visualStart;
      if (logicalPosition >= iRun.start && logicalPosition < logicalLimit)
        break; 
      visualStart = iRun.limit;
    } 
    newRun.start = iRun.start;
    newRun.limit = logicalLimit;
    newRun.level = iRun.level;
    return newRun;
  }
  
  static BidiRun getVisualRun(Bidi bidi, int runIndex) {
    int limit, start = (bidi.runs[runIndex]).start;
    byte level = (bidi.runs[runIndex]).level;
    if (runIndex > 0) {
      limit = start + (bidi.runs[runIndex]).limit - (bidi.runs[runIndex - 1]).limit;
    } else {
      limit = start + (bidi.runs[0]).limit;
    } 
    return new BidiRun(start, limit, level);
  }
  
  static void getSingleRun(Bidi bidi, byte level) {
    bidi.runs = bidi.simpleRuns;
    bidi.runCount = 1;
    bidi.runs[0] = new BidiRun(0, bidi.length, level);
  }
  
  private static void reorderLine(Bidi bidi, byte minLevel, byte maxLevel) {
    if (maxLevel <= (minLevel | 0x1))
      return; 
    minLevel = (byte)(minLevel + 1);
    BidiRun[] runs = bidi.runs;
    byte[] levels = bidi.levels;
    int runCount = bidi.runCount;
    if (bidi.trailingWSStart < bidi.length)
      runCount--; 
    label41: while (true) {
      maxLevel = (byte)(maxLevel - 1);
      if (maxLevel >= minLevel) {
        int firstRun = 0;
        while (true) {
          if (firstRun < runCount && levels[(runs[firstRun]).start] < maxLevel) {
            firstRun++;
            continue;
          } 
          if (firstRun >= runCount)
            continue label41; 
          int limitRun;
          for (limitRun = firstRun; ++limitRun < runCount && levels[(runs[limitRun]).start] >= maxLevel;);
          int endRun = limitRun - 1;
          while (firstRun < endRun) {
            BidiRun tempRun = runs[firstRun];
            runs[firstRun] = runs[endRun];
            runs[endRun] = tempRun;
            firstRun++;
            endRun--;
          } 
          if (limitRun == runCount)
            continue label41; 
          firstRun = limitRun + 1;
        } 
      } 
      break;
    } 
    if ((minLevel & 0x1) == 0) {
      int firstRun = 0;
      if (bidi.trailingWSStart == bidi.length)
        runCount--; 
      while (firstRun < runCount) {
        BidiRun tempRun = runs[firstRun];
        runs[firstRun] = runs[runCount];
        runs[runCount] = tempRun;
        firstRun++;
        runCount--;
      } 
    } 
  }
  
  static int getRunFromLogicalIndex(Bidi bidi, int logicalIndex) {
    BidiRun[] runs = bidi.runs;
    int runCount = bidi.runCount, visualStart = 0;
    for (int i = 0; i < runCount; i++) {
      int length = (runs[i]).limit - visualStart;
      int logicalStart = (runs[i]).start;
      if (logicalIndex >= logicalStart && logicalIndex < logicalStart + length)
        return i; 
      visualStart += length;
    } 
    throw new IllegalStateException("Internal ICU error in getRunFromLogicalIndex");
  }
  
  static void getRuns(Bidi bidi) {
    if (bidi.runCount >= 0)
      return; 
    if (bidi.direction != 2) {
      getSingleRun(bidi, bidi.paraLevel);
    } else {
      int length = bidi.length;
      byte[] levels = bidi.levels;
      byte level = 126;
      int limit = bidi.trailingWSStart;
      int runCount = 0;
      int i;
      for (i = 0; i < limit; i++) {
        if (levels[i] != level) {
          runCount++;
          level = levels[i];
        } 
      } 
      if (runCount == 1 && limit == length) {
        getSingleRun(bidi, levels[0]);
      } else {
        byte minLevel = 62;
        byte maxLevel = 0;
        if (limit < length)
          runCount++; 
        bidi.getRunsMemory(runCount);
        BidiRun[] runs = bidi.runsMemory;
        int runIndex = 0;
        i = 0;
        do {
          int start = i;
          level = levels[i];
          if (level < minLevel)
            minLevel = level; 
          if (level > maxLevel)
            maxLevel = level; 
          while (++i < limit && levels[i] == level);
          runs[runIndex] = new BidiRun(start, i - start, level);
          runIndex++;
        } while (i < limit);
        if (limit < length) {
          runs[runIndex] = new BidiRun(limit, length - limit, bidi.paraLevel);
          if (bidi.paraLevel < minLevel)
            minLevel = bidi.paraLevel; 
        } 
        bidi.runs = runs;
        bidi.runCount = runCount;
        reorderLine(bidi, minLevel, maxLevel);
        limit = 0;
        for (i = 0; i < runCount; i++) {
          (runs[i]).level = levels[(runs[i]).start];
          limit = (runs[i]).limit += limit;
        } 
        if (runIndex < runCount) {
          int trailingRun = ((bidi.paraLevel & 0x1) != 0) ? 0 : runIndex;
          (runs[trailingRun]).level = bidi.paraLevel;
        } 
      } 
    } 
    if (bidi.insertPoints.size > 0)
      for (int ip = 0; ip < bidi.insertPoints.size; ip++) {
        Bidi.Point point = bidi.insertPoints.points[ip];
        int runIndex = getRunFromLogicalIndex(bidi, point.pos);
        (bidi.runs[runIndex]).insertRemove |= point.flag;
      }  
    if (bidi.controlCount > 0)
      for (int ic = 0; ic < bidi.length; ic++) {
        char c = bidi.text[ic];
        if (Bidi.IsBidiControlChar(c)) {
          int runIndex = getRunFromLogicalIndex(bidi, ic);
          (bidi.runs[runIndex]).insertRemove--;
        } 
      }  
  }
  
  static int[] prepareReorder(byte[] levels, byte[] pMinLevel, byte[] pMaxLevel) {
    if (levels == null || levels.length <= 0)
      return null; 
    byte minLevel = 62;
    byte maxLevel = 0;
    int start;
    for (start = levels.length; start > 0; ) {
      byte level = levels[--start];
      if (level > 62)
        return null; 
      if (level < minLevel)
        minLevel = level; 
      if (level > maxLevel)
        maxLevel = level; 
    } 
    pMinLevel[0] = minLevel;
    pMaxLevel[0] = maxLevel;
    int[] indexMap = new int[levels.length];
    for (start = levels.length; start > 0; ) {
      start--;
      indexMap[start] = start;
    } 
    return indexMap;
  }
  
  static int[] reorderLogical(byte[] levels) {
    byte[] aMinLevel = new byte[1];
    byte[] aMaxLevel = new byte[1];
    int[] indexMap = prepareReorder(levels, aMinLevel, aMaxLevel);
    if (indexMap == null)
      return null; 
    byte minLevel = aMinLevel[0];
    byte maxLevel = aMaxLevel[0];
    if (minLevel == maxLevel && (minLevel & 0x1) == 0)
      return indexMap; 
    minLevel = (byte)(minLevel | 0x1);
    while (true) {
      int start = 0;
      while (true) {
        if (start < levels.length && levels[start] < maxLevel) {
          start++;
          continue;
        } 
        if (start >= levels.length)
          break; 
        int limit;
        for (limit = start; ++limit < levels.length && levels[limit] >= maxLevel;);
        int sumOfSosEos = start + limit - 1;
        do {
          indexMap[start] = sumOfSosEos - indexMap[start];
        } while (++start < limit);
        if (limit == levels.length)
          break; 
        start = limit + 1;
      } 
      maxLevel = (byte)(maxLevel - 1);
      if (maxLevel < minLevel)
        return indexMap; 
    } 
  }
  
  static int[] reorderVisual(byte[] levels) {
    byte[] aMinLevel = new byte[1];
    byte[] aMaxLevel = new byte[1];
    int[] indexMap = prepareReorder(levels, aMinLevel, aMaxLevel);
    if (indexMap == null)
      return null; 
    byte minLevel = aMinLevel[0];
    byte maxLevel = aMaxLevel[0];
    if (minLevel == maxLevel && (minLevel & 0x1) == 0)
      return indexMap; 
    minLevel = (byte)(minLevel | 0x1);
    do {
      int start = 0;
      while (true) {
        if (start < levels.length && levels[start] < maxLevel) {
          start++;
          continue;
        } 
        if (start >= levels.length)
          break; 
        int limit;
        for (limit = start; ++limit < levels.length && levels[limit] >= maxLevel;);
        int end = limit - 1;
        while (start < end) {
          int temp = indexMap[start];
          indexMap[start] = indexMap[end];
          indexMap[end] = temp;
          start++;
          end--;
        } 
        if (limit == levels.length)
          break; 
        start = limit + 1;
      } 
      maxLevel = (byte)(maxLevel - 1);
    } while (maxLevel >= minLevel);
    return indexMap;
  }
  
  static int getVisualIndex(Bidi bidi, int logicalIndex) {
    BidiRun[] runs;
    int i, visualStart, visualIndex = -1;
    switch (bidi.direction) {
      case 0:
        visualIndex = logicalIndex;
        break;
      case 1:
        visualIndex = bidi.length - logicalIndex - 1;
        break;
      default:
        getRuns(bidi);
        runs = bidi.runs;
        visualStart = 0;
        for (i = 0; i < bidi.runCount; i++) {
          int length = (runs[i]).limit - visualStart;
          int offset = logicalIndex - (runs[i]).start;
          if (offset >= 0 && offset < length) {
            if (runs[i].isEvenRun()) {
              visualIndex = visualStart + offset;
              break;
            } 
            visualIndex = visualStart + length - offset - 1;
            break;
          } 
          visualStart += length;
        } 
        if (i >= bidi.runCount)
          return -1; 
        break;
    } 
    if (bidi.insertPoints.size > 0) {
      runs = bidi.runs;
      int j = 0, markFound = 0;
      for (i = 0;; i++, j += length) {
        int length = (runs[i]).limit - j;
        int insertRemove = (runs[i]).insertRemove;
        if ((insertRemove & 0x5) > 0)
          markFound++; 
        if (visualIndex < (runs[i]).limit)
          return visualIndex + markFound; 
        if ((insertRemove & 0xA) > 0)
          markFound++; 
      } 
    } 
    if (bidi.controlCount > 0) {
      runs = bidi.runs;
      int j = 0, controlFound = 0;
      char uchar = bidi.text[logicalIndex];
      if (Bidi.IsBidiControlChar(uchar))
        return -1; 
      for (i = 0;; i++, j += length) {
        int length = (runs[i]).limit - j;
        int insertRemove = (runs[i]).insertRemove;
        if (visualIndex >= (runs[i]).limit) {
          controlFound -= insertRemove;
        } else {
          int start;
          int limit;
          if (insertRemove == 0)
            return visualIndex - controlFound; 
          if (runs[i].isEvenRun()) {
            start = (runs[i]).start;
            limit = logicalIndex;
          } else {
            start = logicalIndex + 1;
            limit = (runs[i]).start + length;
          } 
          for (int k = start; k < limit; k++) {
            uchar = bidi.text[k];
            if (Bidi.IsBidiControlChar(uchar))
              controlFound++; 
          } 
          return visualIndex - controlFound;
        } 
      } 
    } 
    return visualIndex;
  }
  
  static int getLogicalIndex(Bidi bidi, int visualIndex) {
    int i;
    BidiRun[] runs = bidi.runs;
    int runCount = bidi.runCount;
    if (bidi.insertPoints.size > 0) {
      int markFound = 0;
      int visualStart = 0;
      for (i = 0;; i++, visualStart += length) {
        int length = (runs[i]).limit - visualStart;
        int insertRemove = (runs[i]).insertRemove;
        if ((insertRemove & 0x5) > 0) {
          if (visualIndex <= visualStart + markFound)
            return -1; 
          markFound++;
        } 
        if (visualIndex < (runs[i]).limit + markFound) {
          visualIndex -= markFound;
          break;
        } 
        if ((insertRemove & 0xA) > 0) {
          if (visualIndex == visualStart + length + markFound)
            return -1; 
          markFound++;
        } 
      } 
    } else if (bidi.controlCount > 0) {
      int controlFound = 0;
      int visualStart = 0;
      for (i = 0;; i++, visualStart += length) {
        int length = (runs[i]).limit - visualStart;
        int insertRemove = (runs[i]).insertRemove;
        if (visualIndex >= (runs[i]).limit - controlFound + insertRemove) {
          controlFound -= insertRemove;
        } else {
          if (insertRemove == 0) {
            visualIndex += controlFound;
            break;
          } 
          int logicalStart = (runs[i]).start;
          boolean evenRun = runs[i].isEvenRun();
          int logicalEnd = logicalStart + length - 1;
          for (int j = 0; j < length; j++) {
            int k = evenRun ? (logicalStart + j) : (logicalEnd - j);
            char uchar = bidi.text[k];
            if (Bidi.IsBidiControlChar(uchar))
              controlFound++; 
            if (visualIndex + controlFound == visualStart + j)
              break; 
          } 
          visualIndex += controlFound;
          break;
        } 
      } 
    } 
    if (runCount <= 10) {
      for (i = 0; visualIndex >= (runs[i]).limit; i++);
    } else {
      int begin = 0, limit = runCount;
      while (true) {
        i = begin + limit >>> 1;
        if (visualIndex >= (runs[i]).limit) {
          begin = i + 1;
          continue;
        } 
        if (i == 0 || visualIndex >= (runs[i - 1]).limit)
          break; 
        limit = i;
      } 
    } 
    int start = (runs[i]).start;
    if (runs[i].isEvenRun()) {
      if (i > 0)
        visualIndex -= (runs[i - 1]).limit; 
      return start + visualIndex;
    } 
    return start + (runs[i]).limit - visualIndex - 1;
  }
  
  static int[] getLogicalMap(Bidi bidi) {
    BidiRun[] runs = bidi.runs;
    int[] indexMap = new int[bidi.length];
    if (bidi.length > bidi.resultLength)
      Arrays.fill(indexMap, -1); 
    int visualStart = 0;
    for (int j = 0; j < bidi.runCount; j++) {
      int logicalStart = (runs[j]).start;
      int visualLimit = (runs[j]).limit;
      if (runs[j].isEvenRun()) {
        do {
          indexMap[logicalStart++] = visualStart++;
        } while (visualStart < visualLimit);
      } else {
        logicalStart += visualLimit - visualStart;
        do {
          indexMap[--logicalStart] = visualStart++;
        } while (visualStart < visualLimit);
      } 
    } 
    if (bidi.insertPoints.size > 0) {
      int markFound = 0, runCount = bidi.runCount;
      runs = bidi.runs;
      visualStart = 0;
      for (int i = 0; i < runCount; i++, visualStart += length) {
        int length = (runs[i]).limit - visualStart;
        int insertRemove = (runs[i]).insertRemove;
        if ((insertRemove & 0x5) > 0)
          markFound++; 
        if (markFound > 0) {
          int logicalStart = (runs[i]).start;
          int logicalLimit = logicalStart + length;
          for (int k = logicalStart; k < logicalLimit; k++)
            indexMap[k] = indexMap[k] + markFound; 
        } 
        if ((insertRemove & 0xA) > 0)
          markFound++; 
      } 
    } else if (bidi.controlCount > 0) {
      int controlFound = 0, runCount = bidi.runCount;
      runs = bidi.runs;
      visualStart = 0;
      for (int i = 0; i < runCount; i++, visualStart += length) {
        int length = (runs[i]).limit - visualStart;
        int insertRemove = (runs[i]).insertRemove;
        if (controlFound - insertRemove != 0) {
          int logicalStart = (runs[i]).start;
          boolean evenRun = runs[i].isEvenRun();
          int logicalLimit = logicalStart + length;
          if (insertRemove == 0) {
            for (int k = logicalStart; k < logicalLimit; k++)
              indexMap[k] = indexMap[k] - controlFound; 
          } else {
            for (int k = 0; k < length; k++) {
              int m = evenRun ? (logicalStart + k) : (logicalLimit - k - 1);
              char uchar = bidi.text[m];
              if (Bidi.IsBidiControlChar(uchar)) {
                controlFound++;
                indexMap[m] = -1;
              } else {
                indexMap[m] = indexMap[m] - controlFound;
              } 
            } 
          } 
        } 
      } 
    } 
    return indexMap;
  }
  
  static int[] getVisualMap(Bidi bidi) {
    BidiRun[] runs = bidi.runs;
    int allocLength = (bidi.length > bidi.resultLength) ? bidi.length : bidi.resultLength;
    int[] indexMap = new int[allocLength];
    int visualStart = 0;
    int idx = 0;
    for (int j = 0; j < bidi.runCount; j++) {
      int logicalStart = (runs[j]).start;
      int visualLimit = (runs[j]).limit;
      if (runs[j].isEvenRun()) {
        do {
          indexMap[idx++] = logicalStart++;
        } while (++visualStart < visualLimit);
      } else {
        logicalStart += visualLimit - visualStart;
        do {
          indexMap[idx++] = --logicalStart;
        } while (++visualStart < visualLimit);
      } 
    } 
    if (bidi.insertPoints.size > 0) {
      int markFound = 0, runCount = bidi.runCount;
      runs = bidi.runs;
      int i;
      for (i = 0; i < runCount; i++) {
        int insertRemove = (runs[i]).insertRemove;
        if ((insertRemove & 0x5) > 0)
          markFound++; 
        if ((insertRemove & 0xA) > 0)
          markFound++; 
      } 
      int k = bidi.resultLength;
      for (i = runCount - 1; i >= 0 && markFound > 0; i--) {
        int insertRemove = (runs[i]).insertRemove;
        if ((insertRemove & 0xA) > 0) {
          indexMap[--k] = -1;
          markFound--;
        } 
        visualStart = (i > 0) ? (runs[i - 1]).limit : 0;
        for (int m = (runs[i]).limit - 1; m >= visualStart && markFound > 0; m--)
          indexMap[--k] = indexMap[m]; 
        if ((insertRemove & 0x5) > 0) {
          indexMap[--k] = -1;
          markFound--;
        } 
      } 
    } else if (bidi.controlCount > 0) {
      int runCount = bidi.runCount;
      runs = bidi.runs;
      visualStart = 0;
      int k = 0;
      for (int i = 0; i < runCount; i++, visualStart += length) {
        int length = (runs[i]).limit - visualStart;
        int insertRemove = (runs[i]).insertRemove;
        if (insertRemove == 0 && k == visualStart) {
          k += length;
        } else if (insertRemove == 0) {
          int visualLimit = (runs[i]).limit;
          for (int m = visualStart; m < visualLimit; m++)
            indexMap[k++] = indexMap[m]; 
        } else {
          int logicalStart = (runs[i]).start;
          boolean evenRun = runs[i].isEvenRun();
          int logicalEnd = logicalStart + length - 1;
          for (int m = 0; m < length; m++) {
            int n = evenRun ? (logicalStart + m) : (logicalEnd - m);
            char uchar = bidi.text[n];
            if (!Bidi.IsBidiControlChar(uchar))
              indexMap[k++] = n; 
          } 
        } 
      } 
    } 
    if (allocLength == bidi.resultLength)
      return indexMap; 
    int[] newMap = new int[bidi.resultLength];
    System.arraycopy(indexMap, 0, newMap, 0, bidi.resultLength);
    return newMap;
  }
  
  static int[] invertMap(int[] srcMap) {
    int srcLength = srcMap.length;
    int destLength = -1, count = 0;
    int i;
    for (i = 0; i < srcLength; i++) {
      int srcEntry = srcMap[i];
      if (srcEntry > destLength)
        destLength = srcEntry; 
      if (srcEntry >= 0)
        count++; 
    } 
    destLength++;
    int[] destMap = new int[destLength];
    if (count < destLength)
      Arrays.fill(destMap, -1); 
    for (i = 0; i < srcLength; i++) {
      int srcEntry = srcMap[i];
      if (srcEntry >= 0)
        destMap[srcEntry] = i; 
    } 
    return destMap;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\ibm\icu\text\BidiLine.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */