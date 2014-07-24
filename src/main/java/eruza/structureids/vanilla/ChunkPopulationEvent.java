package eruza.structureids.vanilla;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import net.minecraft.world.gen.MapGenBase;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureStart;
import net.minecraftforge.event.terraingen.PopulateChunkEvent;
import net.minecraftforge.event.terraingen.PopulateChunkEvent.Populate;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class ChunkPopulationEvent {
	boolean flag = true;

	@SubscribeEvent
	public void populateEvent(PopulateChunkEvent.Populate event) {
		testStructureAndUpdateChest(event, StructureSpawnEvent.villageGenerator, "Village");
		testStructureAndUpdateChest(event, StructureSpawnEvent.mineshaftGenerator, "Mineshaft");
		testStructureAndUpdateChest(event, StructureSpawnEvent.strongholdGenerator, "Stronghold");
		testStructureAndUpdateChest(event, StructureSpawnEvent.scatteredFeatureGenerator, "Scattered Features");
	}

	private void testStructureAndUpdateChest(Populate event, MapGenBase generator, String name) {
		try {
			if(canStructureSpawn(event.chunkX, event.chunkZ, generator)) {
				NamedBoundingBox box = new NamedBoundingBox(getStructureInChunk(event.chunkX, event.chunkZ, generator), name);
				UpdateVanillaChest.addBox(box);
			}
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("rawtypes")
	private boolean canStructureSpawn(int chunkX, int chunkZ, MapGenBase generator) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
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
