<template>
  <div class="about-container">
    <div class="page-header">
      <h1>关于我们</h1>
      <p>专注于智能学习辅助的一站式平台</p>
    </div>
    <div class="about-content">
      <div class="about-card">
        <h3>平台介绍</h3>
        <p>本平台提供智能问答、学习计划、课程推荐、学习工具等功能，助力学生高效学习。</p>
      </div>
      <div class="about-card">
        <h3>技术支持</h3>
        <p>基于Spring Cloud微服务架构 + Vue3前端框架开发，集成AI能力提升学习体验。</p>
      </div>
    </div>
    <button class="back-btn" @click="goBack">返回首页</button>
  </div>
</template>

<script setup>
import { useRouter } from 'vue-router';

const router = useRouter();
// 返回首页
const goBack = () => router.push('/');
</script>

<style scoped>
.about-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 40px 20px;
  text-align: center;
}
.page-header h1 {
  font-size: 2.5rem;
  color: #2c3e50;
  margin-bottom: 10px;
}
.page-header p {
  font-size: 1.2rem;
  color: #7f8c8d;
  margin-bottom: 40px;
}
.about-content {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
  gap: 20px;
  margin-bottom: 40px;
}
.about-card {
  padding: 30px;
  background-color: #f8f9fa;
  border-radius: 8px;
  text-align: left;
}
.about-card h3 {
  color: #34495e;
  margin-bottom: 15px;
  font-size: 1.2rem;
}
.about-card p {
  color: #666;
  line-height: 1.6;
}
.back-btn {
  padding: 10px 24px;
  background-color: #3498db;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 1rem;
}
.back-btn:hover {
  background-color: #2980b9;
}
</style>
