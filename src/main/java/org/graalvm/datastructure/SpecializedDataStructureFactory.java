package org.graalvm.datastructure;

import java.util.AbstractList;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

import org.graalvm.datastructure.specialization.TypeSpecialization;

import java.util.ArrayList;
import java.util.Collection;

public class SpecializedDataStructureFactory extends DataStructureFactory {

	private static String jdkSources = "/home/rbruno/git/labs-openjdk-11/src/java.base/share/classes/";
	private static String tmpDir = "/tmp/org.graalvm.datastructure.specialization";
	private static String patchedSrcPath = tmpDir + "/src/";
	private static String patchedModPath = tmpDir + "/patched/java.base/";

	private static void compile(String[] filePaths) throws Exception {
        Process p;
        try {
            p = Runtime.getRuntime().exec("javac --patch-module java.base=" + patchedSrcPath +  " -d " + patchedModPath  + " " + String.join(" ", filePaths));
            p.waitFor();

            if (p.exitValue() != 0) {
            	throw new Exception("compilation of specialized data structures failed...");
            }

            p.destroy();
        } catch (Exception e) {
        	e.printStackTrace();
        	throw e;
        }
	}

	// TODO - what if the parameter cannot be statically discovered?
	// TODO - have a cache

	private String setupArrayList(Class<?> parT) throws Exception {
		String specializedClassName = "java.util.ArrayList" + parT.getSimpleName();
		String specializedPath = TypeSpecialization.specializeArrayList(jdkSources, patchedSrcPath, parT.getSimpleName());
		compile(new String[] { specializedPath });
		return specializedClassName;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> AbstractList<T> ArrayList(Class<T> parT) {
		try {
			String specializedClassName = setupArrayList(parT);
			return (AbstractList<T>) Class.forName(specializedClassName).getDeclaredConstructor().newInstance();
		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<T>();
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> AbstractList<T> ArrayList(Class<T> parT, int initialCapacity) {
		try {
			String specializedClassName = setupArrayList(parT);
			return (AbstractList<T>) Class.forName(specializedClassName).getDeclaredConstructor(int.class).newInstance(initialCapacity);
		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<T>(initialCapacity);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> AbstractList<T> ArrayList(Class<T> parT, Collection<? extends T> c) {
		try {
			String specializedClassName = setupArrayList(parT);
			return (AbstractList<T>) Class.forName(specializedClassName).getDeclaredConstructor(Collection.class).newInstance(c);
		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<T>(c);
		}
	}

	// TODO - add remaining constructors

	@SuppressWarnings("unchecked")
	@Override
	public <K,V> AbstractMap<K,V> HashMap(Class<K> parK, Class<V> parV) {
		try {
			String hmClassName = "java.util.HashMap" + parK.getSimpleName() + parV.getSimpleName();
			String hmSpecializedPath = TypeSpecialization.specializeHashMap(jdkSources, patchedSrcPath, parK.getSimpleName(), parV.getSimpleName());
			String lhmSpecializedPath = TypeSpecialization.specializeLinkedHashMap(jdkSources, patchedSrcPath, parK.getSimpleName(), parV.getSimpleName());
			compile(new String[] { hmSpecializedPath, lhmSpecializedPath });
			return (AbstractMap<K,V>) Class.forName(hmClassName).getDeclaredConstructor().newInstance();
		} catch (Exception e) {
			e.printStackTrace();
			return new HashMap<K,V>();
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public <K,V> AbstractMap<K,V> ConcurrentHashMap(Class<K> parK, Class<V> parV) {
		try {
			String className = "java.util.concurrent.ConcurrentHashMap" + parK.getSimpleName() + parV.getSimpleName();
			String specializedPath = TypeSpecialization.specializeConcurrentHashMap(jdkSources, patchedSrcPath, parK.getSimpleName(), parV.getSimpleName());
			compile(new String[] { specializedPath });
			return (AbstractMap<K,V>) Class.forName(className).getDeclaredConstructor().newInstance();
		} catch (Exception e) {
			e.printStackTrace();
			return new ConcurrentHashMap<K,V>();
		}
	}
}