package com.zero.retrowrapper.emulator.register;

import java.util.ArrayList;
import java.util.List;

import com.zero.retrowrapper.emulator.register.handlers.GameHandler;
import com.zero.retrowrapper.emulator.register.handlers.ListmapsHandler;
import com.zero.retrowrapper.emulator.register.handlers.LoadHandler;
import com.zero.retrowrapper.emulator.register.handlers.ResourcesHandler;
import com.zero.retrowrapper.emulator.register.handlers.SaveHandler;
import com.zero.retrowrapper.emulator.register.handlers.SkinHandler;

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
		register(new SkinHandler());
		register(new GameHandler());
		register(new SaveHandler());
		register(new LoadHandler());
		register(new ListmapsHandler());
		register(new ResourcesHandler());
	}
}
