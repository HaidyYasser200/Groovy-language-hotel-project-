class collectDataClient implements Database {

  def name =[]
  def num_room =[]
  def start_date =[]
  def end_date =[]
  def telephone =[]
  def data = []
  def getData(){return this.data}
  def getDataClient(){
    try{
      def sql = startConn()
      sql.eachRow("""select * from CLIENT """){ row ->
        this.name.push(row[1])
        this.num_room.push(row[2])
        this.telephone.push(row[3])
        this.start_date.push(row[4])
        this.end_date.push(row[5])
      }
      sql.close()  
      def s,e
      for(int i = 0;i<name.size;i++) {
        String f=start_date[i]
        String v=end_date[i]
        s=Date.parse('yyyy-mm-dd', f).format("dd-mm-yyyy")
        e=Date.parse('yyyy-mm-dd', v).format('dd-mm-yyyy')
        def m=[name[i],num_room[i],telephone[i],s,e]
        this.data.push(m)
      }
    }catch(Exception e){
      return "Current is Error"
    }
  }
}