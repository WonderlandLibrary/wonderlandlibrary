/*
 * Decompiled with CFR 0.152.
 */
package com.mpatric.mp3agic;

import com.mpatric.mp3agic.NotSupportedException;

public interface ID3v1 {
    public String getVersion();

    public String getTrack();

    public void setTrack(String var1);

    public String getArtist();

    public void setArtist(String var1);

    public String getTitle();

    public void setTitle(String var1);

    public String getAlbum();

    public void setAlbum(String var1);

    public String getYear();

    public void setYear(String var1);

    public int getGenre();

    public void setGenre(int var1);

    public String getGenreDescription();

    public String getComment();

    public void setComment(String var1);

    public byte[] toBytes() throws NotSupportedException;
}

