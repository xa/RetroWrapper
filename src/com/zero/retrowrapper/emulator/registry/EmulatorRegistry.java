package com.zero.retrowrapper.emulator.registry;

import java.util.ArrayList;
import java.util.List;

import com.zero.retrowrapper.emulator.registry.handlers.GameHandler;
import com.zero.retrowrapper.emulator.registry.handlers.ListmapsHandler;
import com.zero.retrowrapper.emulator.registry.handlers.LoadHandler;
import com.zero.retrowrapper.emulator.registry.handlers.ResourcesHandler;
import com.zero.retrowrapper.emulator.registry.handlers.ResourcesHandlerBeta;
import com.zero.retrowrapper.emulator.registry.handlers.SaveHandler;
import com.zero.retrowrapper.emulator.registry.handlers.SkinHandler;

public class EmulatorRegistry
{
	public List<IHandler> handlers = new ArrayList<IHandler>();
	
	private void register(EmulatorHandler handler)
	{
		handlers.add(handler);
	}

	public IHandler getHandlerByUrl(String url)
	{
		for(IHandler handler : handlers)
		{
			if(url.contains(handler.getUrl()))
			{
				return handler;
			}
		}
		return null;
	}

	public void registerAll()
	{
		register(new GameHandler());
		register(new SaveHandler());
		register(new LoadHandler());
		register(new ListmapsHandler());
		register(new ResourcesHandler());
		register(new ResourcesHandlerBeta());
		register(new SkinHandler("/skin/"));
		register(new SkinHandler("/MinecraftSkins/"));
	}
}
