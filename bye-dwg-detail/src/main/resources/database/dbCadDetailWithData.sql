/*
SQLyog Ultimate v12.09 (64 bit)
MySQL - 5.7.26 : Database - dbcaddetail
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`dbcaddetail` /*!40100 DEFAULT CHARACTER SET utf8 */;

USE `dbcaddetail`;

/*Table structure for table `artifact` */

DROP TABLE IF EXISTS `artifact`;

CREATE TABLE `artifact` (
  `artifactId` bigint(20) NOT NULL AUTO_INCREMENT,
  `artifactName` varchar(255) NOT NULL COMMENT '产品（工件）名称',
  `artifactCode` varchar(255) DEFAULT NULL COMMENT '产品（工件）代号',
  `weight` float DEFAULT NULL COMMENT '重量',
  `materialCode` varchar(255) DEFAULT NULL COMMENT '材料代码',
  `materialName` varchar(255) DEFAULT NULL COMMENT '材料名称',
  `productFlag` smallint(6) NOT NULL DEFAULT '1' COMMENT '用于标记此工件是否是顶层工件，即产品。0-产品，非0-工件',
  `productModel` varchar(255) DEFAULT NULL COMMENT '产品型号',
  `canBeSplit` tinyint(1) NOT NULL DEFAULT '1' COMMENT '标记此工件是否可以继续分解（拥有下一级明细），目前代码以8ZT开头的工件为不可分解，其它工件为可分解',
  `artifactMemo` varchar(255) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`artifactId`)
) ENGINE=InnoDB AUTO_INCREMENT=65 DEFAULT CHARSET=utf8 COMMENT='变压器产品或变压器构件';

/*Data for the table `artifact` */

