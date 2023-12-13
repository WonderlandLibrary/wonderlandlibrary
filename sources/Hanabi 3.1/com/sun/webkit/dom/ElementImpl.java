package com.sun.webkit.dom;

import org.w3c.dom.Attr;
import org.w3c.dom.DOMException;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;
import org.w3c.dom.TypeInfo;
import org.w3c.dom.css.CSSStyleDeclaration;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.html.HTMLCollection;

public class ElementImpl extends NodeImpl implements Element {
   public static final int ALLOW_KEYBOARD_INPUT = 1;

   ElementImpl(long var1) {
      super(var1);
   }

   static Element getImpl(long var0) {
      return (Element)create(var0);
   }

   static native boolean isHTMLElementImpl(long var0);

   public String getTagName() {
      return getTagNameImpl(this.getPeer());
   }

   static native String getTagNameImpl(long var0);

   public NamedNodeMap getAttributes() {
      return NamedNodeMapImpl.getImpl(getAttributesImpl(this.getPeer()));
   }

   static native long getAttributesImpl(long var0);

   public CSSStyleDeclaration getStyle() {
      return CSSStyleDeclarationImpl.getImpl(getStyleImpl(this.getPeer()));
   }

   static native long getStyleImpl(long var0);

   public String getId() {
      return getIdImpl(this.getPeer());
   }

   static native String getIdImpl(long var0);

   public void setId(String var1) {
      setIdImpl(this.getPeer(), var1);
   }

   static native void setIdImpl(long var0, String var2);

   public double getOffsetLeft() {
      return getOffsetLeftImpl(this.getPeer());
   }

   static native double getOffsetLeftImpl(long var0);

   public double getOffsetTop() {
      return getOffsetTopImpl(this.getPeer());
   }

   static native double getOffsetTopImpl(long var0);

   public double getOffsetWidth() {
      return getOffsetWidthImpl(this.getPeer());
   }

   static native double getOffsetWidthImpl(long var0);

   public double getOffsetHeight() {
      return getOffsetHeightImpl(this.getPeer());
   }

   static native double getOffsetHeightImpl(long var0);

   public double getClientLeft() {
      return getClientLeftImpl(this.getPeer());
   }

   static native double getClientLeftImpl(long var0);

   public double getClientTop() {
      return getClientTopImpl(this.getPeer());
   }

   static native double getClientTopImpl(long var0);

   public double getClientWidth() {
      return getClientWidthImpl(this.getPeer());
   }

   static native double getClientWidthImpl(long var0);

   public double getClientHeight() {
      return getClientHeightImpl(this.getPeer());
   }

   static native double getClientHeightImpl(long var0);

   public int getScrollLeft() {
      return getScrollLeftImpl(this.getPeer());
   }

   static native int getScrollLeftImpl(long var0);

   public void setScrollLeft(int var1) {
      setScrollLeftImpl(this.getPeer(), var1);
   }

   static native void setScrollLeftImpl(long var0, int var2);

   public int getScrollTop() {
      return getScrollTopImpl(this.getPeer());
   }

   static native int getScrollTopImpl(long var0);

   public void setScrollTop(int var1) {
      setScrollTopImpl(this.getPeer(), var1);
   }

   static native void setScrollTopImpl(long var0, int var2);

   public int getScrollWidth() {
      return getScrollWidthImpl(this.getPeer());
   }

   static native int getScrollWidthImpl(long var0);

   public int getScrollHeight() {
      return getScrollHeightImpl(this.getPeer());
   }

   static native int getScrollHeightImpl(long var0);

   public Element getOffsetParent() {
      return getImpl(getOffsetParentImpl(this.getPeer()));
   }

   static native long getOffsetParentImpl(long var0);

   public String getInnerHTML() {
      return getInnerHTMLImpl(this.getPeer());
   }

   static native String getInnerHTMLImpl(long var0);

   public void setInnerHTML(String var1) throws DOMException {
      setInnerHTMLImpl(this.getPeer(), var1);
   }

   static native void setInnerHTMLImpl(long var0, String var2);

   public String getOuterHTML() {
      return getOuterHTMLImpl(this.getPeer());
   }

   static native String getOuterHTMLImpl(long var0);

   public void setOuterHTML(String var1) throws DOMException {
      setOuterHTMLImpl(this.getPeer(), var1);
   }

