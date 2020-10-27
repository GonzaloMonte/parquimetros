package parquimetros;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.sql.SQLException;
import java.sql.Types;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.border.BevelBorder;

import quick.dbtable.DBTable;


public class VentanaAdmin extends javax.swing.JInternalFrame 
{
	   private JPanel pnlConsulta;
	   private JTextArea txtConsulta;
	   private JButton botonBorrar;
	   private JButton ejecutarBot;
	   private DBTable tabla;    
	   private JScrollPane scrConsulta;
	   private JList<String> lTablas;
	   private JList<String> lAtributos;
	   
	   
	   public VentanaAdmin() 
	   {
	      super();
	      initGUI();
	   }
	   
	   private void initGUI() 
	   {
	      try {
	         setPreferredSize(new Dimension(800, 600));
	         this.setBounds(0, 0, 800, 600);
	         setVisible(true);
	         BorderLayout thisLayout = new BorderLayout();
	         this.setTitle("Consultas ");
	         getContentPane().setLayout(thisLayout);
	         this.setClosable(true);
	         this.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
	         this.setMaximizable(true);
	         
	         lTablas=new JList<String>();
	         lAtributos=new JList<String>();
	         
	         this.addComponentListener(new ComponentAdapter() {
	            public void componentHidden(ComponentEvent evt) {
	               thisDesconectorBD(evt);
	            }
	            public void componentShown(ComponentEvent evt) {
	               thisConectorBD(evt);
	            }
	         });
	         {
	            pnlConsulta = new JPanel();
	            getContentPane().add(pnlConsulta, BorderLayout.NORTH);
	            {
	               scrConsulta = new JScrollPane();
	               pnlConsulta.add(scrConsulta);
	               {
	                  txtConsulta = new JTextArea();
	                  scrConsulta.setViewportView(txtConsulta);
	                  txtConsulta.setTabSize(3);
	                  txtConsulta.setColumns(80);
	                  txtConsulta.setBorder(BorderFactory.createEtchedBorder(BevelBorder.LOWERED));
	                  txtConsulta.setFont(new java.awt.Font("Monospaced",0,12));
	                  txtConsulta.setRows(10);
	               }
	            }
	            {
	               ejecutarBot = new JButton();
	               pnlConsulta.add(ejecutarBot);
	               ejecutarBot.setText("Ejecutar");
	               ejecutarBot.addActionListener(new ActionListener() {
	                  public void actionPerformed(ActionEvent evt) {
	                     btnRefreshTabla(evt);
	                  }
	               });
	            }
	            {
	            	botonBorrar = new JButton();
	            	pnlConsulta.add(botonBorrar);
	            	botonBorrar.setText("Borrar");            
	            	botonBorrar.addActionListener(new ActionListener() {
	            		public void actionPerformed(ActionEvent arg0) {
	            		  txtConsulta.setText("");            			
	            		}
	            	});
	            }	
	         }
	         {
	        	// crea la tabla  
	        	tabla = new DBTable();
	        	
	        	// Agrega la tabla al frame (no necesita JScrollPane como Jtable)
	            getContentPane().add(tabla, BorderLayout.CENTER);           
	                      
	           // setea la tabla para sólo lectura (no se puede editar su contenido)  
	           tabla.setEditable(false);       
	           
	           
	           
	         }
	      } catch (Exception e) {
	         e.printStackTrace();
	      }
	   }

	   private void thisConectorBD(ComponentEvent evt) 
	   {
	      this.conectarBD();
	   }
	   
	   private void thisDesconectorBD(ComponentEvent evt) 
	   {
	      this.desconectarBD();
	   }

	   private void btnRefreshTabla(ActionEvent evt) 
	   {
	      this.refrescarTabla();      
	   }
	       
	   private void conectarBD()
	   {
	         try
	         {
	            String driver ="com.mysql.cj.jdbc.Driver";
	        	String servidor = "localhost:3306";
	        	String baseDatos = "parquimetros"; 
	        	String usuario = "admin";
	        	String clave = "admin";
	            String uriConexion = "jdbc:mysql://" + servidor + "/" + 
	        	                     baseDatos +"?serverTimezone=America/Argentina/Buenos_Aires";
	   
	       //establece una conexión con la  B.D. "parquimetros"  usando directamante una tabla DBTable    
	            tabla.connectDatabase(driver, uriConexion, usuario, clave);
	           
	         }
	         catch (SQLException ex)
	         {
	            JOptionPane.showMessageDialog(this,
	                           "Se produjo un error al intentar conectarse a la base de datos.\n" 
	                            + ex.getMessage(),
	                            "Error",
	                            JOptionPane.ERROR_MESSAGE);
	            System.out.println("SQLException: " + ex.getMessage());
	            System.out.println("SQLState: " + ex.getSQLState());
	            System.out.println("VendorError: " + ex.getErrorCode());
	         }
	         catch (ClassNotFoundException e)
	         {
	            e.printStackTrace();
	         }
	      
	   }

	   private void desconectarBD()
	   {
	         try
	         {
	            tabla.close();            
	         }
	         catch (SQLException ex)
	         {
	            System.out.println("SQLException: " + ex.getMessage());
	            System.out.println("SQLState: " + ex.getSQLState());
	            System.out.println("VendorError: " + ex.getErrorCode());
	         }      
	   }

	   private void refrescarTabla()
	   {
	      try
	      {    
	    	 // seteamos la consulta a partir de la cual se obtendrán los datos para llenar la tabla
	    	 tabla.setSelectSql(this.txtConsulta.getText().trim());

	    	  // obtenemos el modelo de la tabla a partir de la consulta para 
	    	  // modificar la forma en que se muestran de algunas columnas  
	    	  tabla.createColumnModelFromQuery();    	    
	    	  for (int i = 0; i < tabla.getColumnCount(); i++)
	    	  { // para que muestre correctamente los valores de tipo hora	   		  
	    		 if	 (tabla.getColumn(i).getType()==Types.TIME)  
	    		 {    		 
	    		    tabla.getColumn(i).setType(Types.CHAR);  
	  	       	 }
	    	
	    		 if	 (tabla.getColumn(i).getType()==Types.DATE)
	    		 {
	    		    tabla.getColumn(i).setDateFormat("dd/MM/YYYY");
	    		 }
	          }  
	    	  // actualizamos el contenido de la tabla.   	     	  
	    	  tabla.refresh();
	  
	          
	    	  
	    	  
	       }
	      catch (SQLException ex)
	      {
	         // se muestra porque ocurre el error
	         System.out.println("SQLException: " + ex.getMessage());
	         System.out.println("SQLState: " + ex.getSQLState());
	         System.out.println("VendorError: " + ex.getErrorCode());
	         JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(this),
	                                       ex.getMessage() + "\n", 
	                                       "Error al ejecutar la consulta.",
	                                       JOptionPane.ERROR_MESSAGE);
	         
	      }
	      
	   }

	   
	}