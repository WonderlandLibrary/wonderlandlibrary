package com.jcraft.jorbis;

import com.jcraft.jogg.Packet;
import com.jcraft.jogg.Page;
import com.jcraft.jogg.StreamState;
import com.jcraft.jogg.SyncState;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;

public class VorbisFile {
  static final int CHUNKSIZE = 8500;
  
  static final int SEEK_SET = 0;
  
  static final int SEEK_CUR = 1;
  
  static final int SEEK_END = 2;
  
  static final int OV_FALSE = -1;
  
  static final int OV_EOF = -2;
  
  static final int OV_HOLE = -3;
  
  static final int OV_EREAD = -128;
  
  static final int OV_EFAULT = -129;
  
  static final int OV_EIMPL = -130;
  
  static final int OV_EINVAL = -131;
  
  static final int OV_ENOTVORBIS = -132;
  
  static final int OV_EBADHEADER = -133;
  
  static final int OV_EVERSION = -134;
  
  static final int OV_ENOTAUDIO = -135;
  
  static final int OV_EBADPACKET = -136;
  
  static final int OV_EBADLINK = -137;
  
  static final int OV_ENOSEEK = -138;
  
  InputStream datasource;
  
  boolean seekable = false;
  
  long offset;
  
  long end;
  
  SyncState oy = new SyncState();
  
  int links;
  
  long[] offsets;
  
  long[] dataoffsets;
  
  int[] serialnos;
  
  long[] pcmlengths;
  
  Info[] vi;
  
  Comment[] vc;
  
  long pcm_offset;
  
  boolean decode_ready = false;
  
  int current_serialno;
  
  int current_link;
  
  float bittrack;
  
  float samptrack;
  
  StreamState os = new StreamState();
  
  DspState vd = new DspState();
  
  Block vb = new Block(this.vd);
  
  public VorbisFile(String file) throws JOrbisException {
    InputStream is = null;
    try {
      is = new SeekableInputStream(file);
      int ret = open(is, null, 0);
      if (ret == -1)
        throw new JOrbisException("VorbisFile: open return -1"); 
    } catch (Exception e) {
      throw new JOrbisException("VorbisFile: " + e.toString());
    } finally {
      if (is != null)
        try {
          is.close();
        } catch (IOException e) {
          e.printStackTrace();
        }  
    } 
  }
  
  public VorbisFile(InputStream is, byte[] initial, int ibytes) throws JOrbisException {
    int ret = open(is, initial, ibytes);
    if (ret == -1);
  }
  
  private int get_data() {
    int index = this.oy.buffer(8500);
    byte[] buffer = this.oy.data;
    int bytes = 0;
    try {
      bytes = this.datasource.read(buffer, index, 8500);
    } catch (Exception e) {
      return -128;
    } 
    this.oy.wrote(bytes);
    if (bytes == -1)
      bytes = 0; 
    return bytes;
  }
  
  private void seek_helper(long offst) {
    fseek(this.datasource, offst, 0);
    this.offset = offst;
    this.oy.reset();
  }
  
  private int get_next_page(Page page, long boundary) {
    int more;
    if (boundary > 0L)
      boundary += this.offset; 
    while (true) {
      if (boundary > 0L && this.offset >= boundary)
        return -1; 
      more = this.oy.pageseek(page);
      if (more < 0) {
        this.offset -= more;
        continue;
      } 
      if (more == 0) {
        if (boundary == 0L)
          return -1; 
        int i = get_data();
        if (i == 0)
          return -2; 
        if (i < 0)
          return -128; 
        continue;
      } 
      break;
    } 
    int ret = (int)this.offset;
    this.offset += more;
    return ret;
  }
  
  private int get_prev_page(Page page) throws JOrbisException {
    long begin = this.offset;
    int offst = -1;
    while (offst == -1) {
      begin -= 8500L;
      if (begin < 0L)
        begin = 0L; 
      seek_helper(begin);
      while (this.offset < begin + 8500L) {
        int i = get_next_page(page, begin + 8500L - this.offset);
        if (i == -128)
          return -128; 
        if (i < 0) {
          if (offst == -1)
            throw new JOrbisException(); 
          break;
        } 
        offst = i;
      } 
    } 
    seek_helper(offst);
    int ret = get_next_page(page, 8500L);
    if (ret < 0)
      return -129; 
    return offst;
  }
  
