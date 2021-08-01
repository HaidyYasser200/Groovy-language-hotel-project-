import groovy.sql.*;
class Client implements Database,CheckIn,CheckOut{
    
    private String name
    private String telephone
    private Date startDate
    private Date endDate
    void setName(name){this.name=name}
    String getName(){return this.name}
    void setTelephone(telephone){this.telephone=telephone}
    String getTelephone(){return this.telephone}
    void setStartDate(startDate){this.startDate=startDate}
    Date getStartDate(){return this.startDate}
    void setEndDate(endDate){this.endDate=endDate}
    Date getEndDate(){return this.endDate}
    public Client(){}
    public Client(name,telephone,startDate,endDate){
      this.name=name
      this.telephone=telephone
      this.startDate=startDate
      this.endDate=endDate
    }
    
}