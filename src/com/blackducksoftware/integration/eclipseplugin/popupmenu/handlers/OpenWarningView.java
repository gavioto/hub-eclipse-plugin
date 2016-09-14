package com.blackducksoftware.integration.eclipseplugin.popupmenu.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.handlers.HandlerUtil;

public class OpenWarningView extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		if (event == null) {
			System.err.println("null ExecutionEvent");
			return null;
		}
		
		// open window in which warnings will be displayed
		try {
			HandlerUtil.getActiveWorkbenchWindow(event).getActivePage().showView("com.blackduck.integration.eclipseplugin.views.WarningView");
		} catch (PartInitException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	

}
