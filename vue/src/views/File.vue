<template>
  <div>
    <div style="margin: 10px 0">
      <el-input style="width: 200px" placeholder="请输入名称" suffix-icon="el-icon-search" v-model="name"></el-input>
      <el-button class="ml-5" type="primary" @click="load">搜索</el-button>
      <el-button type="warning" @click="reset">重置</el-button>
    </div>

    <div style="margin: 10px 0">
      <!--文件上传-->
      <el-upload :action= "baseUrl  + '/file/upload'" :headers="{'token':token.token}" :show-file-list="false" :on-success="handleFileUploadSuccess" style="display: inline-block">
        <el-button type="primary" class="ml-5">上传文件 <i class="el-icon-top"></i></el-button>
      </el-upload>
      <!--批量删除-->
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
    <!--表格 状态改变-->
    <el-table :data="results" border stripe :header-cell-class-name="'headerBg'"  @row-click="preview" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55"></el-table-column>
      <el-table-column prop="id" label="设备ID" width="150"></el-table-column>
      <el-table-column prop="name1" label="设备所属厂区"></el-table-column>
      <el-table-column prop="name" label="设备名" width="180"></el-table-column>
      <el-table-column prop="fileSize" label="设备大小"></el-table-column>

      <el-table-column label="下载">
        <template slot-scope="scope">
<!--          <el-button :href="'http://116.62.204.14:9000/test/' + scope.row.fileName" type="primary">删除</el-button>-->
          <a :href="FileURL  + '/test/' + scope.row.name" class="el-icon-download">下载</a>
        </template>
      </el-table-column>
      <!--开关-->
      <el-table-column label="查看">
        <template slot-scope="scope" >
          <!--状态改变-->
          <div class="demo-image__preview" v-if="scope.row.type === 'jpg' || scope.row.type === 'png'">
            <el-image style="width: 100px; height: 100px" :src="bucketURLFile + scope.row.url"></el-image>
          </div>
          <div v-else>
            {{scope.row.name}}
          </div>
        </template>
      </el-table-column>

      <el-table-column label="操作"  width="200" align="center">
        <template slot-scope="scope">
          <el-button type="primary" @click="deleteFile(scope.row.name)">删除</el-button>
<!--          <a :href="'http://116.62.204.14:8083/file/delete/' + scope.row.fileName + ''" >删除</a>-->
        </template>
      </el-table-column>

    </el-table>
    <!--分页-->
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
  name: "File",
  data() {
    return {
      tableData: [],
      name: '',
      multipleSelection: [],
      pageNum: 1,
      pageSize: 5,
      total: 0,
      imgSrc: '',
      imgList: [],
      results:[],
      bucketURLFile: process.env.VUE_APP_API_FILE  + '/test/',
      FileURL: process.env.VUE_APP_API_FILE,
      baseUrl: process.env.VUE_APP_API,
      previewImg: true,
      token: ''
    }
  },
  // created() {
  //   this.load()
  // },
  methods: {
    downLoad() {

    },
    deleteFile(val) {
      console.log('asdasd')
      this.request.get("/file/delete/" + val).then(res => {
        this.init()
      })
    },
    load() {
      this.request.get("/file/page", {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          name: this.name,
        }
      }).then(res => {
        this.results = res.data.records
        this.total = res.data.total
      })
    },
    //开关
    changeEnable(row) {
      this.request.post("/file/update", row).then(res => {
        if (res.code === '200') {
          this.$message.success("操作成功")
        }
      })
    },
    //单个删除
    del(e,index,list) {
      this.request.post(e.target.href).then(res => {
        if (res.data.code == 200) {
          this.$message('删除成功！');
          list.splice(index, 1);
          this.previewImg = false;
        } else {
          this.$message('删除失败！');
        }
      });
    },
    preview(row) {
      this.imgSrc = this.bucketURLFile + row.fileName;
      //window.open(this.imgSrc);
      //console.log(row.fileName)
      //console.log(this.imgSrc);
      this.previewImg = true;
    },
    //批量选择
    handleSelectionChange(val) {
      console.log(val)
      this.multipleSelection = val
    },
    //批量删除
    delBatch() {
      let ids = this.multipleSelection.map(v => v.id)  // [{}, {}, {}] => [1,2,3]
      this.request.post("/file/del/batch", ids).then(res => {
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
    //文件上传
    handleFileUploadSuccess(res) {
      console.log('res',res)
      this.load()
      this.init()
    },
    init() {
      this.load()
      console.log('init--')
      // this.request.get('/file/list').then(res => {
      //   this.results = res;
      //   //console.log(res);
      //   if (this.results.length == 0) {
      //     this.imgSrc = '';
      //     this.previewImg = false;
      //   } else {
      //     console.log('date',this.results)
      //     for (let i = 0; i < this.results.length; i++) {
      //       this.imgList.push(this.bucketURLFile + this.results[i].fileName);
      //       if (i == 0) {
      //         this.imgSrc = this.bucketURLFile + this.results[0].fileName;
      //       }
      //     }
      //   }
      // });
    }
  },
  mounted() {
     this.token = localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")) : null
    this.init();
  }
}
</script>

<style scoped>

</style>
