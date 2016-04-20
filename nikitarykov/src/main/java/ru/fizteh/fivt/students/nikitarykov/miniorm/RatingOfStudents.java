package ru.fizteh.fivt.students.nikitarykov.miniorm;

import ru.fizteh.fivt.students.nikitarykov.miniorm.annotations.Column;
import ru.fizteh.fivt.students.nikitarykov.miniorm.annotations.PrimaryKey;
import ru.fizteh.fivt.students.nikitarykov.miniorm.annotations.Table;

import java.sql.Date;

/**
 * Created by Nikita Rykov on 19.12.2015.
 */
@Table
public class RatingOfStudents {
    @Column
    @PrimaryKey
    private String name;

    @Column
    private Integer rank;

    @Column(name = "birthday")
    private Date dateOfBirth;

    RatingOfStudents() {
    }

    RatingOfStudents(String name, Integer rank, Date dateOfBirth) {
        this.name = name;
        this.rank = rank;
        this.dateOfBirth = dateOfBirth;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRank(Integer rank) {
        this.rank = rank;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getName() {
        return name;
    }

    public Integer getRank() {
        return rank;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }
}
