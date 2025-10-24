/*
 Navicat Premium Data Transfer

 Source Server         : test
 Source Server Type    : MySQL
 Source Server Version : 80031
 Source Host           : localhost:3306
 Source Schema         : morita-hotel

 Target Server Type    : MySQL
 Target Server Version : 80031
 File Encoding         : 65001

 Date: 20/09/2025 17:26:16
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for banner
-- ----------------------------
DROP TABLE IF EXISTS `banner`;
CREATE TABLE `banner`  (
  `banner_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '轮播图ID',
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '名称',
  `image` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '图片',
  `sort` int NOT NULL COMMENT '排序',
  PRIMARY KEY (`banner_id`) USING BTREE,
  UNIQUE INDEX `banner_id`(`banner_id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '轮播图' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of banner
-- ----------------------------
INSERT INTO `banner` VALUES ('3f2f7baa829944e5874990eac3699aa6', '海景客房', '/profile/upload/2025/09/19/海景客房_20250919213554A002.png', 1);
INSERT INTO `banner` VALUES ('b82b757246ee4c63b521ca6e88e32070', '豪华大堂', '/profile/upload/2025/09/19/豪华大堂_20250919213611A004.png', 3);
INSERT INTO `banner` VALUES ('dd480417deca4008a7c46cd9d7829723', '豪华餐厅', '/profile/upload/2025/09/19/豪华餐厅_20250919213602A003.png', 2);

-- ----------------------------
-- Table structure for checkin
-- ----------------------------
DROP TABLE IF EXISTS `checkin`;
CREATE TABLE `checkin`  (
  `checkin_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '入住ID',
  `order_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '订单ID',
  `room_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '房间ID',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '姓名',
  `gender` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '性别',
  `phone` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '电话号码',
  PRIMARY KEY (`checkin_id`) USING BTREE,
  UNIQUE INDEX `checkin_id`(`checkin_id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '入住登记' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of checkin
-- ----------------------------
INSERT INTO `checkin` VALUES ('3ca2ddf07b244120aa5241caeca09bd8', 'HD202509181512211', '0cceddd02dde4ef29062df7e2522ea58', '张三', '0', '13688889999');
INSERT INTO `checkin` VALUES ('78b2abd7bb9a4bb7837df574cb79335f', 'HD202509182151371', '614050f624b44d53ba92f4fcd5064a3d', '李四', '0', '15933332222');

-- ----------------------------
-- Table structure for checkout
-- ----------------------------
DROP TABLE IF EXISTS `checkout`;
CREATE TABLE `checkout`  (
  `checkOut_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '退房ID',
  `order_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '订单ID',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '退房时间',
  PRIMARY KEY (`checkOut_id`) USING BTREE,
  UNIQUE INDEX `checkOut_id`(`checkOut_id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '退房记录' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of checkout
-- ----------------------------
INSERT INTO `checkout` VALUES ('79dfda5df10b485f91333d78ad7aeb94', 'HD202509181512211', '2025-09-18 23:01:59');

-- ----------------------------
-- Table structure for gen_table
-- ----------------------------
DROP TABLE IF EXISTS `gen_table`;
CREATE TABLE `gen_table`  (
  `table_id` bigint NOT NULL AUTO_INCREMENT COMMENT '编号',
  `table_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '' COMMENT '表名称',
  `table_comment` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '' COMMENT '表描述',
  `sub_table_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '关联子表的表名',
  `sub_table_fk_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '子表关联的外键名',
  `class_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '' COMMENT '实体类名称',
  `tpl_category` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT 'crud' COMMENT '使用的模板（crud单表操作 tree树表操作）',
  `tpl_web_type` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '' COMMENT '前端模板类型（element-ui模版 element-plus模版）',
  `package_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '生成包路径',
  `module_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '生成模块名',
  `business_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '生成业务名',
  `function_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '生成功能名',
  `function_author` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '生成功能作者',
  `gen_type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '0' COMMENT '生成代码方式（0zip压缩包 1自定义路径）',
  `gen_path` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '/' COMMENT '生成路径（不填默认项目路径）',
  `options` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '其它生成选项',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`table_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 16 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '代码生成业务表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of gen_table
-- ----------------------------
INSERT INTO `gen_table` VALUES (16, 'banner', '轮播图', NULL, NULL, 'Banner', 'crud', 'element-plus', 'com.morita.hotel', 'hotel', 'banner', '轮播图', 'morita', '0', '/', '{}', 'admin', '2025-09-19 21:29:09', '', '2025-09-19 21:29:42', NULL);

-- ----------------------------
-- Table structure for gen_table_column
-- ----------------------------
DROP TABLE IF EXISTS `gen_table_column`;
CREATE TABLE `gen_table_column`  (
  `column_id` bigint NOT NULL AUTO_INCREMENT COMMENT '编号',
  `table_id` bigint NULL DEFAULT NULL COMMENT '归属表编号',
  `column_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '列名称',
  `column_comment` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '列描述',
  `column_type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '列类型',
  `java_type` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT 'JAVA类型',
  `java_field` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT 'JAVA字段名',
  `is_pk` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '是否主键（1是）',
  `is_increment` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '是否自增（1是）',
  `is_required` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '是否必填（1是）',
  `is_insert` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '是否为插入字段（1是）',
  `is_edit` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '是否编辑字段（1是）',
  `is_list` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '是否列表字段（1是）',
  `is_query` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '是否查询字段（1是）',
  `query_type` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT 'EQ' COMMENT '查询方式（等于、不等于、大于、小于、范围）',
  `html_type` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '显示类型（文本框、文本域、下拉框、复选框、单选框、日期控件）',
  `dict_type` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '' COMMENT '字典类型',
  `sort` int NULL DEFAULT NULL COMMENT '排序',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`column_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 110 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '代码生成业务表字段' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of gen_table_column
-- ----------------------------
INSERT INTO `gen_table_column` VALUES (110, 16, 'banner_id', '轮播图ID', 'varchar(255)', 'String', 'bannerId', '1', '0', '0', '1', NULL, NULL, NULL, 'EQ', 'input', '', 1, 'admin', '2025-09-19 21:29:09', '', '2025-09-19 21:29:42');
INSERT INTO `gen_table_column` VALUES (111, 16, 'title', '名称', 'varchar(255)', 'String', 'title', '0', '0', '1', '1', '1', '1', '1', 'LIKE', 'input', '', 2, 'admin', '2025-09-19 21:29:09', '', '2025-09-19 21:29:42');
INSERT INTO `gen_table_column` VALUES (112, 16, 'image', '图片', 'varchar(255)', 'String', 'image', '0', '0', '1', '1', '1', '1', '0', 'EQ', 'imageUpload', '', 3, 'admin', '2025-09-19 21:29:09', '', '2025-09-19 21:29:42');
INSERT INTO `gen_table_column` VALUES (113, 16, 'sort', '排序', 'int', 'Long', 'sort', '0', '0', '1', '1', '1', '1', '0', 'EQ', 'input', '', 4, 'admin', '2025-09-19 21:29:09', '', '2025-09-19 21:29:42');

-- ----------------------------
-- Table structure for menu
-- ----------------------------
DROP TABLE IF EXISTS `menu`;
CREATE TABLE `menu`  (
  `menu_id` int NOT NULL AUTO_INCREMENT COMMENT '菜单',
  `restaurant_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '餐厅ID',
  `category` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '菜单种类',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '菜品名',
  `description` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '描述',
  `price` decimal(10, 2) NOT NULL COMMENT '价格',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`menu_id`) USING BTREE,
  UNIQUE INDEX `menu_id`(`menu_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 16 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '菜单' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of menu
-- ----------------------------
INSERT INTO `menu` VALUES (1, '50e410aa357c4a02b8b90568da4cccb1', '主菜', '香煎鳕鱼', '香煎鳕鱼配柠檬黄油汁', 158.00, NULL);
INSERT INTO `menu` VALUES (2, '50e410aa357c4a02b8b90568da4cccb1', '开胃菜', '凯撒沙拉', '新鲜罗马生菜配帕玛森芝士和面包丁', 48.00, NULL);
INSERT INTO `menu` VALUES (3, '50e410aa357c4a02b8b90568da4cccb1', '甜品', '提拉米苏', '经典意式提拉米苏', 48.00, NULL);
INSERT INTO `menu` VALUES (4, '29a90ff72f9c4c61bc78c2d8710c9151', '鸡尾酒', '玛格丽特', '龙舌兰酒配柠檬汁和橙味利口酒', 62.00, NULL);
INSERT INTO `menu` VALUES (5, '50e410aa357c4a02b8b90568da4cccb1', '甜品', '香草冰淇淋', '自制香草冰淇淋配焦糖酱', 38.00, NULL);
INSERT INTO `menu` VALUES (6, '29a90ff72f9c4c61bc78c2d8710c9151', '鸡尾酒', '长岛冰茶', '多种烈酒调制的鸡尾酒', 68.00, NULL);
INSERT INTO `menu` VALUES (7, '29a90ff72f9c4c61bc78c2d8710c9151', '鸡尾酒', '莫吉托', '朗姆酒、薄荷、青柠和苏打水', 58.00, NULL);
INSERT INTO `menu` VALUES (8, '50e410aa357c4a02b8b90568da4cccb1', '开胃菜', '烟熏三文鱼', '配柠檬和酸豆的烟熏三文鱼', 68.00, NULL);
INSERT INTO `menu` VALUES (9, '50e410aa357c4a02b8b90568da4cccb1', '主菜', '澳洲牛排', '澳洲谷饲牛排配黑椒汁', 188.00, NULL);
INSERT INTO `menu` VALUES (10, '29a90ff72f9c4c61bc78c2d8710c9151', '咖啡', '卡布奇诺', '意式浓缩咖啡配奶泡', 32.00, NULL);
INSERT INTO `menu` VALUES (11, '50e410aa357c4a02b8b90568da4cccb1', '甜品', '巧克力慕斯', '浓郁巧克力慕斯蛋糕', 42.00, NULL);
INSERT INTO `menu` VALUES (12, '50e410aa357c4a02b8b90568da4cccb1', '主菜', '意大利面', '意式番茄肉酱面', 78.00, NULL);
INSERT INTO `menu` VALUES (13, '50e410aa357c4a02b8b90568da4cccb1', '开胃菜', '法式洋葱汤', '经典法式洋葱汤配芝士面包片', 38.00, NULL);
INSERT INTO `menu` VALUES (14, '29a90ff72f9c4c61bc78c2d8710c9151', '咖啡', '拿铁咖啡', '意式浓缩咖啡配蒸奶', 32.00, NULL);
INSERT INTO `menu` VALUES (15, '29a90ff72f9c4c61bc78c2d8710c9151', '咖啡', '美式咖啡', '意式浓缩咖啡加水', 28.00, NULL);

-- ----------------------------
-- Table structure for order
-- ----------------------------
DROP TABLE IF EXISTS `order`;
CREATE TABLE `order`  (
  `order_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '订单号',
  `category_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '房间类型ID',
  `check_in_date` date NOT NULL COMMENT '入住日期',
  `check_out_date` date NOT NULL COMMENT '退房日期',
  `nights` int NOT NULL COMMENT '天数',
  `rooms` int NOT NULL COMMENT '房间数',
  `unit_price` decimal(10, 2) NOT NULL COMMENT '单价',
  `total_price` decimal(10, 2) NOT NULL COMMENT '总价',
  `status` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '待入住' COMMENT '订单状态',
  `user_id` int NOT NULL COMMENT '订单创建用户ID',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '订单创建时间',
  PRIMARY KEY (`order_id`) USING BTREE,
  UNIQUE INDEX `order_id`(`order_id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '订单' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of order
-- ----------------------------
INSERT INTO `order` VALUES ('HD202509172213511', '3d6f45c3ab89423b8c11d727a35d0bb8', '2025-09-17', '2025-09-18', 1, 1, 1888.00, 1888.00, '已取消', 1, '2025-09-17 22:13:52');
INSERT INTO `order` VALUES ('HD202509181512211', '2ec9a52aaed543128424711299def6a0', '2025-09-18', '2025-09-19', 1, 1, 1288.00, 1288.00, '已完成', 1, '2025-09-18 15:12:22');
INSERT INTO `order` VALUES ('HD202509182151371', '7c63078f817848c38a7984d952751a34', '2025-09-18', '2025-09-19', 1, 1, 888.00, 888.00, '已入住', 1, '2025-09-18 21:51:37');

-- ----------------------------
-- Table structure for restaurant
-- ----------------------------
DROP TABLE IF EXISTS `restaurant`;
CREATE TABLE `restaurant`  (
  `restaurant_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '餐厅ID',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '餐厅名称',
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '描述',
  `location` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '位置',
  `phone` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '联系电话',
  `specialty` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '特色',
  `image` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '图片',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`restaurant_id`) USING BTREE,
  UNIQUE INDEX `restaurant_id`(`restaurant_id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '餐厅' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of restaurant
-- ----------------------------
INSERT INTO `restaurant` VALUES ('29a90ff72f9c4c61bc78c2d8710c9151', '大堂吧', '优雅舒适的休闲场所，提供各类饮品、精美小食和下午茶，是您会友或放松的理想选择。', '一楼大堂', '+86 123 4567 8902', '咖啡、鸡尾酒、下午茶', '/profile/upload/2025/09/19/大堂吧_20250919161912A002.png', '2025-09-12 13:27:24');
INSERT INTO `restaurant` VALUES ('50e410aa357c4a02b8b90568da4cccb1', '主餐厅', '提供丰盛的中西式自助早餐、午餐和晚餐，食材新鲜，菜品丰富，满足不同宾客的口味需求。', '一楼', '+86 123 4567 8901', '自助餐、海鲜', '/profile/upload/2025/09/19/主餐厅_20250919161919A003.png', '2025-09-12 13:25:45');

-- ----------------------------
-- Table structure for room
-- ----------------------------
DROP TABLE IF EXISTS `room`;
CREATE TABLE `room`  (
  `room_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '房间ID',
  `room_number` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '房间号',
  `category_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '房间分类ID',
  `status` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '空闲' COMMENT '房间状态',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`room_id`) USING BTREE,
  UNIQUE INDEX `room_id`(`room_id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '房间' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of room
-- ----------------------------
INSERT INTO `room` VALUES ('0cceddd02dde4ef29062df7e2522ea58', '601', '2ec9a52aaed543128424711299def6a0', '空闲', '2025-09-16 00:10:38');
INSERT INTO `room` VALUES ('17af569e0443484fbc37f79fa44a0bef', '203', 'bc03784920cf45679337b52cbbc926f7', '空闲', '2025-09-16 00:10:38');
INSERT INTO `room` VALUES ('1c4a36930c2d496f9b2fd9553e0709a8', '701', '3d6f45c3ab89423b8c11d727a35d0bb8', '空闲', '2025-09-16 00:10:38');
INSERT INTO `room` VALUES ('1c68e8de124149218038a4f68496e285', '602', '2ec9a52aaed543128424711299def6a0', '空闲', '2025-09-16 00:10:38');
INSERT INTO `room` VALUES ('22b0236decd74feca4648c6bf27a6e78', '503', '365e77a9609348088a2198f52c398331', '空闲', '2025-09-16 00:10:38');
INSERT INTO `room` VALUES ('2429ed3278564c09b343840c4bf0ef50', '303', '9fbd5280e78644ba84a7ae8737e5eafa', '空闲', '2025-09-16 00:10:38');
INSERT INTO `room` VALUES ('614050f624b44d53ba92f4fcd5064a3d', '403', '7c63078f817848c38a7984d952751a34', '已入住', '2025-09-16 00:10:38');
INSERT INTO `room` VALUES ('83225bff1b2a41769faa25f8baef4006', '501', '365e77a9609348088a2198f52c398331', '空闲', '2025-09-16 00:10:38');
INSERT INTO `room` VALUES ('8ea121ff306740588c9636bfab700342', '302', '9fbd5280e78644ba84a7ae8737e5eafa', '空闲', '2025-09-16 00:10:38');
INSERT INTO `room` VALUES ('8feec476d83f45e9bf75f982fc2ce647', '201', 'bc03784920cf45679337b52cbbc926f7', '空闲', '2025-09-16 00:10:38');
INSERT INTO `room` VALUES ('9ea7a371557f4a9b8b7c81fe7c66eb49', '401', '7c63078f817848c38a7984d952751a34', '空闲', '2025-09-16 00:10:38');
INSERT INTO `room` VALUES ('a881734c7627489f93507dd9c1892b98', '402', '7c63078f817848c38a7984d952751a34', '空闲', '2025-09-16 00:10:38');
INSERT INTO `room` VALUES ('ad9c1be024f94cb7a4e35bfc59df020a', '502', '365e77a9609348088a2198f52c398331', '空闲', '2025-09-16 00:10:38');
INSERT INTO `room` VALUES ('b703f48082954cc68aaed0bc1dacb166', '603', '2ec9a52aaed543128424711299def6a0', '空闲', '2025-09-16 00:10:38');
INSERT INTO `room` VALUES ('c4c319cccfc141a48e4619c3318c4b49', '702', '3d6f45c3ab89423b8c11d727a35d0bb8', '空闲', '2025-09-16 00:10:38');
INSERT INTO `room` VALUES ('e7c415aa377443eda734f7ed2fef9c0a', '801', 'bc03784920cf45679337b52cbbc926f7', '空闲', '2025-09-20 17:02:45');
INSERT INTO `room` VALUES ('edd7d111ff1b4a97840560fccac3a1a3', '301', '9fbd5280e78644ba84a7ae8737e5eafa', '空闲', '2025-09-16 00:10:38');
INSERT INTO `room` VALUES ('ee21e78931b24d9f88a88d3ac775f907', '703', '3d6f45c3ab89423b8c11d727a35d0bb8', '空闲', '2025-09-16 00:10:38');
INSERT INTO `room` VALUES ('fbbfb9b3177f4e2c847727246c48a160', '202', 'bc03784920cf45679337b52cbbc926f7', '空闲', '2025-09-16 00:10:38');

-- ----------------------------
-- Table structure for room_category
-- ----------------------------
DROP TABLE IF EXISTS `room_category`;
CREATE TABLE `room_category`  (
  `category_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '分类ID',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '名称',
  `description` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '描述',
  `price` decimal(10, 2) NOT NULL COMMENT '单晚价格',
  `image` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '图片',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`category_id`) USING BTREE,
  UNIQUE INDEX `category_id`(`category_id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '房间分类' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of room_category
-- ----------------------------
INSERT INTO `room_category` VALUES ('2ec9a52aaed543128424711299def6a0', '行政套房', '超大空间，独立客厅，享受行政专属服务', 1288.00, '/profile/upload/2025/09/14/行政套房_20250914224615A001.png', '2025-09-14 22:45:04');
INSERT INTO `room_category` VALUES ('365e77a9609348088a2198f52c398331', '家庭套房', '适合家庭入住，配备一张大床和一张单人床', 988.00, '/profile/upload/2025/09/14/家庭套房_20250914224627A002.png', '2025-09-14 22:45:04');
INSERT INTO `room_category` VALUES ('3d6f45c3ab89423b8c11d727a35d0bb8', '豪华套房', '极致奢华，独立卧室和客厅，配备私人管家服务', 1888.00, '/profile/upload/2025/09/14/豪华套房_20250914224633A003.png', '2025-09-14 22:45:04');
INSERT INTO `room_category` VALUES ('7c63078f817848c38a7984d952751a34', '海景双床房', '直面海景，配备两张单人床，适合家庭出行', 888.00, '/profile/upload/2025/09/14/海景双床房_20250914224639A004.png', '2025-09-14 22:45:04');
INSERT INTO `room_category` VALUES ('9fbd5280e78644ba84a7ae8737e5eafa', '豪华大床房', '宽敞舒适，配备豪华大床，适合商务出行', 688.00, '/profile/upload/2025/09/14/豪华大床房_20250914224645A005.png', '2025-09-14 22:45:04');
INSERT INTO `room_category` VALUES ('bc03784920cf45679337b52cbbc926f7', '标准双床房', '经济实用，配备两张单人床，适合预算有限的旅客', 488.00, '/profile/upload/2025/09/14/标准双床房_20250914224650A006.png', '2025-09-14 22:45:04');

-- ----------------------------
-- Table structure for sys_config
-- ----------------------------
DROP TABLE IF EXISTS `sys_config`;
CREATE TABLE `sys_config`  (
  `config_id` int NOT NULL AUTO_INCREMENT COMMENT '参数主键',
  `config_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '' COMMENT '参数名称',
  `config_key` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '' COMMENT '参数键名',
  `config_value` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '' COMMENT '参数键值',
  `config_type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT 'N' COMMENT '系统内置（Y是 N否）',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`config_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 100 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '参数配置表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_config
-- ----------------------------
INSERT INTO `sys_config` VALUES (1, '账号初始密码', 'sys.user.initPassword', '123456', 'Y', 'admin', '2025-08-30 08:59:54', '', NULL, '初始化密码 123456');
INSERT INTO `sys_config` VALUES (2, '验证码开关', 'sys.account.captchaEnabled', 'true', 'Y', 'admin', '2025-08-30 08:59:54', 'admin', '2025-09-11 13:30:28', '是否开启验证码功能（true开启，false关闭）');
INSERT INTO `sys_config` VALUES (3, '是否开启用户注册功能', 'sys.account.registerUser', 'true', 'Y', 'admin', '2025-08-30 08:59:54', 'admin', '2025-09-11 13:31:46', '是否开启注册用户功能（true开启，false关闭）');

-- ----------------------------
-- Table structure for sys_dept
-- ----------------------------
DROP TABLE IF EXISTS `sys_dept`;
CREATE TABLE `sys_dept`  (
  `dept_id` bigint NOT NULL AUTO_INCREMENT COMMENT '部门id',
  `parent_id` bigint NULL DEFAULT 0 COMMENT '父部门id',
  `ancestors` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '' COMMENT '祖级列表',
  `dept_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '' COMMENT '部门名称',
  `order_num` int NULL DEFAULT 0 COMMENT '显示顺序',
  `leader` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '负责人',
  `phone` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '联系电话',
  `email` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '邮箱',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '0' COMMENT '部门状态（0正常 1停用）',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`dept_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 202 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '部门表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_dept
-- ----------------------------
INSERT INTO `sys_dept` VALUES (100, 0, '0', '森田编程', 0, 'morita', '15888888888', 'morita@qq.com', '0', '0', 'admin', '2025-08-30 08:59:53', 'admin', '2025-08-30 10:36:00');

-- ----------------------------
-- Table structure for sys_dict_data
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict_data`;
CREATE TABLE `sys_dict_data`  (
  `dict_code` bigint NOT NULL AUTO_INCREMENT COMMENT '字典编码',
  `dict_sort` int NULL DEFAULT 0 COMMENT '字典排序',
  `dict_label` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '' COMMENT '字典标签',
  `dict_value` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '' COMMENT '字典键值',
  `dict_type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '' COMMENT '字典类型',
  `css_class` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '样式属性（其他样式扩展）',
  `list_class` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '表格回显样式',
  `is_default` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT 'N' COMMENT '是否默认（Y是 N否）',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '0' COMMENT '状态（0正常 1停用）',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`dict_code`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 111 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '字典数据表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_dict_data
-- ----------------------------
INSERT INTO `sys_dict_data` VALUES (1, 1, '男', '0', 'sys_user_sex', '', '', 'Y', '0', 'admin', '2025-08-30 08:59:54', '', NULL, '性别男');
INSERT INTO `sys_dict_data` VALUES (2, 2, '女', '1', 'sys_user_sex', '', '', 'N', '0', 'admin', '2025-08-30 08:59:54', '', NULL, '性别女');
INSERT INTO `sys_dict_data` VALUES (3, 3, '未知', '2', 'sys_user_sex', '', '', 'N', '0', 'admin', '2025-08-30 08:59:54', '', NULL, '性别未知');
INSERT INTO `sys_dict_data` VALUES (4, 1, '显示', '0', 'sys_show_hide', '', 'primary', 'Y', '0', 'admin', '2025-08-30 08:59:54', '', NULL, '显示菜单');
INSERT INTO `sys_dict_data` VALUES (5, 2, '隐藏', '1', 'sys_show_hide', '', 'danger', 'N', '0', 'admin', '2025-08-30 08:59:54', '', NULL, '隐藏菜单');
INSERT INTO `sys_dict_data` VALUES (6, 1, '正常', '0', 'sys_normal_disable', '', 'primary', 'Y', '0', 'admin', '2025-08-30 08:59:54', '', NULL, '正常状态');
INSERT INTO `sys_dict_data` VALUES (7, 2, '停用', '1', 'sys_normal_disable', '', 'danger', 'N', '0', 'admin', '2025-08-30 08:59:54', '', NULL, '停用状态');
INSERT INTO `sys_dict_data` VALUES (8, 1, '是', 'Y', 'sys_yes_no', '', 'primary', 'Y', '0', 'admin', '2025-08-30 08:59:54', '', NULL, '系统默认是');
INSERT INTO `sys_dict_data` VALUES (9, 2, '否', 'N', 'sys_yes_no', '', 'danger', 'N', '0', 'admin', '2025-08-30 08:59:54', '', NULL, '系统默认否');
INSERT INTO `sys_dict_data` VALUES (100, 1, '空闲', '空闲', 'room_status', NULL, 'primary', 'N', '0', 'admin', '2025-09-15 22:48:55', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (101, 2, '已入住', '已入住', 'room_status', NULL, 'success', 'N', '0', 'admin', '2025-09-15 22:49:11', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (102, 1, '待入住', '待入住', 'order_status', NULL, 'warning', 'N', '0', 'admin', '2025-09-16 17:51:33', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (103, 2, '已入住', '已入住', 'order_status', NULL, 'primary', 'N', '0', 'admin', '2025-09-16 17:51:46', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (104, 3, '已完成', '已完成', 'order_status', NULL, 'success', 'N', '0', 'admin', '2025-09-16 17:52:05', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (105, 4, '已取消', '已取消', 'order_status', NULL, 'danger', 'N', '0', 'admin', '2025-09-16 17:52:16', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (106, 1, '开胃菜', '开胃菜', 'menu_category', NULL, 'default', 'N', '0', 'admin', '2025-09-19 16:07:34', 'admin', '2025-09-19 16:07:42', NULL);
INSERT INTO `sys_dict_data` VALUES (107, 2, '主菜', '主菜', 'menu_category', NULL, 'default', 'N', '0', 'admin', '2025-09-19 16:07:39', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (108, 3, '甜品', '甜品', 'menu_category', NULL, 'default', 'N', '0', 'admin', '2025-09-19 16:07:48', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (109, 4, '咖啡', '咖啡', 'menu_category', NULL, 'default', 'N', '0', 'admin', '2025-09-19 16:07:54', '', NULL, NULL);
INSERT INTO `sys_dict_data` VALUES (110, 5, '鸡尾酒', '鸡尾酒', 'menu_category', NULL, 'default', 'N', '0', 'admin', '2025-09-19 16:07:59', '', NULL, NULL);

-- ----------------------------
-- Table structure for sys_dict_type
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict_type`;
CREATE TABLE `sys_dict_type`  (
  `dict_id` bigint NOT NULL AUTO_INCREMENT COMMENT '字典主键',
  `dict_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '' COMMENT '字典名称',
  `dict_type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '' COMMENT '字典类型',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '0' COMMENT '状态（0正常 1停用）',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`dict_id`) USING BTREE,
  UNIQUE INDEX `dict_type`(`dict_type` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 103 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '字典类型表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_dict_type
-- ----------------------------
INSERT INTO `sys_dict_type` VALUES (1, '用户性别', 'sys_user_sex', '0', 'admin', '2025-08-30 08:59:54', '', NULL, '用户性别列表');
INSERT INTO `sys_dict_type` VALUES (2, '菜单状态', 'sys_show_hide', '0', 'admin', '2025-08-30 08:59:54', '', NULL, '菜单状态列表');
INSERT INTO `sys_dict_type` VALUES (3, '系统开关', 'sys_normal_disable', '0', 'admin', '2025-08-30 08:59:54', '', NULL, '系统开关列表');
INSERT INTO `sys_dict_type` VALUES (4, '系统是否', 'sys_yes_no', '0', 'admin', '2025-08-30 08:59:54', '', NULL, '系统是否列表');
INSERT INTO `sys_dict_type` VALUES (100, '房间状态', 'room_status', '0', 'admin', '2025-09-15 22:48:00', '', NULL, NULL);
INSERT INTO `sys_dict_type` VALUES (101, '订单状态', 'order_status', '0', 'admin', '2025-09-16 17:51:19', '', NULL, NULL);
INSERT INTO `sys_dict_type` VALUES (102, '菜单类型', 'menu_category', '0', 'admin', '2025-09-19 16:07:27', '', NULL, NULL);

-- ----------------------------
-- Table structure for sys_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu`  (
  `menu_id` bigint NOT NULL AUTO_INCREMENT COMMENT '菜单ID',
  `menu_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '菜单名称',
  `parent_id` bigint NULL DEFAULT 0 COMMENT '父菜单ID',
  `order_num` int NULL DEFAULT 0 COMMENT '显示顺序',
  `path` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '' COMMENT '路由地址',
  `component` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '组件路径',
  `query` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '路由参数',
  `route_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '' COMMENT '路由名称',
  `is_frame` int NULL DEFAULT 1 COMMENT '是否为外链（0是 1否）',
  `is_cache` int NULL DEFAULT 1 COMMENT '是否缓存（0缓存 1不缓存）',
  `menu_type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '' COMMENT '菜单类型（M目录 C菜单 F按钮）',
  `visible` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '0' COMMENT '菜单状态（0显示 1隐藏）',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '0' COMMENT '菜单状态（0正常 1停用）',
  `perms` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '权限标识',
  `icon` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '#' COMMENT '菜单图标',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '' COMMENT '备注',
  PRIMARY KEY (`menu_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2044 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '菜单权限表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_menu
-- ----------------------------
INSERT INTO `sys_menu` VALUES (1, '系统管理', 0, 10, 'system', NULL, '', '', 1, 0, 'M', '0', '0', '', '系统管理', 'admin', '2025-08-30 08:59:54', 'admin', '2025-09-16 18:04:37', '系统管理目录');
INSERT INTO `sys_menu` VALUES (100, '用户管理', 1, 1, 'user', 'system/user/index', '', '', 1, 1, 'C', '0', '0', 'system:user:list', '用户管理', 'admin', '2025-08-30 08:59:54', 'admin', '2025-09-01 11:20:55', '用户管理菜单');
INSERT INTO `sys_menu` VALUES (101, '角色管理', 1, 2, 'role', 'system/role/index', '', '', 1, 1, 'C', '0', '0', 'system:role:list', '角色管理', 'admin', '2025-08-30 08:59:54', 'admin', '2025-09-01 11:26:22', '角色管理菜单');
INSERT INTO `sys_menu` VALUES (102, '菜单管理', 1, 3, 'menu', 'system/menu/index', '', '', 1, 1, 'C', '0', '0', 'system:menu:list', '菜单管理', 'admin', '2025-08-30 08:59:54', 'admin', '2025-09-01 11:26:28', '菜单管理菜单');
INSERT INTO `sys_menu` VALUES (103, '部门管理', 1, 4, 'dept', 'system/dept/index', '', '', 1, 1, 'C', '0', '0', 'system:dept:list', '部门管理', 'admin', '2025-08-30 08:59:54', 'admin', '2025-09-01 11:26:33', '部门管理菜单');
INSERT INTO `sys_menu` VALUES (105, '字典管理', 1, 6, 'dict', 'system/dict/index', '', '', 1, 1, 'C', '0', '0', 'system:dict:list', '字典管理', 'admin', '2025-08-30 08:59:54', 'admin', '2025-09-01 11:26:38', '字典管理菜单');
INSERT INTO `sys_menu` VALUES (106, '参数设置', 1, 7, 'config', 'system/config/index', '', '', 1, 1, 'C', '0', '0', 'system:config:list', '参数设置', 'admin', '2025-08-30 08:59:54', 'admin', '2025-09-01 11:26:44', '参数设置菜单');
INSERT INTO `sys_menu` VALUES (116, '代码生成', 1, 8, 'gen', 'tool/gen/index', '', '', 1, 1, 'C', '0', '0', 'tool:gen:list', '代码生成', 'admin', '2025-08-30 08:59:54', 'admin', '2025-09-01 11:26:49', '代码生成菜单');
INSERT INTO `sys_menu` VALUES (1000, '用户查询', 100, 1, '', '', '', '', 1, 0, 'F', '0', '0', 'system:user:query', '#', 'admin', '2025-08-30 08:59:54', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1001, '用户新增', 100, 2, '', '', '', '', 1, 0, 'F', '0', '0', 'system:user:add', '#', 'admin', '2025-08-30 08:59:54', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1002, '用户修改', 100, 3, '', '', '', '', 1, 0, 'F', '0', '0', 'system:user:edit', '#', 'admin', '2025-08-30 08:59:54', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1003, '用户删除', 100, 4, '', '', '', '', 1, 0, 'F', '0', '0', 'system:user:remove', '#', 'admin', '2025-08-30 08:59:54', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1004, '用户导出', 100, 5, '', '', '', '', 1, 0, 'F', '0', '0', 'system:user:export', '#', 'admin', '2025-08-30 08:59:54', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1005, '用户导入', 100, 6, '', '', '', '', 1, 0, 'F', '0', '0', 'system:user:import', '#', 'admin', '2025-08-30 08:59:54', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1006, '重置密码', 100, 7, '', '', '', '', 1, 0, 'F', '0', '0', 'system:user:resetPwd', '#', 'admin', '2025-08-30 08:59:54', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1007, '角色查询', 101, 1, '', '', '', '', 1, 0, 'F', '0', '0', 'system:role:query', '#', 'admin', '2025-08-30 08:59:54', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1008, '角色新增', 101, 2, '', '', '', '', 1, 0, 'F', '0', '0', 'system:role:add', '#', 'admin', '2025-08-30 08:59:54', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1009, '角色修改', 101, 3, '', '', '', '', 1, 0, 'F', '0', '0', 'system:role:edit', '#', 'admin', '2025-08-30 08:59:54', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1010, '角色删除', 101, 4, '', '', '', '', 1, 0, 'F', '0', '0', 'system:role:remove', '#', 'admin', '2025-08-30 08:59:54', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1011, '角色导出', 101, 5, '', '', '', '', 1, 0, 'F', '0', '0', 'system:role:export', '#', 'admin', '2025-08-30 08:59:54', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1012, '菜单查询', 102, 1, '', '', '', '', 1, 0, 'F', '0', '0', 'system:menu:query', '#', 'admin', '2025-08-30 08:59:54', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1013, '菜单新增', 102, 2, '', '', '', '', 1, 0, 'F', '0', '0', 'system:menu:add', '#', 'admin', '2025-08-30 08:59:54', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1014, '菜单修改', 102, 3, '', '', '', '', 1, 0, 'F', '0', '0', 'system:menu:edit', '#', 'admin', '2025-08-30 08:59:54', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1015, '菜单删除', 102, 4, '', '', '', '', 1, 0, 'F', '0', '0', 'system:menu:remove', '#', 'admin', '2025-08-30 08:59:54', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1016, '部门查询', 103, 1, '', '', '', '', 1, 0, 'F', '0', '0', 'system:dept:query', '#', 'admin', '2025-08-30 08:59:54', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1017, '部门新增', 103, 2, '', '', '', '', 1, 0, 'F', '0', '0', 'system:dept:add', '#', 'admin', '2025-08-30 08:59:54', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1018, '部门修改', 103, 3, '', '', '', '', 1, 0, 'F', '0', '0', 'system:dept:edit', '#', 'admin', '2025-08-30 08:59:54', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1019, '部门删除', 103, 4, '', '', '', '', 1, 0, 'F', '0', '0', 'system:dept:remove', '#', 'admin', '2025-08-30 08:59:54', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1025, '字典查询', 105, 1, '#', '', '', '', 1, 0, 'F', '0', '0', 'system:dict:query', '#', 'admin', '2025-08-30 08:59:54', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1026, '字典新增', 105, 2, '#', '', '', '', 1, 0, 'F', '0', '0', 'system:dict:add', '#', 'admin', '2025-08-30 08:59:54', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1027, '字典修改', 105, 3, '#', '', '', '', 1, 0, 'F', '0', '0', 'system:dict:edit', '#', 'admin', '2025-08-30 08:59:54', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1028, '字典删除', 105, 4, '#', '', '', '', 1, 0, 'F', '0', '0', 'system:dict:remove', '#', 'admin', '2025-08-30 08:59:54', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1029, '字典导出', 105, 5, '#', '', '', '', 1, 0, 'F', '0', '0', 'system:dict:export', '#', 'admin', '2025-08-30 08:59:54', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1030, '参数查询', 106, 1, '#', '', '', '', 1, 0, 'F', '0', '0', 'system:config:query', '#', 'admin', '2025-08-30 08:59:54', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1031, '参数新增', 106, 2, '#', '', '', '', 1, 0, 'F', '0', '0', 'system:config:add', '#', 'admin', '2025-08-30 08:59:54', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1032, '参数修改', 106, 3, '#', '', '', '', 1, 0, 'F', '0', '0', 'system:config:edit', '#', 'admin', '2025-08-30 08:59:54', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1033, '参数删除', 106, 4, '#', '', '', '', 1, 0, 'F', '0', '0', 'system:config:remove', '#', 'admin', '2025-08-30 08:59:54', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1034, '参数导出', 106, 5, '#', '', '', '', 1, 0, 'F', '0', '0', 'system:config:export', '#', 'admin', '2025-08-30 08:59:54', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1055, '生成查询', 116, 1, '#', '', '', '', 1, 0, 'F', '0', '0', 'tool:gen:query', '#', 'admin', '2025-08-30 08:59:54', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1056, '生成修改', 116, 2, '#', '', '', '', 1, 0, 'F', '0', '0', 'tool:gen:edit', '#', 'admin', '2025-08-30 08:59:54', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1057, '生成删除', 116, 3, '#', '', '', '', 1, 0, 'F', '0', '0', 'tool:gen:remove', '#', 'admin', '2025-08-30 08:59:54', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1058, '导入代码', 116, 4, '#', '', '', '', 1, 0, 'F', '0', '0', 'tool:gen:import', '#', 'admin', '2025-08-30 08:59:54', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1059, '预览代码', 116, 5, '#', '', '', '', 1, 0, 'F', '0', '0', 'tool:gen:preview', '#', 'admin', '2025-08-30 08:59:54', '', NULL, '');
INSERT INTO `sys_menu` VALUES (1060, '生成代码', 116, 6, '#', '', '', '', 1, 0, 'F', '0', '0', 'tool:gen:code', '#', 'admin', '2025-08-30 08:59:54', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2006, '房间分类管理', 2012, 1, 'category', 'hotel/category/index', NULL, '', 1, 1, 'C', '0', '0', 'hotel:category:list', '房间分类管理', 'admin', '2025-09-14 22:27:38', 'admin', '2025-09-14 22:32:52', '房间分类菜单');
INSERT INTO `sys_menu` VALUES (2007, '房间分类查询', 2006, 1, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'hotel:category:query', '#', 'admin', '2025-09-14 22:27:38', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2008, '房间分类新增', 2006, 2, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'hotel:category:add', '#', 'admin', '2025-09-14 22:27:38', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2009, '房间分类修改', 2006, 3, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'hotel:category:edit', '#', 'admin', '2025-09-14 22:27:38', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2010, '房间分类删除', 2006, 4, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'hotel:category:remove', '#', 'admin', '2025-09-14 22:27:38', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2011, '房间分类导出', 2006, 5, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'hotel:category:export', '#', 'admin', '2025-09-14 22:27:38', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2012, '酒店信息管理', 0, 0, 'hotelInfo', NULL, NULL, '', 1, 1, 'M', '0', '0', '', '酒店信息管理', 'admin', '2025-09-14 22:29:35', 'admin', '2025-09-14 22:32:37', '');
INSERT INTO `sys_menu` VALUES (2013, '房间管理', 2012, 2, 'room', 'hotel/room/index', NULL, '', 1, 1, 'C', '0', '0', 'hotel:room:list', '房间管理', 'admin', '2025-09-15 22:50:52', 'admin', '2025-09-15 22:54:26', '房间菜单');
INSERT INTO `sys_menu` VALUES (2014, '房间查询', 2013, 1, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'hotel:room:query', '#', 'admin', '2025-09-15 22:50:52', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2015, '房间新增', 2013, 2, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'hotel:room:add', '#', 'admin', '2025-09-15 22:50:52', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2016, '房间修改', 2013, 3, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'hotel:room:edit', '#', 'admin', '2025-09-15 22:50:52', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2017, '房间删除', 2013, 4, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'hotel:room:remove', '#', 'admin', '2025-09-15 22:50:52', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2018, '房间导出', 2013, 5, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'hotel:room:export', '#', 'admin', '2025-09-15 22:50:52', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2019, '订单管理', 2025, 1, 'order', 'hotel/order/index', NULL, '', 1, 1, 'C', '0', '0', 'hotel:order:list', '订单管理', 'admin', '2025-09-16 17:54:13', 'admin', '2025-09-16 18:06:03', '订单菜单');
INSERT INTO `sys_menu` VALUES (2020, '订单查询', 2019, 1, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'hotel:order:query', '#', 'admin', '2025-09-16 17:54:13', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2021, '订单新增', 2019, 2, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'hotel:order:add', '#', 'admin', '2025-09-16 17:54:13', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2022, '订单修改', 2019, 3, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'hotel:order:edit', '#', 'admin', '2025-09-16 17:54:13', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2023, '订单删除', 2019, 4, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'hotel:order:remove', '#', 'admin', '2025-09-16 17:54:13', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2024, '订单导出', 2019, 5, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'hotel:order:export', '#', 'admin', '2025-09-16 17:54:13', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2025, '酒店业务管理', 0, 1, 'hotels', NULL, NULL, '', 1, 1, 'M', '0', '0', '', '酒店业务管理', 'admin', '2025-09-16 18:05:13', 'admin', '2025-09-16 18:06:28', '');
INSERT INTO `sys_menu` VALUES (2026, '入住登记记录', 2025, 2, 'checkin', 'hotel/checkin/index', NULL, '', 1, 1, 'C', '0', '0', 'hotel:checkin:list', '入住登记记录', 'admin', '2025-09-18 20:37:08', 'admin', '2025-09-18 20:40:08', '入住登记菜单');
INSERT INTO `sys_menu` VALUES (2027, '入住登记查询', 2026, 1, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'hotel:checkin:query', '#', 'admin', '2025-09-18 20:37:08', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2028, '入住登记新增', 2026, 2, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'hotel:checkin:add', '#', 'admin', '2025-09-18 20:37:08', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2029, '入住登记修改', 2026, 3, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'hotel:checkin:edit', '#', 'admin', '2025-09-18 20:37:08', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2030, '入住登记删除', 2026, 4, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'hotel:checkin:remove', '#', 'admin', '2025-09-18 20:37:08', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2031, '入住登记导出', 2026, 5, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'hotel:checkin:export', '#', 'admin', '2025-09-18 20:37:08', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2032, '退房记录', 2025, 3, 'checkout', 'hotel/checkout/index', NULL, '', 1, 1, 'C', '0', '0', 'hotel:checkout:list', '退房记录', 'admin', '2025-09-18 22:48:17', 'admin', '2025-09-18 22:50:44', '退房记录菜单');
INSERT INTO `sys_menu` VALUES (2033, '退房记录查询', 2032, 1, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'hotel:checkout:query', '#', 'admin', '2025-09-18 22:48:17', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2034, '退房记录新增', 2032, 2, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'hotel:checkout:add', '#', 'admin', '2025-09-18 22:48:17', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2035, '退房记录修改', 2032, 3, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'hotel:checkout:edit', '#', 'admin', '2025-09-18 22:48:17', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2036, '退房记录删除', 2032, 4, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'hotel:checkout:remove', '#', 'admin', '2025-09-18 22:48:17', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2037, '退房记录导出', 2032, 5, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'hotel:checkout:export', '#', 'admin', '2025-09-18 22:48:17', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2038, '餐饮服务管理', 2012, 3, 'restaurant', 'hotel/restaurant/index', NULL, '', 1, 1, 'C', '0', '0', 'hotel:restaurant:list', '餐饮服务管理', 'admin', '2025-09-19 16:10:39', 'admin', '2025-09-19 16:13:58', '餐厅菜单');
INSERT INTO `sys_menu` VALUES (2039, '餐厅查询', 2038, 1, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'hotel:restaurant:query', '#', 'admin', '2025-09-19 16:10:39', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2040, '餐厅新增', 2038, 2, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'hotel:restaurant:add', '#', 'admin', '2025-09-19 16:10:39', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2041, '餐厅修改', 2038, 3, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'hotel:restaurant:edit', '#', 'admin', '2025-09-19 16:10:39', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2042, '餐厅删除', 2038, 4, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'hotel:restaurant:remove', '#', 'admin', '2025-09-19 16:10:39', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2043, '餐厅导出', 2038, 5, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'hotel:restaurant:export', '#', 'admin', '2025-09-19 16:10:39', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2044, '轮播图管理', 2012, 4, 'banner', 'hotel/banner/index', NULL, '', 1, 1, 'C', '0', '0', 'hotel:banner:list', '轮播图管理', 'admin', '2025-09-19 21:30:14', 'admin', '2025-09-19 21:34:57', '轮播图菜单');
INSERT INTO `sys_menu` VALUES (2045, '轮播图查询', 2044, 1, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'hotel:banner:query', '#', 'admin', '2025-09-19 21:30:14', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2046, '轮播图新增', 2044, 2, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'hotel:banner:add', '#', 'admin', '2025-09-19 21:30:14', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2047, '轮播图修改', 2044, 3, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'hotel:banner:edit', '#', 'admin', '2025-09-19 21:30:14', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2048, '轮播图删除', 2044, 4, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'hotel:banner:remove', '#', 'admin', '2025-09-19 21:30:14', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2049, '轮播图导出', 2044, 5, '#', '', NULL, '', 1, 0, 'F', '0', '0', 'hotel:banner:export', '#', 'admin', '2025-09-19 21:30:14', '', NULL, '');
INSERT INTO `sys_menu` VALUES (2050, '客服回复', 0, 2, 'chat', 'system/customerService/index', NULL, '', 1, 1, 'C', '0', '0', '', '客服回复', 'admin', '2025-09-19 22:57:55', 'admin', '2025-09-20 00:50:28', '');

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role`  (
  `role_id` bigint NOT NULL AUTO_INCREMENT COMMENT '角色ID',
  `role_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '角色名称',
  `role_key` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '角色权限字符串',
  `role_sort` int NOT NULL COMMENT '显示顺序',
  `data_scope` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '1' COMMENT '数据范围（1：全部数据权限 2：自定数据权限 3：本部门数据权限 4：本部门及以下数据权限）',
  `menu_check_strictly` tinyint(1) NULL DEFAULT 1 COMMENT '菜单树选择项是否关联显示',
  `dept_check_strictly` tinyint(1) NULL DEFAULT 1 COMMENT '部门树选择项是否关联显示',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '角色状态（0正常 1停用）',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`role_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 100 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '角色信息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO `sys_role` VALUES (1, '超级管理员', 'admin', 1, '1', 1, 1, '0', '0', 'admin', '2025-08-30 08:59:54', '', NULL, '超级管理员');
INSERT INTO `sys_role` VALUES (2, '普通角色', 'common', 2, '2', 1, 1, '0', '2', 'admin', '2025-08-30 08:59:54', 'admin', '2025-08-30 10:34:10', '普通角色');

-- ----------------------------
-- Table structure for sys_role_dept
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_dept`;
CREATE TABLE `sys_role_dept`  (
  `role_id` bigint NOT NULL COMMENT '角色ID',
  `dept_id` bigint NOT NULL COMMENT '部门ID',
  PRIMARY KEY (`role_id`, `dept_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '角色和部门关联表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_role_dept
-- ----------------------------

-- ----------------------------
-- Table structure for sys_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_menu`;
CREATE TABLE `sys_role_menu`  (
  `role_id` bigint NOT NULL COMMENT '角色ID',
  `menu_id` bigint NOT NULL COMMENT '菜单ID',
  PRIMARY KEY (`role_id`, `menu_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '角色和菜单关联表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_role_menu
-- ----------------------------

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user`  (
  `user_id` bigint NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `dept_id` bigint NULL DEFAULT NULL COMMENT '部门ID',
  `user_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '用户账号',
  `nick_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '用户昵称',
  `user_type` varchar(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '00' COMMENT '用户类型（00系统用户）',
  `email` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '' COMMENT '用户邮箱',
  `phonenumber` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '' COMMENT '手机号码',
  `sex` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '0' COMMENT '用户性别（0男 1女 2未知）',
  `avatar` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '' COMMENT '头像地址',
  `password` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '' COMMENT '密码',
  `status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '0' COMMENT '账号状态（0正常 1停用）',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）',
  `login_ip` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '' COMMENT '最后登录IP',
  `login_date` datetime NULL DEFAULT NULL COMMENT '最后登录时间',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '' COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '' COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '备注',
  `balance` decimal(10, 2) NOT NULL DEFAULT 0.00 COMMENT '账户余额',
  PRIMARY KEY (`user_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 105 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '用户信息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES (1, 100, 'admin', '森田', '00', 'morita@163.com', '15888888888', '1', '', '$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2', '0', '0', '127.0.0.1', '2025-09-20 16:34:55', 'admin', '2025-08-30 08:59:53', '', '2025-09-20 16:34:54', '管理员', 10126.00);
INSERT INTO `sys_user` VALUES (104, NULL, '张三', '张三', '00', '', '', '0', '', '$2a$10$FXZg6TcxdbHsn.TiJYAjMOmcYcrazI6RWvo6JeV9/S3daQU3/oHRe', '0', '0', '127.0.0.1', '2025-09-19 23:38:29', '', '2025-09-13 22:46:37', '', '2025-09-19 23:38:29', NULL, 0.00);

-- ----------------------------
-- Table structure for sys_user_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role`  (
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `role_id` bigint NOT NULL COMMENT '角色ID',
  PRIMARY KEY (`user_id`, `role_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '用户和角色关联表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_user_role
-- ----------------------------

SET FOREIGN_KEY_CHECKS = 1;
