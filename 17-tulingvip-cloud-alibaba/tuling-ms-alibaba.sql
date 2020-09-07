/*
Navicat MySQL Data Transfer

Source Server         : 本地mysql
Source Server Version : 50726
Source Host           : localhost:3306
Source Database       : tuling-ms-alibaba

Target Server Type    : MYSQL
Target Server Version : 50726
File Encoding         : 65001

Date: 2020-02-06 13:59:47
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for order_info
-- ----------------------------
DROP TABLE IF EXISTS `order_info`;
CREATE TABLE `order_info` (
  `order_no` varchar(50) NOT NULL,
  `user_name` varchar(50) DEFAULT NULL,
  `create_dt` date DEFAULT NULL,
  `product_no` varchar(50) DEFAULT NULL,
  `product_count` int(10) DEFAULT NULL,
  PRIMARY KEY (`order_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of order_info
-- ----------------------------
INSERT INTO `order_info` VALUES ('1', 'zhangsanfen', '2019-11-15', '1', '1');
INSERT INTO `order_info` VALUES ('1578659334078', 'admin', '2020-01-10', '1', '1');
INSERT INTO `order_info` VALUES ('1578659393746', 'admin', '2020-01-10', '1', '1');
INSERT INTO `order_info` VALUES ('1578659628557', 'admin', '2020-01-10', '', '1');
INSERT INTO `order_info` VALUES ('1578659702791', 'admin', '2020-01-10', null, '1');
INSERT INTO `order_info` VALUES ('1578659791054', 'admin', '2020-01-10', null, '1');
INSERT INTO `order_info` VALUES ('1578659899513', 'admin', '2020-01-10', '', '1');
INSERT INTO `order_info` VALUES ('1578660065908', 'admin', '2020-01-10', '', '1');
INSERT INTO `order_info` VALUES ('1578660313739', 'admin', '2020-01-10', '2', '1');
INSERT INTO `order_info` VALUES ('1578662046898', 'admin', '2020-01-10', '2', '1');
INSERT INTO `order_info` VALUES ('1578662483782', 'admin', '2020-01-10', '2', '1');
INSERT INTO `order_info` VALUES ('1578662550298', 'admin', '2020-01-10', '2', '1');
INSERT INTO `order_info` VALUES ('1578662657642', 'admin', '2020-01-10', '2', '1');
INSERT INTO `order_info` VALUES ('1578662873258', 'admin', '2020-01-10', '2', '1');
INSERT INTO `order_info` VALUES ('1578662965452', 'admin', '2020-01-10', '2', '1');
INSERT INTO `order_info` VALUES ('1578663091132', 'admin', '2020-01-10', '2', '1');
INSERT INTO `order_info` VALUES ('1578663220769', 'admin', '2020-01-10', '2', '1');
INSERT INTO `order_info` VALUES ('1578663869212', 'admin', '2020-01-10', '2', '1');
INSERT INTO `order_info` VALUES ('1578664235456', 'admin', '2020-01-10', '2', '1');
INSERT INTO `order_info` VALUES ('1578664306210', 'admin', '2020-01-10', '2', '1');
INSERT INTO `order_info` VALUES ('1578664387608', 'admin', '2020-01-10', '2', '1');
INSERT INTO `order_info` VALUES ('1578665001620', 'admin', '2020-01-10', '2', '1');
INSERT INTO `order_info` VALUES ('1578665104123', 'admin', '2020-01-10', '2', '1');
INSERT INTO `order_info` VALUES ('1578665173370', 'admin', '2020-01-10', '2', '1');
INSERT INTO `order_info` VALUES ('1578665254702', 'admin', '2020-01-10', '2', '1');
INSERT INTO `order_info` VALUES ('1578665315965', 'admin', '2020-01-10', '2', '1');
INSERT INTO `order_info` VALUES ('1578665475227', 'admin', '2020-01-10', '2', '1');
INSERT INTO `order_info` VALUES ('1578665589303', 'admin', '2020-01-10', '2', '1');
INSERT INTO `order_info` VALUES ('1578665608013', 'admin', '2020-01-10', '2', '1');
INSERT INTO `order_info` VALUES ('1578665772428', 'admin', '2020-01-10', '2', '1');
INSERT INTO `order_info` VALUES ('1578667672812', 'admin', '2020-01-10', '2', '1');
INSERT INTO `order_info` VALUES ('1578667789257', 'admin', '2020-01-10', '2', '1');
INSERT INTO `order_info` VALUES ('1578833209688', 'admin', '2020-01-12', '1', '1');
INSERT INTO `order_info` VALUES ('1578837601473', 'admin', '2020-01-12', '2', '1');
INSERT INTO `order_info` VALUES ('1578839359061', 'admin', '2020-01-12', '1', '1');
INSERT INTO `order_info` VALUES ('1578839612451', 'admin', '2020-01-12', '2', '1');
INSERT INTO `order_info` VALUES ('1578839676992', 'admin', '2020-01-12', '1', '1');
INSERT INTO `order_info` VALUES ('1578839713894', 'admin', '2020-01-12', '1', '1');
INSERT INTO `order_info` VALUES ('1578839765073', 'admin', '2020-01-12', '1', '1');
INSERT INTO `order_info` VALUES ('1578882703514', 'admin', '2020-01-13', '2', '1');
INSERT INTO `order_info` VALUES ('1578882712629', 'admin', '2020-01-13', '1', '1');
INSERT INTO `order_info` VALUES ('1578895248038', 'admin', '2020-01-13', '1', '1');
INSERT INTO `order_info` VALUES ('1578896142178', 'admin', '2020-01-13', '1', '1');
INSERT INTO `order_info` VALUES ('1579096540389', 'admin', '2020-01-15', '2', '1');
INSERT INTO `order_info` VALUES ('1579180092960', null, '2020-01-16', '1', '1');
INSERT INTO `order_info` VALUES ('1579180127154', null, '2020-01-16', '1', '1');
INSERT INTO `order_info` VALUES ('1579180150186', null, '2020-01-16', '1', '1');
INSERT INTO `order_info` VALUES ('1579180182930', null, '2020-01-16', '1', '1');
INSERT INTO `order_info` VALUES ('1579180295846', null, '2020-01-16', '1', '1');
INSERT INTO `order_info` VALUES ('1579180363472', null, '2020-01-16', '1', '1');
INSERT INTO `order_info` VALUES ('1579180375269', null, '2020-01-16', '1', '1');
INSERT INTO `order_info` VALUES ('1579181128075', 'admin', '2020-01-16', '1', '1');
INSERT INTO `order_info` VALUES ('1579181397437', null, '2020-01-16', '2', '1');
INSERT INTO `order_info` VALUES ('1579181434931', null, '2020-01-16', '2', '1');
INSERT INTO `order_info` VALUES ('1579181852398', 'admin', '2020-01-16', '2', '1');
INSERT INTO `order_info` VALUES ('1579182038939', 'admin', '2020-01-16', '2', '1');
INSERT INTO `order_info` VALUES ('1579182184284', null, '2020-01-16', '2', '1');
INSERT INTO `order_info` VALUES ('1579182577868', null, '2020-01-16', '2', '1');
INSERT INTO `order_info` VALUES ('1579182663298', null, '2020-01-16', '2', '1');
INSERT INTO `order_info` VALUES ('1579182974222', null, '2020-01-16', '1', '1');
INSERT INTO `order_info` VALUES ('1579183049367', 'admin', '2020-01-16', '1', '1');
INSERT INTO `order_info` VALUES ('1579183234525', null, '2020-01-16', '1', '1');
INSERT INTO `order_info` VALUES ('1579183372215', null, '2020-01-16', '1', '1');
INSERT INTO `order_info` VALUES ('1579183461114', null, '2020-01-16', '2', '1');
INSERT INTO `order_info` VALUES ('1579183787188', 'admin', '2020-01-16', '1', '1');
INSERT INTO `order_info` VALUES ('1579183913331', null, '2020-01-16', '1', '1');
INSERT INTO `order_info` VALUES ('1579184324682', null, '2020-01-16', '1', '1');
INSERT INTO `order_info` VALUES ('1579184655824', 'admin', '2020-01-16', '1', '1');
INSERT INTO `order_info` VALUES ('1579416630673', 'admin', '2020-01-19', '2', '1');
INSERT INTO `order_info` VALUES ('1579416634146', 'admin', '2020-01-19', '2', '1');
INSERT INTO `order_info` VALUES ('1579416636033', 'admin', '2020-01-19', '2', '1');
INSERT INTO `order_info` VALUES ('1579416732075', 'admin', '2020-01-19', '2', '1');
INSERT INTO `order_info` VALUES ('1579416752094', 'admin', '2020-01-19', '2', '1');
INSERT INTO `order_info` VALUES ('1579416771658', 'admin', '2020-01-19', '2', '1');
INSERT INTO `order_info` VALUES ('1579416784391', 'admin', '2020-01-19', '2', '1');
INSERT INTO `order_info` VALUES ('1579416794292', 'admin', '2020-01-19', '2', '1');
INSERT INTO `order_info` VALUES ('1579490324242', 'admin', '2020-01-20', '2', '1');
INSERT INTO `order_info` VALUES ('1579491057613', 'admin', '2020-01-20', '2', '1');
INSERT INTO `order_info` VALUES ('1579491094655', 'admin', '2020-01-20', '2', '1');
INSERT INTO `order_info` VALUES ('1579520794516', 'admin', '2020-01-20', '2', '1');
INSERT INTO `order_info` VALUES ('2', 'smlz', '2019-11-27', '2', '2');
INSERT INTO `order_info` VALUES ('3', 'lisi', '2019-11-26', '1', '2');
INSERT INTO `order_info` VALUES ('4', 'admin', '2020-01-10', '1', '1');

-- ----------------------------
-- Table structure for pay_info
-- ----------------------------
DROP TABLE IF EXISTS `pay_info`;
CREATE TABLE `pay_info` (
  `pay_no` varchar(50) NOT NULL,
  `order_no` varchar(50) DEFAULT NULL,
  `user_name` varchar(50) DEFAULT NULL,
  `pay_time` date DEFAULT NULL,
  PRIMARY KEY (`pay_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of pay_info
-- ----------------------------
INSERT INTO `pay_info` VALUES ('1', '1', 'zhangsan', '2019-11-20');
INSERT INTO `pay_info` VALUES ('2', '2', 'zhangsan', '2019-11-22');

-- ----------------------------
-- Table structure for product_info
-- ----------------------------
DROP TABLE IF EXISTS `product_info`;
CREATE TABLE `product_info` (
  `product_no` varchar(50) NOT NULL,
  `product_name` varchar(50) DEFAULT NULL,
  `product_store` int(10) DEFAULT NULL,
  `product_price` decimal(10,2) DEFAULT NULL,
  PRIMARY KEY (`product_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of product_info
-- ----------------------------
INSERT INTO `product_info` VALUES ('1', 'iphone11', '100', '5999.00');
INSERT INTO `product_info` VALUES ('2', '华为meta30', '500', '4999.00');
