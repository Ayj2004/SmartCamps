# SmartCamps

SmartCamps

# 1. 查看修改的文件（可选，确认修改范围）

git status

# 2. 暂存所有修改（. 表示所有文件，也可指定单个文件如 git add xxx.java）

git add .

# 3. 提交到本地仓库（必须写有意义的提交信息）

git commit -m "修复 user-service 登录接口 bug/新增用户查询接口"

# 4. 推送到远程（已建立追踪，直接 git push 即可）

git push

# 拉取远程 main 分支的最新内容到本地 main 分支

git pull

# 查看本地分支（\* 表示当前分支）

git branch

# 查看提交历史（按时间倒序，简洁版）

git log --oneline

# 回滚到上一个提交（保留修改，仅撤销 commit）

git reset --soft HEAD^

# 强制回滚到指定提交（会删除后续修改，风险高）

git reset --hard 提交 ID（如 edbd7c3）
