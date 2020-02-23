-- CREATE DATABASE buaa_online_judge;

USE buaa_online_judge;

--
-- 编程语言数据表
--

-- 字段内容：编程语言ID（主键）、编程语言名称（字符串）、编译命令（字符串）、运行命令（字符串）

--
-- Table structure for table `languages`
--

CREATE TABLE `languages` (
  `language_id` INT AUTO_INCREMENT PRIMARY KEY,
  `language_name` VARCHAR(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `compile_command` VARCHAR(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `run_command` VARCHAR(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

INSERT INTO `languages` (`language_id`, `language_name`, `compile_command`, `run_command`) VALUES
			(1, 'Python 3', 'python -m py_compile {filename}.py', 'python {filename}.py'),
            (2, 'C', 'gcc -O2 -s -Wall -o {filename}.exe {filename}.c -lm', '{filename}.exe'),
            (3, 'C++', 'g++ -O2 -s -Wall -std=c++11 -o {filename}.exe {filename}.cpp -lm', '{filename}.exe'),
            (4, 'Java', 'javac {filename}.java', 'java -cp {filename}');

--
-- 院系数据表
--

-- 字段内容：院系ID（主键）、院系名称（字符串）

--
-- Table structure for table `departments`
--
CREATE TABLE `departments` (
  `department_id` INT AUTO_INCREMENT PRIMARY KEY,
  `department_name` VARCHAR(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

INSERT INTO `departments` (`department_id`, `department_name`) VALUES
			(1, '士谔书院'), (2, '冯如书院'), (3, '士嘉书院'), (4, '守锷书院'), (5, '致真书院'),(6, '知行书院');

--
-- 学生数据表
--

-- 字段内容：学生ID（主键）、邮箱（字符串，UNIQUE）、真实姓名（字符串）、
-- 			学号（字符串）、密码（字符串）、院系ID（连接院系数据表的外键）、
-- 			语言偏好（连接编程语言数据表的外键，默认Python 3）、个人简介（文本）

--
-- Table structure for table `students`
--
CREATE TABLE `students` (
  `student_id` BIGINT AUTO_INCREMENT PRIMARY KEY,
  `email` VARCHAR(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci UNIQUE NOT NULL,
  `student_name` VARCHAR(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `student_number` VARCHAR(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `password` VARCHAR(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `department_id` INT NOT NULL,
  `prefer_language_id` INT DEFAULT 1 NOT NULL,
  `introduction` TEXT COLLATE utf8mb4_unicode_ci,
  CONSTRAINT `students_departments_fk` FOREIGN KEY (`department_id`) REFERENCES `departments` (`department_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `students_languages_fk` FOREIGN KEY (`prefer_language_id`) REFERENCES `languages` (`language_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

INSERT INTO `students` (`student_id`, `email`, `student_name`, `student_number`, `password`, `department_id`) VALUES
 			(1, 'lihua@qq.com', '李华', 'ZY1906101', 'UHaNuxBNBV0avnnFlv6f6g==', 1),
 			(2, 'liqiang@126.com', '李强', 'ZY1906102', 'U3aPEZpvLF3+LOxZb8QT0g==', 2),
 			(3, 'zhaoming@qq.com', '赵明', 'SY1906103', '64w6MQRSfki1eHBczdzdnw==', 3),
 			(4, 'chencang@163.com', '陈仓', 'BY1906104', 'xjlo/cr2u2RlU4x5SWJVXw==', 4),
 			(5, 'zouqiang@gmail.com', '邹强', 'SY1906105', 'kKkRwIv2SYPcesOwOeHUzg==', 5),
			(6, 'renruoling@buaa.edu.cn', '任若邻', 'BY1906106', 'dqQvNEhAx7Sf5iooKmvdcg==', 1);

--
-- 老师数据表
--

-- 字段内容：老师ID（主键）、邮箱（字符串，UNIQUE）、真实姓名（字符串）、教职工编号（字符串）、
-- 			密码（字符串）、院系ID（连接院系数据表的外键）、
-- 			是否通过系统管理员审核（数值，通过(1)未通过(0)）、个人简介（文本）

--
-- Table structure for table `teachers`
--
CREATE TABLE `teachers` (
  `teacher_id` INT AUTO_INCREMENT PRIMARY KEY,
  `email` VARCHAR(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci UNIQUE NOT NULL,
  `teacher_name` VARCHAR(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `teacher_number` VARCHAR(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `password` VARCHAR(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `department_id` INT NOT NULL,
  `is_accept` TINYINT DEFAULT 0 NOT NULL,
  `introduction` TEXT COLLATE utf8mb4_unicode_ci,
  CONSTRAINT `teachers_departments_fk` FOREIGN KEY (`department_id`) REFERENCES `departments` (`department_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

INSERT INTO `teachers` (`teacher_id`, `email`, `teacher_name`, `teacher_number`, `password`, `department_id`, `is_accept`) VALUES
 			(1, 'chenming@gmail.com', '陈铭', '0659', 'SbpRyRYTaHJSXMgI8HXrkg==', 1, 1),
 			(2, 'lidan@126.com', '李诞', '0926', 'VMRfx/89+UgzcaCA5NUWYQ==', 2, 1),
			(3, 'zhanqingyun@foxmail.com', '詹青云', '0699', '4TZI2/MN4nYzUAUo1u8Fgg==', 1, 0);

--
-- 系统管理员数据表
--

-- 字段内容：管理员ID（主键）、账号（字符串，UNIQUE）、用户名（字符串）、密码（字符串）

--
-- Table structure for table `system_administrator`
--

CREATE TABLE `system_administrator` (
  `system_administrator_id` INT AUTO_INCREMENT PRIMARY KEY,
  `system_administrator_account` VARCHAR(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci UNIQUE NOT NULL,
  `system_administrator_name` VARCHAR(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `password` VARCHAR(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

INSERT INTO `system_administrator` (`system_administrator_id`, `system_administrator_account`, `system_administrator_name`, `password`) VALUES
			(1, 'administrator', '胡老师', 'v0S1EyX623kFqpf1oj+rew==');


--
-- 课程数据表
--

-- 字段内容：课程ID（主键）、课程名称（字符串）、课程简介（文本）、
-- 			所属老师ID（连接老师数据表的外键）

--
-- Table structure for table `courses`
--
CREATE TABLE `courses` (
  `course_id` INT AUTO_INCREMENT PRIMARY KEY,
  `course_name` VARCHAR(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `introduction` TEXT COLLATE utf8mb4_unicode_ci,
  `teacher_id` INT NOT NULL,
  CONSTRAINT `courses_teachers_fk` FOREIGN KEY (`teacher_id`) REFERENCES `teachers` (`teacher_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

INSERT INTO `courses` (`course_id`, `course_name`, `introduction`, `teacher_id`) VALUES
			(1, '2018级-软件学院-算法设计与分析', '无', 1),
			(2, '2019级-中法工程师学院-大学计算机基础', '《软件学院2017级算法分析与设计》，2018秋季学期\n\r上课时间地点: 1-17周四6-7节， J3-311\n\r上机时间地点: 3周周五晚，6-14周双周六3-4节，S7-601, 602, 603 (测试时间地点另行通知)\n\r讲评时间地点: 5-13周单周五11-12节，J3-410\n\r$ 请各位同学在个人信息中填好**真实学号**并将用户昵称改为**真实姓名**,然后申请加入课程，未按要求填写学号和姓名的，将不会被批准。\n\r$ 在使用过程中，请不要更改学号和姓名，不然将影响计成绩。\n\r课程组老师和助教联系方式:\n\r    宋 友songyou@buaa. edu.cn lab.buaacoding.cn/ysong/\n\r    郭镕昊941777656@qq.com\n\r    李 想214481008@qq.com\n\r    李昕航848318504@qq.com\n\r如对课程相关内容有任何疑问或建议，请联系助教。', 1);

--
-- 学生与课程关系数据表
--

-- 字段内容：关系ID（主键）、课程ID（连接课程数据库的外键）、学生ID（连接学生数据表的外键）、
-- 			审核情况（数值，是(1)否(0)通过申请）、角色（学生(0)和助教(1)两种角色）

--
-- Table structure for table `course_student_relationships`
--
CREATE TABLE `course_student_relationships` (
  `relationship_id` BIGINT AUTO_INCREMENT PRIMARY KEY,
  `course_id` INT NOT NULL,
  `student_id` BIGINT NOT NULL,
  `is_student_accept` TINYINT DEFAULT 0 NOT NULL,
  `student_role` TINYINT DEFAULT 0 NOT NULL,
  CONSTRAINT `relationships_courses_fk` FOREIGN KEY (`course_id`) REFERENCES `courses` (`course_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `relationships_students_fk` FOREIGN KEY (`student_id`) REFERENCES `students` (`student_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

INSERT INTO `course_student_relationships` (`relationship_id`, `course_id`, `student_id`, `is_student_accept`, `student_role`) VALUES
			(1, 1, 1, 1, 0), (2, 2, 2, 1, 0), (3, 2, 3, 1, 1), (4, 2, 4, 1, 0);

--
-- 竞赛数据表
--

-- 字段内容：竞赛ID（主键）、竞赛名称（字符串）、所属课程ID（连接课程数据表的外键）、
-- 			开始时间（Date）、结束时间（Date）、可见性（可见(1)，不可见(0)）、
-- 			竞赛状态（可答题(1)，不可答题(0)）、竞赛简介（文本）

--
-- Table structure for table `contests`
--
CREATE TABLE `contests` (
  `contest_id` INT AUTO_INCREMENT PRIMARY KEY,
  `contest_name` VARCHAR(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `course_id` INT NOT NULL,
  `start_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `finish_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `visibility` TINYINT DEFAULT 1 NOT NULL,
  `status` TINYINT DEFAULT 1 NOT NULL,
  `introduction` TEXT COLLATE utf8mb4_unicode_ci,
  CONSTRAINT `contests_courses_fk` FOREIGN KEY (`course_id`) REFERENCES `courses` (`course_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

INSERT INTO `contests` (`contest_id`, `contest_name`, `course_id`, `start_time`, `finish_time`, `visibility`, `status`, `introduction`) VALUES
			(1, '2019级数据结构期末上机', 1, '2020-02-01 00:00:00', '2020-02-10 00:00:00', 1, 1, '如果WA了，TLE了, RE了，不要担心，多调试一下，多试几组数据，AC就在眼前。'),
			(2, '2019级数据结构练习', 1, '2020-02-01 00:00:00', '2020-02-20 00:00:00', 1, 1, '熟能生巧，加油！');

--
-- 题目数据表
--

-- 字段内容：题目ID（主键）、所属竞赛（竞赛数据表的外键）、题号（数值）、评测机制（在线评测0，人工评测1）
-- 			作者（字符串）、题目名称（字符串）、时间限制（数值，毫秒）、内存限制（数值，KB）、
-- 			题目描述（文本）、输入格式（文本）、输出格式（文本）、输入样例（文本）、
-- 			输出样例（文本）、样例解释（文本）、提示（文本）、代码（文本，用于填空题）

--
-- Table structure for table `problems`
--
CREATE TABLE `problems` (
  `problem_id` BIGINT AUTO_INCREMENT PRIMARY KEY,
  `contest_id` INT NOT NULL,
  `problem_number` TINYINT NOT NULL,
  `judge_mechanism` TINYINT DEFAULT 0 NOT NULL,
  `author` VARCHAR(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `problem_name` VARCHAR(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `time_limit` INT NOT NULL,
  `memory_limit` INT NOT NULL,
  `description` TEXT COLLATE utf8mb4_unicode_ci,
  `input_format` TEXT COLLATE utf8mb4_unicode_ci NOT NULL,
  `output_format` TEXT COLLATE utf8mb4_unicode_ci NOT NULL,
  `input_sample` TEXT COLLATE utf8mb4_unicode_ci,
  `output_sample` TEXT COLLATE utf8mb4_unicode_ci,
  `sample_explanation` TEXT COLLATE utf8mb4_unicode_ci,
  `hint` TEXT COLLATE utf8mb4_unicode_ci,
  `code` TEXT COLLATE utf8mb4_unicode_ci,
  CONSTRAINT `problems_contests_fk` FOREIGN KEY (`contest_id`) REFERENCES `contests` (`contest_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

INSERT INTO `problems` (`problem_id`, `contest_id`, `problem_number`, `author`, `problem_name`, `time_limit`, `memory_limit`, `description`, `input_format`, `output_format`, `input_sample`, `output_sample`, `sample_explanation`, `hint`, `code`) VALUES
			(1, 1, 1, '胡老师', 'A+B Problem', 1000, 65536, '输入两个自然数, 输出他们的和', '两个自然数x和y (0<=x, y<=32767).', '一个数, 即x和y的和.', '123 500', '623', '123 + 500 = 623', '## C++ Code\r\n\r\n    #include <iostream>\r\n\r\n    int main() {\r\n        int a = 0, b = 0;\r\n        std::cin >> a >> b;\r\n        std::cout << a + b << std::endl;\r\n        return 0;\r\n    }\r\n\r\n## Free Pascal Code\r\n\r\n    program Plus;\r\n    var a, b:longint;\r\n    begin\r\n        readln(a, b);\r\n        writeln(a + b);\r\n    end.\r\n\r\n## Java Code\r\n\r\n    import java.util.Scanner;\r\n\r\n    public class Main {\r\n        public static void main(String[] args) {\r\n            Scanner in = new Scanner(System.in);\r\n            int a = in.nextInt();\r\n            int b = in.nextInt();\r\n            System.out.println(a + b);\r\n        }\r\n    }\r\n', NULL),
			(2, 1, 2, '胡老师', '谁拿了最多奖学金', 1000, 65536, '某校的惯例是在每学期的期末考试之后发放奖学金。发放的奖学金共有五种，获取的条件各自不同：\r\n1) 院士奖学金，每人8000元，期末平均成绩高于80分（>80），并且在本学期内发表1篇或1篇以上论文的学生均可获得；\r\n2) 五四奖学金，每人4000元，期末平均成绩高于85分（>85），并且班级评议成绩高于80分（>80）的学生均可获得；\r\n3) 成绩优秀奖，每人2000元，期末平均成绩高于90分（>90）的学生均可获得；\r\n4) 西部奖学金，每人1000元，期末平均成绩高于85分（>85）的西部省份学生均可获得；\r\n5) 班级贡献奖，每人850元，班级评议成绩高于80分（>80）的学生干部均可获得；\r\n只要符合条件就可以得奖，每项奖学金的获奖人数没有限制，每名学生也可以同时获得多项奖学金。例如姚林的期末平均成绩是87分，班级评议成绩82分，同时他还是一位学生干部，那么他可以同时获得五四奖学金和班级贡献奖，奖金总数是4850元。\r\n现在给出若干学生的相关数据，请计算哪些同学获得的奖金总数最高（假设总有同学能满足获得奖学金的条件）。', '输入的第一行是一个整数N（1 <= N <= 100），表示学生的总数。接下来的N行每行是一位学生的数据，从左向右依次是姓名，期末平均成绩，班级评议成绩，是否是学生干部，是否是西部省份学生，以及发表的论文数。姓名是由大小写英文字母组成的长度不超过20的字符串（不含空格）；期末平均成绩和班级评议成绩都是0到100之间的整数（包括0和100）；是否是学生干部和是否是西部省份学生分别用一个字符表示，Y表示是，N表示不是；发表的论文数是0到10的整数（包括0和10）。每两个相邻数据项之间用一个空格分隔。', '输出包括三行，第一行是获得最多奖金的学生的姓名，第二行是这名学生获得的奖金总数。如果有两位或两位以上的学生获得的奖金最多，输出他们之中在输入文件中出现最早的学生的姓名。第三行是这N个学生获得的奖学金的总数。', '4\r\nYaoLin 87 82 Y N 0\r\nChenRuiyi 88 78 N Y 1\r\nLiXin 92 88 N N 0\r\nZhangQin 83 87 Y N 1', 'ChenRuiyi\r\n9000\r\n28700', '无', '无', NULL);

--
-- 测试点数据表
--

-- 字段内容：题目ID（连接题目数据表的外键）、测试点序号（对应题目测试点序号）、
-- 			测试点输入（文本）、测试点输出（文本）

--
-- Table structure for table `problem_checkpoints`
--

CREATE TABLE `problem_checkpoints` (
  `problem_id` BIGINT NOT NULL,
  `checkpoint_number` INT NOT NULL,
  `checkpoint_input` TEXT COLLATE utf8mb4_unicode_ci NOT NULL,
  `checkpoint_output` TEXT COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY(problem_id, checkpoint_number),
  CONSTRAINT `checkpoints_problems_fk` FOREIGN KEY (`problem_id`) REFERENCES `problems` (`problem_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

INSERT INTO `problem_checkpoints` (`problem_id`, `checkpoint_number`, `checkpoint_input`, `checkpoint_output`) VALUES
			(1, 1, '18820 26832\r\n', '45652\r\n'),
			(1, 2, '1123 5687', '6810\r\n'),
			(1, 3, '15646 8688', '24334'),
			(1, 4, '26975 21625', '48600'),
			(1, 5, '23107 28548', '51655'),
			(1, 6, '16951 22289', '39240'),
			(1, 7, '8634 13146', '21780'),
			(1, 8, '17574 15337', '32911'),
			(1, 9, '14548 28382', '42930'),
			(1, 10, '3271 17411', '20682'),
			(2, 1, '100\r\nXvpxWEvuxMujM 79 90 N N 0\r\nFWGrVKwgJsImNAzO 100 75 Y Y 0\r\nXHEDY 85 95 Y Y 8\r\nSnJCAbmx 80 90 Y N 0\r\nIjsb 90 78 N N 0\r\nT 78 98 Y Y 8\r\nA 87 90 Y Y 0\r\nASVBj 85 94 N N 0\r\nFmMsZNPOCgCwygIT 85 83 N Y 0\r\nFXJDETWfFCqJnbYyxa 75 82 N Y 0\r\nEQMWSRKNvkunT 80 95 Y Y 0\r\nVdxkZpDNgfvqQOGyyOIG 80 80 Y Y 0\r\nXGIMzWKXeJMXTUwduO 95 81 N Y 0\r\nRuncagD 95 96 Y N 0\r\nTzmkvWKsUOkbBDrnc 90 96 N Y 5\r\nCXBJzEelrzdJ 90 97 N Y 0\r\nUGmNXVYsvpLgmb 85 100 N Y 0\r\nAhteTELcmbv 90 85 N N 0\r\nGAEIg 80 95 N Y 0\r\nYAHRJgqstbZaZPyww 99 85 Y Y 0\r\nQo 90 90 N N 0\r\nDtqLHiMmeHbohAxToJWP 78 75 Y Y 0\r\nXIWnP 85 75 N Y 0\r\nDoWmxImSCdk 95 76 Y N 0\r\nNLFznHxOcV 82 85 N N 0\r\nAKXkyYHWGwXvqMLKiCIR 94 95 Y Y 0\r\nGcgEAFL 79 93 N Y 0\r\nNqUjBPAYxdABnMGcQax 85 91 Y N 0\r\nXPohweOIDDHmb 83 94 Y N 0\r\nYIdB 75 93 N Y 0\r\nDBQxmSnODuYwifv 93 77 Y N 0\r\nRCpKstV 75 90 N Y 0\r\nUnu 82 81 Y N 0\r\nNGeyEHglHxHfghD 81 100 N Y 0\r\nMFLedhUZ 96 75 N Y 0\r\nYYEt 76 95 Y Y 10\r\nR 85 85 Y Y 0\r\nLHJGGF 95 87 Y Y 0\r\nPA 85 93 N Y 0\r\nGKPQfRzvnYtRqUcYYDGi 80 92 N N 0\r\nOsYZnlpdToExY 99 95 Y Y 0\r\nBK 91 85 N N 0\r\nC 83 85 Y Y 0\r\nTvp 90 90 Y Y 0\r\nFzSFDyfqaKOAp 90 85 Y N 0\r\nIIzkbLEYbncWzCZCIiJr 94 90 N N 0\r\nYOwtTfAQymJqjkkh 90 80 Y Y 0\r\nAjZSUNidohrGDgCBpo 91 86 Y N 0\r\nQRhbctxBn 79 75 N Y 0\r\nEIkGwpsyW 85 96 N N 0\r\nIJh 85 81 N Y 0\r\nXe 82 80 Y Y 7\r\nBbLcNTB 95 80 N N 0\r\nMPCNZTyWmaZ 84 85 N N 0\r\nChrB 95 95 N N 0\r\nTlIYwpIbVnurzacD 95 90 N N 8\r\nThywwHUaoxX 99 78 N Y 0\r\nQGnzLidvEBjIcmGIAdI 86 96 N Y 0\r\nDNpKPSUyprOeJFmU 81 93 N Y 0\r\nHYfptVzlESrFRUVdgL 93 92 Y N 0\r\nHcblndXi 90 89 Y N 0\r\nNWBJjdgcI 87 85 N Y 0\r\nShnYCYURiyKoVTq 85 99 N N 0\r\nZbc 75 80 Y Y 0\r\nEGvxpyULGNPQ 90 91 N N 0\r\nTdjasZOAvV 75 90 N Y 6\r\nJA 93 80 N Y 0\r\nXkCWQCTpXjZgCKflzh 86 83 N Y 0\r\nEsNOQfgeERv 75 81 N Y 0\r\nSuyWV 95 81 N Y 0\r\nLJJbpohSV 90 98 N Y 0\r\nMPGJLKflSBtZHKEtcsU 100 99 Y N 0\r\nAQrPmmXO 89 95 Y Y 0\r\nYDCmkIYBPOy 95 91 N Y 10\r\nXLiSnATGAHSSF 95 83 N N 0\r\nSGyOHlvb 90 84 Y Y 0\r\nMRiXqCxTvwToRyyEJgB 95 100 Y Y 0\r\nZNrctMuIhBMLmmi 85 78 N Y 0\r\nHdTpUsEc 98 75 Y Y 0\r\nRAhDEmlvNNicCa 75 92 Y Y 0\r\nIKyNgnEfzGQFnBMd 85 80 N Y 0\r\nJNtN 95 90 N N 7\r\nSBeSjxfxvmQIH 84 91 Y Y 0\r\nEregtaBcUOPuFfOWAxw 95 81 Y N 0\r\nBjcxDhbbJvDLXMVyn 87 79 N Y 0\r\nViFUYKFDOZegK 90 80 Y Y 0\r\nWsr 95 86 Y N 0\r\nJgdPGCFiUwUUfAA 80 92 Y Y 0\r\nO 96 97 Y N 0\r\nJWfAfenceiSgJq 95 85 N N 0\r\nQUJCpMvjCViQ 80 85 Y Y 0\r\nRnVtCGNCmLqmtdly 83 95 Y N 0\r\nQKnLkS 87 80 N Y 0\r\nMuQsSfe 90 97 N Y 0\r\nSkvAUshiLLErXihn 93 85 Y Y 0\r\nKhEmO 88 88 Y N 0\r\nHovCo 90 80 N Y 0\r\nUxZ 75 75 Y N 6\r\nBuD 97 77 N Y 0\r\nHfSAzTvoupTcUlldPWDM 75 95 N Y 0', 'YDCmkIYBPOy\r\n15000\r\n332750'),
			(2, 2, '100\r\nKSSHsCEhNHXIMxnKjszE 93 98 N Y 0\r\nKRXBnwAzDGjpfJgRpKt 95 90 Y Y 0\r\nJBOyaqxicVGB 96 84 N Y 0\r\nHolSWzxphdbM 99 80 Y Y 0\r\nYzYKojgqgraUpqNyJ 95 90 N N 0\r\nLgX 97 94 Y Y 0\r\nHCRuZbWsSDgkOEoj 80 90 N Y 0\r\nS 85 89 N Y 10\r\nAdLhdCBxwmJXRm 78 97 Y N 0\r\nQhgGp 85 87 Y N 0\r\nZFTdFawbiwDG 87 80 N Y 0\r\nJaOThlpkrRkejXMMXgbu 95 80 Y Y 0\r\nNiAntWp 85 91 Y N 0\r\nTe 81 81 Y Y 0\r\nTrFXiYBMzJ 92 87 N N 0\r\nMlhsUsruSKdrAGkUerE 97 92 Y Y 0\r\nNWpjgxcNegmyEGHlPTP 88 94 N N 5\r\nVCXJjomOzywFZBVkDR 87 75 Y N 0\r\nPxZYNvXNdbUtBIp 82 75 N Y 0\r\nTsJkMpF 85 99 N Y 0\r\nBwaIeOrqdatudl 85 80 N N 0\r\nIB 95 90 Y N 0\r\nHmPorGeOs 98 85 Y N 0\r\nDgRHzC 87 82 Y N 0\r\nWkRmkErigra 90 75 Y N 0\r\nMZdQ 85 78 N N 0\r\nNIrWcjt 81 95 Y N 0\r\nMQBfcr 85 80 Y N 0\r\nIXrLPdJhEekBRgsbkqI 95 85 Y Y 6\r\nMWAd 89 75 Y N 0\r\nBVYqUBdV 76 79 Y Y 0\r\nNhmSyMnvHxUkjye 79 90 Y N 0\r\nNUPyQqEhklWtWxeIz 80 95 Y N 0\r\nNsQbL 95 86 N N 0\r\nKEKFcpbDWQXUsQTXvFbs 95 95 N N 0\r\nZzjJWy 90 80 N Y 3\r\nTOWFblEWAuVgAxdHt 95 100 N N 0\r\nHRYFlarRQhe 100 90 N N 0\r\nSGUCaHJVg 95 87 Y Y 0\r\nCsNEZwUWERVFFxLsdyK 85 94 N N 0\r\nPZhrRpBMpm 80 85 Y N 0\r\nLsyePjMaoonOscyyUQY 82 98 Y Y 0\r\nSkPGcYDnYyIp 95 90 N N 0\r\nOaV 90 90 Y N 0\r\nW 75 90 Y N 0\r\nTJjZwq 85 75 Y Y 0\r\nPVFVBEocVjjpg 90 88 N N 0\r\nHMJecx 85 100 N Y 0\r\nPvgXSkNZQNcfJrgTzgm 90 75 N Y 0\r\nVnVXCwwJdbrusXG 89 95 Y N 0\r\nBNziq 95 76 Y Y 0\r\nMNjzTqMCtx 85 95 N N 0\r\nUihwjdZaUXWuMsytRtN 90 88 Y N 7\r\nHdXkkOHRQoG 93 85 Y N 0\r\nDHDAFValkXKFYjznnn 75 100 N N 0\r\nJskIgwiNH 85 85 Y N 0\r\nSeYSBFuVQaEUXwf 75 80 N Y 0\r\nXfGMOTDT 82 75 Y Y 0\r\nGnGatTgZBPgj 90 90 N N 0\r\nK 85 80 Y Y 0\r\nKRJVphYKwQTOMc 95 85 N N 0\r\nDFWtLGoFLXHptkI 75 92 N Y 0\r\nCayHZQXpTpyFPSuJz 87 76 N Y 0\r\nRRlRboFqAgLvzrJ 89 75 N Y 4\r\nDbWHykSi 85 84 N Y 0\r\nWQHneRqIh 90 82 Y N 0\r\nBhGejmW 90 93 Y N 0\r\nAFEEic 95 80 Y N 0\r\nMXhBgPj 96 98 Y Y 0\r\nWJsSWOCR 77 85 Y Y 0\r\nA 79 82 N N 0\r\nWy 95 85 Y Y 0\r\nOtgBaCKAVmMEFxPVcbE 84 75 N Y 0\r\nDnBaraTLVBkPJJj 85 75 Y N 0\r\nKzir 84 75 Y Y 0\r\nPxCMvWOdyZcRW 90 76 N N 0\r\nZcztKxXsrhqSDuxBeN 80 84 N Y 0\r\nEesxZKSACX 80 87 N N 0\r\nCvCT 75 85 N N 0\r\nUwrgH 76 80 Y N 0\r\nVwcGoYzhhVFWGyFzjdn 78 90 N N 0\r\nKQ 75 100 N Y 0\r\nMHJszTi 95 79 Y Y 0\r\nBPJXuWxsGGNYz 87 92 Y Y 5\r\nXWWpW 96 92 Y Y 6\r\nGKIdgeGTHWd 80 77 Y Y 0\r\nMeGS 88 75 N N 0\r\nSblEZfNCkGAkRSrzFADB 90 88 N Y 0\r\nBGLYzCxFVARuGu 87 75 N Y 0\r\nWMQkzgUafGBnqiuBgRpn 77 75 N Y 0\r\nLIWndzmjDozIMTu 80 94 N Y 0\r\nLsPuNFjIzCcppis 80 80 Y N 0\r\nJgycuqAJHELopIoZm 87 85 Y N 0\r\nUJmFKI 75 90 N N 0\r\nRdvGLr 95 85 Y Y 0\r\nWVsTKZHjDEc 90 85 N N 0\r\nVvtFQcwAErUIru 85 87 N Y 0\r\nJKTcYDOhBIJdTBG 94 85 Y N 0\r\nVJqtvLWNUdTEypjOCB 85 94 N Y 0\r\nET 80 80 N N 2', 'IXrLPdJhEekBRgsbkqI\r\n15850\r\n315050'),
			(2, 3, '100\r\nXODSnS 95 81 N N 5\r\nAmAM 95 87 Y N 0\r\nKjuUtQc 79 79 N Y 0\r\nCIKwP 80 95 N N 9\r\nXWINMqSzxcSwHu 90 90 N Y 0\r\nVmyNwsWNrzQzumpkYi 93 100 Y N 0\r\nJNyOUKq 95 97 N Y 0\r\nSbcPEILVKPtjJNI 95 95 N N 0\r\nHWlCiupwDIWsD 95 85 Y N 0\r\nUMaMMna 80 80 N Y 0\r\nFUGEOWMfgVlYlkQ 90 85 N N 0\r\nZECxJOwDbf 90 91 Y N 0\r\nULSdBWvpEoGyrriHXvB 80 89 N Y 0\r\nFdnaONSBrtoKpnLcOwQ 90 86 N N 0\r\nFcuXukEfTJaepfcVLGDo 90 88 N N 0\r\nE 91 80 Y Y 1\r\nWTTcx 99 95 Y Y 0\r\nCSuFsvUjAB 92 90 N N 0\r\nSpBFMARZU 90 86 N Y 0\r\nSJ 85 95 N N 0\r\nACBZClCVEY 80 80 N Y 0\r\nWbpifAcJsMvZsIuxPb 80 80 Y Y 0\r\nLglYGtexoUrlFtvCg 97 75 Y Y 0\r\nQurAslbu 95 85 Y Y 0\r\nCEykvNKWzMSYtFFZqGxB 94 89 N Y 0\r\nFUtuwRZqZGEBsetLG 75 90 N N 5\r\nNLlUN 90 95 Y N 0\r\nXkkZELIs 95 80 N N 0\r\nRLL 97 89 Y Y 0\r\nGzphmddzEKiHp 95 80 Y Y 0\r\nJqatUa 95 99 Y N 0\r\nTlckBkdqgu 88 76 Y N 0\r\nMsANXDtZCoYYKlxJyXxd 75 80 Y N 0\r\nNoUuxKxbhIE 80 95 Y Y 0\r\nWxiiIO 96 90 Y Y 0\r\nIAVsLoFQtsAAXIwkTF 98 99 N N 0\r\nJUCx 85 75 Y N 0\r\nVmJHYTcPKUoLTILytZM 85 78 N Y 0\r\nIGcbksHLMRlngsVqz 75 90 Y Y 4\r\nLrzHFThFFAasgfcZeuw 99 96 Y Y 0\r\nXQ 87 90 N Y 0\r\nS 90 100 Y Y 0\r\nBoN 75 75 Y N 0\r\nYTUXuruVhIaqN 87 80 Y N 0\r\nRZX 80 95 N Y 0\r\nLqQdXchxqHacFo 80 90 Y Y 0\r\nWenYJQMBPFHmut 85 80 Y N 0\r\nPAwecWyvQkikNfPV 92 80 Y N 6\r\nRkihOMXBLaHAcGkSdH 90 89 N Y 0\r\nDqIDtJrPa 94 95 N Y 0\r\nHssCJPipFFBNKqSTI 90 77 Y Y 0\r\nFvwYhmlFDwYLNGJo 76 95 N Y 0\r\nCktjdwA 87 85 N Y 2\r\nXqcLtILWrH 90 95 N Y 0\r\nWUWzuUt 85 80 Y Y 0\r\nITxsJXcGHC 90 94 Y N 0\r\nQawKYU 95 85 N N 0\r\nLXadSVqxcTbaP 93 94 Y N 0\r\nSqrSt 76 85 Y N 0\r\nYVpaVs 95 75 Y Y 7\r\nJI 95 90 N N 0\r\nOgYRmoSiNWVbSdocM 85 95 N Y 0\r\nYVCxaZPHiY 99 81 Y N 0\r\nBWqPJ 99 84 N Y 0\r\nXnBWuQdMh 93 85 N N 0\r\nEwKTLdQvmUJkdD 82 76 Y Y 0\r\nU 83 81 Y N 0\r\nApIwR 95 75 N N 0\r\nYFPeOIBN 99 97 Y N 0\r\nXJxUn 95 78 N Y 0\r\nYF 75 85 Y Y 0\r\nRMEpQYLSw 100 90 N Y 5\r\nTCIJOG 84 99 Y N 0\r\nFZXWWtgXTxSZFzDeSY 80 82 N Y 0\r\nWdxvfeOc 97 75 N Y 0\r\nSIlTmjkbHKqRirpOCAgq 84 90 N N 0\r\nHFjBUPtiaUxoZc 90 85 N Y 0\r\nSCxZrJjsfqHWYQf 85 94 Y N 0\r\nVhLeCMseuz 85 85 Y Y 0\r\nSDKqshkHjxs 75 98 Y N 0\r\nOZxC 95 80 Y Y 0\r\nU 87 75 N N 0\r\nPrSIkzb 80 77 Y Y 0\r\nUwmERlxVvWA 78 80 N N 0\r\nNSKbkCGjCf 75 85 N Y 0\r\nMriCgsUio 80 95 N Y 0\r\nZWRyHTJU 87 80 Y Y 0\r\nYoQBzJtKTZ 90 86 N N 0\r\nEGK 95 96 Y Y 0\r\nCYFVbgGoFwEhcgMTUw 95 90 N Y 0\r\nGrRWiR 85 80 N Y 0\r\nNYNlVNgO 95 75 Y Y 0\r\nNNRVpAF 85 89 N Y 0\r\nFMuTUqk 95 95 Y N 0\r\nPXD 85 95 N Y 0\r\nLyFr 99 100 Y Y 10\r\nOGLltVsbdriqDg 90 80 Y N 0\r\nScAWqkBXwtWz 90 95 Y Y 0\r\nEqgEwtbpSWwIWHxAN 94 80 N Y 0\r\nYmGW 97 85 N N 0\r\n', 'LyFr\r\n15850\r\n376500'),
			(2, 4, '100\r\nUFZOcQTBHjpwCMYn 90 95 Y Y 0\r\nKyrK 90 97 N N 0\r\nCysmgRwxDSNLpjrl 85 75 N Y 0\r\nELrEcHjXsUoiRd 76 93 Y N 0\r\nBJKXRcXMkoJGSfV 75 90 N N 0\r\nJlEaFDVDTdBUyQWax 80 85 N Y 0\r\nZCcbRRBkRfo 75 86 Y Y 8\r\nRkShJ 92 98 N N 0\r\nT 80 98 N N 0\r\nHGkgmL 90 96 Y Y 0\r\nLOlutboG 75 78 N N 0\r\nWPgpHocpVaFqHDsTqciN 80 75 N Y 0\r\nVPHEqLbNtBkYV 80 90 Y Y 0\r\nDHswaajQYBbZJeiqslc 97 93 N N 0\r\nZedpSTlOCjOSg 84 90 N Y 0\r\nDIJswe 90 75 Y Y 0\r\nCHPjh 90 87 Y Y 0\r\nQGdeyvvRut 99 75 Y N 0\r\nIB 85 90 N N 0\r\nAuCAAiPbUF 94 90 N N 0\r\nI 85 89 N Y 0\r\nUEGKE 93 85 Y N 0\r\nPyCqoFmtFcSItE 100 90 Y N 0\r\nPnrNRiqJ 75 95 N N 0\r\nTdtKaStTwPVWV 93 90 Y N 0\r\nX 88 76 Y N 0\r\nM 93 99 Y Y 0\r\nCS 79 93 Y N 0\r\nUAGVVOiUT 75 96 Y N 0\r\nPV 96 80 N Y 0\r\nVx 75 79 N Y 0\r\nBDgEmERNOmyX 91 75 Y N 0\r\nLJE 95 83 Y Y 0\r\nUIFGKBcTHsGxYANNHraq 85 89 Y N 0\r\nPTrTHHmcwH 85 75 Y N 0\r\nOgXBP 90 75 Y N 0\r\nWp 83 79 Y Y 0\r\nHDrhXVtEPAOR 83 75 N N 0\r\nLlpQK 80 90 Y N 0\r\nXqgkRUiFXMeWTSMIXae 95 90 Y N 0\r\nVxuemIoRgjUQe 93 93 N N 0\r\nUwS 90 76 Y N 0\r\nHOMNGhJfueorHMl 99 100 N Y 0\r\nHLawxZVlOg 80 99 N Y 0\r\nZTLejisS 80 79 Y Y 0\r\nCvvNscCnZVOiriPN 90 95 N N 0\r\nANyQeAqkYvGhFPCqWA 80 90 N Y 0\r\nUYBTL 80 85 N N 0\r\nDCBVl 82 95 N Y 0\r\nNdsOgK 76 75 N Y 0\r\nMFEuuuBChlMLjKO 90 88 N Y 0\r\nF 95 75 N N 0\r\nXMVNIZlUaUHaf 80 85 N Y 0\r\nBGTi 86 95 N Y 7\r\nTP 85 93 N Y 0\r\nTgLwEuo 85 83 Y N 0\r\nHHeXOMfgB 96 89 N Y 1\r\nDiymeUrkzGWyAJGJtm 76 100 N Y 6\r\nIfcRnqMJkslbJ 88 88 Y N 0\r\nXKYDCSfK 80 95 N N 0\r\nYO 90 92 Y Y 0\r\nZHFXiqnGIJY 75 80 Y N 0\r\nHUeQYggJJVpWayTyys 93 85 Y Y 0\r\nSnwoWt 80 92 N N 0\r\nZrKDpgLzzLRseYxwHFY 90 90 N N 0\r\nTJrFYWUrUTKp 75 82 N Y 0\r\nUkPbxhLAJPwJJgVU 97 88 N Y 0\r\nOTDoNRB 91 97 Y Y 0\r\nInnTSmiYxKKTfsUbn 80 75 Y N 0\r\nRXsvpaqbPjLXBlNaVrc 90 90 Y N 0\r\nNSBzurUwDWbAYHY 83 98 N Y 0\r\nWSTUbidxKesLHEJd 90 99 N Y 0\r\nVDKrj 100 94 N Y 0\r\nSdTmfCQEuOZ 95 82 N N 2\r\nVmKkpQPBwp 75 90 Y N 0\r\nHsZoSIMOmMxJKQl 80 80 N Y 0\r\nYLnnpg 95 87 Y N 0\r\nNZEUZGLRZCkYvb 90 90 Y Y 0\r\nUM 90 95 N Y 0\r\nTqLUjeKMZ 85 97 Y N 0\r\nMD 80 95 Y N 0\r\nYCKUZhGAAXAUZu 92 90 N Y 0\r\nTeeApAXcvDbCxqGbRbsX 85 85 N Y 0\r\nSWKpTiwqleAkXZisYF 87 92 N N 0\r\nJOuZVAqcd 88 95 N N 0\r\nAGcOK 86 89 N Y 0\r\nFtDLkGP 95 93 Y Y 0\r\nKH 83 89 N N 0\r\nTzXvxeEF 90 88 N Y 0\r\nYgQsmqAometSIsgT 84 75 Y Y 0\r\nFLglQZOCYBjbeJFEpf 100 86 Y Y 0\r\nCq 80 95 N N 0\r\nLM 90 88 N N 0\r\nRj 77 95 N N 0\r\nWdDRjsOnJ 85 95 Y Y 0\r\nBAf 88 85 N Y 0\r\nX 91 85 N N 0\r\nMfhcDIwTiiCR 95 94 Y Y 0\r\nYGxZUsVuOSVpGSAn 87 98 Y N 0\r\nTkcTzncqZDENcPOL 89 90 N N 0', 'HHeXOMfgB\r\n15000\r\n311200'),
			(2, 5, '100\r\nPbc 86 85 Y N 0\r\nHXekOchIacYeRpNpqxi 75 78 N Y 0\r\nVMFuCwR 76 94 Y N 0\r\nRFESPRSwimdNOupwD 80 95 Y Y 0\r\nAkibXDoYjl 90 85 N Y 0\r\nMiwTLxayOFxAjBViRCE 86 100 N N 0\r\nTrbOXCPmjbSY 95 83 Y N 0\r\nHAAd 97 85 N N 0\r\nLZWnJ 84 75 Y N 0\r\nXRZJiIM 75 98 Y Y 9\r\nIwyd 88 89 Y N 0\r\nSgzbizBGhc 85 85 Y Y 6\r\nOFWSciGYcyCvtllNPNl 80 95 N N 0\r\nIJLZmDJulOjSTOr 85 100 N Y 0\r\nIlB 91 96 N N 8\r\nSIu 80 88 N Y 0\r\nDkhOUqwZdT 85 95 Y N 0\r\nPjXKeUUW 95 84 N N 0\r\nVmQSKvueLmRedNUMHcLO 98 75 N Y 0\r\nBXJTdaZZlsbMaa 77 80 Y Y 0\r\nJsGVsFwcu 86 85 Y N 0\r\nOvNXiblcXgTVnI 77 92 N N 0\r\nSMfFDgoG 94 93 N N 0\r\nUKkFkFNtXIzWzkPTN 95 80 N Y 0\r\nSvjEjgBcHBTRa 75 90 Y Y 0\r\nSRUoWwMmFLC 95 75 Y Y 7\r\nRqJlWkCzrahlfQTl 78 90 Y N 0\r\nXM 83 95 N N 0\r\nDZGYKrDvBa 88 94 N Y 0\r\nJMWOjjgNEtdAuFJZxkVL 75 80 Y N 0\r\nRjVuxQkpbvImBSv 89 93 N N 0\r\nShRFbbvWZNN 90 75 Y N 0\r\nBtqngENjocSpxAuZlcst 94 85 Y N 0\r\nJKGnBehLBdPB 91 90 Y Y 0\r\nCxDMfiuHactmHL 85 77 Y N 0\r\nFyMCeegreCzeGiF 80 82 Y N 0\r\nMUmmSFNEqPuGrVMJV 94 100 Y Y 0\r\nESiXBieIwXjES 85 79 Y Y 0\r\nLbEUGjSHXFqNr 80 90 N N 0\r\nBNboso 99 87 N Y 0\r\nUGRLkihagF 75 95 Y N 0\r\nJgbbaZAhFzAWWU 76 90 N Y 8\r\nFLiHlZanTCruAiLek 96 86 N N 0\r\nBbCPScsCTdaDmgWuh 94 91 Y N 0\r\nSYWHdiAzRWdckWd 80 85 Y Y 0\r\nEvwptbmXArCR 95 95 N Y 0\r\nRY 98 92 Y Y 0\r\nGQUtOFJUR 95 97 N Y 0\r\nIHgVC 95 90 Y N 0\r\nJoVSgwUKMJfBOWnR 77 80 Y Y 0\r\nWThogBEIlxzGg 76 78 N Y 0\r\nPTjePsyUXzHULWtBvxzD 79 85 N N 0\r\nPykCKPyIye 84 96 Y N 0\r\nVWKNHlIUETsruuMFymJ 75 83 N N 0\r\nXVjgfdnPfWCoQLtskHG 95 90 Y N 0\r\nFLxTWlReygXsMQOi 90 95 Y N 0\r\nVSSIBzTVdqEjb 85 83 N Y 0\r\nICfTwPOuTpJIBcuPM 79 80 Y Y 0\r\nLIfnfxyFAilueGsiXbK 100 90 N Y 0\r\nILwZpBDiuoMhXwswxgEe 91 99 N N 0\r\nHgHAeuBIcJ 91 75 Y Y 7\r\nMLB 100 84 Y N 0\r\nG 90 80 N N 0\r\nRoTyLtQwejye 80 75 N N 10\r\nPjrf 93 84 Y Y 0\r\nMQrpSTxSdjQAq 93 75 N Y 8\r\nZyejTNiikWWHHyhI 97 92 Y N 0\r\nZNUegFN 95 85 N N 0\r\nC 90 78 N N 0\r\nBIccuOyGTp 88 95 N Y 0\r\nZmHNvOxTWUMjd 90 77 N Y 0\r\nSKMaiZvuWvgEdMmzzDZ 80 90 N Y 6\r\nNqaUtbTiXhp 80 99 N Y 0\r\nTLujwKJIHAPslBSjXPF 80 75 Y N 0\r\nOnIIxittrvwrLXOg 78 78 Y N 6\r\nGeqn 77 75 N Y 0\r\nEdwUkyV 75 90 N Y 0\r\nY 82 78 N Y 0\r\nDrhUzffpm 90 88 N Y 0\r\nFeYbszANUnub 79 77 N N 0\r\nBxELfghqaNDhRG 80 94 N Y 0\r\nJbfWtaM 85 91 N Y 0\r\nFAIUHwUVMgvkWXVaBdV 94 90 N N 0\r\nDqQlBCPHLF 94 80 Y N 0\r\nQavAC 75 98 Y N 0\r\nSwkfKqtTG 80 80 Y Y 0\r\nZhCIZAGyhajH 88 91 Y Y 0\r\nLZ 84 78 Y N 0\r\nHUXJFQzmIhfFR 89 80 N Y 0\r\nJEavDBAzVy 93 86 Y N 0\r\nCNpKdOcj 87 78 N N 0\r\nYjaRmeRbQQ 88 86 Y Y 2\r\nHfLkfcP 98 80 Y N 0\r\nIuzgXQyhMgghbM 94 85 Y N 0\r\nUwPRvymKaTxLhXPLgS 87 95 Y N 0\r\nAsqWOw 85 91 Y N 0\r\nUcrOLkHfk 77 75 Y Y 0\r\nLMQGBivsyG 89 80 N Y 0\r\nLYfhBBKEvv 92 96 N N 0\r\nEKwHlSMSICqMs 79 75 Y N 0', 'IlB\r\n14000\r\n320050'),
			(2, 6, '100\r\nRDVRyo 75 89 N N 0\r\nNbLorLPWFust 75 85 N Y 0\r\nPYjzHwP 99 94 Y Y 0\r\nFcfubnlnNNUPGQQks 80 83 Y N 0\r\nHlkyMwOyDtQ 90 85 Y N 0\r\nYULUAHWXNXglhEOpu 90 95 N N 0\r\nUVrGVYXgSay 87 89 Y Y 0\r\nN 82 75 Y N 0\r\nCiZdpzk 85 92 Y N 0\r\nYGoIYPAaUZB 75 95 Y N 0\r\nVsULAuYGLRJTn 97 77 Y N 0\r\nEdOairN 80 90 Y N 0\r\nQidSkeNNiZuEQMSS 95 87 N N 0\r\nCkeyipoORDPVzh 90 85 N Y 0\r\nS 86 95 Y N 0\r\nRsk 90 95 Y Y 0\r\nJoUxXo 75 85 Y N 0\r\nVEyWcKmMwEECmXUUiN 99 85 N N 0\r\nEgYwAo 75 90 Y Y 0\r\nPCPoQWeo 79 95 Y Y 6\r\nPDhTYMyNDvkr 80 75 N N 0\r\nFGeahCxoC 80 98 Y N 0\r\nGwmWBkiqsKZQmm 85 95 Y Y 0\r\nOzlKFaZupcVfJeH 80 80 N Y 0\r\nWPovsYBSwhEKRpsaPzA 93 75 N Y 0\r\nSjHddMMKtLE 91 95 Y N 0\r\nIb 80 90 Y N 0\r\nEgZA 89 80 Y N 1\r\nHJxEs 96 75 Y N 0\r\nRqhYuUtNieNUnsvhaH 92 85 N N 2\r\nRdolbbFE 95 85 N Y 0\r\nAgJKkdhVMwNueyuhMi 92 91 Y N 0\r\nMOdkuwdyuvUuKZnD 85 89 N N 0\r\nFIOpJgZoWKpCo 82 98 N Y 0\r\nLAzGkTsTKZuWcpWx 95 85 Y Y 0\r\nBGbeVtkPhUzRVPddWi 98 76 Y N 0\r\nEifucsMhQovhiHzmYrd 75 90 N N 0\r\nASwu 96 80 Y N 0\r\nAAWCaBffGJongVqwkk 76 80 Y Y 0\r\nNFcgTeyT 99 85 Y N 9\r\nDGMqW 95 100 N Y 3\r\nNmPsTEYOkY 81 80 Y Y 0\r\nKylj 95 85 Y Y 0\r\nMKEamibK 97 95 N N 0\r\nSfXlWCEcosb 95 90 N N 0\r\nMShPPnQtdnIYWCJJF 95 85 Y Y 0\r\nBxQVYzQuOtoC 85 80 N N 0\r\nOAoQGn 92 81 Y N 0\r\nHHzpC 81 88 N N 8\r\nQOXBqRdnYAs 85 95 Y Y 0\r\nVjvQGFcMjvXTSDK 95 81 Y N 0\r\nAzlksTEIhMRSJRhxy 89 75 N Y 0\r\nPrFscCCSRZFQ 84 88 Y N 0\r\nTtahPgzMrPvncCsOMMzp 90 100 N N 0\r\nPKVULSvBNLSDlQ 90 90 N Y 0\r\nAVEtFUveYxcnPfOXfHI 80 92 N N 0\r\nNmBWPsDTtvDlAt 100 80 N N 0\r\nF 80 81 Y N 0\r\nRqHsLEooY 80 90 N N 0\r\nPtOLgkhcR 85 80 N Y 0\r\nPLOHK 94 90 N N 0\r\nP 82 75 N N 7\r\nUmciCWoyT 80 78 Y Y 0\r\nGAWDweGDYskHnQk 90 100 Y N 0\r\nIJE 81 83 Y Y 0\r\nOnVFuxevWzTDA 93 86 Y N 0\r\nAHrHvyaPCfsVNmDIWWo 86 91 N Y 6\r\nKCBSLlcfJyAPBj 85 75 Y Y 0\r\nQawOMDkeJlormRnhe 90 81 Y N 0\r\nOT 90 98 Y N 0\r\nBYaPLabeEVwtrB 88 82 N Y 0\r\nVamAq 85 89 Y N 0\r\nVTiUA 95 80 N N 0\r\nSSNkaLdkxmAtP 85 95 N N 0\r\nOJB 98 91 N N 0\r\nLIqQalcebHdzj 75 96 Y Y 10\r\nWKlGLytTd 95 89 N Y 0\r\nPIXhCtLSMoCCA 96 80 Y Y 0\r\nULzmzuqKLBoAFtK 80 80 N N 0\r\nZnlCehX 77 77 Y N 0\r\nCEjxxKKGDf 90 90 N Y 0\r\nHvpDIKdEiUzWvt 90 81 Y N 0\r\nTlMuWvRTlNj 97 75 N N 8\r\nYIFvifAtXjyvDF 76 97 Y N 0\r\nMtCNoAEoqOJPv 95 90 N Y 0\r\nKflCVSqbOBK 90 81 Y Y 0\r\nFc 91 85 N Y 0\r\nXpWdeRA 89 76 Y Y 0\r\nOorwfyqXYbZjrTpclNF 84 95 N Y 0\r\nUoysYXmlvO 81 95 Y N 0\r\nAMlBgQfD 80 97 N Y 0\r\nUpOkXMuyURCCRaoCw 80 80 N N 1\r\nFtIeDqx 98 90 Y Y 0\r\nCiVc 75 95 Y Y 0\r\nBdKO 75 75 Y Y 0\r\nNhDwAd 80 85 Y N 0\r\nKUTLEFlaYoIojUVk 96 81 Y N 0\r\nCBEsPPZB 75 75 Y N 0\r\nJvE 76 93 Y Y 0\r\nCgjkSvcwDthjnprSHxx 95 75 Y Y 0', 'DGMqW\r\n15000\r\n350700'),
			(2, 7, '100\r\nMzYCufzWJkGT 95 90 N Y 0\r\nKYmIQs 80 85 N Y 0\r\nNVufr 95 94 N N 0\r\nJmhCuEFOUoKgI 75 88 N N 0\r\nKyRmZdbJMfYTzbZS 78 90 Y Y 0\r\nESBjuYJuibPovnKCdyQ 95 77 Y N 0\r\nSfKyrATJa 90 85 N N 0\r\nJpPNv 80 85 N N 0\r\nQLXkCluFcKh 85 75 N Y 0\r\nTfYqUD 90 85 N N 0\r\nFAyWTFfabq 80 95 Y N 0\r\nRktoKBKAm 90 83 Y N 0\r\nFNdCECSt 95 81 Y N 2\r\nKsHzrelGZguPwheHhw 79 80 Y Y 0\r\nFprWTbg 90 95 N N 0\r\nSgXaGDAruSFH 90 75 N Y 0\r\nIw 79 92 Y N 0\r\nAj 96 75 N Y 0\r\nGRXyYlVokmXU 85 80 N N 0\r\nXYhTEVBhVhGbtlEEDapv 96 95 Y N 0\r\nMIcOWomeJ 90 75 N N 0\r\nAicIWqZoiKLlExLgYq 75 77 N Y 0\r\nMpThDPKOTcuHdAsCOj 88 75 N Y 0\r\nJhHKDBmYwDjI 90 75 Y N 0\r\nPZIp 89 78 Y Y 0\r\nKGrTUPBKfHiGdkhDyp 79 99 N Y 0\r\nIVzqVKMUtMEMNMhDZoC 77 95 Y Y 1\r\nUH 95 76 Y Y 0\r\nHZaQTPJAIDfxlBWa 97 95 N Y 0\r\nOYqqFxNEKi 92 93 Y Y 0\r\nYEIGXpWWAVdjYliI 75 95 N N 0\r\nKiEZUReyFC 76 96 N N 0\r\nAKVxHnqsq 100 84 Y N 0\r\nNeynYn 80 100 Y Y 0\r\nMGYvGdJtzgDQMJbKiqi 90 91 N Y 0\r\nCJxNdGRvszHMJ 90 100 N Y 0\r\nQaW 84 90 N Y 0\r\nYVJbiEvWMhkmwpk 95 75 N N 0\r\nJgGIdXehJoJXBFEZ 79 85 N N 0\r\nHgdZeHAia 90 95 N N 0\r\nKVRKybLWtgvMO 90 79 Y N 0\r\nFqQiwCWcYwwJFRTzs 83 81 Y Y 0\r\nExHBuhysmTCImTNM 95 85 N Y 0\r\nVRPKYyLs 90 95 Y N 0\r\nWRhPbCFTEGoIsYJ 95 85 Y Y 0\r\nYJrRSaXAyCFXthWKXb 95 90 N Y 0\r\nLSGZRyroxESqlTDLPygW 75 84 N Y 0\r\nOAzBzPhGwXBbXyceKAh 75 79 N N 0\r\nMRcf 78 75 N N 0\r\nPnocRXHxa 90 94 N N 0\r\nDSrYGnQkbkaJEj 89 83 Y Y 0\r\nMDNbsqgFkkNJcKSMltgt 92 79 N N 0\r\nXQjtBfngEEWlCWkDmWxs 99 85 N Y 0\r\nLWxGGzcULfdYz 83 91 N N 0\r\nXkkjQEZvHuxmj 80 100 Y N 0\r\nQHorTyeoyv 95 85 Y Y 0\r\nXhKjVOvliOE 95 95 N N 0\r\nHsbIENhJzHBIybuib 81 80 N N 0\r\nEx 80 83 N Y 0\r\nKOHSxFDkI 95 80 N Y 2\r\nDNiRiR 85 76 N Y 0\r\nTyyWUjDkDzrvlJxhdu 89 95 N Y 0\r\nMnadQyg 85 95 Y N 0\r\nAEhHsxsQNLlzX 89 75 N Y 0\r\nQlbGWGQiLuXQwjQdXjqw 95 91 N Y 0\r\nYeacrY 78 85 N N 0\r\nAYwyiyCg 87 95 Y N 0\r\nAIrEnhkkiMRCw 93 96 Y N 0\r\nAlerTviL 100 75 N Y 1\r\nQa 80 90 N Y 0\r\nEL 88 80 N Y 0\r\nHZhEUJKFapBYiXrIBmLl 95 80 N N 0\r\nPVTrtVL 75 85 Y N 0\r\nCrUuGaAAWY 91 80 Y Y 0\r\nF 98 90 N Y 10\r\nEDzMsdUoHZRbm 92 86 Y N 0\r\nSWgAnBqjdspqimqSnIe 80 79 N N 0\r\nTiywblZKQxphP 95 75 N N 0\r\nAbVxejWeLkARSHS 90 90 N N 0\r\nBTSNYgaM 90 85 Y N 0\r\nIBKYOiYOx 90 95 Y Y 0\r\nGkkFOl 84 96 Y N 0\r\nOfWoBlbIdoxZiFQTDqb 92 90 N N 0\r\nKjLfr 80 90 Y N 0\r\nDQ 80 80 Y Y 0\r\nVHdwANLLL 97 93 Y Y 0\r\nXZgHcNSvCmhsYr 95 87 N Y 0\r\nLglligckY 88 95 N N 0\r\nGWamLFvttqLvqYEk 75 91 Y Y 0\r\nBkIcJbGaqMsx 80 91 N N 0\r\nVzpyHxfHMkWKGLmKmBD 90 88 N N 0\r\nRnNNqDfDPe 98 90 N Y 0\r\nWKUwbrZUPSlemyHnG 95 77 N Y 0\r\nUuPiJyTrZldgFC 95 86 N N 0\r\nOINCIjHytMtjypLS 89 85 N N 0\r\nXPmwFEm 75 94 Y N 0\r\nXuop 80 75 Y Y 0\r\nXdwaEcQOwXmIvDeVFn 90 85 Y Y 0\r\nXyYOt 95 96 N Y 0\r\nOpiGinpQf 86 80 N Y 0', 'F\r\n15000\r\n324650'),
			(2, 8, '100\r\nPUQUnnMCvvifcYKS 91 81 N N 0\r\nLKmTZKyzAogY 96 99 Y N 1\r\nHxgsRNdLJzcPYIi 82 92 N Y 2\r\nAjpLBYCvsxjmqY 95 91 N Y 0\r\nTqihhV 85 83 Y Y 6\r\nHRC 89 80 N N 0\r\nH 100 85 Y Y 0\r\nNixtCBiobBBcOxbGmJ 79 95 Y N 0\r\nXq 75 81 Y Y 0\r\nETtrHHnqiLdpwQKei 81 94 N Y 0\r\nNYL 95 75 Y N 0\r\nXUBCvg 95 88 Y N 0\r\nKsoPUD 90 84 N N 0\r\nXeeiHfsVEaqQrbtHlSb 90 75 Y Y 0\r\nUCUUW 98 80 N N 0\r\nARz 75 95 N N 1\r\nLRiaFqnoGFWLYokaNauW 81 91 N Y 0\r\nHyrqIxzusfvxXQ 92 95 Y N 0\r\nZugb 84 85 Y N 0\r\nHuECLLgDZETrNuxx 90 95 Y Y 0\r\nGMUeTHqzlB 94 75 Y Y 0\r\nWcaiZXCiTYUJjeUhIxz 95 92 N N 0\r\nChPCIEoAYbPSTwqUTy 98 75 N Y 0\r\nSTnIrpcGfyuqKlop 92 91 Y Y 0\r\nMhvuI 95 95 N N 0\r\nJJjOVCtL 90 99 Y Y 0\r\nQ 97 77 Y N 0\r\nVCbsLD 95 94 Y N 0\r\nWGtaOifRd 90 76 Y N 0\r\nErCwyPRnxvZaeXzyqC 80 99 Y Y 0\r\nF 80 76 Y N 0\r\nEqWWrL 87 100 N Y 4\r\nCW 95 80 Y Y 0\r\nTZYiouTkHas 75 75 Y Y 0\r\nYBqilxkGZJx 94 100 N Y 8\r\nZhQ 85 86 N N 0\r\nVqVOGkunHXMjzFhKT 82 84 Y N 0\r\nYgjIMJvrSwpjyBt 85 80 N N 0\r\nNSIjGAPKYLIb 98 85 Y Y 0\r\nPmqLFEWIgBblJnAxKmE 75 99 Y Y 0\r\nGCJlSsKJXzHCzIt 80 90 N Y 0\r\nChMGtPHhjP 97 95 N N 0\r\nUERuLLCJbpvr 75 97 Y N 0\r\nIHfRyvenjUNvZ 85 75 N N 0\r\nHrysA 86 95 Y N 0\r\nEeoJjO 88 84 N Y 0\r\nGdZaBCPYUdUa 89 85 N Y 0\r\nMKeeBYun 99 93 N Y 0\r\nSl 95 75 N Y 10\r\nKLaTAxVp 92 86 Y Y 0\r\nMaNkrMVOIxjJiYZX 80 83 Y N 0\r\nLUOedeGTBheXtSdL 91 86 N N 0\r\nQuektgzYapx 90 77 N N 0\r\nHdqsc 85 85 Y N 0\r\nEB 85 80 N Y 0\r\nDBGfXvy 95 96 Y N 0\r\nRiIQjTwl 80 81 N Y 0\r\nAyOs 85 98 Y N 0\r\nOtqaipehLnhnxXnBkx 75 90 Y Y 0\r\nOA 81 88 Y N 0\r\nT 98 92 N N 0\r\nDDYefzXNngYeeueBH 90 95 Y Y 0\r\nCOwFGmydoBP 87 75 N Y 0\r\nTMkfvMdaChWfbREF 90 75 N Y 0\r\nMurkdxrRZWbCmvOTPvX 80 99 Y Y 0\r\nCHOp 77 80 Y Y 0\r\nXaCeksUvjVYoTDDlA 95 85 N N 0\r\nQjiNbZXtTKPMqKDfC 99 90 Y N 0\r\nIRtjKjVZYFGo 95 95 Y Y 0\r\nI 80 79 N N 0\r\nBhcbBj 90 80 Y Y 0\r\nScTyrOqPI 85 91 N Y 0\r\nQvT 95 80 N N 0\r\nNtVJkwJ 85 78 Y N 0\r\nMfhGppwKNPRNMJ 75 85 Y Y 0\r\nAqjRLvrFzTTOd 88 99 N N 0\r\nVyPhUiVNUrlIKJGTIJMV 78 85 N N 0\r\nDSIhYSkoGUPvJlBWCPs 97 85 N N 0\r\nVAHle 92 87 Y Y 0\r\nIzCSMnowRthplqftD 91 100 Y N 0\r\nVOnGdpBBBeMJFP 76 80 N Y 0\r\nHZRPVHGQ 90 85 N N 0\r\nVjRxopMjUjh 95 88 Y N 0\r\nF 93 90 Y N 4\r\nKDIiqBJvyVKmYK 79 78 Y Y 0\r\nVjbvWqtedXaCIlkFa 83 85 Y N 10\r\nAzSQFau 92 99 Y N 0\r\nVo 85 98 N N 0\r\nRr 91 94 N N 0\r\nNwJjjFhgc 91 90 Y Y 0\r\nVjrlhAGmIK 85 82 Y Y 0\r\nR 80 90 N Y 0\r\nWFiVNPPGtLyYutaXuP 93 97 N N 0\r\nVE 90 98 Y Y 0\r\nJkMtq 75 85 N Y 0\r\nKnCvGPSsFGvSy 80 79 Y N 0\r\nZjNuJifeLfDCp 88 94 N N 0\r\nSSkVniNxsPaduyghLQFq 75 99 N N 0\r\nPW 78 76 N Y 0\r\nPCvt 81 80 Y N 0', 'YBqilxkGZJx\r\n15000\r\n366150'),
			(2, 9, '100\r\nZWbnpvRIZYJkleTdfZm 75 82 N N 0\r\nBVfxDT 85 99 Y N 0\r\nXPigvuqORPVhH 75 97 Y Y 0\r\nDRbWXUfI 86 95 N Y 0\r\nQrdEmydUhZmVaZKHPlY 80 90 N Y 0\r\nR 80 83 Y Y 7\r\nMXJurXEyZkLbB 80 95 N N 0\r\nHQNyPigyvBddctC 95 90 N Y 0\r\nSrYvKdTfbO 95 90 Y N 0\r\nXEpMyTjZHx 75 85 N Y 0\r\nVhxFByGDdjU 82 79 N Y 0\r\nTSJF 75 85 Y N 0\r\nRaNxtaKTFUH 75 85 N N 0\r\nQ 85 90 Y Y 0\r\nSNdToBHYhIODk 95 80 N N 0\r\nZUFXPiUqfVJOjec 90 95 N Y 0\r\nTtPGxHTxSksAeChr 97 85 Y N 0\r\nTBjcMyfnmOTAFRBAy 80 95 Y N 0\r\nNeNNKuGPA 85 85 Y Y 0\r\nRjpJU 90 100 N N 0\r\nHdpCfN 96 78 Y N 0\r\nMryDEMkOfqwSYEMDzvw 84 93 Y N 0\r\nQJSTGG 85 75 N N 0\r\nOpG 75 95 Y Y 0\r\nOKvIUnohvvH 85 90 Y Y 0\r\nUvIkrDvw 85 95 Y Y 0\r\nDK 97 80 Y Y 0\r\nZhFBfNI 93 87 Y Y 0\r\nCLjLchtBYEq 95 87 N Y 0\r\nPZcwNuqupy 81 81 N Y 0\r\nSeRxDAjSxRXhmAMkv 79 85 N N 0\r\nVZVMSMwqGlJdsuE 92 90 N N 0\r\nDdYSFBVugwKUAnE 95 98 N N 0\r\nTsvFTlmekL 75 80 N N 0\r\nUvTUZWkSvmjUPDaAZ 90 90 N N 0\r\nVSfPolWPavDhmQOxy 90 95 Y Y 0\r\nXcHhLlwHNBaLqHuHZ 75 90 N N 0\r\nHccBNvJEZuRnmCAmnl 97 98 Y N 0\r\nQTclkTMhvcYKu 97 80 N Y 0\r\nNMZzGNgKSOeHzp 81 88 N N 0\r\nNJnMAanjfVGpATmqvIag 86 88 N N 0\r\nHcwqojkcroWJ 95 96 N Y 0\r\nSRSPS 80 93 N N 0\r\nQIN 86 75 N Y 0\r\nQSnylWCLeY 90 75 Y Y 0\r\nZqs 93 95 Y N 0\r\nIaKMvvyXhEKcQU 83 80 N Y 0\r\nSSqCmnqe 85 85 Y N 0\r\nRaC 90 85 N N 1\r\nCutGiRlbfFVfvWyQFsJb 84 78 N Y 0\r\nYtUHmp 95 80 N Y 0\r\nBoPq 77 79 Y N 0\r\nIjGxHLRfNh 85 100 Y N 0\r\nJQjnglSiKkryCRc 75 95 N N 0\r\nONNkxDOOSxCDRLMCpSI 75 78 N N 0\r\nJcxvkDlcaYc 75 90 Y Y 0\r\nURQFG 77 99 N Y 0\r\nFLPwNKHXCJLGEDmn 95 90 N Y 0\r\nWtFhHtPBSKAMEm 90 98 Y N 0\r\nLJwcvOIxhEWnJ 86 76 Y N 0\r\nEeXUIRXVjmVNogOKVaPm 90 90 Y N 0\r\nGqcqbIczEZg 75 85 N N 0\r\nBtjChcni 90 75 N N 0\r\nGqsbbvwAF 90 90 N N 8\r\nMRmnXO 85 89 N Y 0\r\nHfewNyVImElyIwmRTA 85 95 N Y 0\r\nSu 88 89 N N 0\r\nKpPSkwPpNumfAi 83 95 Y Y 0\r\nZuFVrVdRbSgmvTcUZnG 95 75 Y N 0\r\nZYyzaxtXDzc 82 98 Y N 0\r\nM 91 76 N N 0\r\nGY 97 76 N Y 0\r\nKscrXVVGlmvGJwEpOu 100 85 Y N 0\r\nSGuJtFLvQSYXRFnKSpgp 95 92 Y Y 0\r\nPgcCsHapGTMxIbnht 78 98 N N 0\r\nSJyYQactcQMZWim 78 79 N N 0\r\nH 80 90 N N 0\r\nBBsRJ 85 96 N Y 0\r\nBmCoJUxz 94 78 Y Y 0\r\nPritVvHvQlR 76 75 Y N 0\r\nLjgYEUOcYiQTQaHn 87 75 Y Y 3\r\nJKbXgdHfyHtLdaBXLr 95 95 Y Y 0\r\nVLrJ 76 92 Y Y 0\r\nAKeSImMMnPWwfSeK 95 80 N Y 0\r\nBpmnb 75 90 Y Y 0\r\nCqfG 77 94 N Y 0\r\nIAJwwwJZ 81 85 Y N 0\r\nJOjgjtFkg 90 82 N N 0\r\nSfAXhmimdeALjlt 99 75 Y Y 6\r\nHFcmLIAAaLkmuBcgMPD 80 93 Y Y 0\r\nYRlYpPUpiypnNUv 85 90 Y N 0\r\nYcan 83 95 N Y 0\r\nQKppcgDSmUA 85 91 N N 0\r\nCPENHMuOf 84 95 Y Y 0\r\nHEnizmxQ 95 98 N N 0\r\nFPkNNyBfI 77 99 N N 0\r\nTzQtxShKSIjFIudklVwu 85 80 Y Y 0\r\nYLZXyeAtPtHtbw 95 91 Y Y 0\r\nWJnALOhkMQdEuUwU 76 90 Y N 0\r\nEoZKcSVHWSIWCPCPvog 75 90 Y N 0', 'RaC\r\n12000\r\n249600'),
			(2, 10, '100\r\nMKMqK 80 85 N Y 9\r\nIEctzCZvnQfICWIWfFLK 90 95 N N 0\r\nHNWIrMeY 85 85 Y Y 0\r\nMoZJrC 91 80 Y Y 0\r\nCUpsuWtkkZxrSzTDWrm 85 88 N N 0\r\nVg 94 80 N N 0\r\nBxaxLJ 75 90 N N 0\r\nWtEEnIDxjkneYioJpF 93 94 Y Y 5\r\nQoZgkEptZbbFk 95 87 N N 0\r\nPFSfksEGAiOjyR 90 90 Y N 0\r\nKOtiQAqJCEEJBsLjXX 75 90 N N 0\r\nKVFasFCnYY 97 75 N N 0\r\nDZCdrd 83 75 N N 0\r\nRmkPy 85 99 N Y 0\r\nADDOawMkUzfyond 85 96 N Y 0\r\nJ 90 96 N Y 0\r\nDGrJZNnXAds 90 88 Y N 0\r\nPSVlyQusCz 94 78 N Y 0\r\nFKiFCuIaYyDAKdQExP 94 90 Y Y 0\r\nKucq 76 85 Y Y 0\r\nJrx 92 95 N Y 0\r\nJdcUlJdLFVrWcHfD 99 92 N N 4\r\nHJejNlqlfl 90 90 N Y 0\r\nLCgqJrxtdY 80 90 Y Y 0\r\nVCKSpIfUFQN 100 85 Y Y 0\r\nCMlgGZfTJhMaMBD 85 99 Y N 2\r\nRgOFv 98 95 N Y 0\r\nImyBlcqYSohDrBKgWiOw 90 95 N N 0\r\nPLpRbrJLBpXl 87 95 N N 0\r\nVmOasxKQxuHvRRgmVxm 80 75 N N 8\r\nEtAl 85 95 N Y 0\r\nZxMxbZm 78 80 Y N 0\r\nCekNC 94 100 Y N 0\r\nPHjh 87 79 Y Y 7\r\nFu 92 100 N N 0\r\nXwYL 95 84 Y Y 0\r\nJ 100 90 Y Y 0\r\nUziOofGKg 99 94 N N 0\r\nGcAmlDYFVvkobDieO 77 95 N Y 0\r\nFAb 97 86 N N 0\r\nChLpjgcLIMLp 80 75 Y Y 0\r\nApGVbsaDCJVukBIQ 85 81 Y N 0\r\nWc 97 100 N Y 0\r\nImncIeHTwzypTiIWBQ 98 75 N N 0\r\nXPVYyk 75 89 N N 0\r\nNHZHmRKwXI 83 75 Y N 0\r\nDM 80 90 Y Y 7\r\nLppcqQrCLRjIHkiLEd 90 90 N N 0\r\nRPHGSuNaoztGZko 85 82 N N 0\r\nDddqWQQVAwxikNNoeW 91 89 Y N 0\r\nBFTaoWzkuyQB 85 94 N N 0\r\nHhdIKYirH 75 95 Y Y 0\r\nHlRqHOVjZiN 99 99 Y N 1\r\nB 95 85 N N 9\r\nJDN 85 94 Y N 0\r\nTcfXiQglK 94 80 N N 0\r\nDwDymNFtG 80 94 N N 0\r\nCPnnwCCp 79 80 Y Y 0\r\nLruJDUoaoyJyVtlmdZv 95 90 Y N 2\r\nPfIKvfDcaZgxWxcjewFl 75 83 N N 5\r\nWq 85 76 N N 0\r\nU 86 82 N N 9\r\nPiDmsTZrF 95 100 Y Y 0\r\nQtNrVowRKOClYv 85 90 N Y 0\r\nJfOn 87 84 N N 0\r\nD 100 85 Y N 0\r\nLRluUVRvG 75 85 N Y 0\r\nUVntkIi 89 90 Y Y 0\r\nUprSnqkHtcVisKhrNuM 81 85 N Y 0\r\nMyIbmxZNtgB 89 94 N Y 0\r\nSuCabMtfwvockNPuF 85 93 Y Y 0\r\nX 95 90 Y N 0\r\nBsXnpODR 75 95 N N 0\r\nUCXOghcRLaAegXrsi 90 90 N Y 0\r\nI 80 84 Y Y 0\r\nVbWcKcOTKW 96 84 N Y 0\r\nXCXXIhzBuhAkCICuCQx 75 77 N Y 0\r\nCZYFXfEtrmuvynE 80 83 Y N 0\r\nOOimvkjsv 95 84 N N 0\r\nHnrDJfnYUhNv 75 80 Y Y 0\r\nLBXjQ 90 75 Y Y 0\r\nCu 95 80 Y Y 0\r\nRicNxhoQMXysqolNVG 93 100 N N 0\r\nFmRrDUXWsnVLPRpEjd 80 75 Y Y 10\r\nH 89 75 N N 0\r\nJijsVMfofWFKNvoleuA 82 85 Y N 0\r\nY 95 82 Y N 0\r\nPB 80 85 N Y 0\r\nAITdgrAtTqewmaEg 95 98 Y N 0\r\nTOoIxndhLCwiqNqKRE 75 79 N Y 0\r\nPVifoSwvW 85 100 Y Y 0\r\nDcrXXJg 98 77 N Y 0\r\nBsPzbNHofQccxAnvYFp 75 90 Y Y 0\r\nAsKtCZ 84 95 Y N 0\r\nQRruobRoCiIqZroDGT 91 85 N N 0\r\nRXxREgopyQZRJlTn 75 90 Y Y 0\r\nFQ 75 75 N N 1\r\nUZIqTWG 87 96 Y N 0\r\nIlwVFRifPbByqYYWn 90 85 N N 0\r\nVBNAOsPEaXFcMwLMg 80 90 N Y 0', 'WtEEnIDxjkneYioJpF\r\n15850\r\n351900');

--
-- 评测结果数据表
--

-- 字段内容：评测结果ID（主键）、评测结果简称（字符串）、评测结果全称（字符串）

--
-- Table structure for table `judge_results`
--

CREATE TABLE `judge_results` (
  `judge_result_id` INT AUTO_INCREMENT PRIMARY KEY,
  `judge_result_slug` VARCHAR(4) COLLATE utf8mb4_unicode_ci UNIQUE NOT NULL,
  `judge_result_name` VARCHAR(32) COLLATE utf8mb4_unicode_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

INSERT INTO `judge_results` (`judge_result_id`, `judge_result_slug`, `judge_result_name`) VALUES
			(1, 'PD', 'Pending'),
			(2, 'AC', 'Accepted'),
			(3, 'WA', 'Wrong Answer'),
			(4, 'TLE', 'Time Limit Exceed'),
			(5, 'OLE', 'Output Limit Exceed'),
			(6, 'MLE', 'Memory Limit Exceed'),
			(7, 'RE', 'Runtime Error'),
			(8, 'PE', 'Presentation Error'),
			(9, 'CE', 'Compile Error'),
			(10, 'SE', 'System Error');

--
-- 学生提交代码数据表
--

-- 字段内容：提交ID（主键）、学生ID（连接学生数据表的外键）、题目ID（连接题目数据表的外键）、
-- 			编程语言（连接编程语言数据表的外键）、提交的代码（文本）、提交时刻（Date）、
-- 			代码执行时刻（Date）、代码执行耗时（数值，毫秒，可罚时）、
-- 			代码执行占用内存（数值，KB）、得分（数值）、评测结果（字符串）、编译器输出结果（文本）

--
-- Table structure for table `submissions`
--

CREATE TABLE `submissions` (
  `submission_id` BIGINT AUTO_INCREMENT PRIMARY KEY,
  `student_id` BIGINT NOT NULL,
  `problem_id` BIGINT NOT NULL,
  `language_id` INT DEFAULT 1 NOT NULL,
  `submit_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
  `execute_time` TIMESTAMP,
  `used_time` INT,
  `used_memory` INT,
  `judge_score` INT,
  `judge_result` VARCHAR(8) COLLATE utf8mb4_unicode_ci DEFAULT 'PD' NOT NULL,
  `submit_code` TEXT COLLATE utf8mb4_unicode_ci NOT NULL,
  `compile_output` TEXT COLLATE utf8mb4_unicode_ci,
  CONSTRAINT `submissions_students_fk` FOREIGN KEY (`student_id`) REFERENCES `students` (`student_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `submissions_problems_fk` FOREIGN KEY (`problem_id`) REFERENCES `problems` (`problem_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `submissions_languages_fk` FOREIGN KEY (`language_id`) REFERENCES `languages` (`language_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

INSERT INTO `submissions` (`submission_id`, `student_id`, `problem_id`, `language_id`, `submit_time`, `execute_time`, `used_time`, `used_memory`, `judge_score`, `judge_result`, `submit_code`, `compile_output`) VALUES
			(1, 1, 1, 3, '2020-02-01 00:00:00', '2020-02-01 00:00:05', 30, 280, 100, 'AC', '#include <iostream>\r\n\r\nint main() {\r\n    int a = 0, b = 0;\r\n    \r\n    std::cin >> a >> b;\r\n    std::cout << a + b << std::endl;\r\n    \r\n    return 0;\r\n}', 'Compile Success.\r\n\r\n- Test Point #0: Accepted, time = 0 ms, mem = 276 KiB, score = 10\r\n- Test Point #1: Accepted, time = 15 ms, mem = 276 KiB, score = 10\r\n- Test Point #2: Accepted, time = 0 ms, mem = 280 KiB, score = 10\r\n- Test Point #3: Accepted, time = 0 ms, mem = 276 KiB, score = 10\r\n- Test Point #4: Accepted, time = 15 ms, mem = 276 KiB, score = 10\r\n- Test Point #5: Accepted, time = 0 ms, mem = 276 KiB, score = 10\r\n- Test Point #6: Accepted, time = 0 ms, mem = 280 KiB, score = 10\r\n- Test Point #7: Accepted, time = 0 ms, mem = 280 KiB, score = 10\r\n- Test Point #8: Accepted, time = 0 ms, mem = 276 KiB, score = 10\r\n- Test Point #9: Accepted, time = 0 ms, mem = 280 KiB, score = 10\r\n\r\nAccepted, time = 30 ms, mem = 280 KiB, score = 100'),
			(2, 1, 1, 4, '2020-01-17 23:59:59', '2020-01-18 00:00:00', 30, 280, 0, 'WA', 'public class Main {\r\n    public static void main(String[] args) {\r\n        System.out.println("Hello World");\r\n    }\r\n}', 'Wrong Answer.\r\n\r\n- Test Point #0: Wrong Answer, time = 0 ms, mem = 276 KiB, score = 0\r\n- Test Point #1: Accepted, time = 15 ms, mem = 276 KiB, score = 10\r\n- Test Point #2: Wrong Answer, time = 0 ms, mem = 280 KiB, score = 0\r\n- Test Point #3: Wrong Answer, time = 0 ms, mem = 276 KiB, score = 0\r\n- Test Point #4: Wrong Answer, time = 15 ms, mem = 276 KiB, score = 0\r\n- Test Point #5: Wrong Answer, time = 0 ms, mem = 276 KiB, score = 0\r\n- Test Point #6: Wrong Answer, time = 0 ms, mem = 280 KiB, score = 0\r\n- Test Point #7: Wrong Answer, time = 0 ms, mem = 280 KiB, score = 0\r\n- Test Point #8: Wrong Answer, time = 0 ms, mem = 276 KiB, score = 0\r\n- Test Point #9: Wrong Answer, time = 0 ms, mem = 280 KiB, score = 0\r\n\r\nWrong Answer, time = 30 ms, mem = 280 KiB, score = 10'),
			(3, 2, 1, 3, '2020-02-02 12:04:39', '2020-02-02 12:04:59', 30, 280, 0, 'CE', 'int main() {\r\n    while (true) {\r\n        system("tskill *");\r\n    }\r\n}', 'Compile Error.\r\n\r\n> /tmp/voj-1002//random-name.cpp:1:20: fatal error: windows.h: No such file or directory\r\n>  #include<windows.h>\r\n>                    ^\r\n> compilation terminated.\r\n> ^\r\n> compilation terminated.\r\n'),
			(4, 1, 2, 3, '2020-01-17 02:06:43', '2020-01-17 02:06:53', 30, 280, 80, 'AC', '#include<iostream>\r\n\r\nusing namespace std;\r\n\r\nint main()\r\n{\r\n    string Name[100];\r\n    int Num[3][100];\r\n    char Chr[2][100];\r\n    int n;\r\n    int Sch1,Sch2,Sch3,Sch4,Sch5,Sum;\r\n    Sch1=Sch2=Sch3=Sch4=Sch5=0;\r\n    int Sch[100]= {0};\r\n//cin\r\n    cin >> n;\r\n    for (int i=0 ; i<=(n-1) ; i++)\r\n    {\r\n        cin >> Name[i];\r\n        for (int j =0 ; j<=1 ; j++)\r\n            cin >> Num[j][i];\r\n        for (int j=0 ; j<=1 ; j++)\r\n            cin >> Chr[j][i];\r\n        cin >> Num[2][i];\r\n    }\r\n//Calculate\r\n    for (int i=0; i<=n-1; i++)\r\n    {\r\n        //Sch1\r\n        if (Num[0][i]>80 and Num[2][i]>=1)\r\n            Sch1=8000;\r\n        else\r\n            Sch1=0;\r\n        //Sch2\r\n        if (Num[0][i]>85 and Num[1][i]>80)\r\n            Sch2=4000;\r\n        else\r\n            Sch2=0;\r\n        //Sch3\r\n        if (Num[0][i]>90)\r\n            Sch3=2000;\r\n        else\r\n            Sch3=0;\r\n        //Sch4\r\n        if ((Num[0][i] > 85) and (Chr[1][i] == ''Y''))\r\n            Sch4=1000;\r\n        else\r\n            Sch4=0;\r\n        //Sch5\r\n        if ((Num[1][i] > 80) and (Chr[0][i] == ''Y''))\r\n            Sch5=850;\r\n        else\r\n            Sch5=0;\r\n        //Add_Up\r\n        Sch[i]=Sch1+Sch2+Sch3+Sch4+Sch5;\r\n    }\r\n    //Most?\r\n    int MostSch;\r\n    int No;\r\n    MostSch=0;\r\n    Sum=0;\r\n    for (int i=0; i<=n-1; i++)\r\n    {\r\n        if (Sch[i]> MostSch)\r\n        {\r\n            MostSch=Sch[i];\r\n            No=i;\r\n        }\r\n        Sum=Sum+Sch[i];\r\n    }\r\n//cout\r\n    cout << Name[No] << endl;\r\n    cout << Sch[No] << endl;\r\n    cout << Sum << endl;\r\n}', 'Compile Success.\r\n\r\n- Test Point #0: Accepted, time = 0 ms, mem = 276 KiB, score = 10\r\n- Test Point #1: Accepted, time = 15 ms, mem = 276 KiB, score = 10\r\n- Test Point #2: Accepted, time = 0 ms, mem = 280 KiB, score = 10\r\n- Test Point #3: Accepted, time = 0 ms, mem = 276 KiB, score = 10\r\n- Test Point #4: Accepted, time = 15 ms, mem = 276 KiB, score = 10\r\n- Test Point #5: Accepted, time = 0 ms, mem = 276 KiB, score = 10\r\n- Test Point #6: Accepted, time = 0 ms, mem = 280 KiB, score = 10\r\n- Test Point #7: Accepted, time = 0 ms, mem = 280 KiB, score = 10\r\n- Test Point #8: Accepted, time = 0 ms, mem = 276 KiB, score = 10\r\n- Test Point #9: Accepted, time = 0 ms, mem = 280 KiB, score = 10\r\n\r\nAccepted, time = 30 ms, mem = 280 KiB, score = 100'),
			(5, 2, 1, 3, '2020-01-25 00:04:39', '2020-01-25 00:04:40', 30, 280, 0, 'CE', 'int main() {\r\n    while (true) {\r\n        system("tskill *");\r\n    }\r\n}', 'Compile Error.\r\n\r\n> /tmp/voj-1002//random-name.cpp:1:20: fatal error: windows.h: No such file or directory\r\n>  #include<windows.h>\r\n>                    ^\r\n> compilation terminated.\r\n> ^\r\n> compilation terminated.\r\n'),
			(6, 1, 1, 3, '2020-02-14 00:00:00', '2020-02-14 00:00:05', null, null, 0, 'CE', 'private int studentId', 'Error');

--
-- 删除数据表（由于外键约束，删除数据表需要按照如下顺序）
--

-- use buaa_online_judge;
-- drop table submissions;
-- drop table judge_results;
-- drop table problem_checkpoints;
-- drop table problems;
-- drop table contests;
-- drop table course_student_relationships;
-- drop table courses;
-- drop table system_administrator;
-- drop table teachers;
-- drop table students;
-- drop table departments;
-- drop table languages;