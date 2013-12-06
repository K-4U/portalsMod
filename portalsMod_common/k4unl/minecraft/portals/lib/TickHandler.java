package k4unl.minecraft.portals.lib;

import java.util.ArrayList;
import java.util.EnumSet;

import k4unl.minecraft.portals.lib.config.Constants;
import k4unl.minecraft.portals.vars.PortalStorage;
import k4unl.minecraft.portals.vars.PortalStorage.teleportedEntity;
import k4unl.minecraft.portals.vars.Types.Location;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.TickType;

public class TickHandler implements ITickHandler {
	
	@Override
	public void tickStart(EnumSet<TickType> type, Object... tickData) {

	}

	@Override
	public void tickEnd(EnumSet<TickType> type, Object... tickData) {
		if(type.equals(EnumSet.of(TickType.CLIENT))){
			ArrayList<Integer> toRemove = new ArrayList<Integer>();
			ArrayList<teleportedEntity> l = PortalStorage.getTeleportedList();
			for(int i = 0;i < l.size(); i++){
				teleportedEntity entity = l.get(i);
				if(entity.teleportedAgo >= Constants.portalTimeout){
					toRemove.add(entity.entityId);
				}else{
					entity.teleportedAgo++;
				}
			}
			for(int toRemoveId: toRemove){
				PortalStorage.removeFromTeleportedList(toRemoveId);
			}
		}
	}

	@Override
	public EnumSet<TickType> ticks() {
		return EnumSet.of(TickType.CLIENT);
	}

	@Override
	public String getLabel() {
		// TODO Auto-generated method stub
		return null;
	}

}
