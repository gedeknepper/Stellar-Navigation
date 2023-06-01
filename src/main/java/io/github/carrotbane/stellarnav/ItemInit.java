package io.github.carrotbane.stellarnav;

import io.github.carrotbane.stellarnav.items.SextantItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ItemInit {
	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, StellarNavMod.MOD_ID);
			
	public static final RegistryObject<SextantItem> SEXTANT = ITEMS.register("sextant", () -> new SextantItem(new Item.Properties().tab(ItemGroup.TAB_BREWING)));

}
