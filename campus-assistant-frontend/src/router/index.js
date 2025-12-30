import { createRouter, createWebHistory } from 'vue-router';

// 统一封装懒加载组件（复用+错误处理）
const lazyLoad = (view) => {
  return () => import(`@/views/${view}.vue`).catch(() => import('@/views/NotFoundView.vue'));
};

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: lazyLoad('HomeView'),
      meta: {
        title: '智能校园助手 - 首页',
      },
    },
    {
      path: '/chat',
      name: 'chat',
      component: lazyLoad('ChatView'),
      meta: {
        title: '智能校园助手 - 智能问答',
      },
    },
    {
      path: '/study-plan',
      name: 'studyPlan', // 小驼峰命名
      component: lazyLoad('StudyPlanView'),
      meta: {
        title: '智能校园助手 - 学习计划',
      },
    },
    // 新增课程管理路由
    {
      path: '/courses',
      name: 'courses',
      component: lazyLoad('CoursesView'),
      meta: {
        title: '智能校园助手 - 课程查询',
      },
    },
    // 新增工具页面路由
    {
      path: '/tools',
      name: 'tools',
      component: lazyLoad('ToolsView'),
      meta: {
        title: '智能校园助手 - 校园工具',
      },
    },
    {
      path: '/about',
      name: 'about',
      component: lazyLoad('AboutView'),
      meta: {
        title: '智能校园助手 - 关于我们',
      },
    },
    // 404 兜底路由（必选）
    {
      path: '/:pathMatch(.*)*',
      name: 'notFound',
      component: lazyLoad('NotFoundView'),
      meta: {
        title: '页面不存在 - 智能校园助手',
      },
    },
  ],
  // 可选：添加路由滚动行为（优化体验）
  scrollBehavior(to, from, savedPosition) {
    return savedPosition || { top: 0 };
  },
});

// 路由守卫：设置页面标题 + 异常处理
router.beforeEach((to, from, next) => {
  try {
    document.title = to.meta.title || '智能校园助手';
    next();
  } catch (error) {
    console.error('路由守卫执行异常:', error);
    next('/');
  }
});

export default router;
