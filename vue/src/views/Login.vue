<template>
 <div class="bigBox">
   <div class="box">
   <!-- 滑动盒子 -->
   <div class="pre-box" ref="preRef">
     <h1>WELCOME</h1>
     <p>JOIN US!</p>
     <div class="img-box">
       <img src="../assets/img/waoku.jpg" alt="" id="avatar" >
     </div>
   </div>
   <!-- 注册盒子 -->
   <div class="register-form">
     <!-- 标题盒子 -->
     <div class="title-box">
       <h1>注册</h1>
     </div>
     <!-- 输入框盒子 -->
     <el-form :rules="rules" :model="user" >
       <el-form-item prop="username" >
       <el-input type="text" placeholder="用户名" v-model="user.username" ></el-input>
       </el-form-item>
       <el-form-item prop="nickname">
       <el-input type="text" placeholder="昵称" v-model="user.nickname"/>
       </el-form-item>
       <el-form-item prop="password">
       <el-input type="password"placeholder="密码"v-model="user.password"></el-input>
       </el-form-item>
       <el-form-item prop="confirmPassword">
       <el-input type="password" placeholder="确认密码" v-model="user.confirmPassword"/>
       </el-form-item>

     </el-form>
     <!-- 按钮盒子 -->
     <div class="btn-box">
       <button @click="register">注册</button>
       <!-- 绑定点击事件 -->
       <p @click="mySwitch()">已有账号?去登录</p>
     </div>
   </div>
   <!-- 登录盒子 -->
     <div class="login-form">
       <!-- 标题盒子 -->
       <div class="title-box">
         <h1>登录</h1>
       </div>
       <!-- 输入框盒子 -->
     <el-form :model="user":rules="rules" label-width="5px" ref="userForm" >
       <el-form-item prop="username">
       <el-input type="text" placeholder="用户名" v-model="user.username"/>
       </el-form-item>
       <el-form-item prop="password">
       <el-input type="text" show-password placeholder="密码" v-model="user.password"></el-input>
       </el-form-item>
     </el-form>
     <!-- 按钮盒子 -->
     <div class="btn-box">
       <button @click="login">登录</button>
       <!-- 绑定点击事件 -->
       <p @click="mySwitch()">没有账号?去注册</p>
     </div>
   </div>
 </div>
 </div>
</template>

<script>
import mySwitch from "../utils/mySwitch";
import {setRoutes} from "../router";
import {setUserInfo} from "../utils/request";

export default {
  name:"Login",
  data(){
    return{
      imgList : [require('../assets/img/wuwu.jpeg'),require('../assets/img/waoku.jpg')],
      user:{},
      rules: {
        username: [
          { required: true, message: '请输入用户名', trigger: 'blur' },
          { min: 3, max: 10, message: '长度应该在3~10个字符之间', trigger: 'blur' }
        ],
        password: [
          { required: true, message: '请输入密码', trigger: 'blur' },
          { min: 5, message: '长度应该大于5', trigger: 'blur' }
        ],
        confirmPassword: [
          { required: true, message: '请确保两次密码一致', trigger: 'blur' },
          { min: 5, message: '长度应该大于5', trigger: 'blur' }
        ]
      }
    }

  },
  methods:{
    mySwitch,
    login(){
      this.$refs['userForm'].validate((valid) => {
        if(valid){//表单校验合法
          this.request.post("/user/login",this.user).then(res =>{
            if(res.code === '200'){
              console.log('login---',res.data)
              localStorage.setItem("user",JSON.stringify(res.data))//存储用户信息到浏览器
              localStorage.setItem("menus", JSON.stringify(res.data.menus))  // 存储用户信息到浏览器
              setUserInfo(res.data)
              //动态设置当前路由
              setRoutes()
              this.$router.push("/home")
              this.$message.success("登陆成功")
            }else{
              this.$message.error(res.msg)
            }
          })
        }
      });
    },
    register(){
      this.$refs['userForm'].validate((valid) => {
        if(valid){//表单校验合法
          if(this.user.password !== this.user.confirmPassword){
            this.$message.error("两次密码不一致")
            return false
          }
          this.request.post("/user/register",this.user).then(res =>{
            if(res.code === '200'){
              this.$message.success("注册成功")
            }else{
              this.$message.error(res.msg)
            }
          })
        }
      });
    }
  }
}

// const mySwitch = () => {
//   // if(flag.value){
//   //   preRef.value.style.background = '#c9e0ed'
//   //   preRef.value.style.transform = 'translateX(100%)'
//   // }else {
//   //   preRef.value.style.background = '#edd4dc'
//   //   preRef.value.style.transform = 'translateX(0%)'
//   // }
//   // flag.value =! flag.value
// }
// const loginForm = reactive({
//   username: '',
//   password: ''
// })
// const registerForm = reactive({
//   username: '',
//   password: '',
//   confirmPassword: ''
// })
// const onInput = () => {
//   this.$forceUpdate();
// }

// const loginFormRef = ref('')
// const registerFormRef = ref('')
// const login = () => {
//   loginFormRef.value.validate((valid) => {
//     if (valid) {
//       api.loginApi(loginForm).then(res => {
//         console.log('login', res)
//         if (res.status === 0) {
//           Message.success(res.message)
//           window.sessionStorage.setItem('token', res.token)
//           router.push('/home')
//         }
//       }).catch(error => {
//         console.log(error);
//       })
//     } else {
//       return
//     }
//   })
// }

