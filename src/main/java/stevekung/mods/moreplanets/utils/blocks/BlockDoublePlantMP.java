package stevekung.mods.moreplanets.utils.blocks;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDoublePlant;
import net.minecraft.block.IGrowable;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.IShearable;
import stevekung.mods.moreplanets.init.MPBlocks;
import stevekung.mods.moreplanets.init.MPItems;

public class BlockDoublePlantMP extends BlockBushMP implements IGrowable, IShearable
{
    private BlockType type;

    public BlockDoublePlantMP(String name, BlockType type)
    {
        super(Material.VINE);
        this.setDefaultState(this.blockState.getBaseState().withProperty(BlockDoublePlant.HALF, BlockDoublePlant.EnumBlockHalf.LOWER));
        this.setUnlocalizedName(name);
        this.type = type;
    }

    @Override
    public boolean canPlaceBlockAt(World world, BlockPos pos)
    {
        return super.canPlaceBlockAt(world, pos) && world.isAirBlock(pos.up());
    }

    @Override
    public boolean isReplaceable(IBlockAccess world, BlockPos pos)
    {
        IBlockState state = world.getBlockState(pos);

        if (state.getBlock() != this)
        {
            return true;
        }
        else
        {
            return this.type.isGrass();
        }
    }

    @Override
    public boolean canBlockStay(World world, BlockPos pos, IBlockState state)
    {
        if (state.getBlock() != this)
        {
            return super.canBlockStay(world, pos, state);
        }
        if (state.getValue(BlockDoublePlant.HALF) == BlockDoublePlant.EnumBlockHalf.UPPER)
        {
            return world.getBlockState(pos.down()).getBlock() == this;
        }
        else
        {
            IBlockState otherState = world.getBlockState(pos.up());
            return otherState.getBlock() == this && super.canBlockStay(world, pos, otherState);
        }
    }

    @Override
    protected boolean validBlock(Block block)
    {
        if (this.type == BlockType.CHEESE_TALL_GRASS)
        {
            return block == MPBlocks.CHEESE_GRASS_BLOCK || block == MPBlocks.CHEESE_DIRT || block == MPBlocks.CHEESE_COARSE_DIRT || block == MPBlocks.CHEESE_FARMLAND;
        }
        else if (this.type == BlockType.GREEN_VEIN_TALL_GRASS)
        {
            return block == MPBlocks.GREEN_VEIN_GRASS_BLOCK || block == MPBlocks.INFECTED_DIRT || block == MPBlocks.INFECTED_COARSE_DIRT || block == MPBlocks.INFECTED_FARMLAND;
        }
        else if (this.type == BlockType.INFECTED_ORANGE_ROSE_BUSH || this.type == BlockType.INFECTED_TALL_GRASS || this.type == BlockType.INFECTED_LARGE_FERN)
        {
            return block == MPBlocks.INFECTED_GRASS_BLOCK || block == MPBlocks.INFECTED_DIRT || block == MPBlocks.INFECTED_COARSE_DIRT || block == MPBlocks.INFECTED_FARMLAND;
        }
        else if (this.type == BlockType.LARGE_WHEAT || this.type == BlockType.FRONOS_TALL_GRASS)
        {
            return block == MPBlocks.FRONOS_GRASS_BLOCK || block == MPBlocks.FRONOS_DIRT || block == MPBlocks.FRONOS_COARSE_DIRT || block == MPBlocks.FRONOS_FARMLAND;
        }
        return super.validBlock(block);



    @Override
            else if (this.type == BlockType.INFECTED_TALL_GRASS)
            {
                return rand.nextInt(8) == 0 ? MPItems.INFECTED_WHEAT_SEEDS : Items.AIR;
            }
            {
                return rand.nextInt(8) == 0 ? Items.WHEAT_SEEDS : Items.AIR;
            }


        boolean isShears = !player.getHeldItemMainhand().isEmpty() && player.getHeldItemMainhand().getItem() == Items.SHEARS;


        }

        if (!player.capabilities.isCreativeMode && this.type == BlockType.LARGE_WHEAT)
        {
            if (isShears)
            {
                Block.spawnAsEntity(world, pos, new ItemStack(this));
            }
            else
            {
                Block.spawnAsEntity(world, pos, new ItemStack(Items.WHEAT, 4 + world.rand.nextInt(5)));
            }
        }











        if (this.type == BlockType.LARGE_WHEAT)
        {
            ret.add(new ItemStack(MPBlocks.LARGE_WHEAT));
        }
        {
            ret.add(new ItemStack(MPBlocks.FRONOS_TALL_GRASS));
        }



        FRONOS_TALL_GRASS(true);




}