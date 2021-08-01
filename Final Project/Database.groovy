import groovy.sql.*;

@GrabConfig(systemClassLoader=true)
@Grab('com.oracle:ojdbc6')
trait Database {
    def startConn(){
        def username = "SYSTEM"
        def password = "1234"
        return Sql.newInstance("jdbc:oracle:thin:@localhost:1521:XE", username, password,
        "oracle.jdbc.driver.OracleDriver")
    }
    void createDatebase(){
        createTable('ROOM')
        createTable('CLIENT')
        createTable('EMPLOYEE')
        createTable('RECEPTIONIST')
        createTable('ADMIN')
        createTrigger('EMPLOYEE','EMPLOYEE_ID','EMPLOYEE_ID_SEQ')
        createTrigger('client','CLIENT_ID','CLIENT_ID_SEQ')
        createTrigger('ADMIN','ADMIN_ID','ADMIN_ID_SEQ')
        createTrigger('RECEPTIONIST','RECEPTIONIST_ID','RECEPTIONIST_ID_SEQ')
        for(int i=1;i<=10;i++){
            insertRooms(i)
        }
        insertAdmin('admin','admin@gmail.com','012','admin#1234')
    }
    void insertAdmin(String Name,String Email,String Phone,String Password){
        try{
            def sql=this.startConn()
            def flag=true
            sql.eachRow( """select * from "EMPLOYEE" where EMPLOYMENT='Admin'""" ){row3->
                flag=false
            }
            if (flag){
                Password=Password.md5()
                sql.execute ("insert into EMPLOYEE (NAME, EMAIL, EMPLOYMENT,TELEPHONE,PASSWORD) values "+ "('${Name}','${Email}','Admin','${Phone}','${Password}')")
                sql.eachRow("select * from EMPLOYEE where EMAIL='" +Email+ "' AND PASSWORD='"+Password+"'" ){row3->
                    sql.execute("insert into ADMIN (ID_EMPLOYEE) values("+row3.ID+")")
                }
            }
            sql.close()
            // println "succ add Admin is name: $Name"
        }catch(Exception e){
            // println "Current is Error "
        }
    }
    void insertRooms(num){
        try{
            def sql=this.startConn()
            def flag=true
            def params = [num]
            sql.eachRow( """select * from "ROOM" where NUM=${num} """ ){row3->
                flag=false
            }
            if (flag){
                sql.execute ('insert into ROOM (NUM) values (?)', params)
            }
            sql.close()
            // println "succ add room num $num"
        }catch(Exception e){
            // println "Current is Error"
        }
    }
    void createSequence(sequenceName){
        try{
            def sql=this.startConn()
            def sqlStr=''
            def sqlstr="SELECT count(*) as c FROM USER_SEQUENCES where SEQUENCE_NAME='"+"${sequenceName}'"
            sql.eachRow(sqlstr){row->
                if(row.c==0){
                    sqlStr="CREATE SEQUENCE "+"${sequenceName}"
                    sql.execute(sqlStr)        
                }
            }           
            sql.close()
        //    println "succ create ${sequenceName}"
        }catch(Exception e){
            // println "Current is Error"
        }
    }
    void createTrigger(tableName,triggerName,sequenceName){
        try{
            def sqlStr=''
            createSequence(sequenceName)
            def sql=this.startConn()
            def sqlstr="SELECT count(*) as c FROM USER_TRIGGERS where TRIGGER_NAME='"+"${triggerName}'"
            sql.eachRow(sqlstr){row->
                if(row.c==0){
                    sqlStr="CREATE TRIGGER "+"${triggerName}"+
                    " BEFORE INSERT ON "+"${tableName}"+
                    " FOR EACH ROW BEGIN SELECT "+"${sequenceName}"+
                    ".nextval INTO :new.id FROM dual; END;"
                    sql.execute(sqlStr) 
                }           
            }
            sql.close()
        //    println "succ create ${triggerName}"
        }catch(Exception e){
            // println "Current is Error"
        }
    }
    void createTable(tableName){
        try{
            def sql=this.startConn()
            def sqlStr=''
            def sqlstr ="SELECT count(*) as c FROM USER_TABLES where TABLE_NAME ='"+"${tableName}' "; 
            sql.eachRow(sqlstr){row->
                if(row.c==0){
                    switch(tableName){
                        case 'ADMIN':
                            sqlStr="""CREATE TABLE  "ADMIN" 
                            (	"ID" NUMBER NOT NULL ENABLE, 
                                "ID_EMPLOYEE" NUMBER NOT NULL ENABLE, 
                                CONSTRAINT "ADMIN_PK" PRIMARY KEY ("ID") ENABLE, 
                                CONSTRAINT "ADMIN_UK1" UNIQUE ("ID_EMPLOYEE") ENABLE, 
                                CONSTRAINT "ADMIN_FK" FOREIGN KEY ("ID_EMPLOYEE")
                                REFERENCES  "EMPLOYEE" ("ID") ON DELETE CASCADE ENABLE
                            )
                            """
                            break
                        case 'EMPLOYEE':
                            sqlStr="""CREATE TABLE  "EMPLOYEE" 
                            (	"ID" NUMBER NOT NULL ENABLE, 
                                "NAME" VARCHAR2(4000) NOT NULL ENABLE, 
                                "EMAIL" VARCHAR2(4000) NOT NULL ENABLE, 
                                "EMPLOYMENT" VARCHAR2(4000) NOT NULL ENABLE, 
                                "TELEPHONE" VARCHAR2(4000) NOT NULL ENABLE, 
                                "PASSWORD" VARCHAR2(4000) NOT NULL ENABLE, 
                                CONSTRAINT "EMPLOYEE_PK" PRIMARY KEY ("ID") ENABLE, 
                                CONSTRAINT "EMPLOYEE_CON" UNIQUE ("EMAIL") ENABLE
                            )
                            """
                            break
                        case 'CLIENT':
                            sqlStr="""CREATE TABLE  "CLIENT" 
                            (	"ID" NUMBER NOT NULL ENABLE, 
                                "NAME" VARCHAR2(4000) NOT NULL ENABLE, 
                                "NO_OF_ROOM" NUMBER NOT NULL ENABLE, 
                                "TELEPHONE" VARCHAR2(4000) NOT NULL ENABLE, 
                                "START_DATE" DATE NOT NULL ENABLE, 
                                "END_DATE" DATE NOT NULL ENABLE, 
                                CONSTRAINT "CLIENT_PK" PRIMARY KEY ("ID") ENABLE, 
                                CONSTRAINT "CLIENT_CON" FOREIGN KEY ("NO_OF_ROOM")
                                REFERENCES  "ROOM" ("NUM") ON DELETE CASCADE ENABLE
                            )
                            """
                            break
                        case 'RECEPTIONIST':
                            sqlStr="""CREATE TABLE  "RECEPTIONIST" 
                            (	"ID" NUMBER NOT NULL ENABLE, 
                                "ID_EMPLOYEE" NUMBER NOT NULL ENABLE, 
                                CONSTRAINT "RECEPTIONIST_PK" PRIMARY KEY ("ID") ENABLE, 
                                CONSTRAINT "RECEPTIONIST_UK1" UNIQUE ("ID_EMPLOYEE") ENABLE, 
                                CONSTRAINT "RECEPTIONIST_FK" FOREIGN KEY ("ID_EMPLOYEE")
                                REFERENCES  "EMPLOYEE" ("ID") ON DELETE CASCADE ENABLE
                            )
                            """
                            break
                        case 'ROOM':
                            sqlStr="""CREATE TABLE  "ROOM" 
                            (	"NUM" NUMBER NOT NULL ENABLE, 
                                "AVALIABLE" NUMBER DEFAULT 1 NOT NULL ENABLE, 
                                CONSTRAINT "ROOM_PK" PRIMARY KEY ("NUM") ENABLE
                            )
                            """
                            break                    
                    }
                    sql.execute(sqlStr) 
                }           
            }
            sql.close()
        //    println "succ create ${tableName}"
        }catch(Exception e){
            // println "Current is Error"
        }
    }
    
}