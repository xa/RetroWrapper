package com.zero.retrowrapper.hack;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class RetroPlayer
{
	public Field x, y, z, x2, y2, z2;
	public double ax, ay, az;
	public Object aabb;
	public Class<?> entityClass;
	public Object playerObj;
	
	public HackThread thread;
	public Field playerField;
	public Object minecraft;
	private boolean modeFloat;
	
	public RetroPlayer(HackThread thread)
	{
		this.thread = thread;
	}
	
	public void tick() throws IllegalArgumentException, IllegalAccessException, InterruptedException
	{
		try
		{
			playerObj = playerField.get(minecraft);
			if(playerObj != null)
			{
				setAABB();
				ax = getVariable(x2, aabb) - getX();
				ay = getVariable(y2, aabb) - getY();
				az = getVariable(z2, aabb) - getZ();
				thread.label.setText("<html>Position:<br>&nbsp&nbsp&nbsp;x: "+Math.floor(getX()*10)/10+"<br>&nbsp&nbsp&nbsp;y: "+Math.floor(getY()*10)/10+"<br>&nbsp&nbsp&nbsp;z: "+Math.floor(getZ()*10)/10+"</html>");
			}
		}catch(Exception e)
		{
			e.printStackTrace();
			Thread.sleep(1000);
		}
	}

	public void setAABB() throws IllegalArgumentException, IllegalAccessException
	{
		boolean flag2 = false;
		
		for(Field f : entityClass.getDeclaredFields())
		{
			if(!flag2)
			{
				if(f.getType().getName().equals("float"))
				{
					flag2 = true;
				}
			}else
			{
				if(!f.getType().isPrimitive())
				{
					aabb = f.get(playerObj);
					break;
				}
			}
		}

		if(aabb != null)
		{
			int doubleCount = 0;
			
			label:
			for(Field f : aabb.getClass().getDeclaredFields())
			{
				if(Modifier.isPublic(f.getModifiers()) && (f.getType().getName().equals("double") || f.getType().getName().equals("float")))
				{
					if(f.getType().getName().equals("float"))
					{
						modeFloat = true;
					}
					switch(doubleCount)
					{
						case 0:
							x = f;
							break;
						case 1:
							y = f;
							break;
						case 2:
							z = f;
							break;
						case 3:
							x2 = f;
							break;
						case 4:
							y2 = f;
							break;
						case 5:
							z2 = f;
							break;
						default:
							break label;
					}
					doubleCount++;
				}
			}
		}
	}

	private double getX() throws IllegalArgumentException, IllegalAccessException
	{
		return getVariable(x, aabb);
	}	
	
	private double getY() throws IllegalArgumentException, IllegalAccessException
	{
		return getVariable(y, aabb);
	}
	
	private double getZ() throws IllegalArgumentException, IllegalAccessException
	{
		return getVariable(z, aabb);
	}
	
	private double getVariable(Field f, Object o) throws IllegalArgumentException, IllegalAccessException
	{
		if(modeFloat)
		{
			return ((Float)f.getFloat(o)).doubleValue();
		}else
		{
			return f.getDouble(o);
		}
	}

	public void teleport(double dx, double dy, double dz) throws IllegalArgumentException, IllegalAccessException
	{
		if(modeFloat)
		{
			x.set(aabb, (float)dx);
			y.set(aabb, (float)dy);
			z.set(aabb, (float)dz);
			x2.set(aabb, (float)(dx+ax));
			y2.set(aabb, (float)(dy+ay));
			z2.set(aabb, (float)(dz+az));
		}else
		{
			x.set(aabb, dx);
			y.set(aabb, dy);
			z.set(aabb, dz);
			x2.set(aabb, dx+ax);
			y2.set(aabb, dy+ay);
			z2.set(aabb, dz+az);			
		}
	}	
}
