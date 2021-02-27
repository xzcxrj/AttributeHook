package gitee.xzcxrj.attributehook;

import github.saukiya.sxattribute.data.attribute.AttributeMap;
import github.saukiya.sxattribute.data.attribute.SXAttributeData;
import github.saukiya.sxattribute.data.attribute.SubAttribute;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class AttributeHook extends JavaPlugin {

    private static String version;

    @Override
    public void onEnable() {
        version = BukkitLoader.getVersion("SX-Attribute");
        api.init();
    }


    public static String getSXAttributeVersion() {
        return version;
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

        private static final Map<String, SubAttribute> attributes = new HashMap<>();

        @SuppressWarnings("unchecked")
        private static void init() {
            try {
                Class<?> plugin = Class.forName("github.saukiya.sxattribute.SXAttribute");
                getApi = plugin.getMethod("getApi");
                Class<?> apiClass;
                Class<?> conditionTypeClass;
                Class<?> attributeClass = Class.forName("github.saukiya.sxattribute.data.attribute.SubAttribute");

                if (version.contains("2.0.2")) {
                    apiClass = Class.forName("github.saukiya.sxattribute.api.SXAttributeAPI");
                    conditionTypeClass = Class.forName("github.saukiya.sxattribute.data.condition.SXConditionType");
                    loadListData = apiClass.getMethod("getLoreData", LivingEntity.class, conditionTypeClass, List.class);
                    loadItemData = apiClass.getMethod("getItemData", LivingEntity.class, conditionTypeClass, ItemStack[].class);
                    attributeUpdate = apiClass.getMethod("updateStats", LivingEntity.class);
                    updateData = apiClass.getMethod("updateSlotData", Player.class);
                    getEntityData = apiClass.getMethod("getEntityAllData", LivingEntity.class, SXAttributeData[].class);
                    hasEntityAPIData = apiClass.getMethod("isEntityAPIData", Class.class, UUID.class);
                    Field attributeMap = attributeClass.getDeclaredField("attributeMap");
                    attributeMap.setAccessible(true);
                    AttributeMap map = (AttributeMap) attributeMap.get(null);
                    for (Map.Entry<Integer, SubAttribute> entry : map.entrySet()) {
                        attributes.put(entry.getValue().getName().toUpperCase(), entry.getValue());
                    }
                }else {
                    apiClass = Class.forName("github.saukiya.sxattribute.api.SXAPI");
                    loadListData = apiClass.getMethod("loadListData", List.class);
                    loadItemData = apiClass.getMethod("loadItemData", LivingEntity.class, ItemStack[].class);
                    attributeUpdate = apiClass.getMethod("attributeUpdate", LivingEntity.class);
                    updateData = apiClass.getMethod("updateData", LivingEntity.class);
                    getEntityData = apiClass.getMethod("getEntityData", LivingEntity.class);
                    hasEntityAPIData = apiClass.getMethod("hasEntityAPIData", Class.class, UUID.class);
                    Field attributeMap = attributeClass.getDeclaredField("attributes");
                    attributeMap.setAccessible(true);
                    List<SubAttribute> list = (List<SubAttribute>) attributeMap.get(null);
                    for (SubAttribute attribute : list) {
                        attributes.put(attribute.getName().toUpperCase(), attribute);
                    }
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
            } catch (ClassNotFoundException | NoSuchMethodException | NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        public static SubAttribute getSubAttribute(String name) {
            return attributes.get(name.toUpperCase());
        }

        public static Collection<SubAttribute> getAttributes() {
            return attributes.values();
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

        public static AttributeData getProjectileData(UUID uuid) {
            try {
                return new AttributeData().add(((SXAttributeData) getProjectileData.invoke(getApi.invoke(null), uuid)));
            } catch (IllegalAccessException | InvocationTargetException e) {
                return new AttributeData();
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

        public static AttributeData getEntityAPIData(JavaPlugin plugin, UUID uuid) {
            AttributeData data = new AttributeData();
            try {
                return data.add((SXAttributeData) getEntityAPIData.invoke(getApi.invoke(null), plugin.getClass(), uuid));
            } catch (IllegalAccessException | InvocationTargetException e) {
                return data;
            }
        }

        public static AttributeData getEntityData(LivingEntity livingEntity) {
            AttributeData data = new AttributeData();
            try {
                if (version.contains("2.0.2")) {
                    SXAttributeData[] array = new SXAttributeData[]{new SXAttributeData()};
                    data.add((SXAttributeData) getEntityData.invoke(getApi.invoke(null), livingEntity, array));
                }else {
                    data.add((SXAttributeData) getEntityData.invoke(getApi.invoke(null), livingEntity));
                }
            } catch (IllegalAccessException | InvocationTargetException ignored) {
            }
            return data;
        }

        public static AttributeData loadListData(List<String> list) {
            AttributeData data = new AttributeData();
            try {
                if (version.contains("2.0.2")) {
                    data.add((SXAttributeData) loadListData.invoke(getApi.invoke(null), null, null, list));
                }else {
                    data.add((SXAttributeData) loadListData.invoke(getApi.invoke(null), list));
                }
            } catch (IllegalAccessException | InvocationTargetException ignored) {
            }
            return data;
        }

        public static AttributeData loadItemData(LivingEntity entity, ItemStack... itemStacks) {
            AttributeData data = new AttributeData();
            ItemStack[] items;
            if (itemStacks.length < 1) {
                items = new ItemStack[] {new ItemStack(Material.AIR)};
            }else {
                items = itemStacks;
            }
            try {
                if (version.contains("2.0.2")) {
                    data.add((SXAttributeData) loadItemData.invoke(getApi.invoke(null), entity, null, items));
                }else {
                    data.add((SXAttributeData) loadItemData.invoke(getApi.invoke(null), entity, items));
                }
            } catch (IllegalAccessException | InvocationTargetException ignored) {
            }
            return data;
        }
    }
}
