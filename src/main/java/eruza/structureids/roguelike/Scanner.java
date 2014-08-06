package eruza.structureids.roguelike;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.world.World;
import eruza.structureids.StructureIds;
import eruza.structureids.util.SidLog;

public class Scanner {
	int x, y, z;
	String curDir;
	HashSet<TileEntity> chests = new HashSet<TileEntity>();
	World world;
	ArrayList<Block> air = new ArrayList<Block>();
	private static final Block pathBlock = Blocks.torch;
	int rightTurns = 0, leftTurns = 0, steps = 0;
	
	public Scanner(World world) {
		this.world = world;
		air.add(Blocks.air);
		air.add(Blocks.brown_mushroom);
		air.add(Blocks.red_mushroom_block);
		air.add(Blocks.wooden_door);
		air.add(Blocks.gravel);
		if(StructureIds.debug) air.add(pathBlock);
	}
	
	public void beginSearch(String dir, int x, int y, int z, int startX, int startZ) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.curDir = dir;
		SidLog.info("Initiating scan facing "+dir+". Setting startX: "+startX+" startZ: "+startZ+".");
		while(!(this.x == startX && this.z == startZ)) {
			if(rightTurns > 4) {
				SidLog.warn("Too many right turns");
				break;
			}
			if(leftTurns > 4) {
				SidLog.warn("Too many left turns");
				break;
			}
			if(steps > 2000) {
				SidLog.warn("Too many steps");
				break;
			}
			followWall();
		}
		SidLog.info("Finished searching dungeon " + steps + " steps");
	}
	
	public boolean addItemToChest(String level) {
		String name = ("Roguelike Dungeon Level " + level);
		Random random = new Random();
		TileEntityChest chest = getRandomChest();
		if(chest == null) {
			//TODO Change this to utility method in main class give it 3 coords returns string; ugh;
			SidLog.info("Failed to find chest at "  + SidLog.coordToString(x, y, z) + " for " + name );
			return false;
		}
		chest.setInventorySlotContents(random.nextInt(chest.getSizeInventory()), StructureIds.getItemStack(name));
		SidLog.info("Found chest at " + chest.xCoord + " " + chest.yCoord + " " + chest.zCoord + " for " + name);
		return true;
	}
	
	public TileEntityChest getRandomChest() {
		Random rand = new Random();
		int i = rand.nextInt(chests.size());
		SidLog.info("Found " + chests.size() + " chests picking #" + i);
		return (TileEntityChest) chests.toArray()[i];
	}

	private void followWall() {
		if(air.contains(getBlockAtLeft())) {
			turnLeft();
			stepForward();
		}
		else if(air.contains(getBlockAhead(curDir))) stepForward();
		else turnRight();
	}
	
	private void turnRight() {
		rightTurns++;
		curDir = lookRight();
	}
	
	private void turnLeft() {
		leftTurns++;
		curDir = lookLeft();
	}
	
	private String lookRight() {
		if(curDir.equals("north")) return "east";
		if(curDir.equals("west")) return "north";
		if(curDir.equals("south")) return "west";
		if(curDir.equals("east")) return "south";
		SidLog.error("ERROR: MISNAMED DIRECTION: " + curDir);
		return "north";
	}

	private Block getBlockAtLeft() {
		return getBlockAhead(lookLeft());
	}
	
	private Block getBlockAhead(String dir) {
		int x = this.x;
		int z = this.z;
		if(dir.equals("east")) x++;
		if(dir.equals("west")) x--;
		if(dir.equals("south")) z++;
		if(dir.equals("north")) z--;
		Block block = world.getBlock(x, y, z);
		if(block == Blocks.chest) {
			chests.add(world.getTileEntity(x, y, z));
		}
		return block;
	}

	private String lookLeft() {
		if(curDir.equals("north")) return "west";
		if(curDir.equals("west")) return "south";
		if(curDir.equals("south")) return "east";
		if(curDir.equals("east")) return "north";
		SidLog.error("ERROR: MISNAMED DIRECTION: " + curDir);
		return "north";
	}
	
	private void stepForward() {
		steps++;
		if(StructureIds.debug && world.getBlock(x, y, z) == Blocks.air) world.setBlock(x, y, z, pathBlock);
		if(curDir.equals("east")) x++;
		if(curDir.equals("west")) x--;
		if(curDir.equals("south")) z++;
		if(curDir.equals("north")) z--;
		rightTurns = 0;
		leftTurns = 0;
		checkForNextLevel();
	}

	private void checkForNextLevel() {
		//bottom level
		if(y==10) return;
		int checkX = x;
		int checkZ = z;
		int checkY = y-1;
		String right = lookRight();
		if(right.equals("east")) checkX=checkX+2;
		if(right.equals("west")) checkX=checkX-2;
		if(right.equals("south")) checkZ=checkZ+2;
		if(right.equals("north")) checkZ=checkZ-2;
		if(world.getBlock(checkX, checkY, checkZ) == Blocks.stone_brick_stairs) {
			//TODO Add check for metadata, filter inverted stairs
			//TODO Stairs are not center of level, find center
			SidLog.info("Found stairs at " + SidLog.coordToString(checkX, checkY, checkZ));
			SidLog.info("Setting level 2 to " + SidLog.coordToString(checkX, checkY-10, checkZ));
		}
	}
}
