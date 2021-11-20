/*******************************CODE JAVA DU TP1: DE LA PARTIE_1  A LA  PARTIE_2_1**********************************************************************/


package com.fst.servlet;

/*** Nécessaire  pour manupiler le fichier et creer la list de sauvegarde des emails  (avec des exceptions)   ***************************************/

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;



/****************************************Pour le servlet ****************************************************************************************/ 

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.swing.Spring;



public class ListEmailServlet extends HttpServlet {                
	private static final long serialVersionUID = 1L;
	                    
/****************************************************  Declaration  des Variables    *************************************************************/
	
	public  String F_adresse;                                              // Path du fichier
	public List<Object> list_adress = new ArrayList<Object>();             // List des adresses recuperé dans le fichier
	

	
/*****************************************************Constructor************************************************************************************/	
	 
	public ListEmailServlet(){
		super();
	}
	

/****************************************************La methode init()***********************************************************************************************/	
	public void init() {
	    
		F_adresse = this.getInitParameter("membres");    //recuperation du path du fichier adresses.txt dans web.xml
		
		ObjectInputStream in;
	     /** Lecture et chargement des adresses dans la liste list_adress  **/
		try {
			in = new ObjectInputStream(new FileInputStream(F_adresse));
			while(in.available()>0){  
	              list_adress.add(in.readObject());          
	         }
		} catch (IOException | ClassNotFoundException e) {
			
			e.printStackTrace();
		}	
	}
	

/************************************************La Methode doget *******************************************************************************************************/

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		      
        
            /************************avant la jsp de presentation  **********************
     
             PrintWriter out = response.getWriter();
             out.println("<html>");
             out.println("<head><title>liste email</title></head>");
             out.println("<body>");
             out.println("<h2>Membres</h2>");
             for (Object str : list_adress) {
               out.println(str);
               out.println("<br>");
             }
             out.println("<FORM METHOD=POST ACTION=ListEmailServlet>");
             out.println("Entrer vore adress email: <INPUT TYPE=TEXT NAME=adress><BR>");
             out.println("<INPUT TYPE=SUBMIT NAME=action VALUE=subscribe>");
             out.println("<INPUT TYPE=SUBMIT NAME=action VALUE=unsubscribe>");
             out.println("</body></html>");	
             
            /******************************************************************************/
		
		
		
		/**** Redirection vers le fichier  ListEmailMVC *****/
		HttpSession session = request.getSession(true);            
		session.setAttribute("list_adress", list_adress);         
		request.getRequestDispatcher("/ListEmailMVC.jsp").forward(request,response);   
	}

	
/**********************************************La methode d'insertion d'email dans le fichier subscribe()*****************************************************************/	
  
	private void subscribe(String email) throws IOException {
	  list_adress.add(email);
	  save();
  }
	
  
 /**********************************************La methode de supression d'email dans le fichier unsubscribe()*****************************************************************/	
  
  private void  unsubscribe(String email) throws IOException {
	    	  list_adress.remove(email);
	  save();
  }
  
/******************************************************save()*******************************************************************************************************************/  
  private void save() throws  IOException {
	  ObjectOutputStream out= new ObjectOutputStream(new FileOutputStream(F_adresse));
	  out.writeObject(list_adress);
	  out.close();
	  }
  
  
/**********************************************************La metode dopost()****************************************************************************************************/

  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	  String adress= request.getParameter("adress") ;
	  String action= request.getParameter("action") ;
      PrintWriter out = response.getWriter();
      String suite = null;
     if(adress.equals("")) {
    	 response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Aucune  addresse email specifiée.");
     }	 
     else {
        if(action.equals("subscribe")) {
    	     subscribe(adress);
		     suite="inscrite";
         }
        else {
        	if(list_adress.contains(adress)) {
        		unsubscribe(adress);
                suite="suprimé";
        	}
        	else {
        		 response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Adress  "+adress+" non inscrite");
        	}
            
         } 
     
		out.println("<html>");
		out.println("<head><title>liste email</title></head>");
		out.println("<body>");
		out.println("<p>L' adresse "+adress+" "+suite+"</p>");
		out.println("<a href='ListEmailServlet'>Afficher la liste</a>");
		out.println("</body></html>"); 
		}    
	}

}
