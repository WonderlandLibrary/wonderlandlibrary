/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.apache.logging.log4j.LogManager
 *  org.apache.logging.log4j.Logger
 */
package net.minecraft.client.resources;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
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
        I18n.setLocale(currentLocale);
    }

    public void parseLanguageMetadata(List<IResourcePack> p_135043_1_) {
        this.languageMap.clear();
        Iterator<IResourcePack> iterator = p_135043_1_.iterator();
        block3: while (iterator.hasNext()) {
            IResourcePack iresourcepack = iterator.next();
            try {
                LanguageMetadataSection languagemetadatasection = (LanguageMetadataSection)iresourcepack.getPackMetadata(this.theMetadataSerializer, "language");
                if (languagemetadatasection == null) continue;
                Iterator<Language> iterator2 = languagemetadatasection.getLanguages().iterator();
                while (true) {
                    if (!iterator2.hasNext()) continue block3;
                    Language language = iterator2.next();
                    if (this.languageMap.containsKey(language.getLanguageCode())) continue;
                    this.languageMap.put(language.getLanguageCode(), language);
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

    @Override
    public void onResourceManagerReload(IResourceManager resourceManager) {
        ArrayList<String> list = Lists.newArrayList("en_US");
        if (!"en_US".equals(this.currentLanguage)) {
            list.add(this.currentLanguage);
        }
        currentLocale.loadLocaleDataFiles(resourceManager, list);
        StringTranslate.replaceWith(LanguageManager.currentLocale.properties);
    }

    public boolean isCurrentLocaleUnicode() {
        return currentLocale.isUnicode();
    }

    public boolean isCurrentLanguageBidirectional() {
        if (this.getCurrentLanguage() == null) return false;
        if (!this.getCurrentLanguage().isBidirectional()) return false;
        return true;
    }

    public void setCurrentLanguage(Language currentLanguageIn) {
        this.currentLanguage = currentLanguageIn.getLanguageCode();
    }

    public Language getCurrentLanguage() {
        Language language;
        if (this.languageMap.containsKey(this.currentLanguage)) {
            language = this.languageMap.get(this.currentLanguage);
            return language;
        }
        language = this.languageMap.get("en_US");
        return language;
    }

    public SortedSet<Language> getLanguages() {
        return Sets.newTreeSet(this.languageMap.values());
    }
}

