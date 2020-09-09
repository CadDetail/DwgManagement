/**
 * 适配于login.html
 */
	//自定义的弹出对话框
	function myAlert(info) {
		Swal.fire({
			toast: true,
			position: 'top',
			padding: '1em',
			html: '<span style="color:#ffffff"><i class="icon fas fa-exclamation-triangle"></i>&nbsp;&nbsp;' + info + '</span>',
			showConfirmButton: false,
			background: '#dc3545',
			timer: 3000
		});
	}

	$(document).ready(function() {		
		
		sessionStorage.clear();
		
		// 提交登录
		$("#dologin").click(function() {
			var username = $("#username").val();
			var password = $("#password").val();
			var rememberMe = $('#rememberMe').is(':checked');
			if(username == "" || password == "") {
				myAlert('账号和密码均不能为空！');
				return;
			}
			$.post("/login", {
				"username" : username,
				"password" : password,
				"rememberMe" : rememberMe
			}, function(result) {
				if (result.status == 1) {
					location.href = "/index";
				} else {
					myAlert(result.message);
				}
			});
		});
	});