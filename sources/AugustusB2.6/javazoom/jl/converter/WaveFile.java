// 
// Decompiled by Procyon v0.5.36
// 

package javazoom.jl.converter;

public class WaveFile extends RiffFile
{
    public static final int MAX_WAVE_CHANNELS = 2;
    private WaveFormat_Chunk wave_format;
    private RiffChunkHeader pcm_data;
    private long pcm_data_offset;
    private int num_samples;
    
    public WaveFile() {
        this.pcm_data_offset = 0L;
        this.num_samples = 0;
        this.pcm_data = new RiffChunkHeader();
        this.wave_format = new WaveFormat_Chunk();
        this.pcm_data.ckID = RiffFile.FourCC("data");
        this.pcm_data.ckSize = 0;
        this.num_samples = 0;
    }
    
    public int OpenForWrite(final String Filename, final int SamplingRate, final short BitsPerSample, final short NumChannels) {
        if (Filename == null || (BitsPerSample != 8 && BitsPerSample != 16) || NumChannels < 1 || NumChannels > 2) {
            return 4;
        }
        this.wave_format.data.Config(SamplingRate, BitsPerSample, NumChannels);
        int retcode = this.Open(Filename, 1);
        if (retcode == 0) {
            final byte[] theWave = { 87, 65, 86, 69 };
            retcode = this.Write(theWave, 4);
            if (retcode == 0) {
                retcode = this.Write(this.wave_format.header, 8);
                retcode = this.Write(this.wave_format.data.wFormatTag, 2);
                retcode = this.Write(this.wave_format.data.nChannels, 2);
                retcode = this.Write(this.wave_format.data.nSamplesPerSec, 4);
                retcode = this.Write(this.wave_format.data.nAvgBytesPerSec, 4);
                retcode = this.Write(this.wave_format.data.nBlockAlign, 2);
                retcode = this.Write(this.wave_format.data.nBitsPerSample, 2);
                if (retcode == 0) {
                    this.pcm_data_offset = this.CurrentFilePosition();
                    retcode = this.Write(this.pcm_data, 8);
                }
            }
        }
        return retcode;
    }
    
    public int WriteData(final short[] data, final int numData) {
        final int extraBytes = numData * 2;
        final RiffChunkHeader pcm_data = this.pcm_data;
        pcm_data.ckSize += extraBytes;
        return super.Write(data, extraBytes);
    }
    
    @Override
    public int Close() {
        int rc = 0;
        if (this.fmode == 1) {
            rc = this.Backpatch(this.pcm_data_offset, this.pcm_data, 8);
        }
        if (rc == 0) {
            rc = super.Close();
        }
        return rc;
    }
    
    public int SamplingRate() {
        return this.wave_format.data.nSamplesPerSec;
    }
    
    public short BitsPerSample() {
        return this.wave_format.data.nBitsPerSample;
    }
    
    public short NumChannels() {
        return this.wave_format.data.nChannels;
    }
    
    public int NumSamples() {
        return this.num_samples;
    }
    
    public int OpenForWrite(final String Filename, final WaveFile OtherWave) {
        return this.OpenForWrite(Filename, OtherWave.SamplingRate(), OtherWave.BitsPerSample(), OtherWave.NumChannels());
    }
    
    @Override
    public long CurrentFilePosition() {
        return super.CurrentFilePosition();
    }
    
    class WaveFormat_ChunkData
    {
        public short wFormatTag;
        public short nChannels;
        public int nSamplesPerSec;
        public int nAvgBytesPerSec;
        public short nBlockAlign;
        public short nBitsPerSample;
        
        public WaveFormat_ChunkData() {
            this.wFormatTag = 0;
            this.nChannels = 0;
            this.nSamplesPerSec = 0;
            this.nAvgBytesPerSec = 0;
            this.nBlockAlign = 0;
            this.nBitsPerSample = 0;
            this.Config(44100, (short)16, this.wFormatTag = 1);
        }
        
        public void Config(final int NewSamplingRate, final short NewBitsPerSample, final short NewNumChannels) {
            this.nSamplesPerSec = NewSamplingRate;
            this.nChannels = NewNumChannels;
            this.nBitsPerSample = NewBitsPerSample;
            this.nAvgBytesPerSec = this.nChannels * this.nSamplesPerSec * this.nBitsPerSample / 8;
            this.nBlockAlign = (short)(this.nChannels * this.nBitsPerSample / 8);
        }
    }
    
    class WaveFormat_Chunk
    {
        public RiffChunkHeader header;
        public WaveFormat_ChunkData data;
        
        public WaveFormat_Chunk() {
            this.header = new RiffChunkHeader();
            this.data = new WaveFormat_ChunkData();
            this.header.ckID = RiffFile.FourCC("fmt ");
            this.header.ckSize = 16;
        }
        
        public int VerifyValidity() {
            final boolean ret = this.header.ckID == RiffFile.FourCC("fmt ") && (this.data.nChannels == 1 || this.data.nChannels == 2) && this.data.nAvgBytesPerSec == this.data.nChannels * this.data.nSamplesPerSec * this.data.nBitsPerSample / 8 && this.data.nBlockAlign == this.data.nChannels * this.data.nBitsPerSample / 8;
            if (ret) {
                return 1;
            }
            return 0;
        }
    }
    
    public class WaveFileSample
    {
        public short[] chan;
        
        public WaveFileSample() {
            this.chan = new short[2];
        }
    }
}
