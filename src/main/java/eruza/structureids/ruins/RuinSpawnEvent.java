package eruza.structureids.ruins;

import atomicstryker.ruins.common.EventRuinTemplateSpawn;
import atomicstryker.ruins.common.RuinData;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class RuinSpawnEvent {

	@SubscribeEvent
	public void ruinSpawn(EventRuinTemplateSpawn event)
	{
		if(!event.isPrePhase) {
			System.out.println(event.template.getName() + " " + event.x + " " + event.y + " " + event.z);
			RuinData data = event.template.getRuinData(event.x, event.y, event.z, event.rotation);
			UpdateChestEvent.ruinData.add(data);
		}
	}
}
