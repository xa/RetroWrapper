package com.zero.retrowrapper.injector;

import net.minecraft.launchwrapper.IClassTransformer;
import net.minecraft.launchwrapper.Launch;
import net.minecraft.launchwrapper.injector.VanillaTweakInjector;

import javax.swing.*;
import java.applet.Applet;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

public class _4KTweakInjector implements IClassTransformer
{
	/**
	 * 
	 * THIS IS MODIFIED VERSION OF ALPHAVANILLATWEAKINJECTOR
	 *   ALL RIGHTS TO MOJANG
	 * 
	 */

    @Override
    public byte[] transform(final String name, final String transformedName, final byte[] bytes)
    {
        return bytes;
    }

    public static void main(String[] args) throws NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, ClassNotFoundException
    {
        Class<?> clazz = getaClass("M");

        System.out.println("_4KTweakInjector.class.getClassLoader() = " + _4KTweakInjector.class.getClassLoader());
        Constructor<?> constructor = clazz.getConstructor();
        Object object = constructor.newInstance();

        startMinecraft((Applet) object, args);
    }

    private static void startMinecraft(final Applet applet, String[] args)
    {
        final Frame launcherFrameFake = new Frame();
        launcherFrameFake.setTitle("Minecraft 4K");
        launcherFrameFake.setBackground(Color.BLACK);

        final JPanel panel = new JPanel();
        launcherFrameFake.setLayout(new BorderLayout());
        panel.setPreferredSize(new Dimension(854, 480));
        launcherFrameFake.add(panel, BorderLayout.CENTER);
        launcherFrameFake.pack();

        launcherFrameFake.setLocationRelativeTo(null);
        launcherFrameFake.setVisible(true);

        launcherFrameFake.addWindowListener(new WindowAdapter()
        {
            @Override
            public void windowClosing(WindowEvent e)
            {
                System.exit(1);
            }
        });
      
        LauncherFake fakeLauncher = new LauncherFake(new HashMap<String, String>());
        applet.setStub(fakeLauncher);

        fakeLauncher.setLayout(new BorderLayout());
        fakeLauncher.add(applet, BorderLayout.CENTER);
        fakeLauncher.validate();

        launcherFrameFake.removeAll();
        launcherFrameFake.setLayout(new BorderLayout());
        launcherFrameFake.add(fakeLauncher, BorderLayout.CENTER);
        launcherFrameFake.validate();

        applet.init();
        applet.start();

        Runtime.getRuntime().addShutdownHook(new Thread()
        {
            public void run()
            {
                applet.stop();
            }
        });

        VanillaTweakInjector.loadIconsOnFrames();
    }

    private static Class<?> getaClass(String name) throws ClassNotFoundException
    {
        return Launch.classLoader.findClass(name);
    }
}
