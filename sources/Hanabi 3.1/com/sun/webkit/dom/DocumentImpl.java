package com.sun.webkit.dom;

import org.w3c.dom.Attr;
import org.w3c.dom.CDATASection;
import org.w3c.dom.Comment;
import org.w3c.dom.DOMConfiguration;
import org.w3c.dom.DOMException;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.w3c.dom.EntityReference;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.ProcessingInstruction;
import org.w3c.dom.Text;
import org.w3c.dom.css.CSSStyleDeclaration;
import org.w3c.dom.events.DocumentEvent;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.html.HTMLCollection;
import org.w3c.dom.html.HTMLElement;
import org.w3c.dom.html.HTMLHeadElement;
import org.w3c.dom.html.HTMLScriptElement;
import org.w3c.dom.ranges.Range;
import org.w3c.dom.stylesheets.StyleSheetList;
import org.w3c.dom.traversal.NodeFilter;
import org.w3c.dom.traversal.NodeIterator;
import org.w3c.dom.traversal.TreeWalker;
import org.w3c.dom.views.AbstractView;
import org.w3c.dom.views.DocumentView;
import org.w3c.dom.xpath.XPathEvaluator;
import org.w3c.dom.xpath.XPathExpression;
import org.w3c.dom.xpath.XPathNSResolver;
import org.w3c.dom.xpath.XPathResult;

public class DocumentImpl extends NodeImpl implements Document, XPathEvaluator, DocumentView, DocumentEvent {
   DocumentImpl(long var1) {
      super(var1);
   }

   static Document getImpl(long var0) {
      return (Document)create(var0);
   }

   static native boolean isHTMLDocumentImpl(long var0);

   public Object evaluate(String var1, Node var2, XPathNSResolver var3, short var4, Object var5) throws DOMException {
      return this.evaluate(var1, var2, var3, var4, (XPathResult)var5);
   }

   public DocumentType getDoctype() {
      return DocumentTypeImpl.getImpl(getDoctypeImpl(this.getPeer()));
   }

   static native long getDoctypeImpl(long var0);

   public DOMImplementation getImplementation() {
      return DOMImplementationImpl.getImpl(getImplementationImpl(this.getPeer()));
   }

   static native long getImplementationImpl(long var0);

   public Element getDocumentElement() {
      return ElementImpl.getImpl(getDocumentElementImpl(this.getPeer()));
   }

   static native long getDocumentElementImpl(long var0);

   public String getInputEncoding() {
      return getInputEncodingImpl(this.getPeer());
   }

   static native String getInputEncodingImpl(long var0);

   public String getXmlEncoding() {
      return getXmlEncodingImpl(this.getPeer());
   }

   static native String getXmlEncodingImpl(long var0);

   public String getXmlVersion() {
      return getXmlVersionImpl(this.getPeer());
   }

   static native String getXmlVersionImpl(long var0);

   public void setXmlVersion(String var1) throws DOMException {
      setXmlVersionImpl(this.getPeer(), var1);
   }

   static native void setXmlVersionImpl(long var0, String var2);

   public boolean getXmlStandalone() {
      return getXmlStandaloneImpl(this.getPeer());
   }

   static native boolean getXmlStandaloneImpl(long var0);

   public void setXmlStandalone(boolean var1) throws DOMException {
      setXmlStandaloneImpl(this.getPeer(), var1);
   }

   static native void setXmlStandaloneImpl(long var0, boolean var2);

   public String getDocumentURI() {
      return getDocumentURIImpl(this.getPeer());
   }

   static native String getDocumentURIImpl(long var0);

   public void setDocumentURI(String var1) {
      setDocumentURIImpl(this.getPeer(), var1);
   }

   static native void setDocumentURIImpl(long var0, String var2);

   public AbstractView getDefaultView() {
      return DOMWindowImpl.getImpl(getDefaultViewImpl(this.getPeer()));
   }

   static native long getDefaultViewImpl(long var0);

   public StyleSheetList getStyleSheets() {
      return StyleSheetListImpl.getImpl(getStyleSheetsImpl(this.getPeer()));
   }

   static native long getStyleSheetsImpl(long var0);

   public String getContentType() {
      return getContentTypeImpl(this.getPeer());
   }

   static native String getContentTypeImpl(long var0);

   public String getTitle() {
      return getTitleImpl(this.getPeer());
   }

   static native String getTitleImpl(long var0);

   public void setTitle(String var1) {
      setTitleImpl(this.getPeer(), var1);
   }

   static native void setTitleImpl(long var0, String var2);

   public String getReferrer() {
      return getReferrerImpl(this.getPeer());
   }

   static native String getReferrerImpl(long var0);

   public String getDomain() {
      return getDomainImpl(this.getPeer());
   }

   static native String getDomainImpl(long var0);

   public String getURL() {
      return getURLImpl(this.getPeer());
   }

   static native String getURLImpl(long var0);

   public String getCookie() throws DOMException {
      return getCookieImpl(this.getPeer());
   }

   static native String getCookieImpl(long var0);

   public void setCookie(String var1) throws DOMException {
      setCookieImpl(this.getPeer(), var1);
   }

   static native void setCookieImpl(long var0, String var2);

   public HTMLElement getBody() {
      return HTMLElementImpl.getImpl(getBodyImpl(this.getPeer()));
   }

   static native long getBodyImpl(long var0);

   public void setBody(HTMLElement var1) throws DOMException {
      setBodyImpl(this.getPeer(), HTMLElementImpl.getPeer(var1));
   }

   static native void setBodyImpl(long var0, long var2);

   public HTMLHeadElement getHead() {
      return HTMLHeadElementImpl.getImpl(getHeadImpl(this.getPeer()));
   }

   static native long getHeadImpl(long var0);

