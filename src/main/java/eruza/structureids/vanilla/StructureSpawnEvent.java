package eruza.structureids.vanilla;

import net.minecraftforge.event.terraingen.InitMapGenEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class StructureSpawnEvent {

	@SubscribeEvent
	public void structureSpawn(InitMapGenEvent event)
	{
		System.out.println("Vanilla gen event: " + event.type);
		System.out.println("Vanilla gen event result name: " + event.getResult().name());
	}
}
