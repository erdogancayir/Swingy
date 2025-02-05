package com.avaj.dbConnectionTest;

import com.avaj.database.DbInterface;
import com.avaj.database.HeroManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.mockito.Mockito.*;

public class H2DbTest
{
    private HeroManager heroManager;
    private DbInterface mockDbInterface;

    @BeforeEach
    void setUp() {
    }

    @Test
    void testCreateHeroesTable() {
    }

    @Test
    void testInsertHero() {
    }

    @Test
    void testPrintAllHeroes() throws SQLException {
    }
}