   public HTMLCollection getImages() {
      return HTMLCollectionImpl.getImpl(getImagesImpl(this.getPeer()));
   }

   static native long getImagesImpl(long var0);

   public HTMLCollection getApplets() {
      return HTMLCollectionImpl.getImpl(getAppletsImpl(this.getPeer()));
   }

   static native long getAppletsImpl(long var0);

   public HTMLCollection getLinks() {
      return HTMLCollectionImpl.getImpl(getLinksImpl(this.getPeer()));
   }

   static native long getLinksImpl(long var0);

   public HTMLCollection getForms() {
      return HTMLCollectionImpl.getImpl(getFormsImpl(this.getPeer()));
   }

   static native long getFormsImpl(long var0);

   public HTMLCollection getAnchors() {
      return HTMLCollectionImpl.getImpl(getAnchorsImpl(this.getPeer()));
   }

   static native long getAnchorsImpl(long var0);

   public String getLastModified() {
      return getLastModifiedImpl(this.getPeer());
   }

   static native String getLastModifiedImpl(long var0);

   public String getCharset() {
      return getCharsetImpl(this.getPeer());
   }

   static native String getCharsetImpl(long var0);

   public String getDefaultCharset() {
      return getDefaultCharsetImpl(this.getPeer());
   }

   static native String getDefaultCharsetImpl(long var0);

   public String getReadyState() {
      return getReadyStateImpl(this.getPeer());
   }

   static native String getReadyStateImpl(long var0);

   public String getCharacterSet() {
      return getCharacterSetImpl(this.getPeer());
   }

   static native String getCharacterSetImpl(long var0);

   public String getPreferredStylesheetSet() {
      return getPreferredStylesheetSetImpl(this.getPeer());
   }

   static native String getPreferredStylesheetSetImpl(long var0);

   public String getSelectedStylesheetSet() {
      return getSelectedStylesheetSetImpl(this.getPeer());
   }

   static native String getSelectedStylesheetSetImpl(long var0);

   public void setSelectedStylesheetSet(String var1) {
      setSelectedStylesheetSetImpl(this.getPeer(), var1);
   }

   static native void setSelectedStylesheetSetImpl(long var0, String var2);

   public Element getActiveElement() {
      return ElementImpl.getImpl(getActiveElementImpl(this.getPeer()));
   }

   static native long getActiveElementImpl(long var0);

   public String getCompatMode() {
      return getCompatModeImpl(this.getPeer());
   }

   static native String getCompatModeImpl(long var0);

   public boolean getWebkitIsFullScreen() {
      return getWebkitIsFullScreenImpl(this.getPeer());
   }

   static native boolean getWebkitIsFullScreenImpl(long var0);

   public boolean getWebkitFullScreenKeyboardInputAllowed() {
      return getWebkitFullScreenKeyboardInputAllowedImpl(this.getPeer());
   }

   static native boolean getWebkitFullScreenKeyboardInputAllowedImpl(long var0);

   public Element getWebkitCurrentFullScreenElement() {
      return ElementImpl.getImpl(getWebkitCurrentFullScreenElementImpl(this.getPeer()));
   }

   static native long getWebkitCurrentFullScreenElementImpl(long var0);

   public boolean getWebkitFullscreenEnabled() {
      return getWebkitFullscreenEnabledImpl(this.getPeer());
   }

   static native boolean getWebkitFullscreenEnabledImpl(long var0);

   public Element getWebkitFullscreenElement() {
      return ElementImpl.getImpl(getWebkitFullscreenElementImpl(this.getPeer()));
   }

   static native long getWebkitFullscreenElementImpl(long var0);

   public String getVisibilityState() {
      return getVisibilityStateImpl(this.getPeer());
   }

   static native String getVisibilityStateImpl(long var0);

   public boolean getHidden() {
      return getHiddenImpl(this.getPeer());
   }

   static native boolean getHiddenImpl(long var0);

   public HTMLScriptElement getCurrentScript() {
      return HTMLScriptElementImpl.getImpl(getCurrentScriptImpl(this.getPeer()));
   }

   static native long getCurrentScriptImpl(long var0);

   public String getOrigin() {
      return getOriginImpl(this.getPeer());
   }

   static native String getOriginImpl(long var0);

   public Element getScrollingElement() {
      return ElementImpl.getImpl(getScrollingElementImpl(this.getPeer()));
   }

   static native long getScrollingElementImpl(long var0);

   public EventListener getOnbeforecopy() {
      return EventListenerImpl.getImpl(getOnbeforecopyImpl(this.getPeer()));
   }

   static native long getOnbeforecopyImpl(long var0);

   public void setOnbeforecopy(EventListener var1) {
      setOnbeforecopyImpl(this.getPeer(), EventListenerImpl.getPeer(var1));
   }

   static native void setOnbeforecopyImpl(long var0, long var2);

   public EventListener getOnbeforecut() {
      return EventListenerImpl.getImpl(getOnbeforecutImpl(this.getPeer()));
   }

   static native long getOnbeforecutImpl(long var0);

   public void setOnbeforecut(EventListener var1) {
      setOnbeforecutImpl(this.getPeer(), EventListenerImpl.getPeer(var1));
   }

   static native void setOnbeforecutImpl(long var0, long var2);

   public EventListener getOnbeforepaste() {
      return EventListenerImpl.getImpl(getOnbeforepasteImpl(this.getPeer()));
   }

   static native long getOnbeforepasteImpl(long var0);

   public void setOnbeforepaste(EventListener var1) {
      setOnbeforepasteImpl(this.getPeer(), EventListenerImpl.getPeer(var1));
   }

   static native void setOnbeforepasteImpl(long var0, long var2);

   public EventListener getOncopy() {
      return EventListenerImpl.getImpl(getOncopyImpl(this.getPeer()));
   }

   static native long getOncopyImpl(long var0);

