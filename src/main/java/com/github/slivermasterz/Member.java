package com.github.slivermasterz;

import java.io.Serializable;
import java.time.Instant;
import java.util.Date;

public class Member implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 3850255598684225612L;
    String name;
    long id;
    int count = 0;
    long creationDate = -1;

    /**
     * Init
     * @param name
     * @param id
     */
    public Member(String name, long id) {
        this.name = name;
        this.id = id;
    }

    /**
     * Checks if striked
     * @return Boolean
     */
    public boolean isStriked() {
        return false;
    }

    /**
     *
     * @return int count
     */
    public int count() {
        return count;
    }

    /**
     * Increase count
     */
    public void increase() {
        count++;
    }

    /**
     * Get id
     * @return Long id
     */
    public long getId() {
        return id;
    }

    /**
     * Set id
     * @param id
     */
    public void setId(long id) {
        this.id = id;
    }

    public void setCreationDate(long creationDate) {
        this.creationDate = creationDate;
    }

    public long age() {
        return Instant.now().toEpochMilli() - creationDate;
    }
}