   static native void setOuterHTMLImpl(long var0, String var2);

   public String getClassName() {
      return getClassNameImpl(this.getPeer());
   }

   static native String getClassNameImpl(long var0);

   public void setClassName(String var1) {
      setClassNameImpl(this.getPeer(), var1);
   }

   static native void setClassNameImpl(long var0, String var2);

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

   public EventListener getOnanimationend() {
      return EventListenerImpl.getImpl(getOnanimationendImpl(this.getPeer()));
   }

   static native long getOnanimationendImpl(long var0);

   public void setOnanimationend(EventListener var1) {
      setOnanimationendImpl(this.getPeer(), EventListenerImpl.getPeer(var1));
   }

   static native void setOnanimationendImpl(long var0, long var2);

   public EventListener getOnanimationiteration() {
      return EventListenerImpl.getImpl(getOnanimationiterationImpl(this.getPeer()));
   }

   static native long getOnanimationiterationImpl(long var0);

   public void setOnanimationiteration(EventListener var1) {
      setOnanimationiterationImpl(this.getPeer(), EventListenerImpl.getPeer(var1));
   }

   static native void setOnanimationiterationImpl(long var0, long var2);

   public EventListener getOnanimationstart() {
      return EventListenerImpl.getImpl(getOnanimationstartImpl(this.getPeer()));
   }

   static native long getOnanimationstartImpl(long var0);

   public void setOnanimationstart(EventListener var1) {
      setOnanimationstartImpl(this.getPeer(), EventListenerImpl.getPeer(var1));
   }

   static native void setOnanimationstartImpl(long var0, long var2);

   public EventListener getOntransitionend() {
      return EventListenerImpl.getImpl(getOntransitionendImpl(this.getPeer()));
   }

   static native long getOntransitionendImpl(long var0);

   public void setOntransitionend(EventListener var1) {
      setOntransitionendImpl(this.getPeer(), EventListenerImpl.getPeer(var1));
   }

   static native void setOntransitionendImpl(long var0, long var2);

   public EventListener getOnwebkitanimationend() {
      return EventListenerImpl.getImpl(getOnwebkitanimationendImpl(this.getPeer()));
   }

   static native long getOnwebkitanimationendImpl(long var0);

   public void setOnwebkitanimationend(EventListener var1) {
      setOnwebkitanimationendImpl(this.getPeer(), EventListenerImpl.getPeer(var1));
   }

   static native void setOnwebkitanimationendImpl(long var0, long var2);

   public EventListener getOnwebkitanimationiteration() {
      return EventListenerImpl.getImpl(getOnwebkitanimationiterationImpl(this.getPeer()));
   }

   static native long getOnwebkitanimationiterationImpl(long var0);

   public void setOnwebkitanimationiteration(EventListener var1) {
      setOnwebkitanimationiterationImpl(this.getPeer(), EventListenerImpl.getPeer(var1));
   }

   static native void setOnwebkitanimationiterationImpl(long var0, long var2);

   public EventListener getOnwebkitanimationstart() {
      return EventListenerImpl.getImpl(getOnwebkitanimationstartImpl(this.getPeer()));
   }

   static native long getOnwebkitanimationstartImpl(long var0);

   public void setOnwebkitanimationstart(EventListener var1) {
      setOnwebkitanimationstartImpl(this.getPeer(), EventListenerImpl.getPeer(var1));
   }

   static native void setOnwebkitanimationstartImpl(long var0, long var2);

   public EventListener getOnwebkittransitionend() {
      return EventListenerImpl.getImpl(getOnwebkittransitionendImpl(this.getPeer()));
   }

   static native long getOnwebkittransitionendImpl(long var0);

   public void setOnwebkittransitionend(EventListener var1) {
      setOnwebkittransitionendImpl(this.getPeer(), EventListenerImpl.getPeer(var1));
   }

   static native void setOnwebkittransitionendImpl(long var0, long var2);

   public EventListener getOnfocusin() {
      return EventListenerImpl.getImpl(getOnfocusinImpl(this.getPeer()));
   }

   static native long getOnfocusinImpl(long var0);

   public void setOnfocusin(EventListener var1) {
      setOnfocusinImpl(this.getPeer(), EventListenerImpl.getPeer(var1));
   }

   static native void setOnfocusinImpl(long var0, long var2);