   public void setOncopy(EventListener var1) {
      setOncopyImpl(this.getPeer(), EventListenerImpl.getPeer(var1));
   }

   static native void setOncopyImpl(long var0, long var2);

   public EventListener getOncut() {
      return EventListenerImpl.getImpl(getOncutImpl(this.getPeer()));
   }

   static native long getOncutImpl(long var0);

   public void setOncut(EventListener var1) {
      setOncutImpl(this.getPeer(), EventListenerImpl.getPeer(var1));
   }

   static native void setOncutImpl(long var0, long var2);

   public EventListener getOnpaste() {
      return EventListenerImpl.getImpl(getOnpasteImpl(this.getPeer()));
   }

   static native long getOnpasteImpl(long var0);

   public void setOnpaste(EventListener var1) {
      setOnpasteImpl(this.getPeer(), EventListenerImpl.getPeer(var1));
   }

   static native void setOnpasteImpl(long var0, long var2);

   public EventListener getOnselectstart() {
      return EventListenerImpl.getImpl(getOnselectstartImpl(this.getPeer()));
   }

   static native long getOnselectstartImpl(long var0);

   public void setOnselectstart(EventListener var1) {
      setOnselectstartImpl(this.getPeer(), EventListenerImpl.getPeer(var1));
   }

   static native void setOnselectstartImpl(long var0, long var2);

   public EventListener getOnselectionchange() {
      return EventListenerImpl.getImpl(getOnselectionchangeImpl(this.getPeer()));
   }

   static native long getOnselectionchangeImpl(long var0);

   public void setOnselectionchange(EventListener var1) {
      setOnselectionchangeImpl(this.getPeer(), EventListenerImpl.getPeer(var1));
   }

   static native void setOnselectionchangeImpl(long var0, long var2);

   public EventListener getOnreadystatechange() {
      return EventListenerImpl.getImpl(getOnreadystatechangeImpl(this.getPeer()));
   }

   static native long getOnreadystatechangeImpl(long var0);

   public void setOnreadystatechange(EventListener var1) {
      setOnreadystatechangeImpl(this.getPeer(), EventListenerImpl.getPeer(var1));
   }

   static native void setOnreadystatechangeImpl(long var0, long var2);

   public EventListener getOnabort() {
      return EventListenerImpl.getImpl(getOnabortImpl(this.getPeer()));
   }

   static native long getOnabortImpl(long var0);

   public void setOnabort(EventListener var1) {
      setOnabortImpl(this.getPeer(), EventListenerImpl.getPeer(var1));
   }

   static native void setOnabortImpl(long var0, long var2);

   public EventListener getOnblur() {
      return EventListenerImpl.getImpl(getOnblurImpl(this.getPeer()));
   }

   static native long getOnblurImpl(long var0);

   public void setOnblur(EventListener var1) {
      setOnblurImpl(this.getPeer(), EventListenerImpl.getPeer(var1));
   }

   static native void setOnblurImpl(long var0, long var2);

   public EventListener getOncanplay() {
      return EventListenerImpl.getImpl(getOncanplayImpl(this.getPeer()));
   }

   static native long getOncanplayImpl(long var0);

   public void setOncanplay(EventListener var1) {
      setOncanplayImpl(this.getPeer(), EventListenerImpl.getPeer(var1));
   }

   static native void setOncanplayImpl(long var0, long var2);

   public EventListener getOncanplaythrough() {
      return EventListenerImpl.getImpl(getOncanplaythroughImpl(this.getPeer()));
   }

   static native long getOncanplaythroughImpl(long var0);

   public void setOncanplaythrough(EventListener var1) {
      setOncanplaythroughImpl(this.getPeer(), EventListenerImpl.getPeer(var1));
   }

   static native void setOncanplaythroughImpl(long var0, long var2);

   public EventListener getOnchange() {
      return EventListenerImpl.getImpl(getOnchangeImpl(this.getPeer()));
   }

   static native long getOnchangeImpl(long var0);

   public void setOnchange(EventListener var1) {
      setOnchangeImpl(this.getPeer(), EventListenerImpl.getPeer(var1));
   }

   static native void setOnchangeImpl(long var0, long var2);

   public EventListener getOnclick() {
      return EventListenerImpl.getImpl(getOnclickImpl(this.getPeer()));
   }

   static native long getOnclickImpl(long var0);

   public void setOnclick(EventListener var1) {
      setOnclickImpl(this.getPeer(), EventListenerImpl.getPeer(var1));
   }

   static native void setOnclickImpl(long var0, long var2);

   public EventListener getOncontextmenu() {
      return EventListenerImpl.getImpl(getOncontextmenuImpl(this.getPeer()));
   }

   static native long getOncontextmenuImpl(long var0);

   public void setOncontextmenu(EventListener var1) {
      setOncontextmenuImpl(this.getPeer(), EventListenerImpl.getPeer(var1));
   }

   static native void setOncontextmenuImpl(long var0, long var2);

   public EventListener getOndblclick() {
      return EventListenerImpl.getImpl(getOndblclickImpl(this.getPeer()));
   }

   static native long getOndblclickImpl(long var0);

   public void setOndblclick(EventListener var1) {
      setOndblclickImpl(this.getPeer(), EventListenerImpl.getPeer(var1));
   }

   static native void setOndblclickImpl(long var0, long var2);

   public EventListener getOndrag() {
      return EventListenerImpl.getImpl(getOndragImpl(this.getPeer()));
   }

   static native long getOndragImpl(long var0);

   public void setOndrag(EventListener var1) {
      setOndragImpl(this.getPeer(), EventListenerImpl.getPeer(var1));
   }

   static native void setOndragImpl(long var0, long var2);

   public EventListener getOndragend() {
      return EventListenerImpl.getImpl(getOndragendImpl(this.getPeer()));
   }

