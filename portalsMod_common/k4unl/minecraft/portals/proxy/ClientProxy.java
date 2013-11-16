package k4unl.minecraft.portals.proxy;

import k4unl.minecraft.portals.renderers.Renderers;

public class ClientProxy extends CommonProxy {
	@Override
	public void initRenderers() {
		Renderers.init();
	}

	@Override
	public void initSounds() {

	}

}
