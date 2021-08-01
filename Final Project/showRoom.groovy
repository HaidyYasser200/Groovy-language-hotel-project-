package groovy.swing
import groovy.swing.*
import javax.swing.*
import groovy.sql.*
import javax.swing.BoxLayout as BXL
import java.awt.Color
import java.awt.Font
@GrabConfig(systemClassLoader=true)
@Grab('com.oracle:ojdbc6')
def showRoom(swing,frameLogin,frameHome) {
    Font font1 = new Font("Serif", Font.BOLD, 20) 
    Font font = new Font("Lora", Font.BOLD, 15) 
    GroovyShell shell = new GroovyShell()
    Class collectDataRoom=shell.getClassLoader().parseClass(new File('collectDataRoom.groovy'))
    GroovyObject dataRoom = (GroovyObject) collectDataRoom.newInstance();
    dataRoom.getDataRoom()
    return swing.frame(id: 'roomView', title: 'Hotel',location : [500, 200], size: [700, 500], show: true,defaultCloseOperation : WindowConstants.EXIT_ON_CLOSE) {
      menuBar (){
        menuItem('Back',actionPerformed:{
          frameHome.visible=true
          roomView.dispose()
        })
        menuItem()
        menuItem()
        menuItem()
        menuItem()
        menuItem('Sign Out',actionPerformed:{
          frameLogin.visible=true
          roomView.dispose()
        })
      }
      panel() {
        boxLayout(axis: BXL.Y_AXIS) 
        panel(background:new Color(16, 37, 71)) {
             label(text : 'Rooms\' Data', horizontalAlignment : JLabel.CENTER,foreground: java.awt.Color.WHITE).setFont(font1)
         } 
        scrollPane {
           JTable t = new JTable(dataRoom.getData() as Object[][], ['Room number', 'Is Available'] as Object[])
           t.setEnabled(false)
           t.setRowHeight(25);
           table(t) {
               columnModel {
                column('num', modelIndex: 0, headerValue: 'Room number', width:280) {
                   cellRenderer {
                      label(horizontalAlignment : JLabel.CENTER).setFont(font)
                      onRender { 
                        children[0].text = value
                      }
                  }
                }
                column('available', modelIndex: 1, headerValue: 'Is Available',  width: [100, 70]) {
                   cellRenderer {
                    label(foreground: java.awt.Color.BLUE,horizontalAlignment : JLabel.CENTER).setFont(font)
                    onRender { children[0].text = value }
                   }
                }
             }
          }
       }.getViewport().setBackground(new Color(16, 37, 71))
    }
        boxLayout(axis: BXL.Y_AXIS)
        panel(id:'secondPanel', background:java.awt.Color.WHITE){                       
            button('Quit', actionPerformed:{
            roomView.dispose()
          })
        }   
 } 
}