// 
// Decompiled by Procyon v0.5.36
// 

package javazoom.jl.converter;

import java.io.IOException;
import java.io.RandomAccessFile;

public class RiffFile
{
    public static final int DDC_SUCCESS = 0;
    public static final int DDC_FAILURE = 1;
    public static final int DDC_OUT_OF_MEMORY = 2;
    public static final int DDC_FILE_ERROR = 3;
    public static final int DDC_INVALID_CALL = 4;
    public static final int DDC_USER_ABORT = 5;
    public static final int DDC_INVALID_FILE = 6;
    public static final int RFM_UNKNOWN = 0;
    public static final int RFM_WRITE = 1;
    public static final int RFM_READ = 2;
    private RiffChunkHeader riff_header;
    protected int fmode;
    protected RandomAccessFile file;
    
    public RiffFile() {
        this.file = null;
        this.fmode = 0;
        this.riff_header = new RiffChunkHeader();
        this.riff_header.ckID = FourCC("RIFF");
        this.riff_header.ckSize = 0;
    }
    
    public int CurrentFileMode() {
        return this.fmode;
    }
    
    public int Open(final String Filename, final int NewMode) {
        int retcode = 0;
        if (this.fmode != 0) {
            retcode = this.Close();
        }
        if (retcode == 0) {
            switch (NewMode) {
                case 1: {
                    try {
                        this.file = new RandomAccessFile(Filename, "rw");
                        try {
                            final byte[] br = { (byte)(this.riff_header.ckID >>> 24 & 0xFF), (byte)(this.riff_header.ckID >>> 16 & 0xFF), (byte)(this.riff_header.ckID >>> 8 & 0xFF), (byte)(this.riff_header.ckID & 0xFF), 0, 0, 0, 0 };
                            final byte br2 = (byte)(this.riff_header.ckSize >>> 24 & 0xFF);
                            final byte br3 = (byte)(this.riff_header.ckSize >>> 16 & 0xFF);
                            final byte br4 = (byte)(this.riff_header.ckSize >>> 8 & 0xFF);
                            final byte br5 = (byte)(this.riff_header.ckSize & 0xFF);
                            br[4] = br5;
                            br[5] = br4;
                            br[6] = br3;
                            br[7] = br2;
                            this.file.write(br, 0, 8);
                            this.fmode = 1;
                        }
                        catch (IOException ioe) {
                            this.file.close();
                            this.fmode = 0;
                        }
                    }
                    catch (IOException ioe) {
                        this.fmode = 0;
                        retcode = 3;
                    }
                    break;
                }
                case 2: {
                    try {
                        this.file = new RandomAccessFile(Filename, "r");
                        try {
                            final byte[] br = new byte[8];
                            this.file.read(br, 0, 8);
                            this.fmode = 2;
                            this.riff_header.ckID = ((br[0] << 24 & 0xFF000000) | (br[1] << 16 & 0xFF0000) | (br[2] << 8 & 0xFF00) | (br[3] & 0xFF));
                            this.riff_header.ckSize = ((br[4] << 24 & 0xFF000000) | (br[5] << 16 & 0xFF0000) | (br[6] << 8 & 0xFF00) | (br[7] & 0xFF));
                        }
                        catch (IOException ioe) {
                            this.file.close();
                            this.fmode = 0;
                        }
                    }
                    catch (IOException ioe) {
                        this.fmode = 0;
                        retcode = 3;
                    }
                    break;
                }
                default: {
                    retcode = 4;
                    break;
                }
            }
        }
        return retcode;
    }
    
    public int Write(final byte[] Data, final int NumBytes) {
        if (this.fmode != 1) {
            return 4;
        }
        try {
            this.file.write(Data, 0, NumBytes);
            this.fmode = 1;
        }
        catch (IOException ioe) {
            return 3;
        }
        final RiffChunkHeader riff_header = this.riff_header;
        riff_header.ckSize += NumBytes;
        return 0;
    }
    
    public int Write(final short[] Data, final int NumBytes) {
        final byte[] theData = new byte[NumBytes];
        int yc = 0;
        for (int y = 0; y < NumBytes; y += 2) {
            theData[y] = (byte)(Data[yc] & 0xFF);
            theData[y + 1] = (byte)(Data[yc++] >>> 8 & 0xFF);
        }
        if (this.fmode != 1) {
            return 4;
        }
        try {
            this.file.write(theData, 0, NumBytes);
            this.fmode = 1;
        }
        catch (IOException ioe) {
            return 3;
        }
        final RiffChunkHeader riff_header = this.riff_header;
        riff_header.ckSize += NumBytes;
        return 0;
    }
    
    public int Write(final RiffChunkHeader Triff_header, final int NumBytes) {
        final byte[] br = { (byte)(Triff_header.ckID >>> 24 & 0xFF), (byte)(Triff_header.ckID >>> 16 & 0xFF), (byte)(Triff_header.ckID >>> 8 & 0xFF), (byte)(Triff_header.ckID & 0xFF), 0, 0, 0, 0 };
        final byte br2 = (byte)(Triff_header.ckSize >>> 24 & 0xFF);
        final byte br3 = (byte)(Triff_header.ckSize >>> 16 & 0xFF);
        final byte br4 = (byte)(Triff_header.ckSize >>> 8 & 0xFF);
        final byte br5 = (byte)(Triff_header.ckSize & 0xFF);
        br[4] = br5;
        br[5] = br4;
        br[6] = br3;
        br[7] = br2;
        if (this.fmode != 1) {
            return 4;
        }
        try {
            this.file.write(br, 0, NumBytes);
            this.fmode = 1;
        }
        catch (IOException ioe) {
            return 3;
        }
        final RiffChunkHeader riff_header = this.riff_header;
        riff_header.ckSize += NumBytes;
        return 0;
    }
    
