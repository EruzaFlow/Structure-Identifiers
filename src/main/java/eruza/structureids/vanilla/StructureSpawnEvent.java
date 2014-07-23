package eruza.structureids.vanilla;

import net.minecraftforge.event.terraingen.BiomeEvent.GetVillageBlockID;
import net.minecraftforge.event.terraingen.InitMapGenEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class StructureSpawnEvent {

	@SubscribeEvent
	public void structureSpawn(InitMapGenEvent event)
	{
		//System.out.println("Vanilla gen event has biome: " + event.biome);
		System.out.println("Vanilla gen event has type: " + event.type);
		//System.out.println("Vanilla gen event has type: " + event.original);
		System.out.println("Vanilla gen event result name: " + event.getResult().name());

	}
}
