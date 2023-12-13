/*
 * Decompiled with CFR 0.152.
 */
package com.mpatric.mp3agic;

import com.mpatric.mp3agic.ID3v1;
import com.mpatric.mp3agic.ID3v2ChapterFrameData;
import com.mpatric.mp3agic.ID3v2ChapterTOCFrameData;
import com.mpatric.mp3agic.ID3v2FrameSet;
import java.util.ArrayList;
import java.util.Map;

public interface ID3v2
extends ID3v1 {
    public boolean getPadding();

    public void setPadding(boolean var1);

    public boolean hasFooter();

    public void setFooter(boolean var1);

    public boolean hasUnsynchronisation();

    public void setUnsynchronisation(boolean var1);

    public int getBPM();

    public void setBPM(int var1);

    public String getGrouping();

    public void setGrouping(String var1);

    public String getKey();

    public void setKey(String var1);

    public String getDate();

    public void setDate(String var1);

    public String getComposer();

    public void setComposer(String var1);

    public String getPublisher();

    public void setPublisher(String var1);

    public String getOriginalArtist();

    public void setOriginalArtist(String var1);

    public String getAlbumArtist();

    public void setAlbumArtist(String var1);

    public String getCopyright();

    public void setCopyright(String var1);

    public String getArtistUrl();

    public void setArtistUrl(String var1);

    public String getCommercialUrl();

    public void setCommercialUrl(String var1);

    public String getCopyrightUrl();

    public void setCopyrightUrl(String var1);

    public String getAudiofileUrl();

    public void setAudiofileUrl(String var1);

    public String getAudioSourceUrl();

    public void setAudioSourceUrl(String var1);

    public String getRadiostationUrl();

    public void setRadiostationUrl(String var1);

    public String getPaymentUrl();

    public void setPaymentUrl(String var1);

    public String getPublisherUrl();

    public void setPublisherUrl(String var1);

    public String getUrl();

    public void setUrl(String var1);

    public String getPartOfSet();

    public void setPartOfSet(String var1);

    public boolean isCompilation();

    public void setCompilation(boolean var1);

    public ArrayList<ID3v2ChapterFrameData> getChapters();

    public void setChapters(ArrayList<ID3v2ChapterFrameData> var1);

    public ArrayList<ID3v2ChapterTOCFrameData> getChapterTOC();

    public void setChapterTOC(ArrayList<ID3v2ChapterTOCFrameData> var1);

    public String getEncoder();

    public void setEncoder(String var1);

    public byte[] getAlbumImage();

    public void setAlbumImage(byte[] var1, String var2);

    public void setAlbumImage(byte[] var1, String var2, byte var3, String var4);

    public void clearAlbumImage();

    public String getAlbumImageMimeType();

    public int getWmpRating();

    public void setWmpRating(int var1);

    public String getItunesComment();

    public void setItunesComment(String var1);

    public String getLyrics();

    public void setLyrics(String var1);

    public void setGenreDescription(String var1);

    public int getDataLength();

    public int getLength();

    public boolean getObseleteFormat();

    public Map<String, ID3v2FrameSet> getFrameSets();

    public void clearFrameSet(String var1);
}

