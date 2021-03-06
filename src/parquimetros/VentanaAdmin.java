package parquimetros;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import quick.dbtable.DBTable;


public class VentanaAdmin extends JFrame
{
	   private JPanel pnlConsulta;
	   private JPanel pnlEntrada;
	   private JPanel pnlListas;
	   private String clave;
	   private JPasswordField ingresoContraseña;
	   private JLabel TituloLabel;
	   private JTextArea txtConsulta;
	   private JButton botonBorrar;
	   private JButton ejecutarBot;
	   private JButton volver;
	   private DBTable tabla;    
	   private JScrollPane scrConsulta;
	   private JScrollPane scrListaTablas;
	   private JScrollPane scrListaAtributos;
	   private JList<String> lTablas;
	   private JList<String>  lAtributos;
	   private String tablaElegida;
	   protected Connection conexionBD = null;
	   
	   
	   public VentanaAdmin() 
	   {
	      super();
	      iniGUIsesion();

	      
	   }
		// inicia la GUI de iniciar session
	   private void iniGUIsesion() {
			this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			setBounds(100, 100, 550, 400);
			pnlEntrada = new JPanel();
			pnlEntrada.setBorder(new EmptyBorder(5, 5, 5, 5));
			pnlEntrada.setLayout(null);
			
			pnlListas=new JPanel();
			pnlListas.setLayout(new GridLayout(1,2));
			JLabel TituloLabel = new JLabel("Introdusca contraseña del administrador :");
			TituloLabel.setBounds(100, 90, 300, 80);
			pnlEntrada.add(TituloLabel);
			
			JPasswordField ingresoContraseña = new JPasswordField();
			ingresoContraseña.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					clave=String.valueOf(ingresoContraseña.getPassword());
					if (clave.equals("admin")) {
						
					
					pnlEntrada.setVisible(false);
				     initGUI();
					}
					else { ingresoContraseña.setText("");
					JOptionPane.showMessageDialog(null,"Contraseña incorrecta, porfavor intente de nuevo","Error al introducir contraseña",JOptionPane.INFORMATION_MESSAGE);
					}
					}
				
			});
			ingresoContraseña.setBounds(100, 175, 200, 44);
			
			pnlEntrada.add(ingresoContraseña);
			pnlEntrada.setVisible(true);
			JButton btnVolver = new JButton("Volver ");
			btnVolver.setBounds(271, 299, 89, 23);
			
			btnVolver.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e) {
					InicioSesion nueva=new InicioSesion();
					cerrarVentana();
					nueva.setVisible(true);
				}
			});
			pnlEntrada.add(btnVolver);
			
			 getContentPane().add(pnlEntrada);
		}
	   
	   //inicia toda la GUI del administrador
	   private void initGUI() 
	   {
	      try {
	         setPreferredSize(new Dimension(900, 600));
	         this.setBounds(0, 0, 900, 600);
	         setVisible(true);
	         BorderLayout thisLayout = new BorderLayout();
	         this.setTitle("Consultas ");
	         getContentPane().setLayout(thisLayout);

	         this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

	         conectarBD();
	        
	         lTablas=new JList<String>(llenarTabla());
	         lTablas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	         
	         DefaultListModel<String> modelA = new DefaultListModel<>();
	         modelA.addElement("");
	         lAtributos=new JList<String>(modelA);
			 lAtributos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
	         lTablas.addListSelectionListener(new ListSelectionListener() { 
	         
	        	       
					public void valueChanged(ListSelectionEvent event) {
						 if (!event.getValueIsAdjusting()){ 
		        	         JList<String> source = (JList<String>)event.getSource(); 
		        	         tablaElegida = source.getSelectedValue().toString();
		        	         llenarListaAtributos();
		        	        } 
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
	       	 	volver = new JButton("Volver ");				
				volver.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent e) {
						VentanaAdmin nueva=new VentanaAdmin();
						cerrarVentana();
						nueva.setVisible(true);
					}
				});
				pnlConsulta.add(volver);
	         }
	         {
	        	// crea la tabla  
	        	tabla = new DBTable();
	        	
	        	
	        	
	        	// Agrega la tabla al frame (no necesita JScrollPane como Jtable)
	            getContentPane().add(tabla, BorderLayout.CENTER);           
	                      
	           // setea la tabla para sólo lectura (no se puede editar su contenido)  
	           tabla.setEditable(false);
	           scrListaTablas=new JScrollPane();
	           scrListaAtributos=new JScrollPane();
	           scrListaTablas.setViewportView(lTablas);
	           scrListaAtributos.setViewportView(lAtributos);
	           pnlListas.add(scrListaTablas);
	           pnlListas.add(scrListaAtributos);
	   
	           pnlListas.setVisible(true);
	           getContentPane().add(pnlListas, BorderLayout.SOUTH);     
	         
	           
	          
	         }
	       
	      }
	      catch (Exception e) {
	         e.printStackTrace();
	      }
	   } 



	   private void btnRefreshTabla(ActionEvent evt) 
	   {
	      this.refrescarTabla();      
	   }
	    
	   private void conectarBD()
	   {
	         try
	         {
	            
	        	String servidor = "localhost:3306";
	        	String baseDatos = "parquimetros"; 
	        	String usuario = "admin";
	        	String clave = "admin";
	            String uriConexion = "jdbc:mysql://" + servidor + "/" + 
	        	                     baseDatos +"?serverTimezone=America/Argentina/Buenos_Aires";
	            
	            
	       //establece una conexión con la  B.D. "parquimetros"  usando directamante una tabla DBTable    
	           
	            this.conexionBD = DriverManager.getConnection(uriConexion, usuario, clave);
	           
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
	    	 Statement stmt = this.conexionBD.createStatement();
	    	 String sql=this.txtConsulta.getText().trim();
	    	 if (!sql.isEmpty()) {
	    	 String comp=String.valueOf(sql.charAt(0));
	    	 	if (comp.equals("S")|| comp.equals("s")) {
	    	 		ResultSet rs= stmt.executeQuery(sql);
	    	 		tabla.refresh(rs);
	    	 		}
	    	 	else {
	    	 		stmt.execute(sql);
	    	 	}
	    	 }
	    	 
	
	    	  // modificar la forma en que se muestran de algunas columnas  
	     	    
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
	       	  
	    	desconectarBD();
	    	  
	          
	    	  
	    	  
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
	   //devuelve una lista con todas las tablas que hay en la BD
	   private DefaultListModel<String> llenarTabla() {
		   try
		   {
			DefaultListModel<String> model = new DefaultListModel<>();
		  
		   DatabaseMetaData mDatos= conexionBD.getMetaData();
		
		   java.sql.ResultSet rs = mDatos.getTables("parquimetros",null,"%",null);
				   
		 
		   while (rs.next())
		   {
		   String nombreTabla = rs.getString(3);
		    model.addElement(nombreTabla);
		   
		   }
		   rs.close();
		
		   return model;
		   }
		   catch (java.sql.SQLException ex) {
			   return null;
		   } 
	   }
	   private void llenarListaAtributos() {
		   try
		   {
			DefaultListModel<String> model = new DefaultListModel<>();
		  
		   DatabaseMetaData mDatos= conexionBD.getMetaData();
		
		   java.sql.ResultSet rs = mDatos.getColumns(null,null,tablaElegida,null);
				   
		 
		   while (rs.next())
		   {
			
		   String nombreTabla = rs.getString(4);

		    model.addElement(nombreTabla);
		   
		   }
		   lAtributos.setModel(model);
		   
		   rs.close();
		
		  
		   }
		   catch (java.sql.SQLException ex) {
			  ex.getStackTrace();
		   } 
	   }
	   private void cerrarVentana() {
			this.dispose();
		}
	}