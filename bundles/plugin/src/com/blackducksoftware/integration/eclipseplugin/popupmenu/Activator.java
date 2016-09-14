package com.blackducksoftware.integration.eclipseplugin.popupmenu;

import java.net.URL;
import java.net.URLClassLoader;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "com.blackduck.integration.popupmenu"; //$NON-NLS-1$

	// The shared instance
	private static Activator plugin;

	/**
	 * The constructor
	 */
	public Activator() {
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.
	 * BundleContext)
	 */
	@Override
	public void start(final BundleContext context) throws Exception {
		super.start(context);
		if (!getPreferenceStore().contains("minutesBetweenChecks")) {
			getPreferenceStore().setValue("minutesBetweenChecks", 5);
		}
		if (!getPreferenceStore().contains("activeProject")) {
			getPreferenceStore().setValue("activeProject", "NONE");
		}
		plugin = this;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.
	 * BundleContext)
	 */
	@Override
	public void stop(final BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static Activator getDefault() {
		return plugin;
	}

	/**
	 * Returns an image descriptor for the image file at the given plug-in
	 * relative path
	 *
	 * @param path
	 *            the path
	 * @return the image descriptor
	 */
	public static ImageDescriptor getImageDescriptor(final String path) {
		return imageDescriptorFromPlugin(PLUGIN_ID, path);
	}

	/**
	 * Returns a list of projects currently in the workspace
	 *
	 * @return list of projects in workspace
	 */
	public static IProject[] getProjects() {
		final IProject[] projects = ResourcesPlugin.getWorkspace().getRoot().getProjects();
		return projects;
	}

	public static int getNumProjects() {
		return getProjects().length;
	}

	public static String[][] getProjectNames() {
		final IProject[] projects = getProjects();
		final String[][] names = new String[projects.length + 1][2];
		for (int i = 0; i < projects.length; i++) {
			names[i][0] = projects[i].toString().split("/")[1];
			names[i][1] = projects[i].toString();
		}
		names[projects.length][0] = "no active project";
		names[projects.length][1] = "NONE";
		return names;
	}

	public static void printProjectInfo() {
		final IProject[] projects = getProjects();
		for (int i = 0; i < projects.length; i++) {
			try {
				System.out.print("name: ");
				System.out.println(projects[i].getDescription().getName());
				System.out.println();
				System.out.println("nature ids:");
				final String[] natureIds = projects[i].getDescription().getNatureIds();
				for (int j = 0; j < natureIds.length; j++) {
					System.out.println(natureIds[j]);
				}
				System.out.println();
				if (projects[i].hasNature(JavaCore.NATURE_ID)) {
					final IJavaProject javaProject = JavaCore.create(projects[i]);
					final IClasspathEntry[] classpathEntries = javaProject.getRawClasspath();
					final String[] requiredProjectNames = javaProject.getRequiredProjectNames();
					System.out.println("entries:");
					for (int j = 0; j < classpathEntries.length; j++) {
						final IClasspathEntry entry = classpathEntries[j];
						System.out.println("kind: " + entry.getEntryKind());
						System.out.println("path: " + entry.getPath().toOSString());
					}
					System.out.println();
					System.out.println("required projects: ");
					for (int j = 0; j < requiredProjectNames.length; j++) {
						System.out.println(requiredProjectNames[j]);
					}
				}
				System.out.println("-------------------------");
			} catch (final CoreException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		System.out.println("files:");
		final ClassLoader cl = ClassLoader.getSystemClassLoader();

		final URL[] urls = ((URLClassLoader) cl).getURLs();

		for (final URL url : urls) {
			System.out.println(url.getFile());
		}
	}

}
