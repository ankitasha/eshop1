<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Amazon Online Shop</title>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/style.css">
</head>
<body>
   <jsp:include page="_header.jsp"/>
   <jsp:include page="_menu.jsp" />
 
   <div class="page-title">Shopping Cart Demo</div>
  
   <div class="demo-container">
   <h3>Demo content</h3>
    <ul>
     <li>Buy Online</li>
     <li>Admin Pages</li>
     <li>Reports</li>
    </ul>
   </div>
  
   <jsp:include page="_footer.jsp" />
 
</body>
</html> 