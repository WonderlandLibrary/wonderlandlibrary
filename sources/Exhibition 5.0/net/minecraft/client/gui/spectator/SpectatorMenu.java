// 
// Decompiled by Procyon v0.5.30
// 

package net.minecraft.client.gui.spectator;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiSpectator;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import net.minecraft.client.gui.spectator.categories.SpectatorDetails;
import java.util.ArrayList;
import com.google.common.base.Objects;
import com.google.common.collect.Lists;
import java.util.List;

public class SpectatorMenu
{
    private static final ISpectatorMenuObject field_178655_b;
    private static final ISpectatorMenuObject field_178656_c;
    private static final ISpectatorMenuObject field_178653_d;
    private static final ISpectatorMenuObject field_178654_e;
    public static final ISpectatorMenuObject field_178657_a;
    private final ISpectatorMenuReciepient field_178651_f;
    private final List field_178652_g;
    private ISpectatorMenuView field_178659_h;
    private int field_178660_i;
    private int field_178658_j;
    private static final String __OBFID = "CL_00001927";
    
    public SpectatorMenu(final ISpectatorMenuReciepient p_i45497_1_) {
        this.field_178652_g = Lists.newArrayList();
        this.field_178659_h = new BaseSpectatorGroup();
        this.field_178660_i = -1;
        this.field_178651_f = p_i45497_1_;
    }
    
    public ISpectatorMenuObject func_178643_a(final int p_178643_1_) {
        final int var2 = p_178643_1_ + this.field_178658_j * 6;
        return (ISpectatorMenuObject)((this.field_178658_j > 0 && p_178643_1_ == 0) ? SpectatorMenu.field_178656_c : ((p_178643_1_ == 7) ? ((var2 < this.field_178659_h.func_178669_a().size()) ? SpectatorMenu.field_178653_d : SpectatorMenu.field_178654_e) : ((p_178643_1_ == 8) ? SpectatorMenu.field_178655_b : ((var2 >= 0 && var2 < this.field_178659_h.func_178669_a().size()) ? Objects.firstNonNull(this.field_178659_h.func_178669_a().get(var2), (Object)SpectatorMenu.field_178657_a) : SpectatorMenu.field_178657_a))));
    }
    
    public List func_178642_a() {
        final ArrayList var1 = Lists.newArrayList();
        for (int var2 = 0; var2 <= 8; ++var2) {
            var1.add(this.func_178643_a(var2));
        }
        return var1;
    }
    
    public ISpectatorMenuObject func_178645_b() {
        return this.func_178643_a(this.field_178660_i);
    }
    
    public ISpectatorMenuView func_178650_c() {
        return this.field_178659_h;
    }
    
    public void func_178644_b(final int p_178644_1_) {
        final ISpectatorMenuObject var2 = this.func_178643_a(p_178644_1_);
        if (var2 != SpectatorMenu.field_178657_a) {
            if (this.field_178660_i == p_178644_1_ && var2.func_178662_A_()) {
                var2.func_178661_a(this);
            }
            else {
                this.field_178660_i = p_178644_1_;
            }
        }
    }
    
    public void func_178641_d() {
        this.field_178651_f.func_175257_a(this);
    }
    
    public int func_178648_e() {
        return this.field_178660_i;
    }
    
    public void func_178647_a(final ISpectatorMenuView p_178647_1_) {
        this.field_178652_g.add(this.func_178646_f());
        this.field_178659_h = p_178647_1_;
        this.field_178660_i = -1;
        this.field_178658_j = 0;
    }
    
    public SpectatorDetails func_178646_f() {
        return new SpectatorDetails(this.field_178659_h, this.func_178642_a(), this.field_178660_i);
    }
    
    static {
        field_178655_b = new EndSpectatorObject(null);
        field_178656_c = new MoveMenuObject(-1, true);
        field_178653_d = new MoveMenuObject(1, true);
        field_178654_e = new MoveMenuObject(1, false);
        field_178657_a = new ISpectatorMenuObject() {
            private static final String __OBFID = "CL_00001926";
            
            @Override
            public void func_178661_a(final SpectatorMenu p_178661_1_) {
            }
            
            @Override
            public IChatComponent func_178664_z_() {
                return new ChatComponentText("");
            }
            
            @Override
            public void func_178663_a(final float p_178663_1_, final int p_178663_2_) {
            }
            
            @Override
            public boolean func_178662_A_() {
                return false;
            }
        };
    }
    
    static class EndSpectatorObject implements ISpectatorMenuObject
    {
        private static final String __OBFID = "CL_00001925";
        
        private EndSpectatorObject() {
        }
        
        @Override
        public void func_178661_a(final SpectatorMenu p_178661_1_) {
            p_178661_1_.func_178641_d();
        }
        
        @Override
        public IChatComponent func_178664_z_() {
            return new ChatComponentText("Close menu");
        }
        
        @Override
        public void func_178663_a(final float p_178663_1_, final int p_178663_2_) {
            Minecraft.getMinecraft().getTextureManager().bindTexture(GuiSpectator.field_175269_a);
            Gui.drawModalRectWithCustomSizedTexture(0, 0, 128.0f, 0.0f, 16, 16, 256.0f, 256.0f);
        }
        
        @Override
        public boolean func_178662_A_() {
            return true;
        }
        
        EndSpectatorObject(final Object p_i45496_1_) {
            this();
        }
    }
    
    static class MoveMenuObject implements ISpectatorMenuObject
    {
        private final int field_178666_a;
        private final boolean field_178665_b;
        private static final String __OBFID = "CL_00001924";
        
        public MoveMenuObject(final int p_i45495_1_, final boolean p_i45495_2_) {
            this.field_178666_a = p_i45495_1_;
            this.field_178665_b = p_i45495_2_;
        }
        
        @Override
        public void func_178661_a(final SpectatorMenu p_178661_1_) {
            p_178661_1_.field_178658_j = this.field_178666_a;
        }
        
        @Override
        public IChatComponent func_178664_z_() {
            return (this.field_178666_a < 0) ? new ChatComponentText("Previous Page") : new ChatComponentText("Next Page");
        }
        
        @Override
        public void func_178663_a(final float p_178663_1_, final int p_178663_2_) {
            Minecraft.getMinecraft().getTextureManager().bindTexture(GuiSpectator.field_175269_a);
            if (this.field_178666_a < 0) {
                Gui.drawModalRectWithCustomSizedTexture(0, 0, 144.0f, 0.0f, 16, 16, 256.0f, 256.0f);
            }
            else {
                Gui.drawModalRectWithCustomSizedTexture(0, 0, 160.0f, 0.0f, 16, 16, 256.0f, 256.0f);
            }
        }
        
        @Override
        public boolean func_178662_A_() {
            return this.field_178665_b;
        }
    }
}
