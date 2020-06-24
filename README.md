# market
2020高级软件开发能力训练
### 使用maven进行构建
首先clone到自己的仓库，然后将自己的项目clone到本地后使用IDEA打开。  
参考网上的资料进行maven配置
打开设置找到maven更改默认的maven工具地址到自行安装的maven路径  
后将群里下载的maven repo解压到设置的仓库位置。  
进入pom.xml，右侧应该会出现maven的标识，点击后打开maven工具栏。  
工具栏左上角刷新当前的maven依赖即可。  

### QA

* 如果maven依赖报错怎么办？
  * 修改到国内源
  * 自己百度
* Git克隆到本地以后怎么办？
  * 先自己修改一个地方，然后push一下，会出现登录窗口，使用自己的gitee账号登录即可。
* Git clone clone unable to access
  * 在clone之前加git config --system http.sslverify false