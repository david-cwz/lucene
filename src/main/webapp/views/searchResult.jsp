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
</head>
<body style="margin:1px;">
<table id="dg" title="“${record.record}”的系统搜索结果" class="easyui-datagrid" fitColumns="true"
       pagination="true" rownumbers="true"
       url="${pageContext.request.contextPath}/search/resultList.do?recordId=${record.id}" fit="true"
       toolbar="#tb">
    <thead>
    <tr>
        <th field="messageId" width="50" align="center">结果所在编号</th>
        <th field="intro" width="100" align="center">简介</th>
        <th field="content" width="150" align="center">内容匹配</th>
        <th field="userName" width="50" align="center">发布者</th>
        <th field="email" width="100" align="center">发布者联系方式</th>
        <th field="date" width="50" align="center">发布日期</th>
    </tr>
    </thead>
</table>
</div>
</body>
</html>