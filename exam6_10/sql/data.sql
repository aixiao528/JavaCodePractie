USE course_selection;

INSERT INTO students (id, name, major) VALUES
(1001, '张三', '软件工程'),
(1002, '李四', '计算机科学'),
(1003, '王五', '数据科学'),
(1004, '赵六', '网络工程'),
(1005, '孙七', '人工智能'),
(1006, '周八', '信息安全');

INSERT INTO courses (id, name, credits, capacity, selected_count) VALUES
(2001, 'Java程序设计', 4, 3, 2),
(2002, '数据库原理', 3, 4, 1),
(2003, '操作系统', 4, 2, 1),
(2004, '计算机网络', 3, 2, 0);

INSERT INTO enrollments (student_id, course_id, grade) VALUES
(1001, 2001, 88.00),
(1002, 2001, 91.00),
(1003, 2002, 85.50),
(1004, 2003, NULL);
