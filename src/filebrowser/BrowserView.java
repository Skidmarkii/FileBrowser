package filebrowser;

import java.io.File;

import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.internal.win32.OS;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.ui.part.ViewPart;

@SuppressWarnings("restriction")
public class BrowserView extends ViewPart {
	public BrowserView() {
	}

	private TreeViewer treeViewer;
	private Tree tree;
	public static final String ID = "FileBrowser.browserView";

	/***** 트리 구조 출력 메소드 *****/
	@Override
	public void createPartControl(Composite parent) {
		// tree viewer 생성
		tree = new Tree(parent, SWT.V_SCROLL | SWT.H_SCROLL);
		
		// 윈도우의 스타일 속성 값을 받아와서 변경 - 계층을 표현하는 점선 출력
		OS.SetWindowLong(tree.handle, OS.GWL_STYLE, OS.GetWindowLong(tree.handle, OS.GWL_STYLE) | OS.TVS_HASLINES);
		
		treeViewer = new TreeViewer(tree);
		treeViewer.setContentProvider(new FileTreeContentProvider()); 	// 트리의 '내용'에 대한 정보
		treeViewer.setLabelProvider(new FileTreeLabelProvider()); 		// 트리의 '모양'에 대한 정보를 제공
		treeViewer.setInput(File.listRoots()); 							// 파일 트리의 루트.

		getSite().setSelectionProvider(treeViewer);
	}

	@Override
	public void setFocus() {
		treeViewer.getControl().setFocus();
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		super.dispose();
	}
}


