<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>

<head>

  <meta charset="utf-8">
  <!-- <meta http-equiv="X-UA-Compatible" content="IE=edge"> -->
  <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
  <meta name="description" content="">
  <meta name="author" content="">

  <title>MIRI</title>

  <!-- Custom fonts for this template-->
  <link href="/miri/static/vendor/fontawesome-free/css/all.min.css" rel="stylesheet" type="text/css">
  <link href="https://fonts.googleapis.com/css?family=Nunito:200,200i,300,300i,400,400i,600,600i,700,700i,800,800i,900,900i" rel="stylesheet">

  <!-- Custom styles for this template-->
  <link href="/miri/static/css/sb-admin-2.min.css" rel="stylesheet">


<script type="text/javascript">
	
</script>

</head>

<body class="bg-gradient-primary">

  <div class="container">

    <!-- Outer Row -->
    <div class="row justify-content-center">

      <div class="col-xl-10 col-lg-12 col-md-9">

        <div class="card o-hidden border-0 shadow-lg my-5">
          <div class="card-body p-0">
            <!-- Nested Row within Card Body -->
            <div class="row">
              <div class="col-lg-6 d-none d-lg-block bg-login-image"></div>
              <div class="col-lg-6">
                <div class="p-5">
                  <div class="text-center">
                    <h1 class="h4 text-gray-900 mb-4">MIRI에 오신 것을 환영합니다</h1>
                  </div>
                  <form class="user" action="/miri/index.do" method="post">
                    <div class="form-group">
                      <input type="text" class="form-control form-control-user" name="member_id" id="inputAdminId" aria-describedby="emailHelp" placeholder="Enter Id...">
                    </div>
                    <div class="form-group">
                      <input type="password" class="form-control form-control-user" name="member_pass" id="inputAdminPassword" placeholder="Password">
                    </div>
                    <button type="submit" class="btn btn-primary btn-user btn-block" style="">Login</button>
	                <div class="text-center">
	                    <a class="small" href="register.do">Create an Account!</a>
	                </div>
                    <!-- <a href="/miri/index.do" class="btn btn-primary btn-user btn-block">
                      Login
                    </a> -->
                    <hr>
                    <!-- <a href="index.html" class="btn btn-google btn-user btn-block">
                      <i class="fab fa-google fa-fw"></i> Login with Google
                    </a>
                    <a href="index.html" class="btn btn-facebook btn-user btn-block">
                      <i class="fab fa-facebook-f fa-fw"></i> Login with Facebook
                    </a> -->
                  </form>
                  <hr>
                  <div style="background-image: 'url(car_insurance)'">
                  
                  </div>
                  
                  <div class="text-center">
					<img src="/miri/static/img/car_insurance.jpg">
				  </div>
                  
                  <div class="text-center">
                	<a class="large">보험사에선 안 알려주는 자동차 보험 가입팁!</a>
              	  </div>
              	  <div class="text-center">
                	<a class="small">차를 소유한 사람이라면 반드시 가입해야 하는 자동차 보험. 여러분은 어떻게 가입하고 계신가요? 혹시 보험사에서 처음 보여주는 담보와 가입금액 그대로 가입하시진 않으셨나요?</a>
              	  </div>
              	  
                  <!-- <div class="text-center">
                    <a class="small" href="forgot-password.html">Forgot Password?</a>
                  </div>
                  <div class="text-center">
                    <a class="small" href="register.html">Create an Account!</a>
                  </div> -->
                </div>
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
  <script src="js/sb-admin-2.min.js?v=<%=System.currentTimeMillis() %>"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.6/umd/popper.min.js" integrity="sha384-wHAiFfRlMFy6i5SRaxvfOCifBUQy1xHdJ/yoi7FRNXMRBu5WHdZYu1hA6ZOblgut" crossorigin="anonymous"></script>
  <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js" integrity="sha384-JjSmVgyd0p3pXB1rRibZUAYoIIy6OrQ6VrjIEaFf/nJGzIxFDsf4x0xIM+B07jRM" crossorigin="anonymous"></script>

</body>

</html>
