package com.google.common.base;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import java.io.Serializable;
import java.util.Iterator;
import java.util.Set;
import javax.annotation.Nullable;

@GwtCompatible(
   serializable = true
)
public abstract class Optional implements Serializable {
   private static final long serialVersionUID = 0L;

   public static Optional absent() {
      return Absent.withType();
   }

   public static Optional of(Object var0) {
      return new Present(Preconditions.checkNotNull(var0));
   }

   public static Optional fromNullable(@Nullable Object var0) {
      return (Optional)(var0 == null ? absent() : new Present(var0));
   }

   Optional() {
   }

   public abstract boolean isPresent();

   public abstract Object get();

   public abstract Object or(Object var1);

   public abstract Optional or(Optional var1);

   @Beta
   public abstract Object or(Supplier var1);

   @Nullable
   public abstract Object orNull();

   public abstract Set asSet();

   public abstract Optional transform(Function var1);

   public abstract boolean equals(@Nullable Object var1);

   public abstract int hashCode();

   public abstract String toString();

   @Beta
   public static Iterable presentInstances(Iterable var0) {
      Preconditions.checkNotNull(var0);
      return new Iterable(var0) {
         final Iterable val$optionals;

         {
            this.val$optionals = var1;
         }

         public Iterator iterator() {
            return new AbstractIterator(this) {
               private final Iterator iterator;
               final <undefinedtype> this$0;

               {
                  this.this$0 = var1;
                  this.iterator = (Iterator)Preconditions.checkNotNull(this.this$0.val$optionals.iterator());
               }

               protected Object computeNext() {
                  while(true) {
                     if (this.iterator.hasNext()) {
                        Optional var1 = (Optional)this.iterator.next();
                        if (!var1.isPresent()) {
                           continue;
                        }

                        return var1.get();
                     }

                     return this.endOfData();
                  }
               }
            };
         }
      };
   }
}
