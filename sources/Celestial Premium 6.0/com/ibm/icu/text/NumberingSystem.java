/*
 * Decompiled with CFR 0.150.
 */
package com.ibm.icu.text;

import com.ibm.icu.impl.ICUCache;
import com.ibm.icu.impl.ICUResourceBundle;
import com.ibm.icu.impl.SimpleCache;
import com.ibm.icu.lang.UCharacter;
import com.ibm.icu.text.UCharacterIterator;
import com.ibm.icu.util.ULocale;
import com.ibm.icu.util.UResourceBundle;
import com.ibm.icu.util.UResourceBundleIterator;
import java.util.ArrayList;
import java.util.Locale;
import java.util.MissingResourceException;

public class NumberingSystem {
    private String desc = "0123456789";
    private int radix = 10;
    private boolean algorithmic = false;
    private String name = "latn";
    private static ICUCache<String, NumberingSystem> cachedLocaleData = new SimpleCache<String, NumberingSystem>();
    private static ICUCache<String, NumberingSystem> cachedStringData = new SimpleCache<String, NumberingSystem>();

    public static NumberingSystem getInstance(int radix_in, boolean isAlgorithmic_in, String desc_in) {
        return NumberingSystem.getInstance(null, radix_in, isAlgorithmic_in, desc_in);
    }

    private static NumberingSystem getInstance(String name_in, int radix_in, boolean isAlgorithmic_in, String desc_in) {
        if (radix_in < 2) {
            throw new IllegalArgumentException("Invalid radix for numbering system");
        }
        if (!(isAlgorithmic_in || desc_in.length() == radix_in && NumberingSystem.isValidDigitString(desc_in))) {
            throw new IllegalArgumentException("Invalid digit string for numbering system");
        }
        NumberingSystem ns = new NumberingSystem();
        ns.radix = radix_in;
        ns.algorithmic = isAlgorithmic_in;
        ns.desc = desc_in;
        ns.name = name_in;
        return ns;
    }

    public static NumberingSystem getInstance(Locale inLocale) {
        return NumberingSystem.getInstance(ULocale.forLocale(inLocale));
    }

    public static NumberingSystem getInstance(ULocale locale) {
        String baseName;
        NumberingSystem ns;
        String[] OTHER_NS_KEYWORDS = new String[]{"native", "traditional", "finance"};
        Boolean nsResolved = true;
        String numbersKeyword = locale.getKeywordValue("numbers");
        if (numbersKeyword != null) {
            for (String keyword : OTHER_NS_KEYWORDS) {
                if (!numbersKeyword.equals(keyword)) continue;
                nsResolved = false;
                break;
            }
        } else {
            numbersKeyword = "default";
            nsResolved = false;
        }
        if (nsResolved.booleanValue()) {
            ns = NumberingSystem.getInstanceByName(numbersKeyword);
            if (ns != null) {
                return ns;
            }
            numbersKeyword = "default";
            nsResolved = false;
        }
        if ((ns = cachedLocaleData.get((baseName = locale.getBaseName()) + "@numbers=" + numbersKeyword)) != null) {
            return ns;
        }
        String originalNumbersKeyword = numbersKeyword;
        String resolvedNumberingSystem = null;
        while (!nsResolved.booleanValue()) {
            try {
                ICUResourceBundle rb = (ICUResourceBundle)UResourceBundle.getBundleInstance("com/ibm/icu/impl/data/icudt51b", locale);
                rb = rb.getWithFallback("NumberElements");
                resolvedNumberingSystem = rb.getStringWithFallback(numbersKeyword);
                nsResolved = true;
            }
            catch (MissingResourceException ex) {
                if (numbersKeyword.equals("native") || numbersKeyword.equals("finance")) {
                    numbersKeyword = "default";
                    continue;
                }
                if (numbersKeyword.equals("traditional")) {
                    numbersKeyword = "native";
                    continue;
                }
                nsResolved = true;
            }
        }
        if (resolvedNumberingSystem != null) {
            ns = NumberingSystem.getInstanceByName(resolvedNumberingSystem);
        }
        if (ns == null) {
            ns = new NumberingSystem();
        }
        cachedLocaleData.put(baseName + "@numbers=" + originalNumbersKeyword, ns);
        return ns;
    }

    public static NumberingSystem getInstance() {
        return NumberingSystem.getInstance(ULocale.getDefault(ULocale.Category.FORMAT));
    }

    public static NumberingSystem getInstanceByName(String name) {
        boolean isAlgorithmic;
        int radix;
        String description;
        NumberingSystem ns = cachedStringData.get(name);
        if (ns != null) {
            return ns;
        }
        try {
            UResourceBundle numberingSystemsInfo = UResourceBundle.getBundleInstance("com/ibm/icu/impl/data/icudt51b", "numberingSystems");
            UResourceBundle nsCurrent = numberingSystemsInfo.get("numberingSystems");
            UResourceBundle nsTop = nsCurrent.get(name);
            description = nsTop.getString("desc");
            UResourceBundle nsRadixBundle = nsTop.get("radix");
            UResourceBundle nsAlgBundle = nsTop.get("algorithmic");
            radix = nsRadixBundle.getInt();
            int algorithmic = nsAlgBundle.getInt();
            isAlgorithmic = algorithmic == 1;
        }
        catch (MissingResourceException ex) {
            return null;
        }
        ns = NumberingSystem.getInstance(name, radix, isAlgorithmic, description);
        cachedStringData.put(name, ns);
        return ns;
    }

    public static String[] getAvailableNames() {
        UResourceBundle numberingSystemsInfo = UResourceBundle.getBundleInstance("com/ibm/icu/impl/data/icudt51b", "numberingSystems");
        UResourceBundle nsCurrent = numberingSystemsInfo.get("numberingSystems");
        ArrayList<String> output = new ArrayList<String>();
        UResourceBundleIterator it = nsCurrent.getIterator();
        while (it.hasNext()) {
            UResourceBundle temp = it.next();
            String nsName = temp.getKey();
            output.add(nsName);
        }
        return output.toArray(new String[output.size()]);
    }

    public static boolean isValidDigitString(String str) {
        int c;
        int i = 0;
        UCharacterIterator it = UCharacterIterator.getInstance(str);
        it.setToStart();
        while ((c = it.nextCodePoint()) != -1) {
            if (UCharacter.isSupplementary(c)) {
                return false;
            }
            ++i;
        }
        return i == 10;
    }

    public int getRadix() {
        return this.radix;
    }

    public String getDescription() {
        return this.desc;
    }

    public String getName() {
        return this.name;
    }

    public boolean isAlgorithmic() {
        return this.algorithmic;
    }
}

