import groovy.swing.SwingBuilder
import javax.swing.*
import java.awt.*
import static javax.swing.JFrame.EXIT_ON_CLOSE
import static java.awt.GridBagConstraints.EAST
import static java.awt.GridBagConstraints.REMAINDER
import static javax.swing.SwingConstants.HORIZONTAL
import javax.swing.JOptionPane;

@GrabConfig(systemClassLoader=true)
@Grab('com.oracle:ojdbc6')

def homeFrame(String x,swing,frameLogin){
        GroovyShell shell2 = new GroovyShell()
        Class addClass=shell2.getClassLoader().parseClass(new File('addEmployee.groovy'))
        GroovyObject tools2 = (GroovyObject) addClass.newInstance()
        def toolsCheckIn = shell2.parse(new File('checkinView.groovy'))
        def toolsCheckOut = shell2.parse(new File('checkoutView.groovy'))
        def toolsShowClient = shell2.parse(new File('showClient.groovy'))
        def toolsShowRoom = shell2.parse(new File('showRoom.groovy'))
        def flag=false
        def labelfont =new Font("Lora", Font.PLAIN, 17)
        def backgroundColor =new Color(16, 37, 71);
        def defaultInsets = [10,10,10,10]
        def size=[700, 500]
        switch (x) {
           case "receptionist":  
                flag=false    
                break
           case "admin": 
                flag=true
                break
           case "Incorrect email or password":
                return JOptionPane.showMessageDialog(null, 'Incorrect email or password', "Warning", JOptionPane.ERROR_MESSAGE);
                break
           default :
                return JOptionPane.showMessageDialog(null, 'Field Login', "Warning", JOptionPane.ERROR_MESSAGE);
                break
        }
        return swing.frame(id:'Homepage',title: 'Hotel',location : [500, 200], size:size, 
        defaultCloseOperation: EXIT_ON_CLOSE,show:true) {
                menuBar (){
                        menuItem()
                        menuItem()
                        menuItem()
                        menuItem() 
                        menuItem('Log Out',actionPerformed:{
                                frameLogin.visible=true
                                Homepage.dispose()
                        })
                }
                panel(background:backgroundColor,border: titledBorder(font:new Font("Lora", Font.BOLD, 15),color:Color.WHITE,title: 'Homepage', justification: 'leading', position: 'Top',)){

                        gridBagLayout()
                        button(
                                font:labelfont,
                                text:'   Check in      ',
                                constraints:gbc(gridx:0,gridy:0,insets:defaultInsets,gridwidth:3, anchor: WEST),
                                actionPerformed:{
                                        toolsCheckIn.guiFrameCheckIn(swing,frameLogin,Homepage)
                                        Homepage.dispose()
                                }
                        )
                        button(
                                font:labelfont,
                                text:'   Check out    ',
                                constraints:gbc(gridx:0,gridy:2,insets:defaultInsets,gridwidth:3, anchor: WEST),
                                actionPerformed:{
                                        toolsCheckOut.guiCheckOut(swing,frameLogin,Homepage)
                                        Homepage.dispose()
                                }
                        )
                        button(
                                font:labelfont,
                                text:'  View Rooms ',
                                constraints:gbc(gridx:0,gridy:4,insets:defaultInsets,gridwidth:3, anchor: WEST),
                                actionPerformed:{
                                        toolsShowRoom.showRoom(swing,frameLogin,Homepage)
                                        Homepage.dispose()
                                }
                        )
                        button(
                                font:labelfont,
                                text:'  View Clients ',
                                constraints:gbc(gridx:0,gridy:6,insets:defaultInsets,gridwidth:3, anchor: WEST),
                                actionPerformed:{
                                        toolsShowClient.showClient(swing,frameLogin,Homepage)
                                        Homepage.dispose()
                                }
                        )
                        button(
                                font:labelfont,
                                id:'add',
                                visible:flag,
                                text:'Add Employee',
                                constraints:gbc(gridx:0,gridy:8,insets:defaultInsets,gridwidth:3, anchor: WEST),
                                actionPerformed:{
                                        tools2.addEmployeeFrame(swing,frameLogin,Homepage)
                                        Homepage.dispose()
                                }
                        )
                } 
        }  
}