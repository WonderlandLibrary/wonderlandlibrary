package com.google.common.collect;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.NavigableMap;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.TreeMap;
import java.util.Map.Entry;
import javax.annotation.Nullable;

@Beta
@GwtIncompatible("uses NavigableMap")
public class TreeRangeSet extends AbstractRangeSet {
   @VisibleForTesting
   final NavigableMap rangesByLowerBound;
   private transient Set asRanges;
   private transient RangeSet complement;

   public static TreeRangeSet create() {
      return new TreeRangeSet(new TreeMap());
   }

   public static TreeRangeSet create(RangeSet var0) {
      TreeRangeSet var1 = create();
      var1.addAll(var0);
      return var1;
   }

   private TreeRangeSet(NavigableMap var1) {
      this.rangesByLowerBound = var1;
   }

   public Set asRanges() {
      Set var1 = this.asRanges;
      return var1 == null ? (this.asRanges = new TreeRangeSet.AsRanges(this)) : var1;
   }

   @Nullable
   public Range rangeContaining(Comparable var1) {
      Preconditions.checkNotNull(var1);
      Entry var2 = this.rangesByLowerBound.floorEntry(Cut.belowValue(var1));
      return var2 != null && ((Range)var2.getValue()).contains(var1) ? (Range)var2.getValue() : null;
   }

   public boolean encloses(Range var1) {
      Preconditions.checkNotNull(var1);
      Entry var2 = this.rangesByLowerBound.floorEntry(var1.lowerBound);
      return var2 != null && ((Range)var2.getValue()).encloses(var1);
   }

   @Nullable
   private Range rangeEnclosing(Range var1) {
      Preconditions.checkNotNull(var1);
      Entry var2 = this.rangesByLowerBound.floorEntry(var1.lowerBound);
      return var2 != null && ((Range)var2.getValue()).encloses(var1) ? (Range)var2.getValue() : null;
   }

   public Range span() {
      Entry var1 = this.rangesByLowerBound.firstEntry();
      Entry var2 = this.rangesByLowerBound.lastEntry();
      if (var1 == null) {
         throw new NoSuchElementException();
      } else {
         return Range.create(((Range)var1.getValue()).lowerBound, ((Range)var2.getValue()).upperBound);
      }
   }

   public void add(Range var1) {
      Preconditions.checkNotNull(var1);
      if (!var1.isEmpty()) {
         Cut var2 = var1.lowerBound;
         Cut var3 = var1.upperBound;
         Entry var4 = this.rangesByLowerBound.lowerEntry(var2);
         if (var4 != null) {
            Range var5 = (Range)var4.getValue();
            if (var5.upperBound.compareTo(var2) >= 0) {
               if (var5.upperBound.compareTo(var3) >= 0) {
                  var3 = var5.upperBound;
               }

               var2 = var5.lowerBound;
            }
         }

         Entry var7 = this.rangesByLowerBound.floorEntry(var3);
         if (var7 != null) {
            Range var6 = (Range)var7.getValue();
            if (var6.upperBound.compareTo(var3) >= 0) {
               var3 = var6.upperBound;
            }
         }

         this.rangesByLowerBound.subMap(var2, var3).clear();
         this.replaceRangeWithSameLowerBound(Range.create(var2, var3));
      }
   }

   public void remove(Range var1) {
      Preconditions.checkNotNull(var1);
      if (!var1.isEmpty()) {
         Entry var2 = this.rangesByLowerBound.lowerEntry(var1.lowerBound);
         if (var2 != null) {
            Range var3 = (Range)var2.getValue();
            if (var3.upperBound.compareTo(var1.lowerBound) >= 0) {
               if (var1.hasUpperBound() && var3.upperBound.compareTo(var1.upperBound) >= 0) {
                  this.replaceRangeWithSameLowerBound(Range.create(var1.upperBound, var3.upperBound));
               }

               this.replaceRangeWithSameLowerBound(Range.create(var3.lowerBound, var1.lowerBound));
            }
         }

         Entry var5 = this.rangesByLowerBound.floorEntry(var1.upperBound);
         if (var5 != null) {
            Range var4 = (Range)var5.getValue();
            if (var1.hasUpperBound() && var4.upperBound.compareTo(var1.upperBound) >= 0) {
               this.replaceRangeWithSameLowerBound(Range.create(var1.upperBound, var4.upperBound));
            }
         }

         this.rangesByLowerBound.subMap(var1.lowerBound, var1.upperBound).clear();
      }
   }

