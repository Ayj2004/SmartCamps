<template>
  <div class="not-found-container">
    <h1>404 - 页面不存在</h1>
    <p>你访问的页面走丢了 😯</p>
    <el-button type="primary" @click="goHome">返回首页</el-button>
  </div>
</template>

<script setup>
import { useRouter } from 'vue-router';
const router = useRouter();

const goHome = () => {
  router.push('/');
};
</script>

<style scoped>
.not-found-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 100vh;
  gap: 20px;
  font-size: 18px;
}

.not-found-container h1 {
  font-size: 60px;
  color: #e54d42;
  margin: 0;
}

.el-button {
  padding: 10px 20px;
  font-size: 16px;
}
</style>
