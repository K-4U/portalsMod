package k4unl.minecraft.portals.tiles;

import k4unl.minecraft.portals.lib.config.Names;
import net.minecraft.tileentity.TileEntity;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;

public class Tiles {

	public static void init() {
		GameRegistry.registerTileEntity(TilePortalCore.class, "tilePortalCore");
		GameRegistry.registerTileEntity(TilePortalFrame.class, "tilePortalDummy");
		GameRegistry.registerTileEntity(TilePortalPortal.class, "tilePortalPortal");
		GameRegistry.registerTileEntity(TilePortalIndicator.class, "tilePortalIndicator");
		
		GameRegistry.registerTileEntity(TilePortalSpawner.class, "tilePortalSpawner");
	}


}
