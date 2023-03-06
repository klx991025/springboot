<template>
  <div>
    <div style="margin: 10px 0">
      <el-input style="width: 200px" placeholder="请输入名称" suffix-icon="el-icon-search" v-model="name"></el-input>
      <el-button class="ml-5" type="primary" @click="load">搜索</el-button>
      <el-button type="warning" @click="reset">重置</el-button>
    </div>

    <div style="margin: 10px 0">
      <el-button type="primary" @click="handleAdd">新增 <i class="el-icon-circle-plus-outline"></i></el-button>
      <el-popconfirm
          class="ml-5"
          confirm-button-text='确定'
          cancel-button-text='我再想想'
          icon="el-icon-info"
          icon-color="red"
          title="您确定批量删除这些数据吗？"
          @confirm="delBatch"
      >
        <el-button type="danger" slot="reference">批量删除 <i class="el-icon-remove-outline"></i></el-button>
      </el-popconfirm>
    </div>

    <el-table :data="tableData" border stripe :header-cell-class-name="'headerBg'"  @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55"></el-table-column>
      <el-table-column prop="id" label="ID" width="80"></el-table-column>
      <el-table-column prop="name" label="名称"></el-table-column>
      <el-table-column prop="flag" label="唯一标识"></el-table-column>
      <el-table-column prop="description" label="描述"></el-table-column>

      <el-table-column label="操作"  width="280" align="center">
        <!--菜单               -->
        <template slot-scope="scope">
          <!--分配菜单-->
          <el-button type="info" @click="selectMenu(scope.row)">分配菜单 <i class="el-icon-menu"></i></el-button>
          <el-button type="success" @click="handleEdit(scope.row)">编辑 <i class="el-icon-edit"></i></el-button>
          <el-popconfirm
              class="ml-5"
              confirm-button-text='确定'
              cancel-button-text='我再想想'
              icon="el-icon-info"
              icon-color="red"
              title="您确定删除吗？"
              @confirm="del(scope.row.id)"
          >
            <el-button type="danger" slot="reference">删除 <i class="el-icon-remove-outline"></i></el-button>
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
    <!--修改删除用户-->
    <el-dialog title="角色信息" :visible.sync="dialogFormVisible" width="30%" >
      <el-form label-width="80px" size="small">
        <el-form-item label="名称">
          <el-input v-model="form.name" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="描述">
          <el-input v-model="form.description" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="唯一标识">
          <el-input v-model="form.flag" autocomplete="off"></el-input>
        </el-form-item>
      </el-form>

      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="save">确 定</el-button>
      </div>
    </el-dialog>

    <!--分配菜单-->
    <el-dialog title="菜单分配" :visible.sync="menuDialogVis" width="30%">
      <!--
          :props="props" 指定标题名和孩子名
          :data="menuData" 菜单的数据
          show-checkbox  显示网格
          node-key="id"  菜单的id
          :default-expanded-keys="[1]" 展开的节点
          :default-checked-keys="[4]"  选中的节点
          @check-change="handleCheckChange 处理变化的方法

      -->
      <el-tree
          :props="props"
          :data="menuData"
          ref="tree"
          show-checkbox
          node-key="id"
          :default-expanded-keys="expends"
          :default-checked-keys="checks"
          :check-strictly="true">
        <!--图标-->
        <span class="custom-tree-node" slot-scope="{ node, data }">
            <span><i :class="data.icon"></i> {{ data.name }}</span>
         </span>

      </el-tree>
      <div slot="footer" class="dialog-footer">
        <el-button @click="menuDialogVis = false">取 消</el-button>
        <el-button type="primary" @click="saveRoleMenu">确 定</el-button>
      </div>
    </el-dialog>

  </div>
</template>

