package org.jboss.errai.reflections.util;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.Sets;
import java.net.URL;
import java.util.Arrays;
import java.util.Collection;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.jboss.errai.reflections.Configuration;
import org.jboss.errai.reflections.adapters.JavassistAdapter;
import org.jboss.errai.reflections.adapters.MetadataAdapter;
import org.jboss.errai.reflections.scanners.Scanner;
import org.jboss.errai.reflections.scanners.SubTypesScanner;
import org.jboss.errai.reflections.scanners.TypeAnnotationsScanner;
import org.jboss.errai.reflections.serializers.Serializer;
import org.jboss.errai.reflections.serializers.XmlSerializer;

public class ConfigurationBuilder implements Configuration {
   private final Set scanners = Sets.newHashSet((Object[])(new TypeAnnotationsScanner(), new SubTypesScanner()));
   private Set urls = Sets.newHashSet();
   private MetadataAdapter metadataAdapter = new JavassistAdapter();
   private Predicate inputsFilter = Predicates.alwaysTrue();
   private Serializer serializer;
   private ExecutorService executorService;

   public Set getScanners() {
      return this.scanners;
   }

   public ConfigurationBuilder setScanners(Scanner... var1) {
      this.scanners.addAll(Arrays.asList(var1));
      return this;
   }

   public Set getUrls() {
      return this.urls;
   }

   public ConfigurationBuilder setUrls(Collection var1) {
      this.urls = Sets.newHashSet((Iterable)var1);
      return this;
   }

   public ConfigurationBuilder setUrls(URL... var1) {
      this.urls = Sets.newHashSet((Object[])var1);
      return this;
   }

   public ConfigurationBuilder setUrls(Collection... var1) {
      this.urls.clear();
      this.addUrls(var1);
      return this;
   }

   public ConfigurationBuilder addUrls(Collection var1) {
      this.urls.addAll(var1);
      return this;
   }

   public ConfigurationBuilder addUrls(URL... var1) {
      this.urls.addAll(Sets.newHashSet((Object[])var1));
      return this;
   }

   public ConfigurationBuilder addUrls(Collection... var1) {
      Collection[] var2 = var1;
      int var3 = var1.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         Collection var5 = var2[var4];
         this.addUrls(var5);
      }

      return this;
   }

   public MetadataAdapter getMetadataAdapter() {
      return this.metadataAdapter;
   }

   public ConfigurationBuilder setMetadataAdapter(MetadataAdapter var1) {
      this.metadataAdapter = var1;
      return this;
   }

   public boolean acceptsInput(String var1) {
      return this.inputsFilter.apply(var1);
   }

   public ConfigurationBuilder filterInputsBy(Predicate var1) {
      this.inputsFilter = var1;
      return this;
   }

   public ExecutorService getExecutorService() {
      return this.executorService;
   }

   public ConfigurationBuilder setExecutorService(ExecutorService var1) {
      this.executorService = var1;
      return this;
   }

   public ConfigurationBuilder useParallelExecutor() {
      return this.useParallelExecutor(Runtime.getRuntime().availableProcessors());
   }

   public ConfigurationBuilder useParallelExecutor(int var1) {
      this.setExecutorService(Executors.newFixedThreadPool(var1));
      return this;
   }

   public Serializer getSerializer() {
      if (this.serializer == null) {
         this.serializer = new XmlSerializer();
      }

      return this.serializer;
   }

   public ConfigurationBuilder setSerializer(Serializer var1) {
      this.serializer = var1;
      return this;
   }
}