  int bisect_forward_serialno(long begin, long searched, long end, int currentno, int m) {
    long endsearched = end;
    long next = end;
    Page page = new Page();
    while (searched < endsearched) {
      long bisect;
      if (endsearched - searched < 8500L) {
        bisect = searched;
      } else {
        bisect = (searched + endsearched) / 2L;
      } 
      seek_helper(bisect);
      int i = get_next_page(page, -1L);
      if (i == -128)
        return -128; 
      if (i < 0 || page.serialno() != currentno) {
        endsearched = bisect;
        if (i >= 0)
          next = i; 
        continue;
      } 
      searched = (i + page.header_len + page.body_len);
    } 
    seek_helper(next);
    int ret = get_next_page(page, -1L);
    if (ret == -128)
      return -128; 
    if (searched >= end || ret == -1) {
      this.links = m + 1;
      this.offsets = new long[m + 2];
      this.offsets[m + 1] = searched;
    } else {
      ret = bisect_forward_serialno(next, this.offset, end, page.serialno(), m + 1);
      if (ret == -128)
        return -128; 
    } 
    this.offsets[m] = begin;
    return 0;
  }
  
  int fetch_headers(Info vi, Comment vc, int[] serialno, Page og_ptr) {
    Page og = new Page();
    Packet op = new Packet();
    if (og_ptr == null) {
      int ret = get_next_page(og, 8500L);
      if (ret == -128)
        return -128; 
      if (ret < 0)
        return -132; 
      og_ptr = og;
    } 
    if (serialno != null)
      serialno[0] = og_ptr.serialno(); 
    this.os.init(og_ptr.serialno());
    vi.init();
    vc.init();
    int i = 0;
    while (i < 3) {
      this.os.pagein(og_ptr);
      while (i < 3) {
        int result = this.os.packetout(op);
        if (result == 0)
          break; 
        if (result == -1) {
          vi.clear();
          vc.clear();
          this.os.clear();
          return -1;
        } 
        if (vi.synthesis_headerin(vc, op) != 0) {
          vi.clear();
          vc.clear();
          this.os.clear();
          return -1;
        } 
        i++;
      } 
      if (i < 3 && 
        get_next_page(og_ptr, 1L) < 0) {
        vi.clear();
        vc.clear();
        this.os.clear();
        return -1;
      } 
    } 
    return 0;
  }
  
  void prefetch_all_headers(Info first_i, Comment first_c, int dataoffset) throws JOrbisException {
    Page og = new Page();
    this.vi = new Info[this.links];
    this.vc = new Comment[this.links];
    this.dataoffsets = new long[this.links];
    this.pcmlengths = new long[this.links];
    this.serialnos = new int[this.links];
    for (int i = 0; i < this.links; i++) {
      if (first_i != null && first_c != null && i == 0) {
        this.vi[i] = first_i;
        this.vc[i] = first_c;
        this.dataoffsets[i] = dataoffset;
      } else {
        seek_helper(this.offsets[i]);
        this.vi[i] = new Info();
        this.vc[i] = new Comment();
        if (fetch_headers(this.vi[i], this.vc[i], null, null) == -1) {
          this.dataoffsets[i] = -1L;
        } else {
          this.dataoffsets[i] = this.offset;
          this.os.clear();
        } 
      } 
      long end = this.offsets[i + 1];
      seek_helper(end);
      while (true) {
        int ret = get_prev_page(og);
        if (ret == -1) {
          this.vi[i].clear();
          this.vc[i].clear();
          break;
        } 
        if (og.granulepos() != -1L) {
          this.serialnos[i] = og.serialno();
          this.pcmlengths[i] = og.granulepos();
          break;
        } 
      } 
    } 
  }
  
  private int make_decode_ready() {
    if (this.decode_ready)
      System.exit(1); 
    this.vd.synthesis_init(this.vi[0]);
    this.vb.init(this.vd);
    this.decode_ready = true;
    return 0;
  }
  
  int open_seekable() throws JOrbisException {
    Info initial_i = new Info();
    Comment initial_c = new Comment();
    Page og = new Page();
    int[] foo = new int[1];
    int ret = fetch_headers(initial_i, initial_c, foo, null);
    int serialno = foo[0];
    int dataoffset = (int)this.offset;
    this.os.clear();
    if (ret == -1)
      return -1; 
    if (ret < 0)
      return ret; 
    this.seekable = true;
    fseek(this.datasource, 0L, 2);
    this.offset = ftell(this.datasource);
    long end = this.offset;
    end = get_prev_page(og);
    if (og.serialno() != serialno) {
      if (bisect_forward_serialno(0L, 0L, end + 1L, serialno, 0) < 0) {
        clear();
        return -128;
      } 
    } else if (bisect_forward_serialno(0L, end, end + 1L, serialno, 0) < 0) {
      clear();
      return -128;
    } 
    prefetch_all_headers(initial_i, initial_c, dataoffset);
    return 0;
  }
  
