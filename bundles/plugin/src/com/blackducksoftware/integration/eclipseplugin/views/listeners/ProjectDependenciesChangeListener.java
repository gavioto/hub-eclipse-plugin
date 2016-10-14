package com.blackducksoftware.integration.eclipseplugin.views.listeners;

import org.eclipse.jdt.core.ElementChangedEvent;
import org.eclipse.jdt.core.IElementChangedListener;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaElementDelta;
import org.eclipse.swt.widgets.Display;

import com.blackducksoftware.integration.eclipseplugin.views.ui.WarningView;

public class ProjectDependenciesChangeListener implements IElementChangedListener {

	private final WarningView warningView;
	private final Display display;

	public ProjectDependenciesChangeListener(final WarningView warningView, final Display display) {
		this.warningView = warningView;
		this.display = display;
	}

	@Override
	public void elementChanged(final ElementChangedEvent event) {
		visit(event.getDelta());
	}

	private void visit(final IJavaElementDelta delta) {
		final IJavaElement el = delta.getElement();
		switch (el.getElementType()) {
		case IJavaElement.JAVA_MODEL:
			visitChildren(delta);
			break;
		case IJavaElement.JAVA_PROJECT:
			if (isClasspathChanged(delta.getFlags())) {
				display.asyncExec(new Runnable() {
					@Override
					public void run() {
						if (warningView.getTable() != null) {
							warningView.setTableInput(warningView.getLastSelectedProjectName());
						}
					}
				});
			}
			break;
		default:
			break;
		}
	}

	private boolean isClasspathChanged(final int flags) {
		return 0 != (flags & (IJavaElementDelta.F_CLASSPATH_CHANGED | IJavaElementDelta.F_RESOLVED_CLASSPATH_CHANGED));
	}

	public void visitChildren(final IJavaElementDelta delta) {
		for (final IJavaElementDelta c : delta.getAffectedChildren()) {
			visit(c);
		}
	}
}
