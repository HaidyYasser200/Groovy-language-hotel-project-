class collectDataRoom implements Database {
  def num =[]
  def available =[]
  def data = []
  def getData(){return this.data}
  def getDataRoom(){
    try{
      def sql = startConn()
      sql.eachRow("""select * from ROOM """){ row ->
        this.num.push(row[0])
        this.available.push(row[1])
      }
      sql.close()  
      def m
      for(int i = 0;i<num.size;i++) {
          if (available[i]==1){
              m = [num[i],"True"]
          }
          else{
              m = [num[i],"False"]
          }
          this.data.push(m)
      }
    }catch(Exception e){
      return "Current is Error"
    }
  }
}