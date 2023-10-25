/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 *  com.google.common.collect.Maps
 *  com.google.common.collect.Sets
 *  org.apache.logging.log4j.LogManager
 *  org.apache.logging.log4j.Logger
 */
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
    private String currentLanguage;
    private static final Logger logger = LogManager.getLogger();
    private final IMetadataSerializer theMetadataSerializer;
    private Map<String, Language> languageMap = Maps.newHashMap();
    protected static final Locale currentLocale = new Locale();

    @Override
    public void onResourceManagerReload(IResourceManager iResourceManager) {
        ArrayList arrayList = Lists.newArrayList((Object[])new String[]{"en_US"});
        if (!"en_US".equals(this.currentLanguage)) {
            arrayList.add(this.currentLanguage);
        }
        currentLocale.loadLocaleDataFiles(iResourceManager, arrayList);
        StringTranslate.replaceWith(LanguageManager.currentLocale.properties);
    }

    public boolean isCurrentLanguageBidirectional() {
        return this.getCurrentLanguage() != null && this.getCurrentLanguage().isBidirectional();
    }

    public void setCurrentLanguage(Language language) {
        this.currentLanguage = language.getLanguageCode();
    }

    public void parseLanguageMetadata(List<IResourcePack> list) {
        this.languageMap.clear();
        for (IResourcePack iResourcePack : list) {
            try {
                LanguageMetadataSection languageMetadataSection = (LanguageMetadataSection)iResourcePack.getPackMetadata(this.theMetadataSerializer, "language");
                if (languageMetadataSection == null) continue;
                for (Language language : languageMetadataSection.getLanguages()) {
                    if (this.languageMap.containsKey(language.getLanguageCode())) continue;
                    this.languageMap.put(language.getLanguageCode(), language);
                }
            }
            catch (RuntimeException runtimeException) {
                logger.warn("Unable to parse metadata section of resourcepack: " + iResourcePack.getPackName(), (Throwable)runtimeException);
            }
            catch (IOException iOException) {
                logger.warn("Unable to parse metadata section of resourcepack: " + iResourcePack.getPackName(), (Throwable)iOException);
            }
        }
    }

    public SortedSet<Language> getLanguages() {
        return Sets.newTreeSet(this.languageMap.values());
    }

    public boolean isCurrentLocaleUnicode() {
        return currentLocale.isUnicode();
    }

    public Language getCurrentLanguage() {
        return this.languageMap.containsKey(this.currentLanguage) ? this.languageMap.get(this.currentLanguage) : this.languageMap.get("en_US");
    }

    public LanguageManager(IMetadataSerializer iMetadataSerializer, String string) {
        this.theMetadataSerializer = iMetadataSerializer;
        this.currentLanguage = string;
        I18n.setLocale(currentLocale);
    }
}