   private void replaceRangeWithSameLowerBound(Range var1) {
      if (var1.isEmpty()) {
         this.rangesByLowerBound.remove(var1.lowerBound);
      } else {
         this.rangesByLowerBound.put(var1.lowerBound, var1);
      }

   }

   public RangeSet complement() {
      RangeSet var1 = this.complement;
      return var1 == null ? (this.complement = new TreeRangeSet.Complement(this)) : var1;
   }

   public RangeSet subRangeSet(Range var1) {
      return (RangeSet)(var1.equals(Range.all()) ? this : new TreeRangeSet.SubRangeSet(this, var1));
   }

   public boolean equals(Object var1) {
      return super.equals(var1);
   }

   public void removeAll(RangeSet var1) {
      super.removeAll(var1);
   }

   public void addAll(RangeSet var1) {
      super.addAll(var1);
   }

   public boolean enclosesAll(RangeSet var1) {
      return super.enclosesAll(var1);
   }

   public void clear() {
      super.clear();
   }

   public boolean isEmpty() {
      return super.isEmpty();
   }

   public boolean contains(Comparable var1) {
      return super.contains(var1);
   }

   TreeRangeSet(NavigableMap var1, Object var2) {
      this(var1);
   }

   static Range access$600(TreeRangeSet var0, Range var1) {
      return var0.rangeEnclosing(var1);
   }

   private final class SubRangeSet extends TreeRangeSet {
      private final Range restriction;
      final TreeRangeSet this$0;

      SubRangeSet(TreeRangeSet var1, Range var2) {
         super(new TreeRangeSet.SubRangeSetRangesByLowerBound(Range.all(), var2, var1.rangesByLowerBound), null);
         this.this$0 = var1;
         this.restriction = var2;
      }

      public boolean encloses(Range var1) {
         if (!this.restriction.isEmpty() && this.restriction.encloses(var1)) {
            Range var2 = TreeRangeSet.access$600(this.this$0, var1);
            return var2 != null && !var2.intersection(this.restriction).isEmpty();
         } else {
            return false;
         }
      }

      @Nullable
      public Range rangeContaining(Comparable var1) {
         if (!this.restriction.contains(var1)) {
            return null;
         } else {
            Range var2 = this.this$0.rangeContaining(var1);
            return var2 == null ? null : var2.intersection(this.restriction);
         }
      }

      public void add(Range var1) {
         Preconditions.checkArgument(this.restriction.encloses(var1), "Cannot add range %s to subRangeSet(%s)", var1, this.restriction);
         super.add(var1);
      }

      public void remove(Range var1) {
         if (var1.isConnected(this.restriction)) {
            this.this$0.remove(var1.intersection(this.restriction));
         }

      }

      public boolean contains(Comparable var1) {
         return this.restriction.contains(var1) && this.this$0.contains(var1);
      }

      public void clear() {
         this.this$0.remove(this.restriction);
      }

      public RangeSet subRangeSet(Range var1) {
         if (var1.encloses(this.restriction)) {
            return this;
         } else {
            return (RangeSet)(var1.isConnected(this.restriction) ? new TreeRangeSet.SubRangeSet(this, this.restriction.intersection(var1)) : ImmutableRangeSet.of());
         }
      }
   }

   private static final class SubRangeSetRangesByLowerBound extends AbstractNavigableMap {
      private final Range lowerBoundWindow;
      private final Range restriction;
      private final NavigableMap rangesByLowerBound;
      private final NavigableMap rangesByUpperBound;

      private SubRangeSetRangesByLowerBound(Range var1, Range var2, NavigableMap var3) {
         this.lowerBoundWindow = (Range)Preconditions.checkNotNull(var1);
         this.restriction = (Range)Preconditions.checkNotNull(var2);
         this.rangesByLowerBound = (NavigableMap)Preconditions.checkNotNull(var3);
         this.rangesByUpperBound = new TreeRangeSet.RangesByUpperBound(var3);
      }

      private NavigableMap subMap(Range var1) {
         return (NavigableMap)(!var1.isConnected(this.lowerBoundWindow) ? ImmutableSortedMap.of() : new TreeRangeSet.SubRangeSetRangesByLowerBound(this.lowerBoundWindow.intersection(var1), this.restriction, this.rangesByLowerBound));
      }