insert  into `artifact`(`artifactId`,`artifactName`,`artifactCode`,`weight`,`materialCode`,`materialName`,`productFlag`,`productModel`,`canBeSplit`,`artifactMemo`) values (1,'上节油箱','5ZT.384.0201.001',6459.14,NULL,NULL,0,'0SFZ11-360000/330',1,NULL),(2,'上节箱沿','8ZT.084.0201.001',1394,NULL,'30钢板Q235B',1,'0SFZ11-360000/330',0,NULL),(3,'箱壁','8ZT.051.0201.001',108.5,NULL,'10钢板Q235B',1,'0SFZ11-360000/330',0,NULL),(4,'箱盖','8ZT.312.0201.001',3285,NULL,'20钢板Q235B',1,'0SFZ11-360000/330',0,NULL),(5,'加强铁','8ZT.130.0201.010',2.7,NULL,NULL,1,'0SFZ11-360000/330',0,NULL),(6,'加强铁','8ZT.130.0201.011',2.7,NULL,'30钢板Q235B',1,'0SFZ11-360000/330',0,NULL),(7,'电缆槽支架 L=140','7ZT.043.017',1.2,NULL,NULL,1,'0SFZ11-360000/330',0,NULL),(8,'角钢100*10','长200',3.14,NULL,NULL,1,'0SFZ11-360000/330',0,NULL),(9,'箱壁','8ZT.051.0201.002',87,NULL,'10钢板Q235B',1,'0SFZ11-360000/330',0,NULL),(10,'加强铁','8ZT.130.0201.012',11,NULL,NULL,1,'0SFZ11-360000/330',0,NULL),(11,'加强铁','8ZT.130.0201.013',7.1,NULL,NULL,1,'0SFZ11-360000/330',0,NULL),(12,'底板','8ZT.151.0201.002',1.36,NULL,'6钢板Q235B',1,'0SFZ11-360000/330',0,'其中一件配割'),(13,'绝缘子底板','9ZT.055.0232',0.19,NULL,'20钢板Q235B',1,'0SFZ11-360000/330',0,'与下节底板对齐'),(14,'箱壁','8ZT.051.0201.003',269.3,NULL,'20钢板Q235B',1,'0SFZ11-360000/330',0,NULL),(15,'开关法兰','8ZT.180.0201.001',22,NULL,'32钢板Q235B',1,'0SFZ11-360000/330',0,NULL),(16,'接地螺母','9ZT.940.004',0.1,NULL,'∅30不锈钢棒OCr18Ni10Ti',1,'0SFZ11-360000/330',0,NULL),(17,'法 兰','7ZT.180.006',5.59,NULL,NULL,1,'0SFZ11-360000/330',0,NULL),(18,'50蝶阀法兰','7ZT.180.001',2.9,NULL,NULL,1,'0SFZ11-360000/330',0,NULL),(19,'讯号温度计座','7ZT.421.002',0.97,NULL,NULL,1,'0SFZ11-360000/330',0,NULL),(20,'中压法兰','5ZT.180.0201.001',55.93,NULL,NULL,1,'0SFZ11-360000/330',1,NULL),(21,'上定位装置','7ZT.271.003',36.36,NULL,NULL,1,'0SFZ11-360000/330',0,NULL),(22,'螺母','8ZT.940.0201.001',0.6,NULL,NULL,1,'0SFZ11-360000/330',0,NULL),(23,'加强铁','8ZT.130.0201.014',85.3,NULL,NULL,1,'0SFZ11-360000/330',0,NULL),(24,'法兰','7ZT.180.002',2.31,NULL,NULL,1,'0SFZ11-360000/330',0,NULL),(25,'高压中性点法兰','5ZT.180.0201.003',30.83,NULL,NULL,1,'0SFZ11-360000/330',1,NULL),(26,'管接头','5ZT.454.0201.001',16.5,NULL,NULL,1,'0SFZ11-360000/330',1,NULL),(27,'低压法兰','5ZT.180.0201.002',108.5,NULL,NULL,1,'0SFZ11-360000/330',1,NULL),(28,'冲撞记录仪底板','9ZT.055.011',2.3,NULL,'20钢板Q235-B',1,'0SFZ11-360000/330',0,NULL),(29,'护架L=50，H=30','9ZT.043.009',0.13,NULL,'φ8圆钢',1,'0SFZ11-360000/330',0,NULL),(30,'接地板','9ZT.151.002',0.21,NULL,'12不锈钢板',1,'0SFZ11-360000/330',0,NULL),(31,'出线盒升高座L=275','5ZT.454.0201.004',NULL,NULL,NULL,1,'0SFZ11-360000/330',1,NULL),(32,'出线盒升高座L=165','5ZT.454.0201.004',0.1,NULL,'6钢板Q235B',1,'0SFZ11-360000/330',1,NULL),(33,'字牌底板','9ZT.055.024',0.1,NULL,'6钢板Q235B',1,'0SFZ11-360000/330',0,NULL),(34,'管接头L=122','5ZT.454.0201.002',5.5,NULL,NULL,1,'0SFZ11-360000/330',1,NULL),(35,'管接头L=191','5ZT.454.0201.002',6,NULL,NULL,1,'0SFZ11-360000/330',1,NULL),(36,'绝缘子底板','8ZT.055.0201.001',0.35,NULL,'40圆钢',1,'0SFZ11-360000/330',0,'与下节底板对齐'),(37,'管接头','5ZT.454.0201.003',17.7,NULL,NULL,1,'0SFZ11-360000/330',1,NULL),(38,'把手','9ZT.253.001',0.13,NULL,'φ10圆钢',1,'0SFZ11-360000/330',0,NULL),(39,'法兰','8ZT.180.0201.002',28.4,NULL,'30钢板Q235B',1,'0SFZ11-360000/330',0,NULL),(40,'盖板','5ZT.312.0201.001',27.53,NULL,'8钢板Q235B',1,'0SFZ11-360000/330',1,NULL),(41,'密封圈 ∅12x∅655','9ZT.370.001',NULL,NULL,'丁腈橡胶',1,'0SFZ11-360000/330',0,NULL),(42,'螺栓 M16x45-热镀锌','GB/T 5783-2000',NULL,NULL,NULL,1,'0SFZ11-360000/330',0,NULL),(43,'垫圈 16-热镀锌','GB/T 97.2-2002',NULL,NULL,NULL,1,'0SFZ11-360000/330',0,NULL),(44,'弹簧垫圈 16-热镀锌','GB/T 93-1987',NULL,NULL,NULL,1,'0SFZ11-360000/330',0,NULL),(45,'法兰','8ZT.180.0201.003',17.1,NULL,'30钢板Q235B',1,'0SFZ11-360000/330',0,NULL),(46,'盖板','5ZT.312.0201.002',13.73,NULL,'8钢板Q235B',1,'0SFZ11-360000/330',1,NULL),(47,'法兰','8ZT.180.0201.004',8.6,NULL,'25钢板Q235B',1,'0SFZ11-360000/330',0,NULL),(48,'管长','长148',7.3,NULL,'∅219*9.5无缝钢管',1,'0SFZ11-360000/330',0,NULL),(49,'法兰','8ZT.180.0201.005',47.8,NULL,'30钢板Q235B',1,'0SFZ11-360000/330',0,NULL),(50,'密封垫L1=470 L2=1640','9ZT.370.008',NULL,NULL,'丁腈橡胶',1,'0SFZ11-360000/330',0,NULL),(51,'盖板','5ZT.310.0201.003',60.7,NULL,NULL,1,'0SFZ11-360000/330',1,NULL),(52,'吊板','9ZT.472.012',0.3,NULL,'16钢板Q235B',1,'0SFZ11-360000/330',0,NULL),(53,'盖板','如图',60.1,NULL,'8钢板Q235B',1,'0SFZ11-360000/330',0,NULL),(54,'法兰','9ZT.180.118',NULL,NULL,'25钢板Q235B',1,'0SFZ11-360000/330',0,NULL),(55,'%%c194钢管  ','L+13',NULL,NULL,'φ194x6无缝钢管Q235B',1,'0SFZ11-360000/330',0,NULL),(56,'接地螺母','9ZT.940.004',NULL,NULL,'φ30不锈钢棒0Cr18Ni10Ti',1,'0SFZ11-360000/330',0,NULL),(57,'法兰%%Cd=70','9ZT.180.125',2.7,NULL,'25钢板Q235B',1,'0SFZ11-360000/330',0,NULL),(58,'管长','长L',NULL,NULL,'∅114*7无缝钢管',1,'0SFZ11-360000/330',0,NULL),(59,'法兰','9ZT.180.021.4',5.1,NULL,'20钢板Q235B',1,'0SFZ11-360000/330',0,NULL),(60,'φ168钢管','长293',10,NULL,'φ168x9无缝钢管Q235B',1,'0SFZ11-360000/330',0,NULL),(61,'盖板','9ZT.310.010.4',2.66,NULL,'6钢板Q235B',1,'0SFZ11-360000/330',0,NULL);

