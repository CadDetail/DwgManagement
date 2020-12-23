/**
 * 适配于needsplitprefix.html
 */
        $(document).ready(function() {
        	// 加载组件标签列表
        	loadNeedsplitprefix();
            // 保存按钮单击事件
            $("#btnSaveNeedsplitprefix").click(function() {
                var prefixLabel = $("#dlgPrefixLabel").val();
                var prefixProduct = $("#dlgPrefixProduct").val();
                if(prefixLabel == "" || prefixProduct == "") {
                    myAlert("信息输入不完整！")
                    return;
                }
                $.ajax({
                    type: "POST",
                    url:  "/needsplitprefix/saveNeedsplitprefix",
                    data: $("#frmNeedsplitprefix").serialize(),
                    error: function() {
                        myAlert("出错啦，☹");
                    },
                    success: function(result) {
                        if(result.status == 1) {
                            $("#modal-needsplitprefix").modal("hide");
                            loadNeedsplitprefix();
                        } else {
                            myAlert(result.message);
                        }
                    },
                    dataType: "json"
                });
            });


        });       
        
        function loadNeedsplitprefix() {
        	var permissions = sessionStorage.getItem("permissions");
        	$.ajax({
                type: "GET",
                url:  "/needsplitprefix/findNeedsplitprefix",
                success: function(result) {
                	let html = '';
                	let colorlist = ['bg-primary', 'bg-secondary', 'bg-info', 'bg-success', 'bg-warning', 'bg-danger'];
                	$.each(result, function (i, item) {
                		html += '<div class="col-sm-4 col-lg-2">';
                		html += '  <!-- small card -->';
                		if(item.prefixProduct) {
                			html += '  <div class="small-box bg-success">';
                		} else {
                			html += '  <div class="small-box bg-info">';
                		}
                		// html += '  <div class="small-box ' + colorlist[i % 6] + '">';
                		html += '    <div class="inner">';
                		html += '      <h4>' + (i+1) + '</h4>';
                		html += '      <p>' + item.prefixLabel + '</p>';
                		html += '    </div>';
                		html += '    <div class="icon">';
                		if(item.prefixProduct) {
                			html += '      <i class="ion ion-toggle-filled"></i>';
                		} else {
                			html += '      <i class="ion ion-toggle"></i>';
                		}
                		html += '    </div>';
                		if(permissions.indexOf("workingsteps:del") != -1) {
	                		html += '    <a href="#" class="small-box-footer" onclick="deleteNeedsplitprefix(' + item.prefixId + ')">';
	                		html += '      删除 ';
	                		html += '    </a>';
                		} else {
                			html += '    <a href="#" class="small-box-footer">';
                			html += '      前缀 ';
	                		html += '    </a>';
                		}
                		html += '  </div>';
                		html += '</div>';
                	}); 
                	$('#rowNeedsplitprefix').html(html);
                },
                dataType: "json"
            });
        }

        // 添加组件标签
        function newNeedsplitprefix() {
            $("#modal-needsplitprefix input").val("");
            $("#dlgNeedsplitprefixTitleWnd").html("新增组件标签");
            $("#modal-needsplitprefix").modal("show");
        }

        // 删除工序
        function deleteNeedsplitprefix(prefixId) {
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
                    $.post("/needsplitprefix/deleteNeedsplitprefix", {prefixId: prefixId}, function(data){
                        if(data.status == 1) {
                        	loadNeedsplitprefix();
                        } else {
                            Swal.fire('失败！', data.message, 'error');
                        }
                    });
                }
            });
        }