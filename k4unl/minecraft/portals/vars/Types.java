package k4unl.minecraft.portals.vars;

import k4unl.minecraft.portals.vars.Types.Location;
import k4unl.minecraft.portals.vars.Types.portalColor;

public class Types {
	public static class portalColor{
		private int[] colors;
		public portalColor(int color1, int color2, int color3) {
			colors = new int[3];
			colors[0] = color1;
			colors[1] = color2;
			colors[2] = color3;
		}
		
		public portalColor(int[] aColors) {
			colors = new int[3];
			for(int i = 0; i<= 2; i++){
				colors[i] = aColors[i];
			}
		}
		
		public boolean equals(portalColor toTest){
			for(int i = 0; i<= 2; i++){
				if(colors[i] != toTest.getColor(i)){
					return false;
				}
			}
			return true;
		}
		
		public int[] getColors(){
			return colors;
		}
		
		public void setColor(int index, int newValue){
			colors[index] = newValue;
		}
		
		public int getColor(int index){
			return colors[index];
		}
	}
	
	public static class Location{
		private int x;
		private int y;
		private int z;
		
		
		public Location(int x, int y, int z) {
			this.x = x;
			this.y = y;
			this.z = z;
		}
		
		public Location(int[] coords) {
			if(coords.length >= 2){
				this.x = coords[0];
				this.y = coords[1];
				this.z = coords[2];
			}
		}
		
		public boolean equals(Location toTest){
			if(this.x == toTest.x && this.y == toTest.y && this.z == toTest.z){
				return true;
			}
			return false;
		}

		public void setLocation(int x, int y, int z) {
			this.x = x;
			this.y = y;
			this.z = z;
		}
		
		public void setLocation(int[] coords) {
			this.x = coords[0];
			this.y = coords[1];
			this.z = coords[2];
		}
		
		public int getX(){
			return this.x;
		}
		
		public int getY(){
			return this.y;
		}
		
		public int getZ(){
			return this.z;
		}
		
		public void setX(int newX){
			this.x = newX;
		}
		
		public void setY(int newY){
			this.y = newY;
		}
		
		public void setZ(int newZ){
			this.z = newZ;
		}

		public int[] getLocation() {
			int[] ret = new int[3];
			ret[0] = this.x;
			ret[1] = this.y;
			ret[2] = this.z;
			return ret;
		}
	}
}
