package eruza.structureids;

import atomicstryker.ruins.common.RuinData;

public class ExtendedRuinData extends RuinData {
	private ExtendedRuinData(int xmin, int xmax, int ymin, int ymax, int zmin, int zmax, String n) {
		super(xmin, xmax, ymin, ymax, zmin, zmax, n);
	}
	
	public ExtendedRuinData(RuinData data, int embed) {
		this(data.xMin, data.xMax, data.yMin-embed, data.yMax, data.zMin, data.zMax, data.name);
		
	}
}
