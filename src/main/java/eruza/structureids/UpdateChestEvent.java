package eruza.structureids;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
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
		if(ruinData.size() > 0 && counter >= 0) counter++;
		if(ruinData.size() == 0 && counter > 0) counter = 0;
		if(event.phase == Phase.END) {
			world = event.world;
			Iterator<RuinData> it = ruinData.iterator();
			while(it.hasNext())
			{
				RuinData data = it.next();
				if(findChestCoords(data)) {
					System.out.println("COUNTER: " + counter);
					deletedRuinData.add(data);
				}
				if(counter>100) {
					System.out.println("ERROR: Counter >100");
					System.out.println("Total ruins looking for chests: " + ruinData.size());
					System.out.println("Current: " + data);
					System.out.println("Removing " + data.name);
					deletedRuinData.add(data);
					counter = 0;
				}
			}
			ruinData.removeAll(deletedRuinData);
		}
	}

	//TODO Check if we need to subtract embed from y to get the bounding box correct
	private boolean findChestCoords(RuinData data) {
		for(int y=data.yMin;y<=data.yMax;y++) {
			for(int x=data.xMin;x<=data.xMax;x++) {
				for(int z=data.zMin;z<=data.zMax;z++) {
					if(world.getBlock(x, y, z) == Blocks.chest) {
						addItemToChest(data.name, x, y, z);
						System.out.println("FOUND CHEST AT " + x + " " + y + " " + z + " in " + data);
						return true;
					}
				}
			}
		}
		return false;
	}

	private void addItemToChest(String name, int x, int y, int z) {
		//Temp testing; adds a chest 10 blocks above origin of template
		Block block = world.getBlock(x, y, z);
		if(block != Blocks.chest) {
			System.out.println("BLOCK IS NOT A CHEST");
		}
		TileEntityChest chest = (TileEntityChest) world.getTileEntity(x, y, z);
		if (chest != null)
		{
			ItemStack stack = null;

			stack = new ItemStack(Items.paper);
			stack.setStackDisplayName(name.replace(".tml", "").replace("_", " "));
			if (stack != null)
			{
				Random random = new Random();
				chest.setInventorySlotContents(random.nextInt(chest.getSizeInventory()), stack);
			}

		}
		else {
			System.out.println("ERROR: No tile entity found at chest coords");
		}
	}

}