/*Table structure for table `artifactdetail` */

DROP TABLE IF EXISTS `artifactdetail`;

CREATE TABLE `artifactdetail` (
  `detailId` bigint(20) NOT NULL AUTO_INCREMENT,
  `parentArtifactId` bigint(20) DEFAULT NULL COMMENT '上层工件ID',
  `artifactId` bigint(20) DEFAULT NULL,
  `number` int(11) NOT NULL DEFAULT '1' COMMENT '数量',
  `needSplit` tinyint(1) NOT NULL DEFAULT '0' COMMENT '明细表中的此工件是否需要继续分解（获取其下一级明细）',
  `dimension` varchar(255) DEFAULT NULL COMMENT '参考尺寸（会签）',
  `unit` varchar(255) DEFAULT NULL COMMENT '单位（会签）',
  `quota` float DEFAULT NULL COMMENT '定额（会签）',
  `workingSteps` varchar(255) DEFAULT NULL COMMENT '工序（会签）',
  `classificationSign` varchar(255) DEFAULT NULL COMMENT '分类标识（会签）',
  `processSign` varchar(255) DEFAULT NULL COMMENT '处理标志（会签）',
  `inspector` varchar(50) DEFAULT NULL COMMENT '会签人',
  `detailMemo` varchar(255) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`detailId`),
  KEY `FK_artifact_master` (`parentArtifactId`),
  KEY `FK_artifact_slave` (`artifactId`),
  CONSTRAINT `FK_artifact_master` FOREIGN KEY (`parentArtifactId`) REFERENCES `artifact` (`artifactId`),
  CONSTRAINT `FK_artifact_slave` FOREIGN KEY (`artifactId`) REFERENCES `artifact` (`artifactId`)
) ENGINE=InnoDB AUTO_INCREMENT=76 DEFAULT CHARSET=utf8;

/*Data for the table `artifactdetail` */

