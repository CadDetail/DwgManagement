/**
 * 适配于index.html
 */
	$(document).ready(function() {
			
			// 取消活动菜单的反亮显示
			$(".sidebar .nav-treeview .nav-link").removeClass("active");
			
			  // The Calender
			  $('#calendar').datetimepicker({
			    format: 'L',
			    locale: 'zh-cn',
			    inline: true,
			    calendarWeeks: true
			  })
						
			$.ajax({
				type: "GET",
				url:  "/getCountInfo",
				success: function(result) {	
					$("#lblCntUser").html(result.user);
					$("#lblCntRole").html(result.role);
					$("#lblCntPermission").html(result.permission);
					$("#lblCntArtifact").html(result.artifact);
				},
			    dataType: "json"
			});	
   
		});