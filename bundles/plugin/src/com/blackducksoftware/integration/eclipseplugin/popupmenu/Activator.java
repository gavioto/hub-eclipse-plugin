package com.blackducksoftware.integration.eclipseplugin.popupmenu;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import com.blackducksoftware.integration.eclipseplugin.internal.ProjectInfoProvider;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "com.blackduck.integration.eclipseplugin"; //$NON-NLS-1$

	// The shared instance
	private static Activator plugin;

	// set default preferences for project every time a new project is created
	private final IResourceChangeListener projectAddedListener = new IResourceChangeListener() {
		@Override
		public void resourceChanged(final IResourceChangeEvent event) {
			if (event.getSource() != null && event.getSource().equals(ResourcesPlugin.getWorkspace())) {
				if (event.getDelta() != null) {
					final IResourceDelta[] childrenDeltas = event.getDelta().getAffectedChildren();
					if (childrenDeltas != null) {
						for (final IResourceDelta delta : childrenDeltas) {
							if (delta.getKind() == IResourceDelta.ADDED) {
								if (delta.getResource() != null) {
									final IResource resource = delta.getResource();
									try {
										if (resource instanceof IProject
												&& ((IProject) resource).hasNature(JavaCore.NATURE_ID)) {
											final String projectName = resource.getName();
											final String displayWarnings = StringUtils
													.join(new String[] { projectName, "displayWarnings" }, ':');
											getPreferenceStore().setDefault(displayWarnings, true);
											getPreferenceStore().setDefault(projectName, true);
										}
									} catch (final CoreException e) {
										e.printStackTrace();
									}
								}
							}
						}
					}
				}
			}
		}
	};

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
		final String[] projectNames = ProjectInfoProvider.getJavaProjectNames();

		// make sure all default preferences are set
		for (final String projectName : projectNames) {
			final String displayWarnings = StringUtils.join(new String[] { projectName, "displayWarnings" }, ':');
			getPreferenceStore().setDefault(displayWarnings, true);
			getPreferenceStore().setDefault(projectName, true);
		}
		ResourcesPlugin.getWorkspace().addResourceChangeListener(projectAddedListener);
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

}
