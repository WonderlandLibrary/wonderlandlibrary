package com.sun.webkit.dom;

import com.sun.webkit.Disposer;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.UserDataHandler;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.EventTarget;

public class NodeImpl extends JSObject implements Node, EventTarget {
   private static SelfDisposer[] hashTable = new SelfDisposer[64];
   private static int hashCount;
   public static final int ELEMENT_NODE = 1;
   public static final int ATTRIBUTE_NODE = 2;
   public static final int TEXT_NODE = 3;
   public static final int CDATA_SECTION_NODE = 4;
   public static final int ENTITY_REFERENCE_NODE = 5;
   public static final int ENTITY_NODE = 6;
   public static final int PROCESSING_INSTRUCTION_NODE = 7;
   public static final int COMMENT_NODE = 8;
   public static final int DOCUMENT_NODE = 9;
   public static final int DOCUMENT_TYPE_NODE = 10;
   public static final int DOCUMENT_FRAGMENT_NODE = 11;
   public static final int NOTATION_NODE = 12;
   public static final int DOCUMENT_POSITION_DISCONNECTED = 1;
   public static final int DOCUMENT_POSITION_PRECEDING = 2;
   public static final int DOCUMENT_POSITION_FOLLOWING = 4;
   public static final int DOCUMENT_POSITION_CONTAINS = 8;
   public static final int DOCUMENT_POSITION_CONTAINED_BY = 16;
   public static final int DOCUMENT_POSITION_IMPLEMENTATION_SPECIFIC = 32;

   private static int hashPeer(long var0) {
      return (int)(~var0 ^ var0 >> 7) & hashTable.length - 1;
   }

   private static Node getCachedImpl(long var0) {
      if (var0 == 0L) {
         return null;
      } else {
         int var2 = hashPeer(var0);
         SelfDisposer var3 = hashTable[var2];
         SelfDisposer var4 = null;

         SelfDisposer var6;
         for(SelfDisposer var5 = var3; var5 != null; var5 = var6) {
            var6 = var5.next;
            if (var5.peer == var0) {
               NodeImpl var7 = (NodeImpl)var5.get();
               if (var7 != null) {
                  dispose(var0);
                  return var7;
               }

               if (var4 != null) {
                  var4.next = var6;
               } else {
                  hashTable[var2] = var6;
               }
               break;
            }

            var4 = var5;
         }

         NodeImpl var8 = (NodeImpl)createInterface(var0);
         var6 = new SelfDisposer(var8, var0);
         Disposer.addRecord(var6);
         var6.next = var3;
         hashTable[var2] = var6;
         if (3 * hashCount >= 2 * hashTable.length) {
            rehash();
         }

         ++hashCount;
         return var8;
      }
   }

   static int test_getHashCount() {
      return hashCount;
   }

   private static void rehash() {
      SelfDisposer[] var0 = hashTable;
      int var1 = var0.length;
      SelfDisposer[] var2 = new SelfDisposer[2 * var1];
      hashTable = var2;
      int var3 = var1;

      while(true) {
         --var3;
         if (var3 < 0) {
            return;
         }

         SelfDisposer var5;
         for(SelfDisposer var4 = var0[var3]; var4 != null; var4 = var5) {
            var5 = var4.next;
            int var6 = hashPeer(var4.peer);
            var4.next = var2[var6];
            var2[var6] = var4;
         }
      }
   }

   NodeImpl(long var1) {
      super(var1, 1);
   }

   static Node createInterface(long var0) {
      // $FF: Couldn't be decompiled
   }

   static Node create(long var0) {
      return getCachedImpl(var0);
   }

   static long getPeer(Node var0) {
      return var0 == null ? 0L : ((NodeImpl)var0).getPeer();
   }

   private static native void dispose(long var0);

   static Node getImpl(long var0) {
      return create(var0);
   }

   public String getNodeName() {
      return getNodeNameImpl(this.getPeer());
   }

   static native String getNodeNameImpl(long var0);

   public String getNodeValue() {
      return getNodeValueImpl(this.getPeer());
   }

