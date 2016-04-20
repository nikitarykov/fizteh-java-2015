package ru.fizteh.fivt.students.nikitarykov.collectionquery;

import ru.fizteh.fivt.students.nikitarykov.collectionquery.impl.Tuple;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

import static ru.fizteh.fivt.students.nikitarykov.collectionquery.Aggregates.avg;
import static ru.fizteh.fivt.students.nikitarykov.collectionquery.Aggregates.count;
import static ru.fizteh.fivt.students.nikitarykov.collectionquery.CollectionQuery.Student.student;
import static ru.fizteh.fivt.students.nikitarykov.collectionquery.Conditions.rlike;
import static ru.fizteh.fivt.students.nikitarykov.collectionquery.OrderByConditions.asc;
import static ru.fizteh.fivt.students.nikitarykov.collectionquery.OrderByConditions.desc;
import static ru.fizteh.fivt.students.nikitarykov.collectionquery.Sources.list;
import static ru.fizteh.fivt.students.nikitarykov.collectionquery.impl.FromStmt.from;

/**
 * @author akormushin
 */
public class CollectionQuery {

    /**
     * Make this code work!
     *
     * @param args
     */
    public static void main(String[] args) {
        try {
            Iterable<Statistics> statistics =
                    from(list(
                            student("ivanov", LocalDate.parse("1986-08-06"), "494"),
                            student("sidorov", LocalDate.parse("1986-08-06"), "495"),
                            student("smith", LocalDate.parse("1986-08-06"), "495"),
                            student("petrov", LocalDate.parse("2006-08-06"), "494")))
                            .select(Statistics.class, Student::getGroup, count(Student::getGroup), avg(Student::age))
                            .where(rlike(Student::getName, ".*ov").and(s -> s.age() > 20))
                            .groupBy(Student::getGroup)
                            .having(s -> s.getCount() > 0)
                            .orderBy(asc(Statistics::getGroup), desc(Statistics::getCount))
                            .limit(100)
                            .union()
                            .from(list(student("ivanov", LocalDate.parse("1985-08-06"), "494")))
                            .selectDistinct(Statistics.class, s -> "all", count(s -> 1), avg(Student::age))
                            .execute();
            System.out.println(statistics);

            Iterable<Tuple<String, String>> mentorsByStudent =
                    from(list(student("ivanov", LocalDate.parse("1985-08-06"), "494")))
                            .join(list(new Group("494", "mr.sidorov")))
                            .on((s, g) -> Objects.equals(s.getGroup(), g.getGroup()))
                            .select(sg -> sg.getFirst().getName(), sg -> sg.getSecond().getMentor())
                            .execute();
            System.out.println(mentorsByStudent);
        } catch (Exception exception) {
            System.err.println("Incorrect query");
        }
    }


    public static class Student {
        private final String name;

        private final LocalDate dateOfBirth;

        private final String group;

        public String getName() {
            return name;
        }

        public Student(String name, LocalDate dateOfBirth, String group) {
            this.name = name;
            this.dateOfBirth = dateOfBirth;
            this.group = group;
        }

        public LocalDate getDateOfBirth() {
            return dateOfBirth;
        }

        public String getGroup() {
            return group;
        }

        public Double age() {
            return (double) ChronoUnit.YEARS.between(getDateOfBirth(), LocalDateTime.now());
        }

        public static Student student(String name, LocalDate dateOfBirth, String group) {
            return new Student(name, dateOfBirth, group);
        }

        @Override
        public boolean equals(Object other) {
            if (other == null) {
                return false;
            } else if (other == this) {
                return true;
            } else if (!(other instanceof Student)) {
                return false;
            }
            Student student = (Student) other;
            if (!group.equals(student.getGroup())) {
                return false;
            } else if (!name.equals(student.getName())) {
                return false;
            } else if (!dateOfBirth.equals(student.getDateOfBirth())) {
                return false;
            }
            return true;
        }

        @Override
        public int hashCode() {
            int result = 0;
            if (group != null) {
                result = 89 * group.hashCode();
            }
            if (name != null) {
                result = 89 * (result + name.hashCode());
            } else {
                result = 89 * result;
            }
            if (dateOfBirth != null) {
                result = 89 * (result + dateOfBirth.hashCode());
            } else {
                result = 89 * result;
            }
            return result;
        }

        @Override
        public String toString() {
            return "Student{"
                    + "name=" + name
                    + ", group=" + group
                    + ", age=" + age()
                    + '}';
        }
    }

    public static class Group {
        private final String group;
        private final String mentor;

        public Group(String group, String mentor) {
            this.group = group;
            this.mentor = mentor;
        }

        public String getGroup() {
            return group;
        }

        public String getMentor() {
            return mentor;
        }

        @Override
        public boolean equals(Object other) {
            if (other == null) {
                return false;
            } else if (other == this) {
                return true;
            } else if (!(other instanceof Group)) {
                return false;
            }
            Group otherGroup = (Group) other;
            if (!group.equals(otherGroup.getGroup())) {
                return false;
            } else if (!mentor.equals(otherGroup.getMentor())) {
                return false;
            }
            return true;
        }

        @Override
        public int hashCode() {
            int result = 0;
            if (group != null) {
                result = 89 * group.hashCode();
            }
            if (mentor != null) {
                result = 89 * (result + mentor.hashCode());
            } else {
                result = 89 * result;
            }
            return result;
        }

        @Override
        public String toString() {
            return "Group{"
                    + "group=" + group
                    + ", mentor=" + mentor
                    + '}';
        }
    }


    public static class Statistics {

        private final String group;
        private final Long count;
        private final Double age;

        public String getGroup() {
            return group;
        }

        public Long getCount() {
            return count;
        }

        public Double getAge() {
            return age;
        }

        public Statistics(String group, Long count, Double age) {
            this.group = group;
            this.count = count;
            this.age = age;
        }

        @Override
        public boolean equals(Object other) {
            if (other == null) {
                return false;
            } else if (other == this) {
                return true;
            } else if (!(other instanceof Statistics)) {
                return false;
            }
            Statistics statistics = (Statistics) other;
            if (!group.equals(statistics.getGroup())) {
                return false;
            } else if (!count.equals(statistics.getCount())) {
                return false;
            } else if (!age.equals(statistics.getAge())) {
                return false;
            }
            return true;
        }

        @Override
        public int hashCode() {
            int result = 0;
            if (group != null) {
                result = 89 * group.hashCode();
            }
            if (count != null) {
                result = 89 * (result + count.hashCode());
            } else {
                result = 89 * result;
            }
            if (age != null) {
                result = 89 * (result + age.hashCode());
            } else {
                result = 89 * result;
            }
            return result;
        }

        @Override
        public String toString() {
            return "Statistics{"
                    + "group='" + group + '\''
                    + ", count=" + count
                    + ", age=" + age
                    + '}';
        }
    }

}
