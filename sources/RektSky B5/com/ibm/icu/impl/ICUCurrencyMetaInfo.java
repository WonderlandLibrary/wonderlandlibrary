/*
 * Decompiled with CFR 0.152.
 */
package com.ibm.icu.impl;

import com.ibm.icu.impl.ICUResourceBundle;
import com.ibm.icu.text.CurrencyMetaInfo;
import com.ibm.icu.util.Currency;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ICUCurrencyMetaInfo
extends CurrencyMetaInfo {
    private ICUResourceBundle regionInfo;
    private ICUResourceBundle digitInfo;
    private static final long MASK = 0xFFFFFFFFL;
    private static final int Region = 1;
    private static final int Currency = 2;
    private static final int Date = 4;
    private static final int Tender = 8;
    private static final int Everything = Integer.MAX_VALUE;

    public ICUCurrencyMetaInfo() {
        ICUResourceBundle bundle = (ICUResourceBundle)ICUResourceBundle.getBundleInstance("com/ibm/icu/impl/data/icudt62b/curr", "supplementalData", ICUResourceBundle.ICU_DATA_CLASS_LOADER);
        this.regionInfo = bundle.findTopLevel("CurrencyMap");
        this.digitInfo = bundle.findTopLevel("CurrencyMeta");
    }

    @Override
    public List<CurrencyMetaInfo.CurrencyInfo> currencyInfo(CurrencyMetaInfo.CurrencyFilter filter) {
        return this.collect(new InfoCollector(), filter);
    }

    @Override
    public List<String> currencies(CurrencyMetaInfo.CurrencyFilter filter) {
        return this.collect(new CurrencyCollector(), filter);
    }

    @Override
    public List<String> regions(CurrencyMetaInfo.CurrencyFilter filter) {
        return this.collect(new RegionCollector(), filter);
    }

    @Override
    public CurrencyMetaInfo.CurrencyDigits currencyDigits(String isoCode) {
        return this.currencyDigits(isoCode, Currency.CurrencyUsage.STANDARD);
    }

    @Override
    public CurrencyMetaInfo.CurrencyDigits currencyDigits(String isoCode, Currency.CurrencyUsage currencyPurpose) {
        ICUResourceBundle b2 = this.digitInfo.findWithFallback(isoCode);
        if (b2 == null) {
            b2 = this.digitInfo.findWithFallback("DEFAULT");
        }
        int[] data = b2.getIntVector();
        if (currencyPurpose == Currency.CurrencyUsage.CASH) {
            return new CurrencyMetaInfo.CurrencyDigits(data[2], data[3]);
        }
        if (currencyPurpose == Currency.CurrencyUsage.STANDARD) {
            return new CurrencyMetaInfo.CurrencyDigits(data[0], data[1]);
        }
        return new CurrencyMetaInfo.CurrencyDigits(data[0], data[1]);
    }

    private <T> List<T> collect(Collector<T> collector, CurrencyMetaInfo.CurrencyFilter filter) {
        if (filter == null) {
            filter = CurrencyMetaInfo.CurrencyFilter.all();
        }
        int needed = collector.collects();
        if (filter.region != null) {
            needed |= 1;
        }
        if (filter.currency != null) {
            needed |= 2;
        }
        if (filter.from != Long.MIN_VALUE || filter.to != Long.MAX_VALUE) {
            needed |= 4;
        }
        if (filter.tenderOnly) {
            needed |= 8;
        }
        if (needed != 0) {
            if (filter.region != null) {
                ICUResourceBundle b2 = this.regionInfo.findWithFallback(filter.region);
                if (b2 != null) {
                    this.collectRegion(collector, filter, needed, b2);
                }
            } else {
                for (int i2 = 0; i2 < this.regionInfo.getSize(); ++i2) {
                    this.collectRegion(collector, filter, needed, this.regionInfo.at(i2));
                }
            }
        }
        return collector.getList();
    }

    private <T> void collectRegion(Collector<T> collector, CurrencyMetaInfo.CurrencyFilter filter, int needed, ICUResourceBundle b2) {
        String region = b2.getKey();
        if (needed == 1) {
            collector.collect(b2.getKey(), null, 0L, 0L, -1, false);
            return;
        }
        for (int i2 = 0; i2 < b2.getSize(); ++i2) {
            ICUResourceBundle r2 = b2.at(i2);
            if (r2.getSize() == 0) continue;
            String currency = null;
            long from = Long.MIN_VALUE;
            long to = Long.MAX_VALUE;
            boolean tender = true;
            if ((needed & 2) != 0) {
                ICUResourceBundle currBundle = r2.at("id");
                currency = currBundle.getString();
                if (filter.currency != null && !filter.currency.equals(currency)) continue;
            }
            if ((needed & 4) != 0) {
                from = this.getDate(r2.at("from"), Long.MIN_VALUE, false);
                to = this.getDate(r2.at("to"), Long.MAX_VALUE, true);
                if (filter.from > to || filter.to < from) continue;
            }
            if ((needed & 8) != 0) {
                ICUResourceBundle tenderBundle = r2.at("tender");
                boolean bl = tender = tenderBundle == null || "true".equals(tenderBundle.getString());
                if (filter.tenderOnly && !tender) continue;
            }
            collector.collect(region, currency, from, to, i2, tender);
        }
    }

    private long getDate(ICUResourceBundle b2, long defaultValue, boolean endOfDay) {
        if (b2 == null) {
            return defaultValue;
        }
        int[] values = b2.getIntVector();
        return (long)values[0] << 32 | (long)values[1] & 0xFFFFFFFFL;
    }

    private static interface Collector<T> {
        public int collects();

        public void collect(String var1, String var2, long var3, long var5, int var7, boolean var8);

        public List<T> getList();
    }

    private static class CurrencyCollector
    implements Collector<String> {
        private final UniqueList<String> result = UniqueList.access$300();

        private CurrencyCollector() {
        }

        @Override
        public void collect(String region, String currency, long from, long to, int priority, boolean tender) {
            this.result.add(currency);
        }

        @Override
        public int collects() {
            return 2;
        }

        @Override
        public List<String> getList() {
            return this.result.list();
        }
    }

    private static class RegionCollector
    implements Collector<String> {
        private final UniqueList<String> result = UniqueList.access$300();

        private RegionCollector() {
        }

        @Override
        public void collect(String region, String currency, long from, long to, int priority, boolean tender) {
            this.result.add(region);
        }

        @Override
        public int collects() {
            return 1;
        }

        @Override
        public List<String> getList() {
            return this.result.list();
        }
    }

    private static class InfoCollector
    implements Collector<CurrencyMetaInfo.CurrencyInfo> {
        private List<CurrencyMetaInfo.CurrencyInfo> result = new ArrayList<CurrencyMetaInfo.CurrencyInfo>();

        private InfoCollector() {
        }

        @Override
        public void collect(String region, String currency, long from, long to, int priority, boolean tender) {
            this.result.add(new CurrencyMetaInfo.CurrencyInfo(region, currency, from, to, priority, tender));
        }

        @Override
        public List<CurrencyMetaInfo.CurrencyInfo> getList() {
            return Collections.unmodifiableList(this.result);
        }

        @Override
        public int collects() {
            return Integer.MAX_VALUE;
        }
    }

    private static class UniqueList<T> {
        private Set<T> seen = new HashSet<T>();
        private List<T> list = new ArrayList<T>();

        private UniqueList() {
        }

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

        static /* synthetic */ UniqueList access$300() {
            return UniqueList.create();
        }
    }
}

