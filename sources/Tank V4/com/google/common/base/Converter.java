package com.google.common.base;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import java.io.Serializable;
import java.util.Iterator;
import javax.annotation.Nullable;

@Beta
@GwtCompatible
public abstract class Converter implements Function {
   private final boolean handleNullAutomatically;
   private transient Converter reverse;

   protected Converter() {
      this(true);
   }

   Converter(boolean var1) {
      this.handleNullAutomatically = var1;
   }

   protected abstract Object doForward(Object var1);

   protected abstract Object doBackward(Object var1);

   @Nullable
   public final Object convert(@Nullable Object var1) {
      return this.correctedDoForward(var1);
   }

   @Nullable
   Object correctedDoForward(@Nullable Object var1) {
      if (this.handleNullAutomatically) {
         return var1 == null ? null : Preconditions.checkNotNull(this.doForward(var1));
      } else {
         return this.doForward(var1);
      }
   }

   @Nullable
   Object correctedDoBackward(@Nullable Object var1) {
      if (this.handleNullAutomatically) {
         return var1 == null ? null : Preconditions.checkNotNull(this.doBackward(var1));
      } else {
         return this.doBackward(var1);
      }
   }

   public Iterable convertAll(Iterable var1) {
      Preconditions.checkNotNull(var1, "fromIterable");
      return new Iterable(this, var1) {
         final Iterable val$fromIterable;
         final Converter this$0;

         {
            this.this$0 = var1;
            this.val$fromIterable = var2;
         }

         public Iterator iterator() {
            return new Iterator(this) {
               private final Iterator fromIterator;
               final <undefinedtype> this$1;

               {
                  this.this$1 = var1;
                  this.fromIterator = this.this$1.val$fromIterable.iterator();
               }

               public boolean hasNext() {
                  return this.fromIterator.hasNext();
               }

               public Object next() {
                  return this.this$1.this$0.convert(this.fromIterator.next());
               }

               public void remove() {
                  this.fromIterator.remove();
               }
            };
         }
      };
   }

   public Converter reverse() {
      Converter var1 = this.reverse;
      return var1 == null ? (this.reverse = new Converter.ReverseConverter(this)) : var1;
   }

   public Converter andThen(Converter var1) {
      return new Converter.ConverterComposition(this, (Converter)Preconditions.checkNotNull(var1));
   }

   /** @deprecated */
   @Deprecated
   @Nullable
   public final Object apply(@Nullable Object var1) {
      return this.convert(var1);
   }

   public boolean equals(@Nullable Object var1) {
      return super.equals(var1);
   }

   public static Converter from(Function var0, Function var1) {
      return new Converter.FunctionBasedConverter(var0, var1);
   }

   public static Converter identity() {
      return Converter.IdentityConverter.INSTANCE;
   }

   private static final class IdentityConverter extends Converter implements Serializable {
      static final Converter.IdentityConverter INSTANCE = new Converter.IdentityConverter();
      private static final long serialVersionUID = 0L;

      protected Object doForward(Object var1) {
         return var1;
      }

      protected Object doBackward(Object var1) {
         return var1;
      }

      public Converter.IdentityConverter reverse() {
         return this;
      }

      public Converter andThen(Converter var1) {
         return (Converter)Preconditions.checkNotNull(var1, "otherConverter");
      }

      public String toString() {
         return "Converter.identity()";
      }

      private Object readResolve() {
         return INSTANCE;
      }

      public Converter reverse() {
         return this.reverse();
      }
   }

   private static final class FunctionBasedConverter extends Converter implements Serializable {
      private final Function forwardFunction;
      private final Function backwardFunction;

      private FunctionBasedConverter(Function var1, Function var2) {
         this.forwardFunction = (Function)Preconditions.checkNotNull(var1);
         this.backwardFunction = (Function)Preconditions.checkNotNull(var2);
      }

      protected Object doForward(Object var1) {
         return this.forwardFunction.apply(var1);
      }

      protected Object doBackward(Object var1) {
         return this.backwardFunction.apply(var1);
      }

      public boolean equals(@Nullable Object var1) {
         if (!(var1 instanceof Converter.FunctionBasedConverter)) {
            return false;
         } else {
            Converter.FunctionBasedConverter var2 = (Converter.FunctionBasedConverter)var1;
            return this.forwardFunction.equals(var2.forwardFunction) && this.backwardFunction.equals(var2.backwardFunction);
         }
      }

      public int hashCode() {
         return this.forwardFunction.hashCode() * 31 + this.backwardFunction.hashCode();
      }

      public String toString() {
         return "Converter.from(" + this.forwardFunction + ", " + this.backwardFunction + ")";
      }

      FunctionBasedConverter(Function var1, Function var2, Object var3) {
         this(var1, var2);
      }
   }

   private static final class ConverterComposition extends Converter implements Serializable {
      final Converter first;
      final Converter second;
      private static final long serialVersionUID = 0L;

      ConverterComposition(Converter var1, Converter var2) {
         this.first = var1;
         this.second = var2;
      }

      protected Object doForward(Object var1) {
         throw new AssertionError();
      }

      protected Object doBackward(Object var1) {
         throw new AssertionError();
      }

      @Nullable
      Object correctedDoForward(@Nullable Object var1) {
         return this.second.correctedDoForward(this.first.correctedDoForward(var1));
      }

      @Nullable
      Object correctedDoBackward(@Nullable Object var1) {
         return this.first.correctedDoBackward(this.second.correctedDoBackward(var1));
      }

      public boolean equals(@Nullable Object var1) {
         if (!(var1 instanceof Converter.ConverterComposition)) {
            return false;
         } else {
            Converter.ConverterComposition var2 = (Converter.ConverterComposition)var1;
            return this.first.equals(var2.first) && this.second.equals(var2.second);
         }
      }

      public int hashCode() {
         return 31 * this.first.hashCode() + this.second.hashCode();
      }

      public String toString() {
         return this.first + ".andThen(" + this.second + ")";
      }
   }

   private static final class ReverseConverter extends Converter implements Serializable {
      final Converter original;
      private static final long serialVersionUID = 0L;

      ReverseConverter(Converter var1) {
         this.original = var1;
      }

      protected Object doForward(Object var1) {
         throw new AssertionError();
      }

      protected Object doBackward(Object var1) {
         throw new AssertionError();
      }

      @Nullable
      Object correctedDoForward(@Nullable Object var1) {
         return this.original.correctedDoBackward(var1);
      }

      @Nullable
      Object correctedDoBackward(@Nullable Object var1) {
         return this.original.correctedDoForward(var1);
      }

      public Converter reverse() {
         return this.original;
      }

      public boolean equals(@Nullable Object var1) {
         if (var1 instanceof Converter.ReverseConverter) {
            Converter.ReverseConverter var2 = (Converter.ReverseConverter)var1;
            return this.original.equals(var2.original);
         } else {
            return false;
         }
      }

      public int hashCode() {
         return ~this.original.hashCode();
      }

      public String toString() {
         return this.original + ".reverse()";
      }
   }
}