   static native long getOndragendImpl(long var0);

   public void setOndragend(EventListener var1) {
      setOndragendImpl(this.getPeer(), EventListenerImpl.getPeer(var1));
   }

   static native void setOndragendImpl(long var0, long var2);

   public EventListener getOndragenter() {
      return EventListenerImpl.getImpl(getOndragenterImpl(this.getPeer()));
   }

   static native long getOndragenterImpl(long var0);

   public void setOndragenter(EventListener var1) {
      setOndragenterImpl(this.getPeer(), EventListenerImpl.getPeer(var1));
   }

   static native void setOndragenterImpl(long var0, long var2);

   public EventListener getOndragleave() {
      return EventListenerImpl.getImpl(getOndragleaveImpl(this.getPeer()));
   }

   static native long getOndragleaveImpl(long var0);

   public void setOndragleave(EventListener var1) {
      setOndragleaveImpl(this.getPeer(), EventListenerImpl.getPeer(var1));
   }

   static native void setOndragleaveImpl(long var0, long var2);

   public EventListener getOndragover() {
      return EventListenerImpl.getImpl(getOndragoverImpl(this.getPeer()));
   }

   static native long getOndragoverImpl(long var0);

   public void setOndragover(EventListener var1) {
      setOndragoverImpl(this.getPeer(), EventListenerImpl.getPeer(var1));
   }

   static native void setOndragoverImpl(long var0, long var2);

   public EventListener getOndragstart() {
      return EventListenerImpl.getImpl(getOndragstartImpl(this.getPeer()));
   }

   static native long getOndragstartImpl(long var0);

   public void setOndragstart(EventListener var1) {
      setOndragstartImpl(this.getPeer(), EventListenerImpl.getPeer(var1));
   }

   static native void setOndragstartImpl(long var0, long var2);

   public EventListener getOndrop() {
      return EventListenerImpl.getImpl(getOndropImpl(this.getPeer()));
   }

   static native long getOndropImpl(long var0);

   public void setOndrop(EventListener var1) {
      setOndropImpl(this.getPeer(), EventListenerImpl.getPeer(var1));
   }

   static native void setOndropImpl(long var0, long var2);

   public EventListener getOndurationchange() {
      return EventListenerImpl.getImpl(getOndurationchangeImpl(this.getPeer()));
   }

   static native long getOndurationchangeImpl(long var0);

   public void setOndurationchange(EventListener var1) {
      setOndurationchangeImpl(this.getPeer(), EventListenerImpl.getPeer(var1));
   }

   static native void setOndurationchangeImpl(long var0, long var2);

   public EventListener getOnemptied() {
      return EventListenerImpl.getImpl(getOnemptiedImpl(this.getPeer()));
   }

   static native long getOnemptiedImpl(long var0);

   public void setOnemptied(EventListener var1) {
      setOnemptiedImpl(this.getPeer(), EventListenerImpl.getPeer(var1));
   }

   static native void setOnemptiedImpl(long var0, long var2);

   public EventListener getOnended() {
      return EventListenerImpl.getImpl(getOnendedImpl(this.getPeer()));
   }

   static native long getOnendedImpl(long var0);

   public void setOnended(EventListener var1) {
      setOnendedImpl(this.getPeer(), EventListenerImpl.getPeer(var1));
   }

   static native void setOnendedImpl(long var0, long var2);

   public EventListener getOnerror() {
      return EventListenerImpl.getImpl(getOnerrorImpl(this.getPeer()));
   }

   static native long getOnerrorImpl(long var0);

   public void setOnerror(EventListener var1) {
      setOnerrorImpl(this.getPeer(), EventListenerImpl.getPeer(var1));
   }

   static native void setOnerrorImpl(long var0, long var2);

   public EventListener getOnfocus() {
      return EventListenerImpl.getImpl(getOnfocusImpl(this.getPeer()));
   }

   static native long getOnfocusImpl(long var0);

   public void setOnfocus(EventListener var1) {
      setOnfocusImpl(this.getPeer(), EventListenerImpl.getPeer(var1));
   }

   static native void setOnfocusImpl(long var0, long var2);

   public EventListener getOninput() {
      return EventListenerImpl.getImpl(getOninputImpl(this.getPeer()));
   }

   static native long getOninputImpl(long var0);

   public void setOninput(EventListener var1) {
      setOninputImpl(this.getPeer(), EventListenerImpl.getPeer(var1));
   }

   static native void setOninputImpl(long var0, long var2);

   public EventListener getOninvalid() {
      return EventListenerImpl.getImpl(getOninvalidImpl(this.getPeer()));
   }

   static native long getOninvalidImpl(long var0);

   public void setOninvalid(EventListener var1) {
      setOninvalidImpl(this.getPeer(), EventListenerImpl.getPeer(var1));
   }

   static native void setOninvalidImpl(long var0, long var2);

   public EventListener getOnkeydown() {
      return EventListenerImpl.getImpl(getOnkeydownImpl(this.getPeer()));
   }

   static native long getOnkeydownImpl(long var0);

   public void setOnkeydown(EventListener var1) {
      setOnkeydownImpl(this.getPeer(), EventListenerImpl.getPeer(var1));
   }

   static native void setOnkeydownImpl(long var0, long var2);

   public EventListener getOnkeypress() {
      return EventListenerImpl.getImpl(getOnkeypressImpl(this.getPeer()));
   }

   static native long getOnkeypressImpl(long var0);

   public void setOnkeypress(EventListener var1) {
      setOnkeypressImpl(this.getPeer(), EventListenerImpl.getPeer(var1));
   }

   static native void setOnkeypressImpl(long var0, long var2);

   public EventListener getOnkeyup() {
      return EventListenerImpl.getImpl(getOnkeyupImpl(this.getPeer()));
   }

