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
		if(!event.isPrePhase && event.template != null) {
			RuinData data = event.template.getRuinData(event.x, event.y, event.z, event.rotation);
			if(!findChestCoords(event.world, data) && StructureIds.debug) {
				SidLog.info("Failed to find chest at " + data);
			}
		}
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
						chest.setInventorySlotContents(random.nextInt(chest.getSizeInventory()), StructureIds.getItemStack(name));
						SidLog.info("Found chest at " + x + " " + y + " " + z + " for " + name);
						return true;
					}
				}
			}
		}
		return false;
	}
}
