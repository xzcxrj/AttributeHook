package gitee.xzcxrj.attributehook;

import github.saukiya.sxattribute.data.attribute.SXAttributeData;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.UUID;

public class AttributeHook extends JavaPlugin {

    private static String version;

    @Override
    public void onEnable() {
        version = BukkitLoader.getVersion("SX-Attribute");
        api.init();
    }

    public static final class api {

        private static Method getApi;
        private static Method loadListData;
        private static Method setProjectileData;
        private static Method getProjectileData;
        private static Method setEntityAPIData;
        private static Method removeEntityAPIData;
        private static Method attributeUpdate;
        private static Method updateData;
        private static Method loadItemData;
        private static Method getEntityData;
        private static Method getEntityAPIData;
        private static Method hasEntityAPIData;
        private static Method removePluginAllEntityData;
        private static Method removeEntityAllPluginData;
        private static Method getEntityName;
        private static Method getItemLevel;

        private static void init() {
            try {
                Class<?> plugin = Class.forName("github.saukiya.sxattribute.SXAttribute");
                getApi = plugin.getMethod("getApi");
                Class<?> apiClass;
                Class<?> conditionTypeClass;
                if (version.contains("2.0.2")) {
                    apiClass = Class.forName("github.saukiya.sxattribute.api.SXAttributeAPI");
                    conditionTypeClass = Class.forName("github.saukiya.sxattribute.data.condition.SXConditionType");
                    loadListData = apiClass.getMethod("getLoreData", LivingEntity.class, conditionTypeClass, List.class);
                    loadItemData = apiClass.getMethod("getItemData", LivingEntity.class, conditionTypeClass, ItemStack[].class);
                    attributeUpdate = apiClass.getMethod("updateStats", LivingEntity.class);
                    updateData = apiClass.getMethod("updateSlotData", Player.class);
                    getEntityData = apiClass.getMethod("getEntityAllData", LivingEntity.class, SXAttributeData[].class);
                    hasEntityAPIData = apiClass.getMethod("isEntityAPIData", Class.class, UUID.class);
                }else {
                    apiClass = Class.forName("github.saukiya.sxattribute.api.SXAPI");
                    loadListData = apiClass.getMethod("loadListData", List.class);
                    loadItemData = apiClass.getMethod("loadItemData", LivingEntity.class, ItemStack[].class);
                    attributeUpdate = apiClass.getMethod("attributeUpdate", LivingEntity.class);
                    updateData = apiClass.getMethod("updateData", LivingEntity.class);
                    getEntityData = apiClass.getMethod("getEntityData", LivingEntity.class);
                    hasEntityAPIData = apiClass.getMethod("hasEntityAPIData", Class.class, UUID.class);
                }
                setEntityAPIData = apiClass.getMethod("setEntityAPIData", Class.class, UUID.class, SXAttributeData.class);
                setProjectileData = apiClass.getMethod("setProjectileData", UUID.class, SXAttributeData.class);
                getProjectileData = apiClass.getMethod("getProjectileData", UUID.class);
                removeEntityAPIData = apiClass.getMethod("removeEntityAPIData", Class.class, UUID.class);
                getEntityAPIData = apiClass.getMethod("getEntityAPIData", Class.class, UUID.class);
                removePluginAllEntityData = apiClass.getMethod("removePluginAllEntityData", Class.class);
                removeEntityAllPluginData = apiClass.getMethod("removeEntityAllPluginData", UUID.class);
                getEntityName = apiClass.getMethod("getEntityName", LivingEntity.class);
                getItemLevel = apiClass.getMethod("getItemLevel", ItemStack.class);
            } catch (ClassNotFoundException | NoSuchMethodException e) {
                e.printStackTrace();
            }
        }

        public static int getItemLevel(ItemStack itemStack) {
            try {
                return (Integer)getItemLevel.invoke(getApi.invoke(null), itemStack);
            } catch (IllegalAccessException | InvocationTargetException e) {
                return 0;
            }
        }

