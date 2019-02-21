package thorsten.ide;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.border.EmptyBorder;

public class AboutThorsten extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			
				for( LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()){
					if("Nimbus".equals(info.getName())){
						UIManager.setLookAndFeel(info.getClassName());
						break;
					}
				}
			AboutThorsten dialog = new AboutThorsten();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public AboutThorsten() {
		setTitle("About Thorsten");
		setBounds(100, 100, 372, 298);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BorderLayout(0, 0));
		{
			JTextPane txtpnThisIsThe = new JTextPane();
			txtpnThisIsThe.setEditable(false);
			txtpnThisIsThe.setBackground(new Color(238,238,238));
			txtpnThisIsThe.setFont(new Font("Dialog", Font.PLAIN, 14));
			txtpnThisIsThe.setText("\n\nThis is the Thorsten IDE for the simple PIC programming language PICkup.\n\nPICkup by Markus B.\nThorsten by Florian M.\n\nIf you have any questions Contact us with the Contact button below!");
			contentPanel.add(txtpnThisIsThe, BorderLayout.CENTER);
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton btnBugreport = new JButton("Bugreport");
				btnBugreport.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						try {
							
							int res = JOptionPane.showConfirmDialog(null, "Do you really want to send a bugreport?\n\n" +
									"If you do, please describe detailed how the Error or Crash happened!\n" +
									"For Example: I pressed Button A and the programm said Error B occured.", "Bugreport", JOptionPane.OK_OPTION);
							//title = "Bugreport"
							if(res==JOptionPane.OK_OPTION){
							Desktop.getDesktop().mail(new URI("mailto:peter@trash-mail.com?Subject=Thorsten_Bugrep"));
							}
						} catch (IOException e) {
							e.printStackTrace();
						} catch (URISyntaxException e) {
							e.printStackTrace();
						}
					}
				});
				buttonPane.add(btnBugreport);
			}
			{
				JButton btnContact = new JButton("Contact");
				btnContact.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						try {
							Desktop.getDesktop().mail(new URI("mailto:peter@trash-mail.com?Subject=Thorsten_Contact"));
						} catch (IOException e) {
							e.printStackTrace();
						} catch (URISyntaxException e) {
							e.printStackTrace();
						}
					}
				});
				buttonPane.add(btnContact);
			}
			{
				JButton btnWebsite = new JButton("Website");
				btnWebsite.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						try {
							Desktop.getDesktop().browse(new URI("http://www.nyan.cat"));
						} catch (IOException e) {
							e.printStackTrace();
						} catch (URISyntaxException e) {
							e.printStackTrace();
						}
					}
				});
				buttonPane.add(btnWebsite);
			}
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						dispose();
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
		}
	}

}
