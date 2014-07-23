package eruza.structureids;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Random;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.world.World;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import atomicstryker.ruins.common.EventRuinTemplateSpawn;
import atomicstryker.ruins.common.RuinData;
import atomicstryker.ruins.common.RuinRuleAir;
import atomicstryker.ruins.common.RuinTemplate;
import atomicstryker.ruins.common.RuinTemplateLayer;
import atomicstryker.ruins.common.RuinTemplateRule;
import atomicstryker.ruins.common.RuinsMod;

public class StructureSpawnEvent {
	private int x, y, z, rotation, chestX, chestY, chestZ;
	private RuinTemplate template;
	ArrayList<RuinTemplateRule> rules;
	ArrayList<RuinTemplateLayer> layers;
	private ArrayList<Integer> chestRules;

	@SubscribeEvent
	public void ruinSpawn(EventRuinTemplateSpawn event)
	{
		rules = new ArrayList<RuinTemplateRule>();
		layers = new ArrayList<RuinTemplateLayer>();
		chestRules = new ArrayList<Integer>();
		try {
			//get x, y, z and template for ruin
			getEventFields(event);
			setChestRules();
			findChestCoords();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		}

		String name = "";
		if(template != null) name = template.getName();
		System.out.println(name + " RUIN SPAWNED @ " + x + " " + y + " " + z);

		World world = event.world;
		int meta = 0;
		int items = 1;
		Random random = new Random();

		//Temp testing; adds a chest 10 blocks above origin of template
		world.setBlock(x, y+10, z, Blocks.chest, meta, 3);
		world.setBlockMetadataWithNotify(x, y+10, z, meta, 3);
		TileEntityChest chest = (TileEntityChest) world.getTileEntity(x, y+10, z);
		if (chest != null)
		{
			ItemStack stack = null;
			for (int i = 0; i < items; i++)
			{
				stack = new ItemStack(Items.paper);
				stack.setStackDisplayName(name.replace(".tml", ""));
				if (stack != null)
				{
					chest.setInventorySlotContents(random.nextInt(chest.getSizeInventory()), stack);
				}
			}
		}
	}

	@SuppressWarnings("unchecked")
	private void findChestCoords() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		Field layersField = template.getClass().getDeclaredField("layers");
		layersField.setAccessible(true);
		layers.addAll((ArrayList<RuinTemplateLayer>) layersField.get(template));
		for(int i=0;i<layers.size();i++) {
			RuinTemplateLayer layer = layers.get(i);
			Field layerField = layer.getClass().getDeclaredField("layer");
			layerField.setAccessible(true);
			int[][] layerCoords = (int[][]) layerField.get(layer);
			printLayerCoords(layerCoords, i);
		}
	}

	private void printLayerCoords(int[][] layerCoords, int layerNum) {
		for(int i=0;i<layerCoords.length;i++) {
			for(int j=0;j<layerCoords[i].length;j++) {
				Integer layerId = layerCoords[i][j];
				if(chestRules.contains(layerId)) {
					System.out.println("Layer " + layerNum + " has chest at: "  + i + " " + j);
					shiftCoords(j, layerNum, i);
				}
/*				for(int k=0;k<chestRules.size();k++) {
					if(chestRules.get(k) == layerId) {
						System.out.println("Chest has rule at " + i + " " + j);
					}
				}*/
			}
		}
	}
	
	private void shiftCoords(int j, int layerNum, int i) {
		RuinData data = template.getRuinData(x, y, z, rotation);
		int x, y, z;
		x = j + data.xMid;
		y = layerNum + data.yMid;
		z = i + data.zMid;
		System.out.println("Anticipating chest at " + x + " " + y + " " + z);
	}

	@SuppressWarnings("unchecked")
	private void setChestRules() throws NoSuchFieldException, IllegalAccessException {
		String[] blockStrings;
		Field rulesField = template.getClass().getDeclaredField("rules");
		rulesField.setAccessible(true);
		rules.addAll((ArrayList<RuinTemplateRule>) rulesField.get(template));
		for(int i=0;i<rules.size();i++) {
			RuinTemplateRule rule = rules.get(i);
			if(!(rule instanceof RuinRuleAir)) {
				Field blockStringsField = rule.getClass().getDeclaredField("blockStrings");
				blockStringsField.setAccessible(true);
				blockStrings = (String[]) blockStringsField.get(rule);
				for(int j=0;j<blockStrings.length;j++) {
					if(blockStrings[j].toLowerCase().contains("chest")) {
						System.out.println("Rule " + i + " contains 'chest' " + blockStrings[j]);
						this.chestRules.add(i);
					}
				}
			}
		}
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
}
