package com.sun.webkit.dom;

import com.sun.webkit.Disposer;
import com.sun.webkit.DisposerRecord;
import org.w3c.dom.DOMException;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.css.CSSStyleSheet;
import org.w3c.dom.html.HTMLDocument;

public class DOMImplementationImpl implements DOMImplementation {
   private final long peer;

   DOMImplementationImpl(long var1) {
      this.peer = var1;
      Disposer.addRecord(this, new SelfDisposer(var1));
   }

   static DOMImplementation create(long var0) {
      return var0 == 0L ? null : new DOMImplementationImpl(var0);
   }

   long getPeer() {
      return this.peer;
   }

   public boolean equals(Object var1) {
      return var1 instanceof DOMImplementationImpl && this.peer == ((DOMImplementationImpl)var1).peer;
   }

   public int hashCode() {
      long var1 = this.peer;
      return (int)(var1 ^ var1 >> 17);
   }

   static long getPeer(DOMImplementation var0) {
      return var0 == null ? 0L : ((DOMImplementationImpl)var0).getPeer();
   }

   private static native void dispose(long var0);

   static DOMImplementation getImpl(long var0) {
      return create(var0);
   }

   public boolean hasFeature(String var1, String var2) {
      return hasFeatureImpl(this.getPeer(), var1, var2);
   }

   static native boolean hasFeatureImpl(long var0, String var2, String var3);

   public DocumentType createDocumentType(String var1, String var2, String var3) throws DOMException {
      return DocumentTypeImpl.getImpl(createDocumentTypeImpl(this.getPeer(), var1, var2, var3));
   }

   static native long createDocumentTypeImpl(long var0, String var2, String var3, String var4);

   public Document createDocument(String var1, String var2, DocumentType var3) throws DOMException {
      return DocumentImpl.getImpl(createDocumentImpl(this.getPeer(), var1, var2, DocumentTypeImpl.getPeer(var3)));
   }

   static native long createDocumentImpl(long var0, String var2, String var3, long var4);

   public CSSStyleSheet createCSSStyleSheet(String var1, String var2) throws DOMException {
      return CSSStyleSheetImpl.getImpl(createCSSStyleSheetImpl(this.getPeer(), var1, var2));
   }

   static native long createCSSStyleSheetImpl(long var0, String var2, String var3);

   public HTMLDocument createHTMLDocument(String var1) {
      return HTMLDocumentImpl.getImpl(createHTMLDocumentImpl(this.getPeer(), var1));
   }

   static native long createHTMLDocumentImpl(long var0, String var2);

   public Object getFeature(String var1, String var2) {
      throw new UnsupportedOperationException("Not supported yet.");
   }

   private static class SelfDisposer implements DisposerRecord {
      private final long peer;

      SelfDisposer(long var1) {
         this.peer = var1;
      }

      public void dispose() {
         DOMImplementationImpl.dispose(this.peer);
      }
   }
}