  int open_nonseekable() {
    this.links = 1;
    this.vi = new Info[this.links];
    this.vi[0] = new Info();
    this.vc = new Comment[this.links];
    this.vc[0] = new Comment();
    int[] foo = new int[1];
    if (fetch_headers(this.vi[0], this.vc[0], foo, null) == -1)
      return -1; 
    this.current_serialno = foo[0];
    make_decode_ready();
    return 0;
  }
  
  void decode_clear() {
    this.os.clear();
    this.vd.clear();
    this.vb.clear();
    this.decode_ready = false;
    this.bittrack = 0.0F;
    this.samptrack = 0.0F;
  }
  
  int process_packet(int readp) {
    Page og = new Page();
    while (true) {
      if (this.decode_ready) {
        Packet op = new Packet();
        int result = this.os.packetout(op);
        if (result > 0) {
          long granulepos = op.granulepos;
          if (this.vb.synthesis(op) == 0) {
            int oldsamples = this.vd.synthesis_pcmout((float[][][])null, null);
            this.vd.synthesis_blockin(this.vb);
            this.samptrack += (this.vd.synthesis_pcmout((float[][][])null, null) - oldsamples);
            this.bittrack += (op.bytes * 8);
            if (granulepos != -1L && op.e_o_s == 0) {
              int link = this.seekable ? this.current_link : 0;
              int samples = this.vd.synthesis_pcmout((float[][][])null, null);
              granulepos -= samples;
              for (int i = 0; i < link; i++)
                granulepos += this.pcmlengths[i]; 
              this.pcm_offset = granulepos;
            } 
            return 1;
          } 
        } 
      } 
      if (readp == 0)
        return 0; 
      if (get_next_page(og, -1L) < 0)
        return 0; 
      this.bittrack += (og.header_len * 8);
      if (this.decode_ready && 
        this.current_serialno != og.serialno())
        decode_clear(); 
      if (!this.decode_ready) {
        if (this.seekable) {
          this.current_serialno = og.serialno();
          int i;
          for (i = 0; i < this.links && 
            this.serialnos[i] != this.current_serialno; i++);
          if (i == this.links)
            return -1; 
          this.current_link = i;
          this.os.init(this.current_serialno);
          this.os.reset();
        } else {
          int[] foo = new int[1];
          int ret = fetch_headers(this.vi[0], this.vc[0], foo, og);
          this.current_serialno = foo[0];
          if (ret != 0)
            return ret; 
          this.current_link++;
          int i = 0;
        } 
        make_decode_ready();
      } 
      this.os.pagein(og);
    } 
  }
  
  int clear() {
    this.vb.clear();
    this.vd.clear();
    this.os.clear();
    if (this.vi != null && this.links != 0) {
      for (int i = 0; i < this.links; i++) {
        this.vi[i].clear();
        this.vc[i].clear();
      } 
      this.vi = null;
      this.vc = null;
    } 
    if (this.dataoffsets != null)
      this.dataoffsets = null; 
    if (this.pcmlengths != null)
      this.pcmlengths = null; 
    if (this.serialnos != null)
      this.serialnos = null; 
    if (this.offsets != null)
      this.offsets = null; 
    this.oy.clear();
    return 0;
  }
  
  static int fseek(InputStream fis, long off, int whence) {
    if (fis instanceof SeekableInputStream) {
      SeekableInputStream sis = (SeekableInputStream)fis;
      try {
        if (whence == 0) {
          sis.seek(off);
        } else if (whence == 2) {
          sis.seek(sis.getLength() - off);
        } 
      } catch (Exception e) {}
      return 0;
    } 
    try {
      if (whence == 0)
        fis.reset(); 
      fis.skip(off);
    } catch (Exception e) {
      return -1;
    } 
    return 0;
  }
  
  static long ftell(InputStream fis) {
    try {
      if (fis instanceof SeekableInputStream) {
        SeekableInputStream sis = (SeekableInputStream)fis;
        return sis.tell();
      } 
    } catch (Exception e) {}
    return 0L;
  }
  
