package utils;

import java.io.File;

import org.eclipse.core.filesystem.EFS;
import org.eclipse.core.filesystem.IFileStore;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.StatusLineContributionItem;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;
import org.eclipse.ui.ide.FileStoreEditorInput;

import editors.ImageEditor;
import editors.MyTextEditor;
import filebrowser.views.BrowserView;

public class FileOpenAction extends Action implements IWorkbenchAction {

	private static final String ID = "filebrowser.FileOpenAction";
	private volatile static FileOpenAction instance;

	private FileOpenAction() {
		setId(ID);
	}

	public static FileOpenAction getInstance() {
		if (instance == null) {
			synchronized (FileOpenAction.class) {
				if (instance == null) {
					instance = new FileOpenAction();
				}
			}
		}
		return instance;
	}

	public void run() {
		IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		String path = page.getSelection().toString();
		String loc = path.substring(1, path.length() - 1);
		File file = new File(loc);
		IPath ipath = new Path(file.getAbsolutePath());
		IFileStore fs = EFS.getLocalFileSystem().getStore(ipath);
		FileStoreEditorInput fileStoreEditorInput = new FileStoreEditorInput(fs);
		TreeViewer tv = ((BrowserView) page.getActivePart()).getTreeViewer(); 
		
		try {
			if (file.isDirectory()) {
				if(tv.getExpandedState(file))
					tv.setExpandedState(file, false);
				
				else
				tv.setExpandedState(file, true);
			}
			
			else if (file.getName().endsWith(".txt")) {
				page.openEditor(fileStoreEditorInput, MyTextEditor.ID, false);
			}

			else if ((file.getName().endsWith(".jpg") || file.getName().endsWith(".png"))) {
				page.openEditor(fileStoreEditorInput, ImageEditor.ID, false);
			}
		} catch (PartInitException e) {
			e.printStackTrace();
		}
		
		if(!file.isDirectory()){
			IActionBars bars = ((IViewSite) page.getActivePart().getSite()).getActionBars();
			bars.getStatusLineManager().setMessage(file.getName() + " is opened.");
			StatusLineContributionItem statusBars = ((StatusLineContributionItem) bars.getStatusLineManager().find("Size"));
			statusBars.setText(file.length() + " Bytes");
		}
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

}