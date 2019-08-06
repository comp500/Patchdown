package link.infra.patchdown.cache;

import link.infra.patchdown.Patchdown;
import link.infra.patchdown.parse.BookLoader;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.fml.loading.FileUtils;
import net.minecraftforge.fml.loading.moddiscovery.ModInfo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

public class CacheManager {
	public static Path CACHE_PATH = FMLPaths.getOrCreateGameRelativePath(Paths.get(""), Patchdown.MODID + "_cache");
	private CacheIndex index = new CacheIndex(CACHE_PATH.resolve("versions.txt"));
	private static final Logger LOGGER = LogManager.getLogger();
	private BookLoader loader = new BookLoader();

	public void run() {
		try {
			index.load();
		} catch (IOException e) {
			LOGGER.warn("Failed to load versions index", e);
		}
		// Delete old mods
		index.getModIDsToRemove().forEach(m -> {
			try {
				Files.walkFileTree(getPathForMod(m), new SimpleFileVisitor<Path>() {
					@Override
					public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
						Files.delete(file);
						return FileVisitResult.CONTINUE;
					}

					@Override
					public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
						Files.delete(dir);
						return FileVisitResult.CONTINUE;
					}
				});
			} catch (IOException e) {
				LOGGER.debug("Error cleaning up old mods", e);
			}
		});

		index.getModsToUpdate().stream().filter(loader::hasBook).forEach(m -> {
			Path modFolder = FileUtils.getOrCreateDirectory(CACHE_PATH, m.getModId());
			// TODO: Clear out cache folders of mods that just updated
			loader.compileBook(m, modFolder);
		});

		index.updateVersions();
		try {
			index.save();
		} catch (IOException e) {
			LOGGER.warn("Failed to save cache index", e);
		}
	}

	public static Path getPathForMod(ModInfo info) {
		return getPathForMod(info.getModId());
	}

	public static Path getPathForMod(String modID) {
		return CACHE_PATH.resolve(modID);
	}
}
