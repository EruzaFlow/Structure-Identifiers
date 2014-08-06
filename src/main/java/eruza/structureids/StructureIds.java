package eruza.structureids;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import eruza.structureids.roguelike.RoguelikeGenEvent;
import eruza.structureids.ruins.RuinSpawnEvent;
import eruza.structureids.util.SidLog;
import eruza.structureids.vanilla.VanillaChunkPopEvent;
import eruza.structureids.vanilla.MapGen;
import eruza.structureids.vanilla.LocateVanillaChest;

@Mod(modid = StructureIds.MODID, name = StructureIds.NAME, version = StructureIds.VERSION)
public class StructureIds
{
	public static final String MODID = "StructIds";
	public static final String NAME = "Structure Identifiers";
	public static final String VERSION = "0.15";
	public static boolean isBopInstalled = false;
	public static boolean debug = true;
	

	@EventHandler
	public void init(FMLInitializationEvent event)
	{
		if(cpw.mods.fml.common.Loader.isModLoaded("AS_Ruins")) MinecraftForge.EVENT_BUS.register(new RuinSpawnEvent());
		if(cpw.mods.fml.common.Loader.isModLoaded("BiomesOPlenty")) isBopInstalled = true;
		if(cpw.mods.fml.common.Loader.isModLoaded("Roguelike")) MinecraftForge.EVENT_BUS.register(new RoguelikeGenEvent());
		MinecraftForge.TERRAIN_GEN_BUS.register(new MapGen());
		MinecraftForge.TERRAIN_GEN_BUS.register(new VanillaChunkPopEvent());
		FMLCommonHandler.instance().bus().register(new LocateVanillaChest());
		if(debug) SidLog.error("DEBUG MODE IS ENABLED; THIS IS BAD");
	}
	
	public static ItemStack getItemStack(String name) {
		ItemStack stack = new ItemStack(Items.paper);
		stack.setStackDisplayName(name);
		return stack;
	}
}
