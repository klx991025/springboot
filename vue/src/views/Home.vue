<template>
  <div>
    <!--统计信息-->
    <el-row :gutter="10" style="margin-bottom: 60px">
      <el-col :span="6">
        <el-card style="color: #409EFF">
          <div><i class="el-icon-user-solid" /> 用户总数</div>
          <div style="padding: 10px 0; text-align: center; font-weight: bold">
            100
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card style="color: #F56C6C">
          <div><i class="el-icon-money" /> 运行设备总量</div>
          <div style="padding: 10px 0; text-align: center; font-weight: bold">
           10
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card style="color: #67C23A">
          <div><i class="el-icon-bank-card" /> 历史报警数量</div>
          <div style="padding: 10px 0; text-align: center; font-weight: bold">
           30
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card style="color: #E6A23C">
          <div><i class="el-icon-s-shop" /> 通道总数</div>
          <div style="padding: 10px 0; text-align: center; font-weight: bold">
            20
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-row >
      <el-col :span="8">
        <div id="main" style="width: 400px; height: 400px"></div>
      </el-col>
      <el-col :span="8">
        <div id="pie" style="width: 400px; height: 400px"></div>
      </el-col>
      <el-col :span="8">
        <div id="line" style="width: 400px; height: 400px"></div>
      </el-col>
    </el-row>





  </div>
</template>

<script>
import * as echarts from 'echarts'

export default {
  name: "Home",
  data(){
    return{

    }
  },
  mounted() {//页面元素渲染之后再触发
    //心形图
    var chartDom = document.getElementById('main');
    var myChart = echarts.init(chartDom);
    //饼图
    var pieDom = document.getElementById('pie');
    var pieChart = echarts.init(pieDom);
    //折线图
    var lineChartDom = document.getElementById('line');
    var lineChart = echarts.init(lineChartDom);


    const data = [];
    for (let i = 0; i <= 100; i++) {
      let theta = (i / 100) * 360;
      let r = 5 * (1 + Math.sin((theta / 180) * Math.PI));
      data.push([r, theta]);
    }
    var option = {
      // title: {
      //   text: '不明觉厉'
      // },
      legend: {
        data: ['line']
      },
      polar: {},
      tooltip: {
        trigger: 'axis',
        axisPointer: {
          type: 'cross'
        }
      },
      angleAxis: {
        type: 'value',
        startAngle: 0
      },
      radiusAxis: {},
      series: [
        {
          coordinateSystem: 'polar',
          name: 'line',
          type: 'line',
          data: data
        }
      ]
    };
    myChart.setOption(option);

    //饼图
    var pieOption = {
      tooltip: {
        trigger: 'item'
      },
      legend: {
        top: '5%',
        left: 'center'
      },
      series: [
        {
          type: 'pie',
          radius: ['40%', '70%'],
          avoidLabelOverlap: false,
          itemStyle: {
            borderRadius: 10,
            borderColor: '#fff',
            borderWidth: 2
          },
          label: {
            show: false,
            position: 'center',
            formatter: '{d}%({c})'
          },
          emphasis: {
            label: {
              show: true,
              fontSize: '40',
              fontWeight: 'bold'
            }
          },
          labelLine: {
            show: false

          },
          data: []//
        }
      ]
  };


    var lineOption = {
      title: {
        text: '各季度注册会员数量',
        subtext: '趋势图',
        left: 'center'
      },
      xAxis: {
        type: 'category',
        data: ["第一季度","第二季度","第三季度","第四季度"]
      },
      yAxis: {
        type: 'value'
      },
      series: [
        {
          data: [],
          type: 'line'
        },
        {
          data: [],
          type: 'bar'
        }
      ]
    };

    this.request.get("/echarts/members").then(res => {
      //lineOption.xAxis.data = res.data.x
      lineOption.series[0].data = res.data
      lineOption.series[1].data = res.data
      //数据准备完毕之后再set
      lineChart.setOption(lineOption);

      pieOption.series[0].data=[
        {name:"第一季度",value: res.data[0]},
        {name:"第二季度",value: res.data[1]},
        {name:"第三季度",value: res.data[2]},
        {name:"第四季度",value: res.data[3]}
      ]
      pieChart.setOption(pieOption);
    })


}
}
</script>

<style scoped>

</style>