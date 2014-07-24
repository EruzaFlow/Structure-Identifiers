package eruza.structureids;

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

@Mod(modid = StructureIds.MODID, version = StructureIds.VERSION)
public class StructureIds
{
	public static final String MODID = "examplemod";
	public static final String VERSION = "1.0";

	@EventHandler
	public void init(FMLInitializationEvent event)
	{
		if(cpw.mods.fml.common.Loader.isModLoaded("AS_Ruins")) {
			MinecraftForge.EVENT_BUS.register(new RuinSpawnEvent());
			FMLCommonHandler.instance().bus().register(new UpdateChestEvent());
		}
		MinecraftForge.TERRAIN_GEN_BUS.register(new StructureSpawnEvent());
		MinecraftForge.TERRAIN_GEN_BUS.register(new ChunkPopulationEvent());
		FMLCommonHandler.instance().bus().register(new UpdateVanillaChest());
	}
}
