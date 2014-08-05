package eruza.structureids.roguelike;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import greymerk.roguelike.EventRoguelikeGen;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import eruza.structureids.util.SidLog;

@SuppressWarnings("unused")
public class RoguelikeGenEvent {
	private static final int levelOne = 50, levelTwo = 40, levelThree = 30, levelFour = 20, levelFive = 10;
	private EventRoguelikeGen event;
	private World world;


	@SubscribeEvent
	public void roguelikeGen(EventRoguelikeGen event)
	{
		SidLog.info("Roguelike generated at " + event.x + " 80 " + event.z);
		this.world = event.world;
		this.event = event;
		findChest();
	}

	private void findChest() {
		Scanner scanner = new Scanner(world);
		startScans(levelOne, scanner);
	}

	private void startScans(int y, Scanner scanner) {
		int x = event.x + 4;
		int z = event.z;
		if(world.getBlock(x, y, z) == Blocks.iron_bars) scanner.beginSearch("east", x+1, y, z-1, x+1, z);
		x = event.x - 4;
		if(world.getBlock(x, y, z) == Blocks.iron_bars) scanner.beginSearch("west", x-1, y, z+1, x-1, z);
		x = event.x;
		z = event.z + 4;
		if(world.getBlock(x, y, z) == Blocks.iron_bars) scanner.beginSearch("south", x+1, y, z+1, x, z+1);
		z = event.z - 4;
		if(world.getBlock(x, y, z) == Blocks.iron_bars) scanner.beginSearch("north", x-1, y, z-1, x, z-1);
		//TODO Obviously make this a generated string for the level
		scanner.addItemToChest("1");
	}

	//TODO
	//Useful things:
	//ITheme theme = settings.getTheme();
	//theme.getPrimaryWall();
}
