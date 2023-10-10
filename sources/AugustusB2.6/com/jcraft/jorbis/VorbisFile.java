// 
// Decompiled by Procyon v0.5.36
// 

package com.jcraft.jorbis;

import java.io.RandomAccessFile;
import com.jcraft.jogg.Packet;
import com.jcraft.jogg.Page;
import java.io.IOException;
import com.jcraft.jogg.StreamState;
import com.jcraft.jogg.SyncState;
import java.io.InputStream;

public class VorbisFile
{
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
    boolean seekable;
    long offset;
    long end;
    SyncState oy;
    int links;
    long[] offsets;
    long[] dataoffsets;
    int[] serialnos;
    long[] pcmlengths;
    Info[] vi;
    Comment[] vc;
    long pcm_offset;
    boolean decode_ready;
    int current_serialno;
    int current_link;
    float bittrack;
    float samptrack;
    StreamState os;
    DspState vd;
    Block vb;
    
    public VorbisFile(final String s) throws JOrbisException {
        this.seekable = false;
        this.oy = new SyncState();
        this.decode_ready = false;
        this.os = new StreamState();
        this.vd = new DspState();
        this.vb = new Block(this.vd);
        InputStream inputStream = null;
        try {
            inputStream = new SeekableInputStream(s);
            if (this.open(inputStream, null, 0) == -1) {
                throw new JOrbisException("VorbisFile: open return -1");
            }
        }
        catch (Exception ex) {
            throw new JOrbisException("VorbisFile: " + ex.toString());
        }
        finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                }
                catch (IOException ex2) {
                    ex2.printStackTrace();
                }
            }
        }
    }
    
    public VorbisFile(final InputStream inputStream, final byte[] array, final int n) throws JOrbisException {
        this.seekable = false;
        this.oy = new SyncState();
        this.decode_ready = false;
        this.os = new StreamState();
        this.vd = new DspState();
        this.vb = new Block(this.vd);
        if (this.open(inputStream, array, n) == -1) {}
    }
    
    private int get_data() {
        final int buffer = this.oy.buffer(8500);
        final byte[] data = this.oy.data;
        int read;
        try {
            read = this.datasource.read(data, buffer, 8500);
        }
        catch (Exception ex) {
            return -128;
        }
        this.oy.wrote(read);
        if (read == -1) {
            read = 0;
        }
        return read;
    }
    
    private void seek_helper(final long offset) {
        fseek(this.datasource, offset, 0);
        this.offset = offset;
        this.oy.reset();
    }
    
    private int get_next_page(final Page page, long n) {
        if (n > 0L) {
            n += this.offset;
        }
        while (n <= 0L || this.offset < n) {
            final int pageseek = this.oy.pageseek(page);
            if (pageseek < 0) {
                this.offset -= pageseek;
            }
            else {
                if (pageseek != 0) {
                    final int n2 = (int)this.offset;
                    this.offset += pageseek;
                    return n2;
                }
                if (n == 0L) {
                    return -1;
                }
                final int get_data = this.get_data();
                if (get_data == 0) {
                    return -2;
                }
                if (get_data < 0) {
                    return -128;
                }
                continue;
            }
        }
        return -1;
    }
    
    private int get_prev_page(final Page page) throws JOrbisException {
        long offset = this.offset;
        int i = -1;
        while (i == -1) {
            offset -= 8500L;
            if (offset < 0L) {
                offset = 0L;
            }
            this.seek_helper(offset);
            while (this.offset < offset + 8500L) {
                final int get_next_page = this.get_next_page(page, offset + 8500L - this.offset);
                if (get_next_page == -128) {
                    return -128;
                }
                if (get_next_page < 0) {
                    if (i == -1) {
                        throw new JOrbisException();
                    }
                    break;
                }
                else {
                    i = get_next_page;
                }
            }
        }
        this.seek_helper(i);
        if (this.get_next_page(page, 8500L) < 0) {
            return -129;
        }
        return i;
    }
    
    int bisect_forward_serialno(final long n, long n2, final long n3, final int n4, final int n5) {
        long n6 = n3;
        long n7 = n3;
        final Page page = new Page();
        while (n2 < n6) {
            long n8;
            if (n6 - n2 < 8500L) {
                n8 = n2;
            }
            else {
                n8 = (n2 + n6) / 2L;
            }
            this.seek_helper(n8);
            final int get_next_page = this.get_next_page(page, -1L);
            if (get_next_page == -128) {
                return -128;
            }
            if (get_next_page < 0 || page.serialno() != n4) {
                n6 = n8;
                if (get_next_page < 0) {
                    continue;
                }
                n7 = get_next_page;
            }
            else {
                n2 = get_next_page + page.header_len + page.body_len;
            }
        }
        this.seek_helper(n7);
        final int get_next_page2 = this.get_next_page(page, -1L);
        if (get_next_page2 == -128) {
            return -128;
        }
        if (n2 >= n3 || get_next_page2 == -1) {
            this.links = n5 + 1;
            (this.offsets = new long[n5 + 2])[n5 + 1] = n2;
        }
        else if (this.bisect_forward_serialno(n7, this.offset, n3, page.serialno(), n5 + 1) == -128) {
            return -128;
        }
        this.offsets[n5] = n;
        return 0;
    }
    
    int fetch_headers(final Info info, final Comment comment, final int[] array, Page page) {
        final Page page2 = new Page();
        final Packet packet = new Packet();
        if (page == null) {
            final int get_next_page = this.get_next_page(page2, 8500L);
            if (get_next_page == -128) {
                return -128;
            }
            if (get_next_page < 0) {
                return -132;
            }
            page = page2;
        }
        if (array != null) {
            array[0] = page.serialno();
        }
        this.os.init(page.serialno());
        info.init();
        comment.init();
        int i = 0;
        while (i < 3) {
            this.os.pagein(page);
            while (i < 3) {
                final int packetout = this.os.packetout(packet);
                if (packetout == 0) {
                    break;
                }
                if (packetout == -1) {
                    info.clear();
                    comment.clear();
                    this.os.clear();
                    return -1;
                }
                if (info.synthesis_headerin(comment, packet) != 0) {
                    info.clear();
                    comment.clear();
                    this.os.clear();
                    return -1;
                }
                ++i;
            }
            if (i < 3 && this.get_next_page(page, 1L) < 0) {
                info.clear();
                comment.clear();
                this.os.clear();
                return -1;
            }
        }
        return 0;
    }
    
    void prefetch_all_headers(final Info info, final Comment comment, final int n) throws JOrbisException {
        final Page page = new Page();
        this.vi = new Info[this.links];
        this.vc = new Comment[this.links];
        this.dataoffsets = new long[this.links];
        this.pcmlengths = new long[this.links];
        this.serialnos = new int[this.links];
        int i = 0;
    Label_0301:
        while (i < this.links) {
            if (info != null && comment != null && i == 0) {
                this.vi[i] = info;
                this.vc[i] = comment;
                this.dataoffsets[i] = n;
            }
            else {
                this.seek_helper(this.offsets[i]);
                this.vi[i] = new Info();
                this.vc[i] = new Comment();
                if (this.fetch_headers(this.vi[i], this.vc[i], null, null) == -1) {
                    this.dataoffsets[i] = -1L;
                }
                else {
                    this.dataoffsets[i] = this.offset;
                    this.os.clear();
                }
            }
            this.seek_helper(this.offsets[i + 1]);
            while (true) {
                while (this.get_prev_page(page) != -1) {
                    if (page.granulepos() != -1L) {
                        this.serialnos[i] = page.serialno();
                        this.pcmlengths[i] = page.granulepos();
                        ++i;
                        continue Label_0301;
                    }
                }
                this.vi[i].clear();
                this.vc[i].clear();
                continue;
            }
        }
    }
    
    int make_decode_ready() {
        if (this.decode_ready) {
            System.exit(1);
        }
        this.vd.synthesis_init(this.vi[0]);
        this.vb.init(this.vd);
        this.decode_ready = true;
        return 0;
    }
    
    int open_seekable() throws JOrbisException {
        final Info info = new Info();
        final Comment comment = new Comment();
        final Page page = new Page();
        final int[] array = { 0 };
        final int fetch_headers = this.fetch_headers(info, comment, array, null);
        final int n = array[0];
        final int n2 = (int)this.offset;
        this.os.clear();
        if (fetch_headers == -1) {
            return -1;
        }
        this.seekable = true;
        fseek(this.datasource, 0L, 2);
        this.offset = ftell(this.datasource);
        final long offset = this.offset;
        final long n3 = this.get_prev_page(page);
        if (page.serialno() != n) {
            if (this.bisect_forward_serialno(0L, 0L, n3 + 1L, n, 0) < 0) {
                this.clear();
                return -128;
            }
        }
        else if (this.bisect_forward_serialno(0L, n3, n3 + 1L, n, 0) < 0) {
            this.clear();
            return -128;
        }
        this.prefetch_all_headers(info, comment, n2);
        return this.raw_seek(0);
    }
    
    int open_nonseekable() {
        this.links = 1;
        (this.vi = new Info[this.links])[0] = new Info();
        (this.vc = new Comment[this.links])[0] = new Comment();
        final int[] array = { 0 };
        if (this.fetch_headers(this.vi[0], this.vc[0], array, null) == -1) {
            return -1;
        }
        this.current_serialno = array[0];
        this.make_decode_ready();
        return 0;
    }
    
    void decode_clear() {
        this.os.clear();
        this.vd.clear();
        this.vb.clear();
        this.decode_ready = false;
        this.bittrack = 0.0f;
        this.samptrack = 0.0f;
    }
    
    int process_packet(final int n) {
        final Page page = new Page();
        while (true) {
            if (this.decode_ready) {
                final Packet packet = new Packet();
                if (this.os.packetout(packet) > 0) {
                    final long granulepos = packet.granulepos;
                    if (this.vb.synthesis(packet) == 0) {
                        final int synthesis_pcmout = this.vd.synthesis_pcmout(null, null);
                        this.vd.synthesis_blockin(this.vb);
                        this.samptrack += this.vd.synthesis_pcmout(null, null) - synthesis_pcmout;
                        this.bittrack += packet.bytes * 8;
                        if (granulepos != -1L && packet.e_o_s == 0) {
                            final int n2 = this.seekable ? this.current_link : 0;
                            long pcm_offset = granulepos - this.vd.synthesis_pcmout(null, null);
                            for (int i = 0; i < n2; ++i) {
                                pcm_offset += this.pcmlengths[i];
                            }
                            this.pcm_offset = pcm_offset;
                        }
                        return 1;
                    }
                }
            }
            if (n == 0) {
                return 0;
            }
            if (this.get_next_page(page, -1L) < 0) {
                return 0;
            }
            this.bittrack += page.header_len * 8;
            if (this.decode_ready && this.current_serialno != page.serialno()) {
                this.decode_clear();
            }
            if (!this.decode_ready) {
                if (this.seekable) {
                    this.current_serialno = page.serialno();
                    int current_link;
                    for (current_link = 0; current_link < this.links && this.serialnos[current_link] != this.current_serialno; ++current_link) {}
                    if (current_link == this.links) {
                        return -1;
                    }
                    this.current_link = current_link;
                    this.os.init(this.current_serialno);
                    this.os.reset();
                }
                else {
                    final int[] array = { 0 };
                    final int fetch_headers = this.fetch_headers(this.vi[0], this.vc[0], array, page);
                    this.current_serialno = array[0];
                    if (fetch_headers != 0) {
                        return fetch_headers;
                    }
                    ++this.current_link;
                }
                this.make_decode_ready();
            }
            this.os.pagein(page);
        }
    }
    
    int clear() {
        this.vb.clear();
        this.vd.clear();
        this.os.clear();
        if (this.vi != null && this.links != 0) {
            for (int i = 0; i < this.links; ++i) {
                this.vi[i].clear();
                this.vc[i].clear();
            }
            this.vi = null;
            this.vc = null;
        }
        if (this.dataoffsets != null) {
            this.dataoffsets = null;
        }
        if (this.pcmlengths != null) {
            this.pcmlengths = null;
        }
        if (this.serialnos != null) {
            this.serialnos = null;
        }
        if (this.offsets != null) {
            this.offsets = null;
        }
        this.oy.clear();
        return 0;
    }
    
    static int fseek(final InputStream inputStream, final long n, final int n2) {
        if (inputStream instanceof SeekableInputStream) {
            final SeekableInputStream seekableInputStream = (SeekableInputStream)inputStream;
            try {
                if (n2 == 0) {
                    seekableInputStream.seek(n);
                }
                else if (n2 == 2) {
                    seekableInputStream.seek(seekableInputStream.getLength() - n);
                }
            }
            catch (Exception ex) {}
            return 0;
        }
        try {
            if (n2 == 0) {
                inputStream.reset();
            }
            inputStream.skip(n);
        }
        catch (Exception ex2) {
            return -1;
        }
        return 0;
    }
    
    static long ftell(final InputStream inputStream) {
        try {
            if (inputStream instanceof SeekableInputStream) {
                return ((SeekableInputStream)inputStream).tell();
            }
        }
        catch (Exception ex) {}
        return 0L;
    }
    
    int open(final InputStream inputStream, final byte[] array, final int n) throws JOrbisException {
        return this.open_callbacks(inputStream, array, n);
    }
    
    int open_callbacks(final InputStream datasource, final byte[] array, final int n) throws JOrbisException {
        this.datasource = datasource;
        this.oy.init();
        if (array != null) {
            System.arraycopy(array, 0, this.oy.data, this.oy.buffer(n), n);
            this.oy.wrote(n);
        }
        int n2;
        if (datasource instanceof SeekableInputStream) {
            n2 = this.open_seekable();
        }
        else {
            n2 = this.open_nonseekable();
        }
        if (n2 != 0) {
            this.datasource = null;
            this.clear();
        }
        return n2;
    }
    
    public int streams() {
        return this.links;
    }
    
    public boolean seekable() {
        return this.seekable;
    }
    
    public int bitrate(final int n) {
        if (n >= this.links) {
            return -1;
        }
        if (!this.seekable && n != 0) {
            return this.bitrate(0);
        }
        if (n < 0) {
            long n2 = 0L;
            for (int i = 0; i < this.links; ++i) {
                n2 += (this.offsets[i + 1] - this.dataoffsets[i]) * 8L;
            }
            return (int)Math.rint(n2 / this.time_total(-1));
        }
        if (this.seekable) {
            return (int)Math.rint((this.offsets[n + 1] - this.dataoffsets[n]) * 8L / this.time_total(n));
        }
        if (this.vi[n].bitrate_nominal > 0) {
            return this.vi[n].bitrate_nominal;
        }
        if (this.vi[n].bitrate_upper <= 0) {
            return -1;
        }
        if (this.vi[n].bitrate_lower > 0) {
            return (this.vi[n].bitrate_upper + this.vi[n].bitrate_lower) / 2;
        }
        return this.vi[n].bitrate_upper;
    }
    
    public int bitrate_instant() {
        final int n = this.seekable ? this.current_link : 0;
        if (this.samptrack == 0.0f) {
            return -1;
        }
        final int n2 = (int)(this.bittrack / this.samptrack * this.vi[n].rate + 0.5);
        this.bittrack = 0.0f;
        this.samptrack = 0.0f;
        return n2;
    }
    
    public int serialnumber(final int n) {
        if (n >= this.links) {
            return -1;
        }
        if (!this.seekable && n >= 0) {
            return this.serialnumber(-1);
        }
        if (n < 0) {
            return this.current_serialno;
        }
        return this.serialnos[n];
    }
    
    public long raw_total(final int n) {
        if (!this.seekable || n >= this.links) {
            return -1L;
        }
        if (n < 0) {
            long n2 = 0L;
            for (int i = 0; i < this.links; ++i) {
                n2 += this.raw_total(i);
            }
            return n2;
        }
        return this.offsets[n + 1] - this.offsets[n];
    }
    
    public long pcm_total(final int n) {
        if (!this.seekable || n >= this.links) {
            return -1L;
        }
        if (n < 0) {
            long n2 = 0L;
            for (int i = 0; i < this.links; ++i) {
                n2 += this.pcm_total(i);
            }
            return n2;
        }
        return this.pcmlengths[n];
    }
    
    public float time_total(final int n) {
        if (!this.seekable || n >= this.links) {
            return -1.0f;
        }
        if (n < 0) {
            float n2 = 0.0f;
            for (int i = 0; i < this.links; ++i) {
                n2 += this.time_total(i);
            }
            return n2;
        }
        return this.pcmlengths[n] / (float)this.vi[n].rate;
    }
    
    public int raw_seek(final int n) {
        if (!this.seekable) {
            return -1;
        }
        if (n < 0 || n > this.offsets[this.links]) {
            this.pcm_offset = -1L;
            this.decode_clear();
            return -1;
        }
        this.pcm_offset = -1L;
        this.decode_clear();
        this.seek_helper(n);
        switch (this.process_packet(1)) {
            case 0: {
                this.pcm_offset = this.pcm_total(-1);
                return 0;
            }
            case -1: {
                this.pcm_offset = -1L;
                this.decode_clear();
                return -1;
            }
            default: {
                while (true) {
                    switch (this.process_packet(0)) {
                        case 0: {
                            return 0;
                        }
                        case -1: {
                            this.pcm_offset = -1L;
                            this.decode_clear();
                            return -1;
                        }
                        default: {
                            continue;
                        }
                    }
                }
                break;
            }
        }
    }
    
    public int pcm_seek(final long n) {
        long pcm_total = this.pcm_total(-1);
        if (!this.seekable) {
            return -1;
        }
        if (n < 0L || n > pcm_total) {
            this.pcm_offset = -1L;
            this.decode_clear();
            return -1;
        }
        int i;
        for (i = this.links - 1; i >= 0; --i) {
            pcm_total -= this.pcmlengths[i];
            if (n >= pcm_total) {
                break;
            }
        }
        final long n2 = n - pcm_total;
        long n3 = this.offsets[i + 1];
        long offset = this.offsets[i];
        int n4 = (int)offset;
        final Page page = new Page();
        while (offset < n3) {
            long n5;
            if (n3 - offset < 8500L) {
                n5 = offset;
            }
            else {
                n5 = (n3 + offset) / 2L;
            }
            this.seek_helper(n5);
            final int get_next_page = this.get_next_page(page, n3 - n5);
            if (get_next_page == -1) {
                n3 = n5;
            }
            else if (page.granulepos() < n2) {
                n4 = get_next_page;
                offset = this.offset;
            }
            else {
                n3 = n5;
            }
        }
        if (this.raw_seek(n4) != 0) {
            this.pcm_offset = -1L;
            this.decode_clear();
            return -1;
        }
        if (this.pcm_offset >= n) {
            this.pcm_offset = -1L;
            this.decode_clear();
            return -1;
        }
        if (n > this.pcm_total(-1)) {
            this.pcm_offset = -1L;
            this.decode_clear();
            return -1;
        }
        while (this.pcm_offset < n) {
            final int n6 = (int)(n - this.pcm_offset);
            final float[][][] array = { null };
            int synthesis_pcmout = this.vd.synthesis_pcmout(array, new int[this.getInfo(-1).channels]);
            final float[][] array2 = array[0];
            if (synthesis_pcmout > n6) {
                synthesis_pcmout = n6;
            }
            this.vd.synthesis_read(synthesis_pcmout);
            this.pcm_offset += synthesis_pcmout;
            if (synthesis_pcmout < n6 && this.process_packet(1) == 0) {
                this.pcm_offset = this.pcm_total(-1);
            }
        }
        return 0;
    }
    
    int time_seek(final float n) {
        long pcm_total = this.pcm_total(-1);
        float time_total = this.time_total(-1);
        if (!this.seekable) {
            return -1;
        }
        if (n < 0.0f || n > time_total) {
            this.pcm_offset = -1L;
            this.decode_clear();
            return -1;
        }
        int i;
        for (i = this.links - 1; i >= 0; --i) {
            pcm_total -= this.pcmlengths[i];
            time_total -= this.time_total(i);
            if (n >= time_total) {
                break;
            }
        }
        return this.pcm_seek((long)(pcm_total + (n - time_total) * this.vi[i].rate));
    }
    
    public long raw_tell() {
        return this.offset;
    }
    
    public long pcm_tell() {
        return this.pcm_offset;
    }
    
    public float time_tell() {
        int i = -1;
        long pcm_total = 0L;
        float time_total = 0.0f;
        if (this.seekable) {
            pcm_total = this.pcm_total(-1);
            time_total = this.time_total(-1);
            for (i = this.links - 1; i >= 0; --i) {
                pcm_total -= this.pcmlengths[i];
                time_total -= this.time_total(i);
                if (this.pcm_offset >= pcm_total) {
                    break;
                }
            }
        }
        return time_total + (this.pcm_offset - pcm_total) / (float)this.vi[i].rate;
    }
    
    public Info getInfo(final int n) {
        if (this.seekable) {
            if (n < 0) {
                if (this.decode_ready) {
                    return this.vi[this.current_link];
                }
                return null;
            }
            else {
                if (n >= this.links) {
                    return null;
                }
                return this.vi[n];
            }
        }
        else {
            if (this.decode_ready) {
                return this.vi[0];
            }
            return null;
        }
    }
    
    public Comment getComment(final int n) {
        if (this.seekable) {
            if (n < 0) {
                if (this.decode_ready) {
                    return this.vc[this.current_link];
                }
                return null;
            }
            else {
                if (n >= this.links) {
                    return null;
                }
                return this.vc[n];
            }
        }
        else {
            if (this.decode_ready) {
                return this.vc[0];
            }
            return null;
        }
    }
    
    int host_is_big_endian() {
        return 1;
    }
    
    int read(final byte[] array, final int n, final int n2, final int n3, final int n4, final int[] array2) {
        final int host_is_big_endian = this.host_is_big_endian();
        int n5 = 0;
        while (true) {
            if (this.decode_ready) {
                final float[][][] array3 = { null };
                final int[] array4 = new int[this.getInfo(-1).channels];
                int synthesis_pcmout = this.vd.synthesis_pcmout(array3, array4);
                final float[][] array5 = array3[0];
                if (synthesis_pcmout != 0) {
                    final int channels = this.getInfo(-1).channels;
                    final int n6 = n3 * channels;
                    if (synthesis_pcmout > n / n6) {
                        synthesis_pcmout = n / n6;
                    }
                    if (n3 == 1) {
                        final int n7 = (n4 != 0) ? 0 : 128;
                        for (int i = 0; i < synthesis_pcmout; ++i) {
                            for (int j = 0; j < channels; ++j) {
                                int n8 = (int)(array5[j][array4[j] + i] * 128.0 + 0.5);
                                if (n8 > 127) {
                                    n8 = 127;
                                }
                                else if (n8 < -128) {
                                    n8 = -128;
                                }
                                array[n5++] = (byte)(n8 + n7);
                            }
                        }
                    }
                    else {
                        final int n9 = (n4 != 0) ? 0 : 32768;
                        if (host_is_big_endian == n2) {
                            if (n4 != 0) {
                                for (int k = 0; k < channels; ++k) {
                                    final int n10 = array4[k];
                                    int n11 = k;
                                    for (int l = 0; l < synthesis_pcmout; ++l) {
                                        int n12 = (int)(array5[k][n10 + l] * 32768.0 + 0.5);
                                        if (n12 > 32767) {
                                            n12 = 32767;
                                        }
                                        else if (n12 < -32768) {
                                            n12 = -32768;
                                        }
                                        array[n11] = (byte)(n12 >>> 8);
                                        array[n11 + 1] = (byte)n12;
                                        n11 += channels * 2;
                                    }
                                }
                            }
                            else {
                                for (int n13 = 0; n13 < channels; ++n13) {
                                    final float[] array6 = array5[n13];
                                    int n14 = n13;
                                    for (int n15 = 0; n15 < synthesis_pcmout; ++n15) {
                                        int n16 = (int)(array6[n15] * 32768.0 + 0.5);
                                        if (n16 > 32767) {
                                            n16 = 32767;
                                        }
                                        else if (n16 < -32768) {
                                            n16 = -32768;
                                        }
                                        array[n14] = (byte)(n16 + n9 >>> 8);
                                        array[n14 + 1] = (byte)(n16 + n9);
                                        n14 += channels * 2;
                                    }
                                }
                            }
                        }
                        else if (n2 != 0) {
                            for (int n17 = 0; n17 < synthesis_pcmout; ++n17) {
                                for (int n18 = 0; n18 < channels; ++n18) {
                                    int n19 = (int)(array5[n18][n17] * 32768.0 + 0.5);
                                    if (n19 > 32767) {
                                        n19 = 32767;
                                    }
                                    else if (n19 < -32768) {
                                        n19 = -32768;
                                    }
                                    final int n20 = n19 + n9;
                                    array[n5++] = (byte)(n20 >>> 8);
                                    array[n5++] = (byte)n20;
                                }
                            }
                        }
                        else {
                            for (int n21 = 0; n21 < synthesis_pcmout; ++n21) {
                                for (int n22 = 0; n22 < channels; ++n22) {
                                    int n23 = (int)(array5[n22][n21] * 32768.0 + 0.5);
                                    if (n23 > 32767) {
                                        n23 = 32767;
                                    }
                                    else if (n23 < -32768) {
                                        n23 = -32768;
                                    }
                                    final int n24 = n23 + n9;
                                    array[n5++] = (byte)n24;
                                    array[n5++] = (byte)(n24 >>> 8);
                                }
                            }
                        }
                    }
                    this.vd.synthesis_read(synthesis_pcmout);
                    this.pcm_offset += synthesis_pcmout;
                    if (array2 != null) {
                        array2[0] = this.current_link;
                    }
                    return synthesis_pcmout * n6;
                }
            }
            switch (this.process_packet(1)) {
                case 0: {
                    return 0;
                }
                case -1: {
                    return -1;
                }
                default: {
                    continue;
                }
            }
        }
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
    
    class SeekableInputStream extends InputStream
    {
        RandomAccessFile raf;
        final String mode = "r";
        
        private SeekableInputStream() {
            this.raf = null;
        }
        
        SeekableInputStream(final String name) throws IOException {
            this.raf = null;
            this.raf = new RandomAccessFile(name, "r");
        }
        
        public int read() throws IOException {
            return this.raf.read();
        }
        
        public int read(final byte[] b) throws IOException {
            return this.raf.read(b);
        }
        
        public int read(final byte[] b, final int off, final int len) throws IOException {
            return this.raf.read(b, off, len);
        }
        
        public long skip(final long n) throws IOException {
            return this.raf.skipBytes((int)n);
        }
        
        public long getLength() throws IOException {
            return this.raf.length();
        }
        
        public long tell() throws IOException {
            return this.raf.getFilePointer();
        }
        
        public int available() throws IOException {
            return (this.raf.length() != this.raf.getFilePointer()) ? 1 : 0;
        }
        
        public void close() throws IOException {
            this.raf.close();
        }
        
        public synchronized void mark(final int n) {
        }
        
        public synchronized void reset() throws IOException {
        }
        
        public boolean markSupported() {
            return false;
        }
        
        public void seek(final long pos) throws IOException {
            this.raf.seek(pos);
        }
    }
}
