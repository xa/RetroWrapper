package com.zero.retrowrapper.emulator;

import java.util.Random;

public class EmulatorConfig
{
	private static EmulatorConfig instance;

	private int port;
	
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
