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
) ENGINE=InnoDB AUTO_INCREMENT=105 DEFAULT CHARSET=utf8 COMMENT='变压器产品或变压器构件';

/*Data for the table `artifact` */

insert  into `artifact`(`artifactId`,`artifactName`,`artifactCode`,`weight`,`materialCode`,`materialName`,`productFlag`,`productModel`,`canBeSplit`,`artifactMemo`) values (1,'变压器明细','ZB8169-411.00MX',5468,'','',0,'SCB11-2500/10.5',1,NULL),(2,'变压器','ZB8169-411.00',5480,'','',1,'SCB11-2500/10.5',0,NULL),(3,'铁芯','ZB8169-411.03.01',3341,'27Q110','电工钢带',1,'SCB11-2500/10.5',0,NULL),(4,'出厂文件','ZB8169-411CW',NULL,'','',1,'SCB11-2500/10.5',0,NULL),(5,'技术条件','ZB8169-411JT',NULL,'','',1,'SCB11-2500/10.5',0,NULL),(6,'本体铭牌标志图','ZB8169-411MP',NULL,'','',1,'SCB11-2500/10.5',0,NULL),(7,'图样目录','ZB8169-411TM',NULL,'','',1,'SCB11-2500/10.5',0,NULL),(8,'变压器外形图','ZB8169-411WX',5480,'','',1,'SCB11-2500/10.5',0,NULL),(9,'高压出线布置图','ZG8168-411.10.01',NULL,'','',1,'SCB11-2500/10.5',0,NULL),(10,'高压上夹件','ZG8169-411.03.02',48.71,'','',1,'SCB11-2500/10.5',1,NULL),(11,'低压上夹件','ZG8169-411.03.03',48.53,'','',1,'SCB11-2500/10.5',1,NULL),(12,'下夹件','ZG8169-411.03.04',47.74,'','',1,'SCB11-2500/10.5',1,NULL),(13,'夹件绝缘','ZG8169-411.03.06',1.32,'','',1,'SCB11-2500/10.5',1,NULL),(14,'底脚绝缘','ZG8169-411.03.09',0.6,'JA330-8.0','硅橡胶',1,'SCB11-2500/10.5',0,NULL),(15,'铁芯装配','ZG8169-411.03',3685,'','',1,'SCB11-2500/10。5',1,NULL),(16,'零线母排','ZG8169-411.05.01',21,'TMY-15*120','铜排',1,'SCB11-2500/10.5',0,NULL),(17,'高、低压引线明细','ZG8169-411.05MX',27,'UK=6%','Dyn11;±2*2.5%',0,'SCB11-2500/10.5',1,NULL),(18,'高、低压引线','ZG8169-411.05',0,'Uk=6%','Dyn11;±2*2.5%',1,'SCB11-2500/10.5',0,NULL),(19,'高压线圈','ZG8169-411.10',323.08,'D接','10.5±2*2.5%',1,'SCB11-2500/10.5',1,NULL),(20,'内母排','ZG8169-411.13.01',20,'TMY-15*120','母排',1,'SCB11-2500/10.5',0,NULL),(21,'外母排','ZG8169-411.13.02',20,'TMY-15*120','母排',1,'SCB11-2500/10.5',0,NULL),(22,'低压线圈','ZG8169-411.13',251.8,'Y接','400V',1,'SCB11-2500/10.5',1,NULL),(23,'温控传感线走线','TZ20.01',0,'',NULL,1,NULL,0,NULL),(24,'温控器','BWDK-3206D',0,'',NULL,1,NULL,0,NULL),(25,'铭牌底板','TL11.02',0,'','钢材',1,NULL,0,NULL),(26,'本体铭牌标志图','ZG8169-411MP',0,'',NULL,1,NULL,0,NULL),(27,'铝合金抽芯铆钉','GB3098',0,'%%C4.8×25',NULL,1,NULL,0,NULL),(28,'上铁轭大垫块  \\P','TL04.02',0,'M=22,L1=75,L=150,H=40',NULL,1,NULL,0,NULL),(29,'红色硅胶垫','',0,'6×60×67/66',NULL,1,NULL,0,NULL),(30,'镀锌螺栓','GB5782',0,'35#钢, M16×60',NULL,1,NULL,0,NULL),(31,'垫板','TL15.03',0,'',NULL,1,NULL,0,NULL),(32,'镀锌薄螺母','GB6172',0,'35#钢, M16',NULL,1,NULL,0,NULL),(33,'绝缘筒','',5,'1.1 PET，785×1737',NULL,1,NULL,0,NULL),(34,'下铁轭大垫块','TL04.04',16,'M=22,L1=75,L=150,H=70',NULL,1,NULL,0,NULL),(35,'硅胶棒','',0,'硅橡胶%%c20×40',NULL,1,NULL,0,NULL),(36,'冷却风机','GFDD520-120',10,'',NULL,1,NULL,0,NULL),(37,'镀锌盘头螺钉','GB823',0,'35#钢, M6×10',NULL,1,NULL,0,NULL),(38,'镀锌垫片','GB97',0,'35#钢, D6',NULL,1,NULL,0,NULL),(39,'镀锌弹簧垫片','GB859',0,'35#钢，D6',NULL,1,NULL,0,NULL),(40,'字  牌','TL07.01',0,'',NULL,1,NULL,0,NULL),(41,'字  牌','TL07.03',0,'',NULL,1,NULL,0,NULL),(42,'标志牌','TL07.06',0,'',NULL,1,NULL,0,NULL),(43,'标志牌','TL07.07',0,'',NULL,1,NULL,0,NULL),(44,'接地标志牌','TL07.11',0,'',NULL,1,NULL,0,NULL),(45,'PVC绝缘管','',0,'φ20,L=3200',NULL,1,NULL,0,NULL),(46,'直角弯头','',0,'PVC绝缘管%%c20',NULL,1,NULL,0,NULL),(47,'1.5平方铜芯电线','',0,'L=7500',NULL,1,NULL,0,NULL),(48,'尼龙扎带','',0,'L=100',NULL,1,NULL,0,NULL),(49,'钢板','',46,'δ10钢板Q235',NULL,1,NULL,0,NULL),(50,'支板','TL36.03',0.27,'',NULL,1,NULL,0,NULL),(51,'吊板','TL12.03',1.6,'δ16钢板',NULL,1,NULL,0,NULL),(52,'压钉螺母','TL13.02',0.24,'M16',NULL,1,NULL,0,NULL),(53,'销钉','TL39.06',0.6,'',NULL,1,NULL,0,NULL),(54,'固定板','TL36.07',0.09,'',NULL,1,NULL,0,NULL),(55,'','',46,'δ10钢板Q235',NULL,1,NULL,0,NULL),(56,'','',0.42,'圆钢Q235,%%C10×15',NULL,1,NULL,0,NULL),(57,'风机支架','',0.72,'δ6钢板 Q235',NULL,1,NULL,0,NULL),(58,'环氧垫块','',0.56,'环氧板3240,δ9×120×20',NULL,1,NULL,0,NULL),(59,'铁    芯','ZG8169-411.03.01',3341,'27Q110',NULL,1,NULL,0,NULL),(60,'接地片','TL14.01',0.014,'',NULL,1,NULL,0,NULL),(61,'镀锌螺栓','GB5783',0,'35#钢，M10×20',NULL,1,NULL,0,NULL),(62,'双头螺杆(穿心）','TL03.03',3,'M16×510',NULL,1,NULL,0,NULL),(63,'绝缘管(穿心）','TL35.04',0.2,'φ17-420',NULL,1,NULL,0,NULL),(64,'绝缘大垫圈(穿心）','TL27.03',0,'',NULL,1,NULL,0,NULL),(65,'镀锌螺母','GB6170',0,'35#钢，M16',NULL,1,NULL,0,NULL),(66,'拉  板','TL38.06',36.6,'780/1105，(δ8×80)',NULL,1,NULL,0,NULL),(67,'拉板绝缘','TL40.05',7.2,'90×1245',NULL,1,NULL,0,NULL),(68,'撑板','TL41.02',0.6,'30×895',NULL,1,NULL,0,NULL),(69,'镀锌方斜垫圈','GB853',0,'35#钢，D16',NULL,1,NULL,0,NULL),(70,'镀锌钢管','',0,'钢管Q235A，%%C35/42,L=71',NULL,1,NULL,0,NULL),(71,'底脚','TL26.16',88,'236.5/236.5/1070；30/340',NULL,1,NULL,0,NULL),(72,'120热缩管','MPRS-1KV',0,'交联PE阻燃材料，L=1400',NULL,1,NULL,0,NULL),(73,'镀锌弹簧垫圈','GB93',0,'35#钢，D16',NULL,1,NULL,0,NULL),(74,'低压绝缘子','TL33.02',0,'%%c60×60',NULL,1,NULL,0,NULL),(75,'全丝钢螺柱','HG/T20613',0,'35#钢，M12×35',NULL,1,NULL,0,NULL),(76,'镀锌垫片','GB97-85',0,'35#钢，D12',NULL,1,NULL,0,NULL),(77,'联结铜管','TL01.15',0,'L=797',NULL,1,NULL,0,NULL),(78,'联结铜管','TL01.18',0,'L=1403',NULL,1,NULL,0,NULL),(79,'硅胶螺栓塞座','',0,'大红色硅橡胶, M16×15',NULL,1,NULL,0,NULL),(80,'硅胶螺母护套','',0,'大红色硅橡胶，M16',NULL,1,NULL,0,NULL),(81,'联结片','TL02.03',0,'L=78',NULL,1,NULL,0,NULL),(82,'绝缘子装配(10KV)','TZ01.03',2.7,'80×130',NULL,1,NULL,0,NULL),(83,'软联结','TL01.11',3,'L=302',NULL,1,NULL,0,NULL),(84,'内径绝缘','',4.8,'网格  1.5×770×1800',NULL,1,NULL,0,NULL),(85,'扁铜线','',224.2,'FMB-35/155 2*8.5',NULL,1,NULL,0,NULL),(86,'层间绝缘','',9,'网格  0.5×180×101700',NULL,1,NULL,0,NULL),(87,'气道绝缘（内）','',1.6,'网格  1.0×770×1900',NULL,1,NULL,0,NULL),(88,'气道绝缘（外）','',1.7,'网格  1.0×770×2000',NULL,1,NULL,0,NULL),(89,'外径绝缘','',1.9,'网格  1.0×770×2200',NULL,1,NULL,0,NULL),(90,'无碱无蜡玻璃丝布带','',0,'EW0.2×30',NULL,1,NULL,0,NULL),(91,'环氧树脂','',78.2,'8088A/B',NULL,1,NULL,0,NULL),(92,'嵌 件','TL18.03',1.68,'M16',NULL,1,NULL,0,NULL),(93,'F级网状无纬带','',0,'0.2×50',NULL,1,NULL,0,NULL),(94,'层间绝缘','',4,'DMD预浸料,0.2×778×24000',NULL,1,NULL,0,NULL),(95,'铜箔','T2',196.2,'1.35×750',NULL,1,NULL,0,NULL),(96,'端部绝缘','',1.1,'DMD预浸料,(0.4×7)×14',NULL,1,NULL,0,NULL),(97,'气道条','TL42.05',10.2,'树脂引拔件,16×14×775',NULL,1,NULL,0,NULL),(98,'绝缘管','',0,'热敏电阻管 %%C6×100',NULL,1,NULL,0,NULL),(99,'绝缘带','',0,'DMD预浸料,0.2×30',NULL,1,NULL,0,NULL),(100,'绝缘子','TL33.14',0.3,'%%C60×90',NULL,1,NULL,0,NULL),(102,'测试','CeShi',1000,'Ceshi','Ceshi',0,'Certain',1,NULL),(103,'测试零件','T0001',10,'嘻嘻嘻','哈哈和',1,NULL,1,NULL),(104,'学习系','T0002',0,'的发生','啊手动阀',1,NULL,1,NULL);

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
  `quota` varchar(255) DEFAULT NULL COMMENT '定额（会签）',
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
) ENGINE=InnoDB AUTO_INCREMENT=109 DEFAULT CHARSET=utf8;

