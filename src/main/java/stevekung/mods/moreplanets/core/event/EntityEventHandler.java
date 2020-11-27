package stevekung.mods.moreplanets.core.event;

import java.util.UUID;
import java.util.stream.Collectors;

import micdoodle8.mods.galacticraft.api.event.oxygen.GCCoreOxygenSuffocationEvent;
import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.util.ConfigManagerCore;
import micdoodle8.mods.galacticraft.core.util.OxygenUtil;
import micdoodle8.mods.galacticraft.core.util.WorldUtil;
import micdoodle8.mods.galacticraft.planets.venus.entities.EntityJuicer;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.living.EnderTeleportEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.event.entity.living.ZombieEvent.SummonAidEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.EntityInteract;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;
import stevekung.mods.moreplanets.core.capability.AbstractCapabilityDataMP;
import stevekung.mods.moreplanets.core.config.ConfigManagerMP;
import stevekung.mods.moreplanets.core.dimension.WorldProviderSpaceNether;
import stevekung.mods.moreplanets.init.MPItems;
import stevekung.mods.moreplanets.init.MPPotions;
import stevekung.mods.moreplanets.moons.koentus.entity.EntityKoentusMeteor;
import stevekung.mods.moreplanets.network.PacketSimpleMP;
import stevekung.mods.moreplanets.network.PacketSimpleMP.EnumSimplePacketMP;
import stevekung.mods.moreplanets.planets.diona.entity.EntityZeliusZombie;
import stevekung.mods.moreplanets.planets.nibiru.dimension.WorldProviderNibiru;
import stevekung.mods.moreplanets.planets.nibiru.entity.EntityInfectedZombie;
import stevekung.mods.moreplanets.planets.nibiru.entity.EntityShlime;
import stevekung.mods.moreplanets.planets.nibiru.world.gen.biome.BiomeGreenVeinFields;
import stevekung.mods.moreplanets.tileentity.TileEntityShieldGenerator;
import stevekung.mods.moreplanets.utils.CompatibilityManagerMP;
import stevekung.mods.moreplanets.utils.EntityEffectUtils;
import stevekung.mods.moreplanets.utils.LoggerMP;
import stevekung.mods.moreplanets.utils.TeleporterSpaceNether;
import stevekung.mods.moreplanets.world.IMeteorType;

public class EntityEventHandler
{
    private boolean openCelestialGui;

    @SubscribeEvent
    public void onZombieSummonAid(SummonAidEvent event)
    {
        if (event.getEntity() instanceof EntityInfectedZombie)
        {
            EntityInfectedZombie zombie = new EntityInfectedZombie(event.getWorld());
            event.setCustomSummonedAid(zombie);

            if (zombie.getRNG().nextFloat() < zombie.getEntityAttribute(zombie.getReinforcementsAttribute()).getAttributeValue())
            {
                event.setResult(Result.ALLOW);
            }
            event.setResult(Result.DENY);
        }
        if (event.getEntity() instanceof EntityZeliusZombie)
        {
            EntityZeliusZombie zombie = new EntityZeliusZombie(event.getWorld());
            event.setCustomSummonedAid(zombie);

            if (zombie.getRNG().nextFloat() < zombie.getEntityAttribute(zombie.getReinforcementsAttribute()).getAttributeValue())
            {
                event.setResult(Result.ALLOW);
            }
            event.setResult(Result.DENY);
        }
    }

    @SubscribeEvent
    public void onLivingFall(LivingFallEvent event)
    {
        if (!CompatibilityManagerMP.isBaubleLoaded)
        {
            EntityLivingBase living = event.getEntityLiving();

            if (living instanceof EntityPlayerMP)
            {
                EntityPlayerMP player = (EntityPlayerMP) living;

                if (player.inventory.hasItemStack(new ItemStack(MPItems.GRAVITY_AMULET)))
                {
                    event.setCanceled(true);
                }
                else
                {
                    event.setCanceled(false);
                }
            }
        }
    }

