package com.sun.javafx.fxml;

public interface LoadListener {
   void readImportProcessingInstruction(String var1);

   void readLanguageProcessingInstruction(String var1);

   void readComment(String var1);

   void beginInstanceDeclarationElement(Class var1);

   void beginUnknownTypeElement(String var1);

   void beginIncludeElement();

   void beginReferenceElement();

   void beginCopyElement();

   void beginRootElement();

   void beginPropertyElement(String var1, Class var2);

   void beginUnknownStaticPropertyElement(String var1);

   void beginScriptElement();

   void beginDefineElement();

   void readInternalAttribute(String var1, String var2);

   void readPropertyAttribute(String var1, Class var2, String var3);

   void readUnknownStaticPropertyAttribute(String var1, String var2);

   void readEventHandlerAttribute(String var1, String var2);

   void endElement(Object var1);
}