   static native String getNodeValueImpl(long var0);

   public void setNodeValue(String var1) throws DOMException {
      setNodeValueImpl(this.getPeer(), var1);
   }

   static native void setNodeValueImpl(long var0, String var2);

   public short getNodeType() {
      return getNodeTypeImpl(this.getPeer());
   }

   static native short getNodeTypeImpl(long var0);

   public Node getParentNode() {
      return getImpl(getParentNodeImpl(this.getPeer()));
   }

   static native long getParentNodeImpl(long var0);

   public NodeList getChildNodes() {
      return NodeListImpl.getImpl(getChildNodesImpl(this.getPeer()));
   }

   static native long getChildNodesImpl(long var0);

   public Node getFirstChild() {
      return getImpl(getFirstChildImpl(this.getPeer()));
   }

   static native long getFirstChildImpl(long var0);

   public Node getLastChild() {
      return getImpl(getLastChildImpl(this.getPeer()));
   }

   static native long getLastChildImpl(long var0);

   public Node getPreviousSibling() {
      return getImpl(getPreviousSiblingImpl(this.getPeer()));
   }

   static native long getPreviousSiblingImpl(long var0);

   public Node getNextSibling() {
      return getImpl(getNextSiblingImpl(this.getPeer()));
   }

   static native long getNextSiblingImpl(long var0);

   public Document getOwnerDocument() {
      return DocumentImpl.getImpl(getOwnerDocumentImpl(this.getPeer()));
   }

   static native long getOwnerDocumentImpl(long var0);

   public String getNamespaceURI() {
      return getNamespaceURIImpl(this.getPeer());
   }

   static native String getNamespaceURIImpl(long var0);

   public String getPrefix() {
      return getPrefixImpl(this.getPeer());
   }

   static native String getPrefixImpl(long var0);

   public void setPrefix(String var1) throws DOMException {
      setPrefixImpl(this.getPeer(), var1);
   }

   static native void setPrefixImpl(long var0, String var2);

   public String getLocalName() {
      return getLocalNameImpl(this.getPeer());
   }

   static native String getLocalNameImpl(long var0);

   public NamedNodeMap getAttributes() {
      return NamedNodeMapImpl.getImpl(getAttributesImpl(this.getPeer()));
   }

   static native long getAttributesImpl(long var0);

   public String getBaseURI() {
      return getBaseURIImpl(this.getPeer());
   }

   static native String getBaseURIImpl(long var0);

   public String getTextContent() {
      return getTextContentImpl(this.getPeer());
   }

   static native String getTextContentImpl(long var0);

   public void setTextContent(String var1) throws DOMException {
      setTextContentImpl(this.getPeer(), var1);
   }

   static native void setTextContentImpl(long var0, String var2);

   public Element getParentElement() {
      return ElementImpl.getImpl(getParentElementImpl(this.getPeer()));
   }

   static native long getParentElementImpl(long var0);

   public Node insertBefore(Node var1, Node var2) throws DOMException {
      return getImpl(insertBeforeImpl(this.getPeer(), getPeer(var1), getPeer(var2)));
   }

   static native long insertBeforeImpl(long var0, long var2, long var4);

   public Node replaceChild(Node var1, Node var2) throws DOMException {
      return getImpl(replaceChildImpl(this.getPeer(), getPeer(var1), getPeer(var2)));
   }

   static native long replaceChildImpl(long var0, long var2, long var4);

   public Node removeChild(Node var1) throws DOMException {
      return getImpl(removeChildImpl(this.getPeer(), getPeer(var1)));
   }

   static native long removeChildImpl(long var0, long var2);

   public Node appendChild(Node var1) throws DOMException {
      return getImpl(appendChildImpl(this.getPeer(), getPeer(var1)));
   }

   static native long appendChildImpl(long var0, long var2);

   public boolean hasChildNodes() {
      return hasChildNodesImpl(this.getPeer());
   }

   static native boolean hasChildNodesImpl(long var0);

