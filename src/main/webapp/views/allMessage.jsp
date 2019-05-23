<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Insert title here</title>
    <link rel="stylesheet" type="text/css"
          href="${pageContext.request.contextPath}/jquery-easyui-1.3.3/themes/default/easyui.css">
    <link rel="stylesheet" type="text/css"
          href="${pageContext.request.contextPath}/jquery-easyui-1.3.3/themes/icon.css">
    <script type="text/javascript"
            src="${pageContext.request.contextPath}/jquery-easyui-1.3.3/jquery.min.js"></script>
    <script type="text/javascript"
            src="${pageContext.request.contextPath}/jquery-easyui-1.3.3/jquery.easyui.min.js"></script>
    <script type="text/javascript"
            src="${pageContext.request.contextPath}/jquery-easyui-1.3.3/locale/easyui-lang-zh_CN.js"></script>
    <script type="text/javascript">
        var url;

        window.onload=function(){
            if ("管理员" !== "${currentUser.role}") {
                $(".system").hide();
                $(".system").disabled=true;
            }
        }

        function deleteMessage() {
            var selectedRows = $("#dg").datagrid('getSelections');
            if (selectedRows.length == 0) {
                $.messager.alert("系统提示", "请选择要删除的信息！");
                return;
            }
            var strIdList = [];
            for (var i = 0; i < selectedRows.length; i++) {
                strIdList.push(selectedRows[i].id);
            }
            var idList = strIdList.join(",");
            $.messager.confirm("系统提示", "您确认要删除这<font color=red>"
                + selectedRows.length + "</font>条数据吗？", function (r) {
                if (r) {
                    $.post("${pageContext.request.contextPath}/message/delete.do", {
                        idList: idList
                    }, function (result) {
                        if (result.success) {
                            $.messager.alert("系统提示", "信息已成功删除！");

                            $.post("${pageContext.request.contextPath}/search/index.do", {
                            }, function (result) {
                                if (result.success) {
                                    $.messager.alert("系统提示", "lucene索引更新成功！");
                                } else {
                                    $.messager.alert("系统提示", "lucene索引更新失败！");
                                }
                            }, "json");

                            $("#dg").datagrid("reload");
                        } else {
                            $.messager.alert("系统提示", "信息删除失败！");
                        }
                    }, "json");
                }
            });

        }
    </script>
</head>
<body style="margin:1px;">
<table id="dg" title="所有信息" class="easyui-datagrid" fitColumns="true"
       pagination="true" rownumbers="true"
       url="${pageContext.request.contextPath}/message/list.do" fit="true"
       toolbar="#tb">
    <thead>
    <tr>
        <th class="system" field="cb" checkbox="true" align="center"></th>
        <th field="id" width="50" align="center">编号</th>
        <th field="intro" width="50" align="center">简介</th>
        <th field="content" width="150" align="center">内容</th>
        <th field="userName" width="50" align="center">发布者</th>
        <th field="date" width="100" align="center">发布时间</th>
        <th field="email" width="100" align="center">联系方式</th>
    </tr>
    </thead>
</table>
<div id="tb" class="system">
    <div>
        <a href="javascript:deleteMessage()" class="easyui-linkbutton"
           iconCls="icon-remove" plain="true">删除</a>
    </div>
</div>
</body>
</html>