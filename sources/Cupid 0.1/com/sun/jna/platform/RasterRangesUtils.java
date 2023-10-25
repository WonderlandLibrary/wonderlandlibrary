package com.sun.jna.platform;

import java.awt.Rectangle;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.DataBufferInt;
import java.awt.image.MultiPixelPackedSampleModel;
import java.awt.image.Raster;
import java.awt.image.SampleModel;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

public class RasterRangesUtils {
  private static final int[] subColMasks = new int[] { 128, 64, 32, 16, 8, 4, 2, 1 };
  
  private static final Comparator<Object> COMPARATOR = new Comparator() {
      public int compare(Object o1, Object o2) {
        return ((Rectangle)o1).x - ((Rectangle)o2).x;
      }
    };
  
  public static interface RangesOutput {
    boolean outputRange(int param1Int1, int param1Int2, int param1Int3, int param1Int4);
  }
  
  public static boolean outputOccupiedRanges(Raster raster, RangesOutput out) {
    Rectangle bounds = raster.getBounds();
    SampleModel sampleModel = raster.getSampleModel();
    boolean hasAlpha = (sampleModel.getNumBands() == 4);
    if (raster.getParent() == null && bounds.x == 0 && bounds.y == 0) {
      DataBuffer data = raster.getDataBuffer();
      if (data.getNumBanks() == 1)
        if (sampleModel instanceof MultiPixelPackedSampleModel) {
          MultiPixelPackedSampleModel packedSampleModel = (MultiPixelPackedSampleModel)sampleModel;
          if (packedSampleModel.getPixelBitStride() == 1)
            return outputOccupiedRangesOfBinaryPixels(((DataBufferByte)data).getData(), bounds.width, bounds.height, out); 
        } else if (sampleModel instanceof java.awt.image.SinglePixelPackedSampleModel && 
          sampleModel.getDataType() == 3) {
          return outputOccupiedRanges(((DataBufferInt)data).getData(), bounds.width, bounds.height, hasAlpha ? -16777216 : 16777215, out);
        }  
    } 
    int[] pixels = raster.getPixels(0, 0, bounds.width, bounds.height, (int[])null);
    return outputOccupiedRanges(pixels, bounds.width, bounds.height, hasAlpha ? -16777216 : 16777215, out);
  }
  
  public static boolean outputOccupiedRangesOfBinaryPixels(byte[] binaryBits, int w, int h, RangesOutput out) {
    Set<Rectangle> rects = new HashSet<Rectangle>();
    Set<Rectangle> prevLine = Collections.EMPTY_SET;
    int scanlineBytes = binaryBits.length / h;
    for (int row = 0; row < h; row++) {
      Set<Rectangle> curLine = new TreeSet(COMPARATOR);
      int rowOffsetBytes = row * scanlineBytes;
      int startCol = -1;
      for (int byteCol = 0; byteCol < scanlineBytes; byteCol++) {
        int firstByteCol = byteCol << 3;
        byte byteColBits = binaryBits[rowOffsetBytes + byteCol];
        if (byteColBits == 0) {
          if (startCol >= 0) {
            curLine.add(new Rectangle(startCol, row, firstByteCol - startCol, 1));
            startCol = -1;
          } 
        } else if (byteColBits == 255) {
          if (startCol < 0)
            startCol = firstByteCol; 
        } else {
          for (int subCol = 0; subCol < 8; subCol++) {
            int col = firstByteCol | subCol;
            if ((byteColBits & subColMasks[subCol]) != 0) {
              if (startCol < 0)
                startCol = col; 
            } else if (startCol >= 0) {
              curLine.add(new Rectangle(startCol, row, col - startCol, 1));
              startCol = -1;
            } 
          } 
        } 
      } 
      if (startCol >= 0)
        curLine.add(new Rectangle(startCol, row, w - startCol, 1)); 
      Set<Rectangle> unmerged = mergeRects(prevLine, curLine);
      rects.addAll(unmerged);
      prevLine = curLine;
    } 
    rects.addAll(prevLine);
    for (Iterator<Rectangle> i = rects.iterator(); i.hasNext(); ) {
      Rectangle r = i.next();
      if (!out.outputRange(r.x, r.y, r.width, r.height))
        return false; 
    } 
    return true;
  }
  
  public static boolean outputOccupiedRanges(int[] pixels, int w, int h, int occupationMask, RangesOutput out) {
    Set<Rectangle> rects = new HashSet<Rectangle>();
    Set<Rectangle> prevLine = Collections.EMPTY_SET;
    for (int row = 0; row < h; row++) {
      Set<Rectangle> curLine = new TreeSet(COMPARATOR);
      int idxOffset = row * w;
      int startCol = -1;
      for (int col = 0; col < w; col++) {
        if ((pixels[idxOffset + col] & occupationMask) != 0) {
          if (startCol < 0)
            startCol = col; 
        } else if (startCol >= 0) {
          curLine.add(new Rectangle(startCol, row, col - startCol, 1));
          startCol = -1;
        } 
      } 
      if (startCol >= 0)
        curLine.add(new Rectangle(startCol, row, w - startCol, 1)); 
      Set<Rectangle> unmerged = mergeRects(prevLine, curLine);
      rects.addAll(unmerged);
      prevLine = curLine;
    } 
    rects.addAll(prevLine);
    for (Iterator<Rectangle> i = rects.iterator(); i.hasNext(); ) {
      Rectangle r = i.next();
      if (!out.outputRange(r.x, r.y, r.width, r.height))
        return false; 
    } 
    return true;
  }
  
  private static Set<Rectangle> mergeRects(Set<Rectangle> prev, Set<Rectangle> current) {
    Set<Rectangle> unmerged = new HashSet<Rectangle>(prev);
    if (!prev.isEmpty() && !current.isEmpty()) {
      Rectangle[] pr = prev.<Rectangle>toArray(new Rectangle[prev.size()]);
      Rectangle[] cr = current.<Rectangle>toArray(new Rectangle[current.size()]);
      int ipr = 0;
      int icr = 0;
      while (ipr < pr.length && icr < cr.length) {
        while ((cr[icr]).x < (pr[ipr]).x) {
          if (++icr == cr.length)
            return unmerged; 
        } 
        if ((cr[icr]).x == (pr[ipr]).x && (cr[icr]).width == (pr[ipr]).width) {
          unmerged.remove(pr[ipr]);
          (cr[icr]).y = (pr[ipr]).y;
          (pr[ipr]).height++;
          icr++;
          continue;
        } 
        ipr++;
      } 
    } 
    return unmerged;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\sun\jna\platform\RasterRangesUtils.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */