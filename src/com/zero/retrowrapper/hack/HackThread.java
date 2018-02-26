package com.zero.retrowrapper.hack;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Field;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.WindowConstants;

import com.zero.retrowrapper.emulator.EmulatorConfig;
import com.zero.retrowrapper.injector.RetroTweakInjectorTarget;

public class HackThread extends Thread implements Runnable
{
	public JFrame frame;
	public JLabel label;
	
	@Override
	public void run()
	{
		final RetroPlayer player = new RetroPlayer(this);

		try
		{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}catch (Exception e)
		{  
			e.printStackTrace();
		}

		frame = new JFrame("Retrowrapper");
		Dimension dim = new Dimension(654, 310);
		frame.setPreferredSize(dim);
		frame.setMinimumSize(dim);
		frame.setLayout(null);
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		
		label = new JLabel("<html>Position:<br>&nbsp&nbsp&nbsp;x: null<br>&nbsp&nbsp&nbsp;y: null<br>&nbsp&nbsp&nbsp;z: null</html>");
		label.setBounds(30, 10, 500, 80);
		frame.add(label);

		JLabel xl = new JLabel("x:");
		xl.setBounds(30, 103, 50, 20);
		frame.add(xl);
		
		JLabel yl = new JLabel("y:");
		yl.setBounds(30, 135, 50, 20);
		frame.add(yl);

		JLabel zl = new JLabel("z:");
		zl.setBounds(30, 167, 50, 20);
		frame.add(zl);
		
		final JTextField x = new JTextField();
		x.setBounds(50, 100, 200, 30);
		frame.add(x);

		final JTextField y = new JTextField();
		y.setBounds(50, 132, 200, 30);
		frame.add(y);
		
		final JTextField z = new JTextField();
		z.setBounds(50, 164, 200, 30);
		frame.add(z);
		
		JButton b = new JButton("Teleport");
		b.setBounds(50, 202, 200, 40);
		b.addActionListener(new ActionListener()
		{
		    @Override
		    public void actionPerformed(ActionEvent e)
		    {
		    	try
		    	{
		    		float dx = Float.valueOf(x.getText().replaceAll(",", "").replaceAll(" ", ""));
		    		float dy = Float.valueOf(y.getText().replaceAll(",", "").replaceAll(" ", ""));
		    		float dz = Float.valueOf(z.getText().replaceAll(",", "").replaceAll(" ", ""));
		    		
		    		player.teleport(dx, dy, dz);
		    	}catch(Exception ee)
		    	{
		    		JOptionPane.showMessageDialog(null, "Exception occured!\n"+ee.getClass().getName()+"\n"+ee.getMessage());
		    	}
		    }
		});
		frame.add(b);

		frame.setVisible(true);
		
		try
		{
			EmulatorConfig config = EmulatorConfig.getInstance();
			
			config.minecraftField.setAccessible(true);
			player.minecraft = config.minecraftField.get(config.applet);
			
			Class<?> mcClass = getMostSuper(player.minecraft.getClass());
			
			System.out.println("Minecraft class: "+mcClass.getName());
			System.out.println("Mob class: "+config.mobClass);
			player.playerObj = null;
			Class<?> mobClass = RetroTweakInjectorTarget.getaClass(config.mobClass);
			
			while(player.playerObj == null)
			{	
				for(Field f : mcClass.getDeclaredFields())
				{
					if(mobClass.isAssignableFrom(f.getType()) || f.getType().equals(mobClass))
					{
						player.playerField = f;
						player.playerObj = f.get(player.minecraft);
						break;
					}
				}					
				
				Thread.sleep(1000);
			}
			
			System.out.println("Player class: "+player.playerObj.getClass().getName());
			
			player.entityClass = getMostSuper(mobClass);
			
			System.out.println("Entity class: "+player.entityClass.getName());
			
			player.setAABB();
			
			if(player.aabb != null)
			{
				while(true)
				{
					player.tick();
					Thread.sleep(100);
				}
			}
		}catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	private Class<?> getMostSuper(Class<?> mobClass)
	{
		while(true)
		{				
			if(!mobClass.getSuperclass().equals(Object.class))
			{
				mobClass = mobClass.getSuperclass();
			}else
			{
				break;
			}
		}
		
		return mobClass;
	}
}
