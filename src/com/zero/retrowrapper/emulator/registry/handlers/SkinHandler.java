package com.zero.retrowrapper.emulator.registry.handlers;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.Base64;
import java.util.HashMap;
import java.util.Scanner;

import javax.imageio.ImageIO;

import com.zero.retrowrapper.emulator.ByteUtils;
import com.zero.retrowrapper.emulator.RetroEmulator;
import com.zero.retrowrapper.emulator.registry.EmulatorHandler;
import com.zero.retrowrapper.emulator.registry.IHandler;

public class SkinHandler extends EmulatorHandler implements IHandler
{
	private HashMap<String, byte[]> skinsCache = new HashMap<>();

	public SkinHandler(String url)
	{
		super(url);
	}

	@Override
	public void handle(OutputStream os, String get, byte[] data) throws IOException
	{
		String username = get.replace(url, "").replace(".png", "");
		
		if(skinsCache.containsKey(username))
		{
			os.write(skinsCache.get(username));
		}else
		{							
			byte[] bytes3 = downloadSkin(username);
			if(bytes3 != null)
			{
				BufferedImage imgSkinRaw = ImageIO.read(new ByteArrayInputStream(bytes3));
				BufferedImage imgSkinFixed = new BufferedImage(64, 32, BufferedImage.TYPE_INT_ARGB);
				
				imgSkinFixed.getGraphics().drawImage(imgSkinRaw, 0, 0, null);
				
				ByteArrayOutputStream osSkin = new ByteArrayOutputStream();
				
				ImageIO.write(imgSkinFixed, "png", osSkin);
				osSkin.flush();
				
				byte[] bytes = osSkin.toByteArray();				
				os.write(bytes);
				
				skinsCache.put(username, bytes);
			}
		}
	}
	
	private byte[] downloadSkin(String username) throws IOException
	{
		//need to rewrite this to use json
		
		File skinCache = new File(RetroEmulator.getInstance().getCacheDirectory(), username + ".png");
		
		try(Scanner sc = new Scanner(new URL("https://api.mojang.com/users/profiles/minecraft/"+username+"?at="+System.currentTimeMillis()).openStream()))
		{
			String resp = sc.nextLine();
			String uuid = resp.split("\"")[3].split("\"")[0];			
			System.out.println(uuid);
			try(Scanner sc2 = new Scanner(new URL("https://sessionserver.mojang.com/session/minecraft/profile/"+uuid).openStream()))
			{
				String base64 = sc2.nextLine().split("\"name\":\"textures\",\"value\":\"")[1].split("\"")[0];
				String skinURL = new String(Base64.getDecoder().decode(base64)).split("\"SKIN\":\\{\"url\":\"")[1].split("\"")[0];
				
				System.out.println(skinURL);
				InputStream is = new URL(skinURL).openStream();
				
				byte[] skinBytes = ByteUtils.readFully(is);
				
				try(FileOutputStream fos = new FileOutputStream(skinCache))
				{
					fos.write(skinBytes);
					fos.close();
				}
				
			   	return skinBytes;
			}
		}catch(Exception e)
		{
			e.printStackTrace();

			if(skinCache.exists())
			{
				try(FileInputStream fis = new FileInputStream(skinCache))
				{
					return ByteUtils.readFully(fis);
				}			
			}
			
			return null;
		}		
	}
}
