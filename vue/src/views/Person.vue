<template>
  <el-card style="width: 500px;" >
  <el-form label-width="60px" >
    <el-form-item label="用户名"  size="small" >
      <el-input v-model="form.username" disabled auto-complete="off"></el-input>
    </el-form-item>
    <el-form-item label="昵称" >
      <el-input v-model="form.nickname" auto-complete="off"></el-input>
    </el-form-item>
    <el-form-item label="电话" >
      <el-input v-model="form.phone" auto-complete="off"></el-input>
    </el-form-item>
    <el-form-item>
    <el-button type="primary" @click="save">确 定</el-button>
  </el-form-item>
  </el-form>

  </el-card>
</template>

<script>
export default {
name: "Person",
  data(){
    return {
      form:{},
      user: localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")) : {}
    }
  },
  methods:{
    async getUser() {
      return (await this.request.get("/user/username/" + this.user.username)).data
    },
    save(){
      this.request.post("/user", this.form).then(res => {

        if (res.code === '200') {
          this.$message.success("保存成功")

          // 更新浏览器存储的用户信息
          this.getUser().then(res => {
            res.token = JSON.parse(localStorage.getItem("user")).token
            localStorage.setItem("user", JSON.stringify(res))
            //person=>Manage(子传父)=>Header(父传子)
            this.$emit("refreshUser")
          })


        } else {
          this.$message.error("保存失败")
        }
      })
    },


  },
  created() {
  this.request.get("/user/username/" + this.user.username).then(res => {
    if(res.code === '200'){
      this.form = res.data
    }
  })
  }
}
</script>

<style scoped>

</style>