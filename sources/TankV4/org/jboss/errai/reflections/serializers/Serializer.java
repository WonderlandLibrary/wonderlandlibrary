package org.jboss.errai.reflections.serializers;

import java.io.File;
import java.io.InputStream;
import org.jboss.errai.reflections.Reflections;

public interface Serializer {
   Reflections read(InputStream var1);

   File save(Reflections var1, String var2);

   String toString(Reflections var1);
}
