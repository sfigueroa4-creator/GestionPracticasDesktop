package com.practicas.util;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseInstaller {

    private final Connection conn;

    public DatabaseInstaller(Connection conn) {
        this.conn = conn;
    }

    public void crearTablas() throws SQLException {
        Statement st = conn.createStatement();

        // ─── SECUENCIAS (reemplazo de IDENTITY para Oracle 10g) ──────────────

        st.executeUpdate("CREATE SEQUENCE SEQ_USUARIO         START WITH 1 INCREMENT BY 1 NOCACHE NOCYCLE");
        st.executeUpdate("CREATE SEQUENCE SEQ_INSTITUCION     START WITH 1 INCREMENT BY 1 NOCACHE NOCYCLE");
        st.executeUpdate("CREATE SEQUENCE SEQ_PRACTICA        START WITH 1 INCREMENT BY 1 NOCACHE NOCYCLE");
        st.executeUpdate("CREATE SEQUENCE SEQ_GRUPO           START WITH 1 INCREMENT BY 1 NOCACHE NOCYCLE");
        st.executeUpdate("CREATE SEQUENCE SEQ_INSCRIPCION     START WITH 1 INCREMENT BY 1 NOCACHE NOCYCLE");
        st.executeUpdate("CREATE SEQUENCE SEQ_AUDITORIA       START WITH 1 INCREMENT BY 1 NOCACHE NOCYCLE");

        // ─── TABLAS ──────────────────────────────────────────────────────────

        st.executeUpdate(
            "CREATE TABLE USUARIO (" +
            "    ID_USUARIO     NUMBER NOT NULL," +
            "    NOMBRE         VARCHAR2(100) NOT NULL," +
            "    APELLIDO       VARCHAR2(100) NOT NULL," +
            "    EMAIL          VARCHAR2(150) NOT NULL," +
            "    PASSWORD_HASH  VARCHAR2(255) NOT NULL," +
            "    ROL            VARCHAR2(30)  NOT NULL," +
            "    ACTIVO         NUMBER(1) DEFAULT 1," +
            "    TELEFONO       VARCHAR2(30)," +
            "    FECHA_CREACION TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
            "    CONSTRAINT PK_USUARIO       PRIMARY KEY (ID_USUARIO)," +
            "    CONSTRAINT UK_USUARIO_EMAIL UNIQUE (EMAIL)" +
            ")"
        );

        st.executeUpdate(
            "CREATE TABLE INSTITUCION_RECEPTORA (" +
            "    ID_INSTITUCION  NUMBER NOT NULL," +
            "    NOMBRE          VARCHAR2(150) NOT NULL," +
            "    NIT             VARCHAR2(50)  NOT NULL," +
            "    DIRECCION       VARCHAR2(200)," +
            "    MUNICIPIO       VARCHAR2(100)," +
            "    DEPARTAMENTO    VARCHAR2(100)," +
            "    TELEFONO        VARCHAR2(30)," +
            "    EMAIL_CONTACTO  VARCHAR2(150)," +
            "    CONVENIO_ACTIVO NUMBER(1) DEFAULT 1," +
            "    FECHA_CONVENIO  DATE," +
            "    CONSTRAINT PK_INSTITUCION     PRIMARY KEY (ID_INSTITUCION)," +
            "    CONSTRAINT UK_INSTITUCION_NIT UNIQUE (NIT)" +
            ")"
        );

        st.executeUpdate(
            "CREATE TABLE PRACTICA (" +
            "    ID_PRACTICA            NUMBER NOT NULL," +
            "    NOMBRE                 VARCHAR2(150) NOT NULL," +
            "    DESCRIPCION            VARCHAR2(500)," +
            "    NUMERO_PRACTICA        NUMBER(2) NOT NULL," +
            "    HORAS_REGLAMENTARIAS   NUMBER(5)," +
            "    FECHA_INICIO           DATE," +
            "    FECHA_FIN              DATE," +
            "    ESTADO                 VARCHAR2(30)," +
            "    TIPO_PRACTICA          VARCHAR2(100)," +
            "    FECHA_CREACION         TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
            "    CONSTRAINT PK_PRACTICA PRIMARY KEY (ID_PRACTICA)" +
            ")"
        );

        st.executeUpdate(
            "CREATE TABLE GRUPO_PRACTICA (" +
            "    ID_GRUPO      NUMBER NOT NULL," +
            "    NOMBRE        VARCHAR2(100) NOT NULL," +
            "    ID_PRACTICA   NUMBER NOT NULL," +
            "    ID_DOCENTE    NUMBER NOT NULL," +
            "    ID_INSTITUCION NUMBER NOT NULL," +
            "    CUPO_MAXIMO   NUMBER(5)," +
            "    OBSERVACIONES VARCHAR2(500)," +
            "    CONSTRAINT PK_GRUPO            PRIMARY KEY (ID_GRUPO)," +
            "    CONSTRAINT FK_GRUPO_PRACTICA   FOREIGN KEY (ID_PRACTICA)    REFERENCES PRACTICA(ID_PRACTICA)," +
            "    CONSTRAINT FK_GRUPO_DOCENTE    FOREIGN KEY (ID_DOCENTE)     REFERENCES USUARIO(ID_USUARIO)," +
            "    CONSTRAINT FK_GRUPO_INSTITUCION FOREIGN KEY (ID_INSTITUCION) REFERENCES INSTITUCION_RECEPTORA(ID_INSTITUCION)" +
            ")"
        );

        st.executeUpdate(
            "CREATE TABLE INSCRIPCION_GRUPO (" +
            "    ID_INSCRIPCION    NUMBER NOT NULL," +
            "    ID_ESTUDIANTE     NUMBER NOT NULL," +
            "    ID_GRUPO          NUMBER NOT NULL," +
            "    HORAS_CUMPLIDAS   NUMBER(8,2) DEFAULT 0," +
            "    ESTADO            VARCHAR2(30)," +
            "    FECHA_INSCRIPCION TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
            "    OBSERVACION_FINAL VARCHAR2(500)," +
            "    CONSTRAINT PK_INSCRIPCION_GRUPO    PRIMARY KEY (ID_INSCRIPCION)," +
            "    CONSTRAINT FK_INSCRIPCION_ESTUDIANTE FOREIGN KEY (ID_ESTUDIANTE) REFERENCES USUARIO(ID_USUARIO)," +
            "    CONSTRAINT FK_INSCRIPCION_GRUPO      FOREIGN KEY (ID_GRUPO)      REFERENCES GRUPO_PRACTICA(ID_GRUPO)" +
            ")"
        );

        st.executeUpdate(
            "CREATE TABLE AUDITORIA_PASSWORD (" +
            "    ID_AUDITORIA NUMBER NOT NULL," +
            "    ID_USUARIO   NUMBER NOT NULL," +
            "    FECHA_CAMBIO TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
            "    CONSTRAINT PK_AUDITORIA        PRIMARY KEY (ID_AUDITORIA)," +
            "    CONSTRAINT FK_AUDITORIA_USUARIO FOREIGN KEY (ID_USUARIO) REFERENCES USUARIO(ID_USUARIO)" +
            ")"
        );

        // ─── TRIGGERS DE AUTOINCREMENTO (reemplazo de IDENTITY) ─────────────

        st.execute(
            "CREATE OR REPLACE TRIGGER TRG_AI_USUARIO " +
            "BEFORE INSERT ON USUARIO FOR EACH ROW " +
            "BEGIN " +
            "    IF :NEW.ID_USUARIO IS NULL THEN " +
            "        SELECT SEQ_USUARIO.NEXTVAL INTO :NEW.ID_USUARIO FROM DUAL; " +
            "    END IF; " +
            "END;"
        );

        st.execute(
            "CREATE OR REPLACE TRIGGER TRG_AI_INSTITUCION " +
            "BEFORE INSERT ON INSTITUCION_RECEPTORA FOR EACH ROW " +
            "BEGIN " +
            "    IF :NEW.ID_INSTITUCION IS NULL THEN " +
            "        SELECT SEQ_INSTITUCION.NEXTVAL INTO :NEW.ID_INSTITUCION FROM DUAL; " +
            "    END IF; " +
            "END;"
        );

        st.execute(
            "CREATE OR REPLACE TRIGGER TRG_AI_PRACTICA " +
            "BEFORE INSERT ON PRACTICA FOR EACH ROW " +
            "BEGIN " +
            "    IF :NEW.ID_PRACTICA IS NULL THEN " +
            "        SELECT SEQ_PRACTICA.NEXTVAL INTO :NEW.ID_PRACTICA FROM DUAL; " +
            "    END IF; " +
            "END;"
        );

        st.execute(
            "CREATE OR REPLACE TRIGGER TRG_AI_GRUPO " +
            "BEFORE INSERT ON GRUPO_PRACTICA FOR EACH ROW " +
            "BEGIN " +
            "    IF :NEW.ID_GRUPO IS NULL THEN " +
            "        SELECT SEQ_GRUPO.NEXTVAL INTO :NEW.ID_GRUPO FROM DUAL; " +
            "    END IF; " +
            "END;"
        );

        st.execute(
            "CREATE OR REPLACE TRIGGER TRG_AI_INSCRIPCION " +
            "BEFORE INSERT ON INSCRIPCION_GRUPO FOR EACH ROW " +
            "BEGIN " +
            "    IF :NEW.ID_INSCRIPCION IS NULL THEN " +
            "        SELECT SEQ_INSCRIPCION.NEXTVAL INTO :NEW.ID_INSCRIPCION FROM DUAL; " +
            "    END IF; " +
            "END;"
        );

        st.execute(
            "CREATE OR REPLACE TRIGGER TRG_AI_AUDITORIA " +
            "BEFORE INSERT ON AUDITORIA_PASSWORD FOR EACH ROW " +
            "BEGIN " +
            "    IF :NEW.ID_AUDITORIA IS NULL THEN " +
            "        SELECT SEQ_AUDITORIA.NEXTVAL INTO :NEW.ID_AUDITORIA FROM DUAL; " +
            "    END IF; " +
            "END;"
        );

        // ─── USUARIO ADMINISTRADOR POR DEFECTO ───────────────────────────────

        st.executeUpdate(
            "INSERT INTO USUARIO (NOMBRE, APELLIDO, EMAIL, PASSWORD_HASH, ROL, ACTIVO) " +
            "VALUES ('GestionP', 'Admin', 'GestionP', 'GestionP', 'ADMIN', 1)"
        );

        // ─── USUARIOS ORACLE POR ROL ─────────────────────────────────────────
        // Cada rol de la aplicacion tiene su propio usuario Oracle con privilegios
        // restringidos segun sus funciones. Las contrasenas son internas al sistema.

        crearUsuariosOracle(st);

        // ─── ROLES Y PRIVILEGIOS ─────────────────────────────────────────────

        crearRolesYPrivilegios(st);

        // ─── PROCEDIMIENTOS ──────────────────────────────────────────────────

        crearProcedimientos(st);

        // ─── FUNCIONES ───────────────────────────────────────────────────────

        crearFunciones(st);

        // ─── DISPARADORES DE NEGOCIO ─────────────────────────────────────────

        crearDisparadores(st);

        st.close();
    }

    private void crearUsuariosOracle(Statement st) throws SQLException {
        // Crear usuarios Oracle uno por rol y asignarles el rol correspondiente.
        // La contrasena de cada usuario Oracle es interna al sistema.
        String[][] usuarios = {
            {"USR_ADMIN",       "Adm1nGP#",    "ROL_ADMIN"},
            {"USR_DIRECTOR",    "Dir3ctGP#",   "ROL_DIRECTOR"},
            {"USR_COORDINADOR", "Coord3GP#",   "ROL_COORDINADOR"},
            {"USR_DOCENTE",     "Doc3ntGP#",   "ROL_DOCENTE"},
            {"USR_ESTUDIANTE",  "Est4dGP#",    "ROL_ESTUDIANTE"},
            {"USR_INSTITUCION", "Inst1tGP#",   "ROL_INSTITUCION"}
        };

        for (String[] u : usuarios) {
            try {
                st.executeUpdate("CREATE USER " + u[0] + " IDENTIFIED BY \"" + u[1] + "\"");
            } catch (Exception e) { /* ya existe */ }
            try {
                st.executeUpdate("GRANT CREATE SESSION TO " + u[0]);
            } catch (Exception e) {}
            try {
                st.executeUpdate("GRANT " + u[2] + " TO " + u[0]);
            } catch (Exception e) {}
        }
    }

    private void crearRolesYPrivilegios(Statement st) throws SQLException {
        String[] roles = {
            "ROL_ADMIN", "ROL_DIRECTOR", "ROL_COORDINADOR",
            "ROL_DOCENTE", "ROL_ESTUDIANTE", "ROL_INSTITUCION"
        };
        for (String rol : roles) {
            try { st.executeUpdate("CREATE ROLE " + rol); } catch (Exception e) {}
        }

        try { st.executeUpdate("GRANT SELECT, INSERT, UPDATE, DELETE ON USUARIO TO ROL_ADMIN"); } catch (Exception e) {}
        try { st.executeUpdate("GRANT SELECT, INSERT, UPDATE, DELETE ON PRACTICA TO ROL_ADMIN"); } catch (Exception e) {}
        try { st.executeUpdate("GRANT SELECT, INSERT, UPDATE, DELETE ON INSTITUCION_RECEPTORA TO ROL_ADMIN"); } catch (Exception e) {}
        try { st.executeUpdate("GRANT SELECT, INSERT, UPDATE, DELETE ON GRUPO_PRACTICA TO ROL_ADMIN"); } catch (Exception e) {}
        try { st.executeUpdate("GRANT SELECT, INSERT, UPDATE, DELETE ON INSCRIPCION_GRUPO TO ROL_ADMIN"); } catch (Exception e) {}
        try { st.executeUpdate("GRANT SELECT ON AUDITORIA_PASSWORD TO ROL_ADMIN"); } catch (Exception e) {}

        try { st.executeUpdate("GRANT SELECT, INSERT, UPDATE, DELETE ON PRACTICA TO ROL_DIRECTOR"); } catch (Exception e) {}
        try { st.executeUpdate("GRANT SELECT, INSERT, UPDATE, DELETE ON INSTITUCION_RECEPTORA TO ROL_DIRECTOR"); } catch (Exception e) {}
        try { st.executeUpdate("GRANT SELECT, INSERT, UPDATE, DELETE ON GRUPO_PRACTICA TO ROL_DIRECTOR"); } catch (Exception e) {}
        try { st.executeUpdate("GRANT SELECT, INSERT, UPDATE, DELETE ON INSCRIPCION_GRUPO TO ROL_DIRECTOR"); } catch (Exception e) {}
        try { st.executeUpdate("GRANT SELECT ON USUARIO TO ROL_DIRECTOR"); } catch (Exception e) {}

        try { st.executeUpdate("GRANT SELECT ON PRACTICA TO ROL_COORDINADOR"); } catch (Exception e) {}
        try { st.executeUpdate("GRANT SELECT ON INSTITUCION_RECEPTORA TO ROL_COORDINADOR"); } catch (Exception e) {}
        try { st.executeUpdate("GRANT SELECT, INSERT, UPDATE, DELETE ON GRUPO_PRACTICA TO ROL_COORDINADOR"); } catch (Exception e) {}
        try { st.executeUpdate("GRANT SELECT, INSERT, UPDATE, DELETE ON INSCRIPCION_GRUPO TO ROL_COORDINADOR"); } catch (Exception e) {}
        try { st.executeUpdate("GRANT SELECT ON USUARIO TO ROL_COORDINADOR"); } catch (Exception e) {}

        try { st.executeUpdate("GRANT SELECT ON GRUPO_PRACTICA TO ROL_DOCENTE"); } catch (Exception e) {}
        try { st.executeUpdate("GRANT SELECT, INSERT, UPDATE ON INSCRIPCION_GRUPO TO ROL_DOCENTE"); } catch (Exception e) {}
        try { st.executeUpdate("GRANT SELECT ON USUARIO TO ROL_DOCENTE"); } catch (Exception e) {}

        try { st.executeUpdate("GRANT SELECT ON INSCRIPCION_GRUPO TO ROL_ESTUDIANTE"); } catch (Exception e) {}
        try { st.executeUpdate("GRANT SELECT ON INSCRIPCION_GRUPO TO ROL_INSTITUCION"); } catch (Exception e) {}
    }

    private void crearProcedimientos(Statement st) throws SQLException {
        st.execute(
            "CREATE OR REPLACE PROCEDURE SP_INSCRIBIR_ESTUDIANTE(" +
            "    p_id_estudiante IN NUMBER," +
            "    p_id_grupo      IN NUMBER," +
            "    p_horas         IN NUMBER," +
            "    p_estado        IN VARCHAR2" +
            ") AS" +
            "    v_cupo_maximo NUMBER;" +
            "    v_inscritos   NUMBER;" +
            "BEGIN" +
            "    SELECT CUPO_MAXIMO INTO v_cupo_maximo" +
            "    FROM GRUPO_PRACTICA WHERE ID_GRUPO = p_id_grupo;" +
            "    SELECT COUNT(*) INTO v_inscritos" +
            "    FROM INSCRIPCION_GRUPO" +
            "    WHERE ID_GRUPO = p_id_grupo" +
            "      AND ESTADO NOT IN ('RETIRADO', 'REPROBADO');" +
            "    IF v_inscritos >= v_cupo_maximo THEN" +
            "        RAISE_APPLICATION_ERROR(-20001," +
            "            'El grupo ha alcanzado su cupo maximo de ' ||" +
            "            v_cupo_maximo || ' estudiantes.');" +
            "    END IF;" +
            "    INSERT INTO INSCRIPCION_GRUPO" +
            "        (ID_ESTUDIANTE, ID_GRUPO, HORAS_CUMPLIDAS, ESTADO, FECHA_INSCRIPCION)" +
            "    VALUES" +
            "        (p_id_estudiante, p_id_grupo, p_horas, p_estado, CURRENT_TIMESTAMP);" +
            "    COMMIT;" +
            "EXCEPTION" +
            "    WHEN NO_DATA_FOUND THEN" +
            "        RAISE_APPLICATION_ERROR(-20002, 'Grupo no encontrado.');" +
            "END SP_INSCRIBIR_ESTUDIANTE;"
        );

        st.execute(
            "CREATE OR REPLACE PROCEDURE SP_ACTUALIZAR_HORAS(" +
            "    p_id_inscripcion IN NUMBER," +
            "    p_horas          IN NUMBER," +
            "    p_estado         IN VARCHAR2," +
            "    p_observacion    IN VARCHAR2" +
            ") AS" +
            "BEGIN" +
            "    UPDATE INSCRIPCION_GRUPO" +
            "    SET HORAS_CUMPLIDAS   = p_horas," +
            "        ESTADO            = p_estado," +
            "        OBSERVACION_FINAL = p_observacion" +
            "    WHERE ID_INSCRIPCION = p_id_inscripcion;" +
            "    IF SQL%ROWCOUNT = 0 THEN" +
            "        RAISE_APPLICATION_ERROR(-20003, 'Inscripcion no encontrada.');" +
            "    END IF;" +
            "    COMMIT;" +
            "END SP_ACTUALIZAR_HORAS;"
        );
    }

    private void crearFunciones(Statement st) throws SQLException {
        st.execute(
            "CREATE OR REPLACE FUNCTION FN_PORCENTAJE_HORAS(" +
            "    p_id_inscripcion IN NUMBER" +
            ") RETURN NUMBER AS" +
            "    v_horas_cumplidas      NUMBER;" +
            "    v_horas_reglamentarias NUMBER;" +
            "    v_porcentaje           NUMBER;" +
            "BEGIN" +
            "    SELECT ig.HORAS_CUMPLIDAS, p.HORAS_REGLAMENTARIAS" +
            "    INTO v_horas_cumplidas, v_horas_reglamentarias" +
            "    FROM INSCRIPCION_GRUPO ig" +
            "    JOIN GRUPO_PRACTICA gp ON ig.ID_GRUPO = gp.ID_GRUPO" +
            "    JOIN PRACTICA p ON gp.ID_PRACTICA = p.ID_PRACTICA" +
            "    WHERE ig.ID_INSCRIPCION = p_id_inscripcion;" +
            "    IF v_horas_reglamentarias = 0 THEN" +
            "        RETURN 0;" +
            "    END IF;" +
            "    v_porcentaje := ROUND((v_horas_cumplidas / v_horas_reglamentarias) * 100, 2);" +
            "    RETURN LEAST(v_porcentaje, 100);" +
            "EXCEPTION" +
            "    WHEN NO_DATA_FOUND THEN" +
            "        RETURN 0;" +
            "END FN_PORCENTAJE_HORAS;"
        );

        st.execute(
            "CREATE OR REPLACE FUNCTION FN_INSCRITOS_ACTIVOS(" +
            "    p_id_grupo IN NUMBER" +
            ") RETURN NUMBER AS" +
            "    v_total NUMBER;" +
            "BEGIN" +
            "    SELECT COUNT(*) INTO v_total" +
            "    FROM INSCRIPCION_GRUPO" +
            "    WHERE ID_GRUPO = p_id_grupo" +
            "      AND ESTADO NOT IN ('RETIRADO', 'REPROBADO');" +
            "    RETURN v_total;" +
            "END FN_INSCRITOS_ACTIVOS;"
        );
    }

    private void crearDisparadores(Statement st) throws SQLException {
        st.execute(
            "CREATE OR REPLACE TRIGGER TRG_AUDITORIA_PASSWORD " +
            "AFTER UPDATE OF PASSWORD_HASH ON USUARIO " +
            "FOR EACH ROW " +
            "BEGIN " +
            "    IF :OLD.PASSWORD_HASH != :NEW.PASSWORD_HASH THEN " +
            "        INSERT INTO AUDITORIA_PASSWORD (ID_USUARIO, FECHA_CAMBIO) " +
            "        VALUES (:NEW.ID_USUARIO, CURRENT_TIMESTAMP); " +
            "    END IF; " +
            "END TRG_AUDITORIA_PASSWORD;"
        );

        st.execute(
            "CREATE OR REPLACE TRIGGER TRG_VALIDAR_CUPO " +
            "BEFORE INSERT ON INSCRIPCION_GRUPO " +
            "FOR EACH ROW " +
            "DECLARE " +
            "    v_cupo_maximo NUMBER; " +
            "    v_inscritos   NUMBER; " +
            "BEGIN " +
            "    SELECT CUPO_MAXIMO INTO v_cupo_maximo " +
            "    FROM GRUPO_PRACTICA WHERE ID_GRUPO = :NEW.ID_GRUPO; " +
            "    SELECT COUNT(*) INTO v_inscritos " +
            "    FROM INSCRIPCION_GRUPO " +
            "    WHERE ID_GRUPO = :NEW.ID_GRUPO " +
            "      AND ESTADO NOT IN ('RETIRADO', 'REPROBADO'); " +
            "    IF v_inscritos >= v_cupo_maximo THEN " +
            "        RAISE_APPLICATION_ERROR(-20001, 'Cupo maximo del grupo alcanzado.'); " +
            "    END IF; " +
            "END TRG_VALIDAR_CUPO;"
        );

        st.execute(
            "CREATE OR REPLACE TRIGGER TRG_COMPLETAR_INSCRIPCION " +
            "BEFORE UPDATE OF HORAS_CUMPLIDAS ON INSCRIPCION_GRUPO " +
            "FOR EACH ROW " +
            "DECLARE " +
            "    v_horas_reglamentarias NUMBER; " +
            "BEGIN " +
            "    SELECT p.HORAS_REGLAMENTARIAS INTO v_horas_reglamentarias " +
            "    FROM GRUPO_PRACTICA gp " +
            "    JOIN PRACTICA p ON gp.ID_PRACTICA = p.ID_PRACTICA " +
            "    WHERE gp.ID_GRUPO = :NEW.ID_GRUPO; " +
            "    IF :NEW.HORAS_CUMPLIDAS >= v_horas_reglamentarias " +
            "       AND :OLD.ESTADO = 'ACTIVO' THEN " +
            "        :NEW.ESTADO := 'COMPLETADO'; " +
            "    END IF; " +
            "END TRG_COMPLETAR_INSCRIPCION;"
        );
    }

    public void borrarTablas() throws SQLException {
        Statement st = conn.createStatement();

        // Disparadores de negocio
        try { st.executeUpdate("DROP TRIGGER TRG_COMPLETAR_INSCRIPCION"); } catch (Exception e) {}
        try { st.executeUpdate("DROP TRIGGER TRG_VALIDAR_CUPO"); } catch (Exception e) {}
        try { st.executeUpdate("DROP TRIGGER TRG_AUDITORIA_PASSWORD"); } catch (Exception e) {}

        // Disparadores de autoincremento
        try { st.executeUpdate("DROP TRIGGER TRG_AI_AUDITORIA"); } catch (Exception e) {}
        try { st.executeUpdate("DROP TRIGGER TRG_AI_INSCRIPCION"); } catch (Exception e) {}
        try { st.executeUpdate("DROP TRIGGER TRG_AI_GRUPO"); } catch (Exception e) {}
        try { st.executeUpdate("DROP TRIGGER TRG_AI_PRACTICA"); } catch (Exception e) {}
        try { st.executeUpdate("DROP TRIGGER TRG_AI_INSTITUCION"); } catch (Exception e) {}
        try { st.executeUpdate("DROP TRIGGER TRG_AI_USUARIO"); } catch (Exception e) {}

        // Procedimientos y funciones
        try { st.executeUpdate("DROP PROCEDURE SP_INSCRIBIR_ESTUDIANTE"); } catch (Exception e) {}
        try { st.executeUpdate("DROP PROCEDURE SP_ACTUALIZAR_HORAS"); } catch (Exception e) {}
        try { st.executeUpdate("DROP FUNCTION FN_PORCENTAJE_HORAS"); } catch (Exception e) {}
        try { st.executeUpdate("DROP FUNCTION FN_INSCRITOS_ACTIVOS"); } catch (Exception e) {}

        // Usuarios Oracle por rol
        try { st.executeUpdate("DROP USER USR_ADMIN CASCADE"); } catch (Exception e) {}
        try { st.executeUpdate("DROP USER USR_DIRECTOR CASCADE"); } catch (Exception e) {}
        try { st.executeUpdate("DROP USER USR_COORDINADOR CASCADE"); } catch (Exception e) {}
        try { st.executeUpdate("DROP USER USR_DOCENTE CASCADE"); } catch (Exception e) {}
        try { st.executeUpdate("DROP USER USR_ESTUDIANTE CASCADE"); } catch (Exception e) {}
        try { st.executeUpdate("DROP USER USR_INSTITUCION CASCADE"); } catch (Exception e) {}

        // Roles
        try { st.executeUpdate("DROP ROLE ROL_ADMIN"); } catch (Exception e) {}
        try { st.executeUpdate("DROP ROLE ROL_DIRECTOR"); } catch (Exception e) {}
        try { st.executeUpdate("DROP ROLE ROL_COORDINADOR"); } catch (Exception e) {}
        try { st.executeUpdate("DROP ROLE ROL_DOCENTE"); } catch (Exception e) {}
        try { st.executeUpdate("DROP ROLE ROL_ESTUDIANTE"); } catch (Exception e) {}
        try { st.executeUpdate("DROP ROLE ROL_INSTITUCION"); } catch (Exception e) {}

        // Tablas
        try { st.executeUpdate("DROP TABLE AUDITORIA_PASSWORD CASCADE CONSTRAINTS"); } catch (Exception e) {}
        try { st.executeUpdate("DROP TABLE INSCRIPCION_GRUPO CASCADE CONSTRAINTS"); } catch (Exception e) {}
        try { st.executeUpdate("DROP TABLE GRUPO_PRACTICA CASCADE CONSTRAINTS"); } catch (Exception e) {}
        try { st.executeUpdate("DROP TABLE PRACTICA CASCADE CONSTRAINTS"); } catch (Exception e) {}
        try { st.executeUpdate("DROP TABLE INSTITUCION_RECEPTORA CASCADE CONSTRAINTS"); } catch (Exception e) {}
        try { st.executeUpdate("DROP TABLE USUARIO CASCADE CONSTRAINTS"); } catch (Exception e) {}

        // Secuencias
        try { st.executeUpdate("DROP SEQUENCE SEQ_USUARIO"); } catch (Exception e) {}
        try { st.executeUpdate("DROP SEQUENCE SEQ_INSTITUCION"); } catch (Exception e) {}
        try { st.executeUpdate("DROP SEQUENCE SEQ_PRACTICA"); } catch (Exception e) {}
        try { st.executeUpdate("DROP SEQUENCE SEQ_GRUPO"); } catch (Exception e) {}
        try { st.executeUpdate("DROP SEQUENCE SEQ_INSCRIPCION"); } catch (Exception e) {}
        try { st.executeUpdate("DROP SEQUENCE SEQ_AUDITORIA"); } catch (Exception e) {}

        st.close();
    }
}
