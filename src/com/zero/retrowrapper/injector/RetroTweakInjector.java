package com.zero.retrowrapper.injector;

import net.minecraft.launchwrapper.IClassTransformer;
import net.minecraft.launchwrapper.Launch;
import net.minecraft.launchwrapper.injector.VanillaTweakInjector;

import org.objectweb.asm.*;
import org.objectweb.asm.tree.*;

import static org.objectweb.asm.Opcodes.*;

import java.io.File;
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
			RetroTweakClassWriter cw = new RetroTweakClassWriter(0);
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
			e.printStackTrace();
			return bytesOld;
		}
	}

	public static File inject()
	{   	
		System.out.println("Turning off ImageIO disk-caching");
		ImageIO.setUseCache(false);
		VanillaTweakInjector.loadIconsOnFrames();
		System.out.println("Setting gameDir to: " + Launch.minecraftHome);
		return Launch.minecraftHome;
	}
}
