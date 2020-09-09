/**
 * 适配于detail.html
 */
		var saveUrl = "", activeTab = -1, inspectDetailId = -1;
	
		// 创建info-box
		function createInfoBox(data) {
			var html = '';
			html += '<div class="row" style="margin-top:10px;">';
			html += '  <div class="col-md-3 col-sm-6 col-12">';
			html += '    <div class="info-box h-100">';
			html += '      <span class="info-box-icon bg-info"><i class="far fa-bookmark"></i></span>';
			html += '      <div class="info-box-content text-truncate">';
			html += '        <span class="info-box-text">图号</span>';
			html += '        <span class="info-box-number">' + data.artifact.artifactCode + '</span>';
			html += '      </div>';
			html += '    </div>';
			html += '  </div>';
			html += '  <div class="col-md-3 col-sm-6 col-12">';
			html += '    <div class="info-box h-100">';
			html += '      <span class="info-box-icon bg-success"><i class="ion ion-ios-gear-outline"></i></span>';
			html += '      <div class="info-box-content text-truncate">';
			html += '        <span class="info-box-text">名称</span>';
			html += '        <span class="info-box-number">' + data.artifact.artifactName + '</span>';
			html += '      </div>';
			html += '    </div>';
			html += '  </div>';
			html += '  <div class="col-md-3 col-sm-6 col-12">';
			html += '    <div class="info-box h-100">';
			html += '      <span class="info-box-icon bg-warning"><i class="far fa-hourglass"></i></span>';
			html += '      <div class="info-box-content">';
			html += '        <span class="info-box-text">重量</span>';
			var weight = data.artifact.weight;
			if(weight == null) { weight = "未知"; }
			html += '        <span class="info-box-number">' + weight + '</span>';
			html += '      </div>';
			html += '    </div>';
			html += '  </div>';
			html += '  <div class="col-md-3 col-sm-6 col-12">';
			html += '    <div class="info-box h-100">';
			html += '      <span class="info-box-icon bg-danger"><i class="far fa-star"></i></span>';
			html += '      <div class="info-box-content">';
			html += '        <span class="info-box-text">倍乘</span>';
			html += '        <span class="info-box-number">' + data.times + '</span>';
			html += '      </div>';
			html += '    </div>';
			html += '  </div>';
			html += '</div>';
			return html;
		}
	
		// 创建一个表格
		function createTable(tableId, data) {
			var permissions = sessionStorage.getItem("permissions");
			var html = '<div class="table-responsive">';
			html += '<table class="table table-hover table-striped projects" id="' + tableId + '">';
			html += '<thead>';
			html += '    <tr>';
			html += '      <th scope="col">#</th>';
			html += '      <th scope="col">名称</th>';
			html += '      <th scope="col">图号</th>';
			html += '      <th scope="col">重量</th>';
			html += '      <th scope="col">数量</th>';
			html += '      <th scope="col">材料</th>';
			html += '      <th scope="col">需分解</th>';
			html += '      <th scope="col"></th>';
			html += '    </tr>';
			html += '</thead>';
			html += '<tbody>';
			$.each(data, function(i, item) {
				html += '<tr id="tr' + item.detailId + '">';
				html += '<th scope="row">' + (i+1) + '</th>';
				if(item.needSplit) {					
					html += '<td><a href="#" onclick="showMatchingTab(\'tab'+ item.slave.artifactId + '\')">' + item.slave.artifactName + '</a></td>';
				} else {
					html += '<td>' + item.slave.artifactName + '</td>';
				}
				html += '<td>' + item.slave.artifactCode + '</td>';
				var weight = item.slave.weight;
				if(weight == null) { weight = ""; };
				html += '<td>' + weight + '</td>';
				html += '<td>' + item.number + '</td>';
				var materialName = item.slave.materialName;
				if(materialName == null) { materialName = ""; }
				html += '<td>' + materialName + '</td>';
				if(item.needSplit) {
					html += '<td><span class="badge bg-secondary">' + item.needSplit + '</span></td>';
				} else {
					html += '<td>' + item.needSplit + '</td>';
				}
				html += '<td class="project-actions text-right">';				
            	if(permissions.indexOf("detail:edit") != -1) {
					html += "<button type='button' class='btn btn-info btn-sm ml-2' onclick='editDetail(" + JSON.stringify(item) + ")'><i class='fas fa-pencil-alt mr-1'></i>编辑</button>";
            	}
            	if(permissions.indexOf("detail:del") != -1) {
					html += '<button type="button" class="btn btn-danger btn-sm ml-2" onclick="deleteDetail(' + item.detailId + ')"><i class="fas fa-trash mr-1"></i>删除</button>';
            	}
            	if(permissions.indexOf("detail:check") != -1) {
					html += "<button type='button' class='btn btn-success btn-sm ml-2' onclick='doInspect(" + JSON.stringify(item) + ")'><i class='fas fa-paw mr-1'></i>会签</button>";
            	}
            	html += '</td>';
				html += '</tr>';
			});
			html += '</tbody>';
			html += '</table>';
			html += '</div>';
			return html;
		}
		
		function showMatchingTab(tabId) {
			$("#" + tabId).tab('show');
		}
		
		function newDetail() {
			saveUrl = "/detail/saveArtifactDetail";
			$("#modal-artifactDetail input").val("");
			$("#dlgDetailTitle").html("新增明细");
			$("#modal-artifactDetail").modal("show");
		}
		
		function editDetail(detail) {
			saveUrl = "/detail/saveArtifactDetail?id=" + detail.detailId;
			$("#dlgArtifactName").val(detail.slave.artifactName);
			$("#dlgArtifactCode").val(detail.slave.artifactCode);
			$("#dlgNumber").val(detail.number);
			$("#dlgNeedSplit").prop("checked", detail.needSplit);
			$("#dlgDetailMemo").val(detail.detailMemo);
			$("#dlgDetailTitle").html("修改明细");
			$("#modal-artifactDetail").modal("show");
		}
		
		function deleteDetail(detailId) {
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
					  $.post("/detail/deleteDetail", {detailId: detailId}, function(data){
						  if(data.status == 1) {
							  // 在表格中删除对应的数据行
							  $("table #tr" + detailId).remove();
						  } else {							  
							  Swal.fire('失败！', data.message, 'error');
						  }
					  });
				  }
				});
		}
		
		function doInspect(detail) {
			inspectDetailId = detail.detailId;
			$("#dlgInspectArtifactCode").val(detail.slave.artifactCode);
			$("#dlgInspectArtifactName").val(detail.slave.artifactName);
			$("#dlgDimension").val(detail.dimension);
			$("#dlgUnit").val(detail.unit);
			$("#dlgQuota").val(detail.quota);
			$("#dlgWorkingSteps").val(detail.workingSteps);
			$("#dlgClassificationSign").val(detail.classificationSign);
			$("#dlgProcessSign").val(detail.processSign);
			var inspector = detail.inspector;
			if(inspector == null || inspector == "") {
				inspector = $("#useralias").html();
			}
			$("#dlgInspector").val(inspector);
			$("#modal-inspect").modal("show");
		}
		
		function loadData(masterId) {
			$.ajax({
				type: "GET",
				url:  "/artifact/findRecursiveDetail",
				data: {masterId: masterId},
				success: function(result) {						
					var navitem = '<ul class="nav nav-tabs" id="tabDetail" role="tablist">';
					var content = '<div class="tab-content" id="tabContent">';
					$.each(result, function(i, item) {					
						navitem += '<li class="nav-item">';
						if(i == 0) {
							navitem += '  <a class="nav-link active" id="tab' + item.artifact.artifactId + '" data-toggle="pill" href="#content' + item.artifact.artifactId + '" role="tab" aria-controls="content' + item.artifact.artifactId + '" aria-selected="true">' + item.artifact.artifactName + '</a>';
						} else {
							navitem += '  <a class="nav-link" id="tab' + item.artifact.artifactId + '" data-toggle="pill" href="#content' + item.artifact.artifactId + '" role="tab" aria-controls="content' + item.artifact.artifactId + '" aria-selected="false">' + item.artifact.artifactName + '</a>';
						}							
						navitem += '</li>';
						if(i == 0) {
							content += '<div class="tab-pane fade show active" id="content' + item.artifact.artifactId + '" role="tabpanel" aria-labelledby="tab' + item.artifact.artifactId + '">';
						} else {
							content += '<div class="tab-pane fade" id="content' + item.artifact.artifactId + '" role="tabpanel" aria-labelledby="tab' + item.artifact.artifactId + '">';
						}
						content += createInfoBox(item);
						content += createTable('table' + item.artifact.artifactId, item.detail);
						content += '</div>';							
					});
					navitem += '</ul>';
					content += '</div>';
					var html = '';
					html += navitem;
					// 公共区域, 可用于显示公共信息, 备用
					// html += '<div class="tab-custom-content">';
					// html += '  <p class="lead mb-0">Custom Content goes here</p>';
					// html += '</div>';
					html += content;
					$("#detailCardBody").html(html);
					if(activeTab != -1) {
						$('#tab' + activeTab).tab('show');
					}
			    },
			    dataType: "json"
			});
		}
	
		$(document).ready(function() {
			var masterId = sessionStorage.getItem("artifactId");
			if(masterId != null) {
				// 数据表加载数据
				loadData(masterId);				
				// 为导出按钮添加事件
				$('#btnExportDetail').click(function() {
					var url = "/export/exportDetail?masterId=" + masterId;
					Swal.fire({
						  title: '明细导出到Excel',
						  text: "导出操作可能会消耗较长时间，确定导出？",
						  icon: 'question',
						  showCancelButton: true,
						  confirmButtonColor: '#3085d6',
						  confirmButtonText: '确定',
						  cancelButtonText: '取消'
						}).then((result) => {
						  if (result.value) {
							  window.location.href = url;
						  }
						})		            
				});
			}
			// 添加明细时，由用户先输入图号，然后触发查找此零件
			$("#dlgArtifactCode").change(function() {
				$("#dlgArtifactName").val("");
				var code = $(this).val();
				$.post("/artifact/findArtifactByCode", {artifactCode: code}, function(data) {
					if(data.artifactName) {
						$("#dlgArtifactName").val(data.artifactName);
					} else {
						myAlert("图号输入有误！");
					}
				}, "json");
			});
			// 明细保存按钮单击事件, 进行添加或修改明细
 			$("#btnSaveDetail").click(function() {
 				var parentId = $('a[class="nav-link active"][data-toggle="pill"]').attr("id").substring(3);
 				activeTab = parentId;
 				var artifactName = $("#dlgArtifactName").val();
 				var artifactCode = $("#dlgArtifactCode").val();
 				var number =$("#dlgNumber").val();
 				var needSplit = $("#dlgNeedSplit").is(":checked") ? true : false;
 				var detailMemo = $("#detailMemo").val();
 				if(artifactName == "" || artifactCode == "" || number == "") {
 					myAlert("明细数据输入不完整！")
 					return;
 				}
				$.ajax({
					type: "POST",
					url:  saveUrl,
					data: {parentId: parentId, artifactCode: artifactCode, number: number, needSplit: needSplit, detailMemo: detailMemo},
					error: function() {
						myAlert("出错啦！权限不足？");
					},
					success: function(result) {	
						if(result.status == 1) {
							$("#modal-artifactDetail").modal("hide");
							loadData(masterId);
						} else {
							myAlert(result.message);
						}
					},
					dataType: "json"
				});
			}); 
 			// 会签保存按钮单击事件
 			$("#btnSaveInspect").click(function() {
 				var parentId = $('a[class="nav-link active"][data-toggle="pill"]').attr("id").substring(3);
 				activeTab = parentId;
 				var dimension = $("#dlgDimension").val();
 				var unit = $("#dlgUnit").val();
 				var quota =$("#dlgQuota").val();
 				if(dimension == "" || unit == "" || quota == "") {
 					myAlert("会签数据输入不完整！")
 					return;
 				}
				$.ajax({
					type: "POST",
					url:  "/detail/saveArtifactInspect?id=" + inspectDetailId,
					data: $("#frmInspect").serialize(),
					error: function() {
						myAlert("出错啦！权限不足？");
					},
					success: function(result) {	
						if(result.status == 1) {
							$("#modal-inspect").modal("hide");
							loadData(masterId);
						} else {
							myAlert(result.message);
						}
					},
					dataType: "json"
				});
			}); 
		    
		});