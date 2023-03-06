<template>
  <el-menu :default-openeds="opens" style="min-height: 100vh; overflow-x:hidden"
           background-color= "rgb(48 65 86)"
           text-color="#fff"
           active-text-color="#ffd04b"
           :collapse-transition="false"
           :collapse="isCollapse"
           router
  >

    <div style="height: 60px;line-height: 60px;text-align: center">
      <img src="../assets/logo.png" alt="" style="width: 20px;position: relative;top:5px;" >
      <b style="color: white" v-show="logoTextShow"> 设备监测</b>
    </div>
    <div v-for="item in menus" :key="item.id">
      <div v-if="item.path">
        <el-menu-item :index="item.path">
          <i :class="item.icon"></i>
          <span slot="title">{{ item.name }}</span>
        </el-menu-item>
      </div>
      <div v-else>
        <el-submenu :index="item.id + ''">
          <template slot="title">
            <i :class="item.icon"></i>
            <span slot="title">{{ item.name }}</span>
          </template>
          <div  v-for="subItem in item.children" :key="subItem.id">
            <el-menu-item :index="subItem.path">
              <i :class="subItem.icon"></i>
              <span slot="title">{{ subItem.name }}</span>
            </el-menu-item>
          </div>
        </el-submenu>
      </div>
    </div>




  </el-menu>
</template>
<script>
export default {
  name: "Aside",
  //定义子组件接收变量
  props: {
    // 菜单栏显示隐藏
    isCollapse: Boolean,
    //文本显示隐藏
    logoTextShow: Boolean
  },
  data() {
    return {
      //菜单信息
      menus: localStorage.getItem("menus") ? JSON.parse(localStorage.getItem("menus")) : [],
      //获取一级菜单的id数组，用于展开
      opens: localStorage.getItem("menus") ? JSON.parse(localStorage.getItem("menus")).map(v => v.id + '') : []
    }
  }
}
</script>
<style scoped>

</style>