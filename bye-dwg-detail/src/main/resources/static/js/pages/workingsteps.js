/**
 * 适配于workingsteps.html
 */
        $(document).ready(function() {
        	// 加载工序列表
        	loadWorkingsteps();
            // 保存按钮单击事件
            $("#btnSaveWorkingsteps").click(function() {
                var stepName = $("#dlgWorkingstepName").val();
                if(stepName == "") {
                    myAlert("工序信息输入不完整！")
                    return;
                }
                $.ajax({
                    type: "POST",
                    url:  "/workingsteps/saveWorkingsteps",
                    data: $("#frmWorkingsteps").serialize(),
                    error: function() {
                        myAlert("出错啦，☹");
                    },
                    success: function(result) {
                        if(result.status == 1) {
                            $("#modal-workingsteps").modal("hide");
                            loadWorkingsteps();
                        } else {
                            myAlert(result.message);
                        }
                    },
                    dataType: "json"
                });
            });


        });       
        
        function loadWorkingsteps() {
        	var permissions = sessionStorage.getItem("permissions");
        	$.ajax({
                type: "GET",
                url:  "/workingsteps/findWorkingsteps",
                success: function(result) {
                	let html = '';
                	let colorlist = ['bg-primary', 'bg-secondary', 'bg-info', 'bg-success', 'bg-warning', 'bg-danger'];
                	$.each(result, function (i, item) {
                		html += '<div class="col-sm-4 col-lg-2">';
                		html += '  <!-- small card -->';
                		html += '  <div class="small-box ' + colorlist[i % 6] + '">';
                		html += '    <div class="inner">';
                		html += '      <h4>' + (i+1) + '</h4>';
                		html += '      <p>' + item.stepName + '</p>';
                		html += '    </div>';
                		html += '    <div class="icon">';
                		html += '      <i class="ion ion-hammer"></i>';
                		html += '    </div>';
                		if(permissions.indexOf("workingsteps:del") != -1) {
	                		html += '    <a href="#" class="small-box-footer" onclick="deleteWorkingsteps(' + item.stepId + ')">';
	                		html += '      删除 ';
	                		html += '    </a>';
                		} else {
                			html += '    <a href="#" class="small-box-footer">';
                			html += '      工序 ';
	                		html += '    </a>';
                		}
                		html += '  </div>';
                		html += '</div>';
                	}); 
                	$('#rowWorkingsteps').html(html);
                },
                dataType: "json"
            });
        }

        // 添加工序
        function newWorkingsteps() {
            $("#modal-workingsteps input").val("");
            $("#dlgWorkingstepsTitleWnd").html("新增工序");
            $("#modal-workingsteps").modal("show");
        }

        // 删除工序
        function deleteWorkingsteps(stepId) {
            Swal.fire({
                title: '确定吗？',
                text: "一旦删除，数据将不可恢复！",
                icon: 'warning',
                showCancelButton: true,
                confirmButtonColor: '#3085d6',
                cancelButtonColor: '#dd3333',
                confirmButtonText: '确定',
                cancelButtonText: '取消'
            }).then((result) => {
                if (result.value) {
                    $.post("/workingsteps/deleteWorkingsteps", {stepId: stepId}, function(data){
                        if(data.status == 1) {
                        	loadWorkingsteps();
                        } else {
                            Swal.fire('失败！', data.message, 'error');
                        }
                    });
                }
            });
        }