// const register = () => {
//   registerFormRef.value.validate((valid) => {
//     if (valid) {
//       api.registerApi(registerForm).then(res => {
//         if (res.status === 0) {
//           Message.success(res.message)
//         }
//       }).catch(error => {
//         console.log(error);
//       })
//     } else {
//       return
//     }
//   })
// }
</script>

<style scoped>
/* 去除input的轮廓 */
input {
  outline: none;
}
.bigBox {
  /* 溢出隐藏 */
  height: 100vh;
  overflow-x: hidden;
  display: flex;
  /* 渐变方向从左到右 */
  background: linear-gradient(to right, rgb(247, 209, 215), rgb(191, 227, 241));
}
.box {
  width: 1050px;
  height: 600px;
  display: flex;
  /* 相对定位 */
  position: relative;
  z-index: 2;
  margin: auto;
  /* 设置圆角 */
  border-radius: 8px;
  /* 设置边框 */
  border: 1px solid rgba(255, 255, 255, .6);
  /* 设置盒子阴影 */
  box-shadow: 2px 1px 19px rgba(0, 0, 0, .1);
}
/* 输入框盒子 */
.el-form-item /deep/ .el-form-item__error{
  margin-left: 108px;
}
/* 滑动的盒子 */
.pre-box {
  /* 宽度为大盒子的一半 */
  width: 50%;
  /* width: var(--width); */
  height: 100%;
  /* 绝对定位 */
  position: absolute;
  /* 距离大盒子左侧为0 */
  left: 0;
  /* 距离大盒子顶部为0 */
  top: 0;
  z-index: 99;
  border-radius: 4px;
  background-color: #edd4dc;
  box-shadow: 2px 1px 19px rgba(0, 0, 0, .1);
  /* 动画过渡，先加速再减速 */
  transition: 0.5s ease-in-out;
}
/* 滑动盒子的标题 */
.pre-box h1 {
  margin-top: 150px;
  text-align: center;
  /* 文字间距 */
  letter-spacing: 5px;
  color: white;
  /* 禁止选中 */
  user-select: none;
  /* 文字阴影 */
  text-shadow: 4px 4px 3px rgba(0, 0, 0, .1);
}

/* 滑动盒子的文字 */
.pre-box p {
  height: 30px;
  line-height: 30px;
  text-align: center;
  margin: 20px 0;
  /* 禁止选中 */
  user-select: none;
  font-weight: bold;
  color: white;
  text-shadow: 4px 4px 3px rgba(0, 0, 0, .1);
}

/* 图片盒子 */
.img-box {
  width: 200px;
  height: 200px;
  margin: 20px auto;
  /* 设置为圆形 */
  border-radius: 50%;
  /* 设置用户禁止选中 */
  user-select: none;
  overflow: hidden;
  box-shadow: 4px 4px 3px rgba(0, 0, 0, .1);
}

/* 图片 */
.img-box img {
  width: 100%;
  transition: 0.5s;
}

/* 登录和注册盒子 */
.login-form,
.register-form {
  flex: 1;
  height: 100%;
}

/* 标题盒子 */
.title-box {
  height: 180px;
  line-height: 280px;

}

/* 标题 */
.title-box h1 {
  text-align: center;
  color: white;
  /* 禁止选中 */
  user-select: none;
  letter-spacing: 5px;
  text-shadow: 4px 4px 3px rgba(0, 0, 0, .1);

}

/*!* 输入框盒子 *!*/
/*.input-box {*/
/*  display: flex;*/
/*  !* 纵向布局 *!*/
/*  flex-direction: column;*/
/*  !* 水平居中 *!*/
/*  align-items: center;*/
/*}*/
/*.el-form {*/
/*  display: flex;*/
/*  !* 纵向布局 *!*/
/*  flex-direction: column;*/
/*  !* 水平居中 *!*/
/*  align-items: center;*/
/*}*/
/*.el-form-item {*/
/*  width: 100%;*/
/*}*/
/* 输入框 */
.el-input /deep/ .el-input__inner{
  width: 60%;
  height: 40px;
  margin-left: 105px;
  margin-bottom: 10px;
  text-indent: 10px;
  border: 1px solid #fff;
  background-color: rgba(255, 255, 255, 0.3);
  border-radius: 120px;
  /* 增加磨砂质感 */
  backdrop-filter: blur(10px);
}
input:focus {
  /* 光标颜色 */
  color: #b0cfe9;

}

/* 聚焦时隐藏文字 */
input:focus::placeholder {
  opacity: 0;
}

/* 按钮盒子 */
.btn-box {
  display: flex;
  justify-content: center;
}

/* 按钮 */
button {
  width: 100px;
  height: 30px;
  margin: 0 7px;
  line-height: 30px;
  border: none;
  border-radius: 4px;
  background-color: #69b3f0;
  color: white;
}

/* 按钮悬停时 */
button:hover {
  /* 鼠标小手 */
  cursor: pointer;
  /* 透明度 */
  opacity: .8;
}

/* 按钮文字 */
.btn-box p {
  height: 30px;
  line-height: 30px;
  /* 禁止选中 */
  user-select: none;
  font-size: 14px;
  color: white;

}

.btn-box p:hover {
  cursor: pointer;
  border-bottom: 1px solid white;
}
</style>
