/**
 * 适配于permission.html
 */
        var saveUrl = "";
        $(document).ready(function() {
            // 对数据表格进行初始化
            var datatable = $('#tablePermissions').DataTable({
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
                    url:  "/permissionInfo/findSysPermissions",
                    type: "POST",
                    data: function(param) {
                        param.permissionTitle = $("#sysPermissionTitle").val();

                    }
                },
                columns: [
                    { data: 'permissionId', visible: false},
                    { data: null, orderable: false},
                    { data: 'permissionTitle' },
                    { data: 'permissionCode' },
                    { data: 'permissionAvailable'},
                    { data: null, orderable: false, "render": function(row, meta) {
                            var htmlOpt = "<div class='d-flex justify-content-end'>";
                            var permissions = sessionStorage.getItem("permissions");

                            if(permissions.indexOf("permission:edit") != -1) {
                                htmlOpt += "<button type='button' class='btn btn-info btn-sm ml-2' onclick='editPermission("   + JSON.stringify(row) + ")'><i class='fas fa-pencil-alt mr-1'></i>编辑</button>";
                            }
                            if(permissions.indexOf("permission:del") != -1) {
                                htmlOpt += "<button type='button' class='btn btn-danger btn-sm ml-2' onclick='deletePermission(" + row.permissionId + ")'><i class='fas fa-trash mr-1'></i>删除</button>";
                            }
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


            // 查询按钮、复选框事件
            $("#btnFindPermissions").click(function() {
                 var param = {
                     permissionTitle: $("#sysPermissionTitle").val()
                 }
                // 重新加载查询参数并执行查询
                datatable.settings()[0].ajax.data = param;
                //datatable.ajax.reload();
                datatable.draw();
            });

            // 保存按钮单击事件
            $("#btnSavePermission").click(function() {
                var permissionTitle = $("#dlgPermissionTitle").val();
                var permissionCode = $("#dlgPermissionCode").val();
                var permissionAvailable =$("#dlgPermissionAvailable").val();
                console.log(permissionAvailable);
                if(permissionTitle == "" || permissionCode == "" || permissionAvailable == "") {
                    myAlert("权限信息输入不完整！")
                    return;
                }
                $.ajax({
                    type: "POST",
                    url:  saveUrl,
                    data: $("#frmPermission").serialize(),
                    error: function() {
                        myAlert("出错啦！权限不足？");
                    },
                    success: function(result) {
                        if(result.status == 1) {
                            $("#modal-permission").modal("hide");
                            var title = $("#dlgPermissionTitleWnd").html();
                            if(title == "新增权限") {
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

        // 添加一个新权限
        function newPermission() {
            saveUrl = "/permissionInfo/saveSysPermission";
            $("#modal-permission input").val("");
            $("#dlgPermissionTitleWnd").html("新增权限");
            $("#modal-permission").modal("show");
        }

        // 编辑（修改）权限
        function editPermission(permission) {
             saveUrl = "/permissionInfo/saveSysPermission?id=" + permission.permissionId;
            $("#dlgPermissionTitle").val(permission.permissionTitle);
            $("#dlgPermissionCode").val(permission.permissionCode);
            $("#dlgPermissionAvailable").prop("checked", permission.permissionAvailable);
            $("#dlgPermissionTitleWnd").html("编辑权限");
            $("#modal-permission").modal("show");
        }

        // 删除权限
        function deletePermission(permissionId) {
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
                    $.post("/permissionInfo/deleteSysPermission", {permissionId: permissionId}, function(data){
                        if(data.status == 1) {
                            // 重新加载数据, 但是保持原来的分页参数
                            var datatable = $('#tablePermissions').DataTable();
                            datatable.ajax.reload(null, false);
                        } else {
                            Swal.fire('失败！', data.message, 'error');
                        }
                    });
                }
            });
        }