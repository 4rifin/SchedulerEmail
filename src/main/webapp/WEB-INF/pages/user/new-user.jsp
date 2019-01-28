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
	<h3>Add New User</h3>
	<form id="addNew"action="/addUser/new" method="post">		
		<table>
			<tr>
				<td>User Name</td>
				<td><input type="text" name="userName" value="${register.userName}"></td>					
			</tr>	
			<tr>
				<td>Email</td>
				<td><input id="email" type="text" name="email" onkeyup="checkEmail();" autocomplete="off" value="${register.email}"></td>					
				<td><span id='messageEmail'></span></td>
			</tr>
			<tr>
				<td></td>
				<td><input id="buttonSubmit" type="submit" value="Save" onclick="return confirm('Do you want save?');return false;"></td>					
			</tr>				
		</table>
		&copy; Developer <!-- &copy; ndms.arifin@gmail.com -->
	</form>
</body>

</html>
<script type="text/javascript" >
function validationRegister(){
	var exec = document.getElementById('addNew');
			exec.submit();
			return true;
		
	}
 
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
		  } else {
			  document.getElementById('messageEmail').style.color = 'red';
			  document.getElementById('messageEmail').innerHTML = 'email is not valid';
		  }  
	  }
	  
	}
</script>        