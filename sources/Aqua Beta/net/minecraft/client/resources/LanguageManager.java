package net.minecraft.client.resources;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraft.client.resources.IResourcePack;
import net.minecraft.client.resources.Language;
import net.minecraft.client.resources.Locale;
import net.minecraft.client.resources.data.IMetadataSerializer;
import net.minecraft.client.resources.data.LanguageMetadataSection;
import net.minecraft.util.StringTranslate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LanguageManager
implements IResourceManagerReloadListener {
    private static final Logger logger = LogManager.getLogger();
    private final IMetadataSerializer theMetadataSerializer;
    private String currentLanguage;
    protected static final Locale currentLocale = new Locale();
    private Map<String, Language> languageMap = Maps.newHashMap();

    public LanguageManager(IMetadataSerializer theMetadataSerializerIn, String currentLanguageIn) {
        this.theMetadataSerializer = theMetadataSerializerIn;
        this.currentLanguage = currentLanguageIn;
        I18n.setLocale((Locale)currentLocale);
    }

    public void parseLanguageMetadata(List<IResourcePack> resourcesPacks) {
        this.languageMap.clear();
        for (IResourcePack iresourcepack : resourcesPacks) {
            try {
                LanguageMetadataSection languagemetadatasection = (LanguageMetadataSection)iresourcepack.getPackMetadata(this.theMetadataSerializer, "language");
                if (languagemetadatasection == null) continue;
                for (Language language : languagemetadatasection.getLanguages()) {
                    if (this.languageMap.containsKey((Object)language.getLanguageCode())) continue;
                    this.languageMap.put((Object)language.getLanguageCode(), (Object)language);
                }
            }
            catch (RuntimeException runtimeexception) {
                logger.warn("Unable to parse metadata section of resourcepack: " + iresourcepack.getPackName(), (Throwable)runtimeexception);
            }
            catch (IOException ioexception) {
                logger.warn("Unable to parse metadata section of resourcepack: " + iresourcepack.getPackName(), (Throwable)ioexception);
            }
        }
    }

    public void onResourceManagerReload(IResourceManager resourceManager) {
        ArrayList list = Lists.newArrayList((Object[])new String[]{"en_US"});
        if (!"en_US".equals((Object)this.currentLanguage)) {
            list.add((Object)this.currentLanguage);
        }
        currentLocale.loadLocaleDataFiles(resourceManager, (List)list);
        StringTranslate.replaceWith((Map)LanguageManager.currentLocale.properties);
    }

    public boolean isCurrentLocaleUnicode() {
        return currentLocale.isUnicode();
    }

    public boolean isCurrentLanguageBidirectional() {
        return this.getCurrentLanguage() != null && this.getCurrentLanguage().isBidirectional();
    }

    public void setCurrentLanguage(Language currentLanguageIn) {
        this.currentLanguage = currentLanguageIn.getLanguageCode();
    }

    public Language getCurrentLanguage() {
        return this.languageMap.containsKey((Object)this.currentLanguage) ? (Language)this.languageMap.get((Object)this.currentLanguage) : (Language)this.languageMap.get((Object)"en_US");
    }

    public SortedSet<Language> getLanguages() {
        return Sets.newTreeSet((Iterable)this.languageMap.values());
    }
}
