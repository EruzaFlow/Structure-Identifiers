package eruza.structureids.vanilla;

import net.minecraft.world.gen.structure.StructureBoundingBox;

public class NamedBoundingBox extends StructureBoundingBox {
	public final String name;
	
	public NamedBoundingBox(StructureBoundingBox box, String name) {
		super(box);
		this.name = name;
	}
	
	@Override
	public String toString() {
		return name + " " + super.toString();
	}

}