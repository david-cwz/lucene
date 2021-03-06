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

        function searchUser() {
            $("#dg").datagrid('load', {
                "userName": $("#s_userName").val()
            });
        }

        function deleteUser() {
            var selectedRows = $("#dg").datagrid('getSelections');
            if (selectedRows.length === 0) {
                $.messager.alert("系统提示", "请选择要删除的用户！");
                return;
            }
            var strNameList = [];
            for (var i = 0; i < selectedRows.length; i++) {
                if (selectedRows[i].role === "管理员") {
                    $.messager.alert("系统提示", "您不能删除管理员！");
                    return;
                }
                strNameList.push(selectedRows[i].userName);
            }
            var nameList = strNameList.join(",");
            $.messager.confirm("系统提示", "您确认要删除这<font color=red>"
                    + selectedRows.length + "</font>条数据吗？", function (r) {
                if (r) {
                    $.post("${pageContext.request.contextPath}/user/delete.do", {
                        nameList: nameList
                    }, function (result) {
                        if (result.success) {
                            $.messager.alert("系统提示", "数据已成功删除！");
                            $("#dg").datagrid("reload");
                        } else {
                            $.messager.alert("系统提示", "数据删除失败！");
                        }
                    }, "json");
                }
            });
        }

        function changeToSystem() {
            var selectedRows = $("#dg").datagrid('getSelections');
            if (selectedRows.length === 0) {
                $.messager.alert("系统提示", "请选择要升级的用户！");
                return;
            }
            var strNameList = [];
            for (var i = 0; i < selectedRows.length; i++) {
                strNameList.push(selectedRows[i].userName);
            }
            var nameList = strNameList.join(",");
            $.messager.confirm("系统提示", "您确认要升级这<font color=red>"
                + selectedRows.length + "</font>位用户吗？", function (r) {
                if (r) {
                    $.post("${pageContext.request.contextPath}/user/changeToSystem.do", {
                        nameList: nameList
                    }, function (result) {
                        if (result.success) {
                            $.messager.alert("系统提示", "用户升级成功！");
                            $("#dg").datagrid("reload");
                        } else {
                            $.messager.alert("系统提示", "用户升级失败！");
                        }
                    }, "json");
                }
            });

        }

        function resetValue() {
            $("#userName").val("");
            $("#password").val("");
        }

    </script>
</head>
<body style="margin:1px;">
<table id="dg" title="用户管理" class="easyui-datagrid" fitColumns="true"
       pagination="true" rownumbers="true"
       url="${pageContext.request.contextPath}/user/list.do" fit="true"
       toolbar="#tb">
    <thead>
    <tr>
        <th field="cb" checkbox="true" align="center"></th>
        <th field="userName" width="100" align="center">用户名</th>
        <th field="email" width="100" align="center">邮箱</th>
        <th field="role" width="50" align="center">权限</th>
    </tr>
    </thead>
</table>
<div id="tb">
    <div>
         <a href="javascript:changeToSystem()" class="easyui-linkbutton"
            iconCls="icon-edit" plain="true">将所选用户升级为管理员</a>
        <a href="javascript:deleteUser()" class="easyui-linkbutton"
            iconCls="icon-remove" plain="true">删除用户</a>
    </div>
    <%--<div>--%>
        <%--&nbsp;用户名：&nbsp;<input type="text" id="s_userName" size="20"--%>
                               <%--onkeydown="if(event.keyCode==13) searchUser()"/> <a--%>
            <%--href="javascript:searchUser()" class="easyui-linkbutton"--%>
            <%--iconCls="icon-search" plain="true">搜索</a>--%>
    <%--</div>--%>
</div>

</div>
</body>
</html>