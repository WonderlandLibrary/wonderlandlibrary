// 
// Decompiled by Procyon v0.5.36
// 

package oshi.hardware.platform.linux;

import org.slf4j.LoggerFactory;
import oshi.hardware.SoundCard;
import java.util.Iterator;
import java.util.Map;
import oshi.util.FileUtil;
import java.util.ArrayList;
import oshi.util.platform.linux.ProcPath;
import java.io.File;
import java.util.List;
import org.slf4j.Logger;
import oshi.annotation.concurrent.Immutable;
import oshi.hardware.common.AbstractSoundCard;

@Immutable
final class LinuxSoundCard extends AbstractSoundCard
{
    private static final Logger LOG;
    private static final String CARD_FOLDER = "card";
    private static final String CARDS_FILE = "cards";
    private static final String ID_FILE = "id";
    
    LinuxSoundCard(final String kernelVersion, final String name, final String codec) {
        super(kernelVersion, name, codec);
    }
    
    private static List<File> getCardFolders() {
        final File cardsDirectory = new File(ProcPath.ASOUND);
        final List<File> cardFolders = new ArrayList<File>();
        final File[] allContents = cardsDirectory.listFiles();
        if (allContents != null) {
            for (final File card : allContents) {
                if (card.getName().startsWith("card") && card.isDirectory()) {
                    cardFolders.add(card);
                }
            }
        }
        else {
            LinuxSoundCard.LOG.warn("No Audio Cards Found");
        }
        return cardFolders;
    }
    
    private static String getSoundCardVersion() {
        final String driverVersion = FileUtil.getStringFromFile(ProcPath.ASOUND + "version");
        return driverVersion.isEmpty() ? "not available" : driverVersion;
    }
    
    private static String getCardCodec(final File cardDir) {
        String cardCodec = "";
        final File[] cardFiles = cardDir.listFiles();
        if (cardFiles != null) {
            for (final File file : cardFiles) {
                if (file.getName().startsWith("codec")) {
                    if (!file.isDirectory()) {
                        cardCodec = FileUtil.getKeyValueMapFromFile(file.getPath(), ":").get("Codec");
                    }
                    else {
                        final File[] codecs = file.listFiles();
                        if (codecs != null) {
                            for (final File codec : codecs) {
                                if (!codec.isDirectory() && codec.getName().contains("#")) {
                                    cardCodec = codec.getName().substring(0, codec.getName().indexOf(35));
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }
        return cardCodec;
    }
    
    private static String getCardName(final File file) {
        String cardName = "Not Found..";
        final Map<String, String> cardNamePairs = FileUtil.getKeyValueMapFromFile(ProcPath.ASOUND + "/" + "cards", ":");
        final String cardId = FileUtil.getStringFromFile(file.getPath() + "/" + "id");
        for (final Map.Entry<String, String> entry : cardNamePairs.entrySet()) {
            if (entry.getKey().contains(cardId)) {
                cardName = entry.getValue();
                return cardName;
            }
        }
        return cardName;
    }
    
    public static List<SoundCard> getSoundCards() {
        final List<SoundCard> soundCards = new ArrayList<SoundCard>();
        for (final File cardFile : getCardFolders()) {
            soundCards.add(new LinuxSoundCard(getSoundCardVersion(), getCardName(cardFile), getCardCodec(cardFile)));
        }
        return soundCards;
    }
    
    static {
        LOG = LoggerFactory.getLogger(LinuxSoundCard.class);
    }
}
