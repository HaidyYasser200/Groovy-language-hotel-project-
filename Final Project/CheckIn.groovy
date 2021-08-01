trait CheckIn {

    def checkName(name){
        if (name=='')
            return 'Please Enter Name'
        return 0
    }
    def checkTelephone(telephone){
        if (telephone=='')
            return 'Please Enter Telephone'
        return 0
    }
    def checkStartDate(client,startDate){
        if (startDate=='')
            return 'Please Enter Start Date'
        else
            try{
                Date date = Date.parse('dd/MM/yyyy',startDate);
                client.setStartDate(date)
            }catch(Exception e){
                return "Please Enter date :dd/MM/yyyy "
            }
            
        return 0
    }
    def checkEndDate(client,endDate){
        if (endDate=='')
            return 'Please Enter End Date'
        else
            try{
                Date date = Date.parse('dd/MM/yyyy',endDate);
                client.setEndDate(date)
            }catch(Exception e){
                return "Please Enter date :dd/MM/yyyy "
            }
        
        return 0
    }

    public def avaliableRoom(startDate,endDate){
        try{
            def sql=startConn()
            def numRoomsAv=[];
            sql.eachRow("""select * from ROOM """){ row ->
                if(row.AVALIABLE){
                    numRoomsAv.add(row.NUM)
                }else{
                    sql.eachRow("""select START_DATE,END_DATE from CLIENT where NO_OF_ROOM=${row.NUM}"""){ rowC ->
                        if(rowC.END_DATE.before(startDate)||rowC.START_DATE.after(endDate)){
                            numRoomsAv.add(row.NUM)
                        }
                    }
                }
            }
            sql.close()
            return numRoomsAv
        }catch(Exception e){
            return "Current is Error"
        }
    }
    public def checkAvaliableRoom(num,startDate,endDate){
        try{
            num= Integer.parseInt(num)
            if(!startDate.before(endDate) || startDate.before(new Date()) )
                return 'Date is Error'
            def numRoomsAv=avaliableRoom(startDate,endDate)
            for(def i in numRoomsAv) { 
                if(i==num)
                    return 1
            } 
        }catch(Exception e){
            return 'Invalid Number of Room '
        }
        return "Room is Not Avaliable"
    }
    public def addClient(Client client,numRoom){
        def checkStr=checkAvaliableRoom(numRoom,client.getStartDate(),client.getEndDate())
        if(checkStr==1){
            try {
                def sql=startConn()
                def sqlStr="insert into client (NAME,TELEPHONE,START_DATE,END_DATE,NO_OF_ROOM) values('"+"${client.getName()}"+"','"+"${client.getTelephone()}"+
                "',to_date('"+"${client.getStartDate().format('dd/MM/yyyy')}"+"','dd/MM/yyyy'),to_date('"+"${client.getEndDate().format('dd/MM/yyyy')}"+"','dd/MM/yyyy'),"+"${numRoom}"+")"
                def sqlStr2="""UPDATE ROOM SET AVALIABLE = 0 WHERE NUM = ${numRoom}"""
                sql.execute(sqlStr)
                sql.execute(sqlStr2)
                sql.close()
                return "Successfully Add Client"
            }catch(Exception ex) {
                return "Field Add Client"
            }
        }
        return checkStr
    }
}