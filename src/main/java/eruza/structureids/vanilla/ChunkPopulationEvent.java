package eruza.structureids.vanilla;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import net.minecraft.world.gen.MapGenBase;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureStart;
import net.minecraftforge.event.terraingen.PopulateChunkEvent;
import net.minecraftforge.event.terraingen.PopulateChunkEvent.Populate;
import biomesoplenty.common.eventhandler.world.BOPMapGenVillageEventHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import eruza.structureids.StructureIds;

public class ChunkPopulationEvent {

	@SubscribeEvent
	public void populateEvent(PopulateChunkEvent.Populate event) {
		testStructureAndUpdateChest(event, StructureSpawnEvent.villageGenerator, "Village");
		testStructureAndUpdateChest(event, StructureSpawnEvent.mineshaftGenerator, "Mineshaft");
		testStructureAndUpdateChest(event, StructureSpawnEvent.strongholdGenerator, "Stronghold");
		//Below line sometimes causes illegal argument exceptions due to reflection, not sure why
		//testStructureAndUpdateChest(event, StructureSpawnEvent.scatteredFeatureGenerator, "Scattered Features");
	}

	private void testStructureAndUpdateChest(Populate event, MapGenBase generator, String name) {
		try {
			if(canStructureSpawn(event.chunkX, event.chunkZ, generator)) {
				NamedBoundingBox box = new NamedBoundingBox(getStructureInChunk(event.chunkX, event.chunkZ, generator), name);
				UpdateVanillaChest.addBox(box);
			}
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			System.out.println("Generator class: " + generator.getClass());
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			System.out.println("Generator class: " + generator.getClass());
			e.printStackTrace();
		}
	}

	private boolean canStructureSpawn(int chunkX, int chunkZ, MapGenBase generator) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Class<?> genClass;
		//BOP compatibility
		if(StructureIds.isBopInstalled && generator instanceof BOPMapGenVillageEventHandler) genClass = generator.getClass().getSuperclass();
		else genClass = generator.getClass();
		Method canStructureSpawn = getCorrectMethod(genClass, "canStructureSpawn");
		Boolean canSpawn = (Boolean) canStructureSpawn.invoke(generator, chunkX, chunkZ);
		return canSpawn;
	}

	private StructureBoundingBox getStructureInChunk(int chunkX, int chunkZ, MapGenBase generator) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Class<?> genClass;
		//BOP compatibility
		if(StructureIds.isBopInstalled && generator instanceof BOPMapGenVillageEventHandler) genClass = generator.getClass().getSuperclass();
		else genClass = generator.getClass();
		Method getStructureStart = getCorrectMethod(genClass, "getStructureStart");
		StructureStart start = (StructureStart) getStructureStart.invoke(generator, chunkX, chunkZ);
		return start.getBoundingBox();
	}

	private Method getCorrectMethod(Class<?> genClass, String method) {
		Method[] methods = genClass.getDeclaredMethods();
		for(int i=0;i<methods.length;i++) {
			//Makes this work in dev environments and production
			if(method.equals("canStructureSpawn")) {
				if(methods[i].getName().equals("canSpawnStructureAtCoords") || methods[i].getName().equals("func_75047_a")) {
					methods[i].setAccessible(true);
					return methods[i];
				}
			}
			else if(method.equals("getStructureStart")) {
				//Makes this work in dev environments and production
				if(methods[i].getName().equals("getStructureStart") || methods[i].getName().equals("func_75049_b")) {
					methods[i].setAccessible(true);
					return methods[i];
				}
			}
		}
		return null;
	}
}
