/*
 * Decompiled with CFR 0.152.
 */
package com.mpatric.mp3agic;

import com.mpatric.mp3agic.ID3v1;
import com.mpatric.mp3agic.ID3v2;

public class ID3Wrapper {
    private final ID3v1 id3v1Tag;
    private final ID3v2 id3v2Tag;

    public ID3Wrapper(ID3v1 iD3v1, ID3v2 iD3v2) {
        this.id3v1Tag = iD3v1;
        this.id3v2Tag = iD3v2;
    }

    public ID3v1 getId3v1Tag() {
        return this.id3v1Tag;
    }

    public ID3v2 getId3v2Tag() {
        return this.id3v2Tag;
    }

    public String getTrack() {
        if (this.id3v2Tag != null && this.id3v2Tag.getTrack() != null && this.id3v2Tag.getTrack().length() > 0) {
            return this.id3v2Tag.getTrack();
        }
        if (this.id3v1Tag != null) {
            return this.id3v1Tag.getTrack();
        }
        return null;
    }

    public void setTrack(String string) {
        if (this.id3v2Tag != null) {
            this.id3v2Tag.setTrack(string);
        }
        if (this.id3v1Tag != null) {
            this.id3v1Tag.setTrack(string);
        }
    }

    public String getArtist() {
        if (this.id3v2Tag != null && this.id3v2Tag.getArtist() != null && this.id3v2Tag.getArtist().length() > 0) {
            return this.id3v2Tag.getArtist();
        }
        if (this.id3v1Tag != null) {
            return this.id3v1Tag.getArtist();
        }
        return null;
    }

    public void setArtist(String string) {
        if (this.id3v2Tag != null) {
            this.id3v2Tag.setArtist(string);
        }
        if (this.id3v1Tag != null) {
            this.id3v1Tag.setArtist(string);
        }
    }

    public String getTitle() {
        if (this.id3v2Tag != null && this.id3v2Tag.getTitle() != null && this.id3v2Tag.getTitle().length() > 0) {
            return this.id3v2Tag.getTitle();
        }
        if (this.id3v1Tag != null) {
            return this.id3v1Tag.getTitle();
        }
        return null;
    }

    public void setTitle(String string) {
        if (this.id3v2Tag != null) {
            this.id3v2Tag.setTitle(string);
        }
        if (this.id3v1Tag != null) {
            this.id3v1Tag.setTitle(string);
        }
    }

    public String getAlbum() {
        if (this.id3v2Tag != null && this.id3v2Tag.getAlbum() != null && this.id3v2Tag.getAlbum().length() > 0) {
            return this.id3v2Tag.getAlbum();
        }
        if (this.id3v1Tag != null) {
            return this.id3v1Tag.getAlbum();
        }
        return null;
    }

    public void setAlbum(String string) {
        if (this.id3v2Tag != null) {
            this.id3v2Tag.setAlbum(string);
        }
        if (this.id3v1Tag != null) {
            this.id3v1Tag.setAlbum(string);
        }
    }

    public String getYear() {
        if (this.id3v2Tag != null && this.id3v2Tag.getYear() != null && this.id3v2Tag.getYear().length() > 0) {
            return this.id3v2Tag.getYear();
        }
        if (this.id3v1Tag != null) {
            return this.id3v1Tag.getYear();
        }
        return null;
    }

    public void setYear(String string) {
        if (this.id3v2Tag != null) {
            this.id3v2Tag.setYear(string);
        }
        if (this.id3v1Tag != null) {
            this.id3v1Tag.setYear(string);
        }
    }

    public int getGenre() {
        if (this.id3v2Tag != null && this.id3v2Tag.getGenre() != -1) {
            return this.id3v2Tag.getGenre();
        }
        if (this.id3v1Tag != null) {
            return this.id3v1Tag.getGenre();
        }
        return -1;
    }

    public void setGenre(int n2) {
        if (this.id3v2Tag != null) {
            this.id3v2Tag.setGenre(n2);
        }
        if (this.id3v1Tag != null) {
            this.id3v1Tag.setGenre(n2);
        }
    }

