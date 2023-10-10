package com.google.common.collect;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.NavigableMap;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.Map.Entry;
import javax.annotation.Nullable;

@Beta
@GwtIncompatible("NavigableMap")
public final class TreeRangeMap implements RangeMap {
   private final NavigableMap entriesByLowerBound = Maps.newTreeMap();
   private static final RangeMap EMPTY_SUB_RANGE_MAP = new RangeMap() {
      @Nullable
      public Object get(Comparable var1) {
         return null;
      }

      @Nullable
      public Entry getEntry(Comparable var1) {
         return null;
      }

      public Range span() {
         throw new NoSuchElementException();
      }

      public void put(Range var1, Object var2) {
         Preconditions.checkNotNull(var1);
         throw new IllegalArgumentException("Cannot insert range " + var1 + " into an empty subRangeMap");
      }

      public void putAll(RangeMap var1) {
         if (!var1.asMapOfRanges().isEmpty()) {
            throw new IllegalArgumentException("Cannot putAll(nonEmptyRangeMap) into an empty subRangeMap");
         }
      }

      public void clear() {
      }

      public void remove(Range var1) {
         Preconditions.checkNotNull(var1);
      }

      public Map asMapOfRanges() {
         return Collections.emptyMap();
      }

      public RangeMap subRangeMap(Range var1) {
         Preconditions.checkNotNull(var1);
         return this;
      }
   };

   public static TreeRangeMap create() {
      return new TreeRangeMap();
   }

   private TreeRangeMap() {
   }

   @Nullable
   public Object get(Comparable var1) {
      Entry var2 = this.getEntry(var1);
      return var2 == null ? null : var2.getValue();
   }

   @Nullable
   public Entry getEntry(Comparable var1) {
      Entry var2 = this.entriesByLowerBound.floorEntry(Cut.belowValue(var1));
      return var2 != null && ((TreeRangeMap.RangeMapEntry)var2.getValue()).contains(var1) ? (Entry)var2.getValue() : null;
   }

   public void put(Range var1, Object var2) {
      if (!var1.isEmpty()) {
         Preconditions.checkNotNull(var2);
         this.remove(var1);
         this.entriesByLowerBound.put(var1.lowerBound, new TreeRangeMap.RangeMapEntry(var1, var2));
      }

   }

   public void putAll(RangeMap var1) {
      Iterator var2 = var1.asMapOfRanges().entrySet().iterator();

      while(var2.hasNext()) {
         Entry var3 = (Entry)var2.next();
         this.put((Range)var3.getKey(), var3.getValue());
      }

   }

   public void clear() {
      this.entriesByLowerBound.clear();
   }

   public Range span() {
      Entry var1 = this.entriesByLowerBound.firstEntry();
      Entry var2 = this.entriesByLowerBound.lastEntry();
      if (var1 == null) {
         throw new NoSuchElementException();
      } else {
         return Range.create(((TreeRangeMap.RangeMapEntry)var1.getValue()).getKey().lowerBound, ((TreeRangeMap.RangeMapEntry)var2.getValue()).getKey().upperBound);
      }
   }

   private void putRangeMapEntry(Cut var1, Cut var2, Object var3) {
      this.entriesByLowerBound.put(var1, new TreeRangeMap.RangeMapEntry(var1, var2, var3));
   }

   public void remove(Range var1) {
      if (!var1.isEmpty()) {
         Entry var2 = this.entriesByLowerBound.lowerEntry(var1.lowerBound);
         if (var2 != null) {
            TreeRangeMap.RangeMapEntry var3 = (TreeRangeMap.RangeMapEntry)var2.getValue();
            if (var3.getUpperBound().compareTo(var1.lowerBound) > 0) {
               if (var3.getUpperBound().compareTo(var1.upperBound) > 0) {
                  this.putRangeMapEntry(var1.upperBound, var3.getUpperBound(), ((TreeRangeMap.RangeMapEntry)var2.getValue()).getValue());
               }

               this.putRangeMapEntry(var3.getLowerBound(), var1.lowerBound, ((TreeRangeMap.RangeMapEntry)var2.getValue()).getValue());
            }
         }

         Entry var5 = this.entriesByLowerBound.lowerEntry(var1.upperBound);
         if (var5 != null) {
            TreeRangeMap.RangeMapEntry var4 = (TreeRangeMap.RangeMapEntry)var5.getValue();
            if (var4.getUpperBound().compareTo(var1.upperBound) > 0) {
               this.putRangeMapEntry(var1.upperBound, var4.getUpperBound(), ((TreeRangeMap.RangeMapEntry)var5.getValue()).getValue());
               this.entriesByLowerBound.remove(var1.lowerBound);
            }
         }

         this.entriesByLowerBound.subMap(var1.lowerBound, var1.upperBound).clear();
      }
   }

