/*******************************************************************************
 * Copyright 2015 SteveKunG - More Planets Mod
 *
 * This work is licensed under a Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International Public License.
 * To view a copy of this license, visit http://creativecommons.org/licenses/by-nc-nd/4.0/.
 ******************************************************************************/

package stevekung.mods.moreplanets.planets.diona.inventory.slot;

import micdoodle8.mods.galacticraft.core.GalacticraftCore;
import micdoodle8.mods.galacticraft.core.network.PacketSimple;
import micdoodle8.mods.galacticraft.core.network.PacketSimple.EnumSimplePacket;
import micdoodle8.mods.galacticraft.planets.asteroids.items.AsteroidsItems;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import stevekung.mods.moreplanets.core.recipe.RecipeIntegration;
import stevekung.mods.moreplanets.planets.diona.items.DionaItems;

public class SlotSchematicTier4RocketNoFlag extends Slot
{
    private final int index;
    private final int x, y, z;
    private final EntityPlayer player;

    public SlotSchematicTier4RocketNoFlag(IInventory par2IInventory, int par3, int par4, int par5, int x, int y, int z, EntityPlayer player)
    {
        super(par2IInventory, par3, par4, par5);
        this.index = par3;
        this.x = x;
        this.y = y;
        this.z = z;
        this.player = player;
    }

    @Override
    public void onSlotChanged()
    {
        if (this.player instanceof EntityPlayerMP)
        {
            for (int var12 = 0; var12 < this.player.worldObj.playerEntities.size(); ++var12)
            {
                final EntityPlayerMP var13 = (EntityPlayerMP) this.player.worldObj.playerEntities.get(var12);

                if (var13.dimension == this.player.worldObj.provider.dimensionId)
                {
                    final double var14 = this.x - var13.posX;
                    final double var16 = this.y - var13.posY;
                    final double var18 = this.z - var13.posZ;

                    if (var14 * var14 + var16 * var16 + var18 * var18 < 20 * 20)
                    {
                        GalacticraftCore.packetPipeline.sendTo(new PacketSimple(EnumSimplePacket.C_SPAWN_SPARK_PARTICLES, new Object[] { this.x, this.y, this.z }), var13);
                    }
                }
            }
        }
    }

    @Override
    public boolean isItemValid(ItemStack par1ItemStack)
    {
    	final Item item = par1ItemStack.getItem();
        final int meta = par1ItemStack.getItemDamage();

        switch (this.index)
        {
        case 1:
            return item == AsteroidsItems.heavyNoseCone;
        case 2: case 3: case 4: case 5: case 6: case 7: case 8: case 9:
        	return item == DionaItems.tier4_rocket_module && meta == 1;
        case 10: case 11:
            return item == RecipeIntegration.RocketParts() && meta == 41;
        case 12:
            return item == AsteroidsItems.basicItem && meta == 1;
        case 13: case 14: case 17: case 18:
            return item == AsteroidsItems.basicItem && meta == 2;
        case 15: case 16:
            return item == RecipeIntegration.RocketParts() && meta == 32;
        case 19:
            return item == RecipeIntegration.LanderT3();
        case 20:
            return item == RecipeIntegration.ControlComputer() && meta == 4;
        case 21:
            return item == RecipeIntegration.IronChest() && (meta == 0 || meta == 1 || meta == 3);
        }
        return false;
    }

    @Override
    public int getSlotStackLimit()
    {
        return this.index == 10 || this.index == 11 ? 2 : 1;
    }
}