    public String getGenreDescription() {
        if (this.id3v2Tag != null) {
            return this.id3v2Tag.getGenreDescription();
        }
        if (this.id3v1Tag != null) {
            return this.id3v1Tag.getGenreDescription();
        }
        return null;
    }

    public String getComment() {
        if (this.id3v2Tag != null && this.id3v2Tag.getComment() != null && this.id3v2Tag.getComment().length() > 0) {
            return this.id3v2Tag.getComment();
        }
        if (this.id3v1Tag != null) {
            return this.id3v1Tag.getComment();
        }
        return null;
    }

    public void setComment(String string) {
        if (this.id3v2Tag != null) {
            this.id3v2Tag.setComment(string);
        }
        if (this.id3v1Tag != null) {
            this.id3v1Tag.setComment(string);
        }
    }

    public String getComposer() {
        if (this.id3v2Tag != null) {
            return this.id3v2Tag.getComposer();
        }
        return null;
    }

    public void setComposer(String string) {
        if (this.id3v2Tag != null) {
            this.id3v2Tag.setComposer(string);
        }
    }

    public String getOriginalArtist() {
        if (this.id3v2Tag != null) {
            return this.id3v2Tag.getOriginalArtist();
        }
        return null;
    }

    public void setOriginalArtist(String string) {
        if (this.id3v2Tag != null) {
            this.id3v2Tag.setOriginalArtist(string);
        }
    }

    public void setAlbumArtist(String string) {
        if (this.id3v2Tag != null) {
            this.id3v2Tag.setAlbumArtist(string);
        }
    }

    public String getAlbumArtist() {
        if (this.id3v2Tag != null) {
            return this.id3v2Tag.getAlbumArtist();
        }
        return null;
    }

    public String getCopyright() {
        if (this.id3v2Tag != null) {
            return this.id3v2Tag.getCopyright();
        }
        return null;
    }

    public void setCopyright(String string) {
        if (this.id3v2Tag != null) {
            this.id3v2Tag.setCopyright(string);
        }
    }

    public String getUrl() {
        if (this.id3v2Tag != null) {
            return this.id3v2Tag.getUrl();
        }
        return null;
    }

    public void setUrl(String string) {
        if (this.id3v2Tag != null) {
            this.id3v2Tag.setUrl(string);
        }
    }

    public String getEncoder() {
        if (this.id3v2Tag != null) {
            return this.id3v2Tag.getEncoder();
        }
        return null;
    }

    public void setEncoder(String string) {
        if (this.id3v2Tag != null) {
            this.id3v2Tag.setEncoder(string);
        }
    }

    public byte[] getAlbumImage() {
        if (this.id3v2Tag != null) {
            return this.id3v2Tag.getAlbumImage();
        }
        return null;
    }

    public void setAlbumImage(byte[] byArray, String string) {
        if (this.id3v2Tag != null) {
            this.id3v2Tag.setAlbumImage(byArray, string);
        }
    }

    public String getAlbumImageMimeType() {
        if (this.id3v2Tag != null) {
            return this.id3v2Tag.getAlbumImageMimeType();
        }
        return null;
    }

    public void setLyrics(String string) {
        if (this.id3v2Tag != null) {
            this.id3v2Tag.setLyrics(string);
        }
    }

    public String getLyrics() {
        if (this.id3v2Tag != null) {
            return this.id3v2Tag.getLyrics();
        }
        return null;
    }

    public void clearComment() {
        if (this.id3v2Tag != null) {
            this.id3v2Tag.clearFrameSet("COMM");
        }
        if (this.id3v1Tag != null) {
            this.id3v1Tag.setComment(null);
        }
    }

    public void clearCopyright() {
        if (this.id3v2Tag != null) {
            this.id3v2Tag.clearFrameSet("TCOP");
        }
    }

    public void clearEncoder() {
        if (this.id3v2Tag != null) {
            this.id3v2Tag.clearFrameSet("TENC");
        }
    }
}