      public NavigableMap subMap(Cut var1, boolean var2, Cut var3, boolean var4) {
         return this.subMap(Range.range(var1, BoundType.forBoolean(var2), var3, BoundType.forBoolean(var4)));
      }

      public NavigableMap headMap(Cut var1, boolean var2) {
         return this.subMap(Range.upTo(var1, BoundType.forBoolean(var2)));
      }

      public NavigableMap tailMap(Cut var1, boolean var2) {
         return this.subMap(Range.downTo(var1, BoundType.forBoolean(var2)));
      }

      public Comparator comparator() {
         return Ordering.natural();
      }

      public boolean containsKey(@Nullable Object var1) {
         return this.get(var1) != null;
      }

      @Nullable
      public Range get(@Nullable Object var1) {
         if (var1 instanceof Cut) {
            try {
               Cut var2 = (Cut)var1;
               if (!this.lowerBoundWindow.contains(var2) || var2.compareTo(this.restriction.lowerBound) < 0 || var2.compareTo(this.restriction.upperBound) >= 0) {
                  return null;
               }

               Range var3;
               if (var2.equals(this.restriction.lowerBound)) {
                  var3 = (Range)Maps.valueOrNull(this.rangesByLowerBound.floorEntry(var2));
                  if (var3 != null && var3.upperBound.compareTo(this.restriction.lowerBound) > 0) {
                     return var3.intersection(this.restriction);
                  }
               } else {
                  var3 = (Range)this.rangesByLowerBound.get(var2);
                  if (var3 != null) {
                     return var3.intersection(this.restriction);
                  }
               }
            } catch (ClassCastException var4) {
               return null;
            }
         }

         return null;
      }

      Iterator entryIterator() {
         if (this.restriction.isEmpty()) {
            return Iterators.emptyIterator();
         } else if (this.lowerBoundWindow.upperBound.isLessThan(this.restriction.lowerBound)) {
            return Iterators.emptyIterator();
         } else {
            Iterator var1;
            if (this.lowerBoundWindow.lowerBound.isLessThan(this.restriction.lowerBound)) {
               var1 = this.rangesByUpperBound.tailMap(this.restriction.lowerBound, false).values().iterator();
            } else {
               var1 = this.rangesByLowerBound.tailMap(this.lowerBoundWindow.lowerBound.endpoint(), this.lowerBoundWindow.lowerBoundType() == BoundType.CLOSED).values().iterator();
            }

            Cut var2 = (Cut)Ordering.natural().min(this.lowerBoundWindow.upperBound, Cut.belowValue(this.restriction.upperBound));
            return new AbstractIterator(this, var1, var2) {
               final Iterator val$completeRangeItr;
               final Cut val$upperBoundOnLowerBounds;
               final TreeRangeSet.SubRangeSetRangesByLowerBound this$0;

               {
                  this.this$0 = var1;
                  this.val$completeRangeItr = var2;
                  this.val$upperBoundOnLowerBounds = var3;
               }

               protected Entry computeNext() {
                  if (!this.val$completeRangeItr.hasNext()) {
                     return (Entry)this.endOfData();
                  } else {
                     Range var1 = (Range)this.val$completeRangeItr.next();
                     if (this.val$upperBoundOnLowerBounds.isLessThan(var1.lowerBound)) {
                        return (Entry)this.endOfData();
                     } else {
                        var1 = var1.intersection(TreeRangeSet.SubRangeSetRangesByLowerBound.access$300(this.this$0));
                        return Maps.immutableEntry(var1.lowerBound, var1);
                     }
                  }
               }

               protected Object computeNext() {
                  return this.computeNext();
               }
            };
         }
      }

