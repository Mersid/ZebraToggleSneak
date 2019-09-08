package net.mersid;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.lwjgl.glfw.GLFW;

import net.minecraft.client.Minecraft;

public class Keyboard {
	private static final Map<String, Integer> KEYBOARD_MAP = GenerateKeyboardMap();
	private static final Map<Integer, String> REVERSE_KEYBOARD_MAP = ReverseKeyboardMap(KEYBOARD_MAP);
	private static final long HANDLE = Minecraft.getInstance().mainWindow.getHandle();
	
	/**
	 * Returns whether the key is down.
	 * @param key A key found under GLFW.GLFW_KEY_*
	 * @return true if down, false if not.
	 */
	public static boolean isKeyDown(int key)
	{
		GLFW.glfwPollEvents();
		return GLFW.glfwGetKey(HANDLE, key) != 0;
	}
	
	/**
	 * Given a keycode name, return its keycode.
	 * @param name
	 * @return The integer the name corresponds to. If none, returns 0.
	 */
	public static int getKeyIndex(String name)
	{
		Integer i = KEYBOARD_MAP.get(name);
		return i == null ? 0 : i;
	}
	
	/**
	 * Given a keyCode, return its name.
	 * @return The name of the keycode.
	 */
	public static String getKeyName(int keyCode)
	{
		return REVERSE_KEYBOARD_MAP.get(keyCode);
	}
	
	private static Map<String, Integer> GenerateKeyboardMap()
	{
		try
		{
			Map<String, Integer> map = new HashMap<String, Integer>();
			Field[] fields = GLFW.class.getFields(); // List of all fields in GLFW class, including non-key fields.
			for (Field field : fields)
			{
				if (!field.getName().matches("GLFW_KEY_.*")) continue; // Skip non key fields
				int fieldValue = field.getInt(null);
				String key = field.getName().replaceFirst("GLFW_KEY_", "");
				
				map.put(key, fieldValue);
			}
			
			return map;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return new HashMap<String, Integer>();
		}
	}
	
	private static Map<Integer, String> ReverseKeyboardMap(Map<String, Integer> KeyboardMap)
	{
		//https://stackoverflow.com/questions/20412354/reverse-hashmap-keys-and-values-in-java
		Map<Integer, String> myNewHashMap = new HashMap<>();
		for(Map.Entry<String, Integer> entry : KeyboardMap.entrySet()){
		    myNewHashMap.put(entry.getValue(), entry.getKey());
		}
		
		return myNewHashMap;
	}
	
	/**
	 * Use this instead of toString(). Returns a string of key-pair values of the map.
	 * @return A string of key-pair values of the map.
	 */
	public static String dumpAsString()
	{
		StringBuilder sb = new StringBuilder();
		
		for (Entry<String, Integer> entry : KEYBOARD_MAP.entrySet())
		{
			sb.append("{" + entry.getKey() + ", " + entry.getValue() + "}\n");
		}
		
		return sb.toString();
	}
	
	/**
	 * Dumps the reverse map as a string. See dumpAsString().
	 * @return A string of key-pair values of the reverse map.
	 */
	public static String dumpReverseAsString()
	{
		StringBuilder sb = new StringBuilder();
		
		for (Entry<Integer, String> entry : REVERSE_KEYBOARD_MAP.entrySet())
		{
			sb.append("{" + entry.getKey() + ", " + entry.getValue() + "}\n");
		}
		
		return sb.toString();
	}
}