  int open(InputStream is, byte[] initial, int ibytes) throws JOrbisException {
    return open_callbacks(is, initial, ibytes);
  }
  
  int open_callbacks(InputStream is, byte[] initial, int ibytes) throws JOrbisException {
    int ret;
    this.datasource = is;
    this.oy.init();
    if (initial != null) {
      int index = this.oy.buffer(ibytes);
      System.arraycopy(initial, 0, this.oy.data, index, ibytes);
      this.oy.wrote(ibytes);
    } 
    if (is instanceof SeekableInputStream) {
      ret = open_seekable();
    } else {
      ret = open_nonseekable();
    } 
    if (ret != 0) {
      this.datasource = null;
      clear();
    } 
    return ret;
  }
  
  public int streams() {
    return this.links;
  }
  
  public boolean seekable() {
    return this.seekable;
  }
  
  public int bitrate(int i) {
    if (i >= this.links)
      return -1; 
    if (!this.seekable && i != 0)
      return bitrate(0); 
    if (i < 0) {
      long bits = 0L;
      for (int j = 0; j < this.links; j++)
        bits += (this.offsets[j + 1] - this.dataoffsets[j]) * 8L; 
      return (int)Math.rint(((float)bits / time_total(-1)));
    } 
    if (this.seekable)
      return (int)Math.rint(((float)((this.offsets[i + 1] - this.dataoffsets[i]) * 8L) / time_total(i))); 
    if ((this.vi[i]).bitrate_nominal > 0)
      return (this.vi[i]).bitrate_nominal; 
    if ((this.vi[i]).bitrate_upper > 0) {
      if ((this.vi[i]).bitrate_lower > 0)
        return ((this.vi[i]).bitrate_upper + (this.vi[i]).bitrate_lower) / 2; 
      return (this.vi[i]).bitrate_upper;
    } 
    return -1;
  }
  
  public int bitrate_instant() {
    int _link = this.seekable ? this.current_link : 0;
    if (this.samptrack == 0.0F)
      return -1; 
    int ret = (int)((this.bittrack / this.samptrack * (this.vi[_link]).rate) + 0.5D);
    this.bittrack = 0.0F;
    this.samptrack = 0.0F;
    return ret;
  }
  
  public int serialnumber(int i) {
    if (i >= this.links)
      return -1; 
    if (!this.seekable && i >= 0)
      return serialnumber(-1); 
    if (i < 0)
      return this.current_serialno; 
    return this.serialnos[i];
  }
  
  public long raw_total(int i) {
    if (!this.seekable || i >= this.links)
      return -1L; 
    if (i < 0) {
      long acc = 0L;
      for (int j = 0; j < this.links; j++)
        acc += raw_total(j); 
      return acc;
    } 
    return this.offsets[i + 1] - this.offsets[i];
  }
  
  public long pcm_total(int i) {
    if (!this.seekable || i >= this.links)
      return -1L; 
    if (i < 0) {
      long acc = 0L;
      for (int j = 0; j < this.links; j++)
        acc += pcm_total(j); 
      return acc;
    } 
    return this.pcmlengths[i];
  }
  
  public float time_total(int i) {
    if (!this.seekable || i >= this.links)
      return -1.0F; 
    if (i < 0) {
      float acc = 0.0F;
      for (int j = 0; j < this.links; j++)
        acc += time_total(j); 
      return acc;
    } 
    return (float)this.pcmlengths[i] / (this.vi[i]).rate;
  }
  
  public int raw_seek(int pos) {
    if (!this.seekable)
      return -1; 
    if (pos < 0 || pos > this.offsets[this.links]) {
      this.pcm_offset = -1L;
      decode_clear();
      return -1;
    } 
    this.pcm_offset = -1L;
    decode_clear();
    seek_helper(pos);
    switch (process_packet(1)) {
      case 0:
        this.pcm_offset = pcm_total(-1);
        return 0;
      case -1:
        this.pcm_offset = -1L;
        decode_clear();
        return -1;
    } 
    while (true) {
      switch (process_packet(0)) {
        case 0:
          return 0;
        case -1:
          break;
      } 
    } 
    this.pcm_offset = -1L;
    decode_clear();
    return -1;
  }
  