   public Map asMapOfRanges() {
      return new TreeRangeMap.AsMapOfRanges(this);
   }

   public RangeMap subRangeMap(Range var1) {
      return (RangeMap)(var1.equals(Range.all()) ? this : new TreeRangeMap.SubRangeMap(this, var1));
   }

   private RangeMap emptySubRangeMap() {
      return EMPTY_SUB_RANGE_MAP;
   }

   public boolean equals(@Nullable Object var1) {
      if (var1 instanceof RangeMap) {
         RangeMap var2 = (RangeMap)var1;
         return this.asMapOfRanges().equals(var2.asMapOfRanges());
      } else {
         return false;
      }
   }

   public int hashCode() {
      return this.asMapOfRanges().hashCode();
   }

   public String toString() {
      return this.entriesByLowerBound.values().toString();
   }

   static NavigableMap access$100(TreeRangeMap var0) {
      return var0.entriesByLowerBound;
   }

   static RangeMap access$200(TreeRangeMap var0) {
      return var0.emptySubRangeMap();
   }

   private class SubRangeMap implements RangeMap {
      private final Range subRange;
      final TreeRangeMap this$0;

      SubRangeMap(TreeRangeMap var1, Range var2) {
         this.this$0 = var1;
         this.subRange = var2;
      }

      @Nullable
      public Object get(Comparable var1) {
         return this.subRange.contains(var1) ? this.this$0.get(var1) : null;
      }

      @Nullable
      public Entry getEntry(Comparable var1) {
         if (this.subRange.contains(var1)) {
            Entry var2 = this.this$0.getEntry(var1);
            if (var2 != null) {
               return Maps.immutableEntry(((Range)var2.getKey()).intersection(this.subRange), var2.getValue());
            }
         }

         return null;
      }

      public Range span() {
         Entry var2 = TreeRangeMap.access$100(this.this$0).floorEntry(this.subRange.lowerBound);
         Cut var1;
         if (var2 != null && ((TreeRangeMap.RangeMapEntry)var2.getValue()).getUpperBound().compareTo(this.subRange.lowerBound) > 0) {
            var1 = this.subRange.lowerBound;
         } else {
            var1 = (Cut)TreeRangeMap.access$100(this.this$0).ceilingKey(this.subRange.lowerBound);
            if (var1 == null || var1.compareTo(this.subRange.upperBound) >= 0) {
               throw new NoSuchElementException();
            }
         }

         Entry var4 = TreeRangeMap.access$100(this.this$0).lowerEntry(this.subRange.upperBound);
         if (var4 == null) {
            throw new NoSuchElementException();
         } else {
            Cut var3;
            if (((TreeRangeMap.RangeMapEntry)var4.getValue()).getUpperBound().compareTo(this.subRange.upperBound) >= 0) {
               var3 = this.subRange.upperBound;
            } else {
               var3 = ((TreeRangeMap.RangeMapEntry)var4.getValue()).getUpperBound();
            }

            return Range.create(var1, var3);
         }
      }

      public void put(Range var1, Object var2) {
         Preconditions.checkArgument(this.subRange.encloses(var1), "Cannot put range %s into a subRangeMap(%s)", var1, this.subRange);
         this.this$0.put(var1, var2);
      }

      public void putAll(RangeMap var1) {
         if (!var1.asMapOfRanges().isEmpty()) {
            Range var2 = var1.span();
            Preconditions.checkArgument(this.subRange.encloses(var2), "Cannot putAll rangeMap with span %s into a subRangeMap(%s)", var2, this.subRange);
            this.this$0.putAll(var1);
         }
      }

      public void clear() {
         this.this$0.remove(this.subRange);
      }

      public void remove(Range var1) {
         if (var1.isConnected(this.subRange)) {
            this.this$0.remove(var1.intersection(this.subRange));
         }

      }

      public RangeMap subRangeMap(Range var1) {
         return !var1.isConnected(this.subRange) ? TreeRangeMap.access$200(this.this$0) : this.this$0.subRangeMap(var1.intersection(this.subRange));
      }

      public Map asMapOfRanges() {
         return new TreeRangeMap.SubRangeMap.SubRangeMapAsMap(this);
      }

