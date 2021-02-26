package gitee.xzcxrj.attributehook;

import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

/**
 * @author DogLong
 */
public class BukkitLoader {

    private static File FOLDER;
    private static final Map<String, String> PLUGIN_VERSION = new HashMap<>();

    private BukkitLoader() {
    }

    static {
        String filename = null;
        try {
            filename = URLDecoder.decode(JavaPlugin.class.getProtectionDomain().getCodeSource().getLocation().getPath(), StandardCharsets.UTF_8.toString());
        } catch (UnsupportedEncodingException e) {
            try {
                filename = URLDecoder.decode(JavaPlugin.class.getProtectionDomain().getCodeSource().getLocation().getPath(), StandardCharsets.ISO_8859_1.toString());
            } catch (UnsupportedEncodingException e1) {
                e1.printStackTrace();
            }
        }
        if (filename != null) {
            File file = new File(filename);
            FOLDER = new File(file.getParent());
        }
        init();
    }

    /**
     * 获取服务器目录里的某个文件
     * @param file file
     * @return file
     */
    public static File getFile(String file) {
        for (File listFile : Objects.requireNonNull(FOLDER.listFiles())) {
            if (listFile.getName().equalsIgnoreCase(file)) {
                return listFile;
            }
        }
        return null;
    }

    /**
     * 获取插件的版本
     * @param plugin plugin
     * @return version
     */
    public static String getVersion(String plugin) {
        return PLUGIN_VERSION.getOrDefault(plugin.toUpperCase(), "none plugin");
    }

    private static void init(){
        File plugins = getFile("plugins");
        if (plugins == null) {
            return;
        }
        FilenameFilter fileNameFilter = (dir, name) -> name.endsWith(".jar");
        File[] jars = plugins.listFiles(fileNameFilter);
        if (jars == null) {
            return;
        }
        for (File file : jars) {
            JarFile jarFile = null;
            BufferedReader reader = null;
            try {
                jarFile = new JarFile(file);
                Enumeration<JarEntry> jarEnters = jarFile.entries();
                while(jarEnters.hasMoreElements()){
                    ZipEntry entry = jarEnters.nextElement();
                    String name = entry.getName();
                    if (name.isEmpty()) {
                        continue;
                    }
                    if (!name.endsWith("plugin.yml")) {
                        continue;
                    }
                    reader = new BufferedReader(new InputStreamReader(jarFile.getInputStream(entry)));
                    String pluginName = "";
                    String pluginVersion = "";
                    while (reader.ready()) {
                        String line = reader.readLine();
                        if (line.startsWith("name:")) {
                            pluginName = line.replaceAll("name:", "").replaceAll(" ", "");
                        }else if (line.startsWith("version:")) {
                            pluginVersion = line.replaceAll("version:", "").replaceAll(" ", "");
                        }
                    }
                    PLUGIN_VERSION.put(pluginName.toUpperCase(), pluginVersion);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                try {
                    if (jarFile != null) {
                        jarFile.close();
                    }
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