   static native long getOnkeyupImpl(long var0);

   public void setOnkeyup(EventListener var1) {
      setOnkeyupImpl(this.getPeer(), EventListenerImpl.getPeer(var1));
   }

   static native void setOnkeyupImpl(long var0, long var2);

   public EventListener getOnload() {
      return EventListenerImpl.getImpl(getOnloadImpl(this.getPeer()));
   }

   static native long getOnloadImpl(long var0);

   public void setOnload(EventListener var1) {
      setOnloadImpl(this.getPeer(), EventListenerImpl.getPeer(var1));
   }

   static native void setOnloadImpl(long var0, long var2);

   public EventListener getOnloadeddata() {
      return EventListenerImpl.getImpl(getOnloadeddataImpl(this.getPeer()));
   }

   static native long getOnloadeddataImpl(long var0);

   public void setOnloadeddata(EventListener var1) {
      setOnloadeddataImpl(this.getPeer(), EventListenerImpl.getPeer(var1));
   }

   static native void setOnloadeddataImpl(long var0, long var2);

   public EventListener getOnloadedmetadata() {
      return EventListenerImpl.getImpl(getOnloadedmetadataImpl(this.getPeer()));
   }

   static native long getOnloadedmetadataImpl(long var0);

   public void setOnloadedmetadata(EventListener var1) {
      setOnloadedmetadataImpl(this.getPeer(), EventListenerImpl.getPeer(var1));
   }

   static native void setOnloadedmetadataImpl(long var0, long var2);

   public EventListener getOnloadstart() {
      return EventListenerImpl.getImpl(getOnloadstartImpl(this.getPeer()));
   }

   static native long getOnloadstartImpl(long var0);

   public void setOnloadstart(EventListener var1) {
      setOnloadstartImpl(this.getPeer(), EventListenerImpl.getPeer(var1));
   }

   static native void setOnloadstartImpl(long var0, long var2);

   public EventListener getOnmousedown() {
      return EventListenerImpl.getImpl(getOnmousedownImpl(this.getPeer()));
   }

   static native long getOnmousedownImpl(long var0);

   public void setOnmousedown(EventListener var1) {
      setOnmousedownImpl(this.getPeer(), EventListenerImpl.getPeer(var1));
   }

   static native void setOnmousedownImpl(long var0, long var2);

   public EventListener getOnmouseenter() {
      return EventListenerImpl.getImpl(getOnmouseenterImpl(this.getPeer()));
   }

   static native long getOnmouseenterImpl(long var0);

   public void setOnmouseenter(EventListener var1) {
      setOnmouseenterImpl(this.getPeer(), EventListenerImpl.getPeer(var1));
   }

   static native void setOnmouseenterImpl(long var0, long var2);

   public EventListener getOnmouseleave() {
      return EventListenerImpl.getImpl(getOnmouseleaveImpl(this.getPeer()));
   }

   static native long getOnmouseleaveImpl(long var0);

   public void setOnmouseleave(EventListener var1) {
      setOnmouseleaveImpl(this.getPeer(), EventListenerImpl.getPeer(var1));
   }

   static native void setOnmouseleaveImpl(long var0, long var2);

   public EventListener getOnmousemove() {
      return EventListenerImpl.getImpl(getOnmousemoveImpl(this.getPeer()));
   }

   static native long getOnmousemoveImpl(long var0);

   public void setOnmousemove(EventListener var1) {
      setOnmousemoveImpl(this.getPeer(), EventListenerImpl.getPeer(var1));
   }

   static native void setOnmousemoveImpl(long var0, long var2);

   public EventListener getOnmouseout() {
      return EventListenerImpl.getImpl(getOnmouseoutImpl(this.getPeer()));
   }

   static native long getOnmouseoutImpl(long var0);

   public void setOnmouseout(EventListener var1) {
      setOnmouseoutImpl(this.getPeer(), EventListenerImpl.getPeer(var1));
   }

   static native void setOnmouseoutImpl(long var0, long var2);

   public EventListener getOnmouseover() {
      return EventListenerImpl.getImpl(getOnmouseoverImpl(this.getPeer()));
   }

   static native long getOnmouseoverImpl(long var0);

   public void setOnmouseover(EventListener var1) {
      setOnmouseoverImpl(this.getPeer(), EventListenerImpl.getPeer(var1));
   }

   static native void setOnmouseoverImpl(long var0, long var2);

   public EventListener getOnmouseup() {
      return EventListenerImpl.getImpl(getOnmouseupImpl(this.getPeer()));
   }

   static native long getOnmouseupImpl(long var0);

   public void setOnmouseup(EventListener var1) {
      setOnmouseupImpl(this.getPeer(), EventListenerImpl.getPeer(var1));
   }

   static native void setOnmouseupImpl(long var0, long var2);

   public EventListener getOnmousewheel() {
      return EventListenerImpl.getImpl(getOnmousewheelImpl(this.getPeer()));
   }

   static native long getOnmousewheelImpl(long var0);

   public void setOnmousewheel(EventListener var1) {
      setOnmousewheelImpl(this.getPeer(), EventListenerImpl.getPeer(var1));
   }

   static native void setOnmousewheelImpl(long var0, long var2);

   public EventListener getOnpause() {
      return EventListenerImpl.getImpl(getOnpauseImpl(this.getPeer()));
   }

   static native long getOnpauseImpl(long var0);

   public void setOnpause(EventListener var1) {
      setOnpauseImpl(this.getPeer(), EventListenerImpl.getPeer(var1));
   }

   static native void setOnpauseImpl(long var0, long var2);

   public EventListener getOnplay() {
      return EventListenerImpl.getImpl(getOnplayImpl(this.getPeer()));
   }

   static native long getOnplayImpl(long var0);

