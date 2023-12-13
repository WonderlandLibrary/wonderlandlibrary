package com.ibm.icu.impl.data;

import com.ibm.icu.impl.ICUData;
import java.util.ListResourceBundle;

public class BreakIteratorRules_th extends ListResourceBundle {
   private static final String DATA_NAME = "data/th.brk";

   public Object[][] getContents() {
      boolean var1 = ICUData.exists("data/th.brk");
      return !var1 ? new Object[0][0] : new Object[][]{{"BreakIteratorClasses", new String[]{"RuleBasedBreakIterator", "DictionaryBasedBreakIterator", "DictionaryBasedBreakIterator", "RuleBasedBreakIterator"}}, {"WordBreakDictionary", "data/th.brk"}, {"LineBreakDictionary", "data/th.brk"}};
   }
}