      Iterator descendingEntryIterator() {
         if (this.restriction.isEmpty()) {
            return Iterators.emptyIterator();
         } else {
            Cut var1 = (Cut)Ordering.natural().min(this.lowerBoundWindow.upperBound, Cut.belowValue(this.restriction.upperBound));
            Iterator var2 = this.rangesByLowerBound.headMap(var1.endpoint(), var1.typeAsUpperBound() == BoundType.CLOSED).descendingMap().values().iterator();
            return new AbstractIterator(this, var2) {
               final Iterator val$completeRangeItr;
               final TreeRangeSet.SubRangeSetRangesByLowerBound this$0;

               {
                  this.this$0 = var1;
                  this.val$completeRangeItr = var2;
               }

               protected Entry computeNext() {
                  if (!this.val$completeRangeItr.hasNext()) {
                     return (Entry)this.endOfData();
                  } else {
                     Range var1 = (Range)this.val$completeRangeItr.next();
                     if (TreeRangeSet.SubRangeSetRangesByLowerBound.access$300(this.this$0).lowerBound.compareTo(var1.upperBound) >= 0) {
                        return (Entry)this.endOfData();
                     } else {
                        var1 = var1.intersection(TreeRangeSet.SubRangeSetRangesByLowerBound.access$300(this.this$0));
                        return TreeRangeSet.SubRangeSetRangesByLowerBound.access$400(this.this$0).contains(var1.lowerBound) ? Maps.immutableEntry(var1.lowerBound, var1) : (Entry)this.endOfData();
                     }
                  }
               }

               protected Object computeNext() {
                  return this.computeNext();
               }
            };
         }
      }

      public int size() {
         return Iterators.size(this.entryIterator());
      }

      public Object get(Object var1) {
         return this.get(var1);
      }

      public NavigableMap tailMap(Object var1, boolean var2) {
         return this.tailMap((Cut)var1, var2);
      }

      public NavigableMap headMap(Object var1, boolean var2) {
         return this.headMap((Cut)var1, var2);
      }

      public NavigableMap subMap(Object var1, boolean var2, Object var3, boolean var4) {
         return this.subMap((Cut)var1, var2, (Cut)var3, var4);
      }

      static Range access$300(TreeRangeSet.SubRangeSetRangesByLowerBound var0) {
         return var0.restriction;
      }

      static Range access$400(TreeRangeSet.SubRangeSetRangesByLowerBound var0) {
         return var0.lowerBoundWindow;
      }

      SubRangeSetRangesByLowerBound(Range var1, Range var2, NavigableMap var3, Object var4) {
         this(var1, var2, var3);
      }
   }

   private final class Complement extends TreeRangeSet {
      final TreeRangeSet this$0;

      Complement(TreeRangeSet var1) {
         super(new TreeRangeSet.ComplementRangesByLowerBound(var1.rangesByLowerBound), null);
         this.this$0 = var1;
      }

      public void add(Range var1) {
         this.this$0.remove(var1);
      }

      public void remove(Range var1) {
         this.this$0.add(var1);
      }

      public boolean contains(Comparable var1) {
         return !this.this$0.contains(var1);
      }

      public RangeSet complement() {
         return this.this$0;
      }
   }

   private static final class ComplementRangesByLowerBound extends AbstractNavigableMap {
      private final NavigableMap positiveRangesByLowerBound;
      private final NavigableMap positiveRangesByUpperBound;
      private final Range complementLowerBoundWindow;

      ComplementRangesByLowerBound(NavigableMap var1) {
         this(var1, Range.all());
      }

      private ComplementRangesByLowerBound(NavigableMap var1, Range var2) {
         this.positiveRangesByLowerBound = var1;
         this.positiveRangesByUpperBound = new TreeRangeSet.RangesByUpperBound(var1);
         this.complementLowerBoundWindow = var2;
      }

      private NavigableMap subMap(Range var1) {
         if (!this.complementLowerBoundWindow.isConnected(var1)) {
            return ImmutableSortedMap.of();
         } else {
            var1 = var1.intersection(this.complementLowerBoundWindow);
            return new TreeRangeSet.ComplementRangesByLowerBound(this.positiveRangesByLowerBound, var1);
         }
      }

      public NavigableMap subMap(Cut var1, boolean var2, Cut var3, boolean var4) {
         return this.subMap(Range.range(var1, BoundType.forBoolean(var2), var3, BoundType.forBoolean(var4)));
      }

      public NavigableMap headMap(Cut var1, boolean var2) {
         return this.subMap(Range.upTo(var1, BoundType.forBoolean(var2)));
      }

      public NavigableMap tailMap(Cut var1, boolean var2) {
         return this.subMap(Range.downTo(var1, BoundType.forBoolean(var2)));
      }

      public Comparator comparator() {
         return Ordering.natural();
      }

