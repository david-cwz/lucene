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

        function deleteUrl() {
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
                    + selectedRows.length + "</font>个网站吗？", function (r) {
                if (r) {
                    $.post("${pageContext.request.contextPath}/search/deleteUrl.do", {
                        idList: idList
                    }, function (result) {
                        if (result.success) {
                            $.messager.alert("系统提示", "删除成功！");
                            $("#dg").datagrid("reload");
                        } else {
                            $.messager.alert("系统提示", "信息删除失败！");
                        }
                    }, "json");
                }
            });

        }

        function openUrlAddDialog() {
            resetValue();
            $("#id").val(0);
            $("#dlg").dialog("open").dialog("setTitle", "添加网站");
            url = "${pageContext.request.contextPath}/search/addUrl.do";
        }
        function openUrlModifyDialog() {
            var selectedRows = $("#dg").datagrid('getSelections');
            if (selectedRows.length !== 1) {
                $.messager.alert("系统提示", "请选择“1”条要修改的信息！");
                return;
            }
            var id = selectedRows[0].id;
            var name = selectedRows[0].name;
            var url2 = selectedRows[0].url;

            $("#dlg").dialog("open").dialog("setTitle", "编辑网站");
            url = "${pageContext.request.contextPath}/search/modifyUrl.do";
            $("#id").val(id);
            $("#name").val(name);
            $("#url").val(url2);
        }

        function saveUrl() {
            $("#fm").form("submit", {
                url: url,
                onSubmit: function () {
                    return $(this).form("validate");
                },
                success: function (result) {
                    var _result = JSON.parse(result);
                    if (_result.success) {
                        $.messager.alert("系统提示", "添加成功");
                    } else {
                        $.messager.alert("系统提示", "添加失败");
                    }
                    resetValue();
                    $("#dlg").dialog("close");
                    $("#dg").datagrid("reload");
                }
            });
        }

        function modifyUrl() {
            $("#fm2").form("submit", {
                url: url,
                onSubmit: function () {
                    return $(this).form("validate");
                },
                success: function (result) {
                    var _result = JSON.parse(result);
                    if (_result.success) {
                        $.messager.alert("系统提示", "编辑成功");
                    } else {
                        $.messager.alert("系统提示", "编辑失败");
                    }
                    resetValue();
                    $("#dlg").dialog("close");
                    $("#dg").datagrid("reload");
                }
            });
        }

        function resetValue() {
            $("#name").val("");
            $("#url").val("");
        }

        function closeUrlDialog() {
            $("#dlg").dialog("close");
            resetValue();
        }

    </script>
</head>
<table id="dg" title="网站管理" class="easyui-datagrid" fitColumns="true"
       pagination="true" rownumbers="true"
       url="${pageContext.request.contextPath}/search/urlList.do" fit="true"
       toolbar="#tb">
    <thead>
    <tr>
        <th field="cb" checkbox="true" align="center"></th>
        <th field="id" width="50" align="center">网站编号</th>
        <th field="name" width="50" align="center">网站名</th>
        <th field="url" width="200" align="center">网址</th>
    </tr>
    </thead>
</table>
<div id="tb">
    <div>
        <a href="javascript:openUrlAddDialog()" class="easyui-linkbutton"
           iconCls="icon-add" plain="true">添加网站</a>
        <a href="javascript:openUrlModifyDialog()" class="easyui-linkbutton"
           iconCls="icon-edit" plain="true">编辑所选网站</a>
        <a href="javascript:deleteUrl()" class="easyui-linkbutton"
           iconCls="icon-remove" plain="true">删除网站</a>
    </div>
</div>

<div id="dlg" class="easyui-dialog"
     style="width: 620px;height:250px;padding: 10px 20px" closed="true"
     buttons="#dlg-buttons">
    <form id="fm" method="post">
        <table cellspacing="8px">
            <tr  style="display:none;">
                <td>编号：</td>
                <td><input type="text" id="id" name="id"/>&nbsp;<font
                        color="red">*</font>
                </td>
            </tr>
            <tr>
                <td>网站名：</td>
                <td><input type="text" id="name" name="name"
                           class="easyui-validatebox" required="true"/>&nbsp;<font
                        color="red">*</font>
                </td>
            </tr>
            <tr>
                <td>网址：</td>
                <td><input type="text" id="url" name="url"
                           value="${currentUser.userName}"
                           class="easyui-validatebox" required="true"/>&nbsp;<font
                        color="red">*</font>
                </td>
            </tr>
        </table>
    </form>
</div>

<div id="dlg-buttons">
    <a href="javascript:saveUrl()" class="easyui-linkbutton"
       iconCls="icon-ok">保存</a> <a href="javascript:closeUrlDialog()"
                                   class="easyui-linkbutton" iconCls="icon-cancel">关闭</a>
</div>

</body>
</html>