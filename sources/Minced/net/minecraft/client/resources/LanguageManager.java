// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.client.resources;

import org.apache.logging.log4j.LogManager;
import com.google.common.collect.Sets;
import java.util.SortedSet;
import net.minecraft.util.text.translation.LanguageMap;
import com.google.common.collect.Lists;
import java.util.Iterator;
import java.io.IOException;
import net.minecraft.client.resources.data.LanguageMetadataSection;
import java.util.List;
import com.google.common.collect.Maps;
import java.util.Map;
import net.minecraft.client.resources.data.MetadataSerializer;
import org.apache.logging.log4j.Logger;

public class LanguageManager implements IResourceManagerReloadListener
{
    private static final Logger LOGGER;
    private final MetadataSerializer metadataSerializer;
    private String currentLanguage;
    protected static final Locale CURRENT_LOCALE;
    private final Map<String, Language> languageMap;
    
    public LanguageManager(final MetadataSerializer theMetadataSerializerIn, final String currentLanguageIn) {
        this.languageMap = (Map<String, Language>)Maps.newHashMap();
        this.metadataSerializer = theMetadataSerializerIn;
        this.currentLanguage = currentLanguageIn;
        I18n.setLocale(LanguageManager.CURRENT_LOCALE);
    }
    
    public void parseLanguageMetadata(final List<IResourcePack> resourcesPacks) {
        this.languageMap.clear();
        for (final IResourcePack iresourcepack : resourcesPacks) {
            try {
                final LanguageMetadataSection languagemetadatasection = iresourcepack.getPackMetadata(this.metadataSerializer, "language");
                if (languagemetadatasection == null) {
                    continue;
                }
                for (final Language language : languagemetadatasection.getLanguages()) {
                    if (!this.languageMap.containsKey(language.getLanguageCode())) {
                        this.languageMap.put(language.getLanguageCode(), language);
                    }
                }
            }
            catch (RuntimeException runtimeexception) {
                LanguageManager.LOGGER.warn("Unable to parse language metadata section of resourcepack: {}", (Object)iresourcepack.getPackName(), (Object)runtimeexception);
            }
            catch (IOException ioexception) {
                LanguageManager.LOGGER.warn("Unable to parse language metadata section of resourcepack: {}", (Object)iresourcepack.getPackName(), (Object)ioexception);
            }
        }
    }
    
    @Override
    public void onResourceManagerReload(final IResourceManager resourceManager) {
        final List<String> list = (List<String>)Lists.newArrayList((Object[])new String[] { "en_us" });
        if (!"en_us".equals(this.currentLanguage)) {
            list.add(this.currentLanguage);
        }
        LanguageManager.CURRENT_LOCALE.loadLocaleDataFiles(resourceManager, list);
        LanguageMap.replaceWith(LanguageManager.CURRENT_LOCALE.properties);
    }
    
    public boolean isCurrentLocaleUnicode() {
        return LanguageManager.CURRENT_LOCALE.isUnicode();
    }
    
    public boolean isCurrentLanguageBidirectional() {
        return this.getCurrentLanguage() != null && this.getCurrentLanguage().isBidirectional();
    }
    
    public void setCurrentLanguage(final Language currentLanguageIn) {
        this.currentLanguage = currentLanguageIn.getLanguageCode();
    }
    
    public Language getCurrentLanguage() {
        final String s = this.languageMap.containsKey(this.currentLanguage) ? this.currentLanguage : "en_us";
        return this.languageMap.get(s);
    }
    
    public SortedSet<Language> getLanguages() {
        return (SortedSet<Language>)Sets.newTreeSet((Iterable)this.languageMap.values());
    }
    
    public Language getLanguage(final String p_191960_1_) {
        return this.languageMap.get(p_191960_1_);
    }
    
    static {
        LOGGER = LogManager.getLogger();
        CURRENT_LOCALE = new Locale();
    }
}
