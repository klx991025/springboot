<template>
<div>

  <div style="padding: 10px 0">
    <el-input style="width: 200px" suffix-icon=" el-icon-search" placeholder="请输入处理人搜索" v-model="username"></el-input>
    <el-button class="ml-5" type="primary" @click="load">搜索</el-button>
    <el-button type="warning" @click="reset">重置</el-button>
  </div>
  <el-table :data="tableData" border stripe highlight-current-row header-cell-class-name="headerBg" @selection-change="handleSelectionChange">
    <el-table-column type="selection" width="55"></el-table-column>
    <el-table-column prop="id" label="id" width="80" align="center"></el-table-column>
    <el-table-column prop="username" label="设备名称" width="140" align="center"></el-table-column>
    <el-table-column prop="ip" label="告警等级" width="120" align="center"></el-table-column>
    <el-table-column prop="web" label="告警时间" align="center"></el-table-column>
    <el-table-column prop="os" label="状态" align="center"></el-table-column>
    <el-table-column prop="date" label="实际处理人" align="center"></el-table-column>
    <el-table-column prop="date" label="处理时间" align="center"></el-table-column>
  </el-table>
  <div style="padding: 10px 0">
    <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="pageNum"
        :page-sizes="[2, 5, 10, 20]"
        :page-size="pageSize"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total">
    </el-pagination>
  </div>
</div>
</template>

<script>
export default {
  name: "User",
  data(){
    return{
        tableData: [],
        total: 0,
        pageNum:1,
        pageSize:5,
        username:'',
        nickname:'',
        phone:'',
        form:{},
        dialogFormVisible:false,
        multipleSelection:[],
        roles:[],
      bucketURLFile: process.env.VUE_APP_API_FILE  + '/test/',
      FileURL: process.env.VUE_APP_API_FILE,
      baseUrl: process.env.VUE_APP_API
    }
  },
  created() {
    this.load()
    },
  methods: {
    load(){
      //请求分页查询
      this.request.get("/user/pageAlarmLog",{
        params:{
          pageNum:this.pageNum,
          pageSize:this.pageSize,
          username:this.username
        }
      }).then(res => {
        this.tableData = res.data.records
        this.total = res.data.total
        //console.log(this.total)
        // this.$message.success("成功")
      })
      // this.request.get("/role").then(res =>{
      //   this.roles = res.data
      // })
    },
    reset(){
      this.username=""
      this.nickname=""
      this.phone=""
      this.load()
    },
    save(){
      this.request.post("/user", this.form).then(res => {
        if(res.data){
          this.$message.success("保存成功")
          this.dialogFormVisible = false
          this.load()
        }else{
          this.$message.error("保存失败")
        }
      })
    },
    handleAdd(){
      this.dialogFormVisible = true
      this.form = {}
    },
    handleEdit(row){
      this.form = Object.assign({},row)
      this.dialogFormVisible = true
    },
    del(id){
      this.request.delete("/user/" + id).then(res => {
        if(res.data){
          this.$message.success("删除成功")
          this.load()
        }else{
          this.$message.error("删除失败")
        }
      })
    },
    cancel(){
      this.dialogFormVisible = false
      this.load()
    },
    delBatch(){
      let ids = this.multipleSelection.map(v => v.id)
      this.request.post("/user/del/batch" , ids).then(res => {
        if(res.data){
          this.$message.success("删除成功")
          this.load()
        }else{
          this.$message.error("删除失败")
        }
      })
    },
    handleSelectionChange(val){
      console.log(val)
      this.multipleSelection = val
    },
    handleSizeChange(pageSize) {
      console.log(pageSize)
      this.pageSize = pageSize
      this.load()
    },
    handleCurrentChange(pageNum) {
      console.log(pageNum)
      this.pageNum = pageNum
      this.load()
    },
    exp(){
      window.open(baseUrl  + "/user/export")
    },
    handleExcelImportSuccess(){
      this.$message.success("文件导入成功")
      this.load()
    }
  }
}
</script>
<style scoped>
/*表头样式--*/
>>> .headerBg {
  background: #e1fae5 !important;
}
</style>
