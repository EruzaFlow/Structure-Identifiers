package eruza.structureids.ruins;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.world.World;
import atomicstryker.ruins.common.RuinData;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;

public class UpdateChestEvent {

	public static CopyOnWriteArrayList<RuinData> ruinData = new CopyOnWriteArrayList<RuinData>();
	private ArrayList<RuinData> deletedRuinData = new ArrayList<RuinData>();
	private World world;
	private int counter;


	@SubscribeEvent
	public void tickEvent(TickEvent.WorldTickEvent event) {
		if(ruinData.size() == 0) counter = 0;
		if(event.phase == Phase.END) {
			world = event.world;
			Iterator<RuinData> it = ruinData.iterator();
			while(it.hasNext())
			{
				counter++;
				RuinData data = it.next();
				if(findChestCoords(data)) deletedRuinData.add(data);
				else if(counter>50) {
					if(data.yMin < 125) {
						System.out.println("ERROR: Counter > 50");
						System.out.println("Total ruin structures looking for chests: " + ruinData.size());
						System.out.println("Current: " + data);
						System.out.println("Removing " + data.name);
					}
					deletedRuinData.add(data);
					counter = 0;
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
						ItemStack stack = new ItemStack(Items.paper);
						stack.setStackDisplayName(data.name.replace(".tml", "").replace("_", " "));
						Random random = new Random();
						chest.setInventorySlotContents(random.nextInt(chest.getSizeInventory()), stack);
						//System.out.println("FOUND CHEST AT " + x + " " + y + " " + z + " in " + data);
						return true;
					}
				}
			}
		}
		return false;
	}
}
