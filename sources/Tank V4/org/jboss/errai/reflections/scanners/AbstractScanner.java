package org.jboss.errai.reflections.scanners;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.Multimap;
import java.io.IOException;
import java.io.InputStream;
import org.jboss.errai.reflections.Configuration;
import org.jboss.errai.reflections.ReflectionsException;
import org.jboss.errai.reflections.adapters.MetadataAdapter;
import org.jboss.errai.reflections.util.Utils;
import org.jboss.errai.reflections.vfs.Vfs;

public abstract class AbstractScanner implements Scanner {
   private Configuration configuration;
   private Multimap store;
   private Predicate resultFilter = Predicates.alwaysTrue();

   public String getName() {
      return this.getClass().getName();
   }

   public boolean acceptsInput(String var1) {
      return var1.endsWith(".class");
   }

   public void scan(Vfs.File var1) {
      InputStream var2 = null;

      try {
         var2 = var1.openInputStream();
         Object var3 = this.configuration.getMetadataAdapter().createClassObject(var2);
         this.scan(var3);
      } catch (IOException var5) {
         throw new ReflectionsException("could not create class file from " + var1.getName(), var5);
      }

      Utils.close(var2);
   }

   public abstract void scan(Object var1);

   public Configuration getConfiguration() {
      return this.configuration;
   }

   public void setConfiguration(Configuration var1) {
      this.configuration = var1;
   }

   public Multimap getStore() {
      return this.store;
   }

   public void setStore(Multimap var1) {
      this.store = var1;
   }

   public Predicate getResultFilter() {
      return this.resultFilter;
   }

   public void setResultFilter(Predicate var1) {
      this.resultFilter = var1;
   }

   public Scanner filterResultsBy(Predicate var1) {
      this.setResultFilter(var1);
      return this;
   }

   protected boolean acceptResult(String var1) {
      return var1 != null && this.getResultFilter().apply(var1);
   }

   protected MetadataAdapter getMetadataAdapter() {
      return this.configuration.getMetadataAdapter();
   }
}
