<%@ page language="java" contentType="text/html; charset=utf-8"
         pageEncoding="utf-8" %>
<%--<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">--%>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <title>预埋单搜索匹配系统主页</title>
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

    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0">
    <meta name="apple-mobile-web-app-capable" content="yes">
    <meta name="apple-mobile-web-app-status-bar-style" content="black">
    <script type="text/javascript" src="views/js/vue.js"></script>
    <script type="text/javascript" src="views/js/vue-resource.js"></script>
    <!--<link rel="stylesheet" href="css/input.css" type="text/css" />-->
    <link rel="icon" href="views/img/icon.jpg">
    <link rel="stylesheet" href="views/css/index.css">

    <script type="text/javascript">
        var url;
        function addTab(url, text, iconCls) {
            var content = "<iframe frameborder=0 scrolling='auto' style='width:100%;height:100%' src='${pageContext.request.contextPath}/views/"
                + url + "'></iframe>";
            $("#tabs").tabs("add", {
                title: text,
                iconCls: iconCls,
                closable: true,
                content: content
            });
        }
        function openTab(text, url, iconCls) {
            if ($("#tabs").tabs("exists", text)) {
                $("#tabs").tabs("close", text);
                addTab(url, text, iconCls);
                $("#tabs").tabs("select", text);
            } else {
                addTab(url, text, iconCls);
            }
        }

        function openPasswordModifyDialog() {
            $("#dlg").dialog("open").dialog("setTitle", "修改密码");
            url = "${pageContext.request.contextPath}/user/modifyPassword.do?";
        }
        function openUserInfoModifyDialog() {
            $("#dlg2").dialog("open").dialog("setTitle", "修改用户信息");
            $("#userName2").val("${currentUser.userName}");
            $("#email").val("${currentUser.email}");
            url = "${pageContext.request.contextPath}/user/modifyUserInfo.do?currentUser=${currentUser.userName}";
        }

        function closePasswordModifyDialog() {
            $("#dlg").dialog("close");
            $("#oldPassword").val("");
            $("#newPassword").val("");
            $("#newPassword2").val("");
        }
        function closeUserInfoModifyDialog() {
            $("#dlg2").dialog("close");
            $("#userName2").val("");
            $("#email").val("");
        }

        function modifyPassword() {
            $("#fm").form("submit", {
                url: url,
                onSubmit: function () {
                    var oldPassword = $("#oldPassword").val();
                    var newPassword = $("#newPassword").val();
                    var newPassword2 = $("#newPassword2").val();
                    if (!$(this).form("validate")) {
                        return false;
                    }
                    if (oldPassword !== "${currentUser.password}") {
                        $.messager.alert("系统提示", "原密码输入错误！");
                        return false;
                    }
                    if (newPassword !== newPassword2) {
                        $.messager.alert("系统提示", "确认密码输入错误！");
                        return false;
                    }
                    return true;
                },
                success: function (result) {
                    var result = eval('(' + result + ')');
                    if (result.success) {
                        $.messager.alert("系统提示", "密码修改成功！");
                        closePasswordModifyDialog();
                    } else {
                        $.messager.alert("系统提示", "密码修改失败");
                        return;
                    }
                }
            });
        }
        function modifyUserInfo() {
            $("#fm2").form("submit", {
                url: url,
                onSubmit: function () {
                    var userName = $("#userName2").val();
                    var email = $("#email").val();
                    if (!$(this).form("validate")) {
                        return false;
                    }
                    return true;
                },
                success: function (result) {
                    var result = eval('(' + result + ')');
                    if (result.success) {
                        $.messager.alert("系统提示", "用户信息修改成功！");
                        closeUserInfoModifyDialog();
                        window.location.reload();
                    } else {
                        $.messager.alert("系统提示", "用户信息修改失败");
                        return;
                    }
                }
            });
        }

        function logout() {
            $.messager
                .confirm(
                    "系统提示",
                    "您确定要退出系统吗",
                    function (r) {
                        if (r) {
                            window.location.href = "${pageContext.request.contextPath}/user/logout.do";
                        }
                    });
        }

        window.onload=function(){
            if ("管理员" !== "${currentUser.role}") {
                $(".system").hide();
                $(".system").disabled=true;
            }
        }

        function clearText(elm){
            elm.value="";
            elm.onfocus=null;
        }
    </script>
    <jsp:include page="login_chk.jsp"/>
<body class="easyui-layout">
<div region="north" style="height: 78px;background-color: #ffff">
    <table width="100%">
        <tr>
            <td width="50%"></td>
            <td valign="bottom"
                style="font-size: 20px;color:#8B8B8B;font-family: '楷体';"
                align="right" width="50%"><font size="3">&nbsp;&nbsp;<strong>当前用户：</strong>${currentUser.userName}(${currentUser.role})</font>
            </td>
        </tr>
    </table>
</div>
<div region="center">
    <div class="easyui-tabs" fit="true" border="false" id="tabs">
        <div title="首页" data-options="iconCls:'icon-home'">
            <section>
                <img src="views/img/title.JPG" alt="未加载成功">
            </section>
        </div>
    </div>
