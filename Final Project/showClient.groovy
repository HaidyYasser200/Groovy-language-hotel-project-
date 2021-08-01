package groovy.swing
import groovy.swing.*
import javax.swing.*
import groovy.sql.*
import javax.swing.BoxLayout as BXL
import java.awt.Font
import java.awt.Color
@GrabConfig(systemClassLoader=true)
@Grab('com.oracle:ojdbc6')
def showClient(swing,frameLogin,frameHome) {
  Font font1 = new Font("Serif", Font.BOLD, 20) 
  Font font = new Font("Lora", Font.BOLD, 15)
  GroovyShell shell = new GroovyShell() 
  Class collectDataClient=shell.getClassLoader().parseClass(new File('collectDataClient.groovy'))
  GroovyObject dataClient = (GroovyObject) collectDataClient.newInstance();
  dataClient.getDataClient()
  return swing.frame(id: 'clientView', title: 'Hotel',location : [500, 200], size: [700, 500], show: true,defaultCloseOperation : WindowConstants.EXIT_ON_CLOSE) {
      menuBar (){
        menuItem('Back',actionPerformed:{
          frameHome.visible=true
          clientView.dispose()
        })
        menuItem()
        menuItem()
        menuItem()
        menuItem()
        menuItem('Sign Out',actionPerformed:{
          frameLogin.visible=true
          clientView.dispose()
        })
      }
      panel() {
        boxLayout(axis: BXL.Y_AXIS) 
        panel(background:new Color(16, 37, 71)) {
            label(text : 'Clients\' Data', horizontalAlignment : JLabel.CENTER,foreground: java.awt.Color.WHITE).setFont(font1)
        } 
        scrollPane() {
          JTable t =new JTable(dataClient.getData() as Object[][], ['name', 'numRoom','tele','s_date','e_date'] as Object[])
          t.setEnabled(false)
          t.setRowHeight(25);
          table(t) {
          columnModel {
            column('name', modelIndex: 0, headerValue: 'Name') {
                cellRenderer {
                label(horizontalAlignment : JLabel.CENTER).setFont(font)
                onRender { children[0].text = value }
                }
              }
            column('numRoom', modelIndex: 1, headerValue: 'Room number') {
                cellRenderer {
                label(foreground: java.awt.Color.BLUE,horizontalAlignment : JLabel.CENTER).setFont(font)
                onRender { children[0].text = value }
                }
              }
            column('tele', modelIndex: 2, headerValue: 'Telephone') {
                cellRenderer {
                label(horizontalAlignment : JLabel.CENTER).setFont(font)
                onRender { children[0].text = value }
                }
              }
            column('s_date', modelIndex: 3, headerValue: 'Start Date') {
                cellRenderer {
                  label(horizontalAlignment : JLabel.CENTER).setFont(font)
                  onRender { children[0].text = value }
                }
            }
            column('e_date', modelIndex: 4, headerValue: 'End Date') {
                cellRenderer {
                  label(horizontalAlignment : JLabel.CENTER).setFont(font)
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
            clientView.dispose()
          })
  }   
  }
}