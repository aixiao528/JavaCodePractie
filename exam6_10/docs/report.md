# 项目报告

## 一、数据库表设计方案

### 1. 学生表 `students`

| 字段名 | 类型 | 说明 |
|---|---|---|
| `id` | `INT` | 学生编号，主键 |
| `name` | `VARCHAR(50)` | 学生姓名 |
| `major` | `VARCHAR(50)` | 专业 |

### 2. 课程表 `courses`

| 字段名 | 类型 | 说明 |
|---|---|---|
| `id` | `INT` | 课程编号，主键 |
| `name` | `VARCHAR(100)` | 课程名 |
| `credits` | `INT` | 学分 |
| `capacity` | `INT` | 课程容量 |
| `selected_count` | `INT` | 当前已选人数 |

### 3. 选课表 `enrollments`

| 字段名 | 类型 | 说明 |
|---|---|---|
| `id` | `INT` | 自增主键 |
| `student_id` | `INT` | 学生编号，外键 |
| `course_id` | `INT` | 课程编号，外键 |
| `grade` | `DECIMAL(5,2)` | 成绩，可为空 |

说明：

- 通过 `UNIQUE(student_id, course_id)` 防止重复选课
- 通过外键保证学生、课程与选课记录的一致性
- 通过 `selected_count` 与 `capacity` 实现课程名额控制

## 二、测试数据生成

项目已提供测试脚本：

- 建表脚本：`sql/schema.sql`
- 初始化数据：`sql/data.sql`

初始数据特点：

- 共 6 名学生
- 共 4 门课程
- 已预置 4 条选课记录
- 课程容量设置不同，方便演示名额不足和事务回滚

## 三、功能截图位置说明

本项目为控制台程序，运行后可依次演示以下功能：

1. 查看课程列表
2. 单条选课
3. 退课
4. 转课给其他同学
5. 批量选课
6. 联表查询
7. 事务回滚演示

查看课程列表：

![image-20260610165956931](C:\Users\YuMo\AppData\Roaming\Typora\typora-user-images\image-20260610165956931.png)



学生退课

![image-20260610170020119](D:\MySQL_learn\exp\lab6\redbase\image-20260610170020119.png)



学生转课：

![image-20260610170107002](C:\Users\YuMo\AppData\Roaming\Typora\typora-user-images\image-20260610170107002.png)



教务批量选课：
![image-20260610170220960](C:\Users\YuMo\AppData\Roaming\Typora\typora-user-images\image-20260610170220960.png)



查询学生全部已选课程：

![image-20260610170246040](C:\Users\YuMo\AppData\Roaming\Typora\typora-user-images\image-20260610170246040.png)



模拟异常：

![image-20260610170314615](C:\Users\YuMo\AppData\Roaming\Typora\typora-user-images\image-20260610170314615.png)![image-20260610170325794](C:\Users\YuMo\AppData\Roaming\Typora\typora-user-images\image-20260610170325794.png)

## 四、功能实现方案

### 采用的技术

* 连接数据库：

​	项目采用 Java 21 + JDBC + MySQL 8 + 控制台交互 ，数据库连接通过 ConnectionManager 和 DatabaseConfig 统一管理

* 配置：

   数据库 URL、账号、密码、驱动类从 db.properties 读取，读取失败时抛出 IllegalStateException

* 控制台交互：

  统一菜单放在main里

* 负责SQL执行的：

  DAO相关代码可见，只负责SQL执行，如CourseDao，EnrollmentDao文件

* 边界条件处理：
  * 先查存在性，学生与课程存在不存在
  * 再查是否重复选课，业务逻辑上有没有错误
  * 然后查课程是否埋怨，更新数据库再+条件限制
  * 不嫩那个转课给自己
