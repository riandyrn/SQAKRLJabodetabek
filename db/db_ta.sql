/*
Navicat MySQL Data Transfer

Source Server         : mylocal
Source Server Version : 50616
Source Host           : localhost:3306
Source Database       : db_ta

Target Server Type    : MYSQL
Target Server Version : 50616
File Encoding         : 65001

Date: 2015-04-13 07:14:23
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for jadwal_keberangkatan
-- ----------------------------
DROP TABLE IF EXISTS `jadwal_keberangkatan`;
CREATE TABLE `jadwal_keberangkatan` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `id_jurusan` varchar(255) DEFAULT NULL,
  `waktu_berangkat` time DEFAULT NULL,
  `stasiun_asal` varchar(255) DEFAULT NULL,
  `stasiun_tujuan` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `coverage` (`stasiun_asal`,`stasiun_tujuan`,`waktu_berangkat`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=latin1;

-- ----------------------------
-- Records of jadwal_keberangkatan
-- ----------------------------
INSERT INTO `jadwal_keberangkatan` VALUES ('1', '1', '15:00:00', 'bogor', 'cilebut');
INSERT INTO `jadwal_keberangkatan` VALUES ('2', '1', '05:05:00', 'cilebut', 'bojong_gede');
INSERT INTO `jadwal_keberangkatan` VALUES ('3', '1', '05:10:00', 'bojong_gede', 'citayam');
INSERT INTO `jadwal_keberangkatan` VALUES ('4', '1', '05:15:00', 'citayam', 'depok');
INSERT INTO `jadwal_keberangkatan` VALUES ('5', '1', '05:00:00', 'depok', 'citayam');
INSERT INTO `jadwal_keberangkatan` VALUES ('6', '1', '05:05:00', 'citayam', 'bojong_gede');
INSERT INTO `jadwal_keberangkatan` VALUES ('7', '1', '05:10:00', 'bojong_gede', 'cilebut');
INSERT INTO `jadwal_keberangkatan` VALUES ('8', '1', '05:15:00', 'cilebut', 'bogor');
INSERT INTO `jadwal_keberangkatan` VALUES ('9', '1', '05:00:00', 'bogor', 'cilebut');
INSERT INTO `jadwal_keberangkatan` VALUES ('10', '1', '21:00:00', 'bogor', 'cilebut');

-- ----------------------------
-- Table structure for jurusan
-- ----------------------------
DROP TABLE IF EXISTS `jurusan`;
CREATE TABLE `jurusan` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `deskripsi` text NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=latin1;

-- ----------------------------
-- Records of jurusan
-- ----------------------------
INSERT INTO `jurusan` VALUES ('1', 'Bogor / Depok - Manggarai - Jakarta Kota (PP)');
INSERT INTO `jurusan` VALUES ('2', 'Bogor / Depok - Tanah Abang - Pasar Senen - Jatinegara (PP)');
INSERT INTO `jurusan` VALUES ('3', 'Bekasi - Jatinegara - Manggarai - Jakarta Kota (PP)');
INSERT INTO `jurusan` VALUES ('4', 'Maja / Parung Panjang / Serpong - Tanah Abang (PP)');
INSERT INTO `jurusan` VALUES ('5', 'Tangerang - Duri (PP)');
INSERT INTO `jurusan` VALUES ('6', 'Tanjung Priok - Jakarta Kota (PP)');

-- ----------------------------
-- Table structure for rute
-- ----------------------------
DROP TABLE IF EXISTS `rute`;
CREATE TABLE `rute` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `stasiun_asal` varchar(255) NOT NULL,
  `stasiun_tujuan` varchar(255) NOT NULL,
  `terminal` text,
  `rincian` longtext,
  PRIMARY KEY (`id`),
  KEY `start_end` (`stasiun_asal`,`stasiun_tujuan`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=latin1;

-- ----------------------------
-- Records of rute
-- ----------------------------
INSERT INTO `rute` VALUES ('1', 'bogor', 'cilebut', null, null);
INSERT INTO `rute` VALUES ('2', 'bogor', 'cikini', 'manggarai', null);
INSERT INTO `rute` VALUES ('3', 'bogor', 'tangerang', 'manggarai, tanah_abang, duri', null);
INSERT INTO `rute` VALUES ('4', 'bogor', 'depok', null, 'cilebut, bojong_gede, citayam');
INSERT INTO `rute` VALUES ('5', 'depok', 'bogor', null, 'citayam, bojong_gede, cilebut');
