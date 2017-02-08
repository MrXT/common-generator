<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml" xmlns:th="http://www.thymeleaf.org">
<head th:include="head">
</head>
<body class="no-skin">
	<div class="main-container" id="main-container">
		<div class="main-content">
			<div class="main-content-inner">
				<div class="page-content">
					<div class="row">
						<div class="col-xs-12">
							<!-- 检索  -->
							<form id="searchForm">
								<!-- 默认查询有效的数据 -->
								<input type="hidden" name="valid" value="true"/>
								<table style="margin-top: 5px;">
									<tr>
										<td>
											<div class="nav-search">
												<span class="input-icon"> <input type="text" placeholder="请输入关键字" class="nav-search-input" style="width: 120px;" autocomplete="off"
													name="keyword" /> <i class="ace-icon fa fa-search nav-search-icon"></i>
												</span>
											</div>
										</td>
										<!-- 操作时间查询 -->
										<td style="padding-left: 2px;">
											<div class="input-group input-medium date-picker input-daterange" >
												<input name="startDate" id="startDate" class="form-control" type="text" value="" readonly="readonly" style="width: 88px; height: 28px;"
													placeholder="开始日期" title="开始日期" /> <span class="input-group-addon">-</span><input name="endDate" id="endDate" class="form-control"
													readonly="readonly" placeholder="结束日期" title="结束日期" style="width: 88px; height: 28px;" type="text" value="" />
											</div>
										</td>
										<!-- 如果要查询失效的数据，去掉注释 -->
										<!--
										<td style="vertical-align: top; padding-left: 2px;"><select class="chosen-select form-control" name="valid" data-placeholder="请选择"
											style="vertical-align: top; width: 90px;">
												<option value=""></option>
												<option value="">全部</option>
												<option value="true" selected="selected">有效</option>
												<option value="false">无效</option>
										</select></td>
										-->
										<td style="vertical-align: top; padding-left: 2px"><a class="btn btn-light btn-xs" onclick="searchData();" title="检索"><i
												id="nav-search-icon" class="ace-icon fa fa-search bigger-110 nav-search-icon blue"></i></a></td>
										<td style="vertical-align: top; padding-left: 2px"><a class="btn btn-light btn-xs" onclick="clearForm('searchForm');" title="重置"><i
												class="ace-icon fa fa-circle-o-notch bigger-110 nav-search-icon blue"></i></a></td>
										<!-- <td style="vertical-align: top; padding-left: 2px;"><a class="btn btn-light btn-xs" onclick="toExcel();" title="导出到EXCEL"><i
												id="nav-search-icon" class="ace-icon fa fa-download bigger-110 nav-search-icon blue"></i></a></td> -->
									</tr>
								</table>
							</form>
							<!-- 检索  -->
							<table id="simple-table" class="table table-striped table-bordered table-hover" style="margin-top: 5px;">
							</table>
							<!-- 操作 按钮模版（为了扩展操作按钮，所以提到界面上来，可以很好的自定义）  -->
							<div style="display: none;"  id="optButtonId">
								<a class="btn btn-xs btn-success" title="编辑" th:if="${r'${update}'}" handle="savePage"> <i class="ace-icon fa fa-pencil-square-o bigger-120" title="编辑"></i></a><a
									class="btn btn-xs btn-danger" th:if="${r'${invalid}'}" handle="invalid"> <i class="ace-icon fa fa-trash-o bigger-120" title="删除"></i></a> <a
									class="btn btn-xs btn-info" th:if="${r'${revalid}'}" handle="revalid"> <i class="ace-icon fa fa-recycle bigger-120" title="恢复"></i></a>
							</div>
							<div class="page-header position-relative">
								<table style="width: 100%;">
									<tr>
										<td style="vertical-align: top;"><a class="btn btn-mini btn-success" th:if="${r'${insert}'}" onclick="savePage();">新增</a> <a
											class="btn btn-mini btn-danger" id="batchInvalid" onclick="makeAll(0);" th:if="${r'${invalid}'}" title="批量删除"><i
												class='ace-icon fa fa-trash-o bigger-120'></i></a> 
										<!-- 如果要恢复删除的数据打开下面的注释 -->			
										<!-- <a class="btn btn-mini btn-info" id="batchRevalid" onclick="makeAll(1);" th:if="${r'${revalid}'}"
											title="批量恢复"><i class='ace-icon fa fa-recycle bigger-120'></i></a>
										-->
										</td>
										<!-- 分页 -->
										<td style="vertical-align: top; float: right; position: relative;" id="page" th:include="page"></td>
									</tr>
								</table>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<div class="modal fade" id="myModal" data-backdrop="static" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
		<form id="saveForm" class="form-horizontal">
			<div class="modal-dialog">
				<div class="modal-content">
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
						<h4 class="modal-title" id="myModalLabel">编辑</h4>
					</div>
					<div class="modal-body">
						<input type="hidden" name="${simpleEntityName}Id" id="${simpleEntityName}Id" /> 
						<#list filedCommets as filedCommet>
							<div class="form-group">
								<label for="${filedCommet.propertyName}" class="col-sm-2 control-label">${filedCommet.columnName}</label>
								<div class="col-sm-10">
									<!-- required="required" check-msg="${filedCommet.columnName}不能为空!"  验证空 -->
									<input type="text" class="form-control" required="required" check-msg="${filedCommet.columnName}不能为空!" name="${filedCommet.propertyName}" id="${filedCommet.propertyName}" placeholder="请输入${filedCommet.columnName}" />
								</div>
							</div>
						</#list>
					</div>
					<div class="modal-footer">
						<button type="button" class="btn btn-default btn-sm" onclick="clearForm('saveForm');">重置</button>
						<button id="saveButton" type="button" class="btn btn-primary btn-sm">保存</button>
					</div>
				</div>
			</div>
		</form>
	</div>
	<script th:inline="javascript">
		/*<![CDATA[*/
		${jsons}
		var idProperity = "${simpleEntityName}Id";
		var datagrid = getDefalutDatagrid(jsons, idProperity,true);
		$(function() {
			
		});
		function searchData() {
			searchDefault({}, function(result) {
				datagrid.loadData(result.list);
			});
		}
		/*
		 *  新增操作前验证修改请求参数
		 */
		function beforeAddUpdateParam(data) {
			return check(data);
		}
		/*
		 *  修改操作前验证修改请求参数
		 */
		function beforeEditUpdateParam(data) {
			return check(data);
		}
		function check(data) {
			return true;
		}
		/*]]>*/
	</script>
</body>
</html>