        public static String getEntityName(LivingEntity entity) {
            try {
                return (String)getEntityName.invoke(getApi.invoke(null), entity);
            } catch (IllegalAccessException | InvocationTargetException e) {
                return "none";
            }
        }

        public static boolean hasEntityAPIData(JavaPlugin plugin, UUID uuid) {
            try {
                Object invoke = hasEntityAPIData.invoke(getApi.invoke(null), plugin.getClass(), uuid);
                return String.valueOf(invoke).equalsIgnoreCase("true");
            } catch (IllegalAccessException | InvocationTargetException e) {
                return false;
            }
        }

        public static void removePluginAllEntityData(JavaPlugin plugin) {
            try {
                removePluginAllEntityData.invoke(getApi.invoke(null), plugin.getClass());
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }

        public static void removeEntityAllPluginData(UUID uuid) {
            try {
                removeEntityAllPluginData.invoke(getApi.invoke(null), uuid);
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }

        public static void setProjectileData(UUID uuid, SXAttributeData data) {
            try {
                setProjectileData.invoke(getApi.invoke(null), uuid, data);
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }

        public static SXAttributeData getProjectileData(UUID uuid) {
            try {
                return (SXAttributeData) getProjectileData.invoke(getApi.invoke(null), uuid);
            } catch (IllegalAccessException | InvocationTargetException e) {
                return new SXAttributeData();
            }
        }

        public static void setEntityAPIData(JavaPlugin plugin, UUID uuid, SXAttributeData data) {
            try {
                setEntityAPIData.invoke(getApi.invoke(null), plugin.getClass(), uuid, data);
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }

        public static void removeEntityAPIData(JavaPlugin plugin, UUID uuid) {
            try {
                removeEntityAPIData.invoke(getApi.invoke(null), plugin.getClass(), uuid);
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }

        public static void updateData(Player player) {
            try {
                updateData.invoke(getApi.invoke(null), player);
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }

        public static void attributeUpdate(LivingEntity entityAPIData) {
            try {
                attributeUpdate.invoke(getApi.invoke(null), entityAPIData);
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }

        public static SXAttributeData getEntityAPIData(JavaPlugin plugin, UUID uuid) {
            try {
                return (SXAttributeData) getEntityAPIData.invoke(getApi.invoke(null), plugin.getClass(), uuid);
            } catch (IllegalAccessException | InvocationTargetException e) {
                return new SXAttributeData();
            }
        }

        public static SXAttributeData getEntityData(LivingEntity livingEntity) {
            try {
                if (version.contains("2.0.2")) {
                    return (SXAttributeData) getEntityData.invoke(getApi.invoke(null), livingEntity, new SXAttributeData(){});
                }
                return (SXAttributeData) getEntityData.invoke(getApi.invoke(null), livingEntity);
            } catch (IllegalAccessException | InvocationTargetException e) {
                return new SXAttributeData();
            }
        }

        public static SXAttributeData loadListData(List<String> list) {
            try {
                if (version.contains("2.0.2")) {
                    return (SXAttributeData) loadListData.invoke(getApi.invoke(null), null, null, list);
                }
                return (SXAttributeData) loadListData.invoke(getApi.invoke(null), list);
            } catch (IllegalAccessException | InvocationTargetException e) {
                return new SXAttributeData();
            }
        }

        public static SXAttributeData loadItemData(LivingEntity entity, ItemStack... itemStacks) {
            try {
                if (version.contains("2.0.2")) {
                    return (SXAttributeData) loadItemData.invoke(getApi.invoke(null), entity, null, itemStacks);
                }
                return (SXAttributeData) loadItemData.invoke(getApi.invoke(null), entity, itemStacks);
            } catch (IllegalAccessException | InvocationTargetException e) {
                return new SXAttributeData();
            }
        }
    }
}