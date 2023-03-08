import Vue from 'vue'
import VueRouter from 'vue-router'
import store from "../store";
import ElementUI from "element-ui";


Vue.use(VueRouter)

const routes = [
  {
    path: '/login',
    name: 'Login',

    component: () => import('../views/Login.vue')
  },
  {
    path: '/register',
    name: 'Register',

    component: () => import('../views/Login.vue')
  },
  {
    path: '*',
    name: '404',

    component: () => import('../views/404.vue')
  },
]

const router = new VueRouter({
  // mode: 'history',
  base: process.env.BASE_URL,
  routes
})

// 注意：刷新页面会导致页面路由重置
export const setRoutes = () => {
  const menus = JSON.parse(localStorage.getItem("menus"))
  //登入的时候设置过一次，刷新也设置，
  // 获取当前的路由对象名称数组
  const currentRouteNames = router.getRoutes().map(v => v.name)
  //  如果没有以前的路由，就添加
  if (!currentRouteNames.includes('Manage')) {
    if (menus) {
      // 拼装动态路由
      const manageRoute = { path: '/', name: 'Manage', component: () => import('../views/Manage.vue'), redirect: "/home", children: [
          { path: 'person', name: '个人信息', component: () => import('../views/Person.vue')},
        ] }

      menus.forEach(item => {
        // 当且仅当path不为空的时候才去设置路由
        if (item.path) {
          //设置二级路由
          let itemMenu = { path: item.path.replace("/", ""), name: item.name, component: () => import('../views/' + item.pagePath + '.vue'), meta:{title: item.name}}
          manageRoute.children.push(itemMenu)
        } else if(item.children.length) {
          //设置三级路由u
          item.children.forEach(item => {
            if (item.path) {
              let itemMenu = { path: item.path.replace("/", ""), name: item.name, component: () => import('../views/' + item.pagePath + '.vue'), meta:{title: item.name}}
              manageRoute.children.push(itemMenu)
            }
          })
        }
      })


      // 动态添加到现在的路由对象中去
      router.addRoute(manageRoute)
    }

  }
}

// 刷新页面 重置路由我就再set一次路由
setRoutes()


//全局前置守卫：初始化时执行、每次路由切换前执行
router.beforeEach((to, from, next) => {
  localStorage.setItem("currentPathName", to.name)  // 设置当前的路由名称，为了在Header组件中去使用
  store.commit("setPath")  // 触发store的数据更新
  //不是去登入页也不是去注册页
  if(to.name !== "Login" && to.name !== "Register"){
    let userJson = localStorage.getItem("user")
    if (!userJson) {
      ElementUI.Message.error("请登入")
      next("/login")
    }
  }

  next()  // 放行路由
})


//全局后置路由守卫————初始化的时候被调用、每次路由切换之后被调用
router.afterEach((to,from)=>{
  document.title = to.meta.title || '登录'
})


export default router
