// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.advancements;

import java.lang.reflect.Type;
import com.google.gson.GsonBuilder;
import org.apache.logging.log4j.LogManager;
import net.minecraft.network.play.server.SPacketSelectAdvancementsTab;
import net.minecraft.network.Packet;
import java.util.Collection;
import net.minecraft.network.play.server.SPacketAdvancementInfo;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import java.util.stream.Stream;
import java.io.IOException;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.function.Function;
import java.util.Comparator;
import com.google.gson.JsonParseException;
import net.minecraft.util.JsonUtils;
import com.google.common.io.Files;
import java.nio.charset.StandardCharsets;
import java.util.List;
import com.google.common.collect.Lists;
import java.util.Iterator;
import com.google.common.collect.Sets;
import com.google.common.collect.Maps;
import javax.annotation.Nullable;
import net.minecraft.entity.player.EntityPlayerMP;
import java.util.Set;
import java.io.File;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import java.util.Map;
import com.google.gson.reflect.TypeToken;
import com.google.gson.Gson;
import org.apache.logging.log4j.Logger;

public class PlayerAdvancements
{
    private static final Logger LOGGER;
    private static final Gson GSON;
    private static final TypeToken<Map<ResourceLocation, AdvancementProgress>> MAP_TOKEN;
    private final MinecraftServer server;
    private final File progressFile;
    private final Map<Advancement, AdvancementProgress> progress;
    private final Set<Advancement> visible;
    private final Set<Advancement> visibilityChanged;
    private final Set<Advancement> progressChanged;
    private EntityPlayerMP player;
    @Nullable
    private Advancement lastSelectedTab;
    private boolean isFirstPacket;
    
    public PlayerAdvancements(final MinecraftServer server, final File p_i47422_2_, final EntityPlayerMP player) {
        this.progress = (Map<Advancement, AdvancementProgress>)Maps.newLinkedHashMap();
        this.visible = (Set<Advancement>)Sets.newLinkedHashSet();
        this.visibilityChanged = (Set<Advancement>)Sets.newLinkedHashSet();
        this.progressChanged = (Set<Advancement>)Sets.newLinkedHashSet();
        this.isFirstPacket = true;
        this.server = server;
        this.progressFile = p_i47422_2_;
        this.player = player;
        this.load();
    }
    
    public void setPlayer(final EntityPlayerMP player) {
        this.player = player;
    }
    
    public void dispose() {
        for (final ICriterionTrigger<?> icriteriontrigger : CriteriaTriggers.getAll()) {
            icriteriontrigger.removeAllListeners(this);
        }
    }
    
    public void reload() {
        this.dispose();
        this.progress.clear();
        this.visible.clear();
        this.visibilityChanged.clear();
        this.progressChanged.clear();
        this.isFirstPacket = true;
        this.lastSelectedTab = null;
        this.load();
    }
    
    private void registerListeners() {
        for (final Advancement advancement : this.server.getAdvancementManager().getAdvancements()) {
            this.registerListeners(advancement);
        }
    }
    
    private void ensureAllVisible() {
        final List<Advancement> list = (List<Advancement>)Lists.newArrayList();
        for (final Map.Entry<Advancement, AdvancementProgress> entry : this.progress.entrySet()) {
            if (entry.getValue().isDone()) {
                list.add(entry.getKey());
                this.progressChanged.add(entry.getKey());
            }
        }
        for (final Advancement advancement : list) {
            this.ensureVisibility(advancement);
        }
    }
    
    private void checkForAutomaticTriggers() {
        for (final Advancement advancement : this.server.getAdvancementManager().getAdvancements()) {
            if (advancement.getCriteria().isEmpty()) {
                this.grantCriterion(advancement, "");
                advancement.getRewards().apply(this.player);
            }
        }
    }
    
    private void load() {
        if (this.progressFile.isFile()) {
            try {
                final String s = Files.toString(this.progressFile, StandardCharsets.UTF_8);
                final Map<ResourceLocation, AdvancementProgress> map = JsonUtils.gsonDeserialize(PlayerAdvancements.GSON, s, PlayerAdvancements.MAP_TOKEN.getType());
                if (map == null) {
                    throw new JsonParseException("Found null for advancements");
                }
                final Stream<Map.Entry<ResourceLocation, AdvancementProgress>> stream = map.entrySet().stream().sorted(Comparator.comparing((Function<? super Map.Entry<ResourceLocation, AdvancementProgress>, ? extends Comparable>)Map.Entry::getValue));
                for (final Map.Entry<ResourceLocation, AdvancementProgress> entry : stream.collect((Collector<? super Map.Entry<ResourceLocation, AdvancementProgress>, ?, List<? super Map.Entry<ResourceLocation, AdvancementProgress>>>)Collectors.toList())) {
                    final Advancement advancement = this.server.getAdvancementManager().getAdvancement(entry.getKey());
                    if (advancement == null) {
                        PlayerAdvancements.LOGGER.warn("Ignored advancement '" + entry.getKey() + "' in progress file " + this.progressFile + " - it doesn't exist anymore?");
                    }
                    else {
                        this.startProgress(advancement, entry.getValue());
                    }
                }
            }
            catch (JsonParseException jsonparseexception) {
                PlayerAdvancements.LOGGER.error("Couldn't parse player advancements in " + this.progressFile, (Throwable)jsonparseexception);
            }
            catch (IOException ioexception) {
                PlayerAdvancements.LOGGER.error("Couldn't access player advancements in " + this.progressFile, (Throwable)ioexception);
            }
        }
        this.checkForAutomaticTriggers();
        this.ensureAllVisible();
        this.registerListeners();
    }
    
    public void save() {
        final Map<ResourceLocation, AdvancementProgress> map = (Map<ResourceLocation, AdvancementProgress>)Maps.newHashMap();
        for (final Map.Entry<Advancement, AdvancementProgress> entry : this.progress.entrySet()) {
            final AdvancementProgress advancementprogress = entry.getValue();
            if (advancementprogress.hasProgress()) {
                map.put(entry.getKey().getId(), advancementprogress);
            }
        }
        if (this.progressFile.getParentFile() != null) {
            this.progressFile.getParentFile().mkdirs();
        }
        try {
            Files.write((CharSequence)PlayerAdvancements.GSON.toJson((Object)map), this.progressFile, StandardCharsets.UTF_8);
        }
        catch (IOException ioexception) {
            PlayerAdvancements.LOGGER.error("Couldn't save player advancements to " + this.progressFile, (Throwable)ioexception);
        }
    }
    
    public boolean grantCriterion(final Advancement p_192750_1_, final String p_192750_2_) {
        boolean flag = false;
        final AdvancementProgress advancementprogress = this.getProgress(p_192750_1_);
        final boolean flag2 = advancementprogress.isDone();
        if (advancementprogress.grantCriterion(p_192750_2_)) {
            this.unregisterListeners(p_192750_1_);
            this.progressChanged.add(p_192750_1_);
            flag = true;
            if (!flag2 && advancementprogress.isDone()) {
                p_192750_1_.getRewards().apply(this.player);
                if (p_192750_1_.getDisplay() != null && p_192750_1_.getDisplay().shouldAnnounceToChat() && this.player.world.getGameRules().getBoolean("announceAdvancements")) {
                    this.server.getPlayerList().sendMessage(new TextComponentTranslation("chat.type.advancement." + p_192750_1_.getDisplay().getFrame().getName(), new Object[] { this.player.getDisplayName(), p_192750_1_.getDisplayText() }));
                }
            }
        }
        if (advancementprogress.isDone()) {
            this.ensureVisibility(p_192750_1_);
        }
        return flag;
    }
    
    public boolean revokeCriterion(final Advancement p_192744_1_, final String p_192744_2_) {
        boolean flag = false;
        final AdvancementProgress advancementprogress = this.getProgress(p_192744_1_);
        if (advancementprogress.revokeCriterion(p_192744_2_)) {
            this.registerListeners(p_192744_1_);
            this.progressChanged.add(p_192744_1_);
            flag = true;
        }
        if (!advancementprogress.hasProgress()) {
            this.ensureVisibility(p_192744_1_);
        }
        return flag;
    }
    
    private void registerListeners(final Advancement p_193764_1_) {
        final AdvancementProgress advancementprogress = this.getProgress(p_193764_1_);
        if (!advancementprogress.isDone()) {
            for (final Map.Entry<String, Criterion> entry : p_193764_1_.getCriteria().entrySet()) {
                final CriterionProgress criterionprogress = advancementprogress.getCriterionProgress(entry.getKey());
                if (criterionprogress != null && !criterionprogress.isObtained()) {
                    final ICriterionInstance icriterioninstance = entry.getValue().getCriterionInstance();
                    if (icriterioninstance == null) {
                        continue;
                    }
                    final ICriterionTrigger<ICriterionInstance> icriteriontrigger = CriteriaTriggers.get(icriterioninstance.getId());
                    if (icriteriontrigger == null) {
                        continue;
                    }
                    icriteriontrigger.addListener(this, new ICriterionTrigger.Listener<ICriterionInstance>(icriterioninstance, p_193764_1_, entry.getKey()));
                }
            }
        }
    }
    
    private void unregisterListeners(final Advancement p_193765_1_) {
        final AdvancementProgress advancementprogress = this.getProgress(p_193765_1_);
        for (final Map.Entry<String, Criterion> entry : p_193765_1_.getCriteria().entrySet()) {
            final CriterionProgress criterionprogress = advancementprogress.getCriterionProgress(entry.getKey());
            if (criterionprogress != null && (criterionprogress.isObtained() || advancementprogress.isDone())) {
                final ICriterionInstance icriterioninstance = entry.getValue().getCriterionInstance();
                if (icriterioninstance == null) {
                    continue;
                }
                final ICriterionTrigger<ICriterionInstance> icriteriontrigger = CriteriaTriggers.get(icriterioninstance.getId());
                if (icriteriontrigger == null) {
                    continue;
                }
                icriteriontrigger.removeListener(this, new ICriterionTrigger.Listener<ICriterionInstance>(icriterioninstance, p_193765_1_, entry.getKey()));
            }
        }
    }
    
