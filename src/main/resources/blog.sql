/*
Navicat MySQL Data Transfer

Source Server         : MySQL
Source Server Version : 50611
Source Host           : localhost:3306
Source Database       : blog

Target Server Type    : MYSQL
Target Server Version : 50611
File Encoding         : 65001

Date: 2014-01-22 16:02:08
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for blog
-- ----------------------------
DROP TABLE IF EXISTS `blog`;
CREATE TABLE `blog` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `title` varchar(255) NOT NULL DEFAULT '',
  `global_url` varchar(255) NOT NULL COMMENT '全局的url',
  `catalog` int(11) NOT NULL DEFAULT '1',
  `content` text NOT NULL,
  `desc` varchar(255) NOT NULL DEFAULT '' COMMENT '简述',
  `update_time` datetime NOT NULL,
  `read_count` int(11) NOT NULL DEFAULT '0',
  `comment_count` int(11) NOT NULL DEFAULT '0',
  `type` int(1) NOT NULL DEFAULT '0' COMMENT '0-普通 1-置顶',
  PRIMARY KEY (`id`),
  UNIQUE KEY `globalurl` (`global_url`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of blog
-- ----------------------------
INSERT INTO `blog` VALUES ('1', '博客设计说明', 'blinkcoder-design-notes', '6', '<p style=\"margin-top:5px;margin-bottom:5px;margin-left: 0;text-indent:28px\"><span style=\"font-family:&#39;微软雅黑&#39;,&#39;sans-serif&#39;;color:#333333;background:white\">Michael Chen.....</span><span style=\"font-family:&#39;微软雅黑&#39;,&#39;sans-serif&#39;;color:#333333;background:white\">现在主要从事Java Web开发，现在大四了..在深圳一家公司实习，平常下班后就折腾出了这个博客。</span></p><p style=\"margin: 5px 0;text-indent: 28px\"><span style=\"font-family:&#39;微软雅黑&#39;,&#39;sans-serif&#39;;color:#333333;background:white\">其实建博客的想法是从大二开始的，当时候用WordPress搭了一个玩了一下，由于自己那时候主要在做C++、MFC方面的东西，对WEB 方面的只是可谓是一窍不通吧。感觉如果要想在上面该基本上无从下手吧，所以后来博客也就没继续弄了。</span></p><p style=\"margin: 5px 0;text-indent: 28px\"><span style=\"font-family:&#39;微软雅黑&#39;,&#39;sans-serif&#39;;color:#333333;background:white\">由于种种原因，在大二下学期接触了Java，到后面J2EE。自己感觉对于Web方面也有点熟悉了，所以又萌生了自己写博客的年头。用于大三写的学校的研究生管理系统，使用SSH、ExtJS、Spring Security、Maven等技术，但是如果用这些来做一个博客自己都感觉有点太臃肿或者小题大做吧。由于平常比较喜欢逛技术论坛，有一次无意中看到了开源中国分享的它们框架的部分代码，看了一下确实挺精简，同时也比较好理解。读它们分享的那些代码学到了很多。部分工具借鉴的OSCHina分享的部分开源代码。</span></p><p style=\"margin: 5px 0;text-indent: 28px\"><span style=\"font-family:&#39;微软雅黑&#39;,&#39;sans-serif&#39;;color:#333333;background:white\">&nbsp;</span><span style=\"font-family:&#39;微软雅黑&#39;,&#39;sans-serif&#39;;color:#333333;background:white\">后来，在OSChina看到讨论JFinal人比较多，了解到了JFinal框架，决定去稍微了解了解一下下，发现里面确实写的非常的精简。可以以极少的代码就能实现功能，相比以前的SSH框架显得轻巧很多。在对源码进行一个透读的过程后，萌生了用这JFinal写一个博客的想法。</span></p><p style=\"margin: 5px 0;text-indent: 28px\"><span style=\"font-family:&#39;微软雅黑&#39;,&#39;sans-serif&#39;;color:#333333;background:white\">由于JFinal里面内置的Velocity渲染插件功能相对比较单调，所以在写博客过程中写了一个支持Velocity-ToolBox的一个渲染插件，同时由于开始准备用Redis做缓存，写了一个缓存插件，提交给了JFinal-Ext了。但是现在部署采用的是Ehcache吧，购买的VPS只有512内存，和同学一起购买，运行了两个这样的项目，所以放弃使用Redis做缓存吧。至于这个Velocity-Toolbox的插件，由于为了避免写大量RenderVelocity这样的方法，对JFinal的Action处理稍稍做了调整，具体可以参见<a href=\"https://github.com/yidongnan/BlinkCoder\" target=\"_blank\" title=\"BlinkCoder\">GitHub</a>上的地址，所以就没有提交了。</span></p><p style=\"margin: 5px 0;text-indent: 28px\"><span style=\"font-family:&#39;微软雅黑&#39;,&#39;sans-serif&#39;;color:#333333;background:white\">系统存在一些Bug或者不安全的隐患还需要大家帮忙提提建议或者意见，如果发现有啥bug或者好的建议，可以去建议页面进行吐槽和建议，如果有兴趣一起参与这博客的开发，Fork代码就OK，接受任何人的好的Pull Request...</span></p><p style=\"margin-top:5px;margin-bottom:5px;margin-left: 0\"><span style=\"font-family:&#39;微软雅黑&#39;,&#39;sans-serif&#39;;color:#333333;background:white\">&nbsp;</span></p><p style=\"margin-top:5px;margin-bottom:5px;text-align:center\"><span style=\"font-family:&#39;微软雅黑&#39;,&#39;sans-serif&#39;;color:#333333;background:white\">源码地址：<a href=\"https://github.com/yidongnan/BlinkCoder\" target=\"_blank\" textvalue=\"BlinkCoder\">BlinkCoder</a></span></p><p style=\"margin: 5px 0\"><span style=\"font-family: Arial, sans-serif\">&nbsp;</span></p><h3><span style=\"background:white\">BlinkCoder</span><span style=\"font-family:宋体;background:white\">使用的技术</span></h3><p style=\"margin-top:5px;margin-bottom:5px;margin-left: 28px\"><span style=\"font-family: Arial, sans-serif\">1.<span style=\"font-size: 9px;font-family: &#39;Times New Roman&#39;\">&nbsp;&nbsp;&nbsp;&nbsp; </span></span><span style=\"font-family: Arial, sans-serif\">JFinal</span>框架</p><p style=\"margin: 5px 0 5px 28px\"><span style=\"font-family: Arial, sans-serif\">2.<span style=\"font-size: 9px;font-family: &#39;Times New Roman&#39;\">&nbsp;&nbsp;&nbsp;&nbsp; </span></span><span style=\"font-family: Arial, sans-serif\">Velocity</span>模板引擎</p><p style=\"margin: 5px 0 5px 28px\"><span style=\"font-family: Arial, sans-serif\">3.<span style=\"font-size: 9px;font-family: &#39;Times New Roman&#39;\">&nbsp;&nbsp;&nbsp;&nbsp; </span></span><span style=\"font-family: Arial, sans-serif\">EhCache</span>缓存（可切换到<span style=\"font-family: Arial, sans-serif\">Redis</span>）</p><p style=\"margin: 5px 0 5px 28px\"><span style=\"font-family: Arial, sans-serif\">4.<span style=\"font-size: 9px;font-family: &#39;Times New Roman&#39;\">&nbsp;&nbsp;&nbsp;&nbsp; </span></span><span style=\"font-family: Arial, sans-serif\">Maven</span>构建</p><p style=\"margin: 5px 0 5px 28px\"><span style=\"font-family: Arial, sans-serif\">5.<span style=\"font-size: 9px;font-family: &#39;Times New Roman&#39;\">&nbsp;&nbsp;&nbsp;&nbsp; </span></span>前台采用<span style=\"font-family: Arial, sans-serif\">Bootstrap3</span></p><p style=\"margin: 5px 0 5px 28px\"><span style=\"font-family: Arial, sans-serif\">6.<span style=\"font-size: 9px;font-family: &#39;Times New Roman&#39;\">&nbsp;&nbsp;&nbsp;&nbsp; </span></span>使用<span style=\"font-family: Arial, sans-serif\">Git</span>作为版本控制</p><p style=\"margin: 5px 0 5px 28px\"><span style=\"font-family: Arial, sans-serif\">7.<span style=\"font-size: 9px;font-family: &#39;Times New Roman&#39;\">&nbsp;&nbsp;&nbsp;&nbsp; </span></span>后台<span style=\"font-family: Arial, sans-serif\">UEditor</span>上传图片引入七牛云的上传接口</p><p style=\"margin: 5px 0 5px 28px\"><span style=\"font-family: Arial, sans-serif\">8.<span style=\"font-size: 9px;font-family: &#39;Times New Roman&#39;\">&nbsp;&nbsp;&nbsp;&nbsp; </span></span><span style=\"font-family: Arial, sans-serif\">Lucene</span>构建搜索模块（待开发）</p><p style=\"margin: 5px 0 5px 28px\"><span style=\"font-family:Arial, sans-serif\">9.</span><span style=\"font-family: &#39;Times New Roman&#39;; font-size: 9px;\">&nbsp; &nbsp; </span>&nbsp;使用Nginx处理部分后台静态文件</p><p style=\"margin: 5px 0 5px 28px\"><br/></p><p style=\"margin-top:5px;margin-bottom:5px;margin-left: 0\"><span style=\"font-family: Arial, sans-serif\">&nbsp;</span></p><h3><span style=\"font-family:宋体\">云空间的选择</span></h3><p style=\"margin-top:5px;margin-bottom:5px;margin-left: 0\"><span style=\"font-family:&#39;微软雅黑&#39;,&#39;sans-serif&#39;;color:#333333;background:white\">云空间选择的是七牛云存储，主要看中的是他有一个免费的额度，也就是每个月用户10G的下行流量，这个对于我们这种小型博客还是绰绰有余的，博客用户加载的静态资源都来自七牛云存储，后台的UEditor部分来自本机服务器。</span></p><p style=\"margin-top:5px;margin-bottom:5px;text-align:center\"><span style=\"font-family:&#39;微软雅黑&#39;,&#39;sans-serif&#39;;color:#333333;background:white\">注册链接：<a href=\"https://portal.qiniu.com/signup?code=1oz5ny8ztjm\" target=\"_blank\" textvalue=\"七牛云存储\">七牛云存储</a></span></p><p style=\"margin-top:5px;margin-bottom:5px;margin-left: 0\"><span style=\"font-family:&#39;微软雅黑&#39;,&#39;sans-serif&#39;;color:#333333;background:white\">&nbsp;</span></p><h3><span style=\"font-family:宋体;background:white\">域名的选择</span></h3><p style=\"margin-top:5px;margin-bottom:5px;margin-left: 0\"><span style=\"font-family:&#39;微软雅黑&#39;,&#39;sans-serif&#39;;color:#333333;background:white\">想域名其实想了我挺久的，最后定BlinkCoder.com，感觉还算挺好记的吧。Chrome的内存现在都改成Blink了。后面Coder我就不说了，虽然不是做Chrome内核开发。理解为极速开发也不为过吧。域名我是在Godaddy上购买的，第一次购买域名，问了好多朋友，最后还是选择在了Godaddy上购买，原因就不在这详述了，百度上能搜出一大堆。使用优惠码购买便宜35%，也就50多一个的样子。支付的时候输入优惠码即可。</span></p><p style=\"margin-top:5px;margin-bottom:5px;text-align:center\"><span style=\"font-family: 微软雅黑, sans-serif\">注册链接：<a href=\"http://godaddy.com\" target=\"_blank\">Godaddy</a></span></p><p style=\"margin-top:5px;margin-bottom:5px;text-align:center\"><span style=\"font-family:&#39;微软雅黑&#39;,&#39;sans-serif&#39;;color:#333333;background:white\">优惠码：WOWblink</span></p><p style=\"margin-top:5px;margin-bottom:5px;margin-left: 0\"><span style=\"font-family:&#39;微软雅黑&#39;,&#39;sans-serif&#39;;color:#333333;background:white\">&nbsp;</span></p><h3><span style=\"font-family:宋体;background:white\">主机的选择</span></h3><p style=\"margin-top:5px;margin-bottom:5px;margin-left: 0\"><span style=\"font-family:&#39;微软雅黑&#39;,&#39;sans-serif&#39;;color:#333333;background:white\">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; </span><span style=\"font-family:&#39;微软雅黑&#39;,&#39;sans-serif&#39;;color:#333333;background:white\">主机我最后是选用的DigitalOcean，虽然机房在美国旧金山，但是该博客几乎所有的静态资源都在七牛云上，所以访问博客的速度不会觉的有很大的延迟。选择的主机类型是最便宜的。5$一个月，512的内存，20G的SSD，单核，1T的流量。现在是和同学一起购买，部署了两个这样的博客，根据内存上来说，这种博客应该部署3个没有啥问题吧，也就是每人10块。哈哈。现在新活动还赠送10$哦，虽然我错过了这，后悔啊。相当于免费2个月啊。</span></p><p style=\"margin-top:5px;margin-bottom:5px;text-align:center\"><span style=\"font-family: 微软雅黑, sans-serif\">注册链接：<a href=\"https://www.digitalocean.com/?refcode=db097e70179d\" target=\"_blank\" textvalue=\"DigtalOcean\">DigtalOcean</a></span></p><p style=\"margin-top:5px;margin-bottom:5px;text-align:center\"><span style=\"font-family: 微软雅黑, sans-serif\">注册后使用优惠码：</span><span style=\"font-size: 14px;font-family: 微软雅黑, sans-serif\">2014SSD</span></p><p style=\"margin-top:5px;margin-bottom:5px;margin-left: 0\"><span style=\"font-size: 14px;font-family: 微软雅黑, sans-serif\">&nbsp;</span></p><p style=\"margin-top:5px;margin-bottom:5px;margin-left: 0\"><span style=\"font-size: 14px;font-family: 微软雅黑, sans-serif\"><br/></span></p><p style=\"margin-top:5px;margin-bottom:5px;margin-left: 0\"><span style=\"font-family: Arial, sans-serif\">PS:</span>上面的几个链接都是邀请链接，互惠互利吧，也算是对博客开源的一个支持。</p><p style=\"margin-top:5px;margin-bottom:5px;margin-left: 0\"><span style=\"font-family:&#39;微软雅黑&#39;,&#39;sans-serif&#39;;color:#333333;background:white\">&nbsp;</span></p><p style=\"margin-top:5px;margin-bottom:5px;margin-left: 0\"><span style=\"font-family:&#39;微软雅黑&#39;,&#39;sans-serif&#39;;color:#333333;background:white\">计划以后分享一些自己学习、实习、工作上的一些技术经验。</span></p><p style=\"margin: 5px 0\"><span style=\"font-family: Arial, sans-serif\">&nbsp;</span></p><p style=\"margin: 5px 0\"><span style=\"font-family:&#39;微软雅黑&#39;,&#39;sans-serif&#39;;color:#333333;background:white\">关于使用本站源码搭建一个简单的Blog教程将会在此帖贴出所有的链接，也可以<a target=\"_blank\" href=\"http://shang.qq.com/wpa/qunwpa?idkey=ca0fdf4a182b38908f56d8bcdd39226b042447e83976c69ad3ace316d5d86bd2\"><img border=\"0\" src=\"http://pub.idqqimg.com/wpa/images/group.png\" alt=\"BlinkCoder\" title=\"BlinkCoder\"/></a>进行讨论交流。</span> &nbsp;</p><p style=\"margin: 5px 0\"><span style=\"font-family: Arial, sans-serif\">&nbsp;</span></p><p style=\"margin: 5px 0\"><span style=\"font-family: Arial, sans-serif\">&nbsp;</span></p><p style=\"margin: 5px 0\"><span style=\"font-family: Arial, sans-serif\">&nbsp;</span></p><p style=\"margin: 5px 0\"><span style=\"font-family: Arial, sans-serif\">&nbsp;</span></p><p style=\"margin: 5px 0\"><span style=\"font-family:&#39;微软雅黑&#39;,&#39;sans-serif&#39;;color:#333333;background:white\">（不定时更新....）</span></p><p><br/></p>', '1', '2014-01-19 02:23:36', '75', '1', '1');

-- ----------------------------
-- Table structure for blog_label
-- ----------------------------
DROP TABLE IF EXISTS `blog_label`;
CREATE TABLE `blog_label` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `blog_id` int(11) DEFAULT NULL,
  `label_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of blog_label
-- ----------------------------
INSERT INTO `blog_label` VALUES ('1', '1', '1');
INSERT INTO `blog_label` VALUES ('2', '1', '2');
INSERT INTO `blog_label` VALUES ('3', '2', '1');

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
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of label
-- ----------------------------
INSERT INTO `label` VALUES ('1', 'java', 'Java相关');
INSERT INTO `label` VALUES ('2', 'sql', '数据库相关');

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
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of link
-- ----------------------------
INSERT INTO `link` VALUES ('1', '开源中国', '开源中国', 'http://www.oschina.net/', '1');

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
INSERT INTO `user` VALUES ('1', '111', '698D51A19D8A121CE581499D7B701668', 'Michael', '111');