      Iterator entryIterator() {
         Collection var1;
         if (this.complementLowerBoundWindow.hasLowerBound()) {
            var1 = this.positiveRangesByUpperBound.tailMap(this.complementLowerBoundWindow.lowerEndpoint(), this.complementLowerBoundWindow.lowerBoundType() == BoundType.CLOSED).values();
         } else {
            var1 = this.positiveRangesByUpperBound.values();
         }

         PeekingIterator var2 = Iterators.peekingIterator(var1.iterator());
         Cut var3;
         if (this.complementLowerBoundWindow.contains(Cut.belowAll()) && (!var2.hasNext() || ((Range)var2.peek()).lowerBound != Cut.belowAll())) {
            var3 = Cut.belowAll();
         } else {
            if (!var2.hasNext()) {
               return Iterators.emptyIterator();
            }

            var3 = ((Range)var2.next()).upperBound;
         }

         return new AbstractIterator(this, var3, var2) {
            Cut nextComplementRangeLowerBound;
            final Cut val$firstComplementRangeLowerBound;
            final PeekingIterator val$positiveItr;
            final TreeRangeSet.ComplementRangesByLowerBound this$0;

            {
               this.this$0 = var1;
               this.val$firstComplementRangeLowerBound = var2;
               this.val$positiveItr = var3;
               this.nextComplementRangeLowerBound = this.val$firstComplementRangeLowerBound;
            }

            protected Entry computeNext() {
               if (!TreeRangeSet.ComplementRangesByLowerBound.access$100(this.this$0).upperBound.isLessThan(this.nextComplementRangeLowerBound) && this.nextComplementRangeLowerBound != Cut.aboveAll()) {
                  Range var1;
                  if (this.val$positiveItr.hasNext()) {
                     Range var2 = (Range)this.val$positiveItr.next();
                     var1 = Range.create(this.nextComplementRangeLowerBound, var2.lowerBound);
                     this.nextComplementRangeLowerBound = var2.upperBound;
                  } else {
                     var1 = Range.create(this.nextComplementRangeLowerBound, Cut.aboveAll());
                     this.nextComplementRangeLowerBound = Cut.aboveAll();
                  }

                  return Maps.immutableEntry(var1.lowerBound, var1);
               } else {
                  return (Entry)this.endOfData();
               }
            }

            protected Object computeNext() {
               return this.computeNext();
            }
         };
      }

      Iterator descendingEntryIterator() {
         Cut var2 = this.complementLowerBoundWindow.hasUpperBound() ? (Cut)this.complementLowerBoundWindow.upperEndpoint() : Cut.aboveAll();
         boolean var3 = this.complementLowerBoundWindow.hasUpperBound() && this.complementLowerBoundWindow.upperBoundType() == BoundType.CLOSED;
         PeekingIterator var4 = Iterators.peekingIterator(this.positiveRangesByUpperBound.headMap(var2, var3).descendingMap().values().iterator());
         Cut var5;
         if (var4.hasNext()) {
            var5 = ((Range)var4.peek()).upperBound == Cut.aboveAll() ? ((Range)var4.next()).lowerBound : (Cut)this.positiveRangesByLowerBound.higherKey(((Range)var4.peek()).upperBound);
         } else {
            if (!this.complementLowerBoundWindow.contains(Cut.belowAll()) || this.positiveRangesByLowerBound.containsKey(Cut.belowAll())) {
               return Iterators.emptyIterator();
            }

            var5 = (Cut)this.positiveRangesByLowerBound.higherKey(Cut.belowAll());
         }

         Cut var6 = (Cut)Objects.firstNonNull(var5, Cut.aboveAll());
         return new AbstractIterator(this, var6, var4) {
            Cut nextComplementRangeUpperBound;
            final Cut val$firstComplementRangeUpperBound;
            final PeekingIterator val$positiveItr;
            final TreeRangeSet.ComplementRangesByLowerBound this$0;

            {
               this.this$0 = var1;
               this.val$firstComplementRangeUpperBound = var2;
               this.val$positiveItr = var3;
               this.nextComplementRangeUpperBound = this.val$firstComplementRangeUpperBound;
            }

            protected Entry computeNext() {
               if (this.nextComplementRangeUpperBound == Cut.belowAll()) {
                  return (Entry)this.endOfData();
               } else {
                  Range var1;
                  if (this.val$positiveItr.hasNext()) {
                     var1 = (Range)this.val$positiveItr.next();
                     Range var2 = Range.create(var1.upperBound, this.nextComplementRangeUpperBound);
                     this.nextComplementRangeUpperBound = var1.lowerBound;
                     if (TreeRangeSet.ComplementRangesByLowerBound.access$100(this.this$0).lowerBound.isLessThan(var2.lowerBound)) {
                        return Maps.immutableEntry(var2.lowerBound, var2);
                     }
                  } else if (TreeRangeSet.ComplementRangesByLowerBound.access$100(this.this$0).lowerBound.isLessThan(Cut.belowAll())) {
                     var1 = Range.create(Cut.belowAll(), this.nextComplementRangeUpperBound);
                     this.nextComplementRangeUpperBound = Cut.belowAll();
                     return Maps.immutableEntry(Cut.belowAll(), var1);
                  }

                  return (Entry)this.endOfData();
               }
            }

            protected Object computeNext() {
               return this.computeNext();
            }
         };
      }

