package org.reflections;

import com.google.common.base.Predicate;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import javax.annotation.Nullable;
import org.reflections.adapters.MetadataAdapter;
import org.reflections.serializers.Serializer;

public interface Configuration {
   Set getScanners();

   Set getUrls();

   MetadataAdapter getMetadataAdapter();

   @Nullable
   Predicate getInputsFilter();

   ExecutorService getExecutorService();

   Serializer getSerializer();

   @Nullable
   ClassLoader[] getClassLoaders();
}
