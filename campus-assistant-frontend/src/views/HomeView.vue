<template>
  <div class="home">
    <!-- 导航栏 -->
    <nav class="navbar">
      <div class="container">
        <div class="logo">
          <i class="fas fa-robot"></i>
          <span>智能校园助手</span>
        </div>
        <div class="nav-links">
          <router-link to="/" class="nav-link active">首页</router-link>
          <router-link to="/chat" class="nav-link">智能问答</router-link>
          <router-link to="/study-plan" class="nav-link">学习计划</router-link>
          <router-link to="/courses" class="nav-link">课程推荐</router-link>
          <router-link to="/tools" class="nav-link">学习工具</router-link>
          <router-link to="/about" class="nav-link">关于</router-link>
        </div>
        <div class="mobile-menu-btn" @click="toggleMobileMenu">
          <i class="fas fa-bars"></i>
        </div>
      </div>
    </nav>

    <!-- 移动端菜单 -->
    <div v-if="showMobileMenu" class="mobile-menu">
      <router-link to="/" class="mobile-link" @click="closeMobileMenu">首页</router-link>
      <router-link to="/chat" class="mobile-link" @click="closeMobileMenu">智能问答</router-link>
      <router-link to="/study-plan" class="mobile-link" @click="closeMobileMenu">学习计划</router-link>
      <router-link to="/courses" class="mobile-link" @click="closeMobileMenu">课程推荐</router-link>
      <router-link to="/tools" class="mobile-link" @click="closeMobileMenu">学习工具</router-link>
      <router-link to="/about" class="mobile-link" @click="closeMobileMenu">关于</router-link>
    </div>

    <!-- 主要内容 -->
    <main class="main-content">
      <!-- 英雄区域 -->
      <section class="hero-section">
        <div class="container">
          <div class="hero-content">
            <h1 class="hero-title">🤖 欢迎使用智能校园助手</h1>
            <p class="hero-subtitle">您的个性化学习伙伴，让大学生活更高效、更有趣！</p>

            <div class="hero-stats">
              <div class="stat-item">
                <div class="stat-number">1,247</div>
                <div class="stat-label">注册用户</div>
              </div>
              <div class="stat-item">
                <div class="stat-number">8,392</div>
                <div class="stat-label">已回答问题</div>
              </div>
              <div class="stat-item">
                <div class="stat-number">156</div>
                <div class="stat-label">可用课程</div>
              </div>
              <div class="stat-item">
                <div class="stat-number">3,485</div>
                <div class="stat-label">生成学习计划</div>
              </div>
            </div>
          </div>
        </div>
      </section>

      <!-- 快速开始卡片 -->
      <!-- 快速开始卡片部分修改点击事件 -->
      <section class="features-section">
        <div class="container">
          <h2 class="section-title">快速开始</h2>
          <div class="features-grid">
            <!-- 智能问答卡片 -->
            <div class="feature-card" @click="goToChat">
              <div class="feature-icon">
                <i class="fas fa-comment-dots"></i>
              </div>
              <h3 class="feature-title">智能问答</h3>
              <p class="feature-desc">向AI助手提问学习问题，获取专业解答和建议</p>
              <button class="feature-btn" @click.stop="goToChat">立即体验</button>
            </div>

            <!-- 其他卡片类似修改... -->
            <div class="feature-card" @click="goToPage('/study-plan')">
              <div class="feature-icon">
                <i class="fas fa-calendar-alt"></i>
              </div>
              <h3 class="feature-title">学习计划</h3>
              <p class="feature-desc">生成个性化学习方案，科学规划你的学习时间</p>
              <button class="feature-btn" @click.stop="goToPage('/study-plan')">开始规划</button>
            </div>

            <div class="feature-card" @click="goToPage('/courses')">
              <div class="feature-icon">
                <i class="fas fa-book"></i>
              </div>
              <h3 class="feature-title">课程推荐</h3>
              <p class="feature-desc">根据你的兴趣智能推荐优质课程，发现学习方向</p>
              <button class="feature-btn" @click.stop="goToPage('/courses')">查看推荐</button>
            </div>

            <div class="feature-card" @click="goToPage('/tools')">
              <div class="feature-icon">
                <i class="fas fa-magic"></i>
              </div>
              <h3 class="feature-title">学习工具</h3>
              <p class="feature-desc">生成学习场景图像，激发学习动力和创造力</p>
              <button class="feature-btn" @click.stop="goToPage('/tools')">使用工具</button>
            </div>
          </div>
        </div>
      </section>

      <!-- 页脚 -->
      <footer class="footer">
        <div class="container">
          <p>© 2025 智能校园助手 - KGC.CN</p>
          <p>Spring Cloud + Spring AI + Vue 3 + DeepSeek</p>
        </div>
      </footer>
    </main>
  </div>
