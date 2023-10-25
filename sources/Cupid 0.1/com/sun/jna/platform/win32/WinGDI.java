package com.sun.jna.platform.win32;

import com.sun.jna.Structure;
import com.sun.jna.win32.StdCallLibrary;

public interface WinGDI extends StdCallLibrary {
  public static final int RDH_RECTANGLES = 1;
  
  public static final int RGN_AND = 1;
  
  public static final int RGN_OR = 2;
  
  public static final int RGN_XOR = 3;
  
  public static final int RGN_DIFF = 4;
  
  public static final int RGN_COPY = 5;
  
  public static final int ERROR = 0;
  
  public static final int NULLREGION = 1;
  
  public static final int SIMPLEREGION = 2;
  
  public static final int COMPLEXREGION = 3;
  
  public static final int ALTERNATE = 1;
  
  public static final int WINDING = 2;
  
  public static final int BI_RGB = 0;
  
  public static final int BI_RLE8 = 1;
  
  public static final int BI_RLE4 = 2;
  
  public static final int BI_BITFIELDS = 3;
  
  public static final int BI_JPEG = 4;
  
  public static final int BI_PNG = 5;
  
  public static final int DIB_RGB_COLORS = 0;
  
  public static final int DIB_PAL_COLORS = 1;
  
  public static class RGNDATAHEADER extends Structure {
    public int dwSize = size();
    
    public int iType = 1;
    
    public int nCount;
    
    public int nRgnSize;
    
    public WinDef.RECT rcBound;
  }
  
  public static class RGNDATA extends Structure {
    public WinGDI.RGNDATAHEADER rdh;
    
    public byte[] Buffer;
    
    public RGNDATA(int bufferSize) {
      this.Buffer = new byte[bufferSize];
      allocateMemory();
    }
  }
  
  public static class BITMAPINFOHEADER extends Structure {
    public int biSize = size();
    
    public int biWidth;
    
    public int biHeight;
    
    public short biPlanes;
    
    public short biBitCount;
    
    public int biCompression;
    
    public int biSizeImage;
    
    public int biXPelsPerMeter;
    
    public int biYPelsPerMeter;
    
    public int biClrUsed;
    
    public int biClrImportant;
  }
  
  public static class RGBQUAD extends Structure {
    public byte rgbBlue;
    
    public byte rgbGreen;
    
    public byte rgbRed;
    
    public byte rgbReserved = 0;
  }
  
  public static class BITMAPINFO extends Structure {
    public WinGDI.BITMAPINFOHEADER bmiHeader = new WinGDI.BITMAPINFOHEADER();
    
    public WinGDI.RGBQUAD[] bmiColors = new WinGDI.RGBQUAD[1];
    
    public BITMAPINFO() {
      this(1);
    }
    
    public BITMAPINFO(int size) {
      this.bmiColors = new WinGDI.RGBQUAD[size];
    }
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\sun\jna\platform\win32\WinGDI.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */