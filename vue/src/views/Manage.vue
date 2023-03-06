<template xmlns:el-pagination="http://www.w3.org/1999/html">
    <el-container style="min-height: 100vh; max-height: 100vh">
      <el-aside :width="sideWidth + 'px'" style="background-color: rgb(238, 241, 246); box-shadow: 2px 0 6px rgb(0 21 41 / 35%)">
     <Aside :isCollapse="isCollapse"  :logoTextShow="logoTextShow" />
      </el-aside>

      <el-container>
        <el-header style="background-color: skyblue;font-weight:bold;font-size: 18px; border-bottom: 1px solid #ccc; line-height: 60px">
          <Header :collapseBtnClass="collapseBtnClass" :collapse="collapse" :user="user"/>
        </el-header>

        <el-main style="background-color: #c9ddeb">
<!--          表示当前页面的子路由会在 router-view展示-->
         <router-view @refreshUser = "refreshUser" />

        </el-main>
      </el-container>
    </el-container>
</template>

<script>



import Aside from "../components/Aside";
import Header from "../components/Header";

export default {
  name: 'Home',
  data(){
    return {
      collapseBtnClass: 'el-icon-s-unfold',
      isCollapse:false,
      sideWidth: 200,
      logoTextShow: true,
      user:  {}
    }
  },
  components:{
    Aside,
    Header
  },
  created() {
    //从后台获取最新的数据
    this.refreshUser()
  },

  methods: {
    collapse(){//收缩按钮触发
      this.isCollapse = !this.isCollapse
      if (this.isCollapse){
        this.sideWidth = 64
        this.collapseBtnClass = "el-icon-s-unfold"
        this.logoTextShow = false
      }else {
        this.sideWidth = 200
        this.collapseBtnClass = "el-icon-s-fold"
        this.logoTextShow = true
      }
    },
    refreshUser() {
      this.user = localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")) : {}
    }


  }
}
</script>


