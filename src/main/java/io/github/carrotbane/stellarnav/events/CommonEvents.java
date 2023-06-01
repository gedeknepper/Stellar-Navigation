package io.github.carrotbane.stellarnav.events;

import java.io.IOException;

import io.github.carrotbane.stellarnav.StarHandler;
import io.github.carrotbane.stellarnav.StellarNavMod;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@EventBusSubscriber(modid = StellarNavMod.MOD_ID, bus = Bus.FORGE)
public class CommonEvents {
	
	@SubscribeEvent
	public static void stargaze(EntityEvent.EnteringChunk event) { // this runs twice (server+client?) and it also runs for each subchunk
		//WorldEvent.ChunkEvent
		if(event.getEntity() instanceof ClientPlayerEntity) {// && !event.getEntity().level.isClientSide) {
//			System.out.println("Entered a chunk");
			System.out.println("Client side? " + event.getEntity().level.isClientSide);
			int x = event.getNewChunkX();
			int z = event.getNewChunkZ();
			System.out.println("x: " + x + ", z: " + z);
			try {
				System.out.println("i'm inside a try");
//				StarHandler.makePics(event.getNewChunkX(), event.getNewChunkZ());
				StarHandler.makePics(x, z);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.out.println("i'm inside a catch");
				e.printStackTrace();
			}
		}
		
	}
	
}