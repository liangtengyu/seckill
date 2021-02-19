-- MySQL dump 10.13  Distrib 5.7.29, for osx10.14 (x86_64)
--
-- Host: 127.0.0.1    Database: seckill
-- ------------------------------------------------------
-- Server version	5.7.29

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `goods_all`
--

DROP TABLE IF EXISTS `goods_all`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `goods_all` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '商品ID',
  `goods_name` varchar(30) DEFAULT NULL COMMENT '商品名称',
  `goods_title` varchar(64) DEFAULT NULL COMMENT '商品标题',
  `goods_img` varchar(64) DEFAULT NULL COMMENT '商品图片',
  `goods_detail` longtext COMMENT '商品详情',
  `goods_price` decimal(10,2) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `goods_all`
--

LOCK TABLES `goods_all` WRITE;
/*!40000 ALTER TABLE `goods_all` DISABLE KEYS */;
INSERT INTO `goods_all` VALUES (1,'iphone12 pro max','Apple/苹果iPhone12 pro max 全网通5G手机苹果','/img/iphonex.png','Apple/苹果iPhone12 pro max 全网通5G手机苹果',9998.00),(2,'华为 Mate 10','Huawei/华为 Mate 10 6G+128G 全网通4G智能手机','/img/meta10.png','Huawei/华为 Mate 10 6G+128G 全网通4G智能手机',2111.00),(3,'小米2s','小米/2s','/img/xiaomi2s.png','小米/2s/全网通3G智能手机',1999.00);
/*!40000 ALTER TABLE `goods_all` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `goods_seckill`
--

DROP TABLE IF EXISTS `goods_seckill`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `goods_seckill` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '秒杀商品id',
  `goods_id` bigint(20) DEFAULT NULL COMMENT '商品id',
  `seckill_price` decimal(10,2) DEFAULT '0.00' COMMENT '秒杀价',
  `stock_count` int(11) DEFAULT NULL COMMENT '库存数量',
  `start_date` datetime DEFAULT NULL COMMENT '秒杀开始时间',
  `end_date` datetime DEFAULT NULL COMMENT '秒杀结束时间',
  `version` int(11) DEFAULT NULL COMMENT '并发版本控制',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `goods_seckill`
--

LOCK TABLES `goods_seckill` WRITE;
/*!40000 ALTER TABLE `goods_seckill` DISABLE KEYS */;
INSERT INTO `goods_seckill` VALUES (1,1,1.00,100,'2018-05-22 17:22:52','2018-05-22 18:23:00',0),(2,2,1.00,100,'2018-04-29 22:56:10','2018-05-01 22:56:15',0);
/*!40000 ALTER TABLE `goods_seckill` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `order_info_seckill`
--

DROP TABLE IF EXISTS `order_info_seckill`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `order_info_seckill` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `username` varchar(40) DEFAULT NULL,
  `goods_id` bigint(20) DEFAULT NULL,
  `goods_name` varchar(30) DEFAULT NULL,
  `goods_count` int(11) DEFAULT NULL,
  `goods_price` decimal(10,2) DEFAULT NULL,
  `order_channel` tinyint(4) DEFAULT NULL COMMENT '订单渠道，1在线，2android，3ios',
  `status` tinyint(4) DEFAULT NULL COMMENT '订单状态，0新建未支付，1已支付，2已发货，3已收货，4已退款，5已完成',
  `create_date` datetime DEFAULT NULL,
  `pay_date` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `order_info_seckill`
--

LOCK TABLES `order_info_seckill` WRITE;
/*!40000 ALTER TABLE `order_info_seckill` DISABLE KEYS */;
/*!40000 ALTER TABLE `order_info_seckill` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `order_seckill`
--

DROP TABLE IF EXISTS `order_seckill`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `order_seckill` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `username` varchar(40) DEFAULT NULL,
  `order_id` varchar(40) DEFAULT NULL,
  `goods_id` bigint(20) DEFAULT NULL,
  `order_info_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `u_uid_gid` (`username`,`goods_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `order_seckill`
--

LOCK TABLES `order_seckill` WRITE;
/*!40000 ALTER TABLE `order_seckill` DISABLE KEYS */;
/*!40000 ALTER TABLE `order_seckill` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user` (
  `id` bigint(20) unsigned NOT NULL COMMENT '用户id',
  `nickname` varchar(255) NOT NULL COMMENT '昵称',
  `password` varchar(32) DEFAULT NULL COMMENT 'MD5(MD5(pass明文+固定salt)+salt',
  `salt` varchar(10) DEFAULT NULL COMMENT '混淆盐',
  `head` varchar(128) DEFAULT NULL COMMENT '头像，云存储的ID',
  `register_date` datetime DEFAULT NULL COMMENT '注册时间',
  `last_login_date` datetime DEFAULT NULL COMMENT '上次登录时间',
  `login_count` int(11) DEFAULT NULL COMMENT '登录次数',
  `address` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (1,'lty','b3bce039f10c241b7dcaa6421af77412','1a2b3c4d',NULL,'2018-05-21 21:10:21','2021-05-21 21:10:25',1,'浙江省宁波市江东区'),(2,'liangtengyu','b7797cce01b4b131b433b6acf4add449','1a2b3c4d',NULL,'2018-05-21 21:10:21','2021-05-21 21:10:25',1,'浙江省宁波市江东区');
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2021-02-19 14:23:51
