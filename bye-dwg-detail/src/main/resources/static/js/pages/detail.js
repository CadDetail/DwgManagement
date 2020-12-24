/**
 * 适配于detail.html
 */
		var saveUrl = "", activeTab = -1, inspectDetailId = -1;
		var quotaformulas = new Object();
		
		// 加载工序
		function loadWorkingstes() {
		    $.ajax({
		        type: 'GET',
		        url: '/workingsteps/findWorkingsteps',
		        dataType: 'json',
		        success: function (result) {
		            if (result != null) {
		                var sHtml = '<option value="">请选择</option>'; ;
		                $.each(result, function (i, val) {
		                	sHtml += "<option value ='" + val.stepName + "'>" + val.stepName + "</option>";
		                });
		                $('#dlgWorkingSteps').html(sHtml);
		            }
		        }
		    });
		}
		
		// 加载材料材质和公式
		function loadQuotaformula() {
		    $.ajax({
		        type: 'GET',
		        url: '/materialfeature/findQuotaformula/',
		        dataType: 'json',
		        success: function (result) {
		        	quotaformulas = result;		        	
		        }
		    });
		}
	
		// 创建info-box
		function createInfoBox(data) {
			var html = '';
			html += '<div class="row" style="margin-top:10px;">';
			html += '  <div class="col-md-3 col-sm-6 col-12">';
			html += '    <div class="info-box h-100">';
			html += '      <span class="info-box-icon bg-info"><i class="icon ion-flag"></i></span>';
			html += '      <div class="info-box-content text-truncate">';
			html += '        <span class="info-box-text">图号</span>';
			html += '        <span class="info-box-number">' + data.artifact.artifactCode + '</span>';
			html += '      </div>';
			html += '    </div>';
			html += '  </div>';
			html += '  <div class="col-md-3 col-sm-6 col-12">';
			html += '    <div class="info-box h-100">';
			html += '      <span class="info-box-icon bg-success"><i class="icon ion-gear-a"></i></span>';
			html += '      <div class="info-box-content text-truncate">';
			html += '        <span class="info-box-text">名称</span>';
			html += '        <span class="info-box-number">' + data.artifact.artifactName + '</span>';
			html += '      </div>';
			html += '    </div>';
			html += '  </div>';
			html += '  <div class="col-md-3 col-sm-6 col-12">';
			html += '    <div class="info-box h-100">';
			html += '      <span class="info-box-icon bg-warning"><i class="icon ion-speedometer"></i></span>';
			html += '      <div class="info-box-content">';
			html += '        <span class="info-box-text">重量</span>';
			html += '        <span class="info-box-number">' + data.weight + '</span>';
			html += '      </div>';
			html += '    </div>';
			html += '  </div>';
			html += '  <div class="col-md-3 col-sm-6 col-12">';
			html += '    <div class="info-box h-100">';
			html += '      <span class="info-box-icon bg-danger"><i class="icon ion-star"></i></span>';
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
		function createTable(tableId, data, artifacts) {
			var permissions = sessionStorage.getItem("permissions");
			var html = '<div class="table-responsive">';
			html += '<table class="table table-hover table-striped projects" id="' + tableId + '">';
			html += '<thead>';
			html += '    <tr>';
			html += '      <th scope="col">#</th>';
			html += '      <th scope="col">图号</th>';
			html += '      <th scope="col">名称</th>';
			html += '      <th scope="col">材料代码</th>';
			html += '      <th scope="col">材料名称</th>';
			html += '      <th scope="col">重量</th>';
			html += '      <th scope="col">数量</th>';
			html += '      <th scope="col">备注</th>';
			html += '      <th scope="col">需要分拆</th>';
			html += '      <th scope="col"></th>';
			html += '    </tr>';
			html += '</thead>';
			html += '<tbody>';
			$.each(data, function(i, item) {
				/*if(item.needSplit) {
					$.each(artifacts, function(index, element) {
						if(item.slave.artifactId == element.artifact.artifactId) {
							if(element.detail.length > 0) {
								html += '<tr class="table-success" id="tr' + item.detailId + '">';
							} else {
								html += '<tr class="table-danger" id="tr' + item.detailId + '">';
							}
							return false;
						}
					})
				} else {
					html += '<tr id="tr' + item.detailId + '">';
				}*/
				html += '<tr id="tr' + item.detailId + '">';
				html += '<th scope="row">' + (i+1) + '</th>';
				html += '<td>' + item.slave.artifactCode + '</td>';
				if(item.needSplit) {					
					html += '<td><a href="#" onclick="showMatchingTab(\'tab'+ item.slave.artifactId + '\')">' + item.slave.artifactName + '</a></td>';
				} else {
					html += '<td>' + item.slave.artifactName + '</td>';
				}
				var materialCode = item.slave.materialCode;
				if(materialCode == null) { materialCode = ""; }
				html += '<td>' + materialCode + '</td>';
				var materialName = item.slave.materialName;
				if(materialName == null) { materialName = ""; }
				html += '<td>' + materialName + '</td>';
				var weight = item.slave.weight;
				if(weight == null) { weight = ""; };
				html += '<td>' + weight + '</td>';
				if(item.number <= 0) {
					html += '<td><span class="badge bg-warning">' + item.number + '</span></td>';
				} else {
					html += '<td>' + item.number + '</td>';
				}
				var memo = item.detailMemo;
				html += '<td>' + (memo == null ? "" : memo) + '</td>';
				if(item.needSplit) {
					$.each(artifacts, function(index, element) {
						if(item.slave.artifactId == element.artifact.artifactId) {
							if(element.detail.length > 0) {
								html += '<td><span class="badge bg-success">' + (item.needSplit ? "是" : "否") + '</span></td>';
							} else {
								html += '<td><span class="badge bg-danger">' + (item.needSplit ? "是" : "否") + '</span></td>';
							}
							return false;
						}
					})
				} else {
					html += '<td>' + (item.needSplit ? "是" : "否") + '</td>';
				}
				html += '<td class="project-actions text-right">';
				html += '  <div class="btn-group btn-group-sm">';
            	if(permissions.indexOf("system:all") != -1 || permissions.indexOf("detail:edit") != -1) {
					html += "<button type='button' class='btn btn-info' onclick='editDetail(" + JSON.stringify(item) + ")'><i class='fas fa-pencil-alt mr-1'></i></button>";
            	}
            	if(permissions.indexOf("system:all") != -1 || permissions.indexOf("detail:del") != -1) {
					html += '<button type="button" class="btn btn-danger" onclick="deleteDetail(' + item.detailId + ')"><i class="fas fa-trash mr-1"></i></button>';
            	}
            	if(permissions.indexOf("system:all") != -1 || permissions.indexOf("detail:check") != -1) {
            		var inspector = item.inspector;
            		if(inspector != null && inspector != "") {
            			html += "<button type='button' class='btn btn-secondary' onclick='doInspect(" + JSON.stringify(item) + ")'><i class='fas fa-paw mr-1'></i></button>";
            		} else {
            			html += "<button type='button' class='btn btn-success' onclick='doInspect(" + JSON.stringify(item) + ")'><i class='fas fa-paw mr-1'></i></button>";
            		}					
            	}
            	html += '  </tr>';
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
			//$("#dlgArtifactCode").removeAttr("readonly");
			//$("#dlgArtifactCode").addClass("is-warning");
			$("#modal-artifactDetail input").val("");
			$("#dlgDetailTitle").html("新增明细");
			$("#modal-artifactDetail").modal("show");
		}
		
		function editDetail(detail) {
			saveUrl = "/detail/saveArtifactDetail?id=" + detail.detailId;
			//$("#dlgArtifactCode").attr("readonly", true);
			//$("#dlgArtifactCode").removeClass("is-warning");
			$("#dlgArtifactName").val(detail.slave.artifactName);
			$("#dlgArtifactCode").val(detail.slave.artifactCode);
			$("#dlgMaterialCode").val(detail.slave.materialCode);
			$("#dlgMaterialName").val(detail.slave.materialName);
			$("#dlgArtifactWeight").val(detail.slave.weight);
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
			var unit = detail.unit;
			if(detail.unit == null) {
				unit = "kg";
			}
			$("#dlgUnit").val(unit);
			var feature = null, factor = 1.0;			
			if(quotaformulas != null) {
				$.each(quotaformulas, function(i, item) {					
					if(item.materialName == detail.slave.materialName) {						
						feature = item.feature;
						factor = item.formulaFactor;
						return false;
					}
				});
			}
			$("#dlgFeature").val(feature);
			$("#dlgFactor").val(factor);
			$("#dlgQuota").val(detail.quota);
			$("#dlgWorkingSteps").val(detail.workingSteps);			
			$("#modal-inspect").modal("show");
		}
		
		function calculateQuota() {
			var dimension = $("#dlgDimension").val();
			var feature = $("#dlgFeature").val();
			var factor = $("#dlgFactor").val();
			dimension = dimension.toLowerCase().replace(/cm/g, "");
			var operands = dimension.split("*");
			if(operands.length != 2 && operands.length != 3) {
				myAlert("尺寸输入格式：长*宽*高（直径*长）");
				return;
			}
			if(feature == null || feature == "") { feature = 0; }
			var quota = 0;
			if(operands.length == 2) {		// 圆柱体，结果以公斤为单位
				quota = (3.14 * (operands[0]/2) * (operands[0]/2) * operands[1]) * feature / factor / 1000;				
			}			
			else if(operands.length == 3) {	// 长方体，结果以公斤为单位
				quota = (operands[0] * operands[1] * operands[2]) * feature / factor / 1000;
			}			
			$("#dlgQuota").val(quota.toFixed(2));
			$("#dlgUnit").val("kg");
		}
		
		function loadData(masterId) {
			$.ajax({
				type: "GET",
				// url:  "/artifact/findRecursiveDetail",
				url:  "/artifact/findSubDetails",
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
						content += createTable('table' + item.artifact.artifactId, item.detail, result);
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
				// 加载可供选择的工序 
				loadWorkingstes();
				// 加载材料材质及公式
				loadQuotaformula();
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
			//$("#dlgArtifactCode").change(function() {
			//	$("#dlgArtifactName").val("");
			//	var code = $(this).val();
			//	if(code.trim().length == 0) { return; }
			//	$.post("/artifact/findArtifactByCode", {artifactCode: code}, function(data) {
			//		if(data.artifactName) {
			//			$("#dlgArtifactName").val(data.artifactName);
			//		} else {
			//			myAlert("图号输入有误！");
			//		}
			//	}, "json");
			//});
			// 明细保存按钮单击事件, 进行添加或修改明细
 			$("#btnSaveDetail").click(function() {
 				var parentId = $('a[class="nav-link active"][data-toggle="pill"]').attr("id").substring(3);
 				activeTab = parentId;
 				var artifactCode = $("#dlgArtifactCode").val();
 				var artifactName = $("#dlgArtifactName").val(); 				
 				var materialCode = $("#dlgMaterialCode").val();
 				var materialName = $("#dlgMaterialName").val();
 				var weight = $("#dlgArtifactWeight").val();
 				var number =$("#dlgNumber").val();
 				var needSplit = $("#dlgNeedSplit").is(":checked") ? true : false;
 				var detailMemo = $("#dlgDetailMemo").val();
 				if(artifactCode =="" || artifactName == "" || number == "") {
 					myAlert("明细数据输入不完整！")
 					return;
 				}
				$.ajax({
					type: "POST",
					url:  saveUrl,
					data: { parentId: parentId, artifactCode: artifactCode, artifactName:artifactName,
							materialCode: materialCode, materialName: materialName, weight: weight, 
							number: number, needSplit: needSplit, detailMemo: detailMemo },
					error: function() {
						myAlert("出错啦，☹");
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
 				if(dimension == "" || quota == "") {
 					myAlert("会签数据输入不完整！")
 					return;
 				}
				$.ajax({
					type: "POST",
					url:  "/detail/saveArtifactInspect?id=" + inspectDetailId,
					data: $("#frmInspect").serialize() + "&" + $.param({inspector:$("#username").html()}),
					error: function() {
						myAlert("出错啦，☹");
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