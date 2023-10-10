package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Predicate;

@GwtCompatible
interface FilteredMultimap extends Multimap {
   Multimap unfiltered();

   Predicate entryPredicate();
}
