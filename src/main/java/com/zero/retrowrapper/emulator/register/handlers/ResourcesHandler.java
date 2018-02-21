package com.zero.retrowrapper.emulator.register.handlers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.Scanner;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;
import com.zero.retrowrapper.emulator.ByteUtils;
import com.zero.retrowrapper.emulator.RetroEmulator;
import com.zero.retrowrapper.emulator.register.IHandler;
import com.zero.retrowrapper.emulator.register.EmulatorHandler;

public class ResourcesHandler extends EmulatorHandler implements IHandler
{
	private static final byte[] SOUNDS_LIST =
			("\nsound/step/wood4.ogg,0,1245702004000\n"
			+"sound/step/gravel3.ogg,0,1245702004000\n"
			+"sound/step/wood2.ogg,0,1245702004000\n"
			+"sound/step/gravel1.ogg,0,1245702004000\n"
			+"sound/step/grass2.ogg,0,1245702004000\n"
			+"sound/step/gravel4.ogg,0,1245702004000\n"
			+"sound/step/grass4.ogg,0,1245702004000\n"
			+"sound/step/gravel2.ogg,0,1245702004000\n"
			+"sound/step/wood1.ogg,0,1245702004000\n"
			+"sound/step/stone4.ogg,0,1245702004000\n"
			+"sound/step/grass3.ogg,0,1245702004000\n"
			+"sound/step/wood3.ogg,0,1245702004000\n"
			+"sound/step/stone2.ogg,0,1245702004000\n"
			+"sound/step/stone3.ogg,0,1245702004000\n"
			+"sound/step/grass1.ogg,0,1245702004000\n"
			+"sound/step/stone1.ogg,0,1245702004000\n"
			+"music/calm2.ogg,0,1245702004000\n"
			+"music/calm3.ogg,0,1245702004000\n"
			+"music/calm1.ogg,0,1245702004000\n").getBytes();
	
	private JsonObject jsonObjects;
	
	public ResourcesHandler()
	{
		super("/resources/");
		downloadSoundData();
	}
	
	private void downloadSoundData()
    {
    	try(Scanner sc = new Scanner(new URL("https://launchermeta.mojang.com/mc/assets/legacy/c0fd82e8ce9fbc93119e40d96d5a4e62cfa3f729/legacy.json").openStream()).useDelimiter("\\A"))
    	{			
			JsonValue json = Json.parse(sc.next());
			
			JsonObject obj = json.asObject();
			jsonObjects = obj.get("objects").asObject();
			
		} catch (Exception e)
    	{
			e.printStackTrace();
		}		
	}
    
	@Override
	public void handle(OutputStream os, String get, byte[] data) throws IOException
	{
		if(get.equals("/resources/"))
		{
			os.write(SOUNDS_LIST);
		}else
		{
			String name = get.replace("/resources/", "");
       		byte[] bytes = getResourceByName(name);
       		if(bytes != null)
       		{
       			os.write(bytes);
       			System.out.println("Succesfully installed resource! "+name+" ("+bytes.length+")");
       		}
		}
	}
	
	private byte[] getResourceByName(String res) throws FileNotFoundException, IOException
    {
		File resourceCache = new File(RetroEmulator.getInstance().getCacheDirectory(), res);

		if(resourceCache.exists())
		{
			try(FileInputStream fis = new FileInputStream(resourceCache))
			{
				return ByteUtils.readFully(fis);
			}						
		}else
		{
	    	try
	    	{
		    	if(jsonObjects.get(res) != null)
		    	{
		    		String hash = jsonObjects.get(res).asObject().get("hash").asString();
		    		
		    		System.out.println(hash);
		    	    		
					InputStream is = new URL("http://resources.download.minecraft.net/"+hash.substring(0, 2)+"/"+hash).openStream();
					
					byte[] resourceBytes = ByteUtils.readFully(is);
					
					new File(resourceCache.getParent()).mkdirs();
					try(FileOutputStream fos = new FileOutputStream(resourceCache))
					{
						fos.write(resourceBytes);
						fos.close();
					}
					
		            return resourceBytes;
		    	}else
		    	{
		    		return null;
		    	}
	    	}catch(Exception e)
	    	{
	    		e.printStackTrace();
	    		return null;
	    	}
		}
    }
}
