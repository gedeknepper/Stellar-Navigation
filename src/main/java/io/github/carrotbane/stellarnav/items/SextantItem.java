package io.github.carrotbane.stellarnav.items;

import java.io.IOException;

import io.github.carrotbane.stellarnav.StarHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;

public class SextantItem extends Item {

	public SextantItem(Properties props) {
		super(props);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public ActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
		// TODO Auto-generated method stub
		if(player.level.isClientSide) {
			Vector3d look = player.getLookAngle();
			double alt = Math.asin(look.y);
			//double az = look.x > 0 ? Math.acos(-look.z) : -Math.acos(-look.z) + 2 * Math.PI; 
			double az = look.x > 0 ? Math.atan(look.z/look.x) + Math.PI/2 : Math.atan(look.z/look.x)+ 1.5*Math.PI;  
			alt = alt*180/Math.PI;
			az = az*180/Math.PI;
			alt = 0.001*Math.round(alt*1000);
			az = 0.001*Math.round(az*1000);
			player.sendMessage((ITextComponent) new StringTextComponent("Altitude: " + alt + ", azimuth: " + az), player.getUUID());
		}
		return ActionResult.success(player.getMainHandItem());
	}

}
