package stevekung.mods.moreplanets.core.recipe;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class RecipeIntegration {

	public static final String GS_ID = "GalaxySpace";

	public static Item RocketParts() {
		return GameRegistry.findItem(GS_ID, "item.RocketParts");
	}

	public static Item LanderT3() {
		return GameRegistry.findItem(GS_ID, "item.ModuleLander3");
	}

	public static Item ControlComputer() {
		return GameRegistry.findItem(GS_ID, "item.RocketControlComputer");
	}
	
	public static Item SmallFuelCanister() {
		return GameRegistry.findItem(GS_ID, "item.ModuleSmallFuelCanister");
	}

	public static Item IronChest() {
		return GameRegistry.findItem("IronChest", "BlockIronChest");
	}

	private RecipeIntegration() {
	}

}
