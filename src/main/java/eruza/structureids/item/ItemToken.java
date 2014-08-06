package eruza.structureids.item;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import eruza.structureids.StructureIds;

public class ItemToken extends Item {

	public ItemToken() {
        this.maxStackSize = 16;
        this.setCreativeTab(CreativeTabs.tabMisc);
        this.setUnlocalizedName("itemSidToken");
	}
    
	@Override
	public void registerIcons(IIconRegister iconRegister)
	{
		itemIcon = iconRegister.registerIcon(StructureIds.MODID + ":sidtoken");
	}
}
