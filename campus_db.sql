/*
SQLyog Ultimate v12.5.1 (64 bit)
MySQL - 8.0.26 : Database - campus_db
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`campus_db` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;

USE `campus_db`;

/*Table structure for table `tb_course` */

DROP TABLE IF EXISTS `tb_course`;

CREATE TABLE `tb_course` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `course_code` varchar(20) NOT NULL,
  `course_name` varchar(100) NOT NULL,
  `description` text,
  `teacher` varchar(50) NOT NULL,
  `credit` int DEFAULT NULL,
  `hours` int DEFAULT NULL,
  `major` varchar(100) NOT NULL,
  `max_students` int DEFAULT NULL,
  `current_students` int DEFAULT '0',
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Data for the table `tb_course` */

insert  into `tb_course`(`id`,`course_code`,`course_name`,`description`,`teacher`,`credit`,`hours`,`major`,`max_students`,`current_students`,`create_time`,`update_time`) values 
(1,'CS101','Java程序设计','Java语言基础与面向对象编程','张启亮',3,48,'计算机科学',50,0,'2025-12-01 17:01:59','2025-12-01 17:02:15'),
(2,'CS102','数据结构','常用数据结构和算法','李文生',4,64,'计算机科学',40,0,'2025-12-01 17:02:04','2025-12-01 17:02:17'),
(3,'SE201','软件工程','软件开发流程与方法','王丽丽',3,48,'软件工程',45,0,'2025-12-01 17:02:07','2025-12-01 17:02:19'),
(4,'AI301','机器学习','机器学习基础算法','赵宏博',4,64,'人工智能',35,0,'2025-12-01 17:02:09','2025-12-01 17:02:21'),
(5,'CS103','数据库系统','关系数据库设计与SQL','孙永强',3,48,'计算机科学',50,0,'2025-12-01 17:02:11','2025-12-01 17:02:23');

/*Table structure for table `tb_user` */

DROP TABLE IF EXISTS `tb_user`;

CREATE TABLE `tb_user` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `student_id` varchar(20) NOT NULL,
  `username` varchar(50) NOT NULL,
  `password` varchar(100) DEFAULT NULL,
  `major` varchar(100) NOT NULL,
  `grade` int DEFAULT NULL,
  `email` varchar(100) DEFAULT NULL,
  `phone` varchar(20) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `student_id` (`student_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Data for the table `tb_user` */

insert  into `tb_user`(`id`,`student_id`,`username`,`password`,`major`,`grade`,`email`,`phone`,`create_time`,`update_time`) values 
(1,'2023001','李明','123456','计算机科学',3,'liming@kgc.edu.cn','13465879526','2025-12-01 11:07:43','2025-12-22 17:04:56'),
(2,'2023002','张文龙','123456','软件工程',2,'zhangwenlong@kgc.edu.cn','13699875638','2025-12-02 11:07:39','2025-12-22 17:04:59'),
(3,'2023003','王宇航','123456','人工智能',1,'wangyuhang@kgc.edu.cn','13688889999','2025-12-16 11:07:46','2025-12-22 17:05:02');

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
