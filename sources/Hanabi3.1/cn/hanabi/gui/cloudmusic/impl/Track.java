package cn.hanabi.gui.cloudmusic.impl;

import aLph4anTi1eaK_cN.Annotation.ObfuscationClass;

@ObfuscationClass
public class Track {
   public long id;
   public String name;
   public String artists;
   public String picUrl;

   public Track(long id, String name, String artists, String picUrl) {
      this.id = id;
      this.name = name;
      this.artists = artists;
      this.picUrl = picUrl;
   }
}
