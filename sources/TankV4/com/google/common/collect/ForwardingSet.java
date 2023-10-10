package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;
import java.util.Collection;
import java.util.Set;
import javax.annotation.Nullable;

@GwtCompatible
public abstract class ForwardingSet extends ForwardingCollection implements Set {
   protected ForwardingSet() {
   }

   protected abstract Set delegate();

   public boolean equals(@Nullable Object var1) {
      return var1 == this || this.delegate().equals(var1);
   }

   public int hashCode() {
      return this.delegate().hashCode();
   }

   protected boolean standardRemoveAll(Collection var1) {
      return Sets.removeAllImpl(this, (Collection)((Collection)Preconditions.checkNotNull(var1)));
   }

   protected boolean standardEquals(@Nullable Object var1) {
      return Sets.equalsImpl(this, var1);
   }

   protected int standardHashCode() {
      return Sets.hashCodeImpl(this);
   }

   protected Collection delegate() {
      return this.delegate();
   }

   protected Object delegate() {
      return this.delegate();
   }
}
