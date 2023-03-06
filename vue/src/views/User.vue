<template>
<div>

  <div style="padding: 10px 0">
    <el-input style="width: 200px" suffix-icon=" el-icon-search" placeholder="请输入用户名搜索" v-model="username"></el-input>
    <el-input style="width: 200px" suffix-icon=" el-icon-user" placeholder="请输入昵称搜索" v-model="nickname"></el-input>
    <el-input style="width: 200px" suffix-icon=" el-icon-phone" placeholder="请输入电话搜索" v-model="phone"></el-input>
    <el-button class="ml-5" type="primary" @click="load">搜索</el-button>
    <el-button type="warning" @click="reset">重置</el-button>
  </div>
  <div style="margin: 10px 0">
    <el-button type="primary" @click="handleAdd" >新增<i class="el-icon-circle-plus-outline"></i></el-button>
    <el-popconfirm
        class="ml-5"
        confirm-button-text="是的"
        cancel-button-text="再想想"
        icon="el-icon-info"
        icon-color="red"
        title="确认批量删除这些用户吗"
        @confirm="delBatch"
    >
      <el-button type="danger" slot="reference" >批量删除<i class="el-icon-remove-outline"></i></el-button>
    </el-popconfirm>
    <el-upload
        :action="baseUrl  + '/user/import'" style="display: inline-block" :show-file-list="false" accept="xlsx"
        :on-success="handleExcelImportSuccess">
    <el-button type="primary" class="ml-5">导入<i class="el-icon-bottom"></i></el-button>
    </el-upload>
    <el-button type="primary" @click="exp" class="ml-5">导出<i class="el-icon-top"></i></el-button>
  </div>
  <el-table :data="tableData" border stripe highlight-current-row header-cell-class-name="headerBg" @selection-change="handleSelectionChange">
    <el-table-column type="selection" width="55"></el-table-column>
    <el-table-column prop="id" label="id" width="80" align="center"></el-table-column>
    <el-table-column prop="role" label="角色" width="120" align="center"></el-table-column>
    <el-table-column prop="username" label="用户名" width="140" align="center"></el-table-column>
    <el-table-column prop="password" label="密码" width="140" align="center"></el-table-column>
    <el-table-column prop="nickname" label="昵称" width="120" align="center"></el-table-column>
    <el-table-column prop="phone" label="联系电话" align="center"></el-table-column>
    <el-table-column label="操作" width="200px" align="center">
      <template slot-scope="scope">
        <el-button type="success" plain @click="handleEdit(scope.row)">编辑<i class="el-icon-edit"></i></el-button>
        <el-popconfirm
            class="ml-5"
            confirm-button-text="是的"
            cancel-button-text="再想想"
            icon="el-icon-info"
            icon-color="red"
            title="确认删除该用户吗"
            @confirm="del(scope.row.id)"
        >
          <el-button type="danger" slot="reference">删除<i class="el-icon-remove-outline"></i></el-button>
        </el-popconfirm>
      </template>
    </el-table-column>
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
  <el-dialog title="新增用户信息" :visible.sync="dialogFormVisible" width="30%">
    <el-form label-width="60px" >
      <el-form-item label="用户名" size="small" >
        <el-input v-model="form.username" disabled auto-complete="off"></el-input>
      </el-form-item>
      <!--角色-->
      <el-form-item label="角色">
        <el-select clearable v-model="form.role" placeholder="请选择角色" style="width: 100%">
          <el-option v-for="item in roles" :key="item.flag" :label="item.name" :value="item.flag"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="昵称" >
        <el-input v-model="form.nickname" auto-complete="off"></el-input>
      </el-form-item>
      <el-form-item label="电话" >
        <el-input v-model="form.phone" auto-complete="off"></el-input>
      </el-form-item>

    </el-form>
    <div slot="footer" class="dialog-footer">
      <el-button @click="cancel" >取 消</el-button>
      <el-button type="primary" @click="save">确 定</el-button>
    </div>
  </el-dialog>
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
        pageSize:2,
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
      this.request.get("/user/page",{
        params:{
          pageNum:this.pageNum,
          pageSize:this.pageSize,
          username:this.username,
          nickname:this.nickname,
          phone:this.phone
        }
      }).then(res => {
        this.tableData = res.data.records
        this.total = res.data.total
        //console.log(this.total)
        // this.$message.success("成功")
      })
      this.request.get("/role").then(res =>{
        this.roles = res.data
      })
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