      public int size() {
         return Iterators.size(this.entryIterator());
      }

      @Nullable
      public Range get(Object var1) {
         if (var1 instanceof Cut) {
            try {
               Cut var2 = (Cut)var1;
               Entry var3 = this.tailMap(var2, true).firstEntry();
               if (var3 != null && ((Cut)var3.getKey()).equals(var2)) {
                  return (Range)var3.getValue();
               }
            } catch (ClassCastException var4) {
               return null;
            }
         }

         return null;
      }

      public boolean containsKey(Object var1) {
         return this.get(var1) != null;
      }

      public Object get(Object var1) {
         return this.get(var1);
      }

      public NavigableMap tailMap(Object var1, boolean var2) {
         return this.tailMap((Cut)var1, var2);
      }

      public NavigableMap headMap(Object var1, boolean var2) {
         return this.headMap((Cut)var1, var2);
      }

      public NavigableMap subMap(Object var1, boolean var2, Object var3, boolean var4) {
         return this.subMap((Cut)var1, var2, (Cut)var3, var4);
      }

      static Range access$100(TreeRangeSet.ComplementRangesByLowerBound var0) {
         return var0.complementLowerBoundWindow;
      }
   }

   @VisibleForTesting
   static final class RangesByUpperBound extends AbstractNavigableMap {
      private final NavigableMap rangesByLowerBound;
      private final Range upperBoundWindow;

      RangesByUpperBound(NavigableMap var1) {
         this.rangesByLowerBound = var1;
         this.upperBoundWindow = Range.all();
      }

      private RangesByUpperBound(NavigableMap var1, Range var2) {
         this.rangesByLowerBound = var1;
         this.upperBoundWindow = var2;
      }

      private NavigableMap subMap(Range var1) {
         return (NavigableMap)(var1.isConnected(this.upperBoundWindow) ? new TreeRangeSet.RangesByUpperBound(this.rangesByLowerBound, var1.intersection(this.upperBoundWindow)) : ImmutableSortedMap.of());
      }

      public NavigableMap subMap(Cut var1, boolean var2, Cut var3, boolean var4) {
         return this.subMap(Range.range(var1, BoundType.forBoolean(var2), var3, BoundType.forBoolean(var4)));
      }

      public NavigableMap headMap(Cut var1, boolean var2) {
         return this.subMap(Range.upTo(var1, BoundType.forBoolean(var2)));
      }

      public NavigableMap tailMap(Cut var1, boolean var2) {
         return this.subMap(Range.downTo(var1, BoundType.forBoolean(var2)));
      }

      public Comparator comparator() {
         return Ordering.natural();
      }

      public boolean containsKey(@Nullable Object var1) {
         return this.get(var1) != null;
      }

      public Range get(@Nullable Object var1) {
         if (var1 instanceof Cut) {
            try {
               Cut var2 = (Cut)var1;
               if (!this.upperBoundWindow.contains(var2)) {
                  return null;
               }

               Entry var3 = this.rangesByLowerBound.lowerEntry(var2);
               if (var3 != null && ((Range)var3.getValue()).upperBound.equals(var2)) {
                  return (Range)var3.getValue();
               }
            } catch (ClassCastException var4) {
               return null;
            }
         }

         return null;
      }

