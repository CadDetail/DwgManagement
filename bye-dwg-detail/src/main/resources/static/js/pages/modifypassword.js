/**
 * 适配于navigation.html中的修改密码对话框
 */
	        	// 自定义的弹出对话框
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
	        	
	        	// 提取输入信息，提交到后台完成密码修改功能
	        	function saveNewPassword() {
	        		var oldpassword = $("#oldpassword").val();
	        		var newpassword1 = $("#newpassword1").val();
	        		var newpassword2 = $("#newpassword2").val();
	        		if (oldpassword == "" || newpassword1 == "" || newpassword2 == "") {
	        			myAlert("所有输入项均不能为空！")
	        			return;
	        		}
	        		if (newpassword1 != newpassword2) {
	        			myAlert("两次输入的新密码不一致！")
	        			return;
	        		}
	        		$.ajax({
						type: "POST",
						url: "/userInfo/modifyPassword",
						data: {"oldpassword" : oldpassword, "newpassword" : newpassword1},
						success: function(result) {
							if(result.status == 1) {
								Swal.fire({title: '修改成功，请重新登录！', position: 'top'}).then((result) => {
									  if (result.value) {
										window.location = "/logout";
									  }
								});
							} else {
								myAlert(result.message);
							}
					    },
					    dataType: "json"
					});
	        	}