<template>
  <div id="app">
    <!-- 路由视图容器 -->
    <router-view />
  </div>
</template>

<script setup>
// 不需要额外逻辑
</script>

<style>
/* 全局样式 */
#app {
  min-height: 100vh;
  font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
}

/* 移除Vue默认样式 */
#app a {
  text-decoration: none;
}

#app button {
  font-family: inherit;
}
</style>