   public void setOnplay(EventListener var1) {
      setOnplayImpl(this.getPeer(), EventListenerImpl.getPeer(var1));
   }

   static native void setOnplayImpl(long var0, long var2);

   public EventListener getOnplaying() {
      return EventListenerImpl.getImpl(getOnplayingImpl(this.getPeer()));
   }

   static native long getOnplayingImpl(long var0);

   public void setOnplaying(EventListener var1) {
      setOnplayingImpl(this.getPeer(), EventListenerImpl.getPeer(var1));
   }

   static native void setOnplayingImpl(long var0, long var2);

   public EventListener getOnprogress() {
      return EventListenerImpl.getImpl(getOnprogressImpl(this.getPeer()));
   }

   static native long getOnprogressImpl(long var0);

   public void setOnprogress(EventListener var1) {
      setOnprogressImpl(this.getPeer(), EventListenerImpl.getPeer(var1));
   }

   static native void setOnprogressImpl(long var0, long var2);

   public EventListener getOnratechange() {
      return EventListenerImpl.getImpl(getOnratechangeImpl(this.getPeer()));
   }

   static native long getOnratechangeImpl(long var0);

   public void setOnratechange(EventListener var1) {
      setOnratechangeImpl(this.getPeer(), EventListenerImpl.getPeer(var1));
   }

   static native void setOnratechangeImpl(long var0, long var2);

   public EventListener getOnreset() {
      return EventListenerImpl.getImpl(getOnresetImpl(this.getPeer()));
   }

   static native long getOnresetImpl(long var0);

   public void setOnreset(EventListener var1) {
      setOnresetImpl(this.getPeer(), EventListenerImpl.getPeer(var1));
   }

   static native void setOnresetImpl(long var0, long var2);

   public EventListener getOnresize() {
      return EventListenerImpl.getImpl(getOnresizeImpl(this.getPeer()));
   }

   static native long getOnresizeImpl(long var0);

   public void setOnresize(EventListener var1) {
      setOnresizeImpl(this.getPeer(), EventListenerImpl.getPeer(var1));
   }

   static native void setOnresizeImpl(long var0, long var2);

   public EventListener getOnscroll() {
      return EventListenerImpl.getImpl(getOnscrollImpl(this.getPeer()));
   }

   static native long getOnscrollImpl(long var0);

   public void setOnscroll(EventListener var1) {
      setOnscrollImpl(this.getPeer(), EventListenerImpl.getPeer(var1));
   }

   static native void setOnscrollImpl(long var0, long var2);

   public EventListener getOnseeked() {
      return EventListenerImpl.getImpl(getOnseekedImpl(this.getPeer()));
   }

   static native long getOnseekedImpl(long var0);

   public void setOnseeked(EventListener var1) {
      setOnseekedImpl(this.getPeer(), EventListenerImpl.getPeer(var1));
   }

   static native void setOnseekedImpl(long var0, long var2);

   public EventListener getOnseeking() {
      return EventListenerImpl.getImpl(getOnseekingImpl(this.getPeer()));
   }

   static native long getOnseekingImpl(long var0);

   public void setOnseeking(EventListener var1) {
      setOnseekingImpl(this.getPeer(), EventListenerImpl.getPeer(var1));
   }

   static native void setOnseekingImpl(long var0, long var2);

   public EventListener getOnselect() {
      return EventListenerImpl.getImpl(getOnselectImpl(this.getPeer()));
   }

   static native long getOnselectImpl(long var0);

   public void setOnselect(EventListener var1) {
      setOnselectImpl(this.getPeer(), EventListenerImpl.getPeer(var1));
   }

   static native void setOnselectImpl(long var0, long var2);

   public EventListener getOnstalled() {
      return EventListenerImpl.getImpl(getOnstalledImpl(this.getPeer()));
   }

   static native long getOnstalledImpl(long var0);

   public void setOnstalled(EventListener var1) {
      setOnstalledImpl(this.getPeer(), EventListenerImpl.getPeer(var1));
   }

   static native void setOnstalledImpl(long var0, long var2);

   public EventListener getOnsubmit() {
      return EventListenerImpl.getImpl(getOnsubmitImpl(this.getPeer()));
   }

   static native long getOnsubmitImpl(long var0);

   public void setOnsubmit(EventListener var1) {
      setOnsubmitImpl(this.getPeer(), EventListenerImpl.getPeer(var1));
   }

   static native void setOnsubmitImpl(long var0, long var2);

   public EventListener getOnsuspend() {
      return EventListenerImpl.getImpl(getOnsuspendImpl(this.getPeer()));
   }

   static native long getOnsuspendImpl(long var0);

   public void setOnsuspend(EventListener var1) {
      setOnsuspendImpl(this.getPeer(), EventListenerImpl.getPeer(var1));
   }

   static native void setOnsuspendImpl(long var0, long var2);

   public EventListener getOntimeupdate() {
      return EventListenerImpl.getImpl(getOntimeupdateImpl(this.getPeer()));
   }

   static native long getOntimeupdateImpl(long var0);

   public void setOntimeupdate(EventListener var1) {
      setOntimeupdateImpl(this.getPeer(), EventListenerImpl.getPeer(var1));
   }

   static native void setOntimeupdateImpl(long var0, long var2);

   public EventListener getOnvolumechange() {
      return EventListenerImpl.getImpl(getOnvolumechangeImpl(this.getPeer()));
   }

   static native long getOnvolumechangeImpl(long var0);

   public void setOnvolumechange(EventListener var1) {
      setOnvolumechangeImpl(this.getPeer(), EventListenerImpl.getPeer(var1));
   }

   static native void setOnvolumechangeImpl(long var0, long var2);

   public EventListener getOnwaiting() {
      return EventListenerImpl.getImpl(getOnwaitingImpl(this.getPeer()));
   }

