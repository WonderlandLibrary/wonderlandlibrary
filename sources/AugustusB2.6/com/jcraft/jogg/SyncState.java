// 
// Decompiled by Procyon v0.5.36
// 

package com.jcraft.jogg;

public class SyncState
{
    public byte[] data;
    int storage;
    int fill;
    int returned;
    int unsynced;
    int headerbytes;
    int bodybytes;
    private Page pageseek;
    private byte[] chksum;
    
    public SyncState() {
        this.pageseek = new Page();
        this.chksum = new byte[4];
    }
    
    public int clear() {
        this.data = null;
        return 0;
    }
    
    public int buffer(final int n) {
        if (this.returned != 0) {
            this.fill -= this.returned;
            if (this.fill > 0) {
                System.arraycopy(this.data, this.returned, this.data, 0, this.fill);
            }
            this.returned = 0;
        }
        if (n > this.storage - this.fill) {
            final int storage = n + this.fill + 4096;
            if (this.data != null) {
                final byte[] data = new byte[storage];
                System.arraycopy(this.data, 0, data, 0, this.data.length);
                this.data = data;
            }
            else {
                this.data = new byte[storage];
            }
            this.storage = storage;
        }
        return this.fill;
    }
    
    public int wrote(final int n) {
        if (this.fill + n > this.storage) {
            return -1;
        }
        this.fill += n;
        return 0;
    }
    
    public int pageseek(final Page page) {
        final int returned = this.returned;
        final int n = this.fill - this.returned;
        if (this.headerbytes == 0) {
            if (n < 27) {
                return 0;
            }
            if (this.data[returned] != 79 || this.data[returned + 1] != 103 || this.data[returned + 2] != 103 || this.data[returned + 3] != 83) {
                this.headerbytes = 0;
                this.bodybytes = 0;
                int fill = 0;
                for (int i = 0; i < n - 1; ++i) {
                    if (this.data[returned + 1 + i] == 79) {
                        fill = returned + 1 + i;
                        break;
                    }
                }
                if (fill == 0) {
                    fill = this.fill;
                }
                return -((this.returned = fill) - returned);
            }
            final int headerbytes = (this.data[returned + 26] & 0xFF) + 27;
            if (n < headerbytes) {
                return 0;
            }
            for (int j = 0; j < (this.data[returned + 26] & 0xFF); ++j) {
                this.bodybytes += (this.data[returned + 27 + j] & 0xFF);
            }
            this.headerbytes = headerbytes;
        }
        if (this.bodybytes + this.headerbytes > n) {
            return 0;
        }
        synchronized (this.chksum) {
            System.arraycopy(this.data, returned + 22, this.chksum, 0, 4);
            this.data[returned + 22] = 0;
            this.data[returned + 23] = 0;
            this.data[returned + 24] = 0;
            this.data[returned + 25] = 0;
            final Page pageseek = this.pageseek;
            pageseek.header_base = this.data;
            pageseek.header = returned;
            pageseek.header_len = this.headerbytes;
            pageseek.body_base = this.data;
            pageseek.body = returned + this.headerbytes;
            pageseek.body_len = this.bodybytes;
            pageseek.checksum();
            if (this.chksum[0] != this.data[returned + 22] || this.chksum[1] != this.data[returned + 23] || this.chksum[2] != this.data[returned + 24] || this.chksum[3] != this.data[returned + 25]) {
                System.arraycopy(this.chksum, 0, this.data, returned + 22, 4);
                this.headerbytes = 0;
                this.bodybytes = 0;
                int fill2 = 0;
                for (int k = 0; k < n - 1; ++k) {
                    if (this.data[returned + 1 + k] == 79) {
                        fill2 = returned + 1 + k;
                        break;
                    }
                }
                if (fill2 == 0) {
                    fill2 = this.fill;
                }
                return -((this.returned = fill2) - returned);
            }
        }
        final int returned2 = this.returned;
        if (page != null) {
            page.header_base = this.data;
            page.header = returned2;
            page.header_len = this.headerbytes;
            page.body_base = this.data;
            page.body = returned2 + this.headerbytes;
            page.body_len = this.bodybytes;
        }
        this.unsynced = 0;
        final int n2;
        this.returned += (n2 = this.headerbytes + this.bodybytes);
        this.headerbytes = 0;
        this.bodybytes = 0;
        return n2;
    }
    
    public int pageout(final Page page) {
        do {
            final int pageseek = this.pageseek(page);
            if (pageseek > 0) {
                return 1;
            }
            if (pageseek == 0) {
                return 0;
            }
        } while (this.unsynced != 0);
        this.unsynced = 1;
        return -1;
    }
    
    public int reset() {
        this.fill = 0;
        this.returned = 0;
        this.unsynced = 0;
        this.headerbytes = 0;
        return this.bodybytes = 0;
    }
    
    public void init() {
    }
    
    public int getDataOffset() {
        return this.returned;
    }
    
    public int getBufferOffset() {
        return this.fill;
    }
}
