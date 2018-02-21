package com.zero.retrowrapper.injector;

import java.applet.Applet;
import java.applet.AppletStub;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import com.zero.retrowrapper.emulator.EmulatorConfig;

public class LauncherFake extends Applet implements AppletStub
{	
    private static final long serialVersionUID = 1L;

    private Map<String, String> params = new HashMap<String, String>();

    public LauncherFake(Map<String, String> params)
    {
    	this.params = params;
    }

    @Override
    public void appletResize(int width, int height) {}

    @Override
    public boolean isActive()
    {
        return true;
    }

    @Override
    public URL getDocumentBase()
    {
        try
        {
            return new URL("http://127.0.0.1:"+EmulatorConfig.getInstance().getPort()+"/game/");
        }
        catch (MalformedURLException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public URL getCodeBase()
    {
        try
        {
            return new URL("http://127.0.0.1:"+EmulatorConfig.getInstance().getPort()+"/game/");
        }
        catch (MalformedURLException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String getParameter(String paramName)
    {
        if (params.containsKey(paramName))
        {
            return params.get(paramName);
        }
        System.err.println("Client asked for parameter: " + paramName);
        return null;
    }
}