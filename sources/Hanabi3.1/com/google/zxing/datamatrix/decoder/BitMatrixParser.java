package com.google.zxing.datamatrix.decoder;

import com.google.zxing.FormatException;
import com.google.zxing.common.BitMatrix;

final class BitMatrixParser {
   private final BitMatrix mappingBitMatrix;
   private final BitMatrix readMappingMatrix;
   private final Version version;

   BitMatrixParser(BitMatrix bitMatrix) throws FormatException {
      int dimension = bitMatrix.getHeight();
      if (dimension >= 8 && dimension <= 144 && (dimension & 1) == 0) {
         this.version = readVersion(bitMatrix);
         this.mappingBitMatrix = this.extractDataRegion(bitMatrix);
         this.readMappingMatrix = new BitMatrix(this.mappingBitMatrix.getWidth(), this.mappingBitMatrix.getHeight());
      } else {
         throw FormatException.getFormatInstance();
      }
   }

   Version getVersion() {
      return this.version;
   }

   private static Version readVersion(BitMatrix bitMatrix) throws FormatException {
      int numRows = bitMatrix.getHeight();
      int numColumns = bitMatrix.getWidth();
      return Version.getVersionForDimensions(numRows, numColumns);
   }

   byte[] readCodewords() throws FormatException {
      byte[] result = new byte[this.version.getTotalCodewords()];
      int resultOffset = 0;
      int row = 4;
      int column = 0;
      int numRows = this.mappingBitMatrix.getHeight();
      int numColumns = this.mappingBitMatrix.getWidth();
      boolean corner1Read = false;
      boolean corner2Read = false;
      boolean corner3Read = false;
      boolean corner4Read = false;

      do {
         if (row == numRows && column == 0 && !corner1Read) {
            result[resultOffset++] = (byte)this.readCorner1(numRows, numColumns);
            row -= 2;
            column += 2;
            corner1Read = true;
         } else if (row == numRows - 2 && column == 0 && (numColumns & 3) != 0 && !corner2Read) {
            result[resultOffset++] = (byte)this.readCorner2(numRows, numColumns);
            row -= 2;
            column += 2;
            corner2Read = true;
         } else if (row == numRows + 4 && column == 2 && (numColumns & 7) == 0 && !corner3Read) {
            result[resultOffset++] = (byte)this.readCorner3(numRows, numColumns);
            row -= 2;
            column += 2;
            corner3Read = true;
         } else if (row == numRows - 2 && column == 0 && (numColumns & 7) == 4 && !corner4Read) {
            result[resultOffset++] = (byte)this.readCorner4(numRows, numColumns);
            row -= 2;
            column += 2;
            corner4Read = true;
         } else {
            do {
               if (row < numRows && column >= 0 && !this.readMappingMatrix.get(column, row)) {
                  result[resultOffset++] = (byte)this.readUtah(row, column, numRows, numColumns);
               }

               row -= 2;
               column += 2;
            } while(row >= 0 && column < numColumns);

            ++row;
            column += 3;

            do {
               if (row >= 0 && column < numColumns && !this.readMappingMatrix.get(column, row)) {
                  result[resultOffset++] = (byte)this.readUtah(row, column, numRows, numColumns);
               }

               row += 2;
               column -= 2;
            } while(row < numRows && column >= 0);

            row += 3;
            ++column;
         }
      } while(row < numRows || column < numColumns);

      if (resultOffset != this.version.getTotalCodewords()) {
         throw FormatException.getFormatInstance();
      } else {
         return result;
      }
   }

   boolean readModule(int row, int column, int numRows, int numColumns) {
      if (row < 0) {
         row += numRows;
         column += 4 - (numRows + 4 & 7);
      }

      if (column < 0) {
         column += numColumns;
         row += 4 - (numColumns + 4 & 7);
      }

      this.readMappingMatrix.set(column, row);
      return this.mappingBitMatrix.get(column, row);
   }

