// 
// Decompiled by Procyon v0.5.36
// 

package com.jcraft.jorbis;

import com.jcraft.jogg.Packet;
import com.jcraft.jogg.Buffer;

public class Comment
{
    private static byte[] _vorbis;
    private static final int OV_EFAULT = -129;
    private static final int OV_EIMPL = -130;
    public byte[][] user_comments;
    public int[] comment_lengths;
    public int comments;
    public byte[] vendor;
    
    public void init() {
        this.user_comments = null;
        this.comments = 0;
        this.vendor = null;
    }
    
    public void add(final String s) {
        this.add(s.getBytes());
    }
    
    private void add(final byte[] array) {
        final byte[][] user_comments = new byte[this.comments + 2][];
        if (this.user_comments != null) {
            System.arraycopy(this.user_comments, 0, user_comments, 0, this.comments);
        }
        this.user_comments = user_comments;
        final int[] comment_lengths = new int[this.comments + 2];
        if (this.comment_lengths != null) {
            System.arraycopy(this.comment_lengths, 0, comment_lengths, 0, this.comments);
        }
        this.comment_lengths = comment_lengths;
        final byte[] array2 = new byte[array.length + 1];
        System.arraycopy(array, 0, array2, 0, array.length);
        this.user_comments[this.comments] = array2;
        this.comment_lengths[this.comments] = array.length;
        ++this.comments;
        this.user_comments[this.comments] = null;
    }
    
    public void add_tag(final String str, String str2) {
        if (str2 == null) {
            str2 = "";
        }
        this.add(str + "=" + str2);
    }
    
    static boolean tagcompare(final byte[] array, final byte[] array2, final int n) {
        for (int i = 0; i < n; ++i) {
            byte b = array[i];
            byte b2 = array2[i];
            if (90 >= b && b >= 65) {
                b = (byte)(b - 65 + 97);
            }
            if (90 >= b2 && b2 >= 65) {
                b2 = (byte)(b2 - 65 + 97);
            }
            if (b != b2) {
                return false;
            }
        }
        return true;
    }
    
    public String query(final String s) {
        return this.query(s, 0);
    }
    
    public String query(final String s, final int n) {
        final int query = this.query(s.getBytes(), n);
        if (query == -1) {
            return null;
        }
        final byte[] bytes = this.user_comments[query];
        for (int i = 0; i < this.comment_lengths[query]; ++i) {
            if (bytes[i] == 61) {
                return new String(bytes, i + 1, this.comment_lengths[query] - (i + 1));
            }
        }
        return null;
    }
    
    private int query(final byte[] array, final int n) {
        int n2 = 0;
        final int n3 = array.length + 1;
        final byte[] array2 = new byte[n3];
        System.arraycopy(array, 0, array2, 0, array.length);
        array2[array.length] = 61;
        for (int i = 0; i < this.comments; ++i) {
            if (tagcompare(this.user_comments[i], array2, n3)) {
                if (n == n2) {
                    return i;
                }
                ++n2;
            }
        }
        return -1;
    }
    
    int unpack(final Buffer buffer) {
        final int read = buffer.read(32);
        if (read < 0) {
            this.clear();
            return -1;
        }
        buffer.read(this.vendor = new byte[read + 1], read);
        this.comments = buffer.read(32);
        if (this.comments < 0) {
            this.clear();
            return -1;
        }
        this.user_comments = new byte[this.comments + 1][];
        this.comment_lengths = new int[this.comments + 1];
        for (int i = 0; i < this.comments; ++i) {
            final int read2 = buffer.read(32);
            if (read2 < 0) {
                this.clear();
                return -1;
            }
            this.comment_lengths[i] = read2;
            buffer.read(this.user_comments[i] = new byte[read2 + 1], read2);
        }
        if (buffer.read(1) != 1) {
            this.clear();
            return -1;
        }
        return 0;
    }
    
    int pack(final Buffer buffer) {
        final byte[] bytes = "Xiphophorus libVorbis I 20000508".getBytes();
        buffer.write(3, 8);
        buffer.write(Comment._vorbis);
        buffer.write(bytes.length, 32);
        buffer.write(bytes);
        buffer.write(this.comments, 32);
        if (this.comments != 0) {
            for (int i = 0; i < this.comments; ++i) {
                if (this.user_comments[i] != null) {
                    buffer.write(this.comment_lengths[i], 32);
                    buffer.write(this.user_comments[i]);
                }
                else {
                    buffer.write(0, 32);
                }
            }
        }
        buffer.write(1, 1);
        return 0;
    }
    
    public int header_out(final Packet packet) {
        final Buffer buffer = new Buffer();
        buffer.writeinit();
        if (this.pack(buffer) != 0) {
            return -130;
        }
        packet.packet_base = new byte[buffer.bytes()];
        packet.packet = 0;
        packet.bytes = buffer.bytes();
        System.arraycopy(buffer.buffer(), 0, packet.packet_base, 0, packet.bytes);
        packet.b_o_s = 0;
        packet.e_o_s = 0;
        packet.granulepos = 0L;
        return 0;
    }
    
    void clear() {
        for (int i = 0; i < this.comments; ++i) {
            this.user_comments[i] = null;
        }
        this.user_comments = null;
        this.vendor = null;
    }
    
    public String getVendor() {
        return new String(this.vendor, 0, this.vendor.length - 1);
    }
    
    public String getComment(final int n) {
        if (this.comments <= n) {
            return null;
        }
        return new String(this.user_comments[n], 0, this.user_comments[n].length - 1);
    }
    
    public String toString() {
        String s = "Vendor: " + new String(this.vendor, 0, this.vendor.length - 1);
        for (int i = 0; i < this.comments; ++i) {
            s = s + "\nComment: " + new String(this.user_comments[i], 0, this.user_comments[i].length - 1);
        }
        return s + "\n";
    }
    
    static {
        Comment._vorbis = "vorbis".getBytes();
    }
}