    public int Write(final short Data, final int NumBytes) {
        final short theData = (short)((Data >>> 8 & 0xFF) | (Data << 8 & 0xFF00));
        if (this.fmode != 1) {
            return 4;
        }
        try {
            this.file.writeShort(theData);
            this.fmode = 1;
        }
        catch (IOException ioe) {
            return 3;
        }
        final RiffChunkHeader riff_header = this.riff_header;
        riff_header.ckSize += NumBytes;
        return 0;
    }
    
    public int Write(final int Data, final int NumBytes) {
        final short theDataL = (short)(Data >>> 16 & 0xFFFF);
        final short theDataR = (short)(Data & 0xFFFF);
        final short theDataLI = (short)((theDataL >>> 8 & 0xFF) | (theDataL << 8 & 0xFF00));
        final short theDataRI = (short)((theDataR >>> 8 & 0xFF) | (theDataR << 8 & 0xFF00));
        final int theData = (theDataRI << 16 & 0xFFFF0000) | (theDataLI & 0xFFFF);
        if (this.fmode != 1) {
            return 4;
        }
        try {
            this.file.writeInt(theData);
            this.fmode = 1;
        }
        catch (IOException ioe) {
            return 3;
        }
        final RiffChunkHeader riff_header = this.riff_header;
        riff_header.ckSize += NumBytes;
        return 0;
    }
    
    public int Read(final byte[] Data, final int NumBytes) {
        int retcode = 0;
        try {
            this.file.read(Data, 0, NumBytes);
        }
        catch (IOException ioe) {
            retcode = 3;
        }
        return retcode;
    }
    
    public int Expect(final String Data, int NumBytes) {
        byte target = 0;
        int cnt = 0;
        try {
            while (NumBytes-- != 0) {
                target = this.file.readByte();
                if (target != Data.charAt(cnt++)) {
                    return 3;
                }
            }
        }
        catch (IOException ioe) {
            return 3;
        }
        return 0;
    }
    
    public int Close() {
        int retcode = 0;
        switch (this.fmode) {
            case 1: {
                try {
                    this.file.seek(0L);
                    try {
                        final byte[] br = { (byte)(this.riff_header.ckID >>> 24 & 0xFF), (byte)(this.riff_header.ckID >>> 16 & 0xFF), (byte)(this.riff_header.ckID >>> 8 & 0xFF), (byte)(this.riff_header.ckID & 0xFF), (byte)(this.riff_header.ckSize & 0xFF), (byte)(this.riff_header.ckSize >>> 8 & 0xFF), (byte)(this.riff_header.ckSize >>> 16 & 0xFF), (byte)(this.riff_header.ckSize >>> 24 & 0xFF) };
                        this.file.write(br, 0, 8);
                        this.file.close();
                    }
                    catch (IOException ioe) {
                        retcode = 3;
                    }
                }
                catch (IOException ioe) {
                    retcode = 3;
                }
                break;
            }
            case 2: {
                try {
                    this.file.close();
                }
                catch (IOException ioe) {
                    retcode = 3;
                }
                break;
            }
        }
        this.file = null;
        this.fmode = 0;
        return retcode;
    }
    
    public long CurrentFilePosition() {
        long position;
        try {
            position = this.file.getFilePointer();
        }
        catch (IOException ioe) {
            position = -1L;
        }
        return position;
    }
    
    public int Backpatch(final long FileOffset, final RiffChunkHeader Data, final int NumBytes) {
        if (this.file == null) {
            return 4;
        }
        try {
            this.file.seek(FileOffset);
        }
        catch (IOException ioe) {
            return 3;
        }
        return this.Write(Data, NumBytes);
    }
    
    public int Backpatch(final long FileOffset, final byte[] Data, final int NumBytes) {
        if (this.file == null) {
            return 4;
        }
        try {
            this.file.seek(FileOffset);
        }
        catch (IOException ioe) {
            return 3;
        }
        return this.Write(Data, NumBytes);
    }
    
    protected int Seek(final long offset) {
        int rc;
        try {
            this.file.seek(offset);
            rc = 0;
        }
        catch (IOException ioe) {
            rc = 3;
        }
        return rc;
    }
    
    private String DDCRET_String(final int retcode) {
        switch (retcode) {
            case 0: {
                return "DDC_SUCCESS";
            }
            case 1: {
                return "DDC_FAILURE";
            }
            case 2: {
                return "DDC_OUT_OF_MEMORY";
            }
            case 3: {
                return "DDC_FILE_ERROR";
            }
            case 4: {
                return "DDC_INVALID_CALL";
            }
            case 5: {
                return "DDC_USER_ABORT";
            }
            case 6: {
                return "DDC_INVALID_FILE";
            }
            default: {
                return "Unknown Error";
            }
        }
    }
    
    public static int FourCC(final String ChunkName) {
        final byte[] p = { 32, 32, 32, 32 };
        ChunkName.getBytes(0, 4, p, 0);
        final int ret = (p[0] << 24 & 0xFF000000) | (p[1] << 16 & 0xFF0000) | (p[2] << 8 & 0xFF00) | (p[3] & 0xFF);
        return ret;
    }
    
    class RiffChunkHeader
    {
        public int ckID;
        public int ckSize;
        
        public RiffChunkHeader() {
            this.ckID = 0;
            this.ckSize = 0;
        }
    }
}
