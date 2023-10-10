package org.reflections.scanners;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.Multimap;
import org.reflections.Configuration;
import org.reflections.ReflectionsException;
import org.reflections.adapters.MetadataAdapter;
import org.reflections.vfs.Vfs;

public abstract class AbstractScanner implements Scanner {
   private Configuration configuration;
   private Multimap store;
   private Predicate resultFilter = Predicates.alwaysTrue();

   public boolean acceptsInput(String var1) {
      return this.getMetadataAdapter().acceptsInput(var1);
   }

   public Object scan(Vfs.File var1, Object var2) {
      if (var2 == null) {
         try {
            var2 = this.configuration.getMetadataAdapter().getOfCreateClassObject(var1);
         } catch (Exception var4) {
            throw new ReflectionsException("could not create class object from file " + var1.getRelativePath());
         }
      }

      this.scan(var2);
      return var2;
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

   public boolean acceptResult(String var1) {
      return var1 != null && this.resultFilter.apply(var1);
   }

   protected MetadataAdapter getMetadataAdapter() {
      return this.configuration.getMetadataAdapter();
   }

   public boolean equals(Object var1) {
      return this == var1 || var1 != null && this.getClass() == var1.getClass();
   }

   public int hashCode() {
      return this.getClass().hashCode();
   }
}
