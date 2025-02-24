/*
 Navicat Premium Data Transfer

 Source Server         : MySql
 Source Server Type    : MySQL
 Source Server Version : 50720
 Source Host           : localhost:3306
 Source Schema         : schoolandfamily

 Target Server Type    : MySQL
 Target Server Version : 50720
 File Encoding         : 65001

 Date: 23/07/2020 22:35:15
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for tb_application
-- ----------------------------
DROP TABLE IF EXISTS `tb_application`;
CREATE TABLE `tb_application`  (
  `keyid` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '审批表KeyId',
  `studentId` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '提交的用户KeyId',
  `teacherId` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '需要审批的教师用户Id',
  `title` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '审批标题',
  `content` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '审批内容',
  `addTime` datetime(0) NULL DEFAULT NULL COMMENT '添加时间',
  `modifyTime` datetime(0) NULL DEFAULT NULL COMMENT '结束时间',
  `isLock` int(2) NULL DEFAULT NULL COMMENT '是否锁定',
  `isDelete` int(1) NULL DEFAULT NULL COMMENT '是否删除',
  `result` int(2) NULL DEFAULT NULL COMMENT '审批是否同意（0：待处理，1：同意，2：不同意）',
  `remark` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '审批备注',
  `resultTime` datetime(0) NULL DEFAULT NULL COMMENT '审批时间',
  PRIMARY KEY (`keyid`) USING BTREE,
  INDEX `fk_1`(`studentId`) USING BTREE,
  INDEX `fk_2`(`teacherId`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for tb_auth
-- ----------------------------
DROP TABLE IF EXISTS `tb_auth`;
CREATE TABLE `tb_auth`  (
  `keyid` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'keyid',
  `username` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户名',
  `password` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '密码',
  `addTime` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `modifytime` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `islock` int(1) NULL DEFAULT NULL COMMENT '状态(0:未激活，1:正常，2:锁定)',
  `isdelete` int(1) NOT NULL DEFAULT 0 COMMENT '删除标识(0:未删除,1:删除)',
  `source` int(1) NOT NULL COMMENT '来源(默认0:本地注册)',
  `salt` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '盐',
  PRIMARY KEY (`keyid`) USING BTREE,
  UNIQUE INDEX `uniq_username`(`username`) USING BTREE COMMENT 'UNIQ'
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for tb_chat_group
-- ----------------------------
DROP TABLE IF EXISTS `tb_chat_group`;
CREATE TABLE `tb_chat_group`  (
  `keyId` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '群组KeyId',
  `creater` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建者ID',
  `users` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '群组成员，|分割',
  `groupName` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '群组名称',
  `count` int(3) NULL DEFAULT NULL COMMENT '用户人数',
  `isDelete` int(1) NULL DEFAULT NULL COMMENT '是否删除',
  `addTime` datetime(0) NULL DEFAULT NULL COMMENT '添加时间',
  `modifyTime` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`keyId`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for tb_class
-- ----------------------------
DROP TABLE IF EXISTS `tb_class`;
CREATE TABLE `tb_class`  (
  `keyid` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '班级表keyid',
  `classname` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '班级名称',
  `schoolname` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '学校名称',
  `kind` int(1) NULL DEFAULT NULL COMMENT '类型',
  `students` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '学生ids(以竖线分割)',
  `addtime` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `modifytime` datetime(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  `isdelete` int(1) NOT NULL DEFAULT 0 COMMENT '删除标识（0未删除，1删除）',
  `invitecode` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '邀请码',
  `owner` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '教师ID',
  `count` int(3) NULL DEFAULT 0 COMMENT '成员数',
  PRIMARY KEY (`keyid`) USING BTREE,
  INDEX `fk_owner_01`(`owner`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for tb_exam
-- ----------------------------
DROP TABLE IF EXISTS `tb_exam`;
CREATE TABLE `tb_exam`  (
  `keyid` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '考试keyid',
  `classid` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '所属班级keyid',
  `examname` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '考试名称',
  `startTime` datetime(0) NULL DEFAULT NULL COMMENT '考试开始时间',
  `endTime` datetime(0) NULL DEFAULT NULL COMMENT '考试结束时间',
  `subject` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '科目',
  `averageScore` int(3) NULL DEFAULT NULL COMMENT '班平',
  `maxScore` int(3) NULL DEFAULT NULL COMMENT '最高分',
  `num` int(5) NULL DEFAULT NULL COMMENT '参考人数',
  `addtime` datetime(0) NULL DEFAULT NULL COMMENT '添加时间',
  `modifytime` datetime(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  `isdelete` int(1) NOT NULL DEFAULT 0 COMMENT '删除标识',
  PRIMARY KEY (`keyid`) USING BTREE,
  INDEX `fk_exam_01`(`classid`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for tb_index_info
-- ----------------------------
DROP TABLE IF EXISTS `tb_index_info`;
CREATE TABLE `tb_index_info`  (
  `keyId` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '主键ID',
  `kind` int(1) NULL DEFAULT NULL COMMENT '种类：0首页轮播图、1系统公告',
  `title` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '标题',
  `content` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '内容',
  `img` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '图片地址',
  `isDelete` int(1) NULL DEFAULT NULL COMMENT '是否删除,0未删除，1已删除',
  `addTime` date NULL DEFAULT NULL COMMENT '添加时间',
  `modifyTime` date NULL DEFAULT NULL COMMENT '更新时间',
  `rank` int(2) NULL DEFAULT 50 COMMENT '优先级，最高99',
  PRIMARY KEY (`keyId`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for tb_message
-- ----------------------------
DROP TABLE IF EXISTS `tb_message`;
CREATE TABLE `tb_message`  (
  `keyId` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '消息表主键ID',
  `fromUser` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '消息来源，用户id',
  `toUser` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '消息接受者，用户id',
  `type` int(1) NULL DEFAULT NULL COMMENT '消息类型，1为个人消息，2为群消息',
  `content` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '消息内容',
  `isRead` int(1) NULL DEFAULT NULL COMMENT '是否已读，0为未读，1为已读',
  `isDelete` int(1) NULL DEFAULT NULL COMMENT '是否删除，0为未删除，1为删除',
  `addTime` datetime(0) NULL DEFAULT NULL COMMENT '添加时间',
  `modifyTime` datetime(0) NULL DEFAULT NULL COMMENT '修改时间',
  `groupId` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '群ID，即班级Id。消息类型为群消息时，该字段不为空',
  PRIMARY KEY (`keyId`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for tb_score
-- ----------------------------
DROP TABLE IF EXISTS `tb_score`;
CREATE TABLE `tb_score`  (
  `keyid` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '成绩表keyid',
  `examid` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '考试表keyid',
  `userid` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户keyid',
  `score` int(3) NULL DEFAULT NULL COMMENT '分数',
  `rank` int(5) NULL DEFAULT NULL COMMENT '班级排名',
  `addtime` datetime(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '添加时间',
  `modifytime` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `isdelete` int(1) NULL DEFAULT NULL COMMENT '删除标识',
  PRIMARY KEY (`keyid`) USING BTREE,
  INDEX `fk_score_01`(`examid`) USING BTREE,
  INDEX `fk_score_02`(`userid`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for tb_task
-- ----------------------------
DROP TABLE IF EXISTS `tb_task`;
CREATE TABLE `tb_task`  (
  `keyid` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '作业任务keyid',
  `classid` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '所属班级keyid',
  `taskname` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '任务名',
  `starttime` datetime(0) NOT NULL COMMENT '开始时间',
  `endtime` datetime(0) NOT NULL COMMENT '结束时间',
  `content` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '任务内容',
  `remark` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  `addtime` datetime(0) NULL DEFAULT NULL COMMENT '添加时间',
  `modifytime` datetime(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  `isdelete` int(1) NULL DEFAULT NULL COMMENT '删除状态',
  `submitCount` int(3) NULL DEFAULT 0 COMMENT '提交数',
  `isNeedSubmit` int(1) NULL DEFAULT NULL COMMENT '线上线下标识（0：线下；1：线上）',
  `finishedUsers` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '已经提交的用户ID（用,分割）',
  PRIMARY KEY (`keyid`) USING BTREE,
  INDEX `fk_task_01`(`classid`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for tb_task_result
-- ----------------------------
DROP TABLE IF EXISTS `tb_task_result`;
CREATE TABLE `tb_task_result`  (
  `keyId` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '任务结果表KeyId',
  `taskId` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '任务表KeyId',
  `userid` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户表keyId',
  `classid` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '班级表keyid',
  `isLock` int(2) NULL DEFAULT 0 COMMENT '是否锁定',
  `isdelete` int(2) NULL DEFAULT 0 COMMENT '是否删除',
  `file` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '文件地址',
  `count` int(2) NULL DEFAULT NULL COMMENT '提交次数',
  `addTime` datetime(0) NULL DEFAULT NULL COMMENT '添加时间',
  `modifytime` datetime(0) NULL DEFAULT NULL COMMENT '更改时间',
  `score` int(3) NULL DEFAULT NULL COMMENT '分数',
  `remark` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '评语',
  PRIMARY KEY (`keyId`) USING BTREE,
  INDEX `fk_task_result_01`(`taskId`) USING BTREE,
  INDEX `fk_task_result_02`(`userid`) USING BTREE,
  INDEX `fk_task_result_03`(`classid`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for tb_user
-- ----------------------------
DROP TABLE IF EXISTS `tb_user`;
CREATE TABLE `tb_user`  (
  `keyid` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'keyid',
  `authid` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '账户权限表id',
  `classid` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '班级表id',
  `nickname` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '昵称',
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '真实名称',
  `phone` varchar(12) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '联系方式',
  `email` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '邮箱',
  `identity` int(1) NOT NULL DEFAULT 0 COMMENT '身份(默认0:学生,1:教师,2:管理员）',
  `addtime` datetime(0) NULL DEFAULT NULL COMMENT '添加时间',
  `modifytime` datetime(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  `isdelete` int(1) NULL DEFAULT NULL COMMENT '删除标识',
  `headImg` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '头像文件地址',
  PRIMARY KEY (`keyid`) USING BTREE,
  INDEX `fk_auth_user_1`(`authid`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;

-- Default data
INSERT INTO `schoolandfamily`.`tb_auth` (`keyid`, `username`, `password`, `addTime`, `modifytime`, `islock`, `isdelete`, `source`, `salt`) VALUES ('17403529632872608', 'admin', 'c4f37a87cb09188968b6426bea6a6954', '2025-02-24 12:22:43', '2025-02-24 12:22:43', 0, 0, 0, 'd74c');
INSERT INTO `schoolandfamily`.`tb_auth` (`keyid`, `username`, `password`, `addTime`, `modifytime`, `islock`, `isdelete`, `source`, `salt`) VALUES ('17403549350705a6a', 'student', '3cfde0dbf52bbfdccf69bc2df2089ebf', '2025-02-24 12:55:35', '2025-02-24 12:55:35', 0, 0, 0, '95b0');
INSERT INTO `schoolandfamily`.`tb_auth` (`keyid`, `username`, `password`, `addTime`, `modifytime`, `islock`, `isdelete`, `source`, `salt`) VALUES ('174035500266232a5', 'teacher', '050f9244d7fc3c737b3da25cd4cae73c', '2025-02-24 12:56:42', '2025-02-24 12:56:42', 0, 0, 0, 'fb0d');
INSERT INTO `schoolandfamily`.`tb_user` (`keyid`, `authid`, `classid`, `nickname`, `name`, `phone`, `email`, `identity`, `addtime`, `modifytime`, `isdelete`, `headImg`) VALUES ('1740352963287f1f0', '17403529632872608', '', 'admin', 'admin', '13333333333', '123@qq.com', 2, '2025-02-24 12:22:43', '2025-02-24 12:49:05', 0, NULL);
INSERT INTO `schoolandfamily`.`tb_user` (`keyid`, `authid`, `classid`, `nickname`, `name`, `phone`, `email`, `identity`, `addtime`, `modifytime`, `isdelete`, `headImg`) VALUES ('1740354935069b3f2', '17403549350705a6a', '1740355023149c24d', 'student', 'student', '13232323232', '3232@qq.com', 0, '2025-02-24 12:55:35', '2025-02-24 12:55:35', 0, NULL);
INSERT INTO `schoolandfamily`.`tb_user` (`keyid`, `authid`, `classid`, `nickname`, `name`, `phone`, `email`, `identity`, `addtime`, `modifytime`, `isdelete`, `headImg`) VALUES ('1740355002661346e', '174035500266232a5', '1740355023149c24d', 'teacher', 'teacher', '13232323222', '123@qq.com', 1, '2025-02-24 12:56:42', '2025-02-24 12:56:42', 0, NULL);
INSERT INTO `schoolandfamily`.`tb_index_info` (`keyId`, `kind`, `title`, `content`, `img`, `isDelete`, `addTime`, `modifyTime`, `rank`) VALUES ('17403548378245294', 1, 'Breaking News! Please Students Notice The Latest Academic Calendar', 'The latest semester calendar has been released, please check it for students and teachers!', NULL, 0, '2025-02-24', '2025-02-24', 1);
INSERT INTO `schoolandfamily`.`tb_index_info` (`keyId`, `kind`, `title`, `content`, `img`, `isDelete`, `addTime`, `modifyTime`, `rank`) VALUES ('1740355662270da86', 0, 'Welcome', NULL, 'LUNBO_1740355842432.png', 0, '2025-02-24', '2025-02-24', 1);