      public boolean equals(@Nullable Object var1) {
         if (var1 instanceof RangeMap) {
            RangeMap var2 = (RangeMap)var1;
            return this.asMapOfRanges().equals(var2.asMapOfRanges());
         } else {
            return false;
         }
      }

      public int hashCode() {
         return this.asMapOfRanges().hashCode();
      }

      public String toString() {
         return this.asMapOfRanges().toString();
      }

      static Range access$300(TreeRangeMap.SubRangeMap var0) {
         return var0.subRange;
      }

      class SubRangeMapAsMap extends AbstractMap {
         final TreeRangeMap.SubRangeMap this$1;

         SubRangeMapAsMap(TreeRangeMap.SubRangeMap var1) {
            this.this$1 = var1;
         }

         public boolean containsKey(Object var1) {
            return this.get(var1) != null;
         }

         public Object get(Object var1) {
            try {
               if (var1 instanceof Range) {
                  Range var2 = (Range)var1;
                  if (!TreeRangeMap.SubRangeMap.access$300(this.this$1).encloses(var2) || var2.isEmpty()) {
                     return null;
                  }

                  TreeRangeMap.RangeMapEntry var3 = null;
                  if (var2.lowerBound.compareTo(TreeRangeMap.SubRangeMap.access$300(this.this$1).lowerBound) == 0) {
                     Entry var4 = TreeRangeMap.access$100(this.this$1.this$0).floorEntry(var2.lowerBound);
                     if (var4 != null) {
                        var3 = (TreeRangeMap.RangeMapEntry)var4.getValue();
                     }
                  } else {
                     var3 = (TreeRangeMap.RangeMapEntry)TreeRangeMap.access$100(this.this$1.this$0).get(var2.lowerBound);
                  }

                  if (var3 != null && var3.getKey().isConnected(TreeRangeMap.SubRangeMap.access$300(this.this$1)) && var3.getKey().intersection(TreeRangeMap.SubRangeMap.access$300(this.this$1)).equals(var2)) {
                     return var3.getValue();
                  }
               }

               return null;
            } catch (ClassCastException var5) {
               return null;
            }
         }

         public Object remove(Object var1) {
            Object var2 = this.get(var1);
            if (var2 != null) {
               Range var3 = (Range)var1;
               this.this$1.this$0.remove(var3);
               return var2;
            } else {
               return null;
            }
         }

         public void clear() {
            this.this$1.clear();
         }

         private boolean removeEntryIf(Predicate var1) {
            ArrayList var2 = Lists.newArrayList();
            Iterator var3 = this.entrySet().iterator();

            while(var3.hasNext()) {
               Entry var4 = (Entry)var3.next();
               if (var1.apply(var4)) {
                  var2.add(var4.getKey());
               }
            }

            var3 = var2.iterator();

            while(var3.hasNext()) {
               Range var5 = (Range)var3.next();
               this.this$1.this$0.remove(var5);
            }

            return !var2.isEmpty();
         }

         public Set keySet() {
            return new Maps.KeySet(this, this) {
               final TreeRangeMap.SubRangeMap.SubRangeMapAsMap this$2;

               {
                  this.this$2 = var1;
               }

               public boolean remove(@Nullable Object var1) {
                  return this.this$2.remove(var1) != null;
               }

               public boolean retainAll(Collection var1) {
                  return TreeRangeMap.SubRangeMap.SubRangeMapAsMap.access$400(this.this$2, Predicates.compose(Predicates.not(Predicates.in(var1)), Maps.keyFunction()));
               }
            };
         }