   static native long getOnwaitingImpl(long var0);

   public void setOnwaiting(EventListener var1) {
      setOnwaitingImpl(this.getPeer(), EventListenerImpl.getPeer(var1));
   }

   static native void setOnwaitingImpl(long var0, long var2);

   public EventListener getOnsearch() {
      return EventListenerImpl.getImpl(getOnsearchImpl(this.getPeer()));
   }

   static native long getOnsearchImpl(long var0);

   public void setOnsearch(EventListener var1) {
      setOnsearchImpl(this.getPeer(), EventListenerImpl.getPeer(var1));
   }

   static native void setOnsearchImpl(long var0, long var2);

   public EventListener getOnwheel() {
      return EventListenerImpl.getImpl(getOnwheelImpl(this.getPeer()));
   }

   static native long getOnwheelImpl(long var0);

   public void setOnwheel(EventListener var1) {
      setOnwheelImpl(this.getPeer(), EventListenerImpl.getPeer(var1));
   }

   static native void setOnwheelImpl(long var0, long var2);

   public HTMLCollection getChildren() {
      return HTMLCollectionImpl.getImpl(getChildrenImpl(this.getPeer()));
   }

   static native long getChildrenImpl(long var0);

   public Element getFirstElementChild() {
      return ElementImpl.getImpl(getFirstElementChildImpl(this.getPeer()));
   }

   static native long getFirstElementChildImpl(long var0);

   public Element getLastElementChild() {
      return ElementImpl.getImpl(getLastElementChildImpl(this.getPeer()));
   }

   static native long getLastElementChildImpl(long var0);

   public int getChildElementCount() {
      return getChildElementCountImpl(this.getPeer());
   }

   static native int getChildElementCountImpl(long var0);

   public Element createElement(String var1) throws DOMException {
      return ElementImpl.getImpl(createElementImpl(this.getPeer(), var1));
   }

   static native long createElementImpl(long var0, String var2);

   public DocumentFragment createDocumentFragment() {
      return DocumentFragmentImpl.getImpl(createDocumentFragmentImpl(this.getPeer()));
   }

   static native long createDocumentFragmentImpl(long var0);

   public Text createTextNode(String var1) {
      return TextImpl.getImpl(createTextNodeImpl(this.getPeer(), var1));
   }

   static native long createTextNodeImpl(long var0, String var2);

   public Comment createComment(String var1) {
      return CommentImpl.getImpl(createCommentImpl(this.getPeer(), var1));
   }

   static native long createCommentImpl(long var0, String var2);

   public CDATASection createCDATASection(String var1) throws DOMException {
      return CDATASectionImpl.getImpl(createCDATASectionImpl(this.getPeer(), var1));
   }

   static native long createCDATASectionImpl(long var0, String var2);

   public ProcessingInstruction createProcessingInstruction(String var1, String var2) throws DOMException {
      return (ProcessingInstruction)ProcessingInstructionImpl.getImpl(createProcessingInstructionImpl(this.getPeer(), var1, var2));
   }

   static native long createProcessingInstructionImpl(long var0, String var2, String var3);

   public Attr createAttribute(String var1) throws DOMException {
      return AttrImpl.getImpl(createAttributeImpl(this.getPeer(), var1));
   }

   static native long createAttributeImpl(long var0, String var2);

   public EntityReference createEntityReference(String var1) throws DOMException {
      return EntityReferenceImpl.getImpl(createEntityReferenceImpl(this.getPeer(), var1));
   }

   static native long createEntityReferenceImpl(long var0, String var2);

   public NodeList getElementsByTagName(String var1) {
      return NodeListImpl.getImpl(getElementsByTagNameImpl(this.getPeer(), var1));
   }

   static native long getElementsByTagNameImpl(long var0, String var2);

   public Node importNode(Node var1, boolean var2) throws DOMException {
      return NodeImpl.getImpl(importNodeImpl(this.getPeer(), NodeImpl.getPeer(var1), var2));
   }

   static native long importNodeImpl(long var0, long var2, boolean var4);

   public Element createElementNS(String var1, String var2) throws DOMException {
      return ElementImpl.getImpl(createElementNSImpl(this.getPeer(), var1, var2));
   }

   static native long createElementNSImpl(long var0, String var2, String var3);

   public Attr createAttributeNS(String var1, String var2) throws DOMException {
      return AttrImpl.getImpl(createAttributeNSImpl(this.getPeer(), var1, var2));
   }

   static native long createAttributeNSImpl(long var0, String var2, String var3);

   public NodeList getElementsByTagNameNS(String var1, String var2) {
      return NodeListImpl.getImpl(getElementsByTagNameNSImpl(this.getPeer(), var1, var2));
   }

   static native long getElementsByTagNameNSImpl(long var0, String var2, String var3);

   public Node adoptNode(Node var1) throws DOMException {
      return NodeImpl.getImpl(adoptNodeImpl(this.getPeer(), NodeImpl.getPeer(var1)));
   }

   static native long adoptNodeImpl(long var0, long var2);

   public Event createEvent(String var1) throws DOMException {
      return EventImpl.getImpl(createEventImpl(this.getPeer(), var1));
   }

   static native long createEventImpl(long var0, String var2);

   public Range createRange() {
      return RangeImpl.getImpl(createRangeImpl(this.getPeer()));
   }

   static native long createRangeImpl(long var0);

   public NodeIterator createNodeIterator(Node var1, int var2, NodeFilter var3, boolean var4) throws DOMException {
      return NodeIteratorImpl.getImpl(createNodeIteratorImpl(this.getPeer(), NodeImpl.getPeer(var1), var2, NodeFilterImpl.getPeer(var3), var4));
   }

   static native long createNodeIteratorImpl(long var0, long var2, int var4, long var5, boolean var7);

