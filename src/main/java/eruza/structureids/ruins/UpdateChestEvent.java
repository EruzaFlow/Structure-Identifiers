package eruza.structureids.ruins;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.world.World;
import atomicstryker.ruins.common.RuinData;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import eruza.structureids.StructureIds;

public class UpdateChestEvent {

	public static CopyOnWriteArrayList<RuinData> ruinData = new CopyOnWriteArrayList<RuinData>();
	private ArrayList<RuinData> deletedRuinData = new ArrayList<RuinData>();
	private World world;
	private int counter;
	private final boolean debug = true;


	@SubscribeEvent
	public void tickEvent(TickEvent.WorldTickEvent event) {
		if(ruinData.size() == 0) counter = 0;
		else counter++;

		if(counter > 0 && counter % 10 == 0) {
			world = event.world;
			Iterator<RuinData> it = ruinData.iterator();
			while(it.hasNext())
			{
				RuinData data = it.next();
				if(findChestCoords(data)) deletedRuinData.add(data);
				else if(counter>50) {
					if(debug) System.out.println("ERROR: Failed to find chest at " + data);
					deletedRuinData.add(data);
					counter = 30;
				}
			}
			ruinData.removeAll(deletedRuinData);
		}
	}

	/**
	 * Scans within the bounding box of data to find a chest
	 * 
	 * @param data
	 * @return
	 */
	private boolean findChestCoords(RuinData data) {
		for(int y=data.yMin;y<=data.yMax;y++) {
			for(int x=data.xMin;x<=data.xMax;x++) {
				for(int z=data.zMin;z<=data.zMax;z++) {
					TileEntity tileEntity = world.getTileEntity(x, y, z);
					if (tileEntity instanceof TileEntityChest) {
						TileEntityChest chest = (TileEntityChest) tileEntity;
						String name = data.name.replace(".tml", "").replace("_", " ");
						Random random = new Random();
						chest.setInventorySlotContents(random.nextInt(chest.getSizeInventory()), StructureIds.getItemStack(name));
						if(debug) System.out.println("Found chest at " + x + " " + y + " " + z + " for " + name);
						return true;
					}
				}
			}
		}
		return false;
	}
}
