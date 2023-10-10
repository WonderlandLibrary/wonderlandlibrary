package org.jboss.errai.reflections.adapters;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public interface MetadataAdapter {
   String getClassName(Object var1);

   String getSuperclassName(Object var1);

   List getInterfacesNames(Object var1);

   List getFields(Object var1);

   List getMethods(Object var1);

   String getMethodName(Object var1);

   List getParameterNames(Object var1);

   List getClassAnnotationNames(Object var1);

   List getFieldAnnotationNames(Object var1);

   List getMethodAnnotationNames(Object var1);

   List getParameterAnnotationNames(Object var1, int var2);

   String getReturnTypeName(Object var1);

   String getFieldName(Object var1);

   Object createClassObject(InputStream var1) throws IOException;

   String getMethodModifier(Object var1);

   String getMethodKey(Object var1, Object var2);

   String getMethodFullKey(Object var1, Object var2);
}
