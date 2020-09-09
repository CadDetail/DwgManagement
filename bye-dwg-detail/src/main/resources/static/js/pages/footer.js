/**
 * 适配于footer.html
 */
			$(document).ready(function() {				
				// 配置登录用户信息
				var loggedUserStr = sessionStorage.getItem("loggedUser");
				var loggedUser = new Object;
				if(loggedUserStr != null) {
					loggedUser = JSON.parse(loggedUserStr);					
			    } else {
			    	$.ajax({
						type: "GET",
						url: "/userInfo/getLoggedUser",
						success: function(result) {
							sessionStorage.setItem("loggedUser", JSON.stringify(result));
							loggedUser = result;
							$.ajax({
								type: "GET",
								url: "/userInfo/queryPermissions",
								success: function(data) {
									sessionStorage.setItem("permissions", JSON.stringify(data));
							    },
							    dataType: "json"
							});
					    },
					    async: false,
					    dataType: "json"
					});
			    }
				
				$("#username").html(loggedUser.userName);
				$("#useralias").html(loggedUser.userAlias);
				var rolelist = new Array();
				var roleids = new Array();
				$.each(loggedUser.roles, function(i, role) {
					rolelist.push(role.description);
					roleids.push(role.roleId);
				});
				$("#userrole").html(rolelist.join("，"));
				if(roleids.length > 0) {
					if(roleids[0]>=1 && roleids[0]<=7) {
						$("#imgUserPhoto").attr("src", "/img/user" + roleids[0] + "-128x128.jpg");
						$("#imgUserProfile").attr("src", "/img/user" + roleids[0] + "-128x128.jpg");
					} else {
						$("#imgUserPhoto").attr("src", "/img/user8-128x128.jpg");
						$("#imgUserProfile").attr("src", "/img/user8-128x128.jpg");
					}
				} else {
					$("#imgUserPhoto").attr("src", "/img/user8-128x128.jpg");
					$("#imgUserProfile").attr("src", "/img/user8-128x128.jpg");
				}
				
				// 激活对应的导航菜单
				$(".sidebar .nav-link").removeClass("active");
				var activeMenu = sessionStorage.getItem("activemenu");
				if(activeMenu != null) {
					$("#" + activeMenu).addClass("active");
				}
				
				// 初始化bootstrap-switch
				$("input[data-bootstrap-switch]").each(function(){
				      $(this).bootstrapSwitch('state', $(this).prop('checked'));
				    });
			});