   public EventListener getOnfocusout() {
      return EventListenerImpl.getImpl(getOnfocusoutImpl(this.getPeer()));
   }

   static native long getOnfocusoutImpl(long var0);

   public void setOnfocusout(EventListener var1) {
      setOnfocusoutImpl(this.getPeer(), EventListenerImpl.getPeer(var1));
   }

   static native void setOnfocusoutImpl(long var0, long var2);

   public EventListener getOnbeforeload() {
      return EventListenerImpl.getImpl(getOnbeforeloadImpl(this.getPeer()));
   }

   static native long getOnbeforeloadImpl(long var0);

   public void setOnbeforeload(EventListener var1) {
      setOnbeforeloadImpl(this.getPeer(), EventListenerImpl.getPeer(var1));
   }

   static native void setOnbeforeloadImpl(long var0, long var2);

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

   public Element getPreviousElementSibling() {
      return getImpl(getPreviousElementSiblingImpl(this.getPeer()));
   }

   static native long getPreviousElementSiblingImpl(long var0);

   public Element getNextElementSibling() {
      return getImpl(getNextElementSiblingImpl(this.getPeer()));
   }

   static native long getNextElementSiblingImpl(long var0);

   public HTMLCollection getChildren() {
      return HTMLCollectionImpl.getImpl(getChildrenImpl(this.getPeer()));
   }

   static native long getChildrenImpl(long var0);

   public Element getFirstElementChild() {
      return getImpl(getFirstElementChildImpl(this.getPeer()));
   }

   static native long getFirstElementChildImpl(long var0);

   public Element getLastElementChild() {
      return getImpl(getLastElementChildImpl(this.getPeer()));
   }

   static native long getLastElementChildImpl(long var0);

   public int getChildElementCount() {
      return getChildElementCountImpl(this.getPeer());
   }

   static native int getChildElementCountImpl(long var0);

   public String getAttribute(String var1) {
      return getAttributeImpl(this.getPeer(), var1);
   }

   static native String getAttributeImpl(long var0, String var2);

   public void setAttribute(String var1, String var2) throws DOMException {
      setAttributeImpl(this.getPeer(), var1, var2);
   }

   static native void setAttributeImpl(long var0, String var2, String var3);

   public void removeAttribute(String var1) {
      removeAttributeImpl(this.getPeer(), var1);
   }

   static native void removeAttributeImpl(long var0, String var2);

   public Attr getAttributeNode(String var1) {
      return AttrImpl.getImpl(getAttributeNodeImpl(this.getPeer(), var1));
   }

   static native long getAttributeNodeImpl(long var0, String var2);

   public Attr setAttributeNode(Attr var1) throws DOMException {
      return AttrImpl.getImpl(setAttributeNodeImpl(this.getPeer(), AttrImpl.getPeer(var1)));
   }

   static native long setAttributeNodeImpl(long var0, long var2);

   public Attr removeAttributeNode(Attr var1) throws DOMException {
      return AttrImpl.getImpl(removeAttributeNodeImpl(this.getPeer(), AttrImpl.getPeer(var1)));
   }

   static native long removeAttributeNodeImpl(long var0, long var2);

   public NodeList getElementsByTagName(String var1) {
      return NodeListImpl.getImpl(getElementsByTagNameImpl(this.getPeer(), var1));
   }

   static native long getElementsByTagNameImpl(long var0, String var2);

   public boolean hasAttributes() {
      return hasAttributesImpl(this.getPeer());
   }

   static native boolean hasAttributesImpl(long var0);

   public String getAttributeNS(String var1, String var2) {
      return getAttributeNSImpl(this.getPeer(), var1, var2);
   }

   static native String getAttributeNSImpl(long var0, String var2, String var3);

   public void setAttributeNS(String var1, String var2, String var3) throws DOMException {
      setAttributeNSImpl(this.getPeer(), var1, var2, var3);
   }

   static native void setAttributeNSImpl(long var0, String var2, String var3, String var4);

   public void removeAttributeNS(String var1, String var2) {
      removeAttributeNSImpl(this.getPeer(), var1, var2);
   }

   static native void removeAttributeNSImpl(long var0, String var2, String var3);

   public NodeList getElementsByTagNameNS(String var1, String var2) {
      return NodeListImpl.getImpl(getElementsByTagNameNSImpl(this.getPeer(), var1, var2));
   }

