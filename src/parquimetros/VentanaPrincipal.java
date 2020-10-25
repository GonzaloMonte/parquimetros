package parquimetros;

import java.awt.BorderLayout;

import javax.swing.JDesktopPane;
import javax.swing.SwingUtilities;


public class VentanaPrincipal  extends javax.swing.JFrame {
		private JDesktopPane panel;
		protected static Login l;
		   public static void main(String[] args) 
		   {
		      SwingUtilities.invokeLater(new Runnable() {
		         public void run() {
		            VentanaPrincipal inst = new VentanaPrincipal();
		            inst.setLocationRelativeTo(null);
		            inst.setVisible(true);
		         }
		      });
		   }
	
	public VentanaPrincipal() 
	   {
	      super();
	      panel = new JDesktopPane();
          getContentPane().add(panel, BorderLayout.CENTER);
          panel.setPreferredSize(new java.awt.Dimension(800, 600));
	      l=new Login();
	      l.setVisible(true);
	     
	   }
}
