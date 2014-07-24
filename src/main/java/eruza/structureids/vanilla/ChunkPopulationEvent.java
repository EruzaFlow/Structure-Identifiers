package eruza.structureids.vanilla;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import net.minecraft.world.gen.MapGenBase;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureStart;
import net.minecraftforge.event.terraingen.PopulateChunkEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class ChunkPopulationEvent {
	boolean flag = true;

	@SubscribeEvent
	public void populateEvent(PopulateChunkEvent event) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		//System.out.println("NEW populate chunk event");
		//System.out.println("TYPE: " + event.type);
		//System.out.println("Coords: " + event.chunkX + " " + event.chunkZ);
		if(canStructureSpawn(event.chunkX, event.chunkZ, StructureSpawnEvent.villageGenerator)) {
			System.out.println("VILLAGE CAN SPAWN");
			NamedBoundingBox box = new NamedBoundingBox(getStructureInChunk(event.chunkX, event.chunkZ, StructureSpawnEvent.villageGenerator), "Village");
			System.out.println(box);
			UpdateVanillaChest.boundingBoxes.add(box);
		}
		if(canStructureSpawn(event.chunkX,event.chunkZ, StructureSpawnEvent.mineshaftGenerator)) {
			System.out.println("MINESHAFT CAN SPAWN");
			NamedBoundingBox box = new NamedBoundingBox(getStructureInChunk(event.chunkX, event.chunkZ, StructureSpawnEvent.mineshaftGenerator), "Mineshaft");
			System.out.println(box);
			UpdateVanillaChest.boundingBoxes.add(box);
		}
		if(canStructureSpawn(event.chunkX,event.chunkZ, StructureSpawnEvent.strongholdGenerator)) {
			System.out.println("STRONGHOLD CAN SPAWN");
			NamedBoundingBox box = new NamedBoundingBox(getStructureInChunk(event.chunkX, event.chunkZ, StructureSpawnEvent.strongholdGenerator), "Stronghold");
			System.out.println(box);
			UpdateVanillaChest.boundingBoxes.add(box);
		}
	}

	@SuppressWarnings("rawtypes")
	private boolean canStructureSpawn(int chunkX, int chunkZ, MapGenBase generator) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
		Class[] classes = new Class[2];
		classes[0] = int.class;
		classes[1] = int.class;
		Method canStructureSpawn = generator.getClass().getDeclaredMethod("canSpawnStructureAtCoords", classes);
		canStructureSpawn.setAccessible(true);
		boolean canSpawn = (boolean) canStructureSpawn.invoke(generator, chunkX, chunkZ);
		return canSpawn;
	}

	@SuppressWarnings("rawtypes")
	private StructureBoundingBox getStructureInChunk(int chunkX, int chunkZ, MapGenBase generator) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Class[] classes = new Class[2];
		classes[0] = int.class;
		classes[1] = int.class;
		Method getStructureStart = generator.getClass().getDeclaredMethod("getStructureStart", classes);
		getStructureStart.setAccessible(true);
		StructureStart start = (StructureStart) getStructureStart.invoke(generator, chunkX, chunkZ);
		return start.getBoundingBox();
	}
}
