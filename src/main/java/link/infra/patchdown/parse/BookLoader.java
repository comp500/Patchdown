package link.infra.patchdown.parse;


import link.infra.patchdown.Patchdown;
import net.minecraftforge.fml.loading.moddiscovery.ModInfo;

import java.nio.file.Path;

public class BookLoader {
	// TODO: make a proper API to load books at runtime

	public static final String BOOKS_FOLDER = Patchdown.MODID + "_books";

	public boolean hasBook(ModInfo mod) {
		return false;
	}

	public void compileBook(ModInfo mod, Path cachePath) {

	}

}
