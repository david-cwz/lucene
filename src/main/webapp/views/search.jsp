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

        function search() {
            var keyWord = $("#keyWord").val();

            $.post("${pageContext.request.contextPath}/search/search.do?keyWord=" + keyWord + "&userName=${currentUser.userName}&searchTarget=本系统", {
            }, function (result) {
                if (result.success) {
                    window.parent.openTab(' “' + keyWord + '”的系统搜索结果','searchResult.jsp','icon-shujia');
                } else {
                    $.messager.alert("系统提示", "搜索失败！");
                }
            }, "json");
        }

        function searchOnline() {
            var keyWord = $("#keyWord2").val();
            var inputUrl = $("#url").val();
            var selectedRows = $("#dg").datagrid('getSelections');
            if (selectedRows.length === 0 && (inputUrl === "" || inputUrl === null || inputUrl === undefined)) {
                $.messager.alert("系统提示", "请选择要搜索的网站或输入要搜索的网址！");
                return;
            }
            var strUrlList = [];
            if (inputUrl !== "" && inputUrl !== null && inputUrl !== undefined) {
                strUrlList.push(inputUrl);
            }
            for (var i = 0; i < selectedRows.length; i++) {
                strUrlList.push(selectedRows[i].url);
            }
            var urlList = strUrlList.join(" ");

            $.post("${pageContext.request.contextPath}/search/searchOnline.do?keyWord=" + keyWord + "&userName=${currentUser.userName}", {
                urlList:urlList
            }, function (result) {
                if (result.success) {
                    window.parent.openTab(' “' + keyWord + '”的网页搜索结果','searchOnlineResult.jsp','icon-shujia');
                } else {
                    $.messager.alert("系统提示", "搜索失败！");
                }
            }, "json");

        }

    </script>
</head>
<body style="margin:1px;">
<table id="dg" title="搜索" class="easyui-datagrid" fitColumns="true"
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
        &nbsp;输入搜索内容：&nbsp;<input type="text" id="keyWord" size="20"
                               onkeydown="if(event.keyCode===13) search()"/>
        <a href="javascript:search()" class="easyui-linkbutton"
           iconCls="icon-search" plain="true">本系统搜索</a>
    </div>
    <br>
    <div>
        &nbsp;输入搜索内容：&nbsp;<input type="text" id="keyWord2" size="20"
                                 onkeydown="if(event.keyCode===13) searchOnline()"/>
        <a href="javascript:searchOnline()" class="easyui-linkbutton"
           iconCls="icon-search" plain="true">网页搜索（将在输入的网址和以下勾选的网站中搜索）</a>
    </div>
    <br>
    <div>
        &nbsp;输入网址：&nbsp;<input type="text" id="url" size="20""/>
    </div>
</div>

</div>
</body>
</html>