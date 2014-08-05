package eruza.structureids.vanilla;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import net.minecraft.world.gen.MapGenBase;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureStart;
import net.minecraftforge.event.terraingen.PopulateChunkEvent;
import net.minecraftforge.event.terraingen.PopulateChunkEvent.Populate;
import net.minecraftforge.event.terraingen.PopulateChunkEvent.Populate.EventType;
import biomesoplenty.common.eventhandler.world.BOPMapGenVillageEventHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import eruza.structureids.StructureIds;

public class VanillaChunkPopEvent {

	@SubscribeEvent
	public void populateEvent(PopulateChunkEvent.Populate event) {
		listInfo(event);
		testStructureAndUpdateChest(event, MapGen.villageGenerator, "Village");
		testStructureAndUpdateChest(event, MapGen.mineshaftGenerator, "Mineshaft");
		testStructureAndUpdateChest(event, MapGen.strongholdGenerator, "Stronghold");
		//Below line sometimes causes illegal argument exceptions due to reflection, not sure why
		//listInfo(event, MapGen.scatteredFeatureGenerator, "Scattered Features");
	}

	private void listInfo(Populate event) {
		//TODO Check that each type happens for every chunk
		//If so add filter for a single type for efficiency
		//possibly switch this to a generator like roguelike?
		if(event.type == EventType.DUNGEON) {
			
		}
		if(event.type == EventType.CUSTOM) {
			System.out.println("Custome event detected!");
		}
	}

	private void testStructureAndUpdateChest(Populate event, MapGenBase generator, String name) {
		try {
			if(canStructureSpawn(event.chunkX, event.chunkZ, generator)) {
				NamedBoundingBox box = new NamedBoundingBox(getStructureInChunk(event.chunkX, event.chunkZ, generator), name);
				LocateVanillaChest.addBox(box);
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
