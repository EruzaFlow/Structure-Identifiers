package eruza.structureids.vanilla;

import net.minecraft.world.gen.structure.MapGenMineshaft;
import net.minecraft.world.gen.structure.MapGenScatteredFeature;
import net.minecraft.world.gen.structure.MapGenStronghold;
import net.minecraft.world.gen.structure.MapGenStructure;
import net.minecraft.world.gen.structure.MapGenVillage;
import net.minecraftforge.event.terraingen.InitMapGenEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class StructureSpawnEvent {
    protected static MapGenStronghold strongholdGenerator;
    protected static MapGenVillage villageGenerator;
    protected static MapGenMineshaft mineshaftGenerator;
    protected static MapGenScatteredFeature scatteredFeatureGenerator;
    

	@SubscribeEvent
	public void structureSpawn(InitMapGenEvent event)
	{
		//System.out.println("Vanilla gen event has biome: " + event.biome);
		System.out.println("Vanilla gen event has type: " + event.type);
		if(event.newGen instanceof MapGenStructure) {
			MapGenStructure mapGenStruct = (MapGenStructure) event.originalGen;
			newMethod(mapGenStruct);
		}
	}
	
	private void newMethod(MapGenStructure mapGenStruct) {
		if(mapGenStruct instanceof MapGenStronghold) {
			System.out.println("Stronghold gen captured");
			strongholdGenerator = (MapGenStronghold) mapGenStruct;
		}
		if(mapGenStruct instanceof MapGenVillage) {
			System.out.println("Village gen captured");
			villageGenerator = (MapGenVillage) mapGenStruct;
		}
		if(mapGenStruct instanceof MapGenMineshaft) {
			System.out.println("Mineshaft gen captured");
			mineshaftGenerator = (MapGenMineshaft) mapGenStruct;
		}
		if(mapGenStruct instanceof MapGenScatteredFeature) {
			System.out.println("ScatteredFeature gen captured");
			scatteredFeatureGenerator = (MapGenScatteredFeature) mapGenStruct;
		}
	}
}
