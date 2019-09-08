package net.mersid;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;

import com.google.common.base.Charsets;

/**
 * Class that reads and writes config data to/from a file.
 * @author Admin
 *
 */
public class Configuration {
	private final File configPath;
	
	// All these variables are defaults. At config init. time, variables loaded from file will overwrite these.
	public boolean toggleSneak = true;
	public boolean toggleSprint = false;
	public boolean flyBoost = false;
	
	public float flyBoostFactor = 4.0F;
	public int keyHoldTicks = 7;
	
	public final String statusDisplayOpts[] = {"no display", "color coded", "text only"};
	public String statusDisplay = statusDisplayOpts[1];
	public final String displayHPosOpts[] = {"left", "center", "right"};
	public String displayHPos = displayHPosOpts[0];
	public final String displayVPosOpts[] = {"top", "middle", "bottom"};
	public String displayVPos = displayVPosOpts[1];
	
	public Configuration(String configPath)
	{
		this.configPath = new File(configPath);
	}
	
	public void save()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("toggleSneak: " + toggleSneak + "\n");
		sb.append("toggleSprint: " + toggleSprint + "\n");
		sb.append("flyBoost: " + flyBoost + "\n");
		sb.append("flyBoostFactor: " + flyBoostFactor + "\n");
		sb.append("keyHoldTicks: " + keyHoldTicks + "\n");
		sb.append("statusDisplay: " + statusDisplay + "\n");
		sb.append("displayHPos: " + displayHPos + "\n");
		sb.append("displayVPos: " + displayVPos + "\n");
		
		try {
			FileUtils.writeStringToFile(configPath, sb.toString(), StandardCharsets.UTF_8, false);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public void load()
	{
		String parse = parse("toggleSneak");
		if (!parse.equals(""))
		{
			if (parse.equals("true"))
			{
				toggleSneak = true;
			}
			else if (parse.equals("false"))
			{
				toggleSneak = false;
			}
		}
		
		parse = parse("toggleSprint");
		if (!parse.equals(""))
		{
			if (parse.equals("true"))
			{
				toggleSprint = true;
			}
			else if (parse.equals("false"))
			{
				toggleSprint = false;
			}
		}
		
		parse = parse("flyBoost");
		if (!parse.equals(""))
		{
			if (parse.equals("true"))
			{
				flyBoost = true;
			}
			else if (parse.equals("false"))
			{
				flyBoost = false;
			}
		}
		
		flyBoostFactor = Float.parseFloat(parse("flyBoostFactor"));
		keyHoldTicks = Integer.parseInt(parse("keyHoldTicks"));
		statusDisplay = parse("statusDisplay");
		displayHPos = parse("displayHPos");
		displayVPos = parse("displayVPos");
	}
	
	// Returns the string value of the key passed in. key = name.
	private String parse(String name)
	{
		List<String> config;
		try {
			config = FileUtils.readLines(configPath, StandardCharsets.UTF_8);
		} catch (IOException e) {
			System.out.println("Failed to load config file for Toggle Sneak!");
			save(); // Generate save file because it doesnt exist.
			try {
				config = FileUtils.readLines(configPath, StandardCharsets.UTF_8);
			} catch (IOException e1) {
				System.out.println("Failed to load a second time! The game will likely crash.");
				config = new ArrayList<String>();
			}
		}
		
		String line = ""; // The value we want to retrieve.
		for (String s : config)
		{
			if (s.matches("^" + name + ": .*"))
			{
				line = s.replaceAll("^" + name + ": ", "");
				break;
			}
		}
		
		return line;
	}
}
