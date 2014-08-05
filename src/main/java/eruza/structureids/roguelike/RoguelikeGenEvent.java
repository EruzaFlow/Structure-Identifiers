package eruza.structureids.roguelike;

import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import greymerk.roguelike.EventRoguelikeGen;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

@SuppressWarnings("unused")
public class RoguelikeGenEvent {
	private static final int levelOne = 50, levelTwo = 40, levelThree = 30, levelFour = 20, levelFive = 10;
	
	@SubscribeEvent
	public void roguelikeGen(EventRoguelikeGen event)
	{
		System.out.println("Roguelike generated at " + event.x + " 80 " + event.z);
		findChest(event);
	}

	private void findChest(EventRoguelikeGen event) {
		World world = event.world;
		int x = event.x + 4;
		int y = levelOne + 1;
		int z = event.z;
		if(findIronBars(world, x, y, z)) System.out.println("Found iron bars at: " + x + " " + y + " " + z);
		x = event.x - 4;
		if(findIronBars(world, x, y, z)) System.out.println("Found iron bars at: " + x + " " + y + " " + z);
		z = event.z + 4;
		if(findIronBars(world, x, y, z)) System.out.println("Found iron bars at: " + x + " " + y + " " + z);
		z = event.z - 4;
		if(findIronBars(world, x, y, z)) System.out.println("Found iron bars at: " + x + " " + y + " " + z);
	}
	
	private boolean findIronBars(World world, int x, int y, int z) {
		return world.getBlock(x, y, z) == Blocks.iron_bars;
	}
	
	//TODO
	//Useful things:
	//ITheme theme = settings.getTheme();
	//theme.getPrimaryWall();
}
