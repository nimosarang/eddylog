import {createRouter, createWebHistory} from 'vue-router'
// @ts-ignore
import HomeView from '../views/HomeView.vue'
// @ts-ignore
import WriteView from "../views/WriteView.vue";
// @ts-ignore
import ReadView from "../views/ReadView.vue";
// @ts-ignore
import EditView from "@/views/EditView.vue";

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: HomeView
    },
    {
      path: "/write",
      name: "write",
      component: WriteView
    },
    {
      path: "/read/:postId",
      name: "read",
      component: ReadView,
      props:true
    },
    {
      path: "/edit/:postId",
      name: "edit",
      component: EditView,
      props:true
    }
  ]
})

export default router
