package stevekung.mods.moreplanets.planets.nibiru.world.gen.structure;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import micdoodle8.mods.galacticraft.core.GCBlocks;
import micdoodle8.mods.galacticraft.core.blocks.BlockUnlitTorch;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeProvider;
import net.minecraft.world.gen.structure.MapGenStructureIO;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.template.TemplateManager;
import stevekung.mods.moreplanets.init.MPBiomes;
import stevekung.mods.moreplanets.init.MPBlocks;
import stevekung.mods.moreplanets.init.MPLootTables;
import stevekung.mods.moreplanets.planets.nibiru.entity.EntityNibiruVillager;
import stevekung.mods.moreplanets.utils.blocks.BlockChestMP;
import stevekung.mods.moreplanets.utils.blocks.BlockCropsMP;
import stevekung.mods.moreplanets.utils.tileentity.TileEntityChestMP;

public class StructureNibiruVillagePieces
{
    private static final Stones villageStones = new Stones();
    private static final Planks villagePlanks = new Planks();
    private static final Logs villageLogs = new Logs();
    private static final Dirts villageDirts = new Dirts();

    public static void registerVillagePieces()
    {
        MapGenStructureIO.registerStructureComponent(House1.class, "NibiruViBH");
        MapGenStructureIO.registerStructureComponent(Field1.class, "NibiruViDF");
        MapGenStructureIO.registerStructureComponent(Field2.class, "NibiruViF");
        MapGenStructureIO.registerStructureComponent(Torch.class, "NibiruViL");
        MapGenStructureIO.registerStructureComponent(Hall.class, "NibiruViPH");
        MapGenStructureIO.registerStructureComponent(House4Garden.class, "NibiruViSH");
        MapGenStructureIO.registerStructureComponent(WoodHut.class, "NibiruViSmH");
        MapGenStructureIO.registerStructureComponent(Church.class, "NibiruViST");
        MapGenStructureIO.registerStructureComponent(House2.class, "NibiruViS");
        MapGenStructureIO.registerStructureComponent(Start.class, "NibiruViStart");
        MapGenStructureIO.registerStructureComponent(Path.class, "NibiruViSR");
        MapGenStructureIO.registerStructureComponent(House3.class, "NibiruViTRH");
        MapGenStructureIO.registerStructureComponent(Well.class, "NibiruViW");
    }

    public static List<PieceWeight> getStructureVillageWeightedPieceList(Random rand, int size)
    {
        List<PieceWeight> list = new ArrayList<>();
        list.add(new PieceWeight(House4Garden.class, 4, MathHelper.getInt(rand, 2 + size, 4 + size * 2)));
        list.add(new PieceWeight(Church.class, 20, MathHelper.getInt(rand, 0 + size, 1 + size)));
        list.add(new PieceWeight(House1.class, 20, MathHelper.getInt(rand, 0 + size, 2 + size)));
        list.add(new PieceWeight(WoodHut.class, 3, MathHelper.getInt(rand, 2 + size, 5 + size * 3)));
        list.add(new PieceWeight(Hall.class, 15, MathHelper.getInt(rand, 0 + size, 2 + size)));
        list.add(new PieceWeight(Field1.class, 3, MathHelper.getInt(rand, 1 + size, 4 + size)));
        list.add(new PieceWeight(Field2.class, 3, MathHelper.getInt(rand, 2 + size, 4 + size * 2)));
        list.add(new PieceWeight(House2.class, 15, MathHelper.getInt(rand, 0, 1 + size)));
        list.add(new PieceWeight(House3.class, 8, MathHelper.getInt(rand, 0 + size, 3 + size * 2)));
        Iterator<PieceWeight> iterator = list.iterator();

        while (iterator.hasNext())
        {
            if (iterator.next().villagePiecesLimit == 0)
            {
                iterator.remove();
            }
        }
        return list;
    }

    public static class Church extends Village
    {
        public Church() {}

        public Church(Start start, int type, StructureBoundingBox box, EnumFacing facing)
        {
            super(start, type);
            this.setCoordBaseMode(facing);
            this.boundingBox = box;
        }

