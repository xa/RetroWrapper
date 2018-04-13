package com.zero.retrowrapper.injector;

import net.minecraft.launchwrapper.IClassTransformer;
import net.minecraft.launchwrapper.Launch;

import org.objectweb.asm.*;
import org.objectweb.asm.tree.*;

import static org.objectweb.asm.Opcodes.*;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;

import javax.imageio.ImageIO;

public class RetroTweakInjector implements IClassTransformer
{
	/**
	 * 
	 * THIS IS MODIFIED VERSION OF INDEVVANILLATWEAKINJECTOR
	 *   ALL RIGHTS TO MOJANG
	 * 
	 */

	@SuppressWarnings({ "deprecation", "unchecked" })
	@Override
	public byte[] transform(final String name, final String transformedName, final byte[] bytesOld)
	{
		try
		{
			if (bytesOld == null)
			{
				return null;
			}
				
			final ClassReader cr = new ClassReader(bytesOld);
			final ClassNode classNodeOld = new ClassNode();
			cr.accept(classNodeOld, ClassReader.EXPAND_FRAMES);
			RetroTweakClassWriter cw = new RetroTweakClassWriter(0, classNodeOld.name.replaceAll("/", "."));
			ClassVisitor s = new ClassVisitor(ASM4, cw) {};
			cr.accept(s, 0);
			
			byte[] bytes = cw.toByteArray();
			
			final ClassReader classReader = new ClassReader(bytes);
			final ClassNode classNode = new ClassNode();
			
			classReader.accept(classNode, ClassReader.EXPAND_FRAMES);
	
			if (!classNode.interfaces.contains("java/lang/Runnable"))
			{
				return bytes;
			}
	
			MethodNode runMethod = null;
			for (final Object methodNode : classNode.methods)
			{
				MethodNode m = (MethodNode) methodNode;
				if ("run".equals(m.name))
				{
					runMethod = m;
					break;
				}
			}
			
			if (runMethod == null)
			{
				return bytes;
			}
	
			System.out.println("Probably the Minecraft class (it has run && is applet!): " + name);
	
			final ListIterator<AbstractInsnNode> iterator = runMethod.instructions.iterator();
			int firstSwitchJump = -1;
	
			while (iterator.hasNext())
			{
				AbstractInsnNode instruction = iterator.next();
	
				if (instruction.getOpcode() == TABLESWITCH)
				{
					TableSwitchInsnNode tableSwitchInsnNode = (TableSwitchInsnNode) instruction;
	
					firstSwitchJump = runMethod.instructions.indexOf((AbstractInsnNode) tableSwitchInsnNode.labels.get(0));
				}
				else if (firstSwitchJump >= 0 && runMethod.instructions.indexOf(instruction) == firstSwitchJump)
				{
					int endOfSwitch = -1;
					while (iterator.hasNext())
					{
						instruction = iterator.next();
						if (instruction.getOpcode() == GOTO)
						{
							endOfSwitch = runMethod.instructions.indexOf(((JumpInsnNode) instruction).label);
							break;
						}
					}
	
					if (endOfSwitch >= 0)
					{
						while (runMethod.instructions.indexOf(instruction) != endOfSwitch && iterator.hasNext())
						{
							instruction = iterator.next();
						}
	
						instruction = iterator.next();
						runMethod.instructions.insertBefore(instruction, new MethodInsnNode(INVOKESTATIC, "com/zero/retrowrapper/injector/RetroTweakInjector", "inject", "()Ljava/io/File;"));
						runMethod.instructions.insertBefore(instruction, new VarInsnNode(ASTORE, 2));
					}
				}
			}
	
			final ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
			classNode.accept(writer);
			return writer.toByteArray();
		}catch(Exception e)
		{
			return bytesOld;
		}
	}

	public static File inject() throws IllegalArgumentException, IllegalAccessException, ClassNotFoundException
	{   	
		System.out.println("Turning off ImageIO disk-caching");	
		ImageIO.setUseCache(false);
		RetroTweakInjector.loadIconsOnFrames();
		System.out.println("Setting gameDir to: " + Launch.minecraftHome);
		return Launch.minecraftHome;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void loadIconsOnFrames()
	{
		try
		{
			File e = new File(Launch.assetsDir, "icons/icon_16x16.png");
			File bigIcon = new File(Launch.assetsDir, "icons/icon_32x32.png");
			System.out.println("Loading current icons for window from: " + e + " and " + bigIcon);
//			Display.setIcon(new ByteBuffer[]{loadIcon(e), loadIcon(bigIcon)});
			java.awt.Frame[] frames = java.awt.Frame.getFrames();
			if (frames != null)
			{
				List icons = Arrays.asList(new Image[]{ImageIO.read(e), ImageIO.read(bigIcon)});
				java.awt.Frame[] arg3 = frames;
				int arg4 = frames.length;

				for (int arg5 = 0; arg5 < arg4; ++arg5)
				{
					java.awt.Frame frame = arg3[arg5];

					try
					{
						frame.setIconImages(icons);
					} catch (Throwable arg8)
					{
						arg8.printStackTrace();
					}
				}
			}
		} catch (IOException arg9)
		{
			arg9.printStackTrace();
		}
	}
	
	private static ByteBuffer loadIcon(File iconFile) throws IOException
	{
		BufferedImage icon = ImageIO.read(iconFile);
		int[] rgb = icon.getRGB(0, 0, icon.getWidth(), icon.getHeight(), (int[]) null, 0, icon.getWidth());
		ByteBuffer buffer = ByteBuffer.allocate(4 * rgb.length);
		int[] arg3 = rgb;
		int arg4 = rgb.length;

		for (int arg5 = 0; arg5 < arg4; ++arg5)
		{
			int color = arg3[arg5];
			buffer.putInt(color << 8 | color >> 24 & 255);
		}

		buffer.flip();
		return buffer;
	}
}
