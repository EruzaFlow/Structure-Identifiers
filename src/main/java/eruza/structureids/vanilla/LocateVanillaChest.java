package eruza.structureids.vanilla;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import eruza.structureids.StructureIds;
import eruza.structureids.util.SidLog;

public class LocateVanillaChest {

	private static CopyOnWriteArrayList<NamedBoundingBox> boundingBoxes = new CopyOnWriteArrayList<NamedBoundingBox>();
	private ArrayList<NamedBoundingBox> deletedBoxes = new ArrayList<NamedBoundingBox>();
	private World world;
	private int counter;
	private int timeout = 100;


	@SubscribeEvent
	public void tickEvent(TickEvent.WorldTickEvent event) {
		if(boundingBoxes.size() == 0) counter = 0;
		else counter++;

		if(counter > 0 && counter % 10 == 0) {
			world = event.world;
			Iterator<NamedBoundingBox> it = boundingBoxes.iterator();
			while(it.hasNext())
			{
				counter++;
				NamedBoundingBox box = it.next();
				if(box.name.equals("Village") && findVillageRoad(box)) deletedBoxes.add(box);
				if(box.name.equals("Mineshaft") && findMineshaftRail(box)) deletedBoxes.add(box);
				if((box.name.equals("Scattered Features") || box.name.equals("Stronghold")) && findChestCoords(box)) deletedBoxes.add(box);
				if(counter>timeout) {
					SidLog.info("ERROR: Failed to find chest at " + box);
					deletedBoxes.add(box);
					counter = 30;
				}
			}
			boundingBoxes.removeAll(deletedBoxes);
		}
	}

	/**
	 * Scans within the bounding box of data to find a chest
	 * 
	 * @param box
	 * @return
	 */
	private boolean findChestCoords(NamedBoundingBox box) {
		for(int y=box.minY;y<=box.maxY;y++) {
			for(int x=box.minX;x<=box.maxX;x++) {
				for(int z=box.minZ;z<=box.maxZ;z++) {
					TileEntity tileEntity = world.getTileEntity(x, y, z);
					if (tileEntity instanceof TileEntityChest) {
						placeItemInChest(box, x, y, z);
						return true;
					}
				}
			}
		}
		return false;
	}

	private boolean findVillageRoad(NamedBoundingBox box) {
		int y = box.minY;
		for(int x=box.minX;x<=box.maxX;x++) {
			for(int z=box.minZ;z<=box.maxZ;z++) {
				getRoadTypeForBiome(world.getBiomeGenForCoords(x, z));
				if (world.getBlock(x, y, z) == Blocks.gravel || world.getBlock(x, y, z) == Blocks.sandstone) {
					y = y + 1;
					if(world.getBlock(x, y, z) == Blocks.air && world.setBlock(x, y, z, Blocks.chest)) {
						placeItemInChest(box, x, y, z);
						return true;
					}					
				}						
			}
		}
		return false;
	}

	private Block getRoadTypeForBiome(BiomeGenBase biome) {
		//TODO Finish this, for BOP; biome.biomeName returns Desert
		return null;

	}

	private boolean findMineshaftRail(NamedBoundingBox box) {
		//TODO Change to minecart with chest on rail?
		for(int y=box.minY;y<=box.maxY;y++) {
			for(int x=box.minX;x<=box.maxX;x++) {
				for(int z=box.minZ;z<=box.maxZ;z++) {
					if (world.getBlock(x, y, z) == Blocks.rail) {
						x = x + 1;
						if(world.getBlock(x, y, z) == Blocks.air && world.setBlock(x, y, z, Blocks.chest)) {
							placeItemInChest(box, x, y, z);
							return true;					
						}					
					}						
				}
			}
		}
		return false;
	}

	private void placeItemInChest(NamedBoundingBox box, int x, int y, int z) {
		TileEntityChest chest = (TileEntityChest) world.getTileEntity(x, y, z);
		SidLog.info("Found chest at " + x + " " + y + " " + z + " for " + box.name);
		Random random = new Random();
		chest.setInventorySlotContents(random.nextInt(chest.getSizeInventory()), StructureIds.getKeyedToken(box.name));
	}

	public static void addBox(NamedBoundingBox box) {
		if(hasNoIntersects(box)) {
			boundingBoxes.add(box);
		}
	}

	private static boolean hasNoIntersects(NamedBoundingBox box) {
		Iterator<NamedBoundingBox> it = boundingBoxes.iterator();
		while(it.hasNext()) if(box.intersectsWith(it.next())) return false;
		return true;
	}
}