<script>
export default {
  name: "Role",
  data() {
    return {
      tableData: [],
      total: 0,
      pageNum: 1,
      pageSize: 10,
      name: "",
      form: {},
      dialogFormVisible: false,
      //菜单的数据
      menuDialogVis: false,
      multipleSelection: [],
      //菜单list
      menuData: [],
      //配置项
      props: {
        label: 'name',
        children: "children"
      },
      //展开的节点
      expends: [],
      //选中的节点
      checks: [],
      //当前选择菜单的角色id
      roleId: 0,
      //用户的角色
      roleFlag: '',
      ids: []
    }


  },
  created() {
    this.load()
  },
  methods: {
    //初始化
    load() {
      //请求角色数据
      this.request.get("/role/page", {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          name: this.name,
        }
      }).then(res => {
        this.tableData = res.data.records
        this.total = res.data.total
      })

      this.request.get("/menu/ids").then(r => {
        this.ids = r.data
      })
    },
    //保存、修改
    save() {
      this.request.post("/role", this.form).then(res => {
        if (res.code === '200') {
          this.$message.success("保存成功")
          this.dialogFormVisible = false
          this.load()
        } else {
          this.$message.error("保存失败")
        }
      })
    },
    //新增
    handleAdd() {
      this.dialogFormVisible = true
      this.form = {}
    },
    //编辑
    handleEdit(row) {
      this.form = JSON.parse(JSON.stringify(row))
      this.dialogFormVisible = true
    },
    //删除
    del(id) {
      this.request.delete("/role/" + id).then(res => {
        if (res.code === '200') {
          this.$message.success("删除成功")
          this.load()
        } else {
          this.$message.error("删除失败")
        }
      })
    },
    //表格改变
    handleSelectionChange(val) {
      console.log(val)
      this.multipleSelection = val
    },
    //批量删除
    delBatch() {
      let ids = this.multipleSelection.map(v => v.id)  // [{}, {}, {}] => [1,2,3]
      this.request.post("/role/del/batch", ids).then(res => {
        if (res.code === '200') {
          this.$message.success("批量删除成功")
          this.load()
        } else {
          this.$message.error("批量删除失败")
        }
      })
    },
    //重置
    reset() {
      this.name = ""
      this.load()
    },
    //分页
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
    //分配菜单
    async selectMenu(role) {
      this.roleId = role.id
      this.roleFlag = role.flag
      // 请求菜单数据-异步改成同步
      await this.request.get("/menu").then(res => {
        this.menuData = res.data
        // 把后台返回的菜单数据处理成 id数组
        this.expends = this.menuData.map(v => v.id)
      })

      //请求角色id和菜单列表的关系
      this.request.get("/role/roleMenu/" + this.roleId).then(res => {
        this.menuDialogVis = true
        this.checks = res.data
        //解决父id选中，子id有没有都会选中
        this.ids.forEach(id => {
          //不含有ids,取消选中
          if (!this.checks.includes(id)) {
            //未来元素
            this.$nextTick(()=>{
              this.$refs.tree.setChecked(id, false)
            })
          }
        })


        /*   请求里的请求本身就是异步的
                // //获取当前的树id
                this.request.get("/menu/ids").then(r => {
                  const ids = r.data
                  //解决父id选中，子id有没有都会选中
                  ids.forEach(id => {
                    //不含有ids,取消选中
                    if (!this.checks.includes(id)) {
                      this.$refs.tree.setChecked(id, false)
                    }
                  })

                })*/


      })
    },
    //保存角色和菜单的关系
    saveRoleMenu() {
      //获取子节点
      const zi = this.$refs.tree.getCheckedKeys();
      //获取半选的父节点
      const fu = this.$refs.tree.getHalfCheckedKeys();
      //合并父子节点
      const fuZi = fu.concat(zi);
      //路径              请求体
      this.request.post("/role/roleMenu/" + this.roleId, fuZi).then(res => {

        if (res.code === '200') {
          this.$message.success("绑定成功")
          this.menuDialogVis = false

          // 操作管理员角色后需要重新登录
          if (this.roleFlag === 'ROLE_ADMIN') {
            this.$store.commit("logout")
          }

        } else {
          this.$message.error(res.msg)
        }
      })
    },

  }
}
</script>


<style>
.headerBg {
  background: #eee!important;
}
</style>
