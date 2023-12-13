// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.client.resources;

import org.apache.logging.log4j.LogManager;
import com.google.common.collect.Sets;
import java.util.SortedSet;
import java.util.ArrayList;
import net.minecraft.util.StringTranslate;
import com.google.common.collect.Lists;
import java.util.Iterator;
import java.io.IOException;
import net.minecraft.client.resources.data.LanguageMetadataSection;
import java.util.List;
import com.google.common.collect.Maps;
import java.util.Map;
import net.minecraft.client.resources.data.IMetadataSerializer;
import org.apache.logging.log4j.Logger;

public class LanguageManager implements IResourceManagerReloadListener
{
    private static final Logger logger;
    private final IMetadataSerializer theMetadataSerializer;
    private String currentLanguage;
    protected static final Locale currentLocale;
    private Map languageMap;
    private static final String __OBFID = "CL_00001096";
    
    public LanguageManager(final IMetadataSerializer p_i1304_1_, final String p_i1304_2_) {
        this.languageMap = Maps.newHashMap();
        this.theMetadataSerializer = p_i1304_1_;
        this.currentLanguage = p_i1304_2_;
        I18n.setLocale(LanguageManager.currentLocale);
    }
    
    public void parseLanguageMetadata(final List p_135043_1_) {
        this.languageMap.clear();
        for (final IResourcePack var3 : p_135043_1_) {
            try {
                final LanguageMetadataSection var4 = (LanguageMetadataSection)var3.getPackMetadata(this.theMetadataSerializer, "language");
                if (var4 == null) {
                    continue;
                }
                for (final Language var6 : var4.getLanguages()) {
                    if (!this.languageMap.containsKey(var6.getLanguageCode())) {
                        this.languageMap.put(var6.getLanguageCode(), var6);
                    }
                }
            }
            catch (RuntimeException var7) {
                LanguageManager.logger.warn("Unable to parse metadata section of resourcepack: " + var3.getPackName(), (Throwable)var7);
            }
            catch (IOException var8) {
                LanguageManager.logger.warn("Unable to parse metadata section of resourcepack: " + var3.getPackName(), (Throwable)var8);
            }
        }
    }
    
    @Override
    public void onResourceManagerReload(final IResourceManager p_110549_1_) {
        final ArrayList var2 = Lists.newArrayList((Object[])new String[] { "en_US" });
        if (!"en_US".equals(this.currentLanguage)) {
            var2.add(this.currentLanguage);
        }
        LanguageManager.currentLocale.loadLocaleDataFiles(p_110549_1_, var2);
        StringTranslate.replaceWith(LanguageManager.currentLocale.field_135032_a);
    }
    
    public boolean isCurrentLocaleUnicode() {
        return LanguageManager.currentLocale.isUnicode();
    }
    
    public boolean isCurrentLanguageBidirectional() {
        return this.getCurrentLanguage() != null && this.getCurrentLanguage().isBidirectional();
    }
    
    public void setCurrentLanguage(final Language p_135045_1_) {
        this.currentLanguage = p_135045_1_.getLanguageCode();
    }
    
    public Language getCurrentLanguage() {
        return this.languageMap.containsKey(this.currentLanguage) ? this.languageMap.get(this.currentLanguage) : this.languageMap.get("en_US");
    }
    
    public SortedSet getLanguages() {
        return Sets.newTreeSet((Iterable)this.languageMap.values());
    }
    
    static {
        logger = LogManager.getLogger();
        currentLocale = new Locale();
    }
}
