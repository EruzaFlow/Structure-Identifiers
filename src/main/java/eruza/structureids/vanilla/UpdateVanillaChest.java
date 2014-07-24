package eruza.structureids.vanilla;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.world.World;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;

public class UpdateVanillaChest {

	public static CopyOnWriteArrayList<NamedBoundingBox> boundingBoxes = new CopyOnWriteArrayList<NamedBoundingBox>();
	private ArrayList<NamedBoundingBox> deletedBoxes = new ArrayList<NamedBoundingBox>();
	private World world;
	private int counter;


	@SubscribeEvent
	public void tickEvent(TickEvent.WorldTickEvent event) {
		if(boundingBoxes.size() == 0) counter = 0;
		if(event.phase == Phase.END) {
			world = event.world;
			Iterator<NamedBoundingBox> it = boundingBoxes.iterator();
			while(it.hasNext())
			{
				counter++;
				NamedBoundingBox box = it.next();
				if(findChestCoords(box)) deletedBoxes.add(box);
				else if(counter>200) {
					System.out.println("ERROR: Counter > 200");
					System.out.println("Total ruins looking for chests: " + boundingBoxes.size());
					System.out.println("Removing " + box);
					deletedBoxes.add(box);
					counter = 0;
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
						TileEntityChest chest = (TileEntityChest) tileEntity;
						System.out.println("FOUND CHEST AT " + x + " " + y + " " + z + " in " + box);
						ItemStack stack = new ItemStack(Items.paper);
						stack.setStackDisplayName(box.name);
						Random random = new Random();
						chest.setInventorySlotContents(random.nextInt(chest.getSizeInventory()), stack);
						return true;
					}						
				}
			}
		}
		return false;
	}
}