  public int pcm_seek(long pos) {
    int link = -1;
    long total = pcm_total(-1);
    if (!this.seekable)
      return -1; 
    if (pos < 0L || pos > total) {
      this.pcm_offset = -1L;
      decode_clear();
      return -1;
    } 
    for (link = this.links - 1; link >= 0; link--) {
      total -= this.pcmlengths[link];
      if (pos >= total)
        break; 
    } 
    long target = pos - total;
    long end = this.offsets[link + 1];
    long begin = this.offsets[link];
    int best = (int)begin;
    Page og = new Page();
    while (begin < end) {
      long bisect;
      if (end - begin < 8500L) {
        bisect = begin;
      } else {
        bisect = (end + begin) / 2L;
      } 
      seek_helper(bisect);
      int ret = get_next_page(og, end - bisect);
      if (ret == -1) {
        end = bisect;
        continue;
      } 
      long granulepos = og.granulepos();
      if (granulepos < target) {
        best = ret;
        begin = this.offset;
        continue;
      } 
      end = bisect;
    } 
    if (raw_seek(best) != 0) {
      this.pcm_offset = -1L;
      decode_clear();
      return -1;
    } 
    if (this.pcm_offset >= pos) {
      this.pcm_offset = -1L;
      decode_clear();
      return -1;
    } 
    if (pos > pcm_total(-1)) {
      this.pcm_offset = -1L;
      decode_clear();
      return -1;
    } 
    while (this.pcm_offset < pos) {
      int i = (int)(pos - this.pcm_offset);
      float[][][] _pcm = new float[1][][];
      int[] _index = new int[(getInfo(-1)).channels];
      int samples = this.vd.synthesis_pcmout(_pcm, _index);
      if (samples > i)
        samples = i; 
      this.vd.synthesis_read(samples);
      this.pcm_offset += samples;
      if (samples < i && 
        process_packet(1) == 0)
        this.pcm_offset = pcm_total(-1); 
    } 
    return 0;
  }
  
  int time_seek(float seconds) {
    int link = -1;
    long pcm_total = pcm_total(-1);
    float time_total = time_total(-1);
    if (!this.seekable)
      return -1; 
    if (seconds < 0.0F || seconds > time_total) {
      this.pcm_offset = -1L;
      decode_clear();
      return -1;
    } 
    for (link = this.links - 1; link >= 0; link--) {
      pcm_total -= this.pcmlengths[link];
      time_total -= time_total(link);
      if (seconds >= time_total)
        break; 
    } 
    long target = (long)((float)pcm_total + (seconds - time_total) * (this.vi[link]).rate);
    return pcm_seek(target);
  }
  
  public long raw_tell() {
    return this.offset;
  }
  
  public long pcm_tell() {
    return this.pcm_offset;
  }
  
  public float time_tell() {
    int link = -1;
    long pcm_total = 0L;
    float time_total = 0.0F;
    if (this.seekable) {
      pcm_total = pcm_total(-1);
      time_total = time_total(-1);
      for (link = this.links - 1; link >= 0; link--) {
        pcm_total -= this.pcmlengths[link];
        time_total -= time_total(link);
        if (this.pcm_offset >= pcm_total)
          break; 
      } 
    } 
    return time_total + (float)(this.pcm_offset - pcm_total) / (this.vi[link]).rate;
  }
  
  public Info getInfo(int link) {
    if (this.seekable) {
      if (link < 0) {
        if (this.decode_ready)
          return this.vi[this.current_link]; 
        return null;
      } 
      if (link >= this.links)
        return null; 
      return this.vi[link];
    } 
    if (this.decode_ready)
      return this.vi[0]; 
    return null;
  }
  
  public Comment getComment(int link) {
    if (this.seekable) {
      if (link < 0) {
        if (this.decode_ready)
          return this.vc[this.current_link]; 
        return null;
      } 
      if (link >= this.links)
        return null; 
      return this.vc[link];
    } 
    if (this.decode_ready)
      return this.vc[0]; 
    return null;
  }
  
  int host_is_big_endian() {
    return 1;
  }
  
