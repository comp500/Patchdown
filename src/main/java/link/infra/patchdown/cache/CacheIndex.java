package link.infra.patchdown.cache;

import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.moddiscovery.ModInfo;

import java.io.*;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CacheIndex {
	private Map<String, String> modVersions = new HashMap<>();
	private final Path file;

	public CacheIndex(Path file) {
		this.file = file;
	}

	public void load() throws IOException {
		try (BufferedReader r = new BufferedReader(new FileReader(file.toString()))) {
			String line;
			while ((line = r.readLine()) != null) {
				String[] lineSplit = line.split(":");
				if (lineSplit.length < 2) {
					continue;
				}
				String modID = lineSplit[0].trim();
				String modVersion = lineSplit[1].trim();
				modVersions.put(modID, modVersion);
			}
		}
	}

	public void save() throws IOException {
		try (BufferedWriter w = new BufferedWriter(new FileWriter(file.toString()))) {
			for (Map.Entry<String, String> entry : modVersions.entrySet()) {
				w.write(entry.getKey() + ":" + entry.getValue() + System.lineSeparator());
			}
		}
	}

	public String getCachedVersion(String modID) {
		return modVersions.get(modID);
	}

	public List<ModInfo> getModsToUpdate() {
		return ModList.get().getMods().stream().filter(this::needsUpdate).collect(Collectors.toList());
	}

	public boolean needsUpdate(ModInfo mod) {
		return needsUpdate(mod.getModId(), mod.getVersion().toString());
	}

	public boolean needsUpdate(String modID, String version) {
		String oldVersion = getCachedVersion(modID);
		if (version == null || version.trim().length() == 0) {
			return true;
		}
		return !version.equals(oldVersion);
	}

	public List<String> getModIDsToRemove() {
		List<String> modIDsInstalled = ModList.get().getMods().stream().map(ModInfo::getModId).collect(Collectors.toList());
		List<String> modIDsCached = new ArrayList<>(modVersions.keySet());
		modIDsCached.removeAll(modIDsInstalled);
		return modIDsCached;
	}

	public void updateVersions() {
		List<ModInfo> mods = ModList.get().getMods();
		modVersions.clear();
		for (ModInfo mod : mods) {
			modVersions.put(mod.getModId(), mod.getVersion().toString());
		}
	}
}
