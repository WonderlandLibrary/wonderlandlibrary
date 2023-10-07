/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.Gson
 *  com.google.gson.JsonElement
 *  com.google.gson.JsonObject
 *  com.google.gson.JsonParser
 *  com.google.gson.annotations.SerializedName
 *  net.minecraft.client.gui.FontRenderer
 *  net.minecraft.client.gui.Gui
 *  net.minecraft.client.gui.GuiButton
 *  net.minecraft.client.gui.GuiScreen
 *  net.minecraft.client.gui.GuiSlot
 *  net.minecraft.client.renderer.GlStateManager
 *  org.jetbrains.annotations.NotNull
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
import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.Unit;
import kotlin.collections.CollectionsKt;
import kotlin.concurrent.ThreadsKt;
import kotlin.io.CloseableKt;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;
import net.ccbluex.liquidbounce.injection.implementations.IMixinGuiSlot;
import net.ccbluex.liquidbounce.ui.font.Fonts;
import net.ccbluex.liquidbounce.utils.ClientUtils;
import net.ccbluex.liquidbounce.utils.misc.HttpUtils;
import net.ccbluex.liquidbounce.utils.render.CustomTexture;
import net.ccbluex.liquidbounce.utils.render.RenderUtils;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiSlot;
import net.minecraft.client.renderer.GlStateManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.opengl.GL11;

@Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, xi=2, d1={"\u0000N\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u0007\n\u0002\b\u0004\n\u0002\u0010\f\n\u0002\b\t\u0018\u00002\u00020\u0001:\u0006\u001e\u001f !\"#B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0001\u00a2\u0006\u0002\u0010\u0003J\u0010\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u0010H\u0014J \u0010\u0011\u001a\u00020\u000e2\u0006\u0010\u0012\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\u00132\u0006\u0010\u0015\u001a\u00020\u0016H\u0016J\b\u0010\u0017\u001a\u00020\u000eH\u0016J\b\u0010\u0018\u001a\u00020\u000eH\u0016J\u0018\u0010\u0019\u001a\u00020\u000e2\u0006\u0010\u001a\u001a\u00020\u001b2\u0006\u0010\u001c\u001a\u00020\u0013H\u0014J\b\u0010\u001d\u001a\u00020\u000eH\u0002R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0018\u0010\u0006\u001a\f\u0012\b\u0012\u00060\bR\u00020\u00000\u0007X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0012\u0010\u000b\u001a\u00060\fR\u00020\u0000X\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0001X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006$"}, d2={"Lnet/ccbluex/liquidbounce/ui/client/GuiContributors;", "Lnet/minecraft/client/gui/GuiScreen;", "prevGui", "(Lnet/minecraft/client/gui/GuiScreen;)V", "DECIMAL_FORMAT", "Ljava/text/DecimalFormat;", "credits", "", "Lnet/ccbluex/liquidbounce/ui/client/GuiContributors$Credit;", "failed", "", "list", "Lnet/ccbluex/liquidbounce/ui/client/GuiContributors$GuiList;", "actionPerformed", "", "button", "Lnet/minecraft/client/gui/GuiButton;", "drawScreen", "mouseX", "", "mouseY", "partialTicks", "", "handleMouseInput", "initGui", "keyTyped", "typedChar", "", "keyCode", "loadCredits", "ContributorInformation", "Credit", "GitHubAuthor", "GitHubContributor", "GitHubWeek", "GuiList", "KyinoClient"})
public final class GuiContributors
extends GuiScreen {
    private final DecimalFormat DECIMAL_FORMAT;
    private GuiList list;
    private List<Credit> credits;
    private boolean failed;
    private final GuiScreen prevGui;

    public void func_73866_w_() {
        GuiList guiList = this.list = new GuiList(this);
        if (guiList == null) {
            Intrinsics.throwUninitializedPropertyAccessException("list");
        }
        guiList.func_148134_d(7, 8);
        GuiList guiList2 = this.list;
        if (guiList2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("list");
        }
        guiList2.func_148144_a(-1, false, 0, 0);
        this.field_146292_n.add(new GuiButton(1, this.field_146294_l / 2 - 100, this.field_146295_m - 30, "Back"));
        this.failed = false;
        ThreadsKt.thread$default(false, false, null, null, 0, new Function0<Unit>(this){
            final /* synthetic */ GuiContributors this$0;

            public final void invoke() {
                GuiContributors.access$loadCredits(this.this$0);
            }
            {
                this.this$0 = guiContributors;
                super(0);
            }
        }, 31, null);
    }

    public void func_73863_a(int mouseX, int mouseY, float partialTicks) {
        this.func_146278_c(0);
        GuiList guiList = this.list;
        if (guiList == null) {
            Intrinsics.throwUninitializedPropertyAccessException("list");
        }
        guiList.func_148128_a(mouseX, mouseY, partialTicks);
        Gui.func_73734_a((int)(this.field_146294_l / 4), (int)40, (int)this.field_146294_l, (int)(this.field_146295_m - 40), (int)Integer.MIN_VALUE);
        GuiList guiList2 = this.list;
        if (guiList2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("list");
        }
        if (guiList2.getSelectedSlot$KyinoClient() != -1) {
            GuiList guiList3 = this.list;
            if (guiList3 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("list");
            }
            Credit credit = this.credits.get(guiList3.getSelectedSlot$KyinoClient());
            int y = 45;
            int x = this.field_146294_l / 4 + 5;
            int infoOffset = 0;
            CustomTexture avatar = credit.getAvatar();
            int imageSize = this.field_146289_q.field_78288_b * 4;
            if (avatar != null) {
                GL11.glPushAttrib((int)1048575);
                GlStateManager.func_179141_d();
                GlStateManager.func_179147_l();
                GlStateManager.func_179120_a((int)770, (int)771, (int)1, (int)0);
                GlStateManager.func_179098_w();
                GlStateManager.func_179131_c((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
                GL11.glBindTexture((int)3553, (int)avatar.getTextureId());
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
                GL11.glBindTexture((int)3553, (int)0);
                GlStateManager.func_179084_k();
                infoOffset = imageSize;
                GL11.glPopAttrib();
            }
            y += imageSize;
            String string = "@" + credit.getName();
            float f = x + infoOffset + 5;
            Color color = Color.WHITE;
            Intrinsics.checkExpressionValueIsNotNull(color, "Color.WHITE");
            Fonts.font40.func_175065_a(string, f, 48.0f, color.getRGB(), true);
            String string2 = credit.getCommits() + " commits \u00a7a" + this.DECIMAL_FORMAT.format((Object)credit.getAdditions()) + "++ \u00a74" + this.DECIMAL_FORMAT.format((Object)credit.getDeletions()) + "--";
            float f2 = x + infoOffset + 5;
            float f3 = y - Fonts.font40.field_78288_b;
            Color color2 = Color.WHITE;
            Intrinsics.checkExpressionValueIsNotNull(color2, "Color.WHITE");
            Fonts.font40.func_175065_a(string2, f2, f3, color2.getRGB(), true);
            for (String s : credit.getContributions()) {
                GlStateManager.func_179090_x();
                GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
                GL11.glBegin((int)1);
                GL11.glVertex2i((int)x, (int)((y += Fonts.font40.field_78288_b + 2) + Fonts.font40.field_78288_b / 2 - 1));
                GL11.glVertex2i((int)(x + 3), (int)(y + Fonts.font40.field_78288_b / 2 - 1));
                GL11.glEnd();
                float f4 = (float)x + 5.0f;
                float f5 = y;
                Color color3 = Color.WHITE;
                Intrinsics.checkExpressionValueIsNotNull(color3, "Color.WHITE");
                Fonts.font40.func_175065_a(s, f4, f5, color3.getRGB(), true);
            }
        }
        Fonts.font40.drawCenteredString("Contributors", (float)this.field_146294_l / 2.0f, 6.0f, 0xFFFFFF);
        if (this.credits.isEmpty()) {
            if (this.failed) {
                double d = (double)System.currentTimeMillis() * 0.003003003003003003;
                boolean bl = false;
                int gb = (int)((Math.sin(d) + 1.0) * 127.5);
                this.func_73732_a(Fonts.font40, "Failed to load", this.field_146294_l / 8, this.field_146295_m / 2, new Color(255, gb, gb).getRGB());
            } else {
                FontRenderer fontRenderer = Fonts.font40;
                int n = this.field_146294_l / 8;
                int n2 = this.field_146295_m / 2;
                Color color = Color.WHITE;
                Intrinsics.checkExpressionValueIsNotNull(color, "Color.WHITE");
                this.func_73732_a(fontRenderer, "Loading...", n, n2, color.getRGB());
                RenderUtils.drawLoadingCircle(this.field_146294_l / 8, this.field_146295_m / 2 - 40);
            }
        }
        super.func_73863_a(mouseX, mouseY, partialTicks);
    }

    protected void func_146284_a(@NotNull GuiButton button) {
        Intrinsics.checkParameterIsNotNull(button, "button");
        if (button.field_146127_k == 1) {
            this.field_146297_k.func_147108_a(this.prevGui);
        }
    }

    protected void func_73869_a(char typedChar, int keyCode) {
        if (1 == keyCode) {
            this.field_146297_k.func_147108_a(this.prevGui);
            return;
        }
        super.func_73869_a(typedChar, keyCode);
    }

    public void func_146274_d() {
        super.func_146274_d();
        GuiList guiList = this.list;
        if (guiList == null) {
            Intrinsics.throwUninitializedPropertyAccessException("list");
        }
        guiList.func_178039_p();
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private final void loadCredits() {
        try {
            Gson gson = new Gson();
            JsonParser jsonParser = new JsonParser();
            GitHubContributor[] gitHubContributors = (GitHubContributor[])gson.fromJson(HttpUtils.get("https://api.github.com/repos/CCBlueX/LiquidBounce/stats/contributors"), GitHubContributor[].class);
            JsonElement jsonElement = jsonParser.parse(HttpUtils.get("https://raw.githubusercontent.com/CCBlueX/LiquidCloud/master/LiquidBounce/contributors.json"));
            Intrinsics.checkExpressionValueIsNotNull(jsonElement, "jsonParser.parse(HttpUti\u2026unce/contributors.json\"))");
            JsonObject additionalInformation = jsonElement.getAsJsonObject();
            ArrayList<Credit> credits = new ArrayList<Credit>(gitHubContributors.length);
            for (GitHubContributor gitHubContributor : gitHubContributors) {
                ContributorInformation contributorInformation = null;
                JsonElement jsonElement2 = additionalInformation.get(String.valueOf(gitHubContributor.getAuthor().getId()));
                if (jsonElement2 != null) {
                    contributorInformation = (ContributorInformation)gson.fromJson(jsonElement2, ContributorInformation.class);
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
                    List<String> list = Collections.emptyList();
                    object = list;
                    Intrinsics.checkExpressionValueIsNotNull(list, "Collections.emptyList()");
                }
                credits.add(new Credit(string, string2, null, additions, deletions, commits, bl, (List<String>)object));
            }
            CollectionsKt.sortWith((List)credits, (Comparator)new Comparator<Credit>(){

                public int compare(@NotNull Credit o1, @NotNull Credit o2) {
                    Intrinsics.checkParameterIsNotNull(o1, "o1");
                    Intrinsics.checkParameterIsNotNull(o2, "o2");
                    if (o1.isTeamMember() && o2.isTeamMember()) {
                        return -Intrinsics.compare(o1.getCommits(), o2.getCommits());
                    }
                    if (o1.isTeamMember()) {
                        return -1;
                    }
                    if (o2.isTeamMember()) {
                        return 1;
                    }
                    return -Intrinsics.compare(o1.getAdditions(), o2.getAdditions());
                }
            });
            this.credits = credits;
            for (Credit credit : credits) {
                try {
                    InputStream inputStream = HttpUtils.requestStream$default(HttpUtils.INSTANCE, credit.getAvatarUrl() + "?s=" + this.field_146289_q.field_78288_b * 4, "GET", null, 4, null);
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
                        CloseableKt.closeFinally(closeable, throwable);
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

    public GuiContributors(@NotNull GuiScreen prevGui) {
        Intrinsics.checkParameterIsNotNull(prevGui, "prevGui");
        this.prevGui = prevGui;
        NumberFormat numberFormat = NumberFormat.getInstance(Locale.US);
        if (numberFormat == null) {
            throw new TypeCastException("null cannot be cast to non-null type java.text.DecimalFormat");
        }
        this.DECIMAL_FORMAT = (DecimalFormat)numberFormat;
        List list = Collections.emptyList();
        Intrinsics.checkExpressionValueIsNotNull(list, "Collections.emptyList()");
        this.credits = list;
    }

    public static final /* synthetic */ void access$setCredits$p(GuiContributors $this, List list) {
        $this.credits = list;
    }

    public static final /* synthetic */ void access$loadCredits(GuiContributors $this) {
        $this.loadCredits();
    }

    @Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, xi=2, d1={"\u0000\u001e\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010 \n\u0002\b\b\b\u0080\u0004\u0018\u00002\u00020\u0001B#\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\f\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\u00030\u0007\u00a2\u0006\u0002\u0010\bR\u0017\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\u00030\u0007\u00a2\u0006\b\n\u0000\u001a\u0004\b\t\u0010\nR\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\fR\u0011\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\r\u0010\u000e\u00a8\u0006\u000f"}, d2={"Lnet/ccbluex/liquidbounce/ui/client/GuiContributors$ContributorInformation;", "", "name", "", "teamMember", "", "contributions", "", "(Lnet/ccbluex/liquidbounce/ui/client/GuiContributors;Ljava/lang/String;ZLjava/util/List;)V", "getContributions", "()Ljava/util/List;", "getName", "()Ljava/lang/String;", "getTeamMember", "()Z", "KyinoClient"})
    public final class ContributorInformation {
        @NotNull
        private final String name;
        private final boolean teamMember;
        @NotNull
        private final List<String> contributions;

        @NotNull
        public final String getName() {
            return this.name;
        }

        public final boolean getTeamMember() {
            return this.teamMember;
        }

        @NotNull
        public final List<String> getContributions() {
            return this.contributions;
        }

        public ContributorInformation(String name, @NotNull boolean teamMember, List<String> contributions) {
            Intrinsics.checkParameterIsNotNull(name, "name");
            Intrinsics.checkParameterIsNotNull(contributions, "contributions");
            this.name = name;
            this.teamMember = teamMember;
            this.contributions = contributions;
        }
    }

    @Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, xi=2, d1={"\u0000&\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\b\b\u0080\u0004\u0018\u00002\u00020\u0001B+\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0010\u0010\u0004\u001a\f\u0012\b\u0012\u00060\u0006R\u00020\u00070\u0005\u0012\n\u0010\b\u001a\u00060\tR\u00020\u0007\u00a2\u0006\u0002\u0010\nR\u0015\u0010\b\u001a\u00060\tR\u00020\u0007\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\fR\u0016\u0010\u0002\u001a\u00020\u00038\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\r\u0010\u000eR\u001b\u0010\u0004\u001a\f\u0012\b\u0012\u00060\u0006R\u00020\u00070\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\u0010\u00a8\u0006\u0011"}, d2={"Lnet/ccbluex/liquidbounce/ui/client/GuiContributors$GitHubContributor;", "", "totalContributions", "", "weeks", "", "Lnet/ccbluex/liquidbounce/ui/client/GuiContributors$GitHubWeek;", "Lnet/ccbluex/liquidbounce/ui/client/GuiContributors;", "author", "Lnet/ccbluex/liquidbounce/ui/client/GuiContributors$GitHubAuthor;", "(Lnet/ccbluex/liquidbounce/ui/client/GuiContributors;ILjava/util/List;Lnet/ccbluex/liquidbounce/ui/client/GuiContributors$GitHubAuthor;)V", "getAuthor", "()Lnet/ccbluex/liquidbounce/ui/client/GuiContributors$GitHubAuthor;", "getTotalContributions", "()I", "getWeeks", "()Ljava/util/List;", "KyinoClient"})
    public final class GitHubContributor {
        @SerializedName(value="total")
        private final int totalContributions;
        @NotNull
        private final List<GitHubWeek> weeks;
        @NotNull
        private final GitHubAuthor author;

        public final int getTotalContributions() {
            return this.totalContributions;
        }

        @NotNull
        public final List<GitHubWeek> getWeeks() {
            return this.weeks;
        }

        @NotNull
        public final GitHubAuthor getAuthor() {
            return this.author;
        }

        public GitHubContributor(@NotNull int totalContributions, @NotNull List<GitHubWeek> weeks, GitHubAuthor author) {
            Intrinsics.checkParameterIsNotNull(weeks, "weeks");
            Intrinsics.checkParameterIsNotNull(author, "author");
            this.totalContributions = totalContributions;
            this.weeks = weeks;
            this.author = author;
        }
    }

    @Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, xi=2, d1={"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0010\b\n\u0002\b\n\b\u0080\u0004\u0018\u00002\u00020\u0001B%\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0005\u0012\u0006\u0010\u0007\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\bR\u0016\u0010\u0004\u001a\u00020\u00058\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\t\u0010\nR\u0016\u0010\u0007\u001a\u00020\u00058\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\nR\u0016\u0010\u0006\u001a\u00020\u00058\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\f\u0010\nR\u0016\u0010\u0002\u001a\u00020\u00038\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\r\u0010\u000e\u00a8\u0006\u000f"}, d2={"Lnet/ccbluex/liquidbounce/ui/client/GuiContributors$GitHubWeek;", "", "timestamp", "", "additions", "", "deletions", "commits", "(Lnet/ccbluex/liquidbounce/ui/client/GuiContributors;JIII)V", "getAdditions", "()I", "getCommits", "getDeletions", "getTimestamp", "()J", "KyinoClient"})
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

    @Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, xi=2, d1={"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\b\n\u0002\b\b\b\u0080\u0004\u0018\u00002\u00020\u0001B\u001d\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0007R\u0016\u0010\u0006\u001a\u00020\u00038\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\b\u0010\tR\u0011\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u000bR\u0016\u0010\u0002\u001a\u00020\u00038\u0006X\u0087\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\f\u0010\t\u00a8\u0006\r"}, d2={"Lnet/ccbluex/liquidbounce/ui/client/GuiContributors$GitHubAuthor;", "", "name", "", "id", "", "avatarUrl", "(Lnet/ccbluex/liquidbounce/ui/client/GuiContributors;Ljava/lang/String;ILjava/lang/String;)V", "getAvatarUrl", "()Ljava/lang/String;", "getId", "()I", "getName", "KyinoClient"})
    public final class GitHubAuthor {
        @SerializedName(value="login")
        @NotNull
        private final String name;
        private final int id;
        @SerializedName(value="avatar_url")
        @NotNull
        private final String avatarUrl;

        @NotNull
        public final String getName() {
            return this.name;
        }

        public final int getId() {
            return this.id;
        }

        @NotNull
        public final String getAvatarUrl() {
            return this.avatarUrl;
        }

        public GitHubAuthor(String name, @NotNull int id, String avatarUrl) {
            Intrinsics.checkParameterIsNotNull(name, "name");
            Intrinsics.checkParameterIsNotNull(avatarUrl, "avatarUrl");
            this.name = name;
            this.id = id;
            this.avatarUrl = avatarUrl;
        }
    }

    @Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, xi=2, d1={"\u0000.\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010 \n\u0002\b\u0010\b\u0080\u0004\u0018\u00002\u00020\u0001BM\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003\u0012\b\u0010\u0005\u001a\u0004\u0018\u00010\u0006\u0012\u0006\u0010\u0007\u001a\u00020\b\u0012\u0006\u0010\t\u001a\u00020\b\u0012\u0006\u0010\n\u001a\u00020\b\u0012\u0006\u0010\u000b\u001a\u00020\f\u0012\f\u0010\r\u001a\b\u0012\u0004\u0012\u00020\u00030\u000e\u00a2\u0006\u0002\u0010\u000fR\u0011\u0010\u0007\u001a\u00020\b\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\u0011R\u001c\u0010\u0005\u001a\u0004\u0018\u00010\u0006X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0012\u0010\u0013\"\u0004\b\u0014\u0010\u0015R\u0011\u0010\u0004\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0016\u0010\u0017R\u0011\u0010\n\u001a\u00020\b\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0018\u0010\u0011R\u0017\u0010\r\u001a\b\u0012\u0004\u0012\u00020\u00030\u000e\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0019\u0010\u001aR\u0011\u0010\t\u001a\u00020\b\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001b\u0010\u0011R\u0011\u0010\u000b\u001a\u00020\f\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\u001cR\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001d\u0010\u0017\u00a8\u0006\u001e"}, d2={"Lnet/ccbluex/liquidbounce/ui/client/GuiContributors$Credit;", "", "name", "", "avatarUrl", "avatar", "Lnet/ccbluex/liquidbounce/utils/render/CustomTexture;", "additions", "", "deletions", "commits", "isTeamMember", "", "contributions", "", "(Lnet/ccbluex/liquidbounce/ui/client/GuiContributors;Ljava/lang/String;Ljava/lang/String;Lnet/ccbluex/liquidbounce/utils/render/CustomTexture;IIIZLjava/util/List;)V", "getAdditions", "()I", "getAvatar", "()Lnet/ccbluex/liquidbounce/utils/render/CustomTexture;", "setAvatar", "(Lnet/ccbluex/liquidbounce/utils/render/CustomTexture;)V", "getAvatarUrl", "()Ljava/lang/String;", "getCommits", "getContributions", "()Ljava/util/List;", "getDeletions", "()Z", "getName", "KyinoClient"})
    public final class Credit {
        @NotNull
        private final String name;
        @NotNull
        private final String avatarUrl;
        @Nullable
        private CustomTexture avatar;
        private final int additions;
        private final int deletions;
        private final int commits;
        private final boolean isTeamMember;
        @NotNull
        private final List<String> contributions;

        @NotNull
        public final String getName() {
            return this.name;
        }

        @NotNull
        public final String getAvatarUrl() {
            return this.avatarUrl;
        }

        @Nullable
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

        @NotNull
        public final List<String> getContributions() {
            return this.contributions;
        }

        public Credit(@NotNull String name, @Nullable String avatarUrl, CustomTexture avatar, int additions, int deletions, int commits, @NotNull boolean isTeamMember, List<String> contributions) {
            Intrinsics.checkParameterIsNotNull(name, "name");
            Intrinsics.checkParameterIsNotNull(avatarUrl, "avatarUrl");
            Intrinsics.checkParameterIsNotNull(contributions, "contributions");
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

    @Metadata(mv={1, 1, 16}, bv={1, 0, 3}, k=1, xi=2, d1={"\u0000(\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0002\n\u0002\b\n\n\u0002\u0010\u000b\n\u0002\b\b\b\u0082\u0004\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\b\u0010\u0007\u001a\u00020\bH\u0014J8\u0010\t\u001a\u00020\b2\u0006\u0010\n\u001a\u00020\u00062\u0006\u0010\u000b\u001a\u00020\u00062\u0006\u0010\f\u001a\u00020\u00062\u0006\u0010\r\u001a\u00020\u00062\u0006\u0010\u000e\u001a\u00020\u00062\u0006\u0010\u000f\u001a\u00020\u0006H\u0014J(\u0010\u0010\u001a\u00020\b2\u0006\u0010\u0011\u001a\u00020\u00062\u0006\u0010\u0012\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\u00062\u0006\u0010\u0015\u001a\u00020\u0006H\u0016J\r\u0010\u0016\u001a\u00020\u0006H\u0000\u00a2\u0006\u0002\b\u0017J\b\u0010\u0018\u001a\u00020\u0006H\u0014J\u0010\u0010\u0019\u001a\u00020\u00132\u0006\u0010\u001a\u001a\u00020\u0006H\u0014R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u001b"}, d2={"Lnet/ccbluex/liquidbounce/ui/client/GuiContributors$GuiList;", "Lnet/minecraft/client/gui/GuiSlot;", "gui", "Lnet/minecraft/client/gui/GuiScreen;", "(Lnet/ccbluex/liquidbounce/ui/client/GuiContributors;Lnet/minecraft/client/gui/GuiScreen;)V", "selectedSlot", "", "drawBackground", "", "drawSlot", "entryID", "p_180791_2_", "p_180791_3_", "p_180791_4_", "mouseXIn", "mouseYIn", "elementClicked", "index", "doubleClick", "", "var3", "var4", "getSelectedSlot", "getSelectedSlot$KyinoClient", "getSize", "isSelected", "id", "KyinoClient"})
    private final class GuiList
    extends GuiSlot {
        private int selectedSlot;

        protected boolean func_148131_a(int id) {
            return this.selectedSlot == id;
        }

        protected int func_148127_b() {
            return GuiContributors.this.credits.size();
        }

        public final int getSelectedSlot$KyinoClient() {
            return this.selectedSlot > GuiContributors.this.credits.size() ? -1 : this.selectedSlot;
        }

        public void func_148144_a(int index, boolean doubleClick, int var3, int var4) {
            this.selectedSlot = index;
        }

        protected void func_180791_a(int entryID, int p_180791_2_, int p_180791_3_, int p_180791_4_, int mouseXIn, int mouseYIn) {
            Credit credit = (Credit)GuiContributors.this.credits.get(entryID);
            String string = credit.getName();
            float f = (float)this.field_148155_a / 2.0f;
            float f2 = (float)p_180791_3_ + 2.0f;
            Color color = Color.WHITE;
            Intrinsics.checkExpressionValueIsNotNull(color, "Color.WHITE");
            Fonts.font40.drawCenteredString(string, f, f2, color.getRGB(), true);
        }

        protected void func_148123_a() {
        }

        public GuiList(GuiScreen gui) {
            Intrinsics.checkParameterIsNotNull(gui, "gui");
            super(GuiContributors.this.field_146297_k, gui.field_146294_l / 4, gui.field_146295_m, 40, gui.field_146295_m - 40, 15);
            GuiList guiList = this;
            if (guiList == null) {
                throw new TypeCastException("null cannot be cast to non-null type net.ccbluex.liquidbounce.injection.implementations.IMixinGuiSlot");
            }
            IMixinGuiSlot mixin = (IMixinGuiSlot)((Object)guiList);
            mixin.setListWidth(gui.field_146294_l * 3 / 13);
            mixin.setEnableScissor(true);
        }
    }
}

