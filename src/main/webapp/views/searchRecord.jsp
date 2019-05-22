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

        function deleteRecord() {
            var selectedRows = $("#dg").datagrid('getSelections');
            if (selectedRows.length == 0) {
                $.messager.alert("系统提示", "请选择要删除的数据！");
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
                    $.post("${pageContext.request.contextPath}/search/deleteRecord.do", {
                        idList: idList
                    }, function (result) {
                        if (result.success) {
                            $.messager.alert("系统提示", "记录已成功删除！");
                            $("#dg").datagrid("reload");
                        } else {
                            $.messager.alert("系统提示", "记录删除失败！");
                        }
                    }, "json");
                }
            });

        }

        function reSearchRecord() {
            var selectedRows = $("#dg").datagrid('getSelections');
            if (selectedRows.length !== 1) {
                $.messager.alert("系统提示", "请选择“1”条要重新搜索的记录！");
                return;
            }
            var keyWord = selectedRows[0].record;
            $.post("${pageContext.request.contextPath}/search/setKeyWord.do?keyWord=" + keyWord, {
            }, function (result) {
            }, "json");

            $.post("${pageContext.request.contextPath}/search/updateStatus.do?id=" + selectedRows[0].id + "&haveNewResult=no%20new%20results", {
            }, function (result) {
            }, "json");

            $("#dg").datagrid("reload");

            if (selectedRows[0].searchTarget === "本系统") {
                window.parent.openTab(' “' + keyWord + '”的系统搜索结果','searchResult.jsp','icon-shujia');
            } else {
                $.post("${pageContext.request.contextPath}/search/indexOnline.do", {
                    url:selectedRows[0].searchTarget
                }, function (result) {
                }, "json");
                window.parent.openTab(' “' + keyWord + '”的网页搜索结果','searchOnlineResult.jsp','icon-shujia');
            }
        }

        function openRecordShiftDialog() {
            var selectedRows = $("#dg").datagrid('getSelections');
            if (selectedRows.length !== 1) {
                $.messager.alert("系统提示", "请选择“1”条要转换的记录！");
                return;
            }
            var id = selectedRows[0].id;
            $.messager.confirm("系统提示", "您确认要转换这条记录的预埋单状态吗？", function (r) {
                if (r) {
                    $.post("${pageContext.request.contextPath}/search/shiftStatus.do", {
                        id: id
                    }, function (result) {
                        if (result.success) {
                            $.messager.alert("系统提示", "转换成功！");

                            $.post("${pageContext.request.contextPath}/search/setKeyWord.do?keyWord=" + selectedRows[0].record, {
                            }, function (result) {
                            }, "json");
                        } else {
                            $.messager.alert("系统提示", "转换失败！");
                        }
                        // window.parent.openTab(' “' + selectedRows[0].record + '”的搜索结果','searchResult.jsp','icon-shujia');
                        $("#dg").datagrid("reload");
                    }, "json");
                }
            });
        }

        function saveRecord() {
            $("#fm").form("submit", {
                url: url,
                onSubmit: function () {
                    return $(this).form("validate");
                },
                success: function (result) {
                    var _result = JSON.parse(result);
                    if (_result.success) {
                        $.messager.alert("系统提示", "保存成功");
                    } else {
                        $.messager.alert("系统提示", "保存失败");
                    }
                    resetValue();
                    $("#dlg").dialog("close");
                    $("#dg").datagrid("reload");
                }
            });
        }

        function resetValue() {
            $("#record").val("");
            // $("#date").val("");
        }

        function closeRecordDialog() {
            $("#dlg").dialog("close");
            resetValue();
        }
    </script>
</head>
<body style="margin:1px;">
<table id="dg" title="我的搜索记录" class="easyui-datagrid" fitColumns="true"
       pagination="true" rownumbers="true"
       url="${pageContext.request.contextPath}/search/recordList.do?userName=${currentUser.userName}" fit="true"
       toolbar="#tb">
    <thead>
    <tr>
        <th field="cb" checkbox="true" align="center"></th>
        <th field="id" width="50" align="center">编号</th>
        <th field="record" width="100" align="center">搜索内容</th>
        <th field="date" width="50" align="center">搜索日期</th>
        <th field="preEmbedded" width="50" align="center">是否预埋单</th>
        <th field="haveNewResult" width="50" align="center">是否有新结果（预埋单为true）</th>
        <th field="searchTarget" width="100" align="center">搜索目标</th>
    </tr>
    </thead>
</table>
<div id="tb">
    <div>
        <a href="javascript:openRecordShiftDialog()" class="easyui-linkbutton"
           iconCls="icon-edit" plain="true">转换预埋单状态</a>
        <a href="javascript:reSearchRecord()" class="easyui-linkbutton"
           iconCls="icon-edit" plain="true">重新搜索此条记录</a>
        <a href="javascript:deleteRecord()" class="easyui-linkbutton"
            iconCls="icon-remove" plain="true">删除</a>
    </div>
</div>

<div id="dlg" class="easyui-dialog"
     style="width: 620px;height:250px;padding: 10px 20px" closed="true"
     buttons="#dlg-buttons">
    <form id="fm" method="post">
        <table cellspacing="8px">
            <tr>
                <td>当前用户：</td>
                <td><input type="text" id="userName" name="userName"
                           value="${currentUser.userName}" readonly="readonly"
                           class="easyui-validatebox" required="true"/>&nbsp;<font
                        color="red">*</font>
                </td>
            </tr>
            <tr>
                <td>搜索内容：</td>
                <td><input type="text" id="record" name="record"
                           class="easyui-validatebox" required="true"/>&nbsp;<font
                        color="red">*</font>
                </td>
            </tr>
        </table>
    </form>
</div>

<div id="dlg-buttons">
    <a href="javascript:saveRecord()" class="easyui-linkbutton"
       iconCls="icon-ok">保存</a> <a href="javascript:closeRecordDialog()"
                                   class="easyui-linkbutton" iconCls="icon-cancel">关闭</a>
</div>
</body>
</html>