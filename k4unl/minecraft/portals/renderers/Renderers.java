package k4unl.minecraft.portals.renderers;

import k4unl.minecraft.portals.tiles.TilePortalFrame;
import k4unl.minecraft.portals.tiles.TilePortalIndicator;
import k4unl.minecraft.portals.tiles.TilePortalSpawner;
import cpw.mods.fml.client.registry.ClientRegistry;

public class Renderers {
	public static void init() {
		ClientRegistry.bindTileEntitySpecialRenderer(TilePortalFrame.class, new RendererPortalFrame());
		ClientRegistry.bindTileEntitySpecialRenderer(TilePortalIndicator.class, new RendererPortalIndicator());
		ClientRegistry.bindTileEntitySpecialRenderer(TilePortalSpawner.class, new RendererPortalSpawner());
	}
	
}
