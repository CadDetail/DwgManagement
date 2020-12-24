/**
 * 适配于materialfeature.html
 */
        var saveUrl = "";
        $(document).ready(function() {
            // 对数据表格进行初始化
            var datatable = $('#tableMaterialfeature').DataTable({
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
                    url:  "/materialfeature/findMaterialFeatures",
                    type: "POST",
                    data: function(param) {
                        param.materialName = $("#sysMaterialName").val();

                    }
                },
                columns: [
                    { data: 'formulaId', visible: false},
                    { data: null, orderable: false},
                    { data: 'materialCode' },
                    { data: 'materialName' },
                    { data: 'feature' },
                    { data: 'formulaFactor' },                    
                    { data: null, orderable: false, "render": function(row, meta) {
                            var htmlOpt = "<div class='d-flex justify-content-end'>";
                            htmlOpt += "     <div class='btn-group btn-group-sm'>";
                            var permissions = sessionStorage.getItem("permissions");
                            if(permissions.indexOf("system:all") != -1 || permissions.indexOf("materialfeature:edit") != -1) {
                                htmlOpt += "<button type='button' class='btn btn-info' onclick='editMaterialfeature(" + JSON.stringify(row) + ")'><i class='fas fa-pencil-alt mr-1'></i></button>";
                            }
                            if(permissions.indexOf("system:all") != -1 || permissions.indexOf("materialfeature:del") != -1) {
                                htmlOpt += "<button type='button' class='btn btn-danger' onclick='deleteMaterialfeature(" + row.formulaId + ")'><i class='fas fa-trash mr-1'></i></button>";
                            }
                            htmlOpt += "  </div>";
                            htmlOpt += "</div>";
                            return htmlOpt;
                        }}
                ]
            });

            // 为DataTable添加序号列
            datatable.on('draw.dt', function() {
                //给第一列编号
                datatable.column(1).nodes().each(function(cell, i) {
                    cell.innerHTML = "<b>" + (i+1) + "</b>";
                });
            });


            // 查询按钮事件
            $("#btnFindMaterial").click(function() {
                 var param = {
                	materialName: $("#sysMaterialName").val()
                 }
                // 重新加载查询参数并执行查询
                datatable.settings()[0].ajax.data = param;
                //datatable.ajax.reload();
                datatable.draw();
            });

            // 保存按钮单击事件
            $("#btnSaveMaterialfeature").click(function() {
                var materialCode = $("#dlgMaterialCode").val();
                var materialName = $("#dlgMaterialName").val();
                var feature = $("#dlgMaterialFeature").val();
                var formulaFactor = $("#dlgFormulaFactor").val();
                if(materialName == "" || feature == "" || formulaFactor == "") {
                    myAlert("材料信息输入不完整！")
                    return;
                }
                $.ajax({
                    type: "POST",
                    url:  saveUrl,
                    data: $("#frmMaterialfeature").serialize(),
                    error: function() {
                        myAlert("出错啦，☹");
                    },
                    success: function(result) {
                        if(result.status == 1) {
                            $("#modal-materialfeature").modal("hide");
                            var title = $("#dlgMaterialfeatureTitleWnd").html();
                            if(title == "新增材料") {
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

        // 添加一个新材料及特性
        function newMaterialfeature() {
            saveUrl = "/materialfeature/saveMaterialFeature";
            $("#modal-materialfeature input").val("");
            $("#dlgMaterialfeatureTitleWnd").html("新增材料");
            $("#modal-materialfeature").modal("show");
        }

        // 编辑（修改）材料及特性
        function editMaterialfeature(quotaformula) {
             saveUrl = "/materialfeature/saveMaterialFeature?id=" + quotaformula.formulaId;
            $("#dlgMaterialCode").val(quotaformula.materialCode);
            $("#dlgMaterialName").val(quotaformula.materialName);
            $("#dlgMaterialFeature").val(quotaformula.feature);
            $("#dlgFormulaFactor").val(quotaformula.formulaFactor);
            $("#dlgMaterialfeatureTitleWnd").html("编辑材料");
            $("#modal-materialfeature").modal("show");
        }

        // 删除材料
        function deleteMaterialfeature(formulaId) {
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
                    $.post("/materialfeature/deleteMaterialFeature", {formulaId: formulaId}, function(data){
                        if(data.status == 1) {
                            // 重新加载数据, 但是保持原来的分页参数
                            var datatable = $('#tableMaterialfeature').DataTable();
                            datatable.ajax.reload(null, false);
                        } else {
                            Swal.fire('失败！', data.message, 'error');
                        }
                    });
                }
            });
        }