   int readUtah(int row, int column, int numRows, int numColumns) {
      int currentByte = 0;
      if (this.readModule(row - 2, column - 2, numRows, numColumns)) {
         currentByte |= 1;
      }

      currentByte <<= 1;
      if (this.readModule(row - 2, column - 1, numRows, numColumns)) {
         currentByte |= 1;
      }

      currentByte <<= 1;
      if (this.readModule(row - 1, column - 2, numRows, numColumns)) {
         currentByte |= 1;
      }

      currentByte <<= 1;
      if (this.readModule(row - 1, column - 1, numRows, numColumns)) {
         currentByte |= 1;
      }

      currentByte <<= 1;
      if (this.readModule(row - 1, column, numRows, numColumns)) {
         currentByte |= 1;
      }

      currentByte <<= 1;
      if (this.readModule(row, column - 2, numRows, numColumns)) {
         currentByte |= 1;
      }

      currentByte <<= 1;
      if (this.readModule(row, column - 1, numRows, numColumns)) {
         currentByte |= 1;
      }

      currentByte <<= 1;
      if (this.readModule(row, column, numRows, numColumns)) {
         currentByte |= 1;
      }

      return currentByte;
   }

   int readCorner1(int numRows, int numColumns) {
      int currentByte = 0;
      if (this.readModule(numRows - 1, 0, numRows, numColumns)) {
         currentByte |= 1;
      }

      currentByte <<= 1;
      if (this.readModule(numRows - 1, 1, numRows, numColumns)) {
         currentByte |= 1;
      }

      currentByte <<= 1;
      if (this.readModule(numRows - 1, 2, numRows, numColumns)) {
         currentByte |= 1;
      }

      currentByte <<= 1;
      if (this.readModule(0, numColumns - 2, numRows, numColumns)) {
         currentByte |= 1;
      }

      currentByte <<= 1;
      if (this.readModule(0, numColumns - 1, numRows, numColumns)) {
         currentByte |= 1;
      }

      currentByte <<= 1;
      if (this.readModule(1, numColumns - 1, numRows, numColumns)) {
         currentByte |= 1;
      }

      currentByte <<= 1;
      if (this.readModule(2, numColumns - 1, numRows, numColumns)) {
         currentByte |= 1;
      }

      currentByte <<= 1;
      if (this.readModule(3, numColumns - 1, numRows, numColumns)) {
         currentByte |= 1;
      }

      return currentByte;
   }

   int readCorner2(int numRows, int numColumns) {
      int currentByte = 0;
      if (this.readModule(numRows - 3, 0, numRows, numColumns)) {
         currentByte |= 1;
      }

      currentByte <<= 1;
      if (this.readModule(numRows - 2, 0, numRows, numColumns)) {
         currentByte |= 1;
      }

      currentByte <<= 1;
      if (this.readModule(numRows - 1, 0, numRows, numColumns)) {
         currentByte |= 1;
      }

      currentByte <<= 1;
      if (this.readModule(0, numColumns - 4, numRows, numColumns)) {
         currentByte |= 1;
      }

      currentByte <<= 1;
      if (this.readModule(0, numColumns - 3, numRows, numColumns)) {
         currentByte |= 1;
      }

      currentByte <<= 1;
      if (this.readModule(0, numColumns - 2, numRows, numColumns)) {
         currentByte |= 1;
      }

      currentByte <<= 1;
      if (this.readModule(0, numColumns - 1, numRows, numColumns)) {
         currentByte |= 1;
      }

      currentByte <<= 1;
      if (this.readModule(1, numColumns - 1, numRows, numColumns)) {
         currentByte |= 1;
      }

      return currentByte;
   }

