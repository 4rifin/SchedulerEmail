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
		<h1>Scheduler Email Every 60 Minutes Dengan JAVA Dan PostgreSQL</h1>
		<h2>Menampilkan data dari database</h2>
	</div>

	<br />
	<a class="tombol" href="/addUser" target="_blank">+ Tambah Data Baru</a>

	<h3>Data User</h3>
	<table border="1" class="table">
		<tr>
			<th>No</th>
			<th>User Name</th>
			<th>Email</th>
			<th>Status</th>
			<th>Opsi</th>
		</tr>
		<c:forEach items="${listUser}" var="listUser" varStatus="theNumber">
			<tr align="center">
				<td>${theNumber.index+1}</td>
				<td>${listUser.userName}</td>
				<td>${listUser.email}</td>
				<td>
					<c:if test="${listUser.status == -1}">
						<c:out value="Active"/>
					</c:if>
					<c:if test="${listUser.status == 0}">
						<c:out value="Inactive"/>
					</c:if>
				</td>
				<td>
				<form id="delete${listUser.id}" action="/deleteUser/${listUser.id}" method="post">
					<a class="edit" href="/editUser/${listUser.id}">Edit</a> | 
					<input type="hidden" name="id" value="${listUser.id}">
					 <a href="#" onclick="validation(${listUser.id});">Delete</a>
				</form>
				 </td>
			</tr>
		</c:forEach>
	</table>
		&copy; Developer <!-- &copy; ndms.arifin@gmail.com -->
</body>
</html>
<script>
	function validation(id){
		var form = document.getElementById('delete'+id);
		var ask = confirm('Are you sure delete ? ');
		if(ask){
			form.submit();
			return true;
		}else{
			return false;
		}
	}
	function submitMe()
	{
		document.MyForm.submit();
	return;
	}
</script>