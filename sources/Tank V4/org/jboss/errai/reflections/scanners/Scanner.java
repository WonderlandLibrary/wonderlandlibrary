package org.jboss.errai.reflections.scanners;

import com.google.common.base.Predicate;
import com.google.common.collect.Multimap;
import org.jboss.errai.reflections.Configuration;
import org.jboss.errai.reflections.vfs.Vfs;

public interface Scanner {
   String getName();

   boolean acceptsInput(String var1);

   void scan(Vfs.File var1);

   Predicate getResultFilter();

   Scanner filterResultsBy(Predicate var1);

   void setConfiguration(Configuration var1);

   void setStore(Multimap var1);
}
