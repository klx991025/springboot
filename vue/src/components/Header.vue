<template>
  <div style="line-height: 60px;display: flex">
    <div style="flex: 1;font-size: 20px">
      <span :class="collapseBtnClass" style="cursor: pointer; font-size:18px" @click="collapse"></span>
        <el-breadcrumb separator="/" style="display: inline-block; margin-left: 10px">
          <el-breadcrumb-item :to="'/'">首页</el-breadcrumb-item>
          <el-breadcrumb-item >{{ currentPathName }}</el-breadcrumb-item>
        </el-breadcrumb>
    </div>
  <el-dropdown style="width: 70px ; cursor: pointer">
    <span>{{ user.nickname }}</span>
    <i class="el-icon-arrow-down" style="margin-left: 5px"></i>
    <el-dropdown-menu slot="dropdown" style="width: 100px; text-align: center">
      <router-link to="/person">
        <el-dropdown-item style="font-size: 12px;padding: 5px 0">个人信息</el-dropdown-item>
      </router-link>
      <router-link to="/login" style="text-decoration: none">
        <el-dropdown-item style="font-size: 12px;padding: 5px 0">
          <span @click="logout">退出</span>
        </el-dropdown-item>
      </router-link>
    </el-dropdown-menu>
  </el-dropdown>
  </div>
</template>

<script>
export default {
  name: "Header",
  props:{
    collapseBtnClass: String,
    collapse: Function,
    user:Object
  },
  computed:{
    currentPathName(){
      return this.$store.state.currentPathName;
    }
  },
  watch:{
    currentPathName(newVal,oldVal){
      console.log(newVal)
    }
  },
  data()  {
    return{

    }
  },
  methods:{
    logout(){
      this.$store.commit("logout")
      this.$message.success("退出成功")
    }
  }
}
</script>

<style scoped>
a {
  text-decoration: none;
}
</style>