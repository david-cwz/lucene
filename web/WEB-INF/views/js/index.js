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
    window.location.href="1215text.html?username="+username.value;
}

function register() {
    var username = document.getElementById("userName_r")
    var password = document.getElementById("userPassword_r")
    if (username.value.trim().length==0) {
        username.style.borderColor = "red"
        return;
    }
    if (password.value.trim().length==0) {
        password.style.borderColor = "red"
        return;
    }
    window.location.href="1215text.html?username="+username.value;
}
