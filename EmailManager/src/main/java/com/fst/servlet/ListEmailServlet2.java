/*******************************CODE JAVA DU TP1:  PARTIE_2_2 (échange entre Servlet/JSP selon le modèle MVC) **********************************************************************/

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



public class ListEmailServlet2 extends HttpServlet {                
	private static final long serialVersionUID = 1L;
	                    
/****************************************************  Declaration  des Variables    *************************************************************/
	
	public  String F_adresse;                                              // Path du fichier
	public List<Object> list_adress = new ArrayList<Object>();             // List des adresses recuperé dans le fichier
	

	
/*****************************************************Constructor************************************************************************************/	
	 
	public ListEmailServlet2(){
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
	  String adress= request.getParameter("adress") ;  // recuration de l'adresse du champs du formulaire
      String suite = null;
      
      if(adress.equals("")) { //si le champs du formulaire est vide
     	 response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Aucune  addresse email specifiée."); //erreur du champs vide de formulaire
      }	 
      else {
    	   /** geston des boutons du formulaire (inscription et supression) **/
    	 String action= request.getParameter("action") ;
         if(action.equals("subscribe")) {  //bouton subscribe cliqué
     	     subscribe(adress);    //insertion d'email        
 		     suite="inscrite";
          }
         else {      //dans le cas contraire (bouton unsubscibe cliqué)
         	if(list_adress.contains(adress)) {    // si la liste contient l'adresse du champs
         		unsubscribe(adress); // subscription de l'adress
                 suite="suprimé";
         	}
         	else {    //si non (la list ne contient pas l'adresse du champs)
         		 response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Adress  "+adress+" non inscrite"); //erreur de supression d'adresse inexistant
         	}
             
          } 
         
         /*****redirection vers le fichier Resultat_Oper.jsp *******/
      HttpSession session = request.getSession(true);
	  session.setAttribute("adress",adress);
	  session.setAttribute("suite", suite);
	  request.getRequestDispatcher("/Resultat_Oper.jsp").forward(request,response);	
      }
	}

	

}
