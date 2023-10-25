package com.ibm.icu.impl;

import com.ibm.icu.text.CurrencyMetaInfo;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ICUCurrencyMetaInfo extends CurrencyMetaInfo {
  private ICUResourceBundle regionInfo;
  
  private ICUResourceBundle digitInfo;
  
  private static final long MASK = 4294967295L;
  
  private static final int Region = 1;
  
  private static final int Currency = 2;
  
  private static final int Date = 4;
  
  private static final int Tender = 8;
  
  private static final int Everything = 2147483647;
  
  public ICUCurrencyMetaInfo() {
    ICUResourceBundle bundle = (ICUResourceBundle)ICUResourceBundle.getBundleInstance("com/ibm/icu/impl/data/icudt51b/curr", "supplementalData", ICUResourceBundle.ICU_DATA_CLASS_LOADER);
    this.regionInfo = bundle.findTopLevel("CurrencyMap");
    this.digitInfo = bundle.findTopLevel("CurrencyMeta");
  }
  
  public List<CurrencyMetaInfo.CurrencyInfo> currencyInfo(CurrencyMetaInfo.CurrencyFilter filter) {
    return collect(new InfoCollector(), filter);
  }
  
  public List<String> currencies(CurrencyMetaInfo.CurrencyFilter filter) {
    return collect(new CurrencyCollector(), filter);
  }
  
  public List<String> regions(CurrencyMetaInfo.CurrencyFilter filter) {
    return collect(new RegionCollector(), filter);
  }
  
  public CurrencyMetaInfo.CurrencyDigits currencyDigits(String isoCode) {
    ICUResourceBundle b = this.digitInfo.findWithFallback(isoCode);
    if (b == null)
      b = this.digitInfo.findWithFallback("DEFAULT"); 
    int[] data = b.getIntVector();
    return new CurrencyMetaInfo.CurrencyDigits(data[0], data[1]);
  }
  
  private <T> List<T> collect(Collector<T> collector, CurrencyMetaInfo.CurrencyFilter filter) {
    if (filter == null)
      filter = CurrencyMetaInfo.CurrencyFilter.all(); 
    int needed = collector.collects();
    if (filter.region != null)
      needed |= 0x1; 
    if (filter.currency != null)
      needed |= 0x2; 
    if (filter.from != Long.MIN_VALUE || filter.to != Long.MAX_VALUE)
      needed |= 0x4; 
    if (filter.tenderOnly)
      needed |= 0x8; 
    if (needed != 0)
      if (filter.region != null) {
        ICUResourceBundle b = this.regionInfo.findWithFallback(filter.region);
        if (b != null)
          collectRegion(collector, filter, needed, b); 
      } else {
        for (int i = 0; i < this.regionInfo.getSize(); i++)
          collectRegion(collector, filter, needed, this.regionInfo.at(i)); 
      }  
    return collector.getList();
  }
  
  private <T> void collectRegion(Collector<T> collector, CurrencyMetaInfo.CurrencyFilter filter, int needed, ICUResourceBundle b) {
    String region = b.getKey();
    if (needed == 1) {
      collector.collect(b.getKey(), null, 0L, 0L, -1, false);
      return;
    } 
    for (int i = 0; i < b.getSize(); i++) {
      ICUResourceBundle r = b.at(i);
      if (r.getSize() == 0)
        continue; 
      String currency = null;
      long from = Long.MIN_VALUE;
      long to = Long.MAX_VALUE;
      boolean tender = true;
      if ((needed & 0x2) != 0) {
        ICUResourceBundle currBundle = r.at("id");
        currency = currBundle.getString();
        if (filter.currency != null && !filter.currency.equals(currency))
          continue; 
      } 
      if ((needed & 0x4) != 0) {
        from = getDate(r.at("from"), Long.MIN_VALUE, false);
        to = getDate(r.at("to"), Long.MAX_VALUE, true);
        if (filter.from > to)
          continue; 
        if (filter.to < from)
          continue; 
      } 
      if ((needed & 0x8) != 0) {
        ICUResourceBundle tenderBundle = r.at("tender");
        tender = (tenderBundle == null || "true".equals(tenderBundle.getString()));
        if (filter.tenderOnly && !tender)
          continue; 
      } 
      collector.collect(region, currency, from, to, i, tender);
      continue;
    } 
  }
  
  private long getDate(ICUResourceBundle b, long defaultValue, boolean endOfDay) {
    if (b == null)
      return defaultValue; 
    int[] values = b.getIntVector();
    return values[0] << 32L | values[1] & 0xFFFFFFFFL;
  }
  
  private static class UniqueList<T> {
    private Set<T> seen = new HashSet<T>();
    
    private List<T> list = new ArrayList<T>();
    
    private static <T> UniqueList<T> create() {
      return new UniqueList<T>();
    }
    
    void add(T value) {
      if (!this.seen.contains(value)) {
        this.list.add(value);
        this.seen.add(value);
      } 
    }
    
    List<T> list() {
      return Collections.unmodifiableList(this.list);
    }
  }
  
  private static class InfoCollector implements Collector<CurrencyMetaInfo.CurrencyInfo> {
    private List<CurrencyMetaInfo.CurrencyInfo> result = new ArrayList<CurrencyMetaInfo.CurrencyInfo>();
    
    public void collect(String region, String currency, long from, long to, int priority, boolean tender) {
      this.result.add(new CurrencyMetaInfo.CurrencyInfo(region, currency, from, to, priority, tender));
    }
    
    public List<CurrencyMetaInfo.CurrencyInfo> getList() {
      return Collections.unmodifiableList(this.result);
    }
    
    public int collects() {
      return Integer.MAX_VALUE;
    }
    
    private InfoCollector() {}
  }
  
  private static class RegionCollector implements Collector<String> {
    private final ICUCurrencyMetaInfo.UniqueList<String> result = (ICUCurrencyMetaInfo.UniqueList)ICUCurrencyMetaInfo.UniqueList.create();
    
    public void collect(String region, String currency, long from, long to, int priority, boolean tender) {
      this.result.add(region);
    }
    
    public int collects() {
      return 1;
    }
    
    public List<String> getList() {
      return this.result.list();
    }
    
    private RegionCollector() {}
  }
  
  private static class CurrencyCollector implements Collector<String> {
    private final ICUCurrencyMetaInfo.UniqueList<String> result = (ICUCurrencyMetaInfo.UniqueList)ICUCurrencyMetaInfo.UniqueList.create();
    
    public void collect(String region, String currency, long from, long to, int priority, boolean tender) {
      this.result.add(currency);
    }
    
    public int collects() {
      return 2;
    }
    
    public List<String> getList() {
      return this.result.list();
    }
    
    private CurrencyCollector() {}
  }
  
  private static interface Collector<T> {
    int collects();
    
    void collect(String param1String1, String param1String2, long param1Long1, long param1Long2, int param1Int, boolean param1Boolean);
    
    List<T> getList();
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\ibm\icu\impl\ICUCurrencyMetaInfo.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */