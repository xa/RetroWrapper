package com.zero.retrowrapper.emulator;

import java.applet.Applet;
import java.lang.reflect.Field;
import java.util.Random;

public class EmulatorConfig
{
	private static EmulatorConfig instance;

	public Field minecraftField;
	public Applet applet;
	
	private int port;

	public String mobClass;
	
	public EmulatorConfig()
	{
		port = new Random().nextInt(3000)+25566;
	}
	
	public int getPort()
	{
		return port;
	}
	
	public static EmulatorConfig getInstance()
	{
		if(instance == null)
		{
			instance = new EmulatorConfig();
		}
		
		return instance;
	}
}
