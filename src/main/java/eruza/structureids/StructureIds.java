package eruza.structureids;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import eruza.structureids.ruins.RuinSpawnEvent;
import eruza.structureids.ruins.UpdateChestEvent;
import eruza.structureids.vanilla.ChunkPopulationEvent;
import eruza.structureids.vanilla.StructureSpawnEvent;
import eruza.structureids.vanilla.UpdateVanillaChest;

@Mod(modid = StructureIds.MODID, name = StructureIds.NAME, version = StructureIds.VERSION)
public class StructureIds
{
	public static final String MODID = "StructIds";
	public static final String NAME = "Structure Identifiers";
	public static final String VERSION = "0.15";
	public static boolean isBopInstalled = false;
	

	@EventHandler
	public void init(FMLInitializationEvent event)
	{
		if(cpw.mods.fml.common.Loader.isModLoaded("AS_Ruins")) {
			MinecraftForge.EVENT_BUS.register(new RuinSpawnEvent());
			FMLCommonHandler.instance().bus().register(new UpdateChestEvent());
		}
		if(cpw.mods.fml.common.Loader.isModLoaded("BiomesOPlenty")) isBopInstalled = true;
		MinecraftForge.TERRAIN_GEN_BUS.register(new StructureSpawnEvent());
		MinecraftForge.TERRAIN_GEN_BUS.register(new ChunkPopulationEvent());
		FMLCommonHandler.instance().bus().register(new UpdateVanillaChest());
	}
	
	public static ItemStack getItemStack(String name) {
		ItemStack stack = new ItemStack(Items.paper);
		stack.setStackDisplayName(name);
		return stack;
	}
}
