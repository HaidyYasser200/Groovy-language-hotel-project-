import groovy.sql.*
@GrabConfig(systemClassLoader=true)
@Grab('com.oracle:ojdbc6')
class Login implements Database {
    def checkName(name){
        if (name=='')
            return 'Please Enter Name'
        if (!(name =~ '^[A-Za-z\s]{3,}$'))
            return 'Name is not valid\nName must be contain only char\nLength of Name must be greater than 3'
        
        return 0
    }
    def checkTelephone(telephone){
        if (telephone=='')
            return 'Please Enter Phone'
        if (!(telephone =~ '^([+]([0-9])+)?([0-9])+$'))
            return 'Phone is not valid'
        return 0
    }
    def checkEmployment(employment){
        if (employment=='')
            return 'Please Select Employment'
        return 0
    }
    def checkEmail(email){
        if (email=='')
            return 'Please Enter Email'
        if (!(email =~ '^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$'))
            return 'Email is not valid'

        return 0
    }
    def checkPassword(password){
        if (password=='')
            return 'Please Enter Password'
        if (!(password =~ '^[_A-Za-z0-9[@#$%^&+=.]]{8,20}$'))
            return 'Password is not valid\nPassword must be contaion only char,number and special char @,#,$,%,^,&,+,=,.\nLength Of Password must be greater than 8 and less than 20 '
        return 0
    }
    
    String login ( String Email , String Password ){
        try{
            def sql=startConn()
            String msg= "Incorrect email or password" ;   
            sql.eachRow( "select * from EMPLOYEE where EMAIL='"+ Email +"'" ){ row ->
            sql.eachRow( """select * from "RECEPTIONIST" where ID_EMPLOYEE='""" +row.ID+ """' """ ){ row1->
                    if (row.password== Password.md5() ){
                        msg= "receptionist"
                    } 
                }
                sql.eachRow( """select * from "ADMIN" where ID_EMPLOYEE='""" +row.ID+ """' """ ){row2-> 
                    if (row.password== Password.md5() ){ 
                        msg= "admin"
                    }
                }
            }
            sql.close()
            return msg
        }catch(Exception e){
            return "Field Login"
        }
    }

    String addEmploymee(String Name,String Email,String Employment,String Phone,String Password){
        try{
            def sql=startConn()
            String msg1="This Data isn't valid"
            Password=Password.md5()
            def params = [Name, Email,Employment,Phone,Password]
            sql.execute ('insert into EMPLOYEE (NAME, EMAIL, EMPLOYMENT,TELEPHONE,PASSWORD) values (?,?, ?,?,?)', params)
            sql.eachRow( "select * from EMPLOYEE where EMAIL='" +Email+ "' AND PASSWORD='"+Password+"'"){row3->
            switch(Employment) {
                case "Admin":
                    sql.execute('insert into ADMIN (ID_EMPLOYEE) values(?)',row3.ID)
                    msg1="Admin added"
                break
                case "Receptionist":
                    sql.execute('insert into RECEPTIONIST (ID_EMPLOYEE) values(?)',row3.ID)
                    msg1="Receptionist added"
                break
            }
            }
            sql.close()
            return msg1
        }catch(Exception e){
            return "Email must be Unique\nField Add Employmee"
        }
    }
}