</template>

<script setup>import { ref } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()
const showMobileMenu = ref(false)

const toggleMobileMenu = () => {
  showMobileMenu.value = !showMobileMenu.value
}

const closeMobileMenu = () => {
  showMobileMenu.value = false
}

// 🎯 修复的导航方法
const goToChat = () => {
  router.push('/chat')
  closeMobileMenu()
}

const goToPage = (path) => {
  router.push(path)
  closeMobileMenu()
}

// 移动端菜单导航
const mobileNavigate = (path) => {
  router.push(path)
  closeMobileMenu()
  }
</script>

<style scoped>
/* 导航栏样式 */
.navbar {
  background: white;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
  padding: 1rem 0;
  position: sticky;
  top: 0;
  z-index: 100;
}

.navbar .container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 1rem;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.logo {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  font-size: 1.5rem;
  font-weight: bold;
  color: #409EFF;
}

.nav-links {
  display: flex;
  gap: 2rem;
}

.nav-link {
  color: #606266;
  text-decoration: none;
  font-weight: 500;
  transition: color 0.3s;
  position: relative;
}

.nav-link:hover,
.nav-link.active {
  color: #409EFF;
}

.nav-link.active::after {
  content: '';
  position: absolute;
  bottom: -0.5rem;
  left: 0;
  width: 100%;
  height: 2px;
  background: #409EFF;
  border-radius: 1px;
}

.mobile-menu-btn {
  display: none;
  font-size: 1.5rem;
  color: #606266;
  cursor: pointer;
}

/* 移动端菜单 */
.mobile-menu {
  display: none;
  background: white;
  border-top: 1px solid #e4e7ed;
  padding: 1rem;
}

.mobile-link {
  display: block;
  padding: 0.75rem 1rem;
  color: #606266;
  text-decoration: none;
  border-bottom: 1px solid #f0f7ff;
  transition: all 0.3s;
}

.mobile-link:hover {
  background: #f0f7ff;
  color: #409EFF;
  padding-left: 1.5rem;
}

/* 英雄区域 */
.hero-section {
  background: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%);
  padding: 4rem 1rem;
  text-align: center;
}

.hero-title {
  font-size: 2.5rem;
  color: #303133;
  margin-bottom: 1rem;
}

.hero-subtitle {
  font-size: 1.2rem;
  color: #606266;
  margin-bottom: 3rem;
}

.hero-stats {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 2rem;
  max-width: 800px;
  margin: 0 auto;
}

.stat-item {
  background: white;
  padding: 1.5rem;
  border-radius: 12px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.stat-number {
  font-size: 2rem;
  font-weight: bold;
  color: #409EFF;
  margin-bottom: 0.5rem;
}

.stat-label {
  color: #606266;
  font-size: 0.9rem;
}

/* 功能区域 */
.features-section {
  padding: 4rem 1rem;
}

.container {
  max-width: 1200px;
  margin: 0 auto;
}

.section-title {
  font-size: 2rem;
  color: #303133;
  text-align: center;
  margin-bottom: 3rem;
}

.features-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: 2rem;
}

.feature-card {
  background: white;
  border-radius: 12px;
  padding: 2rem;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  transition: all 0.3s;
  cursor: pointer;
  text-align: center;
}

.feature-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 8px 25px rgba(0, 0, 0, 0.15);
}

.feature-icon {
  width: 70px;
  height: 70px;
  background: linear-gradient(45deg, #409EFF, #67C23A);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  font-size: 1.8rem;
  margin: 0 auto 1.5rem;
}

.feature-title {
  font-size: 1.3rem;
  color: #303133;
  margin-bottom: 1rem;
}

.feature-desc {
  color: #606266;
  margin-bottom: 1.5rem;
  line-height: 1.6;
}

.feature-btn {
  background: #409EFF;
  color: white;
  border: none;
  padding: 0.75rem 1.5rem;
  border-radius: 25px;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.3s;
}

.feature-btn:hover {
  background: #67C23A;
  transform: scale(1.05);
}

/* 页脚 */
.footer {
  background: white;
  padding: 2rem 1rem;
  text-align: center;
  border-top: 1px solid #e4e7ed;
  margin-top: 4rem;
}

.footer p {
  color: #909399;
  margin: 0.5rem 0;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .nav-links {
    display: none;
  }

  .mobile-menu-btn {
    display: block;
  }

  .mobile-menu {
    display: block;
  }

  .hero-title {
    font-size: 2rem;
  }

  .hero-subtitle {
    font-size: 1rem;
  }

  .features-grid {
    grid-template-columns: 1fr;
  }
}
</style>
