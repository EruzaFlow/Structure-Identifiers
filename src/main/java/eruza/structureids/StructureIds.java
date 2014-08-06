package eruza.structureids;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import eruza.structureids.item.ItemToken;
import eruza.structureids.roguelike.RoguelikeGenEvent;
import eruza.structureids.ruins.RuinSpawnEvent;
import eruza.structureids.util.SidLog;
import eruza.structureids.vanilla.LocateVanillaChest;
import eruza.structureids.vanilla.MapGen;
import eruza.structureids.vanilla.VanillaChunkPopEvent;

@Mod(modid = StructureIds.MODID, name = StructureIds.NAME, version = StructureIds.VERSION)
public class StructureIds
{
	public static final String MODID = "StructIds";
	public static final String NAME = "Structure Identifiers";
	public static final String VERSION = "0.15";
	private static Item itemToken;
	
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
	
	@EventHandler
	public void load(FMLInitializationEvent event)
	{
		itemToken = new ItemToken();
		
		GameRegistry.registerItem(itemToken, "itemToken");
	}
	
	public static ItemStack getKeyedToken(String name) {
		name = name + " Token";
		ItemStack stack = new ItemStack(itemToken);
		setKey(stack, name);
		stack.setStackDisplayName(name);
		return stack;
	}
	
    /**
     * Sets a key parameter on nbt to verify that the user didn't just rename the token.
     * 
     * @param stack
     * @param key
     */
    private static void setKey(ItemStack stack, String key) {
        if (stack.stackTagCompound == null)
        {
            stack.stackTagCompound = new NBTTagCompound();
        }

        if (!stack.stackTagCompound.hasKey("sidkey", 10))
        {
            stack.stackTagCompound.setTag("sidkey", new NBTTagCompound());
        }

        stack.stackTagCompound.getCompoundTag("sidkey").setString("Key", key);
     }
	
	
}
