package com.sun.media.jfxmedia;

import com.sun.media.jfxmedia.events.MetadataListener;
import java.io.IOException;

public interface MetadataParser {
   String DURATION_TAG_NAME = "duration";
   String CREATIONDATE_TAG_NAME = "creationdate";
   String WIDTH_TAG_NAME = "width";
   String HEIGHT_TAG_NAME = "height";
   String FRAMERATE_TAG_NAME = "framerate";
   String VIDEOCODEC_TAG_NAME = "video codec";
   String AUDIOCODEC_TAG_NAME = "audio codec";
   String IMAGE_TAG_NAME = "image";
   String ALBUMARTIST_TAG_NAME = "album artist";
   String ALBUM_TAG_NAME = "album";
   String ARTIST_TAG_NAME = "artist";
   String COMMENT_TAG_NAME = "comment";
   String COMPOSER_TAG_NAME = "composer";
   String GENRE_TAG_NAME = "genre";
   String TITLE_TAG_NAME = "title";
   String TRACKNUMBER_TAG_NAME = "track number";
   String TRACKCOUNT_TAG_NAME = "track count";
   String DISCNUMBER_TAG_NAME = "disc number";
   String DISCCOUNT_TAG_NAME = "disc count";
   String YEAR_TAG_NAME = "year";
   String TEXT_TAG_NAME = "text";
   String RAW_METADATA_TAG_NAME = "raw metadata";
   String RAW_FLV_METADATA_NAME = "FLV";
   String RAW_ID3_METADATA_NAME = "ID3";

   void addListener(MetadataListener var1);

   void removeListener(MetadataListener var1);

   void startParser() throws IOException;

   void stopParser();
}