   int readCorner3(int numRows, int numColumns) {
      int currentByte = 0;
      if (this.readModule(numRows - 1, 0, numRows, numColumns)) {
         currentByte |= 1;
      }

      currentByte <<= 1;
      if (this.readModule(numRows - 1, numColumns - 1, numRows, numColumns)) {
         currentByte |= 1;
      }

      currentByte <<= 1;
      if (this.readModule(0, numColumns - 3, numRows, numColumns)) {
         currentByte |= 1;
      }

      currentByte <<= 1;
      if (this.readModule(0, numColumns - 2, numRows, numColumns)) {
         currentByte |= 1;
      }

      currentByte <<= 1;
      if (this.readModule(0, numColumns - 1, numRows, numColumns)) {
         currentByte |= 1;
      }

      currentByte <<= 1;
      if (this.readModule(1, numColumns - 3, numRows, numColumns)) {
         currentByte |= 1;
      }

      currentByte <<= 1;
      if (this.readModule(1, numColumns - 2, numRows, numColumns)) {
         currentByte |= 1;
      }

      currentByte <<= 1;
      if (this.readModule(1, numColumns - 1, numRows, numColumns)) {
         currentByte |= 1;
      }

      return currentByte;
   }

   int readCorner4(int numRows, int numColumns) {
      int currentByte = 0;
      if (this.readModule(numRows - 3, 0, numRows, numColumns)) {
         currentByte |= 1;
      }

      currentByte <<= 1;
      if (this.readModule(numRows - 2, 0, numRows, numColumns)) {
         currentByte |= 1;
      }

      currentByte <<= 1;
      if (this.readModule(numRows - 1, 0, numRows, numColumns)) {
         currentByte |= 1;
      }

      currentByte <<= 1;
      if (this.readModule(0, numColumns - 2, numRows, numColumns)) {
         currentByte |= 1;
      }

      currentByte <<= 1;
      if (this.readModule(0, numColumns - 1, numRows, numColumns)) {
         currentByte |= 1;
      }

      currentByte <<= 1;
      if (this.readModule(1, numColumns - 1, numRows, numColumns)) {
         currentByte |= 1;
      }

      currentByte <<= 1;
      if (this.readModule(2, numColumns - 1, numRows, numColumns)) {
         currentByte |= 1;
      }

      currentByte <<= 1;
      if (this.readModule(3, numColumns - 1, numRows, numColumns)) {
         currentByte |= 1;
      }

      return currentByte;
   }

   BitMatrix extractDataRegion(BitMatrix bitMatrix) {
      int symbolSizeRows = this.version.getSymbolSizeRows();
      int symbolSizeColumns = this.version.getSymbolSizeColumns();
      if (bitMatrix.getHeight() != symbolSizeRows) {
         throw new IllegalArgumentException("Dimension of bitMarix must match the version size");
      } else {
         int dataRegionSizeRows = this.version.getDataRegionSizeRows();
         int dataRegionSizeColumns = this.version.getDataRegionSizeColumns();
         int numDataRegionsRow = symbolSizeRows / dataRegionSizeRows;
         int numDataRegionsColumn = symbolSizeColumns / dataRegionSizeColumns;
         int sizeDataRegionRow = numDataRegionsRow * dataRegionSizeRows;
         int sizeDataRegionColumn = numDataRegionsColumn * dataRegionSizeColumns;
         BitMatrix bitMatrixWithoutAlignment = new BitMatrix(sizeDataRegionColumn, sizeDataRegionRow);

         for(int dataRegionRow = 0; dataRegionRow < numDataRegionsRow; ++dataRegionRow) {
            int dataRegionRowOffset = dataRegionRow * dataRegionSizeRows;

            for(int dataRegionColumn = 0; dataRegionColumn < numDataRegionsColumn; ++dataRegionColumn) {
               int dataRegionColumnOffset = dataRegionColumn * dataRegionSizeColumns;

               for(int i = 0; i < dataRegionSizeRows; ++i) {
                  int readRowOffset = dataRegionRow * (dataRegionSizeRows + 2) + 1 + i;
                  int writeRowOffset = dataRegionRowOffset + i;

                  for(int j = 0; j < dataRegionSizeColumns; ++j) {
                     int readColumnOffset = dataRegionColumn * (dataRegionSizeColumns + 2) + 1 + j;
                     if (bitMatrix.get(readColumnOffset, readRowOffset)) {
                        int writeColumnOffset = dataRegionColumnOffset + j;
                        bitMatrixWithoutAlignment.set(writeColumnOffset, writeRowOffset);
                     }
                  }
               }
            }
         }

         return bitMatrixWithoutAlignment;
      }
   }
}