      Iterator entryIterator() {
         Iterator var1;
         if (!this.upperBoundWindow.hasLowerBound()) {
            var1 = this.rangesByLowerBound.values().iterator();
         } else {
            Entry var2 = this.rangesByLowerBound.lowerEntry(this.upperBoundWindow.lowerEndpoint());
            if (var2 == null) {
               var1 = this.rangesByLowerBound.values().iterator();
            } else if (this.upperBoundWindow.lowerBound.isLessThan(((Range)var2.getValue()).upperBound)) {
               var1 = this.rangesByLowerBound.tailMap(var2.getKey(), true).values().iterator();
            } else {
               var1 = this.rangesByLowerBound.tailMap(this.upperBoundWindow.lowerEndpoint(), true).values().iterator();
            }
         }

         return new AbstractIterator(this, var1) {
            final Iterator val$backingItr;
            final TreeRangeSet.RangesByUpperBound this$0;

            {
               this.this$0 = var1;
               this.val$backingItr = var2;
            }

            protected Entry computeNext() {
               if (!this.val$backingItr.hasNext()) {
                  return (Entry)this.endOfData();
               } else {
                  Range var1 = (Range)this.val$backingItr.next();
                  return TreeRangeSet.RangesByUpperBound.access$000(this.this$0).upperBound.isLessThan(var1.upperBound) ? (Entry)this.endOfData() : Maps.immutableEntry(var1.upperBound, var1);
               }
            }

            protected Object computeNext() {
               return this.computeNext();
            }
         };
      }

      Iterator descendingEntryIterator() {
         Collection var1;
         if (this.upperBoundWindow.hasUpperBound()) {
            var1 = this.rangesByLowerBound.headMap(this.upperBoundWindow.upperEndpoint(), false).descendingMap().values();
         } else {
            var1 = this.rangesByLowerBound.descendingMap().values();
         }

         PeekingIterator var2 = Iterators.peekingIterator(var1.iterator());
         if (var2.hasNext() && this.upperBoundWindow.upperBound.isLessThan(((Range)var2.peek()).upperBound)) {
            var2.next();
         }

         return new AbstractIterator(this, var2) {
            final PeekingIterator val$backingItr;
            final TreeRangeSet.RangesByUpperBound this$0;

            {
               this.this$0 = var1;
               this.val$backingItr = var2;
            }

            protected Entry computeNext() {
               if (!this.val$backingItr.hasNext()) {
                  return (Entry)this.endOfData();
               } else {
                  Range var1 = (Range)this.val$backingItr.next();
                  return TreeRangeSet.RangesByUpperBound.access$000(this.this$0).lowerBound.isLessThan(var1.upperBound) ? Maps.immutableEntry(var1.upperBound, var1) : (Entry)this.endOfData();
               }
            }

            protected Object computeNext() {
               return this.computeNext();
            }
         };
      }

      public int size() {
         return this.upperBoundWindow.equals(Range.all()) ? this.rangesByLowerBound.size() : Iterators.size(this.entryIterator());
      }

      public boolean isEmpty() {
         return this.upperBoundWindow.equals(Range.all()) ? this.rangesByLowerBound.isEmpty() : !this.entryIterator().hasNext();
      }

      public Object get(Object var1) {
         return this.get(var1);
      }

      public NavigableMap tailMap(Object var1, boolean var2) {
         return this.tailMap((Cut)var1, var2);
      }

      public NavigableMap headMap(Object var1, boolean var2) {
         return this.headMap((Cut)var1, var2);
      }

      public NavigableMap subMap(Object var1, boolean var2, Object var3, boolean var4) {
         return this.subMap((Cut)var1, var2, (Cut)var3, var4);
      }

      static Range access$000(TreeRangeSet.RangesByUpperBound var0) {
         return var0.upperBoundWindow;
      }
   }

   final class AsRanges extends ForwardingCollection implements Set {
      final TreeRangeSet this$0;

      AsRanges(TreeRangeSet var1) {
         this.this$0 = var1;
      }

      protected Collection delegate() {
         return this.this$0.rangesByLowerBound.values();
      }

      public int hashCode() {
         return Sets.hashCodeImpl(this);
      }

      public boolean equals(@Nullable Object var1) {
         return Sets.equalsImpl(this, var1);
      }

      protected Object delegate() {
         return this.delegate();
      }
   }
}