   static native long getElementsByTagNameNSImpl(long var0, String var2, String var3);

   public Attr getAttributeNodeNS(String var1, String var2) {
      return AttrImpl.getImpl(getAttributeNodeNSImpl(this.getPeer(), var1, var2));
   }

   static native long getAttributeNodeNSImpl(long var0, String var2, String var3);

   public Attr setAttributeNodeNS(Attr var1) throws DOMException {
      return AttrImpl.getImpl(setAttributeNodeNSImpl(this.getPeer(), AttrImpl.getPeer(var1)));
   }

   static native long setAttributeNodeNSImpl(long var0, long var2);

   public boolean hasAttribute(String var1) {
      return hasAttributeImpl(this.getPeer(), var1);
   }

   static native boolean hasAttributeImpl(long var0, String var2);

   public boolean hasAttributeNS(String var1, String var2) {
      return hasAttributeNSImpl(this.getPeer(), var1, var2);
   }

   static native boolean hasAttributeNSImpl(long var0, String var2, String var3);

   public void focus() {
      focusImpl(this.getPeer());
   }

   static native void focusImpl(long var0);

   public void blur() {
      blurImpl(this.getPeer());
   }

   static native void blurImpl(long var0);

   public void scrollIntoView(boolean var1) {
      scrollIntoViewImpl(this.getPeer(), var1);
   }

   static native void scrollIntoViewImpl(long var0, boolean var2);

   public void scrollIntoViewIfNeeded(boolean var1) {
      scrollIntoViewIfNeededImpl(this.getPeer(), var1);
   }

   static native void scrollIntoViewIfNeededImpl(long var0, boolean var2);

   public void scrollByLines(int var1) {
      scrollByLinesImpl(this.getPeer(), var1);
   }

   static native void scrollByLinesImpl(long var0, int var2);

   public void scrollByPages(int var1) {
      scrollByPagesImpl(this.getPeer(), var1);
   }

   static native void scrollByPagesImpl(long var0, int var2);

   public HTMLCollection getElementsByClassName(String var1) {
      return HTMLCollectionImpl.getImpl(getElementsByClassNameImpl(this.getPeer(), var1));
   }

   static native long getElementsByClassNameImpl(long var0, String var2);

   public boolean matches(String var1) throws DOMException {
      return matchesImpl(this.getPeer(), var1);
   }

   static native boolean matchesImpl(long var0, String var2);

   public Element closest(String var1) throws DOMException {
      return getImpl(closestImpl(this.getPeer(), var1));
   }

   static native long closestImpl(long var0, String var2);

   public boolean webkitMatchesSelector(String var1) throws DOMException {
      return webkitMatchesSelectorImpl(this.getPeer(), var1);
   }

   static native boolean webkitMatchesSelectorImpl(long var0, String var2);

   public void webkitRequestFullScreen(short var1) {
      webkitRequestFullScreenImpl(this.getPeer(), var1);
   }

   static native void webkitRequestFullScreenImpl(long var0, short var2);

   public void webkitRequestFullscreen() {
      webkitRequestFullscreenImpl(this.getPeer());
   }

   static native void webkitRequestFullscreenImpl(long var0);

   public void remove() throws DOMException {
      removeImpl(this.getPeer());
   }

   static native void removeImpl(long var0);

   public Element querySelector(String var1) throws DOMException {
      return getImpl(querySelectorImpl(this.getPeer(), var1));
   }

   static native long querySelectorImpl(long var0, String var2);

   public NodeList querySelectorAll(String var1) throws DOMException {
      return NodeListImpl.getImpl(querySelectorAllImpl(this.getPeer(), var1));
   }

   static native long querySelectorAllImpl(long var0, String var2);

   public void setIdAttribute(String var1, boolean var2) throws DOMException {
      throw new UnsupportedOperationException("Not supported yet.");
   }

   public void setIdAttributeNode(Attr var1, boolean var2) throws DOMException {
      throw new UnsupportedOperationException("Not supported yet.");
   }

   public TypeInfo getSchemaTypeInfo() {
      throw new UnsupportedOperationException("Not supported yet.");
   }

   public void setIdAttributeNS(String var1, String var2, boolean var3) throws DOMException {
      throw new UnsupportedOperationException("Not supported yet.");
   }
}
