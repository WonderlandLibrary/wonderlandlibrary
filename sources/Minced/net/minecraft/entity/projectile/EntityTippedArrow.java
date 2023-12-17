// 
// Decompiled by Procyon v0.5.36
// 

package net.minecraft.entity.projectile;

import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import java.util.Iterator;
import java.util.Collection;
import net.minecraft.potion.PotionUtils;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.entity.EntityLivingBase;
import com.google.common.collect.Sets;
import net.minecraft.init.PotionTypes;
import net.minecraft.world.World;
import net.minecraft.potion.PotionEffect;
import java.util.Set;
import net.minecraft.potion.PotionType;
import net.minecraft.network.datasync.DataParameter;

public class EntityTippedArrow extends EntityArrow
{
    private static final DataParameter<Integer> COLOR;
    private PotionType potion;
    private final Set<PotionEffect> customPotionEffects;
    private boolean fixedColor;
    
    public EntityTippedArrow(final World worldIn) {
        super(worldIn);
        this.potion = PotionTypes.EMPTY;
        this.customPotionEffects = (Set<PotionEffect>)Sets.newHashSet();
    }
    
    public EntityTippedArrow(final World worldIn, final double x, final double y, final double z) {
        super(worldIn, x, y, z);
        this.potion = PotionTypes.EMPTY;
        this.customPotionEffects = (Set<PotionEffect>)Sets.newHashSet();
    }
    
    public EntityTippedArrow(final World worldIn, final EntityLivingBase shooter) {
        super(worldIn, shooter);
        this.potion = PotionTypes.EMPTY;
        this.customPotionEffects = (Set<PotionEffect>)Sets.newHashSet();
    }
    
    public void setPotionEffect(final ItemStack stack) {
        if (stack.getItem() == Items.TIPPED_ARROW) {
            this.potion = PotionUtils.getPotionFromItem(stack);
            final Collection<PotionEffect> collection = PotionUtils.getFullEffectsFromItem(stack);
            if (!collection.isEmpty()) {
                for (final PotionEffect potioneffect : collection) {
                    this.customPotionEffects.add(new PotionEffect(potioneffect));
                }
            }
            final int i = getCustomColor(stack);
            if (i == -1) {
                this.refreshColor();
            }
            else {
                this.setFixedColor(i);
            }
        }
        else if (stack.getItem() == Items.ARROW) {
            this.potion = PotionTypes.EMPTY;
            this.customPotionEffects.clear();
            this.dataManager.set(EntityTippedArrow.COLOR, -1);
        }
    }
    
    public static int getCustomColor(final ItemStack p_191508_0_) {
        final NBTTagCompound nbttagcompound = p_191508_0_.getTagCompound();
        return (nbttagcompound != null && nbttagcompound.hasKey("CustomPotionColor", 99)) ? nbttagcompound.getInteger("CustomPotionColor") : -1;
    }
    
    private void refreshColor() {
        this.fixedColor = false;
        this.dataManager.set(EntityTippedArrow.COLOR, PotionUtils.getPotionColorFromEffectList(PotionUtils.mergeEffects(this.potion, this.customPotionEffects)));
    }
    
