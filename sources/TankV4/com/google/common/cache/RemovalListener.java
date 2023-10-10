package com.google.common.cache;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;

@Beta
@GwtCompatible
public interface RemovalListener {
   void onRemoval(RemovalNotification var1);
}
