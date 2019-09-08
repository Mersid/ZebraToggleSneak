package eu.sajuk.tsdev.zebrastogglesneak;

import java.util.ArrayList;
import java.util.List;

import net.mersid.Configuration;
import net.mersid.Keyboard;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.MovementInputFromOptions;
import net.minecraftforge.client.event.InputEvent.KeyInputEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent.ClientTickEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(value = Reference.MODID)
public class ZebrasToggleSneak {

	public Configuration config;
	private final int configVersionMod = 1;
	private int configVersionFile = 0;
	private KeyBinding sneakBinding;
	private KeyBinding sprintBinding;
	private List<KeyBinding> kbList;
	private final Minecraft mc = Minecraft.getInstance();
	private final MovementInputModded mim = new MovementInputModded(mc.gameSettings, this);
	public final GuiDrawer guiDrawer = new GuiDrawer(this, mim);
	public IEventBus eventBus;
	
	public ZebrasToggleSneak()
	{
		eventBus = FMLJavaModLoadingContext.get().getModEventBus();
		eventBus.addListener(this::init);
		eventBus.addListener(this::preInit);
		eventBus.addListener(this::postInit);
	}

	public void preInit(FMLCommonSetupEvent event) {

	}

	public void init(FMLClientSetupEvent event) {
        kbList = getKeyBindings();
        for(KeyBinding kb: kbList) ClientRegistry.registerKeyBinding(kb);
        
        config = new Configuration("config/ToggleSneak");
        

		MinecraftForge.EVENT_BUS.register(this);
		config.load();
		config.save();
		
		guiDrawer.setDrawPosition(config.displayHPos, config.displayVPos, config.displayHPosOpts, config.displayVPosOpts);
	}

/*
	@SubscribeEvent
	public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent eventArgs) {

		if (eventArgs.getModID().equals("@MOD_ID@")) syncConfig();
	}
	
	private void upgradeConfigFrom(int version) {
		switch (version) {
		case 0:   // upgrade to version 1: convert displayStatus to string option
			if (config.hasKey(Configuration.CATEGORY_GENERAL,"displayEnabled")) {
				if (!config.hasKey(Configuration.CATEGORY_GENERAL,"statusDisplay")) 
					statusDisplay = config.getBoolean("displayEnabled", Configuration.CATEGORY_GENERAL, true, "dummy")
						? statusDisplayOpts[1] : statusDisplayOpts[0];
				config.getCategory(Configuration.CATEGORY_GENERAL).remove("displayEnabled");
			}
			break;
		}
	}
*/
	public List<KeyBinding> getKeyBindings() {
		
		List<KeyBinding> list = new ArrayList<KeyBinding>();		
		list.add(sneakBinding = new KeyBinding("zebrastogglesneak.key.toggle.sneak", Keyboard.getKeyIndex("G"), "zebrastogglesneak.key.categories"));
		list.add(sprintBinding = new KeyBinding("zebrastogglesneak.key.toggle.sprint", Keyboard.getKeyIndex("V"), "zebrastogglesneak.key.categories"));
		return list;
	}

	public void postInit(InterModProcessEvent event) {
	
		if (displayStatus() > 0) MinecraftForge.EVENT_BUS.register(guiDrawer);
	}

	@SubscribeEvent
	public void clientTick(ClientTickEvent event) {
		
		clientTick();
	}

	public void clientTick() {
		
		if ((mc.player != null) && (!(mc.player.movementInput instanceof MovementInputModded))) {
			mc.player.movementInput = mim;
		}
	}
	
	@SubscribeEvent
	public void onKeyInput(KeyInputEvent event) {

		for(KeyBinding kb: kbList) {
			if (kb.isKeyDown()) onKeyInput(kb);
		}
	}

	public void onKeyInput(KeyBinding kb) {
		
		if ((mc.currentScreen instanceof ChatScreen)) return;
		
		if (kb == sneakBinding)
		{
			config.toggleSneak = !config.toggleSneak;
			config.save();
			config.load();
		}
		
		if (kb == sprintBinding)
		{
			config.toggleSprint = !config.toggleSprint;
			config.save();
			config.load();
		}
	}
	
	public int displayStatus() {
		for (int i=0; i < config.statusDisplayOpts.length; i++) 
			if (config.statusDisplayOpts[i].equals(config.statusDisplay)) return i;
		return 0;
	}

}
