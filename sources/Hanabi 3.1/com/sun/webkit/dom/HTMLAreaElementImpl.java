package com.sun.webkit.dom;

import org.w3c.dom.html.HTMLAreaElement;

public class HTMLAreaElementImpl extends HTMLElementImpl implements HTMLAreaElement {
   HTMLAreaElementImpl(long var1) {
      super(var1);
   }

   static HTMLAreaElement getImpl(long var0) {
      return (HTMLAreaElement)create(var0);
   }

   public String getAlt() {
      return getAltImpl(this.getPeer());
   }

   static native String getAltImpl(long var0);

   public void setAlt(String var1) {
      setAltImpl(this.getPeer(), var1);
   }

   static native void setAltImpl(long var0, String var2);

   public String getCoords() {
      return getCoordsImpl(this.getPeer());
   }

   static native String getCoordsImpl(long var0);

   public void setCoords(String var1) {
      setCoordsImpl(this.getPeer(), var1);
   }

   static native void setCoordsImpl(long var0, String var2);

   public boolean getNoHref() {
      return getNoHrefImpl(this.getPeer());
   }

   static native boolean getNoHrefImpl(long var0);

   public void setNoHref(boolean var1) {
      setNoHrefImpl(this.getPeer(), var1);
   }

   static native void setNoHrefImpl(long var0, boolean var2);

   public String getPing() {
      return getPingImpl(this.getPeer());
   }

   static native String getPingImpl(long var0);

   public void setPing(String var1) {
      setPingImpl(this.getPeer(), var1);
   }

   static native void setPingImpl(long var0, String var2);

   public String getRel() {
      return getRelImpl(this.getPeer());
   }

   static native String getRelImpl(long var0);

   public void setRel(String var1) {
      setRelImpl(this.getPeer(), var1);
   }

   static native void setRelImpl(long var0, String var2);

   public String getShape() {
      return getShapeImpl(this.getPeer());
   }

   static native String getShapeImpl(long var0);

   public void setShape(String var1) {
      setShapeImpl(this.getPeer(), var1);
   }

   static native void setShapeImpl(long var0, String var2);

   public String getTarget() {
      return getTargetImpl(this.getPeer());
   }

   static native String getTargetImpl(long var0);

   public void setTarget(String var1) {
      setTargetImpl(this.getPeer(), var1);
   }

   static native void setTargetImpl(long var0, String var2);

   public String getAccessKey() {
      return getAccessKeyImpl(this.getPeer());
   }

   static native String getAccessKeyImpl(long var0);

   public void setAccessKey(String var1) {
      setAccessKeyImpl(this.getPeer(), var1);
   }

   static native void setAccessKeyImpl(long var0, String var2);

   public String getHref() {
      return getHrefImpl(this.getPeer());
   }

   static native String getHrefImpl(long var0);

   public void setHref(String var1) {
      setHrefImpl(this.getPeer(), var1);
   }

   static native void setHrefImpl(long var0, String var2);

   public String getOrigin() {
      return getOriginImpl(this.getPeer());
   }

   static native String getOriginImpl(long var0);

   public String getProtocol() {
      return getProtocolImpl(this.getPeer());
   }

   static native String getProtocolImpl(long var0);

   public void setProtocol(String var1) {
      setProtocolImpl(this.getPeer(), var1);
   }

   static native void setProtocolImpl(long var0, String var2);

   public String getUsername() {
      return getUsernameImpl(this.getPeer());
   }

   static native String getUsernameImpl(long var0);

   public void setUsername(String var1) {
      setUsernameImpl(this.getPeer(), var1);
   }

   static native void setUsernameImpl(long var0, String var2);

   public String getPassword() {
      return getPasswordImpl(this.getPeer());
   }

   static native String getPasswordImpl(long var0);

   public void setPassword(String var1) {
      setPasswordImpl(this.getPeer(), var1);
   }

   static native void setPasswordImpl(long var0, String var2);

   public String getHost() {
      return getHostImpl(this.getPeer());
   }

   static native String getHostImpl(long var0);

   public void setHost(String var1) {
      setHostImpl(this.getPeer(), var1);
   }

   static native void setHostImpl(long var0, String var2);

   public String getHostname() {
      return getHostnameImpl(this.getPeer());
   }

   static native String getHostnameImpl(long var0);

   public void setHostname(String var1) {
      setHostnameImpl(this.getPeer(), var1);
   }

   static native void setHostnameImpl(long var0, String var2);

   public String getPort() {
      return getPortImpl(this.getPeer());
   }

   static native String getPortImpl(long var0);

   public void setPort(String var1) {
      setPortImpl(this.getPeer(), var1);
   }

   static native void setPortImpl(long var0, String var2);

   public String getPathname() {
      return getPathnameImpl(this.getPeer());
   }

   static native String getPathnameImpl(long var0);

   public void setPathname(String var1) {
      setPathnameImpl(this.getPeer(), var1);
   }

   static native void setPathnameImpl(long var0, String var2);

   public String getSearch() {
      return getSearchImpl(this.getPeer());
   }

   static native String getSearchImpl(long var0);

   public void setSearch(String var1) {
      setSearchImpl(this.getPeer(), var1);
   }

   static native void setSearchImpl(long var0, String var2);

   public String getHash() {
      return getHashImpl(this.getPeer());
   }

   static native String getHashImpl(long var0);

   public void setHash(String var1) {
      setHashImpl(this.getPeer(), var1);
   }

   static native void setHashImpl(long var0, String var2);
}
