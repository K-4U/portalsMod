package k4unl.minecraft.portals.renderers;

import k4unl.minecraft.portals.tiles.TilePortalFrame;
import cpw.mods.fml.client.registry.ClientRegistry;

public class Renderers {
	public static void init() {
		ClientRegistry.bindTileEntitySpecialRenderer(TilePortalFrame.class, new RendererPortalFrame());
	}
}
