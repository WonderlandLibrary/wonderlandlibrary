package org.jboss.errai.reflections;

import java.util.Set;
import java.util.concurrent.ExecutorService;
import org.jboss.errai.reflections.adapters.MetadataAdapter;
import org.jboss.errai.reflections.serializers.Serializer;

public interface Configuration {
   Set getScanners();

   Set getUrls();

   MetadataAdapter getMetadataAdapter();

   boolean acceptsInput(String var1);

   ExecutorService getExecutorService();

   Serializer getSerializer();
}
