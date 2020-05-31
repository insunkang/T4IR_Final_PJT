<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">

<head>

  <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge">
  <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
  <meta name="description" content="">
  <meta name="author" content="">

  <title>MIRI</title>

  <!-- Custom fonts for this template-->
  <link href="/miri/static/vendor/fontawesome-free/css/all.min.css" rel="stylesheet" type="text/css">
  <link href="https://fonts.googleapis.com/css?family=Nunito:200,200i,300,300i,400,400i,600,600i,700,700i,800,800i,900,900i" rel="stylesheet">
  <script src="http://code.jquery.com/jquery-1.11.2.min.js"></script>

  <!-- Custom styles for this template-->
  <link href="/miri/static/css/sb-admin-2.min.css" rel="stylesheet">
<script type="text/javascript">
		$(document).ready(function() {
			// 아이디
			$("#inputFamilyId").on("keyup",function(){
				// <<jQuery에서 Ajax로 요청하기>> - get방식
				// url => 요청 path
				// data => 파라미터: json형식
				// 		*json형식 {"name":"value";"name":"value";...}
				// success함수: ajax요청해서 성공적으로 데이터를 받아왔을 때 처리할 내용을 함수로 표현
				// dataType: ajax요청 후 응답받을 데이터의 형식
				$.get("/miri/idCheck.do", // RequestMapping에 걸어준 것
						{"id":$("#inputFamilyId").val()}, 
						function(data) { //응답 데이터
							//alert(data);
							// ajax로 요청해서 응답받은 데이터를 <span>태그 내부에 출력
							$("#checkVal_id").text(data);
						},
						"text")
			});
			
			/* 동적컨텐츠에 이벤트 붙이기-on */
			buttonAble = document.getElementById("sub");
			$("#inputFamilyPasswordCheck").on("keyup",function(){
				check = $("#inputFamilyPassword").val();
				if(check == $("#inputFamilyPasswordCheck").val()){
					$("#checkVal_pass").text("일치");
					buttonAble.disabled = false;
				}else{
					$("#checkVal_pass").text("비밀번호가 일치하지 않습니다.");
					buttonAble.disabled = true;
				}
				
			});
	
		});
		
</script>

</head>

<body class="bg-gradient-primary">



  <div class="container">

    <div class="card o-hidden border-0 shadow-lg my-5">
      <div class="card-body p-0">
        <!-- Nested Row within Card Body -->
        <div class="row">
          <div class="col-lg-5 d-none d-lg-block bg-register-image"></div>
          <div class="col-lg-7">
            <div class="p-5">
              <div class="text-center">
                <h1 class="h4 text-gray-900 mb-4">Create an Account!</h1>
              </div>
              <form class="user"  action="/miri/admin/register.do" method="post">
                
                <div class="form-group">
                  <input type="text" class="form-control form-control-user" name="member_id" id="inputFamilyId" placeholder="ID">
                  <div style="text-align:right;">
                  	<span id="checkVal_id" style="color: red;" ></span>
                  </div>
                </div>
                <div class="form-group">
                  <input type="password" class="form-control form-control-user" name="member_pass" id="inputFamilyPassword" placeholder="Password">
                </div>
                <div class="form-group">
                  <input type="password" class="form-control form-control-user" id="inputFamilyPasswordCheck" placeholder="Password check">
                </div>
                <div style="text-align:right;">
                	<label id= "checkVal_pass" class="text-danger"></label>
                </div>
                <button id="sub" type="submit" class="btn btn-primary btn-user btn-block" style="">Register Account</button>
                <hr>
              </form>
              <hr>
              <div class="text-center">
                <a class="small" href="login.do"></a>
              </div>
              
              <div class="text-center">
                <a class="small">Already have an account? Good!</a>
              </div>
              <div class="text-center">
                <a class="small">Already have an account? Good!</a>
              </div>
              <div class="text-center">
                <a class="small">Already have an account? Good!</a>
              </div>
              <div class="text-center">
                <a class="small">Already have an account? Good!</a>
              </div>
              <div class="text-center">
                <a class="small">Already have an account? Good!</a>
              </div>
              <div class="text-center">
                <a class="small">Already have an account? Good!</a>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

  </div>

  <!-- Bootstrap core JavaScript-->
  <script src="/miri/static/vendor/jquery/jquery.min.js?v=<%=System.currentTimeMillis() %>"></script>
  <script src="/miri/static/vendor/bootstrap/js/bootstrap.bundle.min.js?v=<%=System.currentTimeMillis() %>"></script>

  <!-- Core plugin JavaScript-->
  <script src="/miri/static/vendor/jquery-easing/jquery.easing.min.js?v=<%=System.currentTimeMillis() %>"></script>

  <!-- Custom scripts for all pages-->
  <script src="/miri/static/js/sb-admin-2.min.js?v=<%=System.currentTimeMillis() %>"></script>

</body>

</html>
