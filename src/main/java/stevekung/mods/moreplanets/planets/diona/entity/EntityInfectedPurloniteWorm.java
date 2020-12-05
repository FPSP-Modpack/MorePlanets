package stevekung.mods.moreplanets.planets.diona.entity;

import micdoodle8.mods.galacticraft.api.entity.IEntityBreathable;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import stevekung.mods.moreplanets.init.MPItems;
import stevekung.mods.moreplanets.init.MPPotions;
import stevekung.mods.moreplanets.init.MPSounds;

public class EntityInfectedPurloniteWorm extends EntityMob implements IEntityBreathable
{
    public EntityInfectedPurloniteWorm(World world)
    {
        super(world);
        this.setSize(0.75F, 0.4F);
    }

    @Override
    protected void initEntityAI()
    {
        this.tasks.addTask(1, new EntityAISwimming(this));
        this.tasks.addTask(4, new EntityAIAttackMelee(this, 1.0D, false));
        this.tasks.addTask(5, new AIRandomMovement(this));
        this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, true));
        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget<>(this, EntityPlayer.class, true));
    }

    @Override
    public double getYOffset()
    {
        return 0.2D;
    }

    @Override
    public float getEyeHeight()
    {
        return 0.275F;
    }

    @Override
    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(15.0D);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.25D);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(2.0D);
    }

    @Override
    public void onDeath(DamageSource source)
    {
        if (!this.world.isRemote)
        {
            if (this.rand.nextInt(5) == 0)
            {
                this.world.getEntitiesWithinAABB(EntityPlayer.class, new AxisAlignedBB(this.posX - 2.5D, this.posY - 1.5D, this.posZ - 2.5D, this.posX + 2.5D, this.posY + 1.5D, this.posZ + 2.5D)).forEach(player -> player.addPotionEffect(new PotionEffect(MPPotions.INFECTED_PURLONITE, 24, 0)));
                EntityAlbetiusWorm worm = new EntityAlbetiusWorm(this.world);
                this.world.playSound(null, this.getPosition(), MPSounds.INFECTED_MOB_EXPLODE, SoundCategory.HOSTILE, 1.0F, 1.0F);
                this.world.createExplosion(this, this.posX, this.posY, this.posZ, 0.5F + this.rand.nextInt(2), this.world.getGameRules().getBoolean("mobGriefing"));
                worm.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rand.nextFloat() * 360.0F, 0.0F);
                this.world.spawnEntity(worm);
            }
        }
    }

    @Override
    protected boolean canTriggerWalking()
    {
        return false;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source)
    {
        return MPSounds.INFECTED_MOB_HURT;
    }

    @Override
    protected SoundEvent getDeathSound()
    {
        return MPSounds.INFECTED_MOB_HURT;
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount)
    {
        if (this.isEntityInvulnerable(source))
        {
            return false;
        }
        else
        {
            return super.attackEntityFrom(source, amount);
        }
    }

    @Override
    protected void playStepSound(BlockPos pos, Block block)
    {
        this.playSound(SoundEvents.ENTITY_SILVERFISH_STEP, 0.15F, 1.0F);
    }

    @Override
    protected void dropFewItems(boolean drop, int fortune)
    {
        int j = 3 + this.rand.nextInt(4);

        if (fortune > 0)
        {
            j += this.rand.nextInt(fortune + 1);
        }
        for (int k = 0; k < j; ++k)
        {
            this.entityDropItem(new ItemStack(MPItems.INFECTED_PURLONITE_SHARD), 0.0F);
        }
    }

    @Override
    public void onUpdate()
    {
        this.renderYawOffset = this.rotationYaw;
        super.onUpdate();
    }

    @Override
    protected boolean isValidLightLevel()
    {
        return true;
    }

    @Override
    public boolean getCanSpawnHere()
    {
        return true;
    }

    @Override
    public boolean attackEntityAsMob(Entity entity)
    {
        if (super.attackEntityAsMob(entity))
        {
            if (entity instanceof EntityLivingBase)
            {
                ((EntityLivingBase)entity).addPotionEffect(new PotionEffect(MPPotions.INFECTED_PURLONITE, 40, 0));
                this.world.playSound(null, this.getPosition(), MPSounds.INFECTED_MOB_ATTACK, SoundCategory.PLAYERS, 1.0F, 1.0F);
            }
            return true;
        }
        else
        {
            return false;
        }
    }

    @Override
    public EnumCreatureAttribute getCreatureAttribute()
    {
        return EnumCreatureAttribute.ARTHROPOD;
    }

    @Override
    public boolean canBreath()
    {
        return true;
    }

    @Override
    public boolean isPotionApplicable(PotionEffect potion)
    {
        return potion.getPotion() == MPPotions.INFECTED_PURLONITE ? false : super.isPotionApplicable(potion);
    }

    static class AIRandomMovement extends EntityAIWander
    {
        public AIRandomMovement(EntityInfectedPurloniteWorm entity)
        {
            super(entity, 1.0D, 10);
            this.setMutexBits(1);
        }

        @Override
        public boolean shouldExecute()
        {
            if (this.entity.getAttackTarget() != null)
            {
                return false;
            }
            else if (!this.entity.getNavigator().noPath())
            {
                return false;
            }
            else
            {
                return super.shouldExecute();
            }
        }
    }
}