    @SubscribeEvent
    public void onDisconnect(FMLNetworkEvent.ClientDisconnectionFromServerEvent event)
    {
        this.openCelestialGui = false;
    }

    @SubscribeEvent
    public void onLivingUpdate(LivingEvent.LivingUpdateEvent event)
    {
        EntityLivingBase living = event.getEntityLiving();
        World world = living.world;

        if (living instanceof EntityPlayer)
        {
            EntityPlayer player = (EntityPlayer)living;

            if (!player.world.isRemote && player.world instanceof WorldServer)
            {
                this.runPortalTick(player);
            }
            this.runPortalTickClient(player);
        }
        else if (living instanceof EntityPlayerMP)
        {
            EntityPlayerMP player = (EntityPlayerMP)living;

            if (player.ticksExisted > 100 && ConfigManagerMP.moreplanets_general.enableSurvivalPlanetSelection && !WorldTickEventHandler.survivalPlanetData.hasSurvivalPlanetData && !this.openCelestialGui)
            {
                GalacticraftCore.packetPipeline.sendTo(new PacketSimpleMP(EnumSimplePacketMP.C_OPEN_SURVIVAL_PLANET_GUI, player.dimension), player);
                this.openCelestialGui = true;
            }

            if (player.isPotionActive(MPPotions.INFECTED_SPORE_PROTECTION) || this.isInOxygen(world, player))
            {
                player.removePotionEffect(MPPotions.INFECTED_SPORE);
            }
            if (player.isPotionActive(MPPotions.DARK_ENERGY_PROTECTION))
            {
                player.removePotionEffect(MPPotions.DARK_ENERGY);
            }
            if (world.provider instanceof WorldProviderNibiru)
            {
                if (!this.isGodPlayer(player) && !player.isPotionActive(MPPotions.INFECTED_SPORE_PROTECTION) && !(world.getBiome(player.getPosition()) instanceof BiomeGreenVeinFields))
                {
                    if (world.isRainingAt(player.getPosition()))
                    {
                        player.addPotionEffect(new PotionEffect(MPPotions.INFECTED_SPORE, 40));
                    }
                    if (player.ticksExisted % 128 == 0 && !this.isInOxygen(world, player))
                    {
                        player.addPotionEffect(new PotionEffect(MPPotions.INFECTED_SPORE, 80));
                    }
                }
            }
            if (!world.isRemote && world.provider instanceof IMeteorType)
            {
                this.spawnMeteors(world, player, (IMeteorType)world.provider);
            }
        }
        else
        {
            if (ConfigManagerMP.moreplanets_planet_settings.enableInfectedSporeForMobs && world.provider instanceof WorldProviderNibiru)
            {
                if (!EntityEffectUtils.isGalacticraftMob(living) && !(living instanceof EntityJuicer) && !(world.getBiome(living.getPosition()) instanceof BiomeGreenVeinFields))
                {
                    if (living.ticksExisted % 128 == 0)
                    {
                        living.addPotionEffect(new PotionEffect(MPPotions.INFECTED_SPORE, 80));
                    }
                    else if (world.isRainingAt(living.getPosition()))
                    {
                        living.addPotionEffect(new PotionEffect(MPPotions.INFECTED_SPORE, 40));
                    }
                }
            }
        }

        for (TileEntity tile : world.tickableTileEntities.stream().filter(tile -> tile instanceof TileEntityShieldGenerator).collect(Collectors.toList()))
        {
            if (!tile.getWorld().isRemote)
            {
                TileEntityShieldGenerator shield = (TileEntityShieldGenerator)tile;

                if (living instanceof IMob)
                {
                    if (!shield.disabled && shield.enableShield && shield.shieldCapacity > 0 && shield.isInRangeOfShield(living.getPosition()))
                    {
                        if (!shield.enableDamage)
                        {
                            double d4 = living.getDistance(shield.getPos().getX(), shield.getPos().getY(), shield.getPos().getZ());
                            double d6 = living.posX - shield.getPos().getX();
                            double d8 = living.posY - shield.getPos().getY();
                            double d10 = living.posZ - shield.getPos().getZ();
                            double d11 = MathHelper.sqrt(d6 * d6 + d8 * d8 + d10 * d10);
                            d6 /= d11;
                            d8 /= d11;
                            d10 /= d11;
                            double d13 = (0.0D - d4) * 2.0D / 10.0D;
                            double d14 = d13;
                            double knockSpeed = 10.0D;
                            living.motionX -= d6 * d14 / knockSpeed;
                            living.motionY -= d8 * d14 / knockSpeed;
                            living.motionZ -= d10 * d14 / knockSpeed;
                        }

                        UUID uuid;

                        try
                        {
                            uuid = UUID.fromString(shield.ownerUUID);
                        }
                        catch (Exception e)
                        {
                            uuid = UUID.fromString("eef3a603-1c1b-4c98-8264-d2f04b231ef4"); //default uuid :)
                        }

                        if (uuid != null && shield.getWorld().getPlayerEntityByUUID(uuid) != null)
                        {
                            if (living.ticksExisted % 8 == 0 && shield.getWorld() instanceof WorldServer)
                            {
                                ((WorldServer)shield.getWorld()).spawnParticle(EnumParticleTypes.CRIT_MAGIC, living.posX, living.posY, living.posZ, 20, 0.0D, 0.5D, 0.0D, 1.0D);
                            }
                            if (shield.enableDamage)
                            {
                                living.attackEntityFrom(DamageSource.causePlayerDamage(shield.getWorld().getPlayerEntityByUUID(uuid)), shield.shieldDamage);
                            }
                        }
                        else
                        {
                            if (living.ticksExisted % 8 == 0 && shield.getWorld() instanceof WorldServer)
                            {
                                ((WorldServer)shield.getWorld()).spawnParticle(EnumParticleTypes.CRIT_MAGIC, living.posX, living.posY, living.posZ, 20, 0.0D, 0.5D, 0.0D, 1.0D);
                            }
                            if (shield.enableDamage)
                            {
                                living.attackEntityFrom(DamageSource.GENERIC, shield.shieldDamage);
                            }
                        }
                        float motion = MathHelper.sqrt(living.motionX * living.motionX + living.motionZ * living.motionZ);
                        shield.shieldCapacity -= motion * 2;
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void onOxygenSuffocation(GCCoreOxygenSuffocationEvent.Pre event)
    {
        EntityLivingBase living = event.getEntityLiving();

        if (living.world.provider instanceof WorldProviderSpaceNether)
        {
            if (EntityEffectUtils.isSpaceNetherMob(living))
            {
                event.setCanceled(true);
            }
        }
        if (living.world.getBiome(living.getPosition()) instanceof BiomeGreenVeinFields)
        {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onInteractEntity(EntityInteract event)
    {
        ItemStack itemStack = event.getItemStack();
        Entity entity = event.getTarget();

        if (!itemStack.isEmpty() && itemStack.getItem() == Items.DYE)
        {
            EnumDyeColor color = EnumDyeColor.byDyeDamage(itemStack.getItemDamage() & 15);

            if (entity instanceof EntityShlime)
            {
                EntityShlime shlime = (EntityShlime)entity;

                if (!shlime.getSheared() && shlime.getFleeceColor() != color)
                {
                    shlime.setFleeceColor(color);

                    if (!event.getEntityPlayer().capabilities.isCreativeMode)
                    {
                        itemStack.shrink(1);
                    }
                }
                event.setResult(Result.ALLOW);
            }
        }
    }

    @SubscribeEvent
    public void onEntityUpdate(EntityEvent.CanUpdate event)
    {
        if (event.getEntity() instanceof EntityKoentusMeteor)
        {
            event.setCanUpdate(true);
            return;
        }
    }

    @SubscribeEvent
    public void onLivingSpawn(LivingSpawnEvent.CheckSpawn event)
    {
        if (event.getResult() == Result.ALLOW || event.isSpawner())
        {
            return;
        }

        for (TileEntity tile : event.getWorld().tickableTileEntities.stream().filter(tile -> tile instanceof TileEntityShieldGenerator).collect(Collectors.toList()))
        {
            if (!tile.getWorld().isRemote)
            {
                TileEntityShieldGenerator shield = (TileEntityShieldGenerator)tile;

                if (!shield.disabled && shield.isInRangeOfShield(event.getEntity().getPosition()))
                {
                    event.setResult(Result.DENY);
                }
            }
        }
    }

    @SubscribeEvent
    public void onEnderTeleport(EnderTeleportEvent event)
    {
        for (TileEntity tile : event.getEntityLiving().world.tickableTileEntities.stream().filter(tile -> tile instanceof TileEntityShieldGenerator).collect(Collectors.toList()))
        {
            if (!tile.getWorld().isRemote)
            {
                TileEntityShieldGenerator shield = (TileEntityShieldGenerator)tile;

                if (!shield.disabled && shield.isInRangeOfShield(event.getEntityLiving().getPosition()))
                {
                    event.setCanceled(true);
                }
            }
        }
    }

    private void spawnMeteors(World world, EntityPlayerMP player, IMeteorType meteor)
    {
        if (meteor.getMeteorSpawnFrequency() > 0.0D && ConfigManagerCore.meteorSpawnMod > 0.0D && meteor.getMeteorType() != null)
        {
            Entity meteorEntity = null;
            int frequency = (int)(meteor.getMeteorSpawnFrequency() * 750.0D * (1.0D / ConfigManagerCore.meteorSpawnMod));
            int serverDistance = world.getMinecraftServer().getPlayerList().getViewDistance();
            int x, z;
            double motX, motZ;
            x = world.rand.nextInt(20) + 160;
            z = world.rand.nextInt(20) - 10;
            motX = world.rand.nextDouble() * 2 - 2.5D;
            motZ = world.rand.nextDouble() * 5 - 2.5D;
            int px = MathHelper.floor(player.posX);
            EntityPlayer closestPlayer = world.getClosestPlayerToEntity(player, 100.0D);

            if (closestPlayer == null || closestPlayer.getEntityId() <= player.getEntityId())
            {
                if ((x + px >> 4) - (px >> 4) >= serverDistance)
                {
                    x = ((px >> 4) + serverDistance << 4) - 1 - px;
                }

                if (world.rand.nextInt(frequency) == 0)
                {
                    int size = 1 + world.rand.nextInt(2);

                    switch (meteor.getMeteorType())
                    {
                    case KOENTUS:
                        meteorEntity = new EntityKoentusMeteor(world, player.posX + x, 355.0D, player.posZ + z, motX - 2.5D, 0, motZ - 2.5D, size);
                        break;
                    }
                    world.spawnEntity(meteorEntity);
                    LoggerMP.debug("Spawn meteor {} at {} {} {}", EntityList.getKey(meteorEntity), (int)meteorEntity.posX, (int)meteorEntity.posY, (int)meteorEntity.posZ);
                }
                if (world.rand.nextInt(frequency * 3) == 0)
                {
                    int size = 3 + world.rand.nextInt(6);

                    switch (meteor.getMeteorType())
                    {
                    case KOENTUS:
                        meteorEntity = new EntityKoentusMeteor(world, player.posX + x, 355.0D, player.posZ + z, motX - 2.5D, 0, motZ - 2.5D, size);
                        break;
                    }
                    world.spawnEntity(meteorEntity);
                    LoggerMP.debug("Spawn meteor {} at {} {} {}", EntityList.getKey(meteorEntity), (int)meteorEntity.posX, (int)meteorEntity.posY, (int)meteorEntity.posZ);
                }
            }
        }
    }

    private boolean isInOxygen(World world, EntityPlayer player)
    {
        return OxygenUtil.inOxygenBubble(world, player.posX, player.posY, player.posZ) || OxygenUtil.isAABBInBreathableAirBlock(player);
    }

    private boolean isGodPlayer(EntityPlayer player)
    {
        return player.capabilities.isCreativeMode || player.isSpectator();
    }

    private void runPortalTick(EntityPlayer player)
    {
        AbstractCapabilityDataMP data = AbstractCapabilityDataMP.get(player);

        if (data.isInPortal())
        {
            int maxTime = 80;
            data.setPortalCounter(data.getPortalCounter() + 1);

            if (data.getPortalCounter() >= maxTime)
            {
                data.setPortalCounter(maxTime);
                data.setTimeUntilPortal(player.getPortalCooldown());

                if (player instanceof EntityPlayerMP)
                {
                    EntityPlayerMP playerMP = (EntityPlayerMP)player;

                    if (WorldTickEventHandler.survivalPlanetData != null && WorldTickEventHandler.survivalPlanetData.hasSurvivalPlanetData)
                    {
                        int netherId = ConfigManagerMP.moreplanets_dimension.idDimensionSpaceNether;
                        String survivalPlanet = WorldTickEventHandler.survivalPlanetData.survivalPlanetName;

                        if (playerMP.dimension != netherId)
                        {
                            playerMP.mcServer.getPlayerList().transferPlayerToDimension(playerMP, netherId, new TeleporterSpaceNether(playerMP.mcServer.getWorld(netherId), playerMP.getPosition(), playerMP.world.provider));
                            GalacticraftCore.packetPipeline.sendTo(new PacketSimpleMP(EnumSimplePacketMP.C_RELOAD_RENDERER, playerMP.dimension), playerMP);
                        }
                        else
                        {
                            int dimID = WorldUtil.getProviderForNameServer(survivalPlanet).getDimension();
                            playerMP.mcServer.getPlayerList().transferPlayerToDimension(playerMP, dimID, new TeleporterSpaceNether(playerMP.mcServer.getWorld(dimID), playerMP.getPosition(), playerMP.world.provider));
                            GalacticraftCore.packetPipeline.sendTo(new PacketSimpleMP(EnumSimplePacketMP.C_RELOAD_RENDERER, playerMP.dimension), playerMP);
                        }
                    }
                }
            }
            data.setInPortal(false);
        }
        else
        {
            if (data.getPortalCounter() > 0)
            {
                data.setPortalCounter(data.getPortalCounter() - 4);
            }
            if (data.getPortalCounter() < 0)
            {
                data.setPortalCounter(0);
            }
        }
    }

    private void runPortalTickClient(EntityPlayer player)
    {
        AbstractCapabilityDataMP data = AbstractCapabilityDataMP.get(player);
        data.setPrevTimeInPortal(data.getTimeInPortal());

        if (data.isInPortal())
        {
            if (FMLClientHandler.instance().getClient().currentScreen != null && !FMLClientHandler.instance().getClient().currentScreen.doesGuiPauseGame())
            {
                if (FMLClientHandler.instance().getClient().currentScreen instanceof GuiContainer)
                {
                    player.closeScreen();
                }
                FMLClientHandler.instance().getClient().displayGuiScreen(null);
            }

            if (data.getTimeInPortal() == 0.0F)
            {
                player.playSound(SoundEvents.BLOCK_PORTAL_TRIGGER, 0.25F, player.world.rand.nextFloat() * 0.4F + 0.8F);
            }

            data.setTimeInPortal(data.getTimeInPortal() + 0.0125F);

            if (data.getTimeInPortal() >= 1.0F)
            {
                data.setTimeInPortal(1.0F);
            }
            data.setInPortal(false);
        }
        else
        {
            if (data.getTimeInPortal() > 0.0F)
            {
                data.setTimeInPortal(data.getTimeInPortal() - 0.05F);
            }
            if (data.getTimeInPortal() < 0.0F)
            {
                data.setTimeInPortal(0.0F);
            }
        }

        if (data.getTimeUntilPortal() > 0)
        {
            data.setTimeUntilPortal(data.getTimeUntilPortal() - 1);
        }
    }
}