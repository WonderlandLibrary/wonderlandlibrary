package com.ibm.icu.impl.duration.impl;

import com.ibm.icu.impl.ICUData;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.MissingResourceException;

public class ResourceBasedPeriodFormatterDataService extends PeriodFormatterDataService {
  private Collection<String> availableLocales;
  
  private PeriodFormatterData lastData = null;
  
  private String lastLocale = null;
  
  private Map<String, PeriodFormatterData> cache = new HashMap<String, PeriodFormatterData>();
  
  private static final String PATH = "data/";
  
  private static final ResourceBasedPeriodFormatterDataService singleton = new ResourceBasedPeriodFormatterDataService();
  
  public static ResourceBasedPeriodFormatterDataService getInstance() {
    return singleton;
  }
  
  private ResourceBasedPeriodFormatterDataService() {
    List<String> localeNames = new ArrayList<String>();
    InputStream is = ICUData.getRequiredStream(getClass(), "data/index.txt");
    try {
      BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
      String string = null;
      while (null != (string = br.readLine())) {
        string = string.trim();
        if (string.startsWith("#") || string.length() == 0)
          continue; 
        localeNames.add(string);
      } 
      br.close();
    } catch (IOException e) {
      throw new IllegalStateException("IO Error reading data/index.txt: " + e.toString());
    } 
    this.availableLocales = Collections.unmodifiableList(localeNames);
  }
  
  public PeriodFormatterData get(String localeName) {
    int x = localeName.indexOf('@');
    if (x != -1)
      localeName = localeName.substring(0, x); 
    synchronized (this) {
      if (this.lastLocale != null && this.lastLocale.equals(localeName))
        return this.lastData; 
      PeriodFormatterData ld = this.cache.get(localeName);
      if (ld == null) {
        String ln = localeName;
        while (!this.availableLocales.contains(ln)) {
          int ix = ln.lastIndexOf("_");
          if (ix > -1) {
            ln = ln.substring(0, ix);
            continue;
          } 
          if (!"test".equals(ln)) {
            ln = "test";
            continue;
          } 
          ln = null;
        } 
        if (ln != null) {
          String name = "data/pfd_" + ln + ".xml";
          try {
            InputStream is = ICUData.getStream(getClass(), name);
            if (is == null)
              throw new MissingResourceException("no resource named " + name, name, ""); 
            DataRecord dr = DataRecord.read(ln, new XMLRecordReader(new InputStreamReader(is, "UTF-8")));
            if (dr != null)
              ld = new PeriodFormatterData(localeName, dr); 
          } catch (UnsupportedEncodingException e) {
            throw new MissingResourceException("Unhandled Encoding for resource " + name, name, "");
          } 
        } else {
          throw new MissingResourceException("Duration data not found for  " + localeName, "data/", localeName);
        } 
        this.cache.put(localeName, ld);
      } 
      this.lastData = ld;
      this.lastLocale = localeName;
      return ld;
    } 
  }
  
  public Collection<String> getAvailableLocales() {
    return this.availableLocales;
  }
}


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\com\ibm\icu\impl\duration\impl\ResourceBasedPeriodFormatterDataService.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */