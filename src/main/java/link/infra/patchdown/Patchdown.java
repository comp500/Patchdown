package link.infra.patchdown;

import link.infra.patchdown.cache.CacheManager;
import link.infra.patchdown.convert.ParseTest;
import net.minecraft.block.Blocks;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.stream.Collectors;

@Mod("patchdown")
public class Patchdown {
	public static final String MODID = "patchdown";
	private static final Logger LOGGER = LogManager.getLogger();

	public Patchdown() {
//		// Register the setup method for modloading
//		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
//		// Register the enqueueIMC method for modloading
//		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::enqueueIMC);
//		// Register the processIMC method for modloading
//		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::processIMC);
//		// Register the doClientStuff method for modloading
//		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);
//
//		// Register ourselves for server and other game events we are interested in
//		MinecraftForge.EVENT_BUS.register(this);
		// TODO: what do we do on the server side? what does Patchouli need there?
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);
	}

	private void setup(final FMLCommonSetupEvent event) {
		// some preinit code
		LOGGER.info("HELLO FROM PREINIT");
		LOGGER.info("DIRT BLOCK >> {}", Blocks.DIRT.getRegistryName());
		ParseTest.test();
	}

	private void doClientStuff(final FMLClientSetupEvent event) {
		// do something that can only be done on the client
		//LOGGER.info("Got game settings {}", event.getMinecraftSupplier().get().gameSettings);
		CacheManager mgr = new CacheManager();
		mgr.run();
	}

	private void enqueueIMC(final InterModEnqueueEvent event) {
		// some example code to dispatch IMC to another mod
		InterModComms.sendTo("examplemod", "helloworld", () -> {
			LOGGER.info("Hello world from the MDK");
			return "Hello world";
		});
	}

	private void processIMC(final InterModProcessEvent event) {
		// some example code to receive and process InterModComms from other mods
		LOGGER.info("Got IMC {}", event.getIMCStream().
				map(m -> m.getMessageSupplier().get()).
				collect(Collectors.toList()));
	}
}
