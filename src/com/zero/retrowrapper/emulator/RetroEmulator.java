package com.zero.retrowrapper.emulator;

import java.io.File;
import java.net.ServerSocket;
import java.net.Socket;

import com.zero.retrowrapper.emulator.registry.EmulatorRegistry;

import net.minecraft.launchwrapper.Launch;

public class RetroEmulator extends Thread implements Runnable
{
	private static RetroEmulator instance;
	
	private EmulatorRegistry registry;	
	private File directory;
	private File mapsDirectory;
	private File cacheDirectory;

	@Override
	public void run()
	{
		instance = this;
		
		System.out.println("Old servers emulator is running!");
		
		registry = new EmulatorRegistry();
		registry.registerAll();
		
		directory = new File(Launch.minecraftHome, "retrowrapper");
		directory.mkdirs();
		mapsDirectory = new File(RetroEmulator.getInstance().getDirectory(), "maps");
		mapsDirectory.mkdir();
		cacheDirectory = new File(RetroEmulator.getInstance().getDirectory(), "cache");
		cacheDirectory.mkdir();
  
		try(ServerSocket server = new ServerSocket(EmulatorConfig.getInstance().getPort()))
		{					 
			while(true)
			{
				Socket socket = server.accept();
				try
				{
					new SocketEmulator(socket).parseIncoming();;
				}catch(Exception e)
				{
					e.printStackTrace();
				}
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}			

	public EmulatorRegistry getRegistry()
	{
		return registry;
	}
	
	public File getDirectory()
	{
		return directory;
	}

	public File getMapsDirectory()
	{
		return mapsDirectory;
	}

	public File getCacheDirectory()
	{
		return cacheDirectory;
	}

	public static RetroEmulator getInstance()
	{
		return instance;
	}	
}