        @Override
        public boolean addComponentParts(World world, Random rand, StructureBoundingBox box)
        {
            if (this.averageGroundLvl < 0)
            {
                this.averageGroundLvl = this.getAverageGroundLevel(world, box);

                if (this.averageGroundLvl < 0)
                {
                    return true;
                }
                this.boundingBox.offset(0, this.averageGroundLvl - this.boundingBox.maxY + 12 - 1, 0);
            }

            IBlockState iblockstate = MPBlocks.NIBIRU_COBBLESTONE.getDefaultState();
            IBlockState iblockstate1 = this.getBiomeSpecificBlockState(MPBlocks.NIBIRU_COBBLESTONE_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, EnumFacing.NORTH));
            IBlockState iblockstate2 = this.getBiomeSpecificBlockState(MPBlocks.NIBIRU_COBBLESTONE_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, EnumFacing.WEST));
            IBlockState iblockstate3 = this.getBiomeSpecificBlockState(MPBlocks.NIBIRU_COBBLESTONE_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, EnumFacing.EAST));
            this.fillWithBlocks(world, box, 1, 1, 1, 3, 3, 7, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
            this.fillWithBlocks(world, box, 1, 5, 1, 3, 9, 3, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
            this.fillWithRandomizedBlocks(world, box, 1, 0, 0, 3, 0, 8, false, rand, villageStones);
            this.fillWithRandomizedBlocks(world, box, 1, 1, 0, 3, 10, 0, false, rand, villageStones);
            this.fillWithRandomizedBlocks(world, box, 0, 1, 1, 0, 10, 3, false, rand, villageStones);
            this.fillWithRandomizedBlocks(world, box, 4, 1, 1, 4, 10, 3, false, rand, villageStones);
            this.fillWithRandomizedBlocks(world, box, 0, 0, 4, 0, 4, 7, false, rand, villageStones);
            this.fillWithRandomizedBlocks(world, box, 4, 0, 4, 4, 4, 7, false, rand, villageStones);
            this.fillWithRandomizedBlocks(world, box, 1, 1, 8, 3, 4, 8, false, rand, villageStones);
            this.fillWithRandomizedBlocks(world, box, 1, 5, 4, 3, 10, 4, false, rand, villageStones);
            this.fillWithRandomizedBlocks(world, box, 1, 5, 5, 3, 5, 7, false, rand, villageStones);
            this.fillWithRandomizedBlocks(world, box, 0, 9, 0, 4, 9, 4, false, rand, villageStones);
            this.fillWithRandomizedBlocks(world, box, 0, 4, 0, 4, 4, 4, false, rand, villageStones);
            this.setBlockState(world, iblockstate, 0, 11, 2, box);
            this.setBlockState(world, iblockstate, 4, 11, 2, box);
            this.setBlockState(world, iblockstate, 2, 11, 0, box);
            this.setBlockState(world, iblockstate, 2, 11, 4, box);
            this.setBlockState(world, iblockstate, 1, 1, 6, box);
            this.setBlockState(world, iblockstate, 1, 1, 7, box);
            this.setBlockState(world, iblockstate, 2, 1, 7, box);
            this.setBlockState(world, iblockstate, 3, 1, 6, box);
            this.setBlockState(world, iblockstate, 3, 1, 7, box);
            this.setBlockState(world, iblockstate1, 1, 1, 5, box);
            this.setBlockState(world, iblockstate1, 2, 1, 6, box);
            this.setBlockState(world, iblockstate1, 3, 1, 5, box);
            this.setBlockState(world, iblockstate2, 1, 2, 7, box);
            this.setBlockState(world, iblockstate3, 3, 2, 7, box);
            this.setBlockState(world, rand.nextInt(5) == 0 ? Blocks.AIR.getDefaultState() : Blocks.GLASS_PANE.getDefaultState(), 0, 2, 2, box);
            this.setBlockState(world, rand.nextInt(5) == 0 ? Blocks.AIR.getDefaultState() : Blocks.GLASS_PANE.getDefaultState(), 0, 3, 2, box);
            this.setBlockState(world, rand.nextInt(5) == 0 ? Blocks.AIR.getDefaultState() : Blocks.GLASS_PANE.getDefaultState(), 4, 2, 2, box);
            this.setBlockState(world, rand.nextInt(5) == 0 ? Blocks.AIR.getDefaultState() : Blocks.GLASS_PANE.getDefaultState(), 4, 3, 2, box);
            this.setBlockState(world, rand.nextInt(5) == 0 ? Blocks.AIR.getDefaultState() : Blocks.GLASS_PANE.getDefaultState(), 0, 6, 2, box);
            this.setBlockState(world, rand.nextInt(5) == 0 ? Blocks.AIR.getDefaultState() : Blocks.GLASS_PANE.getDefaultState(), 0, 7, 2, box);
            this.setBlockState(world, rand.nextInt(5) == 0 ? Blocks.AIR.getDefaultState() : Blocks.GLASS_PANE.getDefaultState(), 4, 6, 2, box);
            this.setBlockState(world, rand.nextInt(5) == 0 ? Blocks.AIR.getDefaultState() : Blocks.GLASS_PANE.getDefaultState(), 4, 7, 2, box);
            this.setBlockState(world, rand.nextInt(5) == 0 ? Blocks.AIR.getDefaultState() : Blocks.GLASS_PANE.getDefaultState(), 2, 6, 0, box);
            this.setBlockState(world, rand.nextInt(5) == 0 ? Blocks.AIR.getDefaultState() : Blocks.GLASS_PANE.getDefaultState(), 2, 7, 0, box);
            this.setBlockState(world, rand.nextInt(5) == 0 ? Blocks.AIR.getDefaultState() : Blocks.GLASS_PANE.getDefaultState(), 2, 6, 4, box);
            this.setBlockState(world, rand.nextInt(5) == 0 ? Blocks.AIR.getDefaultState() : Blocks.GLASS_PANE.getDefaultState(), 2, 7, 4, box);
            this.setBlockState(world, rand.nextInt(5) == 0 ? Blocks.AIR.getDefaultState() : Blocks.GLASS_PANE.getDefaultState(), 0, 3, 6, box);
            this.setBlockState(world, rand.nextInt(5) == 0 ? Blocks.AIR.getDefaultState() : Blocks.GLASS_PANE.getDefaultState(), 4, 3, 6, box);
            this.setBlockState(world, rand.nextInt(5) == 0 ? Blocks.AIR.getDefaultState() : Blocks.GLASS_PANE.getDefaultState(), 2, 3, 8, box);
            this.placeTorch(world, EnumFacing.SOUTH, 2, 4, 7, box);
            this.placeTorch(world, EnumFacing.EAST, 1, 4, 6, box);
            this.placeTorch(world, EnumFacing.WEST, 3, 4, 6, box);
            this.placeTorch(world, EnumFacing.NORTH, 2, 4, 5, box);
            IBlockState iblockstate4 = Blocks.LADDER.getDefaultState().withProperty(BlockLadder.FACING, EnumFacing.WEST);

            for (int i = 1; i <= 9; ++i)
            {
                this.setBlockState(world, iblockstate4, 3, i, 3, box);
            }

            this.setBlockState(world, Blocks.AIR.getDefaultState(), 2, 1, 0, box);
            this.setBlockState(world, Blocks.AIR.getDefaultState(), 2, 2, 0, box);
            this.createVillageDoor(world, box, rand, 2, 1, 0);

            if (this.getBlockStateFromPos(world, 2, 0, -1, box).getMaterial() == Material.AIR && this.getBlockStateFromPos(world, 2, -1, -1, box).getMaterial() != Material.AIR)
            {
                this.setBlockState(world, iblockstate1, 2, 0, -1, box);

                if (this.getBlockStateFromPos(world, 2, -1, -1, box).getBlock() == MPBlocks.INFECTED_GRASS_PATH)
                {
                    this.setBlockState(world, MPBlocks.INFECTED_GRASS_BLOCK.getDefaultState(), 2, -1, -1, box);
                }
            }
            for (int k = 0; k < 9; ++k)
            {
                for (int j = 0; j < 5; ++j)
                {
                    this.clearCurrentPositionBlocksUpwards(world, j, 12, k, box);
                    this.replaceAirAndLiquidDownwards(world, iblockstate, j, -1, k, box);
                }
            }
            this.spawnVillagers(world, box, 2, 1, 2, 1);
            return true;
        }

        @Override
        protected int chooseProfession(int villagersSpawnedIn, int currentVillagerProfession)
        {
            return this.structureType == 2 ? 5 : 2;
        }
    }

    public static class Field1 extends Village
    {
        private Block cropTypeA;
        private Block cropTypeB;
        private Block cropTypeC;
        private Block cropTypeD;

        public Field1() {}

        public Field1(Start start, int type, Random rand, StructureBoundingBox box, EnumFacing facing)
        {
            super(start, type);
            this.setCoordBaseMode(facing);
            this.boundingBox = box;
            this.cropTypeA = this.getRandomCropType(rand);
            this.cropTypeB = this.getRandomCropType(rand);
            this.cropTypeC = this.getRandomCropType(rand);
            this.cropTypeD = this.getRandomCropType(rand);
        }

        @Override
        protected void writeStructureToNBT(NBTTagCompound nbt)
        {
            super.writeStructureToNBT(nbt);
            nbt.setInteger("CA", Block.REGISTRY.getIDForObject(this.cropTypeA));
            nbt.setInteger("CB", Block.REGISTRY.getIDForObject(this.cropTypeB));
            nbt.setInteger("CC", Block.REGISTRY.getIDForObject(this.cropTypeC));
            nbt.setInteger("CD", Block.REGISTRY.getIDForObject(this.cropTypeD));
        }

        @Override
        protected void readStructureFromNBT(NBTTagCompound nbt, TemplateManager manager)
        {
            super.readStructureFromNBT(nbt, manager);
            this.cropTypeA = Block.getBlockById(nbt.getInteger("CA"));
            this.cropTypeB = Block.getBlockById(nbt.getInteger("CB"));
            this.cropTypeC = Block.getBlockById(nbt.getInteger("CC"));
            this.cropTypeD = Block.getBlockById(nbt.getInteger("CD"));
        }

        private Block getRandomCropType(Random rand)
        {
            return this.structureType == 2 ? MPBlocks.TERRABERRY : MPBlocks.INFECTED_WHEAT;
        }

        @SuppressWarnings("deprecation")
        @Override
        public boolean addComponentParts(World world, Random rand, StructureBoundingBox box)
        {
            if (this.averageGroundLvl < 0)
            {
                this.averageGroundLvl = this.getAverageGroundLevel(world, box);

                if (this.averageGroundLvl < 0)
                {
                    return true;
                }
                this.boundingBox.offset(0, this.averageGroundLvl - this.boundingBox.maxY + 4 - 1, 0);
            }

            this.fillWithBlocks(world, box, 0, 1, 0, 12, 4, 8, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
            this.fillWithBlocks(world, box, 1, 0, 1, 2, 0, 7, MPBlocks.INFECTED_FARMLAND.getDefaultState(), MPBlocks.INFECTED_FARMLAND.getDefaultState(), false);
            this.fillWithBlocks(world, box, 4, 0, 1, 5, 0, 7, MPBlocks.INFECTED_FARMLAND.getDefaultState(), MPBlocks.INFECTED_FARMLAND.getDefaultState(), false);
            this.fillWithBlocks(world, box, 7, 0, 1, 8, 0, 7, MPBlocks.INFECTED_FARMLAND.getDefaultState(), MPBlocks.INFECTED_FARMLAND.getDefaultState(), false);
            this.fillWithBlocks(world, box, 10, 0, 1, 11, 0, 7, MPBlocks.INFECTED_FARMLAND.getDefaultState(), MPBlocks.INFECTED_FARMLAND.getDefaultState(), false);
            this.fillWithRandomizedBlocks(world, box, 0, 0, 0, 0, 0, 8, false, rand, villageLogs);
            this.fillWithRandomizedBlocks(world, box, 6, 0, 0, 6, 0, 8, false, rand, villageLogs);
            this.fillWithRandomizedBlocks(world, box, 12, 0, 0, 12, 0, 8, false, rand, villageLogs);
            this.fillWithRandomizedBlocks(world, box, 1, 0, 0, 11, 0, 0, false, rand, villageLogs);
            this.fillWithRandomizedBlocks(world, box, 1, 0, 8, 11, 0, 8, false, rand, villageLogs);
            this.fillWithBlocks(world, box, 3, 0, 1, 3, 0, 7, MPBlocks.INFECTED_WATER_FLUID_BLOCK.getDefaultState(), MPBlocks.INFECTED_WATER_FLUID_BLOCK.getDefaultState(), false);
            this.fillWithBlocks(world, box, 9, 0, 1, 9, 0, 7, MPBlocks.INFECTED_WATER_FLUID_BLOCK.getDefaultState(), MPBlocks.INFECTED_WATER_FLUID_BLOCK.getDefaultState(), false);

            for (int i = 1; i <= 7; ++i)
            {
                int j = ((BlockCropsMP)this.cropTypeA).getMaxAge();
                int k = j / 3;
                this.setBlockState(world, this.cropTypeA.getStateFromMeta(MathHelper.getInt(rand, k, j)), 1, 1, i, box);
                this.setBlockState(world, this.cropTypeA.getStateFromMeta(MathHelper.getInt(rand, k, j)), 2, 1, i, box);
                int l = ((BlockCropsMP)this.cropTypeB).getMaxAge();
                int i1 = l / 3;
                this.setBlockState(world, this.cropTypeB.getStateFromMeta(MathHelper.getInt(rand, i1, l)), 4, 1, i, box);
                this.setBlockState(world, this.cropTypeB.getStateFromMeta(MathHelper.getInt(rand, i1, l)), 5, 1, i, box);
                int j1 = ((BlockCropsMP)this.cropTypeC).getMaxAge();
                int k1 = j1 / 3;
                this.setBlockState(world, this.cropTypeC.getStateFromMeta(MathHelper.getInt(rand, k1, j1)), 7, 1, i, box);
                this.setBlockState(world, this.cropTypeC.getStateFromMeta(MathHelper.getInt(rand, k1, j1)), 8, 1, i, box);
                int l1 = ((BlockCropsMP)this.cropTypeD).getMaxAge();
                int i2 = l1 / 3;
                this.setBlockState(world, this.cropTypeD.getStateFromMeta(MathHelper.getInt(rand, i2, l1)), 10, 1, i, box);
                this.setBlockState(world, this.cropTypeD.getStateFromMeta(MathHelper.getInt(rand, i2, l1)), 11, 1, i, box);
            }
            for (int j2 = 0; j2 < 9; ++j2)
            {
                for (int k2 = 0; k2 < 13; ++k2)
                {
                    this.clearCurrentPositionBlocksUpwards(world, k2, 4, j2, box);
                    this.replaceAirAndLiquidDownwards(world, MPBlocks.INFECTED_DIRT.getDefaultState(), k2, -1, j2, box);
                }
            }
            return true;
        }
    }

    public static class Field2 extends Village
    {
        private Block cropTypeA;
        private Block cropTypeB;

        public Field2() {}

        public Field2(Start start, int type, Random rand, StructureBoundingBox box, EnumFacing facing)
        {
            super(start, type);
            this.setCoordBaseMode(facing);
            this.boundingBox = box;
            this.cropTypeA = this.getRandomCropType(rand);
            this.cropTypeB = this.getRandomCropType(rand);
        }

        @Override
        protected void writeStructureToNBT(NBTTagCompound nbt)
        {
            super.writeStructureToNBT(nbt);
            nbt.setInteger("CA", Block.REGISTRY.getIDForObject(this.cropTypeA));
            nbt.setInteger("CB", Block.REGISTRY.getIDForObject(this.cropTypeB));
        }

        @Override
        protected void readStructureFromNBT(NBTTagCompound nbt, TemplateManager manager)
        {
            super.readStructureFromNBT(nbt, manager);
            this.cropTypeA = Block.getBlockById(nbt.getInteger("CA"));
            this.cropTypeB = Block.getBlockById(nbt.getInteger("CB"));
        }

        private Block getRandomCropType(Random rand)
        {
            return this.structureType == 2 ? MPBlocks.TERRABERRY : MPBlocks.INFECTED_WHEAT;
        }

        @SuppressWarnings("deprecation")
        @Override
        public boolean addComponentParts(World world, Random rand, StructureBoundingBox box)
        {
            if (this.averageGroundLvl < 0)
            {
                this.averageGroundLvl = this.getAverageGroundLevel(world, box);

                if (this.averageGroundLvl < 0)
                {
                    return true;
                }
                this.boundingBox.offset(0, this.averageGroundLvl - this.boundingBox.maxY + 4 - 1, 0);
            }

            this.fillWithBlocks(world, box, 0, 1, 0, 6, 4, 8, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
            this.fillWithBlocks(world, box, 1, 0, 1, 2, 0, 7, MPBlocks.INFECTED_FARMLAND.getDefaultState(), MPBlocks.INFECTED_FARMLAND.getDefaultState(), false);
            this.fillWithBlocks(world, box, 4, 0, 1, 5, 0, 7, MPBlocks.INFECTED_FARMLAND.getDefaultState(), MPBlocks.INFECTED_FARMLAND.getDefaultState(), false);
            this.fillWithRandomizedBlocks(world, box, 0, 0, 0, 0, 0, 8, false, rand, villageLogs);
            this.fillWithRandomizedBlocks(world, box, 6, 0, 0, 6, 0, 8, false, rand, villageLogs);
            this.fillWithRandomizedBlocks(world, box, 1, 0, 0, 5, 0, 0, false, rand, villageLogs);
            this.fillWithRandomizedBlocks(world, box, 1, 0, 8, 5, 0, 8, false, rand, villageLogs);
            this.fillWithBlocks(world, box, 3, 0, 1, 3, 0, 7, MPBlocks.INFECTED_WATER_FLUID_BLOCK.getDefaultState(), MPBlocks.INFECTED_WATER_FLUID_BLOCK.getDefaultState(), false);

            for (int i = 1; i <= 7; ++i)
            {
                int j = ((BlockCropsMP)this.cropTypeA).getMaxAge();
                int k = j / 3;
                this.setBlockState(world, this.cropTypeA.getStateFromMeta(MathHelper.getInt(rand, k, j)), 1, 1, i, box);
                this.setBlockState(world, this.cropTypeA.getStateFromMeta(MathHelper.getInt(rand, k, j)), 2, 1, i, box);
                int l = ((BlockCropsMP)this.cropTypeB).getMaxAge();
                int i1 = l / 3;
                this.setBlockState(world, this.cropTypeB.getStateFromMeta(MathHelper.getInt(rand, i1, l)), 4, 1, i, box);
                this.setBlockState(world, this.cropTypeB.getStateFromMeta(MathHelper.getInt(rand, i1, l)), 5, 1, i, box);
            }
            for (int j1 = 0; j1 < 9; ++j1)
            {
                for (int k1 = 0; k1 < 7; ++k1)
                {
                    this.clearCurrentPositionBlocksUpwards(world, k1, 4, j1, box);
                    this.replaceAirAndLiquidDownwards(world, MPBlocks.INFECTED_DIRT.getDefaultState(), k1, -1, j1, box);
                }
            }
            return true;
        }
    }

    public static class Hall extends Village
    {
        public Hall() {}

        public Hall(Start start, int type, StructureBoundingBox box, EnumFacing facing)
        {
            super(start, type);
            this.setCoordBaseMode(facing);
            this.boundingBox = box;
        }

        @SuppressWarnings("deprecation")
        @Override
        public boolean addComponentParts(World world, Random rand, StructureBoundingBox box)
        {
            if (this.averageGroundLvl < 0)
            {
                this.averageGroundLvl = this.getAverageGroundLevel(world, box);

                if (this.averageGroundLvl < 0)
                {
                    return true;
                }
                this.boundingBox.offset(0, this.averageGroundLvl - this.boundingBox.maxY + 7 - 1, 0);
            }

            IBlockState iblockstate = this.getBiomeSpecificBlockState(MPBlocks.NIBIRU_COBBLESTONE.getDefaultState());
            IBlockState iblockstate1 = this.getBiomeSpecificBlockState(MPBlocks.INFECTED_OAK_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, EnumFacing.NORTH));
            IBlockState iblockstate2 = this.getBiomeSpecificBlockState(MPBlocks.INFECTED_OAK_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, EnumFacing.SOUTH));
            IBlockState iblockstate3 = this.getBiomeSpecificBlockState(MPBlocks.INFECTED_OAK_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, EnumFacing.WEST));
            IBlockState iblockstate4 = this.getBiomeSpecificBlockState(MPBlocks.INFECTED_OAK_PLANKS.getDefaultState());
            IBlockState iblockstate5 = this.getBiomeSpecificBlockState(MPBlocks.INFECTED_OAK_LOG.getDefaultState());
            IBlockState iblockstate6 = this.getBiomeSpecificBlockState(MPBlocks.INFECTED_OAK_FENCE.getDefaultState());
            this.fillWithBlocks(world, box, 1, 1, 1, 7, 4, 4, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
            this.fillWithBlocks(world, box, 2, 1, 6, 8, 4, 10, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
            this.fillWithRandomizedBlocks(world, box, 2, 0, 6, 8, 0, 10, false, rand, villageDirts);
            this.setBlockState(world, iblockstate, 6, 0, 6, box);
            this.fillWithBlocks(world, box, 2, 1, 6, 2, 1, 10, iblockstate6, iblockstate6, false);
            this.fillWithBlocks(world, box, 8, 1, 6, 8, 1, 10, iblockstate6, iblockstate6, false);
            this.fillWithBlocks(world, box, 3, 1, 10, 7, 1, 10, iblockstate6, iblockstate6, false);
            this.fillWithRandomizedBlocks(world, box, 1, 0, 1, 7, 0, 4, false, rand, villagePlanks);
            this.fillWithRandomizedBlocks(world, box, 0, 0, 0, 0, 3, 5, false, rand, villageStones);
            this.fillWithRandomizedBlocks(world, box, 8, 0, 0, 8, 3, 5, false, rand, villageStones);
            this.fillWithRandomizedBlocks(world, box, 1, 0, 0, 7, 1, 0, false, rand, villageStones);
            this.fillWithRandomizedBlocks(world, box, 1, 0, 5, 7, 1, 5, false, rand, villageStones);
            this.fillWithRandomizedBlocks(world, box, 1, 2, 0, 7, 3, 0, false, rand, villagePlanks);
            this.fillWithRandomizedBlocks(world, box, 1, 2, 5, 7, 3, 5, false, rand, villagePlanks);
            this.fillWithRandomizedBlocks(world, box, 0, 4, 1, 8, 4, 1, false, rand, villagePlanks);
            this.fillWithRandomizedBlocks(world, box, 0, 4, 4, 8, 4, 4, false, rand, villagePlanks);
            this.fillWithRandomizedBlocks(world, box, 0, 5, 2, 8, 5, 3, false, rand, villagePlanks);
            this.setBlockState(world, iblockstate4, 0, 4, 2, box);
            this.setBlockState(world, iblockstate4, 0, 4, 3, box);
            this.setBlockState(world, iblockstate4, 8, 4, 2, box);
            this.setBlockState(world, iblockstate4, 8, 4, 3, box);
            IBlockState iblockstate7 = iblockstate1;
            IBlockState iblockstate8 = iblockstate2;

            for (int i = -1; i <= 2; ++i)
            {
                for (int j = 0; j <= 8; ++j)
                {
                    this.setBlockState(world, iblockstate7, j, 4 + i, i, box);
                    this.setBlockState(world, iblockstate8, j, 4 + i, 5 - i, box);
                }
            }

            this.setBlockState(world, iblockstate5, 0, 2, 1, box);
            this.setBlockState(world, iblockstate5, 0, 2, 4, box);
            this.setBlockState(world, iblockstate5, 8, 2, 1, box);
            this.setBlockState(world, iblockstate5, 8, 2, 4, box);
            this.setBlockState(world, rand.nextInt(5) == 0 ? Blocks.AIR.getDefaultState() : Blocks.GLASS_PANE.getDefaultState(), 0, 2, 2, box);
            this.setBlockState(world, rand.nextInt(5) == 0 ? Blocks.AIR.getDefaultState() : Blocks.GLASS_PANE.getDefaultState(), 0, 2, 3, box);
            this.setBlockState(world, rand.nextInt(5) == 0 ? Blocks.AIR.getDefaultState() : Blocks.GLASS_PANE.getDefaultState(), 8, 2, 2, box);
            this.setBlockState(world, rand.nextInt(5) == 0 ? Blocks.AIR.getDefaultState() : Blocks.GLASS_PANE.getDefaultState(), 8, 2, 3, box);
            this.setBlockState(world, rand.nextInt(5) == 0 ? Blocks.AIR.getDefaultState() : Blocks.GLASS_PANE.getDefaultState(), 2, 2, 5, box);
            this.setBlockState(world, rand.nextInt(5) == 0 ? Blocks.AIR.getDefaultState() : Blocks.GLASS_PANE.getDefaultState(), 3, 2, 5, box);
            this.setBlockState(world, rand.nextInt(5) == 0 ? Blocks.AIR.getDefaultState() : Blocks.GLASS_PANE.getDefaultState(), 5, 2, 0, box);
            this.setBlockState(world, rand.nextInt(5) == 0 ? Blocks.AIR.getDefaultState() : Blocks.GLASS_PANE.getDefaultState(), 6, 2, 5, box);
            this.setBlockState(world, iblockstate6, 2, 1, 3, box);
            this.setBlockState(world, MPBlocks.INFECTED_OAK_PRESSURE_PLATE.getDefaultState(), 2, 2, 3, box);
            this.setBlockState(world, iblockstate4, 1, 1, 4, box);
            this.setBlockState(world, iblockstate7, 2, 1, 4, box);
            this.setBlockState(world, iblockstate3, 1, 1, 3, box);
            this.fillWithBlocks(world, box, 5, 0, 1, 7, 0, 3, MPBlocks.DOUBLE_INFECTED_STONE_BRICKS_SLAB.getDefaultState(), MPBlocks.DOUBLE_INFECTED_STONE_BRICKS_SLAB.getDefaultState(), false);
            this.setBlockState(world, MPBlocks.DOUBLE_INFECTED_STONE_BRICKS_SLAB.getDefaultState(), 6, 1, 1, box);
            this.setBlockState(world, MPBlocks.DOUBLE_INFECTED_STONE_BRICKS_SLAB.getDefaultState(), 6, 1, 2, box);
            this.setBlockState(world, Blocks.AIR.getDefaultState(), 2, 1, 0, box);
            this.setBlockState(world, Blocks.AIR.getDefaultState(), 2, 2, 0, box);
            this.placeTorch(world, EnumFacing.NORTH, 2, 3, 1, box);
            this.createVillageDoor(world, box, rand, 2, 1, 0);

            if (this.getBlockStateFromPos(world, 2, 0, -1, box).getMaterial() == Material.AIR && this.getBlockStateFromPos(world, 2, -1, -1, box).getMaterial() != Material.AIR)
            {
                this.setBlockState(world, iblockstate7, 2, 0, -1, box);

                if (this.getBlockStateFromPos(world, 2, -1, -1, box).getBlock() == MPBlocks.INFECTED_GRASS_PATH)
                {
                    this.setBlockState(world, MPBlocks.INFECTED_GRASS_BLOCK.getDefaultState(), 2, -1, -1, box);
                }
            }

            this.setBlockState(world, Blocks.AIR.getDefaultState(), 6, 1, 5, box);
            this.setBlockState(world, Blocks.AIR.getDefaultState(), 6, 2, 5, box);
            this.placeTorch(world, EnumFacing.SOUTH, 6, 3, 4, box);
            this.createVillageDoor(world, box, rand, 6, 1, 5);

            for (int k = 0; k < 5; ++k)
            {
                for (int l = 0; l < 9; ++l)
                {
                    this.clearCurrentPositionBlocksUpwards(world, l, 7, k, box);
                    this.replaceAirAndLiquidDownwards(world, iblockstate, l, -1, k, box);
                }
            }
            this.spawnVillagers(world, box, 4, 1, 2, 2);
            return true;
        }

        @Override
        protected int chooseProfession(int villagersSpawned, int currentVillagerProfession)
        {
            return villagersSpawned == 0 ? this.structureType == 2 ? 4 : 2 : super.chooseProfession(villagersSpawned, currentVillagerProfession);
        }
    }

    public static class House1 extends Village
    {
        public House1() {}

        public House1(Start start, int type, StructureBoundingBox box, EnumFacing facing)
        {
            super(start, type);
            this.setCoordBaseMode(facing);
            this.boundingBox = box;
        }

        @Override
        public boolean addComponentParts(World world, Random rand, StructureBoundingBox box)
        {
            if (this.averageGroundLvl < 0)
            {
                this.averageGroundLvl = this.getAverageGroundLevel(world, box);

                if (this.averageGroundLvl < 0)
                {
                    return true;
                }
                this.boundingBox.offset(0, this.averageGroundLvl - this.boundingBox.maxY + 9 - 1, 0);
            }

            IBlockState iblockstate = this.getBiomeSpecificBlockState(MPBlocks.NIBIRU_COBBLESTONE.getDefaultState());
            IBlockState iblockstate1 = this.getBiomeSpecificBlockState(MPBlocks.INFECTED_OAK_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, EnumFacing.NORTH));
            IBlockState iblockstate2 = this.getBiomeSpecificBlockState(MPBlocks.INFECTED_OAK_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, EnumFacing.SOUTH));
            IBlockState iblockstate3 = this.getBiomeSpecificBlockState(MPBlocks.INFECTED_OAK_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, EnumFacing.EAST));
            IBlockState iblockstate4 = this.getBiomeSpecificBlockState(MPBlocks.INFECTED_OAK_PLANKS.getDefaultState());
            IBlockState iblockstate5 = this.getBiomeSpecificBlockState(MPBlocks.NIBIRU_COBBLESTONE_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, EnumFacing.NORTH));
            IBlockState iblockstate6 = this.getBiomeSpecificBlockState(MPBlocks.INFECTED_OAK_FENCE.getDefaultState());
            this.fillWithBlocks(world, box, 1, 1, 1, 7, 5, 4, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
            this.fillWithRandomizedBlocks(world, box, 0, 0, 0, 8, 0, 5, false, rand, villageStones);
            this.fillWithRandomizedBlocks(world, box, 0, 5, 0, 8, 5, 5, false, rand, villageStones);
            this.fillWithRandomizedBlocks(world, box, 0, 6, 1, 8, 6, 4, false, rand, villageStones);
            this.fillWithRandomizedBlocks(world, box, 0, 7, 2, 8, 7, 3, false, rand, villageStones);

            for (int i = -1; i <= 2; ++i)
            {
                for (int j = 0; j <= 8; ++j)
                {
                    this.setBlockState(world, iblockstate1, j, 6 + i, i, box);
                    this.setBlockState(world, iblockstate2, j, 6 + i, 5 - i, box);
                }
            }

            this.fillWithRandomizedBlocks(world, box, 0, 1, 0, 0, 1, 5, false, rand, villageStones);
            this.fillWithRandomizedBlocks(world, box, 1, 1, 5, 8, 1, 5, false, rand, villageStones);
            this.fillWithRandomizedBlocks(world, box, 8, 1, 0, 8, 1, 4, false, rand, villageStones);
            this.fillWithRandomizedBlocks(world, box, 2, 1, 0, 7, 1, 0, false, rand, villageStones);
            this.fillWithRandomizedBlocks(world, box, 0, 2, 0, 0, 4, 0, false, rand, villageStones);
            this.fillWithRandomizedBlocks(world, box, 0, 2, 5, 0, 4, 5, false, rand, villageStones);
            this.fillWithRandomizedBlocks(world, box, 8, 2, 5, 8, 4, 5, false, rand, villageStones);
            this.fillWithRandomizedBlocks(world, box, 8, 2, 0, 8, 4, 0, false, rand, villageStones);
            this.fillWithRandomizedBlocks(world, box, 0, 2, 1, 0, 4, 4, false, rand, villagePlanks);
            this.fillWithRandomizedBlocks(world, box, 1, 2, 5, 7, 4, 5, false, rand, villagePlanks);
            this.fillWithRandomizedBlocks(world, box, 8, 2, 1, 8, 4, 4, false, rand, villagePlanks);
            this.fillWithRandomizedBlocks(world, box, 1, 2, 0, 7, 4, 0, false, rand, villagePlanks);
            this.setBlockState(world, rand.nextInt(5) == 0 ? Blocks.AIR.getDefaultState() : Blocks.GLASS_PANE.getDefaultState(), 4, 2, 0, box);
            this.setBlockState(world, rand.nextInt(5) == 0 ? Blocks.AIR.getDefaultState() : Blocks.GLASS_PANE.getDefaultState(), 5, 2, 0, box);
            this.setBlockState(world, rand.nextInt(5) == 0 ? Blocks.AIR.getDefaultState() : Blocks.GLASS_PANE.getDefaultState(), 6, 2, 0, box);
            this.setBlockState(world, rand.nextInt(5) == 0 ? Blocks.AIR.getDefaultState() : Blocks.GLASS_PANE.getDefaultState(), 4, 3, 0, box);
            this.setBlockState(world, rand.nextInt(5) == 0 ? Blocks.AIR.getDefaultState() : Blocks.GLASS_PANE.getDefaultState(), 5, 3, 0, box);
            this.setBlockState(world, rand.nextInt(5) == 0 ? Blocks.AIR.getDefaultState() : Blocks.GLASS_PANE.getDefaultState(), 6, 3, 0, box);
            this.setBlockState(world, rand.nextInt(5) == 0 ? Blocks.AIR.getDefaultState() : Blocks.GLASS_PANE.getDefaultState(), 0, 2, 2, box);
            this.setBlockState(world, rand.nextInt(5) == 0 ? Blocks.AIR.getDefaultState() : Blocks.GLASS_PANE.getDefaultState(), 0, 2, 3, box);
            this.setBlockState(world, rand.nextInt(5) == 0 ? Blocks.AIR.getDefaultState() : Blocks.GLASS_PANE.getDefaultState(), 0, 3, 2, box);
            this.setBlockState(world, rand.nextInt(5) == 0 ? Blocks.AIR.getDefaultState() : Blocks.GLASS_PANE.getDefaultState(), 0, 3, 3, box);
            this.setBlockState(world, rand.nextInt(5) == 0 ? Blocks.AIR.getDefaultState() : Blocks.GLASS_PANE.getDefaultState(), 8, 2, 2, box);
            this.setBlockState(world, rand.nextInt(5) == 0 ? Blocks.AIR.getDefaultState() : Blocks.GLASS_PANE.getDefaultState(), 8, 2, 3, box);
            this.setBlockState(world, rand.nextInt(5) == 0 ? Blocks.AIR.getDefaultState() : Blocks.GLASS_PANE.getDefaultState(), 8, 3, 2, box);
            this.setBlockState(world, rand.nextInt(5) == 0 ? Blocks.AIR.getDefaultState() : Blocks.GLASS_PANE.getDefaultState(), 8, 3, 3, box);
            this.setBlockState(world, rand.nextInt(5) == 0 ? Blocks.AIR.getDefaultState() : Blocks.GLASS_PANE.getDefaultState(), 2, 2, 5, box);
            this.setBlockState(world, rand.nextInt(5) == 0 ? Blocks.AIR.getDefaultState() : Blocks.GLASS_PANE.getDefaultState(), 3, 2, 5, box);
            this.setBlockState(world, rand.nextInt(5) == 0 ? Blocks.AIR.getDefaultState() : Blocks.GLASS_PANE.getDefaultState(), 5, 2, 5, box);
            this.setBlockState(world, rand.nextInt(5) == 0 ? Blocks.AIR.getDefaultState() : Blocks.GLASS_PANE.getDefaultState(), 6, 2, 5, box);
            this.fillWithRandomizedBlocks(world, box, 1, 4, 1, 7, 4, 1, false, rand, villagePlanks);
            this.fillWithRandomizedBlocks(world, box, 1, 4, 4, 7, 4, 4, false, rand, villagePlanks);
            this.fillWithBlocks(world, box, 1, 3, 4, 7, 3, 4, MPBlocks.INFECTED_OAK_BOOKSHELF.getDefaultState(), MPBlocks.INFECTED_OAK_BOOKSHELF.getDefaultState(), false);
            this.setBlockState(world, iblockstate4, 7, 1, 4, box);
            this.setBlockState(world, iblockstate3, 7, 1, 3, box);
            this.setBlockState(world, iblockstate1, 6, 1, 4, box);
            this.setBlockState(world, iblockstate1, 5, 1, 4, box);
            this.setBlockState(world, iblockstate1, 4, 1, 4, box);
            this.setBlockState(world, iblockstate1, 3, 1, 4, box);
            this.setBlockState(world, iblockstate6, 6, 1, 3, box);
            this.setBlockState(world, MPBlocks.INFECTED_OAK_PRESSURE_PLATE.getDefaultState(), 6, 2, 3, box);
            this.setBlockState(world, iblockstate6, 4, 1, 3, box);
            this.setBlockState(world, MPBlocks.INFECTED_OAK_PRESSURE_PLATE.getDefaultState(), 4, 2, 3, box);
            this.setBlockState(world, MPBlocks.INFECTED_CRAFTING_TABLE.getDefaultState(), 7, 1, 1, box);
            this.generateChest(world, box, rand, 7, 2, 1, MPLootTables.NIBIRU_VILLAGE_LIBRARY);
            this.setBlockState(world, Blocks.AIR.getDefaultState(), 1, 1, 0, box);
            this.setBlockState(world, Blocks.AIR.getDefaultState(), 1, 2, 0, box);
            this.createVillageDoor(world, box, rand, 1, 1, 0);

            if (this.getBlockStateFromPos(world, 1, 0, -1, box).getMaterial() == Material.AIR && this.getBlockStateFromPos(world, 1, -1, -1, box).getMaterial() != Material.AIR)
            {
                this.setBlockState(world, iblockstate5, 1, 0, -1, box);

                if (this.getBlockStateFromPos(world, 1, -1, -1, box).getBlock() == MPBlocks.INFECTED_GRASS_PATH)
                {
                    this.setBlockState(world, MPBlocks.INFECTED_GRASS_BLOCK.getDefaultState(), 1, -1, -1, box);
                }
            }
            for (int l = 0; l < 6; ++l)
            {
                for (int k = 0; k < 9; ++k)
                {
                    this.clearCurrentPositionBlocksUpwards(world, k, 9, l, box);
                    this.replaceAirAndLiquidDownwards(world, iblockstate, k, -1, l, box);
                }
            }
            this.spawnVillagers(world, box, 2, 1, 2, 1);
            return true;
        }

        @Override
        protected int chooseProfession(int villagersSpawned, int currentVillagerProfession)
        {
            return this.structureType == 2 ? 4 : 1;
        }
    }

    public static class House2 extends Village
    {
        private boolean hasMadeChest;

        public House2() {}

        public House2(Start start, int type, StructureBoundingBox box, EnumFacing facing)
        {
            super(start, type);
            this.setCoordBaseMode(facing);
            this.boundingBox = box;
        }

        @Override
        protected void writeStructureToNBT(NBTTagCompound nbt)
        {
            super.writeStructureToNBT(nbt);
            nbt.setBoolean("Chest", this.hasMadeChest);
        }

        @Override
        protected void readStructureFromNBT(NBTTagCompound nbt, TemplateManager manager)
        {
            super.readStructureFromNBT(nbt, manager);
            this.hasMadeChest = nbt.getBoolean("Chest");
        }

        @SuppressWarnings("deprecation")
        @Override
        public boolean addComponentParts(World world, Random rand, StructureBoundingBox box)
        {
            if (this.averageGroundLvl < 0)
            {
                this.averageGroundLvl = this.getAverageGroundLevel(world, box);

                if (this.averageGroundLvl < 0)
                {
                    return true;
                }
                this.boundingBox.offset(0, this.averageGroundLvl - this.boundingBox.maxY + 6 - 1, 0);
            }

            IBlockState iblockstate = MPBlocks.NIBIRU_COBBLESTONE.getDefaultState();
            IBlockState iblockstate1 = this.getBiomeSpecificBlockState(MPBlocks.INFECTED_OAK_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, EnumFacing.NORTH));
            IBlockState iblockstate2 = this.getBiomeSpecificBlockState(MPBlocks.INFECTED_OAK_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, EnumFacing.WEST));
            IBlockState iblockstate3 = this.getBiomeSpecificBlockState(MPBlocks.INFECTED_OAK_PLANKS.getDefaultState());
            IBlockState iblockstate4 = this.getBiomeSpecificBlockState(MPBlocks.NIBIRU_COBBLESTONE_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, EnumFacing.NORTH));
            IBlockState iblockstate6 = this.getBiomeSpecificBlockState(MPBlocks.INFECTED_OAK_FENCE.getDefaultState());
            this.fillWithBlocks(world, box, 0, 1, 0, 9, 4, 6, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
            this.fillWithRandomizedBlocks(world, box, 0, 0, 0, 9, 0, 6, false, rand, villageStones);
            this.fillWithRandomizedBlocks(world, box, 0, 4, 0, 9, 4, 6, false, rand, villageStones);
            this.fillWithBlocks(world, box, 0, 5, 0, 9, 5, 6, MPBlocks.INFECTED_STONE_BRICKS_SLAB.getDefaultState(), MPBlocks.INFECTED_STONE_BRICKS_SLAB.getDefaultState(), false);
            this.fillWithBlocks(world, box, 1, 5, 1, 8, 5, 5, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
            this.fillWithRandomizedBlocks(world, box, 1, 1, 0, 2, 3, 0, false, rand, villagePlanks);
            this.fillWithRandomizedBlocks(world, box, 0, 1, 0, 0, 4, 0, false, rand, villageLogs);
            this.fillWithRandomizedBlocks(world, box, 3, 1, 0, 3, 4, 0, false, rand, villageLogs);
            this.fillWithRandomizedBlocks(world, box, 0, 1, 6, 0, 4, 6, false, rand, villageLogs);
            this.setBlockState(world, iblockstate3, 3, 3, 1, box);
            this.fillWithRandomizedBlocks(world, box, 3, 1, 2, 3, 3, 2, false, rand, villagePlanks);
            this.fillWithRandomizedBlocks(world, box, 4, 1, 3, 5, 3, 3, false, rand, villagePlanks);
            this.fillWithRandomizedBlocks(world, box, 0, 1, 1, 0, 3, 5, false, rand, villagePlanks);
            this.fillWithRandomizedBlocks(world, box, 1, 1, 6, 5, 3, 6, false, rand, villagePlanks);
            this.fillWithBlocks(world, box, 5, 1, 0, 5, 3, 0, iblockstate6, iblockstate6, false);
            this.fillWithBlocks(world, box, 9, 1, 0, 9, 3, 0, iblockstate6, iblockstate6, false);
            this.fillWithRandomizedBlocks(world, box, 6, 1, 4, 9, 4, 6, false, rand, villageStones);
            IBlockState lava = rand.nextInt(100) == 0 ? MPBlocks.NUCLEAR_WASTE_FLUID_BLOCK.getDefaultState() : Blocks.FLOWING_LAVA.getDefaultState();
            this.setBlockState(world, lava, 7, 1, 5, box);
            this.setBlockState(world, lava, 8, 1, 5, box);
            this.setBlockState(world, Blocks.IRON_BARS.getDefaultState(), 9, 2, 5, box);
            this.setBlockState(world, Blocks.IRON_BARS.getDefaultState(), 9, 2, 4, box);
            this.fillWithBlocks(world, box, 7, 2, 4, 8, 2, 5, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
            this.setBlockState(world, iblockstate, 6, 1, 3, box);
            this.setBlockState(world, MPBlocks.INFECTED_FURNACE.getDefaultState(), 6, 2, 3, box);
            this.setBlockState(world, MPBlocks.INFECTED_FURNACE.getDefaultState(), 6, 3, 3, box);
            this.setBlockState(world, MPBlocks.DOUBLE_INFECTED_STONE_BRICKS_SLAB.getDefaultState(), 8, 1, 1, box);
            this.setBlockState(world, rand.nextInt(5) == 0 ? Blocks.AIR.getDefaultState() : Blocks.GLASS_PANE.getDefaultState(), 0, 2, 2, box);
            this.setBlockState(world, rand.nextInt(5) == 0 ? Blocks.AIR.getDefaultState() : Blocks.GLASS_PANE.getDefaultState(), 0, 2, 4, box);
            this.setBlockState(world, rand.nextInt(5) == 0 ? Blocks.AIR.getDefaultState() : Blocks.GLASS_PANE.getDefaultState(), 2, 2, 6, box);
            this.setBlockState(world, rand.nextInt(5) == 0 ? Blocks.AIR.getDefaultState() : Blocks.GLASS_PANE.getDefaultState(), 4, 2, 6, box);
            this.setBlockState(world, iblockstate6, 2, 1, 4, box);
            this.setBlockState(world, MPBlocks.INFECTED_OAK_PRESSURE_PLATE.getDefaultState(), 2, 2, 4, box);
            this.setBlockState(world, iblockstate3, 1, 1, 5, box);
            this.setBlockState(world, iblockstate1, 2, 1, 5, box);
            this.setBlockState(world, iblockstate2, 1, 1, 4, box);

            if (!this.hasMadeChest && box.isVecInside(new BlockPos(this.getXWithOffset(5, 5), this.getYWithOffset(1), this.getZWithOffset(5, 5))))
            {
                this.hasMadeChest = true;
                this.generateChest(world, box, rand, 5, 1, 5, MPLootTables.NIBIRU_VILLAGE_BLACKSMITH);
            }
            for (int i = 6; i <= 8; ++i)
            {
                if (this.getBlockStateFromPos(world, i, 0, -1, box).getMaterial() == Material.AIR && this.getBlockStateFromPos(world, i, -1, -1, box).getMaterial() != Material.AIR)
                {
                    this.setBlockState(world, iblockstate4, i, 0, -1, box);

                    if (this.getBlockStateFromPos(world, i, -1, -1, box).getBlock() == MPBlocks.INFECTED_GRASS_PATH)
                    {
                        this.setBlockState(world, MPBlocks.INFECTED_GRASS_BLOCK.getDefaultState(), i, -1, -1, box);
                    }
                }
            }
            for (int k = 0; k < 7; ++k)
            {
                for (int j = 0; j < 10; ++j)
                {
                    this.clearCurrentPositionBlocksUpwards(world, j, 6, k, box);
                    this.replaceAirAndLiquidDownwards(world, iblockstate, j, -1, k, box);
                }
            }
            this.spawnVillagers(world, box, 7, 1, 1, 1);
            return true;
        }

        @Override
        protected int chooseProfession(int villagersSpawned, int currentVillagerProfession)
        {
            return this.structureType == 2 ? 3 : 0;
        }
    }

    public static class House3 extends Village
    {
        public House3() {}

        public House3(Start start, int type, StructureBoundingBox box, EnumFacing facing)
        {
            super(start, type);
            this.setCoordBaseMode(facing);
            this.boundingBox = box;
        }

        @Override
        public boolean addComponentParts(World world, Random rand, StructureBoundingBox box)
        {
            if (this.averageGroundLvl < 0)
            {
                this.averageGroundLvl = this.getAverageGroundLevel(world, box);

                if (this.averageGroundLvl < 0)
                {
                    return true;
                }
                this.boundingBox.offset(0, this.averageGroundLvl - this.boundingBox.maxY + 7 - 1, 0);
            }

            IBlockState iblockstate = this.getBiomeSpecificBlockState(MPBlocks.NIBIRU_COBBLESTONE.getDefaultState());
            IBlockState iblockstate1 = this.getBiomeSpecificBlockState(MPBlocks.INFECTED_OAK_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, EnumFacing.NORTH));
            IBlockState iblockstate2 = this.getBiomeSpecificBlockState(MPBlocks.INFECTED_OAK_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, EnumFacing.SOUTH));
            IBlockState iblockstate3 = this.getBiomeSpecificBlockState(MPBlocks.INFECTED_OAK_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, EnumFacing.EAST));
            IBlockState iblockstate4 = this.getBiomeSpecificBlockState(MPBlocks.INFECTED_OAK_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, EnumFacing.WEST));
            IBlockState iblockstate5 = this.getBiomeSpecificBlockState(MPBlocks.INFECTED_OAK_PLANKS.getDefaultState());
            IBlockState iblockstate6 = this.getBiomeSpecificBlockState(MPBlocks.INFECTED_OAK_LOG.getDefaultState());
            this.fillWithBlocks(world, box, 1, 1, 1, 7, 4, 4, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
            this.fillWithBlocks(world, box, 2, 1, 6, 8, 4, 10, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
            this.fillWithRandomizedBlocks(world, box, 2, 0, 5, 8, 0, 10, false, rand, villagePlanks);
            this.fillWithRandomizedBlocks(world, box, 1, 0, 1, 7, 0, 4, false, rand, villagePlanks);
            this.fillWithRandomizedBlocks(world, box, 0, 0, 0, 0, 3, 5, false, rand, villageStones);
            this.fillWithRandomizedBlocks(world, box, 8, 0, 0, 8, 3, 10, false, rand, villageStones);
            this.fillWithRandomizedBlocks(world, box, 1, 0, 0, 7, 2, 0, false, rand, villageStones);
            this.fillWithRandomizedBlocks(world, box, 1, 0, 5, 2, 1, 5, false, rand, villageStones);
            this.fillWithRandomizedBlocks(world, box, 2, 0, 6, 2, 3, 10, false, rand, villageStones);
            this.fillWithRandomizedBlocks(world, box, 3, 0, 10, 7, 3, 10, false, rand, villageStones);
            this.fillWithRandomizedBlocks(world, box, 1, 2, 0, 7, 3, 0, false, rand, villagePlanks);
            this.fillWithRandomizedBlocks(world, box, 1, 2, 5, 2, 3, 5, false, rand, villagePlanks);
            this.fillWithRandomizedBlocks(world, box, 0, 4, 1, 8, 4, 1, false, rand, villagePlanks);
            this.fillWithRandomizedBlocks(world, box, 0, 4, 4, 3, 4, 4, false, rand, villagePlanks);
            this.fillWithRandomizedBlocks(world, box, 0, 5, 2, 8, 5, 3, false, rand, villagePlanks);
            this.setBlockState(world, iblockstate5, 0, 4, 2, box);
            this.setBlockState(world, iblockstate5, 0, 4, 3, box);
            this.setBlockState(world, iblockstate5, 8, 4, 2, box);
            this.setBlockState(world, iblockstate5, 8, 4, 3, box);
            this.setBlockState(world, iblockstate5, 8, 4, 4, box);
            IBlockState iblockstate7 = iblockstate1;
            IBlockState iblockstate8 = iblockstate2;
            IBlockState iblockstate9 = iblockstate4;
            IBlockState iblockstate10 = iblockstate3;

            for (int i = -1; i <= 2; ++i)
            {
                for (int j = 0; j <= 8; ++j)
                {
                    this.setBlockState(world, iblockstate7, j, 4 + i, i, box);

                    if ((i > -1 || j <= 1) && (i > 0 || j <= 3) && (i > 1 || j <= 4 || j >= 6))
                    {
                        this.setBlockState(world, iblockstate8, j, 4 + i, 5 - i, box);
                    }
                }
            }

            this.fillWithRandomizedBlocks(world, box, 3, 4, 5, 3, 4, 10, false, rand, villagePlanks);
            this.fillWithRandomizedBlocks(world, box, 7, 4, 2, 7, 4, 10, false, rand, villagePlanks);
            this.fillWithRandomizedBlocks(world, box, 4, 5, 4, 4, 5, 10, false, rand, villagePlanks);
            this.fillWithRandomizedBlocks(world, box, 6, 5, 4, 6, 5, 10, false, rand, villagePlanks);
            this.fillWithRandomizedBlocks(world, box, 5, 6, 3, 5, 6, 10, false, rand, villagePlanks);

            for (int k = 4; k >= 1; --k)
            {
                this.setBlockState(world, iblockstate5, k, 2 + k, 7 - k, box);

                for (int k1 = 8 - k; k1 <= 10; ++k1)
                {
                    this.setBlockState(world, iblockstate10, k, 2 + k, k1, box);
                }
            }

            this.setBlockState(world, iblockstate5, 6, 6, 3, box);
            this.setBlockState(world, iblockstate5, 7, 5, 4, box);
            this.setBlockState(world, iblockstate4, 6, 6, 4, box);

            for (int l = 6; l <= 8; ++l)
            {
                for (int l1 = 5; l1 <= 10; ++l1)
                {
                    this.setBlockState(world, iblockstate9, l, 12 - l, l1, box);
                }
            }

            this.setBlockState(world, iblockstate6, 0, 2, 1, box);
            this.setBlockState(world, iblockstate6, 0, 2, 4, box);
            this.setBlockState(world, rand.nextInt(5) == 0 ? Blocks.AIR.getDefaultState() : Blocks.GLASS_PANE.getDefaultState(), 0, 2, 2, box);
            this.setBlockState(world, rand.nextInt(5) == 0 ? Blocks.AIR.getDefaultState() : Blocks.GLASS_PANE.getDefaultState(), 0, 2, 3, box);
            this.setBlockState(world, iblockstate6, 4, 2, 0, box);
            this.setBlockState(world, rand.nextInt(5) == 0 ? Blocks.AIR.getDefaultState() : Blocks.GLASS_PANE.getDefaultState(), 5, 2, 0, box);
            this.setBlockState(world, iblockstate6, 6, 2, 0, box);
            this.setBlockState(world, iblockstate6, 8, 2, 1, box);
            this.setBlockState(world, rand.nextInt(5) == 0 ? Blocks.AIR.getDefaultState() : Blocks.GLASS_PANE.getDefaultState(), 8, 2, 2, box);
            this.setBlockState(world, rand.nextInt(5) == 0 ? Blocks.AIR.getDefaultState() : Blocks.GLASS_PANE.getDefaultState(), 8, 2, 3, box);
            this.setBlockState(world, iblockstate6, 8, 2, 4, box);
            this.setBlockState(world, iblockstate5, 8, 2, 5, box);
            this.setBlockState(world, iblockstate6, 8, 2, 6, box);
            this.setBlockState(world, rand.nextInt(5) == 0 ? Blocks.AIR.getDefaultState() : Blocks.GLASS_PANE.getDefaultState(), 8, 2, 7, box);
            this.setBlockState(world, rand.nextInt(5) == 0 ? Blocks.AIR.getDefaultState() : Blocks.GLASS_PANE.getDefaultState(), 8, 2, 8, box);
            this.setBlockState(world, iblockstate6, 8, 2, 9, box);
            this.setBlockState(world, iblockstate6, 2, 2, 6, box);
            this.setBlockState(world, rand.nextInt(5) == 0 ? Blocks.AIR.getDefaultState() : Blocks.GLASS_PANE.getDefaultState(), 2, 2, 7, box);
            this.setBlockState(world, rand.nextInt(5) == 0 ? Blocks.AIR.getDefaultState() : Blocks.GLASS_PANE.getDefaultState(), 2, 2, 8, box);
            this.setBlockState(world, iblockstate6, 2, 2, 9, box);
            this.setBlockState(world, iblockstate6, 4, 4, 10, box);
            this.setBlockState(world, rand.nextInt(5) == 0 ? Blocks.AIR.getDefaultState() : Blocks.GLASS_PANE.getDefaultState(), 5, 4, 10, box);
            this.setBlockState(world, iblockstate6, 6, 4, 10, box);
            this.setBlockState(world, iblockstate5, 5, 5, 10, box);
            this.setBlockState(world, Blocks.AIR.getDefaultState(), 2, 1, 0, box);
            this.setBlockState(world, Blocks.AIR.getDefaultState(), 2, 2, 0, box);
            this.placeTorch(world, EnumFacing.NORTH, 2, 3, 1, box);
            this.createVillageDoor(world, box, rand, 2, 1, 0);
            this.fillWithBlocks(world, box, 1, 0, -1, 3, 2, -1, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);

            if (this.getBlockStateFromPos(world, 2, 0, -1, box).getMaterial() == Material.AIR && this.getBlockStateFromPos(world, 2, -1, -1, box).getMaterial() != Material.AIR)
            {
                this.setBlockState(world, iblockstate7, 2, 0, -1, box);

                if (this.getBlockStateFromPos(world, 2, -1, -1, box).getBlock() == MPBlocks.INFECTED_GRASS_PATH)
                {
                    this.setBlockState(world, MPBlocks.INFECTED_GRASS_BLOCK.getDefaultState(), 2, -1, -1, box);
                }
            }
            for (int i1 = 0; i1 < 5; ++i1)
            {
                for (int i2 = 0; i2 < 9; ++i2)
                {
                    this.clearCurrentPositionBlocksUpwards(world, i2, 7, i1, box);
                    this.replaceAirAndLiquidDownwards(world, iblockstate, i2, -1, i1, box);
                }
            }
            for (int j1 = 5; j1 < 11; ++j1)
            {
                for (int j2 = 2; j2 < 9; ++j2)
                {
                    this.clearCurrentPositionBlocksUpwards(world, j2, 7, j1, box);
                    this.replaceAirAndLiquidDownwards(world, iblockstate, j2, -1, j1, box);
                }
            }
            this.spawnVillagers(world, box, 4, 1, 2, 2);
            return true;
        }
    }

    public static class House4Garden extends Village
    {
        private boolean isRoofAccessible;

        public House4Garden() {}

        public House4Garden(Start start, int type, Random rand, StructureBoundingBox box, EnumFacing facing)
        {
            super(start, type);
            this.setCoordBaseMode(facing);
            this.boundingBox = box;
            this.isRoofAccessible = rand.nextBoolean();
        }

        @Override
        protected void writeStructureToNBT(NBTTagCompound nbt)
        {
            super.writeStructureToNBT(nbt);
            nbt.setBoolean("Terrace", this.isRoofAccessible);
        }

        @Override
        protected void readStructureFromNBT(NBTTagCompound nbt, TemplateManager manager)
        {
            super.readStructureFromNBT(nbt, manager);
            this.isRoofAccessible = nbt.getBoolean("Terrace");
        }

        @Override
        public boolean addComponentParts(World world, Random rand, StructureBoundingBox box)
        {
            if (this.averageGroundLvl < 0)
            {
                this.averageGroundLvl = this.getAverageGroundLevel(world, box);

                if (this.averageGroundLvl < 0)
                {
                    return true;
                }
                this.boundingBox.offset(0, this.averageGroundLvl - this.boundingBox.maxY + 6 - 1, 0);
            }

            IBlockState iblockstate = this.getBiomeSpecificBlockState(MPBlocks.NIBIRU_COBBLESTONE.getDefaultState());
            IBlockState iblockstate1 = this.getBiomeSpecificBlockState(MPBlocks.INFECTED_OAK_PLANKS.getDefaultState());
            IBlockState iblockstate2 = this.getBiomeSpecificBlockState(MPBlocks.NIBIRU_COBBLESTONE_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, EnumFacing.NORTH));
            IBlockState iblockstate4 = this.getBiomeSpecificBlockState(MPBlocks.INFECTED_OAK_FENCE.getDefaultState());
            this.fillWithRandomizedBlocks(world, box, 0, 0, 0, 4, 0, 4, false, rand, villageStones);
            this.fillWithRandomizedBlocks(world, box, 0, 4, 0, 4, 4, 4, false, rand, villageLogs);
            this.fillWithRandomizedBlocks(world, box, 1, 4, 1, 3, 4, 3, false, rand, villagePlanks);
            this.setBlockState(world, iblockstate, 0, 1, 0, box);
            this.setBlockState(world, iblockstate, 0, 2, 0, box);
            this.setBlockState(world, iblockstate, 0, 3, 0, box);
            this.setBlockState(world, iblockstate, 4, 1, 0, box);
            this.setBlockState(world, iblockstate, 4, 2, 0, box);
            this.setBlockState(world, iblockstate, 4, 3, 0, box);
            this.setBlockState(world, iblockstate, 0, 1, 4, box);
            this.setBlockState(world, iblockstate, 0, 2, 4, box);
            this.setBlockState(world, iblockstate, 0, 3, 4, box);
            this.setBlockState(world, iblockstate, 4, 1, 4, box);
            this.setBlockState(world, iblockstate, 4, 2, 4, box);
            this.setBlockState(world, iblockstate, 4, 3, 4, box);
            this.fillWithRandomizedBlocks(world, box, 0, 1, 1, 0, 3, 3, false, rand, villagePlanks);
            this.fillWithRandomizedBlocks(world, box, 4, 1, 1, 4, 3, 3, false, rand, villagePlanks);
            this.fillWithRandomizedBlocks(world, box, 1, 1, 4, 3, 3, 4, false, rand, villagePlanks);
            this.setBlockState(world, rand.nextInt(5) == 0 ? Blocks.AIR.getDefaultState() : Blocks.GLASS_PANE.getDefaultState(), 0, 2, 2, box);
            this.setBlockState(world, rand.nextInt(5) == 0 ? Blocks.AIR.getDefaultState() : Blocks.GLASS_PANE.getDefaultState(), 2, 2, 4, box);
            this.setBlockState(world, rand.nextInt(5) == 0 ? Blocks.AIR.getDefaultState() : Blocks.GLASS_PANE.getDefaultState(), 4, 2, 2, box);
            this.setBlockState(world, iblockstate1, 1, 1, 0, box);
            this.setBlockState(world, iblockstate1, 1, 2, 0, box);
            this.setBlockState(world, iblockstate1, 1, 3, 0, box);
            this.setBlockState(world, iblockstate1, 2, 3, 0, box);
            this.setBlockState(world, iblockstate1, 3, 3, 0, box);
            this.setBlockState(world, iblockstate1, 3, 2, 0, box);
            this.setBlockState(world, iblockstate1, 3, 1, 0, box);

            if (this.getBlockStateFromPos(world, 2, 0, -1, box).getMaterial() == Material.AIR && this.getBlockStateFromPos(world, 2, -1, -1, box).getMaterial() != Material.AIR)
            {
                this.setBlockState(world, iblockstate2, 2, 0, -1, box);

                if (this.getBlockStateFromPos(world, 2, -1, -1, box).getBlock() == MPBlocks.INFECTED_GRASS_PATH)
                {
                    this.setBlockState(world, MPBlocks.INFECTED_GRASS_BLOCK.getDefaultState(), 2, -1, -1, box);
                }
            }

            this.fillWithBlocks(world, box, 1, 1, 1, 3, 3, 3, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);

            if (this.isRoofAccessible)
            {
                this.setBlockState(world, iblockstate4, 0, 5, 0, box);
                this.setBlockState(world, iblockstate4, 1, 5, 0, box);
                this.setBlockState(world, iblockstate4, 2, 5, 0, box);
                this.setBlockState(world, iblockstate4, 3, 5, 0, box);
                this.setBlockState(world, iblockstate4, 4, 5, 0, box);
                this.setBlockState(world, iblockstate4, 0, 5, 4, box);
                this.setBlockState(world, iblockstate4, 1, 5, 4, box);
                this.setBlockState(world, iblockstate4, 2, 5, 4, box);
                this.setBlockState(world, iblockstate4, 3, 5, 4, box);
                this.setBlockState(world, iblockstate4, 4, 5, 4, box);
                this.setBlockState(world, iblockstate4, 4, 5, 1, box);
                this.setBlockState(world, iblockstate4, 4, 5, 2, box);
                this.setBlockState(world, iblockstate4, 4, 5, 3, box);
                this.setBlockState(world, iblockstate4, 0, 5, 1, box);
                this.setBlockState(world, iblockstate4, 0, 5, 2, box);
                this.setBlockState(world, iblockstate4, 0, 5, 3, box);
            }

            if (this.isRoofAccessible)
            {
                IBlockState iblockstate5 = Blocks.LADDER.getDefaultState().withProperty(BlockLadder.FACING, EnumFacing.SOUTH);
                this.setBlockState(world, iblockstate5, 3, 1, 3, box);
                this.setBlockState(world, iblockstate5, 3, 2, 3, box);
                this.setBlockState(world, iblockstate5, 3, 3, 3, box);
                this.setBlockState(world, iblockstate5, 3, 4, 3, box);
            }

            this.placeTorch(world, EnumFacing.NORTH, 2, 3, 1, box);

            for (int j = 0; j < 5; ++j)
            {
                for (int i = 0; i < 5; ++i)
                {
                    this.clearCurrentPositionBlocksUpwards(world, i, 6, j, box);
                    this.replaceAirAndLiquidDownwards(world, iblockstate, i, -1, j, box);
                }
            }
            this.spawnVillagers(world, box, 1, 1, 2, 1);
            return true;
        }
    }

    public static class Path extends Road
    {
        private int length;

        public Path() {}

        public Path(Start start, int type, StructureBoundingBox box, EnumFacing facing)
        {
            super(start, type);
            this.setCoordBaseMode(facing);
            this.boundingBox = box;
            this.length = Math.max(box.getXSize(), box.getZSize());
        }

        @Override
        protected void writeStructureToNBT(NBTTagCompound nbt)
        {
            super.writeStructureToNBT(nbt);
            nbt.setInteger("Length", this.length);
        }

        @Override
        protected void readStructureFromNBT(NBTTagCompound nbt, TemplateManager manager)
        {
            super.readStructureFromNBT(nbt, manager);
            this.length = nbt.getInteger("Length");
        }

        @Override
        public void buildComponent(StructureComponent component, List<StructureComponent> list, Random rand)
        {
            boolean flag = false;

            for (int i = rand.nextInt(5); i < this.length - 8; i += 2 + rand.nextInt(5))
            {
                StructureComponent structurecomponent = this.getNextComponentNN((Start)component, list, rand, 0, i);

                if (structurecomponent != null)
                {
                    i += Math.max(structurecomponent.getBoundingBox().getXSize(), structurecomponent.getBoundingBox().getZSize());
                    flag = true;
                }
            }

            for (int j = rand.nextInt(5); j < this.length - 8; j += 2 + rand.nextInt(5))
            {
                StructureComponent structurecomponent1 = this.getNextComponentPP((Start)component, list, rand, 0, j);

                if (structurecomponent1 != null)
                {
                    j += Math.max(structurecomponent1.getBoundingBox().getXSize(), structurecomponent1.getBoundingBox().getZSize());
                    flag = true;
                }
            }

            EnumFacing facing = this.getCoordBaseMode();

            if (flag && rand.nextInt(3) > 0 && facing != null)
            {
                switch (facing)
                {
                case NORTH:
                default:
                    this.generateAndAddRoadPiece((Start)component, list, rand, this.boundingBox.minX - 1, this.boundingBox.minY, this.boundingBox.minZ, EnumFacing.WEST, this.getComponentType());
                    break;
                case SOUTH:
                    this.generateAndAddRoadPiece((Start)component, list, rand, this.boundingBox.minX - 1, this.boundingBox.minY, this.boundingBox.maxZ - 2, EnumFacing.WEST, this.getComponentType());
                    break;
                case WEST:
                    this.generateAndAddRoadPiece((Start)component, list, rand, this.boundingBox.minX, this.boundingBox.minY, this.boundingBox.minZ - 1, EnumFacing.NORTH, this.getComponentType());
                    break;
                case EAST:
                    this.generateAndAddRoadPiece((Start)component, list, rand, this.boundingBox.maxX - 2, this.boundingBox.minY, this.boundingBox.minZ - 1, EnumFacing.NORTH, this.getComponentType());
                }
            }

            if (flag && rand.nextInt(3) > 0 && facing != null)
            {
                switch (facing)
                {
                case NORTH:
                default:
                    this.generateAndAddRoadPiece((Start)component, list, rand, this.boundingBox.maxX + 1, this.boundingBox.minY, this.boundingBox.minZ, EnumFacing.EAST, this.getComponentType());
                    break;
                case SOUTH:
                    this.generateAndAddRoadPiece((Start)component, list, rand, this.boundingBox.maxX + 1, this.boundingBox.minY, this.boundingBox.maxZ - 2, EnumFacing.EAST, this.getComponentType());
                    break;
                case WEST:
                    this.generateAndAddRoadPiece((Start)component, list, rand, this.boundingBox.minX, this.boundingBox.minY, this.boundingBox.maxZ + 1, EnumFacing.SOUTH, this.getComponentType());
                    break;
                case EAST:
                    this.generateAndAddRoadPiece((Start)component, list, rand, this.boundingBox.maxX - 2, this.boundingBox.minY, this.boundingBox.maxZ + 1, EnumFacing.SOUTH, this.getComponentType());
                }
            }
        }

        public static StructureBoundingBox findPieceBox(Start start, List<StructureComponent> list, Random rand, int x, int y, int z, EnumFacing facing)
        {
            for (int i = 7 * MathHelper.getInt(rand, 3, 5); i >= 7; i -= 7)
            {
                StructureBoundingBox box = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, 3, 3, i, facing);

                if (StructureComponent.findIntersecting(list, box) == null)
                {
                    return box;
                }
            }
            return null;
        }

        @Override
        public boolean addComponentParts(World world, Random rand, StructureBoundingBox box)
        {
            IBlockState iblockstate = this.getBiomeSpecificBlockState(MPBlocks.INFECTED_GRASS_PATH.getDefaultState());
            IBlockState iblockstate1 = this.getBiomeSpecificBlockState(MPBlocks.INFECTED_OAK_PLANKS.getDefaultState());

            for (int i = this.boundingBox.minX; i <= this.boundingBox.maxX; ++i)
            {
                for (int j = this.boundingBox.minZ; j <= this.boundingBox.maxZ; ++j)
                {
                    BlockPos blockpos = new BlockPos(i, 64, j);

                    if (box.isVecInside(blockpos))
                    {
                        blockpos = world.getTopSolidOrLiquidBlock(blockpos).down();

                        if (blockpos.getY() < world.getSeaLevel())
                        {
                            blockpos = new BlockPos(blockpos.getX(), world.getSeaLevel() - 1, blockpos.getZ());
                        }

                        while (blockpos.getY() >= world.getSeaLevel() - 1)
                        {
                            IBlockState iblockstate4 = world.getBlockState(blockpos);

                            if ((iblockstate4.getBlock() == MPBlocks.INFECTED_GRASS_BLOCK || iblockstate4.getBlock() == MPBlocks.GREEN_VEIN_GRASS_BLOCK) && world.isAirBlock(blockpos.up()))
                            {
                                world.setBlockState(blockpos, iblockstate, 2);
                                break;
                            }
                            if (iblockstate4.getMaterial().isLiquid())
                            {
                                world.setBlockState(blockpos, iblockstate1, 2);
                                break;
                            }
                            blockpos = blockpos.down();
                        }
                    }
                }
            }
            return true;
        }
    }

    public static class PieceWeight
    {
        public Class<? extends Village> villagePieceClass;
        public final int villagePieceWeight;
        public int villagePiecesSpawned;
        public int villagePiecesLimit;

        public PieceWeight(Class<? extends Village> clazz, int weight, int limit)
        {
            this.villagePieceClass = clazz;
            this.villagePieceWeight = weight;
            this.villagePiecesLimit = limit;
        }

        public boolean canSpawnMoreVillagePiecesOfType(int type)
        {
            return this.villagePiecesLimit == 0 || this.villagePiecesSpawned < this.villagePiecesLimit;
        }

        public boolean canSpawnMoreVillagePieces()
        {
            return this.villagePiecesLimit == 0 || this.villagePiecesSpawned < this.villagePiecesLimit;
        }
    }

    public abstract static class Road extends Village
    {
        public Road() {}

        protected Road(Start start, int type)
        {
            super(start, type);
        }
    }

    public static class Start extends Well
    {
        public BiomeProvider biomeProvider;
        public int terrainType;
        public PieceWeight structVillagePieceWeight;
        public List<PieceWeight> structureVillageWeightedPieceList;
        public List<StructureComponent> pendingHouses = new ArrayList<>();
        public List<StructureComponent> pendingRoads = new ArrayList<>();
        public Biome biome;

        public Start() {}

        public Start(BiomeProvider biomeProvider, Random rand, int x, int z, List<PieceWeight> list, int type)
        {
            super((Start)null, 0, rand, x, z);
            this.biomeProvider = biomeProvider;
            this.structureVillageWeightedPieceList = list;
            this.terrainType = type;
            Biome biome = biomeProvider.getBiome(new BlockPos(x, 0, z), MPBiomes.INFECTED_OCEAN);
            this.biome = biome;
            this.startPiece = this;

            if (biome == MPBiomes.INFECTED_DESERT)
            {
                this.structureType = 1;
            }
            else if (biome == MPBiomes.GREEN_VEIN_FIELDS)
            {
                this.structureType = 2;
            }
            this.setVillageType(this.structureType);
        }
    }

    public static class Torch extends Village
    {
        public Torch() {}

        public Torch(Start start, int type, StructureBoundingBox box, EnumFacing facing)
        {
            super(start, type);
            this.setCoordBaseMode(facing);
            this.boundingBox = box;
        }

        public static StructureBoundingBox findPieceBox(Start start, List<StructureComponent> list, int x, int y, int z, EnumFacing facing)
        {
            StructureBoundingBox box = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, 3, 4, 2, facing);
            return StructureComponent.findIntersecting(list, box) != null ? null : box;
        }

        @Override
        public boolean addComponentParts(World world, Random rand, StructureBoundingBox box)
        {
            if (this.averageGroundLvl < 0)
            {
                this.averageGroundLvl = this.getAverageGroundLevel(world, box);

                if (this.averageGroundLvl < 0)
                {
                    return true;
                }
                this.boundingBox.offset(0, this.averageGroundLvl - this.boundingBox.maxY + 4 - 1, 0);
            }
            IBlockState iblockstate = this.getBiomeSpecificBlockState(MPBlocks.INFECTED_OAK_FENCE.getDefaultState());
            this.fillWithBlocks(world, box, 0, 0, 0, 2, 3, 1, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
            this.setBlockState(world, iblockstate, 1, 0, 0, box);
            this.setBlockState(world, iblockstate, 1, 1, 0, box);
            this.setBlockState(world, iblockstate, 1, 2, 0, box);
            this.setBlockState(world, MPBlocks.INFECTED_DARK_PRISMARINE.getDefaultState(), 1, 3, 0, box);
            this.placeTorch(world, EnumFacing.EAST, 2, 3, 0, box);
            this.placeTorch(world, EnumFacing.NORTH, 1, 3, 1, box);
            this.placeTorch(world, EnumFacing.WEST, 0, 3, 0, box);
            this.placeTorch(world, EnumFacing.SOUTH, 1, 3, -1, box);
            return true;
        }
    }

    public abstract static class Village extends StructureComponent
    {
        protected int averageGroundLvl = -1;
        private int villagersSpawned;
        protected int structureType;
        protected Start startPiece;

        public Village() {}

        protected Village(Start start, int type)
        {
            super(type);

            if (start != null)
            {
                this.structureType = start.structureType;
                this.startPiece = start;
            }
        }

        @Override
        protected void writeStructureToNBT(NBTTagCompound nbt)
        {
            nbt.setInteger("HPos", this.averageGroundLvl);
            nbt.setInteger("VCount", this.villagersSpawned);
            nbt.setByte("Type", (byte)this.structureType);
        }

        @Override
        protected void readStructureFromNBT(NBTTagCompound nbt, TemplateManager manager)
        {
            this.averageGroundLvl = nbt.getInteger("HPos");
            this.villagersSpawned = nbt.getInteger("VCount");
            this.structureType = nbt.getByte("Type");
        }

        @Override
        protected void setBlockState(World world, IBlockState state, int x, int y, int z, StructureBoundingBox box)
        {
            IBlockState iblockstate = this.getBiomeSpecificBlockState(state);
            super.setBlockState(world, iblockstate, x, y, z, box);
        }

        @Override
        protected void fillWithBlocks(World world, StructureBoundingBox box, int xMin, int yMin, int zMin, int xMax, int yMax, int zMax, IBlockState boundaryBlockState, IBlockState insideBlockState, boolean existingOnly)
        {
            IBlockState iblockstate = this.getBiomeSpecificBlockState(boundaryBlockState);
            IBlockState iblockstate1 = this.getBiomeSpecificBlockState(insideBlockState);
            super.fillWithBlocks(world, box, xMin, yMin, zMin, xMax, yMax, zMax, iblockstate, iblockstate1, existingOnly);
        }

        @Override
        protected void replaceAirAndLiquidDownwards(World world, IBlockState state, int x, int y, int z, StructureBoundingBox box)
        {
            state = this.getBiomeSpecificBlockState(state);
            int i = this.getXWithOffset(x, z);
            int j = this.getYWithOffset(y);
            int k = this.getZWithOffset(x, z);
            BlockPos pos = new BlockPos(i, j, k);

            if (box.isVecInside(pos))
            {
                while ((world.isAirBlock(pos) || world.getBlockState(pos).getBlock() == MPBlocks.INFECTED_WATER_FLUID_BLOCK || world.getBlockState(pos).getBlock() == MPBlocks.PURIFIED_WATER_FLUID_BLOCK) && j > 1)
                {
                    world.setBlockState(pos, state, 2);
                    --j;
                }
            }
        }

        @Override
        protected boolean generateChest(World world, StructureBoundingBox box, Random rand, int x, int y, int z, ResourceLocation loot)
        {
            BlockPos pos = new BlockPos(this.getXWithOffset(x, z), this.getYWithOffset(y), this.getZWithOffset(x, z));
            BlockChestMP chest = this.structureType == 2 ? MPBlocks.ALIEN_BERRY_CHEST : MPBlocks.INFECTED_CHEST;

            if (box.isVecInside(pos) && world.getBlockState(pos).getBlock() != chest)
            {
                IBlockState iblockstate = chest.getDefaultState();
                world.setBlockState(pos, chest.correctFacing(world, pos, iblockstate), 2);
                TileEntity tileentity = world.getTileEntity(pos);

                if (tileentity instanceof TileEntityChestMP)
                {
                    ((TileEntityChestMP)tileentity).setLootTable(loot, rand.nextLong());
                }
                return true;
            }
            else
            {
                return false;
            }
        }

        protected StructureComponent getNextComponentNN(Start start, List<StructureComponent> component, Random rand, int x, int z)
        {
            EnumFacing facing = this.getCoordBaseMode();

            if (facing != null)
            {
                switch (facing)
                {
                case NORTH:
                default:
                    return this.generateAndAddComponent(start, component, rand, this.boundingBox.minX - 1, this.boundingBox.minY + x, this.boundingBox.minZ + z, EnumFacing.WEST, this.getComponentType());
                case SOUTH:
                    return this.generateAndAddComponent(start, component, rand, this.boundingBox.minX - 1, this.boundingBox.minY + x, this.boundingBox.minZ + z, EnumFacing.WEST, this.getComponentType());
                case WEST:
                    return this.generateAndAddComponent(start, component, rand, this.boundingBox.minX + z, this.boundingBox.minY + x, this.boundingBox.minZ - 1, EnumFacing.NORTH, this.getComponentType());
                case EAST:
                    return this.generateAndAddComponent(start, component, rand, this.boundingBox.minX + z, this.boundingBox.minY + x, this.boundingBox.minZ - 1, EnumFacing.NORTH, this.getComponentType());
                }
            }
            else
            {
                return null;
            }
        }

        protected StructureComponent getNextComponentPP(Start start, List<StructureComponent> component, Random rand, int x, int z)
        {
            EnumFacing facing = this.getCoordBaseMode();

            if (facing != null)
            {
                switch (facing)
                {
                case NORTH:
                default:
                    return this.generateAndAddComponent(start, component, rand, this.boundingBox.maxX + 1, this.boundingBox.minY + x, this.boundingBox.minZ + z, EnumFacing.EAST, this.getComponentType());
                case SOUTH:
                    return this.generateAndAddComponent(start, component, rand, this.boundingBox.maxX + 1, this.boundingBox.minY + x, this.boundingBox.minZ + z, EnumFacing.EAST, this.getComponentType());
                case WEST:
                    return this.generateAndAddComponent(start, component, rand, this.boundingBox.minX + z, this.boundingBox.minY + x, this.boundingBox.maxZ + 1, EnumFacing.SOUTH, this.getComponentType());
                case EAST:
                    return this.generateAndAddComponent(start, component, rand, this.boundingBox.minX + z, this.boundingBox.minY + x, this.boundingBox.maxZ + 1, EnumFacing.SOUTH, this.getComponentType());
                }
            }
            else
            {
                return null;
            }
        }

        protected int getAverageGroundLevel(World world, StructureBoundingBox box)
        {
            int i = 0;
            int j = 0;
            BlockPos.MutableBlockPos mutableblockpos = new BlockPos.MutableBlockPos();

            for (int k = this.boundingBox.minZ; k <= this.boundingBox.maxZ; ++k)
            {
                for (int l = this.boundingBox.minX; l <= this.boundingBox.maxX; ++l)
                {
                    mutableblockpos.setPos(l, 64, k);

                    if (box.isVecInside(mutableblockpos))
                    {
                        i += Math.max(world.getTopSolidOrLiquidBlock(mutableblockpos).getY(), world.provider.getAverageGroundLevel() - 1);
                        ++j;
                    }
                }
            }
            if (j == 0)
            {
                return -1;
            }
            else
            {
                return i / j;
            }
        }

        protected boolean canVillageGoDeeper(StructureBoundingBox box)
        {
            return box != null && box.minY > 10;
        }

        protected void spawnVillagers(World world, StructureBoundingBox box, int x, int y, int z, int count)
        {
            if (this.villagersSpawned < count)
            {
                for (int i = this.villagersSpawned; i < count; ++i)
                {
                    int j = this.getXWithOffset(x + i, z);
                    int k = this.getYWithOffset(y);
                    int l = this.getZWithOffset(x + i, z);

                    if (!box.isVecInside(new BlockPos(j, k, l)))
                    {
                        break;
                    }
                    ++this.villagersSpawned;
                    EntityNibiruVillager villager = new EntityNibiruVillager(world);
                    villager.setLocationAndAngles(j + 0.5D, k, l + 0.5D, 0.0F, 0.0F);
                    villager.onInitialSpawn(world.getDifficultyForLocation(new BlockPos(villager)), null);
                    villager.setProfession(this.chooseProfession(i, villager.getProfession()));
                    world.spawnEntity(villager);
                }
            }
        }

        protected int chooseProfession(int villagersSpawned, int currentVillagerProfession)
        {
            return currentVillagerProfession;
        }

        @SuppressWarnings("deprecation")
        protected IBlockState getBiomeSpecificBlockState(IBlockState state)
        {
            Block block = state.getBlock();

            if (this.structureType == 1)
            {
                if (block == MPBlocks.NIBIRU_COBBLESTONE || block == MPBlocks.INFECTED_OAK_LOG || block == MPBlocks.INFECTED_GRAVEL)
                {
                    return MPBlocks.INFECTED_SANDSTONE.getDefaultState();
                }
                else if (block == MPBlocks.NIBIRU_VEIN_COBBLESTONE)
                {
                    return MPBlocks.INFECTED_CHISELED_SANDSTONE.getDefaultState();
                }
                else if (block == MPBlocks.INFECTED_OAK_PLANKS)
                {
                    return MPBlocks.INFECTED_CUT_SANDSTONE.getDefaultState();
                }
                else if (block == MPBlocks.INFECTED_OAK_STAIRS || block == MPBlocks.NIBIRU_COBBLESTONE_STAIRS)
                {
                    return MPBlocks.INFECTED_SANDSTONE_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, state.getValue(BlockStairs.FACING));
                }
            }
            else if (this.structureType == 2)
            {
                if (block == MPBlocks.NIBIRU_COBBLESTONE || block == MPBlocks.NIBIRU_VEIN_COBBLESTONE || block == MPBlocks.INFECTED_PRISMARINE || block == MPBlocks.INFECTED_VINES_DIRT || block == MPBlocks.INFECTED_DARK_PRISMARINE)
                {
                    return MPBlocks.TERRASTONE.getDefaultState();
                }
                else if (block == MPBlocks.INFECTED_OAK_PRESSURE_PLATE)
                {
                    return MPBlocks.ALIEN_BERRY_OAK_PRESSURE_PLATE.getDefaultState();
                }
                else if (block == MPBlocks.INFECTED_GRASS_PATH)
                {
                    return MPBlocks.GREEN_VEIN_GRASS_PATH.getDefaultState();
                }
                else if (block == MPBlocks.INFECTED_VINES)
                {
                    return Blocks.AIR.getDefaultState();
                }
                else if (block == MPBlocks.INFECTED_OAK_LOG)
                {
                    return MPBlocks.ALIEN_BERRY_OAK_LOG.getDefaultState();
                }
                else if (block == MPBlocks.INFECTED_OAK_FENCE)
                {
                    return MPBlocks.ALIEN_BERRY_OAK_FENCE.getDefaultState();
                }
                else if (block == MPBlocks.INFECTED_OAK_PLANKS)
                {
                    return MPBlocks.ALIEN_BERRY_OAK_PLANKS.getDefaultState();
                }
                else if (block == MPBlocks.INFECTED_OAK_BOOKSHELF)
                {
                    return MPBlocks.ALIEN_BERRY_OAK_BOOKSHELF.getDefaultState();
                }
                else if (block == MPBlocks.INFECTED_CRAFTING_TABLE)
                {
                    return MPBlocks.ALIEN_BERRY_CRAFTING_TABLE.getDefaultState();
                }
                else if (block == MPBlocks.INFECTED_OAK_STAIRS)
                {
                    return MPBlocks.ALIEN_BERRY_OAK_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, state.getValue(BlockStairs.FACING));
                }
                else if (block == MPBlocks.NIBIRU_COBBLESTONE_STAIRS)
                {
                    return MPBlocks.TERRASTONE_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, state.getValue(BlockStairs.FACING));
                }
                else if (block == MPBlocks.INFECTED_WATER_FLUID_BLOCK)
                {
                    return MPBlocks.PURIFIED_WATER_FLUID_BLOCK.getDefaultState();
                }
                else if (block == MPBlocks.INFECTED_GRAVEL)
                {
                    return MPBlocks.PURIFIED_GRAVEL.getDefaultState();
                }
                else if (block == MPBlocks.INFECTED_STONE_BRICKS_SLAB)
                {
                    return MPBlocks.TERRASTONE_SLAB.getDefaultState();
                }
                else if (block == MPBlocks.DOUBLE_INFECTED_STONE_BRICKS_SLAB)
                {
                    return MPBlocks.DOUBLE_TERRASTONE_SLAB.getDefaultState();
                }
                else if (block == MPBlocks.INFECTED_FURNACE)
                {
                    return MPBlocks.TERRASTONE_FURNACE.getDefaultState();
                }
            }
            return state;
        }

        protected BlockDoor getDoor()
        {
            switch (this.structureType)
            {
            case 2:
                return MPBlocks.ALIEN_BERRY_OAK_DOOR;
            default:
                return MPBlocks.INFECTED_OAK_DOOR;
            }
        }

        protected void createVillageDoor(World world, StructureBoundingBox box, Random rand, int x, int y, int z)
        {
            this.generateDoor(world, box, rand, x, y, z, EnumFacing.NORTH, this.getDoor());
        }

        protected void placeTorch(World world, EnumFacing facing, int x, int y, int z, StructureBoundingBox box)
        {
            Block torch = this.structureType == 2 ? GCBlocks.glowstoneTorch : MPBlocks.INFECTED_TORCH;
            this.setBlockState(world, torch.getDefaultState().withProperty(this.structureType == 2 ? BlockUnlitTorch.FACING : BlockTorch.FACING, facing), x, y, z, box);
        }

        protected void setVillageType(int type)
        {
            this.structureType = type;
        }

        private int updatePieceWeight(List<PieceWeight> list)
        {
            boolean flag = false;
            int i = 0;

            for (PieceWeight piece : list)
            {
                if (piece.villagePiecesLimit > 0 && piece.villagePiecesSpawned < piece.villagePiecesLimit)
                {
                    flag = true;
                }
                i += piece.villagePieceWeight;
            }
            return flag ? i : -1;
        }

        private Village findAndCreateComponentFactory(Start start, PieceWeight weight, List<StructureComponent> component, Random rand, int x, int y, int z, EnumFacing facing, int type)
        {
            Class<? extends Village> oclass = weight.villagePieceClass;
            Village village = null;

            if (oclass == House4Garden.class)
            {
                village = this.createHouse4GardenPiece(start, component, rand, x, y, z, facing, type);
            }
            else if (oclass == Church.class)
            {
                village = this.createChurchPiece(start, component, x, y, z, facing, type);
            }
            else if (oclass == House1.class)
            {
                village = this.createHouse1Piece(start, component, x, y, z, facing, type);
            }
            else if (oclass == WoodHut.class)
            {
                village = this.createWoodHutPiece(start, component, rand, x, y, z, facing, type);
            }
            else if (oclass == Hall.class)
            {
                village = this.createHallPiece(start, component, x, y, z, facing, type);
            }
            else if (oclass == Field1.class)
            {
                village = this.createField1Piece(start, component, rand, x, y, z, facing, type);
            }
            else if (oclass == Field2.class)
            {
                village = this.createField2Piece(start, component, rand, x, y, z, facing, type);
            }
            else if (oclass == House2.class)
            {
                village = this.createHouse2Piece(start, component, x, y, z, facing, type);
            }
            else if (oclass == House3.class)
            {
                village = this.createHouse3Piece(start, component, x, y, z, facing, type);
            }
            return village;
        }

        private Village generateComponent(Start start, List<StructureComponent> component, Random rand, int x, int y, int z, EnumFacing facing, int type)
        {
            int i = this.updatePieceWeight(start.structureVillageWeightedPieceList);

            if (i <= 0)
            {
                return null;
            }
            else
            {
                int j = 0;

                while (j < 5)
                {
                    ++j;
                    int k = rand.nextInt(i);

                    for (PieceWeight piece : start.structureVillageWeightedPieceList)
                    {
                        k -= piece.villagePieceWeight;

                        if (k < 0)
                        {
                            if (!piece.canSpawnMoreVillagePiecesOfType(type) || piece == start.structVillagePieceWeight && start.structureVillageWeightedPieceList.size() > 1)
                            {
                                break;
                            }

                            Village village = this.findAndCreateComponentFactory(start, piece, component, rand, x, y, z, facing, type);

                            if (village != null)
                            {
                                ++piece.villagePiecesSpawned;
                                start.structVillagePieceWeight = piece;

                                if (!piece.canSpawnMoreVillagePieces())
                                {
                                    start.structureVillageWeightedPieceList.remove(piece);
                                }
                                return village;
                            }
                        }
                    }
                }

                StructureBoundingBox box = Torch.findPieceBox(start, component, x, y, z, facing);

                if (box != null)
                {
                    return new Torch(start, type, box, facing);
                }
                else
                {
                    return null;
                }
            }
        }

        private StructureComponent generateAndAddComponent(Start start, List<StructureComponent> component, Random rand, int x, int y, int z, EnumFacing facing, int type)
        {
            if (type > 50)
            {
                return null;
            }
            else if (Math.abs(x - start.getBoundingBox().minX) <= 112 && Math.abs(z - start.getBoundingBox().minZ) <= 112)
            {
                StructureComponent structurecomponent = this.generateComponent(start, component, rand, x, y, z, facing, type + 1);

                if (structurecomponent != null)
                {
                    component.add(structurecomponent);
                    start.pendingHouses.add(structurecomponent);
                    return structurecomponent;
                }
                else
                {
                    return null;
                }
            }
            else
            {
                return null;
            }
        }

        protected StructureComponent generateAndAddRoadPiece(Start start, List<StructureComponent> list, Random rand, int x, int y, int z, EnumFacing facing, int type)
        {
            if (type > 3 + start.terrainType)
            {
                return null;
            }
            else if (Math.abs(x - start.getBoundingBox().minX) <= 112 && Math.abs(z - start.getBoundingBox().minZ) <= 112)
            {
                StructureBoundingBox box = Path.findPieceBox(start, list, rand, x, y, z, facing);

                if (box != null && box.minY > 10)
                {
                    StructureComponent component = new Path(start, type, box, facing);
                    list.add(component);
                    start.pendingRoads.add(component);
                    return component;
                }
                else
                {
                    return null;
                }
            }
            else
            {
                return null;
            }
        }

        private House4Garden createHouse4GardenPiece(Start start, List<StructureComponent> list, Random rand, int x, int y, int z, EnumFacing facing, int type)
        {
            StructureBoundingBox box = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, 5, 6, 5, facing);
            return StructureComponent.findIntersecting(list, box) != null ? null : new House4Garden(start, type, rand, box, facing);
        }

        private Church createChurchPiece(Start start, List<StructureComponent> list, int x, int y, int z, EnumFacing facing, int type)
        {
            StructureBoundingBox box = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, 5, 12, 9, facing);
            return this.canVillageGoDeeper(box) && StructureComponent.findIntersecting(list, box) == null ? new Church(start, type, box, facing) : null;
        }

        private House1 createHouse1Piece(Start start, List<StructureComponent> list, int x, int y, int z, EnumFacing facing, int type)
        {
            StructureBoundingBox box = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, 9, 9, 6, facing);
            return this.canVillageGoDeeper(box) && StructureComponent.findIntersecting(list, box) == null ? new House1(start, type, box, facing) : null;
        }

        private WoodHut createWoodHutPiece(Start start, List<StructureComponent> list, Random rand, int x, int y, int z, EnumFacing facing, int type)
        {
            StructureBoundingBox box = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, 4, 6, 5, facing);
            return this.canVillageGoDeeper(box) && StructureComponent.findIntersecting(list, box) == null ? new WoodHut(start, type, rand, box, facing) : null;
        }

        private Hall createHallPiece(Start start, List<StructureComponent> list, int x, int y, int z, EnumFacing facing, int type)
        {
            StructureBoundingBox box = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, 9, 7, 11, facing);
            return this.canVillageGoDeeper(box) && StructureComponent.findIntersecting(list, box) == null ? new Hall(start, type, box, facing) : null;
        }

        private Field1 createField1Piece(Start start, List<StructureComponent> list, Random rand, int x, int y, int z, EnumFacing facing, int type)
        {
            StructureBoundingBox box = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, 13, 4, 9, facing);
            return this.canVillageGoDeeper(box) && StructureComponent.findIntersecting(list, box) == null ? new Field1(start, type, rand, box, facing) : null;
        }

        private Field2 createField2Piece(Start start, List<StructureComponent> list, Random rand, int x, int y, int z, EnumFacing facing, int type)
        {
            StructureBoundingBox box = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, 7, 4, 9, facing);
            return this.canVillageGoDeeper(box) && StructureComponent.findIntersecting(list, box) == null ? new Field2(start, type, rand, box, facing) : null;
        }

        private House2 createHouse2Piece(Start start, List<StructureComponent> list, int x, int y, int z, EnumFacing facing, int type)
        {
            StructureBoundingBox box = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, 10, 6, 7, facing);
            return this.canVillageGoDeeper(box) && StructureComponent.findIntersecting(list, box) == null ? new House2(start, type, box, facing) : null;
        }

        private House3 createHouse3Piece(Start start, List<StructureComponent> list, int x, int y, int z, EnumFacing facing, int type)
        {
            StructureBoundingBox box = StructureBoundingBox.getComponentToAddBoundingBox(x, y, z, 0, 0, 0, 9, 7, 12, facing);
            return this.canVillageGoDeeper(box) && StructureComponent.findIntersecting(list, box) == null ? new House3(start, type, box, facing) : null;
        }
    }

    public static class Well extends Village
    {
        public Well() {}

        public Well(Start start, int type, Random rand, int x, int z)
        {
            super(start, type);
            this.setCoordBaseMode(EnumFacing.Plane.HORIZONTAL.random(rand));

            if (this.getCoordBaseMode().getAxis() == EnumFacing.Axis.Z)
            {
                this.boundingBox = new StructureBoundingBox(x, 64, z, x + 6 - 1, 78, z + 6 - 1);
            }
            else
            {
                this.boundingBox = new StructureBoundingBox(x, 64, z, x + 6 - 1, 78, z + 6 - 1);
            }
        }

        @Override
        public void buildComponent(StructureComponent component, List<StructureComponent> list, Random rand)
        {
            this.generateAndAddRoadPiece((Start)component, list, rand, this.boundingBox.minX - 1, this.boundingBox.maxY - 4, this.boundingBox.minZ + 1, EnumFacing.WEST, this.getComponentType());
            this.generateAndAddRoadPiece((Start)component, list, rand, this.boundingBox.maxX + 1, this.boundingBox.maxY - 4, this.boundingBox.minZ + 1, EnumFacing.EAST, this.getComponentType());
            this.generateAndAddRoadPiece((Start)component, list, rand, this.boundingBox.minX + 1, this.boundingBox.maxY - 4, this.boundingBox.minZ - 1, EnumFacing.NORTH, this.getComponentType());
            this.generateAndAddRoadPiece((Start)component, list, rand, this.boundingBox.minX + 1, this.boundingBox.maxY - 4, this.boundingBox.maxZ + 1, EnumFacing.SOUTH, this.getComponentType());
        }

        @Override
        public boolean addComponentParts(World world, Random rand, StructureBoundingBox box)
        {
            if (this.averageGroundLvl < 0)
            {
                this.averageGroundLvl = this.getAverageGroundLevel(world, box);

                if (this.averageGroundLvl < 0)
                {
                    return true;
                }
                this.boundingBox.offset(0, this.averageGroundLvl - this.boundingBox.maxY + 3, 0);
            }

            IBlockState iblockstate = this.getBiomeSpecificBlockState(MPBlocks.NIBIRU_COBBLESTONE.getDefaultState());
            IBlockState iblockstate1 = this.getBiomeSpecificBlockState(MPBlocks.INFECTED_OAK_FENCE.getDefaultState());
            this.fillWithBlocks(world, box, 1, 0, 1, 4, 12, 4, iblockstate, MPBlocks.INFECTED_WATER_FLUID_BLOCK.getDefaultState(), false);
            this.setBlockState(world, Blocks.AIR.getDefaultState(), 2, 12, 2, box);
            this.setBlockState(world, Blocks.AIR.getDefaultState(), 3, 12, 2, box);
            this.setBlockState(world, Blocks.AIR.getDefaultState(), 2, 12, 3, box);
            this.setBlockState(world, Blocks.AIR.getDefaultState(), 3, 12, 3, box);
            this.setBlockState(world, iblockstate1, 1, 13, 1, box);
            this.setBlockState(world, iblockstate1, 1, 14, 1, box);
            this.setBlockState(world, iblockstate1, 4, 13, 1, box);
            this.setBlockState(world, iblockstate1, 4, 14, 1, box);
            this.setBlockState(world, iblockstate1, 1, 13, 4, box);
            this.setBlockState(world, iblockstate1, 1, 14, 4, box);
            this.setBlockState(world, iblockstate1, 4, 13, 4, box);
            this.setBlockState(world, iblockstate1, 4, 14, 4, box);
            this.fillWithRandomizedBlocks(world, box, 1, 15, 1, 4, 15, 4, false, rand, villageStones);

            for (int i = 0; i <= 5; ++i)
            {
                for (int j = 0; j <= 5; ++j)
                {
                    if (j == 0 || j == 5 || i == 0 || i == 5)
                    {
                        this.setBlockState(world, MPBlocks.NIBIRU_COBBLESTONE.getDefaultState(), j, 11, i, box);
                        this.clearCurrentPositionBlocksUpwards(world, j, 12, i, box);
                    }
                }
            }
            return true;
        }
    }

    public static class WoodHut extends Village
    {
        private boolean isTallHouse;
        private int tablePosition;

        public WoodHut() {}

        public WoodHut(Start start, int type, Random rand, StructureBoundingBox box, EnumFacing facing)
        {
            super(start, type);
            this.setCoordBaseMode(facing);
            this.boundingBox = box;
            this.isTallHouse = rand.nextBoolean();
            this.tablePosition = rand.nextInt(3);
        }

        @Override
        protected void writeStructureToNBT(NBTTagCompound nbt)
        {
            super.writeStructureToNBT(nbt);
            nbt.setInteger("T", this.tablePosition);
            nbt.setBoolean("C", this.isTallHouse);
        }

        @Override
        protected void readStructureFromNBT(NBTTagCompound nbt, TemplateManager manager)
        {
            super.readStructureFromNBT(nbt, manager);
            this.tablePosition = nbt.getInteger("T");
            this.isTallHouse = nbt.getBoolean("C");
        }

        @Override
        public boolean addComponentParts(World world, Random rand, StructureBoundingBox box)
        {
            if (this.averageGroundLvl < 0)
            {
                this.averageGroundLvl = this.getAverageGroundLevel(world, box);

                if (this.averageGroundLvl < 0)
                {
                    return true;
                }
                this.boundingBox.offset(0, this.averageGroundLvl - this.boundingBox.maxY + 6 - 1, 0);
            }

            IBlockState iblockstate = this.getBiomeSpecificBlockState(MPBlocks.NIBIRU_COBBLESTONE.getDefaultState());
            IBlockState iblockstate2 = this.getBiomeSpecificBlockState(MPBlocks.NIBIRU_COBBLESTONE_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, EnumFacing.NORTH));
            IBlockState iblockstate3 = this.getBiomeSpecificBlockState(MPBlocks.INFECTED_OAK_LOG.getDefaultState());
            IBlockState iblockstate4 = this.getBiomeSpecificBlockState(MPBlocks.INFECTED_OAK_FENCE.getDefaultState());
            this.fillWithBlocks(world, box, 1, 1, 1, 3, 5, 4, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);
            this.fillWithRandomizedBlocks(world, box, 0, 0, 0, 3, 0, 4, false, rand, villageStones);
            this.fillWithRandomizedBlocks(world, box, 1, 0, 1, 2, 0, 3, false, rand, villageDirts);

            if (this.isTallHouse)
            {
                this.fillWithRandomizedBlocks(world, box, 1, 4, 1, 2, 4, 3, false, rand, villageLogs);
            }
            else
            {
                this.fillWithRandomizedBlocks(world, box, 1, 5, 1, 2, 5, 3, false, rand, villageLogs);
            }

            this.setBlockState(world, iblockstate3, 1, 4, 0, box);
            this.setBlockState(world, iblockstate3, 2, 4, 0, box);
            this.setBlockState(world, iblockstate3, 1, 4, 4, box);
            this.setBlockState(world, iblockstate3, 2, 4, 4, box);
            this.setBlockState(world, iblockstate3, 0, 4, 1, box);
            this.setBlockState(world, iblockstate3, 0, 4, 2, box);
            this.setBlockState(world, iblockstate3, 0, 4, 3, box);
            this.setBlockState(world, iblockstate3, 3, 4, 1, box);
            this.setBlockState(world, iblockstate3, 3, 4, 2, box);
            this.setBlockState(world, iblockstate3, 3, 4, 3, box);
            this.fillWithRandomizedBlocks(world, box, 0, 1, 0, 0, 3, 0, false, rand, villageLogs);
            this.fillWithRandomizedBlocks(world, box, 3, 1, 0, 3, 3, 0, false, rand, villageLogs);
            this.fillWithRandomizedBlocks(world, box, 0, 1, 4, 0, 3, 4, false, rand, villageLogs);
            this.fillWithRandomizedBlocks(world, box, 3, 1, 4, 3, 3, 4, false, rand, villageLogs);
            this.fillWithRandomizedBlocks(world, box, 0, 1, 1, 0, 3, 3, false, rand, villagePlanks);
            this.fillWithRandomizedBlocks(world, box, 3, 1, 1, 3, 3, 3, false, rand, villagePlanks);
            this.fillWithRandomizedBlocks(world, box, 1, 1, 0, 2, 3, 0, false, rand, villagePlanks);
            this.fillWithRandomizedBlocks(world, box, 1, 1, 4, 2, 3, 4, false, rand, villagePlanks);
            this.setBlockState(world, rand.nextInt(5) == 0 ? Blocks.AIR.getDefaultState() : Blocks.GLASS_PANE.getDefaultState(), 0, 2, 2, box);
            this.setBlockState(world, rand.nextInt(5) == 0 ? Blocks.AIR.getDefaultState() : Blocks.GLASS_PANE.getDefaultState(), 3, 2, 2, box);

            if (this.tablePosition > 0)
            {
                this.setBlockState(world, iblockstate4, this.tablePosition, 1, 3, box);
                this.setBlockState(world, MPBlocks.INFECTED_OAK_PRESSURE_PLATE.getDefaultState(), this.tablePosition, 2, 3, box);
            }

            this.setBlockState(world, Blocks.AIR.getDefaultState(), 1, 1, 0, box);
            this.setBlockState(world, Blocks.AIR.getDefaultState(), 1, 2, 0, box);
            this.createVillageDoor(world, box, rand, 1, 1, 0);

            if (this.getBlockStateFromPos(world, 1, 0, -1, box).getMaterial() == Material.AIR && this.getBlockStateFromPos(world, 1, -1, -1, box).getMaterial() != Material.AIR)
            {
                this.setBlockState(world, iblockstate2, 1, 0, -1, box);

                if (this.getBlockStateFromPos(world, 1, -1, -1, box).getBlock() == MPBlocks.INFECTED_GRASS_PATH)
                {
                    this.setBlockState(world, MPBlocks.INFECTED_GRASS_BLOCK.getDefaultState(), 1, -1, -1, box);
                }
            }

            for (int i = 0; i < 5; ++i)
            {
                for (int j = 0; j < 4; ++j)
                {
                    this.clearCurrentPositionBlocksUpwards(world, j, 6, i, box);
                    this.replaceAirAndLiquidDownwards(world, iblockstate, j, -1, i, box);
                }
            }
            this.spawnVillagers(world, box, 1, 1, 2, 1);
            return true;
        }
    }

    static class Stones extends StructureComponent.BlockSelector
    {
        private Stones() {}

        @SuppressWarnings("deprecation")
        @Override
        public void selectBlocks(Random rand, int x, int y, int z, boolean wall)
        {
            if (wall)
            {
                float f = rand.nextFloat();

                if (f < 0.05F)
                {
                    this.blockstate = Blocks.WEB.getDefaultState();
                }
                else if (f < 0.1F)
                {
                    this.blockstate = MPBlocks.INFECTED_VINES.getStateFromMeta(15);
                }
                else if (f < 0.125F)
                {
                    this.blockstate = Blocks.AIR.getDefaultState();
                }
                else if (f < 0.5F)
                {
                    this.blockstate = MPBlocks.NIBIRU_VEIN_COBBLESTONE.getDefaultState();
                }
                else
                {
                    this.blockstate = MPBlocks.NIBIRU_COBBLESTONE.getDefaultState();
                }
            }
            else
            {
                this.blockstate = Blocks.AIR.getDefaultState();
            }
        }
    }

    static class Planks extends StructureComponent.BlockSelector
    {
        private Planks() {}

        @SuppressWarnings("deprecation")
        @Override
        public void selectBlocks(Random rand, int x, int y, int z, boolean wall)
        {
            if (wall)
            {
                float f = rand.nextFloat();

                if (f < 0.05F)
                {
                    this.blockstate = Blocks.WEB.getDefaultState();
                }
                else if (f < 0.1F)
                {
                    this.blockstate = MPBlocks.INFECTED_VINES.getStateFromMeta(15);
                }
                else if (f < 0.125F)
                {
                    this.blockstate = Blocks.AIR.getDefaultState();
                }
                else if (f < 0.5F)
                {
                    this.blockstate = MPBlocks.INFECTED_OAK_PLANKS.getDefaultState();
                }
                else
                {
                    this.blockstate = MPBlocks.INFECTED_OAK_PLANKS.getDefaultState();
                }
            }
            else
            {
                this.blockstate = Blocks.AIR.getDefaultState();
            }
        }
    }

    static class Logs extends StructureComponent.BlockSelector
    {
        private Logs() {}

        @SuppressWarnings("deprecation")
        @Override
        public void selectBlocks(Random rand, int x, int y, int z, boolean wall)
        {
            if (wall)
            {
                float f = rand.nextFloat();

                if (f < 0.05F)
                {
                    this.blockstate = Blocks.WEB.getDefaultState();
                }
                else if (f < 0.1F)
                {
                    this.blockstate = MPBlocks.INFECTED_VINES.getStateFromMeta(15);
                }
                else if (f < 0.125F)
                {
                    this.blockstate = Blocks.AIR.getDefaultState();
                }
                else if (f < 0.5F)
                {
                    this.blockstate = MPBlocks.INFECTED_OAK_LOG.getDefaultState();
                }
                else
                {
                    this.blockstate = MPBlocks.INFECTED_OAK_LOG.getDefaultState();
                }
            }
            else
            {
                this.blockstate = Blocks.AIR.getDefaultState();
            }
        }
    }

    static class Dirts extends StructureComponent.BlockSelector
    {
        private Dirts() {}

        @Override
        public void selectBlocks(Random rand, int x, int y, int z, boolean wall)
        {
            if (wall)
            {
                float f = rand.nextFloat();

                if (f < 0.2F)
                {
                    this.blockstate = MPBlocks.INFECTED_COARSE_DIRT.getDefaultState();
                }
                else if (f < 0.4F)
                {
                    this.blockstate = MPBlocks.INFECTED_VINES_DIRT.getDefaultState();
                }
                else
                {
                    this.blockstate = MPBlocks.INFECTED_DIRT.getDefaultState();
                }
            }
            else
            {
                this.blockstate = Blocks.AIR.getDefaultState();
            }
        }
    }
}