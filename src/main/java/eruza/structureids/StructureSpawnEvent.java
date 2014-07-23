package eruza.structureids;

import java.lang.reflect.Field;
import java.util.ArrayList;

import net.minecraft.world.World;
import atomicstryker.ruins.common.EventRuinTemplateSpawn;
import atomicstryker.ruins.common.RuinData;
import atomicstryker.ruins.common.RuinRuleAir;
import atomicstryker.ruins.common.RuinTemplate;
import atomicstryker.ruins.common.RuinTemplateLayer;
import atomicstryker.ruins.common.RuinTemplateRule;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class StructureSpawnEvent {
	private int x, y, z, rotation;
	private RuinTemplate template;
	ArrayList<RuinTemplateRule> rules;
	ArrayList<RuinTemplateLayer> layers;
	private ArrayList<Integer> chestRules;
	World world;
	private int embed;

	@SubscribeEvent
	public void ruinSpawn(EventRuinTemplateSpawn event)
	{
		layers = new ArrayList<RuinTemplateLayer>();
		chestRules = new ArrayList<Integer>();
		rules = new ArrayList<RuinTemplateRule>();
		world = event.world;
		try {
			//get x, y, z and template for ruin
			getEventFields(event);
			setChestRules();
			setEmbed(template);
			RuinData data = template.getRuinData(x, y, z, rotation);
			if(this.chestRules.size() > 0) {
				if(findChestCoords()) {
					ExtendedRuinData extData = new ExtendedRuinData(data, embed);
					UpdateChestEvent.ruinData.add(extData);
				}
				else {
					System.out.println("ERROR TMPL HAS RULE BUT NO CHEST");
				}
			}
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String name = "";
		if(template != null) name = template.getName();
		System.out.println(name + " RUIN SPAWNED @ " + x + " " + y + " " + z);
	}

	@SuppressWarnings("unchecked")
	private boolean findChestCoords() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		Field layersField = template.getClass().getDeclaredField("layers");
		layersField.setAccessible(true);
		layers.addAll((ArrayList<RuinTemplateLayer>) layersField.get(template));
		for(int i=0;i<layers.size();i++) {
			RuinTemplateLayer layer = layers.get(i);
			Field layerField = layer.getClass().getDeclaredField("layer");
			layerField.setAccessible(true);
			int[][] layerCoords = (int[][]) layerField.get(layer);
			if(printLayerCoords(layerCoords, i)) return true;
		}
		return false;
	}
	
	private void setEmbed(RuinTemplate template) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		Field templateField = template.getClass().getDeclaredField("embed");
		templateField.setAccessible(true);
		this.embed = templateField.getInt(template);
	}

	private boolean printLayerCoords(int[][] layerCoords, int layerNum) {
		for(int i=0;i<layerCoords.length;i++) {
			for(int j=0;j<layerCoords[i].length;j++) {
				Integer layerId = layerCoords[i][j];
				if(chestRules.contains(layerId)) {
					return true;
				}
			}
		}
		return false;
	}

	private void getEventFields(EventRuinTemplateSpawn event) throws IllegalAccessException {
		Field eventFields[] = event.getClass().getDeclaredFields();
		for(int i=0;i<eventFields.length;i++) {
			eventFields[i].setAccessible(true);
		}
		template = (RuinTemplate) eventFields[0].get(event);
		x = (int) eventFields[1].get(event);
		y = (int) eventFields[2].get(event);
		z = (int) eventFields[3].get(event);
		rotation = eventFields[4].getInt(event);
	}

	@SuppressWarnings("unchecked")
	private void setChestRules() throws NoSuchFieldException, IllegalAccessException {
		String[] blockStrings;
		Field templateField = template.getClass().getDeclaredField("rules");
		templateField.setAccessible(true);
		rules.addAll((ArrayList<RuinTemplateRule>) templateField.get(template));
		for(int i=0;i<rules.size();i++) {
			RuinTemplateRule rule = rules.get(i);
			if(!(rule instanceof RuinRuleAir)) {
				Field blockStringsField = rule.getClass().getDeclaredField("blockStrings");
				blockStringsField.setAccessible(true);
				blockStrings = (String[]) blockStringsField.get(rule);
				for(int j=0;j<blockStrings.length;j++) {
					if(blockStrings[j].toLowerCase().contains("chest")) {
						this.chestRules.add(i);
					}
				}
			}
		}
	}
}
