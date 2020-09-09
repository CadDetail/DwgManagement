/**
 * 适配于user.html
 */
		var saveUserUrl = "", userPageIndex = 1;
		
		function getAllRoles() {
			$.ajax({
				type: "POST",
				url:  "/roleInfo/getRolesForSelect2",
				success: function(result) {
					$('.select2bs4').select2({
						theme: 'bootstrap4',
						language: 'zh-CN',						
						data: result,
						placeholder: '请选择角色',
						allowClear: true
					});
				},
				dataType: "json"
			});
		}
		
		function newUser() {
			userPageIndex = 1;			
			saveUserUrl = "/userInfo/saveUser";
			$("#dlgUserName").attr("readonly", false);
			$("#modal-user input").val("");
			$(".select2bs4").val(null).trigger('change');
			$("#divPassword").show();
			$("#dlgUserTitle").html("新增用户");
			$("#modal-user").modal("show"); 
		}
		
		function editUser(user) {
			userPageIndex = $('#pagination-user').pagination('getSelectedPageNum');
			saveUserUrl = "/userInfo/saveUser?id=" + user.userId;
			$("#dlgUserName").val(user.userName);
			$("#dlgUserName").attr("readonly", true);
			$("#dlgUserAlias").val(user.userAlias);
			$("#divPassword").hide();
			var roles = new Array();
			$(".select2bs4").val(null).trigger('change');
			$.each(user.roles, function(index, item) {
				roles.push(item.roleName);
			});
			$(".select2bs4").val(roles);
			$(".select2bs4").trigger('change');
			$("#dlgUserTitle").html("编辑用户");
			$("#modal-user").modal("show"); 			
		}
		
		function deleteUser(userId) {
			userPageIndex = $('#pagination-user').pagination('getSelectedPageNum');
			Swal.fire({
				  title: '确定吗？',
				  text: "一旦删除，数据将不可恢复！",
				  icon: 'warning',
				  showCancelButton: true,
				  confirmButtonColor: '#3085d6',
				  cancelButtonColor: '#d33',
				  confirmButtonText: '确定',
				  cancelButtonText: '取消'
				}).then((result) => {
					if (result.value) {
						$.post("/userInfo/deleteUser", {userId: userId}, function(data){
							if(data.status == 1) {
								// 重新加载数据, 但是保持原来的分页参数
								// doPagination($("#txtUserAlias").val());
								$('#pagination-user').pagination('go', userPageIndex);
							} else {							  
								Swal.fire('失败！', data.message, 'error');
							}
						});
					}
				});
		}
		
		function resetPassword(userId) {
			userPageIndex = $('#pagination-user').pagination('getSelectedPageNum');
			Swal.fire({
				  title: '修改密码',
				  input: 'password',
				  inputPlaceholder: '请输入密码',
				  inputAttributes: {
				    autocapitalize: 'off'
				  },
				  showCancelButton: true,
				  confirmButtonText: '修改',
				  cancelButtonText: '取消',
				  confirmButtonColor: '#17a2b8',
				  showLoaderOnConfirm: true,
				  preConfirm: (password) => {
					var data = {'id': userId, 'password': password};
				    return fetch('/userInfo/resetPassword', {				    	
				    	method: 'POST',
				    	headers: {'Accept': 'application/json', 'Content-Type': 'application/json'},
				    	body: JSON.stringify(data)
				      })
				      .then(response => {				    	
						if (!response.ok) {
							throw new Error(response.statusText)
						}				    	  
				    	return response.json();
				      })
				      .then(result => {
						Swal.fire(result.message);
				      })				      
				  },
				  allowOutsideClick: () => !Swal.isLoading()
				})
		}
	
		function doPagination(userAlias) {
			var permissions = sessionStorage.getItem("permissions");
			$('#pagination-user').pagination({
				  dataSource: '/userInfo/getUsers?userAlias=' + userAlias,
			      locator: 'content',
			      totalNumberLocator: function(response) {
			          // you can return totalNumber by analyzing response content
			          return response.totalElements;
			      },
			      pageSize: 9,
			      className: 'custom-paginationjs paginationjs-big',
			      callback: function(response, pagination) {	
			        var dataHtml = '<div class="row row-cols-1 row-cols-sm-2 row-cols-md-3">';

			        var innerHtml = "";
			        $.each(response, function (index, item) {
			          innerHtml += '<div class="col mb-4">';			          
			          innerHtml += '  <div class="card mb-3 h-100">';
			          innerHtml += '	<div class="row no-gutters">';
			          innerHtml += '	  <div class="col-md-4">';
			          innerHtml += '	       <img src="/img/user.png" class="card-img" alt="' + item.userAlias + '">';
			          innerHtml += '      </div>';
			          innerHtml += '      <div class="col-md-8">';
			          innerHtml += '        <div class="card-body">';
			          innerHtml += '	         <h4>' + item.userName + '</h4>';
			          innerHtml += '          <p class="card-text">';
			          innerHtml += '            <span>姓名：<small class="text-muted" id="lblname' + item.userId + '">' + item.userAlias + '</small></span><br>';
			          var rolelist = new Array();
					  $.each(item.roles, function(i, role) {
						rolelist.push(role.description);
					  });
			          innerHtml += '            <span>角色：<small class="text-muted" id="lblrole' + item.userId + '">' + rolelist.join("，") + '</small></span>';
			          innerHtml += '          </p>';			          
			          innerHtml += '          <div class="d-flex justify-content-end">';
			          if(permissions.indexOf("user:edit") != -1 || permissions.indexOf("user:add") != -1) {
			          	innerHtml += "            <button type='button' class='btn btn-success btn-sm ml-1' onclick='resetPassword(" + item.userId + ")'><i class='fas fa-key mr-1'></i>密码</button>";
			          }
			          if(permissions.indexOf("user:edit") != -1) {
			          	innerHtml += "            <button type='button' class='btn btn-info btn-sm ml-1' onclick='editUser(" + JSON.stringify(item) + ")'><i class='fas fa-pencil-alt mr-1'></i>修改</button>";
			          }
			          if(permissions.indexOf("user:del") != -1) {
			          	innerHtml += "            <button type='button' class='btn btn-danger btn-sm ml-1' onclick='deleteUser(" + item.userId + ")'><i class='fas fa-trash mr-1'></i>删除</button>";
			          }
			          innerHtml += '          </div>';			          
			          innerHtml += '        </div>';
			          innerHtml += '      </div>';
			          innerHtml += '    </div>';
			          innerHtml += '  </div>';
			          innerHtml += '</div>';
			        });
					
			        if(innerHtml == "") {
			        	innerHtml = '<p class="ml-4">未找到用户</p>';
			        }
			        
			        dataHtml += innerHtml;			        
			        dataHtml += '</div>';

			        $("#userCardBody").html(dataHtml);
			      }
			});
			
		}
		
		$(document).ready(function() {
			var container = $("#userCardBody");	
			// 初始加载所有用户
			doPagination("");			
			// 查询按钮事件
			$("#btnFindUsers").click(function() {
				doPagination($("#txtUserAlias").val());
			});
			// Initialize Select2 Elements
			getAllRoles();
			// 保存按钮事件
			$("#btnSaveUser").click(function() {				
				var userName  = $("#dlgUserName").val();
 				var userAlias = $("#dlgUserAlias").val();
 				var password  = $("#dlgUserPassword").val();
 				var userRoles = $("#dlgUserRole").select2("data");
 				if(userName == "" || userAlias == "") {
 					myAlert("数据输入不完整！")
 					return;
 				}
 				if(saveUserUrl.indexOf("id=") == -1) {
 					if(password == "") {
 	 					myAlert("数据输入不完整！")
 	 					return;
 	 				}
 				}
 				var roleIds = new Array();
 				var roleNames = new Array();
 				$.each(userRoles, function (index, item) {
 					roleIds.push(item.id);
 					roleNames.push(item.text);
 				}); 				
				$.ajax({
					type: "POST",
					url:  saveUserUrl,
					data: {userName: userName, userAlias: userAlias, password: password, userRole: roleIds.join(",")},
					error: function() {
						myAlert("出错啦！权限不足？");
					},
					success: function(result) {
						if(result.status == 1) {
							$("#modal-user").modal("hide");
							//doPagination($("#txtUserAlias").val());
							$('#pagination-user').pagination('go', userPageIndex);
						} else {
							myAlert(result.message);
						}
					},
					dataType: "json"
				});
			});
			
		});