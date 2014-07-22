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
import atomicstryker.ruins.common.RuinRuleAir;
import atomicstryker.ruins.common.RuinTemplate;
import atomicstryker.ruins.common.RuinTemplateLayer;
import atomicstryker.ruins.common.RuinTemplateRule;

public class StructureSpawnEvent {
	private int x, y, z;
	private RuinTemplate template;
	ArrayList<RuinTemplateRule> rules = new ArrayList<RuinTemplateRule>();
	ArrayList<RuinTemplateLayer> layers = new ArrayList<RuinTemplateLayer>();

	@SubscribeEvent
	public void ruinSpawn(EventRuinTemplateSpawn event)
	{
		try {
			//get x, y, z and template for ruin
			setXYZAndTemplate(event);
			getTemplateRules();
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
	private void getTemplateRules()
			throws NoSuchFieldException, IllegalAccessException {
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
					}
				}
			}
		}
	}

	private void setXYZAndTemplate(EventRuinTemplateSpawn event)
			throws IllegalAccessException {
		Field eventFields[] = event.getClass().getDeclaredFields();
		for(int i=0;i<eventFields.length;i++) {
			eventFields[i].setAccessible(true);
		}
		template = (RuinTemplate) eventFields[0].get(event);
		x = (int) eventFields[1].get(event);
		y = (int) eventFields[2].get(event);
		z = (int) eventFields[3].get(event);
	}
}
