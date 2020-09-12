/**
 * 适配于role.html
 */
		var saveRoleUrl = "", rolePageIndex = 1;
		
		function getAllPermissions() {
			$.ajax({
				type: "POST",
				url:  "/permissionInfo/getPermissionsForSelect2",
				success: function(result) {
					$('.select2bs4').select2({
						theme: 'bootstrap4',
						data: result
					});
				},
				dataType: "json"
			});
		}
		
		function refreshSelect2() {
			$('.select2bs4').select2({
				theme: 'bootstrap4',
				language: 'zh-CN',
				placeholder: '请选择权限',
				allowClear: true
			});
		}
		
		function newRole() {
			rolePageIndex = 1;			
			saveRoleUrl = "/roleInfo/saveRole";
			$("#dlgRoleName").attr("readonly", false);
			$("#modal-role input").val("");
			$(".select2bs4").val(null).trigger('change');
			$("#dlgRoleTitle").html("新增角色");
			$("#modal-role").modal("show"); 
		}
		
		function editRole(role) {
			rolePageIndex = $('#pagination-role').pagination('getSelectedPageNum');
			saveRoleUrl = "/roleInfo/saveRole?id=" + role.roleId;
			$("#dlgRoleName").val(role.roleName);
			$("#dlgRoleName").attr("readonly", true);
			$("#dlgRoleAlias").val(role.description);
			let permissionlist = new Array();
			$(".select2bs4").val(null).trigger('change');
			$.each(role.permissions, function(index, item) {
				permissionlist.push(item.permissionCode);
			});
			$(".select2bs4").val(permissionlist);
			$(".select2bs4").trigger('change');
			$("#dlgRoleTitle").html("编辑角色");
			$("#modal-role").modal("show"); 			
		}
		
		function deleteRole(roleId) {
			rolePageIndex = $('#pagination-role').pagination('getSelectedPageNum');
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
						$.post("/roleInfo/deleteRole", {roleId: roleId}, function(data){
							if(data.status == 1) {
								// 重新加载数据, 但是保持原来的分页参数
								$('#pagination-role').pagination('go', rolePageIndex);
							} else {							  
								Swal.fire('失败！', data.message, 'error');
							}
						});
					}
				});
		}
				
		function doPagination(roleAlias) {
			var permissions = sessionStorage.getItem("permissions");
			$('#pagination-role').pagination({
				  dataSource: '/roleInfo/getRoles?roleAlias=' + roleAlias,
			      locator: 'content',
			      totalNumberLocator: function(response) {
			          // you can return totalNumber by analyzing response content
			          return response.totalElements;
			      },
			      pageSize: 6,
			      className: 'custom-paginationjs paginationjs-big',
			      callback: function(response, pagination) {	
			        var dataHtml = '<div class="row row-cols-1 row-cols-md-3">';

			        var innerHtml = "";
			        $.each(response, function (index, item) {
			          innerHtml += '<div class="col mb-4">';			          
			          innerHtml += '  <div class="card mb-3 h-100">';
			          innerHtml += '	<div class="row no-gutters">';
			          innerHtml += '	  <div class="col-md-4">';
			          innerHtml += '	       <img src="/img/role.png" class="card-img" alt="' + item.description + '">';
			          
			          innerHtml += '          <div class="card-body">';
			          innerHtml += '            <div class="d-flex justify-content-center">';
			          if(permissions.indexOf("role:edit") != -1) {
			          	innerHtml += "            <button type='button' class='btn btn-info btn-sm ml-1' onclick='editRole(" + JSON.stringify(item) + ")'><i class='fas fa-pencil-alt'></i></button>";
			          }
			          if(permissions.indexOf("role:del") != -1) {
			          	innerHtml += "            <button type='button' class='btn btn-danger btn-sm ml-1' onclick='deleteRole(" + item.roleId + ")'><i class='fas fa-trash'></i></button>";
			          }
			          innerHtml += '            </div>';
			          innerHtml += '          </div><!-- /.card-footer -->';
			          
			          innerHtml += '      </div>';
			          innerHtml += '      <div class="col-md-8">';	
			          innerHtml += '        <div class="card-body">';
			          innerHtml += '	         <h4>' + item.roleName + '</h4>';
			          innerHtml += '          <p class="card-text">';
			          innerHtml += '            <span>角色：<small class="text-muted" id="lblname' + item.roleId + '">' + item.description + '</small></span><br>';
			          let permissionlist = new Array();
					  $.each(item.permissions, function(i, permission) {
						  permissionlist.push(permission.permissionTitle);
					  });
			          innerHtml += '            <span>权限：<small class="text-muted" id="lblrole' + item.roleId + '">' + permissionlist.join("，") + '</small></span>';
			          innerHtml += '          </p>';
			          
			          innerHtml += '        </div>';
			          innerHtml += '      </div>';
			          innerHtml += '    </div>';			          
			          innerHtml += '  </div>';
			          innerHtml += '</div>';
			        });
					
			        if(innerHtml == "") {
			        	innerHtml = '<p class="ml-4">未找到角色</p>';
			        }
			        
			        dataHtml += innerHtml;			        
			        dataHtml += '</div>';

			        $("#roleCardBody").html(dataHtml);
			      }
			});
			
		}
		
		$(document).ready(function() {
			var container = $("#roleCardBody");	
			// 初始加载所有角色
			doPagination("");			
			// 查询按钮事件
			$("#btnFindRoles").click(function() {
				doPagination($("#txtRoleAlias").val());
			});
			// 获取所有权限列表
			getAllPermissions();
			// 对话框加载完毕事件
			$('#modal-role').on('shown.bs.modal', function (e) {
				// Refresh Select2 Elements
				refreshSelect2();
			});	
			// 保存按钮事件
			$("#btnSaveRole").click(function() {				
				var roleName  = $("#dlgRoleName").val();
 				var roleAlias = $("#dlgRoleAlias").val();
 				var rolePermissions = $("#dlgRolePermission").select2("data");
 				if(roleName == "" || roleAlias == "") {
 					myAlert("数据输入不完整！")
 					return;
 				}
 				var permissionIds = new Array();
 				$.each(rolePermissions, function (index, item) {
 					permissionIds.push(item.id);
 				}); 				
				$.ajax({
					type: "POST",
					url:  saveRoleUrl,
					data: {roleName: roleName, description: roleAlias, rolePermission: permissionIds.join(",")},
					error: function() {
						myAlert("出错啦！权限不足？");
					},
					success: function(result) {
						if(result.status == 1) {
							$("#modal-role").modal("hide");
							$('#pagination-role').pagination('go', rolePageIndex);
						} else {
							myAlert(result.message);
						}
					},
					dataType: "json"
				});
			}); 
			
		});