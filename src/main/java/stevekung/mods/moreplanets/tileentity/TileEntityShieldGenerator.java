package stevekung.mods.moreplanets.tileentity;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import micdoodle8.mods.galacticraft.core.Constants;
import micdoodle8.mods.galacticraft.core.blocks.BlockMulti.EnumBlockMultiType;
import micdoodle8.mods.galacticraft.core.energy.item.ItemElectricBase;
import micdoodle8.mods.galacticraft.core.entities.IBubbleProvider;
import micdoodle8.mods.galacticraft.core.tile.IMultiBlock;
import micdoodle8.mods.miccore.Annotations.NetworkedField;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityFireball;
import net.minecraft.entity.projectile.EntityPotion;
import net.minecraft.entity.projectile.EntityShulkerBullet;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import stevekung.mods.moreplanets.blocks.BlockShieldGenerator;
import stevekung.mods.moreplanets.core.MorePlanetsMod;
import stevekung.mods.moreplanets.init.MPBlocks;
import stevekung.mods.moreplanets.init.MPSounds;
import stevekung.mods.moreplanets.utils.EnumParticleTypesMP;
import stevekung.mods.stevekunglib.utils.BlockStateProperty;
import stevekung.mods.stevekunglib.utils.LangUtils;

public class TileEntityShieldGenerator extends TileEntityDummy implements IMultiBlock, IBubbleProvider
{
    public int renderTicks;
    public int solarRotate;
    @NetworkedField(targetSide = Side.CLIENT)
    public float shieldSize;
    @NetworkedField(targetSide = Side.CLIENT)
    public boolean shouldRender = true;
    @NetworkedField(targetSide = Side.CLIENT)
    public boolean enableShield = true;
    @NetworkedField(targetSide = Side.CLIENT)
    public boolean enableDamage = true;
    @NetworkedField(targetSide = Side.CLIENT)
    public int maxShieldSize = 16;
    @NetworkedField(targetSide = Side.CLIENT)
    public int maxShieldSizeUpgrade;
    @NetworkedField(targetSide = Side.CLIENT)
    public int shieldCapacity;
    @NetworkedField(targetSide = Side.CLIENT)
    public int maxShieldCapacity = 16000;
    @NetworkedField(targetSide = Side.CLIENT)
    public int shieldDamage = 8;
    @NetworkedField(targetSide = Side.CLIENT)
    public int maxShieldDamage;
    @NetworkedField(targetSide = Side.CLIENT)
    public int shieldChargeCooldown = 1200;
    public boolean needCharged;
    @NetworkedField(targetSide = Side.CLIENT)
    public String ownerUUID = "";
    private boolean initialize;

    public TileEntityShieldGenerator()
    {
        super("container.shield_generator.name");
        this.inventory = NonNullList.withSize(4, ItemStack.EMPTY);
        this.storage.setMaxExtract(250);
        this.storage.setCapacity(100000.0F);
    }

