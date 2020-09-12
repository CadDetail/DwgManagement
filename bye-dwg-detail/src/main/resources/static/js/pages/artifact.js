/**
 * 适配于artifact.html
 */
		var saveUrl = "";
		
		$(document).ready(function() {				
			// 对数据表格进行初始化
			var datatable = $('#tableArtifacts').DataTable({
				responsive: true,
				processing: true,
				serverSide: true,
				autoWidth : false,
				searching : false,
				ordering  : true,
				pagingType: "full",
				language: {
			        url: '/plugins/datatables/datatables-bs4/language-zh.json'
			    },
				ajax: {		        	
					url:  "/artifact/findArtifacts",
		        	type: "POST",
		        	data: function(param) {
		        		param.artifactName = $("#txtArtifactName").val();
		        		param.productFlag = $("#chkOnlyProduct").is(":checked") ? 0 : -1;
		        		param.canBeSplit = $("#chkCanSplit").is(":checked") ? 0 : -1;
		        	}
		        },
				columns: [
					{ data: 'artifactId', visible: false},
					{ data: null, orderable: false},
					{ data: 'artifactCode' },
		            { data: 'artifactName' },
		            { data: 'productModel' },
		            { data: 'materialCode', orderable: false},
		            { data: 'materialName', orderable: false},
		            { data: 'weight' },
		            { data: 'artifactMemo', orderable: false},
		            { data: null, orderable: false, "render": function(data, type, row, meta) {
		            	var htmlOpt = "<div class='d-flex justify-content-end'>";
		            	var permissions = sessionStorage.getItem("permissions");
		            	if(permissions.indexOf("detail:view") != -1) {
			            	if(row.canBeSplit) {
			            		htmlOpt += "<button type='button' class='btn btn-success btn-sm ml-2' onclick=\"viewDetail('" + row.artifactId + "')\"><i class='fas fa-eye mr-1'></i>明细</button>";
			            	}
		            	}
		            	if(permissions.indexOf("artifact:edit") != -1) {
		            		htmlOpt += "<button type='button' class='btn btn-info btn-sm ml-2' onclick='editArtifact("   + JSON.stringify(row) + ")'><i class='fas fa-pencil-alt mr-1'></i>编辑</button>";
		            	}
		            	if(permissions.indexOf("artifact:del") != -1) {
		            		htmlOpt += "<button type='button' class='btn btn-danger btn-sm ml-2' onclick='deleteArtifact(" + row.artifactId + ")'><i class='fas fa-trash mr-1'></i>删除</button>";
		            	}
		            	htmlOpt += "</div>";
		            	return htmlOpt;
		            }}
		        ]
		    });
			
			// 定义导出到Excel和打印按钮
			new $.fn.dataTable.Buttons(datatable, {
			    name: 'commands',
			    buttons: [
			    	{
			            extend: 'excel',
			            text: '导出到Excel',
			            className: 'btn btn-success',
			            exportOptions: {
			            	columns: [ 0, 2, 3, 4, 5, 6 ] 
			            }
			        },
			        {
			            extend: 'print',
			            text: '打印',
			            exportOptions: {
			            	columns: [ 0, 2, 3, 4, 5, 6 ] 
			            }
			        }
		        ]
			} );
			
			// 将导出到Excel和打印按钮放在指定位置
			datatable.buttons( 0, null ).containers().appendTo( '#divButtons' );
			
			// 为DataTable添加序号列
			datatable.on('draw.dt', function() {
			      //给第一列编号
			      datatable.column(1).nodes().each(function(cell, i) {
			          cell.innerHTML = "<b>" + (i+1) + "</b>";
			      });
			  });
			
			// 查询按钮、复选框事件
			$("#btnFindArtifacts, #chkOnlyProduct, #chkCanSplit").click(function() {
				var flag = $("#chkOnlyProduct").is(":checked") ? 0 : -1;
				var flag2 = $("#chkCanSplit").is(":checked") ? 0 : -1;
				var param = {
					artifactName: $("#txtArtifactName").val(),
					productFlag: flag,
					canBeSplit: flag2
				}
				// 重新加载查询参数并执行查询
				datatable.settings()[0].ajax.data = param;
				//datatable.ajax.reload();
				datatable.draw();
			});	
			
			// 保存按钮单击事件
 			$("#btnSaveArtifact").click(function() {
 				var artifactName = $("#dlgArtifactName").val();
 				var artifactCode = $("#dlgArtifactCode").val();
 				var weight =$("#dlgArtifactWeight").val();
 				if(artifactName == "" || artifactCode == "" || weight == "") {
 					myAlert("零件信息输入不完整！")
 					return;
 				}
				$.ajax({
					type: "POST",
					url:  saveUrl,
					data: $("#frmArtifact").serialize(),
					error: function() {
						myAlert("出错啦！权限不足？");
					},
					success: function(result) {
						if(result.status == 1) {
							$("#modal-artifact").modal("hide");
							var title = $("#dlgArtifactTitle").html();
							if(title == "新增工件") {
								// 添加时，跳转到最后一页
								datatable.page("last").draw("page");
							} else {
								// 重新加载数据, 但是保持原来的分页参数
								datatable.ajax.reload(null, false);
							}
						} else {
							myAlert(result.message);
						}
					},
					dataType: "json"
				});
			}); 
			
			
		});
		
		// 明细按钮单击事件
		viewDetail = function(artifactId) {
			sessionStorage.setItem("artifactId", artifactId);
			window.location = "/detail";
		}
		
		// 添加一个新产品（零件）
		function newArtifact() {
			saveUrl = "/artifact/saveArtifact";
			$("#modal-artifact input").val("");
			$("#dlgArtifactTitle").html("新增工件");
			$("#modal-artifact").modal("show");
		}
		
		// 编辑（修改）产品（零件）
		function editArtifact(artifact) {
			saveUrl = "/artifact/saveArtifact?id=" + artifact.artifactId;
			$("#dlgArtifactName").val(artifact.artifactName);
			$("#dlgArtifactCode").val(artifact.artifactCode);
			$("#dlgArtifactWeight").val(artifact.weight);
			$("#dlgMaterialCode").val(artifact.materialCode);
			$("#dlgMaterialName").val(artifact.materialName);			
			$("#dlgArtifactFlag")[0].selectedIndex = artifact.productFlag;
			$("#dlgProductModel").val(artifact.productModel);
			$("#dlgCanSplit").prop("checked", artifact.canBeSplit);
			$("#dlgArtifactMemo").val(artifact.artifactMemo);
			$("#dlgArtifactTitle").html("修改工件");
			$("#modal-artifact").modal("show");
		}
		
		// 删除产品（零件）
		function deleteArtifact(artifactId) {
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
					  $.post("/artifact/deleteArtifact", {artifactId: artifactId}, function(data){
						  if(data.status == 1) {
							// 重新加载数据, 但是保持原来的分页参数
							var datatable = $('#tableArtifacts').DataTable();
							datatable.ajax.reload(null, false);
						  } else {							  
							  Swal.fire('失败！', data.message, 'error');
						  }
					  });
				  }
				});
		}