* 异常处理
  * 输入边界在主入口的NumberFormatException
  * 业务逻辑上的错误全部归咎于BusinessException，在这个类里![image-20260610172353691](C:\Users\YuMo\AppData\Roaming\Typora\typora-user-images\image-20260610172353691.png)
  * 数据访问错误，数据库方面的错误归咎于SQLException![image-20260610172451169](C:\Users\YuMo\AppData\Roaming\Typora\typora-user-images\image-20260610172451169.png)在main里面捕获

### 1. 分层结构

- `config`：数据库配置
- `util`：JDBC 连接工具
- `dao`：数据库访问层
- `service`：业务层与事务控制
- `cache`：课程缓存
- `model`：实体对象
- `exception`：业务异常

### 2. 单条选课

![image-20260610170855294](C:\Users\YuMo\AppData\Roaming\Typora\typora-user-images\image-20260610170855294.png)

实现步骤：

1. 校验学生与课程是否存在
2. 校验学生是否已选该课程
3. 校验课程剩余名额
4. 插入选课记录
5. 更新课程已选人数

### 3. 批量选课

![image-20260610171033283](C:\Users\YuMo\AppData\Roaming\Typora\typora-user-images\image-20260610171033283.png)

实现步骤：

1. 开启手动事务
2. 锁定课程记录
3. 先检查课程余量是否足够整批学生
4. 遍历校验学生是否存在、是否已选
5. 全部通过后再逐条插入选课记录
6. 任一环节出错则整体回滚

### 4. 转课事务

![image-20260610171016993](C:\Users\YuMo\AppData\Roaming\Typora\typora-user-images\image-20260610171016993.png)

实现步骤：

1. 关闭自动提交，开启手动事务
2. 使用 `SELECT ... FOR UPDATE` 锁定待转让课程，避免转课过程中名额被并发操作抢占
3. 确认转出学生已选该课程，且接收学生尚未选该课程
4. 先给接收学生插入该课程的选课记录，保证名额先被接收方占用
5. 再删除转出学生的原选课记录，完成同一门课的转让
6. 全部成功后提交事务
7. 任意步骤抛异常则整体回滚

### 5. 多表联查

通过 `enrollments` 与 `courses` 联表，按学生 ID 查询：

- 课程编号
- 课程名称
- 学分
- 成绩

### 6. 课程缓存

为减少频繁查询数据库，本项目增加了课程列表缓存：

- 首次读取课程列表时查询数据库并写入缓存
- 每次选课、退课、转课、批量选课后主动刷新缓存
- 菜单中提供手动刷新缓存选项

## 五、AI 沟通过程

本项目开发中，AI 主要用于以下辅助工作：

- 梳理需求并拆解为功能模块
- 设计 Java 分层结构和 JDBC DAO 写法
- 生成数据库建表脚本与测试数据
- 补充“课程转让给其他同学”的事务处理与异常回滚逻辑
- 生成 README 和答辩文档草稿
- 辅助检查代码结构

![image-20260610170457943](C:\Users\YuMo\AppData\Roaming\Typora\typora-user-images\image-20260610170551142.png)

![屏幕截图 2026-06-10 170514](C:\Users\YuMo\Pictures\Screenshots\屏幕截图 2026-06-10 170514.png)

## ![屏幕截图 2026-06-10 170442](C:\Users\YuMo\Pictures\Screenshots\屏幕截图 2026-06-10 170442.png)



## 六、总结

本项目已完成以下核心要求：

- 单条选课
- 批量选课
- 转课事务与异常回滚
- 多表联查
- 控制台交互
- DAO 分层结构
- 数据库脚本与测试数据

本项目还完成了以下扩展内容：

- 课程列表缓存
- PowerShell 编译运行脚本
- `pom.xml` 依赖声明，方便 IDEA 直接导入
- 独立“事务回滚演示”菜单，便于现场答辩展示

## 七、可继续扩展的方向

- 增加登录权限区分学生端和教务端
- 增加分页查询和选课日志
- 使用连接池优化 JDBC 性能
- 在高并发下引入乐观锁或消息队列削峰
- 使用 GUI 或 Web 前端替代控制台界面