    @Override
    public void update()
    {
        super.update();
        this.renderTicks++;

        if (!this.initialize)
        {
            this.renderTicks = this.renderTicks + this.world.rand.nextInt(100);
            this.solarRotate = this.solarRotate + this.world.rand.nextInt(360);
            this.initialize = TileEntityDummy.initialiseMultiTiles(this.getPos(), this.getWorld(), this);
        }
        if (this.hasEnoughEnergyToRun && !this.disabled)
        {
            this.solarRotate++;
            this.solarRotate %= 360;
            MorePlanetsMod.PROXY.spawnParticle(EnumParticleTypesMP.ALIEN_MINER_SPARK, this.pos.getX() + 0.5D, this.pos.getY() + 1.5D, this.pos.getZ() + 0.5D, new Object[] { -0.5F });

            if (this.ticks % 33 == 0)
            {
                this.world.playSound(null, this.getPos(), MPSounds.MACHINE_GENERATOR_AMBIENT, SoundCategory.BLOCKS, 0.075F, 1.0F);
            }
        }
        if (!this.world.isRemote)
        {
            int count = 0;
            int capacityUpgradeCount = 0;

            // shield damage upgrade
            if (!this.getInventory().get(1).isEmpty())
            {
                count = this.getInventory().get(1).getCount();
                this.maxShieldDamage = 8 * count;
            }
            else
            {
                this.maxShieldDamage = 8;
            }

            // shield size upgrade
            if (!this.getInventory().get(2).isEmpty())
            {
                count = this.getInventory().get(2).getCount();
                this.maxShieldSizeUpgrade = 16 + count;
            }
            else
            {
                this.maxShieldSizeUpgrade = 16;
            }

            // shield capacity upgrade
            if (!this.getInventory().get(3).isEmpty())
            {
                count = this.getInventory().get(3).getCount();
                capacityUpgradeCount = this.getInventory().get(3).getCount();
                this.maxShieldCapacity = 32000 * count;
            }
            else
            {
                this.maxShieldCapacity = 16000;
            }

            int sum = (int) (this.shieldDamage + this.shieldSize + capacityUpgradeCount) / 2;
            this.storage.setMaxExtract(250 * sum);

            if (!this.needCharged && this.shieldCapacity == 0)
            {
                this.needCharged = true;
                this.shieldChargeCooldown = 1200;

                if (this.shieldChargeCooldown == 0)
                {
                    this.shieldCapacity = 100;
                }
            }
            if (!this.disabled && this.getEnergyStoredGC() > 0.0F && this.hasEnoughEnergyToRun)
            {
                if (this.shieldSize <= this.maxShieldSize)
                {
                    this.shieldSize += 0.1F;

                    if (this.shieldChargeCooldown > 0)
                    {
                        this.shieldChargeCooldown--;
                    }
                    if (this.shieldChargeCooldown == 0 && this.ticks % 2 == 0)
                    {
                        this.shieldCapacity += 100;
                        this.needCharged = false;
                    }
                }
            }
            else
            {
                this.shieldSize -= 0.1F;
            }

            if (this.shieldSize <= this.maxShieldSize)
            {
                this.shieldSize = Math.min(Math.max(this.shieldSize, 0.0F), this.maxShieldSize);
            }
            else
            {
                this.shieldSize -= 0.1F;
            }
            this.shieldCapacity = Math.min(Math.max(this.shieldCapacity, 0), this.maxShieldCapacity);
        }

        float range = this.shieldSize;

        for (Entity entity : this.world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(this.pos.getX() - range, this.pos.getY() - range, this.pos.getZ() - range, this.pos.getX() + range, this.pos.getY() + range, this.pos.getZ() + range)))
        {
            if (!this.disabled && this.enableShield && this.shieldCapacity > 0)
            {
                if (entity instanceof EntityArrow && !(((EntityArrow)entity).shootingEntity instanceof EntityPlayer) && !((EntityArrow)entity).inGround || entity instanceof EntityPotion && !(((EntityPotion)entity).getThrower() instanceof EntityPlayer) || entity instanceof EntityFireball || entity instanceof EntityShulkerBullet)
                {
                    if (this.world instanceof WorldServer)
                    {
                        ((WorldServer)this.world).spawnParticle(EnumParticleTypes.CRIT_MAGIC, entity.posX, entity.posY, entity.posZ, 20, 0.0D, 0.5D, 0.0D, 1.0D);
                    }
                    float motion = MathHelper.sqrt(entity.motionX * entity.motionX + entity.motionZ * entity.motionZ);
                    this.shieldCapacity -= motion * 2;
                    entity.setDead();
                }
            }
        }
    }

    @Override
    public boolean onActivated(EntityPlayer player)
    {
        return ((BlockShieldGenerator) MPBlocks.SHIELD_GENERATOR).onMachineActivated(this.world, this.mainBlockPosition, MPBlocks.SHIELD_GENERATOR.getDefaultState(), player, player.getActiveHand(), player.getHeldItemMainhand(), player.getHorizontalFacing(), 0.0F, 0.0F, 0.0F);
    }

    @Override
    public void onCreate(World world, BlockPos pos)
    {
        this.mainBlockPosition = pos;
        this.markDirty();
        List<BlockPos> positions = new LinkedList<>();
        this.getPositions(pos, positions);
        MPBlocks.SHIELD_GENERATOR_DUMMY.makeFakeBlock(world, positions, pos);
    }

    @Override
    public void onDestroy(TileEntity tile)
    {
        BlockPos thisBlock = this.getPos();
        List<BlockPos> positions = new ArrayList<>();
        this.getPositions(thisBlock, positions);

        for (BlockPos pos : positions)
        {
            if (this.world.isRemote && this.world.rand.nextDouble() < 0.1D)
            {
                FMLClientHandler.instance().getClient().effectRenderer.addBlockDestroyEffects(pos, this.world.getBlockState(pos));
            }
            this.world.destroyBlock(pos, false);
        }
        this.destroyBlock();
    }

    @Override
    public void getPositions(BlockPos placedPosition, List<BlockPos> positions)
    {
        int buildHeight = this.world.getHeight() - 1;

        for (int y = 1; y < 2; y++)
        {
            if (placedPosition.getY() + y > buildHeight)
            {
                return;
            }
            positions.add(new BlockPos(placedPosition.getX(), placedPosition.getY() + y, placedPosition.getZ()));
        }
    }

    @Override
    public void setDisabled(int index, boolean disabled)
    {
        if (this.disableCooldown == 0)
        {
            this.disabled = disabled;
            this.disableCooldown = 0;
        }
    }

    @Override
    public boolean getDisabled(int index)
    {
        return this.disabled;
    }

    @Override
    public double getPacketRange()
    {
        return 64.0D;
    }

    @Override
    public int getPacketCooldown()
    {
        return 1;
    }

    @Override
    public boolean isNetworkedTile()
    {
        return true;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt)
    {
        super.readFromNBT(nbt);
        this.setBubbleVisible(nbt.getBoolean("ShieldVisible"));
        this.shieldSize = nbt.getFloat("ShieldSize");
        this.maxShieldSize = nbt.getInteger("MaxShieldSize");
        this.shieldDamage = nbt.getInteger("ShieldDamage");
        this.maxShieldDamage = nbt.getInteger("MaxShieldDamage");
        this.shieldCapacity = nbt.getInteger("ShieldCapacity");
        this.maxShieldCapacity = nbt.getInteger("MaxShieldCapacity");
        this.shieldChargeCooldown = nbt.getInteger("ShieldChargeCooldown");
        this.needCharged = nbt.getBoolean("NeedCharged");
        this.enableShield = nbt.getBoolean("EnableShield");
        this.enableDamage = nbt.getBoolean("EnableDamage");
        this.ownerUUID = nbt.getString("OwnerUUID");
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt)
    {
        super.writeToNBT(nbt);
        nbt.setBoolean("ShieldVisible", this.shouldRender);
        nbt.setFloat("ShieldSize", this.shieldSize);
        nbt.setInteger("MaxShieldSize", this.maxShieldSize);
        nbt.setInteger("ShieldDamage", this.shieldDamage);
        nbt.setInteger("MaxShieldDamage", this.maxShieldDamage);
        nbt.setInteger("ShieldCapacity", this.shieldCapacity);
        nbt.setInteger("MaxShieldCapacity", this.maxShieldCapacity);
        nbt.setInteger("ShieldChargeCooldown", this.shieldChargeCooldown);
        nbt.setBoolean("NeedCharged", this.needCharged);
        nbt.setBoolean("EnableShield", this.enableShield);
        nbt.setBoolean("EnableDamage", this.enableDamage);
        nbt.setString("OwnerUUID", this.ownerUUID);
        return nbt;
    }

    @Override
    public boolean isUsableByPlayer(EntityPlayer player)
    {
        if (this.world.getTileEntity(this.pos) != this)
        {
            return false;
        }
        else
        {
            return player.getDistanceSq(this.pos.getX() + 0.5D, this.pos.getY() + 0.5D, this.pos.getZ() + 0.5D) <= 64.0D;
        }
    }

    @Override
    public int[] getSlotsForFace(EnumFacing side)
    {
        return new int[] { 0 };
    }

    @Override
    public boolean canExtractItem(int slotID, ItemStack itemStack, EnumFacing side)
    {
        return slotID == 0;
    }

    @Override
    public boolean isItemValidForSlot(int slot, ItemStack itemStack)
    {
        if (slot == 1)
        {
            return itemStack.getItem() == Items.REDSTONE;
        }
        else if (slot == 2)
        {
            return itemStack.getItem() == Items.ENDER_PEARL;
        }
        else if (slot == 3)
        {
            return itemStack.getItem() == Items.DIAMOND;
        }
        return slot == 0 && ItemElectricBase.isElectricItem(itemStack.getItem());
    }

    @Override
    public boolean shouldUseEnergy()
    {
        return !this.disabled;
    }

    @Override
    public EnumFacing getFront()
    {
        IBlockState state = this.world.getBlockState(this.getPos());

        if (state.getBlock() instanceof BlockShieldGenerator)
        {
            return state.getValue(BlockStateProperty.FACING_HORIZON);
        }
        return EnumFacing.NORTH;
    }

    @Override
    public EnumFacing getElectricInputDirection()
    {
        return this.getFront();
    }

    @Override
    public ItemStack getBatteryInSlot()
    {
        return this.getStackInSlot(0);
    }

    @Override
    public void setBubbleVisible(boolean render)
    {
        this.shouldRender = render;
    }

    @Override
    public float getBubbleSize()
    {
        return this.shieldSize;
    }

    @Override
    public boolean getBubbleVisible()
    {
        return this.shouldRender;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public AxisAlignedBB getRenderBoundingBox()
    {
        return TileEntity.INFINITE_EXTENT_AABB;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public double getMaxRenderDistanceSquared()
    {
        return Constants.RENDERDISTANCE_LONG;
    }

    @Override
    public EnumBlockMultiType getMultiType()
    {
        return null;
    }

    public String getStatus()
    {
        if (this.getEnergyStoredGC() == 0)
        {
            return TextFormatting.DARK_RED + LangUtils.translate("gui.status.missingpower.name");
        }
        if (this.getDisabled(0))
        {
            return TextFormatting.GOLD + LangUtils.translate("gui.status.ready.name");
        }
        if (this.getEnergyStoredGC() < this.storage.getMaxExtract())
        {
            return TextFormatting.GOLD + LangUtils.translate("gui.status.missingpower.name");
        }
        if (this.needCharged)
        {
            return TextFormatting.DARK_RED + LangUtils.translate("gui.status.shield_charging.name");
        }
        return TextFormatting.GREEN + LangUtils.translate("gui.status.active.name");
    }

    public boolean isInRangeOfShield(BlockPos pos)
    {
        double dx = this.pos.getX() - pos.getX();
        double dy = Math.abs(this.pos.getY() - pos.getY());
        double dz = this.pos.getZ() - pos.getZ();

        if (dx * dx + dz * dz <= this.shieldSize * this.shieldSize && dy <= this.shieldSize)
        {
            return true;
        }
        return false;
    }

    private boolean destroyBlock()
    {
        IBlockState state = this.world.getBlockState(this.pos);

        if (state.getMaterial() == Material.AIR)
        {
            return false;
        }
        else
        {
            this.world.playEvent(2001, this.pos, Block.getStateId(state));
            ItemStack machine = new ItemStack(MPBlocks.SHIELD_GENERATOR);
            TileEntityShieldGenerator shield = this;
            NBTTagCompound nbt = new NBTTagCompound();
            nbt.setFloat("ShieldSize", shield.shieldSize);
            nbt.setInteger("MaxShieldSize", shield.maxShieldSize);
            nbt.setInteger("ShieldDamage", shield.shieldDamage);
            nbt.setInteger("MaxShieldDamage", shield.maxShieldDamage);
            nbt.setInteger("ShieldCapacity", shield.shieldCapacity);
            nbt.setInteger("MaxShieldCapacity", shield.maxShieldCapacity);
            nbt.setInteger("ShieldChargeCooldown", shield.shieldChargeCooldown);
            nbt.setBoolean("NeedCharged", shield.needCharged);
            nbt.setBoolean("EnableShield", shield.enableShield);
            nbt.setBoolean("EnableDamage", shield.enableDamage);

            if (shield.getEnergyStoredGC() > 0)
            {
                nbt.setFloat("EnergyStored", shield.getEnergyStoredGC());
            }
            ItemStackHelper.saveAllItems(nbt, shield.inventory);
            machine.setTagCompound(nbt);
            Block.spawnAsEntity(this.world, this.pos, machine);
            return this.world.setBlockState(this.pos, Blocks.AIR.getDefaultState(), 3);
        }
    }
}