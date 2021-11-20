<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"
     import = "java.util.ArrayList"
    import = "java.util.List"
    %>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
  <h2>Membres:</h2>
   <%
  for (Object str : (List)session.getValue("list_adress")) {
      out.println(str);%><br>
   <%  }
  %>
  <hr>
  <FORM METHOD=POST ACTION=ListEmailServlet>
   Entrer vore adresse email: <INPUT TYPE=TEXT NAME=adress><br>
  <INPUT TYPE=SUBMIT NAME=action VALUE=subscribe>
  <INPUT TYPE=SUBMIT NAME=action VALUE=unsubscribe>
  </FORM>
</body>
</html>