  int read(byte[] buffer, int length, int bigendianp, int word, int sgned, int[] bitstream) {
    int host_endian = host_is_big_endian();
    int index = 0;
    while (true) {
      if (this.decode_ready) {
        float[][][] _pcm = new float[1][][];
        int[] _index = new int[(getInfo(-1)).channels];
        int samples = this.vd.synthesis_pcmout(_pcm, _index);
        float[][] pcm = _pcm[0];
        if (samples != 0) {
          int channels = (getInfo(-1)).channels;
          int bytespersample = word * channels;
          if (samples > length / bytespersample)
            samples = length / bytespersample; 
          if (word == 1) {
            int off = (sgned != 0) ? 0 : 128;
            for (int j = 0; j < samples; j++) {
              for (int i = 0; i < channels; i++) {
                int val = (int)(pcm[i][_index[i] + j] * 128.0D + 0.5D);
                if (val > 127) {
                  val = 127;
                } else if (val < -128) {
                  val = -128;
                } 
                buffer[index++] = (byte)(val + off);
              } 
            } 
          } else {
            int off = (sgned != 0) ? 0 : 32768;
            if (host_endian == bigendianp) {
              if (sgned != 0) {
                for (int i = 0; i < channels; i++) {
                  int src = _index[i];
                  int dest = i;
                  for (int j = 0; j < samples; j++) {
                    int val = (int)(pcm[i][src + j] * 32768.0D + 0.5D);
                    if (val > 32767) {
                      val = 32767;
                    } else if (val < -32768) {
                      val = -32768;
                    } 
                    buffer[dest] = (byte)(val >>> 8);
                    buffer[dest + 1] = (byte)val;
                    dest += channels * 2;
                  } 
                } 
              } else {
                for (int i = 0; i < channels; i++) {
                  float[] src = pcm[i];
                  int dest = i;
                  for (int j = 0; j < samples; j++) {
                    int val = (int)(src[j] * 32768.0D + 0.5D);
                    if (val > 32767) {
                      val = 32767;
                    } else if (val < -32768) {
                      val = -32768;
                    } 
                    buffer[dest] = (byte)(val + off >>> 8);
                    buffer[dest + 1] = (byte)(val + off);
                    dest += channels * 2;
                  } 
                } 
              } 
            } else if (bigendianp != 0) {
              for (int j = 0; j < samples; j++) {
                for (int i = 0; i < channels; i++) {
                  int val = (int)(pcm[i][j] * 32768.0D + 0.5D);
                  if (val > 32767) {
                    val = 32767;
                  } else if (val < -32768) {
                    val = -32768;
                  } 
                  val += off;
                  buffer[index++] = (byte)(val >>> 8);
                  buffer[index++] = (byte)val;
                } 
              } 
            } else {
              for (int j = 0; j < samples; j++) {
                for (int i = 0; i < channels; i++) {
                  int val = (int)(pcm[i][j] * 32768.0D + 0.5D);
                  if (val > 32767) {
                    val = 32767;
                  } else if (val < -32768) {
                    val = -32768;
                  } 
                  val += off;
                  buffer[index++] = (byte)val;
                  buffer[index++] = (byte)(val >>> 8);
                } 
              } 
            } 
          } 
          this.vd.synthesis_read(samples);
          this.pcm_offset += samples;
          if (bitstream != null)
            bitstream[0] = this.current_link; 
          return samples * bytespersample;
        } 
      } 
      switch (process_packet(1)) {
        case 0:
          return 0;
        case -1:
          break;
      } 
    } 
    return -1;
  }
  
  public Info[] getInfo() {
    return this.vi;
  }
  
  public Comment[] getComment() {
    return this.vc;
  }
  
  public void close() throws IOException {
    this.datasource.close();
  }
  
  class SeekableInputStream extends InputStream {
    RandomAccessFile raf = null;
    
    final String mode = "r";
    
    SeekableInputStream(String file) throws IOException {
      this.raf = new RandomAccessFile(file, "r");
    }
    
    public int read() throws IOException {
      return this.raf.read();
    }
    
    public int read(byte[] buf) throws IOException {
      return this.raf.read(buf);
    }
    
    public int read(byte[] buf, int s, int len) throws IOException {
      return this.raf.read(buf, s, len);
    }
    
    public long skip(long n) throws IOException {
      return this.raf.skipBytes((int)n);
    }
    
    public long getLength() throws IOException {
      return this.raf.length();
    }
    
    public long tell() throws IOException {
      return this.raf.getFilePointer();
    }
    
    public int available() throws IOException {
      return (this.raf.length() == this.raf.getFilePointer()) ? 0 : 1;
    }
    
    public void close() throws IOException {
      this.raf.close();
    }
    
    public synchronized void mark(int m) {}
    
    public synchronized void reset() throws IOException {}
    
    public boolean markSupported() {
      return false;
    }
    
    public void seek(long pos) throws IOException {
      this.raf.seek(pos);
    }
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\jcraft\jorbis\VorbisFile.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */