/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.Gson
 *  com.google.gson.JsonElement
 *  com.google.gson.JsonObject
 *  com.google.gson.JsonParser
 *  com.google.gson.annotations.SerializedName
 *  kotlin.TypeCastException
 *  kotlin.Unit
 *  kotlin.collections.CollectionsKt
 *  kotlin.concurrent.ThreadsKt
 *  kotlin.io.CloseableKt
 *  kotlin.jvm.functions.Function0
 *  kotlin.jvm.internal.Intrinsics
 *  org.jetbrains.annotations.Nullable
 *  org.lwjgl.opengl.GL11
 */
package net.ccbluex.liquidbounce.ui.client;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.annotations.SerializedName;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.Closeable;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import javax.imageio.ImageIO;
import kotlin.TypeCastException;
import kotlin.Unit;
import kotlin.collections.CollectionsKt;
import kotlin.concurrent.ThreadsKt;
import kotlin.io.CloseableKt;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.api.minecraft.client.gui.IGuiButton;
import net.ccbluex.liquidbounce.api.minecraft.client.gui.IGuiScreen;
import net.ccbluex.liquidbounce.api.util.WrappedGuiScreen;
import net.ccbluex.liquidbounce.api.util.WrappedGuiSlot;
import net.ccbluex.liquidbounce.ui.font.Fonts;
import net.ccbluex.liquidbounce.utils.ClientUtils;
import net.ccbluex.liquidbounce.utils.MinecraftInstance;
import net.ccbluex.liquidbounce.utils.misc.HttpUtils;
import net.ccbluex.liquidbounce.utils.render.CustomTexture;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.opengl.GL11;

public final class GuiContributors
extends WrappedGuiScreen {
    private final DecimalFormat DECIMAL_FORMAT;
    private GuiList list;
    private List<Credit> credits;
    private boolean failed;
    private final IGuiScreen prevGui;

    @Override
    public void initGui() {
        this.list = new GuiList(this.getRepresentedScreen());
        this.list.getRepresented().registerScrollButtons(7, 8);
        this.list.getRepresented().elementClicked(-1, false, 0, 0);
        this.getRepresentedScreen().getButtonList().add(MinecraftInstance.classProvider.createGuiButton(1, this.getRepresentedScreen().getWidth() / 2 - 100, this.getRepresentedScreen().getHeight() - 30, "Back"));
        this.failed = false;
        ThreadsKt.thread$default((boolean)false, (boolean)false, null, null, (int)0, (Function0)((Function0)new Function0<Unit>(this){
            final /* synthetic */ GuiContributors this$0;

            public final void invoke() {
                GuiContributors.access$loadCredits(this.this$0);
            }
            {
                this.this$0 = guiContributors;
                super(0);
            }
        }), (int)31, null);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.getRepresentedScreen().drawBackground(0);
        this.list.getRepresented().drawScreen(mouseX, mouseY, partialTicks);
        RenderUtils.drawRect((float)this.getRepresentedScreen().getWidth() / 4.0f, 40.0f, (float)this.getRepresentedScreen().getWidth(), (float)this.getRepresentedScreen().getHeight() - 40.0f, Integer.MIN_VALUE);
        if (this.list.getSelectedSlot$LiquidSense() != -1) {
            Credit credit = this.credits.get(this.list.getSelectedSlot$LiquidSense());
            int y = 45;
            int x = this.getRepresentedScreen().getWidth() / 4 + 5;
            int infoOffset = 0;
            CustomTexture avatar = credit.getAvatar();
            int imageSize = this.getRepresentedScreen().getFontRendererObj().getFontHeight() * 4;
            if (avatar != null) {
                GL11.glPushAttrib((int)1048575);
                MinecraftInstance.classProvider.getGlStateManager().enableAlpha();
                MinecraftInstance.classProvider.getGlStateManager().enableBlend();
                MinecraftInstance.classProvider.getGlStateManager().tryBlendFuncSeparate(770, 771, 1, 0);
                MinecraftInstance.classProvider.getGlStateManager().enableTexture2D();
                GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
                MinecraftInstance.classProvider.getGlStateManager().bindTexture(avatar.getTextureId());
                GL11.glBegin((int)7);
                GL11.glTexCoord2f((float)0.0f, (float)0.0f);
                GL11.glVertex2i((int)x, (int)y);
                GL11.glTexCoord2f((float)0.0f, (float)1.0f);
                GL11.glVertex2i((int)x, (int)(y + imageSize));
                GL11.glTexCoord2f((float)1.0f, (float)1.0f);
                GL11.glVertex2i((int)(x + imageSize), (int)(y + imageSize));
                GL11.glTexCoord2f((float)1.0f, (float)0.0f);
                GL11.glVertex2i((int)(x + imageSize), (int)y);
                GL11.glEnd();
                MinecraftInstance.classProvider.getGlStateManager().bindTexture(0);
                MinecraftInstance.classProvider.getGlStateManager().disableBlend();
                infoOffset = imageSize;
                GL11.glPopAttrib();
            }
            Fonts.font40.drawString("@" + credit.getName(), x + infoOffset + 5, 48.0f, Color.WHITE.getRGB(), true);
            Fonts.font40.drawString(credit.getCommits() + " commits \u00a7a" + this.DECIMAL_FORMAT.format((Object)credit.getAdditions()) + "++ \u00a74" + this.DECIMAL_FORMAT.format((Object)credit.getDeletions()) + "--", x + infoOffset + 5, (y += imageSize) - Fonts.font40.getFontHeight(), Color.WHITE.getRGB(), true);
            for (String s : credit.getContributions()) {
                MinecraftInstance.classProvider.getGlStateManager().disableTexture2D();
                GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
                GL11.glBegin((int)1);
                GL11.glVertex2f((float)x, (float)((float)(y += Fonts.font40.getFontHeight() + 2) + (float)Fonts.font40.getFontHeight() / 2.0f - 1.0f));
                GL11.glVertex2f((float)((float)x + 3.0f), (float)((float)y + (float)Fonts.font40.getFontHeight() / 2.0f - 1.0f));
                GL11.glEnd();
                Fonts.font40.drawString(s, (float)x + 5.0f, y, Color.WHITE.getRGB(), true);
            }
        }
        Fonts.font40.drawCenteredString("Contributors", (float)this.getRepresentedScreen().getWidth() / 2.0f, 6.0f, 0xFFFFFF);
        if (this.credits.isEmpty()) {
            if (this.failed) {
                double d = (double)System.currentTimeMillis() * 0.003003003003003003;
                boolean bl = false;
                int gb = (int)((Math.sin(d) + 1.0) * 127.5);
                Fonts.font40.drawCenteredString("Failed to load", (float)this.getRepresentedScreen().getWidth() / 8.0f, (float)this.getRepresentedScreen().getHeight() / 2.0f, new Color(255, gb, gb).getRGB());
            } else {
                Fonts.font40.drawCenteredString("Loading...", (float)this.getRepresentedScreen().getWidth() / 8.0f, (float)this.getRepresentedScreen().getHeight() / 2.0f, Color.WHITE.getRGB());
                RenderUtils.drawLoadingCircle(this.getRepresentedScreen().getWidth() / 8, this.getRepresentedScreen().getHeight() / 2 - 40);
            }
        }
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    public void actionPerformed(IGuiButton button) {
        if (button.getId() == 1) {
            MinecraftInstance.mc.displayGuiScreen(this.prevGui);
        }
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) {
        if (1 == keyCode) {
            MinecraftInstance.mc.displayGuiScreen(this.prevGui);
            return;
        }
        super.keyTyped(typedChar, keyCode);
    }

    @Override
    public void handleMouseInput() {
        super.handleMouseInput();
        this.list.getRepresented().handleMouseInput();
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private final void loadCredits() {
        try {
            Gson gson = new Gson();
            JsonParser jsonParser = new JsonParser();
            GitHubContributor[] gitHubContributors = (GitHubContributor[])gson.fromJson(HttpUtils.get("https://api.github.com/repos/CCBlueX/LiquidBounce/stats/contributors"), GitHubContributor[].class);
            JsonObject additionalInformation = jsonParser.parse(HttpUtils.get("https://raw.githubusercontent.com/CCBlueX/LiquidCloud/master/LiquidBounce/contributors.json")).getAsJsonObject();
            ArrayList<Credit> credits = new ArrayList<Credit>(gitHubContributors.length);
            for (GitHubContributor gitHubContributor : gitHubContributors) {
                ContributorInformation contributorInformation = null;
                JsonElement jsonElement = additionalInformation.get(String.valueOf(gitHubContributor.getAuthor().getId()));
                if (jsonElement != null) {
                    contributorInformation = (ContributorInformation)gson.fromJson(jsonElement, ContributorInformation.class);
                }
                int additions = 0;
                int deletions = 0;
                int commits = 0;
                for (GitHubWeek week : gitHubContributor.getWeeks()) {
                    additions += week.getAdditions();
                    deletions += week.getDeletions();
                    commits += week.getCommits();
                }
                String string = gitHubContributor.getAuthor().getName();
                String string2 = gitHubContributor.getAuthor().getAvatarUrl();
                ContributorInformation contributorInformation2 = contributorInformation;
                boolean bl = contributorInformation2 != null ? contributorInformation2.getTeamMember() : false;
                Object object = contributorInformation;
                if (object == null || (object = ((ContributorInformation)object).getContributions()) == null) {
                    object = Collections.emptyList();
                }
                credits.add(new Credit(string, string2, null, additions, deletions, commits, bl, (List<String>)object));
            }
            CollectionsKt.sortWith((List)credits, (Comparator)new Comparator<Credit>(){

                public int compare(Credit o1, Credit o2) {
                    if (o1.isTeamMember() && o2.isTeamMember()) {
                        return -Intrinsics.compare((int)o1.getCommits(), (int)o2.getCommits());
                    }
                    if (o1.isTeamMember()) {
                        return -1;
                    }
                    if (o2.isTeamMember()) {
                        return 1;
                    }
                    return -Intrinsics.compare((int)o1.getAdditions(), (int)o2.getAdditions());
                }
            });
            this.credits = credits;
            for (Credit credit : credits) {
                try {
                    InputStream inputStream = HttpUtils.requestStream$default(HttpUtils.INSTANCE, credit.getAvatarUrl() + "?s=" + this.getRepresentedScreen().getFontRendererObj().getFontHeight() * 4, "GET", null, 4, null);
                    if (inputStream == null) continue;
                    Closeable closeable = inputStream;
                    int n = 0;
                    Throwable throwable = null;
                    try {
                        InputStream it = (InputStream)closeable;
                        boolean bl = false;
                        BufferedImage bufferedImage = ImageIO.read(it);
                        if (bufferedImage == null) {
                            Intrinsics.throwNpe();
                        }
                        credit.setAvatar(new CustomTexture(bufferedImage));
                        Unit unit = Unit.INSTANCE;
                    }
                    catch (Throwable throwable2) {
                        throwable = throwable2;
                        throw throwable2;
                    }
                    finally {
                        CloseableKt.closeFinally((Closeable)closeable, (Throwable)throwable);
                    }
                }
                catch (Exception exception) {}
            }
        }
        catch (Exception e) {
            ClientUtils.getLogger().error("Failed to load credits.", (Throwable)e);
            this.failed = true;
        }
    }

    public GuiContributors(IGuiScreen prevGui) {
        this.prevGui = prevGui;
        NumberFormat numberFormat = NumberFormat.getInstance(Locale.US);
        if (numberFormat == null) {
            throw new TypeCastException("null cannot be cast to non-null type java.text.DecimalFormat");
        }
        this.DECIMAL_FORMAT = (DecimalFormat)numberFormat;
        this.credits = Collections.emptyList();
    }

    public static final /* synthetic */ void access$setCredits$p(GuiContributors $this, List list) {
        $this.credits = list;
    }

    public static final /* synthetic */ void access$loadCredits(GuiContributors $this) {
        $this.loadCredits();
    }

    public final class ContributorInformation {
        private final String name;
        private final boolean teamMember;
        private final List<String> contributions;

        public final String getName() {
            return this.name;
        }

        public final boolean getTeamMember() {
            return this.teamMember;
        }

        public final List<String> getContributions() {
            return this.contributions;
        }

        public ContributorInformation(String name, boolean teamMember, List<String> contributions) {
            this.name = name;
            this.teamMember = teamMember;
            this.contributions = contributions;
        }
    }

    public final class GitHubContributor {
        @SerializedName(value="total")
        private final int totalContributions;
        private final List<GitHubWeek> weeks;
        private final GitHubAuthor author;

        public final int getTotalContributions() {
            return this.totalContributions;
        }

        public final List<GitHubWeek> getWeeks() {
            return this.weeks;
        }

        public final GitHubAuthor getAuthor() {
            return this.author;
        }

        public GitHubContributor(int totalContributions, List<GitHubWeek> weeks, GitHubAuthor author) {
            this.totalContributions = totalContributions;
            this.weeks = weeks;
            this.author = author;
        }
    }

    public final class GitHubWeek {
        @SerializedName(value="w")
        private final long timestamp;
        @SerializedName(value="a")
        private final int additions;
        @SerializedName(value="d")
        private final int deletions;
        @SerializedName(value="c")
        private final int commits;

        public final long getTimestamp() {
            return this.timestamp;
        }

        public final int getAdditions() {
            return this.additions;
        }

        public final int getDeletions() {
            return this.deletions;
        }

        public final int getCommits() {
            return this.commits;
        }

        public GitHubWeek(long timestamp, int additions, int deletions, int commits) {
            this.timestamp = timestamp;
            this.additions = additions;
            this.deletions = deletions;
            this.commits = commits;
        }
    }

    public final class GitHubAuthor {
        @SerializedName(value="login")
        private final String name;
        private final int id;
        @SerializedName(value="avatar_url")
        private final String avatarUrl;

        public final String getName() {
            return this.name;
        }

        public final int getId() {
            return this.id;
        }

        public final String getAvatarUrl() {
            return this.avatarUrl;
        }

        public GitHubAuthor(String name, int id, String avatarUrl) {
            this.name = name;
            this.id = id;
            this.avatarUrl = avatarUrl;
        }
    }

    public final class Credit {
        private final String name;
        private final String avatarUrl;
        private CustomTexture avatar;
        private final int additions;
        private final int deletions;
        private final int commits;
        private final boolean isTeamMember;
        private final List<String> contributions;

        public final String getName() {
            return this.name;
        }

        public final String getAvatarUrl() {
            return this.avatarUrl;
        }

        public final CustomTexture getAvatar() {
            return this.avatar;
        }

        public final void setAvatar(@Nullable CustomTexture customTexture) {
            this.avatar = customTexture;
        }

        public final int getAdditions() {
            return this.additions;
        }

        public final int getDeletions() {
            return this.deletions;
        }

        public final int getCommits() {
            return this.commits;
        }

        public final boolean isTeamMember() {
            return this.isTeamMember;
        }

        public final List<String> getContributions() {
            return this.contributions;
        }

        public Credit(String name, @Nullable String avatarUrl, CustomTexture avatar, int additions, int deletions, int commits, boolean isTeamMember, List<String> contributions) {
            this.name = name;
            this.avatarUrl = avatarUrl;
            this.avatar = avatar;
            this.additions = additions;
            this.deletions = deletions;
            this.commits = commits;
            this.isTeamMember = isTeamMember;
            this.contributions = contributions;
        }
    }

    private final class GuiList
    extends WrappedGuiSlot {
        private int selectedSlot;

        @Override
        public boolean isSelected(int id) {
            return this.selectedSlot == id;
        }

        @Override
        public int getSize() {
            return GuiContributors.this.credits.size();
        }

        public final int getSelectedSlot$LiquidSense() {
            return this.selectedSlot > GuiContributors.this.credits.size() ? -1 : this.selectedSlot;
        }

        @Override
        public void elementClicked(int index, boolean doubleClick, int var3, int var4) {
            this.selectedSlot = index;
        }

        @Override
        public void drawSlot(int entryID, int p_180791_2_, int p_180791_3_, int p_180791_4_, int mouseXIn, int mouseYIn) {
            Credit credit = (Credit)GuiContributors.this.credits.get(entryID);
            Fonts.font40.drawCenteredString(credit.getName(), (float)this.getRepresented().getWidth() / 2.0f, (float)p_180791_3_ + 2.0f, Color.WHITE.getRGB(), true);
        }

        @Override
        public void drawBackground() {
        }

        public GuiList(IGuiScreen gui) {
            super(MinecraftInstance.mc, gui.getWidth() / 4, gui.getHeight(), 40, gui.getHeight() - 40, 15);
            this.getRepresented().setListWidth(gui.getWidth() * 3 / 13);
            this.getRepresented().setEnableScissor(true);
        }
    }
}