/*Data for the table `artifactdetail` */

insert  into `artifactdetail`(`detailId`,`parentArtifactId`,`artifactId`,`number`,`needSplit`,`dimension`,`unit`,`quota`,`workingSteps`,`classificationSign`,`processSign`,`inspector`,`detailMemo`) values (1,1,23,1,0,'10*10*10','kg','1.00','器身','1',NULL,'admin',''),(2,1,24,1,0,'10*10*10','kg','2.00','绝缘',NULL,NULL,'admin','(特殊情况时见联络单)'),(3,1,25,1,0,'10*10*10','kg','9.02','总装',NULL,NULL,'admin','(带外壳时不用)'),(4,1,26,1,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'(带外壳时不用)'),(5,1,27,4,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL,''),(6,1,15,1,1,NULL,NULL,NULL,NULL,NULL,NULL,NULL,''),(7,1,19,3,1,NULL,NULL,NULL,NULL,NULL,NULL,NULL,''),(8,1,22,3,1,NULL,NULL,NULL,NULL,NULL,NULL,NULL,''),(9,1,18,1,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL,''),(10,1,28,12,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL,''),(11,1,29,0,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'现场配置'),(12,1,30,12,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL,''),(13,1,31,12,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL,''),(14,1,32,12,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL,''),(15,1,33,3,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'搭接100双面胶封口'),(16,1,34,12,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL,''),(17,1,35,24,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'配做塞紧套装间隙'),(18,1,36,6,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL,''),(19,1,37,24,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL,''),(20,1,38,24,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL,''),(21,1,39,24,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL,''),(22,1,40,1,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'高压A/B/C相序标识'),(23,1,41,1,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'低压a/b/c/0相序标识'),(24,1,42,1,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'小心有电'),(25,1,43,1,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'分接标识'),(26,1,44,1,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'接地标识'),(27,1,45,1,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL,''),(28,1,46,2,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL,''),(29,1,47,1,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL,''),(30,1,48,0,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL,''),(31,10,49,1,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL,''),(32,10,50,3,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL,''),(33,10,51,2,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL,''),(34,10,52,6,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL,''),(35,10,53,3,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL,''),(36,11,49,1,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL,''),(37,11,54,3,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL,''),(38,11,51,2,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL,''),(39,11,52,6,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL,''),(40,11,53,3,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL,''),(41,12,55,1,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL,''),(42,12,56,6,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'见图'),(43,12,57,6,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'见图'),(44,12,53,3,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL,''),(45,13,58,14,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL,''),(46,15,59,1,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL,''),(47,15,60,1,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL,''),(48,15,61,1,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL,''),(49,15,38,1,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL,''),(50,15,62,4,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL,''),(51,15,63,4,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL,''),(52,15,64,0,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL,''),(53,15,65,8,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL,''),(54,15,32,8,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL,''),(55,15,10,1,1,NULL,NULL,NULL,NULL,NULL,NULL,NULL,''),(56,15,11,1,1,NULL,NULL,NULL,NULL,NULL,NULL,NULL,''),(57,15,13,4,1,NULL,NULL,NULL,NULL,NULL,NULL,NULL,''),(58,15,66,6,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL,''),(59,15,67,6,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL,''),(60,15,68,3,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL,''),(61,15,12,2,1,NULL,NULL,NULL,NULL,NULL,NULL,NULL,''),(62,15,30,8,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL,''),(63,15,69,8,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL,''),(64,15,70,8,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL,''),(65,15,14,2,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL,''),(66,15,71,2,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL,''),(67,17,16,1,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL,''),(68,17,72,1,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'黑(零线）'),(69,17,30,12,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL,''),(70,17,38,24,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL,''),(71,17,73,12,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL,''),(72,17,65,12,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL,''),(73,17,32,12,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL,''),(74,17,74,3,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL,''),(75,17,61,6,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL,''),(76,17,75,3,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'绝缘子对装'),(77,17,76,6,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL,''),(78,17,77,2,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'现场配做'),(79,17,78,1,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'现场配做'),(80,17,79,6,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL,''),(81,17,80,6,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL,''),(82,17,81,3,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL,''),(83,17,82,3,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'M16/M16'),(84,17,83,3,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL,''),(85,19,84,2,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'大红色'),(86,19,85,1,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'膜包布包'),(87,19,86,1,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'大红色'),(88,19,87,1,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'大红色'),(89,19,88,1,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'大红色'),(90,19,89,1,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'大红色'),(91,19,90,0,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL,''),(92,19,91,1,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL,''),(93,19,92,8,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL,''),(94,22,38,1,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL,''),(95,22,39,1,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL,''),(96,22,93,0,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL,''),(97,22,94,1,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL,''),(98,22,20,1,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL,''),(99,22,95,1,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL,''),(100,22,96,2,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL,''),(101,22,97,34,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL,''),(102,22,98,1,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL,''),(103,22,99,0,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL,''),(104,22,21,1,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL,''),(105,22,100,1,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL,''),(106,22,61,2,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL,''),(107,102,103,2,0,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'哈哈'),(108,102,104,5,1,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'');