         public Set entrySet() {
            return new Maps.EntrySet(this) {
               final TreeRangeMap.SubRangeMap.SubRangeMapAsMap this$2;

               {
                  this.this$2 = var1;
               }

               Map map() {
                  return this.this$2;
               }

               public Iterator iterator() {
                  if (TreeRangeMap.SubRangeMap.access$300(this.this$2.this$1).isEmpty()) {
                     return Iterators.emptyIterator();
                  } else {
                     Cut var1 = (Cut)Objects.firstNonNull(TreeRangeMap.access$100(this.this$2.this$1.this$0).floorKey(TreeRangeMap.SubRangeMap.access$300(this.this$2.this$1).lowerBound), TreeRangeMap.SubRangeMap.access$300(this.this$2.this$1).lowerBound);
                     Iterator var2 = TreeRangeMap.access$100(this.this$2.this$1.this$0).tailMap(var1, true).values().iterator();
                     return new AbstractIterator(this, var2) {
                        final Iterator val$backingItr;
                        final <undefinedtype> this$3;

                        {
                           this.this$3 = var1;
                           this.val$backingItr = var2;
                        }

                        protected Entry computeNext() {
                           while(true) {
                              if (this.val$backingItr.hasNext()) {
                                 TreeRangeMap.RangeMapEntry var1 = (TreeRangeMap.RangeMapEntry)this.val$backingItr.next();
                                 if (var1.getLowerBound().compareTo(TreeRangeMap.SubRangeMap.access$300(this.this$3.this$2.this$1).upperBound) < 0) {
                                    if (var1.getUpperBound().compareTo(TreeRangeMap.SubRangeMap.access$300(this.this$3.this$2.this$1).lowerBound) <= 0) {
                                       continue;
                                    }

                                    return Maps.immutableEntry(var1.getKey().intersection(TreeRangeMap.SubRangeMap.access$300(this.this$3.this$2.this$1)), var1.getValue());
                                 }
                              }

                              return (Entry)this.endOfData();
                           }
                        }

                        protected Object computeNext() {
                           return this.computeNext();
                        }
                     };
                  }
               }

               public boolean retainAll(Collection var1) {
                  return TreeRangeMap.SubRangeMap.SubRangeMapAsMap.access$400(this.this$2, Predicates.not(Predicates.in(var1)));
               }

               public int size() {
                  return Iterators.size(this.iterator());
               }

               public boolean isEmpty() {
                  return !this.iterator().hasNext();
               }
            };
         }

         public Collection values() {
            return new Maps.Values(this, this) {
               final TreeRangeMap.SubRangeMap.SubRangeMapAsMap this$2;

               {
                  this.this$2 = var1;
               }

               public boolean removeAll(Collection var1) {
                  return TreeRangeMap.SubRangeMap.SubRangeMapAsMap.access$400(this.this$2, Predicates.compose(Predicates.in(var1), Maps.valueFunction()));
               }

               public boolean retainAll(Collection var1) {
                  return TreeRangeMap.SubRangeMap.SubRangeMapAsMap.access$400(this.this$2, Predicates.compose(Predicates.not(Predicates.in(var1)), Maps.valueFunction()));
               }
            };
         }

         static boolean access$400(TreeRangeMap.SubRangeMap.SubRangeMapAsMap var0, Predicate var1) {
            return var0.removeEntryIf(var1);
         }
      }
   }

   private final class AsMapOfRanges extends AbstractMap {
      final TreeRangeMap this$0;

      private AsMapOfRanges(TreeRangeMap var1) {
         this.this$0 = var1;
      }

      public boolean containsKey(@Nullable Object var1) {
         return this.get(var1) != null;
      }

      public Object get(@Nullable Object var1) {
         if (var1 instanceof Range) {
            Range var2 = (Range)var1;
            TreeRangeMap.RangeMapEntry var3 = (TreeRangeMap.RangeMapEntry)TreeRangeMap.access$100(this.this$0).get(var2.lowerBound);
            if (var3 != null && var3.getKey().equals(var2)) {
               return var3.getValue();
            }
         }

         return null;
      }

      public Set entrySet() {
         return new AbstractSet(this) {
            final TreeRangeMap.AsMapOfRanges this$1;

            {
               this.this$1 = var1;
            }

            public Iterator iterator() {
               return TreeRangeMap.access$100(this.this$1.this$0).values().iterator();
            }

            public int size() {
               return TreeRangeMap.access$100(this.this$1.this$0).size();
            }
         };
      }

      AsMapOfRanges(TreeRangeMap var1, Object var2) {
         this(var1);
      }
   }

   private static final class RangeMapEntry extends AbstractMapEntry {
      private final Range range;
      private final Object value;

      RangeMapEntry(Cut var1, Cut var2, Object var3) {
         this(Range.create(var1, var2), var3);
      }

      RangeMapEntry(Range var1, Object var2) {
         this.range = var1;
         this.value = var2;
      }

      public Range getKey() {
         return this.range;
      }

      public Object getValue() {
         return this.value;
      }

      public boolean contains(Comparable var1) {
         return this.range.contains(var1);
      }

      Cut getLowerBound() {
         return this.range.lowerBound;
      }

      Cut getUpperBound() {
         return this.range.upperBound;
      }

      public Object getKey() {
         return this.getKey();
      }
   }
}