   public Node cloneNode(boolean var1) throws DOMException {
      return getImpl(cloneNodeImpl(this.getPeer(), var1));
   }

   static native long cloneNodeImpl(long var0, boolean var2);

   public void normalize() {
      normalizeImpl(this.getPeer());
   }

   static native void normalizeImpl(long var0);

   public boolean isSupported(String var1, String var2) {
      return isSupportedImpl(this.getPeer(), var1, var2);
   }

   static native boolean isSupportedImpl(long var0, String var2, String var3);

   public boolean hasAttributes() {
      return hasAttributesImpl(this.getPeer());
   }

   static native boolean hasAttributesImpl(long var0);

   public boolean isSameNode(Node var1) {
      return isSameNodeImpl(this.getPeer(), getPeer(var1));
   }

   static native boolean isSameNodeImpl(long var0, long var2);

   public boolean isEqualNode(Node var1) {
      return isEqualNodeImpl(this.getPeer(), getPeer(var1));
   }

   static native boolean isEqualNodeImpl(long var0, long var2);

   public String lookupPrefix(String var1) {
      return lookupPrefixImpl(this.getPeer(), var1);
   }

   static native String lookupPrefixImpl(long var0, String var2);

   public boolean isDefaultNamespace(String var1) {
      return isDefaultNamespaceImpl(this.getPeer(), var1);
   }

   static native boolean isDefaultNamespaceImpl(long var0, String var2);

   public String lookupNamespaceURI(String var1) {
      return lookupNamespaceURIImpl(this.getPeer(), var1);
   }

   static native String lookupNamespaceURIImpl(long var0, String var2);

   public short compareDocumentPosition(Node var1) {
      return compareDocumentPositionImpl(this.getPeer(), getPeer(var1));
   }

   static native short compareDocumentPositionImpl(long var0, long var2);

   public boolean contains(Node var1) {
      return containsImpl(this.getPeer(), getPeer(var1));
   }

   static native boolean containsImpl(long var0, long var2);

   public void addEventListener(String var1, EventListener var2, boolean var3) {
      addEventListenerImpl(this.getPeer(), var1, EventListenerImpl.getPeer(var2), var3);
   }

   static native void addEventListenerImpl(long var0, String var2, long var3, boolean var5);

   public void removeEventListener(String var1, EventListener var2, boolean var3) {
      removeEventListenerImpl(this.getPeer(), var1, EventListenerImpl.getPeer(var2), var3);
   }

   static native void removeEventListenerImpl(long var0, String var2, long var3, boolean var5);

   public boolean dispatchEvent(Event var1) throws DOMException {
      return dispatchEventImpl(this.getPeer(), EventImpl.getPeer(var1));
   }

   static native boolean dispatchEventImpl(long var0, long var2);

   public Object getUserData(String var1) {
      throw new UnsupportedOperationException("Not supported yet.");
   }

   public Object setUserData(String var1, Object var2, UserDataHandler var3) {
      throw new UnsupportedOperationException("Not supported yet.");
   }

   public Object getFeature(String var1, String var2) {
      throw new UnsupportedOperationException("Not supported yet.");
   }

   private static final class SelfDisposer extends Disposer.WeakDisposerRecord {
      private final long peer;
      SelfDisposer next;

      SelfDisposer(Object var1, long var2) {
         super(var1);
         this.peer = var2;
      }

      public void dispose() {
         int var1 = NodeImpl.hashPeer(this.peer);
         SelfDisposer var2 = NodeImpl.hashTable[var1];
         SelfDisposer var3 = null;

         SelfDisposer var5;
         for(SelfDisposer var4 = var2; var4 != null; var4 = var5) {
            var5 = var4.next;
            if (var4.peer == this.peer) {
               var4.clear();
               if (var3 != null) {
                  var3.next = var5;
               } else {
                  NodeImpl.hashTable[var1] = var5;
               }

               NodeImpl.hashCount--;
               break;
            }

            var3 = var4;
         }

         NodeImpl.dispose(this.peer);
      }
   }
}
