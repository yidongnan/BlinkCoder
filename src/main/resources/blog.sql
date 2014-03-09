/*
Navicat MySQL Data Transfer

Source Server         : MySQL
Source Server Version : 50616
Source Host           : localhost:3306
Source Database       : blog

Target Server Type    : MYSQL
Target Server Version : 50616
File Encoding         : 65001

Date: 2014-03-09 21:39:49
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for blog
-- ----------------------------
DROP TABLE IF EXISTS `blog`;
CREATE TABLE `blog` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `title` varchar(255) DEFAULT '',
  `global_url` varchar(255) DEFAULT NULL COMMENT '全局的url',
  `catalog` int(11) DEFAULT '1',
  `content` text,
  `desc` varchar(255) DEFAULT '' COMMENT '简述',
  `update_time` datetime DEFAULT NULL,
  `read_count` int(11) DEFAULT '0',
  `comment_count` int(11) DEFAULT '0',
  `type` int(1) DEFAULT '0' COMMENT '0-普通 1-置顶',
  PRIMARY KEY (`id`),
  UNIQUE KEY `globalurl` (`global_url`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of blog
-- ----------------------------
INSERT INTO `blog` VALUES ('1', '博客设计说明', 'blinkcoder-design-notes', '6', 'Michael Chen.....现在主要从事Java Web开发，现在大四了..在深圳一家公司实习，平常下班后就折腾出了这个博客。\n其实建博客的想法是从大二开始的，当时候用WordPress搭了一个玩了一下，由于自己那时候主要在做C++、MFC方面的东西，对WEB 方面的只是可谓是一窍不通吧。感觉如果要想在上面该基本上无从下手吧，所以后来博客也就没继续弄了。\n由于种种原因，在大二下学期接触了Java，到后面J2EE。自己感觉对于Web方面也有点熟悉了，所以又萌生了自己写博客的年头。用于大三写的学校的研究生管理系统，使用SSH、ExtJS、Spring Security、Maven等技术，但是如果用这些来做一个博客自己都感觉有点太臃肿或者小题大做吧。由于平常比较喜欢逛技术论坛，有一次无意中看到了开源中国分享的它们框架的部分代码，看了一下确实挺精简，同时也比较好理解。读它们分享的那些代码学到了很多。部分工具借鉴的OSCHina分享的部分开源代码。\n 后来，在OSChina看到讨论JFinal人比较多，了解到了JFinal框架，决定去稍微了解了解一下下，发现里面确实写的非常的精简。可以以极少的代码就能实现功能，相比以前的SSH框架显得轻巧很多。在对源码进行一个透读的过程后，萌生了用这JFinal写一个博客的想法。\n由于JFinal里面内置的Velocity渲染插件功能相对比较单调，所以在写博客过程中写了一个支持Velocity-ToolBox的一个渲染插件，同时由于开始准备用Redis做缓存，写了一个缓存插件，提交给了JFinal-Ext了。但是现在部署采用的是Ehcache吧，购买的VPS只有512内存，和同学一起购买，运行了两个这样的项目，所以放弃使用Redis做缓存吧。至于这个Velocity-Toolbox的插件，由于为了避免写大量RenderVelocity这样的方法，对JFinal的Action处理稍稍做了调整，具体可以参见[GitHub][1]上的地址，所以就没有提交了。\n系统存在一些Bug或者不安全的隐患还需要大家帮忙提提建议或者意见，如果发现有啥bug或者好的建议，可以去建议页面进行吐槽和建议，如果有兴趣一起参与这博客的开发，Fork代码就OK，接受任何人的好的Pull Request...\n\n源码地址：[BlinkCoder][2]\n\n> ###BlinkCoder使用的技术\n1\\.     JFinal框架\n2\\.     Velocity模板引擎\n3\\.     EhCache缓存\n4\\.     Maven构建\n5\\.     前台采用Bootstrap3\n6\\.     使用Git作为版本控制\n7\\.     后台编辑器自己写的，基于markdown的Bootstrap风格的编辑器,调用七牛云的上传接口\n8\\.     Lucene构建搜索模块\n9\\.     使用Nginx做反向代理和处理部分后台静态文件\n\n云空间的选择\n云空间选择的是七牛云存储，主要看中的是他有一个免费的额度，也就是每个月用户10G的下行流量，这个对于我们这种小型博客还是绰绰有余的，博客用户加载的静态资源都来自七牛云存储，后台的UEditor部分来自本机服务器。\n\n>注册链接：[七牛云存储][3]\n\n域名的选择\n想域名其实想了我挺久的，最后定BlinkCoder.com，感觉还算挺好记的吧。Chrome的内存现在都改成Blink了。后面Coder我就不说了，虽然不是做Chrome内核开发。理解为极速开发也不为过吧。域名我是在Godaddy上购买的，第一次购买域名，问了好多朋友，最后还是选择在了Godaddy上购买，原因就不在这详述了，百度上能搜出一大堆。使用优惠码购买便宜35%，也就50多一个的样子。支付的时候输入优惠码即可。\n>注册链接：[Godaddy][4]\n优惠码：WOWblink\n\n主机的选择\n主机我最后是选用的DigitalOcean，虽然机房在美国旧金山，但是该博客几乎所有的静态资源都在七牛云上，所以访问博客的速度不会觉的有很大的延迟。选择的主机类型是最便宜的。5$一个月，512的内存，20G的SSD，单核，1T的流量。现在是和同学一起购买，部署了两个这样的博客，根据内存上来说，这种博客应该部署3个没有啥问题吧，也就是每人10块。哈哈。现在新活动还赠送10$哦，虽然我错过了这，后悔啊。相当于免费2个月啊。\n>注册链接：[DigtalOcean][5]\n注册后使用优惠码：2014SSD\n\nPS:上面的几个链接都是邀请链接，互惠互利吧，也算是对博客开源的一个支持。\n\n计划以后分享一些自己学习、实习、工作上的一些技术经验。\n\n关于使用本站源码搭建一个简单的Blog教程将会在此帖贴出所有的链接，也可以<a target=\"_blank\" href=\"http://shang.qq.com/wpa/qunwpa?idkey=ca0fdf4a182b38908f56d8bcdd39226b042447e83976c69ad3ace316d5d86bd2\"><img border=\"0\" src=\"http://pub.idqqimg.com/wpa/images/group.png\" alt=\"BlinkCoder\" title=\"BlinkCoder\"><iframe id=\"tmp_downloadhelper_iframe\" style=\"display: none;\"></iframe></a>BlinkCoder进行讨论交流。  \n\n（不定时更新....）\n\n\n  [1]: https://github.com/yidongnan/BlinkCoder\n  [2]: https://github.com/yidongnan/BlinkCoder\n  [3]: https://portal.qiniu.com/signup?code=1oz5ny8ztjm\n  [4]: http://godaddy.com/\n  [5]: https://www.digitalocean.com/?refcode=db097e70179d', '', '2014-03-06 04:59:17', '161', '1', '0');
INSERT INTO `blog` VALUES ('2', '获取BlinkCoder源码', 'blinkcoder-source', '6', 'BlinkCoder的所有源码都是公开的，大家可以在GitHub上得到最新的源码。地址https://github.com/yidongnan/BlinkCoder 如果没有注册的可以先注册一个账号。在GitHub，用户可以十分轻易地找到海量的开源代码。因此GitHub被誉为程序员或IT人士的三宝之一。所以身为程序员的我们拥有这个是必要的。\n\nFork后能基于现在的BlinkCoder版本创建一个属于你的公开库吧。Watch后这边新的提交你都能收到消息，Star就一收藏的意思吧。不知道这么说有什么问题？\n\n至于怎么拉下代码下来。这样的文章很多，在这就不一一阐述了。如果发现系统有什么bug或者好的建议，可以直接pull request。BlinkCoder希望得到大家的帮助。。。\n![目录文件结构][1]\n文件目录大体上就是这样的\n\n>src/main/java/com/blinkcoder包下是博客的源码\n\n>src/main/java/com/jfinal包下是JFinal的框架的代码\n\n>src/mian/resource包下是一些配置文件\n\n>src/main/webapp/WEB-INF/www是velocity自动按照url渲染过来的模板文件\n\n>src/main/webapp/WEB-INF/layout是velocity的布局模板\n\n>src/main/webapp/WEB-INF/macro是自定义的velocity的一些宏\n\n如果想在该博客下进行修改，只需要关注blinkcoder包就行，而如果想对整个系统或者JFinal有个深入了解的话可以去看看jfinal的源码\n\n如果想对界面进行改造美化的可以关注www目录下的文件即可。\n\n对于源码的介绍就先到这。下次将会介绍一些在本博客上进行开发\n\n\n  [1]: http://blinkcoder-img.qiniudn.com/FqTB9fQg50z_S1_sMbKHbCA9UmIU', '', '2014-02-11 15:51:22', '56', '0', '0');
INSERT INTO `blog` VALUES ('3', 'DigitalOcean的vps主机添加Swap', 'how-to-add-swap-for-digitalocean-vps', '3', '官网找到了相关的连接，但是是英文的，自己设置后顺便按照英文做个中文的教程吧\n\n原文地址是：https://www.digitalocean.com/community/articles/how-to-add-swap-on-ubuntu-12-04\n\n本人的VPS是买的512M的，由于和一个同学合租这VPS，平常有喜欢在这上面各种折腾，内存不够还是被我碰到了。但是如果不怎么折腾，这种配置挂3个这样的博客还是没有一点压力。大家可以通过优惠码进行购买，可以送10美元，也就是免费使用2个月哦。\n\n注册链接：[DigtalOcean][1]\n\n注册后使用优惠码：2014SSD\n\n1、检查 Swap 空间\n在设置 Swap 文件之前，有必要先检查一下系统里有没有既存的 Swap 文件。\n运行以下命令：\n\n    swapon -s\n\n如果返回的信息概要是空的，则表示 Swap 文件不存在。\n\n2、检查文件系统\n在设置 Swap 文件之前，同样有必要检查一下文件系统，看看是否有足够的硬盘空间来设置 Swap 。运行以下命令：\n\n    df -hal\n\n检查返回的信息，还剩余足够的硬盘空间即可。\n\n3、创建并允许 Swap 文件\n下面使用 dd 命令来创建 Swap 文件。\n\n    dd if=/dev/zero of=/swapfile bs=1024 count=512k\n\n>参数解读：\nif=文件名：输入文件名，缺省为标准输入。即指定源文件。< if=input file >\nof=文件名：输出文件名，缺省为标准输出。即指定目的文件。< of=output file >\nbs=bytes：同时设置读入/输出的块大小为bytes个字节\ncount=blocks：仅拷贝blocks个块，块大小等于bs指定的字节数。\n\n这里我设置的count是2048K，反正SSD的硬盘20G也用不完，就划分了2G作为Swap吧.,SSD做Swap速度应该还是杠杠的。\n\n4、格式化并激活 Swap 文件\n上面已经创建好 Swap 文件，还需要格式化后才能使用。运行命令：\n\n    mkswap /swapfile\n\n激活 Swap ，运行命令：\n\n    swapon /swapfile\n\n以上步骤做完，再次运行命令：\n\n    swapon -s\n\n你会发现返回的信息概要：\n\n<span style=\";font-family:Consolas;color:#0066CC\">Filename&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Type&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Size&nbsp;&nbsp;&nbsp;&nbsp;Used&nbsp;&nbsp;&nbsp;&nbsp;Priority<br> /swapfile&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;file&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;524284&nbsp;&nbsp;&nbsp;&nbsp;0&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;-1</span>\n\n如果要机器重启的时候自动挂载 Swap ，那么还需要修改 fstab 配置。\n用 vim 打开 /etc/fstab 文件，在其最后添加如下一行：\n\n<span style=\";font-family:Consolas;color:#0066CC\">/swapfile&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;swap&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;swap&nbsp;&nbsp;&nbsp;&nbsp;defaults&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;0&nbsp;0</span>\n\n最后，赋予 Swap 文件适当的权限：\n\n    chown root:root /swapfile \n    chmod 0600 /swapfile\n\n最后看下\n![设置swap后的主机状态][2]\n哈哈  512M的内存设置了swap弄6个这样的博客估计都没啥问题..\n\n\n  [1]: https://www.digitalocean.com/?refcode=db097e70179d\n  [2]: http://blinkcoder-img.qiniudn.com/Fs-2YoIO0m9U2xzZZXd-a7PTUjAc', '', '2014-02-11 15:48:57', '25', '0', '0');
INSERT INTO `blog` VALUES ('4', '使用BlinkCoder的源码进行部署', 'run-tomcat-user-blinkcoder-source', '6', '根据上篇文章获取BlinkCoder源码，源码都应该在你本地了吧。这边文章就让大家把博客运行跑起来把。 \n首先，你必须准备好**JDK7、Tomcat7、Maven**，这些东西的安装就不在这介绍了，如果真不会Google一下，教程大把大把的有。 \n接着对配置动一点手脚吧，进入src/main/resource目录，找到其中的config.txt\n\n导入咱们的测试数据吧，sql文件在src/main/resource目录中，blog.sql，导入的时候一定要以UTF-8的形式导入哦.\n\n>修改数据库名，用户名，密码（后面别留下空格哦）  \n jdbcUrl = jdbc:mysql://localhost:3306/blog \n username = root password = root\n \n如果一切顺利的话现在就可以打包运行了，在BlinkCoder文件夹下打开命令行\n\n\n>输入mvn package\n前提你已经配好了Maven的环境变量，等完成之后会多出个target的目录，里面找到war包拷贝到你的tomcat的webapp目录中就可以运行了\n\n或者你也可以将项目导入到Idea或者Eclipse中，同样可以像普通web项目一样运行，但是在导入的时候一定要记得选择使用Maven导入项目。\n\n>启动tomcat就可以运行，你可以 \n http://localhost:端口号/   或者 \n http://localhost:端口号/BlinkCoder/\n\n可以成功浏览 \n如果还是不行的话就留言或者加QQ群：312695705', '', '2014-02-11 15:43:42', '32', '0', '0');

-- ----------------------------
-- Table structure for blog_label
-- ----------------------------
DROP TABLE IF EXISTS `blog_label`;
CREATE TABLE `blog_label` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `blog_id` int(11) DEFAULT NULL,
  `label_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of blog_label
-- ----------------------------
INSERT INTO `blog_label` VALUES ('1', '2', '1');
INSERT INTO `blog_label` VALUES ('2', '3', '3');
INSERT INTO `blog_label` VALUES ('3', '3', '4');
INSERT INTO `blog_label` VALUES ('4', '3', '3');
INSERT INTO `blog_label` VALUES ('13', '1', '3');

-- ----------------------------
-- Table structure for catalog
-- ----------------------------
DROP TABLE IF EXISTS `catalog`;
CREATE TABLE `catalog` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `desc` varchar(255) NOT NULL COMMENT '链接的描述',
  `flag` int(1) NOT NULL DEFAULT '0' COMMENT '是否显示在首页 0不显示 1显示',
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of catalog
-- ----------------------------
INSERT INTO `catalog` VALUES ('1', 'java', 'Java', '1');
INSERT INTO `catalog` VALUES ('2', 'web', 'Web开发', '1');
INSERT INTO `catalog` VALUES ('3', 'linux', 'Linux', '1');
INSERT INTO `catalog` VALUES ('4', 'sql', '数据库', '1');
INSERT INTO `catalog` VALUES ('5', 'essay', '随笔', '0');
INSERT INTO `catalog` VALUES ('6', 'BlinkCoder', '本站源码', '1');

-- ----------------------------
-- Table structure for label
-- ----------------------------
DROP TABLE IF EXISTS `label`;
CREATE TABLE `label` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `desc` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of label
-- ----------------------------
INSERT INTO `label` VALUES ('1', 'java', 'Java');
INSERT INTO `label` VALUES ('2', 'mysql', 'MySQL');
INSERT INTO `label` VALUES ('3', 'blinkcoder', 'BlinkCoder');
INSERT INTO `label` VALUES ('4', 'digitalocean', 'DigitalOcean');
INSERT INTO `label` VALUES ('5', 'flexpaper', 'FlexPaper');
INSERT INTO `label` VALUES ('6', 'openoffice', 'OpenOffice');
INSERT INTO `label` VALUES ('7', 'swftool', 'SwfTool');
INSERT INTO `label` VALUES ('8', 'jmeter', 'JMeter');
INSERT INTO `label` VALUES ('9', 'jdk', 'JDK');
INSERT INTO `label` VALUES ('10', 'quartz', 'Quartz');
INSERT INTO `label` VALUES ('11', 'maven', 'Maven');

-- ----------------------------
-- Table structure for link
-- ----------------------------
DROP TABLE IF EXISTS `link`;
CREATE TABLE `link` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `title` varchar(255) DEFAULT NULL,
  `desc` varchar(255) DEFAULT NULL,
  `url` varchar(255) DEFAULT NULL,
  `sequence` tinyint(4) DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of link
-- ----------------------------
INSERT INTO `link` VALUES ('1', '开源中国', '开源中国', 'http://www.oschina.net/', '1');
INSERT INTO `link` VALUES ('2', '小邓PHP博客', 'PHP研发工程师_小邓PHP博客', 'http://www.dengwz.com/', '3');
INSERT INTO `link` VALUES ('3', 'P.ROGRAM.ME', 'P.ROGRAM.ME', 'http://p.rogram.me/', '2');
INSERT INTO `link` VALUES ('4', 'Gavin', 'Gavin', 'http://ligavin.net/', '4');
INSERT INTO `link` VALUES ('5', 'Just Think', 'Just Think', 'http://leiblog.duapp.com/', '5');

-- ----------------------------
-- Table structure for lucene_task
-- ----------------------------
DROP TABLE IF EXISTS `lucene_task`;
CREATE TABLE `lucene_task` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `obj_id` int(11) DEFAULT NULL,
  `obj_type` int(11) DEFAULT NULL,
  `opt` int(11) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `handle_time` datetime DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of lucene_task
-- ----------------------------
INSERT INTO `lucene_task` VALUES ('1', '1', '1', '1', '2014-03-06 13:54:24', '2014-03-06 01:34:02', '1');
INSERT INTO `lucene_task` VALUES ('2', '2', '1', '1', '2014-03-06 13:54:24', '2014-03-06 01:34:02', '1');
INSERT INTO `lucene_task` VALUES ('3', '3', '1', '1', '2014-03-06 13:54:24', '2014-03-06 01:34:02', '1');
INSERT INTO `lucene_task` VALUES ('4', '4', '1', '1', '2014-03-06 13:54:24', '2014-03-06 01:34:02', '1');
INSERT INTO `lucene_task` VALUES ('10', '1', '1', '2', '2014-03-06 04:59:17', null, '0');

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `nickname` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES ('1', 'admin', '698D51A19D8A121CE5111111701668', 'Michael', 'yidongnan@gmail.com');