/*Table structure for table `needsplitprefix` */

DROP TABLE IF EXISTS `needsplitprefix`;

CREATE TABLE `needsplitprefix` (
  `prefix_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `prefix_label` varchar(50) NOT NULL,
  `create_time` datetime DEFAULT NULL,
  `create_by` varchar(255) DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `update_by` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`prefix_id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8;

/*Data for the table `needsplitprefix` */

insert  into `needsplitprefix`(`prefix_id`,`prefix_label`,`create_time`,`create_by`,`update_time`,`update_by`) values (1,'1ZT',NULL,NULL,NULL,NULL),(2,'2ZT',NULL,NULL,NULL,NULL),(3,'3ZT',NULL,NULL,NULL,NULL),(4,'4ZT',NULL,NULL,NULL,NULL),(5,'5ZT',NULL,NULL,NULL,NULL),(6,'6ZT',NULL,NULL,NULL,NULL),(7,'7ZT',NULL,NULL,NULL,NULL);

/*Table structure for table `quotaformula` */

DROP TABLE IF EXISTS `quotaformula`;

CREATE TABLE `quotaformula` (
  `formula_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `material_code` varchar(255) DEFAULT NULL COMMENT '材料代码',
  `material_name` varchar(255) DEFAULT NULL COMMENT '材料名称',
  `unit` varchar(255) DEFAULT NULL COMMENT '单位',
  `feature` float DEFAULT NULL COMMENT '材料特性，如密度g/cm^3',
  `formula_factor` float NOT NULL DEFAULT '1' COMMENT '定额计算公式（体积*密度）乘积因素，如0.87',
  `create_time` datetime DEFAULT NULL,
  `create_by` varchar(255) DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `update_by` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`formula_id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8;

/*Data for the table `quotaformula` */

insert  into `quotaformula`(`formula_id`,`material_code`,`material_name`,`unit`,`feature`,`formula_factor`,`create_time`,`create_by`,`update_time`,`update_by`) values (1,NULL,'硅钢片','kg',7.65,1,NULL,NULL,NULL,NULL),(2,NULL,'电磁线','kg',8.89,1,NULL,NULL,NULL,NULL),(3,NULL,'低磁钢板','kg',7.63,0.87,NULL,NULL,NULL,NULL),(4,NULL,'钢材','kg',7.85,0.87,NULL,NULL,NULL,NULL),(5,NULL,'铜母线','kg',8.89,1,NULL,NULL,NULL,NULL),(6,NULL,'绝缘纸板','kg',1.15,0.87,NULL,NULL,NULL,NULL),(7,NULL,'层压纸板T4','kg',1.25,0.87,NULL,NULL,NULL,NULL),(8,NULL,'层压木','kg',1.1,0.87,NULL,NULL,NULL,NULL),(9,NULL,'变压器油','kg',0.886,1,NULL,NULL,NULL,NULL),(10,NULL,'绝缘点胶纸','kg',0.85,0.92,NULL,NULL,NULL,NULL),(11,NULL,'电缆纸','kg',0.85,0.92,NULL,NULL,NULL,NULL),(12,NULL,'环酚玻布板3240','kg',1.9,1,NULL,NULL,NULL,NULL),(13,NULL,'环酚玻布管3640','kg',1.4,1,NULL,NULL,NULL,NULL);

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

insert  into `role_permission`(`permissionId`,`roleId`) values (1,1),(2,1),(3,1),(4,1),(5,1),(6,1),(7,1),(8,1),(9,1),(10,1),(11,1),(12,1),(13,1),(14,1),(15,1),(16,1),(17,1),(18,1),(19,1),(20,1),(21,1),(22,1),(23,1),(24,1),(5,2),(9,2),(15,2),(5,3),(6,3),(7,3),(8,3),(9,3),(10,3),(12,3),(13,3),(15,3),(5,4),(9,4),(13,4),(15,4),(5,5),(9,5),(14,5),(15,5),(16,5);

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

insert  into `sysrole`(`roleId`,`roleName`,`description`,`roleAvailable`) values (1,'Administrator','系统管理员',1),(2,'Staff','普通员工',1),(3,'Manager','产品管理员',1),(4,'Uploader','明细入库员',1),(5,'Inspector','明细会签员',1);

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
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;

/*Data for the table `sysuser` */

insert  into `sysuser`(`userId`,`userName`,`userAlias`,`password`,`salt`,`state`) values (1,'admin','管理员','d3c59d25033dbf980d29554025c23a75','8d78869f470951332959580424d4bf4f',1),(2,'lisa','丽莎','504bd6934290bcd84c902441f3c58843','8d78869f470951332959580424d4bf4f',1),(3,'lin','兴有林栖','20060cbd1e7a0a83a6071f88483bbf5f','1599896405646',1),(4,'tie','铁昊','56b6387fa1636ab92aa0200fa3caac6d','1599896448286',1),(5,'huang','黄声普','3f99143cccb72e3bdf2a64028bca1af7','1599896464936',1),(6,'jack','杰克','f57770473d0395aaff2d5ef049be20d8','1599896491392',1);

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

insert  into `user_role`(`roleId`,`userId`) values (1,1),(2,2),(1,3),(3,4),(4,5),(5,6);

/*Table structure for table `workingsteps` */

DROP TABLE IF EXISTS `workingsteps`;

CREATE TABLE `workingsteps` (
  `step_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `step_name` varchar(255) NOT NULL,
  `create_time` datetime DEFAULT NULL,
  `create_by` varchar(255) DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `update_by` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`step_id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8;

/*Data for the table `workingsteps` */

insert  into `workingsteps`(`step_id`,`step_name`,`create_time`,`create_by`,`update_time`,`update_by`) values (1,'铁心',NULL,NULL,NULL,NULL),(2,'绝缘',NULL,NULL,NULL,NULL),(3,'线圈',NULL,NULL,NULL,NULL),(4,'采购',NULL,NULL,NULL,NULL),(5,'钣焊',NULL,NULL,NULL,NULL),(6,'器身',NULL,NULL,NULL,NULL),(7,'总装',NULL,NULL,NULL,NULL),(8,'配变',NULL,NULL,NULL,NULL),(9,'检验',NULL,NULL,NULL,NULL),(10,'试验',NULL,NULL,NULL,NULL),(11,'营销',NULL,NULL,NULL,NULL);

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
