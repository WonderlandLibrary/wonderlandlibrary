package com.sun.webkit.dom;

import org.w3c.dom.html.HTMLCollection;
import org.w3c.dom.html.HTMLDocument;

public class HTMLDocumentImpl extends DocumentImpl implements HTMLDocument {
   HTMLDocumentImpl(long var1) {
      super(var1);
   }

   static HTMLDocument getImpl(long var0) {
      return (HTMLDocument)create(var0);
   }

   public HTMLCollection getEmbeds() {
      return HTMLCollectionImpl.getImpl(getEmbedsImpl(this.getPeer()));
   }

   static native long getEmbedsImpl(long var0);

   public HTMLCollection getPlugins() {
      return HTMLCollectionImpl.getImpl(getPluginsImpl(this.getPeer()));
   }

   static native long getPluginsImpl(long var0);

   public HTMLCollection getScripts() {
      return HTMLCollectionImpl.getImpl(getScriptsImpl(this.getPeer()));
   }

   static native long getScriptsImpl(long var0);

   public int getWidth() {
      return getWidthImpl(this.getPeer());
   }

   static native int getWidthImpl(long var0);

   public int getHeight() {
      return getHeightImpl(this.getPeer());
   }

   static native int getHeightImpl(long var0);

   public String getDir() {
      return getDirImpl(this.getPeer());
   }

   static native String getDirImpl(long var0);

   public void setDir(String var1) {
      setDirImpl(this.getPeer(), var1);
   }

   static native void setDirImpl(long var0, String var2);

   public String getDesignMode() {
      return getDesignModeImpl(this.getPeer());
   }

   static native String getDesignModeImpl(long var0);

   public void setDesignMode(String var1) {
      setDesignModeImpl(this.getPeer(), var1);
   }

   static native void setDesignModeImpl(long var0, String var2);

   public String getCompatMode() {
      return getCompatModeImpl(this.getPeer());
   }

   static native String getCompatModeImpl(long var0);

   public String getBgColor() {
      return getBgColorImpl(this.getPeer());
   }

   static native String getBgColorImpl(long var0);

   public void setBgColor(String var1) {
      setBgColorImpl(this.getPeer(), var1);
   }

   static native void setBgColorImpl(long var0, String var2);

   public String getFgColor() {
      return getFgColorImpl(this.getPeer());
   }

   static native String getFgColorImpl(long var0);

   public void setFgColor(String var1) {
      setFgColorImpl(this.getPeer(), var1);
   }

   static native void setFgColorImpl(long var0, String var2);

   public String getAlinkColor() {
      return getAlinkColorImpl(this.getPeer());
   }

   static native String getAlinkColorImpl(long var0);

   public void setAlinkColor(String var1) {
      setAlinkColorImpl(this.getPeer(), var1);
   }

   static native void setAlinkColorImpl(long var0, String var2);

   public String getLinkColor() {
      return getLinkColorImpl(this.getPeer());
   }

   static native String getLinkColorImpl(long var0);

   public void setLinkColor(String var1) {
      setLinkColorImpl(this.getPeer(), var1);
   }

   static native void setLinkColorImpl(long var0, String var2);

   public String getVlinkColor() {
      return getVlinkColorImpl(this.getPeer());
   }

   static native String getVlinkColorImpl(long var0);

   public void setVlinkColor(String var1) {
      setVlinkColorImpl(this.getPeer(), var1);
   }

   static native void setVlinkColorImpl(long var0, String var2);

   public void open() {
      openImpl(this.getPeer());
   }

   static native void openImpl(long var0);

   public void close() {
      closeImpl(this.getPeer());
   }

   static native void closeImpl(long var0);

   public void write(String var1) {
      writeImpl(this.getPeer(), var1);
   }

   static native void writeImpl(long var0, String var2);

   public void writeln(String var1) {
      writelnImpl(this.getPeer(), var1);
   }

   static native void writelnImpl(long var0, String var2);

   public void clear() {
      clearImpl(this.getPeer());
   }

   static native void clearImpl(long var0);

   public void captureEvents() {
      captureEventsImpl(this.getPeer());
   }

   static native void captureEventsImpl(long var0);

   public void releaseEvents() {
      releaseEventsImpl(this.getPeer());
   }

   static native void releaseEventsImpl(long var0);
}
