package com.zero.retrowrapper;

import java.io.File;
import java.util.List;

import net.minecraft.launchwrapper.ITweaker;
import net.minecraft.launchwrapper.LaunchClassLoader;

public class _4KTweaker implements ITweaker
{
	private List<String> args;

	@Override
	public void acceptOptions(List<String> args, File gameDir, File assetsDir, String profile)
	{
		this.args = args;
	}

	@Override
	public void injectIntoClassLoader(LaunchClassLoader classLoader)
	{
		classLoader.registerTransformer("com.zero.retrowrapper.injector._4KTweakInjector");
	}

	@Override
	public String getLaunchTarget()
	{
		return "com.zero.retrowrapper.injector._4KTweakInjector";
	}

	@Override
	public String[] getLaunchArguments()
	{
		return args.toArray(new String[args.size()]);
	}
}
