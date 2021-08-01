trait CheckOut{
    public def checkClientExist(name){
        try{
            def flag=false
            def sql=startConn()
            sql.eachRow("select * from CLIENT where NAME='"+name+"'"){ row ->
                flag= true
            }
            sql.close()
            if (flag)
                return 0
            return "Client ${name} is Not Exist"
        }catch(Exception e){
            return "Current is Error"
        }
    }
    public def checkRoomExist(num,name){
        if (num=='')
            return 'Please Enter Number Of Room'
        def checkE=checkClientExist(name)
        if (checkE==0){
            try{
                def sql=startConn()
                sql.eachRow("select * from CLIENT where NO_OF_ROOM="+num+" AND NAME='"+name+"'"){ row ->
                    checkE=0
                }
                sql.close()
                if (checkE==0)
                    return checkE
                return "the Number of Room is not Check In By ${name}"
            }catch(Exception e){
                return "Current is Error"
            }
        }
        return checkE
    }
    public def priceCal(name,num){
        try{
            def avaliable=1
            def sql=startConn()
            def price=0
            sql.eachRow("select ID,START_DATE,END_DATE from CLIENT WHERE NO_OF_ROOM=${num}"+" AND NAME='"+"${name}"+"'"){ rowC ->
                use(groovy.time.TimeCategory){
                def data=rowC.END_DATE-rowC.START_DATE
                price=data.days*500
                // println data.days
                }
                def sqlStr="""DELETE FROM CLIENT WHERE ID= ${rowC.ID}"""
                sql.execute(sqlStr)
                sqlStr="""select NO_OF_ROOM from CLIENT WHERE NO_OF_ROOM=${num}"""
                sql.eachRow(sqlStr){rowAvaliable ->
                    avaliable=0
                }
                if (avaliable==1) 
                    sqlStr="""UPDATE ROOM SET AVALIABLE = 1 WHERE NUM = ${num}"""
                    sql.execute(sqlStr)
                // println("Client checked out successfully")
                
            }
            sql.close()
            return price
        }catch(Exception e){
            return "Current is Error"
        }
    }
}