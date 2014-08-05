package eruza.structureids.vanilla;

import net.minecraft.world.gen.structure.MapGenMineshaft;
import net.minecraft.world.gen.structure.MapGenScatteredFeature;
import net.minecraft.world.gen.structure.MapGenStronghold;
import net.minecraft.world.gen.structure.MapGenStructure;
import net.minecraft.world.gen.structure.MapGenVillage;
import net.minecraftforge.event.terraingen.InitMapGenEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class MapGen {
    protected static MapGenStronghold strongholdGenerator;
    protected static MapGenVillage villageGenerator;
    protected static MapGenMineshaft mineshaftGenerator;
    protected static MapGenScatteredFeature scatteredFeatureGenerator;
    

	@SubscribeEvent
	public void initMapGen(InitMapGenEvent event)
	{
		System.out.println("TYPE OF GEN: " + event.type);
		if(event.newGen instanceof MapGenStructure) {
			MapGenStructure mapGenStruct = (MapGenStructure) event.newGen;
			newMethod(mapGenStruct);
		}
	}
	
	private void newMethod(MapGenStructure mapGenStruct) {
		if(mapGenStruct instanceof MapGenStronghold) {
			strongholdGenerator = (MapGenStronghold) mapGenStruct;
		}
		if(mapGenStruct instanceof MapGenVillage) {
			villageGenerator = (MapGenVillage) mapGenStruct;
		}
		if(mapGenStruct instanceof MapGenMineshaft) {
			mineshaftGenerator = (MapGenMineshaft) mapGenStruct;
		}
		if(mapGenStruct instanceof MapGenScatteredFeature) {
			scatteredFeatureGenerator = (MapGenScatteredFeature) mapGenStruct;
		}
	}
}