insert  into `artifactdetail`(`detailId`,`parentArtifactId`,`artifactId`,`number`,`needSplit`,`dimension`,`unit`,`quota`,`workingSteps`,`classificationSign`,`processSign`,`inspector`,`detailMemo`) values (1,1,2,1,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(2,1,3,2,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(3,1,4,1,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(4,1,5,20,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(5,1,6,4,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(6,1,7,9,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(7,1,8,8,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(8,1,9,1,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(9,1,10,2,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(10,1,11,2,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(11,1,12,2,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(12,1,13,2,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(13,1,14,1,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(14,1,15,3,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(15,1,16,15,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(16,1,17,1,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(17,1,18,1,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(18,1,19,6,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(19,1,20,3,1,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(20,1,21,8,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(21,1,22,8,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(22,1,23,3,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(23,1,24,3,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(24,1,25,1,1,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(25,1,26,4,1,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(26,1,27,3,1,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(27,1,28,1,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(28,1,29,14,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(29,1,30,6,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(30,1,31,2,1,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(31,1,32,2,1,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(32,1,33,2,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(33,1,34,1,1,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(34,1,35,1,1,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(35,1,36,2,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(36,1,37,2,1,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(37,20,38,1,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(38,20,39,1,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(39,20,40,1,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(40,20,41,1,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(41,20,42,20,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(42,20,43,20,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(43,20,44,20,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(44,25,38,1,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(45,25,45,1,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(46,25,46,1,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(47,25,41,1,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(48,25,42,16,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(49,25,43,16,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(50,25,44,16,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(51,26,47,1,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(52,26,48,1,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(53,27,49,1,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(54,27,50,1,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(55,27,42,42,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(56,27,43,42,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(57,27,44,42,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(58,27,51,3,1,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(59,51,52,2,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(60,51,53,1,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(61,31,54,1,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(62,31,55,1,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(63,31,56,1,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(64,32,54,1,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(65,32,55,1,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(66,32,56,1,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(67,33,57,1,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(68,33,58,1,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(69,34,57,1,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(70,34,58,1,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(71,37,59,1,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(72,37,60,1,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(73,37,61,1,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(74,35,57,1,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL),(75,35,58,1,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL);

/*Table structure for table `role_permission` */

DROP TABLE IF EXISTS `role_permission`;

CREATE TABLE `role_permission` (
  `permissionId` bigint(20) NOT NULL,
  `roleId` bigint(20) NOT NULL,
  PRIMARY KEY (`permissionId`,`roleId`),
  KEY `FK_role_permission2` (`roleId`),
  CONSTRAINT `FK_role_permission` FOREIGN KEY (`permissionId`) REFERENCES `syspermission` (`permissionId`),
  CONSTRAINT `FK_role_permission2` FOREIGN KEY (`roleId`) REFERENCES `sysrole` (`roleId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `role_permission` */

insert  into `role_permission`(`permissionId`,`roleId`) values (1,1),(2,1),(3,1),(4,1),(5,1),(6,1),(7,1),(8,1),(9,1),(10,1),(11,1),(12,1),(13,1),(14,1),(15,1),(16,1),(17,1),(18,1),(19,1),(20,1),(21,1),(22,1),(23,1),(24,1),(5,2),(9,2),(15,2),(5,3),(6,3),(7,3),(8,3),(9,3),(10,3),(11,3),(12,3),(13,3),(15,3),(5,4),(9,4),(11,4),(13,4),(15,4),(5,5),(9,5),(11,5),(14,5),(15,5),(16,5);

/*Table structure for table `syspermission` */

DROP TABLE IF EXISTS `syspermission`;

CREATE TABLE `syspermission` (
  `permissionId` bigint(20) NOT NULL AUTO_INCREMENT,
  `permissionTitle` varchar(255) NOT NULL COMMENT '权限名称，用于在界面上显示',
  `permissionCode` varchar(255) NOT NULL COMMENT '权限字符串，如：role:create,role:update,role:delete,role:view',
  `resourceUrl` varchar(1024) DEFAULT NULL COMMENT '资源路径，如：userInfo/userList',
  `resourceType` varchar(255) DEFAULT NULL COMMENT '资源类型，如：menu、button',
  `permissionAvailable` tinyint(1) NOT NULL DEFAULT '1' COMMENT '权限是否可用',
  PRIMARY KEY (`permissionId`)
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=utf8;

/*Data for the table `syspermission` */

insert  into `syspermission`(`permissionId`,`permissionTitle`,`permissionCode`,`resourceUrl`,`resourceType`,`permissionAvailable`) values (1,'查询用户','user:view',NULL,NULL,1),(2,'添加用户','user:add',NULL,NULL,1),(3,'编辑用户','user:edit',NULL,NULL,1),(4,'删除用户','user:del',NULL,NULL,1),(5,'查询产品','artifact:view',NULL,NULL,1),(6,'添加产品','artifact:add',NULL,NULL,1),(7,'编辑产品','artifact:edit',NULL,NULL,1),(8,'删除产品','artifact:del',NULL,NULL,1),(9,'查询明细','detail:view',NULL,NULL,1),(10,'添加明细','detail:add',NULL,NULL,1),(11,'编辑明细','detail:edit',NULL,NULL,1),(12,'删除明细','detail:del',NULL,NULL,1),(13,'导入明细','detail:import',NULL,NULL,1),(14,'会签明细','detail:check',NULL,NULL,1),(15,'导出明细','detail:export',NULL,NULL,1),(16,'打印明细','detail:print',NULL,NULL,1),(17,'查询角色','role:view',NULL,NULL,1),(18,'添加角色','role:add',NULL,NULL,1),(19,'编辑角色','role:edit',NULL,NULL,1),(20,'删除角色','role:del',NULL,NULL,1),(21,'查询权限','permission:view',NULL,NULL,1),(22,'添加权限','permission:add',NULL,NULL,1),(23,'编辑权限','permission:edit',NULL,NULL,1),(24,'删除权限','permission:del',NULL,NULL,1);

/*Table structure for table `sysrole` */

DROP TABLE IF EXISTS `sysrole`;

CREATE TABLE `sysrole` (
  `roleId` bigint(20) NOT NULL AUTO_INCREMENT,
  `roleName` varchar(255) NOT NULL COMMENT '角色名称，程序中使用（如：admin）',
  `description` varchar(255) NOT NULL COMMENT '角色描述，界面中使用（如：管理员）',
  `roleAvailable` tinyint(1) NOT NULL DEFAULT '1' COMMENT '角色是否可用',
  PRIMARY KEY (`roleId`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;

/*Data for the table `sysrole` */

insert  into `sysrole`(`roleId`,`roleName`,`description`,`roleAvailable`) values (1,'Administer','系统管理员',1),(2,'Staff','普通员工',1),(3,'Manager','产品管理员',1),(4,'Uploader','明细入库员',1),(5,'Inspector','明细会签员',1);

/*Table structure for table `sysuser` */

DROP TABLE IF EXISTS `sysuser`;

CREATE TABLE `sysuser` (
  `userId` bigint(20) NOT NULL AUTO_INCREMENT,
  `userName` varchar(255) NOT NULL COMMENT '用户的登录账号',
  `userAlias` varchar(255) NOT NULL COMMENT '用户姓名，用于在界面上显示',
  `password` varchar(1024) NOT NULL COMMENT '用户的登录密码',
  `salt` varchar(1024) NOT NULL COMMENT '用于进行密码加密的盐',
  `state` smallint(6) NOT NULL DEFAULT '1' COMMENT '用户状态。0：待验证；1：正常；2：被锁定',
  PRIMARY KEY (`userId`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8;

/*Data for the table `sysuser` */

insert  into `sysuser`(`userId`,`userName`,`userAlias`,`password`,`salt`,`state`) values (1,'admin','管理员','d3c59d25033dbf980d29554025c23a75','8d78869f470951332959580424d4bf4f',1),(2,'lisa','丽莎','504bd6934290bcd84c902441f3c58843','8d78869f470951332959580424d4bf4f',1),(3,'mary','玛丽','dd9775b3e68ce4e8e9bf6234bcc7bd91','8d78869f470951332959580424d4bf4f',1),(4,'jack','杰克','fdbb4d553f64238ab75bf746287cbc23','8d78869f470951332959580424d4bf4f',1),(5,'lin','兴有林栖','75932fcd04e0ab82d6ffa9af0546bafc','8d78869f470951332959580424d4bf4f',1),(6,'tie','铁昊','fda57fc64356ab7850196343d2b2014f','8d78869f470951332959580424d4bf4f',1),(7,'huang','黄声普','f6f702b78022030335092a849a212bde','8d78869f470951332959580424d4bf4f',1);

/*Table structure for table `user_role` */

DROP TABLE IF EXISTS `user_role`;

CREATE TABLE `user_role` (
  `roleId` bigint(20) NOT NULL,
  `userId` bigint(20) NOT NULL,
  PRIMARY KEY (`roleId`,`userId`),
  KEY `FK_user_role2` (`userId`),
  CONSTRAINT `FK_user_role` FOREIGN KEY (`roleId`) REFERENCES `sysrole` (`roleId`),
  CONSTRAINT `FK_user_role2` FOREIGN KEY (`userId`) REFERENCES `sysuser` (`userId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `user_role` */

insert  into `user_role`(`roleId`,`userId`) values (1,1),(2,2),(5,3),(2,4),(1,5),(3,6),(4,7);

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
