package eruza.structureids.ruins;

import java.util.Random;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.world.World;
import atomicstryker.ruins.common.EventRuinTemplateSpawn;
import atomicstryker.ruins.common.RuinData;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import eruza.structureids.StructureIds;
import eruza.structureids.util.SidLog;

public class RuinSpawnEvent {

	@SubscribeEvent
	public void ruinSpawn(EventRuinTemplateSpawn event)
	{
		boolean flag = true;
		if(event.testingRuin && !StructureIds.debug) flag = false;
		if(!event.isPrePhase &&	flag) {
			if(event.template == null) {
				SidLog.error("The template is null!");
				printEvent(event);
				return;
			}
			RuinData data = event.template.getRuinData(event.x, event.y, event.z, event.rotation);
			if(!findChestCoords(event.world, data) && StructureIds.debug) {
				SidLog.info("Failed to find chest at " + data);
			}
		}
	}
	
	void printEvent(EventRuinTemplateSpawn event) {
		System.out.print("Event " + event.template);
		System.out.print(" " + event.testingRuin);
		System.out.print(" " + event.isPrePhase);
		System.out.print(" " + event.rotation);
		System.out.println(" " + event.toString());
	}

	/**
	 * Scans within the bounding box of data to find a chest
	 * 
	 * @param data
	 * @return
	 */
	private boolean findChestCoords(World world, RuinData data) {
		for(int y=data.yMin;y<=data.yMax;y++) {
			for(int x=data.xMin;x<=data.xMax;x++) {
				for(int z=data.zMin;z<=data.zMax;z++) {
					TileEntity tileEntity = world.getTileEntity(x, y, z);
					if (tileEntity instanceof TileEntityChest) {
						TileEntityChest chest = (TileEntityChest) tileEntity;
						String name = data.name.replace(".tml", "").replace("_", " ");
						Random random = new Random();
						chest.setInventorySlotContents(random.nextInt(chest.getSizeInventory()), StructureIds.getKeyedToken(name));
						SidLog.info("Found chest at " + x + " " + y + " " + z + " for " + name);
						return true;
					}
				}
			}
		}
		return false;
	}
}
