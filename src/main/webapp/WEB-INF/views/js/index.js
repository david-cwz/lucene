window.onload=function(){
    new Vue({
        el:'#box',
        data:{
            myData:[],
            t1:'',
            now:-1,
            isShow:true,
        },
        methods:{
            get:function(ev){
                if(ev.keyCode==38 || ev.keyCode==40)return;

                if(ev.keyCode==13){
                    window.open('https://www.baidu.com/s?wd='+this.t1);
                    this.t1='';
                }

                this.$http.jsonp('https://sp0.baidu.com/5a1Fazu8AA54nxGko9WTAnF6hhy/su',{
                    wd:this.t1
                },{
                    jsonp:'cb'
                }).then(function(res){
                    this.myData=res.data.s;
                    this.isShow='true'
                },function(){

                });
            },
            search:function(){
                window.open('https://www.baidu.com/s?wd='+this.t1);
                this.t1='';
            },
            changeDown:function(){
                this.now++;
                if(this.now==this.myData.length)this.now=-1;
                this.t1=this.myData[this.now];
            },
            changeUp:function(){
                this.now--;
                if(this.now==-2)this.now=this.myData.length-1;
                this.t1=this.myData[this.now];
            },
            clk:function(e){
                var index=e.target.innerText;
                this.t1=index;
                var input=document.getElementById("input");
//                    console.log(input)
                input.style.color="blue";
                input.focus()
                this.isShow=false;
                var boxUl=document.getElementById("boxUl");
                console.log(boxUl);

            }
        }
    });

}

function clearText(elm){
    elm.value="";
    elm.onfocus=null;
}
function changeText(elm){
    elm.value="点击下"
}
function showLogin(){
    document.getElementById("backgroundColor").style.display="block";
    document.getElementById("loginForm").style.display="block";
}

function hideLogin(){
    document.getElementById("backgroundColor").style.display="none";
    document.getElementById("loginForm").style.display="none";
}

function showRegister(){
    document.getElementById("backgroundColor").style.display="block";
    document.getElementById("registerForm").style.display="block";
}

function hideRegister(){
    document.getElementById("backgroundColor").style.display="none";
    document.getElementById("registerForm").style.display="none";
}

function login() {
    var username = document.getElementById("userName")
    var password = document.getElementById("userPassword")
    if (username.value.trim().length==0) {
        username.style.borderColor = "red"
        return;
    }
    if (password.value.trim().length==0) {
        password.style.borderColor = "red"
        return;
    }
    window.location.href="/login";
}

function register() {
    var username = document.getElementById("userName_r")
    var password = document.getElementById("userPassword_r")
    var repeat = document.getElementById("repeatPassword")
    if (username.value.trim().length==0) {
        username.style.borderColor = "red"
        return;
    }
    if (password.value.trim().length==0) {
        password.style.borderColor = "red"
        return;
    }
    if (repeat.value.trim() !== password.value.trim()) {
        repeat.style.borderColor = "red"
        return;
    }
    var registerData = {
        username: username.value,
        password: password.value
    }
    var httpRequest = new XMLHttpRequest();//第一步：创建需要的对象
    httpRequest.open('POST', '/register', true); //第二步：打开连接/***发送json格式文件必须设置请求头 ；如下 - */
    httpRequest.setRequestHeader("Content-type","application/json;charset=UTF-8");//设置请求头 注：post方式必须设置请求头（在建立连接后设置请求头）var obj = { name: 'zhansgan', age: 18 };
    httpRequest.send(JSON.stringify(registerData));//发送请求 将json写入send中
    /**
     * 获取数据后的处理程序
     */
    httpRequest.onreadystatechange = function () {//请求后的回调接口，可将请求成功后要执行的程序写在其中
        if (httpRequest.readyState == 4 && httpRequest.status == 200) {//验证请求是否发送成功
            var json = httpRequest.responseText;//获取到服务端返回的数据
            var output = document.getElementById("output");
            output.innerText = json;
        }
    };
}