</div>
<div region="west" style="width: 200px;height:500px;" title="导航菜单"
     split="true">
    <div class="easyui-accordion">

        <div title="搜索管理" data-options="iconCls:'icon-shujias'"
             style="padding:10px">
            <a href="javascript:openTab(' 搜索页面','search.jsp','icon-shujia')"
               class="easyui-linkbutton"
               data-options="plain:true,iconCls:'icon-shujia'"
               style="width: 150px;"> 搜索页面</a>
            <a href="javascript:openTab(' 我的搜索记录','searchRecord.jsp','icon-shujia')"
               class="easyui-linkbutton"
               data-options="plain:true,iconCls:'icon-shujia'"
               style="width: 150px;"> 我的搜索记录</a>
            <a href="javascript:openTab(' 网站列表管理','urlManage.jsp','icon-shujia')"
               class="easyui-linkbutton system"
               data-options="plain:true,iconCls:'icon-shujia'"
               style="width: 150px;"> 网站列表管理</a>
        </div>
        <div title="信息管理" data-options="iconCls:'icon-shuji'"
             style="padding:10px">
            <a href="javascript:openTab(' 我发布的信息','messageManage.jsp','icon-shuben')"
               class="easyui-linkbutton"
               data-options="plain:true,iconCls:'icon-shuben'"
               style="width: 150px;"
               >我发布的信息</a>
            <a href="javascript:openTab(' 所有信息','allMessage.jsp','icon-shuben')"
               class="easyui-linkbutton"
               data-options="plain:true,iconCls:'icon-shuben'"
               style="width: 150px;">所有信息</a>
        </div>
        <div title="用户管理" data-options="iconCls:'icon-item'"
             style="padding:10px;border:none;">
            <a href="javascript:openTab('用户列表','userManage.jsp','icon-lxr')"
               class="easyui-linkbutton system"
               data-options="plain:true,iconCls:'icon-lxr'" style="width: 150px;">
                用户列表</a>
            <a href="javascript:openUserInfoModifyDialog()"
               class="easyui-linkbutton"
               data-options="plain:true,iconCls:'icon-modifyPassword'"
               style="width: 150px;"> 修改我的信息</a>
            <a href="javascript:openPasswordModifyDialog()"
               class="easyui-linkbutton"
               data-options="plain:true,iconCls:'icon-modifyPassword'"
               style="width: 150px;"> 修改密码</a>
            <a href="javascript:logout()"
               class="easyui-linkbutton"
               data-options="plain:true,iconCls:'icon-exit'"
               style="width: 150px;">安全退出</a>
        </div>
    </div>
</div>

<%--修改密码--%>
<div id="dlg" class="easyui-dialog"
     style="width: 400px;height:250px;padding: 10px 20px" closed="true"
     buttons="#dlg-buttons">
    <form id="fm" method="post">
        <table cellspacing="8px">
            <tr>
                <td>用户名：</td>
                <td><input type="text" id="userName" name="userName"
                           value="${currentUser.userName}" readonly="readonly"
                           style="width: 200px"/>
                </td>
            </tr>
            <tr>
                <td>原密码：</td>
                <td><input type="password" id="oldPassword" name="oldPassword"
                           class="easyui-validatebox" required="true" style="width: 200px"/>
                </td>
            </tr>
            <tr>
                <td>新密码：</td>
                <td><input type="password" id="newPassword" name="password"
                           class="easyui-validatebox" required="true" style="width: 200px"/>
                </td>
            </tr>
            <tr>
                <td>确认新密码：</td>
                <td><input type="password" id="newPassword2"
                           class="easyui-validatebox" required="true" style="width: 200px"/>
                </td>
            </tr>
        </table>
    </form>
</div>
<div id="dlg-buttons">
    <a href="javascript:modifyPassword()" class="easyui-linkbutton"
       iconCls="icon-ok">保存</a><a
        href="javascript:closePasswordModifyDialog()"
        class="easyui-linkbutton" iconCls="icon-cancel">关闭</a>
</div>

<%--修改用户信息--%>
<div id="dlg2" class="easyui-dialog"
     style="width: 400px;height:250px;padding: 10px 20px" closed="true"
     buttons="#dlg2-buttons">
    <form id="fm2" method="post">
        <table cellspacing="8px">
            <tr>
                <td>用户名：</td>
                <td><input type="text" id="userName2" name="userName"
                           value="${currentUser.userName}"
                           style="width: 200px"/>
                </td>
            </tr>
            <tr>
                <td>邮箱：</td>
                <td><input type="text" id="email" name="email"
                           class="easyui-validatebox"
                           value="${currentUser.email}"
                           style="width: 200px"/>
                </td>
            </tr>
        </table>
    </form>
</div>
<div id="dlg2-buttons">
    <a href="javascript:modifyUserInfo()" class="easyui-linkbutton"
       iconCls="icon-ok">保存</a><a
        href="javascript:closeUserInfoModifyDialog()"
        class="easyui-linkbutton" iconCls="icon-cancel">关闭</a>
</div>
</body>
</html>
