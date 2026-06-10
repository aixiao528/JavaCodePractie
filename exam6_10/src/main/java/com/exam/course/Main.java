package com.exam.course;

import com.exam.course.cache.CourseCache;
import com.exam.course.dao.CourseDao;
import com.exam.course.dao.EnrollmentDao;
import com.exam.course.dao.StudentDao;
import com.exam.course.exception.BusinessException;
import com.exam.course.model.Course;
import com.exam.course.model.EnrollmentDetail;
import com.exam.course.service.QueryService;
import com.exam.course.service.SelectionService;

import java.sql.SQLException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static final DateTimeFormatter TIME_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withZone(ZoneId.systemDefault());

    public static void main(String[] args) {
        StudentDao studentDao = new StudentDao();
        CourseDao courseDao = new CourseDao();
        EnrollmentDao enrollmentDao = new EnrollmentDao();
        CourseCache courseCache = new CourseCache(courseDao);
        SelectionService selectionService = new SelectionService(studentDao, courseDao, enrollmentDao, courseCache);
        QueryService queryService = new QueryService(studentDao, enrollmentDao);

        try (Scanner scanner = new Scanner(System.in)) {
            boolean running = true;
            while (running) {
                printMenu(courseCache);
                System.out.print("请输入菜单编号: ");
                String option = scanner.nextLine().trim();
                try {
                    switch (option) {
                        case "1" -> listCourses(courseCache);
                        case "2" -> doSelect(scanner, selectionService);
                        case "3" -> doDrop(scanner, selectionService);
                        case "4" -> doTransfer(scanner, selectionService, false);
                        case "5" -> doBatchSelect(scanner, selectionService);
                        case "6" -> doQuery(scanner, queryService);
                        case "7" -> doTransfer(scanner, selectionService, true);
                        case "8" -> refreshCache(courseCache);
                        case "0" -> running = false;
                        default -> System.out.println("无效菜单编号，请重新输入。");
                    }
                } catch (BusinessException exception) {
                    System.out.println("业务提示: " + exception.getMessage());
                } catch (SQLException exception) {
                    System.out.println("数据库操作失败: " + exception.getMessage());
                } catch (NumberFormatException exception) {
                    System.out.println("输入格式错误，请输入数字编号。");
                }
                if (running) {
                    System.out.println();
                }
            }
        }
        System.out.println("系统已退出。");
    }

    private static void printMenu(CourseCache courseCache) {
        System.out.println("======================================");
        System.out.println(" 基于 MySQL 的学生在线选课系统 ");
        System.out.println("======================================");
        if (courseCache.getLastRefreshTime() > 0) {
            System.out.println("课程缓存时间: " + TIME_FORMATTER.format(Instant.ofEpochMilli(courseCache.getLastRefreshTime())));
        } else {
            System.out.println("课程缓存时间: 尚未加载");
        }
        System.out.println("1. 查看课程列表（缓存）");
        System.out.println("2. 学生单条选课");
        System.out.println("3. 学生退课");
        System.out.println("4. 学生转课给其他同学（正常事务）");
        System.out.println("5. 教务批量选课");
        System.out.println("6. 查询学生全部已选课程");
        System.out.println("7. 转课给其他同学回滚演示（模拟异常）");
        System.out.println("8. 手动刷新课程缓存");
        System.out.println("0. 退出系统");
    }

    private static void listCourses(CourseCache courseCache) throws SQLException {
        List<Course> courses = courseCache.getCourses();
        System.out.println("课程列表:");
        System.out.printf("%-8s %-16s %-8s %-8s %-8s%n", "课程ID", "课程名", "学分", "容量", "已选");
        for (Course course : courses) {
            System.out.printf(
                    "%-8d %-16s %-8d %-8d %-8d%n",
                    course.getId(),
                    course.getName(),
                    course.getCredits(),
                    course.getCapacity(),
                    course.getSelectedCount()
            );
        }
    }

    private static void doSelect(Scanner scanner, SelectionService selectionService) throws SQLException, BusinessException {
        int studentId = readInt(scanner, "请输入学生 ID: ");
        int courseId = readInt(scanner, "请输入课程 ID: ");
        selectionService.selectCourse(studentId, courseId);
        System.out.println("选课成功。");
    }

    private static void doDrop(Scanner scanner, SelectionService selectionService) throws SQLException, BusinessException {
        int studentId = readInt(scanner, "请输入学生 ID: ");
        int courseId = readInt(scanner, "请输入课程 ID: ");
        selectionService.dropCourse(studentId, courseId);
        System.out.println("退课成功。");
    }

    private static void doTransfer(Scanner scanner, SelectionService selectionService, boolean simulateFailure)
            throws SQLException, BusinessException {
        int fromStudentId = readInt(scanner, "请输入转出学生 ID: ");
        int toStudentId = readInt(scanner, "请输入接收学生 ID: ");
        int courseId = readInt(scanner, "请输入要转让的课程 ID: ");
        selectionService.transferCourse(fromStudentId, toStudentId, courseId, simulateFailure);
        if (!simulateFailure) {
            System.out.println("转课成功。");
        }
    }

    private static void doBatchSelect(Scanner scanner, SelectionService selectionService)
            throws SQLException, BusinessException {
        int courseId = readInt(scanner, "请输入课程 ID: ");
        System.out.print("请输入学生 ID 列表（英文逗号分隔）: ");
        String raw = scanner.nextLine().trim();
        List<Integer> studentIds = parseStudentIds(raw);
        selectionService.batchSelectCourse(courseId, studentIds);
        System.out.println("批量选课成功。");
    }

    private static void doQuery(Scanner scanner, QueryService queryService) throws SQLException, BusinessException {
        int studentId = readInt(scanner, "请输入学生 ID: ");
        List<EnrollmentDetail> details = queryService.queryStudentCourses(studentId);
        if (details.isEmpty()) {
            System.out.println("该学生暂无已选课程。");
            return;
        }
        System.out.printf("%-8s %-16s %-8s %-12s%n", "课程ID", "课程名", "学分", "成绩");
        for (EnrollmentDetail detail : details) {
            System.out.printf(
                    "%-8d %-16s %-8d %-12s%n",
                    detail.getCourseId(),
                    detail.getCourseName(),
                    detail.getCredits(),
                    detail.getGrade()
            );
        }
    }

    private static void refreshCache(CourseCache courseCache) throws SQLException {
        courseCache.refresh();
        System.out.println("课程缓存刷新成功。");
    }

    private static int readInt(Scanner scanner, String prompt) {
        System.out.print(prompt);
        return Integer.parseInt(scanner.nextLine().trim());
    }

    private static List<Integer> parseStudentIds(String raw) throws BusinessException {
        if (raw.isBlank()) {
            throw new BusinessException("学生 ID 列表不能为空。");
        }
        String[] parts = raw.split(",");
        List<Integer> studentIds = new ArrayList<>();
        for (String part : parts) {
            String value = part.trim();
            if (value.isEmpty()) {
                continue;
            }
            studentIds.add(Integer.parseInt(value));
        }
        if (studentIds.isEmpty()) {
            throw new BusinessException("学生 ID 列表不能为空。");
        }
        return studentIds;
    }
}