    public void addEffect(final PotionEffect effect) {
        this.customPotionEffects.add(effect);
        this.getDataManager().set(EntityTippedArrow.COLOR, PotionUtils.getPotionColorFromEffectList(PotionUtils.mergeEffects(this.potion, this.customPotionEffects)));
    }
    
    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(EntityTippedArrow.COLOR, -1);
    }
    
    @Override
    public void onUpdate() {
        super.onUpdate();
        if (this.world.isRemote) {
            if (this.inGround) {
                if (this.timeInGround % 5 == 0) {
                    this.spawnPotionParticles(1);
                }
            }
            else {
                this.spawnPotionParticles(2);
            }
        }
        else if (this.inGround && this.timeInGround != 0 && !this.customPotionEffects.isEmpty() && this.timeInGround >= 600) {
            this.world.setEntityState(this, (byte)0);
            this.potion = PotionTypes.EMPTY;
            this.customPotionEffects.clear();
            this.dataManager.set(EntityTippedArrow.COLOR, -1);
        }
    }
    
    private void spawnPotionParticles(final int particleCount) {
        final int i = this.getColor();
        if (i != -1 && particleCount > 0) {
            final double d0 = (i >> 16 & 0xFF) / 255.0;
            final double d2 = (i >> 8 & 0xFF) / 255.0;
            final double d3 = (i >> 0 & 0xFF) / 255.0;
            for (int j = 0; j < particleCount; ++j) {
                this.world.spawnParticle(EnumParticleTypes.SPELL_MOB, this.posX + (this.rand.nextDouble() - 0.5) * this.width, this.posY + this.rand.nextDouble() * this.height, this.posZ + (this.rand.nextDouble() - 0.5) * this.width, d0, d2, d3, new int[0]);
            }
        }
    }
    
    public int getColor() {
        return this.dataManager.get(EntityTippedArrow.COLOR);
    }
    
    private void setFixedColor(final int p_191507_1_) {
        this.fixedColor = true;
        this.dataManager.set(EntityTippedArrow.COLOR, p_191507_1_);
    }
    
    public static void registerFixesTippedArrow(final DataFixer fixer) {
        EntityArrow.registerFixesArrow(fixer, "TippedArrow");
    }
    
    @Override
    public void writeEntityToNBT(final NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        if (this.potion != PotionTypes.EMPTY && this.potion != null) {
            compound.setString("Potion", PotionType.REGISTRY.getNameForObject(this.potion).toString());
        }
        if (this.fixedColor) {
            compound.setInteger("Color", this.getColor());
        }
        if (!this.customPotionEffects.isEmpty()) {
            final NBTTagList nbttaglist = new NBTTagList();
            for (final PotionEffect potioneffect : this.customPotionEffects) {
                nbttaglist.appendTag(potioneffect.writeCustomPotionEffectToNBT(new NBTTagCompound()));
            }
            compound.setTag("CustomPotionEffects", nbttaglist);
        }
    }
    
    @Override
    public void readEntityFromNBT(final NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        if (compound.hasKey("Potion", 8)) {
            this.potion = PotionUtils.getPotionTypeFromNBT(compound);
        }
        for (final PotionEffect potioneffect : PotionUtils.getFullEffectsFromTag(compound)) {
            this.addEffect(potioneffect);
        }
        if (compound.hasKey("Color", 99)) {
            this.setFixedColor(compound.getInteger("Color"));
        }
        else {
            this.refreshColor();
        }
    }
    
    @Override
    protected void arrowHit(final EntityLivingBase living) {
        super.arrowHit(living);
        for (final PotionEffect potioneffect : this.potion.getEffects()) {
            living.addPotionEffect(new PotionEffect(potioneffect.getPotion(), Math.max(potioneffect.getDuration() / 8, 1), potioneffect.getAmplifier(), potioneffect.getIsAmbient(), potioneffect.doesShowParticles()));
        }
        if (!this.customPotionEffects.isEmpty()) {
            for (final PotionEffect potioneffect2 : this.customPotionEffects) {
                living.addPotionEffect(potioneffect2);
            }
        }
    }
    
    @Override
    protected ItemStack getArrowStack() {
        if (this.customPotionEffects.isEmpty() && this.potion == PotionTypes.EMPTY) {
            return new ItemStack(Items.ARROW);
        }
        final ItemStack itemstack = new ItemStack(Items.TIPPED_ARROW);
        PotionUtils.addPotionToItemStack(itemstack, this.potion);
        PotionUtils.appendEffects(itemstack, this.customPotionEffects);
        if (this.fixedColor) {
            NBTTagCompound nbttagcompound = itemstack.getTagCompound();
            if (nbttagcompound == null) {
                nbttagcompound = new NBTTagCompound();
                itemstack.setTagCompound(nbttagcompound);
            }
            nbttagcompound.setInteger("CustomPotionColor", this.getColor());
        }
        return itemstack;
    }
    
    @Override
    public void handleStatusUpdate(final byte id) {
        if (id == 0) {
            final int i = this.getColor();
            if (i != -1) {
                final double d0 = (i >> 16 & 0xFF) / 255.0;
                final double d2 = (i >> 8 & 0xFF) / 255.0;
                final double d3 = (i >> 0 & 0xFF) / 255.0;
                for (int j = 0; j < 20; ++j) {
                    this.world.spawnParticle(EnumParticleTypes.SPELL_MOB, this.posX + (this.rand.nextDouble() - 0.5) * this.width, this.posY + this.rand.nextDouble() * this.height, this.posZ + (this.rand.nextDouble() - 0.5) * this.width, d0, d2, d3, new int[0]);
                }
            }
        }
        else {
            super.handleStatusUpdate(id);
        }
    }
    
    static {
        COLOR = EntityDataManager.createKey(EntityTippedArrow.class, DataSerializers.VARINT);
    }
}
