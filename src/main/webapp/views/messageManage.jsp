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

        function openMessageAddDialog() {
            $("#dlg").dialog("open").dialog("setTitle", "发布信息");
            url = "${pageContext.request.contextPath}/message/add.do";
        }
        function openMessageModifyDialog() {
            var selectedRows = $("#dg").datagrid('getSelections');
            if (selectedRows.length !== 1) {
                $.messager.alert("系统提示", "请选择“1”条要修改的信息！");
                return;
            }
            var id = selectedRows[0].id;
            var intro = selectedRows[0].intro;
            var content = selectedRows[0].content;

            $("#dlg2").dialog("open").dialog("setTitle", "修改信息");
            url = "${pageContext.request.contextPath}/message/modify.do";
            $("#id2").val(id);
            $("#intro2").val(intro);
            $("#content2").val(content);
        }

        function saveMessage() {
            $("#fm").form("submit", {
                url: url,
                onSubmit: function () {
                    return $(this).form("validate");
                },
                success: function (result) {
                    var _result = JSON.parse(result);
                    if (_result.success) {
                        $.messager.alert("系统提示", "信息发布成功");

                        $.post("${pageContext.request.contextPath}/search/index.do", {
                        }, function (result) {
                            if (result.success) {
                                $.messager.alert("系统提示", "lucene索引更新成功！");
                            } else {
                                $.messager.alert("系统提示", "lucene索引更新失败！");
                            }
                        }, "json");

                    } else {
                        $.messager.alert("系统提示", "信息发布失败");
                    }
                    resetValue();
                    $("#dlg").dialog("close");
                    $("#dg").datagrid("reload");
                }
            });
        }

        function modifyMessage() {
            $("#fm2").form("submit", {
                url: url,
                onSubmit: function () {
                    return $(this).form("validate");
                },
                success: function (result) {
                    var _result = JSON.parse(result);
                    if (_result.success) {
                        $.messager.alert("系统提示", "信息修改成功");

                        $.post("${pageContext.request.contextPath}/search/index.do", {
                        }, function (result) {
                            if (result.success) {
                                $.messager.alert("系统提示", "lucene索引更新成功！");
                            } else {
                                $.messager.alert("系统提示", "lucene索引更新失败！");
                            }
                        }, "json");

                    } else {
                        $.messager.alert("系统提示", "信息修改失败");
                    }
                    resetValue();
                    $("#dlg2").dialog("close");
                    $("#dg").datagrid("reload");
                }
            });
        }

        function resetValue() {
            $("#intro").val("");
            $("#content").val("");
        }

        function closeMessageDialog() {
            $("#dlg").dialog("close");
            resetValue();
        }
        function closeMessage2Dialog() {
            $("#dlg2").dialog("close");
            resetValue();
        }
    </script>
</head>
<body style="margin:1px;">
<table id="dg" title="我发布的信息管理" class="easyui-datagrid" fitColumns="true"
       pagination="true" rownumbers="true"
       url="${pageContext.request.contextPath}/message/listByUser.do?userName=${currentUser.userName}" fit="true"
       toolbar="#tb">
    <thead>
    <tr>
        <th field="cb" checkbox="true" align="center"></th>
        <th field="id" width="50" align="center">编号</th>
        <th field="intro" width="50" align="center">简介</th>
        <th field="content" width="150" align="center">内容</th>
        <th field="date" width="100" align="center">发布时间</th>
    </tr>
    </thead>
</table>
<div id="tb">
    <div>
        <a href="javascript:openMessageAddDialog()" class="easyui-linkbutton"
           iconCls="icon-add" plain="true">发布新信息</a>
        <a href="javascript:openMessageModifyDialog()" class="easyui-linkbutton"
           iconCls="icon-edit" plain="true">修改所选信息</a>
        <a href="javascript:deleteMessage()" class="easyui-linkbutton"
           iconCls="icon-remove" plain="true">删除</a>
    </div>
</div>

<div id="dlg" class="easyui-dialog"
     style="width: 620px;height:250px;padding: 10px 20px" closed="true"
     buttons="#dlg-buttons">
    <form id="fm" method="post">
        <table cellspacing="8px">
            <tr>
                <td>简介：</td>
                <td><input type="text" id="intro" name="intro"
                           class="easyui-validatebox" required="true"/>&nbsp;<font
                        color="red">*</font>
                </td>
            </tr>
            <tr>
                <td>内容：</td>
                <td><input type="text" id="content" name="content"
                           class="easyui-validatebox" required="true"/>&nbsp;<font
                        color="red">*</font>
                </td>
            </tr>
            <tr style="display:none;">
                <td>发布者：</td>
                <td><input type="text" id="userName" name="userName"
                           value="${currentUser.userName}"
                           class="easyui-validatebox" required="true"/>&nbsp;<font
                        color="red">*</font>
                </td>
            </tr>
            <tr>
                <td><input type="checkbox" id="anonymity"
                           name="anonymity"
                           value=true />
                </td>
                <td>匿名发布</td>
            </tr>
        </table>
    </form>
</div>

<div id="dlg-buttons">
    <a href="javascript:saveMessage()" class="easyui-linkbutton"
       iconCls="icon-ok">保存</a> <a href="javascript:closeMessageDialog()"
                                   class="easyui-linkbutton" iconCls="icon-cancel">关闭</a>
</div>

<div id="dlg2" class="easyui-dialog"
     style="width: 620px;height:250px;padding: 10px 20px" closed="true"
     buttons="#dlg2-buttons">
    <form id="fm2" method="post">
        <table cellspacing="8px">
            <tr style="display:none;">
                <td>id：</td>
                <td><input type="text" id="id2" name="id"
                           class="easyui-validatebox"/>
                </td>
            </tr>
            <tr>
                <td>简介：</td>
                <td><input type="text" id="intro2" name="intro"
                           class="easyui-validatebox"/>
                </td>
            </tr>
            <tr>
                <td>内容：</td>
                <td><input type="text" id="content2" name="content"
                           class="easyui-validatebox"/>
                </td>
            </tr>
        </table>
    </form>
</div>
<div id="dlg2-buttons">
    <a href="javascript:modifyMessage()" class="easyui-linkbutton"
       iconCls="icon-ok">保存</a> <a href="javascript:closeMessage2Dialog()"
                                   class="easyui-linkbutton" iconCls="icon-cancel">关闭</a>
</div>
</body>
</html>