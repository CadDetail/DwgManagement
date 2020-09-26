/**
 * 适配于importdetail.html
 */
		$(document).ready(function() {
			
			var fileAtSrvr = "";
			
			// 初始化 custom-file-input
			bsCustomFileInput.init();
			
			// 通过浏览按钮选择了不同的上传文件
			$("#inputFile").change(function() {
				if(fileAtSrvr != "") {
					$.post("/artifact/deleteUploadMdbFile", {fileAtSrvr: fileAtSrvr});
				}
			});
			
			// 上传按钮单击事件
			$("#btnUploadFile").click(function() {
				let readyFile = $("#lblUploadFile").html();
				let fileSuffix = readyFile.substring(readyFile.lastIndexOf(".") + 1).toLowerCase();
				if(fileSuffix == "") {
					Swal.fire("还未选择要上传的 MDB 文件！");
					return;
				}
				else if(fileSuffix != "mdb") {
					Swal.fire("选择的文件不是 MDB 类型！");
					return;
				}
				// 之前已经上传过文件了
				if(fileAtSrvr != "") {
					$.post("/artifact/deleteUploadMdbFile", {fileAtSrvr: fileAtSrvr});
				}
				// 开始上传文件
				$("#btnUploadFile").attr("disabled", true);	// 设置上传按钮不可用
				$.ajax({
			        url: "/upload",
			        type: 'POST',
			        cache: false,
			        data: new FormData($('#frmUploadFile')[0]),
			        processData: false,
			        contentType: false,
			        dataType: "json",
			        success: function(data) {
			            if(data.status == 1) {
			            	fileAtSrvr = data.message;
			            	Swal.fire('成功', "明细数据文件上传完毕，请单击提交按钮！", 'success');
			            } else {
			                Swal.fire('错误', data.message, 'error');
			            }
			            $("#btnUploadFile").removeAttr("disabled"); // 上传按钮恢复可用
			        }
			    });
			});
		    
			// 提交按钮单击事件
			$("#btnSubmit").click(function() {
				let conflictMode = $("input[type='radio']:checked").val();
				if(fileAtSrvr == "") {
					Swal.fire("还未上传明细数据文件！");
					return;
				}
				// 开始提交，进入后台进行数据导入操作
				$("#btnSubmit").attr("disabled", true);	// 设置提交按钮不可用
				Swal.fire({
					title: '导入明细',
					text: '正在导入数据，请稍候……',
					showCloseButton: false,
					showCancelButton: false,
					showconfirmButton: false,
					onBeforeOpen	: () => {
						Swal.showLoading();
						$.ajax({
							type: "POST",
							url:  "/artifact/importArtifact",
							data: {conflictMode: conflictMode, fileAtSrvr: fileAtSrvr},					
							success: function(result) {
								Swal.close();
								if(result.status == 1) {									
									Swal.fire('成功', result.message, 'success');
								} else {
									Swal.fire('错误', result.message, 'error');
								}
								// 提交完成后, 进行重置操作
								fileAtSrvr = "";
								$('#frmUploadFile')[0].reset();
								$("#btnSubmit").removeAttr("disabled"); // 提交按钮恢复可用
							},
							dataType: "json"
						});
					}
				});
						    
				
			});
			
		});