   public TreeWalker createTreeWalker(Node var1, int var2, NodeFilter var3, boolean var4) throws DOMException {
      return TreeWalkerImpl.getImpl(createTreeWalkerImpl(this.getPeer(), NodeImpl.getPeer(var1), var2, NodeFilterImpl.getPeer(var3), var4));
   }

   static native long createTreeWalkerImpl(long var0, long var2, int var4, long var5, boolean var7);

   public CSSStyleDeclaration getOverrideStyle(Element var1, String var2) {
      return CSSStyleDeclarationImpl.getImpl(getOverrideStyleImpl(this.getPeer(), ElementImpl.getPeer(var1), var2));
   }

   static native long getOverrideStyleImpl(long var0, long var2, String var4);

   public XPathExpression createExpression(String var1, XPathNSResolver var2) throws DOMException {
      return XPathExpressionImpl.getImpl(createExpressionImpl(this.getPeer(), var1, XPathNSResolverImpl.getPeer(var2)));
   }

   static native long createExpressionImpl(long var0, String var2, long var3);

   public XPathNSResolver createNSResolver(Node var1) {
      return XPathNSResolverImpl.getImpl(createNSResolverImpl(this.getPeer(), NodeImpl.getPeer(var1)));
   }

   static native long createNSResolverImpl(long var0, long var2);

   public XPathResult evaluate(String var1, Node var2, XPathNSResolver var3, short var4, XPathResult var5) throws DOMException {
      return XPathResultImpl.getImpl(evaluateImpl(this.getPeer(), var1, NodeImpl.getPeer(var2), XPathNSResolverImpl.getPeer(var3), var4, XPathResultImpl.getPeer(var5)));
   }

   static native long evaluateImpl(long var0, String var2, long var3, long var5, short var7, long var8);

   public boolean execCommand(String var1, boolean var2, String var3) {
      return execCommandImpl(this.getPeer(), var1, var2, var3);
   }

   static native boolean execCommandImpl(long var0, String var2, boolean var3, String var4);

   public boolean queryCommandEnabled(String var1) {
      return queryCommandEnabledImpl(this.getPeer(), var1);
   }

   static native boolean queryCommandEnabledImpl(long var0, String var2);

   public boolean queryCommandIndeterm(String var1) {
      return queryCommandIndetermImpl(this.getPeer(), var1);
   }

   static native boolean queryCommandIndetermImpl(long var0, String var2);

   public boolean queryCommandState(String var1) {
      return queryCommandStateImpl(this.getPeer(), var1);
   }

   static native boolean queryCommandStateImpl(long var0, String var2);

   public boolean queryCommandSupported(String var1) {
      return queryCommandSupportedImpl(this.getPeer(), var1);
   }

   static native boolean queryCommandSupportedImpl(long var0, String var2);

   public String queryCommandValue(String var1) {
      return queryCommandValueImpl(this.getPeer(), var1);
   }

   static native String queryCommandValueImpl(long var0, String var2);

   public NodeList getElementsByName(String var1) {
      return NodeListImpl.getImpl(getElementsByNameImpl(this.getPeer(), var1));
   }

   static native long getElementsByNameImpl(long var0, String var2);

   public Element elementFromPoint(int var1, int var2) {
      return ElementImpl.getImpl(elementFromPointImpl(this.getPeer(), var1, var2));
   }

   static native long elementFromPointImpl(long var0, int var2, int var3);

   public Range caretRangeFromPoint(int var1, int var2) {
      return RangeImpl.getImpl(caretRangeFromPointImpl(this.getPeer(), var1, var2));
   }

   static native long caretRangeFromPointImpl(long var0, int var2, int var3);

   public CSSStyleDeclaration createCSSStyleDeclaration() {
      return CSSStyleDeclarationImpl.getImpl(createCSSStyleDeclarationImpl(this.getPeer()));
   }

   static native long createCSSStyleDeclarationImpl(long var0);

   public HTMLCollection getElementsByClassName(String var1) {
      return HTMLCollectionImpl.getImpl(getElementsByClassNameImpl(this.getPeer(), var1));
   }

   static native long getElementsByClassNameImpl(long var0, String var2);

   public boolean hasFocus() {
      return hasFocusImpl(this.getPeer());
   }

   static native boolean hasFocusImpl(long var0);

   public void webkitCancelFullScreen() {
      webkitCancelFullScreenImpl(this.getPeer());
   }

   static native void webkitCancelFullScreenImpl(long var0);

   public void webkitExitFullscreen() {
      webkitExitFullscreenImpl(this.getPeer());
   }

   static native void webkitExitFullscreenImpl(long var0);

   public Element getElementById(String var1) {
      return ElementImpl.getImpl(getElementByIdImpl(this.getPeer(), var1));
   }

   static native long getElementByIdImpl(long var0, String var2);

   public Element querySelector(String var1) throws DOMException {
      return ElementImpl.getImpl(querySelectorImpl(this.getPeer(), var1));
   }

   static native long querySelectorImpl(long var0, String var2);

   public NodeList querySelectorAll(String var1) throws DOMException {
      return NodeListImpl.getImpl(querySelectorAllImpl(this.getPeer(), var1));
   }

   static native long querySelectorAllImpl(long var0, String var2);

   public boolean getStrictErrorChecking() {
      throw new UnsupportedOperationException("Not supported yet.");
   }

   public void setStrictErrorChecking(boolean var1) {
      throw new UnsupportedOperationException("Not supported yet.");
   }

   public Node renameNode(Node var1, String var2, String var3) throws DOMException {
      throw new UnsupportedOperationException("Not supported yet.");
   }

   public DOMConfiguration getDomConfig() {
      throw new UnsupportedOperationException("Not supported yet.");
   }

   public void normalizeDocument() {
      throw new UnsupportedOperationException("Not supported yet.");
   }
}