    public void flushDirty(final EntityPlayerMP p_192741_1_) {
        if (!this.visibilityChanged.isEmpty() || !this.progressChanged.isEmpty()) {
            final Map<ResourceLocation, AdvancementProgress> map = (Map<ResourceLocation, AdvancementProgress>)Maps.newHashMap();
            final Set<Advancement> set = (Set<Advancement>)Sets.newLinkedHashSet();
            final Set<ResourceLocation> set2 = (Set<ResourceLocation>)Sets.newLinkedHashSet();
            for (final Advancement advancement : this.progressChanged) {
                if (this.visible.contains(advancement)) {
                    map.put(advancement.getId(), this.progress.get(advancement));
                }
            }
            for (final Advancement advancement2 : this.visibilityChanged) {
                if (this.visible.contains(advancement2)) {
                    set.add(advancement2);
                }
                else {
                    set2.add(advancement2.getId());
                }
            }
            if (!map.isEmpty() || !set.isEmpty() || !set2.isEmpty()) {
                p_192741_1_.connection.sendPacket(new SPacketAdvancementInfo(this.isFirstPacket, set, set2, map));
                this.visibilityChanged.clear();
                this.progressChanged.clear();
            }
        }
        this.isFirstPacket = false;
    }
    
    public void setSelectedTab(@Nullable final Advancement p_194220_1_) {
        final Advancement advancement = this.lastSelectedTab;
        if (p_194220_1_ != null && p_194220_1_.getParent() == null && p_194220_1_.getDisplay() != null) {
            this.lastSelectedTab = p_194220_1_;
        }
        else {
            this.lastSelectedTab = null;
        }
        if (advancement != this.lastSelectedTab) {
            this.player.connection.sendPacket(new SPacketSelectAdvancementsTab((this.lastSelectedTab == null) ? null : this.lastSelectedTab.getId()));
        }
    }
    
    public AdvancementProgress getProgress(final Advancement advancementIn) {
        AdvancementProgress advancementprogress = this.progress.get(advancementIn);
        if (advancementprogress == null) {
            advancementprogress = new AdvancementProgress();
            this.startProgress(advancementIn, advancementprogress);
        }
        return advancementprogress;
    }
    
    private void startProgress(final Advancement p_192743_1_, final AdvancementProgress p_192743_2_) {
        p_192743_2_.update(p_192743_1_.getCriteria(), p_192743_1_.getRequirements());
        this.progress.put(p_192743_1_, p_192743_2_);
    }
    
    private void ensureVisibility(final Advancement p_192742_1_) {
        final boolean flag = this.shouldBeVisible(p_192742_1_);
        final boolean flag2 = this.visible.contains(p_192742_1_);
        if (flag && !flag2) {
            this.visible.add(p_192742_1_);
            this.visibilityChanged.add(p_192742_1_);
            if (this.progress.containsKey(p_192742_1_)) {
                this.progressChanged.add(p_192742_1_);
            }
        }
        else if (!flag && flag2) {
            this.visible.remove(p_192742_1_);
            this.visibilityChanged.add(p_192742_1_);
        }
        if (flag != flag2 && p_192742_1_.getParent() != null) {
            this.ensureVisibility(p_192742_1_.getParent());
        }
        for (final Advancement advancement : p_192742_1_.getChildren()) {
            this.ensureVisibility(advancement);
        }
    }
    
    private boolean shouldBeVisible(Advancement p_192738_1_) {
        for (int i = 0; p_192738_1_ != null && i <= 2; p_192738_1_ = p_192738_1_.getParent(), ++i) {
            if (i == 0 && this.hasCompletedChildrenOrSelf(p_192738_1_)) {
                return true;
            }
            if (p_192738_1_.getDisplay() == null) {
                return false;
            }
            final AdvancementProgress advancementprogress = this.getProgress(p_192738_1_);
            if (advancementprogress.isDone()) {
                return true;
            }
            if (p_192738_1_.getDisplay().isHidden()) {
                return false;
            }
        }
        return false;
    }
    
    private boolean hasCompletedChildrenOrSelf(final Advancement p_192746_1_) {
        final AdvancementProgress advancementprogress = this.getProgress(p_192746_1_);
        if (advancementprogress.isDone()) {
            return true;
        }
        for (final Advancement advancement : p_192746_1_.getChildren()) {
            if (this.hasCompletedChildrenOrSelf(advancement)) {
                return true;
            }
        }
        return false;
    }
    
    static {
        LOGGER = LogManager.getLogger();
        GSON = new GsonBuilder().registerTypeAdapter((Type)AdvancementProgress.class, (Object)new AdvancementProgress.Serializer()).registerTypeAdapter((Type)ResourceLocation.class, (Object)new ResourceLocation.Serializer()).setPrettyPrinting().create();
        MAP_TOKEN = new TypeToken<Map<ResourceLocation, AdvancementProgress>>() {};
    }
}
