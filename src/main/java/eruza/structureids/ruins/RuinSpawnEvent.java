package eruza.structureids.ruins;

import java.lang.reflect.Field;

import net.minecraft.world.World;
import atomicstryker.ruins.common.EventRuinTemplateSpawn;
import atomicstryker.ruins.common.RuinData;
import atomicstryker.ruins.common.RuinTemplate;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class RuinSpawnEvent {
	private int x, y, z, rotation;
	private RuinTemplate template;
	World world;
	private int embed;

	@SubscribeEvent
	public void ruinSpawn(EventRuinTemplateSpawn event)
	{
		world = event.world;
		try {
			getEventFields(event);
			setEmbed(template);
			RuinData data = template.getRuinData(x, y, z, rotation);
			ExtendedRuinData extData = new ExtendedRuinData(data, embed);
			UpdateChestEvent.ruinData.add(extData);
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Uses reflection to get the template embed value
	 * 
	 * @param template
	 * @throws NoSuchFieldException
	 * @throws SecurityException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	private void setEmbed(RuinTemplate template) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		//Embed is the 9th field in RuinTemplate.class
		Field templateFields[] = template.getClass().getDeclaredFields();
		templateFields[8].setAccessible(true);
		this.embed = templateFields[8].getInt(template);
	}
	/**
	 * Uses reflection to get x, y, z and template for ruin
	 * 
	 * @param event
	 * @throws IllegalAccessException
	 */
	private void getEventFields(EventRuinTemplateSpawn event) throws IllegalAccessException {
		Field eventFields[] = event.getClass().getDeclaredFields();
		for(int i=0;i<eventFields.length;i++) {
			eventFields[i].setAccessible(true);
		}
		template = (RuinTemplate) eventFields[0].get(event);
		x = eventFields[1].getInt(event);
		y = eventFields[2].getInt(event);
		z = eventFields[3].getInt(event);
		rotation = eventFields[4].getInt(event);
	}
}
