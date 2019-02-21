package thorsten.ide;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.RandomAccessFile;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.JTree;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

public class MainFrame extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private AboutThorsten abth = new AboutThorsten();
	private JTree projectTree;
	private RandomAccessFile raf;
	private String projName=null; 
	private String source=null;
	private String asembler=null;
	private String hex=null;
	private JTextArea textSource;
	private JPanel panel_1;
	private JPanel panel;
	private File SelectedFile=null;
	private JMenuItem mntmSaveProject;
	private JMenuItem mntmSaveProjectAs;
	private char state= 's';
	/**
	 * @wbp.nonvisual location=-16,-3
	 */
	private final javax.swing.Timer timer = new javax.swing.Timer(0, (ActionListener) null);
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				
				try {
					for( LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()){
						if("Nimbus".equals(info.getName())){
							UIManager.setLookAndFeel(info.getClassName());
							break;
						}
					}
					MainFrame frame = new MainFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public MainFrame() {
		timer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				writeFile(SelectedFile);
			}
		});
		timer.setDelay(60000);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
			}
		});
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 727, 586);
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);
		
		JMenuItem mntmOpen = new JMenuItem("Open");
		mntmOpen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser dlg = new JFileChooser();
				FileFilter filter = new FileNameExtensionFilter("Project files(*.pro)", "pro");
				dlg.addChoosableFileFilter(filter);
				if(dlg.showOpenDialog(null) == JFileChooser.APPROVE_OPTION){
					File selectedFile= dlg.getSelectedFile();
					readFile(selectedFile);
					mntmSaveProject.setEnabled(true);
					mntmSaveProjectAs.setEnabled(true);
				}
			}
		});
		
		JMenuItem mntmNewProject = new JMenuItem("New Project");
		mntmNewProject.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				projName = JOptionPane.showInputDialog("How would you like to name your project?");
				panel.setBorder(new TitledBorder(null, projName, TitledBorder.LEADING, TitledBorder.TOP, null, null));
				DefaultTreeModel model = (DefaultTreeModel)projectTree.getModel();
				DefaultMutableTreeNode root = (DefaultMutableTreeNode) model.getRoot();
				root.setUserObject(projName);
				model.nodeChanged(root);
				mntmSaveProject.setEnabled(true);
				mntmSaveProjectAs.setEnabled(true);
			}
		});
		
		mntmNewProject.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_MASK));
		mnFile.add(mntmNewProject);
		
		JMenuItem mntmBla = new JMenuItem("bla");
		mntmBla.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				projName ="testProject";
				source ="testSource";
				asembler ="testAsembler";
				hex ="testHex";
				mntmSaveProject.setEnabled(true);
				mntmSaveProjectAs.setEnabled(true);
			}
		});
		mnFile.add(mntmBla);
		mntmOpen.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_MASK));
		mnFile.add(mntmOpen);
		
		mntmSaveProject = new JMenuItem("Save Project");
		mntmSaveProject.setEnabled(false);
		mntmSaveProject.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(SelectedFile!=null){
					writeFile(SelectedFile);
				}else{
					JFileChooser dlg = new JFileChooser();

					FileFilter filter = new FileNameExtensionFilter(
							"Project files(*.pro)", "pro");
					dlg.addChoosableFileFilter(filter);
					if (dlg.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
						File selectedFile = dlg.getSelectedFile();
						FileFilter actFilter = dlg.getFileFilter();
						if (actFilter instanceof FileNameExtensionFilter) {
							String ext = ((FileNameExtensionFilter) actFilter).getExtensions()[0];
							String fileName = selectedFile.getPath();
							if (!fileName.endsWith(ext)) {
								fileName += "." + ext;
							}
							selectedFile = new File(fileName);
						}
						SelectedFile = selectedFile;
						writeFile(selectedFile);
				}
			}
		}
		});
		mntmSaveProject.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK));
		mnFile.add(mntmSaveProject);
		
		mntmSaveProjectAs = new JMenuItem("Save Project as...");
		mntmSaveProjectAs.setEnabled(false);
		mntmSaveProjectAs.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser dlg = new JFileChooser();

				FileFilter filter = new FileNameExtensionFilter(
						"Project files(*.pro)", "pro");
				dlg.addChoosableFileFilter(filter);
				if (dlg.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
					File selectedFile = dlg.getSelectedFile();
					FileFilter actFilter = dlg.getFileFilter();
					if (actFilter instanceof FileNameExtensionFilter) {
						String ext = ((FileNameExtensionFilter) actFilter).getExtensions()[0];
						String fileName = selectedFile.getPath();
						if (!fileName.endsWith(ext)) {
							fileName += "." + ext;
						}
						selectedFile = new File(fileName);
					}
					SelectedFile = selectedFile;
					timer.start();
					writeFile(selectedFile);
				}
			}
		});
		mntmSaveProjectAs.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.CTRL_MASK | InputEvent.SHIFT_MASK));
		mnFile.add(mntmSaveProjectAs);
		
		JSeparator separator = new JSeparator();
		mnFile.add(separator);
		
		JMenuItem mntmExit = new JMenuItem("Exit");
		mntmExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.exit(0);
			}
		});
		mntmExit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, InputEvent.ALT_MASK));
		mnFile.add(mntmExit);
		
		JMenu mnEdit = new JMenu("Edit");
		menuBar.add(mnEdit);
		
		JMenuItem mntmCut = new JMenuItem("Cut");
		mntmCut.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				textSource.cut();
			}
		});
		
		JMenuItem mntmUndo = new JMenuItem("Undo");
		mntmUndo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
			}
		});
		mnEdit.add(mntmUndo);
		
		JMenuItem mntmRedo = new JMenuItem("Redo");
		mntmRedo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
			}
		});
		mnEdit.add(mntmRedo);
		
		JSeparator separator_1 = new JSeparator();
		mnEdit.add(separator_1);
		mnEdit.add(mntmCut);
		
		JMenuItem mntmCopy = new JMenuItem("Copy");
		mntmCopy.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				textSource.copy();
			}
		});
		mnEdit.add(mntmCopy);
		
		JMenuItem mntmPaste = new JMenuItem("Paste");
		mntmPaste.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				textSource.paste();
			}
		});
		mnEdit.add(mntmPaste);
		
		JMenu mnProject = new JMenu("Project");
		menuBar.add(mnProject);
		
		JMenuItem mntmCompileSource = new JMenuItem("Compile Source");
		mntmCompileSource.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.ALT_MASK));
		mnProject.add(mntmCompileSource);
		
		JMenuItem mntmCompileProject = new JMenuItem("Compile Project");
		mntmCompileProject.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.ALT_MASK | InputEvent.SHIFT_MASK));
		mnProject.add(mntmCompileProject);
		
		JMenu mnHelp = new JMenu("Help");
		menuBar.add(mnHelp);
		
		JMenuItem mntmAboutThorsten = new JMenuItem("About Thorsten");
		mntmAboutThorsten.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				abth.setVisible(true);
			}
		});
		mnHelp.add(mntmAboutThorsten);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		panel = new JPanel();
		panel.setBorder(new TitledBorder(null, "Project", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel.setBackground(Color.WHITE);
		contentPane.add(panel, BorderLayout.WEST);
		panel.setLayout(new BorderLayout(0, 0));
		
		projectTree = new JTree();
		projectTree.setRootVisible(false);
		projectTree.setModel(new DefaultTreeModel(
			new DefaultMutableTreeNode("New Project") {
				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;

				{
					DefaultMutableTreeNode node_1;
					add(new DefaultMutableTreeNode("Source Code"));
					node_1 = new DefaultMutableTreeNode("Compiler Data");
						node_1.add(new DefaultMutableTreeNode("Asembler Code"));
						node_1.add(new DefaultMutableTreeNode("Hex Code"));
					add(node_1);
				}
			}
		));
		panel.add(projectTree);
		
		panel_1 = new JPanel();
		panel_1.setBorder(new TitledBorder(null, "Source Code", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		contentPane.add(panel_1, BorderLayout.CENTER);
		panel_1.setLayout(new BorderLayout(0, 0));
		
		JScrollPane scrollPane = new JScrollPane();
		panel_1.add(scrollPane, BorderLayout.CENTER);
		
		textSource = new JTextArea();
		scrollPane.setViewportView(textSource);
		
		JPanel panel_3 = new JPanel();
		contentPane.add(panel_3, BorderLayout.SOUTH);
		panel_3.setLayout(new BorderLayout(0, 0));
		
		JScrollPane scrollPane_1 = new JScrollPane();
		panel_3.add(scrollPane_1, BorderLayout.NORTH);
		
		JTextArea textArea = new JTextArea();
		textArea.setEditable(false);
		scrollPane_1.setViewportView(textArea);
		
		setExtendedState(Frame.MAXIMIZED_BOTH);
		
	}
	private void writeFile(File file) {
		try {
			source=textSource.getText();
			//asembler=?;
			//hex=?;
			raf = new RandomAccessFile(file,"rw");
			raf.writeUTF(projName+"%s%"+source+"%s%"+asembler+"%s%"+hex);
			if(raf!=null) raf.close();
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null,
					"Fehler beim Schreiben der Datei " + file.getParent() + "("
							+ e.getMessage() + ")", "Fehler",
					JOptionPane.ERROR_MESSAGE);
		} 
	}
	private void readFile(File inFile){
		try {
			textSource.setText(null);
			raf = new RandomAccessFile(inFile, "rw");
			String tmp=null;
			tmp=raf.readUTF();
			String[] help=tmp.split("%s%");
			projName=help[0];
			source=help[1];
			asembler=help[2];
			hex=help[3];
			setTextSource();
			panel.setBorder(new TitledBorder(null, projName, TitledBorder.LEADING, TitledBorder.TOP, null, null));
			if(raf!=null) raf.close();

			//textSource.append("Name  "+projName+"     Source  "+source+"     Asembler   "+asembler+"     Hex   "+hex); test ob die daten richtig gespeichert werden
			
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Fehler beim lesen der Datei"+ e.getMessage(), "Fehler", JOptionPane.ERROR_MESSAGE);
		}
	}
	private void setTextSource(){
		textSource.setText(source);
	}
	private void setTextAsembler(){
		textSource.setText(asembler);
	}
	private void setTextHex(){
		textSource.setText(hex);
	}
	DefaultMutableTreeNode node = (DefaultMutableTreeNode) projectTree.getLastSelectedPathComponent();
	MouseListener ml = new MouseAdapter() {
	     public void mouseReleased(MouseEvent e) {
	         int selRow = projectTree.getRowForLocation(e.getX(), e.getY());
	         if(selRow != -1) {
	        	 if((node.toString().equals("Source Code")) &&(state!='s')) {
	                 switch(state){
	                 	case 'a':
	                 		asembler=textSource.getText();
	                 	break;
	                 	case 'h':
                 			hex=textSource.getText();
	                 	break;
	                 }
	            	 textSource.setText(source);
	            	 state = 's';
	             }
	        	 if((node.toString().equals("Asembler Code")) &&(state!='a')) {
	                 switch(state){
	                 	case 's':
	                 		source=textSource.getText();
	                 	break;
	                 	case 'h':
                 			hex=textSource.getText();
	                 	break;
	                 }
	            	 textSource.setText(asembler);
	            	 state = 'a';
	             }
	        	 if((node.toString().equals("Hex Code")) &&(state!='h')) {
	                 switch(state){
	                 	case 'a':
	                 		asembler=textSource.getText();
	                 	break;
	                 	case 's':
	                 		source=textSource.getText();
	                 	break;
	                 }
	            	 textSource.setText(hex);
	            	 state = 'h';
	             }   
	             }
	         }
	     };
}
