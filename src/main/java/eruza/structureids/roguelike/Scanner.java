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
	Block[] test = { Blocks.air, Blocks.sponge };
	int rightTurns = 0, leftTurns = 0, steps = 0;
	
	public Scanner(World world) {
		this.world = world;
		air.add(Blocks.air);
		if(StructureIds.debug) air.add(Blocks.sponge);
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
		SidLog.info("Finished searching dungeon");
	}
	
	public boolean addItemToChest(String level) {
		String name = ("Roguelike Dungeon Level " + level);
		Random random = new Random();
		TileEntityChest chest = getRandomChest();
		if(chest == null) {
			//TODO Change this to utility method in main class give it 3 coords returns string; ugh;
			SidLog.info("Failed to find chest at "  + x + " " + y + " " + z + " for " + name );
			return false;
		}
		chest.setInventorySlotContents(random.nextInt(chest.getSizeInventory()), StructureIds.getItemStack(name));
		SidLog.info("Found chest at " + chest.xCoord + " " + chest.yCoord + " " + chest.zCoord + " for " + name);
		return true;
	}
	
	public TileEntityChest getRandomChest() {
		Random rand = new Random();
		int i = rand.nextInt(chests.size());
		return (TileEntityChest) chests.toArray()[i];
	}

	private void followWall() {
		if(air.contains(getBlockAtLeft())) {
			curDir = turnLeft();
			stepForward();
		}
		else if(air.contains(getBlockAhead(curDir))) stepForward();
		else turnRight();
	}
	
	private void turnRight() {
		rightTurns++;
		if(curDir.equals("north")) curDir = "east";
		if(curDir.equals("west")) curDir = "north";
		if(curDir.equals("south")) curDir = "west";
		if(curDir.equals("east")) curDir = "south";
	}

	private Block getBlockAtLeft() {
		return getBlockAhead(turnLeft());
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

	private String turnLeft() {
		leftTurns++;
		if(curDir.equals("north")) return "west";
		if(curDir.equals("west")) return "south";
		if(curDir.equals("south")) return "east";
		if(curDir.equals("east")) return "north";
		SidLog.error("ERROR: MISNAMED DIRECTION: " + curDir);
		return curDir;
	}
	
	private void stepForward() {
		steps++;
		if(StructureIds.debug) world.setBlock(x, y, z, Blocks.sponge);
		if(curDir.equals("east")) x++;
		if(curDir.equals("west")) x--;
		if(curDir.equals("south")) z++;
		if(curDir.equals("north")) z--;
		rightTurns = 0;
		leftTurns = 0;
	}



}
