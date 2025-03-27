package JGamePackage.JGame.Classes.Services;

import java.awt.Color;
import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import JGamePackage.JGame.Classes.Instance;
import JGamePackage.JGame.Classes.Abstracts.AbstractImage;
import JGamePackage.JGame.Classes.Scripts.Script;
import JGamePackage.JGame.Classes.Scripts.Writable.WritableScript;
import JGamePackage.JGame.Types.PointObjects.UDim2;
import JGamePackage.JGame.Types.PointObjects.Vector2;
import JGamePackage.lib.JSONSimple.JSONArray;
import JGamePackage.lib.JSONSimple.JSONObject;
import JGamePackage.lib.JSONSimple.parser.JSONParser;
import JGamePackage.lib.Signal.AbstractSignalInstance;


public class SerializationService extends Service {

    public static final int NODE_WORLD_IDENTIFIER = -1;
    public static final int NODE_UI_IDENTIFIER = -2;
    public static final int STORAGE_WORLD_IDENTIFIER = -3;
    public static final int NODE_SCRIPT_IDENTIFIER = -4;
    public static final int NULL_IDENTIFIER = -5;

    @SuppressWarnings("unused")
    private List<String> instantiateIgnoreList = List.of("Node");

    private HashMap<Class<? extends Instance>, Instance> cachedDefaultComparisonInstances = new HashMap<>();

    private Instance findOrCreateDefaultComparisonInstance(Class<? extends Instance> clazz) {
        if (cachedDefaultComparisonInstances.containsKey(clazz)) {
            return cachedDefaultComparisonInstances.get(clazz);
        }

        try {
            Instance inst = clazz.getDeclaredConstructor().newInstance();
            cachedDefaultComparisonInstances.put(clazz, inst);
            return inst;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    public JSONObject InstanceToJSONObject(Instance inst) throws IllegalArgumentException, IllegalAccessException {
        Instance originalInst = findOrCreateDefaultComparisonInstance(inst.getClass());
        if (originalInst == null) return null;

        JSONObject obj = new JSONObject();

        for (Field field : inst.getClass().getFields()) {
            Object fieldValue = field.get(inst);

            //skip transient fields, allowing classes to define fields that shouldn't be serialized
            if (Modifier.isTransient(field.getModifiers()) || Objects.equals(field.get(originalInst), fieldValue)) continue;

            if (fieldValue instanceof AbstractSignalInstance) continue;

            if (fieldValue instanceof Color) {
                fieldValue = ((Color) fieldValue).getRGB();
            } else if (fieldValue instanceof Vector2) {
                fieldValue = ((Vector2) fieldValue).toString();
            } else if (fieldValue instanceof UnknownError) {
                fieldValue = ((UDim2) fieldValue).toString();
            }

            obj.put(field.getName(), fieldValue);
        }

                    
        //checking for class-specific fields that have getters/setters instead of fields
        if (inst instanceof AbstractImage) {
            obj.put("ImagePath", ((AbstractImage) inst).GetImagePath());
        }

        if (inst instanceof Script && ((Script) inst).GetWritableClassName() != null) {
            obj.put("WritableClassName", ((Script) inst).GetWritableClassName());
        }

        obj.put("class", inst.getClass().getName());

        return obj;
    }
    
    @SuppressWarnings("unchecked")
    public JSONArray InstanceArrayToJSONArray(Instance[] instances) {
        JSONArray arr = new JSONArray();

        HashMap<Instance, Integer> instanceIndexMap = new HashMap<>();
        for (int i = 0; i < instances.length; i++) {
            instanceIndexMap.put(instances[i], i);
        }
        
        for (Instance inst : instances) {
            if (inst == null) continue;
            JSONObject obj;
            try {
                obj = InstanceToJSONObject(inst);
                if (obj == null) continue;
                obj.put("IdentifierIndex", instanceIndexMap.get(inst));

                Integer parentIdentifierIndex = instanceIndexMap.get(inst.GetParent());

                Instance parent = inst.GetParent();

                if (parentIdentifierIndex == null) {
                    if (parent == game.WorldNode) {
                        parentIdentifierIndex = NODE_WORLD_IDENTIFIER;
                    } else if (parent == game.UINode) {
                        parentIdentifierIndex = NODE_UI_IDENTIFIER;
                    } else if (parent == game.StorageNode) {
                        parentIdentifierIndex = STORAGE_WORLD_IDENTIFIER;
                    } else if (parent == game.ScriptNode) {
                        parentIdentifierIndex = NODE_SCRIPT_IDENTIFIER;
                    } else {
                        parentIdentifierIndex = NULL_IDENTIFIER;
                    }
                }

                obj.put("ParentIdentifierIndex", parentIdentifierIndex);
            } catch (IllegalArgumentException | IllegalAccessException e) {
                e.printStackTrace();
                return null;
            }
            arr.add(obj);
        }

        return arr;
    }

    public Instance[] JSONArrayToInstanceArray(JSONArray arr) {
        Instance[] instances = new Instance[arr.size()];

        for (int i = 0; i < arr.size(); i++) {
            JSONObject obj = (JSONObject) arr.get(i);
            Instance inst = JSONObjectToInstance(obj);
            instances[i] = inst;
        }

        for (int i = 0; i < arr.size(); i++) {
            JSONObject obj = (JSONObject) arr.get(i);
            Instance inst = instances[i];

            int parentIdentifier = ((int)obj.get("ParentIdentifierIndex"));

            if (parentIdentifier >= 0) {
                inst.SetParent(instances[parentIdentifier]);
            } else if (parentIdentifier == NODE_WORLD_IDENTIFIER) {
                inst.SetParent(game.WorldNode);
            } else if (parentIdentifier == NODE_UI_IDENTIFIER) {
                inst.SetParent(game.UINode);
            } else if (parentIdentifier == STORAGE_WORLD_IDENTIFIER) {
                inst.SetParent(game.StorageNode);
            } else if (parentIdentifier == NODE_SCRIPT_IDENTIFIER) {
                inst.SetParent(game.ScriptNode);
            }
        }

        return instances;
    }

    @SuppressWarnings("unchecked")
    public <T extends Instance> T JSONObjectToInstance(JSONObject obj) {
        try {
            Class<?> clazz = Class.forName((String) obj.get("class"));
            T inst = (T) clazz.getDeclaredConstructor().newInstance();

            for (Object key : obj.keySet()) {
                if (key.equals("class") || !doesObjectContainField(inst, (String) key)) continue;

                Field field = inst.getClass().getField((String) key);
                Class<?> fieldType = field.getType();
                if (!fieldType.isAssignableFrom(obj.get(key).getClass()) && !fieldType.isPrimitive()) {
                    if (fieldType == Vector2.class) {
                        field.set(inst, Vector2.fromString((String) obj.get(key)));
                        continue;
                    } else if (fieldType == Color.class) {
                        field.set(inst, new Color((int) obj.get(key)));
                        continue;
                    } else if (fieldType == UDim2.class) {
                        field.set(inst, UDim2.fromString((String) obj.get(key)));
                        continue;
                    }

                    System.out.println("Field type mismatch: " + fieldType + " vs " + obj.get(key).getClass());
                    continue;
                }
                field.set(inst, obj.get(key));
            }

            if (inst instanceof AbstractImage) {
                ((AbstractImage) inst).SetImage((String) obj.get("ImagePath"));
            }

            if (inst instanceof Script) {
                Script script = (Script) inst;
                String className = (String) obj.get("WritableClassName");
                script.SetWritableClassName(className);

                if (className != null) {
                    try {
                        script.WritableClass = (Class<? extends WritableScript>) Class.forName(className);
                    } catch (ClassNotFoundException e) {
                        System.out.println("Error loading script with class name "+className+", loading was skipped for this script.");
                    }
                }

                //if (script.WritableClass != null) {
                    //game.ScriptService.LoadScript(script);
                //}
            }

            return inst;
        } catch (IllegalAccessException | NoSuchFieldException | InvocationTargetException | InstantiationException | ClassNotFoundException | NoSuchMethodException e) {
            e.printStackTrace();
            return null;
        }
    }

    //IO methods
    public void WriteInstanceArrayToFile(Instance[] instances, String path) {
        JSONArray arr = InstanceArrayToJSONArray(instances);
        
        try (FileWriter writer = new FileWriter(path)){
            writer.write(arr.toJSONString());
            writer.flush();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public Instance[] ReadInstanceArrayFromFile(String path) {
        try {
            JSONParser parser = new JSONParser();
            JSONArray arr = (JSONArray) parser.parse(new FileReader(path));
            return JSONArrayToInstanceArray(arr);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private boolean doesObjectContainField(Object object, String fieldName) {
        return Arrays.stream(object.getClass().getFields())
                .anyMatch(f -> f.getName().equals(fieldName));
    }
}
