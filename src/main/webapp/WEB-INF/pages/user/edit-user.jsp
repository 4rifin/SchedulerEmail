<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ include file="/WEB-INF/includes/taglibs.jsp"%>

<html>
<head>
<title>Membuat CRUD Dan Scheduler Email Setiap 10 Menit Dengan JAVA Dan PostgreSQL - Menampilkan data dari
	database</title>
</head>
<body>
	<div class="judul">
		<h1>Membuat CRUD Scheduler Email Setiap 10 Menit Dengan JAVA Dan PostgreSQL</h1>
		<h2>Menampilkan data dari database</h2>
	</div>
	<div>
		<strong style="color: blue;">${message}</strong>
	</div>
	<br />
	<a href="/">Show Data</a>
	<br/>
	<h3>Edit User</h3>
	<form action="/editUser/" method="post">		
		<table>
			<tr>
				<td><input type="hidden" name="id" value="${user.id}"></td>					
			</tr>
			<tr>
				<td>User Name</td>
				<td><input type="text" name="userName" value="${user.userName}"></td>					
			</tr>	
			<tr>
				<td>Email</td>
				<td><input id="email" type="text" name="email" value="${user.email}" onkeyup="checkEmail();" autocomplete="off"></td>					
				<td><span id='messageEmail'></span></td>					
			</tr>
			<tr>
				<td>Status</td>
				<td>
				 	<select id="status" name="status" class="select2" required>
		                	<option value=""><c:out value="---Status---"/></option>
		                   		<c:if test="${user.status == '-1'}">
					                  <option selected value="-1">Active</option>
					            	  <option value="0">Inactive</option>
					            </c:if>
					            <c:if test="${user.status == '0'}">
					                  <option value="-1">Active</option>
					            	  <option selected value="0">Inactive</option>
					           </c:if>
		            </select>
			</tr>	
			<tr>
				<td></td>
				<td><input id="buttonSubmit" type="submit" value="Simpan" onclick="return confirm('Do you want edit?');return false;"></td>					
			</tr>				
		</table>
	</form>
</body>
&copy; Developer <!-- &copy; ndms.arifin@gmail.com -->
</html>

<script type="text/javascript" >
function validateEmail(email) {
	  var re = /^(([^<>()[\]\\.,;:\s@\"]+(\.[^<>()[\]\\.,;:\s@\"]+)*)|(\".+\"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
	  return re.test(email);
	}

	function checkEmail() {
	  var email = document.getElementById('email').value
	  if(email == ""){
		  document.getElementById('messageEmail').style.color = 'green';
		  document.getElementById('messageEmail').innerHTML = '';
	  }else{
		  if (validateEmail(email)) {
			  document.getElementById('messageEmail').style.color = 'green';
			  document.getElementById('messageEmail').innerHTML = 'email is valid';
			  showButton()
		  } else {
			  document.getElementById('messageEmail').style.color = 'red';
			  document.getElementById('messageEmail').innerHTML = 'email is not valid';
			  hideButton();
		  }  
	  }
	  
	}

 function hideButton(){
	 document.getElementById('buttonSubmit').style.display = 'none';
 }
 function showButton(){
	 document.getElementById('buttonSubmit').style.display = 'show';
 }
 </script>