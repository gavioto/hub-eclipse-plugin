package com.blackducksoftware.integration.eclipseplugin.popupmenu.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.handlers.HandlerUtil;

import com.blackducksoftware.integration.eclipseplugin.common.constants.ViewIds;

/*
 * Class that opens the warning view
 */
public class OpenWarningView extends AbstractHandler {

	@Override
	public Object execute(final ExecutionEvent event) throws ExecutionException {
		if (event == null) {
			System.err.println("null ExecutionEvent");
			return null;
		}

		// open window in which warnings will be displayed
		try {
			final IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindow(event);
			if (window != null) {
				final IWorkbenchPage page = window.getActivePage();
				if (page != null) {
					page.showView(ViewIds.WARNING);
				}
			}
		} catch (final PartInitException e) {
			showErrorMessage(event);
			e.printStackTrace();
		}
		return null;
	}

	/*
	 * Show an error message if an exception is caught when opening warning view
	 */
	private void showErrorMessage(final ExecutionEvent event) {
		final Shell activeShell = HandlerUtil.getActiveShell(event);
		final MessageBox errorMessageDialog = new MessageBox(activeShell, SWT.OK);
		errorMessageDialog.setText("Warning View Error");
		errorMessageDialog.setMessage("There was an error opening the warning view");
		errorMessageDialog.open();
	}

}
