package k4unl.minecraft.portals;

import java.util.logging.Level;

import k4unl.minecraft.portals.blocks.Blocks;
import k4unl.minecraft.portals.lib.ConfigHandler;
import k4unl.minecraft.portals.lib.LogHelper;
import k4unl.minecraft.portals.lib.TickHandler;
import k4unl.minecraft.portals.lib.config.ModInfo;
import k4unl.minecraft.portals.lib.config.Recipes;
import k4unl.minecraft.portals.proxy.CommonProxy;
import k4unl.minecraft.portals.renderers.Renderers;
import k4unl.minecraft.portals.tiles.Tiles;
import k4unl.minecraft.portals.vars.PortalStorage;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.Side;
 
@Mod( modid = ModInfo.ID, name = ModInfo.NAME, version = ModInfo.VERSION) 
@NetworkMod ( channels = {ModInfo.CHANNEL}, clientSideRequired = true, serverSideRequired = true )
public class PortalsMain {
    // The instance of the mod that Forge uses.
    @Instance(value = "k4unlPortals")
    public static PortalsMain instance;
   
    @SidedProxy( clientSide = ModInfo.PROXY_LOCATION + ".ClientProxy", serverSide = ModInfo.PROXY_LOCATION + ".CommonProxy")
    public static CommonProxy proxy;
    
    @EventHandler 
    public void preInit(FMLPreInitializationEvent event) {
    	LogHelper.init();
    	LogHelper.log(Level.INFO, "Starting K-4U Portals mod!");
    	
    	//Open configs
    	ConfigHandler.init(event.getSuggestedConfigurationFile());
    	
    	//Load proxy. I still have no idea what this does.
    	proxy.initRenderers();
    	proxy.initSounds();
    	
    }
   
    @EventHandler 
    public void load(FMLInitializationEvent event) {
    	TickRegistry.registerTickHandler(new TickHandler(), Side.CLIENT);
    	
    	Blocks.init();
    	Tiles.init();
    	Recipes.init();
    	Renderers.init();
    	
    	
    	PortalStorage.init();
    	
    	
    }
   
    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
            // Stub Method
    }
}