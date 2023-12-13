package com.sun.webkit.dom;

import org.w3c.dom.Node;
import org.w3c.dom.ProcessingInstruction;
import org.w3c.dom.stylesheets.StyleSheet;

public class ProcessingInstructionImpl extends CharacterDataImpl implements ProcessingInstruction {
   ProcessingInstructionImpl(long var1) {
      super(var1);
   }

   static Node getImpl(long var0) {
      return create(var0);
   }

   public String getTarget() {
      return getTargetImpl(this.getPeer());
   }

   static native String getTargetImpl(long var0);

   public StyleSheet getSheet() {
      return StyleSheetImpl.getImpl(getSheetImpl(this.getPeer()));
   }

   static native long getSheetImpl(long var0);
}
